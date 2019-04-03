package net.mercadosocial.moneda.ui.wallet;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;
import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.PaymentInteractor;
import net.mercadosocial.moneda.interactor.WalletInteractor;
import net.mercadosocial.moneda.model.Payment;
import net.mercadosocial.moneda.model.Wallet;

import java.util.List;

/**
 * Created by julio on 2/02/18.
 */


public class WalletPresenter extends BasePresenter {

    private final WalletView view;
    private final WalletInteractor walletInteractor;

    public static WalletPresenter newInstance(WalletView view, Context context) {

        return new WalletPresenter(view, context);

    }

    private WalletPresenter(WalletView view, Context context) {
        super(context, view);

        this.view = view;

        walletInteractor = new WalletInteractor(context, view);

    }

    public void onCreate() {

    }

    public void onResume() {

        refreshData();
    }

    public void refreshData() {

        Data data = App.getUserData(context);
        if (data != null) {
            view.showUserInfo(data.getName(false));
            refreshWalletData();
            refreshPendingPayments();
            refreshSentPayments();
        } else {
            view.showLoggedOutView();
        }


    }


    public boolean isQRGeneratorVisible() {
        return App.isEntity(context);
//        return App.getUserData(context) != null;
    }

    private void refreshPendingPayments() {
        new PaymentInteractor(context, view).getPendingPayments(new BaseInteractor.BaseApiGETListCallback<Payment>() {
            @Override
            public void onResponse(List<Payment> list) {
                if (list != null) {
                    view.showPendingPaymentsNumber(list.size());
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }


    private void refreshSentPayments() {

        new PaymentInteractor(context, view).getSentPayments(new BaseInteractor.BaseApiGETListCallback<Payment>() {
            @Override
            public void onResponse(List<Payment> list) {
                if (list != null) {
                    view.showSentPaymentsNumber(list.size());
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void refreshWalletData() {

        view.showWalletData(true, null);

        walletInteractor.getWallet(new WalletInteractor.Callback() {
            @Override
            public void onResponse(Wallet wallet) {
                getPrefs().edit().putBoolean(App.SHARED_HAS_PINCODE, wallet.getHas_pincode()).commit();
                view.showWalletData(false, wallet);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void onShowQRClick() {

        String id = App.getUserData(context).getEntity().getId();
        String urlId = App.URL_QR_ENTITY + id;
        int sizeQR = context.getResources().getDimensionPixelSize(R.dimen.size_qr);

        Bitmap qrBitmap = QRCode.from(urlId).withSize(sizeQR, sizeQR).bitmap();
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(qrBitmap);

        new AlertDialog.Builder(context)
                .setTitle(R.string.entity_qr)
                .setView(imageView)
                .setNegativeButton(R.string.back, null)
                .show();
    }
}
