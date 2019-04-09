package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.CategoriesResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

public interface CategoriesApi {

    @GET("categories/?limit=100")
    Observable<Response<CategoriesResponse>> getCategories();


}
