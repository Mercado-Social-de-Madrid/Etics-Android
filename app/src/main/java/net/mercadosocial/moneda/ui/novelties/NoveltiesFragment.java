package net.mercadosocial.moneda.ui.novelties;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Novelty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoveltiesFragment extends BaseFragment implements NoveltiesAdapter.OnItemClickListener {


    private RecyclerView recyclerNovelties;
    private NoveltiesAdapter adapter;

    public NoveltiesFragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {
        recyclerNovelties = (RecyclerView) layout.findViewById(R.id.recycler_novelties);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_novelties, container, false);
        findViews(layout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerNovelties.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                linearLayoutManager.getOrientation());
        recyclerNovelties.addItemDecoration(dividerItemDecoration);

        List<Novelty> novelties = generateMockNovelties();
        showNovelties(novelties);

        setHasOptionsMenu(false);

        return layout;
    }

    private List<Novelty> generateMockNovelties() {

        List<Novelty> novelties = new ArrayList<>();

//        novelties.addAll(newsMock);
//
//        novelties.addAll(offersMock);

        Collections.sort(novelties);

        return novelties;

    }


    public void showNovelties(List<Novelty> novelties) {

        if (adapter == null) {

            adapter = new NoveltiesAdapter(getActivity(), novelties);
            adapter.setOnItemClickListener(this);

            recyclerNovelties.setAdapter(adapter);

        } else {
            adapter.updateData(novelties);
        }
    }

    @Override
    public void onEntityClicked(int idEntity) {
//        startActivity(EntityInfoActivity.newEntityInfoActivity(getActivity(), idEntity));

    }

    @Override
    public void onEventFavouriteClicked(int idEvent) {

    }

}
