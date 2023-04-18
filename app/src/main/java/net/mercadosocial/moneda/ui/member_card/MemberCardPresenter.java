package net.mercadosocial.moneda.ui.member_card;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

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

import java.net.URL;


public class MemberCardPresenter extends BasePresenter {

    private final MemberCardView view;
    private MemberCheckHelper memberCheckHelper;

    public static MemberCardPresenter newInstance(MemberCardView view, Context context) {

        return new MemberCardPresenter(view, context);

    }

    private MemberCardPresenter(MemberCardView view, Context context) {
        super(context, view);

        this.view = view;

    }

    public void onCreate() {

        checkInitialDialog();
        memberCheckHelper = new MemberCheckHelper(context, view);

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

        String qrText = memberCheckHelper.createQrUrl(data.getCityCode(), account.getMemberId());
        int sizeQR = context.getResources().getDimensionPixelSize(R.dimen.size_qr);

        Bitmap qrBitmap = QRCode.from(qrText).withSize(sizeQR, sizeQR).bitmap();
        view.showQrBitmap(qrBitmap);

        view.showInactiveMemberView(!account.isActive());

    }

    public void onQRScanned(String qrContent) {

        memberCheckHelper.parseUrlAndCheck(qrContent);

    }

    public void onManualMemberIdCheck(String memberId) {

        Data data = App.getUserData(context);
        memberCheckHelper.checkMemberStatus(data.getCityCode(), memberId);

    }
}
