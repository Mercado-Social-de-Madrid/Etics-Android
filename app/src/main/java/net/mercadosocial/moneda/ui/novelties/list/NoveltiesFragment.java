package net.mercadosocial.moneda.ui.novelties.list;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
public class NoveltiesFragment extends BaseFragment implements NoveltiesAdapter.OnItemClickListener, NoveltiesView {


    private RecyclerView recyclerNovelties;
    private NoveltiesAdapter adapter;
    private NoveltiesPresenter presenter;

    public NoveltiesFragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {
        recyclerNovelties = (RecyclerView) layout.findViewById(R.id.recycler_novelties);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = NoveltiesPresenter.newInstance(this, getActivity());
        setBasePresenter(presenter);
        View layout = inflater.inflate(R.layout.fragment_novelties, container, false);
        findViews(layout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerNovelties.setLayoutManager(linearLayoutManager);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
//                linearLayoutManager.getOrientation());
//        recyclerNovelties.addItemDecoration(dividerItemDecoration);

//        List<Novelty> novelties = generateMockNovelties();
//        showNovelties(novelties);

        setHasOptionsMenu(false);

        presenter.onCreate();

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
    }

    @Override
    public void refreshData() {
        super.refreshData();
        presenter.refreshData();
    }

    private List<Novelty> generateMockNovelties() {

        List<Novelty> novelties = new ArrayList<>();

//        novelties.addAll(newsMock);
//
//        novelties.addAll(offersMock);

        Collections.sort(novelties);

        return novelties;

    }


    @Override
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
    public void onItemClick(int position) {
        presenter.onNoveltyClick(position);
    }


}
