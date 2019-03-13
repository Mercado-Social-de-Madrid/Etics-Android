package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.common.ApiClient;
import net.mercadosocial.moneda.api.model.InvitationRequest;
import net.mercadosocial.moneda.model.Person;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

public interface UserApi {

    @PUT("profile/")
    Observable<Response<Void>> updateFavourites(@Body Person profile);


    @POST(ApiClient.BASE_URL_TOOL + "invite/")
    Observable<Response<Void>> sendInvitation(@Body InvitationRequest invitationRequest);


}
