package net.mercadosocial.moneda.ui.auth.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginView {

    private EditText editUsername;
    private EditText editPassword;
    private View btnLogin;
    private LoginPresenter presenter;
    private View btnRememberPassword;

    private void findViews() {
        editUsername = (EditText)findViewById( R.id.edit_username );
        editPassword = (EditText)findViewById( R.id.edit_password );
        btnRememberPassword = findViewById(R.id.btn_remember_password);
        btnLogin = findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(this);
        btnRememberPassword.setOnClickListener(this);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = LoginPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();

        editUsername.setText("juliotest");
        editPassword.setText("boniatos");

        // todo
        btnRememberPassword.setVisibility(View.GONE);
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
        }
    }

}
