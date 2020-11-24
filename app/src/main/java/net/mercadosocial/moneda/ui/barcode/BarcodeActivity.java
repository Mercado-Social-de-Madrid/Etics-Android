package net.mercadosocial.moneda.ui.barcode;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;

import es.dmoral.toasty.Toasty;

public class BarcodeActivity extends BaseActivity {

    public static final String EXTRA_BARCODE_INFO = "extra_barcode_info";
    private CodeScanner mCodeScanner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        configureSecondLevelActivity();

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        // Use builder
        mCodeScanner = CodeScanner.builder()
                /*camera can be specified by calling .camera(cameraId),
                first back-facing camera on the device by default*/
                /*code formats*/
//                .formats(CodeScanner.ALL_FORMATS)/*List<BarcodeFormat>*/
                /*or .formats(BarcodeFormat.QR_CODE, BarcodeFormat.DATA_MATRIX, ...)*/
                .format(BarcodeFormat.QR_CODE) //- only one format
                /*auto focus*/
                .autoFocus(true).autoFocusMode(AutoFocusMode.SAFE).autoFocusInterval(2000L)
                /*flash*/
                .flash(false)
                /*decode callback*/
                .onDecoded(new DecodeCallback() {
                    @Override
                    public void onDecoded(@NonNull final Result result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.info(BarcodeActivity.this, result.getText(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                        Intent data = new Intent();
                        data.putExtra(BarcodeActivity.EXTRA_BARCODE_INFO, result.getText());
                        setResult(RESULT_OK, data);
                        finish();
                    }
                })
                /*error callback*/
                .onError(new ErrorCallback() {
                    @Override
                    public void onError(@NonNull final Exception error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.error(BarcodeActivity.this, error.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).build(this, scannerView);
        // Or use constructor to create scanner with default parameters
        // All parameters can be changed after scanner created
        // mCodeScanner = new CodeScanner(this, scannerView);
        // mCodeScanner.setDecodeCallback(...);
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkCameraPermission();

    }

    private void checkCameraPermission() {


        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mCodeScanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toasty.warning(BarcodeActivity.this, getString(R.string.permission_denied));
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {
                        new AlertDialog.Builder(BarcodeActivity.this)
                                .setMessage(R.string.need_access_camera)
                                .setPositiveButton(R.string.go_ahead, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        token.continuePermissionRequest();                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).show();
                    }
                }).check();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
