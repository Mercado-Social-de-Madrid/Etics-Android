package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.model.Person;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import rx.Observable;

public interface UserApi {

    @PUT("profile/")
    Observable<Response<Void>> updateFavourites(@Body Person profile);


}
