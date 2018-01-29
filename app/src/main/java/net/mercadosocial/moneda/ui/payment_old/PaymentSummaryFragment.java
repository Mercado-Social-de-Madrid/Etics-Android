package net.mercadosocial.moneda.ui.payment_old;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.util.WindowUtils;

/**
 * Created by julio on 5/10/17.
 */

public class PaymentSummaryFragment extends DialogFragment implements View.OnClickListener {


    private View btnReturn;
    private View btnConfirm;
    private PinEntryEditText editPinEntry;
    private PaymentSummaryCallback callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_payment_summary, null);

        findViews(layout);

        editPinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {

                    WindowUtils.hideSoftKeyboard(editPinEntry);
                }
            });

        return layout;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void findViews(View layout) {

        btnReturn = layout.findViewById(R.id.btn_return);
        btnConfirm = layout.findViewById(R.id.btn_confirm);
        editPinEntry = (PinEntryEditText) layout.findViewById(R.id.edit_pin_entry);

        btnConfirm.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (callback != null) {
                    callback.onConfirm("1234");
                }

                break;

            case R.id.btn_return:
                if (callback != null) {
                    callback.onDismiss();
                }
                break;
        }
    }


    public PaymentSummaryFragment setCallback(PaymentSummaryCallback callback) {
        this.callback = callback;
        return this;
    }

    public interface PaymentSummaryCallback {
        void onConfirm(String pinCode);

        void onDismiss();
    }
}
