package net.mercadosocial.moneda.ui.sent_payments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.PaymentInteractor;
import net.mercadosocial.moneda.model.Payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 28/02/18.
 */


public class SentPaymentsPresenter extends BasePresenter {

    private final SentPaymentsView view;
    private final PaymentInteractor paymentInteractor;
    private List<Payment> payments = new ArrayList<>();

    public static Intent newSentPaymentsActivity(Context context) {

        Intent intent = new Intent(context, SentPaymentsActivity.class);

        return intent;
    }

    public static SentPaymentsPresenter newInstance(SentPaymentsView view, Context context) {

        return new SentPaymentsPresenter(view, context);

    }

    private SentPaymentsPresenter(SentPaymentsView view, Context context) {
        super(context, view);

        this.view = view;
        paymentInteractor = new PaymentInteractor(context, view);

    }

    public void onCreate() {

        refreshData();
    }

    public void onResume() {

    }

    public void refreshData() {

        view.setRefreshing(true);
        paymentInteractor.getSentPayments(new BaseInteractor.BaseApiGETListCallback<Payment>() {
            @Override
            public void onResponse(List<Payment> list) {

                if (list.isEmpty()) {
                    Toasty.info(context, context.getString(R.string.no_more_pending_payments)).show();
                    ((Activity)context).finish();
                    return;
                }

                payments.clear();
                payments.addAll(list);
                Collections.sort(payments);
                view.showPendingPayments(payments);
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message).show();
            }
        });

    }



}
