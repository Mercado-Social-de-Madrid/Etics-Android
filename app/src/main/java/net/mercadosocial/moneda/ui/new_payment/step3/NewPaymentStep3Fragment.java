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
public class NewPaymentStep3Fragment extends BaseFragment implements PinEntryEditText.OnPinEnteredListener, View.OnClickListener {

    private TextView tvPaymentAmount;
    private TextView tvPaymentRecipient;
    private TextView tvBonificationAmount;
    private PinEntryEditText editPinEntry;
    private TextView btnConfirmPayment;


    public NewPaymentStep3Fragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {

        tvPaymentAmount = (TextView)layout.findViewById( R.id.tv_payment_amount );
        tvPaymentRecipient = (TextView)layout.findViewById( R.id.tv_payment_recipient );
        tvBonificationAmount = (TextView)layout.findViewById( R.id.tv_bonification_amount );
        editPinEntry = (PinEntryEditText)layout.findViewById( R.id.edit_pin_entry );
        btnConfirmPayment = (TextView)layout.findViewById( R.id.btn_confirm_payment );

        editPinEntry.setOnPinEnteredListener(this);
        btnConfirmPayment.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_payment_step3, container, false);
        findViews(layout);


        setHasOptionsMenu(false);

        return layout;
    }

    @Override
    public void onPinEntered(CharSequence str) {
        WindowUtils.hideSoftKeyboard(editPinEntry);
    }

    @Override
    public void onClick(View v) {

    }
}
