package com.mariabeyrak.scatter.socket.models.response;

public class ErrorResponse extends ResultApiResponseData {
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
