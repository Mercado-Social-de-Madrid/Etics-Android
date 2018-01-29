package net.mercadosocial.moneda.ui.new_payment.step1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.base.BasePresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPaymentStep1Fragment extends BaseFragment {


    public NewPaymentStep1Fragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.view_new_payment_step1, container, false);
        findViews(layout);

        return layout;
    }


    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
