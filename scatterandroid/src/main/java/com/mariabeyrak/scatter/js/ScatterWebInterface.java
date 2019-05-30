package com.mariabeyrak.scatter.js;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.mariabeyrak.scatter.ScatterClient;
import com.mariabeyrak.scatter.js.models.ScatterRequest;

import static com.mariabeyrak.scatter.js.ScatterJsService.getAppInfo;
import static com.mariabeyrak.scatter.js.ScatterJsService.getEosAccount;
import static com.mariabeyrak.scatter.js.ScatterJsService.requestMsgSignature;
import static com.mariabeyrak.scatter.js.ScatterJsService.requestSignature;
import static com.mariabeyrak.scatter.models.Type.GET_OR_REQUEST_IDENTITY;
import static com.mariabeyrak.scatter.models.Type.GET_VERSION;
import static com.mariabeyrak.scatter.models.Type.REQUEST_ARBITRARY_SIGNATURE;
import static com.mariabeyrak.scatter.models.Type.REQUEST_SIGNATURE;

class ScatterWebInterface {
    private static String TAG = "<<SS";

    private WebView webView;
    private ScatterClient scatterClient;

    final static private Gson gson = new Gson();

    ScatterWebInterface(WebView webView, ScatterClient scatterClient) {
        this.webView = webView;
        this.scatterClient = scatterClient;
    }

    @JavascriptInterface
    public void pushMessage(String data) {
        Log.d(TAG, "pushMessage data: " + data);

        ScatterRequest scatterRequest = gson.fromJson(data, ScatterRequest.class);

        switch (scatterRequest.getMethodName()) {
            case GET_VERSION: {
                getAppInfo(webView, scatterClient, scatterRequest);
                break;
            }
            case GET_OR_REQUEST_IDENTITY: {
                getEosAccount(webView, scatterClient, scatterRequest);
                break;
            }
            case REQUEST_SIGNATURE: {
                requestSignature(webView, scatterClient, scatterRequest);
                break;
            }
            case REQUEST_ARBITRARY_SIGNATURE: {
                requestMsgSignature(webView, scatterClient, scatterRequest);
                break;
            }
            default:
                break;
        }
    }

}
