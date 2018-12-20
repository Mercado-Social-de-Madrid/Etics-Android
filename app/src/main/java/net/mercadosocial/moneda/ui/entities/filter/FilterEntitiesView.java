package net.mercadosocial.moneda.ui.entities.filter;

import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Category;

import java.util.List;

public interface FilterEntitiesView extends BaseView {

    void showCategories(List<Category> categories);
}
