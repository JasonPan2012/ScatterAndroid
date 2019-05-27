package com.mariabeyrak.scatter.models.requests.transaction.response;

public class TransactionResponseData {
    private SignData signData;

    public TransactionResponseData(SignData signData) {
        this.signData = signData;
    }

    public SignData getSignData() {
        return signData;
    }

    @Override
    public String toString() {
        return "TransactionResponseData{" +
                "signData=" + signData +
                '}';
    }
}
