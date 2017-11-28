package net.mercadosocial.moneda.ui.entity_info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Offer;

import java.util.List;

/**
 * Created by julio on 7/07/16.
 */
public class EntitiyOffersAdapter extends RecyclerView.Adapter<EntitiyOffersAdapter.ViewHolder> {


    private List<Offer> offers;
    private Context context;
    private OnItemClickListener itemClickListener;

    private Integer selectedNumber = -1;


    public EntitiyOffersAdapter(Context context, List<Offer> offers) {
        this.context = context;
        this.offers = offers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.row_entity_offer, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position2) {

        final int safePosition = holder.getAdapterPosition();

        final Offer offer = getItemAtPosition(safePosition);

        holder.tvOfferName.setText(offer.getTitle());
//        holder.tvOfferCategory.setText(offer.getCategoriesString());
//
//        Picasso.with(context)
//                .load(offer.getBannerImage())
////                .placeholder(R.mipmap.img_default_grid)
//                .error(R.mipmap.ic_mes_v2_144)
//                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
//                .into(holder.imgOffer);


        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onOfferClicked(offer.getId(), safePosition);
            }
        });


    }

    private void addClickListener(View view, final int position) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (itemClickListener != null) {
//                    itemClickListener.onItemClick(v, position);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public Offer getItemAtPosition(int position) {
        return offers.get(position);
    }

    public void updateData(List<Offer> offers) {
        this.offers = offers;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgOffer;
        private TextView tvOfferName;
        public View rootView;

        public ViewHolder(View itemView) {

            super(itemView);

            imgOffer = (ImageView) itemView.findViewById(R.id.img_offer);
            tvOfferName = (TextView) itemView.findViewById(R.id.tv_offer_name);

            rootView = itemView;
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onOfferClicked(String id, int position);

    }
}
