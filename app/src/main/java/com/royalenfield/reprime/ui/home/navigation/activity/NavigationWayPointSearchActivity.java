package com.royalenfield.reprime.ui.home.navigation.activity;

import static com.royalenfield.reprime.utils.REConstants.KEY_TRIPPER_GTM;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bosch.softtec.components.core.models.LatLng;
import com.bosch.softtec.components.midgard.core.search.SearchCallback;
import com.bosch.softtec.components.midgard.core.search.SearchProvider;
import com.bosch.softtec.components.midgard.core.search.details.DetailsCallback;
import com.bosch.softtec.components.midgard.core.search.models.Response;
import com.bosch.softtec.components.midgard.core.search.models.SearchError;
import com.bosch.softtec.components.midgard.core.search.models.SearchOptions;
import com.bosch.softtec.components.midgard.core.search.models.results.PoiSearchResult;
import com.bosch.softtec.components.midgard.core.search.models.results.SearchResult;
import com.royalenfield.googleSearch.SearchProviderManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.response.googlemap.poi.POIPredictionResults;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;
import com.royalenfield.reprime.models.response.googlemap.poi.StructuredFormatting;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.navigation.adapter.NavigationWayPointSearchAdapter;
import com.royalenfield.reprime.ui.home.navigation.adapter.RecentLocationListAdapter;
import com.royalenfield.reprime.ui.home.navigation.adapter.RecentRoutesAdapter;
import com.royalenfield.reprime.ui.home.navigation.listener.NavigationListeners;
import com.royalenfield.reprime.ui.home.navigation.model.AddAddress;
import com.royalenfield.reprime.ui.home.navigation.model.RecentLocation;
import com.royalenfield.reprime.ui.home.navigation.model.RecentRoute;
import com.royalenfield.reprime.ui.home.navigation.model.RecentRouteLocationManager;
import com.royalenfield.reprime.ui.home.service.search.view.DividerItemDecoration;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * This activity is used for searching places
 */
public class NavigationWayPointSearchActivity extends REBaseActivity implements TextWatcher,
        NavigationWayPointSearchAdapter.OnItemClickListener, TitleBarView.OnNavigationIconClickListener,
        NavigationListeners.OnPlacesCallFinishedListener, NavigationListeners.OnPlaceDetailsCallFinishedListener,
        RecentLocationListAdapter.OnRecentLocationItemClickListener, RecentRoutesAdapter.OnRecentRouteItemClickListener {

    public static final int REQUESTCODE_WAYPOINT = 1;
    public static final int REQUESTCODE_DESTINATION_WAYPOINT = 2;
    public static final int REQUESTCODE_SOURCE_WAYPOINT = 3;
    public static final int REQUESTCODE_ADD_HOME_ADDRESS = 150;
    public static final int REQUESTCODE_ADD_OFFICE_ADDRESS = 151;
    public static final int REQUESTCODE_ADD_FAVOURITE_ADDRESS = 152;
    public static final int REQUESTCODE_MORE_FAVOURITE_ADDRESS = 153;
    private RecyclerView mSearchRecyclerView, mRecentLocationRecyclerView, mRecentRouteRecyclerView;
    private NestedScrollView mNestedScrollView, mNestedScrollViewCore;
    private ConstraintLayout addressLayout;
    private ImageView imgMarkerView;
    private TextView mRecentLocationLabel, mRecentRouteLabel;
    private TextView homeAddressid, officeAddressid;
    private EditText mSearchEditText;
    private String mPlaceSelected;
    private SearchResult mSearchResult;
    private int mAPIFailureCount = 1;
    boolean isMilesUnitSelected = false;
    ArrayList<WayPointsData> route;
    private RelativeLayout home_view_layout, office_view_layout, for_more_view_layout;
    public static final int REQUESTCODE_ADD_FAVOURITE_SEARCH = 154;
    private Intent intent;
    private TitleBarView mTitleBarView;

    /**
     * The ReverseGeocodingCallback to which the results of a search request will be delivered to.
     * Communicating with BILA for getting search results
     */
    SearchCallback<SearchResult> searchCallback = new SearchCallback<SearchResult>() {
        @Override
        public void onSearchFinished(@NotNull Response<SearchResult> response) {
            final List<SearchResult> searchResults = response.getResults();
            List<POIPredictionResults> predictionResults = getPOIPredictionsResults(searchResults);
            runOnUiThread(() -> setResults(predictionResults));
        }

        @Override
        public void onSearchError(@NotNull SearchError searchError, @Nullable String s) {
            RELog.e(NavigationWayPointSearchActivity.class.getName(), "searchError:" + searchError.name());
            /*hideKeyboard();
            runOnUiThread(() -> {
                REUtils.showErrorDialog(NavigationWayPointSearchActivity.this,
                        REUtils.getNavigationError(searchError));
            });*/
        }
    };

    private SearchOptions searchOptions;
    private SearchProvider searchProvider;
    private String mWayPointType;
    private String mLocationType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_search_waypoint);
        initViews();

        Bundle aParams = new Bundle();
        aParams.putString("screenName", "Tripper Destination Options");
        REUtils.logGTMEvent(REConstants.ScreenViewManual, aParams);


        /*try {
            Intent intent = getIntent();
            intent = getIntent();
            mWayPointType= intent.getStringExtra("waypoint_type");
            String place=intent.getStringExtra("place");
            Double latitude= intent.getDoubleExtra("latitude",0);
            Double longitude=intent.getDoubleExtra("longitude",0);
            onItemFetched(place, longitude,latitude,false);

        }catch (Exception e){
            e.printStackTrace();
        }*/

    }

    /**
     * Initializes the views.
     */
    private void initViews() {
        isMilesUnitSelected = REUtils.isMilesUnitSelected();
        intent = getIntent();
        mWayPointType = intent.getStringExtra("waypoint_type");
        mLocationType = intent.getStringExtra("navigation_type");
        mTitleBarView = findViewById(R.id.plan_ride_header);
        addressLayout = findViewById(R.id.address_layout);
        imgMarkerView = findViewById(R.id.marker_info);
        mNestedScrollViewCore = findViewById(R.id.recent_items);

        mNestedScrollView = findViewById(R.id.recent_items_nestedview);

        mRecentLocationLabel = findViewById(R.id.label_recent_location);
        mRecentLocationLabel.setVisibility(View.GONE);
        mRecentRouteLabel = findViewById(R.id.label_recent_route);
        mRecentRouteLabel.setVisibility(View.GONE);
        mSearchEditText = findViewById(R.id.et_waypoint_search);
        mSearchEditText.addTextChangedListener(this);
        mSearchEditText.requestFocus();
        homeAddressid = findViewById(R.id.home_address_id);
        office_view_layout = findViewById(R.id.office_layout);
        home_view_layout = findViewById(R.id.home_layout);
        for_more_view_layout = findViewById(R.id.for_more_layout);

        if (mWayPointType.equalsIgnoreCase(("destination"))) {
            mSearchEditText.setHint("Enter Destination");
        } else if (mWayPointType.equalsIgnoreCase(("waypoint"))) {
            mSearchEditText.setHint("Add Waypoint");
        }

        mNestedScrollView = findViewById(R.id.recent_items_nestedview);
        mSearchRecyclerView = findViewById(R.id.search_recyclerview);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false));
        ((SimpleItemAnimator) Objects.requireNonNull(mSearchRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        mRecentLocationRecyclerView = findViewById(R.id.recentlocation_recyclerview);
        mRecentLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false));
        ((SimpleItemAnimator) Objects.requireNonNull(mRecentLocationRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        mRecentRouteRecyclerView = findViewById(R.id.recentroute_recyclerview);
        mRecentRouteRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false));
        ((SimpleItemAnimator) Objects.requireNonNull(mRecentRouteRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        searchProvider = SearchProviderManager.getInstance().searchProvider(this);
        searchOptions = new SearchOptions.Builder()
                .searchLimit(40)
                .searchLocale(Locale.getDefault())
                .build();

        checkForRecentRoute();
        checkForRecentLocation();
        refreshSearchScreen();

        office_view_layout.setOnClickListener(v -> {
            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "Location Type Selected");
            params.putString("eventLabel", "Work");
            REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
            Log.e( "Location Type Selected: ",params.toString());

            if (officeAddressid.getText().toString().equalsIgnoreCase(getResources().getString(R.string.choose_location))) {

                Intent i = new Intent(NavigationWayPointSearchActivity.this, NavigationAddAddressMapActivity.class);
                i.putExtra("location_type", "Work");
                startActivityForResult(i, REQUESTCODE_ADD_OFFICE_ADDRESS);
            } else {
                AddAddress address = RecentRouteLocationManager.getAddressList(NavigationWayPointSearchActivity.this).get(1);
                onItemFetched(address.getLocationName(), address.getLocationCoordinates().latitude, address.getLocationCoordinates().longitude, false);
            }
        });
        home_view_layout.setOnClickListener(v -> {
            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "Location Type Selected");
            params.putString("eventLabel", "Home");
            REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
            Log.e( "Location Type Selected: ",params.toString() );
            if (homeAddressid.getText().toString().equalsIgnoreCase(getResources().getString(R.string.choose_location))) {

                Intent i = new Intent(NavigationWayPointSearchActivity.this, NavigationAddAddressMapActivity.class);
                i.putExtra("location_type", "Home");
                startActivityForResult(i, REQUESTCODE_ADD_HOME_ADDRESS);
            } else {
                AddAddress address = RecentRouteLocationManager.getAddressList(NavigationWayPointSearchActivity.this).get(0);
                onItemFetched(address.getLocationName(), address.getLocationCoordinates().latitude, address.getLocationCoordinates().longitude, false);
            }
        });
        for_more_view_layout.setOnClickListener(v -> {

            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "Location Type Selected");
            params.putString("eventLabel", "More");
            REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
            Log.e( "Location Type Selected: ",params.toString() );


            Intent i = new Intent(NavigationWayPointSearchActivity.this, SavedLocationsListActivity.class);
            i.putExtra("location_type", "search_favourites");
            startActivityForResult(i, REQUESTCODE_MORE_FAVOURITE_ADDRESS);
        });

    }

    private void checkForRecentRoute() {
        ArrayList<RecentRoute> rList = RecentRouteLocationManager.getRecentRoutes(this);
        if (rList.size() > 0) {
            Collections.reverse(rList);
            setRecentRouteResults(rList);
        } else {
            mRecentRouteLabel.setVisibility(View.GONE);
        }
    }

    private void setRecentRouteResults(ArrayList<RecentRoute> rlist) {
        RecentRoutesAdapter mAdapter = new RecentRoutesAdapter(rlist, this);
        mRecentRouteRecyclerView.addItemDecoration(
                new DividerItemDecoration(
                        this
                )
        );
        mRecentRouteRecyclerView.setAdapter(mAdapter);
        mNestedScrollView.setVisibility(View.VISIBLE);
        mSearchRecyclerView.setVisibility(View.VISIBLE);
        mRecentLocationLabel.setVisibility(View.VISIBLE);
        mRecentRouteRecyclerView.setVisibility(View.VISIBLE);
        mRecentLocationRecyclerView.setVisibility(View.VISIBLE);

        mRecentRouteLabel.setVisibility(View.VISIBLE);
        mRecentRouteRecyclerView.setVisibility(View.VISIBLE);
    }

    private void checkForRecentLocation() {
        ArrayList<RecentLocation> lList = RecentRouteLocationManager.getRecentLocationList(this);
        if (!lList.isEmpty()) {
            Collections.reverse(lList);
            setRecentLocationResults(lList);
        } else {
            mRecentLocationLabel.setVisibility(View.GONE);
        }
    }

    /**
     * This sets the places results to adapter
     *
     * @param poiPredictionResults : List<POIPredictionResults>
     */
    public void setResults(List<POIPredictionResults> poiPredictionResults) {
        NavigationWayPointSearchAdapter mAdapter = new NavigationWayPointSearchAdapter(getApplicationContext(),
                poiPredictionResults, this, isMilesUnitSelected);
        mSearchRecyclerView.addItemDecoration(
                new DividerItemDecoration(
                        this
                )
        );
        mSearchRecyclerView.setAdapter(mAdapter);
        mSearchRecyclerView.setVisibility(View.VISIBLE);
        mNestedScrollView.setVisibility(View.INVISIBLE);
        mRecentLocationRecyclerView.setVisibility(View.GONE);
        mRecentLocationLabel.setVisibility(View.GONE);
        addressLayout.setVisibility(View.GONE);
    }

    public void setRecentLocationResults(ArrayList<RecentLocation> recentLocationResults) {
        RecentLocationListAdapter mAdapter = new RecentLocationListAdapter(recentLocationResults, this);
        mRecentLocationRecyclerView.addItemDecoration(
                new DividerItemDecoration(this));
        mRecentLocationRecyclerView.setAdapter(mAdapter);
        mNestedScrollView.setVisibility(View.VISIBLE);
        mSearchRecyclerView.setVisibility(View.VISIBLE);
        mRecentLocationRecyclerView.setVisibility(View.VISIBLE);
        mRecentLocationLabel.setVisibility(View.VISIBLE);
        mRecentLocationRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.intent = intent;
        if (intent != null && intent.getExtras() != null) {
            mWayPointType = intent.getStringExtra("waypoint_type");
            refreshSearchScreen();
        }
    }

    private void refreshSearchScreen() {
        if (mWayPointType.equalsIgnoreCase(("destination"))) {
            mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.navigation_enter_destination));
            imgMarkerView.setImageResource(R.drawable.ic_marker_red);
        } else if (mWayPointType.equalsIgnoreCase(("waypoint"))) {
            mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.label_add_waypoint));
            imgMarkerView.setImageResource(R.drawable.ic_marker_yellow);
        } else {
            if (mLocationType != null) {
                if (mLocationType.equalsIgnoreCase("Home")) {
                    mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.set_home));
                    mSearchEditText.setHint("Enter home address");
                } else if (mLocationType.equalsIgnoreCase("Work")) {
                    mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.set_work));
                    mSearchEditText.setHint("Enter Work address");
                } else {
                    mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.set_default));
                    mSearchEditText.setHint("Enter an address");
                }
                if (mWayPointType.equalsIgnoreCase(("destination"))) {
                    mSearchEditText.setHint("Enter Destination");
                } else if (mWayPointType.equalsIgnoreCase(("waypoint"))) {
                    mSearchEditText.setHint("Add Waypoint");
                }
            }

//            mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.label_add_destination));
            imgMarkerView.setImageResource(R.drawable.ic_marker_red);
            addressLayout.setVisibility(View.GONE);
            mNestedScrollViewCore.setVisibility(View.GONE);
        }

        if (RecentRouteLocationManager.getAddressList(this).size() >= 1) {
            if (RecentRouteLocationManager.getAddressList(this).get(0).getLocationCoordinates() != null && RecentRouteLocationManager.getAddressList(this).get(0).getLocationCoordinates().equals(new com.google.android.gms.maps.model.LatLng(0, 0))) {
                homeAddressid.setText(R.string.choose_location);
            } else if (RecentRouteLocationManager.getAddressList(this).get(0).getLocationCoordinates() != null) {
                homeAddressid.setText(RecentRouteLocationManager.getAddressList(this).get(0).getLocationCoordinates().toString().substring(8));
            }
        }
        officeAddressid = findViewById(R.id.office_address_id);
        if (RecentRouteLocationManager.getAddressList(this).size() >= 2) {
            if (RecentRouteLocationManager.getAddressList(this).get(1).getLocationCoordinates() != null && RecentRouteLocationManager.getAddressList(this).get(1).getLocationCoordinates().equals(new com.google.android.gms.maps.model.LatLng(0, 0))) {
                officeAddressid.setText(R.string.choose_location);
            } else if (RecentRouteLocationManager.getAddressList(this).get(1).getLocationCoordinates() != null) {
                officeAddressid.setText(RecentRouteLocationManager.getAddressList(this).get(1).getLocationCoordinates().toString().substring(8));
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            String c = "" + s.toString().charAt(0);
            if (!containsSpecialCharacter(c)) {
                if (s.length() >= 3) {
                    //call midgard library to get the place name, address along with distance.
                    LatLng aLocation = new LatLng(REUtils.getLocationFromModel().getLatitude(), REUtils.getLocationFromModel().getLongitude(), null);
                    searchProvider.autocompleteSearchAroundLocation(s.toString(), aLocation, searchOptions, searchCallback);
                }
            } else {
                Toast.makeText(this, "Search query should start with character", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            mSearchEditText.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_light));
            if (mWayPointType.equalsIgnoreCase(("destination"))) {
                checkForRecentRoute();
                checkForRecentLocation();
                addressLayout.setVisibility(View.VISIBLE);
                mNestedScrollView.setVisibility(View.VISIBLE);
                mSearchRecyclerView.setVisibility(View.GONE);
            } else if (mWayPointType.equalsIgnoreCase(("waypoint"))) {
                checkForRecentRoute();
                checkForRecentLocation();
                addressLayout.setVisibility(View.VISIBLE);
                mNestedScrollView.setVisibility(View.VISIBLE);
                mSearchRecyclerView.setVisibility(View.GONE);
            }

        } else {
            mSearchEditText.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular));
        }

    }

    public boolean containsSpecialCharacter(String s) {
        return (s == null) ? false : s.matches("[^A-Za-z0-9 ]");
    }

    @Override
    public void onItemClick(String place, String placeId) {
//        hideKeyboard();
//        //call another API to fetch the location details from google using placeId
//        new GoogleMapAPIInteractor().getPlaceDetails(placeId, this);
    }

    @Override
    public void onItemClick(String place, SearchResult searchResults) {
        mAPIFailureCount = 1;
        hideKeyboard();
        showLoading();
        mPlaceSelected = place;
        mSearchResult = searchResults;
        getPlaceDetails(searchResults);
    }

    private void getPlaceDetails(SearchResult searchResults) {
        searchProvider.fetchDetails(searchResults, Locale.getDefault(), detailsCallback);
    }

    DetailsCallback detailsCallback = new DetailsCallback() {
        @Override
        public void onFinished(@NotNull SearchResult searchResult) {
            hideLoading();
            onItemFetched(mPlaceSelected, searchResult.getAddress().component1().getLatitude(), searchResult.getAddress().component1().getLongitude(), false);
        }

        @Override
        public void onError(@org.jetbrains.annotations.Nullable String s) {
            if (mAPIFailureCount > REConstants.NAV_API_RETRY_COUNTER) {
                hideLoading();
                runOnUiThread(() -> REUtils.showErrorDialog(NavigationWayPointSearchActivity.this, REApplication.getAppContext().getString(R.string.error_tbt_api)));
            } else {
                RELog.e("onPLaceError", "Count :" + mAPIFailureCount);
                mAPIFailureCount++;
                getPlaceDetails(mSearchResult);
            }
        }
    };

    @Override
    public void onNavigationIconClick() {
        hideKeyboard();
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onPlacesSuccess(List<POIPredictionResults> poiPredictionResults) {
        setResults(poiPredictionResults);
    }

    @Override
    public void onPlacesFailure(String error) {

    }

    @Override
    public void onPlaceDetailsSuccess(POIResults poiDetails) {
        onItemFetched(poiDetails.getName(),
                poiDetails.getGeometry().getLocation().getLat(), poiDetails.getGeometry().getLocation().getLng(), false);
    }

    @Override
    public void onPlaceDetailsFailure(String error) {
        RELog.e("NavigationWaypointFragment", " error " + error);
    }

    public void onItemFetched(String place, double latitude, double longitude, boolean isRecentLocation) {
        Intent intent = new Intent();
        intent.putExtra("place", place);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("isRecentLocation", isRecentLocation);

        if (mWayPointType != null) {
            switch (mWayPointType) {
                case "destination":
                    setResult(REQUESTCODE_DESTINATION_WAYPOINT, intent);
                    break;
                case "source":
                    setResult(REQUESTCODE_SOURCE_WAYPOINT, intent);
                    break;
                case "waypoint":
                    setResult(REQUESTCODE_WAYPOINT, intent);
                    break;
                case "add_home":
                    setResult(REQUESTCODE_ADD_HOME_ADDRESS, intent);
                    break;
                case "add_office":
                    setResult(REQUESTCODE_ADD_OFFICE_ADDRESS, intent);
                    break;
                case "add_favourites":
                    setResult(REQUESTCODE_ADD_FAVOURITE_ADDRESS, intent);
                    break;
                case "search_favourites":
                    setResult(REQUESTCODE_ADD_FAVOURITE_SEARCH, intent);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + mWayPointType);
            }
        }
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    private List<POIPredictionResults> getPOIPredictionsResults(List<SearchResult> searchResults) {
        List<POIPredictionResults> result = new ArrayList<>();
        for (SearchResult aSearchResult : searchResults) {
            POIPredictionResults aPredictionResults = new POIPredictionResults();
            StructuredFormatting structuredFormatting = new StructuredFormatting();
            String placeName = "";
            if (aSearchResult instanceof PoiSearchResult)
                placeName = ((PoiSearchResult) aSearchResult).getPoiName();
            else
                placeName = REUtils.getItemFromStringIndex(aSearchResult.getAddress().getFormattedAddress(), ",", 0);
            structuredFormatting.setMainText(placeName);
            String formattedAddress = aSearchResult.getAddress().getFormattedAddress();
            structuredFormatting.setSecondaryText(formattedAddress.substring(formattedAddress.indexOf(",") + 1).trim());
            aPredictionResults.setStructuredFormatting(structuredFormatting);
            if (aSearchResult.getDistance() != null)
                aPredictionResults.setDistanceMeters(aSearchResult.getDistance().toMeters());
            aPredictionResults.setSearchResults(aSearchResult);
            result.add(aPredictionResults);
        }
        return result;
    }

    @Override
    public void onRecentLocationItemClick(String place, com.google.android.gms.maps.model.LatLng latLng) {
        onItemFetched(place, latLng.latitude, latLng.longitude, true);
    }

    @Override
    public void onRecentRouteItemClick(ArrayList<WayPointsData> place) {
        Log.e("Route clicked", "" + place.get(1).getPlaceName());

        Bundle params = new Bundle();
        params.putString("eventCategory", "Tripper");
        params.putString("eventAction", "Recent Route Clicked");
        params.putString("eventLabel", place.get(1).getPlaceName());
        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);


        Intent intent = new Intent();
        intent.putExtra("place", place.get(1).getPlaceName());
        intent.putExtra("latitude", place.get(1).getLatitude());
        intent.putExtra("longitude", place.get(1).getLongitude());
        intent.putExtra("isRecentRoute", true);
        intent.putParcelableArrayListExtra("isRecentRouteData", place);
        setResult(REQUESTCODE_WAYPOINT, intent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            String mPLace = null;
            double latitude = 0, longitude = 0;
            switch (requestCode) {
                case REQUESTCODE_ADD_OFFICE_ADDRESS:
                    if (RecentRouteLocationManager.getAddressList(this).size() >= 2) {
                        if (RecentRouteLocationManager.getAddressList(this).get(1).getLocationCoordinates() != null && RecentRouteLocationManager.getAddressList(this).get(1).getLocationCoordinates().equals(new com.google.android.gms.maps.model.LatLng(0, 0))) {
                            officeAddressid.setText(R.string.choose_location);
                        } else if (RecentRouteLocationManager.getAddressList(this).get(1).getLocationCoordinates() != null) {
                            officeAddressid.setText(RecentRouteLocationManager.getAddressList(this).get(1).getLocationCoordinates().toString().substring(8));
                        }
                    }
                    break;
                case REQUESTCODE_ADD_HOME_ADDRESS:
                    if (RecentRouteLocationManager.getAddressList(this).size() >= 1) {
                        if (RecentRouteLocationManager.getAddressList(this).get(0).getLocationCoordinates() != null && RecentRouteLocationManager.getAddressList(this).get(0).getLocationCoordinates().equals(new com.google.android.gms.maps.model.LatLng(0, 0))) {
                            homeAddressid.setText(R.string.choose_location);
                        } else if (RecentRouteLocationManager.getAddressList(this).get(0).getLocationCoordinates() != null) {
                            homeAddressid.setText(RecentRouteLocationManager.getAddressList(this).get(0).getLocationCoordinates().toString().substring(8));
                            System.out.println("Type>>> <<<<<"+data.getStringExtra("place"));

                        }
                    }
                    break;
                case REQUESTCODE_MORE_FAVOURITE_ADDRESS:


                    if (data != null) {
                        String type = data.getStringExtra("location_type");
                        if (type != null && type.equalsIgnoreCase("search_favourites")) {
                            refreshSearchScreen();
                        } else {
                            mPLace = data.getStringExtra("place");
                            latitude = data.getDoubleExtra("latitude", 0.0);
                            longitude = data.getDoubleExtra("longitude", 0.0);
                            onItemFetched(mPLace, latitude, longitude, false);
                        }
                    }
                    break;
            }
        }
    }

}


