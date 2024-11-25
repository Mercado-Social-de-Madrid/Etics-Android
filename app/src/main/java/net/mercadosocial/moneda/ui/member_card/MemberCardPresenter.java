package net.mercadosocial.moneda.ui.member_card;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import net.glxn.qrgen.android.QRCode;
import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.model.Account;
import net.mercadosocial.moneda.model.Node;

import java.util.Locale;


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

        Account account = data.getAccount();

        if (account == null) {
            view.showLogoutView(true);
            view.toast(R.string.error_no_member_data_found);
            FirebaseCrashlytics.getInstance().recordException(
                    new IllegalStateException("Null account: " + data.getUsername()));
            return;
        }

        view.showLogoutView(false);

        view.showQrScanButton(data.isEntity());

        String memberType = getString(data.isEntity() ? R.string.member_entity :
                data.getPerson().isIntercoop() ? R.string.member_intercoop : R.string.member_consumer)
                .toLowerCase(Locale.ROOT);

        view.showMemberData(account, memberType);

        Node node = getApp().getCurrentNode();

        String qrText = memberCheckHelper.createQrUrl(node.getShortname(), account.getMemberId());
        int sizeQR = context.getResources().getDimensionPixelSize(R.dimen.size_qr);

        Bitmap qrBitmap = QRCode.from(qrText).withSize(sizeQR, sizeQR).bitmap();
        view.showQrBitmap(qrBitmap);

        view.showInactiveMemberView(!account.isActive());

    }

    public void onQRScanned(String qrContent) {

        memberCheckHelper.parseUrlAndCheck(qrContent);

    }

    public void onManualMemberIdCheck(String memberId) {

        Node node = getApp().getCurrentNode();
        memberCheckHelper.checkMemberStatus(node.getShortname(), memberId);

    }
}
