package net.mercadosocial.moneda.ui.main;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.BuildConfig;
import net.mercadosocial.moneda.DebugHelper;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.FilterEntities;
import net.mercadosocial.moneda.model.Node;
import net.mercadosocial.moneda.model.SocialProfile;
import net.mercadosocial.moneda.ui.auth.login.LoginActivity;
import net.mercadosocial.moneda.ui.auth.register_web.RegisterWebActivity;
import net.mercadosocial.moneda.ui.entities.EntitiesFragment;
import net.mercadosocial.moneda.ui.entities.EntitiesPresenter;
import net.mercadosocial.moneda.ui.fediverse.list.FediverseFragment;
import net.mercadosocial.moneda.ui.info.InfoMesActivity;
import net.mercadosocial.moneda.ui.intro.IntroActivity;
import net.mercadosocial.moneda.ui.invitations.InvitationsPresenter;
import net.mercadosocial.moneda.ui.member_card.MemberCardFragment;
import net.mercadosocial.moneda.ui.novelties.list.NoveltiesFragment;
import net.mercadosocial.moneda.ui.profile.ProfileActivity;
import net.mercadosocial.moneda.util.DateUtils;
import net.mercadosocial.moneda.views.CircleTransform;
import net.mercadosocial.moneda.views.DialogSelectMES;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, MainView {


    private static final int REQ_CODE_INTRO = 11;
    private static final int REQ_CODE_PROFILE = 22;
    private static final int REQ_CODE_INFO_MES = 33;

    private DrawerLayout drawerLayout;
    private TextView btnLogin;
    private TextView btnSignup;
    private BottomNavigationView bottomNavView;
    private TextView tvName;
    private View viewEnterButtons;
    private View btnLogout;
    private View viewUserInfo;
    private MainPresenter presenter;
    private ImageView imgAvatar;
    private TextView btnGoToProfile;
    private NavigationView navigationView;
    private TextView tvMES;
    private TextView tvGuestInfo;
    private TextView tvUsername;
    private TextView tvAppVersion;
    private RecyclerView recyclerSocialProfiles;
    private List<SocialProfile> socialProfiles = new ArrayList<>();

    private void findViews() {


        bottomNavView = findViewById(R.id.navigation_bottom_view);
        bottomNavView.setOnNavigationItemSelectedListener(this);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvAppVersion = findViewById(R.id.tv_app_version);

        btnLogin = navigationView.getHeaderView(0).findViewById(R.id.btn_login);
        btnSignup = navigationView.getHeaderView(0).findViewById(R.id.btn_singup);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        tvUsername = navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        tvMES = navigationView.getHeaderView(0).findViewById(R.id.tv_mes);
        tvGuestInfo = navigationView.getHeaderView(0).findViewById(R.id.tv_guest_info);
        viewEnterButtons = navigationView.getHeaderView(0).findViewById(R.id.view_enter_buttons);
        viewUserInfo = navigationView.getHeaderView(0).findViewById(R.id.view_user_info);
        btnLogout = navigationView.getHeaderView(0).findViewById(R.id.btn_logout);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        btnGoToProfile = navigationView.getHeaderView(0).findViewById(R.id.btn_go_to_profile);

        recyclerSocialProfiles = findViewById(R.id.recycler_social_profiles);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnGoToProfile.setOnClickListener(this);
        viewUserInfo.setOnClickListener(this);

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
        configureToolbarBackArrowBehaviour();


        tvAppVersion.setText(BuildConfig.VERSION_NAME);

        if (DebugHelper.SHORTCUT_ACTIVITY != null && DebugHelper.SHORTCUT_ACTIVITY != MainActivity.class) {
            startActivity(new Intent(this, DebugHelper.SHORTCUT_ACTIVITY));
            return;
        }

        Node node = getApp().getCurrentNode();
        if (node != null) {
            showFragment(node.isMemberCardEnabled() ? new MemberCardFragment(): new EntitiesFragment());
            updateMenuViewsByNode();
        } else if (!App.getPrefs(this).getBoolean(App.SHARED_INTRO_SEEN, false)) {
            startActivityForResult(new Intent(this, IntroActivity.class), REQ_CODE_INTRO);
        }

        presenter.onCreate(getIntent());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DebugHelper.SHORTCUT_ACTIVITY != null) {
            DebugHelper.SHORTCUT_ACTIVITY = null;
            return;
        }

        presenter.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
//        getSupportFragmentManager().beginTransaction().remove(fragment).commit();

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_INTRO:
//                showFragment(0);
                updateMenuViewsByNode();
                break;

            case REQ_CODE_PROFILE:
                if (resultCode == RESULT_OK) {
                    presenter.onLogoutClick();
                }
                break;

            case REQ_CODE_INFO_MES:
                if (resultCode == RESULT_OK) {
                    bottomNavView.setSelectedItemId(R.id.navigation_entities);
                }
                break;

        }
    }


    private void configureDrawerLayout() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void configureToolbarBackArrowBehaviour() {

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> {

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {

                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }

            }
        });
    }

    @Override
    public void refreshData() {

        refreshFragmentData();

        presenter.refreshData();
    }

    private void refreshFragmentData() {

        BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        if (baseFragment != null && baseFragment.isAdded()) {
            baseFragment.refreshData();
        }
    }

    public void onNodeChanged() {

        BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        if (baseFragment != null && baseFragment.isAdded()) {
            baseFragment.onNodeChanged();
        }

    }


    public void setFilterEntities(FilterEntities filterEntities) {
        BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        if (baseFragment instanceof EntitiesFragment) {
            ((EntitiesPresenter) baseFragment.getBasePresenter()).setFilterEntities(filterEntities);
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        }
    }


    // INTERACTIONS
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (bottomNavView.getSelectedItemId() == item.getItemId()) {
            return false;
        }

        Node node = getApp().getCurrentNode();

        try {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_member_card) {
                setToolbarTitle(R.string.member_card);
                showFragment(new MemberCardFragment());
                return true;
            } else if (itemId == R.id.navigation_entities) {
                setToolbarTitle(R.string.entities);
                showFragment(new EntitiesFragment());
                return true;
            } else if (itemId == R.id.navigation_profile) {
                setToolbarTitle(R.string.highlighted);
                showFragment(new NoveltiesFragment());
                return true;
            }else if (itemId == R.id.navigation_fediverse) {
                setToolbarTitle(R.string.fediverse);
                showFragment(new FediverseFragment());
            } else if (itemId == R.id.menuItem_the_social_market) {
                startActivityForResult(new Intent(this, InfoMesActivity.class), REQ_CODE_INFO_MES);
            } else if (itemId == R.id.menuItem_change_social_market) {
                DialogSelectMES.with(this)
                        .setOnSelectMESListener(nodeSelected -> {
                            getApp().setCurrentNode(nodeSelected);
                            updateMenuViewsByNode();
                            onNodeChanged();
                            presenter.onLogoutClick();
                            bottomNavView.setSelectedItemId(R.id.navigation_entities);
                        })
                        .show();
            } else if (itemId == R.id.menuItem_invitations) {
                InvitationsPresenter.launchInvitationsActivity(this);
            } else if (itemId == R.id.nav_contact_email) {
                String emailMES = node.getContactEmail();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", emailMES, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_contact_subject));
//                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(emailIntent);

            }
        } catch (ActivityNotFoundException e) {
            // ignore
        }

//        toast("Pulsado: " + item.getTitle());
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }


    private void updateMenuViewsByNode() {

        Node node = getApp().getCurrentNode();

        bottomNavView.getMenu().findItem(R.id.navigation_member_card).setVisible(node.isMemberCardEnabled());

        // TODO Node changes
//        navigationView.getHeaderView(0).setVisibility(isMadrid ? View.VISIBLE : View.GONE);

//        Menu leftMenu = navigationView.getMenu();
//        leftMenu.findItem(R.id.menuItem_the_social_market).setVisible(isMadrid);
//
//        leftMenu.findItem(R.id.nav_contact_web).setVisible(mesData.getWeb() != null);
//        leftMenu.findItem(R.id.nav_contact_facebook).setVisible(mesData.getFacebook() != null);
//        leftMenu.findItem(R.id.nav_contact_twitter).setVisible(mesData.getTwitter() != null);
//        leftMenu.findItem(R.id.nav_contact_linkedin).setVisible(mesData.getLinkedIn() != null);
//        leftMenu.findItem(R.id.nav_contact_instagram).setVisible(mesData.getInstagram() != null);


    }


    private void showFragment(BaseFragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);

        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();

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
                startActivity(new Intent(this, RegisterWebActivity.class));
                break;

            case R.id.btn_logout:
                presenter.onLogoutClick();
                break;

            case R.id.btn_go_to_profile:
            case R.id.view_user_info:
                openProfileActivity();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openProfileActivity() {
        startActivityForResult(new Intent(this, ProfileActivity.class), REQ_CODE_PROFILE);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }



    public void onMenuFilterClick() {

        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }

            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    // PRESENTER CALLBACKS

    @Override
    public void showUserData(Data userData) {

        viewEnterButtons.setVisibility(userData == null ? View.VISIBLE : View.GONE);
        viewUserInfo.setVisibility(userData == null ? View.GONE : View.VISIBLE);

        if (userData != null) {
            tvName.setText(userData.getName(true));
            tvUsername.setText(userData.getUsername());
            String logoUrl = userData.getLogoThumbnail();
            Picasso.get()
                    .load(logoUrl)
                    .placeholder(R.mipmap.ic_avatar_2)
                    .transform(new CircleTransform())
                    .error(R.mipmap.ic_avatar_2)
                    .into(imgAvatar);

            String nodeName = getApp().getCurrentNode().getName();
            tvMES.setText(String.format(getString(R.string.mes_format), nodeName));
            if (!userData.isEntity()) {
                if (userData.getPerson().is_guest_account()) {
                    String dateFormatted = DateUtils.convertDateApiToUserFormat(userData.getPerson().getExpiration_date());
                    tvGuestInfo.setText(String.format(getString(R.string.guest_account_info_format), dateFormatted));
                }
            }

        } else {
            imgAvatar.setImageResource(R.mipmap.ic_avatar_2);

            refreshFragmentData();
        }

    }

    @Override
    public void showNodeData(Node node) {
        socialProfiles.clear();
        if (node.getSocialProfiles() != null) {
            socialProfiles.addAll(node.getSocialProfiles());
        }
        SocialProfileAdapter socialProfileAdapter = new SocialProfileAdapter(this, socialProfiles);
        recyclerSocialProfiles.setAdapter(socialProfileAdapter);
    }
}
