package net.mercadosocial.moneda.ui.new_payment.step2;

import net.mercadosocial.moneda.base.BaseView;

/**
 * Created by julio on 31/01/18.
 */

public interface NewPaymentStep2View extends BaseView {

    void showMaxAcceptedByEntity(String amount);

    void showTotalAmountInputError(String error);

    void showBoniatosAmountInputError(String error);

    void enableContinueButton(boolean enable);
}
