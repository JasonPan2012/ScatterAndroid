package com.mariabeyrak.scatter.socket.models.requests.msgtransaction;

import com.mariabeyrak.scatter.models.response.ResultApiResponseData;

public class MsgTransactionResponse extends ResultApiResponseData {
    private String signature;

    public MsgTransactionResponse(String signature) {
        this.signature = signature;
    }
}
