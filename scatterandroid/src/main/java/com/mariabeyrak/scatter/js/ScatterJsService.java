package com.mariabeyrak.scatter.js;

import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.mariabeyrak.scatter.ScatterClient;
import com.mariabeyrak.scatter.js.models.ScatterRequest;
import com.mariabeyrak.scatter.js.models.ScatterResponse;
import com.mariabeyrak.scatter.models.ProtocolInfo;
import com.mariabeyrak.scatter.models.Type;
import com.mariabeyrak.scatter.models.requests.appinfo.AppInfoResponseData;
import com.mariabeyrak.scatter.models.requests.getaccount.Account;
import com.mariabeyrak.scatter.models.requests.getaccount.GetAccountResponse;
import com.mariabeyrak.scatter.models.requests.msgtransaction.MsgTransactionRequestParams;
import com.mariabeyrak.scatter.models.requests.transaction.request.TransactionRequestParams;
import com.mariabeyrak.scatter.models.requests.transaction.response.ReturnedFields;
import com.mariabeyrak.scatter.models.requests.transaction.response.TransactionResponse;
import com.mariabeyrak.scatter.models.response.ErrorResponse;
import com.mariabeyrak.scatter.models.response.ResultCode;

//TODO Refactor
final class ScatterJsService {
    final static private Gson gson = new Gson();

    private ScatterJsService() {
    }

    static void getAppInfo(final WebView webView, ScatterClient scatterClient, final ScatterRequest scatterRequest) {
        ScatterClient.AppInfoReceived appInfoReceived = new ScatterClient.AppInfoReceived() {
            @Override
            public void onAppInfoReceivedSuccessCallback(String appName, String appVersion) {
                String responseData = gson.toJson(new AppInfoResponseData(appName, appVersion, ProtocolInfo.name, ProtocolInfo.version));
                sendResponse(webView, scatterRequest.getCallback(), gson.toJson(
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

    static void getEosAccount(final WebView webView, ScatterClient scatterClient, final ScatterRequest scatterRequest) {
        ScatterClient.AccountReceived accountReceived = new ScatterClient.AccountReceived() {
            @Override
            public void onAccountReceivedSuccessCallback(String accountName, String publicKey) {
                sendResponse(webView, scatterRequest.getCallback(), gson.toJson(
                        new ScatterResponse(ResultCode.SUCCESS.name(), gson.toJson(
                                new GetAccountResponse(
                                        "db4960659fb585600be9e0ec48d2e6f4826d6f929c4bcef095356ce51424608d",
                                        publicKey,
                                        "ScatterKit",
                                        false,
                                        new Account[]{
                                                new Account(accountName, "active", publicKey, "eos",
                                                        "aca376f206b8fc25a6ed44dbdc66547c36c6c33e3a119ffbeaef943642f0e906",
                                                        false)})
                        ), ResultCode.SUCCESS.getCode())
                ));
            }

            @Override
            public void onAccountReceivedErrorCallback(Error error) {
            }
        };

        scatterClient.getAccount(accountReceived);
    }

    static void requestSignature(final WebView webView, ScatterClient scatterClient, final ScatterRequest scatterRequest) {
        final TransactionRequestParams transactionRequestParams = gson.fromJson(scatterRequest.getParams(), TransactionRequestParams.class);

        ScatterClient.TransactionCompleted transactionCompleted = new ScatterClient.TransactionCompleted() {
            @Override
            public void onTransactionCompletedSuccessCallback(String[] signatures) {
                String response = gson.toJson(new TransactionResponse(signatures, new ReturnedFields()));
                sendResponse(webView, scatterRequest.getCallback(), gson.toJson(
                        new ScatterResponse(ResultCode.SUCCESS.name(),
                                response,
                                ResultCode.SUCCESS.getCode())
                ));
            }

            @Override
            public void onTransactionCompletedErrorCallback(ResultCode resultCode, String messageToUser) {
                sendErrorScript(webView, scatterRequest.getCallback(), resultCode, messageToUser);
            }
        };

        scatterClient.completeTransaction(transactionRequestParams, transactionCompleted);
    }

    static void requestMsgSignature(final WebView webView, ScatterClient scatterClient, final ScatterRequest scatterRequest) {
        final MsgTransactionRequestParams msgTransactionRequestParams = gson.fromJson(scatterRequest.getParams(), MsgTransactionRequestParams.class);

        ScatterClient.MsgTransactionCompleted msgTransactionCompleted = new ScatterClient.MsgTransactionCompleted() {
            @Override
            public void onMsgTransactionCompletedSuccessCallback(String signature) {
                sendResponse(webView, scatterRequest.getCallback(), gson.toJson(
                        new ScatterResponse(ResultCode.SUCCESS.name(), gson.toJson(signature), ResultCode.SUCCESS.getCode())
                ));
            }

            @Override
            public void onMsgTransactionCompletedErrorCallback(ResultCode resultCode, String messageToUser) {
                sendErrorScript(webView, scatterRequest.getCallback(), resultCode, messageToUser);
            }
        };

        scatterClient.completeMsgTransaction(msgTransactionRequestParams, msgTransactionCompleted);
    }

    private static void sendErrorScript(WebView webView, @Type.ScatterWalletTypes String methodName, ResultCode resultCode, String messageToUser) {
        sendResponse(webView, methodName, gson.toJson(
                new ErrorResponse(resultCode.getCode(), messageToUser, resultCode.name())
        ));
    }

    private static void sendResponse(WebView webView, @Type.ScatterWalletTypes String methodName, String response) {
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
