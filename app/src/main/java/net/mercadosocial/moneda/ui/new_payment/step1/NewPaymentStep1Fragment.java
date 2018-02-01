package net.mercadosocial.moneda.ui.new_payment.step1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.android.gms.vision.barcode.Barcode;

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


    public NewPaymentStep1Fragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {

        recyclerRecipients = (RecyclerView)layout.findViewById( R.id.recycler_recipients );
        progressRecipients = (ProgressBar) layout.findViewById(R.id.progress_recipients);
        btnContinue = (TextView)layout.findViewById( R.id.btn_continue );

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

        setHasOptionsMenu(true);

        presenter.onCreate();

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
