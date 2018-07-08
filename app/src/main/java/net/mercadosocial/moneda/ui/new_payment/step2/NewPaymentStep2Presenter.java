package net.mercadosocial.moneda.ui.new_payment.step2;

import android.content.Context;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.WalletInteractor;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Wallet;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentActivity;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentPresenter;
import net.mercadosocial.moneda.util.Util;

/**
 * Created by julio on 31/01/18.
 */


 public class NewPaymentStep2Presenter extends BasePresenter {

     private final NewPaymentStep2View view;
    private Wallet wallet;

    public static NewPaymentStep2Presenter newInstance(NewPaymentStep2View view, Context context) {

         return new NewPaymentStep2Presenter(view, context);

     }

     private NewPaymentStep2Presenter(NewPaymentStep2View view, Context context) {
         super(context, view);

         this.view = view;

     }

     public void onCreate() {

         refreshData();
     }

     public void onResume() {

     }

     public void refreshData() {

         new WalletInteractor(context, view).getWallet(new WalletInteractor.Callback() {
             @Override
             public void onResponse(Wallet walletReceived) {
                 wallet = walletReceived;
             }

             @Override
             public void onError(String error) {

             }
         });

     }

    public void onContinueButtonClick(String totalAmount, String boniatosAmount, String concept) {
        Float totalAmountFloat = convertAmount(totalAmount);
        if (!checkMaxAcceptedByEntity(totalAmount, false)) {
            return;
        }

        if (boniatosAmount.isEmpty()) {
            boniatosAmount = "0";
        }

        Float boniatosAmountFloat = convertAmount(boniatosAmount);
        if (boniatosAmountFloat == null) {
            view.showBoniatosAmountInputError(context.getString(R.string.invalid_number));
            return;
        }

        if (boniatosAmountFloat < 0) {
            view.showBoniatosAmountInputError(context.getString(R.string.amount_cant_be_negative));
            return;
        }

        float maxAcceptedAmount = getSelectedEntity().getMaxAcceptedBoniatosAmount(totalAmountFloat);
        if (boniatosAmountFloat > maxAcceptedAmount) {
            view.showBoniatosAmountInputError(String.format(
                    context.getString(R.string.boniatos_amount_exceed_accepted_payment),
                    getSelectedEntity().getMaxAcceptedBoniatosAmountFormatted(totalAmountFloat)));
            return;
        }

        if (wallet != null) {
            if (boniatosAmountFloat > wallet.getBalance()) {
                view.showBoniatosAmountInputError(String.format(context.getString(R.string.yout_balance_is),
                        wallet.getBalanceFormatted()));
                return;
            }
        }

        getNewPaymentPresenter().onAmountsConfirmed(totalAmountFloat, boniatosAmountFloat, concept);
        view.enableContinueButton(true);
    }


    private Float convertAmount(String amount) {

        try {
            Float amountFloat = Float.parseFloat(amount.replace(",", "."));
            return amountFloat;
        } catch (NumberFormatException e) {
            return null;
        }
    }


    public boolean checkMaxAcceptedByEntity(String totalAmount, boolean updateSuggestedBoniatosAmount) {

        Float totalAmountFloat = convertAmount(totalAmount);
        if (totalAmountFloat != null) {

            if (totalAmountFloat <= 0) {
                view.showTotalAmountInputError(context.getString(R.string.amount_must_be_positive));
                return false;
            }

            String amountFormatted = getSelectedEntity().getMaxAcceptedBoniatosAmountFormatted(totalAmountFloat);
            view.showBoniatosRestrictions(amountFormatted, wallet != null ? wallet.getBalanceFormatted() : "?");

            if (updateSuggestedBoniatosAmount && wallet != null) {
                Float minAmount = Math.min(getSelectedEntity().getMaxAcceptedBoniatosAmount(totalAmountFloat), wallet.getBalance());
                view.showPresetBoniatosAmount(Util.getDecimalFormatted(minAmount, false));
            }
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
