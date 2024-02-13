package net.mercadosocial.moneda.api.common;


import net.mercadosocial.moneda.api.FediverseApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class FediverseApiClient {

    private static Retrofit retrofit = null;

    public static FediverseApi getApiService(String fediverseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(fediverseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit.create(FediverseApi.class);
    }

}
