package net.mercadosocial.moneda.interactor;

import android.content.Context;

import net.mercadosocial.moneda.R;

import net.mercadosocial.moneda.api.common.FediverseApiClient;
import net.mercadosocial.moneda.api.response.ApiError;
import net.mercadosocial.moneda.api.response.FediverseResponse;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.FediversePost;
import net.mercadosocial.moneda.util.Util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FediverseInteractor extends BaseInteractor {

    private String fediverseUrl;

    public FediverseInteractor(Context context, BaseView baseView, String fediverseUrl) {
        this.context = context;
        this.baseView = baseView;
        this.fediverseUrl = fediverseUrl;
    }

    public void getPosts(final BaseApiGETListCallback<FediversePost> callback, String lastId) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        FediverseApiClient.getApiService(fediverseUrl).getPosts(lastId)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<List<FediverseResponse>>>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<List<FediverseResponse>> response) {

                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            callback.onError(apiError.getMessage());
                            return;
                        }

                        if (response.body() != null) {
                            List<FediversePost> posts = new ArrayList<>();

                            for (FediverseResponse r : response.body()) {
                                FediversePost post = new FediversePost(
                                        r.getId(),
                                        r.getAccount().getAvatar(),
                                        r.getAccount().getName(),
                                        r.getAccount().getUsername(),
                                        r.getCreatedAt(),
                                        r.getContent(),
                                        !r.getMediaAttachments().isEmpty()
                                                ? r.getMediaAttachments().stream().map(FediverseResponse.MediaAttachment::getUrl).collect(Collectors.toList())
                                                : Collections.emptyList()
                                );
                                posts.add(post);
                            }
                            callback.onResponse(posts);
                        }
                    }
                });
    }
}


