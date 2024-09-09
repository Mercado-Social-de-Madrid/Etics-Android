package net.mercadosocial.moneda.ui.entities;

import net.mercadosocial.moneda.model.Entity;

import java.util.List;

public interface EntitiesChild {

    void updateEntities(List<Entity> entities);

    void onError(boolean showEmptyView);

    void setRefreshing(boolean refreshing);
}
