package net.mercadosocial.moneda.ui.profile;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.util.WindowUtils;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, ProfileView {

    private AppCompatImageView imgProfile;
    private EditText editNamePerson;
    private EditText editSurnamesPerson;
    private EditText editNif;
    private Button btnSaveProfile;
    private ProfilePresenter presenter;
    private EditText editEmailInvitation;
    private Button btnInvite;

    private void findViews() {
        imgProfile = (AppCompatImageView) findViewById(R.id.img_profile);
        editNamePerson = (EditText) findViewById(R.id.edit_name_person);
        editSurnamesPerson = (EditText) findViewById(R.id.edit_surnames_person);
        editNif = (EditText) findViewById(R.id.edit_nif);
        btnSaveProfile = (Button) findViewById(R.id.btn_save_profile);

        editEmailInvitation = findViewById(R.id.edit_email_invitation);
        btnInvite = findViewById(R.id.btn_invite);

        btnInvite.setOnClickListener(this);
        btnSaveProfile.setOnClickListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItem_logout:
                setResult(RESULT_OK);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

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

            case R.id.btn_invite:
                String emailInvitation = editEmailInvitation.getText().toString();
                presenter.sendInvitation(emailInvitation);
                WindowUtils.hideSoftKeyboard(this);
                break;
        }
    }

    @Override
    public void showPersonProfile(Person person) {

        editNamePerson.setText(person.getName());
        editSurnamesPerson.setText(person.getSurname());
        editNif.setText(person.getNif());

        Picasso.with(this)
                .load(person.getProfile_image())
                .placeholder(R.mipmap.ic_avatar_2)
                .error(R.mipmap.ic_avatar_2)
                .into(imgProfile);

    }

    @Override
    public void configureInviteButton(String text, boolean enabled) {

        btnInvite.setEnabled(enabled);
        btnInvite.setText(text);
    }

    @Override
    public void clearInvitationEmailText() {
        editEmailInvitation.setText("");
    }
}
