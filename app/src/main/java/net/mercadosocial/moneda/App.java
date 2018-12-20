package net.mercadosocial.moneda;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.interactor.CategoriesInteractor;
import net.mercadosocial.moneda.interactor.DeviceInteractor;
import net.mercadosocial.moneda.model.AuthLogin;
import net.mercadosocial.moneda.model.Category;
import net.mercadosocial.moneda.model.Device;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.fabric.sdk.android.Fabric;

/**
 * Created by julio on 17/06/16.
 */

public class App extends MultiDexApplication {

    private static final String TAG = "App";

    public static final String PREFIX = "net.mercadosocial.moneda.";
    public static final String SHARED_TOKEN = PREFIX + "shared_token";
    public static final String SHARED_PHONE_NUMBER = PREFIX + "shared_phone_number";

    public static final String SHARED_FIRST_TIME = PREFIX + "first_time_7";
    private static final String DEBUG_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3ZWIifQ.RR_ekblK831invbbLkIofHrgBXwIU5JVqnhcs_K_bqqcz2zA-wIVzXmWZPOfrSIVZNw4YWRUqXA8tymXKLj1bg";
    public static final String SHARED_INTRO_SEEN = PREFIX + "shared_intro_seen";
    private static final String SHARED_USER_DATA = PREFIX + "shared_user_data";
    public static final String SHARED_TOKEN_FIREBASE_SENT = PREFIX + "shared_token_firebase_sent";
    public static final String SHARED_CATEGORIES_SAVED = PREFIX + "shared_categories_saved";
    public static final String SHARED_MES_CODE_SAVED = PREFIX + "shared_mes_code_saved";
    public static final String ACTION_NOTIFICATION_RECEIVED = PREFIX + "action_notification_received";
    
    public static boolean isInForeground;


    @Override
    public void onCreate() {
        super.onCreate();

        loadApiKey(this);

        ContentLoadingProgressBar progressBar = new ContentLoadingProgressBar(this);


        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);


//        CrashlyticsCore.getInstance().crash();


        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE))
                .memoryCache(new LruCache(10000000));
        Picasso built = builder.build();
//        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

//        Log.i(TAG, "token:" + FirebaseInstanceId.getInstance().getToken());

        Toasty.Config.getInstance()
                .setErrorColor(ContextCompat.getColor(this, R.color.grey_toast))
                .setInfoColor(ContextCompat.getColor(this, R.color.grey_toast))
                .setSuccessColor(ContextCompat.getColor(this, R.color.grey_toast))
                .setWarningColor(ContextCompat.getColor(this, R.color.grey_toast))
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .apply();

//        Toasty.info(this, "Tostaditas moradas").show();




        ProcessLifecycleOwner.get().getLifecycle().addObserver(new MyObserver());

        loadFirstTime();

    }

    private void loadFirstTime() {
        if (getPrefs(this).getString(SHARED_CATEGORIES_SAVED, null) == null) {
            new CategoriesInteractor(this, null).loadCategoriesDefault();
        }
    }


    public class MyObserver implements DefaultLifecycleObserver {
//        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//        public void onAppGoesForeground() {
//            App.isInForeground = true;
//            Log.i(TAG, "onAppGoesForeground: ");
//        }
//
//        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//        public void onAppGoesBackground() {
//            App.isInForeground = false;
//            Log.i(TAG, "onAppGoesBackground: ");
//
//        }

        @Override
        public void onResume(@NonNull LifecycleOwner owner) {
            App.isInForeground = true;
            Log.i(TAG, "onAppGoesForeground: ");
        }

        @Override
        public void onPause(@NonNull LifecycleOwner owner) {
            App.isInForeground = false;
            Log.i(TAG, "onAppGoesBackground: ");
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
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
        getPrefs(context).edit()
                .remove(SHARED_USER_DATA)
                .remove(SHARED_TOKEN_FIREBASE_SENT)
                .commit();

        new DeviceInteractor(context, null).sendDevice(new Device(), new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                AuthLogin.API_KEY = null;
            }

            @Override
            public void onError(String message) {
                // not good at all
                AuthLogin.API_KEY = null;
            }
        });



    }


    public static boolean isEntity(Context context) {
        Data data = getUserData(context);
        return data != null && data.isEntity();
    }
}
