package net.mercadosocial.moneda.ui.profile;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;

import androidx.exifinterface.media.ExifInterface;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.UserInteractor;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

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

    public void onResume() {
    }

    private void loadData() {
        Data data = App.getUserData(context);
        if (data.isEntity()) {
            view.showEntityProfile(data.getEntity());
        } else {
            view.showPersonProfile(data.getPerson());
        }

//        refreshProfile(data.isEntity());
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
        person.setProfileImage(null);
        person.setProfile_thumbnail(null);
        userInteractor.updatePerson(person, new BaseInteractor.BaseApiPOSTCallback() {
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

    public void onImagePicked(File file) {


        Bitmap resizedBitmap;
        try {
            resizedBitmap = resizeBitmap(file.getPath());
        } catch (IOException e) {
            view.toast(R.string.error_image);
            Log.d(TAG, "onImagePicked: ", e);
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.uploading_image));
        progressDialog.show();

        userInteractor.updateProfileImage(base64, new BaseInteractor.BaseApiCallback<String>() {
            @Override
            public void onResponse(String profileImagePath) {
                progressDialog.dismiss();

                Data data = App.getUserData(context);
                data.getAccount().setProfileImage(profileImagePath);
                App.saveUserData(context, data);
                loadData();
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                view.toast(R.string.error_image);
            }
        });

    }

    public Bitmap resizeBitmap(String path) throws IOException {

        Bitmap originalBitmap = BitmapFactory.decodeFile(path);

        // Obtener la orientación de la imagen original
        ExifInterface exif = new ExifInterface(path);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        // Rotar la imagen original si es necesario
        Matrix matrix = new Matrix();
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            matrix.postRotate(90);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            matrix.postRotate(180);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            matrix.postRotate(270);
        }

        // Redimensionar la imagen original para que tenga un ancho máximo de 600px y un alto proporcional
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();
        int newWidth = 400;
        int newHeight = (int) (((float) newWidth / originalWidth) * originalHeight);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);

        // Crear un nuevo Bitmap con la imagen redimensionada y rotada si es necesario
        Bitmap rotatedResizedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight(), matrix, true);

        return rotatedResizedBitmap;
    }

}
