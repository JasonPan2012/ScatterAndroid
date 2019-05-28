package com.mariabeyrak.scatter.socket;

import com.google.gson.Gson;
import com.mariabeyrak.scatter.ScatterClient;
import com.mariabeyrak.scatter.models.requests.msgtransaction.MsgTransactionRequestParams;
import com.mariabeyrak.scatter.models.requests.serializedtransaction.SerializedTransactionRequestParams;
import com.mariabeyrak.scatter.models.requests.transaction.request.TransactionRequestParams;
import com.mariabeyrak.scatter.models.response.ResponseCodeInfo;
import com.mariabeyrak.scatter.socket.models.requests.authenticate.AuthenticateResponse;
import com.mariabeyrak.scatter.socket.models.requests.getaccount.Account;
import com.mariabeyrak.scatter.socket.models.requests.getaccount.GetAccountResponse;
import com.mariabeyrak.scatter.socket.models.requests.msgtransaction.MsgTransactionResponse;
import com.mariabeyrak.scatter.socket.models.requests.transaction.ReturnedFields;
import com.mariabeyrak.scatter.socket.models.requests.transaction.TransactionResponse;
import com.mariabeyrak.scatter.socket.models.response.ApiResponseData;
import com.mariabeyrak.scatter.socket.models.response.BooleanResponse;
import com.mariabeyrak.scatter.socket.models.response.CommandsResponse;
import com.mariabeyrak.scatter.socket.models.response.ErrorApiResponseData;
import com.mariabeyrak.scatter.socket.models.response.ErrorResultApiResponseData;
import com.mariabeyrak.scatter.socket.models.response.StringResponse;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mariabeyrak.scatter.socket.SocketsConstants.MESSAGE_START;
import static com.mariabeyrak.scatter.socket.models.Type.AUTHENTICATE;
import static com.mariabeyrak.scatter.socket.models.Type.FORGET_IDENTITY;
import static com.mariabeyrak.scatter.socket.models.Type.GET_OR_REQUEST_IDENTITY;
import static com.mariabeyrak.scatter.socket.models.Type.GET_PUBLIC_KEY;
import static com.mariabeyrak.scatter.socket.models.Type.GET_VERSION;
import static com.mariabeyrak.scatter.socket.models.Type.IDENTITY_FROM_PERMISSIONS;
import static com.mariabeyrak.scatter.socket.models.Type.LINK_ACCOUNT;
import static com.mariabeyrak.scatter.socket.models.Type.REQUEST_ADD_NETWORK;
import static com.mariabeyrak.scatter.socket.models.Type.REQUEST_ARBITRARY_SIGNATURE;
import static com.mariabeyrak.scatter.socket.models.Type.REQUEST_SIGNATURE;

public class ScatterSocketService {
    final static private Gson gson = new Gson();

    private ScatterSocketService() {
    }

    static void handlePairResponse(WebSocket conn) {
        sendResponse(conn, "[\"" + CommandsResponse.PAIRED + "\",true]");
    }

    static void handleRekeyResponse(WebSocket conn) {
        sendResponse(conn, "[\"" + CommandsResponse.REKEY + "\"]");
    }

    static void handleConnectedResponse(WebSocket conn) {
        sendResponse(conn, "[\"" + CommandsResponse.CONNECTED + "\"]");
    }

    static void handleApiResponse(WebSocket conn, JSONArray params, ScatterClient scatterClient) throws JSONException {
        JSONObject details = params.getJSONObject(1);
        JSONObject data = details.getJSONObject("data");
        String type = data.getString("type");
        String payload = data.getString("payload");
        String id = data.getString("id");

        switch (type) {
            case GET_VERSION: {
                getVersion(conn, id);
                break;
            }
            case IDENTITY_FROM_PERMISSIONS:
            case GET_OR_REQUEST_IDENTITY: {
                getIdentity(conn, id, scatterClient);
                break;
            }
            case REQUEST_SIGNATURE: {
                handleRequestSignature(conn, id, payload, scatterClient);
                break;
            }
            case AUTHENTICATE: {
                authenticate(conn, id);
                break;
            }
            case REQUEST_ADD_NETWORK:
            case LINK_ACCOUNT:
            case FORGET_IDENTITY: {
                sendBooleanTrueResponse(conn, id);
                break;
            }
            case REQUEST_ARBITRARY_SIGNATURE: {
                requestMsgSignature(conn, id, payload, scatterClient);
                break;
            }
            case GET_PUBLIC_KEY: {
                getPublicKey(conn, id, scatterClient);
                break;
            }
            default:
                break;
        }
    }

    private static void handleRequestSignature(final WebSocket conn, final String id,
                                               String payload, ScatterClient scatterClient) {
        TransactionRequestParams transactionRequestParams = gson.fromJson(payload, TransactionRequestParams.class);
        if (transactionRequestParams.getBuf() != null) {
            requestSignature(conn, id, transactionRequestParams, scatterClient);
        } else {
            SerializedTransactionRequestParams serializedTransactionRequestParams =
                    gson.fromJson(payload, SerializedTransactionRequestParams.class);
            requestSerializedTransactionSignature(conn, id, serializedTransactionRequestParams, scatterClient);
        }
    }

    private static void getVersion(final WebSocket conn, final String id) {
        sendResponse(conn,
                gson.toJson(
                        new ArrayList(Arrays.asList(CommandsResponse.API, new ApiResponseData(id,
                                new StringResponse("10.1.0")
                        )))
                )
        );
    }

    private static void authenticate(final WebSocket conn, final String id) {
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            randomString.append(getRandom());
        }

        sendResponse(conn,
                gson.toJson(
                        new ArrayList(Arrays.asList(CommandsResponse.API, new ApiResponseData(id,
                                new AuthenticateResponse(randomString.toString())
                        )))
                )
        );
    }

    private static void getIdentity(final WebSocket conn, final String id, ScatterClient scatterClient) {
        ScatterClient.AccountReceived accountReceived = new ScatterClient.AccountReceived() {
            @Override
            public void onAccountReceivedSuccessCallback(String accountName, String publicKey) {
                sendResponse(conn,
                        gson.toJson(
                                new ArrayList(Arrays.asList(CommandsResponse.API, new ApiResponseData(id,
                                        new GetAccountResponse(new Account[]{
                                                new Account(accountName, "active", publicKey, "eos",
                                                        "aca376f206b8fc25a6ed44dbdc66547c36c6c33e3a119ffbeaef943642f0e906",
                                                        false)})
                                )))
                        )
                );
            }

            @Override
            public void onAccountReceivedErrorCallback(Error error) {
            }
        };

        scatterClient.getAccount(accountReceived);
    }

    private static void getPublicKey(final WebSocket conn, final String id, ScatterClient scatterClient) {
        ScatterClient.PublicKeyReceived publicKeyReceived = new ScatterClient.PublicKeyReceived() {
            @Override
            public void onPublicKeyReceivedSuccessCallback(String publicKey) {
                sendResponse(conn,
                        gson.toJson(
                                new ArrayList(Arrays.asList(CommandsResponse.API, new ApiResponseData(id,
                                        new StringResponse(publicKey)
                                )))
                        )
                );
            }

            @Override
            public void onPublicKeyReceivedErrorCallback(Error error) {
            }
        };

        scatterClient.getPublicKey(publicKeyReceived);
    }

    private static void requestSignature(final WebSocket conn, final String id,
                                         TransactionRequestParams transactionRequestParams,
                                         ScatterClient scatterClient) {
        ScatterClient.TransactionCompleted transactionCompleted = new ScatterClient.TransactionCompleted() {
            @Override
            public void onTransactionCompletedSuccessCallback(String[] signatures) {
                sendResponse(conn,
                        gson.toJson(
                                new ArrayList(Arrays.asList(CommandsResponse.API, new ApiResponseData(id,
                                        new TransactionResponse(signatures, new ReturnedFields())
                                )))
                        )
                );
            }

            @Override
            public void onTransactionCompletedErrorCallback(ResponseCodeInfo errorInfo, String messageToUser) {
                sendErrorResponse(conn, id, errorInfo.getCode().getCode(), messageToUser);
            }
        };

        scatterClient.completeTransaction(transactionRequestParams, transactionCompleted);
    }

    private static void requestSerializedTransactionSignature(final WebSocket conn, final String id,
                                                              SerializedTransactionRequestParams serializedTransactionRequestParams,
                                                              ScatterClient scatterClient) {
        ScatterClient.SerializedTransactionCompleted transactionCompleted = new ScatterClient.SerializedTransactionCompleted() {
            @Override
            public void onTransactionCompletedSuccessCallback(String[] signatures) {
                sendResponse(conn,
                        gson.toJson(
                                new ArrayList(Arrays.asList(CommandsResponse.API, new ApiResponseData(id,
                                        new TransactionResponse(signatures, new ReturnedFields())
                                )))
                        )
                );
            }

            @Override
            public void onTransactionCompletedErrorCallback(ResponseCodeInfo errorInfo, String messageToUser) {
                sendErrorResponse(conn, id, errorInfo.getCode().getCode(), messageToUser);
            }
        };

        scatterClient.completeSerializedTransaction(serializedTransactionRequestParams, transactionCompleted);
    }

    private static void requestMsgSignature(final WebSocket conn, final String id,
                                            String payload, ScatterClient scatterClient) {
        MsgTransactionRequestParams msgTransactionRequestParams = gson.fromJson(payload, MsgTransactionRequestParams.class);

        ScatterClient.MsgTransactionCompleted transactionCompleted = new ScatterClient.MsgTransactionCompleted() {
            @Override
            public void onMsgTransactionCompletedSuccessCallback(String signature) {
                sendResponse(conn,
                        gson.toJson(
                                new ArrayList(Arrays.asList(CommandsResponse.API, new ApiResponseData(id,
                                        new MsgTransactionResponse(signature)
                                )))
                        )
                );
            }

            @Override
            public void onMsgTransactionCompletedErrorCallback(ResponseCodeInfo errorInfo, String messageToUser) {
                sendErrorResponse(conn, id, errorInfo.getCode().getCode(), messageToUser);
            }
        };

        scatterClient.completeMsgTransaction(msgTransactionRequestParams, transactionCompleted);
    }

    private static void sendErrorResponse(WebSocket conn, String id,
                                          Integer errorCode, String messageToUser) {
        sendResponse(conn, gson.toJson(
                new ArrayList(Arrays.asList(CommandsResponse.API,
                        new ErrorApiResponseData(id, new ErrorResultApiResponseData(errorCode,
                                messageToUser))
                )))
        );
    }

    private static void sendBooleanTrueResponse(WebSocket conn, String id) {
        sendResponse(conn,
                gson.toJson(
                        new ArrayList(Arrays.asList(CommandsResponse.API, new ApiResponseData(id,
                                new BooleanResponse(true)
                        )))
                )
        );
    }

    private static void sendResponse(WebSocket conn, String response) {
        conn.send(MESSAGE_START + response);
    }

    private static String getRandom() {
        return Long.valueOf(Math.round(Math.random() * 8 + 1)).toString();
    }

}
