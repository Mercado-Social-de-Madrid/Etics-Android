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
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.util.WindowUtils;
import net.mercadosocial.moneda.views.CircleTransform;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, ProfileView {

    private AppCompatImageView imgProfile;
    private EditText editNamePerson;
    private EditText editSurnamesPerson;
    private EditText editNif;
    private Button btnSaveProfile;
    private ProfilePresenter presenter;

    private void findViews() {
        imgProfile = (AppCompatImageView) findViewById(R.id.img_profile);
        editNamePerson = (EditText) findViewById(R.id.edit_name_person);
        editSurnamesPerson = (EditText) findViewById(R.id.edit_surnames_person);
        editNif = (EditText) findViewById(R.id.edit_nif);
        btnSaveProfile = (Button) findViewById(R.id.btn_save_profile);

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

        }
    }

    @Override
    public void showPersonProfile(Person person) {

        editNamePerson.setText(person.getName());
        editSurnamesPerson.setText(person.getSurname());
        editNif.setText(person.getNif());

        if (person.is_guest_account()) {
            editNif.setVisibility(View.GONE);
        }

        Picasso.with(this)
                .load(person.getProfile_image())
                .placeholder(R.mipmap.ic_avatar_2)
                .error(R.mipmap.ic_avatar_2)
                .transform(new CircleTransform())
                .into(imgProfile);

    }

    @Override
    public void showEntityInfo(Entity entity) {

        editNamePerson.setText(entity.getName());
        editNif.setText(entity.getCif());

        editNamePerson.setEnabled(false);
        editSurnamesPerson.setVisibility(View.GONE);
        btnSaveProfile.setVisibility(View.GONE);

        Picasso.with(this)
                .load(entity.getLogoThumbnailOrCover())
                .placeholder(R.mipmap.ic_avatar_2)
                .error(R.mipmap.ic_avatar_2)
                .transform(new CircleTransform())
                .into(imgProfile);
    }
}
