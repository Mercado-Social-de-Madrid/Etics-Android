package net.mercadosocial.moneda.ui.new_payment.step1;

import android.content.Context;

import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.EntityInteractor;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentActivity;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentPresenter;

import java.util.ArrayList;
import java.util.List;

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

         refreshData();

         // todo uncomment this
         view.enableContinueButton(false);
     }

     public void onResume() {

     }

     public void refreshData() {

        entityInteractor.getEntities(new EntityInteractor.Callback() {

            @Override
            public void onResponse(List<Entity> entitiesReceived) {
                entities.clear();
                entities.addAll(entitiesReceived);
                view.showEntities(entities);
            }

            @Override
            public void onError(String error) {

            }
        });

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

 }
