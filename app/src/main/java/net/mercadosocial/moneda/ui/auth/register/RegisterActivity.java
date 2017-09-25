package net.mercadosocial.moneda.ui.auth.register;

import android.os.Bundle;
import android.view.View;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BasePresenter;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private View btnRegisterEntity;
    private View btnRegisterPerson;

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegisterPerson = findViewById(R.id.btn_register_person);
        btnRegisterEntity = findViewById(R.id.btn_register_entity);

        btnRegisterPerson.setOnClickListener(this);
        btnRegisterEntity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_person:
            case R.id.btn_register_entity:
                btnRegisterPerson.setSelected(v.getId() == R.id.btn_register_person);
                btnRegisterEntity.setSelected(v.getId() == R.id.btn_register_entity);
                break;
        }
    }
}
