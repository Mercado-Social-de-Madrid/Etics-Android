package net.mercadosocial.moneda.ui.sent_payments;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Payment;

import java.util.List;

import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;

public class SentPaymentsActivity extends BaseActivity implements SentPaymentsView {

    private SentPaymentsPresenter presenter;
    private SentPaymentsAdapter adapter;
    private LinearLayout viewPayments;
    private RecyclerView recyclerPayments;
    private ProgressBar progressPayments;

    private void findViews() {
        viewPayments = (LinearLayout)findViewById( R.id.view_payments );
        recyclerPayments = (RecyclerView)findViewById( R.id.recycler_payments );
        progressPayments = (ProgressBar)findViewById( R.id.progress_payments );
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = SentPaymentsPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_paments);

        configureSecondLevelActivity();
        findViews();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerPayments.setLayoutManager(layoutManager);
        recyclerPayments.setItemAnimator(new OvershootInRightAnimator());

        presenter.onCreate();

    }

    @Override
    public void refreshData() {
        presenter.refreshData();
    }

//    @Override
//    public void setRefreshing(boolean refresing) {
//        viewPayments.setVisibility(refresing ? View.GONE : View.VISIBLE);
//        progressPayments.setVisibility(refresing ? View.VISIBLE : View.GONE);
//    }

    @Override
    public void showPendingPayments(List<Payment> payments) {
        if (adapter == null) {
            adapter = new SentPaymentsAdapter(this, payments);
            recyclerPayments.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

}
