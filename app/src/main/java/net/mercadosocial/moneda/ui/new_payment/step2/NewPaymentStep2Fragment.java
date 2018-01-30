package net.mercadosocial.moneda.ui.new_payment.step2;


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
public class NewPaymentStep2Fragment extends BaseFragment {


    public NewPaymentStep2Fragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_payment_step2, container, false);
        findViews(layout);

        setHasOptionsMenu(false);

        return layout;
    }


    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
