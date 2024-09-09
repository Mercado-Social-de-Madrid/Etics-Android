package net.mercadosocial.moneda.ui.entities.list;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.entities.EntitiesChild;
import net.mercadosocial.moneda.ui.entities.EntitiesFragment;
import net.mercadosocial.moneda.ui.entities.EntitiesPresenter;
import net.mercadosocial.moneda.ui.entities.EntityListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntitiesListFragment extends BaseFragment implements EntitiesChild, EntitiesAdapter.OnItemClickListener {


    private static final int NUMBER_ITEM_ASK_MORE = 3;
    private RecyclerView recyclerEntities;
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

        getEntitiesPresenter().updateData();

        return layout;
    }

    private EntitiesPresenter getEntitiesPresenter() {
        return (EntitiesPresenter) ((EntitiesFragment) getParentFragment()).getBasePresenter();
    }

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
