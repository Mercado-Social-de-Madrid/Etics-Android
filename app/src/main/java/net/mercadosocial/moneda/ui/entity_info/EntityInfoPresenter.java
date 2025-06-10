package net.mercadosocial.moneda.ui.entity_info;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.CategoriesInteractor;
import net.mercadosocial.moneda.interactor.EntityInteractor;
import net.mercadosocial.moneda.model.Category;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Offer;
import net.mercadosocial.moneda.ui.novelties.detail.NoveltyDetailPresenter;
import net.mercadosocial.moneda.util.WebUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by julio on 28/11/17.
 */


public class EntityInfoPresenter extends BasePresenter {

    private static final String EXTRA_ID_ENTITY = "extra_id_entity";
//    private static final String EXTRA_ENTITY = "extra_entity";

    private final EntityInfoView view;
    private final EntityInteractor entityInteractor;
    private final CategoriesInteractor categoriesInteractor;
    private Entity entity;
    private Data data;

    public static void startEntityInfoActivity(Context context, String entityId) {
        Intent intent = new Intent(context, EntityInfoActivity.class);
        intent.putExtra(EXTRA_ID_ENTITY, entityId);
        context.startActivity(intent);
    }

    public static EntityInfoPresenter newInstance(EntityInfoView view, Context context) {

        return new EntityInfoPresenter(view, context);

    }

    private EntityInfoPresenter(EntityInfoView view, Context context) {
        super(context, view);

        this.view = view;
        entityInteractor = new EntityInteractor(context, view);
        categoriesInteractor = new CategoriesInteractor(context, view);

    }

    public void onCreate(Intent intent) {

        String entityId = intent.getStringExtra(EXTRA_ID_ENTITY);
        for (Entity entity1 : entityInteractor.getCachedEntities()) {
            if (entity1.getId().equals(entityId)) {
                entity = entity1;
                processCategories();
                break;
            }
        }

        if (entity == null) {
            return;
        }

        view.showEntityInfo(entity);

        data = App.getUserData(context);

        processBenefits();

        view.setBalanceBadge(getApp().getCurrentNode().getBalanceBadge());

    }


    private void processCategories() {
        List<Category> categories = categoriesInteractor.getSavedCategories();
        Map<String, Category> categoriesMap = new HashMap<>();

        if (categories == null || categories.isEmpty()) {
            return;
        } else {
            for (Category category : categories) {
                categoriesMap.put(category.getId(), category);
            }
        }

        if (entity.getCategories() == null || entity.getCategories().isEmpty()) {
            return;
        }

        for (String categoryId : entity.getCategories()) {
            Category category = categoriesMap.get(categoryId);
            if (category != null) {
                entity.addCategoryFull(category);
            }
        }

    }

    private void processBenefits() {

        if (entity.getBenefit() == null || !entity.getBenefit().isActive()) {
            view.showBenefitsInfo(null, false, getString(R.string.no_benefit_currently));
            return;
        }

        // No logged in user
        if (data == null) {
            view.showBenefitsInfo(null, false, getString(R.string.benefits_available_for_active_members));
            return;
        }

        // Inactive members
        if ((data.isEntity() && !data.getEntity().isActive()) || (!data.isEntity() && !data.getPerson().isActive())) {
            view.showBenefitsInfo(null, false, getString(R.string.benefits_available_for_active_members));
            return;
        }

        if (!data.isEntity() && data.getPerson().isIntercoop() && !entity.getBenefit().includesIntercoopMembers()) {
            view.showBenefitsInfo(null, false, getString(R.string.benefits_not_for_intercoop));
            return;
        }

        view.showBenefitsInfo(entity.getBenefit(), data.isEntity(), null);

    }


    public void onOfferClicked(int position) {
        Offer offer = entity.getOffers().get(position);
        context.startActivity(NoveltyDetailPresenter.newNoveltyDetailActivity(context, offer));
    }

    public void onBenefitLinkClick(String benefitLink) {

        Data data = App.getUserData(context);
        benefitLink = benefitLink.trim();

        try {
            if (Patterns.WEB_URL.matcher(benefitLink).matches()) {
                WebUtils.openLink(context, benefitLink);
            } else if (Patterns.EMAIL_ADDRESS.matcher(benefitLink).matches()) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri intentData = Uri.parse("mailto:" + benefitLink + "?subject=" + context.getString(
                        R.string.benefit_request_email_subject, data.getAccount().getMemberId())); // &body=
                intent.setData(intentData);

                context.startActivity(intent);
            } else {
                view.toast(R.string.invalid_link);
            }
        } catch (ActivityNotFoundException e) {
            view.toast(R.string.no_app_to_open_link);
        }
    }
}
