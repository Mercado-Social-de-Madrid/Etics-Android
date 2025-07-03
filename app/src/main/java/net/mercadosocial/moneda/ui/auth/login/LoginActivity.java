package net.mercadosocial.moneda.ui.auth.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.mercadosocial.moneda.DebugHelper;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.ui.auth.register_web.RegisterWebActivity;
import net.mercadosocial.moneda.ui.main.MainActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginView {

    private EditText editUsername;
    private EditText editPassword;
    private View btnLogin;
    private LoginPresenter presenter;
    private View btnRememberPassword;
    private View btnRegister;

    private void findViews() {
        editUsername = (EditText)findViewById( R.id.edit_username );
        editPassword = (EditText)findViewById( R.id.edit_password );
        btnRememberPassword = findViewById(R.id.btn_remember_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);


        btnLogin.setOnClickListener(this);
        btnRememberPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = LoginPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        findViews();

        if (DebugHelper.MOCK_DATA) {
            editUsername.setText("juliotest");
            editPassword.setText("boniatos");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                presenter.onLoginClick(username, password);
                break;

            case R.id.btn_remember_password:
                String username2 = editUsername.getText().toString();
                presenter.onRememberPasswordClick(username2);
                break;

            case R.id.btn_register:
                startActivity(new Intent(this, RegisterWebActivity.class));
                finish();
                overridePendingTransition(0, 0);
                break;

        }
    }

}
