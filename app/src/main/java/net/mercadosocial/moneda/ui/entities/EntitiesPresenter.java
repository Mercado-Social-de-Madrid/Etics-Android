package net.mercadosocial.moneda.ui.entities;

import android.content.Context;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.EntityInteractor;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.entity_info.EntityInfoPresenter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by julio on 11/10/17.
 */


 public class EntitiesPresenter extends BasePresenter {

     private final EntitiesView view;
    private final EntityInteractor entityInteractor;
    private List<Entity> entities = new ArrayList<>();

    public static EntitiesPresenter newInstance(EntitiesView view, Context context) {

         return new EntitiesPresenter(view, context);

     }

     private EntitiesPresenter(EntitiesView view, Context context) {
         super(context, view);

         this.view = view;

         entityInteractor = new EntityInteractor(context, view);

     }

     public void onCreate() {
         refreshData();
     }

     public void onResume() {

//         List<Entity> users = getUserList();
//         List<Entity> result = EntityFilter.builder()
//                 .name().contains("")
//                 .on(entities);
     }

     public void refreshData() {

         entityInteractor.getEntities(new EntityInteractor.Callback() {

             @Override
             public void onResponse(List<Entity> entitiesApi) {
                 entities.clear();
                 entities.addAll(entitiesApi);

                 view.showEntities(entities);
             }

             @Override
             public void onError(String error) {
                view.toast(error);
             }
         });

     }

    public void onSearch(String query) {

        view.showProgressDialog(context.getString(R.string.searching));

        entityInteractor.getEntitiesFiltered(query, new EntityInteractor.Callback() {

            @Override
            public void onResponse(List<Entity> entitiesApi) {
                entities.clear();
                entities.addAll(entitiesApi);

                view.showEntities(entities);
            }

            @Override
            public void onError(String error) {
                view.toast(error);
            }
        });
    }

    public void onEntityClicked(int position, String id) {

        Entity entity = null;
        if (position == -1) {
            entity = findEntityById(id);
        } else {
            entity = entities.get(position);
        }

        EntityInfoPresenter.startEntityInfoActivity(context, entity);
    }

    private Entity findEntityById(String id) {
        for (Entity entity : entities) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    public void onEntityFavouriteClicked(int position, String id) {

        Entity entity = entities.get(position);
//        view.toast("Entity Fav: " + entity.getName());
    }
}
