package net.mercadosocial.moneda.ui.entities.list;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.entities.EntitiesChildView;
import net.mercadosocial.moneda.ui.entities.EntitiesFragment;
import net.mercadosocial.moneda.ui.entities.EntitiesPagerAdapter;
import net.mercadosocial.moneda.ui.entities.EntitiesPresenter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntitiesListFragment extends BaseFragment implements EntitiesAdapter.OnItemClickListener, EntitiesChildView {


    private SuperRecyclerView recyclerEntities;
    private EntitiesAdapter adapter;
    private EntitiesPagerAdapter.EntityListener entityListener;


    public EntitiesListFragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {
        recyclerEntities = layout.findViewById(R.id.recycler_entities);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View layout = inflater.inflate(R.layout.fragment_entities_list, container, false);
        findViews(layout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerEntities.setLayoutManager(linearLayoutManager);

        recyclerEntities.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                getEntitiesPresenter().loadNextPage();
            }}, 3);

//        RecyclerView.ItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
//        recyclerEntities.addItemDecoration(divider);

        return layout;
    }

    private EntitiesPresenter getEntitiesPresenter() {
        return (EntitiesPresenter) ((EntitiesFragment) getParentFragment()).getBasePresenter();
    }


    // Presenter Callbacks
    @Override
    public void showEntities(List<Entity> entities, boolean hasMore) {

        if (!hasMore) {
            recyclerEntities.setupMoreListener(null, 0);
        }

        if (adapter == null) {

            adapter = new EntitiesAdapter(getActivity(), entities);
            adapter.setOnItemClickListener(this);

            recyclerEntities.setAdapter(adapter);

        } else {
            adapter.updateData();
        }
    }



    @Override
    public void onEntityClicked(String id, int position) {
        entityListener.onEntityClick(position, id);
    }

    @Override
    public void onEntityFavouriteClicked(int position, boolean isFavourite) {
        entityListener.onEntityFavouriteClick(position, isFavourite);
    }


    public void setEntityListener(EntitiesPagerAdapter.EntityListener entityListener) {
        this.entityListener = entityListener;
    }
}
