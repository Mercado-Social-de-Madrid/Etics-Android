package net.mercadosocial.moneda.ui.info;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.util.Util;


public class WebViewActivity extends BaseActivity {

    public static final int TYPE_ALCALA_HALLOWEEN = 0;
    public static final int TYPE_7MZ = 1;
    private static final String EXTRA_URL = "extra_url";

    private final String FILENAME_ALCALA_HALLOWEEN_HTML = "info_mock.html";
    private final String FILENAME_MARCHA_ZOMBIE_HTML = "info_marcha_zombie.html";

    private WebView webView;


    public static void start(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    public static void start(Context context, int typeInfo) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Util.EXTRA_INT, typeInfo);
        context.startActivity(intent);
    }

    private void findViews() {
        webView = (WebView) findViewById(R.id.webview);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);

        findViews();

        configureSecondLevelActivity();
        configWebView();

        if (true) {
            String url = getIntent().getStringExtra(EXTRA_URL);
            webView.loadUrl(url);
            return;
        }

//        int typeScreen = getIntent().getIntExtra(Util.EXTRA_INT, -1);
//
//        switch (typeScreen) {
//
//            case TYPE_ALCALA_HALLOWEEN:
//                setToolbarTitle(R.string.alcala_halloween);
//                loadHtml(FILENAME_ALCALA_HALLOWEEN_HTML);
//
//                break;
//
//            case TYPE_7MZ:
//                setToolbarTitle(R.string.marcha_zombie);
//                    loadHtml(FILENAME_MARCHA_ZOMBIE_HTML);
//                break;
//
//            default:
//                throw new IllegalArgumentException(
//                        "Not passed parameter typeScreen");
//
//        }

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

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

        });

    }


}
