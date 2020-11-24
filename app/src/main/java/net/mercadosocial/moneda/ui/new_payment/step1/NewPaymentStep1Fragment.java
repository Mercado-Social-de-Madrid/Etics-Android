package net.mercadosocial.moneda.ui.new_payment.step1;


import android.Manifest;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Entity;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPaymentStep1Fragment extends BaseFragment implements NewPaymentStep1View, View.OnClickListener, EntitiesPaymentAdapter.OnItemClickListener {

    private RecyclerView recyclerRecipients;
    private TextView btnContinue;
    private NewPaymentStep1Presenter presenter;
    private ProgressBar progressRecipients;
    private EntitiesPaymentAdapter adapter;
    private String urlIdEntity;


    public NewPaymentStep1Fragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {

        recyclerRecipients = (RecyclerView) layout.findViewById(R.id.recycler_recipients);
        progressRecipients = (ProgressBar) layout.findViewById(R.id.progress_recipients);
        btnContinue = (TextView) layout.findViewById(R.id.btn_continue);

        btnContinue.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = NewPaymentStep1Presenter.newInstance(this, getActivity());
        setBasePresenter(presenter);

        View layout = inflater.inflate(R.layout.fragment_payment_step1, container, false);
        findViews(layout);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerRecipients.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerRecipients.addItemDecoration(divider);

        setHasOptionsMenu(true);

        presenter.onCreate();

        if (urlIdEntity != null) {
            presenter.onQRScanned(urlIdEntity);
            urlIdEntity = null;
        }

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
                checkPermissionAndStart();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkPermissionAndStart() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startScan();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toasty.warning(getActivity(), getString(R.string.permission_denied)).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage(R.string.need_access_camera)
                                .setPositiveButton(R.string.go_ahead, (dialog, which) -> token.continuePermissionRequest())
                                .setNegativeButton(R.string.cancel, (dialog, which) -> token.cancelPermissionRequest())
                                .show();
                    }
                })
                .check();
    }

    private void startScan() {

        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(getActivity())
                .withEnableAutoFocus(true)
                .withBleepEnabled(false)
                .withBackfacingCamera()
                .withText(getString(R.string.focus_qr_code_entity))
                .withOnlyQRCodeScanning()
//                .withCenterTracker()
                .withResultListener(barcode -> presenter.onQRScanned(barcode.rawValue))
                .build();
        materialBarcodeScanner.startScan();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                presenter.onContinueClick();
                break;
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        presenter.onEntityItemClick(position);
    }

    public void onQRScanned(String url) {
        if (presenter == null) {
            urlIdEntity = url;
        } else {
            presenter.onQRScanned(url);
        }
    }

    // PRESENTER CALLBACKS
    @Override
    public void enableContinueButton(boolean enable) {
        btnContinue.setEnabled(enable);
    }

    @Override
    public void showEntities(List<Entity> entities) {
        progressRecipients.setVisibility(View.GONE);

        if (adapter == null) {
            adapter = new EntitiesPaymentAdapter(getActivity(), entities);
            adapter.setOnItemClickListener(this);
            recyclerRecipients.setAdapter(adapter);
        } else {
            adapter.updateData(entities);
        }
    }

}
