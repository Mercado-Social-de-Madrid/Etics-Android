package net.mercadosocial.moneda.ui.auth.register_web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.common.ApiClient;
import net.mercadosocial.moneda.base.BaseActivity;


public class WebViewRegisterActivity extends BaseActivity {

    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_FILENAME = "extra_filename";
    private static final String EXTRA_TITLE = "extra_title";


    public static final String FILENAME_QUE_ES_MES = "que_es_mes.html";
    public static final String FILENAME_COMO_FUNCIONA_BONIATO = "como_funciona_boniato.html";

    private WebView webView;
    private ProgressBar progressWebview;


    public static void startRemoteUrl(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewRegisterActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    public static void startLocalHtml(Context context, String title, String filename) {
        Intent intent = new Intent(context, WebViewRegisterActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_FILENAME, filename);
        context.startActivity(intent);
    }

    private void findViews() {
        webView = (WebView) findViewById(R.id.webview);
        progressWebview = (ProgressBar) findViewById(R.id.progress_webview);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);

        findViews();

        configureSecondLevelActivity();
        configWebView();

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null) {
            setToolbarTitle(title);
        }

        if (getIntent().hasExtra(EXTRA_URL)) {
            String url = getIntent().getStringExtra(EXTRA_URL);
            webView.loadUrl(url);

        } else if (getIntent().hasExtra(EXTRA_FILENAME)) {
            String filename = getIntent().getStringExtra(EXTRA_FILENAME);
            loadHtml(filename);

        } else {
            throw new IllegalArgumentException(
                    "Not passed url or filename extra parameter");
        }

    }


    private void loadHtml(String htmlFile) {

        webView.loadUrl("file:///android_asset/html/" + htmlFile);

    }

    private void configWebView() {

//        webView.setBackgroundColor(Color.TRANSPARENT);

        WebSettings webviewSettings = webView.getSettings();

        // webviewSettings.setLoadWithOverviewMode(true);
        webviewSettings.setJavaScriptEnabled(true);
        webviewSettings.setPluginState(PluginState.ON);

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());

    }

//    @Override
//    public void onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressWebview.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // Register success closes this screen:
            if (TextUtils.equals(url, ApiClient.BASE_URL_TOOL + "login/?next=/accounts/signup/success/")) {
                finish();
                return true;
            }

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressWebview.setVisibility(View.GONE);
            progressWebview.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressWebview.setVisibility(View.VISIBLE);
            progressWebview.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }

}
