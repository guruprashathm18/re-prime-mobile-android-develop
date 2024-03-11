package com.royalenfield.reprime.ui.riderprofile.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.RidersJoined;
import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.home.rides.custom.RideDetailsLayout;
import com.royalenfield.reprime.ui.home.rides.interactor.RidesInteractor;
import com.royalenfield.reprime.ui.home.rides.presenter.RidesPresenter;
import com.royalenfield.reprime.ui.home.rides.views.RidesView;
import com.royalenfield.reprime.ui.onboarding.AccountCreationSuccessActivity;
import com.royalenfield.reprime.ui.riderprofile.fragment.RiderInvitedFragment;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.FIRESTORE_UPCOMING_RIDE;
import static com.royalenfield.reprime.utils.REConstants.RIDECUSTOM_VIEWTYPE;
import static com.royalenfield.reprime.utils.REConstants.RIDES_LIST_POSITION;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE;


/**
 * This activity is used to display upcoming rides from profile page
 */
public class UpcomingRideActivity extends REBaseActivity implements View.OnClickListener,
        TitleBarView.OnNavigationIconClickListener, RidesView {

    private static final int MODIFY_RIDE_INTENT = 3;
    private TitleBarView mTitleBarView;
    private String mRideTourType;
    private ImageView imageView_options, mIvUpcomingRideImage;
    private int mPosition;
    private Button mStartNow;
    private RideDetailsLayout mRideDetailLayout;
    private FragmentFrameHolder fl_rider_invited;
    private ProfileRidesResponse profileRideResponse;
    private TextView mShare;
    private View mView1StartLine;
    private View mViewShareLine;
    private TextView tvModify;
    private RidesPresenter mRidesPresenter;
    private Handler handler = new Handler();
    private Runnable runnable;
    private String mRideId, mRideType, mCreatedBy;
    private String mRidePageUrl;
    private boolean isRideStarted = false; //This boolean value is used to check whether the ride has ended or not.
    private boolean isStartOnce = true; //This boolean value is used to check whether the ride has ended or not.

    public UpcomingRideActivity() {
    }

    /**
     * This broadcast listens for the ongoing ride update from firestore
     */
    private BroadcastReceiver mOngoingRideBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && REConstants.FIRESTORE_ONGOING_RIDE.equals(intent.getAction()) && isRideStarted &&isStartOnce) {
                // if ongoing ride is pushed to firestore
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                openInRide();
            }
        }
    };

    /**
     * This broadcast listens for the upcoming ride update from firestore
     */
    private BroadcastReceiver mUpcomingRideBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getApplicationContext() != null && intent.getAction() != null &&
                    FIRESTORE_UPCOMING_RIDE.equals(intent.getAction())) {
                // if ongoing ride is pushed to firestore
                List<ProfileRidesResponse> mUpcomingRidesResponse = REUserModelStore.getInstance().getUpcomingRideResponse();
                if (mUpcomingRidesResponse != null && mUpcomingRidesResponse.size() > 0) {
                    for (ProfileRidesResponse profileRidesResponse : mUpcomingRidesResponse) {
                        String id = profileRidesResponse.get_id();
                        if (id != null && !id.isEmpty() && mRideId != null &&
                                !mRideId.isEmpty() && profileRidesResponse.get_id().equals(mRideId)) {
                            profileRideResponse = profileRidesResponse;
                            bindData();
                        }
                    }
                }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_ride);
        try {
            mRidesPresenter = new RidesPresenter(this, new RidesInteractor());
            initViews();
            LocalBroadcastManager.getInstance(this).registerReceiver(mOngoingRideBroadcastReceiver,
                    new IntentFilter(REConstants.FIRESTORE_ONGOING_RIDE));
            LocalBroadcastManager.getInstance(this).registerReceiver(mUpcomingRideBroadcastReceiver,
                    new IntentFilter(FIRESTORE_UPCOMING_RIDE));
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    private void disableStartNow() {
        mStartNow.setBackgroundResource(R.drawable.button_border_disable);
        mStartNow.setTextColor(getResources().getColor(R.color.white_50));
        mStartNow.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterOngoingReceiver();
    }

    /**
     * Unregistering receivers
     */
    private void unregisterOngoingReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mOngoingRideBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpcomingRideBroadcastReceiver);
        isRideStarted = false;
    }

    /**
     * Initializes all the views.
     */
    private void initViews() {

        getIntentData();
        mRideId = profileRideResponse.get_id();
        mRidePageUrl = profileRideResponse.getRidePageUrl();
        //Views..
        mTitleBarView = findViewById(R.id.title_bar_ride_detail);
        imageView_options = findViewById(R.id.imageView_options);
        imageView_options.setOnClickListener(this);

        mIvUpcomingRideImage = findViewById(R.id.iv_upcoming_ride);
        mStartNow = findViewById(R.id.btn_start_now);
        mStartNow.setOnClickListener(this);

        fl_rider_invited = findViewById(R.id.fl_rider_invited);

        mShare = findViewById(R.id.tv_share_ride);
        mShare.setOnClickListener(this);
        mViewShareLine = findViewById(R.id.view_line);
        mView1StartLine = findViewById(R.id.view1_start_line);

        mRideDetailLayout = findViewById(R.id.rideDetailsLayout);

        tvModify = findViewById(R.id.tv_label_modify);
        tvModify.setOnClickListener(this);

        bindData();
    }

    private void bindData() {
        try {
            bindRideDetails();
            setRidersJoinedInfo();
            checkIfShareOptionIsRequired();
            checkIfModifyOptionAvailable();
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
     * Setting riders joined details.
     */
    private void setRidersJoinedInfo() {
        ArrayList<RidersJoined> mRidersJoined = profileRideResponse.getRidersJoined();
        if (mRidersJoined != null && mRidersJoined.size() > 0) {
            if (mRidersJoined.size() == 1) {
                if (mRidersJoined.get(0).getUser().
                        get(REFirestoreConstants.RIDERS_JOINED_USER_ID) != null &&
                        !mRidersJoined.get(0).getUser().get(REFirestoreConstants.
                                RIDERS_JOINED_USER_ID).equals(REUtils.getUserId())) {
                    addRidersInvitedFragment();
                }
            } else if (mRidersJoined.size() > 1) {
                addRidersInvitedFragment();
            }
        }
    }

    /**
     * Opens splash and InRide screen on success of start ride API & firestore update
     */
    private void openInRide() {
        isStartOnce=false;
        hideLoading();
        finish();
        Intent mIntent = new Intent(getApplicationContext(), AccountCreationSuccessActivity.class);
        mIntent.putExtra(REConstants.SUCCESS_ACTIVITY, REConstants.UPCOMIMGRIDE_TYPE);
        startActivity(mIntent);
        overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);

    }

    /**
     * Binds data to Views
     */
    private void bindRideDetails() {
        mTitleBarView.bindData(this, R.drawable.back_arrow,
                profileRideResponse.getRideName());
        REUtils.loadImageWithGlide(this, REUtils.getMobileappbaseURLs().getAssetsURL() +
                profileRideResponse.getRideImages().get(0).getSrcPath(), mIvUpcomingRideImage);
        mRideDetailLayout.bindRiderProfileData(this, profileRideResponse);
    }


    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            mRideTourType = getIntent().getExtras().getString(RIDECUSTOM_VIEWTYPE);
            mPosition = getIntent().getExtras().getInt(RIDES_LIST_POSITION);
            if (REUserModelStore.getInstance().getUpcomingRideResponse() != null && REUserModelStore.getInstance().
                    getUpcomingRideResponse().size() > 0) {
                profileRideResponse = REUserModelStore.getInstance().getUpcomingRideResponse().get(mPosition);
            }
            getRideData();
        }
    }

    /**
     * Initiliasing ride data
     */
    private void getRideData() {
        if (profileRideResponse != null) {
            mRideType = profileRideResponse.getRideType();
            mCreatedBy = profileRideResponse.getCreatedBy();
        }
    }

    /**
     * Loads fragments in to fragment holders
     */
    private void addRidersInvitedFragment() {
        if (profileRideResponse.getRidersJoined() != null &&
                profileRideResponse.getRidersJoined().size() > 0) {
            mView1StartLine.setVisibility(View.VISIBLE);
            mViewShareLine.setVisibility(View.VISIBLE);
            Bundle args = new Bundle();
            RiderInvitedFragment riderInvitedFragment = RiderInvitedFragment.newInstance();
            args.putInt(RIDES_LIST_POSITION, mPosition);
            args.putString(RIDE_TYPE, mRideTourType);
            riderInvitedFragment.setArguments(args);
            fl_rider_invited.loadFragment(this, riderInvitedFragment, null);
        } else {
            //mView1StartLine.setVisibility(View.GONE);
            //mViewShareLine.setVisibility(View.GONE);
        }
    }

    /**
     * Disables share option for solo & private rides
     */
    private void checkIfShareOptionIsRequired() {
        if (mRideType != null && !mRideType.isEmpty()) {
            if (mRideType.equals(REFirestoreConstants.RIDE_TYPE_SOLO)) {
                disableShare();
                fl_rider_invited.setVisibility(View.GONE);
            } else if (mRideType.equals(REFirestoreConstants.RIDE_TYPE_PRIVATE) &&
                    !profileRideResponse.getCreatedBy().equals(REUtils.getUserId())) {
                disableShare();
                fl_rider_invited.setVisibility(View.GONE);
            }
        }
        checkIfstartNowIsRequired();
    }

    /**
     * Disables startNow if user is not the owner
     */
    private void checkIfstartNowIsRequired() {
        if (mCreatedBy != null && !mCreatedBy.isEmpty()) {
            if (!mCreatedBy.equals(REUtils.getUserId())) {
                disableStartNow();
            }
        }
        List<ProfileRidesResponse> ongoingRides = REUserModelStore.getInstance().getOngoingRideResponse();
        if (ongoingRides != null && ongoingRides.size() > 0) {
            disableStartNow();
        }
    }

    /**
     * Disables share button
     */
    private void disableShare() {
        mShare.setTextColor(getResources().getColor(R.color.white_50));
        mShare.setCompoundDrawablesWithIntrinsicBounds(null, null,
                getResources().getDrawable(R.drawable.next_arrow_grey), null);
        mShare.setOnClickListener(null);
    }

    private void checkIfModifyOptionAvailable() {
        if (mCreatedBy != null && !mCreatedBy.isEmpty()) {
            if (mCreatedBy.equals(REUtils.getUserId())) {
                if (profileRideResponse.getRidersJoined().size() > 0) {
                    tvModify.setTextColor(getResources().getColor(R.color.white_50));
                    tvModify.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().
                            getDrawable(R.drawable.next_arrow_grey), null);
                    tvModify.setOnClickListener(null);
                }
            } else {
                tvModify.setTextColor(getResources().getColor(R.color.white_50));
                tvModify.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().
                        getDrawable(R.drawable.next_arrow_grey), null);
                tvModify.setOnClickListener(null);
                imageView_options.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent mIntent;
        switch (v.getId()) {
            case R.id.tv_label_modify:
                try {
                    mIntent = new Intent(this, ModifyRidesActivity.class);
                    mIntent.putExtra(REConstants.RIDECUSTOM_VIEWTYPE, mRideTourType);
                    mIntent.putExtra(RIDES_LIST_POSITION, mPosition);
                    startActivityForResult(mIntent, MODIFY_RIDE_INTENT);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                } catch (IndexOutOfBoundsException e) {
                    RELog.e(e);
                }
                break;
            case R.id.tv_share_ride:
                if (mRidePageUrl != null && !mRidePageUrl.isEmpty()) {
                    REUtils.shareRide(this, mRidePageUrl);
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't share", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_start_now:
                //API call for update ride
                mRidesPresenter.startRide(mRideId);
                break;
            case R.id.imageView_options:
                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.PopupStyle);
                PopupMenu popup = new PopupMenu(wrapper, imageView_options);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.ride_upcoming_popup, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(item -> true);
                popup.show();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MODIFY_RIDE_INTENT && resultCode == RESULT_OK) {
            finish();
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
    public void onGetCheckInSuccess(CheckInResponse response) {

    }

    @Override
    public void onGetCheckInFailure(String errorMessage) {

    }

    @Override
    public void onPublishRideSuccess() {

    }

    @Override
    public void onPublishRideFailure(String errorMessage) {

    }

    @Override
    public void onUpdateRideStatusSuccess() {
        // Navigating back to home screen since there is no update from firestore ongoing rides
        handler = new Handler();
        runnable = () -> {
            hideLoading();
            unregisterOngoingReceiver();
            startActivity(new Intent(getApplicationContext(), REHomeActivity.class));
            overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
            finish();
        };
        // Waiting for the firestore update for 20seconds
        handler.postDelayed(runnable, 20000);
        isRideStarted = true;
    }

    @Override
    public void onUpdateRideStatusFailure(String errorMessage) {
        REUtils.showErrorDialog(this, errorMessage);
    }
}
