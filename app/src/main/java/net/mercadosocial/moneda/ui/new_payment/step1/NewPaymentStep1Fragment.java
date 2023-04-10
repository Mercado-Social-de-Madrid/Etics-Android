package net.mercadosocial.moneda.ui.new_payment.step1;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
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
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.util.WindowUtils;

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
    private AppCompatImageView btnSearchRecipints;
    private AppCompatEditText editSearchRecipients;
    private View tvNoResults;


    public NewPaymentStep1Fragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {

        recyclerRecipients = (RecyclerView) layout.findViewById(R.id.recycler_recipients);
        progressRecipients = (ProgressBar) layout.findViewById(R.id.progress_recipients);
        btnContinue = (TextView) layout.findViewById(R.id.btn_continue);
        btnSearchRecipints = layout.findViewById(R.id.btn_search_recipients);
        editSearchRecipients = layout.findViewById(R.id.edit_search_recipients);
        tvNoResults = layout.findViewById(R.id.tv_no_results);

        btnContinue.setOnClickListener(this);
        btnSearchRecipints.setOnClickListener(this);

        editSearchRecipients.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

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
                                .setMessage(R.string.need_access_camera_scan_qr)
                                .setPositiveButton(R.string.go_ahead, (dialog, which) -> token.continuePermissionRequest())
                                .setNegativeButton(R.string.cancel, (dialog, which) -> token.cancelPermissionRequest())
                                .show();
                    }
                })
                .check();
    }

    private void startScan() {

        // https://github.com/journeyapps/zxing-Android-embedded
        IntentIntegrator.forSupportFragment(this)
                .setBeepEnabled(true)
                .setPrompt(getString(R.string.focus_qr_code_entity))
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setOrientationLocked(false)
                .initiateScan();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                presenter.onQRScanned(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                presenter.onContinueClick();
                break;

            case R.id.btn_search_recipients:
                performSearch();
                break;
        }
    }

    private void performSearch() {
        WindowUtils.hideSoftKeyboard(getActivity());
        String text = editSearchRecipients.getText().toString().trim();
        presenter.searchEntities(text);
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
        recyclerRecipients.setVisibility(View.VISIBLE);

        if (adapter == null) {
            adapter = new EntitiesPaymentAdapter(getActivity(), entities);
            adapter.setOnItemClickListener(this);
            recyclerRecipients.setAdapter(adapter);
        } else {
            adapter.updateData(entities);
        }

        tvNoResults.setVisibility(entities != null && !entities.isEmpty() ? View.GONE : View.VISIBLE);

    }

    @Override
    public void showProgress(boolean show) {
        progressRecipients.setVisibility(View.VISIBLE);
        recyclerRecipients.setVisibility(View.GONE);

    }
}
