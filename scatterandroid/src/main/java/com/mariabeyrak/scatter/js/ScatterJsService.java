package com.mariabeyrak.scatter.js;

import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.mariabeyrak.scatter.ScatterClient;
import com.mariabeyrak.scatter.js.models.MethodName;
import com.mariabeyrak.scatter.js.models.ScatterResponse;
import com.mariabeyrak.scatter.models.ProtocolInfo;
import com.mariabeyrak.scatter.models.requests.appinfo.AppInfoResponseData;
import com.mariabeyrak.scatter.models.requests.msgtransaction.MsgTransactionRequestParams;
import com.mariabeyrak.scatter.models.requests.transaction.request.TransactionRequestParams;
import com.mariabeyrak.scatter.models.requests.transaction.response.ReturnedFields;
import com.mariabeyrak.scatter.models.requests.transaction.response.SignData;
import com.mariabeyrak.scatter.models.requests.transaction.response.TransactionResponseData;
import com.mariabeyrak.scatter.models.response.ErrorResponse;
import com.mariabeyrak.scatter.models.response.ResultCode;

import static com.mariabeyrak.scatter.js.models.MethodName.GET_APP_INFO;
import static com.mariabeyrak.scatter.js.models.MethodName.GET_EOS_ACCOUNT;
import static com.mariabeyrak.scatter.js.models.MethodName.REQUEST_MSG_SIGNATURE;
import static com.mariabeyrak.scatter.js.models.MethodName.REQUEST_SIGNATURE;

final class ScatterJsService {
    final static private Gson gson = new Gson();

    private ScatterJsService() {
    }

    static void getAppInfo(final WebView webView, ScatterClient scatterClient) {
        ScatterClient.AppInfoReceived appInfoReceived = new ScatterClient.AppInfoReceived() {
            @Override
            public void onAppInfoReceivedSuccessCallback(String appName, String appVersion) {
                String responseData = gson.toJson(new AppInfoResponseData(appName, appVersion, ProtocolInfo.name, ProtocolInfo.version));
                sendResponse(webView, GET_APP_INFO, gson.toJson(
                        new ScatterResponse(ResultCode.SUCCESS.name(),
                                responseData,
                                ResultCode.SUCCESS.getCode())
                ));
            }

            @Override
            public void onAccountReceivedErrorCallback(Error error) {
            }
        };

        scatterClient.getAppInfo(appInfoReceived);
    }

    static void getEosAccount(final WebView webView, ScatterClient scatterClient) {
        ScatterClient.AccountReceived accountReceived = new ScatterClient.AccountReceived() {
            @Override
            public void onAccountReceivedSuccessCallback(String accountName, String publicKey) {
                sendResponse(webView, GET_EOS_ACCOUNT, gson.toJson(
                        new ScatterResponse(ResultCode.SUCCESS.name(), gson.toJson(accountName), ResultCode.SUCCESS.getCode())
                ));
            }

            @Override
            public void onAccountReceivedErrorCallback(Error error) {
            }
        };

        scatterClient.getAccount(accountReceived);
    }

    static void requestSignature(String data, final WebView webView, ScatterClient scatterClient) {
        final TransactionRequestParams transactionRequestParams = gson.fromJson(data, TransactionRequestParams.class);

        ScatterClient.TransactionCompleted transactionCompleted = new ScatterClient.TransactionCompleted() {
            @Override
            public void onTransactionCompletedSuccessCallback(String[] signatures) {
                String response = gson.toJson(new TransactionResponseData(new SignData(signatures, new ReturnedFields())));
                sendResponse(webView, REQUEST_SIGNATURE, gson.toJson(
                        new ScatterResponse(ResultCode.SUCCESS.name(),
                                response,
                                ResultCode.SUCCESS.getCode())
                ));
            }

            @Override
            public void onTransactionCompletedErrorCallback(ResultCode resultCode, String messageToUser) {
                sendErrorScript(webView, REQUEST_SIGNATURE, resultCode, messageToUser);
            }
        };

        scatterClient.completeTransaction(transactionRequestParams, transactionCompleted);
    }

    static void requestMsgSignature(String data, final WebView webView, ScatterClient scatterClient) {
        final MsgTransactionRequestParams msgTransactionRequestParams = gson.fromJson(data, MsgTransactionRequestParams.class);

        ScatterClient.MsgTransactionCompleted msgTransactionCompleted = new ScatterClient.MsgTransactionCompleted() {
            @Override
            public void onMsgTransactionCompletedSuccessCallback(String signature) {
                sendResponse(webView, REQUEST_MSG_SIGNATURE, gson.toJson(
                        new ScatterResponse(ResultCode.SUCCESS.name(), gson.toJson(signature), ResultCode.SUCCESS.getCode())
                ));
            }

            @Override
            public void onMsgTransactionCompletedErrorCallback(ResultCode resultCode, String messageToUser) {
                sendErrorScript(webView, REQUEST_MSG_SIGNATURE, resultCode, messageToUser);
            }
        };

        scatterClient.completeMsgTransaction(msgTransactionRequestParams, msgTransactionCompleted);
    }

    private static void sendErrorScript(WebView webView, @MethodName.Methods String methodName, ResultCode resultCode, String messageToUser) {
        sendResponse(webView, methodName, gson.toJson(
                new ErrorResponse(resultCode.getCode(), messageToUser, resultCode.name())
        ));
    }

    private static void sendResponse(WebView webView, @MethodName.Methods String methodName, String response) {
        injectJs(webView, methodName + "('" + response + "')");
    }

    static void injectJs(final WebView webView, final String script) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript(script, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                    }
                });
            }
        });
    }
}
