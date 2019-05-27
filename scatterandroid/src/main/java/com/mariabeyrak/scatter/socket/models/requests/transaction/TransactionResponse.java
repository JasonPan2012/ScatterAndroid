package com.mariabeyrak.scatter.socket.models.requests.transaction;

import com.mariabeyrak.scatter.socket.models.response.ResultApiResponseData;

import java.util.Arrays;

public class TransactionResponse extends ResultApiResponseData {
    private String[] signatures;
    private ReturnedFields returnedFields;

    public TransactionResponse(String[] signatures, ReturnedFields returnedFields) {
        this.signatures = signatures;
        this.returnedFields = returnedFields;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "signatures=" + Arrays.toString(signatures) +
                ", returnedFields=" + returnedFields +
                '}';
    }
}