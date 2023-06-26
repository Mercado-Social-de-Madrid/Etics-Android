package net.mercadosocial.moneda.util.update_app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import net.mercadosocial.moneda.BuildConfig;

import java.util.concurrent.TimeUnit;

public class UpdateAppManager {

    Context context;
    private AppUpdateManager appUpdateManager;
    private AppUpdateInfo appUpdateInfo;
    private UpdateAvailableListener updateAvailableListener;
    private UpdateFinishListener updateFinishListener;

    public static final String TEMPLATE_URL_GOOGLE_PLAY_APP_HTTP = "https://play.google.com/store/apps/details?id=%s";
    public static final String TEMPLATE_URL_GOOGLE_PLAY_APP_DIRECT = "market://details?id=%s";

    public UpdateAppManager(Context context) {
        this.context = context;

        appUpdateManager = AppUpdateManagerFactory.create(context);
    }

    public static void scheduleAppUpdateCheckWork(Context context) {

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest updateAppCheckWork = new PeriodicWorkRequest.Builder(UpdateAppCheckWorker.class,
                8, TimeUnit.HOURS,60, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("appUpdateCheckWork",
                ExistingPeriodicWorkPolicy.KEEP, updateAppCheckWork);

    }

    public void setUpdateAvailableListener(UpdateAvailableListener updateAvailableListener) {
        this.updateAvailableListener = updateAvailableListener;
    }

    public void setFinishListener(UpdateFinishListener updateFinishListener) {
        this.updateFinishListener = updateFinishListener;
    }

    public void checkUpdateAvailable() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        appUpdateInfo = task.getResult();
                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                            if (updateAvailableListener != null) {
                                updateAvailableListener.onUpdateAvailable();
                            }
                        }

                    } else {
                        Exception e = task.getException();
//                        CountlyUtil.recordHandledException(e);
                        e.printStackTrace();

                    }

                    if (updateFinishListener != null) {
                        updateFinishListener.onCheckFinish();
                    }

                });
    }

    public void onUpdateVersionClick() {

        if (appUpdateInfo == null) {
            return;
        }

        if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
            AppUpdateOptions options = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                    .setAllowAssetPackDeletion(true).build();

            if (context instanceof Activity) {
                appUpdateManager.startUpdateFlow(appUpdateInfo, (Activity) context, options);
            }
        } else {
            openGooglePlay();
        }

    }

    private void openGooglePlay() {

        String packageName = BuildConfig.APPLICATION_ID.replace(".debug", "");
        String httpUrl = String.format(TEMPLATE_URL_GOOGLE_PLAY_APP_HTTP, packageName);
        String directUrl = String.format(TEMPLATE_URL_GOOGLE_PLAY_APP_DIRECT, packageName);

        Intent directPlayIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(directUrl));
        try {
            context.startActivity(directPlayIntent);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl)));
        }
    }

    public void onResume() {

        if (appUpdateInfo != null && appUpdateInfo.updateAvailability()
                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
            AppUpdateOptions options = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                    .setAllowAssetPackDeletion(true).build();

            if (context instanceof Activity) {
                appUpdateManager.startUpdateFlow(appUpdateInfo, (Activity) context, options);
            }

        }
    }

    public interface UpdateAvailableListener {
        void onUpdateAvailable();
    }

    public interface UpdateFinishListener {
        void onCheckFinish();
    }
}
