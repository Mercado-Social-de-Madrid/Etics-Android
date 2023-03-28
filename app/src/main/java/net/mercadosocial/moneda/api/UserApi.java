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

    @GET("profile/")
    Observable<Response<Person>> getPerson();

    @PUT("profile/")
    Observable<Response<Void>> updatePerson(@Body Person person);

    @GET("entity/")
    Observable<Response<Entity>> getEntity();

    @PUT("entity/")
    Observable<Response<Void>> updateEntity(@Body Entity entity);

    @POST("invite/")
    Observable<Response<Void>> sendInvitation(@Body InvitationRequest invitationRequest);


}
