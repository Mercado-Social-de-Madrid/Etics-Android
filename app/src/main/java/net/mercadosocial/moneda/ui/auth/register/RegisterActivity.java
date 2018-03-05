package net.mercadosocial.moneda.ui.auth.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.model.User;
import net.mercadosocial.moneda.ui.auth.login.LoginActivity;
import net.mercadosocial.moneda.util.WindowUtils;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, RegisterView {

    private View btnRegisterEntity;
    private View btnRegisterPerson;
    private RegisterPresenter presenter;
    private TextView btnRegisterContinue;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editRepeatPassword;
    private EditText editNamePerson;
    private EditText editNif;
    private EditText editNameEntity;
    private EditText editCif;
    private View viewRegisterUser;
    private View viewRegisterPerson;
    private View viewRegisterEntity;
    private EditText editBonusPercent;
    private EditText editMaxAcceptPercent;
    private View btnGoToLogin;
    private EditText editBonusPercentEntities;
    private EditText editPinCodePerson, editPinCodeRepeatPerson;
    private EditText editPinCodeEntity, editPinCodeRepeatEntity;
    private TextView btnRegisterBack;
    private EditText editEmail;
    private EditText editSurnamesPerson;


    private void findViews() {

        viewRegisterUser = findViewById(R.id.view_register_user);
        viewRegisterPerson = findViewById(R.id.view_register_person);
        viewRegisterEntity = findViewById(R.id.view_register_entity);

        editUsername = (EditText) findViewById(R.id.edit_username);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editRepeatPassword = (EditText) findViewById(R.id.edit_repeat_password);

        btnRegisterPerson = findViewById(R.id.btn_register_person);
        btnRegisterEntity = findViewById(R.id.btn_register_entity);

        btnRegisterBack = (TextView) findViewById(R.id.btn_register_back);
        btnRegisterContinue = (TextView) findViewById(R.id.btn_register_continue);

        editNamePerson = (EditText) findViewById(R.id.edit_name_person);
        editSurnamesPerson = (EditText) findViewById(R.id.edit_surnames_person);
        editNif = (EditText) findViewById(R.id.edit_nif);
        editPinCodePerson = (EditText) findViewById(R.id.view_pin_codes_person).findViewById(R.id.edit_pin_code);
        editPinCodeRepeatPerson = (EditText) findViewById(R.id.view_pin_codes_person).findViewById(R.id.edit_pin_code_repeat);

        editNameEntity = (EditText) findViewById(R.id.edit_name_entity);
        editCif = (EditText) findViewById(R.id.edit_cif);
        editMaxAcceptPercent = (EditText) findViewById(R.id.edit_max_accept_percent);
        editBonusPercent = (EditText) findViewById(R.id.edit_bonus_percent);
        editBonusPercentEntities = (EditText) findViewById(R.id.edit_bonus_percent_entities);
        editPinCodeEntity = (EditText) findViewById(R.id.view_pin_codes_person).findViewById(R.id.edit_pin_code);
        editPinCodeRepeatEntity = (EditText) findViewById(R.id.view_pin_codes_person).findViewById(R.id.edit_pin_code_repeat);

        btnGoToLogin = findViewById(R.id.btn_go_to_login);

        btnRegisterPerson.setOnClickListener(this);
        btnRegisterEntity.setOnClickListener(this);
        btnRegisterContinue.setOnClickListener(this);
        btnRegisterBack.setOnClickListener(this);
        btnGoToLogin.setOnClickListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = RegisterPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
        presenter.onCreate();

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint(getString(R.string.address));

        AutocompleteFilter countryFilter = new AutocompleteFilter.Builder()
                .setCountry("ES")
                .build();

        autocompleteFragment.setFilter(countryFilter);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                Log.i(TAG, "Place: " + place.getName());
                presenter.onPlaceSelected(place);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        autocompleteFragment.setText(place.getAddress());
                    }
                }, 50);
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
                //todo error report
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_person:
                presenter.onUserPersonClick();
                btnRegisterPerson.setSelected(true);
                btnRegisterEntity.setSelected(false);
                break;
            case R.id.btn_register_entity:
                presenter.onUserEntityClick();
                btnRegisterEntity.setSelected(true);
                btnRegisterPerson.setSelected(false);
                break;

            case R.id.btn_register_continue:
                WindowUtils.hideSoftKeyboard(this);
                presenter.onContinueRegisterButtonClick();
                break;

            case R.id.btn_register_back:
                WindowUtils.hideSoftKeyboard(this);
                presenter.onBackRegisterButtonClick();
                break;

            case R.id.btn_go_to_login:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showConfirmExitDialog();
    }

    private void showConfirmExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_register)
                .setMessage(R.string.exit_register_message)
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNeutralButton(R.string.remain, null)
                .show();
    }

    // PRESENTER CALLBACKS
    @Override
    public void showRegisterPerson() {
        viewRegisterUser.setVisibility(View.GONE);
        viewRegisterPerson.setVisibility(View.VISIBLE);
        btnRegisterContinue.setText(R.string.register);
        btnRegisterBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRegisterEntity() {
        viewRegisterUser.setVisibility(View.GONE);
        viewRegisterEntity.setVisibility(View.VISIBLE);
        btnRegisterContinue.setText(R.string.register);
        btnRegisterBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRegisterUserCommonData() {
        btnRegisterContinue.setText(R.string.continue_arrow);
        viewRegisterUser.setVisibility(View.VISIBLE);
        viewRegisterPerson.setVisibility(View.GONE);
        viewRegisterEntity.setVisibility(View.GONE);
        btnRegisterBack.setVisibility(View.GONE);
    }

    @Override
    public void setContinueRegisterEnable(boolean enable) {
        btnRegisterContinue.setEnabled(enable);
    }

    @Override
    public void fillUserAuthData(User user) {
        String username = editUsername.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String repeatPassword = editRepeatPassword.getText().toString();

        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setRepeatPassword(repeatPassword);
    }

    @Override
    public void fillPersonData(Person person) {
        person.setName(editNamePerson.getText().toString());
        person.setSurname(editSurnamesPerson.getText().toString());
        person.setNif(editNif.getText().toString());
        person.setPin_code(editPinCodePerson.getText().toString());
        person.setPin_codeRepeat(editPinCodeRepeatPerson.getText().toString());
    }

    @Override
    public boolean fillEntityData(Entity entity) {

        entity.setName(editNameEntity.getText().toString());
        entity.setCif(editCif.getText().toString());
        entity.setPin_code(editPinCodeEntity.getText().toString());
        entity.setPin_codeRepeat(editPinCodeRepeatEntity.getText().toString());

        try {
            entity.setMax_percent_payment(Float.parseFloat(editMaxAcceptPercent.getText().toString()));
        } catch (NumberFormatException e) {
            editMaxAcceptPercent.setError(getString(R.string.invalid_number));
            Toasty.error(this, getString(R.string.invalid_field)).show();
            return false;
        }

        try {
            entity.setBonus_percent_general(Float.parseFloat(editBonusPercent.getText().toString()));
        } catch (NumberFormatException e) {
            editBonusPercent.setError(getString(R.string.invalid_number));
            Toasty.error(this, getString(R.string.invalid_field)).show();
            return false;
        }

        try {
            entity.setBonus_percent_entity(Float.parseFloat(editBonusPercentEntities.getText().toString()));
        } catch (NumberFormatException e) {
            editBonusPercentEntities.setError(getString(R.string.invalid_number));
            Toasty.error(this, getString(R.string.invalid_field)).show();
            return false;
        }

        return true;

    }
}
