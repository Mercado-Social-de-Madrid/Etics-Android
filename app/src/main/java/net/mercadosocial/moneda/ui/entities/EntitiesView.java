package net.mercadosocial.moneda.ui.entities;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Entity;

import java.util.List;

/**
 * Created by julio on 11/10/17.
 */

public interface EntitiesView extends BaseView {

    void updateEntities(List<Entity> entities);

    void onError(boolean showEmptyView);

    void showScreenType(int currentScreen);

}
