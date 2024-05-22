package net.mercadosocial.moneda.ui.fediverse.list;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.FediversePost;
import net.mercadosocial.moneda.model.Node;
import net.mercadosocial.moneda.ui.info.WebViewActivity;

import java.util.List;

public class FediverseFragment extends BaseFragment implements FediverseView {

    private SwipeRefreshLayout pullToRefresh;
    private RecyclerView recyclerPosts;
    private FediverseAdapter adapter;
    private FediversePresenter presenter;

    private String fediverseServer;

    public FediverseFragment(String fediverseServer) {
        this.fediverseServer = fediverseServer;
    }

    private void findViews(View layout) {
        pullToRefresh = layout.findViewById(R.id.pull_to_refresh);
        recyclerPosts = layout.findViewById(R.id.recycler_fediverse_posts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = FediversePresenter.newInstance(this, getActivity(), fediverseServer);
        setBasePresenter(presenter);
        View layout = inflater.inflate(R.layout.fragment_fediverse, container, false);
        findViews(layout);

        pullToRefresh.setOnRefreshListener(() -> {
            refreshData();
            setRefreshing(false);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerPosts.setLayoutManager(linearLayoutManager);
        recyclerPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = linearLayoutManager.getItemCount();
                int currentLastItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!pullToRefresh.isRefreshing() && currentLastItem == totalItemCount - 1) {
                    refreshData();
                }
            }
        });

        setHasOptionsMenu(true);

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

    @Override
    public void setRefreshing(boolean refreshing) {
        pullToRefresh.setRefreshing(refreshing);
        super.setRefreshing(refreshing);
    }

    @Override
    public void showPosts(List<FediversePost> posts) {
        if (adapter == null) {
            adapter = new FediverseAdapter(getActivity(), posts);
            recyclerPosts.setAdapter(adapter);
        }
        adapter.updateData(posts);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
//        menuItemMapList = menu.findItem(R.id.menuItem_show_map_list);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuItem_info) {
            View layout = View.inflate(getActivity(), R.layout.view_dialog_fediverse_invitation, null);

            new AlertDialog.Builder(requireActivity())
                    .setView(layout)
                    .setNegativeButton(R.string.back, null)
                    .show();

            View requestInvitationBtn = layout.findViewById(R.id.request_invitation);
            requestInvitationBtn.setOnClickListener(v -> {
                Node node = ((App) getActivity().getApplicationContext()).getCurrentNode();
                if (node.getFediverseInviteUrl() != null) {
                    WebViewActivity.startRemoteUrl(getActivity(), "Crea tu cuenta en el Fediverso" , node.getFediverseInviteUrl());
                } else {
                    toast(R.string.fediverse_invite_url_not_found);
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }

}
