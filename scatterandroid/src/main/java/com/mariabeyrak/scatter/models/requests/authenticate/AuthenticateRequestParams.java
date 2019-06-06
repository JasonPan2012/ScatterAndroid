package com.mariabeyrak.scatter.models.requests.authenticate;

import android.support.annotation.Nullable;

public class AuthenticateRequestParams {
    private String nonce;
    private @Nullable
    String data;
    private @Nullable
    String publicKey;
    private @Nullable
    String origin;

    public AuthenticateRequestParams(String nonce, @Nullable String data,
                                     @Nullable String publicKey, @Nullable String origin) {
        this.nonce = nonce;
        this.data = data;
        this.publicKey = publicKey;
        this.origin = origin;
    }

    public String getNonce() {
        return nonce;
    }

    @Nullable
    public String getData() {
        return data;
    }

    @Nullable
    public String getPublicKey() {
        return publicKey;
    }

    @Nullable
    public String getOrigin() {
        return origin;
    }
}
