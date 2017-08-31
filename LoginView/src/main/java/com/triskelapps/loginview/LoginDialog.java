package com.triskelapps.loginview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;

import com.triskelapps.newpasswordview.R;


/**
 * Created by julio on 5/05/15.
 */
public class LoginDialog extends DialogFragment implements LoginView.OnPasswordCheckListener {

    private final String TAG = "LoginDialog";

    private LoginDialogListener mListener;
    private String mTitle;
    private LoginView viewLogin;
    private String mUsernameHintText;
    private String mAcceptButtonText;
    private String mMessage;
    private boolean avoidDismiss;

    public static LoginDialog newInstance() {

        LoginDialog dialog = new LoginDialog();
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return dialog;
    }

    public void configure(String title, LoginDialogListener listener) {

        mTitle = title;
        mListener = listener;
    }

    public void configure(String title, String usernameHintText, String acceptButtonText, String message, LoginDialogListener listener) {

        mTitle = title;
        mListener = listener;
        mUsernameHintText = usernameHintText;
        mAcceptButtonText = acceptButtonText;
        mMessage = message;
    }

    public void setAvoidDismiss(boolean avoidDismiss) {
        this.avoidDismiss = avoidDismiss;
    }


    public interface LoginDialogListener {
        public void onAccept(String username, String password);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final LoginView viewLogin = new LoginView(getActivity());
        viewLogin.setButtonCheckVisible(false);
        viewLogin.digestPassword("MD5");
        viewLogin.setOnPasswordCheckListener(this);
        viewLogin.setMinimumLenght(2);


        if (mUsernameHintText != null) {
            viewLogin.setUsernameHintText(mUsernameHintText);
        }

        if (mMessage != null) {
            viewLogin.setMessageFooter(mMessage);
        }


        String acceptButtonText = mAcceptButtonText != null ? mAcceptButtonText : getString(R.string.accept);

        final AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle(mTitle);
        ab.setView(viewLogin);
        ab.setPositiveButton(acceptButtonText, null);
        ab.setNegativeButton(R.string.cancel, null);

        final AlertDialog d = ab.create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {


                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        viewLogin.performButtonLoginClick();

//                        d.dismiss();
                    }
                });
            }
        });
        return d;

    }



    @Override
    public void onPasswordCorrect(String username, String password) {

        acceptAndDismiss(username, password);
    }

    @Override
    public void onPasswordError(String reason) {

        //Handled internally
    }

    private void acceptAndDismiss(String username,  String password) {

        WindowUtils.hideSoftKeyword(getDialog());

        if (mListener != null) {
            mListener.onAccept(username, password);
        }

        if (!avoidDismiss) {
            this.dismiss();
        }

    }

}
