package net.mercadosocial.moneda.ui.payment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
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

    private AppCompatEditText editTotalAmountEur;
    private View btnSendPayment;
    private View btnSelectQRCode;
    private View btnSelectFavourite;
    private View btnSelectFromList;
    private TextView tvRecipientName;
    private AppCompatEditText editBoniatosAmount;
    private PaymentSummaryFragment paymentSummary;

    public void findViews() {
        editTotalAmountEur = (AppCompatEditText) findViewById(R.id.edit_total_amount_eur);
        editBoniatosAmount = (AppCompatEditText) findViewById(R.id.edit_boniatos_amount);
        btnSendPayment = findViewById(R.id.btn_send_payment);

        tvRecipientName = (TextView) findViewById(R.id.tv_recipient_name);

        btnSelectQRCode = findViewById(R.id.btn_select_qr_code);
        btnSelectFavourite = findViewById(R.id.btn_select_fav);
        btnSelectFromList = findViewById(R.id.btn_select_list);

        btnSendPayment.setOnClickListener(this);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_payment:
                paymentSummary = new PaymentSummaryFragment();
                paymentSummary.setCallback(new PaymentSummaryFragment.PaymentSummaryCallback() {
                    @Override
                    public void onConfirm(String pinCode) {
                        sendPayment();
                    }

                    @Override
                    public void onDismiss() {
                        paymentSummary.dismiss();
                    }
                });
                paymentSummary.show(getSupportFragmentManager(), null);
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


    private void sendPayment() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, getString(R.string.sending_payment));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                paymentSummary.dismiss();
                showAlertPaymentSuccess();
            }
        }, 3000);
    }

    private void showAlertPaymentSuccess() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("Pago enviado correc tamente");
        ab.setMessage("La entidad debe confirmarlo para completar la transacción y obtener tu bonificación");
        ab.setPositiveButton(getString(R.string.return_back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showPaymentConfirmationDialog();
            }
        });
        ab.show();
    }

    private void showPaymentConfirmationDialog() {

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("¡Pago confirmado!");
        ab.setMessage("Acabas de obtener una bonificación de 3 Bts");
        ab.setPositiveButton(getString(R.string.close), null);
        ab.show();
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
