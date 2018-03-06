package net.mercadosocial.moneda.ui.payments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Payment;

import java.util.List;

import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInRightAnimator;

public class PaymentsActivity extends BaseActivity implements PaymentsView, PaymentsAdapter.OnItemClickListener {

    private PaymentsPresenter presenter;
    private PaymentsAdapter adapter;
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
        presenter = PaymentsPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paments);

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

    @Override
    public void setRefresing(boolean refresing) {
        viewPayments.setVisibility(refresing ? View.GONE : View.VISIBLE);
        progressPayments.setVisibility(refresing ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showPendingPayments(List<Payment> payments) {
        if (adapter == null) {
            adapter = new PaymentsAdapter(this, payments);
            adapter.setOnItemClickListener(this);
            recyclerPayments.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAcceptPaymentClick(View view, int position) {
        recyclerPayments.setItemAnimator(new ScaleInRightAnimator());
        presenter.onAcceptPaymentClick(position);
    }

    @Override
    public void onCancelClick(View view, int position) {
        recyclerPayments.setItemAnimator(new OvershootInLeftAnimator());
        presenter.onCancelPaymentClick(position);

    }

    @Override
    public void onItemRemoved(int position) {
        adapter.onItemRemoved(position);
    }
}
