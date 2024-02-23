package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.OffersResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface OffersApi {

    @GET("nodes/{idNode}/offers/?limit=100")
    Observable<Response<OffersResponse>> getOffers(@Path("idNode") long idNode);

}
