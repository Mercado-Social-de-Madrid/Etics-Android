package net.mercadosocial.moneda.ui.entities.list;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.databinding.ItemProviderSocialProfileBinding;
import net.mercadosocial.moneda.databinding.RowEntityBinding;
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

    public EntitiesAdapter(Context context, List<Entity> entities, boolean showFavsStars) {
        this.context = context;
        this.entities = entities;
        this.showFavsStars = showFavsStars;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.row_entity, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position2) {

        final Entity entity = getItemAtPosition(position2);

        holder.binding.tvEntityName.setText(entity.getName());
        if (entity.getCategories().isEmpty()) {
            holder.binding.tvEntityCategory.setVisibility(View.GONE);
        } else {
            holder.binding.tvEntityCategory.setText(entity.getCategoriesString());
            holder.binding.tvEntityCategory.setVisibility(View.VISIBLE);
        }

        String urlImageCover = entity.getImageCover();
        if (urlImageCover != null && !urlImageCover.isEmpty()) {
            Picasso.get()
                    .load(urlImageCover)
                    .placeholder(R.mipmap.img_mes_default_banner_2)
                    .error(R.mipmap.img_mes_default_banner_2)
//                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                    .into(holder.binding.imgLogoEntity);
        }

        holder.binding.imgStarred.setContentDescription(context.getString(R.string.set_entity_fav, entity.getName()));

        holder.binding.imgStarred.setSelected(entity.isFavourite());
        holder.binding.imgStarred.setVisibility(showFavsStars ? View.VISIBLE : View.GONE);

        if (entity.isSearchResult()) {
            holder.binding.imgSemanticResult.setVisibility(View.VISIBLE);
            if (entity.getExactMatch() != null && entity.getExactMatch()) {
                holder.binding.imgSemanticResult.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_exact_result));
            } else {
                holder.binding.imgSemanticResult.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_semantic_result));
            }

        } else {
            holder.binding.imgSemanticResult.setVisibility(View.GONE);
        }

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

        private final RowEntityBinding binding;

        public ViewHolder(View itemView) {

            super(itemView);

            binding = RowEntityBinding.bind(itemView);


            binding.getRoot().setOnClickListener(v -> {
                Entity entity = getItemAtPosition(getAdapterPosition());
                itemClickListener.onEntityClicked(entity);
            });

            binding.imgStarred.setOnClickListener(v -> {
                binding.imgStarred.setSelected(!binding.imgStarred.isSelected());
                itemClickListener.onEntityFavouriteClicked(getAdapterPosition(), binding.imgStarred.isSelected());
            });

            binding.imgSemanticResult.setOnClickListener(v -> {
                Entity entity = getItemAtPosition(getAdapterPosition());
                itemClickListener.onEntitySearchResultClick(entity);
            });
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onEntityClicked(Entity entity);

        void onEntityFavouriteClicked(int position, boolean isFavourite);

        void onEntitySearchResultClick(Entity entity);



    }
}
