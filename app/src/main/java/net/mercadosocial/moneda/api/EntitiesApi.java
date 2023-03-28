package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.EntitiesResponse;
import net.mercadosocial.moneda.model.Entity;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface EntitiesApi {

    int PAGE_LIMIT_ENTITIES = 2000; // for map screen all entities are needed.

    @GET("entities/?limit=" + PAGE_LIMIT_ENTITIES)
    Observable<Response<EntitiesResponse>> getEntities(@Query("q") String text,
                                                       @Query("categories__in") String categoriesIdsCommaSeparated);

//    @GET("entities/?limit=" + PAGE_LIMIT)
//    Observable<Response<EntitiesResponse>> getEntitiesFiltered();

    @GET("entities/{id}")
    Observable<Response<Entity>> getEntityById(@Path("id") String id);

}
