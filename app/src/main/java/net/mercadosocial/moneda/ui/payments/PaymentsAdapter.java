package net.mercadosocial.moneda.ui.payments;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Payment;

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

         final int safePosition = holder.getAdapterPosition();

         final Payment payment = getItemAtPosition(safePosition);


         holder.tvNewPaymentInfo.setText(payment.getSender() + " te ha enviado un pago de " +
                 payment.getBoniatosAmountFormatted() + " Boniatos\n" +
                "La compra total es de: " + payment.getTotalAmountFormatted() + " €");

         holder.btnPaymentAccept.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 itemClickListener.onAcceptPaymentClick(v, safePosition);
             }
         });

         holder.btnPaymentCancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 itemClickListener.onCancelClick(v, safePosition);
             }
         });


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

     public void updateData(List<Payment> payments) {
         this.payments = payments;
         notifyDataSetChanged();
     }


     public static class ViewHolder extends RecyclerView.ViewHolder {

         public View rootView;
         private TextView tvNewPaymentInfo;
         private AppCompatButton btnPaymentCancel;
         private AppCompatButton btnPaymentAccept;


         public ViewHolder(View itemView) {

             super(itemView);

             tvNewPaymentInfo = (TextView)itemView.findViewById( R.id.tv_new_payment_info );
             btnPaymentCancel = (AppCompatButton)itemView.findViewById( R.id.btn_payment_cancel );
             btnPaymentAccept = (AppCompatButton)itemView.findViewById( R.id.btn_payment_accept );

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


