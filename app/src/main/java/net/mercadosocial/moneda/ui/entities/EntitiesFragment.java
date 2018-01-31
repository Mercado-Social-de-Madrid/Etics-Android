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
import net.mercadosocial.moneda.model.Entity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntitiesFragment extends BaseFragment implements EntitiesAdapter.OnItemClickListener, EntitiesView {


    private RecyclerView recyclerEntities;
    private EntitiesAdapter adapter;
    private EntitiesPresenter presenter;


    public EntitiesFragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {
        recyclerEntities = (RecyclerView) layout.findViewById(R.id.recycler_entities);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = EntitiesPresenter.newInstance(this, getActivity());

        View layout = inflater.inflate(R.layout.fragment_entities, container, false);
        findViews(layout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerEntities.setLayoutManager(linearLayoutManager);

//        RecyclerView.ItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
//        recyclerEntities.addItemDecoration(divider);

        setHasOptionsMenu(true);

        presenter.onCreate();

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


    // Presenter Callbacks
    @Override
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
    public void onEntityClicked(String id, int position) {
        presenter.onEntityClicked(position);

    }

    @Override
    public void onEntityFavouriteClicked(String id, int position) {
        presenter.onEntityFavouriteClicked(position);
    }

}
