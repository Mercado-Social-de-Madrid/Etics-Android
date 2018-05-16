package net.mercadosocial.moneda.ui.transactions;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.TransactionInteractor;
import net.mercadosocial.moneda.model.Transaction;
import net.mercadosocial.moneda.views.custom_dialog.TransactionInfoDialog;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 1/03/18.
 */


 public class TransactionsPresenter extends BasePresenter {

     private final TransactionsView view;
    private List<Transaction> transactions;

    public static Intent newTransactionsActivity(Context context) {

         Intent intent = new Intent(context, TransactionsActivity.class);

         return intent;
     }

     public static TransactionsPresenter newInstance(TransactionsView view, Context context) {

         return new TransactionsPresenter(view, context);

     }

     private TransactionsPresenter(TransactionsView view, Context context) {
         super(context, view);

         this.view = view;

     }

     public void onCreate() {

     }

     public void onResume() {

         refreshData();
     }

     public void refreshData() {

         view.setRefreshing(true);

         new TransactionInteractor(context, view).getTransactions(new BaseInteractor.BaseApiGETListCallback<Transaction>() {
             @Override
             public void onResponse(List<Transaction> list) {
                 transactions = list;
                 view.showTransactions(transactions);
             }

             @Override
             public void onError(String message) {
                 Toasty.error(context, message).show();
             }
         });

     }

    public void onItemClick(int position) {
        Transaction transaction = transactions.get(position);
        TransactionInfoDialog.newInstance(transaction).show(((AppCompatActivity)context).getSupportFragmentManager(), null);
    }
}
