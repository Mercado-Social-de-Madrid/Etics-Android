package net.mercadosocial.moneda.ui.payments;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Payment;

import java.util.List;

/**
 * Created by julio on 28/02/18.
 */

public interface PaymentsView extends BaseView {
    void showPendingPayments(List<Payment> payments);

    void onItemRemoved(int position);
}
