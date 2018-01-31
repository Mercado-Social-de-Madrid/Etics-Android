package net.mercadosocial.moneda.ui.new_payment;

import android.content.Context;
import android.content.Intent;

import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.EntityInteractor;
import net.mercadosocial.moneda.model.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 19/01/18.
 */


 public class NewPaymentPresenter extends BasePresenter {

    private static final String EXTRA_ID_ENTITY = "extra_id_entity";

    private final NewPaymentView view;
    private final EntityInteractor entityInteractor;
    private List<Entity> entities = new ArrayList<>();

    public static Intent newNewPaymentActivity(Context context, int idEntity) {

         Intent intent = new Intent(context, NewPaymentActivity.class);
         intent.putExtra(EXTRA_ID_ENTITY, idEntity);

         return intent;
     }

     public static NewPaymentPresenter newInstance(NewPaymentView view, Context context) {

         return new NewPaymentPresenter(view, context);

     }

     private NewPaymentPresenter(NewPaymentView view, Context context) {
         super(context, view);

         this.view = view;
         entityInteractor = new EntityInteractor(context, view);

     }

     public void onCreate(Intent intent) {

        refreshData();
     }

     public void onResume() {

     }

     public void refreshData() {

        entityInteractor.getEntities(new EntityInteractor.Callback() {
            @Override
            public void onResponse(List<Entity> entitiesReceived) {
                entities.clear();
                entities.addAll(entitiesReceived);

            }

            @Override
            public void onError(String error) {

            }
        });

     }

    public void onRecipientSelected(Entity entity) {

        view.showSection(2);
    }
}
