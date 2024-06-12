package net.mercadosocial.moneda.api.common;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.DebugHelper;
import net.mercadosocial.moneda.model.AuthLogin;
import net.mercadosocial.moneda.util.DateUtils;

import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.HttpUrl;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static final String TAG = "ApiClient";

    // Tutorial Retrofit 2.0
    // http://inthecheesefactory.com/blog/retrofit-2.0/en

    public static final String BASE_URL = DebugHelper.SWITCH_PROD_ENVIRONMENT ? ApiConfig.BASE_URL_PRODUCTION : ApiConfig.BASE_URL_DEBUG;

    public static final String MEDIA_PATH = "media/";
    public static final String MEDIA_URL = BASE_URL + MEDIA_PATH;

    public static final String API_PATH = "api/v2/";

    public static String BASE_API_URL = BASE_URL + API_PATH;

    private static Retrofit sharedInstance;

    private static final JsonDeserializer<Date> jsonDateDeserializer = (json, typeOfT, context) -> {

        try {
            return DateUtils.formatDateApi.parse(((JsonObject) json).get("initDate").getAsString());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }

    };


    public static Retrofit getInstance() {
        if (sharedInstance == null) {

            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.VOLATILE, Modifier.TRANSIENT, Modifier.STATIC)
//                    .registerTypeAdapter(Area.class, null)
                    .setPrettyPrinting()
//                    .registerTypeAdapter(Date.class, jsonDateDeserializer)
                    .setDateFormat(DateUtils.formatDateApi.toPattern())
                    .create();


            sharedInstance = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getOkHttpClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

        }

        return sharedInstance;
    }

    private static okhttp3.OkHttpClient getOkHttpClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okhttp3.Interceptor headersInterceptor = chain -> {

            okhttp3.Request original = chain.request();

            okhttp3.Request.Builder requestBuilder = original.newBuilder();
            requestBuilder.header("Content-Type", "application/json");

            requestBuilder.method(original.method(), original.body());
            okhttp3.Request request = requestBuilder.build();

            return chain.proceed(request);
        };


        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headersInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
//                .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .build();

        return client;

    }

}
