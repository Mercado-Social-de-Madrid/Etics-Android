package net.mercadosocial.moneda.base;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * Created by julio on 27/01/16.
 */
public abstract class BaseFragment extends Fragment implements BaseView {


    public final String TAG = this.getClass().getSimpleName();
    protected BaseActivity baseActivity;
    private BasePresenter presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            this.baseActivity = (BaseActivity) context;
        } catch (ClassCastException e) {
            throw new IllegalStateException("The activity "
                    + "hosting this fragment does not extend BaseActivity");
        }

    }

    private BasePresenter getPresenter(){
        return presenter;
    }

    public void setPresenter(BasePresenter presenter) {
        this.presenter = presenter;
    }

    protected SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    @Override
    public void showProgressDialog(String message) {

        ((BaseActivity) getActivity()).showProgressDialog(message);
    }

    @Override
    public void hideProgressDialog() {

        ((BaseActivity) getActivity()).hideProgressDialog();
    }


    @Override
    public void setRefresing(boolean refresing) {

    }


    @Override
    public void toast(int stringResId) {
        ((BaseActivity) getActivity()).toast(stringResId);
    }

    @Override
    public void toast(String mensaje) {
        ((BaseActivity) getActivity()).toast(mensaje);
    }

    @Override
    public void alert(String title, String message) {
        ((BaseActivity) getActivity()).alert(title, message);
    }

    @Override
    public void alert(String message) {
        ((BaseActivity) getActivity()).alert(message);
    }

    @Override
    public void onInvalidToken() {

    }
}
