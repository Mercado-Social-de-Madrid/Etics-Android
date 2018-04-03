package net.mercadosocial.moneda.interactor;

import android.content.Context;

import com.crashlytics.android.Crashlytics;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.AuthApi;
import net.mercadosocial.moneda.api.CustomApiException;
import net.mercadosocial.moneda.api.model.ResetPassword;
import net.mercadosocial.moneda.api.response.ApiError;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.api.response.LoginResponse;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.AuthLogin;
import net.mercadosocial.moneda.model.User;
import net.mercadosocial.moneda.util.Util;

import java.io.IOException;

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

        getApi().login(login)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
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

                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            callback.onError(apiError.getMessage());
                            return;
                        }

                        if (response.body().isSuccess()) {
                            Data data = response.body().getData();
                            data.setUsername(login.getUsername());
                            callback.onResponse(data);
                        }


                    }
                });


    }

    public void register(final User user, final BaseApiCallback<Data> callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        getApi().register(user)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<Data>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                        Crashlytics.logException(new CustomApiException("onError", e));
                    }

                    @Override
                    public void onNext(Response<Data> response) {


                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            callback.onError(apiError.getMessage());
                            try {
                                Crashlytics.logException(new CustomApiException(response.errorBody().string()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }

                        if (response.body() == null) {
                            try {
                                callback.onError("Error: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            callback.onResponse(response.body());
                        }

                    }
                });


    }

    public void resetPassword(final String email, final BaseApiCallback<Data> callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setEmail(email);

        getApi().resetPassword(resetPassword)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<Data>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<Data> response) {


                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            callback.onError(apiError.getMessage());
                            return;
                        }

                        if (response.body() == null) {
                            try {
                                callback.onError("Error: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            callback.onResponse(response.body());
                        }

                    }
                });


    }

    private AuthApi getApi() {
        return getApi(AuthApi.class);
    }


}
