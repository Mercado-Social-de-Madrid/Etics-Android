package net.mercadosocial.moneda.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import net.mercadosocial.moneda.BuildConfig;
import net.mercadosocial.moneda.R;

public class UpdateAppView extends FrameLayout implements View.OnClickListener {

    private static final String TAG = "UpdateAppView";

    public static final String TEMPLATE_URL_GOOGLE_PLAY_APP_HTTP = "https://play.google.com/store/apps/details?id=%s";
    public static final String TEMPLATE_URL_GOOGLE_PLAY_APP_DIRECT = "market://details?id=%s";

    private TextView btnUpdateApp;
    private AppCompatImageView btnCloseUpdateAppView;
    private String httpUrl;
    private String directUrl;
    private String remoteConfigVariableName;

    public UpdateAppView(Context context) {
        super(context);
        init();
    }


    public UpdateAppView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UpdateAppView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void findViews(View layout) {
        btnUpdateApp = (TextView)layout.findViewById( R.id.btn_update_app );
        btnCloseUpdateAppView = (AppCompatImageView)layout.findViewById( R.id.btn_close_update_app_view );

        btnUpdateApp.setOnClickListener(this);
        btnCloseUpdateAppView.setOnClickListener(this);
    }

    private void init() {
        View layout = View.inflate(getContext(), R.layout.view_update_app, null);
        findViews(layout);

        addView(layout);

        setVisibility(GONE);

        configure();
    }

    private void configure() {

        String packageName = getContext().getPackageName().replace(".debug", "");
        this.httpUrl = String.format(TEMPLATE_URL_GOOGLE_PLAY_APP_HTTP, packageName);
        this.directUrl = String.format(TEMPLATE_URL_GOOGLE_PLAY_APP_DIRECT, packageName);
        this.remoteConfigVariableName = getContext().getString(R.string.firebase_remote_config_market_version_var_name);

        checkNewVersionInMarket();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_close_update_app_view:
                setVisibility(GONE);
                break;

            case R.id.btn_update_app:
                if (httpUrl == null) {
                    throw new IllegalStateException("View not configured");
                }

                onUpdateVersionClick();
                break;
        }

    }


    public void onUpdateVersionClick() {

//        Intent directPlayIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(directUrl));
//        if (directPlayIntent.resolveActivity(getContext().getPackageManager()) != null) {
//            getContext().startActivity(directPlayIntent);
//        } else {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl)));
//        }
    }


    private void checkNewVersionInMarket() {

        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        long cacheExpiration = 3600 * 2; // 12 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (BuildConfig.DEBUG) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
        // will use fetch data from the Remote Config service, rather than cached parameter values,
        // if cached parameter values are more than cacheExpiration seconds old.
        // See Best Practices in the README for more information.
        firebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener((Activity) getContext(), task -> {
                    if (task.isSuccessful()) {
                        String lastVersionMarketAndroid = FirebaseRemoteConfig.getInstance().getString(remoteConfigVariableName);
                        try {
                            int marketCode = Integer.parseInt(lastVersionMarketAndroid);
                            int localCode = BuildConfig.VERSION_CODE;
                            if (marketCode > localCode) {
                                setVisibility(VISIBLE);
                            }
                        } catch (Exception e) {
                            Crashlytics.logException(new Error("Wrong last_version_market_variable: " + lastVersionMarketAndroid + ", remote variable name: "+ remoteConfigVariableName));
                        }

                        firebaseRemoteConfig.activate();
                    } else {
                        Log.e(TAG, "onComplete: remote config task failed", task.getException());
                    }
                });

    }
}
