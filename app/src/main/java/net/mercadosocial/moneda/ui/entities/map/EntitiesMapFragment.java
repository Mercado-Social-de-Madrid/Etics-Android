package net.mercadosocial.moneda.ui.entities.map;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.entities.EntitiesPagerAdapter;
import net.mercadosocial.moneda.ui.entities.EntitiesPresenter;
import net.mercadosocial.moneda.ui.entities.EntitiesView;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntitiesMapFragment extends BaseFragment implements MapEventsReceiver, EntitiesView, Marker.OnMarkerClickListener {


    private MapEventsOverlay mapEntitiesOverlay;
    private MapView map;

    private final GeoPoint pointCenterMadrid = new GeoPoint(40.4378693,-3.8199624);
    private EntitiesPresenter presenter;
    private EntitiesPagerAdapter.EntityListener entityListener;

    public EntitiesMapFragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {
        map = (MapView) layout.findViewById(R.id.mapview);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = EntitiesPresenter.newInstance(this, getActivity());
        setBasePresenter(presenter);

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
        map.getOverlays().add(0, mapEntitiesOverlay);
        map.setTileSource(TileSourceFactory.HIKEBIKEMAP);
        map.setMultiTouchControls(true);

        map.getController().setCenter(pointCenterMadrid);
        map.getController().setZoom(10d);
        map.setBuiltInZoomControls(false);

    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    @Override
    public void showEntities(List<Entity> entities) {

        map.getOverlays().clear();

        for (Entity entity : entities) {
            Marker marker = new Marker(map);
            marker.setPosition(new GeoPoint(entity.getLatitude(), entity.getLongitude()));
            marker.setTitle(entity.getName());
            marker.setId(entity.getId());
            marker.setOnMarkerClickListener(this);

            EntityInfoWindow markerInfoWindow = new EntityInfoWindow(R.layout.marker_info_window, map);
            markerInfoWindow.setOnEntityClickListener(entityListener);
            marker.setInfoWindow(markerInfoWindow);

            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(marker);
        }

        map.invalidate();

    }

    public void setEntityListener(EntitiesPagerAdapter.EntityListener entityListener) {
        this.entityListener = entityListener;
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
//        toast(marker.getTitle());

        MarkerInfoWindow.closeAllInfoWindowsOn(map);
        marker.showInfoWindow();

        map.getController().animateTo(marker.getPosition());
        return false;
    }
}
