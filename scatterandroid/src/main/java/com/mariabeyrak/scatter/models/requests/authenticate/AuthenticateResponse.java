package com.mariabeyrak.scatter.models.requests.authenticate;

public class AuthenticateResponse {
    private String randomString;

    public AuthenticateResponse(String randomString) {
        this.randomString = randomString;
    }
}
