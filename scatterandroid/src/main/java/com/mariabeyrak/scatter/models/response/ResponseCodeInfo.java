package com.mariabeyrak.scatter.models.response;

public class ResponseCodeInfo {
    private ResponseType message;
    private ResponseCode code;

    public ResponseCodeInfo(ResponseType message, ResponseCode code) {
        this.message = message;
        this.code = code;
    }

    public ResponseType getMessage() {
        return message;
    }

    public ResponseCode getCode() {
        return code;
    }
}
