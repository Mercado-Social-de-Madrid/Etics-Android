package net.mercadosocial.moneda.base;

/**
 * Created by julio on 16/12/15.
 */
public interface BaseView {

    void setRefreshing(boolean refreshing);

    void showProgressDialog(String message);

    void hideProgressDialog();

    void toast(int stringResId);

    void toast(String mensaje);

    void alert(String title, String message);

    void alert(String message);

    void onInvalidToken();
}
