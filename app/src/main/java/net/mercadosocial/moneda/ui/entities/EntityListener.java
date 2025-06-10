package net.mercadosocial.moneda.ui.entities;

import net.mercadosocial.moneda.model.Entity;

public interface EntityListener {
    void onEntityClick(Entity entity);

    void onEntityFavouriteClick(int position, boolean isFavourite);
}
