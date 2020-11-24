package net.mercadosocial.moneda.ui.novelties.list;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Novelty;
import net.mercadosocial.moneda.util.Util;

import java.util.List;

/**
 * Created by julio on 7/07/16.
 */
public class NoveltiesAdapter extends RecyclerView.Adapter<NoveltiesAdapter.ViewHolder> {


    private List<Novelty> novelties;
    private Context context;
    private OnItemClickListener itemClickListener;

    private Integer selectedNumber = -1;


    public NoveltiesAdapter(Context context, List<Novelty> novelties) {
        this.context = context;
        this.novelties = novelties;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.row_novelty, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position2) {

        final int safePosition = holder.getAdapterPosition();

        final Novelty novelty = getItemAtPosition(safePosition);

        holder.tvNoveltyTitle.setText(novelty.getTitleNovelty());
        holder.tvNoveltyTextShort.setText(Html.fromHtml(novelty.getDescriptionShortNovelty()).toString());

        String image = novelty.getImageNoveltyUrl();

        if (Util.isValidLink(image)) {
            Picasso.with(context)
                    .load(image)
                    .placeholder(novelty.getNoveltyType() == Novelty.TYPE_NEWS ? R.mipmap.ic_mes_v2_144_semitransp : R.mipmap.ic_offer_semitransp)
                    .error(novelty.getNoveltyType() == Novelty.TYPE_NEWS ? R.mipmap.ic_mes_v2_144 : R.mipmap.ic_offer_solid)
                    .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                    .into(holder.imgNovelty);
        } else {
            holder.imgNovelty.setImageResource(novelty.getNoveltyType() == Novelty.TYPE_NEWS ? R.mipmap.ic_mes_v2_144 : R.mipmap.ic_offer_solid);
        }

        switch (novelty.getNoveltyType()) {
            case Novelty.TYPE_NEWS:
                String dateTextNews = String.format(context.getString(R.string.published), novelty.getDate());
                holder.tvNoveltyDate.setText(dateTextNews);
                break;

            case Novelty.TYPE_OFFER:
                String dateTextOffer = String.format(context.getString(R.string.valid_until), novelty.getDate());
                holder.tvNoveltyDate.setText(dateTextOffer);
                break;

                default:
                    throw new IllegalStateException("Novelty type don't exists: " + novelty.getNoveltyType());

        }

        holder.itemView.setSelected(novelty.getNoveltyType() == Novelty.TYPE_NEWS);
//        int backgroundColorResId = novelty.getNoveltyType() == Novelty.TYPE_NEWS ? R.color.green_light : android.R.color.transparent;
//        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, backgroundColorResId));


    }


    @Override
    public int getItemCount() {
        return novelties.size();
    }

    public Novelty getItemAtPosition(int position) {
        return novelties.get(position);
    }

    public void updateData(List<Novelty> novelties) {
        this.novelties = novelties;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        
        private final View rootView;
        private ImageView imgNovelty;
        private TextView tvNoveltyTitle;
        private TextView tvNoveltyTextShort;
        private TextView tvNoveltyDate;

        public ViewHolder(View itemView) {

            super(itemView);

            imgNovelty = (ImageView)itemView.findViewById( R.id.img_novelty );
            tvNoveltyTitle = (TextView)itemView.findViewById( R.id.tv_novelty_title );
            tvNoveltyTextShort = (TextView)itemView.findViewById( R.id.tv_novelty_text_short );
            tvNoveltyDate = (TextView)itemView.findViewById( R.id.tv_novelty_date );

            rootView = itemView;

            rootView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }
}
