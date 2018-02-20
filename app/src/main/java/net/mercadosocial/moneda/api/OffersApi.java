package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.OffersResponse;

import retrofit2.http.GET;
import rx.Observable;

public interface OffersApi {

    @GET("offers/")
    Observable<OffersResponse> getOffers();

}
