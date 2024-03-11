package net.mercadosocial.moneda.ui.auth.register_web;

import android.os.Bundle;
import android.view.View;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Node;

public class RegisterWebActivity extends BaseActivity implements View.OnClickListener {

    private View btnRegisterPerson;
    private View btnRegisterEntity;

    private void findViews() {
        btnRegisterPerson = findViewById(R.id.btn_register_person);
        btnRegisterEntity = findViewById(R.id.btn_register_entity);

        btnRegisterPerson.setOnClickListener(this);
        btnRegisterEntity.setOnClickListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_web);
        configureSecondLevelActivity();
        findViews();
    }

    @Override
    public void onClick(View v) {

        Node node = getApp().getCurrentNode();
        String urlRegister = "";
        int id = v.getId();
        if (id == R.id.btn_register_person) {
            urlRegister = node.getRegisterConsumerURL();
        } else if (id == R.id.btn_register_entity) {
            urlRegister = node.getRegisterProviderURL();
        }

        WebViewRegisterActivity.startRemoteUrl(this, getString(R.string.new_register), urlRegister);
        finish();
    }
}
