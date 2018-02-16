package net.mercadosocial.moneda.ui.auth.register;

import android.os.Bundle;
import android.view.View;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, RegisterView {

    private View btnRegisterEntity;
    private View btnRegisterPerson;
    private RegisterPresenter presenter;
    private View btnRegister;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = RegisterPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegisterPerson = findViewById(R.id.btn_register_person);
        btnRegisterEntity = findViewById(R.id.btn_register_entity);
        btnRegister = findViewById(R.id.btn_register);

        btnRegisterPerson.setOnClickListener(this);
        btnRegisterEntity.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_person:
            case R.id.btn_register_entity:
                btnRegisterPerson.setSelected(v.getId() == R.id.btn_register_person);
                btnRegisterEntity.setSelected(v.getId() == R.id.btn_register_entity);
                btnRegister.setEnabled(true);
                break;

            case R.id.btn_register:
//                presenter.onRegisterButtonClick();
                break;
        }
    }


    // PRESENTER CALLBACKS
    @Override
    public void showRegisterPerson() {

    }

    @Override
    public void showRegisterEntity() {

    }
}
