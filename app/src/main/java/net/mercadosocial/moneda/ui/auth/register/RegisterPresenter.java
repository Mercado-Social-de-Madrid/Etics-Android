package net.mercadosocial.moneda.ui.auth.register;

import android.content.Context;
import android.content.Intent;

import net.mercadosocial.moneda.base.BasePresenter;

/**
 * Created by julio on 16/02/18.
 */


 public class RegisterPresenter extends BasePresenter {

     private final RegisterView view;

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

     }

     public void onResume() {

         refreshData();
     }

     public void refreshData() {


     }

 }
