package com.mariabeyrak.scatter.socket.models.response;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CommandsResponse implements ResponseObject {
    public static final String PAIRED = "paired";
    public static final String API = "api";
    public static final String CONNECTED = "connected";
    public static final String REKEY = "rekey";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({PAIRED, API, CONNECTED, REKEY})
    public @interface ScatterCommandsResponse {
    }
}