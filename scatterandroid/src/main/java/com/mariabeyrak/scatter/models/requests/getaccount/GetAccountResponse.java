package com.mariabeyrak.scatter.models.requests.getaccount;

public class GetAccountResponse {
    private String hash;
    private String publicKey;
    private String name;
    private boolean kyc;
    private Account[] accounts;

    public GetAccountResponse(String hash, String publicKey, String name, boolean kyc, Account[] accounts) {
        this.hash = hash;
        this.publicKey = publicKey;
        this.name = name;
        this.kyc = kyc;
        this.accounts = accounts;
    }
}
