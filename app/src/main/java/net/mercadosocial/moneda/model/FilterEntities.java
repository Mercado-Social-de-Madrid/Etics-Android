package net.mercadosocial.moneda.model;

import java.util.ArrayList;
import java.util.List;

public class FilterEntities {

    private String text;
    private List<String> categoriesIds;
    private boolean onlyFavourites;
    private boolean withBenefits;
    private boolean acceptsEtics;
    private boolean withBadge;

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

    public boolean isAcceptsEtics() {
        return acceptsEtics;
    }

    public void setAcceptsEtics(boolean acceptsEtics) {
        this.acceptsEtics = acceptsEtics;
    }

    public boolean isWithBenefits() {
        return withBenefits;
    }

    public void setWithBenefits(boolean withBenefits) {
        this.withBenefits = withBenefits;
    }

    public boolean isWithBadge() {
        return withBadge;
    }

    public void setWithBadge(boolean withBadge) {
        this.withBadge = withBadge;
    }
}
