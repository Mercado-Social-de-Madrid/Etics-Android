package net.mercadosocial.moneda.ui.member_card;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Account;
import net.mercadosocial.moneda.ui.auth.login.LoginActivity;
import net.mercadosocial.moneda.ui.auth.register_web.RegisterWebActivity;
import net.mercadosocial.moneda.ui.main.MainActivity;
import net.mercadosocial.moneda.views.CircleTransform;
import net.mercadosocial.moneda.views.EditTextDialog;

import es.dmoral.toasty.Toasty;

public class MemberCardFragment  extends BaseFragment implements MemberCardView {

    private MemberCardPresenter presenter;

    private TextView tvMemberName, tvMemberType, tvMemberId, tvMemberDate, tvInactiveMember;
    private ImageView imgMemberQr, imgMemberProfile;
    private View viewMemberLogin, viewMemberLogout;
    private TextView btnLogin, btnSignup;

    private void findViews(View layout) {
        tvMemberName = layout.findViewById(R.id.tv_member_name);
        tvMemberType = layout.findViewById(R.id.tv_member_type);
        tvMemberId = layout.findViewById(R.id.tv_member_id);
        tvMemberDate = layout.findViewById(R.id.tv_member_date);
        imgMemberQr = layout.findViewById(R.id.img_member_qr);
        imgMemberProfile = layout.findViewById(R.id.img_member_profile);
        tvInactiveMember = layout.findViewById(R.id.tv_inactive_member);

        viewMemberLogin = layout.findViewById(R.id.view_member_login);
        viewMemberLogout = layout.findViewById(R.id.view_member_logout);
        btnLogin = layout.findViewById(R.id.btn_login);
        btnSignup = layout.findViewById(R.id.btn_singup);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        presenter = MemberCardPresenter.newInstance(this, getActivity());
        setBasePresenter(presenter);

        View layout = inflater.inflate(R.layout.fragment_member_card, container, false);
        findViews(layout);

        presenter.onCreate();

        btnLogin.setOnClickListener( view -> {
//            checkPermissionAndStart();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });
        btnSignup.setOnClickListener( view -> { startActivity(new Intent(getActivity(), RegisterWebActivity.class)); });
        imgMemberProfile.setOnClickListener( view -> ((MainActivity)getActivity()).openProfileActivity());

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.member_card, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItem_scan_qr:
                checkPermissionAndStart();
                break;
            case R.id.menuItem_check_member_id:
                onManualMemberIdCheckClick();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onManualMemberIdCheckClick() {

        EditTextDialog dialog = new EditTextDialog();
        dialog.setTitle(getString(R.string.enter_member_id));
        dialog.setListener(result -> {
            presenter.onManualMemberIdCheck(result);
        });

        dialog.show(getChildFragmentManager(), null);
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
                .setBeepEnabled(false)
                .setPrompt(getString(R.string.focus_qr_code_member))
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
    public void showMemberData(Account account, String memberType) {
        tvMemberName.setText(account.getMemberName());
        tvMemberId.setText(getString(R.string.member_number, account.getMemberId()));
        tvMemberType.setText(memberType);
        Picasso.get()
                .load(account.getMemberImage())
                .placeholder(R.mipmap.ic_avatar_2)
                .transform(new CircleTransform())
                .error(R.mipmap.ic_avatar_2)
                .into(imgMemberProfile);
    }

    @Override
    public void showQrBitmap(Bitmap qrBitmap) {
        imgMemberQr.setImageBitmap(qrBitmap);
    }

    @Override
    public void showQrScanButton(boolean show) {
        setHasOptionsMenu(show);
    }

    @Override
    public void showLogoutView(boolean showLogoutView) {
        viewMemberLogin.setVisibility(showLogoutView ? View.GONE : View.VISIBLE);
        viewMemberLogout.setVisibility(showLogoutView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showInactiveMemberView(boolean show) {
        tvInactiveMember.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
