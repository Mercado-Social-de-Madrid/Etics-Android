package net.mercadosocial.moneda.ui.profile;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.ui.invitations.InvitationsPresenter;
import net.mercadosocial.moneda.ui.profile.pincode_change.PincodeChangePresenter;
import net.mercadosocial.moneda.util.DateUtils;
import net.mercadosocial.moneda.util.WindowUtils;
import net.mercadosocial.moneda.views.CircleTransform;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, ProfileView {

    private AppCompatImageView imgProfile;
    private EditText editNamePerson;
    private EditText editSurnamesPerson;
    private EditText editNif;
    private Button btnSaveProfile;
    private ProfilePresenter presenter;
    private TextView tvProfileType;
    private TextView tvProfileMarket;
    private LinearLayout btnChangePincode;
    private LinearLayout btnInvitations;
    private AppCompatButton btnLogout;

    private void findViews() {
        imgProfile = (AppCompatImageView) findViewById(R.id.img_profile);
        editNamePerson = (EditText) findViewById(R.id.edit_name_person);
        editSurnamesPerson = (EditText) findViewById(R.id.edit_surnames_person);
        editNif = (EditText) findViewById(R.id.edit_nif);
        btnSaveProfile = (Button) findViewById(R.id.btn_save_profile);
        tvProfileType = (TextView) findViewById(R.id.tv_profile_type);
        tvProfileMarket = (TextView) findViewById(R.id.tv_profile_market);
        btnChangePincode = (LinearLayout) findViewById(R.id.btn_change_pincode);
        btnInvitations = (LinearLayout) findViewById(R.id.btn_invitations);
        btnLogout = (AppCompatButton) findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(this);
        btnSaveProfile.setOnClickListener(this);
        btnChangePincode.setOnClickListener(this);
        btnInvitations.setOnClickListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = ProfilePresenter.newInstance(this, this);
        setBasePresenter(presenter);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        configureSecondLevelActivity();
        findViews();

        presenter.onCreate();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_profile, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menuItem_logout:
//                setResult(RESULT_OK);
//                finish();
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_profile:
                String name = editNamePerson.getText().toString();
                String surname = editSurnamesPerson.getText().toString();
                String nif = editNif.getText().toString();
                presenter.onSaveProfile(name, surname, nif);

                WindowUtils.hideSoftKeyboard(this);

                break;

            case R.id.btn_logout:
                setResult(RESULT_OK);
                finish();
                break;

            case R.id.btn_invitations:
                InvitationsPresenter.launchInvitationsActivity(this);
                break;

            case R.id.btn_change_pincode:
                PincodeChangePresenter.launchPincodeChangeActivity(this);
                break;


        }
    }

    @Override
    public void showPersonProfile(Person person) {

        editNamePerson.setText(person.getName());
        editSurnamesPerson.setText(person.getSurname());
        editNif.setText(person.getNif());
        tvProfileMarket.setText(person.getCityName());

        if (person.is_guest_account()) {

            editNif.setVisibility(View.GONE);

            String dateFormatted = DateUtils.convertDateApiToUserFormat(person.getExpiration_date());
            tvProfileType.setText(String.format(getString(R.string.guest_account_info_format), dateFormatted));
        } else {
           tvProfileType.setText(R.string.consumer);
        }

        Picasso.get()
                .load(person.getProfile_image())
                .placeholder(R.mipmap.ic_avatar_2)
                .error(R.mipmap.ic_avatar_2)
                .transform(new CircleTransform())
                .into(imgProfile);

    }

    @Override
    public void showEntityProfile(Entity entity) {

        editNamePerson.setText(entity.getName());
        editNif.setText(entity.getCif());
        tvProfileMarket.setText(entity.getCityName());
        tvProfileType.setText(R.string.entity);

        editNamePerson.setEnabled(false);
        editSurnamesPerson.setVisibility(View.GONE);
        btnSaveProfile.setVisibility(View.GONE);

        Picasso.get()
                .load(entity.getLogoThumbnailOrCover())
                .placeholder(R.mipmap.ic_avatar_2)
                .error(R.mipmap.ic_avatar_2)
                .transform(new CircleTransform())
                .into(imgProfile);
    }
}
