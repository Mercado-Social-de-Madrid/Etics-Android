package net.mercadosocial.moneda.messaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.interactor.PaymentInteractor;
import net.mercadosocial.moneda.model.Notification;

import es.dmoral.toasty.Toasty;

import static net.mercadosocial.moneda.messaging.MyFirebaseMessagingService.KEY_ID_NOTIFICATION;


public class OperationService extends IntentService {

    private static final String TAG = "OperationService";

    public static final String ACTION_ACCEPT_PAYMENT = "net.mercadosocial.moneda.ACTION_ACCEPT_PAYMENT";
    public static final String ACTION_REJECT_PAYMENT = "net.mercadosocial.moneda.ACTION_REJECT_PAYMENT";
    private NotificationManager notificationManager;
    private PaymentInteractor paymentInteractor;

    public OperationService() {
        super("no_name");
    }

    public OperationService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate: ");

       notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        paymentInteractor = new PaymentInteractor(this, null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.i(TAG, "onHandleIntent: ");

        Bundle extras = intent.getExtras();
        Notification notification = Notification.parseNotification(extras);
        int idNotification = extras.getInt(KEY_ID_NOTIFICATION);

        showProgressNotification(notification, idNotification);

        switch (intent.getAction()) {
            case ACTION_ACCEPT_PAYMENT:
                acceptPayment(notification, idNotification);
                break;

            case ACTION_REJECT_PAYMENT:
                rejectPayment(notification, idNotification);
                break;

        }

    }

    private void showProgressNotification(Notification notification, int idNotification) {

        String title = notification.getTitle();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, MyFirebaseMessagingService.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_mes_v2_144)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_mes_v2_144))
                .setContentTitle(title != null ? title : getString(R.string.app_name))
                .setContentText(getString(R.string.processing))
//                .setAutoCancel(true)
                .setProgress(1, 1, true);

        notificationManager.notify(idNotification /* ID of notification */, notificationBuilder.build());

    }


    private void acceptPayment(Notification notification, final int idNotification) {

        paymentInteractor.acceptPayment(notification.getId(), new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                Toasty.success(OperationService.this, "OK").show();
                notificationManager.cancel(idNotification);
            }

            @Override
            public void onError(String message) {
                Toasty.error(OperationService.this, message).show();
                notificationManager.cancel(idNotification);
            }
        });
    }

    private void rejectPayment(Notification notification, final int idNotification) {

        paymentInteractor.cancelPayment(notification.getId(), new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                Toasty.success(OperationService.this, "OK").show();
                notificationManager.cancel(idNotification);
            }

            @Override
            public void onError(String message) {
                Toasty.error(OperationService.this, message).show();
                notificationManager.cancel(idNotification);
            }
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

}
