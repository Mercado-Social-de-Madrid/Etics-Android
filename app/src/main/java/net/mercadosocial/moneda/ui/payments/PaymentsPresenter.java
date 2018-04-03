package net.mercadosocial.moneda.ui.payments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

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


public class PaymentsPresenter extends BasePresenter {

    private final PaymentsView view;
    private final PaymentInteractor paymentInteractor;
    private List<Payment> payments = new ArrayList<>();

    public static Intent newPaymentsActivity(Context context) {

        Intent intent = new Intent(context, PaymentsActivity.class);

        return intent;
    }

    public static PaymentsPresenter newInstance(PaymentsView view, Context context) {

        return new PaymentsPresenter(view, context);

    }

    private PaymentsPresenter(PaymentsView view, Context context) {
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
        paymentInteractor.getPendingPayments(new BaseInteractor.BaseApiGETListCallback<Payment>() {
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

    public void onAcceptPaymentClick(final int position) {

        payments.get(position).setBlockButtons(true);
        view.showPendingPayments(payments);


        final Payment payment = payments.get(position);
        view.setRefreshing(true);
        paymentInteractor.acceptPayment(payment.getId(), new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                removeAndCheckFinish(position);
            }

            @Override
            public void onError(String message) {

                payments.get(position).setBlockButtons(false);
                view.showPendingPayments(payments);
                Toasty.error(context, message).show();
            }
        });
    }

    public void onCancelPaymentClick(final int position) {

        payments.get(position).setBlockButtons(true);
        view.showPendingPayments(payments);

        Payment payment = payments.get(position);
        view.setRefreshing(true);
        paymentInteractor.cancelPayment(payment.getId(), new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                removeAndCheckFinish(position);
            }

            @Override
            public void onError(String message) {
                payments.get(position).setBlockButtons(false);
                view.showPendingPayments(payments);
                Toasty.error(context, message).show();
            }
        });
    }

    private void removeAndCheckFinish(int position) {

        payments.remove(position);
        view.onItemRemoved(position);
        if (payments.isEmpty()) {
            Toasty.success(context, context.getString(R.string.finish_pending_payments)).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 300);
        } else {
            Toasty.success(context, "OK").show();
        }
    }

}
