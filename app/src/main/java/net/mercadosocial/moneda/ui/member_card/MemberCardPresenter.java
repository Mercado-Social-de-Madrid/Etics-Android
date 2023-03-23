package net.mercadosocial.moneda.ui.member_card;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;
import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BasePresenter;
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

        Account account = data.getAccount();
        view.showMemberData(account);

//        String id = App.getUserData(context).getEntity().getMemberId();
        String id = "000143";
        int sizeQR = context.getResources().getDimensionPixelSize(R.dimen.size_qr);

        Bitmap qrBitmap = QRCode.from(id).withSize(sizeQR, sizeQR).bitmap();
        view.showQrBitmap(qrBitmap);

    }

    public void onQRScanned(String qrContent) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.checking_member, qrContent));
        progressDialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            progressDialog.dismiss();
            String status = "ACTIVA";
            view.alert(null, context.getString(R.string.member_check_result, qrContent, status));
        }, 2000);
    }
}
