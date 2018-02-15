package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.model.Wallet;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

public interface WalletApi {

    @GET("wallet/")
    Observable<Response<Wallet>> getWallet();

}
