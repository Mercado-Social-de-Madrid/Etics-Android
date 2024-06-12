package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.model.InvitationRequest;
import net.mercadosocial.moneda.api.model.MemberStatus;
import net.mercadosocial.moneda.api.model.ProfileImageReqRes;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;


public interface UserApi {


    @PUT("profile_image/")
    Observable<Response<ProfileImageReqRes>> updateProfileImage(
            @Header("Authorization") String token, @Body ProfileImageReqRes profileImageRequest);

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

    @GET("member_status/")
    Observable<Response<MemberStatus>> getMemberStatus(@Query("city") String cityCode, @Query("member_id") String memberId);

}
