package net.mercadosocial.moneda.ui.news;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.entities.EntitiesAdapter;
import net.mercadosocial.moneda.ui.entity_info.EntityInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends BaseFragment implements EntitiesAdapter.OnItemClickListener {


    private RecyclerView recyclerEntities;
    private EntitiesAdapter adapter;

    public static List<Entity> entitiesMock = new ArrayList<>();
    static {
        entitiesMock.add(new Entity(1, "Fabricantes de sueños", "Librería alternativa", "https://madrid.mercadosocial.net/moneda/thumbnail?id=106", "Calle X", "Librería"));
        entitiesMock.add(new Entity(2, "El Salmón Contracorriente", "Medio sobre economía alternativa", "http://www.elsalmoncontracorriente.es/IMG/rubon1.png", "Calle X", "Medio digital"));
        entitiesMock.add(new Entity(3, "Green life", "Cultiva plantas de la paz", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Cannabis_sativa_leaf.jpg/240px-Cannabis_sativa_leaf.jpg", "Calle X", "Grow shop"));
    }

    public static Entity getEntityById(int idEntity) {
        for (int i = 0; i < entitiesMock.size(); i++) {
            if (entitiesMock.get(i).getId() == idEntity) {
                return entitiesMock.get(i);
            }
        }

        throw new IllegalArgumentException("idEntity not valid: " + idEntity);
    }

    public NewsFragment() {
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
        View layout = inflater.inflate(R.layout.fragment_news, container, false);
        findViews(layout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerEntities.setLayoutManager(linearLayoutManager);

        showEntities(entitiesMock);

        return layout;
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
