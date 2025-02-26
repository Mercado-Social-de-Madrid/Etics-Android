package net.mercadosocial.moneda.base;

import android.content.Context;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.common.ApiClient;
import net.mercadosocial.moneda.api.response.Meta;
import net.mercadosocial.moneda.model.Node;
import net.mercadosocial.moneda.util.Util;

import java.util.List;

import rx.functions.Action0;

/**
 * Created by julio on 14/02/16.
 */
public class BaseInteractor {

    public final String TAG = this.getClass().getSimpleName();

    public Context context;
    public BaseView baseView;

    public <T> T getApi(Class<T> service) {
        return ApiClient.getInstance().create(service);
    }

    public interface BaseApiCallback<T> {
        void onResponse(T responseBody);

        void onError(String message);
    }

    public interface BaseApiPOSTCallback {
        void onSuccess(Integer id);

        void onError(String message);
    }

    public interface BaseApiGETListCallback<T> {
        void onResponse(List<T> list);

        void onError(String message);
    }

    public interface BasePaginationCallback {
        void paginationInfo(Meta meta);
    }

    public Action0 actionTerminate = new Action0() {
        @Override
        public void call() {

            if (baseView != null) {
                baseView.setRefreshing(false);
                baseView.hideProgressDialog();
            }

        }
    };


    protected long getNodeId() {
        Node currentNode = ((App) context.getApplicationContext()).getCurrentNode();
        if (currentNode != null) {
            return currentNode.getID();
        } else {
            return -1;
        }
    }

    public boolean isConnected() {

        boolean connected = Util.isConnected(context);

        if (!connected) {
            if (baseView != null) {
                baseView.toast(R.string.no_connection);
            }
        }

        return connected;
    }

}
