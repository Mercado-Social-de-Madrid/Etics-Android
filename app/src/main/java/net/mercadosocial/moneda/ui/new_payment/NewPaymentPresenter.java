package net.mercadosocial.moneda.ui.new_payment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.PaymentInteractor;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Payment;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 19/01/18.
 */


public class NewPaymentPresenter extends BasePresenter {

    private static final String EXTRA_ID_ENTITY = "extra_id_entity";

    private final NewPaymentView view;
    private final PaymentInteractor paymentInteractor;
    private Entity selectedEntity;
    private Payment payment;
    private Integer currentSection;

    public static Intent newNewPaymentActivity(Context context, int idEntity) {

        Intent intent = new Intent(context, NewPaymentActivity.class);
        intent.putExtra(EXTRA_ID_ENTITY, idEntity);

        return intent;
    }

    public static NewPaymentPresenter newInstance(NewPaymentView view, Context context) {

        return new NewPaymentPresenter(view, context);

    }

    private NewPaymentPresenter(NewPaymentView view, Context context) {
        super(context, view);

        this.view = view;
        paymentInteractor = new PaymentInteractor(context, view);

    }

    public void onCreate(Intent intent) {

        payment = new Payment();
        showSection(1);
    }

    public void onResume() {

    }

    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    public Payment getPayment() {
        return payment;
    }

    public void onRecipientSelected(Entity entity) {
        this.selectedEntity = entity;
        payment.setReceiver(entity.getId());
        showSection(2);
    }

    public void onAmountsConfirmed(Float totalAmount, Float boniatosAmount) {
        payment.setTotal_amount(totalAmount);
        payment.setCurrency_amount(boniatosAmount);
        showSection(3);
    }

    public void onIndicatorSectionClick(Integer section) {
        if (section < currentSection) {
            showSection(section);
        } else {
            Toasty.warning(context, context.getString(R.string.click_continue), Toast.LENGTH_SHORT).show();
        }
    }

    private void showSection(Integer section) {
        currentSection = section;
        view.showSection(section);
    }

    public void onConfirmPayment(String pin) {

//        Toasty.info(context, context.getString(R.string.sending_payment)).show();
        view.showProgressDialog(context.getString(R.string.sending_payment));
        paymentInteractor.sendPayment(payment, new BaseInteractor.BaseApiPOSTCallback() {
            @Override
            public void onSuccess(Integer id) {

                showPaymentSuccessDialog();

            }

            @Override
            public void onError(String message) {

                Toasty.error(context, context.getString(R.string.payment_fail)).show();

            }
        });
    }

    private void showPaymentSuccessDialog() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.payment_done)
                .setMessage(String.format(context.getString(R.string.payment_done_message),
                        selectedEntity.getName(), selectedEntity.getBonusFormatted(payment.getTotal_amount())))
                .setNeutralButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)context).finish();
                    }
                })
                .show();
    }
}
