package net.mercadosocial.moneda.interactor;

import android.content.Context;
import android.text.TextUtils;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.WalletApi;
import net.mercadosocial.moneda.api.model.Purchase;
import net.mercadosocial.moneda.api.model.ResetPincodeRequest;
import net.mercadosocial.moneda.api.response.ApiError;
import net.mercadosocial.moneda.api.response.PurchaseResponse;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Wallet;
import net.mercadosocial.moneda.util.Util;

import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class WalletInteractor extends BaseInteractor {


    public interface Callback {

        void onResponse(Wallet wallet);

        void onError(String error);
    }

    public WalletInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void getWallet(final Callback callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        getApi().getWallet()
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<Wallet>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<Wallet> response) {

                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            callback.onError(apiError.getMessage());
                            return;
                        }

                        Wallet wallet = response.body();
                        App.getPrefs(context).edit().putBoolean(App.SHARED_HAS_PINCODE, wallet.getHas_pincode()).commit();
                        callback.onResponse(wallet);

                    }
                });


    }

    public void purchaseCurrency(Float amount, final BaseApiCallback<String> callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        Purchase purchase = new Purchase(amount);

        getApi().purchaseCurrency(purchase)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<PurchaseResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<PurchaseResponse> response) {

                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            callback.onError(apiError.getMessage());
                            return;
                        }

                        callback.onResponse(response.body().getUrl());

                    }
                });


    }

    public void resetPincode(String pincode, String password, final BaseApiPOSTCallback callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        ResetPincodeRequest resetPincodeRequest = new ResetPincodeRequest(pincode, password);

        getApi().resetPincode(resetPincodeRequest)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<Void> response) {

                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            if (TextUtils.equals(apiError.getMessage(), "incorrect")) {
                                callback.onError(context.getString(R.string.incorrect_password));
                            } else {
                                callback.onError(apiError.getMessage());
                            }
                            return;
                        }

                        callback.onSuccess(null);

                    }
                });


    }

    private WalletApi getApi() {
        return getApi(WalletApi.class);
    }


}
