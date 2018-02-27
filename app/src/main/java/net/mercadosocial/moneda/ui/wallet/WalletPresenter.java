package net.mercadosocial.moneda.ui.wallet;

import android.content.Context;

import net.mercadosocial.moneda.App;
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
            view.showUserInfo(data.getUsername());
        }

        refreshWalletData();
        refreshPendingPayments();

    }

    private void refreshPendingPayments() {
        new PaymentInteractor(context, view).getPendingPayments(new BaseInteractor.BaseApiGETListCallback<Payment>() {
            @Override
            public void onResponse(List<Payment> list) {
                if (list != null && !list.isEmpty()) {
                    view.showPendingPaymentsNumber(list.size());
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
                view.showWalletData(false, wallet);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

}
