package net.mercadosocial.moneda.ui.get_boniatos;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.util.WindowUtils;
import net.mercadosocial.moneda.views.WebViewCustom;

public class GetBoniatosActivity extends BaseActivity implements GetBoniatosView, View.OnClickListener, WebViewCustom.WebViewCallback {

    private GetBoniatosPresenter presenter;
    private EditText editGetBoniatosAmount;
    private AppCompatButton btnContinuePurchase;
    private TextView tvPaymentText;
    private WebViewCustom webviewPurchase;

    private void findViews() {
        editGetBoniatosAmount = (EditText)findViewById( R.id.edit_get_boniatos_amount );
        btnContinuePurchase = (AppCompatButton)findViewById( R.id.btn_continue_purchase);
        tvPaymentText = findViewById(R.id.tv_payment_text);
        webviewPurchase = findViewById(R.id.webview_purchase);
        
        btnContinuePurchase.setOnClickListener( this );
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = GetBoniatosPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_boniatos);
        findViews();
        configureSecondLevelActivity();

        tvPaymentText.setVisibility(View.GONE);
        webviewPurchase.setWebViewCallback(this);

//        launchDialog();

    }

    @Override
    public void onClick(View v) {
        if ( v == btnContinuePurchase) {
            String amountStr = editGetBoniatosAmount.getText().toString();
            presenter.onPurchaseClick(amountStr);
            WindowUtils.hideSoftKeyboard(this);
        }
    }

    private void launchDialog() {

        setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(false);
            }
        }, 300);
    }


    @Override
    public void showUrlPayment(String url) {

        tvPaymentText.setVisibility(View.VISIBLE);
        webviewPurchase.loadUrl(url);

    }

    @Override
    public boolean shouldOverrideUrlLoading(String url) {
        if (url.endsWith("/payments/end")) {
            presenter.showPurchaseSuccessDialog();
            return true;
        }

        return false;
    }
}
