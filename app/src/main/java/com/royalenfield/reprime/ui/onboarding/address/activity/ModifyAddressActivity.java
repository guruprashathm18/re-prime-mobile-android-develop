package com.royalenfield.reprime.ui.onboarding.address.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.service.asynctask.GetLocationFromAddressAsyncTask;
import com.royalenfield.reprime.ui.home.service.asynctask.ServiceAsyncTaskListeners;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import com.royalenfield.reprime.utils.RELog;

public class ModifyAddressActivity extends REBaseActivity implements View.OnClickListener,
        TitleBarView.OnNavigationIconClickListener, ServiceAsyncTaskListeners.onLocationComplete,
        OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {

    private static final int LOCATION_SETTINGS_REQUEST = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private LatLng latLngOfUserEnteredAddress;
    private boolean mIsEditProfileOrIsRidesAndService;
    private EditText mAddressLine1;
    private EditText mAddressLine2;
    //Google Map
    private GoogleMap googleMap;
    private LatLng latLngOfUserCurrentLocation;
    private Marker currentLocationMarker;
    private String oldAddress;
    private String oldAddressCopy;
    private String oldFlatCopy;
    private boolean imagePin = false;
    private LocationManager locationManager;
    private Location currentLocation;

    public static Intent getIntent(Context context) {
        return new Intent(context, ModifyAddressActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_address);
        findViewById(R.id.imageView_location_pin).setOnClickListener(this);
        initViews();
        showLoading();
        new GetLocationFromAddressAsyncTask(oldAddress, this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleMap = null;
    }

    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.tb_modify_address);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().
                getString(R.string.title_modify_address));
        mAddressLine1 = findViewById(R.id.editText_address);
        mAddressLine2 = findViewById(R.id.editText_address_two);
        Button mBtSaveAddress = findViewById(R.id.button_save_address);
        mBtSaveAddress.setOnClickListener(this);
        TextView mAddNewAddress = findViewById(R.id.add_new_address);
        mAddNewAddress.setOnClickListener(this);
        Intent intent = getIntent();
        mIsEditProfileOrIsRidesAndService = intent.getBooleanExtra("IsEditProfileOrIsRidesAndService", true);
        try {
            oldAddress = REPreference.getInstance().getString(getApplicationContext(), REConstants.KEY_ADDRESS);
            String oldFlat = REPreference.getInstance().getString(getApplicationContext(), REConstants.KEY_FLAT_NO);
            oldAddressCopy = oldAddress;
            oldFlatCopy = oldFlat;
            mAddressLine1.setText(oldAddress);
            mAddressLine2.setText(oldFlat);
        } catch (PreferenceException e) {
            RELog.e(e);
        }

        mAddressLine1.setSelection(mAddressLine1.getText().length());
        getAddressPreference();
    }

    private void getAddressPreference() {
        latLngOfUserCurrentLocation = new LatLng(REUserModelStore.getInstance().getLatitude(),
                REUserModelStore.getInstance().getLongitude());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save_address:
                String flatNo = mAddressLine2.getText().toString().trim();
                String firstLineAddress = mAddressLine1.getText().toString();
                try {
                    REPreference.getInstance().putString(getApplicationContext(), REConstants.KEY_ADDRESS, firstLineAddress);
                    REPreference.getInstance().putString(getApplicationContext(), REConstants.KEY_FLAT_NO, flatNo);
                } catch (PreferenceException e) {
                    RELog.e(e);
                }
                Intent intent1 = new Intent();
                intent1.putExtra(REConstants.KEY_ADDRESS, firstLineAddress);
                intent1.putExtra(REConstants.KEY_FLAT_NO, flatNo);
                setResult(RESULT_OK, intent1);
                if (mAddressLine1.getText().toString().trim().length() <= 0) {
                    Toast.makeText(this, "Address cannot be empty",
                            Toast.LENGTH_LONG).show();
                    try {
                        REPreference.getInstance().putString(getApplicationContext(), REConstants.KEY_ADDRESS, oldAddressCopy);
                        REPreference.getInstance().putString(getApplicationContext(), REConstants.KEY_FLAT_NO, oldFlatCopy);
                    } catch (PreferenceException e) {
                        RELog.e(e);
                    }
                    Intent intent2 = new Intent();
                    intent2.putExtra(REConstants.KEY_ADDRESS, oldAddressCopy);
                    intent2.putExtra(REConstants.KEY_FLAT_NO, oldFlatCopy);
                    setResult(RESULT_OK, intent2);
                } else {
                    finish();
                }
                break;
            case R.id.add_new_address:
                Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                intent.putExtra(REConstants.KEY_IS_EDIT_PROFILE, mIsEditProfileOrIsRidesAndService);
                startActivity(intent);
                break;
            case R.id.imageView_location_pin:
                imagePin = true;
                animateCamera(latLngOfUserCurrentLocation);
        }
    }

    private void setCurrentAddress(double latitude, double longitude) {
        mAddressLine1.setText(REUtils.getCurrentAddressFromLocation(this, latitude, longitude));
    }

    private void showMarker(@NonNull LatLng currentLatLang) {
        if (currentLocationMarker == null && googleMap != null) {
            if (currentLatLang != null && currentLatLang.latitude != 0 && currentLatLang.longitude != 0) {
                currentLocationMarker = googleMap.addMarker(new MarkerOptions().
                        icon(BitmapDescriptorFactory.defaultMarker()).position(currentLatLang).draggable(true));
            }
        }
    }

    private void animateCamera(LatLng currentLatLang) {
        if (currentLatLang != null) {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (statusOfGPS) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Go to settings to enable permissions", Toast.LENGTH_SHORT).show();
                } else {
                    LatLng latLng = new LatLng(currentLatLang.latitude, currentLatLang.longitude);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                            latLng, 14);
                    googleMap.setMyLocationEnabled(true);
                    if (googleMap != null) googleMap.animateCamera(cameraUpdate);
                }
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Go to settings to enable permissions", Toast.LENGTH_SHORT).show();
                } else {
                    enableTheLocation();
                }
            }
        }
    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //Success Perform Task Here
                    if (imagePin) {
                        getCurLoc();
                    } else {
                        animateCamera(latLngOfUserEnteredAddress);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(ModifyAddressActivity.this, "Unable to fetch location on map",
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private void getCurLoc() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) REApplication.getAppContext()
                    .getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                                currentLocation = location;
                                saveLocToPref();
                                locationManager.removeUpdates(this);
                                getAddressPreference();
                                animateCamera(latLngOfUserCurrentLocation);
                                hideLoading();
                            }
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
        }
    }

    private void saveLocToPref() {
        if (currentLocation != null) {
            REUserModelStore.getInstance().setLatitude(currentLocation.getLatitude());
            REUserModelStore.getInstance().setLongitude(currentLocation.getLongitude());
        }
    }

    @Override
    public void onAddressFromLocationComplete(LatLng latLng) {
        latLngOfUserEnteredAddress = latLng;
        hideLoading();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.modify_map);
        supportMapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;
        if (latLngOfUserCurrentLocation == null || latLngOfUserCurrentLocation.latitude == 0 || latLngOfUserCurrentLocation.longitude == 0) {
            enableTheLocation();
        } else if (latLngOfUserEnteredAddress != null && latLngOfUserEnteredAddress.latitude != 0 && latLngOfUserEnteredAddress.longitude != 0) {
            animateCamera(latLngOfUserEnteredAddress);
            showMarker(latLngOfUserEnteredAddress);
        } else {
            Toast.makeText(ModifyAddressActivity.this, "Unable to fetch location on map",
                    Toast.LENGTH_LONG).show();
        }
        UiSettings mUiSettings = googleMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(false);
        mUiSettings.setScrollGesturesEnabled(true);
        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (latLng != null && latLng.latitude != 0 && latLng.longitude != 0) {
            if (currentLocationMarker != null) currentLocationMarker.remove();
            currentLocationMarker = null;
            showMarker(latLng);
            setCurrentAddress(latLng.latitude, latLng.longitude);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker.getPosition() != null) {
            setCurrentAddress(marker.getPosition().latitude, marker.getPosition().longitude);
        }
    }


}
