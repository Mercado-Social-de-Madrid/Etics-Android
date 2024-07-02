package net.mercadosocial.moneda.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.UserInteractor;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Notification;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.ui.member_card.MemberCheckHelper;
import net.mercadosocial.moneda.util.LangUtils;
import net.mercadosocial.moneda.util.WebUtils;

import java.util.Locale;
import java.util.Objects;

/**
 * Created by julio on 2/02/18.
 */


 public class MainPresenter extends BasePresenter {

     private final MainView view;

     public static Intent newMainActivity(Context context) {

         Intent intent = new Intent(context, MainActivity.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         return intent;
     }

     public static MainPresenter newInstance(MainView view, Context context) {

         return new MainPresenter(view, context);

     }

     private MainPresenter(MainView view, Context context) {
         super(context, view);

         this.view = view;

     }

     public void onCreate(Intent intent) {

         if (intent != null && intent.getExtras() != null) {
             String event = intent.getExtras().getString("event");
             if (Objects.equals(event, Notification.EVENT)) {
                 intent.putExtra(Notification.FIELD_FROM_OUTSIDE, true);
                 ((BaseActivity)context).processNotification(intent.getExtras());
                 intent.getExtras().clear();
             }
         }

         checkIntentUriReceived(intent);

         updateProfileStatus();

         Log.i(TAG, "onCreate: Langs: " + AppCompatDelegate.getApplicationLocales().toLanguageTags());
         Log.i(TAG, "onCreate: Locale: " + Locale.getDefault().getLanguage());
         Log.i(TAG, "onCreate: CurrentLocale: " + LangUtils.getCurrentLang());

     }


    public void onResume() {

         refreshData();
     }

     public void refreshData() {

         Data data = App.getUserData(context);
         view.showUserData(data);
         view.showNodeSocialProfiles(getApp().getCurrentNode());

     }

    private void updateProfileStatus() {
        Data data = App.getUserData(context);
        if (data != null) {
            UserInteractor userInteractor = new UserInteractor(context, null);
            if (data.isEntity()) {
                userInteractor.getEntityProfile(new BaseInteractor.BaseApiCallback<Entity>() {
                    @Override
                    public void onResponse(Entity entity) {
                        data.setEntity(entity);
                        App.saveUserData(context, data);
                        view.showUserData(data);
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            } else {
                userInteractor.getPersonProfile(new BaseInteractor.BaseApiCallback<Person>() {
                    @Override
                    public void onResponse(Person person) {
                        data.setPerson(person);
                        App.saveUserData(context, data);
                        view.showUserData(data);
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        }
    }

    private void checkIntentUriReceived(Intent intent) {

        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            String url = appLinkData.toString();
            if (url.contains(App.URL_QR_MEMBER_CARD)) {
                Data userData = App.getUserData(context);
                if (userData == null || !userData.isEntity()) {
                    showNotLoggedDialog(R.string.not_logged_member_check_message);
                } else {
                    new MemberCheckHelper(context, view).parseUrlAndCheck(url);
                }
            } else {
                showWebLinkDialog(url);
            }
        }
    }

    private void showNotLoggedDialog(int messageId) {
        new AlertDialog.Builder(context)
                .setMessage(messageId)
                .setCancelable(false)
                .setPositiveButton(R.string.understood, null)
                .show();
    }

    private void showWebLinkDialog(String url) {
        new AlertDialog.Builder(context)
                .setMessage(String.format(context.getString(R.string.web_link_dialog_message), url))
                .setCancelable(false)
                .setPositiveButton(R.string.open_link, (dialog, which) -> WebUtils.openLink(context, url))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public void onLogoutClick() {
        App.removeUserData(context);
        view.showUserData(null);
    }


}
