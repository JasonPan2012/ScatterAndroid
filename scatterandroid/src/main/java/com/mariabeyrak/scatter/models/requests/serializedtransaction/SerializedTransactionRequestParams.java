package com.mariabeyrak.scatter.models.requests.serializedtransaction;

public class SerializedTransactionRequestParams {
    private SerializedTransaction transaction;

    public SerializedTransactionRequestParams(SerializedTransaction transaction) {
        this.transaction = transaction;
    }

    public SerializedTransaction getTransaction() {
        return transaction;
    }
}
