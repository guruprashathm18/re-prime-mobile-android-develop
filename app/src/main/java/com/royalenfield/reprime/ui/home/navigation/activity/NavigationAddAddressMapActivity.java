package com.royalenfield.reprime.ui.home.navigation.activity;

import static com.royalenfield.reprime.ui.home.navigation.activity.NavigationWayPointSearchActivity.REQUESTCODE_ADD_HOME_ADDRESS;
import static com.royalenfield.reprime.ui.home.navigation.activity.NavigationWayPointSearchActivity.REQUESTCODE_ADD_OFFICE_ADDRESS;
import static com.royalenfield.reprime.utils.REConstants.KEY_TRIPPER_GTM;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.navigation.model.AddAddress;
import com.royalenfield.reprime.ui.home.navigation.model.RecentRouteLocationManager;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

public class NavigationAddAddressMapActivity extends REBaseActivity implements OnMapReadyCallback, TitleBarView.OnNavigationIconClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener, View.OnClickListener, LocationListener, GoogleMap.OnMapClickListener {
    public static final String TAG_MAP_FRAGMENT = NavigationAddAddressMapActivity.class.getName();
    private GoogleMap mMap;
    private NavigationAddAddressMapActivity.OnTouchListener listener;
    LatLng latLng = null;
    String mPLace = null;
    String locationType = "";
    String strLocation = "";
    TextView searchLocation;
    private LocationManager locationManager;
    private String provider;
    public static final int REQUESTCODE_ADD_FAVOURITE_SEARCH = 154;


    public static NavigationAddAddressMapActivity newInstance() {
        return new NavigationAddAddressMapActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_map_layout);
        Intent intent = getIntent();
        locationType = intent.getStringExtra("location_type");
        strLocation = intent.getStringExtra("edit");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        TitleBarView mTitleBarView = findViewById(R.id.save_address_header);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.save_location));
        if (locationType.equalsIgnoreCase("Home")){
            mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.set_home));
        }else if (locationType.equalsIgnoreCase("Work")){
            mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.set_work));
        }else{
            mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.set_default));
        }
        searchLocation = findViewById(R.id.search_location);
        searchLocation.setOnClickListener(this);
        Button btnSave = findViewById(R.id.btnsavelocation);
        btnSave.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setDefaultLocations();

        Bundle aParams = new Bundle();
        aParams.putString("screenName", "Tripper Save Location");
        REUtils.logGTMEvent(REConstants.ScreenViewManual, aParams);
    }

    private boolean isLocationEdit() {
        ArrayList<AddAddress> savedFavourites = RecentRouteLocationManager.getAddressList(this);
        boolean isDuplicate;
        if(strLocation != null) {
            for (AddAddress address : savedFavourites) {
                isDuplicate = strLocation.equalsIgnoreCase(address.getLocationName());
                if (isDuplicate) {
                    latLng = address.getLocationCoordinates();
                    return true;
                }
            }
        }
        return false;
    }

    private void setDefaultLocations() {
        if (RecentRouteLocationManager.getAddressList(this).size() == 0) {
            LatLng latLngTemp = new LatLng(0, 0);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Home", latLngTemp, 0), this, 0);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Work", latLngTemp, 1), this, 1);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Location 1", latLngTemp, 2), this, 2);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Location 2", latLngTemp, 3), this, 3);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Location 3", latLngTemp, 4), this, 4);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Location 4", latLngTemp, 5), this, 5);
            RecentRouteLocationManager.saveAddressList(new AddAddress("Location 5", latLngTemp, 6), this, 6);
        }
    }

    public void setOnTouchListener(NavigationAddAddressMapActivity.OnTouchListener listener) {
        this.listener = listener;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMarkerListeners();
        enableMyLocation();
    }


    private void drawMarker(LatLng latLng) {
        try {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().draggable(true)
                    .position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void enableMarkerListeners() {
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        RELog.e("Map>> Marker drag end", "" + marker.getPosition().latitude + " " + marker.getPosition().longitude);
        latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        drawMarker(latLng);
        getAddressofLatlng(latLng);
    }

    @Override
    public void onMapLongClick(LatLng latlng) {
        RELog.e("Map>> Long click", "" + latlng.latitude + " " + latlng.longitude);
        latLng = new LatLng(latlng.latitude, latlng.longitude);
        drawMarker(latLng);
        getAddressofLatlng(latLng);
    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }


    @Override
    public void onLocationChanged(Location location) {
        RELog.d(TAG_MAP_FRAGMENT, "Fetching location :" + location);
        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            boolean isLocAvailable = isLocationEdit();
            if (latLng == null && !isLocAvailable) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
            drawMarker(latLng);
            getAddressofLatlng(latLng);
        }
    }

    void enableMyLocation() {

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            RELog.e("NavigationAddAddressMapActivity", "Provider " + provider + " has been selected.");
            onLocationChanged(location);
            locationManager.removeUpdates(this);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_location:
                invokeAddressSearchScreen(locationType);
                break;
            case R.id.btnsavelocation:
                saveAddressToSavedList(locationType, latLng);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void saveAddressToSavedList(String selectedFav, LatLng latlng) {

        Bundle params = new Bundle();
        params.putString("eventCategory", "Tripper");
        params.putString("eventAction", "Save Location");
        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

        ArrayList<AddAddress> savedFavourites = RecentRouteLocationManager.getAddressList(this);
        boolean isDuplicate;
        if(selectedFav != null) {
            for (AddAddress address : savedFavourites) {
                isDuplicate = selectedFav.equalsIgnoreCase(address.getLocationName());
                if (isDuplicate) {
                    switch (address.getPosition()) {
                        case 0:
                            address.setLocationCoordinates(latlng);
                            RecentRouteLocationManager.updateAddressList(address, this, address.getPosition());
                            setResult(REQUESTCODE_ADD_HOME_ADDRESS, new Intent());
                            createToastMessage("Home");
                            break;
                        case 1:
                            address.setLocationCoordinates(latlng);
                            RecentRouteLocationManager.updateAddressList(address, this, address.getPosition());
                            setResult(REQUESTCODE_ADD_OFFICE_ADDRESS, new Intent());
                            createToastMessage("Work");
                            break;
                        default:
                            address.setLocationCoordinates(latlng);
                            RecentRouteLocationManager.updateAddressList(address, this, address.getPosition());
                            createToastMessage("Favourite");
                            break;
                    }
                }
            }
        }
    }


    private void createToastMessage(String value) {
        Toast.makeText(this, value + " Address added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng2) {
        latLng = new LatLng(latLng2.latitude, latLng2.longitude);
        drawMarker(latLng);
        getAddressofLatlng(latLng);
    }

    public interface OnTouchListener {
        void onTouch();
    }

    public class TouchableWrapper extends FrameLayout {
        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    listener.onTouch();
                    break;
                case MotionEvent.ACTION_UP:
                    listener.onTouch();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + event.getAction());
            }
            return super.dispatchTouchEvent(event);
        }
    }

    private void invokeAddressSearchScreen(String type) {
        RELog.e("invokeAddressSearchScreen: ", type);
        Intent intent = new Intent(this, NavigationWayPointSearchActivity.class);
        intent.putExtra("waypoint_type", "search_favourites");
        intent.putExtra("navigation_type", type);
        startActivityForResult(intent, REQUESTCODE_ADD_FAVOURITE_SEARCH);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {

            double latitude;
            double longitude;
            if (data != null) {
                mPLace = data.getStringExtra("place");
                latitude = data.getDoubleExtra("latitude", 0.0);
                longitude = data.getDoubleExtra("longitude", 0.0);
                latLng = new LatLng(latitude, longitude);
                searchLocation.setText(mPLace);
                drawMarker(latLng);
            }
        }
    }

    private void getAddressofLatlng(LatLng location) {
        List<Address> address = REUtils.getCurrentAddressList(this, location.latitude, location.longitude);
        String strLocAddress = "";
        if (!address.isEmpty()) {
            if (address.get(0).getAddressLine(0)!=null){
                strLocAddress = address.get(0).getAddressLine(0);
            } else if (address.get(0).getSubLocality() != null)
                strLocAddress = address.get(0).getSubLocality();
            else if (address.get(0).getSubLocality() == null && address.get(0).getLocality() != null) {
                strLocAddress = address.get(0).getLocality();
            } else if (address.get(0).getLocality() == null && address.get(0).getSubLocality() == null) {
                strLocAddress = address.get(0).getAddressLine(0);
            }
        } else {
            Toast.makeText(this, " Address not found, please try again.", Toast.LENGTH_SHORT).show();
        }
        searchLocation.setText(strLocAddress);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
}
