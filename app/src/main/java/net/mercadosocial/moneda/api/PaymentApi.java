package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.PaymentsResponse;
import net.mercadosocial.moneda.model.Payment;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface PaymentApi {

    @POST("payment/")
    Observable<Response<Void>> sendPayment(@Body Payment payment);

    @GET("payment/")
    Observable<PaymentsResponse> getPendingPayments();

}
