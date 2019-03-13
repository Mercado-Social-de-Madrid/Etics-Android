package net.mercadosocial.moneda.ui.new_payment;

import net.mercadosocial.moneda.base.BaseView;

/**
 * Created by julio on 19/01/18.
 */

public interface NewPaymentView extends BaseView {

    void showSection(int section);

    void onQRScanned(String url);
}
