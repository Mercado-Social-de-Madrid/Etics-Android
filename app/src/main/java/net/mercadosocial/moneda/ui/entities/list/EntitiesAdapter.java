package net.mercadosocial.moneda.ui.entities.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Entity;

import java.util.List;

/**
 * Created by julio on 7/07/16.
 */
public class EntitiesAdapter extends RecyclerView.Adapter<EntitiesAdapter.ViewHolder> {


    private List<Entity> entities;
    private Context context;
    private OnItemClickListener itemClickListener;

    private Integer selectedNumber = -1;


    public EntitiesAdapter(Context context, List<Entity> entities) {
        this.context = context;
        this.entities = entities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.row_entity, parent, false);

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

        Picasso.with(context)
                .load(entity.getLogoFullUrl())
//                .placeholder(R.mipmap.img_default_grid)
                .error(R.mipmap.ic_mes_v2_144)
//                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(holder.imgEntity);

        holder.tvAddress.setText(entity.getAddress());

//        holder.imgStarred.setSelected(entity.isStarred());

//        int color = ContextCompat.getColor(context, entity.getImageLogoUrlFull() != null ? android.R.color.white : android.R.color.black);
//        holder.tvEventName.setTextColor(color);
//        holder.tvEventGenre.setTextColor(color);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onEntityClicked(entity.getId(), safePosition);
            }
        });

        holder.imgStarred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.imgStarred.setSelected(!holder.imgStarred.isSelected());
                itemClickListener.onEntityFavouriteClicked(entity.getId(), safePosition);
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

        private ImageView imgEntity;
        private TextView tvEntityName;
        private TextView tvEntityCategory;
        private ImageView imgStarred;
        private TextView tvAddress;
        public View rootView;

        public ViewHolder(View itemView) {

            super(itemView);

            imgEntity = (ImageView) itemView.findViewById(R.id.img_entity);
            tvEntityName = (TextView) itemView.findViewById(R.id.tv_entity_name);
            tvEntityCategory = (TextView) itemView.findViewById(R.id.tv_entity_category);
            imgStarred = (ImageView) itemView.findViewById(R.id.img_starred);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);

            rootView = itemView;
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onEntityClicked(String id, int position);

        void onEntityFavouriteClicked(String id, int position);

    }
}
