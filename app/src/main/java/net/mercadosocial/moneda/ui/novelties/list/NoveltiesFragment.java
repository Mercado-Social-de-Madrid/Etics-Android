package net.mercadosocial.moneda.ui.novelties.list;


import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

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
    private TextView tvEmptyState;

    public NoveltiesFragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {
        recyclerNovelties = (RecyclerView) layout.findViewById(R.id.recycler_novelties);
        tvEmptyState = layout.findViewById(R.id.tv_empty_state);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            Dexter.withActivity(getActivity())
                    .withPermission(Manifest.permission.POST_NOTIFICATIONS)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(R.string.notifications_permission)
                                    .setMessage(R.string.notifications_permission_rationale_msg)
                                    .setPositiveButton(R.string.accept, (dialog, which) -> token.continuePermissionRequest())
                                    .setNegativeButton(R.string.cancel, (dialog, which) -> token.cancelPermissionRequest())
                                    .show();
                        }
                    }).check();
        }
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

        tvEmptyState.setVisibility(novelties != null && !novelties.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemClick(int position) {
        presenter.onNoveltyClick(position);
    }


}
