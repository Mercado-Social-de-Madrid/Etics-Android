package net.mercadosocial.moneda.views.custom_dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Notification;
import net.mercadosocial.moneda.ui.transactions.TransactionsPresenter;
import net.mercadosocial.moneda.util.Util;

/**
 * Created by julio on 23/03/18.
 */

public class BonusDialog extends AppCompatDialogFragment implements View.OnClickListener {


    private static final String ARG_NOTIFICATION = "arg_notification";

    private TextView tvBonusMessage;
    private AppCompatButton btnGoToTransactions;
    private TextView btnClose;
    private OnCloseListener onDismissOrCancelListener;

    private void findViews(View layout) {
        tvBonusMessage = (TextView)layout.findViewById( R.id.tv_bonus_message );
        btnGoToTransactions = (AppCompatButton)layout.findViewById( R.id.btn_go_to_transactions );
        btnClose = (TextView)layout.findViewById( R.id.btn_close );

        btnGoToTransactions.setOnClickListener( this );
        btnClose.setOnClickListener( this );
    }


    public static BonusDialog newInstance(Notification notification) {
        BonusDialog bonusDialog = new BonusDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATION, notification);
        bonusDialog.setArguments(args);
        return bonusDialog;
    }
    
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogBonus);
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.view_dialog_bonus, container, false);
        findViews(layout);

        Notification notification = (Notification) getArguments().getSerializable(ARG_NOTIFICATION);

        String amountFormatted = Util.getDecimalFormatted(notification.getAmount(), false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvBonusMessage.setText(Html.fromHtml(String.format(getString(R.string.bonification_received_message),
                    amountFormatted, getString(R.string.currency_name_plural)), Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            tvBonusMessage.setText(Html.fromHtml(String.format(getString(R.string.bonification_received_message),
                    amountFormatted, getString(R.string.currency_name_plural))));
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

            case R.id.btn_go_to_transactions:
                startActivity(TransactionsPresenter.newTransactionsActivity(getActivity()));
                dismiss();
                break;

            case R.id.btn_close:
                dismiss();
                break;
        }
    }

    public void setOnDismissOrCancelListener(OnCloseListener listener) {
        this.onDismissOrCancelListener = listener;
    }

}
