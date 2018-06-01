package net.mercadosocial.moneda.ui.wallet_graphics;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Transaction;

import java.util.List;

/**
 * Created by julio on 1/03/18.
 */

public interface GraphicsView extends BaseView {
    void showTransactions(List<Transaction> transactions);
}
