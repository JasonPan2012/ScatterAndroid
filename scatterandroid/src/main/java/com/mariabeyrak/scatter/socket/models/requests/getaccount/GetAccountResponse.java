package com.mariabeyrak.scatter.socket.models.requests.getaccount;

import com.mariabeyrak.scatter.models.response.ResultApiResponseData;

public class GetAccountResponse extends ResultApiResponseData {
    private Account[] accounts;

    public GetAccountResponse(Account[] accounts) {
        this.accounts = accounts;
    }
}
