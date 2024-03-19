package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.model.News;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface NewsApi {

    @GET("nodes/{idNode}/news/?limit=100")
    Observable<Response<List<News>>> getNews(@Path("idNode") long idNode);

    @GET("nodes/{idNode}/news/{id}")
    Observable<Response<News>> getNewsById(@Path("idNode") long idNode, @Path("id") String id);

}
