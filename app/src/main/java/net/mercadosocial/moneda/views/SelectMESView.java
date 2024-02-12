package net.mercadosocial.moneda.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseInteractor;
import net.mercadosocial.moneda.databinding.ViewSelectMesBinding;
import net.mercadosocial.moneda.interactor.NodeInteractor;
import net.mercadosocial.moneda.model.Node;

import java.util.ArrayList;
import java.util.List;


public class SelectMESView extends FrameLayout {
    private final List<Node> nodes = new ArrayList<>();
    private MESListAdapter adapter;
    private ViewSelectMesBinding binding;

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
        binding = ViewSelectMesBinding.inflate(LayoutInflater.from(getContext()));

        adapter = new MESListAdapter(getContext(), nodes);
        binding.recyclerMesList.setAdapter(adapter);

        requestNodes();

        addView(binding.getRoot());
    }

    private void requestNodes() {

        binding.progressNodes.setVisibility(View.VISIBLE);

        new NodeInteractor(getContext(), null).getNodes(new BaseInteractor.BaseApiGETListCallback<Node>() {
            @Override
            public void onResponse(List<Node> list) {
                binding.progressNodes.setVisibility(View.GONE);
                nodes.clear();
                nodes.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                binding.progressNodes.setVisibility(View.GONE);
                binding.tvErrorMsg.setText(message);
            }
        });
    }

    public Node getSelectedNode() {
        if (adapter.getSelectedPosition() == -1) {
            return null;
        } else {
            return nodes.get(adapter.getSelectedPosition());
        }
    }

    public void selectNode(Node node) {
        for (int i = 0; i < nodes.size(); i++) {
            Node nodeItem = nodes.get(i);
            if (nodeItem.equals(node)) {
                adapter.setSelectedPosition(i);
                return;
            }
        }
    }

    public static class MESListAdapter extends RecyclerView.Adapter<MESListAdapter.ViewHolder> {


        private int selectedPosition = -1;
        private List<Node> nodes;
        private Context context;
        private OnItemClickListener itemClickListener;


        public MESListAdapter(Context context, List<Node> nodes) {
            this.context = context;
            this.nodes = nodes;
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

            final Node node = getItemAtPosition(holder.getAdapterPosition());
            holder.tvMESName.setText(node.getName());
            holder.tvMESName.setSelected(position2 == selectedPosition);
        }

        @Override
        public int getItemCount() {
            return nodes.size();
        }

        public Node getItemAtPosition(int position) {
            return nodes.get(position);
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
