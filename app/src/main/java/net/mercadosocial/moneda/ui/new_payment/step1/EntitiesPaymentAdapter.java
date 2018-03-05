package net.mercadosocial.moneda.ui.new_payment.step1;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Entity;

import java.util.List;

/**
 * Created by julio on 31/01/18.
 */


public class EntitiesPaymentAdapter extends RecyclerView.Adapter<EntitiesPaymentAdapter.ViewHolder> {


    private List<Entity> entities;
    private Context context;
    private OnItemClickListener itemClickListener;
    private int selectedPosition = -1;


    public EntitiesPaymentAdapter(Context context, List<Entity> entities) {
        this.context = context;
        this.entities = entities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.row_entity_payment, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position2) {

        final int safePosition = holder.getAdapterPosition();

        final Entity entity = getItemAtPosition(safePosition);

        holder.tvEntityName.setText(entity.getName());
        holder.tvEntityCategory.setText(entity.getCategoriesString());

        String logoUrl = entity.getLogoThumbnailOrCover();

        if (logoUrl != null) {

            Picasso.with(context)
                    .load(logoUrl)
//              .placeholder(R.mipmap.img_default_grid)
                    .placeholder(R.mipmap.ic_mes_v2_144_semitransp)
                    .error(R.mipmap.ic_mes_v2_144)
                    .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                    .into(holder.imgEntity);
        } else {
            holder.imgEntity.setImageResource(R.mipmap.ic_mes_v2_144);
        }

        holder.rootView.setSelected(safePosition == selectedPosition);

        addClickListener(holder.rootView, safePosition);


    }

    private void addClickListener(View view, final int position) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedPosition != -1) {
                    notifyItemChanged(selectedPosition);
                }

                selectedPosition = position;

                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, position);
                }

                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public Entity getItemAtPosition(int position) {
        return entities.get(position);
    }

    public void updateData(List<Entity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        private AppCompatImageView imgEntity;
        private TextView tvEntityName;
        private TextView tvEntityCategory;

        public ViewHolder(View itemView) {

            super(itemView);

            imgEntity = (AppCompatImageView) itemView.findViewById(R.id.img_entity);
            tvEntityName = (TextView) itemView.findViewById(R.id.tv_entity_name);
            tvEntityCategory = (TextView) itemView.findViewById(R.id.tv_entity_category);

            rootView = itemView;
        }

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}


