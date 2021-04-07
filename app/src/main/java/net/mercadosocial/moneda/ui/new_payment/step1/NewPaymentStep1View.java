package net.mercadosocial.moneda.ui.new_payment.step1;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Entity;

import java.util.List;

/**
 * Created by julio on 30/01/18.
 */

public interface NewPaymentStep1View extends BaseView {

    void enableContinueButton(boolean enable);

    void showEntities(List<Entity> entities);

    void showProgress(boolean show);
}
