package com.mariabeyrak.scatter;

import com.mariabeyrak.scatter.models.requests.msgtransaction.MsgTransactionRequestParams;
import com.mariabeyrak.scatter.models.requests.serializedtransaction.SerializedTransactionRequestParams;
import com.mariabeyrak.scatter.models.requests.transaction.request.TransactionRequestParams;
import com.mariabeyrak.scatter.models.response.ResponseCodeInfo;

public abstract class ScatterClient {

    public interface AppInfoReceived {
        void onAppInfoReceivedSuccessCallback(String appName, String appVersion);

        void onAccountReceivedErrorCallback(Error error);
    }

    public interface AccountReceived {
        void onAccountReceivedSuccessCallback(String accountName, String publicKey);

        void onAccountReceivedErrorCallback(Error error);
    }

    public interface TransactionCompleted {
        void onTransactionCompletedSuccessCallback(String[] signatures);

        void onTransactionCompletedErrorCallback(ResponseCodeInfo errorInfo, String messageToUser);
    }

    public interface SerializedTransactionCompleted {
        void onTransactionCompletedSuccessCallback(String[] signatures);

        void onTransactionCompletedErrorCallback(ResponseCodeInfo errorInfo, String messageToUser);
    }

    public interface MsgTransactionCompleted {
        void onMsgTransactionCompletedSuccessCallback(String signature);

        void onMsgTransactionCompletedErrorCallback(ResponseCodeInfo errorInfo, String messageToUser);
    }

    public void getAppInfo(AppInfoReceived onAppInfoReceived) {
    }

    public void getAccount(AccountReceived onAccountReceived) {
    }

    public void completeTransaction(TransactionRequestParams transactionRequestParams, TransactionCompleted onTransactionCompleted) {
    }

    public void completeSerializedTransaction(SerializedTransactionRequestParams serializedTransactionRequestParams, SerializedTransactionCompleted onSerializedTransactionCompleted) {
    }

    public void completeMsgTransaction(MsgTransactionRequestParams msgTransactionRequestParams, MsgTransactionCompleted onMsgTransactionMsgCompleted) {
    }
}