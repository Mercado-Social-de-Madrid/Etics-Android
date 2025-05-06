package net.mercadosocial.moneda.ui.auth.register_web;

import android.os.Bundle;
import android.view.View;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Node;
import net.mercadosocial.moneda.util.WebUtils;

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

        setToolbarTitle(R.string.new_register);
        findViews();

        Node node = getApp().getCurrentNode();

        btnRegisterPerson.setVisibility(WebUtils.isValidLink(node.getRegisterConsumerURL()) ? View.VISIBLE : View.GONE);
        btnRegisterEntity.setVisibility(WebUtils.isValidLink(node.getRegisterProviderURL()) ? View.VISIBLE : View.GONE);
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

        WebUtils.openLink(this, urlRegister);
        finish();
    }
}
