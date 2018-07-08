package net.mercadosocial.moneda.ui.wallet_graphics;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.TransactionInteractor;
import net.mercadosocial.moneda.model.Transaction;
import net.mercadosocial.moneda.views.custom_dialog.TransactionInfoDialog;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 1/03/18.
 */


 public class GraphicsPresenter extends BasePresenter {

     private final GraphicsView view;
    private List<Transaction> transactions;

    public static Intent newGraphicsActivity(Context context) {

         Intent intent = new Intent(context, GraphicsActivity.class);

         return intent;
     }

     public static GraphicsPresenter newInstance(GraphicsView view, Context context) {

         return new GraphicsPresenter(view, context);

     }

     private GraphicsPresenter(GraphicsView view, Context context) {
         super(context, view);

         this.view = view;

     }

     public void onCreate() {

         refreshData();
     }

     public void onResume() {

     }

     public void refreshData() {

         view.setRefreshing(true);

         new TransactionInteractor(context, view).getTransactions100(new BaseInteractor.BaseApiGETListCallback<Transaction>() {
             @Override
             public void onResponse(List<Transaction> list) {
                 transactions = getBalancePerDayOrdered(list);
                 view.showTransactions(transactions);
             }

             @Override
             public void onError(String message) {
                 Toasty.error(context, message).show();
             }
         });

     }

    private List<Transaction> getBalancePerDayOrdered(List<Transaction> listTransactions) {
        List<Transaction> transactions = new ArrayList<>();
        List<String> addedDates = new ArrayList<>();

        for (int i = listTransactions.size()-1; i >= 0; i--) {
            String date = listTransactions.get(i).getDateOnly();
            if (addedDates.contains(date)) {
                continue;
            }

            transactions.add(listTransactions.get(i));
            addedDates.add(date);

            // show 30 days
            if (transactions.size() == 30) {
                break;
            }
        }

        return transactions;
    }

    public void onItemClick(int position) {
        Transaction transaction = transactions.get(position);
        TransactionInfoDialog.newInstance(transaction).show(((AppCompatActivity)context).getSupportFragmentManager(), null);
    }
}
