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


    private boolean showFavsStars;
    private List<Entity> entities;
    private Context context;
    private OnItemClickListener itemClickListener;

    private Integer selectedNumber = -1;


    public EntitiesAdapter(Context context, List<Entity> entities, boolean showFavsStars) {
        this.context = context;
        this.entities = entities;
        this.showFavsStars = showFavsStars;
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

        final Entity entity = getItemAtPosition(position2);

        holder.tvEntityName.setText(entity.getName());
//        holder.tvEntityCategory.setText(entity.getCategoriesString());
        holder.tvEntityCategory.setVisibility(View.GONE);

        Picasso.with(context)
                .load(entity.getImageCover())
                .placeholder(R.mipmap.img_mes_default_banner_2)
                .error(R.mipmap.img_mes_default_banner_2)
//                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(holder.imgEntity);

        holder.tvAddress.setText(entity.getAddress());

        holder.imgStarred.setSelected(entity.isFavourite());
        holder.imgStarred.setVisibility(showFavsStars ? View.VISIBLE : View.INVISIBLE);

//        holder.imgStarred.setSelected(entity.isStarred());

//        int color = ContextCompat.getColor(context, entity.getImageLogoUrlFull() != null ? android.R.color.white : android.R.color.black);
//        holder.tvEventName.setTextColor(color);
//        holder.tvEventGenre.setTextColor(color);


    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public Entity getItemAtPosition(int position) {
        return entities.get(position);
    }

    public void updateData() {
        notifyDataSetChanged();
    }

    public void setShowFavsStarts(boolean showFavsStars) {
        this.showFavsStars = showFavsStars;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

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


            rootView.setOnClickListener(v -> {
                Entity entity = getItemAtPosition(getAdapterPosition());
                itemClickListener.onEntityClicked(entity.getId(), getAdapterPosition());
            });

            imgStarred.setOnClickListener(v -> {
                Entity entity = getItemAtPosition(getAdapterPosition());
                imgStarred.setSelected(!imgStarred.isSelected());
                itemClickListener.onEntityFavouriteClicked(getAdapterPosition(), imgStarred.isSelected());
            });
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onEntityClicked(String id, int position);

        void onEntityFavouriteClicked(int position, boolean isFavourite);

    }
}
