package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.model.Entity;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface EntitiesApi {

    @GET("/entities")
    Observable<List<Entity>> getEntities();

}
