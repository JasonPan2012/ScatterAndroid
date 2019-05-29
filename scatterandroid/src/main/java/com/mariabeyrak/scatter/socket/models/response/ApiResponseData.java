package com.mariabeyrak.scatter.socket.models.response;

import com.mariabeyrak.scatter.models.response.ResultApiResponseData;

public class ApiResponseData extends ResponseData implements ResponseObject {
    private ResultApiResponseData result;

    public ApiResponseData(String id, ResultApiResponseData result) {
        super(id);
        this.result = result;
    }
}
