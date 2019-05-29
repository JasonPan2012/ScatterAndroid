package com.mariabeyrak.scatter.socket.models.response;

import com.mariabeyrak.scatter.models.response.ResultApiResponseData;

public class StringResponse extends ResultApiResponseData {
    private String response;

    public StringResponse(String response) {
        this.response = response;
    }
}