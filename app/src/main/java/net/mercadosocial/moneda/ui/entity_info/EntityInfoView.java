package net.mercadosocial.moneda.ui.entity_info;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Entity;

/**
 * Created by julio on 28/11/17.
 */

public interface EntityInfoView extends BaseView{

    void showEntityInfo(Entity entity);
}
