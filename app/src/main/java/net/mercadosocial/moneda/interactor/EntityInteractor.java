package net.mercadosocial.moneda.interactor;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.EntitiesApi;
import net.mercadosocial.moneda.api.response.ApiError;
import net.mercadosocial.moneda.api.response.EntitiesResponse;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.FilterEntities;
import net.mercadosocial.moneda.util.Util;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

        void onResponse(List<Entity> entities, boolean hasMore);

        void onError(String error);
    }

    public EntityInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void getEntities(FilterEntities filterEntities, final Callback callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        String text = null;
        String categoriesIdsStr = null;

        if (filterEntities != null) {

            text = filterEntities.getText();

            if (filterEntities.getCategoriesIds() != null && !filterEntities.getCategoriesIds().isEmpty()) {
                categoriesIdsStr = "";
                for (int i = 0; i < filterEntities.getCategoriesIds().size(); i++) {
                    String id = filterEntities.getCategoriesIds().get(i);
                    categoriesIdsStr += id + (i < filterEntities.getCategoriesIds().size() - 1 ? "," : "");
                }
            }

        }

        getApi().getEntities(getNodeId(), text, categoriesIdsStr)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<List<Entity>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Response<List<Entity>> response) {

                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            callback.onError(apiError.getMessage());
                            return;
                        }

                        callback.onResponse(response.body(), false);

                        if (filterEntities == null) {
                            cacheEntities(response.body());
                        }

                    }
                });


    }

    private boolean hasCachedEntities() {
        String entitiesSerialized = App.getPrefs(context).getString(App.SHARED_ENTITIES_CACHE, null);
        return entitiesSerialized != null && !entitiesSerialized.isEmpty();
    }

    public List<Entity> getCachedEntities() {
        String entitiesSerialized = App.getPrefs(context).getString(App.SHARED_ENTITIES_CACHE, null);
        Type listType = new TypeToken<ArrayList<Entity>>(){}.getType();
        List<Entity> entities = new Gson().fromJson(entitiesSerialized, listType);
        return entities;
    }

    private void cacheEntities(List<Entity> entities) {
        String entitiesSerialized = new Gson().toJson(entities);
        App.getPrefs(context).edit().putString(App.SHARED_ENTITIES_CACHE, entitiesSerialized).apply();
    }

//    public void getEntitiesFiltered(String query, final Callback callback) {
//
//        if (!Util.isConnected(context)) {
//            baseView.toast(R.string.no_connection);
//            return;
//        }
//
//        getApi().getEntitiesFiltered(query)
//                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
//                .subscribe(new Observer<Response<EntitiesResponse>>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        callback.onError(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(Response<EntitiesResponse> response) {
//
//                        if (!response.isSuccessful()) {
//                            ApiError apiError = ApiError.parse(response);
//                            callback.onError(apiError.getMessage());
//                            return;
//                        }
//
//                        boolean hasMore = response.body().getMeta().getNext() != null;
//                        callback.onResponse(response.body().getEntities(), hasMore);
//
//
//                    }
//                });
//
//
//    }

    public void getEntityById(String id, final BaseApiCallback<Entity> callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        getApi().getEntityById(getNodeId(), id)
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
                    public void onNext(Response<Entity> response) {

                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            callback.onError(apiError.getMessage());
                            return;
                        }

                        callback.onResponse(response.body());


                    }
                });
    }

    private EntitiesApi getApi() {
        return getApi(EntitiesApi.class);
    }


}
