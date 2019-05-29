package com.mariabeyrak.scatter.socket.models.response;

import com.mariabeyrak.scatter.models.response.ResultApiResponseData;

public class BooleanResponse extends ResultApiResponseData {
    private Boolean response;

    public BooleanResponse(Boolean response) {
        this.response = response;
    }
}
