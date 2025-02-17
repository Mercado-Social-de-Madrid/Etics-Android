package net.mercadosocial.moneda.ui.auth.register_web;

import android.annotation.SuppressLint;
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
import net.mercadosocial.moneda.databinding.ActivityWebviewBinding;


public class WebViewRegisterActivity extends BaseActivity {

    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_FILENAME = "extra_filename";
    private static final String EXTRA_TITLE = "extra_title";
    private ActivityWebviewBinding binding;


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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureSecondLevelActivity();

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null) {
            setToolbarTitle(title);
        }

        if (getIntent().hasExtra(EXTRA_URL)) {
            String url = getIntent().getStringExtra(EXTRA_URL);
            binding.webviewCustom.loadUrl(url);

            binding.webviewCustom.setWebViewCallback(url1 -> {

                if (TextUtils.equals(url, "https://gestionmadrid.mercadosocial.net/login/?next=/accounts/signup/success/")) {
                    finish();
                    return true;
                }

                return false;
            });

        } else if (getIntent().hasExtra(EXTRA_FILENAME)) {
            String filename = getIntent().getStringExtra(EXTRA_FILENAME);
            loadHtml(filename);

        } else {
            throw new IllegalArgumentException(
                    "Not passed url or filename extra parameter");
        }

    }


    private void loadHtml(String htmlFile) {

        binding.webviewCustom.loadUrl("file:///android_asset/html/" + htmlFile);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
