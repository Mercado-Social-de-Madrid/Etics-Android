package net.mercadosocial.moneda.ui.payments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.PaymentInteractor;
import net.mercadosocial.moneda.model.Payment;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 28/02/18.
 */


public class PaymentsPresenter extends BasePresenter {

    private final PaymentsView view;
    private final PaymentInteractor paymentInteractor;
    private List<Payment> payments;

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

        view.setRefresing(true);
        paymentInteractor.getPendingPayments(new BaseInteractor.BaseApiGETListCallback<Payment>() {
            @Override
            public void onResponse(List<Payment> list) {

                if (list.isEmpty()) {
                    Toasty.info(context, context.getString(R.string.no_more_pending_payments)).show();
                    ((Activity)context).finish();
                    return;
                }

                payments = list;
                view.showPendingPayments(payments);
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message).show();
            }
        });

    }

    public void onAcceptPaymentClick(int position) {
        Payment payment = payments.get(position);
        view.showProgressDialog(context.getString(R.string.processing));
        paymentInteractor.acceptPayment(payment.getId(), new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                Toasty.success(context, "OK").show();
                refreshData();
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message).show();
            }
        });
    }

    public void onCancelPaymentClick(int position) {
        Payment payment = payments.get(position);
        view.showProgressDialog(context.getString(R.string.processing));
        paymentInteractor.cancelPayment(payment.getId(), new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {
                Toasty.success(context, "OK").show();
                refreshData();
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message).show();
            }
        });
    }
}
