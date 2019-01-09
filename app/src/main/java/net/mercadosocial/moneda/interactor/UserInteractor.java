package net.mercadosocial.moneda.interactor;

import android.content.Context;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.UserApi;
import net.mercadosocial.moneda.api.response.ApiError;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.util.Util;

import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class UserInteractor extends BaseInteractor {



    public UserInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void updateFavourites(Person profile, final BaseApiPOSTCallback callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        getApi().updateFavourites(profile)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
//                .doOnTerminate(actionTerminate)
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
                            callback.onError(apiError.getMessage());
                            return;
                        }

                        callback.onSuccess(null);

                    }
                });


    }

    private UserApi getApi() {
        return getApi(UserApi.class);
    }


}
