package net.mercadosocial.moneda.ui.profile;


import android.content.Context;
import android.content.Intent;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.UserInteractor;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;

import es.dmoral.toasty.Toasty;

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
            view.showEntityProfile(data.getEntity());
        } else {
            view.showPersonProfile(data.getPerson());
        }

        refreshProfile(data.isEntity());
    }

    private void refreshProfile(boolean isEntity) {
        if (isEntity) {
            userInteractor.getEntityProfile(new BaseInteractor.BaseApiCallback<Entity>() {
                @Override
                public void onResponse(Entity entity) {

                    Data data = App.getUserData(context);
                    data.setEntity(entity);
                    App.saveUserData(context, data);

                    view.showEntityProfile(entity);
                }

                @Override
                public void onError(String message) {
                }
            });
        } else {
            userInteractor.getPersonProfile(new BaseInteractor.BaseApiCallback<Person>() {
                @Override
                public void onResponse(Person person) {
                    Data data = App.getUserData(context);
                    data.setPerson(person);
                    App.saveUserData(context, data);

                    view.showPersonProfile(person);
                }

                @Override
                public void onError(String message) {
                }
            });
        }
    }


    public void onSaveProfile(String name, String surname, String nif) {

        view.showProgressDialog(context.getString(R.string.saving));
        Person person = Person.createPersonProfileData(name, surname, nif);
        person.setProfile_image(null);
        person.setProfile_thumbnail(null);
        userInteractor.updateProfile(person, new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                view.hideProgressDialog();
                Toasty.success(context, getString(R.string.profile_saved)).show();
                updateLocalData(person);
            }

            @Override
            public void onError(String message) {
                view.hideProgressDialog();
                Toasty.error(context, message).show();

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

}
