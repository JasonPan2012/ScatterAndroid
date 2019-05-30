package com.mariabeyrak.scatter.socket.models.response;

import com.google.gson.annotations.JsonAdapter;
import com.mariabeyrak.scatter.util.RawJsonGsonAdapter;

public class ApiResponseData extends ResponseData implements ResponseObject {
    @JsonAdapter(RawJsonGsonAdapter.class)
    private String result;

    public ApiResponseData(String id, String result) {
        super(id);
        this.result = result;
    }
}
