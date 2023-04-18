package net.mercadosocial.moneda.ui.member_card;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.model.MemberStatus;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.interactor.UserInteractor;

public class MemberCheckHelper {

    private Context context;
    private BaseView view;

    public MemberCheckHelper(Context context, BaseView view) {
        this.context = context;
        this.view = view;
    }

    public void parseUrlAndCheck(String url) {

        if (url.startsWith(App.URL_QR_MEMBER_CARD)) {

            Uri uri = Uri.parse(url);
            String city = uri.getQueryParameter("city");
            String memberId = uri.getQueryParameter("member_id");

            if (city != null && memberId != null) {
                checkMemberStatus(city, memberId);
                return;
            }
        }

        view.toast(R.string.invalid_link);
    }

    public void checkMemberStatus(String city, String memberId) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.checking_member, memberId));
        progressDialog.show();

        new UserInteractor(context, view).getMemberStatus(city, memberId, new BaseInteractor.BaseApiCallback<MemberStatus>() {
            @Override
            public void onResponse(MemberStatus memberStatus) {

                progressDialog.dismiss();
                String status = context.getString(memberStatus.isActive() ? R.string.active : R.string.inactive);
                new AlertDialog.Builder(context)
                        .setIcon(memberStatus.isActive() ? R.mipmap.ic_green_check : R.mipmap.ic_red_cross)
                        .setTitle(context.getString(R.string.member_status_x, status))
                        .setMessage(context.getString(R.string.member_id_x, memberId))
                        .setPositiveButton(R.string.back, null)
                        .show();
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                view.toast(message);
            }
        });
    }

    public String createQrUrl(String cityCode, String memberId) {
        return App.URL_QR_MEMBER_CARD + String.format("?city=%s&member_id=%s", cityCode, memberId);
    }
}
