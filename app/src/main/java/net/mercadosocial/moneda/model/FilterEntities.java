package net.mercadosocial.moneda.model;

import java.util.List;

public class FilterEntities {

    private String text;
    private List<String> categoriesIds;
    private boolean onlyFavourites;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getCategoriesIds() {
        return categoriesIds;
    }

    public void setCategoriesIds(List<String> categoriesIds) {
        this.categoriesIds = categoriesIds;
    }

    public boolean isOnlyFavourites() {
        return onlyFavourites;
    }

    public void setOnlyFavourites(boolean onlyFavourites) {
        this.onlyFavourites = onlyFavourites;
    }
}
