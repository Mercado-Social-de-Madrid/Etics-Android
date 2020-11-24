package net.mercadosocial.moneda.ui.auth.login;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.DebugHelper;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.AuthInteractor;
import net.mercadosocial.moneda.interactor.DeviceInteractor;
import net.mercadosocial.moneda.model.AuthLogin;
import net.mercadosocial.moneda.ui.main.MainPresenter;

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

    public void onLoginClick(final String username, String password) {

        view.setRefreshing(true);

        AuthLogin authLogin = new AuthLogin(username, password);
        authInteractor.login(authLogin, new AuthInteractor.LoginCallback() {
            @Override
            public void onResponse(Data data) {
                data.setUsername(username);
                App.saveUserData(context, data);
                Toasty.success(context, context.getString(R.string.welcome)).show();
                context.startActivity(MainPresenter.newMainActivity(context));
            }

            @Override
            public void onError(String error) {
                if (error.equals("incorrect")) {
                    error = context.getString(R.string.access_data_incorrect);
                }
                Toasty.error(context, error).show();
            }
        });
    }


    public void onRememberPasswordClick(String username) {

        if (DebugHelper.SWITCH_TEST_FUNCTION) {
            openEmailApp();
            return;
        }

        final EditText editText = new EditText(context);
        editText.setHint(R.string.email);

        new AlertDialog.Builder(context)
                .setTitle(R.string.remember_password)
                .setView(editText)
                .setPositiveButton(R.string.send_to_email, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String email = editText.getText().toString();
                        resetPassword(email);

                    }
                })
                .setNeutralButton(R.string.cancel, null)
                .show();
    }

    private void resetPassword(String email) {
        view.setRefreshing(true);
        authInteractor.resetPassword(email, new BaseInteractor.BaseApiCallback<Data>() {
            @Override
            public void onResponse(Data responseBody) {
                showResetPasswordSuccessDialog();
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message).show();
            }
        });
    }

    private void showResetPasswordSuccessDialog() {

        new AlertDialog.Builder(context)
                .setTitle(R.string.remember_password)
                .setMessage(R.string.remember_password_success_message)
                .setPositiveButton(R.string.go_to_email, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        openEmailApp();

                    }
                })
                .setNeutralButton(R.string.back, null)
                .show();
    }

    private void openEmailApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
