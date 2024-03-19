package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.model.Offer;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface OffersApi {

    @GET("nodes/{idNode}/offers/?limit=100")
    Observable<Response<List<Offer>>> getOffers(@Path("idNode") long idNode);

    @GET("nodes/{idNode}/offers/{id}")
    Observable<Response<Offer>> getOfferById(@Path("idNode") long idNode, @Path("id") String id);
}
