package com.mariabeyrak.scatter.js.models;

import com.mariabeyrak.scatter.models.response.ResultCode;

public class ScatterResponse {
    private String methodName;
    private ResultCode resultCode;
    private String dataInJson;

    public ScatterResponse(String methodName, ResultCode resultCode, String dataInJson) {
        this.methodName = methodName;
        this.resultCode = resultCode;
        this.dataInJson = dataInJson;
    }

    public String formatSuccessResponse() {
        return methodName + "('{\"message\":\"" + resultCode + "\",\"data\":" + dataInJson + ",\"code\":" + resultCode.getCode() + "}')";
    }

    public String formatErrorResponse(String messageToUser) {
        return methodName + "('{\"code\":" + resultCode.getCode() + ",\"message\":\"" + messageToUser + "\",\"isError\":true,\"type\":\"" + resultCode + "\"}')";
    }

}
