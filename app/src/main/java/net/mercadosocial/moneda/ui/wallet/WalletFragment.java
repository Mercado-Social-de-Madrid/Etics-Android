package net.mercadosocial.moneda.ui.wallet;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Wallet;
import net.mercadosocial.moneda.ui.auth.login.LoginActivity;
import net.mercadosocial.moneda.ui.auth.register.RegisterPresenter;
import net.mercadosocial.moneda.ui.get_boniatos.GetBoniatosPresenter;
import net.mercadosocial.moneda.ui.main.MainActivity;
import net.mercadosocial.moneda.ui.new_payment.NewPaymentPresenter;
import net.mercadosocial.moneda.ui.payments.PaymentsPresenter;
import net.mercadosocial.moneda.ui.sent_payments.SentPaymentsPresenter;
import net.mercadosocial.moneda.ui.transactions.TransactionsPresenter;
import net.mercadosocial.moneda.ui.wallet_graphics.GraphicsPresenter;

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
    private MenuItem menuItemQR;
    private View btnSentPayments;
    private TextView tvNumberSentPayments;


    private void findViews(View layout) {
        tvBalance = (TextView) layout.findViewById(R.id.tv_balance);
        tvName = (TextView) layout.findViewById(R.id.tv_name);
        btnNewPayment = layout.findViewById(R.id.btn_new_payment);
        btnGetBoniatos = layout.findViewById(R.id.btn_get_boniatos);
        btnGraphics = layout.findViewById(R.id.btn_graphics);
        btnMovements = layout.findViewById(R.id.btn_movements);
        progressBalance = layout.findViewById(R.id.progress_balance);

        btnPendingPayments = layout.findViewById(R.id.btn_pending_payments);
        tvNumberPendingPayments = (TextView) layout.findViewById(R.id.tv_number_pending_payments);

        btnSentPayments = layout.findViewById(R.id.btn_sent_payments);
        tvNumberSentPayments = (TextView) layout.findViewById(R.id.tv_number_sent_payments);

        viewWallet = layout.findViewById(R.id.view_wallet);
        viewNoWallet = layout.findViewById(R.id.view_no_wallet);

        btnLogin = layout.findViewById(R.id.btn_login);
        btnSignup = layout.findViewById(R.id.btn_singup);

        btnNewPayment.setOnClickListener(this);
        btnGetBoniatos.setOnClickListener(this);
        btnMovements.setOnClickListener(this);
        btnGraphics.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnPendingPayments.setOnClickListener(this);
        btnSentPayments.setOnClickListener(this);
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

        setHasOptionsMenu(true);

        presenter.onCreate();

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onMESCityChanged() {
        super.onMESCityChanged();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        presenter.refreshData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.wallet, menu);
        menuItemQR = menu.findItem(R.id.menuItem_show_qr);
        menuItemQR.setVisible(presenter.isQRGeneratorVisible());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItem_show_qr:
                presenter.onShowQRClick();
                break;
        }

        return super.onOptionsItemSelected(item);
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
                startActivity(RegisterPresenter.newRegisterActivity(getActivity()));
                break;

            case R.id.btn_get_boniatos:
                startActivity(GetBoniatosPresenter.newGetBoniatosActivity(getActivity()));
                break;

            case R.id.btn_graphics:
                startActivity(GraphicsPresenter.newGraphicsActivity(getActivity()));
                break;

            case R.id.btn_movements:
                startActivity(TransactionsPresenter.newTransactionsActivity(getActivity()));
                break;

            case R.id.btn_pending_payments:
                startActivity(PaymentsPresenter.newPaymentsActivity(getActivity()));
                break;

            case R.id.btn_sent_payments:
                startActivity(SentPaymentsPresenter.newSentPaymentsActivity(getActivity()));
                break;
        }

    }


    // PRESENTER CALLBACKS

    @Override
    public void showUserInfo(String name) {
        viewNoWallet.setVisibility(View.GONE);
        viewWallet.setVisibility(View.VISIBLE);

        tvName.setText(String.format(getString(R.string.welcome_name_wallet), name));

    }

    @Override
    public void showLoggedOutView() {
        viewNoWallet.setVisibility(View.VISIBLE);
        viewWallet.setVisibility(View.GONE);
        if (menuItemQR != null) {
            menuItemQR.setVisible(false);
        }
    }


    @Override
    public void showWalletData(boolean showLoading, Wallet wallet) {

        progressBalance.setVisibility(showLoading ? View.VISIBLE : View.INVISIBLE);
        tvBalance.setVisibility(showLoading ? View.INVISIBLE : View.VISIBLE);

        if (wallet != null) {
            tvBalance.setText(wallet.getBalanceFormatted() + " " + getString(R.string.currency_name_abrev));
        } else {
            tvBalance.setText("---");
        }

    }

    @Override
    public void showPendingPaymentsNumber(int numberPendingPayments) {

        if (numberPendingPayments == 0) {
            btnPendingPayments.setVisibility(View.GONE);
            return;
        }

        btnPendingPayments.setVisibility(View.VISIBLE);
        tvNumberPendingPayments.setText(String.format(getString(R.string.pending_payments_warning), numberPendingPayments));

        ((MainActivity) getActivity()).showPendingPaymentsNumber(numberPendingPayments);
    }


    @Override
    public void showSentPaymentsNumber(int numberSentPayments) {

        if (numberSentPayments == 0) {
            btnSentPayments.setVisibility(View.GONE);
            return;
        }

        btnSentPayments.setVisibility(View.VISIBLE);
        tvNumberSentPayments.setText(String.format(getString(R.string.sent_payments_warning), numberSentPayments));

    }


}
