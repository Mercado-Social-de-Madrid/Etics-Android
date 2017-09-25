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
import android.widget.TextView;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.ui.recipient_select.FavRecipientSelectFragment;
import net.mercadosocial.moneda.ui.recipient_select.RecipientSelectActivity;

/**
 * Created by julio on 21/08/17.
 */

public class NewPaymentActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_ID_WALLET = "extra_id_wallet";

    private static final int REQ_CODE_SELECT_RECIPIENT = 1001;


    private AppCompatAutoCompleteTextView autocompleteReceiver;
    private ImageView imgScanQr;
    private SwitchCompat switchCalcBonification;
    private LinearLayout viewCalcBonification;
    private AppCompatEditText editEuroCalcBonification;
    private AppCompatEditText editMesAmount;
    private View btnSendPayment;
    private View btnSelectQRCode;
    private View btnSelectFavourite;
    private View btnSelectFromList;
    private TextView tvRecipientName;

    public void findViews() {
        autocompleteReceiver = (AppCompatAutoCompleteTextView) findViewById(R.id.autocomplete_recipient);
        imgScanQr = (ImageView) findViewById(R.id.img_scan_qr);
        switchCalcBonification = (SwitchCompat) findViewById(R.id.switch_calc_bonification);
        viewCalcBonification = (LinearLayout) findViewById(R.id.view_calc_bonification);
        editEuroCalcBonification = (AppCompatEditText) findViewById(R.id.edit_euro_calc_bonification);
        editMesAmount = (AppCompatEditText) findViewById(R.id.edit_mes_amount);
        btnSendPayment = findViewById(R.id.btn_send_payment);

        tvRecipientName = (TextView) findViewById(R.id.tv_recipient_name);

        btnSelectQRCode = findViewById(R.id.btn_select_qr_code);
        btnSelectFavourite = findViewById(R.id.btn_select_fav);
        btnSelectFromList = findViewById(R.id.btn_select_list);

        btnSendPayment.setOnClickListener(this);
        imgScanQr.setOnClickListener(this);
        btnSelectQRCode.setOnClickListener(this);
        btnSelectFavourite.setOnClickListener(this);
        btnSelectFromList.setOnClickListener(this);
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

        String[] recipients = getResources().getStringArray(R.array.recipients);
        ArrayAdapter<String> autocompletetextAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line, recipients);

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

            case R.id.btn_select_qr_code:
                startScan();
                break;

            case R.id.btn_select_list:
                startActivityForResult(new Intent(this, RecipientSelectActivity.class), REQ_CODE_SELECT_RECIPIENT);
                break;

            case R.id.btn_select_fav:
                new FavRecipientSelectFragment().setOnFavouriteSelectedListener(
                        new FavRecipientSelectFragment.OnFavouriteSelectedListener() {
                            @Override
                            public void onFavouriteSelected(String name) {

                                toast(name);
                                tvRecipientName.setText(name);
                            }
                        }).show(getSupportFragmentManager(), null);
                break;
        }
    }

    private void startScan() {

        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withText(getString(R.string.focus_qr_code_entity))
                .withOnlyQRCodeScanning()
//                .withCenterTracker()
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        toast(barcode.rawValue);
                        tvRecipientName.setText(barcode.rawValue);
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        String textSelected = (String) data.getExtras().get("select");
        toast(textSelected);
        tvRecipientName.setText(textSelected);
    }
}
