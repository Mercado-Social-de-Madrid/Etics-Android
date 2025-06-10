package net.mercadosocial.moneda.ui.entities.filter;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.databinding.FragmentFilterEntitiesBinding;
import net.mercadosocial.moneda.model.Category;
import net.mercadosocial.moneda.util.WindowUtils;

import java.util.List;

public class FilterEntitiesFragment extends BaseFragment implements FilterEntitiesView {


    private FilterEntitiesPresenter presenter;
    private FragmentFilterEntitiesBinding binding;
    private CategoriesFilterAdapter adapter;


    public FilterEntitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = FilterEntitiesPresenter.newInstance(this, getActivity());
        setBasePresenter(presenter);

        binding = FragmentFilterEntitiesBinding.inflate(inflater, container, false);

        configureButtons();

        boolean showFavsView = App.getUserData(getActivity()) != null;
        if (!showFavsView) {
            binding.switchOnlyFavs.setVisibility(View.GONE);
        }

        presenter.onCreate();

        return binding.getRoot();
    }

    private void configureButtons() {

        binding.btnApply.setOnClickListener(v -> {
            String text = binding.editSearchEntities.getText().toString();
            boolean onlyFavs = binding.switchOnlyFavs.isChecked();
            boolean withBenefits = binding.switchWithBenefits.isChecked();
            boolean withBadge = binding.switchWithBadge.isChecked();
            presenter.applyFilter(text, onlyFavs, withBenefits, withBadge);
            WindowUtils.hideSoftKeyboard(getActivity());
        });

        binding.btnRemoveFilter.setOnClickListener( v -> {
            presenter.onRemoveFilterClick();
            WindowUtils.hideSoftKeyboard(getActivity());
        });

        binding.icSemanticSearchInfo.setOnClickListener(v -> {
            alert(getString(R.string.semantic_search), getString(R.string.semantic_search_info));
        });

    }

    @Override
    public void onNodeChanged() {
        super.onNodeChanged();
        presenter.onNodeChanged();
    }

    public void onCreateEntitiesFragment() {
        resetFilter();
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (adapter == null) {
            adapter = new CategoriesFilterAdapter(getActivity(), categories);
            binding.recyclerCategories.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void resetFilter() {
        binding.editSearchEntities.setText("");
        binding.switchWithBadge.setChecked(false);
        binding.switchWithBenefits.setChecked(false);
        binding.switchOnlyFavs.setChecked(false);
    }


}
