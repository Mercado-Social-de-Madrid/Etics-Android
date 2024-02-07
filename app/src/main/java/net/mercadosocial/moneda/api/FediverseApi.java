package net.mercadosocial.moneda.api;

import net.mercadosocial.moneda.api.response.FediverseResponse;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface FediverseApi {

    @GET("api/v1/timelines/public?local=true")
    Observable<Response<List<FediverseResponse>>> getPosts(@Query("max_id") String id);
}
