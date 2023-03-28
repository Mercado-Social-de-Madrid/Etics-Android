package net.mercadosocial.moneda.ui.new_payment.step1;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.EntityInteractor;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.FilterEntities;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentActivity;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentPresenter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 30/01/18.
 */


public class NewPaymentStep1Presenter extends BasePresenter {

    private final NewPaymentStep1View view;
    private final EntityInteractor entityInteractor;
    public List<Entity> entities = new ArrayList<>();
    private Entity entitySelected;

    public static NewPaymentStep1Presenter newInstance(NewPaymentStep1View view, Context context) {

        return new NewPaymentStep1Presenter(view, context);

    }

    private NewPaymentStep1Presenter(NewPaymentStep1View view, Context context) {
        super(context, view);

        this.view = view;
        entityInteractor = new EntityInteractor(context, view);

    }

    public void onCreate() {

//         if (getNewPaymentPresenter().getPreselectedEntity() != null) {
//             showPreselectedEntity(getNewPaymentPresenter().getPreselectedEntity());
//         }

        refreshData();
        view.enableContinueButton(false);
    }


    public void onResume() {

    }

    public void refreshData() {

        entityInteractor.getEntities(null, new EntityInteractor.Callback() {

            @Override
            public void onResponse(List<Entity> entitiesReceived, boolean hasMore) {
                reloadEntities(entitiesReceived);
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    public void searchEntities(String text) {

        FilterEntities filterEntities = null;
        if (!TextUtils.isEmpty(text)) {
            filterEntities = new FilterEntities();
            filterEntities.setText(text);
        }

        view.showProgress(true);

        entityInteractor.getEntities(filterEntities, new EntityInteractor.Callback() {

            @Override
            public void onResponse(List<Entity> entitiesReceived, boolean hasMore) {
                reloadEntities(entitiesReceived);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void reloadEntities(List<Entity> entitiesReceived) {
        entities.clear();
        entities.addAll(entitiesReceived);
        view.showEntities(entities);
    }

    public void onEntityItemClick(int position) {
        this.entitySelected = entities.get(position);
        view.enableContinueButton(true);
    }


    public void onContinueClick() {
        getNewPaymentPresenter().onRecipientSelected(entitySelected);
    }


    private NewPaymentPresenter getNewPaymentPresenter() {
        return (NewPaymentPresenter) ((NewPaymentActivity) context).getBasePresenter();
    }

    public void onQRScanned(String urlId) {

        if (!urlId.contains(App.URL_QR_ENTITY)) {
            view.toast(R.string.invalid_link);
            return;
        }

        Uri uri = Uri.parse(urlId);
        if (uri == null) {
            view.toast(R.string.invalid_link);
            return;
        }

        String id = uri.getLastPathSegment();

        view.setRefreshing(true);

        entityInteractor.getEntityById(id, new BaseInteractor.BaseApiCallback<Entity>() {
            @Override
            public void onResponse(Entity entity) {
                showConfirDialog(entity);
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, context.getString(R.string.user_not_recognized)).show();
            }
        });
    }

    private void showConfirDialog(final Entity entity) {
        new AlertDialog.Builder(context)
                .setTitle(entity.getName())
                .setMessage(String.format(context.getString(R.string.entity_recognized_message), entity.getName()))
                .setPositiveButton(R.string.continue_str, (dialog, which) -> {
                    entitySelected = entity;
                    if (!getNewPaymentPresenter().onRecipientSelected(entitySelected)) {
                        finish();
                    }
                })
                .setNeutralButton(R.string.back, (dialog, which) -> finish())
                .show();
    }
}
