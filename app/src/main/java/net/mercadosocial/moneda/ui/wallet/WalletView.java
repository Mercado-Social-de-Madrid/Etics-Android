package net.mercadosocial.moneda.ui.wallet;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Wallet;

/**
 * Created by julio on 2/02/18.
 */

public interface WalletView extends BaseView {

    void showUserInfo(String username);

    void showWalletData(boolean showLoading, Wallet wallet);

    void showPendingPaymentsNumber(int numberPendingPayments);
}
