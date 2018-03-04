package net.mercadosocial.moneda.ui.get_boniatos;

import android.content.Context;
import android.content.Intent;

import net.mercadosocial.moneda.base.BasePresenter;

/**
 * Created by julio on 3/03/18.
 */


 public class GetBoniatosPresenter extends BasePresenter {

     private final GetBoniatosView view;

     public static Intent newGetBoniatosActivity(Context context) {

         Intent intent = new Intent(context, GetBoniatosActivity.class);

         return intent;
     }

     public static GetBoniatosPresenter newInstance(GetBoniatosView view, Context context) {

         return new GetBoniatosPresenter(view, context);

     }

     private GetBoniatosPresenter(GetBoniatosView view, Context context) {
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
