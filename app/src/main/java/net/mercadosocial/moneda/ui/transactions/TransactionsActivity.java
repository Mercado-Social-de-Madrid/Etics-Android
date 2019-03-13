package net.mercadosocial.moneda.ui.transactions;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Transaction;

import java.util.List;

public class TransactionsActivity extends BaseActivity implements TransactionsView, TransactionsAdapter.OnItemClickListener {

    private TransactionsPresenter presenter;
    private SuperRecyclerView recyclerTransactions;
    private TransactionsAdapter adapter;
    private View viewEmptyList;

    private void findViews() {
        recyclerTransactions = findViewById(R.id.recycler_transactions);
        viewEmptyList = findViewById(R.id.view_empty_list);
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

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_horizontal));
        recyclerTransactions.addItemDecoration(divider);

        recyclerTransactions.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                presenter.loadNextPage();
            }
        }, 5);

        presenter.onCreate();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        presenter.refreshData();
    }


    @Override
    public void onItemClick(View view, int position) {
        presenter.onItemClick(position);
    }

    @Override
    public void showTransactions(List<Transaction> transactions) {

        if (adapter == null) {
            adapter = new TransactionsAdapter(this, transactions);
            adapter.setOnItemClickListener(this);
            recyclerTransactions.setAdapter(adapter);
        } else {
            adapter.updateData(transactions);
        }

        viewEmptyList.setVisibility(transactions == null || transactions.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void disableMoreElementsRequest() {
        recyclerTransactions.setupMoreListener(null, 0);
    }
}
