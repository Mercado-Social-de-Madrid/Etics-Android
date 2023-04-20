package net.mercadosocial.moneda.ui.auth.register_web;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.common.ApiClient;
import net.mercadosocial.moneda.base.BaseActivity;

import es.dmoral.toasty.Toasty;

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

        String path = "";
        switch (v.getId()) {
            case R.id.btn_register_person:
                path = "accounts/signup/consumer/?from_app=true"; // to remove web headers
                break;

            case R.id.btn_register_entity:
                path = "accounts/signup/provider/?from_app=true"; // to remove web headers
                break;

        }

        String url = ApiClient.BASE_URL_TOOL + path;
        WebViewRegisterActivity.startRemoteUrl(this, getString(R.string.new_register), url);
        finish();
    }
}
