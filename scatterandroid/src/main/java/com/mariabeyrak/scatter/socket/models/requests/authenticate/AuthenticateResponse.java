package com.mariabeyrak.scatter.socket.models.requests.authenticate;

import com.mariabeyrak.scatter.models.response.ResultApiResponseData;

public class AuthenticateResponse extends ResultApiResponseData {
    private String randomString;

    public AuthenticateResponse(String randomString) {
        this.randomString = randomString;
    }
}
