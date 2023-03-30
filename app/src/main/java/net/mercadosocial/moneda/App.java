package net.mercadosocial.moneda;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.interactor.CategoriesInteractor;
import net.mercadosocial.moneda.interactor.DeviceInteractor;
import net.mercadosocial.moneda.interactor.UserInteractor;
import net.mercadosocial.moneda.interactor.WalletInteractor;
import net.mercadosocial.moneda.model.AuthLogin;
import net.mercadosocial.moneda.model.Device;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.MES;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.model.User;

import es.dmoral.toasty.Toasty;

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
    public static final String SHARED_FORCE_SEND_TOKEN_FCM_DEVICE = PREFIX + "shared_force_send_token_fcm_device";
    public static final String SHARED_ENTITIES_CACHE = PREFIX + "shared_entities_cache";
    public static final String SHARED_MEMBER_CARD_INTRO_SEEN = PREFIX + "member_card_intro_seen";;


    public static final String ACTION_NOTIFICATION_RECEIVED = PREFIX + "action_notification_received";
    public static final String SHARED_HAS_PINCODE = PREFIX + "has_pincode";

    // FIREBASE MESSAGING
    public static final String TOPIC_NEWS_MADRID = "news";
    public static final String TOPIC_OFFERS_MADRID = "offers";

    public static final String TOPIC_NEWS_MURCIA = "news_mur";
    public static final String TOPIC_OFFERS_MURCIA = "offers_mur";

    public static boolean isInForeground;

//    private List<Entity> entitiesCache = new ArrayList<>();

    public static final String URL_QR_ENTITY = "https://app.mercadosocial.net/qr/"; // {{uuid}}

    @Override
    public void onCreate() {
        super.onCreate();

        loadApiKey(this);

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(DebugHelper.SWITCH_CRASH_REPORTS_ENABLED);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE))
                .memoryCache(new LruCache(10000000));
        Picasso built = builder.build();
//        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        Toasty.Config.getInstance()
                .setErrorColor(ContextCompat.getColor(this, R.color.grey_toast))
                .setInfoColor(ContextCompat.getColor(this, R.color.grey_toast))
                .setSuccessColor(ContextCompat.getColor(this, R.color.grey_toast))
                .setWarningColor(ContextCompat.getColor(this, R.color.grey_toast))
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .apply();

//        Toasty.info(this, "Tostaditas moradas").show();


        Data userData = getUserData(this);
        if (userData != null) {
            refreshUserData();
        }

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new MyObserver());

        loadFirstTime();

        loadMESCity();

        processWorkarounds();

        updateProfileStatus();


    }

    private void updateProfileStatus() {
        Data data = getUserData(this);
        if (data != null) {
            UserInteractor userInteractor = new UserInteractor(this, null);
            if (data.isEntity()) {
                userInteractor.getEntityProfile(new BaseInteractor.BaseApiCallback<Entity>() {
                    @Override
                    public void onResponse(Entity entity) {
                        data.setEntity(entity);
                        saveUserData(App.this, data);
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            } else {
                userInteractor.getPersonProfile(new BaseInteractor.BaseApiCallback<Person>() {
                    @Override
                    public void onResponse(Person person) {
                        data.setPerson(person);
                        saveUserData(App.this, data);
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        }
    }

    private void refreshUserData() {
        // Refresh has_pincode field
        new WalletInteractor(this, null).getWallet(null);
    }

    private void processWorkarounds() {
        if (getPrefs(this).getBoolean(SHARED_FORCE_SEND_TOKEN_FCM_DEVICE, true)) {
            getPrefs(getApplicationContext()).edit().putBoolean(App.SHARED_TOKEN_FIREBASE_SENT, false).commit();
            getPrefs(getApplicationContext()).edit().putBoolean(App.SHARED_FORCE_SEND_TOKEN_FCM_DEVICE, false).commit();
        }
    }

    private void loadMESCity() {
        MES.setCityCode(getPrefs(this).getString(SHARED_MES_CODE_SAVED, null));
    }

    private void loadFirstTime() {
        if (getPrefs(this).getString(SHARED_CATEGORIES_SAVED, null) == null) {
            new CategoriesInteractor(this, null).loadCategoriesDefault();
        }
    }


    public class MyObserver implements DefaultLifecycleObserver {
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
