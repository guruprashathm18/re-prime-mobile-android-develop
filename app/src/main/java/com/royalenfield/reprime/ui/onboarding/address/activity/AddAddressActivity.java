package com.royalenfield.reprime.ui.onboarding.address.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.onboarding.editprofile.activity.EditProfileActivity;
import com.royalenfield.reprime.utils.REUtils;

import java.util.HashMap;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

/**
 * This activity is for adding user address
 */
public class AddAddressActivity extends REBaseActivity implements View.OnClickListener, TitleBarView.OnNavigationIconClickListener,
        OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {

    private boolean mIsEditProfileOrIsRidesAndService;
    private EditText mETAddress;
    private int RequestCode = 1;
    private GoogleMap googleMap;
    private Marker currentLocationMarker;
    private String ADDRESS;
    private HashMap<String, String> mAddressHashMap = new HashMap<>();
    private LatLng latLng, currentLatLng;
    private EditText mFlatNoHouseNo;

    public static Intent getIntent(Context context) {
        return new Intent(context, AddAddressActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        //initializes views
        initViews();
        showLoading();
        enableTheLocation();
    }

    /**
     * Loads map after checking the location
     */
    public void loadMapWithCurrentLocation() {
        //Getting map shared preference
        latLng = new LatLng(REUserModelStore.getInstance().getLatitude(),
                REUserModelStore.getInstance().getLongitude());
        currentLatLng = latLng;
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        findViewById(R.id.imageView_current_location_pin).setOnClickListener(this);
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

    /**
     * Initializes all the views.
     */
    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.tb_add_address);
        mTitleBarView.bindData(this, R.drawable.icon_close, getResources().
                getString(R.string.title_add_address));
        Button mButtonAddAddress = findViewById(R.id.button_add_this_address);
        mButtonAddAddress.setOnClickListener(this);
        mETAddress = findViewById(R.id.editText_address);
        mETAddress.setOnClickListener(this);
        mFlatNoHouseNo = findViewById(R.id.editText_address_two);

        Intent intent = getIntent();
        mIsEditProfileOrIsRidesAndService = intent.getBooleanExtra("IsEditProfileOrIsRidesAndService", true);
    }

    /**
     * @param v Handling onClick actions
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_this_address:
                addThisAddress();
                break;
            case R.id.editText_address:
                Intent intent = new Intent(getApplicationContext(), AddLocationActivity.class);
                intent.putExtra("ADDRESS", mAddressHashMap);
                startActivityForResult(intent, RequestCode);
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                break;

            case R.id.imageView_current_location_pin:
                animateCamera(currentLatLng);
                break;
        }
    }

    /**
     * Saves the address.
     */
    private void addThisAddress() {
        if (!mETAddress.getText().toString().isEmpty()) {
            Intent intent1 = new Intent();
            if (mIsEditProfileOrIsRidesAndService) {
                // Setting home activity but we are using onResume in REServicingFragment to update the address
//                intent1.setClass(getApplicationContext(), REHomeActivity.class);
            } else {
                intent1.setClass(getApplicationContext(), EditProfileActivity.class);
            }
            String flatNo = mFlatNoHouseNo.getText().toString();
            String firstLineAddress = mETAddress.getText().toString();
            intent1.putExtra("flatNo", flatNo);
            intent1.putExtra("address", firstLineAddress);
            ADDRESS = (!(flatNo.isEmpty()) ? flatNo + "," : "") + mETAddress.getText().toString();
            saveAddressToPreferences(flatNo, firstLineAddress);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            setResult(1, intent1);
            overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Address is mandatory", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param requestCode request code.
     * @param resultCode  result code.
     * @param data        This gets called when we come back from AddLocationActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Setting address when coming back from AddLocationActivity
        //Google map
        if (requestCode == RequestCode && data != null) {
            //Getting address from Shared preference
            if (data.getExtras() != null) {
                mAddressHashMap = (HashMap<String, String>) data.getSerializableExtra("ADDRESS");
            }
            ADDRESS = mAddressHashMap.get("name") + "," + mAddressHashMap.get("street") + ","
                    + mAddressHashMap.get("city") + "," + mAddressHashMap.get("pin") + "," + mAddressHashMap.get("state");
            mETAddress.setText(ADDRESS);
        }
    }

    /**
     * This is to finish the activity onClick of title bar close Icon
     */
    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        hideLoading();
        if (latLng != null && latLng.latitude != 0 && latLng.longitude != 0) {
            animateCamera(latLng);
            showMarker(latLng);
            setCurrentAddress(latLng.latitude, latLng.longitude);
            setCurrentLocationHashMap(latLng.latitude, latLng.longitude);
        } else {
            Toast.makeText(AddAddressActivity.this, "Unable to fetch location on map",
                    Toast.LENGTH_LONG).show();
        }
        UiSettings mUiSettings = googleMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(false);
        mUiSettings.setScrollGesturesEnabled(true);
        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnMapLongClickListener(this);
    }

    /**
     * Save the user address to preference.
     */
    private void saveAddressToPreferences(String flatNo, String firstLineAddress) {
        try {
            REPreference.getInstance().putString(getApplicationContext(), "flatNo", flatNo);
            REPreference.getInstance().putString(getApplicationContext(), "address", firstLineAddress);
        } catch (PreferenceException e) {
            RELog.e(e);
        }
    }

    /**
     * Set current location map values.
     */
    private void setCurrentLocationHashMap(double latitude, double longitude) {
        List<Address> addresses = REUtils.getCurrentAddressList(AddAddressActivity.this, latitude, longitude);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            mAddressHashMap.put("name", (address.getSubThoroughfare() != null ? address.getSubThoroughfare() + "," : "")
                    + address.getFeatureName() + ","
                    + (address.getThoroughfare() != null ? address.getThoroughfare() + "," : ""));
            mAddressHashMap.put("street", address.getSubLocality());
            mAddressHashMap.put("city", address.getLocality());
            mAddressHashMap.put("pin", address.getPostalCode());
            mAddressHashMap.put("state", address.getAdminArea());
        }
    }

    /**
     * Sets the user current address.
     *
     * @param latitude  latitude.
     * @param longitude longitude.
     */
    private void setCurrentAddress(double latitude, double longitude) {
        if (latitude != 0 && longitude != 0)
            mETAddress.setText(REUtils.getCurrentAddressFromLocation(this, latitude, longitude));
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
                    enableGps();
                }
            }
        }
    }

    /**
     * For adding tge Marker on the map
     *
     * @param currentLatLang {@link LatLng}
     */
    private void showMarker(@NonNull LatLng currentLatLang) {
        if (currentLocationMarker == null && googleMap != null) {
            if (currentLatLang != null && currentLatLang.latitude != 0 && currentLatLang.longitude != 0) {
                currentLocationMarker = googleMap.addMarker(new MarkerOptions().
                        icon(BitmapDescriptorFactory.defaultMarker()).position(currentLatLang).draggable(true));
            }
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
            setCurrentLocationHashMap(marker.getPosition().latitude, marker.getPosition().longitude);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (latLng != null && latLng.latitude != 0 && latLng.longitude != 0) {
            if (currentLocationMarker != null) currentLocationMarker.remove();
            currentLocationMarker = null;
            showMarker(latLng);
            setCurrentAddress(latLng.latitude, latLng.longitude);
            setCurrentLocationHashMap(latLng.latitude, latLng.longitude);
        }
    }

}
