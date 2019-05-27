package com.mariabeyrak.scatter.socket.models.response;

public class ErrorResultApiResponseData {
    private Integer code;
    private String message;

    public ErrorResultApiResponseData(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}