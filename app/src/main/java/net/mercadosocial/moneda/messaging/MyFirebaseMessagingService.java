package net.mercadosocial.moneda.messaging;

/**
 * Created by julio on 31/05/17.
 */


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.model.Notification;
import net.mercadosocial.moneda.model.User;
import net.mercadosocial.moneda.ui.main.MainActivity;
import net.mercadosocial.moneda.ui.novelties.detail.NoveltyDetailPresenter;
import net.mercadosocial.moneda.util.Util;

import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static final String KEY_ID_NOTIFICATION = "idNotification";

    public static String CHANNEL_ID = "channel_payments_notifs";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        App.getPrefs(getApplicationContext()).edit().putBoolean(App.SHARED_TOKEN_FIREBASE_SENT, false).commit();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        handleNow(remoteMessage);

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     *
     * @param remoteMessage
     */
    private void handleNow(RemoteMessage remoteMessage) {

        if (remoteMessage == null) {
            Toasty.error(getApplicationContext(), "remoteMessage null").show();
            return;
        }

        Map<String, String> extras = remoteMessage.getData();

//        String title;
//        String message;
//        if (remoteMessage.getNotification() != null) {
//            title = remoteMessage.getNotification().getTitle();
//            message = remoteMessage.getNotification().getBody();
//        } else {
//            title = extras.get("title");
//            message = extras.get("message");
//
////            title = "getNotification da null";
//        }


        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : extras.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }

        if (App.isInForeground) {
            Intent intent = new Intent(App.ACTION_NOTIFICATION_RECEIVED);
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else {
            createNotificationChannel();
            showCustomNotification(bundle);
        }



    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager.getNotificationChannels().contains(CHANNEL_ID)) {
                return;
            }


            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel.setDescription(description);
            channel.setImportance(importance);
            notificationManager.createNotificationChannel(channel);

        }
    }


    public void showCustomNotification(Bundle extras) {

        Notification notification = Notification.parseNotification(extras);
        if (notification == null) {
            Crashlytics.logException(new IllegalArgumentException(
                    "Notification could not be parsed. Extras: " + Util.dumpIntentExtras(extras)));
            return;
        }


        String title = extras.getString("title");
        String message = extras.getString("message");

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_mes_v2_144)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_mes_v2_144))
                .setContentTitle(title != null ? title : getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        int idNotification = (int) System.currentTimeMillis();

        extras.putInt(KEY_ID_NOTIFICATION, idNotification);

        Intent intent = null;
        Intent intentActionButton1 = null;
        Intent intentActionButton2 = null;

        switch (notification.getType()) {
            case Notification.TYPE_PAYMENT:
                intent = new Intent(this, MainActivity.class);

                intentActionButton1 = new Intent(OperationService.ACTION_ACCEPT_PAYMENT);
                intentActionButton1.putExtras(extras);
                PendingIntent pendingIntentActionButton1 = PendingIntent.getService(this, 0, intentActionButton1,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                notificationBuilder.addAction(R.mipmap.ic_accept, getString(R.string.accept), pendingIntentActionButton1);

                intentActionButton2 = new Intent(OperationService.ACTION_REJECT_PAYMENT);
                intentActionButton2.putExtras(extras);
                PendingIntent pendingIntentActionButton2 = PendingIntent.getService(this, 0, intentActionButton2,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                notificationBuilder.addAction(R.mipmap.ic_cancel, getString(R.string.reject), pendingIntentActionButton2);

                notificationBuilder.setContentTitle(getString(R.string.new_payment));

                Data userData = App.getUserData(this);
                float bonusPercent = notification.getUser_type() == User.TYPE_PERSON ?
                        userData.getEntity().getBonus_percent_general() :
                        userData.getEntity().getBonus_percent_entity();
                String bonus = Util.getDecimalFormatted(notification.getTotal_amount() * (bonusPercent / 100f), true);

                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Html.fromHtml(
                                String.format(getString(R.string.payment_received_message),
                                        notification.getSender(),
                                        Util.getDecimalFormatted(notification.getAmount(), false) + " " + getString(R.string.currency_name_plural),
                                        Util.getDecimalFormatted(notification.getTotal_amount(), false) + " €",
                                        bonus + " " + getString(R.string.currency_name_plural)))));


                break;

            case Notification.TYPE_TRANSACTION:
                intent = new Intent(this, MainActivity.class);
                notificationBuilder.setContentTitle(getString(R.string.income_received));

                String amountFormatted = Util.getDecimalFormatted(notification.getAmount(), false);

                String textHtml = null;
                if (notification.getIs_bonification()) {
                    textHtml = String.format(getString(R.string.bonification_received_message),
                            amountFormatted, getString(R.string.currency_name_plural));
                } else if (notification.getIs_euro_purchase()) {
                    textHtml = String.format(getString(R.string.income_received_buy_mes_format),
                            amountFormatted, getString(R.string.currency_name_plural));
                } else {
                    textHtml = String.format(getString(R.string.income_was_made_format),
                            amountFormatted, getString(R.string.currency_name_plural));
                }

                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Html.fromHtml(textHtml)));
                break;

            case Notification.TYPE_NEWS:
                intent = NoveltyDetailPresenter.newNoveltyDetailActivity(this, notification.getId());

                break;
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(extras);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

                notificationBuilder.setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(idNotification /* ID of notification */, notificationBuilder.build());

    }

}
