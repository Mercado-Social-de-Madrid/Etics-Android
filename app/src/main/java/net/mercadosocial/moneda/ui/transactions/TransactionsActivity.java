package net.mercadosocial.moneda.ui.transactions;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Transaction;

import java.util.List;

public class TransactionsActivity extends BaseActivity implements TransactionsView {

    private TransactionsPresenter presenter;
    private RecyclerView recyclerTransactions;
    private TransactionsAdapter adapter;

    private void findViews() {
        recyclerTransactions = (RecyclerView) findViewById(R.id.recycler_transactions);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = TransactionsPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        configureSecondLevelActivity();
        findViews();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerTransactions.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerTransactions.addItemDecoration(divider);

        presenter.onCreate();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void showTransactions(List<Transaction> transactions) {

        if (adapter == null) {
            adapter = new TransactionsAdapter(this, transactions);
            recyclerTransactions.setAdapter(adapter);
        } else {
            adapter.updateData(transactions);
        }
    }
}
