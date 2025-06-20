package net.mercadosocial.moneda.ui.entities.filter;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.CategoriesInteractor;
import net.mercadosocial.moneda.model.Category;
import net.mercadosocial.moneda.model.FilterEntities;
import net.mercadosocial.moneda.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class FilterEntitiesPresenter extends BasePresenter {

    private final FilterEntitiesView view;
    private List<Category> categories = new ArrayList<>();

    public static FilterEntitiesPresenter newInstance(FilterEntitiesView view, Context context) {

        return new FilterEntitiesPresenter(view, context);

    }

    private FilterEntitiesPresenter(FilterEntitiesView view, Context context) {
        super(context, view);

        this.view = view;

    }

    public void onCreate() {
        updateCategories();
    }

    public void onResume() {

    }

    public void onNodeChanged() {

        resetFilter();
        updateCategories();
    }

    private void updateCategories() {
        new CategoriesInteractor(context, view).getCategories(new BaseInteractor.BaseApiGETListCallback<Category>() {
            @Override
            public void onResponse(List<Category> list) {

                categories.clear();
                categories.addAll(list);
                view.showCategories(categories);
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "updateCategories onError: " + message);
            }
        });
    }

    public void applyFilter(String text, boolean onlyFavs, boolean withBenefits, boolean withBadge) {
        FilterEntities filterEntities = new FilterEntities();
        filterEntities.setText(TextUtils.isEmpty(text) ? null : text);

        List<String> categoriesIds = new ArrayList<>();
        for (Category category : categories) {
            if (category.isChecked()) {
                categoriesIds.add(category.getId());
            }
        }

        filterEntities.setCategoriesIds(categoriesIds);

        if (categoriesIds.size() == categories.size()) {
            filterEntities.getCategoriesIds().clear();
        }

        filterEntities.setOnlyFavourites(onlyFavs);
        filterEntities.setWithBenefits(withBenefits);
        filterEntities.setWithBadge(withBadge);

        ((MainActivity)context).setFilterEntities(filterEntities, true);
    }

    public void onRemoveFilterClick() {
        resetFilter();
        ((MainActivity)context).setFilterEntities(null, false);
    }

    public void resetFilter() {

        for (Category category : categories) {
            category.setChecked(false);
        }

        view.showCategories(categories);
        view.resetFilter();
    }
}
