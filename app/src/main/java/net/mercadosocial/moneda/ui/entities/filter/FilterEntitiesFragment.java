package net.mercadosocial.moneda.ui.entities.filter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Category;
import net.mercadosocial.moneda.util.WindowUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterEntitiesFragment extends BaseFragment implements FilterEntitiesView, View.OnClickListener {


    private FilterEntitiesPresenter presenter;
    private CategoriesFilterAdapter adapter;
    private EditText editSearchEntities;
    private SwitchCompat switchOnlyFavs;
    private RecyclerView recyclerCategories;
    private AppCompatButton btnApply;
    private View btnRemoveFilter;
    private View viewSeparatorFavs;

    private void findViews(View layout) {
        editSearchEntities = (EditText) layout.findViewById(R.id.edit_search_entities);
        switchOnlyFavs = (SwitchCompat) layout.findViewById(R.id.switch_only_favs);
        recyclerCategories = (RecyclerView) layout.findViewById(R.id.recycler_categories);
        btnApply = (AppCompatButton) layout.findViewById(R.id.btn_apply);
        btnRemoveFilter = layout.findViewById(R.id.btn_remove_filter);
        viewSeparatorFavs = layout.findViewById(R.id.view_separator_favs);

        btnApply.setOnClickListener(this);
        btnRemoveFilter.setOnClickListener(this);
    }


    public FilterEntitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = FilterEntitiesPresenter.newInstance(this, getActivity());
        setBasePresenter(presenter);

        View layout = inflater.inflate(R.layout.fragment_filter_entities, container, false);
        findViews(layout);

        recyclerCategories.setLayoutManager(new LinearLayoutManager(getActivity()));


        boolean showFavsView = App.getUserData(getActivity()) != null;
        if (!showFavsView) {
            switchOnlyFavs.setVisibility(View.GONE);
            viewSeparatorFavs.setVisibility(View.GONE);
        }

        presenter.onCreate();

        return layout;
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (adapter == null) {
            adapter = new CategoriesFilterAdapter(getActivity(), categories);
            recyclerCategories.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_apply:
                String text = editSearchEntities.getText().toString();
                boolean onlyFavs = switchOnlyFavs.isChecked();
                presenter.applyFilter(text, onlyFavs);
                WindowUtils.hideSoftKeyboard(getActivity());
                break;

            case R.id.btn_remove_filter:
                editSearchEntities.setText("");
                presenter.removeFilter();
                WindowUtils.hideSoftKeyboard(getActivity());
                break;
        }
    }
}
