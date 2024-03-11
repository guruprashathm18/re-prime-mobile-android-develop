package com.royalenfield.reprime.ui.home.service.search;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.royalenfield.firebase.realTimeDatabase.FirebaseManager;
import com.royalenfield.firebase.realTimeDatabase.OnFirebaseDealerResponseCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.request.proxy.service.SearchServiceCentreRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.activity.AddCheckInActivity;
import com.royalenfield.reprime.ui.home.rides.interactor.GoogleMapAPIInteractor;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.home.service.asynctask.ServiceAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.service.history.interactor.ServiceBookingInteractor;
import com.royalenfield.reprime.ui.home.service.history.listener.ServiceBookingListener;
import com.royalenfield.reprime.ui.home.service.history.presenter.ServiceBookingPresenter;
import com.royalenfield.reprime.ui.home.service.search.adapter.DealersListAdapter;
import com.royalenfield.reprime.ui.home.service.search.presenter.SearchRowViewPresenter;
import com.royalenfield.reprime.ui.home.service.search.view.ItemClickListener;
import com.royalenfield.reprime.ui.home.service.search.view.SearchFilterEmptyListListener;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RERecyclerViewSeperatorDecoration;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

public class SearchActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener, TextWatcher,
        OnFirebaseDealerResponseCallback, ItemClickListener, SearchFilterEmptyListListener,
        ServiceAsyncTaskListeners.DealerDistanceCalc, DealersListAdapter.CheckInListener,
        RidesListeners.OnPlacesCallFinishedListener, ServiceBookingListener {
    private static final String TAG = SearchActivity.class.getSimpleName();

    private RecyclerView recyclerView_dealersList;
    private DealersListAdapter dealersListAdapter = null;
    private EditText searchView;
    private List<DealerMasterResponse> mDealersList;
    private TextView mNoServiceCenterText;
    private TextView mTvWhereAreYou;
    private String mType = "", mRideId;
    private  String mServiceCenterNo,crmStatus;
    private static final int SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS = 3;
    private TitleBarView mTitleBarView;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 512445;
    private static final int LOCATION_SETTINGS_REQUEST = 201;
    ServiceBookingPresenter mServiceBookingPresenter;
    private LocationManager locationManager;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private Location currentLocation;


    public static Intent getIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initializeViews();
        if (!mType.equals(REConstants.SEARCH_ACTIVITY_CHECK_IN_LIST)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (!isLocationEnabled(getApplicationContext())) {
                    enabletheGps();
                }else {
                    getCurrentLoc();
                    getNearestServiceCenterList();
                }
            } else {
                enableLocation();
            }
        }
    }

    /**
     * Initialising views
     */
    private void initializeViews() {
        getIntentData();
        recyclerView_dealersList = findViewById(R.id.dealers_list);
        searchView = findViewById(R.id.searchView);
        mNoServiceCenterText = findViewById(R.id.textView_no_servicecenter);
        recyclerView_dealersList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_dealersList.setLayoutManager(layoutManager);
        mTitleBarView = findViewById(R.id.search_topbar);
        searchView.addTextChangedListener(this);
        mTvWhereAreYou = findViewById(R.id.tv_where_are_you);
        checkIfCheckInList();
    }

    private void getIntentData() {
        mType = getIntent().getStringExtra(REConstants.SEARCH_ACTIVITY_INPUT_TYPE);
        mRideId = getIntent().getStringExtra(REConstants.SEARCH_ACTIVITY_RIDE_ID);
    }

    private void checkIfCheckInList() {
        if (mType.equals(REConstants.SEARCH_ACTIVITY_CHECK_IN_LIST)) {
            mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.add_checkin));
            mTvWhereAreYou.setVisibility(View.VISIBLE);
            searchView.setHint(getResources().getString(R.string.search_places));
            mNoServiceCenterText.setText(getResources().getString(R.string.no_check_in_available));
        } else {
            mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.search_banner));
        }
    }

    /**
     * Fetches dealer data from firebase
     */
    public void getServiceCenterListFromFCM() {
        if (mType.equals(REConstants.SEARCH_ACTIVITY_DEALER_LIST)) {
            showLoading();
            FirebaseManager.getInstance().fetchDealerResponseFromFirebase("", this);
        }
    }

    /**
     * This sets the places results to adapter
     *
     * @param searchResults : List<POIResults>
     */
    private void setResults(List<POIResults> searchResults) {

        dealersListAdapter = new DealersListAdapter(searchResults, this, mType);
        recyclerView_dealersList.setItemAnimator(new DefaultItemAnimator());
        //Setting item decoration for recyclerView
        RERecyclerViewSeperatorDecoration decoration = new RERecyclerViewSeperatorDecoration(this,
                getApplicationContext().getResources().getColor(R.color.white_50), 1f);
        recyclerView_dealersList.addItemDecoration(decoration);
        recyclerView_dealersList.setAdapter(dealersListAdapter);
    }

    private void showSearchedServiceCenterList() {
        try {
            if (mDealersList != null && mDealersList.size() > 0) {
                dealersListAdapter = new DealersListAdapter(mDealersList, this,
                        this, new SearchRowViewPresenter(), mType);
                recyclerView_dealersList.setItemAnimator(new DefaultItemAnimator());
                //Setting item decoration for recyclerView
                RERecyclerViewSeperatorDecoration decoration = new RERecyclerViewSeperatorDecoration(this,
                        getApplicationContext().getResources().getColor(R.color.white_50), 1f);
                recyclerView_dealersList.addItemDecoration(decoration);
                recyclerView_dealersList.setAdapter(dealersListAdapter);
            } else {
                REUtils.showErrorDialog(this, "Unable to load service centre");
            }
        } catch (Exception e) {
            RELog.d(TAG, "Recyclerview Issue");
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFirebaseDealersListSuccess(List<DealerMasterResponse> dealersResponseList) {
//        mDealersList = dealersResponseList;
//        RELog.e(TAG, "dealer data = ", mDealersList.size());
//        DealersDistanceCalcAsyncTask distanceCalcAsyncTask = new DealersDistanceCalcAsyncTask(mDealersList, this);
//        distanceCalcAsyncTask.execute();
        //No need to calculate distance
        onDealersDistanceCalcComplete(dealersResponseList);
    }

    @Override
    public void onFirebaseDealerListFailure(String message) {
        hideLoading();
//        REUtils.showErrorDialog(this, message);
    }

    @Override
    public void onFirebaseDealerDetailSuccess(DealerMasterResponse dealersDetailResponse) {

    }

    @Override
    public void onFirebaseDealerDetailFailure(String message) {

    }

    @Override
    public void onItemClick(View view, int position, List<DealerMasterResponse> filteredList) {
        Bundle params=new Bundle();
        params.putString("eventCategory", "Motorcycles-Schedule a service");
        params.putString("eventAction", "Search service centre");
        params.putString("eventLabel", filteredList.get(position).getDealerName()+" Clicked");
       REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
        if (mType.equals(REConstants.SEARCH_ACTIVITY_DEALER_LIST)) {
            RELog.e(TAG, "Search Actiivty = "+ filteredList.get(position).getDealerName());
            mServiceCenterNo = filteredList.get(position).getPhone();
            //CRM status check E/D
              crmStatus = filteredList.get(position).getDealerSource();
            Log.e("crmStatusSA",""+crmStatus);
            if (crmStatus.equalsIgnoreCase(REConstants.DEALER_SOURCE_OFFLINE)){
               /* Intent intent = new Intent();
                intent.putExtra("sc_index", "" + null);
                setResult(3, intent);*/
                showOfflineServiceDialog(mServiceCenterNo);
            } else if (crmStatus.equalsIgnoreCase(REConstants.DEALER_SOURCE_ONLINE)) {
                Intent intent = new Intent();
                intent.putExtra("sc_index", "" + position);
                ArrayList<DealerMasterResponse> filteredArrayList = new ArrayList<>();
                filteredArrayList.add(filteredList.get(position));
                intent.putParcelableArrayListExtra(REConstants.DEALERS_FILTERED_LIST_EXTRA, filteredArrayList);
                setResult(3, intent);
                finish();//finishing activity
            }
            else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.dealer_noservice),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mType.equalsIgnoreCase(REConstants.SEARCH_ACTIVITY_CHECK_IN_LIST)) {
            if (s != null && s.length() >= 3) {
                new GoogleMapAPIInteractor().getPlaces(s.toString(), this);
            }
        } else {
            if (dealersListAdapter != null)
                dealersListAdapter.getFilter().filter(searchView.getText());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onSearchFilter(boolean isEmpty) {
        if (isEmpty) {
            recyclerView_dealersList.setVisibility(View.INVISIBLE);
            mNoServiceCenterText.setVisibility(View.VISIBLE);
        } else {
            recyclerView_dealersList.setVisibility(View.VISIBLE);
            mNoServiceCenterText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDealersDistanceCalcComplete(List<DealerMasterResponse> dealersResponseList) {
        RELog.e(TAG, "loading finished");
        hideLoading();
        mDealersList = dealersResponseList;
        showSearchedServiceCenterList();
    }

    @Override
    public void CheckInClicked(View view, int position, String strSelectedValue, String formattedAddress) {
        Intent intent = new Intent(REApplication.getAppContext(), AddCheckInActivity.class);
        intent.putExtra("CheckInPlaceName", strSelectedValue);
        intent.putExtra("CheckInAddress", formattedAddress);
        intent.putExtra(REConstants.SEARCH_ACTIVITY_RIDE_ID, mRideId);
        startActivity(intent);
        //Clearing the entered text and results as the item is selected
        if (searchView != null && searchView.getText().length() > 0) {
            searchView.setText("");
            dealersListAdapter = new DealersListAdapter(new ArrayList<>(), this, mType);
            recyclerView_dealersList.setAdapter(dealersListAdapter);
        }
    }

    private void showOfflineServiceDialog(String mServiceCenterNo) {
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Service Centre Alert");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        REUtils.showOnlyOfflineServicDialog(this, getString(R.string.text_servicecenter_offline_title), getString(R.string.text_servicecenter_offline_msg), new REUtils.OnDialogButtonClickListener() {
            @Override
            public void onOkCLick() {
                // Toast.makeText(getActivity(),"CALLLLLLLLL",Toast.LENGTH_SHORT).show();
                if (mServiceCenterNo != null && !mServiceCenterNo.equals("")) {
                    if ( getApplicationContext()!= null) {
                        checkAndRequestCallPermissions(getApplicationContext(),
                                SearchActivity.this, mServiceCenterNo,
                                SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS, SearchActivity.this);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Contact number unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onPlaceSuccess(List<POIResults> poiResults) {
        setResults(poiResults);
    }

    @Override
    public void onPlaceFailure(String error) {

    }

    private void getNearestServiceCenterList() {
        mServiceBookingPresenter = new
                ServiceBookingPresenter(this, new ServiceBookingInteractor());
        Location currentlocation = REUtils.getLocationFromModel();
        showLoading();
        mServiceBookingPresenter.getServiceCentreSearchResults(new SearchServiceCentreRequest(String.valueOf(currentlocation.getLatitude()),
                String.valueOf(currentlocation.getLongitude()), 500,"",true,false));
       /* mServiceBookingPresenter.getServiceCenters(new ServiceCenterRequest("16.726903",
                "81.67165385", 500));*/
        /*mServiceBookingPresenter.getServiceCentreSearchResults(new SearchServiceCentreRequest("15.755640",
                "79.965480", 500,"",true,false));*/

    }
    @Override
    public void onServiceBookingSuccess() {

    }

    @Override
    public void onServiceBookingFailure(String errorMessage) {

    }

    @Override
    public void onServiceTimeSlotSuccess() {

    }

    @Override
    public void onServiceTimeSlotFailure(String errorMessage) {

    }

    @Override
    public void onServiceCenterFetchSuccess(List<DealerMasterResponse> response) {
        //adding  nearby dealers
        onDealersDistanceCalcComplete(response);
    }

    @Override
    public void onServiceCenterFetchFailed(String errorMessage) {

    }

    @Override
    public void ongetPickupAndDoorstepServiceSlotsSuccess() {

    }

    @Override
    public void ongetPickupAndDoorstepServiceSlotsFailure() {

    }

    private void enableLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestLocationPermission();

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            // Request for Location permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!REUtils.isLocationEnabled(getApplicationContext())) {
                    enabletheGps();
                } else {
                    getCurrentLoc();
                   // getNearestServiceCenterList();
                }
            } else {
                //permission is denied (this is the first time, when "never ask again" is not checked)
                getServiceCenterListFromFCM();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //Success Perform Task Here
                    getCurrentLoc();
                    break;
                case Activity.RESULT_CANCELED:
                    getServiceCenterListFromFCM();
                    break;
            }
        }
    }
    public void enabletheGps() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(SearchActivity.this,
                                LOCATION_SETTINGS_REQUEST);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    /**
     * Gets the current location from NetworkProvider
     */
    private void getCurrentLoc() {
        Log.e("SearchActivity", "getCurrentLocationCalled");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            showLoading();
            locationManager = (LocationManager) REApplication.getAppContext()
                    .getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            if (locationManager != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                                currentLocation = location;
                                REUserModelStore.getInstance().setLatitude(currentLocation.getLatitude());
                                REUserModelStore.getInstance().setLongitude(currentLocation.getLongitude());

                                //After getting the location call nearby proximity Api
                                getNearestServiceCenterList();
                                if (currentLocation != null && location.getLatitude() != 0 && location.getLongitude() != 0)
                                    //stops the current location updates.
                                    locationManager.removeUpdates(this);
                                locationManager = null;
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

    @Override
    protected void onResume() {
        super.onResume();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Service Centre");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }
}
