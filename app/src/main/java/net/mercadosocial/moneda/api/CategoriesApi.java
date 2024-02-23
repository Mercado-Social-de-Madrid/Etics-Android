package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.CategoriesResponse;
import net.mercadosocial.moneda.model.Category;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface CategoriesApi {

    @GET("nodes/{idNode}/categories/")
    Observable<Response<List<Category>>> getCategories(@Path("idNode") long idNode);


}
