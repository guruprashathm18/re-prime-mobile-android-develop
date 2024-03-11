package com.royalenfield.reprime.ui.home.navigation.activity;

import static android.view.View.GONE;
import static com.royalenfield.reprime.utils.REConstants.BCT_DISTANCE;
import static com.royalenfield.reprime.utils.REConstants.BCT_DURATION;
import static com.royalenfield.reprime.utils.REConstants.BCT_SPEED;
import static com.royalenfield.reprime.utils.REConstants.KEY_TRIPPER_GTM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bosch.softtec.components.core.models.Distance;
import com.bosch.softtec.components.core.models.Duration;
import com.bosch.softtec.components.core.models.Speed;
import com.bosch.softtec.components.core.models.breadcrumb.Breadcrumb;
import com.bosch.softtec.components.core.models.breadcrumb.BreadcrumbTrail;
import com.bosch.softtec.components.core.models.breadcrumb.RideSummary;
import com.bosch.softtec.components.nephele.CloudException;
import com.bosch.softtec.components.nephele.extensions.ConversionExtensionsKt;
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
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.homescreen.fragments.RENavigationMapFragment;
import com.royalenfield.reprime.ui.home.navigation.adapter.BCTImageAdapter;
import com.royalenfield.reprime.ui.home.navigation.asynctask.BCTLatLngAsyncTask;
import com.royalenfield.reprime.ui.home.navigation.asynctask.NavigationAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.navigation.asynctask.UploadTripAsyncTask;
import com.royalenfield.reprime.ui.home.navigation.interactor.RENavigationInteractor;
import com.royalenfield.reprime.ui.home.navigation.interactor.UploadImageInteractor;
import com.royalenfield.reprime.ui.home.navigation.listener.SaveTripSummaryDetailsApiRequest;
import com.royalenfield.reprime.ui.home.navigation.listener.SaveTripSummaryEndPoint;
import com.royalenfield.reprime.ui.home.navigation.listener.SaveTripSummaryStartPoint;
import com.royalenfield.reprime.ui.home.navigation.model.BCTImageModel;
import com.royalenfield.reprime.ui.home.navigation.utils.BCTUtils;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.tripdetail.view.WorkaroundMapFragment;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class is used to create a ride
 */
public class BCTSaveInfoActivity extends REBaseActivity implements View.OnClickListener,
        TitleBarView.OnNavigationIconClickListener, BCTImageAdapter.BCTImgDeleteListener,
        NavigationAsyncTaskListeners.LatLngListener, OnMapReadyCallback,
        NavigationAsyncTaskListeners.UploadTripListener, RidesListeners.NavigationImageUploadListener {

    public static final String TAG_SAVE_ACTIVITY = BCTSaveInfoActivity.class.getName();

    private NestedScrollView mScrollView;
    private String strAction;
    private WorkaroundMapFragment mNavigationFrame;
    //private TextView mTVLocationStart, mTVLocationEnd;
    private TextView mTVLocation;
    private TextView mBCTDistance, mBCTDuration;
    private TextView mBCTStartTime, mBCTEndTime;
    private Switch mSwitch;
    private EditText mSaveBCTTitle, mSaveBCTDESC;
    private BreadcrumbTrail breadcrumbTrail;
    private List<BCTImageModel> bitmapList = new ArrayList<>();
    private List<String> uploadImagePaths = new ArrayList<>();
    private ConstraintLayout noImgAddLayout, imgLayout;
    private TextView tvAddImage;
    private BCTImageAdapter bctImageAdapter;
    private List<Breadcrumb> mBreadcrumb;
    private GoogleMap gMap = null;
    private String strDuration, strDistance;
    private static int totalUploadSize = 0;
    private com.google.android.gms.maps.model.LatLng mSrcLatLng, mDestLatLng;
    private ArrayList<BreadcrumbTrail> multipleBct = new ArrayList<>();
    private List<String> gImageUrl = new ArrayList<>();
    private String firstTripId;
    private int limitSize = 9500;
    private RENavigationMapFragment mMapFragment;
    private SharedPreferences sharedpreferences;
    private String tripSummaryTripperSerialNo;
    private String startTimeString, arrivalTimeString;
    private String tripStartTime, tripEndTime;
    private String endLoc="";
    private String startLoc="";


    //private static Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bct_form);

        if (getIntentData() != null) {
            tripStartTime = getIntent().getStringExtra("startTime");
            tripEndTime = getIntent().getStringExtra("endTime");
        }

        //geocoder = new Geocoder(this, Locale.getDefault());
        initViews();
        setValues();
    }

    private String getIntentData() {
        // breadcrumbTrail = getIntent().getParcelableExtra("bct_data");
        return getIntent().getStringExtra("action");
    }

    /**
     * Initialising Views
     */
    private void initViews() {
        strAction = getIntentData();
        sharedpreferences = this.getSharedPreferences("RE_APP", Context.MODE_PRIVATE);
        breadcrumbTrail = RENavigationMapFragment.getBreadcrumbTrail();
        mMapFragment = RENavigationMapFragment.newInstance();
        TitleBarView mTitleBarView = findViewById(R.id.bct_save_header);
        mTitleBarView.bindData(this, R.drawable.icon_close, strAction + " BCT");
        mScrollView = findViewById(R.id.scroll_view_nav_bct);
      mNavigationFrame = (WorkaroundMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.nav_bct_map_holder);

		mNavigationFrame.setListener(new WorkaroundMapFragment.OnTouchListener() {
			@Override
			public void onTouch() {
				mScrollView.requestDisallowInterceptTouchEvent(true);
			}
		});
        // mTVLocationStart = findViewById(R.id.tv_location_start_info);
        // mTVLocationEnd = findViewById(R.id.tv_location_end_info);
        mTVLocation = findViewById(R.id.tv_location_info_saveBct);
        mBCTDistance = findViewById(R.id.nav_bct_distance);
        mBCTDuration = findViewById(R.id.nav_bct_duration);
        mBCTStartTime = findViewById(R.id.bct_start_time);
        mBCTEndTime = findViewById(R.id.bct_end_time);
        Button mBCTSubmit = findViewById(R.id.bct_submit);
        mSwitch = findViewById(R.id.visibility_switch);
        mSaveBCTTitle = findViewById(R.id.save_title_view);
        mSaveBCTDESC = findViewById(R.id.save_desc_view);
        noImgAddLayout = findViewById(R.id.no_img_layout);
        imgLayout = findViewById(R.id.img_layout);
        RecyclerView bctRecyclerView = findViewById(R.id.img_recyclerview);
        RecyclerView.LayoutManager mUserCreatedRidesLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        bctRecyclerView.setHasFixedSize(true);
        bctRecyclerView.setLayoutManager(mUserCreatedRidesLayoutManager);
        tvAddImage = findViewById(R.id.tv_add_images);
        mBCTSubmit.setOnClickListener(this);
        noImgAddLayout.setOnClickListener(this);
        tvAddImage.setOnClickListener(this);
        int IMAGE_MODE_UPLOAD = 1;
        bctImageAdapter = new BCTImageAdapter(this, IMAGE_MODE_UPLOAD, bitmapList, this);
        bctRecyclerView.setAdapter(bctImageAdapter);
        manageBCTImageView();

        mScrollView.setOnTouchListener((view, motionEvent) -> {
            mScrollView.requestDisallowInterceptTouchEvent(false);
            return false;
        });

        addMapFragment();
    }

    /**
     * Adding map fragment
     */
    private void addMapFragment() {
        mNavigationFrame.getMapAsync(this);
       }

    /**
     * Manages UI visiblity based on bitmaps
     */
    private void manageBCTImageView() {
        if (bitmapList.size() == 0) {
            noImgAddLayout.setVisibility(View.VISIBLE);
            imgLayout.setVisibility(GONE);
        } else if (bitmapList.size() >= 1 && bitmapList.size() < 5) {
            noImgAddLayout.setVisibility(GONE);
            imgLayout.setVisibility(View.VISIBLE);
            tvAddImage.setVisibility(View.VISIBLE);
        } else if (bitmapList.size() >= 5) {
            tvAddImage.setVisibility(GONE);
        } else {
            tvAddImage.setVisibility(GONE);
        }
    }

    private String getCurrentTripperSerialNo() {
        String value = "";
        if (sharedpreferences != null) {
            value = sharedpreferences.getString("currentTripperUniqueId", "");
        }
        return value;
    }


    public void saveSummaryDetails(BreadcrumbTrail bct) {

        String dist = (bct.getRideSummary().getTotalDistance() != null) ? String.valueOf(bct.getRideSummary().getTotalDistance().toMeters()) : "0";
        String dur = bct.getRideSummary().getTotalDuration() != null ? String.valueOf(bct.getRideSummary().getTotalDuration().toMinutes()) : "0";
        String speed = bct.getRideSummary().getAverageSpeed() != null ? String.valueOf(bct.getRideSummary().getAverageSpeed().toKilometersPerHour()) : "0";

        tripSummaryTripperSerialNo = getCurrentTripperSerialNo();

        SaveTripSummaryStartPoint startpoint = new SaveTripSummaryStartPoint(mSrcLatLng.latitude, mSrcLatLng.longitude);
        SaveTripSummaryEndPoint endpoint = new SaveTripSummaryEndPoint(mDestLatLng.latitude, mDestLatLng.longitude);

        SaveTripSummaryDetailsApiRequest saveTripSummaryDetailsApiRequest = new SaveTripSummaryDetailsApiRequest(REUtils.getGuid(), tripSummaryTripperSerialNo, startpoint, endpoint, "Tripper", "Android", speed, dur, dist, tripStartTime, tripEndTime);
        Log.e("Valll1", "" + saveTripSummaryDetailsApiRequest.toString());
        RENavigationInteractor reNavigationInteractor = new RENavigationInteractor();
        reNavigationInteractor.saveNavigationSummaryDetails(saveTripSummaryDetailsApiRequest);
        Log.e("Valll", "1 " + dist + " 2 " + dur + " 3 " + speed);
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
                mSrcLatLng = new com.google.android.gms.maps.model.LatLng(
                        mBreadcrumb.get(0).getLocation().getLatitude(), mBreadcrumb.get(0).getLocation().getLongitude());
                mDestLatLng = new com.google.android.gms.maps.model.LatLng(mBreadcrumb.get(mBreadcrumb.size() - 1).
                        getLocation().getLatitude(), mBreadcrumb.get(mBreadcrumb.size() - 1).
                        getLocation().getLongitude());
                getStartLocation(mSrcLatLng);
                startTimeString = convertToTripLocale(breadcrumbTrail.getDateStart().getTime());
                mBCTStartTime.setText(startTimeString);
                arrivalTimeString = convertToTripLocale(breadcrumbTrail.getDateEnd().getTime());
                mBCTEndTime.setText(arrivalTimeString);
                getEndLocation(mDestLatLng);
            }
        } catch (Exception e) {
            Log.e(TAG_SAVE_ACTIVITY, "Exception raised");
        }
    }

    /**
     * Creates the trip body and uploads to cloud
     */

    private void performSaveTrip(List<String> imageUrls) {

        if (canSplitBct()) {
            multipleBct = splitBreadcrumbTrail();
            gImageUrl = imageUrls;
            // loading dialogue needed
            saveBCTsSync(imageUrls, 0);
        } else {
            callApi(imageUrls, breadcrumbTrail, -1);
        }
    }

    private void saveBCTsSync(List<String> imageUrls, int i) {

        BreadcrumbTrail breadcrumbTrailBody = multipleBct.get(i);
        callApi(imageUrls, breadcrumbTrailBody, i);
    }


    private ArrayList<BreadcrumbTrail> splitBreadcrumbTrail() {
        try {
            if (breadcrumbTrail != null && breadcrumbTrail.getBreadcrumbs() != null) {
                Log.e("BCT", "last bct :" + breadcrumbTrail.getBreadcrumbs().size());

                ArrayList<ArrayList<Breadcrumb>> result = new ArrayList<>();
                ArrayList<Breadcrumb> temp = new ArrayList<>();
                for (int i = 0; i < breadcrumbTrail.getBreadcrumbs().size(); i++) {
                    Breadcrumb breadcrumb = breadcrumbTrail.getBreadcrumbs().get(i);
                    temp.add(breadcrumb);

                    if (temp.size() % limitSize == 0) {
                        result.add(temp);
                        temp = new ArrayList<>();
                    }
                    // temp.clear();
                }
                if (temp.size() > 3) {
                    result.add(temp);
                }

                Log.e("temp   file size>>>>: ", result.size() + "");
                ArrayList<BreadcrumbTrail> breadcrumbTrailsNew = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    BreadcrumbTrail breadcrumbTrail = new BreadcrumbTrail(result.get(i));
                    breadcrumbTrailsNew.add(breadcrumbTrail);
                }
                return breadcrumbTrailsNew;

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void callApi(List<String> imageUrls, BreadcrumbTrail breadcrumbs, int index) {
        try {

            Log.e("callApi: Index ", index + "");

            TripBodyWithImageUrl tripBody = ConversionExtensionsKt.toTripBodyWithImageUrl(breadcrumbs);
            if (index == -1) {
                tripBody.name(mSaveBCTTitle.getText().toString().trim());
            } else if (index == 0) {
                tripBody.name(mSaveBCTTitle.getText().toString().trim());
            } else {
                tripBody.name(mSaveBCTTitle.getText().toString().trim() + " " + index);
            }
            tripBody.description(mSaveBCTDESC.getText().toString().trim());
            if (imageUrls != null && imageUrls.size() > 0) {
                tripBody.setImagesUrls(imageUrls);
            }
            if (mSwitch.isChecked()) {
                tripBody.visibility(VisibilityType.PUBLIC);
            } else {
                tripBody.visibility(VisibilityType.PRIVATE);
            }
            int action = 0;
            if (strAction.equalsIgnoreCase("Save")) {
                action = REConstants.SAVE;
            } else if (strAction.equalsIgnoreCase("Share")) {
                action = REConstants.SHARE;
            }
            new UploadTripAsyncTask(this, tripBody, action, index).execute();

        } catch (CloudException e) {
            e.printStackTrace();
            Log.e("test", "errror = " + e.getHttpErrorMessage());
            REUtils.showErrorDialog(getApplicationContext(), e.getHttpErrorMessage());
            saveNextPart(index + 1);
        } catch (NullPointerException e) {
            RELog.e(e);
        }
    }

    private void saveNextPart(int nextCounter) {
        if (nextCounter < multipleBct.size()) {
            saveBCTsSync(gImageUrl, nextCounter);
        }
    }

    /**
     * Resizes image and sends to server
     *
     * @param thumbnail : Bitmap
     */
    private void onCaptureImageResult(Bitmap thumbnail, String path) {
        if (thumbnail != null) {
            Bitmap bitmap = Bitmap.createScaledBitmap(thumbnail, 300, 300, false);
            BCTImageModel bctImageModel = new BCTImageModel(path, bitmap);
            bitmapList.add(bctImageModel);
            manageBCTImageView();
            bctImageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Decoding bitmap using bitmap options
     *
     * @param path : path of selected image
     * @return :Bitmap
     */
    private Bitmap decodeImage(final String path) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        return bitmap;
    }

    private String convertToTripLocale(long dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        return format.format(dateTime);
    }


    private String convertToGALog(long dateTime) {
      //  SimpleDateFormat format = new SimpleDateFormat(("yyyy-MM-dd_HH-mm"), Locale.getDefault());
        SimpleDateFormat format = new SimpleDateFormat(("dd MMM, yyyy - HH:mm a"), Locale.getDefault());
        return format.format(dateTime);
    }

    public void onRecordSavedSuccessfully() {
        Intent backIntent = getIntent();
        backIntent.putExtra("action", strAction);
        setResult(RESULT_OK, backIntent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    public void onRecordShare(String strTripId) {
        Intent backIntent = getIntent();
        backIntent.putExtra("action", strAction);
        backIntent.putExtra("tripId", strTripId);
        backIntent.putExtra("distance", strDistance);
        backIntent.putExtra("duration", strDuration);
//        backIntent.putExtra("speed", averageSpeed);
        setResult(RESULT_OK, backIntent);
        finish();
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
        /*GetLocationAsyncTask asyncTask = new GetLocationAsyncTask(this, mTVLocation, true);
        asyncTask.execute(String.valueOf(srcLatLng.latitude), String.valueOf(srcLatLng.longitude));*/
        startLoc = getAddressofLatlng(srcLatLng);
        mTVLocation.setText(startLoc);
    }

    private void getEndLocation(LatLng destLatLng) {
       /* GetLocationAsyncTask asyncTask = new GetLocationAsyncTask(this, mTVLocation, false);
        asyncTask.execute(String.valueOf(destLatLng.latitude), String.valueOf(destLatLng.longitude));*/
         endLoc = getAddressofLatlng(destLatLng);
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
            //Log.e("AddressVal","SL "+address.get(0).getSubLocality()+" Loc "+address.get(0).getLocality()+" AL "+address.get(0).getAddressLine(0)+" Fname  "+address.get(0).getFeatureName()+" PO "+address.get(0).getPostalCode());
           /* strLocAddress = ((address.get(0).getSubLocality() == null) || (address.get(0).getSubLocality().equalsIgnoreCase("")) ?
                    address.get(0).getLocality() : address.get(0).getSubLocality());*/
            if (address.get(0).getLocality() != null)
                strLocAddress = address.get(0).getLocality();
            else if (address.get(0).getLocality() == null && address.get(0).getSubLocality() != null) {
                strLocAddress = address.get(0).getSubLocality();
            } else if (address.get(0).getLocality() == null && address.get(0).getSubLocality() == null) {
                strLocAddress = address.get(0).getAddressLine(0);
            }
        } else {

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
                    REApplication instance = REApplication.getInstance();
                    CrashHandler crashHandler = instance.getCrashHandler();
                    crashHandler.removeData();

                    onRecordSavedSuccessfully();
                    showNavigationSummary("NoSave");
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    private boolean canSplitBct() {
        return breadcrumbTrail.getBreadcrumbs().size() > limitSize;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bct_submit) {
            mSaveBCTTitle.getText().toString().trim();
            if (!mSaveBCTTitle.getText().toString().trim().isEmpty() && mSaveBCTTitle.getText().toString().trim().length() > 4) {
                if (!mSaveBCTDESC.getText().toString().trim().isEmpty() && mSaveBCTDESC.getText().toString().trim().length() > 3) {
                    showLoading();
                    if (bitmapList != null && bitmapList.size() > 0) {
                        uploadImagePaths.clear();
                        for (BCTImageModel bctImageModel : bitmapList) {
                            uploadImagePaths.add(bctImageModel.getName());
                        }

                        String guid = REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId();

                        String randomUUID = "NA";
                        try {
                            randomUUID = REPreference.getInstance().getString(this, "RandomUUID");
                        } catch (PreferenceException e) {
                            e.printStackTrace();
                        }

                        UploadImageInteractor uploadImageInteractor = new UploadImageInteractor();
                        uploadImageInteractor.uploadImage(uploadImagePaths, this, guid, randomUUID);
                    } else {
                        performSaveTrip(uploadImagePaths);

                        try {

                            Bundle params = new Bundle();
                            params.putString("eventCategory", "Tripper");
                            params.putString("eventAction", "Submit and Save Ride Details");
                            if (mSwitch.isChecked()){
                                params.putString("eventLabel", "Make Ride Public Enable");
                            }else{
                                params.putString("eventLabel", "Make Ride Public Disable");
                            }
                            params.putString("Journey_kms", mBCTDistance.getText().toString());
                            params.putString("Journey_time", mBCTDuration.getText().toString());
                            params.putString("startDateAndTime",convertToGALog(breadcrumbTrail.getDateStart().getTime()));
                            params.putString("endDateAndTime", convertToGALog(breadcrumbTrail.getDateEnd().getTime()));
                            params.putString("origin", startLoc);
                            params.putString("destination",endLoc);
                            params.putString("rideName", mSaveBCTTitle.getText().toString().trim());
                            REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //   createTripBody(uploadImagePaths);
                    }
                } else {
                    Toast.makeText(this, "Please Enter the description", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Title should be more than four characters", Toast.LENGTH_SHORT).show();
            }

           /* if (mSaveBCTDESC.getText().length() > 0 && !mSaveBCTDESC.getText().toString().isEmpty()) {
                if (mSaveBCTTitle.getText().length() > 4 && !mSaveBCTTitle.getText().toString().isEmpty()) {
                    showLoading();
                    if (bitmapList != null && bitmapList.size() > 0) {
                        uploadImagePaths.clear();
                        for (BCTImageModel bctImageModel : bitmapList) {
                            uploadImagePaths.add(bctImageModel.getName());
                        }
                        UploadImageInteractor uploadImageInteractor = new UploadImageInteractor();
                        uploadImageInteractor.uploadImage(uploadImagePaths, this);
                    } else {

                        performSaveTrip(uploadImagePaths);
                        //   createTripBody(uploadImagePaths);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Title should be more than four characters", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Enter Title and description", Toast.LENGTH_SHORT).show();
            }*/

        } else if (v.getId() == R.id.no_img_layout) {
            REUtils.selectImage(BCTSaveInfoActivity.this, BCTSaveInfoActivity.this);
        } else if (v.getId() == R.id.tv_add_images) {
            REUtils.selectImage(BCTSaveInfoActivity.this, BCTSaveInfoActivity.this);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REConstants.REQUEST_CHECK_GALLERY:
            case REConstants.REQUEST_CHECK_CAMERA:
                try {
                    //Setting address when coming back from AddLocationActivity
                    if (resultCode == RESULT_OK && requestCode == REConstants.REQUEST_CHECK_GALLERY && data != null) {
                        Uri imageUri = data.getData();
                        String path = REUtils.getRealPathFromURI(imageUri, BCTSaveInfoActivity.this);
                        onCaptureImageResult(decodeImage(path), path);
                    } else if (requestCode == REConstants.REQUEST_CHECK_CAMERA && data != null && data.getExtras() != null) {
                        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                        if (thumbnail != null) {
                            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                            Uri tempUri = REUtils.getImageUri(getApplicationContext(), thumbnail);
                            String path = REUtils.getRealPathFromURI(tempUri, BCTSaveInfoActivity.this);
                            onCaptureImageResult(thumbnail, path);
                        }
                    }
                } catch (OutOfMemoryError e) {
                    RELog.e(e.getMessage());
                } catch (NullPointerException e) {
                    RELog.e(e.getMessage());
                } catch (Exception e) {
                    RELog.e(e.getMessage());
                }
                break;
        }
    }

    @Override
    public void onNavigationIconClick() {
        if (strAction.equalsIgnoreCase("Save")) {
            showErrorDialog(this, "Are you sure, you don't want to save the route?", "", true);
        } else if (strAction.equalsIgnoreCase("Share")) {
            showErrorDialog(this, "Are you sure, you don't want to share the route?", "", true);
        }
    }

    @Override
    public void onBackPressed() {
        if (strAction.equalsIgnoreCase("Save")) {
            showErrorDialog(this, "Are you sure, you don't want to save the route?", "", true);
        } else if (strAction.equalsIgnoreCase("Share")) {
            showErrorDialog(this, "Are you sure, you don't want to share the route?", "", true);
        }
    }

    @Override
    public void bctImageDeleted(List<BCTImageModel> list) {
        Log.e("test", "Bitmap lsi from adapter =" + list.size());
        Log.e("test", "Bitmap lsi from bitmapList =" + bitmapList.size());
        manageBCTImageView();
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
    public void onUploadTripSuccess(String tripId, boolean isShare, int index) {
        hideLoading();
        Log.e("onUploadTripSuccess: ", tripId + "........" + index + "..." + multipleBct.size());
        if (multipleBct.size() > 0 && index == 0) {
            firstTripId = tripId;
        }
        if (multipleBct.size() > 0 && index <= multipleBct.size() - 2) {
            saveNextPart(index + 1);
        } else if (multipleBct.size() > 0) {
            if (isShare) {
                onRecordShare(firstTripId);
                showNavigationSummary(strAction);
            } else {
                onRecordSavedSuccessfully();
                showNavigationSummary(strAction);
            }
        } else {
            if (isShare) {
                onRecordShare(tripId);
                showNavigationSummary(strAction);
            } else {
                onRecordSavedSuccessfully();
                showNavigationSummary(strAction);
            }
        }

        //  Toast.makeText(getApplicationContext(), "TripUploaded Successfully", Toast.LENGTH_SHORT).show();

        REApplication instance = REApplication.getInstance();
        CrashHandler crashHandler = instance.getCrashHandler();
        crashHandler.removeData();
    }

    private void showNavigationSummary(String action) {

        saveSummaryDetails(breadcrumbTrail);

        if (getApplicationContext() != null) {
            Intent navigationSummaryIntent = new Intent(getApplicationContext(), NavigationSummaryActivity.class);
            navigationSummaryIntent.putExtra("action", action);
            navigationSummaryIntent = createBCTData(navigationSummaryIntent, breadcrumbTrail);
            startActivity(navigationSummaryIntent);

        }
    }

    /**
     * Create and build BCT intent data from various sources.
     *
     * @param aIntent
     * @param breadcrumbTrail
     * @return
     */
   /* private Intent createBCTData(Intent aIntent, BreadcrumbTrail breadcrumbTrail) {
        if (breadcrumbTrail != null) {
            double speed = Objects.requireNonNull(breadcrumbTrail.getRideSummary().getAverageSpeed()).getSpeedValue();
            aIntent.putExtra(BCT_DURATION, strDuration);
            aIntent.putExtra(BCT_DISTANCE, strDistance);
            aIntent.putExtra(BCT_SPEED, speed);
        }
        return aIntent;
    }*/
    private Intent createBCTData(Intent aIntent, BreadcrumbTrail breadcrumbTrail) {
        if (breadcrumbTrail != null) {
            RideSummary rideSummary = breadcrumbTrail.getRideSummary();
            Duration totalDuration = rideSummary.getTotalDuration();

            Long strlngDuration = totalDuration.toMinutes();
            Double strDuration = strlngDuration.doubleValue();

            double strDistance = rideSummary.getTotalDistance().toMeters();

            Speed averageSpeed = rideSummary.getAverageSpeed();

            double speed;
            if (averageSpeed != null) {
                speed = averageSpeed.getSpeedValue();
            } else {
                speed = 0.0;
            }
            //Log.e("BCTcreateBCTData",""+strDistance+ " "+strDuration);
            aIntent.putExtra(BCT_DISTANCE, strDistance);
            aIntent.putExtra(BCT_DURATION, strDuration);
            aIntent.putExtra(BCT_SPEED, speed);
        }
        return aIntent;
    }

    @Override
    public void onUploadTripFailure(String error, int index) {
        hideLoading();
        if (multipleBct.size() > 0 && index < multipleBct.size() - 2) {
            saveNextPart(index + 1);
        } else {
            showErrorDialog(this, "Something went wrong.please try again later", "Alert!", false);
        }

        /*if (error != null && error.contains("'track' must contain atleast 2 points")) {
            error = "Trip needs minimum 2 location points to save the route";
            showErrorDialog(this, getResources().getString(R.string.record_stopped_no_data), "Alert!", false);
        } else {
             showErrorDialog(this, "Something went wrong.please try again later", "Alert!", false);
        }*/
    }

    @Override
    public void onSuccess(List<String> imageUrlList) {
        performSaveTrip(imageUrlList);
    }

    @Override
    public void onFailure(String errorMessage) {
        hideLoading();
        showErrorDialog(this, "" + errorMessage, "Image upload failed", false);
    }

    /**
     * AsyncTask for downloading mapImage
     */
    /*private static class GetImageAsyncTask extends AsyncTask<String, Void, String> {
        private final WeakReference<TextView> tvLocation;
        private final boolean start;

        private GetImageAsyncTask(TextView tvLocation, boolean isStart) {
            this.tvLocation = new WeakReference<>(tvLocation);
            this.start = isStart;
        }

        @Override
        protected String doInBackground(String... params) {

            String currentAddress = "";

            try {
                double lattitude = Double.parseDouble(params[0]);
                double longitude = Double.parseDouble(params[1]);
                List<Address> addresses = geocoder.getFromLocation(lattitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = "";
                if (addresses != null && addresses.size() > 0) {
                    //address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    address = ((addresses.get(0).getSubLocality() == null) || (addresses.get(0).getSubLocality().equalsIgnoreCase("")) ?
                            addresses.get(0).getLocality() : addresses.get(0).getSubLocality());
                }
                currentAddress = address;
            } catch (IOException e) {
                RELog.e(e);
            }
            return currentAddress;

        }

        @Override
        protected void onPostExecute(String location) {
            super.onPostExecute(location);

            if (location != null) {
                if (start) {
                    this.tvLocation.get().setText(location);
                } else {
                    this.tvLocation.get().setText(" to "+location);
                }
            }
        }
    }*/
}