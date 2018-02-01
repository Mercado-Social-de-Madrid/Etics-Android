package net.mercadosocial.moneda.base;

import android.content.Context;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.common.ApiClient;
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

    public interface BaseApiCallback {
        void onSuccess();

        void onError(String message);
    }

    public interface BaseApiPOSTCallback {
        void onSuccess(Integer id);

        void onError(String message);
    }

    public interface BaseApiGETListCallback<T> {
        void onSuccess(List<T> list);

        void onError(String message);
    }

    public Action0 actionTerminate = new Action0() {
        @Override
        public void call() {

            if (baseView != null) {
                baseView.setRefresing(false);
                baseView.hideProgressDialog();
            }

        }
    };


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
