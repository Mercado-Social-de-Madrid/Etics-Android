package net.mercadosocial.moneda.views.custom_dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Transaction;
import net.mercadosocial.moneda.util.Util;

/**
 * Created by julio on 23/03/18.
 */

public class TransactionInfoDialog extends AppCompatDialogFragment implements View.OnClickListener {


    private static final String ARG_DATA = "arg_data";

    private TextView tvMessage;
    private TextView btnClose;
    private OnCloseListener onDismissOrCancelListener;

    private void findViews(View layout) {
        tvMessage = (TextView)layout.findViewById( R.id.tv_message );
        btnClose = (TextView)layout.findViewById( R.id.btn_close );

        btnClose.setOnClickListener( this );
    }


    public static TransactionInfoDialog newInstance(Transaction transaction) {
        TransactionInfoDialog bonusDialog = new TransactionInfoDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATA, transaction);
        bonusDialog.setArguments(args);
        return bonusDialog;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.view_dialog_transaction_info, container, false);
        findViews(layout);

        Transaction transaction = (Transaction) getArguments().getSerializable(ARG_DATA);

        String dateTime = transaction.getDateTimeFormatted().replace("\n", " - ");

        String sendReceiveInfo = transaction.getAmount() > 0 ? getString(R.string.payment_received_from) : getString(R.string.payment_sent_to);
        if (transaction.getIs_bonification()) {
            sendReceiveInfo = transaction.getAmount() > 0 ? getString(R.string.bonus_received_from) : getString(R.string.bonus_sent_to);
        } else if (transaction.getIs_euro_purchase()) {
            sendReceiveInfo = getString(R.string.currency_purchase);
        }

        String account = transaction.getRelated();
        String amount = Util.getDecimalFormatted(transaction.getAmount(), true)
                + " " + getString(R.string.currency_name_abrev);
        String concept = transaction.getConcept();
        String balance = Util.getDecimalFormatted(transaction.getCurrent_balance(), true)
                + " " + getString(R.string.currency_name_abrev);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvMessage.setText(Html.fromHtml(String.format(getString(R.string.transaction_detail_message),
                    dateTime, sendReceiveInfo, account, amount, concept, balance), Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            tvMessage.setText(Html.fromHtml(String.format(getString(R.string.transaction_detail_message),
                    dateTime, sendReceiveInfo, account, amount, concept, balance)));
        }

//        setCancelable(false);

        return layout;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (onDismissOrCancelListener != null) {
                    onDismissOrCancelListener.onClose();
                }
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onDismissOrCancelListener != null) {
                    onDismissOrCancelListener.onClose();
                }
            }
        });
        return dialog;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_close:
                dismiss();
                break;
        }
    }

    public void setOnDismissOrCancelListener(OnCloseListener listener) {
        this.onDismissOrCancelListener = listener;
    }

}
