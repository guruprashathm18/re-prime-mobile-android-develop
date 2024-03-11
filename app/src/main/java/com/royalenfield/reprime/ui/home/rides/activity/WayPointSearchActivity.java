package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.adapter.SearchPlacesListAdapter;
import com.royalenfield.reprime.ui.home.rides.interactor.GoogleMapAPIInteractor;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.home.rides.presenter.SearchPlaceRowPresenter;
import com.royalenfield.reprime.ui.onboarding.address.activity.AddAddressActivity;

import java.util.List;

import static android.view.View.GONE;
import static com.royalenfield.reprime.utils.REConstants.NAVIGATION_SEARCH_ACTIVITY;

/**
 * This activity is used for searching places
 */
public class WayPointSearchActivity extends REBaseActivity implements TextWatcher,
        SearchPlacesListAdapter.OnItemClickListener, TitleBarView.OnNavigationIconClickListener,
        RidesListeners.OnPlacesCallFinishedListener, View.OnClickListener {

    public static final int REQUESTCODE_WAYPOINT = 1;
    public static final int REQUESTCODE_DESTINATION_WAYPOINT = 2;
    public static final int REQUESTCODE_SOURCE_WAYPOINT = 3;
    private static final int REQUEST_CODE_ADDRESS = 1;
    private RecyclerView mSearchRecyclerView;
    private SearchPlaceRowPresenter mSearchPlacesRowPresenter;
    private String mWayPointType, mSourcePoint;
    private ConstraintLayout mAddressLayout;
    private ImageView imgMarkerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_waypoint);
        initViews();
    }

    /**
     * Initializes the views.
     */
    private void initViews() {
        Intent intent = getIntent();
        mWayPointType = intent.getStringExtra("waypoint_type");
        mSourcePoint = intent.getStringExtra("source_start");
        mSearchPlacesRowPresenter = new SearchPlaceRowPresenter();
        TitleBarView mTitleBarView = findViewById(R.id.plan_ride_header);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.label_create_ride));
        imgMarkerView = findViewById(R.id.marker_info);
        if (mWayPointType.equalsIgnoreCase(("destination"))) {
            mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.navigation_enter_destination));
            imgMarkerView.setImageResource(R.drawable.ic_marker_red);
        } else if (mWayPointType.equalsIgnoreCase(("waypoint"))) {
            mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.label_add_waypoint));
            imgMarkerView.setImageResource(R.drawable.ic_marker_yellow);
        } else if (mWayPointType.equalsIgnoreCase(("source"))) {
            mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.label_add_source));
            imgMarkerView.setImageResource(R.drawable.ic_marker_green);
        } else {
            mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.label_add_destination));
            imgMarkerView.setImageResource(R.drawable.ic_marker_green);
        }
        EditText mSearchEditText = findViewById(R.id.et_waypoint_search);
        mSearchEditText.addTextChangedListener(this);
        mAddressLayout = findViewById(R.id.address_layout);
        RelativeLayout mHomeLayout = findViewById(R.id.home_layout);
        mHomeLayout.setOnClickListener(this);

        mSearchRecyclerView = findViewById(R.id.search_recyclerview);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false));
        ((SimpleItemAnimator) mSearchRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        manageViews();
    }

    private void manageViews() {
        if (mSourcePoint != null && mSourcePoint.equalsIgnoreCase(NAVIGATION_SEARCH_ACTIVITY)) {
            mAddressLayout.setVisibility(View.VISIBLE);
        } else {
            mAddressLayout.setVisibility(View.GONE);
        }
    }

    /**
     * This sets the places results to adapter
     *
     * @param searchResults object of POIResults
     */
    public void setResults(List<POIResults> searchResults) {
        SearchPlacesListAdapter mAdapter = new SearchPlacesListAdapter(searchResults, this, mSearchPlacesRowPresenter);
        mSearchRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0 && s.length() >= 3) {
            new GoogleMapAPIInteractor().getPlaces(s.toString(), this);
        }
    }

    @Override
    public void onItemClick(String place, double latitude, double longitude) {
        hideKeyboard();
        setAddressResult(place, latitude, longitude);
    }

    private void setAddressResult(String place, double latitude, double longitude) {
        Intent intent = new Intent();
        intent.putExtra("place", place);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
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
            }
        }
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onNavigationIconClick() {
        hideKeyboard();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyboard();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nav_add_address) {
            Intent addAddressIntent = new Intent(this, AddAddressActivity.class);
            addAddressIntent.putExtra("IsEditProfileOrIsRidesAndService", true);
            startActivityForResult(addAddressIntent, REQUEST_CODE_ADDRESS);
            overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADDRESS && data != null) {
            manageViews();
        }
    }

    @Override
    public void onPlaceSuccess(List<POIResults> poiResults) {
        setResults(poiResults);
    }

    @Override
    public void onPlaceFailure(String error) {
    }
}


