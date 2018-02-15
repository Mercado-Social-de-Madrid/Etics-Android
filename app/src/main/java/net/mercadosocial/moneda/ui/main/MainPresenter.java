package net.mercadosocial.moneda.ui.main;

import android.content.Context;
import android.content.Intent;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BasePresenter;

/**
 * Created by julio on 2/02/18.
 */


 public class MainPresenter extends BasePresenter {

     private final MainView view;

     public static Intent newMainActivity(Context context) {

         Intent intent = new Intent(context, MainActivity.class);

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

         if (intent.hasExtra("amount")) {
             String amount = intent.getStringExtra("amount");
             App.openBonificationDialog(context, amount);
         }
//         :
//         {
//             'amount': transaction.amount,
//                 'is_bonification': transaction.is_bonification,
//                 'is_euro_purchase': transaction.is_euro_purchase,
//                 'concept': transaction.concept
//         }

     }

     public void onResume() {

         refreshData();
     }

     public void refreshData() {

         Data data = App.getUserData(context);
         view.showUserData(data);

     }

    public void onLogoutClick() {
        App.removeUserData(context);
        view.showUserData(null);
    }
}
