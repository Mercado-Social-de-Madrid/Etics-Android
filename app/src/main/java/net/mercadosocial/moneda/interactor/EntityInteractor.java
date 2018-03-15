package net.mercadosocial.moneda.interactor;

import android.content.Context;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.EntitiesApi;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.api.response.EntitiesResponse;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.util.Util;

import java.util.List;

import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class EntityInteractor extends BaseInteractor {



    public interface Callback {

        void onResponse(List<Entity> entities);

        void onError(String error);
    }

    public EntityInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void getEntities(final Callback callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        getApi().getEntities()
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<EntitiesResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(EntitiesResponse entitiesResponse) {

                        baseView.setRefreshing(false);

                        callback.onResponse(entitiesResponse.getEntities());


                    }
                });


    }

    public void getEntitiesFiltered(String query, final Callback callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        getApi().getEntitiesFiltered(query)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<EntitiesResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(EntitiesResponse entitiesResponse) {

                        baseView.setRefreshing(false);

                        callback.onResponse(entitiesResponse.getEntities());


                    }
                });


    }

    public void getEntityById(String id, final BaseApiCallback<Entity> callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        getApi().getEntityById(id)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<Entity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<Entity> entityResponse) {

                        baseView.setRefreshing(false);

                        callback.onResponse(entityResponse.body());


                    }
                });
    }

    private EntitiesApi getApi() {
        return getApi(EntitiesApi.class);
    }


}
