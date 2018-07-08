package net.mercadosocial.moneda.ui.transactions;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Transaction;

import java.util.List;

/**
 * Created by julio on 1/03/18.
 */

public interface TransactionsView extends BaseView {
    void showTransactions(List<Transaction> transactions);

    void disableMoreElementsRequest();
}
