package net.mercadosocial.moneda.ui.entities;

public interface EntityListener {
    void onEntityClick(int position, String id);

    void onEntityFavouriteClick(int position, boolean isFavourite);
}
