package net.mercadosocial.moneda.ui.wallet;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.common.StringUtils;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Wallet;
import net.mercadosocial.moneda.ui.auth.login.LoginActivity;
import net.mercadosocial.moneda.ui.auth.register.RegisterActivity;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentPresenter;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends BaseFragment implements View.OnClickListener, WalletView {

    private TextView tvBalance;
    private View btnNewPayment;
    private View btnGetBoniatos;
    private View btnMovements;
    private WalletPresenter presenter;
    private View viewWallet;
    private View viewNoWallet;
    private View btnLogin;
    private View btnSignup;
    private TextView tvName;
    private View progressBalance;
    private View btnGraphics;
    private TextView tvNumberPendingPayments;
    private View btnPendingPayments;


    private void findViews(View layout) {
        tvBalance = (TextView)layout.findViewById( R.id.tv_balance );
        tvName = (TextView)layout.findViewById( R.id.tv_name );
        btnNewPayment = layout.findViewById( R.id.btn_new_payment );
        btnGetBoniatos = layout.findViewById( R.id.btn_get_boniatos );
        btnGraphics = layout.findViewById( R.id.btn_graphics );
        btnMovements = layout.findViewById( R.id.btn_movements );
        progressBalance = layout.findViewById(R.id.progress_balance);

        btnPendingPayments = layout.findViewById(R.id.btn_pending_payments);
        tvNumberPendingPayments = (TextView)layout.findViewById(R.id.tv_number_pending_payments);

        viewWallet = layout.findViewById(R.id.view_wallet);
        viewNoWallet = layout.findViewById(R.id.view_no_wallet);

        btnLogin = layout.findViewById(R.id.btn_login);
        btnSignup = layout.findViewById(R.id.btn_singup);

        btnNewPayment.setOnClickListener( this );
        btnGetBoniatos.setOnClickListener( this );
        btnMovements.setOnClickListener( this );
        btnGraphics.setOnClickListener( this );
        btnLogin.setOnClickListener( this );
        btnSignup.setOnClickListener( this );
        btnPendingPayments.setOnClickListener( this );
    }

    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = WalletPresenter.newInstance(this, getActivity());
        setBasePresenter(presenter);

        View layout = inflater.inflate(R.layout.fragment_wallet, container, false);
        findViews(layout);

        setHasOptionsMenu(false);

        presenter.onCreate();

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new_payment:
                startActivity(NewPaymentPresenter.newNewPaymentActivity(getActivity(), null));
                break;

            case R.id.btn_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;

            case R.id.btn_singup:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;

            case R.id.btn_get_boniatos:
                Toasty.info(getActivity(), "Trabajando en ello...").show();
                break;

            case R.id.btn_graphics:
            case R.id.btn_movements:
                Toasty.info(getActivity(), "En breve disponible...").show();
                break;
        }
    }


    @Override
    public void showUserInfo(String username) {
        viewNoWallet.setVisibility(View.GONE);
        viewWallet.setVisibility(View.VISIBLE);

        tvName.setText(String.format(getString(R.string.welcome_name_wallet), username));

    }

    @Override
    public void showWalletData(boolean showLoading, Wallet wallet) {

        progressBalance.setVisibility(showLoading ? View.VISIBLE : View.INVISIBLE);
        tvBalance.setVisibility(showLoading ? View.INVISIBLE : View.VISIBLE);

        if (wallet != null) {
            tvBalance.setText(wallet.getBalanceFormatted() + " B");
        } else {
            tvBalance.setText("---");
        }
    }

    @Override
    public void showPendingPaymentsNumber(int numberPendingPayments) {
        btnPendingPayments.setVisibility(View.VISIBLE);
        tvNumberPendingPayments.setText(String.format(getString(R.string.pending_payments_warning), numberPendingPayments));
//        String.format("%d", numberPendingPayments);
    }
}
