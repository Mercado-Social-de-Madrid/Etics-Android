package net.mercadosocial.moneda;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

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


    @Override
    public void onCreate() {
        super.onCreate();


        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
//        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);


    }

    public static SharedPreferences getPrefs(Context context) {
//        return new SecurePreferences(context);
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
