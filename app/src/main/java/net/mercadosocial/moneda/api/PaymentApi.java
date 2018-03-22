package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.PaymentsResponse;
import net.mercadosocial.moneda.model.Payment;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface PaymentApi {

    @POST("payment/")
    Observable<Response<Void>> sendPayment(@Body Payment payment);

    @GET("payment/?limit=100")
    Observable<Response<PaymentsResponse>> getPendingPayments();

    @POST("payment/{id}/accept/")
    Observable<Response<Void>> acceptPayment(@Path("id") String id);

    @POST("payment/{id}/cancel/")
    Observable<Response<Void>> cancelPayment(@Path("id") String id);


}
