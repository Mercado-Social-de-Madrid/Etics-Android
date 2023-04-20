package net.mercadosocial.moneda.ui.entities.list;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.entities.EntitiesFragment;
import net.mercadosocial.moneda.ui.entities.EntitiesPresenter;
import net.mercadosocial.moneda.ui.entities.EntitiesRefreshListener;
import net.mercadosocial.moneda.ui.entities.EntityListener;
import net.mercadosocial.moneda.views.RotativeImageView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntitiesListFragment extends BaseFragment implements EntitiesAdapter.OnItemClickListener, EntitiesRefreshListener {


    private static final int NUMBER_ITEM_ASK_MORE = 3;
    private SuperRecyclerView recyclerEntities;
    private EntitiesAdapter adapter;
    private EntityListener entityListener;
    private View viewEmptyList;
    private TextView tvEmptyListEntities;


    public EntitiesListFragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {
        recyclerEntities = layout.findViewById(R.id.recycler_entities);
        viewEmptyList = layout.findViewById(R.id.view_empty_list);
        tvEmptyListEntities = layout.findViewById(R.id.tv_empty_list_entities);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View layout = inflater.inflate(R.layout.fragment_entities_list, container, false);
        findViews(layout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerEntities.setLayoutManager(linearLayoutManager);

        setEntityListener((EntityListener) getParentFragment());

        getEntitiesPresenter().setEntitiesRefreshListener(this);

        getEntitiesPresenter().refreshData();

        return layout;
    }

    private EntitiesPresenter getEntitiesPresenter() {
        return (EntitiesPresenter) ((EntitiesFragment) getParentFragment()).getBasePresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getEntitiesPresenter().removeEntitiesRefreshListener(this);
    }


    // Presenter Callbacks


    @Override
    public void updateEntities(List<Entity> entities) {

        if (adapter == null) {

            boolean showFavsStars = App.getUserData(getActivity()) != null;
            adapter = new EntitiesAdapter(getActivity(), entities, showFavsStars);
            adapter.setOnItemClickListener(this);

            if (recyclerEntities == null) {
                // Strange error ¿?
                FirebaseCrashlytics.getInstance().recordException(new IllegalStateException("recyclerEntities == null when trying to set adapter"));
            } else {
                recyclerEntities.setAdapter(adapter);
            }


        } else {
            boolean showFavsStars = App.getUserData(getActivity()) != null;
            adapter.setShowFavsStarts(showFavsStars);
            adapter.updateData();
        }

        viewEmptyList.setVisibility(entities.isEmpty() ? View.VISIBLE : View.GONE);
        tvEmptyListEntities.setText(R.string.empty_state_text_entities);
    }

    @Override
    public void onError(boolean showEmptyView) {
        viewEmptyList.setVisibility(View.VISIBLE);
        tvEmptyListEntities.setText(R.string.error_retrieving_data);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);

        if (refreshing) {
            viewEmptyList.setVisibility(View.GONE);
        } else {
            viewEmptyList.setVisibility(View.VISIBLE);
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


    public void setEntityListener(EntityListener entityListener) {
        this.entityListener = entityListener;
    }

}
