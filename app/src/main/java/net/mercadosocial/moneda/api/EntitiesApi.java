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

    @GET("nodes/{idNode}/providers/")
    Observable<Response<List<Entity>>> getEntities(@Path("idNode") long idNode, @Query("search") String text,
                                                   @Query("categories__in") String categoriesIdsCommaSeparated);


    @GET("nodes/{idNode}/providers/{id}")
    Observable<Response<Entity>> getEntityById(@Path("idNode") long idNode, @Path("id") String id);

}
