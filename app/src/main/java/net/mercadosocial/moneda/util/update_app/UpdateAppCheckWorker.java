package net.mercadosocial.moneda.util.update_app;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import com.google.common.util.concurrent.ListenableFuture;

import net.mercadosocial.moneda.BuildConfig;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.ui.main.MainActivity;


@SuppressLint("RestrictedApi")
public class UpdateAppCheckWorker extends ListenableWorker {


    private static final String TAG = UpdateAppCheckWorker.class.getSimpleName();
    private static final String PREF_LAST_VERSION_NOTIFIED = "pref_last_version_notified";
    private SettableFuture<Result> future;
    private UpdateAppManager updateAppManager;

    private static final String CHANNEL_NOTIF_UPDATE_APP = "channel_update_app";
    private static final int ID_UPDATE_APP_NOTIF = 1000;
    private SharedPreferences prefs;

    public UpdateAppCheckWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {

        Log.i(TAG, "startWork");

        future = SettableFuture.create();

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int lastVersionNotified = prefs.getInt(PREF_LAST_VERSION_NOTIFIED, 0);
        if (lastVersionNotified < BuildConfig.VERSION_CODE) {
            checkAppUpdateAvailable();
        } else {
            future.set(Result.success());
        }

        return future;
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "onStopped");
    }

    public void checkAppUpdateAvailable() {

        updateAppManager = new UpdateAppManager(getApplicationContext());
        updateAppManager.setUpdateAvailableListener(() -> prepareAndShowNotification());
        updateAppManager.setFinishListener(() -> future.set(Result.success()));
        updateAppManager.checkUpdateAvailable();


    }

    private void prepareAndShowNotification() {
        initializeOreoChannelsNotification();
        showNotification();
        prefs.edit().putInt(PREF_LAST_VERSION_NOTIFIED, BuildConfig.VERSION_CODE).commit();
    }


    public NotificationCompat.Builder getBaseBuilder(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context, CHANNEL_NOTIF_UPDATE_APP)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_mes_v2_144)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentIntent(pendingIntent);
        return notifBuilder;
    }


    public void showNotification() {
        NotificationCompat.Builder notifBuilder = getBaseBuilder(getApplicationContext());
        notifBuilder.setContentTitle(getApplicationContext().getString(R.string.update_available));
        notifBuilder.setContentText(getApplicationContext().getString(R.string.press_to_update));

        Notification notification = notifBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(ID_UPDATE_APP_NOTIF, notification);
    }

    public void initializeOreoChannelsNotification() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createChannel(CHANNEL_NOTIF_UPDATE_APP,
                    R.string.channel_notif_update_app_name,
                    R.string.channel_notif_update_app_description);

        }

    }

    @SuppressLint("NewApi")
    private void createChannel(String channelId, int nameStringId, int descriptionStringId) {

        NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);

        if (notificationManager.getNotificationChannel(channelId) == null) {
            CharSequence name = getApplicationContext().getString(nameStringId);
            String description = getApplicationContext().getString(descriptionStringId);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
    }

}
