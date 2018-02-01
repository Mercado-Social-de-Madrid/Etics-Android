package net.mercadosocial.moneda.ui.new_payment.step3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.util.WindowUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPaymentStep3Fragment extends BaseFragment implements PinEntryEditText.OnPinEnteredListener, View.OnClickListener, NewPaymentStep3View {

    private TextView tvPaymentAmount;
    private TextView tvPaymentRecipient;
    private TextView tvBonusAmount;
    private PinEntryEditText editPinEntry;
    private TextView btnConfirmPayment;
    private NewPaymentStep3Presenter presenter;


    public NewPaymentStep3Fragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {

        tvPaymentAmount = (TextView)layout.findViewById( R.id.tv_payment_amount );
        tvPaymentRecipient = (TextView)layout.findViewById( R.id.tv_payment_recipient );
        tvBonusAmount = (TextView)layout.findViewById( R.id.tv_bonus_amount );
        editPinEntry = (PinEntryEditText)layout.findViewById( R.id.edit_pin_entry );
        btnConfirmPayment = (TextView)layout.findViewById( R.id.btn_confirm_payment );

        editPinEntry.setOnPinEnteredListener(this);
        btnConfirmPayment.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = NewPaymentStep3Presenter.newInstance(this, getActivity());
        setBasePresenter(presenter);

        View layout = inflater.inflate(R.layout.fragment_payment_step3, container, false);
        findViews(layout);


        setHasOptionsMenu(false);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPinEntered(CharSequence str) {
        WindowUtils.hideSoftKeyboard(editPinEntry);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_confirm_payment:
                confirmPayment();
                break;
        }
    }

    private void confirmPayment() {

        String pin = editPinEntry.getText().toString();
        presenter.onConfirmPaymentClick(pin);
    }

    @Override
    public void showPaymentSummaryInfo(String boniatosAmount, String eurosAmount, String entityName, String bonus) {
        tvPaymentAmount.setText(boniatosAmount);
        tvPaymentRecipient.setText(entityName);
        tvBonusAmount.setText(bonus);

    }

    public void refreshData() {

    }
}
