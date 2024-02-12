package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.EntitiesResponse;
import net.mercadosocial.moneda.model.Entity;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface EntitiesApi {

    int PAGE_LIMIT_ENTITIES = 2000; // for map screen all entities are needed.

    @GET("providers/")
    Observable<Response<List<Entity>>> getEntities(@Query("q") String text,
                                                   @Query("categories__in") String categoriesIdsCommaSeparated);

    @GET("entities/{id}")
    Observable<Response<Entity>> getEntityById(@Path("id") String id);

}
