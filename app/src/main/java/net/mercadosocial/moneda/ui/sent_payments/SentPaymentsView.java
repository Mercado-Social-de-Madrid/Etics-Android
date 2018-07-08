package net.mercadosocial.moneda.ui.sent_payments;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Payment;

import java.util.List;

/**
 * Created by julio on 28/02/18.
 */

public interface SentPaymentsView extends BaseView {
    void showPendingPayments(List<Payment> payments);
}
