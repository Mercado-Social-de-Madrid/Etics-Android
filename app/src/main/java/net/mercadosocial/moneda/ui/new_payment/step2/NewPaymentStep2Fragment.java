package net.mercadosocial.moneda.ui.new_payment.step2;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.util.SoftKeyboardManager;
import net.mercadosocial.moneda.util.WindowUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPaymentStep2Fragment extends BaseFragment implements NewPaymentStep2View, View.OnFocusChangeListener, View.OnClickListener {

    private EditText editTotalAmountEur;
    private EditText editBoniatosAmount;
    private TextView btnContinue;
    private TextView tvMaxAcceptedByEntity;
    private NewPaymentStep2Presenter presenter;

    public NewPaymentStep2Fragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {

        editTotalAmountEur = (EditText)layout.findViewById( R.id.edit_total_amount_eur );
        editBoniatosAmount = (EditText)layout.findViewById( R.id.edit_boniatos_amount );
        tvMaxAcceptedByEntity = (TextView) layout.findViewById(R.id.tv_max_accepted_entity);
        btnContinue = (TextView)layout.findViewById( R.id.btn_continue );

        editTotalAmountEur.setOnFocusChangeListener(this);
        btnContinue.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = NewPaymentStep2Presenter.newInstance(this, getActivity());
        setBasePresenter(presenter);

        View layout = inflater.inflate(R.layout.fragment_payment_step2, container, false);
        findViews(layout);

        setHasOptionsMenu(false);

        SoftKeyboardManager.newInstance().configureSoftKeyboardVisibilityBehaviour(getActivity(), new SoftKeyboardManager.OnSoftKeyboardChangedListener() {
            @Override
            public void onSoftKeyboardVisible(boolean visible) {
                btnContinue.setVisibility(visible ? View.GONE : View.VISIBLE);
            }
        });

        presenter.onCreate();

        // Dont know why second edittext gets focus when coming from entity info. fixed with this
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editTotalAmountEur.requestFocus();
            }
        }, 20);

        return layout;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (editTotalAmountEur != null) {
            editTotalAmountEur.setError(null);
            editTotalAmountEur.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                WindowUtils.hideSoftKeyboard(getActivity());
                getAmountData();
                break;
        }

    }

    private void getAmountData() {
        String totalAmount = editTotalAmountEur.getText().toString();
        String boniatosAmount = editBoniatosAmount.getText().toString();
        presenter.onContinueButtonClick(totalAmount, boniatosAmount);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == editTotalAmountEur) {
            if (!hasFocus) {
                String totalAmount = editTotalAmountEur.getText().toString();
                presenter.checkMaxAcceptedByEntity(totalAmount, true);
            }
        }
    }


    // PRESENTER CALLBACKS

    @Override
    public void showBoniatosRestrictions(String maxAcceptedByEntity, String balance) {
        tvMaxAcceptedByEntity.setText(String.format(getString(R.string.max_boniatos_accepted_by_entity), maxAcceptedByEntity, balance));
    }

    @Override
    public void showTotalAmountInputError(String error) {
        editTotalAmountEur.setError(error);
    }

    @Override
    public void showBoniatosAmountInputError(String error) {
        editBoniatosAmount.setError(error);
    }

    @Override
    public void enableContinueButton(boolean enable) {
        btnContinue.setEnabled(enable);
    }

    @Override
    public void showPresetBoniatosAmount(String amount) {
        editBoniatosAmount.setText(amount);
        editBoniatosAmount.selectAll();
    }

}
