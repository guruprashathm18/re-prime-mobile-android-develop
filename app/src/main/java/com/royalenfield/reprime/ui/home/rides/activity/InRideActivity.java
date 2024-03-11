package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.gms.maps.model.LatLng;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.request.proxy.service.ServiceCenterRequest;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.RidersJoined;
import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;
import com.royalenfield.reprime.ui.custom.views.ServiceCenterDataModel;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.adapter.CheckInsAdapter;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.home.rides.custom.RECircularImageView;
import com.royalenfield.reprime.ui.home.rides.custom.RecyclerViewItemOffsetDecoration;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.AddWayPointFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.MapFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.planride.RecommendedRoutesFragment;
import com.royalenfield.reprime.ui.home.rides.interactor.RidesInteractor;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.home.rides.presenter.CheckInRowRowPresenter;
import com.royalenfield.reprime.ui.home.rides.presenter.RidesPresenter;
import com.royalenfield.reprime.ui.home.rides.views.InRideTitleBarView;
import com.royalenfield.reprime.ui.home.rides.views.RidesView;
import com.royalenfield.reprime.ui.home.service.adapter.ServiceCenterDataViewAdapter;
import com.royalenfield.reprime.ui.home.service.history.interactor.ServiceBookingInteractor;
import com.royalenfield.reprime.ui.home.service.history.listener.ServiceBookingListener;
import com.royalenfield.reprime.ui.home.service.history.presenter.ServiceBookingPresenter;
import com.royalenfield.reprime.ui.home.service.listener.RecyclerViewClickListener;
import com.royalenfield.reprime.ui.home.service.search.SearchActivity;
import com.royalenfield.reprime.ui.onboarding.AccountCreationSuccessActivity;
import com.royalenfield.reprime.ui.riderprofile.fragment.RiderInvitedFragment;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.RERecyclerTouchListener;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.POSITION;
import static com.royalenfield.reprime.utils.REConstants.RIDES_LIST_POSITION;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_ONGOING;
import static com.royalenfield.reprime.utils.REConstants.RIDE_WAYPOINT_TYPE;
import static com.royalenfield.reprime.utils.REConstants.WAYPOINT_INRIDE;

public class InRideActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener,
        RidesListeners.CurrentLocationListener, MapFragment.OnTouchListener,
        AddWayPointFragment.IPlanRideWayPointSearchListener,
        TitleBarView.OnCheckInIconClickListener, RecyclerViewClickListener,
        RidesView, View.OnClickListener, ServiceBookingListener {

    private ArrayList<WayPointsData> mInitialStartDestinationPoints = new ArrayList<>();
    private List<ProfileRidesResponse> mOngoingRideResponse;
    private TextView mDistanceText, mTvLabelCheckIns, mOwnerName, mOwnerLocation, mOwnerRides;
    private Button mEndRideButton;
    private FragmentFrameHolder fl_rider_invited;
    private int mSelectedPosition = 0;
    private RecyclerView mRecomServiceCenterRecyclerView, mCheckInsRecyclerView;
    private FragmentFrameHolder mRideRecommendedStop;
    private List<DealerMasterResponse> mDealersResponseList = new ArrayList<>();
    private ArrayList<ServiceCenterDataModel> mServiceCenterListViewItemList = new ArrayList<>();
    private String mRideId, mOwnerPhoneNo;
    private boolean isRideEnded = false;
    private NestedScrollView mScrollView;
    private TextView mServiceCenterHeader;
    private View mServiceCenterView, mCheckInsSeparator, mCreatedMembersSeperator;
    private boolean isFirstLoad;
    private ConstraintLayout mConstraint;
    private RECircularImageView mOwnerImage;
    private int REQUEST_CODE_EXIT_RIDE = 1;
    /**
     * This broadcast listens for the ongoing ride update from firestore
     */
    private BroadcastReceiver mOngoingRideBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && REConstants.FIRESTORE_ONGOING_RIDE.equals(intent.getAction())
                    && !isRideEnded) {
                // if ongoing ride is pushed to firestore
                if (!checkIfOngoingRideExists()) {
                    finish();
                    Intent mIntent = new Intent(getApplicationContext(), AccountCreationSuccessActivity.class);
                    mIntent.putExtra(REConstants.SUCCESS_ACTIVITY, REConstants.ENDRIDE_TYPE);
                    startActivity(mIntent);
                    overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                }
            }
        }
    };

    /**
     * Checks the current rideId is present in ongoing rides or not & returns boolean
     *
     * @return : boolean
     */
    private boolean checkIfOngoingRideExists() {
        List<ProfileRidesResponse> mOngoingRides = REUserModelStore.getInstance().getOngoingRideResponse();
        if (mOngoingRides != null && mOngoingRides.size() > 0) {
            for (ProfileRidesResponse profileRidesResponse : mOngoingRides) {
                String id = profileRidesResponse.get_id();
                if (mRideId != null && !mRideId.isEmpty() && id != null && id.equals(mRideId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inride);
        isFirstLoad = true;
        mOngoingRideResponse = REUserModelStore.getInstance().getOngoingRideResponse();
        getIntentData();
        initViews();
       // showLoading();
        bindData();
        enableTheLocation();
        LocalBroadcastManager.getInstance(this).registerReceiver(mOngoingRideBroadcastReceiver,
                new IntentFilter(REConstants.FIRESTORE_ONGOING_RIDE));
    }

    public void bindData() {
        hideLoading();
        try {
            addWayPointFragment();
            setRidersJoinedInfo();
            //This logic is for not showing the details to himself(owner)
            if (!mOngoingRideResponse.get(mSelectedPosition).getCreatedBy().
                    equals(REUtils.getUserId())) {
                bindCreatedBy();
            }
            getNearestServiceCenters();
            mEndRideButton.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            RELog.e(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e.getMessage());
        } catch (OutOfMemoryError e) {
            RELog.e(e.getMessage());
        } catch (Exception e) {
            RELog.e(e.getMessage());
        }
    }

    /**
     * API call for gettings Nearest service centers
     */
    private void getNearestServiceCenters() {
        ServiceBookingPresenter mServiceBookingPresenter = new
                ServiceBookingPresenter(this, new ServiceBookingInteractor());
        Location currentlocation = REUtils.getLocationFromModel();
        showLoading();
        mServiceBookingPresenter.getServiceCenters(new ServiceCenterRequest(String.valueOf(currentlocation.getLatitude()),
                String.valueOf(currentlocation.getLongitude()), 500));
    }

    /**
     * Binds the data for the createdBy Info
     */
    private void bindCreatedBy() {
        mCreatedMembersSeperator.setVisibility(View.VISIBLE);
        mConstraint.setVisibility(View.VISIBLE);
        REUtils.loadImageWithGlide(this, REUtils.getMobileappbaseURLs().getAssetsURL() + mOngoingRideResponse.get(mSelectedPosition).getUserInfo().
                get(REFirestoreConstants.USER_INFO_PROFILE_URL), mOwnerImage);
        mOwnerName.setText((CharSequence) mOngoingRideResponse.get(mSelectedPosition).getUserInfo().
                get(REFirestoreConstants.USER_INFO_NAME));
        mOwnerLocation.setText(String.format(getResources().getString(R.string.text_dealer_location),
                mOngoingRideResponse.get(mSelectedPosition).getUserInfo().
                        get(REFirestoreConstants.USER_INFO_CITY), ""));
        mOwnerRides.setText((CharSequence) mOngoingRideResponse.get(mSelectedPosition).getUserInfo().
                get(REFirestoreConstants.USER_INFO_RIDES_CREATED_COUNT));
        mOwnerPhoneNo = String.valueOf(mOngoingRideResponse.get(mSelectedPosition).getUserInfo().
                get(REFirestoreConstants.USER_INFO_PHONE_NO));
    }

    private void getCheckIns() {
        try {
            List<ProfileRidesResponse> mOngoingRideResponse = REUserModelStore.getInstance().getOngoingRideResponse();
            RidesPresenter mRidesPresenter = new RidesPresenter(this, new RidesInteractor());
            mRidesPresenter.getCheckIns(mOngoingRideResponse.get(mSelectedPosition).get_id());

            mTvLabelCheckIns = findViewById(R.id.tv_label_check_ins);
            mCheckInsRecyclerView = findViewById(R.id.rv_check_ins);
            mCheckInsSeparator = findViewById(R.id.check_ins_separator);

            mCheckInsRecyclerView.addOnItemTouchListener(new RERecyclerTouchListener());
            RecyclerView.LayoutManager mCheckInsLayoutManager = new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            mCheckInsRecyclerView.setLayoutManager(mCheckInsLayoutManager);
            mCheckInsRecyclerView.addItemDecoration(new RecyclerViewItemOffsetDecoration(18,
                    RecyclerViewItemOffsetDecoration.HORIZONTAL));
            ((SimpleItemAnimator) Objects.requireNonNull(mCheckInsRecyclerView.getItemAnimator())).
                    setSupportsChangeAnimations(false);

            mCheckInsRecyclerView.setVisibility(View.GONE);
            mTvLabelCheckIns.setVisibility(View.GONE);
            mCheckInsSeparator.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            RELog.e(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e.getMessage());
        } catch (OutOfMemoryError e) {
            RELog.e(e.getMessage());
        } catch (Exception e) {
            RELog.e(e.getMessage());
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        isFirstLoad = false;
        getCheckIns();
    }

    private void getIntentData() {
        if (getIntent() != null) {
            mSelectedPosition = getIntent().getIntExtra(POSITION, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mOngoingRideBroadcastReceiver);
        isRideEnded = false;
    }

    private void initViews() {
        InRideTitleBarView mTitleBarView = findViewById(R.id.titleBarView_inride);
        mTitleBarView.bindData(this, R.drawable.ic_back_up, getRouteStartEndPoint(), R.drawable.ic_add_checkin);
        mScrollView = findViewById(R.id.scroll_home_screen);
        mRideRecommendedStop = findViewById(R.id.inride_recom_stops);
        mServiceCenterHeader = findViewById(R.id.tv_ride_service_centers);
        mServiceCenterView = findViewById(R.id.tv_divider_service_centers);
        mDistanceText = findViewById(R.id.textView_distance);
        mEndRideButton = findViewById(R.id.btn_end_ride);
        fl_rider_invited = findViewById(R.id.fl_rider_invited);
        mRecomServiceCenterRecyclerView = findViewById(R.id.recommended_service_centers);
        mCheckInsRecyclerView = findViewById(R.id.rv_check_ins);
        mTvLabelCheckIns = findViewById(R.id.tv_label_check_ins);
        mCheckInsSeparator = findViewById(R.id.check_ins_separator);
        mCreatedMembersSeperator = findViewById(R.id.created_members_seperator);
        mConstraint = findViewById(R.id.createdBy);
        mOwnerImage = findViewById(R.id.iv_image);
        mOwnerName = findViewById(R.id.tv_name);
        mOwnerLocation = findViewById(R.id.tv_location);
        ImageView mCallOwnerImage = findViewById(R.id.iv_call);
        mOwnerRides = findViewById(R.id.textView_ridecount);
        mCallOwnerImage.setOnClickListener(this);
        manageEndRide();
    }

    private void manageEndRide() {
        if (mOngoingRideResponse != null && mOngoingRideResponse.size() > 0) {
            if (!mOngoingRideResponse.get(mSelectedPosition).getCreatedBy().
                    equals(REUtils.getUserId())) {
                mEndRideButton.setBackgroundResource(R.drawable.button_border_disable);
                mEndRideButton.setTextColor(getResources().getColor(R.color.white_50));
                mEndRideButton.setEnabled(false);
            }
            mRideId = mOngoingRideResponse.get(mSelectedPosition).get_id();
        }
    }


    /**
     * Populates the searched service center from the list.
     */
    private void showNearByServiceCenterView() {
        Location currentlocation = REUtils.getLocationFromModel();
        if (currentlocation != null && currentlocation.getLatitude() != 0 && currentlocation.getLongitude() != 0) {
            mServiceCenterHeader.setVisibility(View.VISIBLE);
            mRecomServiceCenterRecyclerView.setVisibility(View.VISIBLE);
            mServiceCenterView.setVisibility(View.VISIBLE);
            int iServiceType = ServiceCenterDataModel.NEAR_YOU_NO_VEHICLE_TYPE;
            if (mDealersResponseList != null && mDealersResponseList.size() > 0) {
                List<DealerMasterResponse> subDealersList;
                if (mDealersResponseList.size() > 5) {
                    subDealersList = mDealersResponseList.subList(0, 5);
                } else {
                    subDealersList = mDealersResponseList;
                }
                for (DealerMasterResponse dealersResponse : subDealersList) {
                    mServiceCenterListViewItemList.add(new ServiceCenterDataModel(iServiceType,
                            dealersResponse
                    ));
                }
            }

            ServiceCenterDataViewAdapter mServiceCenterAdapter = new ServiceCenterDataViewAdapter(this,
                    mServiceCenterListViewItemList, this, null);
            mRecomServiceCenterRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false));
            ((SimpleItemAnimator) Objects.requireNonNull(mRecomServiceCenterRecyclerView.getItemAnimator())).
                    setSupportsChangeAnimations(false);
            mRecomServiceCenterRecyclerView.setAdapter(mServiceCenterAdapter);
        }
    }

    /**
     * Gets the string for title
     *
     * @return :String
     */
    private String getRouteStartEndPoint() {
        if (mOngoingRideResponse != null && mOngoingRideResponse.size() > 0) {
            String startPoint = mOngoingRideResponse.get(mSelectedPosition).getStartPoint();
            String endPoint = mOngoingRideResponse.get(mSelectedPosition).getEndPoint();
            startPoint = REUtils.splitPlaceName(startPoint != null && !startPoint.isEmpty() ? startPoint :
                    getApplicationContext().getResources().getString(R.string.text_hyphen_na));
            endPoint = REUtils.splitPlaceName(endPoint != null && !endPoint.isEmpty() ? endPoint :
                    getApplicationContext().getResources().getString(R.string.text_hyphen_na));
            return String.format(getResources().
                            getString(R.string.ride_three_string_placeholder), startPoint,
                    getResources().getString(R.string.ride_route_title), endPoint);
        }
        return "";
    }

    /**
     * Adding map fragment
     */
    public void addMapFragment() {
        FragmentFrameHolder mInRideFrame = findViewById(R.id.fl_mapholder);
        MapFragment mMapFragment = MapFragment.newInstance(this);
        mMapFragment.setArguments(setBundleData());
        mMapFragment.setListener(this);
        mInRideFrame.loadFragment(this, mMapFragment, MapFragment.TAG_MAP_FRAGMENT);
    }

    /**
     * Adding the AddWayPointFragment
     */
    private void addWayPointFragment() {
        FragmentFrameHolder locationRideFrame = findViewById(R.id.fl_waypoint_holder);
        AddWayPointFragment mAddWayPointFragment = AddWayPointFragment.newInstance();
        mAddWayPointFragment.setArguments(setBundleData());
        locationRideFrame.loadFragment(this, mAddWayPointFragment, null);
    }

    /**
     * Sets Bundle data
     */
    private Bundle setBundleData() {
        Bundle bundle = new Bundle();
        setStartEndPoint();
        bundle.putParcelableArrayList(REConstants.WAY_POINTS_LIST, mInitialStartDestinationPoints);
        bundle.putString(RIDE_WAYPOINT_TYPE, WAYPOINT_INRIDE);
        bundle.putBoolean(REConstants.IS_LOCATION_REQUIRED, false);
        bundle.putBoolean(REConstants.IS_NAVIGATION, false);
        return bundle;
    }

    /**
     * Adding start and end points
     */
    private void setStartEndPoint() {
        try {
            mInitialStartDestinationPoints.clear();
            //Adding start and end points
            mInitialStartDestinationPoints.add(mInitialStartDestinationPoints.size(),
                    new WayPointsData(mOngoingRideResponse.get(mSelectedPosition).
                            getStartPointCoordinates().get(0), mOngoingRideResponse.get(mSelectedPosition).
                            getStartPointCoordinates().get(1), mOngoingRideResponse.get(mSelectedPosition).getStartPoint()));
            if (mOngoingRideResponse.get(mSelectedPosition).getWaypoints() != null && mOngoingRideResponse.
                    get(mSelectedPosition).getWaypoints().size() > 0) {
                mInitialStartDestinationPoints.addAll(mOngoingRideResponse.get(mSelectedPosition).getWaypoints());
            }
            mInitialStartDestinationPoints.add(mInitialStartDestinationPoints.size(),
                    new WayPointsData(mOngoingRideResponse.get(mSelectedPosition).
                            getEndPointCoordinates().get(0), mOngoingRideResponse.get(mSelectedPosition).
                            getEndPointCoordinates().get(1), mOngoingRideResponse.get(mSelectedPosition).getEndPoint()));
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    /**
     * Adding RecommendedRouteFragment.
     */
    public void addRecommendedRouteFragment() {
        if (isFirstLoad) {
            RecommendedRoutesFragment mRecommendedRouteFragment = RecommendedRoutesFragment.
                    newInstance(getResources().getString(R.string.text_recommended_stops), mSelectedPosition);
            mRideRecommendedStop.loadFragment(this, mRecommendedRouteFragment, null);
        }
    }

    /**
     * Setting riders joined details.
     */
    private void setRidersJoinedInfo() {
        ArrayList<RidersJoined> mRidersJoined = mOngoingRideResponse.get(mSelectedPosition).getRidersJoined();
        if (mRidersJoined != null && mRidersJoined.size() > 0) {
            if (mRidersJoined.size() == 1) {
                if (mRidersJoined.get(0).getUser().
                        get(REFirestoreConstants.RIDERS_JOINED_USER_ID) != null &&
                        !mRidersJoined.get(0).getUser().get(REFirestoreConstants.
                                RIDERS_JOINED_USER_ID).equals(REUtils.getUserId())) {
                    initRidersJoinedFragment();
                }
            } else if (mRidersJoined.size() > 1) {
                initRidersJoinedFragment();
            }
        }
    }

    private void initRidersJoinedFragment() {
        RiderInvitedFragment riderInvitedFragment = RiderInvitedFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt(RIDES_LIST_POSITION, mSelectedPosition);
        args.putString(RIDE_TYPE, RIDE_TYPE_ONGOING);
        riderInvitedFragment.setArguments(args);
        fl_rider_invited.loadFragment(this, riderInvitedFragment, null);
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
    public void updateCurrentLocation(LatLng latLng) {

    }

    @Override
    public void setDistance(Double iDistanceKM) {
        mDistanceText.setText(String.format(getResources().getString(R.string.ride_three_string_placeholder),
                String.valueOf(iDistanceKM), getResources().getString(R.string.ride_to_text),
                mOngoingRideResponse.get(mSelectedPosition).getEndPoint()));
    }


    @Override
    public void onTouch() {
        mScrollView.requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void updateRoute(ArrayList<WayPointsData> wayPointDataList) {
        mInitialStartDestinationPoints.clear();
        mInitialStartDestinationPoints = wayPointDataList;
        addMapFragment();
    }

    public void onExitRide(View view) {
        isRideEnded = true;
        Intent intent = new Intent(getApplicationContext(), ExitRideActivity.class);
        intent.putExtra(REConstants.RIDE_ID, mRideId);
        startActivityForResult(intent, REQUEST_CODE_EXIT_RIDE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EXIT_RIDE && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onCheckInClickListener() {
        Intent mIntent = new Intent(this, SearchActivity.class);
        mIntent.putExtra(REConstants.SEARCH_ACTIVITY_INPUT_TYPE, REConstants.SEARCH_ACTIVITY_CHECK_IN_LIST);
        mIntent.putExtra(REConstants.SEARCH_ACTIVITY_RIDE_ID, mOngoingRideResponse.get(mSelectedPosition).get_id());
        startActivity(mIntent);
    }


    @Override
    public void onItemClick(int value, boolean isSelected) {

    }

    @Override
    public void onTimeClick(int position) {

    }

    @Override
    public void onGetCheckInSuccess(CheckInResponse response) {
        mTvLabelCheckIns.setVisibility(View.VISIBLE);
        mCheckInsRecyclerView.setVisibility(View.VISIBLE);
        mCheckInsSeparator.setVisibility(View.VISIBLE);
        RecyclerView.Adapter mCheckInsAdapter = new CheckInsAdapter(getApplicationContext(), new CheckInRowRowPresenter(),
                response);
        mCheckInsRecyclerView.setAdapter(mCheckInsAdapter);
        addRecommendedRouteFragment();
    }

    @Override
    public void onGetCheckInFailure(String errorMessage) {
        mCheckInsRecyclerView.setVisibility(View.GONE);
        mTvLabelCheckIns.setVisibility(View.GONE);
        mCheckInsSeparator.setVisibility(View.GONE);
        addRecommendedRouteFragment();
    }

    @Override
    public void onPublishRideSuccess() {

    }

    @Override
    public void onPublishRideFailure(String errorMessage) {

    }

    @Override
    public void onUpdateRideStatusSuccess() {

    }

    @Override
    public void onUpdateRideStatusFailure(String errorMessage) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_call) {
            if (mOwnerPhoneNo != null && !mOwnerPhoneNo.equals("")) {
                checkAndRequestCallPermissions(getApplicationContext(), this, mOwnerPhoneNo,
                        REConstants.RIDER_INVITED_CALL_PERMISSIONS_REQUESTS, this);
            } else {
                Toast.makeText(getApplicationContext(), "Contact number unavailable", Toast.LENGTH_SHORT).show();
            }
        }
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
        hideLoading();
        mDealersResponseList = response;
        showNearByServiceCenterView();
        getCheckIns();
    }

    @Override
    public void onServiceCenterFetchFailed(String errorMessage) {
        hideLoading();
        getCheckIns();
    }

    @Override
    public void ongetPickupAndDoorstepServiceSlotsSuccess() {

    }

    @Override
    public void ongetPickupAndDoorstepServiceSlotsFailure() {

    }
}
