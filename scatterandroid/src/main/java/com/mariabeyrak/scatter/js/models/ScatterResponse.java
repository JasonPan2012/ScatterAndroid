package com.mariabeyrak.scatter.js.models;

import com.google.gson.annotations.JsonAdapter;
import com.mariabeyrak.scatter.util.RawJsonGsonAdapter;

public class ScatterResponse {
    private String message;
    @JsonAdapter(RawJsonGsonAdapter.class)
    private String data;
    private int code;

    public ScatterResponse(String message, String data, int code) {
        this.message = message;
        this.data = data;
        this.code = code;
    }
}
