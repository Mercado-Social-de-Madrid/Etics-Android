package net.mercadosocial.moneda.ui.payments;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.model.Payment;
import net.mercadosocial.moneda.model.User;
import net.mercadosocial.moneda.util.Util;

import java.util.List;

/**
 * Created by julio on 28/02/18.
 */


public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {


    private List<Payment> payments;
    private Context context;
    private OnItemClickListener itemClickListener;


    public PaymentsAdapter(Context context, List<Payment> payments) {
        this.context = context;
        this.payments = payments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.row_payment, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position2) {

        final Payment payment = getItemAtPosition(holder.getAdapterPosition());

        holder.tvNewPaymentAccount.setText(payment.getSender());
        holder.tvNewPaymentConcept.setText(payment.getConcept());
        holder.tvNewPaymentConcept.setVisibility(payment.getConcept() == null || payment.getConcept().isEmpty() ? View.GONE : View.VISIBLE);
        holder.tvNewPaymentDatetime.setText(payment.getTimestampFormatted());

        // Falta info de si quien paga es entidad o usuario para calcular con la bonificación correspondiente
        Data userData = App.getUserData(context);
        float bonusPercent = TextUtils.equals(payment.getUser_type(), User.TYPE_PERSON) ?
                userData.getEntity().getBonus_percent_general() :
                userData.getEntity().getBonus_percent_entity();

        String bonus = Util.getDecimalFormatted(payment.getTotal_amount() * (bonusPercent / 100f), true);

        String textInfo = String.format(context.getString(R.string.payment_info_format),
                payment.getTotalAmountFormatted() + " " + context.getString(R.string.euros),
                payment.getCurrencyAmountFormatted() + " " + context.getString(R.string.currency_name_abrev),
                bonus + " " + context.getString(R.string.currency_name_abrev));

        Util.setHtmlLinkableText(holder.tvNewPaymentInfo, textInfo);

//                payment.getSender() + " te ha enviado un pago de " +
//                payment.getCurrencyAmountFormatted() + " Boniatos\n" +
//                "La compra total es de: " + payment.getTotalAmountFormatted() + " €");


        holder.btnPaymentAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onAcceptPaymentClick(v, holder.getAdapterPosition());
            }
        });

        holder.btnPaymentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onCancelClick(v, holder.getAdapterPosition());
            }
        });

        holder.btnPaymentAccept.setEnabled(!payment.isBlockButtons());
        holder.btnPaymentCancel.setEnabled(!payment.isBlockButtons());

//         addClickListener(holder.rootView, safePosition);


    }

//     private void addClickListener(View view, final int position) {
//
//         view.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//
//                 if (itemClickListener != null) {
//                     itemClickListener.onItemClick(v, position);
//                 }
//             }
//         });
//     }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public Payment getItemAtPosition(int position) {
        return payments.get(position);
    }

    public void updateData(List<Payment> payments, boolean notify) {
//        this.payments.clear();
//        this.payments.addAll(payments);
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public void onItemRemoved(int position) {
        notifyItemRemoved(position);

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNewPaymentAccount;
        private final TextView tvNewPaymentConcept;
        public View rootView;
        private TextView tvNewPaymentInfo;
        private AppCompatButton btnPaymentCancel;
        private AppCompatButton btnPaymentAccept;
        private final TextView tvNewPaymentDatetime;


        public ViewHolder(View itemView) {

            super(itemView);

            tvNewPaymentInfo = (TextView) itemView.findViewById(R.id.tv_new_payment_info);
            tvNewPaymentAccount = (TextView) itemView.findViewById(R.id.tv_new_payment_account);
            tvNewPaymentConcept = (TextView) itemView.findViewById(R.id.tv_new_payment_concept);
            tvNewPaymentDatetime = (TextView) itemView.findViewById(R.id.tv_new_payment_datetime);
            btnPaymentCancel = (AppCompatButton) itemView.findViewById(R.id.btn_payment_cancel);
            btnPaymentAccept = (AppCompatButton) itemView.findViewById(R.id.btn_payment_accept);

            rootView = itemView;
        }

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        //         void onItemClick(View view, int position);
        void onAcceptPaymentClick(View view, int position);

        void onCancelClick(View view, int position);
    }
}


