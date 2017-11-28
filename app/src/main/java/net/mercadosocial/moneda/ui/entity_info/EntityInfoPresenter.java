package net.mercadosocial.moneda.ui.entity_info;

import android.content.Context;
import android.content.Intent;

import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Offer;

/**
 * Created by julio on 28/11/17.
 */


 public class EntityInfoPresenter extends BasePresenter {

    private static final String EXTRA_ID_ENTITY = "extra_id_entity";
    private static final String EXTRA_ENTITY = "extra_entity";

    private final EntityInfoView view;
    private Entity entity;

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
     }

     public void onResume() {

         refreshData();
     }

     public void refreshData() {


     }

    public void onOfferClicked(int position) {
        Offer offer = entity.getOffers().get(position);
        view.toast("Oferta click: " + offer.getTitle());
    }
}
