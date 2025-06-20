package net.mercadosocial.moneda.ui.entities;

import android.content.Context;
import android.util.Log;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.CategoriesInteractor;
import net.mercadosocial.moneda.interactor.EntityInteractor;
import net.mercadosocial.moneda.interactor.UserInteractor;
import net.mercadosocial.moneda.model.Category;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.FilterEntities;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.ui.entity_info.EntityInfoPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by julio on 11/10/17.
 */


public class EntitiesPresenter extends BasePresenter {

    private final EntitiesView view;
    private final EntityInteractor entityInteractor;
    private final UserInteractor userInteractor;
    private final CategoriesInteractor categoriesInteractor;
    private List<Entity> entities = new ArrayList<>();
    private FilterEntities filterEntities;
    private boolean refreshFavouritesAtEnd;

    public static final int SCREEN_ENTITIES_TYPE_LIST = 0;
    public static final int SCREEN_ENTITIES_TYPE_MAP = 1;

    private int currentScreen = SCREEN_ENTITIES_TYPE_LIST;


    public static EntitiesPresenter newInstance(EntitiesView view, Context context) {

        return new EntitiesPresenter(view, context);

    }

    private EntitiesPresenter(EntitiesView view, Context context) {
        super(context, view);

        this.view = view;

        entityInteractor = new EntityInteractor(context, view);
        userInteractor = new UserInteractor(context, view);
        categoriesInteractor = new CategoriesInteractor(context, view);

    }

    public void onCreate() {
        view.showScreenType(currentScreen);

    }

    public void onResume() {

        Log.i(TAG, "onResume: isNewLaunch:" + getApp().isNewLaunch());

        if (getApp().isNewLaunch()) {
            refreshData();
            getApp().setNewLaunch(false);
        }

        if (entities != null && !entities.isEmpty()) {
            processFavs();
            processLocalFilter();
            view.updateEntities(entities);
        }
    }

    public void onPause() {
        if (refreshFavouritesAtEnd) {
            updateFavouritesOnServer();
            refreshFavouritesAtEnd = false;
        }

    }

    private void updateFavouritesLocally() {

        Data data = App.getUserData(context);

        if (data == null) {
            return;
        }

        List<String> favEntitiesUpdated = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.isFavourite()) {
                favEntitiesUpdated.add(entity.getId());
            }
        }

        data.setFav_entities(favEntitiesUpdated);
        App.saveUserData(context, data);
    }

    private void updateFavouritesOnServer() {
        Data data = App.getUserData(context);

        if (data == null) {
            return;
        }

        List<String> favEntitiesUpdated = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.isFavourite()) {
                favEntitiesUpdated.add(entity.getId());
            }
        }

        if (!data.isEntity()) {
            Person profile = Person.createPersonProfileFavourites(favEntitiesUpdated);
            userInteractor.updatePerson(profile, new BaseInteractor.BaseApiPOSTCallback() {
                @Override
                public void onSuccess(Integer id) {

                }

                @Override
                public void onError(String message) {
                    Log.e(TAG, "onError: updateFavouritesOnServer. " + message);
                }
            });
        }

    }

    /**
     * Objetivo: carga de datos rápida por caché, orden aleatorio de entidades en cada carga, integrar bien con filtro y vista de progreso al sincronizar.
     * Casos:
     * - Sin caché guardada -> mostrar progreso, sincronizar con api, cargar datos con orden aleatorio
     * - Con cache guardado -> cargar datos cache con orden aleatorio, no mostrar progreso, sincronizar y guardar en caché sin recargar datos (se perdería el orden aleatorio anterior y haría un efecto no deseado para el usuario al aparecer de repente entidades nuevas en la lista)
     * - Se realiza una búsqueda -> se hace petición al api mostrando progreso, los resultados no se cachean ni se hace aleatorio el orden ya que vienen el api por orden de relevancia por similitud semántica.
     * - Se resetea la búsqueda -> se recargan los datos de caché con orden aleatorio
     */

    public void loadCacheData() {
        List<Entity> entitiesCached = entityInteractor.getCachedEntities();

        if (entitiesCached != null && !entitiesCached.isEmpty()) {
            Collections.shuffle(entitiesCached);
            loadEntities(entitiesCached);
        }
    }

    public void refreshData() {
        refreshEntities();
    }

    private void refreshEntities() {

        List<Entity> entitiesCached = entityInteractor.getCachedEntities();

        final boolean loadAfterSync = entitiesCached == null || entitiesCached.isEmpty() || filterEntities != null;
        if (loadAfterSync) {
            view.setRefreshing(true);
        }

        Log.i(TAG, ">> syncing");

        entityInteractor.getEntities(filterEntities, new EntityInteractor.Callback() {

            @Override
            public void onResponse(List<Entity> entitiesApi, boolean hasMore) {
                Log.i(TAG, ">> sync finished");
                if (loadAfterSync) {
                    Log.i(TAG, ">> loading entities");
                    loadEntities(entitiesApi);
                }
            }

            @Override
            public void onError(String error) {

                boolean showEmptyView = entities.isEmpty();
                view.onError(showEmptyView);

                view.toast(error);

            }
        });
    }

    private void loadEntities(List<Entity> entitiesUpdated) {

        if (entitiesUpdated == null) {
            return;
        }

        entities.clear();
        entities.addAll(entitiesUpdated);

        processFavs();
        processLocalFilter();
        processCategories();

        view.updateEntities(entities);
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

        for (Entity entity : entities) {
            if (entity.getCategories() == null || entity.getCategories().isEmpty()) {
                continue;
            }

            for (String categoryId : entity.getCategories()) {
                Category category = categoriesMap.get(categoryId);
                if (category != null) {
                    entity.addCategoryFull(category);
                }
            }
        }
    }

    private void processLocalFilter() {
        if (filterEntities != null) {
            List<Entity> entitiesFiltered = entities.stream()
                    .filter(entity -> !filterEntities.isWithBenefits() || entity.getBenefit() != null)
                    .filter(entity -> !filterEntities.isWithBadge() || entity.getBalanceUrl() != null)
                    .collect(Collectors.toList());
            entities.clear();
            entities.addAll(entitiesFiltered);
        }
    }

    private void processFavs() {

        Data data = App.getUserData(context);
        if (data == null) {
            return;
        }

        List<Entity> entitiesToRemove = new ArrayList<>();

        for (Entity entity : entities) {
            if (data.getFav_entities() != null && data.getFav_entities().contains(entity.getId())) {
                entity.setFavourite(true);
            } else if (filterEntities != null && filterEntities.isOnlyFavourites()) {
                entitiesToRemove.add(entity);
            }
        }

        entities.removeAll(entitiesToRemove);
    }


    public void onEntityClicked(Entity entity) {

        EntityInfoPresenter.startEntityInfoActivity(context, entity.getId());
    }

    private Entity findEntityById(String id) {
        for (Entity entity : entities) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    public void onEntityFavouriteClicked(int position, boolean isFavourite) {

        entities.get(position).setFavourite(isFavourite);
        refreshFavouritesAtEnd = true;
        updateFavouritesLocally();

    }

    public void setFilterEntities(FilterEntities filterEntities) {
        boolean clearingFilter = this.filterEntities != null && filterEntities == null;
        this.filterEntities = filterEntities;
        if (clearingFilter) {
            loadCacheData();
        }
    }

    public void onMapListButtonClick() {
        currentScreen = currentScreen == SCREEN_ENTITIES_TYPE_LIST ? SCREEN_ENTITIES_TYPE_MAP : SCREEN_ENTITIES_TYPE_LIST;
        view.showScreenType(currentScreen);
    }

}
