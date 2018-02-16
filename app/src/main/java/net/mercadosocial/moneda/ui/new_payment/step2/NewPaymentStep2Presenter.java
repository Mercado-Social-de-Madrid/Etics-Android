package net.mercadosocial.moneda.ui.new_payment.step2;

import android.content.Context;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentActivity;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentPresenter;

/**
 * Created by julio on 31/01/18.
 */


 public class NewPaymentStep2Presenter extends BasePresenter {

     private final NewPaymentStep2View view;

    public static NewPaymentStep2Presenter newInstance(NewPaymentStep2View view, Context context) {

         return new NewPaymentStep2Presenter(view, context);

     }

     private NewPaymentStep2Presenter(NewPaymentStep2View view, Context context) {
         super(context, view);

         this.view = view;

     }

     public void onCreate() {


         // todo uncomment this
//         view.enableContinueButton(false);
     }

     public void onResume() {

         refreshData();
     }

     public void refreshData() {


     }

    public void onContinueButtonClick(String totalAmount, String boniatosAmount) {
        Float totalAmountFloat = convertAmount(totalAmount);
        if (!checkMaxAcceptedByEntity(totalAmount)) {
            return;
        }

        Float boniatosAmountFloat = convertAmount(boniatosAmount);
        if (boniatosAmountFloat == null) {
            view.showBoniatosAmountInputError(context.getString(R.string.invalid_number));
            return;
        }

        float maxAcceptedAmount = getSelectedEntity().getMaxAcceptedBoniatosAmount(totalAmountFloat);
        if (boniatosAmountFloat > maxAcceptedAmount) {
            view.showBoniatosAmountInputError(String.format(
                    context.getString(R.string.boniatos_amount_exceed_accepted_payment),
                    getSelectedEntity().getMaxAcceptedBoniatosAmountFormatted(totalAmountFloat)));
            return;
        }

        getNewPaymentPresenter().onAmountsConfirmed(totalAmountFloat, boniatosAmountFloat);
        view.enableContinueButton(true);
    }


    private Float convertAmount(String amount) {

        try {
            Float amountFloat = Float.parseFloat(amount);
            return amountFloat;
        } catch (NumberFormatException e) {
            return null;
        }
    }


    public boolean checkMaxAcceptedByEntity(String totalAmount) {

        Float totalAmountFloat = convertAmount(totalAmount);
        if (totalAmountFloat != null) {
            String amountFormatted = getSelectedEntity().getMaxAcceptedBoniatosAmountFormatted(totalAmountFloat);
            view.showMaxAcceptedByEntity(amountFormatted);
            return true;
        } else {
            view.showTotalAmountInputError(context.getString(R.string.invalid_number));
            return false;
        }
    }

    private Entity getSelectedEntity() {
        return getNewPaymentPresenter().getSelectedEntity();
    }

    private NewPaymentPresenter getNewPaymentPresenter() {
        return (NewPaymentPresenter) ((NewPaymentActivity) context).getBasePresenter();
    }
}