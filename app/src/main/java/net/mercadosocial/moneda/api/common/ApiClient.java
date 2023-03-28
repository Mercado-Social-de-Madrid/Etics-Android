package net.mercadosocial.moneda.api.common;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.mercadosocial.moneda.DebugHelper;
import net.mercadosocial.moneda.model.AuthLogin;
import net.mercadosocial.moneda.model.MES;
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
    public static final String BASE_URL_TOOL = DebugHelper.SWITCH_PROD_ENVIRONMENT ? ApiConfig.BASE_URL_TOOL_PRODUCTION_MADRID : ApiConfig.BASE_URL_TOOL_DEBUG;

    public static final String API_PATH = "/api/v1/";

    public static String BASE_API_URL = BASE_URL + API_PATH;

    private static Retrofit sharedInstance;

    public static String baseUrl = BASE_URL;
    public static String baseApiUrl = BASE_API_URL;

    private static JsonDeserializer<Date> jsonDateDeserializer = (json, typeOfT, context) -> {

        try {
            return DateUtils.formatDateApi.parse(((JsonObject) json).get("initDate").getAsString());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }

    };


    public static void setBaseUrl(String url) {
        sharedInstance = null;
        baseUrl = url;
        baseApiUrl = url + API_PATH;
        getInstance();
    }

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
                    .baseUrl(baseApiUrl)
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

            if (AuthLogin.API_KEY != null) {
                requestBuilder.header("Authorization", AuthLogin.API_KEY);
            }

//
//                if (Auth.token != null) {
//                    requestBuilder.header("nonce", Auth.token);
//                }

            requestBuilder.method(original.method(), original.body());
            okhttp3.Request request = requestBuilder.build();

            HttpUrl url = request.url().newBuilder().addQueryParameter("city", MES.getCityCodeHeaderParam()).build();
            request = request.newBuilder().url(url).build();

            okhttp3.Response response = chain.proceed(request);

//            int tryCount = 0;
//            while (!response.isSuccessful() && tryCount < 3) {
//
//                Log.d("intercept", "Request is not successful - " + tryCount);
//
//                tryCount++;
//
//                // retry the request
//                response = chain.proceed(request);
//            }

            // otherwise just pass the original response on
            return response;
        };


        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headersInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
//                .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
//                        return hostname.equals("triskelapps.com");
                    }
                })
                .build();

        return client;

    }

}
