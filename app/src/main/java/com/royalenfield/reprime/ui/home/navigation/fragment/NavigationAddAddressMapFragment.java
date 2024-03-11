package com.royalenfield.reprime.ui.home.navigation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;

public class NavigationAddAddressMapFragment extends REBaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener,  GoogleMap.OnMapLongClickListener {

    private SupportMapFragment supportMapFragment;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_createride, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_create_ride);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMarkerListeners();
        // Add a marker in tvm and move the camera
        LatLng tvm = new LatLng(8.524139, 76.936638);
        mMap.addMarker(new MarkerOptions().draggable(true)
                .position(tvm)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tvm,14));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.e("Map>> Long click",""+latLng.latitude+" "+latLng.longitude);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.e("Map>> Marker drag end",""+marker.getPosition().latitude+" "+marker.getPosition().longitude);

    }
    private void enableMarkerListeners() {
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }

}
