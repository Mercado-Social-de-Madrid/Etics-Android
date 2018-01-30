package net.mercadosocial.moneda.ui.new_payment.step1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.base.BasePresenter;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPaymentStep1Fragment extends BaseFragment {


    public NewPaymentStep1Fragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_payment_step1, container, false);
        findViews(layout);

        setHasOptionsMenu(true);

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_payment_scan, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItem_scan_qr:
                startScan();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startScan() {

        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(getActivity())
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withText(getString(R.string.focus_qr_code_entity))
                .withOnlyQRCodeScanning()
//                .withCenterTracker()
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        Toasty.info(getActivity(), barcode.rawValue).show();
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
