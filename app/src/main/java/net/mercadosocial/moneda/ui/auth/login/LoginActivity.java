package net.mercadosocial.moneda.ui.auth.login;

import android.os.Bundle;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BasePresenter;

public class LoginActivity extends BaseActivity {

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

}
