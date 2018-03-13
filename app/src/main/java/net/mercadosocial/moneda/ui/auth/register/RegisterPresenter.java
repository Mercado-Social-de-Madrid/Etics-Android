package net.mercadosocial.moneda.ui.auth.register;

import android.content.Context;
import android.content.Intent;
import android.util.Patterns;

import com.google.android.gms.location.places.Place;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.AuthInteractor;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.model.User;
import net.mercadosocial.moneda.ui.main.MainPresenter;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 16/02/18.
 */


public class RegisterPresenter extends BasePresenter {

    private final RegisterView view;
    public final String TYPE_ENTITY = "entity";
    public final String TYPE_PERSON = "person";
    private final AuthInteractor authInteractor;
    private User user;
    private int registerScreen = 1;
    private Place place;

    public static Intent newRegisterActivity(Context context) {

        Intent intent = new Intent(context, RegisterActivity.class);

        return intent;
    }

    public static RegisterPresenter newInstance(RegisterView view, Context context) {

        return new RegisterPresenter(view, context);

    }

    private RegisterPresenter(RegisterView view, Context context) {
        super(context, view);

        this.view = view;
        authInteractor = new AuthInteractor(context, view);

    }

    public void onCreate() {

        user = new User();
        view.setContinueRegisterEnable(true);

//        registerScreen = 2;
//        user.setType(TYPE_ENTITY);
//        view.showRegisterEntity();

    }

    public void onResume() {

        refreshData();
    }

    public void refreshData() {


    }

    public void onUserEntityClick() {
        user.setType(TYPE_ENTITY);
    }


    public void onUserPersonClick() {
        user.setType(TYPE_PERSON);
    }

    private boolean checkValidData(User user) {
        if (!Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()) {
            Toasty.error(context, context.getString(R.string.invalid_email)).show();
            return false;
        }

        if (user.getPassword().length() < 5) {
            Toasty.error(context, context.getString(R.string.password_at_least_5_chars)).show();
            return false;
        }

        if (!user.getPassword().equals(user.getRepeatPassword())) {
            Toasty.error(context, context.getString(R.string.paswords_does_not_match)).show();
            return false;
        }

        if (user.getType() == null) {
            Toasty.warning(context, context.getString(R.string.select_person_or_entity)).show();
            return false;
        }

        return true;
    }


    public void onContinueRegisterButtonClick() {

        if (registerScreen == 1) {
            processUserAuthData();
        } else if (registerScreen == 2) {
            if (processUserInfoData()) {
                performRegisterApi();
            }
        }

    }

    public void onBackRegisterButtonClick() {
        registerScreen = 1;
        view.showRegisterUserCommonData();
    }

    private void processUserAuthData() {

        view.fillUserAuthData(user);

        if (!checkValidData(user)) {
            return;
        }

        if (user.getType().equals(TYPE_PERSON)) {
            view.showRegisterPerson();
        } else {
            view.showRegisterEntity();
        }

        registerScreen = 2;
    }


    private boolean processUserInfoData() {
        switch (user.getType()) {
            case TYPE_PERSON:
                Person person = new Person();
                view.fillPersonData(person);
                if (!checkValidData(person)) {
                    return false;
                }
                user.setPerson(person);
                break;

            case TYPE_ENTITY:
                Entity entity = new Entity();
                if (!view.fillEntityData(entity)) {
                    return false;
                }

                if (!checkValidData(entity)) {
                    return false;
                }

                if (place != null) {
                    entity.setAddress(place.getAddress().toString());
                    entity.setLatitude(place.getLatLng().latitude);
                    entity.setLongitude(place.getLatLng().longitude);
                }
                user.setEntity(entity);
                break;
        }

        return true;
    }

    private boolean checkValidData(Entity entity) {

        return checkValidPinCode(entity.getPin_code(), entity.getPin_codeRepeat());
    }

    private boolean checkValidData(Person person) {
        if (person.getName().isEmpty()) {
            Toasty.error(context, context.getString(R.string.name_cannot_be_empty)).show();
            return false;
        }

        if (person.getNif().isEmpty()) {
            Toasty.error(context, context.getString(R.string.nif_cannot_be_empty)).show();
            return false;
        }

        return checkValidPinCode(person.getPin_code(), person.getPin_codeRepeat());
    }

    private boolean checkValidPinCode(String pinCode, String pinCodeRepeat) {

        if (pinCode.length() < 4) {
            Toasty.error(context, context.getString(R.string.pin_code_4_lenght)).show();
            return false;
        }

        try {
            Integer.parseInt(pinCode);
        } catch (NumberFormatException e) {
            Toasty.error(context, context.getString(R.string.invalid_pin_number)).show();
            return false;
        }

        if (!pinCode.equals(pinCodeRepeat)) {
            Toasty.error(context, context.getString(R.string.pin_codes_does_not_match)).show();
            return false;
        }

        return true;
    }

    private void performRegisterApi() {

        view.setRefresing(true);

        authInteractor.register(user, new BaseInteractor.BaseApiCallback<Data>() {

            @Override
            public void onResponse(Data data) {
                data.setUsername(user.getUsername());
                App.saveUserData(context, data);
                Toasty.success(context, context.getString(R.string.welcome)).show();
                context.startActivity(MainPresenter.newMainActivity(context));
            }

            @Override
            public void onError(String error) {
                Toasty.error(context, error).show();
            }
        });
    }


    public void onPlaceSelected(Place place) {
        this.place = place;
    }

}
