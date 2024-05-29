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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.interactor.CategoriesInteractor;
import net.mercadosocial.moneda.interactor.NodeInteractor;
import net.mercadosocial.moneda.model.AuthLogin;
import net.mercadosocial.moneda.model.Node;
import net.mercadosocial.moneda.util.Util;
import net.mercadosocial.moneda.util.update_app.UpdateAppManager;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 17/06/16.
 */

public class App extends MultiDexApplication {

    private static final String TAG = "App";

    public static final String PREFIX = "net.mercadosocial.moneda.";
    public static final String SHARED_INTRO_SEEN = PREFIX + "shared_intro_seen_3";
    private static final String SHARED_USER_DATA = PREFIX + "shared_user_data";
    public static final String SHARED_TOKEN_FIREBASE_SENT = PREFIX + "shared_token_firebase_sent";
    public static final String SHARED_CATEGORIES_SAVED = PREFIX + "shared_categories_saved";
    public static final String SHARED_FORCE_SEND_TOKEN_FCM_DEVICE = PREFIX + "shared_force_send_token_fcm_device";
    public static final String SHARED_ENTITIES_CACHE = PREFIX + "shared_entities_cache";
    public static final String SHARED_MEMBER_CARD_INTRO_SEEN = PREFIX + "member_card_intro_seen";

    public static final String ACTION_NOTIFICATION_RECEIVED = PREFIX + "action_notification_received";
    public static final String SHARED_CURRENT_NODE = PREFIX + "current_node";

    public static boolean isInForeground;

//    private List<Entity> entitiesCache = new ArrayList<>();

    public static final String URL_QR_ENTITY = "https://app.mercadosocial.net/qr/"; // {{uuid}}
    public static final String URL_QR_MEMBER_CARD = "https://app.mercadosocial.net/socia/"; // {{uuid}}

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

        UpdateAppManager.scheduleAppUpdateCheckWork(this);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new MyObserver());

        loadFirstTime();

        processWorkarounds();

        setDefaultNodeForMadridUpdates();
        new NodeInteractor(this, null).updateNodeData();

        // FirebaseMessaging.getInstance().subscribeToTopic("news_test");
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Log.d(TAG, "Firebase token: " + task.getResult());
//            } else {
//                Log.d(TAG, "Firebase token ERROR: " + task.getException().getMessage());
//            }
//        });
    }

    private void setDefaultNodeForMadridUpdates() {
        if (getCurrentNode() == null) {
            String madridNodeData = Util.getStringFromAssets(this, "data/node_mad.json");
            Node madridNode = new Gson().fromJson(madridNodeData, Node.class);
            setCurrentNode(madridNode);
        }
    }


    private void processWorkarounds() {
        if (getPrefs(this).getBoolean(SHARED_FORCE_SEND_TOKEN_FCM_DEVICE, true)) {
            getPrefs(getApplicationContext()).edit().putBoolean(App.SHARED_TOKEN_FIREBASE_SENT, false).commit();
            getPrefs(getApplicationContext()).edit().putBoolean(App.SHARED_FORCE_SEND_TOKEN_FCM_DEVICE, false).commit();
        }
    }

    private void loadFirstTime() {
        if (getPrefs(this).getString(SHARED_CATEGORIES_SAVED, null) == null) {
            new CategoriesInteractor(this, null).loadCategoriesDefault();
        }
    }

    public Node getCurrentNode() {
        String nodeSerialized = getPrefs(this).getString(SHARED_CURRENT_NODE, null);
        if (nodeSerialized != null) {
            return new Gson().fromJson(nodeSerialized, Node.class);
        } else {
            return null;
        }
    }

    public void setCurrentNode(Node node) {

        Node previousNode = getCurrentNode();

        String nodeSerialized = new Gson().toJson(node);
        getPrefs(this).edit()
                .putString(App.SHARED_CURRENT_NODE, nodeSerialized)
                .apply();

        if (previousNode != null && Objects.equals(previousNode.getShortname(), node.getShortname())) {
            return;
        }

        if (previousNode != null) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(previousNode.getShortname() + "_news");
            FirebaseMessaging.getInstance().unsubscribeFromTopic(previousNode.getShortname() + "_offers");
        }

        getPrefs(this).edit()
                .remove(App.SHARED_ENTITIES_CACHE)
                .remove(App.SHARED_CATEGORIES_SAVED)
                .commit();

        FirebaseMessaging.getInstance().subscribeToTopic(node.getShortname() + "_news");
        FirebaseMessaging.getInstance().subscribeToTopic(node.getShortname() + "_offers");
    }


    public static class MyObserver implements DefaultLifecycleObserver {
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
        getPrefs(context).edit().putString(App.SHARED_USER_DATA, dataSerial).apply();
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
                .apply();

    }


    public static boolean isEntity(Context context) {
        Data data = getUserData(context);
        return data != null && data.isEntity();
    }
}
