package net.mercadosocial.moneda.views.custom_dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.interactor.PaymentInteractor;
import net.mercadosocial.moneda.model.Notification;
import net.mercadosocial.moneda.model.User;
import net.mercadosocial.moneda.util.Util;

import es.dmoral.toasty.Toasty;

/**
 * Created by julio on 28/02/18.
 */

public class NewPaymentDialog extends DialogFragment implements View.OnClickListener {

    private static final String ARG_NOTIFICATION = "arg_notification";
    private TextView tvNewPaymentInfo;
    private AppCompatButton btnPaymentCancel;
    private AppCompatButton btnPaymentAccept;
    private TextView btnDecideLater;
    private Notification notification;
    private PaymentInteractor paymentInteractor;
    private BaseActivity baseActivity;
    private OnCloseListener closeListener;

    private void findViews(View layout) {
        tvNewPaymentInfo = (TextView)layout.findViewById( R.id.tv_new_payment_info );
        btnPaymentCancel = (AppCompatButton)layout.findViewById( R.id.btn_payment_cancel );
        btnPaymentAccept = (AppCompatButton)layout.findViewById( R.id.btn_payment_accept );
        btnDecideLater = (TextView)layout.findViewById( R.id.btn_decide_later );

        btnPaymentCancel.setOnClickListener( this );
        btnPaymentAccept.setOnClickListener( this );
        btnDecideLater.setOnClickListener( this );
    }



    public static NewPaymentDialog newInstance(Notification notification) {
        NewPaymentDialog dialog = new NewPaymentDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATION, notification);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity) getActivity();
        notification = (Notification) getArguments().getSerializable(ARG_NOTIFICATION);
        paymentInteractor = new PaymentInteractor(getActivity(), baseActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.view_dialog_new_payment, null);
        findViews(layout);


        Data userData = App.getUserData(getActivity());
        float bonusPercent = notification.getUser_type() == User.TYPE_PERSON ?
                userData.getEntity().getBonus_percent_general() :
                userData.getEntity().getBonus_percent_entity();

        String bonus = Util.getDecimalFormatted(notification.getTotal_amount() * (bonusPercent / 100f), true);

        tvNewPaymentInfo.setText(Html.fromHtml(
                String.format(getString(R.string.payment_received_message),
                notification.getSender(),
                        Util.getDecimalFormatted(notification.getAmount(), false) + " " + getString(R.string.currency_name_plural),
                        Util.getDecimalFormatted(notification.getTotal_amount(), false) + " €",
                        bonus + " " + getString(R.string.currency_name_plural)) ));

        return layout;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        return dialog;
    }



    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_payment_accept:
                baseActivity.showProgressDialog(getString(R.string.processing));
                enableButtons(false);
                paymentInteractor.acceptPayment(notification.getId(), acceptCancelCallback);
                break;

            case R.id.btn_payment_cancel:
                baseActivity.showProgressDialog(getString(R.string.processing));
                enableButtons(false);
                paymentInteractor.cancelPayment(notification.getId(), acceptCancelCallback);
                break;

            case R.id.btn_decide_later:
                dismiss();
                break;
        }
    }

    private void enableButtons(boolean enable) {
        btnPaymentAccept.setEnabled(enable);
        btnPaymentCancel.setEnabled(enable);
    }

    private BaseInteractor.BaseApiPOSTCallback acceptCancelCallback =  new BaseInteractor.BaseApiPOSTCallback() {
        @Override
        public void onSuccess(Integer id) {
            Toasty.success(getActivity(), "OK").show();
            dismiss();
        }

        @Override
        public void onError(String message) {
            Toasty.error(getActivity(), message).show();
            enableButtons(true);
        }
    };

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (closeListener != null) {
            closeListener.onClose();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (closeListener != null) {
            closeListener.onClose();
        }
    }

    public NewPaymentDialog setOnCloseListener(OnCloseListener listener) {
        this.closeListener = listener;
        return this;
    }

}
