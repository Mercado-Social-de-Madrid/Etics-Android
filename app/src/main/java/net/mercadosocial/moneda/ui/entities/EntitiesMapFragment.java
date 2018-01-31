package net.mercadosocial.moneda.ui.entities;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntitiesMapFragment extends BaseFragment implements MapEventsReceiver {


    private MapEventsOverlay mapEntitiesOverlay;
    private MapView mapView;

    private final GeoPoint pointCenterMadrid = new GeoPoint(40.4378693,-3.8199624);

    public EntitiesMapFragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {
        mapView = (MapView) layout.findViewById(R.id.mapview);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_entities_map, container, false);

        findViews(layout);

        configureMap();

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));
    }

    private void configureMap() {

        mapEntitiesOverlay = new MapEventsOverlay(this);
        mapView.getOverlays().add(0, mapEntitiesOverlay);
        mapView.setTileSource(TileSourceFactory.HIKEBIKEMAP);
        mapView.setMultiTouchControls(true);

        mapView.getController().setCenter(pointCenterMadrid);
        mapView.getController().setZoom(14);

    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
}
