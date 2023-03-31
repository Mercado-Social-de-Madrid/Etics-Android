package net.mercadosocial.moneda.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.messaging.FirebaseMessaging;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.DeviceInteractor;
import net.mercadosocial.moneda.interactor.PaymentInteractor;
import net.mercadosocial.moneda.interactor.UserInteractor;
import net.mercadosocial.moneda.model.AuthLogin;
import net.mercadosocial.moneda.model.Device;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Notification;
import net.mercadosocial.moneda.model.Payment;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentPresenter;

import java.util.List;

/**
 * Created by julio on 2/02/18.
 */


 public class MainPresenter extends BasePresenter {

     private final MainView view;

     public static Intent newMainActivity(Context context) {

         Intent intent = new Intent(context, MainActivity.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         return intent;
     }

     public static MainPresenter newInstance(MainView view, Context context) {

         return new MainPresenter(view, context);

     }

     private MainPresenter(MainView view, Context context) {
         super(context, view);

         this.view = view;

     }

     public void onCreate(Intent intent) {

         if (intent.hasExtra("type")) {
             intent.putExtra(Notification.FIELD_FROM_OUTSIDE, true);
             ((BaseActivity)context).processNotification(intent);
             intent.getExtras().clear();
         }

         checkIntentUriReceived(intent);

         updateProfileStatus();

     }


    public void onResume() {

         refreshData();
        checkTokenFirebaseSent();
     }

     public void refreshData() {

         Data data = App.getUserData(context);
         view.showUserData(data);

         if (data != null) {
             refreshPendingPayments();
         }
     }

    private void updateProfileStatus() {
        Data data = App.getUserData(context);
        if (data != null) {
            UserInteractor userInteractor = new UserInteractor(context, null);
            if (data.isEntity()) {
                userInteractor.getEntityProfile(new BaseInteractor.BaseApiCallback<Entity>() {
                    @Override
                    public void onResponse(Entity entity) {
                        data.setEntity(entity);
                        App.saveUserData(context, data);
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
                        App.saveUserData(context, data);
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        }
    }

    private void checkIntentUriReceived(Intent intent) {

        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            String url = appLinkData.toString();
            if (url.contains(App.URL_QR_ENTITY)) {
                if (App.getUserData(context) != null) {
                    context.startActivity(NewPaymentPresenter.newNewPaymentActivityWithUrl(context, url));
                } else {
                    showNotLoggedDialog();
                }
            } else {
                showWebLinkDialog(url);
            }
        }
    }

    private void showNotLoggedDialog() {
        new AlertDialog.Builder(context)
                .setMessage(R.string.not_logged_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.understood, null)
                .show();
    }

    private void showWebLinkDialog(String url) {
        new AlertDialog.Builder(context)
                .setMessage(String.format(context.getString(R.string.web_link_dialog_message), url))
                .setCancelable(false)
                .setPositiveButton(R.string.open_link, (dialog, which) -> context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void refreshPendingPayments() {
        new PaymentInteractor(context, view).getPendingPayments(new BaseInteractor.BaseApiGETListCallback<Payment>() {
            @Override
            public void onResponse(List<Payment> list) {
                if (list != null) {
                    view.showPendingPaymentsNumber(list.size());
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void onLogoutClick() {
        App.removeUserData(context);
        view.showUserData(null);
        view.showPendingPaymentsNumber(0);
    }

    private void checkTokenFirebaseSent() {
        if (!getPrefs().getBoolean(App.SHARED_TOKEN_FIREBASE_SENT, false)
                && AuthLogin.API_KEY != null) {
            sendDevice();
        }
    }

    private void sendDevice() {

        String model = Build.MANUFACTURER + " " + Build.MODEL;
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {
                Log.w(TAG, "getInstanceId failed", task.getException());
                return;
            }

            Device device = new Device(model, task.getResult());
            new DeviceInteractor(context,  view).sendDevice(device, new BaseInteractor.BaseApiPOSTCallback() {
                @Override
                public void onSuccess(Integer id) {
                    getPrefs().edit().putBoolean(App.SHARED_TOKEN_FIREBASE_SENT, true).commit();
                }

                @Override
                public void onError(String message) {
                    Log.e(TAG, "onError: error sending device token");
                }
            });

        });

    }
}
