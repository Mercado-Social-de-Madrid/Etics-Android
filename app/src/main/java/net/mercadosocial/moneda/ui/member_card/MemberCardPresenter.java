package net.mercadosocial.moneda.ui.member_card;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;

import net.glxn.qrgen.android.QRCode;
import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.model.MemberStatus;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.UserInteractor;
import net.mercadosocial.moneda.model.Account;


public class MemberCardPresenter extends BasePresenter {

    private final MemberCardView view;

    public static MemberCardPresenter newInstance(MemberCardView view, Context context) {

        return new MemberCardPresenter(view, context);

    }

    private MemberCardPresenter(MemberCardView view, Context context) {
        super(context, view);

        this.view = view;

    }

    public void onCreate() {

        checkInitialDialog();

    }

    private void checkInitialDialog() {
        if (!getPrefs().getBoolean(App.SHARED_MEMBER_CARD_INTRO_SEEN, false)) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.member_card)
                    .setMessage(R.string.member_card_intro_message)
                    .setPositiveButton(R.string.accept, null)
                    .show();
            getPrefs().edit().putBoolean(App.SHARED_MEMBER_CARD_INTRO_SEEN, true).apply();
        }
    }

    public void onResume() {

        refreshData();
    }

    public void refreshData() {

        Data data = App.getUserData(context);
        if (data == null) {
            view.showLogoutView(true);
            return;
        }

        view.showLogoutView(false);

        if (data.isEntity()) {
            view.showQrScanButton(true);
        }

        String memberType = getString(data.isEntity() ? R.string.entity :
                data.getPerson().isIntercoop() ? R.string.intercoop_member : R.string.member);

        Account account = data.getAccount();
        view.showMemberData(account, memberType);

        QrInfo qrInfo = new QrInfo(data.getCityCode(), account.getMemberId());
        String qrText = new Gson().toJson(qrInfo);
        int sizeQR = context.getResources().getDimensionPixelSize(R.dimen.size_qr);

        Bitmap qrBitmap = QRCode.from(qrText).withSize(sizeQR, sizeQR).bitmap();
        view.showQrBitmap(qrBitmap);

        view.showInactiveMemberView(!account.isActive());

    }

    public void onQRScanned(String qrContent) {

        QrInfo qrInfo = new Gson().fromJson(qrContent, QrInfo.class);
        checkMemberStatus(qrInfo.getCity(), qrInfo.getMemberId());

    }

    private void checkMemberStatus(String city, String memberId) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.checking_member, memberId));
        progressDialog.show();

        new UserInteractor(context, view).getMemberStatus(city, memberId, new BaseInteractor.BaseApiCallback<MemberStatus>() {
            @Override
            public void onResponse(MemberStatus memberStatus) {

                progressDialog.dismiss();
                String status = getString(memberStatus.isActive() ? R.string.active : R.string.inactive);
                view.alert(null, context.getString(R.string.member_check_result, memberId, status));
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                view.toast(message);
            }
        });
    }

    public void onManualMemberIdCheck(String memberId) {

        Data data = App.getUserData(context);
        checkMemberStatus(data.getCityCode(), memberId);

    }
}
