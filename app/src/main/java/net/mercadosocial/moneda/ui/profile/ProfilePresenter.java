package net.mercadosocial.moneda.ui.profile;


import android.content.Context;
import android.content.Intent;
import android.util.Patterns;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.UserInteractor;
import net.mercadosocial.moneda.model.Person;

public class ProfilePresenter extends BasePresenter {

    private final ProfileView view;
    private final UserInteractor userInteractor;

    public static void launchProfileActivity(Context context) {

        Intent intent = new Intent(context, ProfileActivity.class);

        context.startActivity(intent);
    }

    public static ProfilePresenter newInstance(ProfileView view, Context context) {

        return new ProfilePresenter(view, context);

    }

    private ProfilePresenter(ProfileView view, Context context) {
        super(context, view);

        this.view = view;
        userInteractor = new UserInteractor(context, view);

    }

    public void onCreate() {

        loadData();
    }

    private void loadData() {
        Data data = App.getUserData(context);
        if (data.isEntity()) {
            view.toast(R.string.entities_dont_edit_profile);
            finish();
            return;
        }

        view.showPersonProfile(data.getPerson());
    }


    public void onSaveProfile(String name, String surname, String nif) {

        view.showProgressDialog(context.getString(R.string.saving));
        Person person = Person.createPersonProfileData(name, surname, nif);
        userInteractor.updateProfile(person, new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                view.hideProgressDialog();
                view.toast(R.string.profile_saved);
                updateLocalData(person);
            }

            @Override
            public void onError(String message) {
                view.hideProgressDialog();
                view.toast(message);

            }
        });
    }

    private void updateLocalData(Person person) {
        Data data = App.getUserData(context);
        Person personSaved = data.getPerson();
        personSaved.setName(person.getName());
        personSaved.setSurname(person.getSurname());
        personSaved.setNif(person.getNif());
        App.saveUserData(context, data);

    }

    public void sendInvitation(String emailInvitation) {

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInvitation).matches()) {
            view.toast(R.string.invalid_email);
            return;
        }

        view.configureInviteButton(context.getString(R.string.sending), false);
        userInteractor.sendInvitation(emailInvitation, new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                view.toast(R.string.invitation_sent);
                view.configureInviteButton(context.getString(R.string.invite), true);
                view.clearInvitationEmailText();
            }

            @Override
            public void onError(String message) {
                view.toast(message);
                view.configureInviteButton(context.getString(R.string.invite), true);
            }
        });
    }
}
