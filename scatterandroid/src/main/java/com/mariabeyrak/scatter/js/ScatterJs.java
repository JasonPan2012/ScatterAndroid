package com.mariabeyrak.scatter.js;

import android.util.Log;
import android.webkit.WebView;

import com.mariabeyrak.Scatter;
import com.mariabeyrak.scatter.ScatterClient;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Scanner;

import static com.mariabeyrak.scatter.js.ScatterJsService.injectJs;

public class ScatterJs extends Scatter {
    private static String TAG = "<<SS";
    private String javascriptInterfaceName = "WebView";

    private WeakReference<WebView> webView;

    public ScatterJs(WebView webView, ScatterClient scatterClient) {
        super(scatterClient);
        this.webView = new WeakReference<>(webView);
        initInterface();
    }

    private void initInterface() {
        if (webView.get() != null) {
            webView.get().addJavascriptInterface(new ScatterWebInterface(webView.get(), scatterClient), javascriptInterfaceName);
        }
    }

    @Override
    public void injectJS() {
        try {
            if (webView.get() != null) {
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("assets/scatterkit_script.js");

                Scanner s = new Scanner(inputStream).useDelimiter("\\A");
                String jsScript = s.hasNext() ? s.next() : "";

                final String script = new StringBuilder().append("var SP_SCRIPT = document.createElement('script');\n")
                        .append("var SP_USER_AGENT_ANDROID = \"").append(webView.get().getSettings().getUserAgentString()).append("\";\n")
                        .append("var SP_USER_AGENT_IOS = \"SP_USER_AGENT_IOS\";\n")
                        .append("var SP_TIMEOUT = ").append(60 * 1000).append(";\n")
                        .append("SP_SCRIPT.type = 'text/javascript';\n")
                        .append("SP_SCRIPT.text = \"")
                        .append(jsScript)
                        .append("\";document.getElementsByTagName('head')[0].appendChild(SP_SCRIPT);").toString();

                injectJs(webView.get(), script);
            }
        } catch (Exception e) {
            Log.d(TAG, "Some error with ScatterJs js file");
        }
    }

    @Override
    public void onDestroy() {
        if (webView.get() != null) {
            webView.get().removeJavascriptInterface(javascriptInterfaceName);
            webView.clear();
        }
    }
}
