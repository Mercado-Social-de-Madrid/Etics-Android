package net.mercadosocial.moneda.ui.novelties;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Novelty;

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
        holder.tvNoveltyTextShort.setText(novelty.getDescriptionShortNovelty());

        Picasso.with(context)
                .load(novelty.getImageNoveltyUrl())
//                .placeholder(R.mipmap.img_default_grid)
                .error(R.mipmap.ic_mes_v2_144)
                .resizeDimen(R.dimen.width_image_small, R.dimen.height_image_small)
                .into(holder.imgNovelty);

        holder.tvNoveltyDate.setText(novelty.getDate());

        int backgroundColorResId = novelty.getNoveltyType() == Novelty.TYPE_NEWS ? R.color.green_light : android.R.color.transparent;
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, backgroundColorResId));

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                itemClickListener.onEntityClicked(novelty.getId());
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
        return novelties.size();
    }

    public Novelty getItemAtPosition(int position) {
        return novelties.get(position);
    }

    public void updateData(List<Novelty> novelties) {
        this.novelties = novelties;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        
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
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onEntityClicked(int idEntity);

        void onEventFavouriteClicked(int idEvent);

    }
}
