package net.mercadosocial.moneda.api;


import net.mercadosocial.moneda.api.response.NewsResponse;

import retrofit2.http.GET;
import rx.Observable;

public interface NewsApi {

    @GET("news/")
    Observable<NewsResponse> getNews();

}
