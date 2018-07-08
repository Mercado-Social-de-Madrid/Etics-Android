package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.TransactionResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface TransactionApi {

    public static final int PAGE_LIMIT = 30;

    @GET("transaction/?limit=" + PAGE_LIMIT)
    Observable<Response<TransactionResponse>> getTransactions(@Query("offset") int offset);


    @GET("transaction/?limit=100")
    Observable<Response<TransactionResponse>> getTransactions100();

}
