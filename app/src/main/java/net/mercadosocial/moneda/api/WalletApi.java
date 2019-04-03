package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.model.Purchase;
import net.mercadosocial.moneda.api.model.ResetPincodeRequest;
import net.mercadosocial.moneda.api.response.PurchaseResponse;
import net.mercadosocial.moneda.model.Wallet;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface WalletApi {

    @GET("wallet/")
    Observable<Response<Wallet>> getWallet();

    @POST("wallet/purchase/")
    Observable<Response<PurchaseResponse>> purchaseCurrency(@Body Purchase purchase);

    @POST("wallet/reset_pincode/")
    Observable<Response<Void>> resetPincode(@Body ResetPincodeRequest resetPincodeRequest);

}
