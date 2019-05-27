package com.mariabeyrak.scatter;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mariabeyrak.Scatter;
import com.mariabeyrak.scatter.models.requests.msgtransaction.MsgTransactionRequestParams;
import com.mariabeyrak.scatter.models.requests.serializedtransaction.SerializedTransaction;
import com.mariabeyrak.scatter.models.requests.serializedtransaction.SerializedTransactionRequestParams;
import com.mariabeyrak.scatter.models.requests.transaction.request.TransactionRequestParams;
import com.mariabeyrak.scatterandroid.R;
import com.paytomat.core.util.HashUtil;
import com.paytomat.eos.Eos;
import com.paytomat.eos.PrivateKey;
import com.paytomat.eos.signature.Signature;

import org.bouncycastle.util.encoders.Hex;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "<<SS";

    private Scatter scatterImplementation;

    private static final String accountName = "YOUR_ACCOUNT_NAME";
    private static final String privateKey = "YOUR_PRIVATE_KEY";
    private static final String publicKey = "YOUR_PUBLIC_KEY";

    private ScatterClient scatterClient = new ScatterClient() {
        @Override
        public void getAccount(AccountReceived onAccountReceived) {
            onAccountReceived.onAccountReceivedSuccessCallback(accountName, publicKey);
        }

        @Override
        public void completeTransaction(TransactionRequestParams transactionRequestParams, TransactionCompleted onTransactionCompleted) {
            String[] signatures = ScatterHelper.toEosTransaction(transactionRequestParams, new PrivateKey(privateKey)).getPackedTx().getSignatures();
            onTransactionCompleted.onTransactionCompletedSuccessCallback(signatures);
        }

        @Override
        public void completeSerializedTransaction(SerializedTransactionRequestParams serializedTransactionRequestParams, SerializedTransactionCompleted onTransactionCompleted) {
            SerializedTransaction transaction = serializedTransactionRequestParams.getTransaction();
            String[] signatures = ScatterHelper.getSignaturesForSerializedTransaction(
                    transaction.getSerializedTransaction(),
                    transaction.getChainId(),
                    new PrivateKey(privateKey));
            onTransactionCompleted.onTransactionCompletedSuccessCallback(signatures);
        }

        @Override
        public void completeMsgTransaction(MsgTransactionRequestParams params, MsgTransactionCompleted onMsgTransactionCompleted) {
            byte[] data;

            if (params.getIsHash().equals("true")) data = Hex.decode(params.getData());
            else data = HashUtil.sha256(params.getData().getBytes()).getBytes();

            Signature signature = Eos.signTransactionRaw(data, new PrivateKey(privateKey));
            onMsgTransactionCompleted.onMsgTransactionCompletedSuccessCallback(signature.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webView);

        scatterImplementation = new ScatterFactory().getScatter(webView, scatterClient, true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setWebViewClient(new ScatterWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("https://eostowergame.com");
    }

    @Override
    protected void onDestroy() {
        scatterImplementation.onDestroy();
        super.onDestroy();
    }

    private class ScatterWebViewClient extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            Log.d(TAG, "request: " + request.getUrl().toString());
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.d(TAG, "request: " + url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            scatterImplementation.injectJS();
        }
    }
}
