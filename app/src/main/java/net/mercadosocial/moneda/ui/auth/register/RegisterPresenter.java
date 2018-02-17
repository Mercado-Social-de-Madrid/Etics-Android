package net.mercadosocial.moneda.ui.auth.register;

import android.content.Context;
import android.content.Intent;
import android.util.Patterns;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.model.User;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 16/02/18.
 */


public class RegisterPresenter extends BasePresenter {

    private final RegisterView view;
    public final String TYPE_ENTITY = "entity";
    public final String TYPE_PERSON = "person";
    private User user;

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

    }

    public void onCreate() {

        user = new User();
        view.setContinueRegisterEnable(true);
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

        if (user.getPassword().isEmpty()) {
            Toasty.error(context, context.getString(R.string.password_empty)).show();
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

        view.fillUserData(user);

        if (!checkValidData(user)) {
            return;
        }

        if (user.getType().equals(TYPE_PERSON)) {
            view.showRegisterPerson();
        } else {
            view.showRegisterEntity();
        }
    }
}
