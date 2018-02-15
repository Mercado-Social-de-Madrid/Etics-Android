package net.mercadosocial.moneda;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.api.common.ApiClient;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.model.AuthLogin;

/**
 * Created by julio on 17/06/16.
 */

public class App extends Application {

    private static final String TAG = "App";

    public static final String PREFIX = "net.mercadosocial.moneda.";
    public static final String SHARED_TOKEN = PREFIX + "shared_token";
    public static final String SHARED_PHONE_NUMBER = PREFIX + "shared_phone_number";

    public static final String SHARED_FIRST_TIME = PREFIX + "first_time_7";
    private static final String DEBUG_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3ZWIifQ.RR_ekblK831invbbLkIofHrgBXwIU5JVqnhcs_K_bqqcz2zA-wIVzXmWZPOfrSIVZNw4YWRUqXA8tymXKLj1bg";
    public static final String SHARED_INTRO_SEEN = PREFIX + "shared_intro_seen";
    private static final String SHARED_USER_DATA = PREFIX + "shared_user_data";
    public static final String ACTION_NOTIFICATION_RECEIVED = PREFIX + "action_notification_received";


    @Override
    public void onCreate() {
        super.onCreate();

        loadApiKey(this);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE))
                .memoryCache(new LruCache(10000000));
        Picasso built = builder.build();
//        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);


        ApiClient.clearInstance();
        ApiClient.BASE_URL_DEBUG = getPrefs(this).getString("baseUrl", null);


//        Log.i(TAG, "token:" + FirebaseInstanceId.getInstance().getToken());

    }


    public static SharedPreferences getPrefs(Context context) {
//        return new SecurePreferences(context);
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveUserData(Context context, Data data) {
        String dataSerial = new Gson().toJson(data);
        getPrefs(context).edit().putString(App.SHARED_USER_DATA, dataSerial).commit();
        AuthLogin.API_KEY = data.getApiKeyFull();
    }

    public static Data getUserData(Context context) {
        String dataSerial = getPrefs(context).getString(SHARED_USER_DATA, null);
        if (dataSerial == null) {
            return null;
        }

        Data data = new Gson().fromJson(dataSerial, Data.class);
        return data;
    }

    private static void loadApiKey(Context context) {
        Data data = getUserData(context);
        if (data != null) {
            AuthLogin.API_KEY = data.getApiKeyFull();
        }
    }

    public static void removeUserData(Context context) {
        getPrefs(context).edit().remove(SHARED_USER_DATA).commit();
    }

    public static void openBonificationDialog(Context context, String amount) {

        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle("¡Enhorabuena!");
        ab.setMessage("Has recibido la bonificación por tu compra en el Mercado Social. \n\n" + amount + " Boniatos");
        ab.setNeutralButton(R.string.close, null);
        ab.show();
    }
}
