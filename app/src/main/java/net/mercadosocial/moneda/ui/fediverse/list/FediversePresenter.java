package net.mercadosocial.moneda.ui.fediverse.list;

import android.content.Context;

import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.interactor.FediverseInteractor;
import net.mercadosocial.moneda.model.FediversePost;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FediversePresenter extends BasePresenter {

    private final FediverseFragment view;
    private final FediverseInteractor fediverseInteractor;
    private final List<FediversePost> posts = new ArrayList<>();

    private boolean refreshing = false;

    public static FediversePresenter newInstance(FediverseFragment view, Context context) {
        return new FediversePresenter(view, context);
    }

    private FediversePresenter(FediverseFragment view, Context context) {
        super(context, view);

        this.view = view;
        fediverseInteractor = new FediverseInteractor(context, view);
    }

    public void onCreate() {
        refreshData();
    }

    public void onResume() {}

    public void refreshData() {
        setRefreshing(true);

        getPosts(posts.isEmpty() ? null : posts.get(posts.size()-1).getId());
    }

    private void getPosts(String lastId) {
        fediverseInteractor.getPosts(new BaseInteractor.BaseApiGETListCallback<FediversePost>(){
            @Override
            public void onResponse(List<FediversePost> response) {
                if (!response.isEmpty()) {
                    posts.addAll(response);
                    view.showPosts(posts);
                    setRefreshing(false);
                }
            }

            @Override
            public void onError(String message) {
                setRefreshing(false);
                Toasty.error(context, message).show();
            }
        }, lastId);
    }

    private void setRefreshing(boolean refreshing) {
        view.setRefreshing(refreshing);
        this.refreshing = refreshing;
    }

    public boolean isRefreshing() {
        return refreshing;
    }
}
