package net.mercadosocial.moneda;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.ui.entities.EntitiesFragment;
import net.mercadosocial.moneda.ui.wallet.WalletFragment;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureToolbar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        getFragmentManager().beginTransaction().replace(R.id.content, new EntitiesFragment()).commit();
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_entities:
                getFragmentManager().beginTransaction().replace(R.id.content, new EntitiesFragment()).commit();
                return true;
            case R.id.navigation_wallet:
                getFragmentManager().beginTransaction().replace(R.id.content, new WalletFragment()).commit();
                return true;
            case R.id.navigation_profile:
                toast("perfil");
                return true;
        }
        return false;
    }


}
