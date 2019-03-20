package net.mercadosocial.moneda.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import net.mercadosocial.moneda.R;

public class WebViewCustom extends FrameLayout {

    private WebView webview;
    private ProgressBar progressWebview;
    private WebViewCallback webViewCallback;

    public WebViewCustom(Context context) {
        super(context);
        init();
    }

    public WebViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebViewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void findViews(View layout) {
        webview = (WebView) layout.findViewById(R.id.webview);
        progressWebview = (ProgressBar) layout.findViewById(R.id.progress_webview);
    }

    public WebView getWebview() {
        return webview;
    }


    private void init() {
        View layout = View.inflate(getContext(), R.layout.webview_custom, null);
        findViews(layout);
        configWebView();

        progressWebview.setVisibility(GONE);

        addView(layout);
    }


    private void configWebView() {

//        webView.setBackgroundColor(Color.TRANSPARENT);

        WebSettings webviewSettings = webview.getSettings();

        // webviewSettings.setLoadWithOverviewMode(true);
        webviewSettings.setJavaScriptEnabled(true);
        webviewSettings.setPluginState(WebSettings.PluginState.ON);

        webview.setWebViewClient(new CustomWebViewClient());
        webview.setWebChromeClient(new CustomWebChromeClient());

    }

    public void loadUrl(String url) {
        webview.loadUrl(url);
    }

    public void setWebViewCallback(WebViewCallback webViewCallback) {
        this.webViewCallback = webViewCallback;
    }

    public interface WebViewCallback {
        boolean shouldOverrideUrlLoading(String url);
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

            if (webViewCallback != null) {
                return webViewCallback.shouldOverrideUrlLoading(url);
            }

            return false;
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
