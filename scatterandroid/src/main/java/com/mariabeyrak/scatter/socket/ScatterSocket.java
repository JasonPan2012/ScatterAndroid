package com.mariabeyrak.scatter.socket;

import com.mariabeyrak.Scatter;
import com.mariabeyrak.scatter.ScatterClient;

import java.io.IOException;

public class ScatterSocket extends Scatter {
    private ScatterWebSocket webSocket;

    public ScatterSocket(ScatterClient scatterClient) {
        super(scatterClient);
        this.webSocket = new ScatterWebSocket(scatterClient);
        webSocket.start();
    }

    @Override
    public void injectJS() {
    }

    @Override
    public void onDestroy() {
        try {
            webSocket.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
