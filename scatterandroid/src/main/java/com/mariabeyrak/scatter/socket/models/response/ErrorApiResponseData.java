package com.mariabeyrak.scatter.socket.models.response;

public class ErrorApiResponseData extends ResponseData implements ResponseObject {
    private ErrorResultApiResponseData error;

    public ErrorApiResponseData(String id, ErrorResultApiResponseData error) {
        super(id);
        this.error = error;
    }
}
