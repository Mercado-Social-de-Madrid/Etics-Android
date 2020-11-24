package net.mercadosocial.moneda.views;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.MES;

import java.util.List;




public class SelectMESView extends FrameLayout {
    private RecyclerView recyclerMESList;
    private MESListAdapter adapter;

    public SelectMESView(Context context) {
        super(context);
        init();
    }

    public SelectMESView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectMESView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View layout = View.inflate(getContext(), R.layout.view_select_mes, null);

        recyclerMESList = layout.findViewById(R.id.recycler_mes_list);
        recyclerMESList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MESListAdapter(getContext(), MES.mesList);
        recyclerMESList.setAdapter(adapter);

        addView(layout);
    }

    public MES getSelectedMES() {
        if (adapter.getSelectedPosition() == -1) {
            return null;
        } else {
            return MES.mesList.get(adapter.getSelectedPosition());
        }
    }

    public void setSelectedMESPosition(int position) {
        adapter.setSelectedPosition(position);
    }


    public class MESListAdapter extends RecyclerView.Adapter<MESListAdapter.ViewHolder> {


        private int selectedPosition = -1;
        private List<MES> mesList;
        private Context context;
        private OnItemClickListener itemClickListener;


        public MESListAdapter(Context context, List<MES> mesList) {
            this.context = context;
            this.mesList = mesList;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public void setSelectedPosition(int position) {
            this.selectedPosition = position;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View contactView = LayoutInflater.from(context).inflate(R.layout.row_mes_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position2) {

            final MES mes = getItemAtPosition(holder.getAdapterPosition());
            holder.tvMESName.setText(mes.getName());
            holder.tvMESName.setSelected(position2 == selectedPosition);
        }

        @Override
        public int getItemCount() {
            return mesList.size();
        }

        public MES getItemAtPosition(int position) {
            return mesList.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView tvMESName;
            public View rootView;

            public ViewHolder(View itemView) {
                super(itemView);

                tvMESName = (TextView) itemView.findViewById(R.id.tv_mes_name);
                rootView = itemView;
                rootView.setOnClickListener(v -> {
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, selectedPosition);
                    }
                });
            }

        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.itemClickListener = listener;
        }

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        adapter.setOnItemClickListener(listener);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
