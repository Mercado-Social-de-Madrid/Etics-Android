package net.mercadosocial.moneda.interactor;

import android.content.Context;

import com.google.gson.Gson;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.CategoriesApi;
import net.mercadosocial.moneda.api.response.ApiError;
import net.mercadosocial.moneda.api.response.CategoriesResponse;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Category;
import net.mercadosocial.moneda.util.Util;

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

        getApi().getCategories()
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<CategoriesResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<CategoriesResponse> response) {

                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            callback.onError(apiError.getMessage());
                            return;
                        }

                        saveCategories(response.body());

                        callback.onResponse(response.body().getCategories());


                    }

                });


    }

    private List<Category> getSavedCategories() {
        String serialized = App.getPrefs(context).getString(App.SHARED_CATEGORIES_SAVED, null);
        // this never must be null
        CategoriesResponse categoriesResponse = new Gson().fromJson(serialized, CategoriesResponse.class);
        return categoriesResponse.getCategories();
    }

    private void saveCategories(CategoriesResponse body) {
        String serialized = new Gson().toJson(body);
        App.getPrefs(context).edit().putString(App.SHARED_CATEGORIES_SAVED, serialized).commit();
    }

    private CategoriesApi getApi() {
        return getApi(CategoriesApi.class);
    }


    public void loadCategoriesDefault() {
        String categsDefautl = Util.getStringFromAssets(context, "data/categories_default.json");
        App.getPrefs(context).edit().putString(App.SHARED_CATEGORIES_SAVED, categsDefautl).commit();
    }
}
