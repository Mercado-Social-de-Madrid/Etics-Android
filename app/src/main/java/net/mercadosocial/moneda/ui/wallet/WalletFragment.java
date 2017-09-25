package net.mercadosocial.moneda.ui.wallet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.base.BasePresenter;
import net.mercadosocial.moneda.ui.payment.NewPaymentActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvBalance;
    private AppCompatButton btnNewPayment;
    private AppCompatButton btnGetBoniatos;
    private AppCompatButton btnTransactions;

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
    
    private void findViews(View layout) {
        tvBalance = (TextView)layout.findViewById( R.id.tv_balance );
        btnNewPayment = (AppCompatButton)layout.findViewById( R.id.btn_new_payment );
        btnGetBoniatos = (AppCompatButton)layout.findViewById( R.id.btn_get_boniatos );
        btnTransactions = (AppCompatButton)layout.findViewById( R.id.btn_transactions );

        btnNewPayment.setOnClickListener( this );
        btnGetBoniatos.setOnClickListener( this );
        btnTransactions.setOnClickListener( this );
    }

    public WalletFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_wallet, container, false);
        findViews(layout);

        return layout;
    }

    @Override
    public void onClick(View v) {
        if ( v == btnNewPayment ) {
            startActivity(NewPaymentActivity.newNewPaymentActivity(getActivity(), -1));
        } else if ( v == btnGetBoniatos ) {
            // Handle clicks for btnGetBoniatos
        } else if ( v == btnTransactions ) {
            // Handle clicks for btnTransactions
        }
    }
    
    
}
