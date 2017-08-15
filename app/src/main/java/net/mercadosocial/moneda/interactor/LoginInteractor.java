package net.mercadosocial.moneda.interactor;

import android.content.Context;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.EntitiesApi;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.util.Util;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class LoginInteractor extends BaseInteractor {


    public interface Callback {

        void onResponse(List<Entity> entities);

        void onError(String error);
    }

    public LoginInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void checkActivePhone(final Callback callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        baseView.setRefresing(true);

        getApi().getEntities()
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<List<Entity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

//                        callback.onError(handleError(e));
                    }

                    @Override
                    public void onNext(List<Entity> entities) {

                        baseView.setRefresing(false);

                        callback.onResponse(entities);


                    }
                });


    }



    private EntitiesApi getApi() {
        return getApi(EntitiesApi.class);
    }


}
