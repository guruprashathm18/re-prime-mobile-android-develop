package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.firestore.ourworld.EventsResponse;
import com.royalenfield.reprime.models.response.firestore.rides.MarqueeRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.PopularRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.home.rides.custom.RideDetailsLayout;
import com.royalenfield.reprime.ui.home.rides.fragment.ridecategory.CategoryRideFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.ridestour.ItineraryFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.ridestour.RidesTourKeyPlacesFragment;
import com.royalenfield.reprime.ui.riderprofile.activity.SettingsActivity;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.RIDES_LIST_POSITION;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_MARQUEE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_POPULAR;
import static com.royalenfield.reprime.utils.REConstants.SETTING_ACTIVITY_INPUT_TYPE;
import static com.royalenfield.reprime.utils.REConstants.SETTING_ACTIVITY_POLICIES;
import static com.royalenfield.reprime.utils.REFirestoreConstants.POPULAR_RIDES_KEY_PLACE_NAME;

/**
 * This activity is used to display the details of popular & marquee rides
 */
public class RidesTourActivity extends REBaseActivity implements View.OnClickListener,
        TitleBarView.OnNavigationIconClickListener {

    private ImageView iv_coastal_ride;
    private FragmentFrameHolder fl_key_places;
    private RideDetailsLayout mRideDetailLayout;
    private FragmentFrameHolder f2_itinerary;
    private FragmentFrameHolder f3_gallery;
    private TextView text_ride_info, mTvRideGallery;
    private String mRideTourType, mRidesTitle;
    private List<PopularRidesResponse> mPopularRidesResponse;
    private List<MarqueeRidesResponse> mMarqueeRidesResponse;
    private EventsResponse mEventsResponse;
    private TextView tv_label_registration_policies;
    //Position..
    private int mPosition;
    private ArrayList<Map<String, Object>> mWaypoints = new ArrayList<>();
    private ArrayList<Map<String, Object>> mItinerary = new ArrayList<>();
    private ArrayList<Map<String, String>> mGallery = new ArrayList<>();
    private ArrayList<Map<String, String>> mPolicyUrls = new ArrayList<>();
    private String mLightWeightURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides_tour);
        try {
            initViews();
            bindDataToViews();
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Initializes all the views.
     */
    private void initViews() {

        getIntentData();
        getData();
        //Views..
        TitleBarView mTitleBarView = findViewById(R.id.title_bar_ride_detail);
        mTitleBarView.bindData(this, R.drawable.icon_close, mRidesTitle);

        tv_label_registration_policies = findViewById(R.id.tv_label_registration_policies);
        tv_label_registration_policies.setOnClickListener(this);

        iv_coastal_ride = findViewById(R.id.iv_coastal_ride);
        mTvRideGallery = findViewById(R.id.textView_ride_gallery);
        fl_key_places = findViewById(R.id.fl_key_places);
        f2_itinerary = findViewById(R.id.f2_itinerary);
        f3_gallery = findViewById(R.id.f3_gallery);
        mRideDetailLayout = findViewById(R.id.rideDetailsLayout);

        text_ride_info = findViewById(R.id.text_ride_info);
        Button btn_register = findViewById(R.id.btn_register);
        Button btn_register_now = findViewById(R.id.btn_register_now);

        btn_register.setOnClickListener(this);
        btn_register_now.setOnClickListener(this);

        loadFragments();
        checkPolicyURL();
    }

    /**
     * Gets data base on type selected
     */
    private void getData() {
        if (mRideTourType.equals(RIDE_TYPE_POPULAR)) {
            mPopularRidesResponse = RERidesModelStore.getInstance().getPopularRidesResponse();
            mRidesTitle = mPopularRidesResponse.get(mPosition).getTitle();
            mWaypoints = mPopularRidesResponse.get(mPosition).getWaypoints();
            mItinerary = mPopularRidesResponse.get(mPosition).getItinerary();
            mGallery = mPopularRidesResponse.get(mPosition).getGallery();
            mPolicyUrls = mPopularRidesResponse.get(mPosition).getPolicyUrls();
            mLightWeightURL = mPopularRidesResponse.get(mPosition).getLightWeightPageUrl();
        } else if (mRideTourType.equals(RIDE_TYPE_MARQUEE)) {
            mMarqueeRidesResponse = RERidesModelStore.getInstance().getMarqueeRidesResponse();
            mRidesTitle = mMarqueeRidesResponse.get(mPosition).getTitle();
            mWaypoints = mMarqueeRidesResponse.get(mPosition).getWaypoints();
            mItinerary = mMarqueeRidesResponse.get(mPosition).getItinerary();
            mGallery = mMarqueeRidesResponse.get(mPosition).getGallery();
            mPolicyUrls = mMarqueeRidesResponse.get(mPosition).getPolicyUrls();
            mLightWeightURL = mMarqueeRidesResponse.get(mPosition).getLightWeightPageUrl();
        } else {
            mEventsResponse = REUtils.mEventsResponse;
            mRidesTitle = mEventsResponse.getTitle();
            mItinerary = mEventsResponse.getItinerary();
            mGallery = mEventsResponse.getGallery();
            mPolicyUrls = mEventsResponse.getPolicyUrls();
            mLightWeightURL = mEventsResponse.getLightWeightPageUrl();
        }
    }

    /**
     * Gest the intent data
     */
    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            mRideTourType = getIntent().getExtras().getString(RIDE_TYPE);
            mPosition = getIntent().getExtras().getInt(RIDES_LIST_POSITION);
        }
    }

    /**
     * Enable/Disable the text based on policy url
     */
    private void checkPolicyURL() {
        if (mPolicyUrls != null && mPolicyUrls.size() == 0) {
            tv_label_registration_policies.setEnabled(false);
            tv_label_registration_policies.setTextColor(getResources().getColor(R.color.white_50));
            tv_label_registration_policies.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources()
                    .getDrawable(R.drawable.next_arrow_grey), null);
        }
    }

    /**
     * Binds data to Views
     */
    private void bindDataToViews() {
        //Dealer Ride list details..
        if (mRideTourType.equals(RIDE_TYPE_POPULAR)) {
            text_ride_info.setText(mPopularRidesResponse.get(mPosition).getDescription());
            mRideDetailLayout.bindPopularAndMarqueeRideDetailsData(mPosition, mRideTourType);
            REUtils.loadImageWithGlide(this, REUtils.getMobileappbaseURLs().getAssetsURL() +
                    mPopularRidesResponse.get(mPosition).getThumbnailImagePath(), iv_coastal_ride);
        } else if (mRideTourType.equals(RIDE_TYPE_MARQUEE)) {
            text_ride_info.setText(mMarqueeRidesResponse.get(mPosition).getDescription());
            mRideDetailLayout.bindPopularAndMarqueeRideDetailsData(mPosition, mRideTourType);
            REUtils.loadImageWithGlide(this, REUtils.getMobileappbaseURLs().getAssetsURL() +
                    mMarqueeRidesResponse.get(mPosition).getThumbnailImagePath(), iv_coastal_ride);
        } else {
            REUtils.loadImageWithGlide(this, REUtils.getMobileappbaseURLs().getAssetsURL() +
                    mEventsResponse.getThumbnailImageSrc(), iv_coastal_ride);
            text_ride_info.setText(mEventsResponse.getDescription());
            mRideDetailLayout.bindEventDetailsData(mPosition, mRideTourType);
            mTvRideGallery.setText(getApplicationContext().getResources().getString(R.string.text_pics_from_previous_year));
        }

    }


    /**
     * Loads fragments in to fragment holders
     */
    private void loadFragments() {
        Bundle args = new Bundle();
        if (mWaypoints != null && mWaypoints.size() > 0 && isWaypointsData()) {
            RidesTourKeyPlacesFragment ridesTourKeyPlacesFragment = RidesTourKeyPlacesFragment.newInstance();
            args.putInt(RIDES_LIST_POSITION, mPosition);
            args.putString(RIDE_TYPE, mRideTourType);
            args.putString("RIDE_TITLE", mRidesTitle);
            ridesTourKeyPlacesFragment.setArguments(args);
            fl_key_places.loadFragment(this, ridesTourKeyPlacesFragment, null);
        }

        if (mItinerary != null && mItinerary.size() > 0) {
            ItineraryFragment mItineraryFragment = ItineraryFragment.newInstance();
            args.putInt(RIDES_LIST_POSITION, mPosition);
            args.putString(RIDE_TYPE, mRideTourType);
            mItineraryFragment.setArguments(args);
            f2_itinerary.loadFragment(this, mItineraryFragment, null);
        }

        if (mGallery != null && mGallery.size() > 0) {
            //Initializing MarqueeRidesList
            CategoryRideFragment mCategoryRideFragment = new CategoryRideFragment();
            args.putString(RIDE_TYPE, mRideTourType);
            args.putInt(RIDES_LIST_POSITION, mPosition);
            mCategoryRideFragment.setArguments(args);
            f3_gallery.loadFragment(this, mCategoryRideFragment, null);
        }
    }

    private boolean isWaypointsData() {
        String mWaypointsName="";
        if (mWaypoints != null && mWaypoints.size() > 0) {
            mWaypointsName = String.valueOf(mWaypoints.get(0).get(POPULAR_RIDES_KEY_PLACE_NAME));
        }
        return !mWaypointsName.equals("null") && !mWaypointsName.isEmpty();
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_register:
                if (mLightWeightURL != null && !mLightWeightURL.isEmpty()) {
                    intent = new Intent(this, RidesLightWeightWebActivity.class);
                    intent.putExtra(RIDE_TYPE, mRideTourType);
                    intent.putExtra(RIDES_LIST_POSITION, mPosition);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                }
                break;
            case R.id.tv_label_registration_policies:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, mRideTourType);
                intent.putExtra(SETTING_ACTIVITY_POLICIES, true);
                intent.putExtra(RIDES_LIST_POSITION, mPosition);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                break;

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


}
