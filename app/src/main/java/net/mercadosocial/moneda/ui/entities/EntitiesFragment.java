package net.mercadosocial.moneda.ui.entities;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.ui.entities.list.EntitiesListFragment;
import net.mercadosocial.moneda.ui.entities.map.EntitiesMapFragment;
import net.mercadosocial.moneda.ui.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntitiesFragment extends BaseFragment implements EntitiesView, EntityListener {

    private EntitiesPresenter presenter;
    private MenuItem menuItemMapList;



    public EntitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = EntitiesPresenter.newInstance(this, getActivity());
        setBasePresenter(presenter);

        View layout = inflater.inflate(R.layout.fragment_entities, container, false);

        presenter.onCreate();


        setHasOptionsMenu(true);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            presenter.onPause();
        }
    }


    @Override
    public void onNodeChanged() {
        super.onNodeChanged();
        presenter.refreshData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_entities, menu);
        menuItemMapList = menu.findItem(R.id.menuItem_show_map_list);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItem_search:

                if (getActivity() != null) {
                    ((MainActivity) getActivity()).onMenuFilterClick();
                }

//                viewSearchEntities.setVisibility(viewSearchEntities.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//                if (viewSearchEntities.getVisibility() == View.GONE) {
//                    WindowUtils.hideSoftKeyboard(getActivity());
//                }

                break;

            case R.id.menuItem_show_map_list:
//                viewpagerEntities.setCurrentItem(viewpagerEntities.getCurrentItem() == 0 ? 1 : 0, true);
                presenter.onMapListButtonClick();

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    // PRESENTER CALLBACKS


    @Override
    public void showScreenType(int currentScreen) {
        Fragment fragment = null;
        switch (currentScreen) {
            case EntitiesPresenter.SCREEN_ENTITIES_TYPE_LIST:
                fragment = new EntitiesListFragment();
                break;

            case EntitiesPresenter.SCREEN_ENTITIES_TYPE_MAP:
                fragment = new EntitiesMapFragment();
                break;

        }

        if (menuItemMapList != null) {
            menuItemMapList.setIcon(currentScreen == EntitiesPresenter.SCREEN_ENTITIES_TYPE_LIST ? R.mipmap.ic_map : R.mipmap.ic_list);
        }

        getChildFragmentManager().beginTransaction().replace(R.id.frame_entities, fragment).commit();
    }



    // CHILD ENTITIES CALLBACKS
    @Override
    public void onEntityClick(int position, String id) {
        presenter.onEntityClicked(position, id);
    }

    @Override
    public void onEntityFavouriteClick(int position, boolean isFavourite) {
        presenter.onEntityFavouriteClicked(position, isFavourite);
    }


}
