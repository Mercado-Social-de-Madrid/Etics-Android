package net.mercadosocial.moneda.ui.entities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.entities.list.EntitiesListFragment;
import net.mercadosocial.moneda.ui.entities.map.EntitiesMapFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 21/02/18.
 */

public class EntitiesPagerAdapter extends FragmentStatePagerAdapter {

    private List<EntitiesView> entitiesFragments = new ArrayList<>();

    public EntitiesPagerAdapter(FragmentManager fm, EntityListener entityListener) {
        super(fm);

        EntitiesListFragment listFragment = new EntitiesListFragment();
        listFragment.setEntityListener(entityListener);
        entitiesFragments.add(listFragment);

        EntitiesMapFragment mapFragment = new EntitiesMapFragment();
        mapFragment.setEntityListener(entityListener);
        entitiesFragments.add(mapFragment);

    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) entitiesFragments.get(position);
    }

    @Override
    public int getCount() {
        return entitiesFragments.size();
    }

    public void updateData(List<Entity> entities) {
        for (EntitiesView entitiyView : entitiesFragments) {
            entitiyView.showEntities(entities);
        }
        notifyDataSetChanged();
    }


    public interface EntityListener {
        void onEntityClick(int position, String id);

        void onEntityFavouriteClick(int position, String id);
    }
}
