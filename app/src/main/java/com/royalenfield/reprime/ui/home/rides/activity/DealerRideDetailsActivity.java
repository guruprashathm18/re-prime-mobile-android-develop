package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.DealerUpcomingRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.UserUpcomingRidesResponse;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.asynctask.RideJoinedAsyncTask;
import com.royalenfield.reprime.ui.home.rides.asynctask.RidesAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.rides.custom.RECircularImageView;
import com.royalenfield.reprime.ui.home.rides.custom.RideDetailsLayout;
import com.royalenfield.reprime.ui.home.rides.interactor.JoinRideInteractor;
import com.royalenfield.reprime.ui.home.rides.presenter.JoinRidePresenter;
import com.royalenfield.reprime.ui.home.rides.views.JoinRideView;
import com.royalenfield.reprime.ui.onboarding.AccountCreationSuccessActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

/**
 * @author BOP1KOR on 3/29/2019.
 * <p>
 * Activity holds the all details about the Dealer/User ride and has option to join a ride.
 */
public class DealerRideDetailsActivity extends REBaseActivity implements View.OnClickListener, JoinRideView,
        RidesAsyncTaskListeners.RidesAsyncTaskCompleteListener {
    private static final String TAG = DealerRideDetailsActivity.class.getSimpleName();

    //Views..
    private TitleBarView mTitleBarView;
    private ImageView mDealerRideCoverImage;
    private Button mJoinRide, mJoinRide2;
    private ImageView mDealerImage, mUserCall;
    private RECircularImageView mDealerRoundImage;
    private RideDetailsLayout mRideDetailLayout;
    private TextView mDealerName, mDealerLocation, mRideLocationAddress, mTvCreatorNoOfRides, mTvRides, mShareRide;
    private View mView;
    //Position..
    private int mPosition;
    //Type
    private String mType;
    private String mPhoneNumber;

    private String rideType, rideId, mUserPhoneNo;

    private List<DealerUpcomingRidesResponse> mDealerUpcomingRidesList;
    private List<UserUpcomingRidesResponse> mUserUpcomingRidesList;
    private String mRideType;
    private final List<UserUpcomingRidesResponse> userUpcomingRidesResponseFiltered= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(R.layout.activity_dealer_ride_details);
        try {
            mDealerUpcomingRidesList = RERidesModelStore.getInstance().getDealerUpcomingRidesResponse();
            setFilteredArraListForUserUpcomingRides();
            initViews();
            new RideJoinedAsyncTask(mPosition, mType, mDealerUpcomingRidesList, userUpcomingRidesResponseFiltered, this).
                    execute();
            bindDataToViews();
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    private void setFilteredArraListForUserUpcomingRides() {
        userUpcomingRidesResponseFiltered.clear();
        mUserUpcomingRidesList = RERidesModelStore.getInstance().getUserUpcomingRidesResponse();
        for (int i=0;i<mUserUpcomingRidesList.size();i++) {
            if (!mUserUpcomingRidesList.get(i).getCreatedBy().equals(REUtils.getUserId())) {
                userUpcomingRidesResponseFiltered.add(mUserUpcomingRidesList.get(i));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getIntentData() {
        if (getIntent() != null) {
            mPosition = getIntent().getIntExtra(REConstants.POSITION, -1);
            mType = getIntent().getStringExtra(REConstants.DEALER_RIDES_TYPE);
        }
    }

    /**
     * Initializes all the views.
     */
    private void initViews() {
        mDealerRideCoverImage = findViewById(R.id.iv_ride_detail);
        mDealerRoundImage = findViewById(R.id.iv_dealer_image);
        mDealerImage = findViewById(R.id.iv_dealer_location);
        mUserCall = findViewById(R.id.iv_dealer_whatsapp);
        mDealerName = findViewById(R.id.tv_dealer_name);
        mDealerLocation = findViewById(R.id.tv_dealer_location);
        mTvCreatorNoOfRides = findViewById(R.id.creator_no_of_rides);
        mShareRide = findViewById(R.id.tv_share_ride);
        mShareRide.setOnClickListener(this);
        mTvRides = findViewById(R.id.tv_rides);
        mRideLocationAddress = findViewById(R.id.tv_ride_location_address);
        mRideDetailLayout = findViewById(R.id.rideDetailsLayout);
        mView = findViewById(R.id.view_line);
        ImageView mNavCloseButton = findViewById(R.id.iv_navigation);
        mNavCloseButton.setOnClickListener(this);
        mTitleBarView = findViewById(R.id.title_bar_ride_detail);
        mJoinRide = findViewById(R.id.button_join_ride);
        mJoinRide2 = findViewById(R.id.button_join_ride_2);
        mJoinRide.setOnClickListener(this);
        mJoinRide2.setOnClickListener(this);

    }

    /**
     * Binds data to Views
     */
    private void bindDataToViews() {
        //TODO Later will send the selected dealer ride name for the detail screen.
        //Dealer Ride list details..
        setDataBasedOnRideType();
    }


    /**
     * Sets the data base on type sent from previous activity
     */
    private void setDataBasedOnRideType() {
        if (mType.equals(REConstants.TYPE_JOIN_DEALER_RIDE)) {
            rideType = REConstants.DEALER_JOIN_RIDE_TYPE;
            rideId = mDealerUpcomingRidesList.get(mPosition).get_id();
            setDataForJoinDealerRide();
        } else if (mType.equals(REConstants.TYPE_USER_CREATED_RIDE_DETAILS)) {
            rideType = REConstants.USER_JOIN_RIDE_TYPE;
            rideId = userUpcomingRidesResponseFiltered.get(mPosition).get_id();
            setDataForUserRide();
        } else {
            mJoinRide2.setText(R.string.text_button_unjoin_ride);
            mTitleBarView.bindData(this, R.drawable.ic_back_arrow, mDealerUpcomingRidesList.
                    get(mPosition).getRideName());
            mDealerName.setText(mDealerUpcomingRidesList.get(mPosition).getDealerName());
            mDealerImage.setVisibility(View.GONE);
            mRideLocationAddress.setVisibility(View.GONE);
            mView.setVisibility(View.GONE);
            mJoinRide.setVisibility(View.GONE);
        }

    }

    private void setDataForUserRide() {
        mTitleBarView.bindData(this, R.drawable.ic_back_arrow,
                userUpcomingRidesResponseFiltered.get(mPosition).getRideName());
        REUtils.loadImageWithGlide(this, REUtils.getMobileappbaseURLs().getAssetsURL() +
                userUpcomingRidesResponseFiltered.get(mPosition).
                        getRideImages().get(0).get(REFirestoreConstants.RIDE_IMAGE_PATH), mDealerRideCoverImage);
        loadCreatorImage();
        mUserCall.setVisibility(View.VISIBLE);
        mUserCall.setOnClickListener(this);
        mDealerName.setText((CharSequence) userUpcomingRidesResponseFiltered.get(mPosition).getUserInfo().
                get(REFirestoreConstants.USER_INFO_NAME));
        mUserPhoneNo = String.valueOf(userUpcomingRidesResponseFiltered.get(mPosition).getUserInfo().
                get(REFirestoreConstants.USER_INFO_PHONE_NO));
        if (userUpcomingRidesResponseFiltered.get(mPosition).getUserInfo().get(REFirestoreConstants.USER_INFO_CITY) != null) {
            mDealerLocation.setText(String.format(getResources().getString(R.string.ride_two_string_placeholder),
                    userUpcomingRidesResponseFiltered.get(mPosition).getUserInfo().
                            get(REFirestoreConstants.USER_INFO_CITY), getResources().getString(R.string.ride_pipe)));
        } else {
            mDealerLocation.setText(String.format(getResources().getString(R.string.ride_two_string_placeholder),
                    getApplicationContext().getResources().getString(R.string.text_hyphen_na),
                    getResources().getString(R.string.ride_pipe)));
        }
        mShareRide.setVisibility(View.VISIBLE);
        mRideType = userUpcomingRidesResponseFiltered.get(mPosition).getRideType();
        checkIfShareOptionIsRequired();
        mTvCreatorNoOfRides.setVisibility(View.VISIBLE);
        mTvCreatorNoOfRides.setText((CharSequence) userUpcomingRidesResponseFiltered.get(mPosition).getUserInfo().
                get(REFirestoreConstants.USER_INFO_RIDES_CREATED_COUNT));
        mPhoneNumber = (String) userUpcomingRidesResponseFiltered.get(mPosition).getUserInfo().
                get(REFirestoreConstants.USER_INFO_PHONE_NO);


        mTvRides.setVisibility(View.VISIBLE);
        mRideLocationAddress.setVisibility(View.GONE);
        mDealerImage.setVisibility(View.GONE);
        mView.setVisibility(View.GONE);
        mJoinRide.setText(R.string.text_button_join_ride);
        mRideDetailLayout.bindUserUpcomingRideDetailsData(this, userUpcomingRidesResponseFiltered.get(mPosition));
    }

    /**
     * Disables share option for solo & private rides
     */
    private void checkIfShareOptionIsRequired() {
        if (mRideType != null && !mRideType.isEmpty()) {
            if (mRideType.equals(REFirestoreConstants.RIDE_TYPE_SOLO)) {
                disableShare();
            } else if (mRideType.equals(REFirestoreConstants.RIDE_TYPE_PRIVATE) &&
                    !userUpcomingRidesResponseFiltered.get(mPosition).getCreatedBy().
                            equals(REUtils.getUserId())) {
                disableShare();
            }
        }
    }

    /**
     * Disables share button
     */
    private void disableShare() {
        mShareRide.setTextColor(getResources().getColor(R.color.white_50));
        mShareRide.setCompoundDrawablesWithIntrinsicBounds(null, null,
                getResources().getDrawable(R.drawable.next_arrow_grey), null);
        mShareRide.setOnClickListener(null);
    }

    private void setDataForJoinDealerRide() {
        mUserCall.setVisibility(View.GONE);
        mJoinRide.setText(R.string.text_button_join_ride);
        mJoinRide2.setText(R.string.text_button_join_ride);
        REUtils.loadImageWithGlide(this, REUtils.getMobileappbaseURLs().getAssetsURL() +
                mDealerUpcomingRidesList.get(mPosition).
                        getRideImages().get(0).get(REFirestoreConstants.RIDE_IMAGE_PATH), mDealerRideCoverImage);
        mTitleBarView.bindData(this, R.drawable.icon_close, mDealerUpcomingRidesList.
                get(mPosition).getRideName());
        String dealerName = mDealerUpcomingRidesList.get(mPosition).getDealerName();
        mDealerName.setText(!dealerName.isEmpty() ? dealerName : getApplicationContext().getResources().getString(R.string.text_hyphen_na));
        mTitleBarView.bindData(this, R.drawable.icon_close, mDealerUpcomingRidesList.
                get(mPosition).getRideName());
        if (mDealerUpcomingRidesList.get(mPosition).getDealerRideInfo() != null) {
            if (mDealerUpcomingRidesList.get(mPosition).getDealerRideInfo().
                    get(REFirestoreConstants.USER_INFO_CITY) == null) {
                mDealerLocation.setText(String.format(getResources().getString(R.string.text_dealer_location),
                        getApplicationContext().getResources().getString(R.string.text_hyphen_na),
                        mDealerUpcomingRidesList.get(mPosition).getDealerRideInfo().
                                get(REFirestoreConstants.USER_INFO_PHONE_NO)));
            } else {
                mDealerLocation.setText(String.format(getResources().getString(R.string.text_dealer_location)
                        , mDealerUpcomingRidesList.get(mPosition).getDealerRideInfo().
                                get(REFirestoreConstants.USER_INFO_CITY),
                        mDealerUpcomingRidesList.get(mPosition).getDealerRideInfo().
                                get(REFirestoreConstants.USER_INFO_PHONE_NO)));
            }
            String address = String.valueOf(mDealerUpcomingRidesList.get(mPosition).
                    getDealerRideInfo().get(REFirestoreConstants.DEALER_ADDRESS));
            mRideLocationAddress.setText(!address.isEmpty() ? address : getApplicationContext().getResources().getString(R.string.text_hyphen_na));
        } else {
            mDealerLocation.setText(String.format(getResources().getString(R.string.text_dealer_location),
                    getApplicationContext().getResources().getString(R.string.text_hyphen_na),
                    getResources().getString(R.string.text_hyphen_na)));
            mRideLocationAddress.setText(getApplicationContext().getResources().getString(R.string.text_hyphen_na));
        }
        mJoinRide.setText(R.string.text_button_join_ride);
        mRideDetailLayout.bindDealerRideDetailsData(this, mDealerUpcomingRidesList.get(mPosition));
        loadCreatorImage();
    }

    private void loadCreatorImage() {
        String creatorImageUrl = null;
        if (mType.equals(REConstants.TYPE_JOIN_DEALER_RIDE)) {
            if (mDealerUpcomingRidesList.get(mPosition).getDealerRideInfo() != null) {
                creatorImageUrl = String.valueOf(mDealerUpcomingRidesList.get(mPosition).
                        getDealerRideInfo().get(REFirestoreConstants.DEALER_IMAGE));
            }
        } else if (mType.equals(REConstants.TYPE_USER_CREATED_RIDE_DETAILS)) {
            creatorImageUrl = (String) userUpcomingRidesResponseFiltered.get(mPosition).getUserInfo().
                    get(REFirestoreConstants.USER_INFO_PROFILE_URL);
        }
        if (creatorImageUrl != null) {
            REUtils.loadImageWithGlide(this, REUtils.getMobileappbaseURLs().getAssetsURL() +
                    creatorImageUrl, mDealerRoundImage);
        } else {
            mDealerRoundImage.setImageDrawable(getResources().getDrawable(R.drawable.no_image_found));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_navigation:
                hideKeyboard(this);
                finish();
                if (mType.equals(REConstants.TYPE_USER_CREATED_RIDE_DETAILS)) {
                    overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
                } else {
                    overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                }
                break;
            case R.id.button_join_ride:
            case R.id.button_join_ride_2:
                joinRide();
                break;
            case R.id.tv_share_ride:
                String mRidePageUrl = userUpcomingRidesResponseFiltered.get(mPosition).getRidePageUrl();
                if (mRidePageUrl != null && !mRidePageUrl.isEmpty()) {
                    REUtils.shareRide(this, mRidePageUrl);
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't share", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_dealer_whatsapp:
                if (mUserPhoneNo != null && !mUserPhoneNo.equals("")) {
                    checkAndRequestCallPermissions(getApplicationContext(), this, mUserPhoneNo,
                            REConstants.RIDER_INVITED_CALL_PERMISSIONS_REQUESTS, this);
                } else {
                    Toast.makeText(getApplicationContext(), "Contact number unavailable", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void joinRide() {
        JoinRidePresenter mJoinRidePresenter = new JoinRidePresenter(this, new JoinRideInteractor());
        showLoading();
        mJoinRidePresenter.joinRide(rideType, rideId);
    }

    @Override
    public void onBackPressed() {
        finish();
        if (mType.equals(REConstants.TYPE_USER_CREATED_RIDE_DETAILS)) {
            overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
        }
    }

    @Override
    public void onJoinRideSuccess() {
        hideLoading();
        Intent intent = null;
        if (mType.equals(REConstants.TYPE_JOIN_DEALER_RIDE)) {
            intent = new Intent(this, JoinRideStatusActivity.class);
            intent.putExtra(REConstants.TEXT_SPLASH_DESC_SUCCESS, getApplicationContext().getResources().getString(R.string.text_adding_to_your_rides));
        } else if (mType.equals(REConstants.TYPE_USER_CREATED_RIDE_DETAILS)) {
            intent = new Intent(this, AccountCreationSuccessActivity.class);
            intent.putExtra(REConstants.SUCCESS_ACTIVITY, REConstants.USER_CREATED_JOIN_RIDE);
            intent.putExtra(REConstants.CREATOR_PHONE_NO, mPhoneNumber);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
        finish();
    }

    @Override
    public void onJoinRideFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialog(this, errorMessage);
    }

    @Override
    public void onRidesDistanceCalcComplete(Object result) {

    }

    @Override
    public void onRideJoinedComplete() {
        mJoinRide.setEnabled(false);
        mJoinRide.setBackgroundResource(R.drawable.button_border_disable);
        mJoinRide.setTextColor(getResources().getColor(R.color.white_50));
    }

}
