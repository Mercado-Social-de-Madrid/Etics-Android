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

    private final FediverseView view;
    private final FediverseInteractor fediverseInteractor;
    private final List<FediversePost> posts = new ArrayList<>();

    public static FediversePresenter newInstance(FediverseView view, Context context, String fediverseUrl) {
        return new FediversePresenter(view, context, fediverseUrl);
    }

    private FediversePresenter(FediverseView view, Context context, String fediverseUrl) {
        super(context, view);

        this.view = view;
        fediverseInteractor = new FediverseInteractor(context, view, fediverseUrl);
    }

    public void onCreate() {
        refreshData();
    }

    public void onResume() {}

    public void refreshData() {
        getPosts(posts.isEmpty() ? null : posts.get(posts.size()-1).getId());
    }

    private void getPosts(String lastId) {
        fediverseInteractor.getPosts(new BaseInteractor.BaseApiGETListCallback<FediversePost>(){
            @Override
            public void onResponse(List<FediversePost> response) {
                if (!response.isEmpty()) {
                    posts.addAll(response);
                    view.showPosts(posts);
                }
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message).show();
            }
        }, lastId);

    }

}
