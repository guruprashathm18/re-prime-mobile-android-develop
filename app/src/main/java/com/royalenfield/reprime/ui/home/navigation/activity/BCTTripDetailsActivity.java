package com.royalenfield.reprime.ui.home.navigation.activity;

import static com.royalenfield.reprime.utils.REConstants.KEY_TRIPPER_GTM;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bosch.softtec.components.core.models.Distance;
import com.bosch.softtec.components.core.models.breadcrumb.Breadcrumb;
import com.bosch.softtec.components.core.models.breadcrumb.BreadcrumbTrail;
import com.bosch.softtec.components.core.models.breadcrumb.RideSummary;
import com.bosch.softtec.components.nephele.CloudException;
import com.bosch.softtec.components.nephele.extensions.ConversionExtensionsKt;
import com.bosch.softtec.components.theseus.BreadcrumbTrailManager;
import com.bosch.softtec.micro.tenzing.client.model.Trip;
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
import com.royalenfield.extensions.TripExtensionKt;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.navigation.adapter.BCTImageAdapter;
import com.royalenfield.reprime.ui.home.navigation.asynctask.BCTLatLngAsyncTask;
import com.royalenfield.reprime.ui.home.navigation.asynctask.FetchTripAsyncTask;
import com.royalenfield.reprime.ui.home.navigation.asynctask.NavigationAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.navigation.asynctask.UploadTripAsyncTask;
import com.royalenfield.reprime.ui.home.navigation.model.BCTImageModel;
import com.royalenfield.reprime.ui.home.navigation.utils.BCTUtils;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.tripdetail.view.WorkaroundMapFragment;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REGridSpacingItemDecoration;
import com.royalenfield.reprime.utils.REUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import com.royalenfield.reprime.utils.RELog;


/**
 * This class is used to create a ride
 */
public class BCTTripDetailsActivity extends REBaseActivity implements
        TitleBarView.OnNavigationIconClickListener, TitleBarView.OnCheckInIconClickListener, OnMapReadyCallback, NavigationAsyncTaskListeners.FetchTripListener,
        NavigationAsyncTaskListeners.UploadTripListener, NavigationAsyncTaskListeners.LatLngListener,
        View.OnTouchListener{

    private NestedScrollView mScrollView;
    private TextView mTVLocation;
    private WorkaroundMapFragment mNavigationFrame;
    private TextView mBCTDistance, mBCTDuration;
    private TextView mBCTStartTime, mBCTEndTime;
    private TextView tvTripDesc, tvRouteMetadata;
    private BreadcrumbTrail breadcrumbTrail;
    private List<BCTImageModel> bitmapList = new ArrayList<>();
    private RecyclerView bctRecyclerView;
    private BCTImageAdapter bctImageAdapter;
    private View viewShare;
    private String strTripName, strTripDesc, strTripVisibility, strTripId;
    private Trip selectedTrip;
    private GoogleMap gMap = null;
    private List<Breadcrumb> mBreadcrumb;
    BreadcrumbTrailManager mBreadcrumbTrailManager;
    private com.google.android.gms.maps.model.LatLng mSrcLatLng, mDestLatLng;
    //Don't change the value as 1, because private int IMAGE_MODE_UPLOAD = 1; is used in Upload Trip.
    private int IMAGE_MODE_VIEW = 2;
    private String deeplink = "";
    Button editBCT;
    private static  int REQUEST_TRIP_EDIT_INTENT  = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_details);
        initViews();

        Bundle aParams = new Bundle();
        aParams.putString("screenName", "Tripper Navigation Summary");
        REUtils.logGTMEvent(REConstants.ScreenViewManual, aParams);


        getSharedNavigationId();
        REApplication application = REApplication.getInstance();
        mBreadcrumbTrailManager = application.getBreadcrumbTrailManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        strTripId = getIntent().getStringExtra("bct_trip_id");
        strTripName = getIntent().getStringExtra("bct_trip_name");
        strTripDesc = getIntent().getStringExtra("bct_trip_desc");
        strTripVisibility = getIntent().getStringExtra("bct_trip_visibility");

        TitleBarView mTitleBarView = findViewById(R.id.bct_save_header);
        mTitleBarView.bindData(this, R.drawable.icon_close, "" + strTripName, R.drawable.ic_delete);
        mScrollView = findViewById(R.id.scroll_view_nav_bct);
		mNavigationFrame = (WorkaroundMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.nav_bct_map_holder);

		mNavigationFrame.setListener(new WorkaroundMapFragment.OnTouchListener() {
			@Override
			public void onTouch() {
				mScrollView.requestDisallowInterceptTouchEvent(true);
			}
		});
        mTVLocation = findViewById(R.id.tv_location_info);
        mBCTDistance = findViewById(R.id.nav_bct_distance);
        mBCTDuration = findViewById(R.id.nav_bct_duration);
        mBCTStartTime = findViewById(R.id.bct_start_time);
        mBCTEndTime = findViewById(R.id.bct_end_time);
        bctRecyclerView = findViewById(R.id.img_recyclerview);
        tvTripDesc = findViewById(R.id.tv_bct_desc);
        tvRouteMetadata = findViewById(R.id.tv_route_metadata);

        int spacing = Math.round(5.6f * getResources().getDisplayMetrics().density);
        bctRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        bctRecyclerView.addItemDecoration(new REGridSpacingItemDecoration(2, spacing, false));
        bctRecyclerView.setHasFixedSize(true);

        bctImageAdapter = new BCTImageAdapter(this, IMAGE_MODE_VIEW, bitmapList, null);
        bctRecyclerView.setAdapter(bctImageAdapter);

        mScrollView.setOnTouchListener(this);
        editBCT = findViewById(R.id.bct_edit);
        editBCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bctEditDetailsIntent = new Intent(BCTTripDetailsActivity.this, BCTEditTripActivity.class);
                bctEditDetailsIntent.putExtra("bct_trip_id", strTripId);
                bctEditDetailsIntent.putExtra("bct_trip_name", strTripName);
                bctEditDetailsIntent.putExtra("bct_trip_desc", strTripDesc);
                bctEditDetailsIntent.putExtra("bct_trip_visibility", strTripVisibility);
                startActivityForResult(bctEditDetailsIntent, REQUEST_TRIP_EDIT_INTENT);

                Bundle params = new Bundle();
                params.putString("eventCategory", "Tripper");
                params.putString("eventAction", "BCT Edit icon clicked");
                REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
            }
        });
    }

    private void setValues() {
        if (breadcrumbTrail != null) {
            final RideSummary rideSummary = breadcrumbTrail.getRideSummary();
            if (rideSummary != null) {
                Double duration = Double.valueOf(rideSummary.getTotalDuration().toMinutes());
                /*String strDuration = BCTUploadExtensionsKt.calcTrackTime(rideSummary.
                        getTotalDuration().getDurationValue());*/
                String strDuration = REUtils.getDurationInUnits(duration);
                mBCTDuration.setText(strDuration);

                mBCTDistance.setText(BCTUtils.INSTANCE.getDistance(new Distance(rideSummary.
                        getTotalDistance().getDistanceValue(),
                        rideSummary.getTotalDistance().getLengthUnit())));
            }
            if (breadcrumbTrail != null) {
                mBreadcrumb = breadcrumbTrail.getBreadcrumbs();
            }
            mSrcLatLng = new com.google.android.gms.maps.model.LatLng(
                    mBreadcrumb.get(0).getLocation().getLatitude(), mBreadcrumb.get(0).getLocation().getLongitude());
            mDestLatLng = new com.google.android.gms.maps.model.LatLng(mBreadcrumb.get(mBreadcrumb.size() - 1).
                    getLocation().getLatitude(), mBreadcrumb.get(mBreadcrumb.size() - 1).
                    getLocation().getLongitude());
            getStartLocation(mSrcLatLng);
            getEndLocation(mDestLatLng);
            String startTimeString = convertToTripLocale(breadcrumbTrail.getDateStart().getTime());
            mBCTStartTime.setText(startTimeString);
            String arrivalTimeString = convertToTripLocale(breadcrumbTrail.getDateEnd().getTime());
            mBCTEndTime.setText(arrivalTimeString);

            tvTripDesc.setText(strTripDesc);
            if (strTripVisibility.equalsIgnoreCase("Public")) {
                tvRouteMetadata.setText("Public  Route");
            } else {
                tvRouteMetadata.setText("Private  Route");
            }
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
    /**
     * Adding map fragment
     */
    private void addMapFragment() {
      mNavigationFrame.getMapAsync(this);
       }

    public void onBCTShareClicked(View v) {
        viewShare = v;
        Bundle params = new Bundle();
        params.putString("eventCategory", "Tripper");
        params.putString("eventAction", "BCT share icon clicked");
        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
        if (viewShare != null) {
            viewShare.setClickable(false);
        }
        new UploadTripAsyncTask(this, null, REConstants.RE_SHARE,0).execute(strTripId);
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
    public void onMapReady(GoogleMap googleMap) {
        Log.e("test", "onMapReadyCallback called ");
        gMap = googleMap;
        if (gMap != null) {
            gMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    UiSettings mUiSettings = gMap.getUiSettings();
                    //mUiSettings.setCompassEnabled(true);
                    mUiSettings.setRotateGesturesEnabled(true);
                    mUiSettings.setTiltGesturesEnabled(true);
                    mUiSettings.setMapToolbarEnabled(false);
                    mUiSettings.setZoomGesturesEnabled(true);
                    mUiSettings.setMyLocationButtonEnabled(true);
                    mUiSettings.setScrollGesturesEnabled(true);
                    mUiSettings.setZoomControlsEnabled(true);
                    getBctLatLng();
                }
            });
        }

    }

    private void getBctLatLng() {
        if (mBreadcrumb != null && mBreadcrumb.size() > 0 && gMap != null) {
            showLoading();
            new BCTLatLngAsyncTask(this, mBreadcrumb).execute();
        }
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

        // comment this line on production
      /*  if (gMap != null) {
            for (int i = 0; i < latLngList.size(); i++) {
                gMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).
                        position(latLngList.get(i)).draggable(false));
            }
        }*/
    }

    /**
     * For adding the EndLocation Marker on the map
     *
     * @param endLatLang {@link com.bosch.softtec.components.core.models.LatLng}
     */
    public void showEndLocationMarker(@NonNull com.google.android.gms.maps.model.LatLng endLatLang) {
        if (gMap != null)
            gMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).
                    position(endLatLang).draggable(false));
    }

    /**
     * For adding the StartLocation Marker on the map
     */
    private void showStartLocationMarker(@NonNull com.google.android.gms.maps.model.LatLng srcLatLang) {
        gMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(srcLatLang).draggable(false));
    }


    @Override
    public void onBCTLatLngSuccess(List<LatLng> latLngs, LatLngBounds.Builder builder) {
        hideLoading();
        drawRecorderRoute(latLngs, builder);
        //List<String> strImagesid = TripExtensionKt.getImageIds(selectedTrip);
        List<String> strImagesid = TripExtensionKt.getImageIds(selectedTrip);
        if (strImagesid.size() > 0) {
            bitmapList.clear();
            for (String url : strImagesid) {
                BCTImageModel bctImageModel = new BCTImageModel(url, null);
                bitmapList.add(bctImageModel);
            }
            bctImageAdapter.notifyDataSetChanged();
            //showLoading();
            //FetchImagesAsyncTask fetchImagesAsyncTask = new FetchImagesAsyncTask(strImagesid, this);
            //fetchImagesAsyncTask.execute(selectedTrip.getTripId());
        }
       /* if (strImagesid.size() > 0) {
            showLoading();
            FetchImagesAsyncTask fetchImagesAsyncTask = new FetchImagesAsyncTask(strImagesid, this);
            fetchImagesAsyncTask.execute(selectedTrip.getTripId());
        }*/
    }

    @Override
    public void onBCTLatLngFailure() {
        hideLoading();
        Toast.makeText(this, "Could not able to get BCT values ", Toast.LENGTH_SHORT).show();
    }


    private String convertToTripLocale(long dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        return format.format(dateTime);
    }

    private String getAddressofLatlng(LatLng location) {
        List<Address> address = REUtils.getCurrentAddressList(this, location.latitude, location.longitude);
        String strLocAddress = "";
        if (address != null && address.size() > 0) {
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

            //Toast.makeText(this, getResources().getText(R.string.text_noaddress_found), Toast.LENGTH_LONG).show();
        }

        return strLocAddress;
    }

    @Override
    public void onFetchTripSuccess(Trip trip) {
        hideLoading();
        selectedTrip = trip;
        breadcrumbTrail = ConversionExtensionsKt.toBreadcrumbTrail(trip);
        Log.e("test", "breadcrumbtrail = " + breadcrumbTrail);
        setValues();
        addMapFragment();
       // exportBctToGpx();
    }

    @Override
    public void onFetchTripFailure(String error) {
        hideLoading();
        Toast.makeText(this, "Something went wrong.please try again later", Toast.LENGTH_SHORT).show();
    }

    public void onRecordShare(String strTripId) {
        try {
            showLoading();
            //Branchio
            LinkProperties linkProperties = new LinkProperties();
            linkProperties.addControlParameter("$android_url", "https://play.google.com/store/apps/details?id=com.royalenfield.reprime");
            linkProperties.addControlParameter("$always_deeplink", "true");
            ContentMetadata data = new ContentMetadata();

            data.addCustomMetadata(REConstants.SHARE_TYPE, REConstants.SHARE_TYPE_BCT_ROUTE);
            data.addCustomMetadata(REConstants.SHARE_CODE_KEY, strTripId);
            BranchUniversalObject branchUniversalObject = new BranchUniversalObject().setCanonicalIdentifier(REConstants.CANONICAL_IDENTIFIER).setContentMetadata(data).setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
            branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError error) {
                    if (error == null) {
                        deeplink = url;
                        hideLoading();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, deeplink);
                        intent.setType("text/plain");
                        startActivityForResult(Intent.createChooser(intent, "Share BCT route via"), 256);
                    } else {
                        RELog.e(error.toString());
                        runOnUiThread(() -> {
                            hideLoading();
                            Toast.makeText(getApplicationContext(), "Not able to Share !!!", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "http://reprime.royalenfield.com/sharedId?" + strTripId);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "BCT Share via"));*/
    }

    @Override
    public void onUploadTripSuccess(String result, boolean isSave, int totalSize) {
        if (viewShare != null) {
            viewShare.setClickable(true);
        }
        onRecordShare(result);
    }

    @Override
    public void onUploadTripFailure(String error, int totalSize) {
        if (viewShare != null) {
            viewShare.setClickable(true);
        }
        Toast.makeText(this, "Trip Share Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckInClickListener() {
        //TODO refresh callback we have to check with BSOT team then will see how to handle if else condition
        showErrorDialog(this, "Are you sure, You want to delete the Route?");
    }

    private void showErrorDialog(final Context context, String message) {
        Bundle params = new Bundle();
        params.putString("eventCategory", "Tripper");
        params.putString("eventAction", "Delete icon clicked");
        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

        try {
            TextView alert_message, alert_ok,alert_cancel;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            alert_cancel = alertLayout.findViewById(R.id.textView_alert_cancel_button);
            alert_cancel.setVisibility(View.VISIBLE);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            //alert_ok.setOnClickListener(v -> dialog.dismiss());
            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Tripper");
                    params.putString("eventAction", "ok - delete my route");
                    REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

                    dialog.dismiss();
                    new DeleteAsyncTask().execute();
                }
            });
            alert_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Tripper");
                    params.putString("eventAction", "cancel- delete my route");
                    REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    public void onBCTNavigateClicked(View view) {

        Bundle params = new Bundle();
        params.putString("eventCategory", "Tripper");
        params.putString("eventAction", "Navigation from Ride Summary Clicked");
        params.putString("eventLabel", tvRouteMetadata.getText().toString());
        params.putString("Journey_kms",mBCTDistance.getText().toString());
        params.putString("Journey_time",mBCTDuration.getText().toString());
        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
        Intent homeIntent = new Intent();
        homeIntent.putExtra(REConstants.IS_SERVICE_NOTIFICATION, false);
        homeIntent.putExtra(REConstants.IS_NAVIGATION_NOTIFICATION, true);
        homeIntent.putExtra(REConstants.IS_NAVIGATION_DETAILS, true);
        homeIntent.putExtra(REConstants.NAVIGATION_NOTIFICATION, strTripId);
        homeIntent.putExtra("action", "Home");
        setResult(RESULT_OK, homeIntent);
        finish();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mScrollView.requestDisallowInterceptTouchEvent(false);
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TRIP_EDIT_INTENT && resultCode == RESULT_OK) {
            Intent homeIntent = new Intent();
            homeIntent.putExtra("action", "EditBCT");
            setResult(RESULT_OK, homeIntent);
            finish();
        }
    }

    class DeleteAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private CloudException cloudException;

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.e("test", "deleted called ");
            boolean res = false;
            try {
                res = REApplication.getInstance().getCloudManager().deleteTripById(strTripId);
            } catch (CloudException e) {
                cloudException = e;
            }
            Log.e("test", "deleted value = " + res);
            return res;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.e("test", "deleted value on post execute= " + result.booleanValue());
            hideLoading();
            if (result.booleanValue()) {
                Intent homeIntent = new Intent();
                homeIntent.putExtra("action", "Delete");
                setResult(RESULT_OK, homeIntent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Delete failed with " + cloudException.getHttpErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }
    }
    private void exportBctToGpx() {

        Log.e("exportBctToGpx: ", "called >>>");
        File root;
        String folderName = "BCTData";
        if (Build.VERSION.SDK_INT >= 30) {
            // Workaround for Android 11 and above
            root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), folderName);
        } else  {
            root=  new File(Environment.getExternalStorageDirectory(), folderName);
        }
        if (!root.exists()) {
            root.mkdirs();
        }
        File gpxfile = new File(root, "Gpxtest "+ System.currentTimeMillis()+".gpx");
        mBreadcrumbTrailManager.exportToGpx(breadcrumbTrail, gpxfile);
    }
}
