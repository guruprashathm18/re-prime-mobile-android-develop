package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.royalenfield.bluetooth.BleSearchActivity;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.response.remoteconfig.ConfigFeatures;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.MapFragment;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.FEATURE_DISABLED;
import static com.royalenfield.reprime.utils.REConstants.WAYPOINT_PLANRIDE;

/**
 * This class is used to create a ride
 */
public class CreateRideActivity extends REBaseActivity implements View.OnClickListener,
        TitleBarView.OnNavigationIconClickListener, RidesListeners.CurrentLocationListener, MapFragment.OnTouchListener {

    private NestedScrollView mScrollView;
    private EditText etCurrentLoc;
    private EditText etDestination;
    private TextView tvDistanceInfo, mTextConfigMessage;
    private Button btnCreateRide;
    private double mLatitude, mLongitude;
    private double mSourceLat, mSourceLon;
    private ArrayList<WayPointsData> mWayPointList = new ArrayList<>();
    private ImageView ic_close;
    private String distance;
    private MapFragment mMapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);
        initViews();
        mWayPointList.add(0, new WayPointsData(0, 0, null));
    }

    /**
     * Initialising Views
     */
    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.create_ride_header);
        mTitleBarView.bindData(this, R.drawable.ic_back_up, getString(R.string.label_create_ride));
        mTextConfigMessage = findViewById(R.id.text_config_message);
        manageWarningText();
        mScrollView = findViewById(R.id.scroll_home_screen);
        tvDistanceInfo = findViewById(R.id.tv_distance_label);
        btnCreateRide = findViewById(R.id.btn_createride);
        etCurrentLoc = findViewById(R.id.et_current_loc);
        etDestination = findViewById(R.id.et_destination);
        ic_close = findViewById(R.id.imageView_close);
        ic_close.setOnClickListener(this);
        etCurrentLoc.setOnClickListener(this);
        etDestination.setOnClickListener(this);
        btnCreateRide.setOnClickListener(this);

        addMapFragment();
        showButtonVisibility(View.GONE);
    }

    private void manageWarningText() {
       // ConfigFeatures configFeature = REUtils.getConfigFeatures();
       /* if (configFeature != null && configFeature.getCreateRide() != null && configFeature.getCreateRide().
                getReleaseFeatureStatus().equalsIgnoreCase(REConstants.FEATURE_STATUS_DISABLED) && configFeature.getCreateRide().getCategory().equalsIgnoreCase("4")) {
            mTextConfigMessage.setVisibility(View.VISIBLE);
            mTextConfigMessage.setText(configFeature.getCreateRide().getMessage());*/
        if(REUtils.getFeatures().getDefaultFeatures().getIs_rides_enabled().equalsIgnoreCase(REConstants.FEATURE_DISABLED)){
        mTextConfigMessage.setVisibility(View.VISIBLE);
        mTextConfigMessage.setText(REUtils.getRidesKeys().getMessage_ride_disabled());
        }
        else {
            mTextConfigMessage.setVisibility(View.GONE);
        }
    }

    /**
     * Adding map fragment
     */
    private void addMapFragment() {
        FragmentFrameHolder mCreateRideFrame = findViewById(R.id.fl_create_ride_holder);
        mMapFragment = MapFragment.newInstance(this);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(REConstants.WAY_POINTS_LIST, mWayPointList);
        bundle.putString(REConstants.RIDE_WAYPOINT_TYPE, WAYPOINT_PLANRIDE);
        bundle.putBoolean(REConstants.IS_LOCATION_REQUIRED, true);
        bundle.putBoolean(REConstants.IS_NAVIGATION, false);
        mMapFragment.setArguments(bundle);
        mMapFragment.setListener(this);
        mCreateRideFrame.loadFragment(this, mMapFragment, MapFragment.TAG_MAP_FRAGMENT);
    }

    /**
     * Managing create plan views based on source and dest edittext
     */
    private void manageCreatePLanViews() {
        String source = etCurrentLoc.getText().toString();
        String dest = etDestination.getText().toString();
        if (source.length() > 0 && dest.length() > 0) {
            showButtonVisibility(View.VISIBLE);
        } else {
            showButtonVisibility(View.GONE);
        }
    }

    /**
     * Making views visible
     */
    private void showButtonVisibility(int visibility) {
        tvDistanceInfo.setVisibility(visibility);
        btnCreateRide.setVisibility(visibility);
    }


    @Override
    public void onClick(View v) {
        try {
            String source = etCurrentLoc.getText().toString();
            String dest = etDestination.getText().toString();
            switch (v.getId()) {
                case R.id.et_current_loc:
                    Intent intent = new Intent(getApplicationContext(), CreateRideSearchActivity.class);
                    intent.putExtra("dest", dest);
                    intent.putExtra("type", true);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                    break;
                case R.id.et_destination:
                    Intent mSearchIntent = new Intent(getApplicationContext(), CreateRideSearchActivity.class);
                    mSearchIntent.putExtra("type", false);
                    mSearchIntent.putExtra("source", source);
                    startActivityForResult(mSearchIntent, 2);
                    overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                    break;
                case R.id.btn_createride:
                    Intent createRideIntent = new Intent(getApplicationContext(), PlanRideActivity.class);
                    createRideIntent.putParcelableArrayListExtra(REConstants.WAY_POINTS_LIST, mWayPointList);
                    createRideIntent.putExtra("distance", distance);
                    finish();
                    startActivity(createRideIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                    break;
                case R.id.ble_search_create_ride:
                    startActivity(new Intent(this, BleSearchActivity.class));
                    overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                    break;
                case R.id.imageView_close:
                    etDestination.getText().clear();
                    etDestination.setTypeface(ResourcesCompat.getFont(CreateRideActivity.this, R.font.montserrat_light));
                    etDestination.setHintTextColor(getResources().getColor(R.color.black_two));
                    etDestination.setHint(R.string.label_destination);
                    manageCreatePLanViews();
                    mMapFragment.calculateStartDestinationAndWayPointLatLang(null, false);
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REConstants.REQUEST_CHECK_SETTINGS) {
            Fragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.fl_create_ride_holder);
            if (mapFragment != null) {
                //This calls the onActivityResult in MapFragment
                mapFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        String mPLace = null;
        if (requestCode == REConstants.REQUEST_CHECK_SETTINGS) {
            Fragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.fl_create_ride_holder);
            if (mapFragment != null) {
                //This calls the onActivityResult in MapFragment
                mapFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        if (data != null) {
            mPLace = data.getStringExtra("place");
            mLatitude = data.getDoubleExtra("latitude", 0);
            mLongitude = data.getDoubleExtra("longitude", 0);
        }
        if (resultCode == 1) {
            setStartLocationData(mPLace);
        } else if (resultCode == 2) {
            setDestinationLocationData(mPLace);
            ic_close.setVisibility(View.VISIBLE);
        }
        // Redraws the route
        if (mPLace != null && !mPLace.isEmpty()) {
            mMapFragment.calculateStartDestinationAndWayPointLatLang(mWayPointList, false);
        }
        manageCreatePLanViews();
    }

    /**
     * Handles the Destination location data.
     *
     * @param destinationPlaceName destination name.
     */
    private void setDestinationLocationData(String destinationPlaceName) {
        etDestination.setText(destinationPlaceName);
        etDestination.setTextColor(getResources().getColor(R.color.black));
        etDestination.setTypeface(ResourcesCompat.getFont(CreateRideActivity.this, R.font.montserrat_bold));
        double mDestLat = mLatitude;
        double mDestLon = mLongitude;
        if (mWayPointList.size() > 1) {
            mWayPointList.set(1, new WayPointsData(mDestLat, mDestLon, etDestination.getText().toString()));
        } else {
            mWayPointList.add(1, new WayPointsData(mDestLat, mDestLon, etDestination.getText().toString()));
        }
    }

    /**
     * Handles the Start location data for the ride.
     *
     * @param startPlaceName starting place name.
     */
    private void setStartLocationData(String startPlaceName) {
        etCurrentLoc.setText(startPlaceName);
        etCurrentLoc.setTextColor(getResources().getColor(R.color.black));
        etCurrentLoc.setTypeface(ResourcesCompat.getFont(CreateRideActivity.this, R.font.montserrat_regular));
        mSourceLat = mLatitude;
        mSourceLon = mLongitude;
        if (mWayPointList.size() == 1 || mWayPointList.size() == 2) {
            mWayPointList.set(0, new WayPointsData(mSourceLat, mSourceLon, etCurrentLoc.getText().toString()));
        } else {
            mWayPointList.add(0, new WayPointsData(mSourceLat, mSourceLon, etCurrentLoc.getText().toString()));
        }
    }


    @Override
    public void updateCurrentLocation(LatLng latLng) {
        if (mSourceLat == 0 && mSourceLon == 0) {
            mSourceLat = latLng.latitude;
            mSourceLon = latLng.longitude;
            List<Address> address = REUtils.getCurrentAddressList(this, latLng.latitude, latLng.longitude);
            String startAddress = "";
            if (address != null && address.size() > 0) {
                startAddress = ((address.get(0).getSubLocality() == null) || (address.get(0).getSubLocality() == "") ?
                        address.get(0).getLocality() : address.get(0).getSubLocality());
                etCurrentLoc.setText(startAddress);
                etCurrentLoc.setCursorVisible(false);
                etCurrentLoc.setTypeface(ResourcesCompat.getFont(CreateRideActivity.this, R.font.montserrat_regular));
            } else {
                Toast.makeText(this, getResources().getText(R.string.text_noaddress_found), Toast.LENGTH_LONG).show();
                etCurrentLoc.setTypeface(ResourcesCompat.getFont(CreateRideActivity.this, R.font.montserrat_light));
                etCurrentLoc.setHintTextColor(getResources().getColor(R.color.black_two));
                etCurrentLoc.setHint(R.string.label_source);
            }

            mWayPointList.set(0, new WayPointsData(mSourceLat, mSourceLon, startAddress));
        }
    }

    @Override
    public void setDistance(Double iDistanceKM) {
        distance = String.valueOf(iDistanceKM);
        if (iDistanceKM > 50) {
            tvDistanceInfo.setText(String.format(getResources().getString(R.string.approx_distance_long_ride),
                    String.valueOf(iDistanceKM)));
        } else {
            tvDistanceInfo.setText(String.format(getResources().getString(R.string.approx_distance),
                    String.valueOf(iDistanceKM)));
        }
    }


    @Override
    public void onTouch() {
        mScrollView.requestDisallowInterceptTouchEvent(true);
    }
}
