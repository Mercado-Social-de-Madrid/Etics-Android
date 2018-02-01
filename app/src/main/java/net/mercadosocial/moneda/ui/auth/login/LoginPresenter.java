package net.mercadosocial.moneda.ui.auth.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.google.firebase.iid.FirebaseInstanceId;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.AuthInteractor;
import net.mercadosocial.moneda.interactor.DeviceInteractor;
import net.mercadosocial.moneda.model.AuthLogin;
import net.mercadosocial.moneda.model.Device;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 1/02/18.
 */


 public class LoginPresenter extends BasePresenter {

     private final LoginView view;
    private final AuthInteractor authInteractor;
    private final DeviceInteractor deviceInteractor;

    public static Intent newLoginActivity(Context context) {

         Intent intent = new Intent(context, LoginActivity.class);

         return intent;
     }

     public static LoginPresenter newInstance(LoginView view, Context context) {

         return new LoginPresenter(view, context);

     }

     private LoginPresenter(LoginView view, Context context) {
         super(context, view);

         this.view = view;
         authInteractor = new AuthInteractor(context, view);
         deviceInteractor = new DeviceInteractor(context, view);

     }

     public void onCreate() {

     }

     public void onResume() {

         refreshData();
     }

     public void refreshData() {


     }

    public void onLoginClick(String username, String password) {

        view.showProgressDialog(context.getString(R.string.loading));

        AuthLogin authLogin = new AuthLogin(username, password);
        authInteractor.login(authLogin, new AuthInteractor.LoginCallback() {
            @Override
            public void onResponse(Data data) {
                App.saveUserData(context, data);

                sendDevice();
            }

            @Override
            public void onError(String error) {

                view.hideProgressDialog();
                Toasty.error(context, error).show();
            }
        });
    }

    private void sendDevice() {

        String model = Build.MANUFACTURER + " " + Build.MODEL;
        Device device = new Device(model, FirebaseInstanceId.getInstance().getToken());
        deviceInteractor.sendDevice(device, new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                Toasty.success(context, context.getString(R.string.welcome)).show();
                ((Activity)context).finish();
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message).show();
            }
        });
    }

    public void onRememberPasswordClick(String username2) {
        //todo
    }
}
