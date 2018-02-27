package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.EntitiesResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface EntitiesApi {

    @GET("entities/")
    Observable<EntitiesResponse> getEntities();

    @GET("entities/")
    Observable<EntitiesResponse> getEntitiesFiltered(@Query("q") String query);

}
