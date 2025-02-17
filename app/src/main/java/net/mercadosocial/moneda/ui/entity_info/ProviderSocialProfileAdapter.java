package net.mercadosocial.moneda.ui.entity_info;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.databinding.ItemProviderSocialProfileBinding;
import net.mercadosocial.moneda.model.SocialProfile;
import net.mercadosocial.moneda.views.CircleTransform;

import java.util.List;

public class ProviderSocialProfileAdapter extends RecyclerView.Adapter<ProviderSocialProfileAdapter.ViewHolder> {


    private List<SocialProfile> socialProfiles;
    private Context context;
    private OnItemClickListener itemClickListener;


    public ProviderSocialProfileAdapter(Context context, List<SocialProfile> socialProfiles) {
        this.context = context;
        this.socialProfiles = socialProfiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(context).inflate(R.layout.item_provider_social_profile, parent, false);
        return new ViewHolder(contactView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final SocialProfile socialProfile = getItemAtPosition(position);

        Picasso.get()
                .load(socialProfile.getLogo())
                .placeholder(R.mipmap.ic_avatar_2)
                .error(R.mipmap.ic_avatar_2)
//                .transform(new CircleTransform())
                .into(holder.binding.btnSocialProfile);

        holder.binding.btnSocialProfile.setContentDescription(socialProfile.getName());

    }

    @Override
    public int getItemCount() {
        return socialProfiles.size();
    }

    public SocialProfile getItemAtPosition(int position) {
        return socialProfiles.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemProviderSocialProfileBinding binding;

        public ViewHolder(View itemView) {

            super(itemView);

            binding = ItemProviderSocialProfileBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}


