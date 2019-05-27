package com.mariabeyrak.scatter.socket.models.response;

public class ApiResponseData extends ResponseData implements ResponseObject {
    private ResultApiResponseData result;

    public ApiResponseData(String id, ResultApiResponseData result) {
        super(id);
        this.result = result;
    }
}
