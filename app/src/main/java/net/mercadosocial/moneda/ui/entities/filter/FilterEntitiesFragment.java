package net.mercadosocial.moneda.ui.entities.filter;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterEntitiesFragment extends BaseFragment implements FilterEntitiesView, View.OnClickListener {


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

        binding.btnApply.setOnClickListener(this);
        binding.btnRemoveFilter.setOnClickListener(this);

        boolean showFavsView = App.getUserData(getActivity()) != null;
        if (!showFavsView) {
            binding.switchOnlyFavs.setVisibility(View.GONE);
        }

        presenter.onCreate();

        return binding.getRoot();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_apply:
                String text = binding.editSearchEntities.getText().toString();
                boolean onlyFavs = binding.switchOnlyFavs.isChecked();
                boolean withBenefits = binding.switchWithBenefits.isChecked();
                boolean withBadge = binding.checkWithBadge.isChecked();
                presenter.applyFilter(text, onlyFavs, withBenefits, withBadge);
                WindowUtils.hideSoftKeyboard(getActivity());
                break;

            case R.id.btn_remove_filter:
                binding.editSearchEntities.setText("");
                presenter.removeFilter();
                WindowUtils.hideSoftKeyboard(getActivity());
                break;
        }
    }
}
