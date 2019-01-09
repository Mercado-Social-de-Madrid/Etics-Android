package net.mercadosocial.moneda.ui.profile;

import android.os.Bundle;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;

public class ProfileActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        configureSecondLevelActivity();


    }
}
