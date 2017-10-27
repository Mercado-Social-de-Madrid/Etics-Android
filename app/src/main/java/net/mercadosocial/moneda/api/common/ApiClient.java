package net.mercadosocial.moneda.api.common;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import net.mercadosocial.moneda.DebugHelper;
import net.mercadosocial.moneda.util.DateUtils;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static final String TAG = "ApiClient";

    // Tutorial Retrofit 2.0
    // http://inthecheesefactory.com/blog/retrofit-2.0/en

    public static final String BASE_URL_PRODUCTION = "---";
    public static final String BASE_URL_DEBUG = "http://ec2-52-212-36-198.eu-west-1.compute.amazonaws.com/api/v1/";

    public static final String BASE_URL =
            DebugHelper.SWITCH_PROD_ENVIRONMENT ? BASE_URL_PRODUCTION : BASE_URL_DEBUG;

    private static Retrofit sharedInstance;

    private static JsonDeserializer<Date> jsonDateDeserializer = new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            try {
                return DateUtils.formatDateApi.parse(((JsonObject) json).get("initDate").getAsString());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }

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
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .client(getUnsafeOkHttpClient())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

        }

        return sharedInstance;
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            okHttpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {


                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder();
                    requestBuilder.header("Content-Type", "application/vnd.api+json");

                    requestBuilder.method(original.method(), original.body());
                    Request request = requestBuilder.build();


                    try {
                        return chain.proceed(request);
                    } catch (IOException e) {
                        Log.e(TAG, "intercept: error api", e);
                        throw new IOException("Network error");
                    }
//
//                    return null;
                }
            });

            // LOGGIN INTERCEPTOR
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.interceptors().add(logging);


            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    private static OkHttpClient getSSLClient(Context context) {
//
//
//        SchemeRegistry schemeRegistry = new SchemeRegistry();
//        schemeRegistry.register(new Scheme("http", PlainSocketFactory
//                .getSocketFactory(), 80));
//        schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(),
//                443));
//
//        HttpParams params = new BasicHttpParams();
//        params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
//        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
//                new ConnPerRouteBean(30));
//        params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
//        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//
//        ClientConnectionManager cm = new SingleClientConnManager(params,
//                schemeRegistry);
//        DefaultHttpClient httpClient = new DefaultHttpClient(cm, params);
//
//        OkHttpClient client = new OkHttpClient();
//
//        return null;
//
//    }
//
//
//    private static OkHttpClient getSSLClientTrusted(Context context) {
//
//        try {
//            OkHttpClient client = new OkHttpClient();
//            KeyStore keyStore = readKeyStore(context); //your method to obtain KeyStore
//
//            SSLContext sslContext = SSLContext.getInstance("SSL");
//
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init(keyStore);
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());
//            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
//            client.setSslSocketFactory(sslContext.getSocketFactory());
//
//            return client;
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        throw new RuntimeException("Unable to configure SSL client");
//
//    }
//
//    static KeyStore readKeyStore(Context context) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
//        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//
//        // get user password and file input stream
//        char[] password = getPassword();
//
//        FileInputStream fis = null;
//        try {
//            fis = context.getResources().openRawResource(R.raw.your_keystore_filename);
//            ks.load(fis, password);
//        } finally {
//            if (fis != null) {
//                fis.close();
//            }
//        }
//        return ks;
//    }
}
