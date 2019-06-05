package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.model.InvitationRequest;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;


public interface UserApi {

    @PUT("profile/")
    Observable<Response<Void>> updateFavourites(@Body Person profile);

    @GET("profile/")
    Observable<Response<Person>> getPersonProfile();

    @GET("entity/")
    Observable<Response<Entity>> getEntityProfile();

    @POST("invite/")
    Observable<Response<Void>> sendInvitation(@Body InvitationRequest invitationRequest);


}
