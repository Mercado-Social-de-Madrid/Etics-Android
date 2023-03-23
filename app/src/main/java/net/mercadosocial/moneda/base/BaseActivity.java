package net.mercadosocial.moneda.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Notification;
import net.mercadosocial.moneda.ui.novelties.detail.NoveltyDetailPresenter;
import net.mercadosocial.moneda.ui.transactions.TransactionsPresenter;
import net.mercadosocial.moneda.util.Util;
import net.mercadosocial.moneda.views.ProgressDialogMESOLD;
import net.mercadosocial.moneda.views.custom_dialog.BonusDialog;
import net.mercadosocial.moneda.views.custom_dialog.NewPaymentDialog;
import net.mercadosocial.moneda.views.custom_dialog.OnCloseListener;
import net.mercadosocial.moneda.views.custom_dialog.ProgressDialogMES;

import es.dmoral.toasty.Toasty;


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
        super.onCreate(savedInstanceState);

        if (getBasePresenter() != null) {
            getBasePresenter().setBaseView(this);
        }

    }

    BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            processNotification(intent);
        }
    };


    public void processNotification(Intent intent) {

        Notification notification = Notification.parseNotification(intent.getExtras());
        if (notification == null) {
            FirebaseCrashlytics.getInstance().recordException(new IllegalArgumentException(
                    "Notification could not be parsed. Extras: " + Util.dumpIntentExtras(intent.getExtras())));
            return;
        }


        switch (notification.getType()) {
            case Notification.TYPE_PAYMENT:
                NewPaymentDialog.newInstance(notification)
                        .setOnCloseListener(() -> refreshData()).show(getSupportFragmentManager(), null);
                break;

            case Notification.TYPE_TRANSACTION:
                String amountFormatted = Util.getDecimalFormatted(notification.getAmount(), true);
                if (notification.getIs_bonification()) {
                    BonusDialog bonusDialog = BonusDialog.newInstance(notification);
                    bonusDialog.setOnDismissOrCancelListener(() -> refreshData());
                    bonusDialog.show(getSupportFragmentManager(), null);
//                    showBonificationDialog(amountFormatted);
                } else {
                    Toasty.info(this, String.format(getString(R.string.income_received_format), amountFormatted)).show();
                }
                break;

            case Notification.TYPE_NEWS:
                if (notification.isFromOutside()) {
                    startActivity(NoveltyDetailPresenter.newNoveltyDetailActivity(this, notification.getId()));
                } else {
                    showNewsDialog(notification);
                }
                break;
        }

    }

    public void showBonificationDialog(String amountFormatted) {

        View layout = getLayoutInflater().inflate(R.layout.view_dialog_bonus, null);

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(R.string.congratulations);
//        ab.setIcon(R.mipmap.img_happy_face);
        ab.setMessage(Html.fromHtml(String.format(getString(R.string.bonification_received_message),
                amountFormatted, getString(R.string.currency_name_plural))));
        ab.setPositiveButton(R.string.go_to_transactions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(TransactionsPresenter.newTransactionsActivity(BaseActivity.this));
            }
        });
        ab.setNeutralButton(R.string.close, null);
        Dialog dialog = ab.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                refreshData();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                refreshData();
            }
        });
    }


    private void showNewsDialog(final Notification notification) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.mes_news)
                .setMessage(Html.fromHtml(String.format(getString(R.string.mes_news_message_format),
                        notification.getTitle(), notification.getMessage())))
                .setPositiveButton(R.string.see_full_news, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(NoveltyDetailPresenter.newNoveltyDetailActivity(BaseActivity.this, notification.getId()));
                    }
                })
                .setNeutralButton(R.string.close, null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(notificationReceiver, new IntentFilter(App.ACTION_NOTIFICATION_RECEIVED));
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
    public void setRefreshing(boolean refresing) {

        Log.i(TAG, "setRefreshing: refreshing = " + refresing);

        if (refresing) {
            ProgressDialogMES.getInstance(getSupportFragmentManager()).show();
        } else {
            ProgressDialogMES.getInstance(getSupportFragmentManager()).hide();
        }

//        try {
//
//            if (refresing) {
//                if (handlerDialog == null) {
//                    handlerDialog = new Handler();
//                    Log.i(TAG, "setRefreshing: newHandler()");
//                    handlerDialog.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            Log.i(TAG, "setRefreshing: enter handler post");
//                            if (refreshingDialog == null) {
//                                Log.i(TAG, "setRefreshing: showing dialog");
//                                refreshingDialog = ProgressDialogMESOLD.newInstance();
//                                refreshingDialog.show(getSupportFragmentManager(), null);
//                            }
//                        }
//                    }, ProgressDialogMES.MIN_DELAY);
//                }
//            } else {
//                if (handlerDialog != null) {
//                    Log.i(TAG, "setRefreshing: removing Callbackds handler");
//                    handlerDialog.removeCallbacksAndMessages(null);
//                    handlerDialog = null;
//                }
//                if (refreshingDialog != null) {
//                    Log.i(TAG, "setRefreshing: dismissTimeSafe");
//                    refreshingDialog.dismissTimeSafe();
//                    refreshingDialog = null;
//                }
//            }
//        } catch (Exception e) {
//            Log.i(TAG, "setRefreshing: exception", e);
//            handlerDialog = null;
//            refreshingDialog = null;
//        }


//        if (progressBar != null) {
//            progressBar.setVisibility(refresing ? View.VISIBLE : View.INVISIBLE);
////            progressBar.setIndeterminate(refresing);
//        }
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
//        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white);

//        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        upArrow.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
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
