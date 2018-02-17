package net.mercadosocial.moneda.ui.auth.register;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.model.User;
import net.mercadosocial.moneda.util.WindowUtils;

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
    private EditText editAddress;
    private View viewRegisterUser;
    private View viewRegisterPerson;
    private View viewRegisterEntity;



    private void findViews() {

        viewRegisterUser = findViewById( R.id.view_register_user );
        viewRegisterPerson = findViewById( R.id.view_register_person );
        viewRegisterEntity = findViewById( R.id.view_register_entity );
        
        editUsername = (EditText)findViewById( R.id.edit_username );
        editPassword = (EditText)findViewById( R.id.edit_password );
        editRepeatPassword = (EditText)findViewById( R.id.edit_repeat_password );

        btnRegisterPerson = findViewById(R.id.btn_register_person);
        btnRegisterEntity = findViewById(R.id.btn_register_entity);
        btnRegisterContinue = (TextView) findViewById(R.id.btn_register_continue);

        editNamePerson = (EditText)findViewById( R.id.edit_name_person );
        editNif = (EditText)findViewById( R.id.edit_nif );

        editNameEntity = (EditText)findViewById( R.id.edit_name_entity );
        editCif = (EditText)findViewById( R.id.edit_cif );
        editAddress = (EditText)findViewById( R.id.edit_address );

        btnRegisterPerson.setOnClickListener(this);
        btnRegisterEntity.setOnClickListener(this);
        btnRegisterContinue.setOnClickListener(this);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = RegisterPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
        presenter.onCreate();

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
    }

    @Override
    public void showRegisterEntity() {
        viewRegisterUser.setVisibility(View.GONE);
        viewRegisterEntity.setVisibility(View.VISIBLE);
        btnRegisterContinue.setText(R.string.register);
    }

    @Override
    public void setContinueRegisterEnable(boolean enable) {
        btnRegisterContinue.setEnabled(enable);
    }

    @Override
    public void fillUserData(User user) {
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        String repeatPassword = editRepeatPassword.getText().toString();

        user.setEmail(username);
        user.setPassword(password);
        user.setRepeatPassword(repeatPassword);
    }

    @Override
    public void fillPersonData(Person person) {
        String personName = editNamePerson.getText().toString();
        String nif = editNif.getText().toString();

        person.setName(personName);
        person.setNIF(nif);
    }

    @Override
    public void fillEntityData(Entity entity) {
        String entityName = editNameEntity.getText().toString();
        String cif = editCif.getText().toString();
        String address = editAddress.getText().toString();

        entity.setName(entityName);
        entity.setCif(cif);
        entity.setAddress(address);
    }
}
