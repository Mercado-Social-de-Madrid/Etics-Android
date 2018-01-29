package net.mercadosocial.moneda.messaging;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by julio on 31/05/17.
 * <p>
 * QUICK START MESSAGING: https://github.com/firebase/quickstart-android/tree/master/messaging
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseTokenService";

    @Override
    public void onTokenRefresh() {
//        super.onTokenRefresh();

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String refreshedToken) {

//        int idAccount = App.getAccountId(this);
//        if (idAccount == -1) {
//            return;
//        }
//
//        FirebasePush firebasePush = new FirebasePush(idAccount, refreshedToken);
//        new FirebasePushInteractor(this, null).updateFirebasePush(idAccount, firebasePush, new BaseInteractor.BaseApiCallback() {
//
//            @Override
//            public void onSuccess() {
//                Log.i(TAG, "onSuccess: Register Token Firebase OK");
//            }
//
//            @Override
//            public void onError(String message) {
//                Log.e(TAG, "onError: Register Token Firebase failed. Message: " + message);
//            }
//        });

    }

    private String getUniqueDeviceId() {
        TelephonyManager telephonyManager;

        telephonyManager = (TelephonyManager) getSystemService(Context.
                TELEPHONY_SERVICE);

/*
* getDeviceId() returns the unique device ID.
* For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
*/
        @SuppressLint({"HardwareIds", "MissingPermission"}) String deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }
}
