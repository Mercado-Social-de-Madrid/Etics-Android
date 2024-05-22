package net.mercadosocial.moneda.interactor;

import android.content.Context;
import android.util.Log;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.NodesApi;
import net.mercadosocial.moneda.api.response.ApiError;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.base.BaseView;
import net.mercadosocial.moneda.model.Node;
import net.mercadosocial.moneda.util.Util;

import java.util.List;

import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NodeInteractor extends BaseInteractor {

    public NodeInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void getNodes(final BaseApiGETListCallback<Node> callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        getApi(NodesApi.class).getNodes()
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<Response<List<Node>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Response<List<Node>> response) {

                        if (!response.isSuccessful()) {
                            ApiError apiError = ApiError.parse(response);
                            callback.onError(apiError.getMessage());
                            return;
                        }

                        callback.onResponse(response.body());


                    }
                });


    }

    public void updateNodeData() {

        App app = ((App) context.getApplicationContext());
        Node currentNode = app.getCurrentNode();
        if (currentNode != null) {

            getApi(NodesApi.class).getNode(currentNode.getID())
                    .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
//                    .doOnTerminate(actionTerminate)
                    .subscribe(new Observer<Response<Node>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: ", e);
                        }

                        @Override
                        public void onNext(Response<Node> response) {
                            if (response.isSuccessful()) {
                                app.setCurrentNode(response.body());
                            }

                        }
                    });

        }

    }

}
