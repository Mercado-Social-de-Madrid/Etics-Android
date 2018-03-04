package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.EntitiesResponse;
import net.mercadosocial.moneda.model.Entity;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface EntitiesApi {

    @GET("entities/?limit=100")
    Observable<EntitiesResponse> getEntities();

    @GET("entities/?limit=100")
    Observable<EntitiesResponse> getEntitiesFiltered(@Query("q") String query);

    @GET("entities/{id}")
    Observable<Response<Entity>> getEntityById(@Path("id") String id);

}
