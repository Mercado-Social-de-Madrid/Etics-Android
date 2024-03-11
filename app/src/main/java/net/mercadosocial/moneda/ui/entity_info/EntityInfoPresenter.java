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
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Offer;
import net.mercadosocial.moneda.ui.novelties.detail.NoveltyDetailPresenter;
import net.mercadosocial.moneda.util.Util;
import net.mercadosocial.moneda.util.WebUtils;

/**
 * Created by julio on 28/11/17.
 */


 public class EntityInfoPresenter extends BasePresenter {

    private static final String EXTRA_ID_ENTITY = "extra_id_entity";
    private static final String EXTRA_ENTITY = "extra_entity";

    private final EntityInfoView view;
    private Entity entity;
    private Data data;

    public static void startEntityInfoActivity(Context context, Entity entity) {
        Intent intent = new Intent(context, EntityInfoActivity.class);
        intent.putExtra(EXTRA_ENTITY, entity);
        context.startActivity(intent);
    }

     public static EntityInfoPresenter newInstance(EntityInfoView view, Context context) {

         return new EntityInfoPresenter(view, context);

     }

     private EntityInfoPresenter(EntityInfoView view, Context context) {
         super(context, view);

         this.view = view;

     }

     public void onCreate(Intent intent) {

         entity = (Entity) intent.getSerializableExtra(EXTRA_ENTITY);
         view.showEntityInfo(entity);

         data = App.getUserData(context);

         processBenefits();
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
