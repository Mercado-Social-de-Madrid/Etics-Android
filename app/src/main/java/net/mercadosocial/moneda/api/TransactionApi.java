package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.TransactionResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

public interface TransactionApi {

    @GET("transaction/?limit=100")
    Observable<Response<TransactionResponse>> getTransactions();

}
