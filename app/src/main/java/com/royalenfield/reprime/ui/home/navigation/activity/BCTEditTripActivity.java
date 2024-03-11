package com.royalenfield.reprime.ui.home.navigation.activity;

import static com.bosch.softtec.micro.tenzing.client.model.JsonPatch.SERIALIZED_NAME_OP;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;

import com.bosch.softtec.components.core.models.Distance;
import com.bosch.softtec.components.core.models.breadcrumb.Breadcrumb;
import com.bosch.softtec.components.core.models.breadcrumb.BreadcrumbTrail;
import com.bosch.softtec.components.nephele.extensions.ConversionExtensionsKt;
import com.bosch.softtec.micro.tenzing.client.model.JsonPatch;
import com.bosch.softtec.micro.tenzing.client.model.Trip;
import com.bosch.softtec.micro.tenzing.client.model.TripBodyWithImageUrl;
import com.bosch.softtec.micro.tenzing.client.model.VisibilityType;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.navigation.asynctask.BCTLatLngAsyncTask;
import com.royalenfield.reprime.ui.home.navigation.asynctask.EditTripAsyncTask;
import com.royalenfield.reprime.ui.home.navigation.asynctask.FetchTripAsyncTask;
import com.royalenfield.reprime.ui.home.navigation.asynctask.NavigationAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.navigation.utils.BCTUtils;
import com.royalenfield.reprime.ui.splash.activity.RESuccessSplashActivity;
import com.royalenfield.reprime.ui.tripdetail.view.WorkaroundMapFragment;
import com.royalenfield.reprime.utils.REUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * This class is used to create a ride
 */
public class BCTEditTripActivity extends REBaseActivity implements View.OnClickListener,
        TitleBarView.OnNavigationIconClickListener,NavigationAsyncTaskListeners.FetchTripListener,
        NavigationAsyncTaskListeners.LatLngListener, OnMapReadyCallback,NavigationAsyncTaskListeners.UpdateTripListener {

    public static final String TAG_SAVE_ACTIVITY = BCTEditTripActivity.class.getName();

    private NestedScrollView mScrollView;
    private String strAction;
    private WorkaroundMapFragment mNavigationFrame;
    private TextView mTVLocation;
    private TextView mBCTDistance, mBCTDuration;
    private TextView mBCTStartTime, mBCTEndTime;
    private Switch mSwitch;
    private EditText mSaveBCTTitle, mSaveBCTDESC;
    private BreadcrumbTrail breadcrumbTrail;
    private List<Breadcrumb> mBreadcrumb;
    private GoogleMap gMap = null;
    private String strDuration, strDistance, strTripId, strTripName, strTripDesc, strTripVisibility;
    private LatLng mSrcLatLng, mDestLatLng;
    //private static Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_bct_form);
        initViews();
        getSharedNavigationId();
    }

    private void getIntentData() {
        strTripId = getIntent().getStringExtra("bct_trip_id");
        strTripName = getIntent().getStringExtra("bct_trip_name");
        strTripDesc = getIntent().getStringExtra("bct_trip_desc");
        strTripVisibility = getIntent().getStringExtra("bct_trip_visibility");
    }

    private void getSharedNavigationId() {
        if (strTripId != null) {
            showLoading();
            new FetchTripAsyncTask("details", this).execute(strTripId);
        }
    }

    /**
     * Initialising Views
     */
    private void initViews() {
        getIntentData();
        TitleBarView mTitleBarView = findViewById(R.id.bct_edit_bct_header);
        mTitleBarView.bindData(this, R.drawable.icon_close, getResources().getString(R.string.bct_edit_text) + " BCT");
        mScrollView = findViewById(R.id.scroll_view_nav_bct);
        mNavigationFrame = (WorkaroundMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.nav_edit_bct_map_holder);

	mNavigationFrame.setListener(new WorkaroundMapFragment.OnTouchListener() {
			@Override
			public void onTouch() {
				mScrollView.requestDisallowInterceptTouchEvent(true);
			}
		});

		mTVLocation = findViewById(R.id.tv_location_info_editBct);
        mBCTDistance = findViewById(R.id.nav_bct_distance);
        mBCTDuration = findViewById(R.id.nav_bct_duration);
        mBCTStartTime = findViewById(R.id.bct_start_time);
        mBCTEndTime = findViewById(R.id.bct_end_time);
        Button mBCTSubmit = findViewById(R.id.bct_submit);
        mSwitch = findViewById(R.id.visibility_switch);
        mSaveBCTTitle = findViewById(R.id.save_title_view);
        mSaveBCTTitle.setText(strTripName);
        mSaveBCTDESC = findViewById(R.id.save_desc_view);
        mSaveBCTDESC.setText(strTripDesc);
        if (strTripVisibility.equalsIgnoreCase("Public")) {
            mSwitch.setChecked(true);
        } else {
            mSwitch.setChecked(false);
        }
        mBCTSubmit.setOnClickListener(this);

        mScrollView.setOnTouchListener((view, motionEvent) -> {
            mScrollView.requestDisallowInterceptTouchEvent(false);
            return false;
        });
    }

    /**
     * Adding map fragment
     */
    private void addMapFragment() {
//        NavigationBCTMapFragment navigationBCTMapFragment = NavigationBCTMapFragment.newInstance();
//        navigationBCTMapFragment.setOnTouchListener(this);

		mNavigationFrame.getMapAsync(this);
      //  mNavigationFrame.loadFragment(this, navigationBCTMapFragment, NavigationBCTMapFragment.TAG_MAP_FRAGMENT);
    }

    /**
     * Set the values
     */
    private void setValues() {
        try {
            if (breadcrumbTrail != null) {
                Double duration = Double.valueOf(breadcrumbTrail.getRideSummary().getTotalDuration().toMinutes());
                strDuration = REUtils.getDurationInUnits(duration);
                mBCTDuration.setText(strDuration);
                strDistance = BCTUtils.INSTANCE.getDistance(new Distance(breadcrumbTrail.getRideSummary().
                        getTotalDistance().getDistanceValue(),
                        breadcrumbTrail.getRideSummary().getTotalDistance().getLengthUnit()));
                mBCTDistance.setText(strDistance);
                if (breadcrumbTrail != null) {
                    mBreadcrumb = breadcrumbTrail.getBreadcrumbs();
                }
                mSrcLatLng = new LatLng(
                        mBreadcrumb.get(0).getLocation().getLatitude(), mBreadcrumb.get(0).getLocation().getLongitude());
                mDestLatLng = new LatLng(mBreadcrumb.get(mBreadcrumb.size() - 1).
                        getLocation().getLatitude(), mBreadcrumb.get(mBreadcrumb.size() - 1).
                        getLocation().getLongitude());
                getStartLocation(mSrcLatLng);
                String startTimeString = convertToTripLocale(breadcrumbTrail.getDateStart().getTime());
                mBCTStartTime.setText(startTimeString);
                String arrivalTimeString = convertToTripLocale(breadcrumbTrail.getDateEnd().getTime());
                mBCTEndTime.setText(arrivalTimeString);
                getEndLocation(mDestLatLng);
            }
        } catch (Exception e) {
            Log.e(TAG_SAVE_ACTIVITY, "Exception raised");
        }
    }

    private List<JsonPatch> generateRequestBody(){
        String mTripName,mTripDescription,isPublic ;
        mTripName  =  mSaveBCTTitle.getText().toString().trim();
        mTripDescription  =  mSaveBCTDESC.getText().toString().trim();
        if (mSwitch.isChecked()) {
            isPublic = "PUBLIC";
        } else {
            isPublic = "PRIVATE";
        }
        JsonPatch.OpEnum value  = JsonPatch.OpEnum.REPLACE;
        JsonPatch namePatch = new JsonPatch();
        namePatch.setOp(value);
        namePatch.path("/name");
        namePatch.value(mTripName);
        JsonPatch descriptionPatch = new JsonPatch();
        descriptionPatch.setOp(value);
        descriptionPatch.path("/description");
        descriptionPatch.value(mTripDescription);
        JsonPatch visibilityPatch = new JsonPatch();
        visibilityPatch.setOp(value);
        visibilityPatch.path("/visibility");
        visibilityPatch.value(isPublic);
        List<JsonPatch> patch  = new ArrayList<>();
        patch.add(namePatch);
        patch.add(descriptionPatch);
        patch.add(visibilityPatch);
        return patch;
    }

    private String convertToTripLocale(long dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        return format.format(dateTime);
    }

    public void drawRecorderRoute(List<LatLng> latLngList,
                                  LatLngBounds.Builder builder) {
        if (latLngList != null && latLngList.size() > 0) {
            drawPolyLine(latLngList);
            showStartLocationMarker(mSrcLatLng);
            showEndLocationMarker(mDestLatLng);
            animateCamera(builder);
        }
    }

    private void getStartLocation(LatLng srcLatLng) {
        String startLoc = getAddressofLatlng(srcLatLng);
        mTVLocation.setText(startLoc);
    }

    private void getEndLocation(LatLng destLatLng) {
        String endLoc = getAddressofLatlng(destLatLng);
        if (endLoc != null && endLoc.length() > 0) {
            if (mTVLocation.getText() != null && mTVLocation.getText().toString().length() > 0) {
                mTVLocation.setText(mTVLocation.getText().toString() + " to " + endLoc);
            } else {
                mTVLocation.setText(endLoc);
            }
        }
    }

    private String getAddressofLatlng(LatLng location) {
        List<Address> address = REUtils.getCurrentAddressList(this, location.latitude, location.longitude);
        String strLocAddress = "";
        if (address != null && address.size() > 0) {
            strLocAddress = ((address.get(0).getSubLocality() == null) || (address.get(0).getSubLocality().equalsIgnoreCase("")) ?
                    address.get(0).getLocality() : address.get(0).getSubLocality());
            //Log.e("AddressVal","SL "+address.get(0).getSubLocality()+" Loc "+address.get(0).getLocality()+" AL "+address.get(0).getAddressLine(0)+" Fname  "+address.get(0).getFeatureName()+" PO "+address.get(0).getPostalCode());
           /* strLocAddress = ((address.get(0).getSubLocality() == null) || (address.get(0).getSubLocality().equalsIgnoreCase("")) ?
                    address.get(0).getLocality() : address.get(0).getSubLocality());*/
            if (address.get(0).getLocality()!=null)
                strLocAddress = address.get(0).getLocality();
            else if(address.get(0).getLocality() == null && address.get(0).getSubLocality()!=null){
                strLocAddress = address.get(0).getSubLocality();
            } else if(address.get(0).getLocality() == null && address.get(0).getSubLocality() == null){
                strLocAddress = address.get(0).getAddressLine(0);
            }
        } else {
            Toast.makeText(this, getResources().getText(R.string.text_noaddress_found), Toast.LENGTH_LONG).show();

            //Toast.makeText(this, getResources().getText(R.string.text_noaddress_found), Toast.LENGTH_LONG).show();
        }

        return strLocAddress;
    }


    private void animateCamera(LatLngBounds.Builder builder) {
        int padding = 90;
        /**create the camera with bounds and padding to set into map*/
        if (builder != null) {
            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), padding);
            gMap.animateCamera(cu);
        }

    }

    /**
     * Draws PolyLine on the map
     *
     * @param latLngList : List<com.google.android.gms.maps.model.LatLng>
     */
    private void drawPolyLine(List<LatLng> latLngList) {
        PolylineOptions polylineOptions = new PolylineOptions();
        int routeTraceColor = 0xFF0098D9;
        float zIndex = 1.0F;
        RoundCap roundCap = new RoundCap();
        polylineOptions.color(routeTraceColor).zIndex(zIndex).startCap(roundCap).
                endCap(roundCap).width(18.0F).geodesic(true).clickable(true);
        polylineOptions.addAll(latLngList);
        if (gMap != null) {
            gMap.clear();
            gMap.addPolyline(polylineOptions);
        }
    }

    /**
     * For adding the EndLocation Marker on the map
     *
     * @param endLatLang {@link com.bosch.softtec.components.core.models.LatLng}
     */
    public void showEndLocationMarker(@NonNull LatLng endLatLang) {
        if (gMap != null)
            gMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).
                    position(endLatLang).draggable(false));
    }

    /**
     * For adding the StartLocation Marker on the map
     */
    private void showStartLocationMarker(@NonNull LatLng srcLatLang) {
        gMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(srcLatLang).draggable(false));
    }

    //TODO Later this dialog needs to implements as per MVP pattern......temporary
    private void showErrorDialog(final Context context, String message, String title, boolean isExit) {
        try {
            TextView alert_title, alert_message, alert_ok;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            alert_title = alertLayout.findViewById(R.id.textView_alert_title);
            if (title != null && title.length() > 0) {
                alert_title.setText(title);
            }
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(v -> {
                dialog.dismiss();
                if (isExit) {
                   // onRecordSavedSuccessfully();
                    //showNavigationSummary("NoSave");
                }
            });
        } catch (Exception e) {
            Timber.e(e);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bct_submit) {
            if (mSaveBCTTitle.getText() != null && mSaveBCTTitle.getText().toString().trim().length() > 0 &&
                    mSaveBCTDESC.getText() != null && mSaveBCTDESC.getText().toString().trim().length() > 0 && !mSaveBCTDESC.getText().toString().isEmpty()) {
                if (mSaveBCTTitle.getText().toString().trim().length() > 4 && !mSaveBCTTitle.getText().toString().isEmpty() && mSaveBCTDESC.getText().toString().trim().length() > 3) {
                    //new UploadTripAsyncTask(this, tripBody, action).execute();
                    List<JsonPatch> mjsonPatches = generateRequestBody();
                    if (mjsonPatches != null) {
                        new EditTripAsyncTask(this, strTripId, mjsonPatches).execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Title and Description should be more than four characters", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Enter Title and description", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNavigationIconClick() {
        finish();
    }

    @Override
    public void onBackPressed() {
       finish();
    }

    @Override
    public void onBCTLatLngSuccess(List<LatLng> latLngs, LatLngBounds.Builder builder) {
        hideLoading();
        drawRecorderRoute(latLngs, builder);
    }

    @Override
    public void onBCTLatLngFailure() {
        hideLoading();
        Toast.makeText(this, "Could not able to get BCT values ", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        UiSettings mUiSettings = this.gMap.getUiSettings();
        //mUiSettings.setCompassEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
        //mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setMapToolbarEnabled(false);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomControlsEnabled(true);
        if (mBreadcrumb != null && mBreadcrumb.size() > 0 && gMap != null) {
            showLoading();
            new BCTLatLngAsyncTask(this, mBreadcrumb).execute();
        }
    }


    @Override
    public void onFetchTripSuccess(Trip trip) {
        hideLoading();
        breadcrumbTrail = ConversionExtensionsKt.toBreadcrumbTrail(trip);
        setValues();
        addMapFragment();
    }

    @Override
    public void onFetchTripFailure(String error) {
        hideLoading();
        Toast.makeText(this, "Cloud Exception  : " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateTripSuccess(Boolean value) {
        Intent intentsuccess = RESuccessSplashActivity.getIntent(getApplicationContext(), null,
                "EDIT BCT");
        startActivity(intentsuccess);
        Intent homeIntent = new Intent();
        setResult(RESULT_OK, homeIntent);
        finish();
    }

    @Override
    public void onUpdateTripFailure() {
        Toast.makeText(this, "Something went wrong.please try again later " , Toast.LENGTH_SHORT).show();
    }
}