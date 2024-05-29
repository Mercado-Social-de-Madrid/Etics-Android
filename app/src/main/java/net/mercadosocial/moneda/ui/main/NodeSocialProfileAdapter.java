package net.mercadosocial.moneda.ui.main;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.databinding.ItemNodeSocialProfileBinding;
import net.mercadosocial.moneda.model.SocialProfile;

import java.util.List;

public class NodeSocialProfileAdapter extends RecyclerView.Adapter<NodeSocialProfileAdapter.ViewHolder> {


    private List<SocialProfile> socialProfiles;
    private Context context;
    private OnItemClickListener itemClickListener;


    public NodeSocialProfileAdapter(Context context, List<SocialProfile> socialProfiles) {
        this.context = context;
        this.socialProfiles = socialProfiles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(context).inflate(R.layout.item_node_social_profile, parent, false);
        return new ViewHolder(contactView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final SocialProfile socialProfile = getItemAtPosition(position);

        holder.binding.tvSocialProfileName.setText(socialProfile.getName());

        Picasso.get()
                .load(socialProfile.getLogo())
                .placeholder(R.mipmap.ic_avatar_2)
                .error(R.mipmap.ic_avatar_2)
                .into(holder.binding.imgSocialIcon);


    }

    @Override
    public int getItemCount() {
        return socialProfiles.size();
    }

    public SocialProfile getItemAtPosition(int position) {
        return socialProfiles.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemNodeSocialProfileBinding binding;

        public ViewHolder(View itemView) {

            super(itemView);

            binding = ItemNodeSocialProfileBinding.bind(itemView);

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


