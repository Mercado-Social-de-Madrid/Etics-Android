package net.mercadosocial.moneda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.DebugHelper;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.ui.auth.login.LoginActivity;
import net.mercadosocial.moneda.ui.auth.register.RegisterActivity;
import net.mercadosocial.moneda.ui.entities.EntitiesFragment;
import net.mercadosocial.moneda.ui.entities.EntitiesMapFragment;
import net.mercadosocial.moneda.ui.intro.IntroActivity;
import net.mercadosocial.moneda.ui.novelties.NoveltiesFragment;
import net.mercadosocial.moneda.ui.wallet.WalletFragment;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private MenuItem menuItemMapList;
    private boolean showingMap;
    private TextView btnLogin;
    private TextView btnSignup;
    private BottomNavigationView bottomNavView;

    private void findViews() {


        bottomNavView = (BottomNavigationView) findViewById(R.id.navigation_bottom_view);
        bottomNavView.setOnNavigationItemSelectedListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnLogin = (TextView) navigationView.getHeaderView(0).findViewById(R.id.btn_login);
        btnSignup = (TextView) navigationView.getHeaderView(0).findViewById(R.id.btn_singup);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        configureToolbar();
        configureDrawerLayout();

        if (DebugHelper.SHORTCUT_ACTIVITY != null) {
            startActivity(new Intent(this, DebugHelper.SHORTCUT_ACTIVITY));
        }


        getFragmentManager().beginTransaction().replace(R.id.content, new EntitiesFragment()).commit();

        if (!App.getPrefs(this).getBoolean(App.SHARED_INTRO_SEEN, false)) {
            startActivity(new Intent(this, IntroActivity.class));
            getPrefs().edit().putBoolean(App.SHARED_INTRO_SEEN, true).commit();
        }
    }



    private void configureDrawerLayout() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        menuItemMapList = menu.findItem(R.id.menuItem_map_list);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (bottomNavView.getSelectedItemId() == item.getItemId()) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.navigation_entities:
                showSection(new EntitiesFragment());
                showingMap = false;
                refreshMapListIcon();
                menuItemMapList.setVisible(true);
                return true;
            case R.id.navigation_wallet:
                showSection(new WalletFragment());
                menuItemMapList.setVisible(false);
                return true;
            case R.id.navigation_profile:
                showSection(new NoveltiesFragment());
                menuItemMapList.setVisible(false);
                return true;

            case R.id.menuItem_the_social_market:
                drawerLayout.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(this, IntroActivity.class));
                return true;
        }

        toast("Pulsado: " + item.getTitle());
        drawerLayout.closeDrawer(Gravity.LEFT);
        return false;
    }

    private void showSection(BaseFragment fragment) {

        getFragmentManager().beginTransaction().setCustomAnimations(
                R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.content, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItem_map_list:

                showingMap = !showingMap;
                refreshMapListIcon();

                getFragmentManager().beginTransaction().replace(R.id.content,
                        showingMap ? new EntitiesMapFragment() : new EntitiesFragment())
                        .commit();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshMapListIcon() {

        menuItemMapList.setIcon(showingMap ? R.mipmap.ic_list : R.mipmap.ic_map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

//                LoginDialog loginDialog = LoginDialog.newInstance();
//                loginDialog.configure(getString(R.string.enter), new LoginDialog.LoginDialogListener() {
//                    @Override
//                    public void onAccept(String username, String password) {
//                        toast("Entrando con: " + username + "...");
//                    }
//                });
//
//                loginDialog.setAvoidDismiss(true);
//                loginDialog.show(getSupportFragmentManager(), null);

                startActivity(new Intent(this, LoginActivity.class));

                break;

            case R.id.btn_singup:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }
}
