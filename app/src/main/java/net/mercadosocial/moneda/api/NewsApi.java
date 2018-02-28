package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.NewsResponse;
import net.mercadosocial.moneda.model.News;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface NewsApi {

    @GET("news/")
    Observable<NewsResponse> getNews();

    @GET("news/{id}")
    Observable<News> getNewsById(@Path("id") String id);

}
