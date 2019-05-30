package com.mariabeyrak.scatter.models.requests.getaccount;

public class GetAccountResponse {
    private Account[] accounts;

    public GetAccountResponse(Account[] accounts) {
        this.accounts = accounts;
    }
}
