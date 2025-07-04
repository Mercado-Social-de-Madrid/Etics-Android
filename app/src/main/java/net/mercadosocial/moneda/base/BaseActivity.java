package net.mercadosocial.moneda.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Notification;
import net.mercadosocial.moneda.ui.novelties.detail.NoveltyDetailPresenter;
import net.mercadosocial.moneda.util.Util;
import net.mercadosocial.moneda.views.ProgressDialogMESOLD;
import net.mercadosocial.moneda.views.custom_dialog.ProgressDialogMES;

import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    public final String TAG = this.getClass().getSimpleName();
    private Activity activity;
    private ProgressDialog progressDialog;
    private String[] dataEntitiesSubscribed;
    private ProgressBar progressBar;
    private TextView tvTitleToolbar;
    public Toolbar toolbar;

    // I need another result code custom
    public static final int REQ_CODE_EDIT = 1;
    public static final int RESULT_DELETED = 1234;
    private AppBarLayout appBarLayout;
    private BasePresenter basePresenter;
    private Handler handlerDialog;
    private ProgressDialogMESOLD refreshingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        int colorBar = ContextCompat.getColor(this, R.color.colorAccent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            SystemBarStyle systemBarStyle = SystemBarStyle.dark(colorBar);
            EdgeToEdge.enable(this, systemBarStyle);
        }

        super.onCreate(savedInstanceState);

        if (getBasePresenter() != null) {
            getBasePresenter().setBaseView(this);
        }

    }


    BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            processNotification(intent.getExtras());
        }
    };


    public void processNotification(Bundle extras) {
        Notification notification = Notification.parseNotification(extras);
        if (notification == null) {
            FirebaseCrashlytics.getInstance().recordException(new IllegalArgumentException(
                    "Notification could not be parsed. Extras: " + Util.dumpIntentExtras(extras)));
            return;
        }

        if (notification.isFromOutside()) {
            NoveltyDetailPresenter.NOVELTY_TYPE noveltyType = Objects.equals(notification.getType(), Notification.TYPE_NEWS)
                    ? NoveltyDetailPresenter.NOVELTY_TYPE.NEWS
                    : NoveltyDetailPresenter.NOVELTY_TYPE.OFFER;
            startActivity(NoveltyDetailPresenter.newNoveltyDetailActivity(this, noveltyType, notification.getId()));
        } else {
            showNoveltyDialog(notification);
        }
    }


    private void showNoveltyDialog(final Notification notification) {

        String title = "";
        int positiveButtonStringId = R.string.see_more;

        switch(notification.getType()) {
            case Notification.TYPE_NEWS: {
                title = getString(R.string.mes_news);
                positiveButtonStringId = R.string.see_full_news;
                break;
            }
            case Notification.TYPE_OFFER: {
                String providerName = notification.getExtras().getString("proveedora");
                if (providerName != null) {
                    title = String.format(getString(R.string.new_offer_by), providerName);
                } else {
                    title = getString(R.string.new_offer);
                }
                positiveButtonStringId = R.string.see_full_offer;
                break;
            }

        }


        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(Html.fromHtml(String.format(getString(R.string.mes_news_message_format),
                        notification.getTitle(), notification.getMessage())))
                .setPositiveButton(positiveButtonStringId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoveltyDetailPresenter.NOVELTY_TYPE noveltyType = notification.getType().equals(Notification.TYPE_NEWS)
                                ? NoveltyDetailPresenter.NOVELTY_TYPE.NEWS
                                : NoveltyDetailPresenter.NOVELTY_TYPE.OFFER;

                        startActivity(NoveltyDetailPresenter.newNoveltyDetailActivity(BaseActivity.this, noveltyType, notification.getId()));
                    }
                })
                .setNeutralButton(R.string.close, null)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            int colorBar = ContextCompat.getColor(this, R.color.colorAccent);
            View root = findViewById(R.id.container);
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets bars = insets.getInsets(
                        WindowInsetsCompat.Type.systemBars()
                                | WindowInsetsCompat.Type.displayCutout()
                );
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                v.setBackgroundColor(colorBar);

                return WindowInsetsCompat.CONSUMED;
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(notificationReceiver, new IntentFilter(App.ACTION_NOTIFICATION_RECEIVED), Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(notificationReceiver, new IntentFilter(App.ACTION_NOTIFICATION_RECEIVED));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(notificationReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setRefreshing(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void setBasePresenter(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }

    public BasePresenter getBasePresenter() {
        return basePresenter;
    }

    public App getApp() {
        return (App) getApplicationContext();
    }

    // ------------ UI NOTIFICATIONS -----------

    public void refreshData() {
    }

    @Override
    public void onInvalidToken() {

        // Maybe an alertdialog?
//        toast(R.string.should_login_again);
        finish();
//        startActivity(LoginPresenter.newLoginActivity(this));
    }

    @Override
    public void toast(int stringResId) {
        toast(getString(stringResId));
    }

    @Override
    public void toast(final String mensaje) {

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

    }

    @Override
    public void alert(final String title, final String message) {

        AlertDialog.Builder ab = new AlertDialog.Builder(this);

        if (title != null) {
            ab.setTitle(title);
        }

        ab.setMessage(message);
        ab.setNegativeButton(R.string.back, null);
        ab.show();


    }

    @Override
    public void alert(final String message) {

        alert(getString(R.string.attention), message);
    }


    @Override
    public void showProgressDialog(String message) {

        Log.i(TAG, "showProgressDialog: ");
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
            progressDialog.show();
        } catch (Exception e) {
            // Nothing to do. Most probably activity was destroyed
        }
    }


    @Override
    public void hideProgressDialog() {

        Log.i(TAG, "hideProgressDialog: ");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {

        Log.i(TAG, "setRefreshing: refreshing = " + refreshing);

        if (refreshing) {
            ProgressDialogMES.getInstance(getSupportFragmentManager()).show();
        } else {
            ProgressDialogMES.getInstance(getSupportFragmentManager()).hide();
        }
    }

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    // ----CONFIGURATIONS --

    public void configureToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar == null) {
            return;
        }

        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        tvTitleToolbar = (TextView) findViewById(R.id.tv_title_toolbar);
        tvTitleToolbar.setVisibility(View.VISIBLE);

        tvTitleToolbar.setText(getSupportActionBar().getTitle());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);

    }

    protected void configureSecondLevelActivity() {

        configureToolbar();

        if (toolbar == null) {
            throw new IllegalStateException("No Toolbar in this second level activity");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setToolbarTitle(String title) {
        tvTitleToolbar.setText(title);
    }

    public void setToolbarTitle(int stringId) {
        tvTitleToolbar.setText(stringId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_EDIT) {
            if (resultCode == RESULT_DELETED) {
                finish();
                return;

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
