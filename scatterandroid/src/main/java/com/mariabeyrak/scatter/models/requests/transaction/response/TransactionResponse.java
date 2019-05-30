package com.mariabeyrak.scatter.models.requests.transaction.response;

import java.util.Arrays;

public class TransactionResponse {
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