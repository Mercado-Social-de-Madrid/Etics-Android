package net.mercadosocial.moneda.ui.entities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.entity_info.EntityInfoActivity;

import java.util.List;

import static net.mercadosocial.moneda.model.Entity.entitiesMock;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntitiesFragment extends BaseFragment implements EntitiesAdapter.OnItemClickListener {


    private RecyclerView recyclerEntities;
    private EntitiesAdapter adapter;


    public static Entity getEntityById(int idEntity) {
        for (int i = 0; i < entitiesMock.size(); i++) {
            if (entitiesMock.get(i).getId() == idEntity) {
                return entitiesMock.get(i);
            }
        }

        throw new IllegalArgumentException("idEntity not valid: " + idEntity);
    }

    public EntitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    private void findViews(View layout) {
        recyclerEntities = (RecyclerView) layout.findViewById(R.id.recycler_entities);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_entities, container, false);
        findViews(layout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerEntities.setLayoutManager(linearLayoutManager);

        showEntities(entitiesMock);

        setHasOptionsMenu(true);

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_entities_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItem_search:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showEntities(List<Entity> entities) {

        if (adapter == null) {

            adapter = new EntitiesAdapter(getActivity(), entities);
            adapter.setOnItemClickListener(this);

            recyclerEntities.setAdapter(adapter);

        } else {
            adapter.updateData(entities);
        }
    }

    @Override
    public void onEntityClicked(int idEntity) {
        startActivity(EntityInfoActivity.newEntityInfoActivity(getActivity(), idEntity));

    }

    @Override
    public void onEventFavouriteClicked(int idEvent) {

    }

}
