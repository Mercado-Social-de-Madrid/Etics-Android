package net.mercadosocial.moneda.ui.entities.filter;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Category;

import java.util.List;

public class CategoriesFilterAdapter extends RecyclerView.Adapter<CategoriesFilterAdapter.ViewHolder> {


    private List<Category> categories;
    private Context context;
    private OnItemClickListener itemClickListener;


    public CategoriesFilterAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View contactView = LayoutInflater.from(context).inflate(R.layout.row_filter_category, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position2) {

        final Category category = getItemAtPosition(holder.getAdapterPosition());

        holder.checkCategory.setText(category.getName());
        holder.checkCategory.setChecked(category.isChecked());

        int color = Color.BLACK;
//        if (category.getColor() != null) {
//            color = Color.parseColor(category.getColor());
//        }
        holder.checkCategory.setTextColor(color);
        setCheckBoxColor(holder.checkCategory, color, color);


    }

    public void setCheckBoxColor(CheckBox checkBox, int checkedColor, int uncheckedColor) {
        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {checkedColor, uncheckedColor};
        CompoundButtonCompat.setButtonTintList(checkBox, new
                ColorStateList(states, colors));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public Category getItemAtPosition(int position) {
        return categories.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkCategory;
        public View rootView;

        public ViewHolder(View itemView) {

            super(itemView);

            checkCategory = (CheckBox) itemView.findViewById(R.id.check_category);

            rootView = itemView;

            checkCategory.setOnCheckedChangeListener((buttonView, isChecked) -> getItemAtPosition(getAdapterPosition()).setChecked(isChecked));
        }

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}


