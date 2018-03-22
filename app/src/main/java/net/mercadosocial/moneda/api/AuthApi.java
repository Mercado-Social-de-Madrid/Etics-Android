package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.model.ResetPassword;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.api.response.LoginResponse;
import net.mercadosocial.moneda.model.AuthLogin;
import net.mercadosocial.moneda.model.User;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface AuthApi {

    @POST("login/")
    Observable<Response<LoginResponse>> login(@Body AuthLogin login);

    @POST("register/")
    Observable<Response<Data>> register(@Body User user);

    @POST("reset_password/")
    Observable<Response<Data>> resetPassword(@Body ResetPassword resetPassword);



}
