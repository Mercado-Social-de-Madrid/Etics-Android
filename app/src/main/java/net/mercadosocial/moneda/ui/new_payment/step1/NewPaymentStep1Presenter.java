package net.mercadosocial.moneda.ui.new_payment.step1;

import android.content.Context;

import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentActivity;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentPresenter;

/**
 * Created by julio on 30/01/18.
 */


 public class NewPaymentStep1Presenter extends BasePresenter {

     private final NewPaymentStep1View view;

     public static NewPaymentStep1Presenter newInstance(NewPaymentStep1View view, Context context) {

         return new NewPaymentStep1Presenter(view, context);

     }

     private NewPaymentStep1Presenter(NewPaymentStep1View view, Context context) {
         super(context, view);

         this.view = view;

     }

     public void onCreate() {

     }

     public void onResume() {

         refreshData();
     }

     public void refreshData() {


     }


     private NewPaymentPresenter getNewPaymentPresenter() {
         return (NewPaymentPresenter) ((NewPaymentActivity) context).getPresenter();
     }

 }
