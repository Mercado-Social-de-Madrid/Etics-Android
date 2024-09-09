package net.mercadosocial.moneda.ui.entities.map;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseFragment;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.ui.entities.EntitiesChild;
import net.mercadosocial.moneda.ui.entities.EntitiesFragment;
import net.mercadosocial.moneda.ui.entities.EntitiesPresenter;
import net.mercadosocial.moneda.ui.entities.EntityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntitiesMapFragment extends BaseFragment implements EntitiesChild, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


//    private final LatLng pointCenterMadrid = new LatLng(40.4378693, -3.8199624);

    private EntityListener entityListener;
    private GoogleMap mMap;

    List<Marker> markersEntities = new ArrayList<>();
    private boolean mapLoaded;

    public EntitiesMapFragment() {
        // Required empty public constructor
    }

    private void findViews(View layout) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View layout = inflater.inflate(R.layout.fragment_entities_map, container, false);
        findViews(layout);

        configureMap();

        setEntityListener((EntityListener) getParentFragment());


        return layout;
    }


    private void configureMap() {

//        showProgressDialog(getString(R.string.loading));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = new SupportMapFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_maps, mapFragment).commit();
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        /*
        Styling Google Map:
        https://mapstyle.withgoogle.com/
        https://developers.google.com/maps/documentation/android-api/style-reference
         */
//        boolean success = googleMap.setMapStyle(
//                MapStyleOptions.loadRawResourceStyle(
//                        getActivity(), R.raw.style_map));

        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);

        mMap.setOnInfoWindowClickListener(this);

//        mMap.setOnMarkerClickListener(this);

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointCenterMadrid, 8));

        mMap.setOnMapLoadedCallback(() -> {
            mapLoaded = true;
            updateCamera();
        });

        getEntitiesPresenter().updateData();
    }

    private void updateCamera() {

        if (!mapLoaded) {
            return;
        }

        if (markersEntities == null || markersEntities.isEmpty()) {
            return;
        }

        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        for (Marker marker : markersEntities) {
            latLngBoundsBuilder.include(marker.getPosition());
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(),
                getResources().getDimensionPixelSize(R.dimen.padding_map)));
    }

    private EntitiesPresenter getEntitiesPresenter() {
        return (EntitiesPresenter) ((EntitiesFragment) getParentFragment()).getBasePresenter();
    }

    @Override
    public void updateEntities(List<Entity> entities) {

        showEntities(entities);
        updateCamera();

    }

    public void showEntities(List<Entity> entities) {

        clearMarkers();

        for (Entity entity : entities) {

            Marker markerEntity = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(entity.getLatitude(), entity.getLongitude()))
                    .title(entity.getName())
//                .snippet(subtitle)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_mes))
                    .visible(true));

            markerEntity.setTag(entity);

            markersEntities.add(markerEntity);

//            Marker marker = new Marker(map);
//            marker.setPosition(new GeoPoint(entity.getLatitude(), entity.getLongitude()));
//            marker.setTitle(entity.getName());
//            marker.setId(entity.getId());
//            marker.setOnMarkerClickListener(this);
//
//            EntityInfoWindow markerInfoWindow = new EntityInfoWindow(R.layout.marker_info_window, map);
//            markerInfoWindow.setOnEntityClickListener(entityListener);
//            marker.setInfoWindow(markerInfoWindow);
//
//            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//            map.getOverlays().add(marker);
        }


    }

    @Override
    public void onError(boolean showEmptyView) {

    }

    private void clearMarkers() {
        for (Marker markerEntity : markersEntities) {
            markerEntity.remove();
        }

        markersEntities.clear();
    }

    public void setEntityListener(EntityListener entityListener) {
        this.entityListener = entityListener;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        if (entityListener != null) {
            Entity entity = (Entity) marker.getTag();
            entityListener.onEntityClick(-1, entity.getId());
        }
    }
}
