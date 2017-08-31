package com.triskelapps.loginview;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.triskelapps.newpasswordview.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by julio on 26/08/15.
 */
public class LoginView extends LinearLayout implements View.OnClickListener, TextView.OnEditorActionListener {

    private EditText mEditUsername, mEditPassword;
    private Button mButtonLogin;
    private int minimumLenght = 6;
    private OnPasswordCheckListener onPasswordCheckListener;
    private String digestAlgorithm;
    private TextView mTvMessage;

    public LoginView(Context context) {
        this(context, null);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    private void setUp() {

        View layout = View.inflate(getContext(), R.layout.view_login, this);
        mEditUsername = (EditText) layout.findViewById(R.id.edit_username);
        mEditPassword = (EditText) layout.findViewById(R.id.edit_password);
        mTvMessage = (TextView) layout.findViewById(R.id.tv_message);
        mButtonLogin = (Button) layout.findViewById(R.id.btn_login);

        mButtonLogin.setOnClickListener(this);

        mEditPassword.setOnEditorActionListener(this);

        mEditUsername.requestFocus();
    }

    // --- CONFIGURATIONS ---
    public void performButtonLoginClick() {
        mButtonLogin.performClick();
    }

    public void setButtonCheckVisible(boolean visible) {
        mButtonLogin.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * Set the minimum lenght of the password. By default 6
     * @param lenght
     */
    public void setMinimumLenght(int lenght) {
        this.minimumLenght = lenght;
    }

    public void digestPassword(String algorithm) throws IllegalArgumentException {
        this.digestAlgorithm = algorithm;

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Algorithm not supported: " + algorithm);
        }
    }

    // --- USER ACTIONS ---
    @Override
    public void onClick(View v) {

        if (v == mButtonLogin) {
            checkPassword();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            mButtonLogin.performClick();
            return true;
        }
        return false;
    }


    // --- LOGIC ---
    private void checkPassword() {


        String username = mEditUsername.getText().toString().trim();
        String pass = mEditPassword.getText().toString().trim();

        if (pass.length() < minimumLenght) {

            String error = String.format(getContext().getString(R.string.at_least_x_characters), minimumLenght);

            mEditPassword.setError(error);

            if (onPasswordCheckListener != null) {
                onPasswordCheckListener.onPasswordError(error);
            }

            return;
        }


        if (onPasswordCheckListener != null) {

            if (digestAlgorithm != null) {
                try {
                    pass = DigestUtils.hashString(pass, digestAlgorithm);
                } catch (NoSuchAlgorithmException e) {
                    // This will never enter here, checked before
                }
            }

            onPasswordCheckListener.onPasswordCorrect(username, pass);
        }
    }

    public void setUsernameHintText(String usernameHintText) {
        mEditUsername.setHint(usernameHintText);
    }

    public void setMessageFooter(String messageFooter) {

        mTvMessage.setLinksClickable(true);
        mTvMessage.setMovementMethod(LinkMovementMethod.getInstance());

        mTvMessage.setText(Html.fromHtml(messageFooter));
    }


    // --- CALLBACK ---
    public interface OnPasswordCheckListener {

        /**
         * Called if password have minimum lenght and both matches
         * @param password The password string user have introduced. If an encryption method was configured, this is the hashed password
         */
        public void onPasswordCorrect(String username, String password);

        /**
         * View holds internally error messages, but it can be interesting to handle externally as well :)
         * @param reason 2 errors possible: "At least x characters" and "Password does not match"
         */
        public void onPasswordError(String reason);
    }

    public void setOnPasswordCheckListener(OnPasswordCheckListener listener) {
        this.onPasswordCheckListener = listener;
    }
}
