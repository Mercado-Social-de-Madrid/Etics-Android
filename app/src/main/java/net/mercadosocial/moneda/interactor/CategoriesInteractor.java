package net.mercadosocial.moneda.interactor;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.CategoriesApi;
import net.mercadosocial.moneda.api.response.ApiError;
import net.mercadosocial.moneda.api.response.CategoriesResponse;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Category;
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
public class CategoriesInteractor extends BaseInteractor {
    

    public CategoriesInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void getCategories(final BaseApiGETListCallback<Category> callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        List<Category> categories = getSavedCategories();
        callback.onResponse(categories);

        getApi().getCategories(getNodeId())
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<Category>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<List<Category>> response) {

                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            callback.onError(apiError.getMessage());
                            return;
                        }

                        saveCategories(response.body());

                        callback.onResponse(response.body());


                    }

                });


    }

    public List<Category> getSavedCategories() {
        String serialized = App.getPrefs(context).getString(App.SHARED_CATEGORIES_SAVED, null);
        if (serialized == null) {
            return new ArrayList<>();
        }

        // For retrocompatibility
        try {
            Type listType = new TypeToken<List<Category>>() {}.getType();
            return new Gson().fromJson(serialized, listType);
        } catch (JsonSyntaxException e) {
            return new ArrayList<>();
        }
    }

    private void saveCategories(List<Category> categories) {
        String serialized = new Gson().toJson(categories);
        App.getPrefs(context).edit().putString(App.SHARED_CATEGORIES_SAVED, serialized).apply();
    }

    private CategoriesApi getApi() {
        return getApi(CategoriesApi.class);
    }


    public void loadCategoriesDefault() {
        String categsDefautl = Util.getStringFromAssets(context, "data/categories_default.json");
        App.getPrefs(context).edit().putString(App.SHARED_CATEGORIES_SAVED, categsDefautl).commit();
    }
}
