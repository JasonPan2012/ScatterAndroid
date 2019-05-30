package com.mariabeyrak.scatter.models.requests.msgtransaction;

public class MsgTransactionRequestParams {
    private String data;
    private String publicKey;
    private boolean isHash;
    private String whatfor;

    public MsgTransactionRequestParams(String data, String publicKey, boolean isHash, String whatfor) {
        this.data = data;
        this.publicKey = publicKey;
        this.isHash = isHash;
        this.whatfor = whatfor;
    }

    public String getData() {
        return data;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public boolean getIsHash() {
        return isHash;
    }

    public String getWhatfor() {
        return whatfor;
    }

    @Override
    public String toString() {
        return "MsgTransactionRequestParams{" +
                "data='" + data + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", isHash='" + isHash + '\'' +
                ", whatfor='" + whatfor + '\'' +
                '}';
    }
}


