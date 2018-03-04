package net.mercadosocial.moneda.ui.main;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.DebugHelper;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.ui.auth.login.LoginActivity;
import net.mercadosocial.moneda.ui.auth.register.RegisterActivity;
import net.mercadosocial.moneda.ui.entities.EntitiesFragment;
import net.mercadosocial.moneda.ui.get_boniatos.GetBoniatosPresenter;
import net.mercadosocial.moneda.ui.info.WebViewActivity;
import net.mercadosocial.moneda.ui.intro.IntroActivity;
import net.mercadosocial.moneda.ui.novelties.list.NoveltiesFragment;
import net.mercadosocial.moneda.ui.wallet.WalletFragment;
import net.mercadosocial.moneda.ui.wallet.WalletPresenter;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, MainView {


    private DrawerLayout drawerLayout;
    private TextView btnLogin;
    private TextView btnSignup;
    private BottomNavigationView bottomNavView;
    private TextView tvUserName;
    private View viewEnterButtons;
    private View btnLogout;
    private View viewUserInfo;
    private MainPresenter presenter;
    private ImageView imgAvatar;

    private void findViews() {


        bottomNavView = (BottomNavigationView) findViewById(R.id.navigation_bottom_view);
        bottomNavView.setOnNavigationItemSelectedListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnLogin = (TextView) navigationView.getHeaderView(0).findViewById(R.id.btn_login);
        btnSignup = (TextView) navigationView.getHeaderView(0).findViewById(R.id.btn_singup);
        tvUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_user_name);
        viewEnterButtons = navigationView.getHeaderView(0).findViewById(R.id.view_enter_buttons);
        viewUserInfo = navigationView.getHeaderView(0).findViewById(R.id.view_user_info);
        btnLogout = navigationView.getHeaderView(0).findViewById(R.id.btn_logout);
        imgAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.img_avatar);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = MainPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        configureToolbar();
        configureDrawerLayout();

        if (DebugHelper.SHORTCUT_ACTIVITY != null) {
            startActivity(new Intent(this, DebugHelper.SHORTCUT_ACTIVITY));
        }

//        if (App.getUserData(this) == null) {
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//            return;
//        }

        getFragmentManager().beginTransaction().replace(R.id.content, new EntitiesFragment()).commit();

        if (!App.getPrefs(this).getBoolean(App.SHARED_INTRO_SEEN, false)) {
            startActivity(new Intent(this, IntroActivity.class));
            getPrefs().edit().putBoolean(App.SHARED_INTRO_SEEN, true).commit();
        }


        presenter.onCreate(getIntent());
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    private void configureDrawerLayout() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
//        toolbar.setNavigationIcon(null);
    }

//    public void toggleDrawerRight() {
//        if (drawerLayout.isDrawerOpen(Gravity.END)) {
//            drawerLayout.closeDrawer(Gravity.END);
//        } else {
//            drawerLayout.openDrawer(Gravity.END);
//        }
//    }


    @Override
    public void refreshData() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof WalletFragment) {
            ((WalletPresenter)((WalletFragment)fragment).getBasePresenter()).refreshData();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (bottomNavView.getSelectedItemId() == item.getItemId()) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.navigation_entities:
                showSection(new EntitiesFragment());
                return true;
            case R.id.navigation_wallet:
                showSection(new WalletFragment());
                return true;
            case R.id.navigation_profile:
                showSection(new NoveltiesFragment());
                return true;

            case R.id.menuItem_the_social_market:
//                startActivity(new Intent(this, IntroActivity.class));
                String url = "https://madrid.mercadosocial.net/";
                WebViewActivity.start(this, url);
                return true;

            case R.id.menuItem_how_boniato_works:
                String url2 = "https://madrid.mercadosocial.net/reboniato/";
                WebViewActivity.start(this, url2);
                break;

            case R.id.menuItem_get_boniatos:

//                Bundle bundle = new Bundle();
//                bundle.putString("title", "Noticia de prueba");
//                bundle.putString("message", "Texto de noticia");
//
//                bundle.putString("type", Notification.TYPE_NEWS);
//                bundle.putString("id", "87938556-10e1-41fb-bd1c-0fb854df72b1");
//
//                Intent intent = new Intent(App.ACTION_NOTIFICATION_RECEIVED);
//                intent.putExtras(bundle);
//                sendBroadcast(intent);

                startActivity(GetBoniatosPresenter.newGetBoniatosActivity(this));

                break;
        }

//        toast("Pulsado: " + item.getTitle());
        drawerLayout.closeDrawer(Gravity.LEFT);
        return false;
    }

    private void openIPDialog() {
        final EditText editText = new EditText(this);
        editText.setText("192.168.43.42:8000");

        new AlertDialog.Builder(this)
                .setView(editText)
                .setPositiveButton("Establecer IP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPrefs().edit().putString("baseUrl", "http://" + editText.getText().toString()).commit();
                    }
                }).show();
    }

    private void showSection(BaseFragment fragment) {

        getFragmentManager().beginTransaction().setCustomAnimations(
                R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.content, fragment)
                .commit();
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

                drawerLayout.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(this, LoginActivity.class));

                break;

            case R.id.btn_singup:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.btn_logout:
                presenter.onLogoutClick();
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


    // PRESENTER CALLBACKS

    @Override
    public void showUserData(Data userData) {

        viewEnterButtons.setVisibility(userData == null ? View.VISIBLE : View.GONE);
        viewUserInfo.setVisibility(userData == null ? View.GONE : View.VISIBLE);

        if (userData != null) {
            tvUserName.setText(userData.getName());
            if (userData.getEntity() != null) {
                String logoUrl = userData.getEntity().getLogoThumbnail();
                Picasso.with(this)
                        .load(logoUrl)
                        .placeholder(R.mipmap.ic_avatar)
                        .error(R.mipmap.ic_mes_v2_144)
                        .into(imgAvatar);
            }
        }
    }
}
