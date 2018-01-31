package net.mercadosocial.moneda.ui.new_payment.step2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPaymentStep2Fragment extends BaseFragment {

    private EditText editTotalAmountEur;
    private EditText editBoniatosAmount;
    private TextView btnContinue;

    public NewPaymentStep2Fragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {

        editTotalAmountEur = (EditText)layout.findViewById( R.id.edit_total_amount_eur );
        editBoniatosAmount = (EditText)layout.findViewById( R.id.edit_boniatos_amount );
        btnContinue = (TextView)layout.findViewById( R.id.btn_continue );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_payment_step2, container, false);
        findViews(layout);

        setHasOptionsMenu(false);

        return layout;
    }

}
