package net.mercadosocial.moneda.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BasePresenter;

/**
 * Created by julio on 21/08/17.
 */

public class NewPaymentActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_ID_WALLET = "extra_id_wallet";


    private AppCompatAutoCompleteTextView autocompleteReceiver;
    private ImageView imgScanQr;
    private SwitchCompat switchCalcBonification;
    private LinearLayout viewCalcBonification;
    private AppCompatEditText editEuroCalcBonification;
    private AppCompatEditText editMesAmount;
    private View btnSendPayment;

    public void findViews() {
        autocompleteReceiver = (AppCompatAutoCompleteTextView) findViewById(R.id.autocomplete_receiver);
        imgScanQr = (ImageView) findViewById(R.id.img_scan_qr);
        switchCalcBonification = (SwitchCompat) findViewById(R.id.switch_calc_bonification);
        viewCalcBonification = (LinearLayout) findViewById(R.id.view_calc_bonification);
        editEuroCalcBonification = (AppCompatEditText) findViewById(R.id.edit_euro_calc_bonification);
        editMesAmount = (AppCompatEditText) findViewById(R.id.edit_mes_amount);
        btnSendPayment = findViewById(R.id.btn_send_payment);

        btnSendPayment.setOnClickListener(this);
        imgScanQr.setOnClickListener(this);
    }



    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    public static Intent newNewPaymentActivity(Context context, int idWallet) {
        Intent intent = new Intent(context, NewPaymentActivity.class);
        intent.putExtra(EXTRA_ID_WALLET, idWallet);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);

        configureSecondLevelActivity();
        findViews();

        String[] receivers = getResources().getStringArray(R.array.receivers);
        ArrayAdapter<String> autocompletetextAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line, receivers);

        autocompleteReceiver.setAdapter(autocompletetextAdapter);

        switchCalcBonification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewCalcBonification.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_payment:
                toast("Enviaríamos el pago");
                break;

            case R.id.img_scan_qr:
                toast("Abriríamos el escáner QR");
                break;
        }
    }
}
