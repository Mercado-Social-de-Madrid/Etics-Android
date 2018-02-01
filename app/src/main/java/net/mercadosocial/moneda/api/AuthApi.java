package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.LoginResponse;
import net.mercadosocial.moneda.model.AuthLogin;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface AuthApi {

    @POST("login/")
    Observable<Response<LoginResponse>> login(@Body AuthLogin login);

}
