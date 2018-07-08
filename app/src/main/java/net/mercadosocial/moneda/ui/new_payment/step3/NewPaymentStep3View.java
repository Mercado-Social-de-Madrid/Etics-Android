package net.mercadosocial.moneda.ui.new_payment.step3;

import net.mercadosocial.moneda.base.BaseView;

/**
 * Created by julio on 1/02/18.
 */

public interface NewPaymentStep3View extends BaseView {

    void showPaymentSummaryInfo(String boniatosAmount, String eurosAmount, String entityName, String bonus, String concept);
}
