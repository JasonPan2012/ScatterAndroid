package com.mariabeyrak.scatter.models.response;

public class ErrorResponse {
    private int code;
    private String message;
    private boolean isError;
    private String type;

    public ErrorResponse(int code, String message, String type) {
        this.code = code;
        this.message = message;
        this.isError = true;
        this.type = type;
    }
}
