package com.mariabeyrak.scatter.models.requests.serializedtransaction;

public class SerializedTransaction {
    private String chainId;
    private String serializedTransaction;
    private String[] requiredKeys;

    public SerializedTransaction(String chainId, String serializedTransaction, String[] requiredKeys) {
        this.chainId = chainId;
        this.serializedTransaction = serializedTransaction;
        this.requiredKeys = requiredKeys;
    }

    public String getChainId() {
        return chainId;
    }

    public String getSerializedTransaction() {
        return serializedTransaction;
    }

    public String[] getRequiredKeys() {
        return requiredKeys;
    }
}
