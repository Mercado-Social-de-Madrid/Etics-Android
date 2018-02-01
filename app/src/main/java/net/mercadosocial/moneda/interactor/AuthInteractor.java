package net.mercadosocial.moneda.interactor;

import android.content.Context;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.AuthApi;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.api.response.LoginResponse;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.AuthLogin;
import net.mercadosocial.moneda.util.Util;

import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class AuthInteractor extends BaseInteractor {


    public interface LoginCallback {

        void onResponse(Data data);

        void onError(String error);
    }

    public AuthInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void login(final AuthLogin login, final LoginCallback callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        baseView.setRefresing(true);

        getApi().login(login)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<LoginResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<LoginResponse> response) {

                        baseView.setRefresing(false);
                        if (response.body().isSuccess()) {
                            Data data = response.body().getData();
                            data.setUsername(login.getUsername());
                            callback.onResponse(data);
                        }



                    }
                });


    }


    private AuthApi getApi() {
        return getApi(AuthApi.class);
    }


}
