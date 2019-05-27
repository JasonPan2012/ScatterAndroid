package com.mariabeyrak;

import com.mariabeyrak.scatter.ScatterClient;

public abstract class Scatter {
    protected ScatterClient scatterClient;

    public Scatter(ScatterClient scatterClient) {
        this.scatterClient = scatterClient;
    }

    abstract public void onDestroy();

    abstract public void injectJS();
}
