package net.mercadosocial.moneda.ui.fediverse.list;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.FediversePost;
import net.mercadosocial.moneda.ui.entity_info.gallery.GalleryFullScreenActivity;
import net.mercadosocial.moneda.util.EmojiGetter;
import net.mercadosocial.moneda.util.WebUtils;

import java.util.ArrayList;
import java.util.List;

public class FediverseAdapter extends RecyclerView.Adapter<FediverseAdapter.ViewHolder> {

    private List<FediversePost> posts;
    private final Context context;

    public FediverseAdapter(Context context, List<FediversePost> posts) {
        this.context = context;
        this.posts = posts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView logo;
        private TextView name;
        private TextView username;
        private TextView content;
        private TextView timePosted;
        private GridLayout mediaGallery;
        private List<ImageView> mediaAttachments;

        public ViewHolder(View itemView) {
            super(itemView);

            logo = (ImageView) itemView.findViewById(R.id.logo);
            name = (TextView) itemView.findViewById(R.id.name);
            username = (TextView) itemView.findViewById(R.id.username);
            content = (TextView) itemView.findViewById(R.id.content);
            timePosted = (TextView) itemView.findViewById(R.id.time_posted);
            mediaGallery = (GridLayout) itemView.findViewById(R.id.media_gallery);
            mediaAttachments = new ArrayList<>();
            mediaAttachments.add ((ImageView) itemView.findViewById(R.id.image_preview_1));
            mediaAttachments.add ((ImageView) itemView.findViewById(R.id.image_preview_2));
            mediaAttachments.add ((ImageView) itemView.findViewById(R.id.image_preview_3));
            mediaAttachments.add ((ImageView) itemView.findViewById(R.id.image_preview_4));
        }
    }

    @Override
    public FediverseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View fediversePostView = LayoutInflater.from(context).inflate(R.layout.row_fediverse_post, parent, false);

        return new ViewHolder(fediversePostView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final int safePosition = holder.getAdapterPosition();
        final FediversePost post = getItemAtPosition(safePosition);

        holder.name.setText(post.getAccountName());
        holder.username.setText(post.getAccountUsername());
        holder.timePosted.setText(post.getPublishedDate());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            EmojiGetter imageGetter = new EmojiGetter(context, holder.content);
            holder.content.setText(Html.fromHtml(post.getContent(), Html.FROM_HTML_MODE_COMPACT, imageGetter, null));
            holder.content.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            holder.content.setText(Html.fromHtml(post.getContent()));
        }

        String profilePicture = post.getProfilePicture();
        if (WebUtils.isValidLink(profilePicture)) {
            Picasso.get()
                    .load(profilePicture)
                    .into(holder.logo);
        }

        List<String> attachedImages = post.getAttachedImages();
        holder.mediaAttachments.forEach(m -> m.setVisibility(View.GONE));
        if (attachedImages != null && !attachedImages.isEmpty()) {
            for (int i = 0; i < attachedImages.size(); i++) {
                int attachedImageIndex = i;
                String attachedImage = attachedImages.get(attachedImageIndex);
                ImageView mediaAttachment = holder.mediaAttachments.get(attachedImageIndex);

                if (WebUtils.isValidLink(attachedImage)) {
                    Picasso.get()
                            .load(attachedImage)
                            .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                            .into(mediaAttachment);

                    mediaAttachment.setOnClickListener(v ->
                            GalleryFullScreenActivity.launchGalleryFullScreen(
                                    context,
                                    new Gson().toJson(attachedImages),
                                    attachedImageIndex
                            )
                    );

                    mediaAttachment.setVisibility(View.VISIBLE);
                    holder.mediaGallery.setVisibility(View.VISIBLE);
                } else {
                    mediaAttachment.setVisibility(View.GONE);
                }
            }

            updateFirstAttachedImageSpan(holder.mediaAttachments.get(0), attachedImages.size());
        } else {
            // If there are no attached images, hide the entire gallery container
            holder.mediaGallery.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public FediversePost getItemAtPosition(int position) {
        return posts.get(position);
    }

    public void updateData(List<FediversePost> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    private void updateFirstAttachedImageSpan(ImageView firstImage, int imageCount) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) firstImage.getLayoutParams();
            int columnSpan = imageCount == 3 ? 2 : 1;
            params.columnSpec = GridLayout.spec(0, columnSpan, 1);
            firstImage.setLayoutParams(params);
        }
    }

}
