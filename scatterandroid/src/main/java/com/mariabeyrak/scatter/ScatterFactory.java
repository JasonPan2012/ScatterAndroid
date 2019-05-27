package com.mariabeyrak.scatter;

import android.webkit.WebView;

import com.mariabeyrak.Scatter;
import com.mariabeyrak.scatter.js.ScatterJs;
import com.mariabeyrak.scatter.socket.ScatterSocket;

public class ScatterFactory {

    public Scatter getScatter(WebView webView, ScatterClient scatterClient,
                              Boolean usesScatterWebExtension) {
        if (usesScatterWebExtension)
            return new ScatterJs(webView, scatterClient);
        else
            return new ScatterSocket(scatterClient);
    }

}
