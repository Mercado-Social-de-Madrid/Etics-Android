package net.mercadosocial.moneda.ui.entities.filter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.model.Category;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterEntitiesFragment extends Fragment {


    private RecyclerView recyclerCategories;

    public FilterEntitiesFragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {
        recyclerCategories = (RecyclerView) layout.findViewById(R.id.recycler_categories);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_filter_entities, container, false);
        findViews(layout);

        recyclerCategories.setLayoutManager(new LinearLayoutManager(getActivity()));

        CategoriesFilterAdapter adapter = new CategoriesFilterAdapter(getActivity(), Category.categoriyNames);
        recyclerCategories.setAdapter(adapter);


        return layout;
    }


}
