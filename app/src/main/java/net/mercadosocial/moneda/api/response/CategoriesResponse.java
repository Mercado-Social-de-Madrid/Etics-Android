package net.mercadosocial.moneda.api.response;

import net.mercadosocial.moneda.model.Category;

import java.util.List;

/**
 * Created by julio on 11/10/17.
 */

public class CategoriesResponse {

    private List<Category> categories;
    private Meta meta;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Category> getCategories() {
        return categories;
    }

}
