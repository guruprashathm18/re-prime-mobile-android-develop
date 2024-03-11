package com.royalenfield.reprime.ui.riderprofile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.RidersJoined;
import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.adapter.CheckInsAdapter;
import com.royalenfield.reprime.ui.home.rides.custom.RECircularImageView;
import com.royalenfield.reprime.ui.home.rides.custom.RecyclerViewItemOffsetDecoration;
import com.royalenfield.reprime.ui.home.rides.custom.RideDetailsLayout;
import com.royalenfield.reprime.ui.home.rides.interactor.RidesInteractor;
import com.royalenfield.reprime.ui.home.rides.presenter.CheckInRowRowPresenter;
import com.royalenfield.reprime.ui.home.rides.presenter.RidesPresenter;
import com.royalenfield.reprime.ui.home.rides.views.RidesView;
import com.royalenfield.reprime.ui.onboarding.AccountCreationSuccessActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.RERecyclerTouchListener;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

/*
 * It contains the details of the rides of the user.(Pending, Past and Rejected Rides)
 * */
public class RideDetailsActivity extends REBaseActivity implements
        TitleBarView.OnNavigationIconClickListener, View.OnClickListener, RidesView {

    private final int MODIFY_REQUEST_CODE = 1;
    private String mRideTourType;
    private int mRidePosition;
    private TitleBarView mTitleBarView;
    private TextView mTvLabelModifyRideDetails;
    private View mModifyLine;
    private View mRidersJoinedLayout;
    private TextView mTvLabelCheckIns;
    private RecyclerView mCheckInsRecyclerView;
    private View mCheckInsSeparator;
    private TextView mTvLabelInspireOtherRiders;
    private Button mPublishRideButton;
    private ImageView mIvRideImage;
    private RECircularImageView mCircularImageView1, mCircularImageView2, mCircularImageView3;
    private TextView mTvRideMemberNames;
    private Guideline mImageGuideline;

    private ProfileRidesResponse profileRideResponse;
    private RidesPresenter mRidesPresenter;
    private String mRideId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);
        try {
            mRidesPresenter = new RidesPresenter(this, new RidesInteractor());
            //initialising the views
            initViews();
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    /**
     * Initialising views
     */
    private void initViews() {
        getIntentData();
        mTitleBarView = findViewById(R.id.title_bar_ride_detail);
        mIvRideImage = findViewById(R.id.iv_ride_detail_image);
        mTvLabelModifyRideDetails = findViewById(R.id.tv_label_modify_ride_details);
        mTvLabelModifyRideDetails.setOnClickListener(this);
        mModifyLine = findViewById(R.id.view1_modify_line);
        mRidersJoinedLayout = findViewById(R.id.layout_view_riders_joined);
        mTvLabelCheckIns = findViewById(R.id.tv_label_check_ins);
        mCheckInsRecyclerView = findViewById(R.id.rv_check_ins);
        mCheckInsRecyclerView.addOnItemTouchListener(new RERecyclerTouchListener());
        RecyclerView.LayoutManager mCheckInsLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mCheckInsRecyclerView.setLayoutManager(mCheckInsLayoutManager);
        mCheckInsRecyclerView.addItemDecoration(new RecyclerViewItemOffsetDecoration(18,
                RecyclerViewItemOffsetDecoration.HORIZONTAL));
        ((SimpleItemAnimator) Objects.requireNonNull(mCheckInsRecyclerView.getItemAnimator())).
                setSupportsChangeAnimations(false);

        mCheckInsSeparator = findViewById(R.id.check_ins_separator);
        mTvLabelInspireOtherRiders = findViewById(R.id.tv_label_inspire_other_riders);
        mPublishRideButton = findViewById(R.id.button_publish_ride);
        mPublishRideButton.setOnClickListener(this);

        mCircularImageView1 = findViewById(R.id.other_ride_members_image1);
        mCircularImageView2 = findViewById(R.id.other_ride_members_image2);
        mCircularImageView3 = findViewById(R.id.other_ride_members_image3);
        mTvRideMemberNames = findViewById(R.id.other_riders_names);
        mImageGuideline = findViewById(R.id.guideline_v_image);

        setRideDetailsLayout();
        setDataToViews();

    }

    private void getIntentData() {
        mRideTourType = getIntent().getStringExtra(REConstants.RIDECUSTOM_VIEWTYPE);
        mRidePosition = getIntent().getIntExtra(REConstants.RIDES_LIST_POSITION, 0);
    }

    /**
     * Sets the rides layout titles dynamically
     */
    private void setRideDetailsLayout() {

        switch (mRideTourType) {
            case REConstants.PENDINGRIDE_TYPE:
                profileRideResponse = REUserModelStore.getInstance().getPendingRideResponse().get(mRidePosition);
                break;

            case REConstants.PASTRIDE_TYPE:
                TextView mTvLabelRidersJoined = findViewById(R.id.tv_label_riders_joined);
                mTvLabelRidersJoined.setVisibility(View.GONE);
                TextView mTvRiderJoined = findViewById(R.id.tv_rider_joined);
                mTvRiderJoined.setVisibility(View.GONE);
                profileRideResponse = REUserModelStore.getInstance().getPastRideResponse().get(mRidePosition);
                mRideId = profileRideResponse.get_id();
                //API call for checkIn places for the ride
                mRidesPresenter.getCheckIns(profileRideResponse.get_id());
                managePublishButton();
                break;

            case REConstants.REJECTEDRIDE_TYPE:
                profileRideResponse = REUserModelStore.getInstance().getRejectedRideResponse().get(mRidePosition);
                break;

        }

        RideDetailsLayout mRideDetailLayout = findViewById(R.id.past_ride_details_layout);
        REUtils.loadImageWithGlide(this, REUtils.getMobileappbaseURLs().getAssetsURL() +
                profileRideResponse.getRideImages().get(0).getSrcPath(), mIvRideImage);
        mRideDetailLayout.bindRiderProfileData(this, profileRideResponse);
    }

    /**
     * Disable publish button based on page status or createdBy
     */
    private void managePublishButton() {
        /**
         * Disable publish button always, REP-2870 (Disable the publish button in past ride)
         */
        disablePublishButton();

        String mPageStatus = profileRideResponse.getPageStatus();
        if ((mPageStatus != null && !mPageStatus.isEmpty())) {
            if (mPageStatus.equals(REFirestoreConstants.PAGE_STATUS_PUBLISHED) ||
                    !profileRideResponse.getCreatedBy().equals(REUtils.getUserId())) {
                disablePublishButton();
            }
        } else {
            if (!profileRideResponse.getCreatedBy().equals(REUtils.getUserId())) {
                disablePublishButton();
            }
        }
    }

    /**
     * Disable publish button
     */
    private void disablePublishButton() {
        mPublishRideButton.setBackgroundResource(R.drawable.button_border_disable);
        mPublishRideButton.setTextColor(getResources().getColor(R.color.white_50));
        mPublishRideButton.setEnabled(false);
    }

    private void setDataToViews() {
        switch (mRideTourType) {
            case REConstants.PENDINGRIDE_TYPE:
                mTitleBarView.bindData(this, R.drawable.back_arrow, profileRideResponse.getRideName());
                mRidersJoinedLayout.setVisibility(View.GONE);
                mTvLabelCheckIns.setVisibility(View.GONE);
                mCheckInsRecyclerView.setVisibility(View.GONE);
                mTvLabelInspireOtherRiders.setVisibility(View.GONE);
                mPublishRideButton.setVisibility(View.GONE);
                mModifyLine.setVisibility(View.GONE);
                break;

            case REConstants.PASTRIDE_TYPE:
                mTitleBarView.bindData(this, R.drawable.back_arrow, profileRideResponse.getRideName());
                mTvLabelModifyRideDetails.setVisibility(View.GONE);
                mModifyLine.setVisibility(View.GONE);
                if (profileRideResponse.getRidersJoined() == null || profileRideResponse.getRidersJoined().isEmpty()) {
                    mRidersJoinedLayout.setVisibility(View.GONE);
                } else {
                    ArrayList<RidersJoined> mRidersJoined = profileRideResponse.getRidersJoined();
                    ArrayList<RidersJoined> sResultRiders = new ArrayList<RidersJoined>();
                    if (mRidersJoined != null && mRidersJoined.size() > 0) {
                        if (mRidersJoined.size() == 1 && mRidersJoined.get(0).getUser().get("guid").equals(REUtils.getUserId())) {
                            mRidersJoinedLayout.setVisibility(View.GONE);
                            return;
                        }
                        for (int i = 0; i < mRidersJoined.size(); i++) {
                            if (!mRidersJoined.get(i).getUser().get("guid").equals(REUtils.getUserId())) {
                                sResultRiders.add(mRidersJoined.get(i));
                            }
                        }
                        profileRideResponse.setRidersJoined(sResultRiders);
                    }
                    REUtils.setRidersJoinedInfo(this, profileRideResponse, mCircularImageView1,
                            mCircularImageView2, mCircularImageView3, mTvRideMemberNames, mImageGuideline);
                }
                break;
            case REConstants.REJECTEDRIDE_TYPE:
                mTitleBarView.bindData(this, R.drawable.back_arrow, profileRideResponse.getRideName());
                mTvLabelModifyRideDetails.setVisibility(View.GONE);
                mModifyLine.setVisibility(View.GONE);
                mRidersJoinedLayout.setVisibility(View.GONE);
                mTvLabelCheckIns.setVisibility(View.GONE);
                mCheckInsRecyclerView.setVisibility(View.GONE);
                mCheckInsSeparator.setVisibility(View.GONE);
                mTvLabelInspireOtherRiders.setVisibility(View.GONE);
                mPublishRideButton.setVisibility(View.GONE);
                break;
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
    public void onClick(View view) {
        if (view.getId() == R.id.tv_label_modify_ride_details) {
            try {
                Intent mIntent = new Intent(this, ModifyRidesActivity.class);
                mIntent.putExtra(REConstants.RIDECUSTOM_VIEWTYPE, mRideTourType);
                mIntent.putExtra(REConstants.RIDES_LIST_POSITION, mRidePosition);
                startActivityForResult(mIntent, MODIFY_REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
            } catch (IndexOutOfBoundsException e) {
                RELog.e(e);
            }
        } else if (view.getId() == R.id.button_publish_ride) {
            //API call for publish ride
            mRidesPresenter.publishRide(mRideId);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MODIFY_REQUEST_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }

    public void onPublishRide(View view) {
        Intent mIntent = new Intent(this, AccountCreationSuccessActivity.class);
        mIntent.putExtra(REConstants.SUCCESS_ACTIVITY, REConstants.PASTRIDE_TYPE);
        startActivity(mIntent);
        overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
    }

    @Override
    public void onGetCheckInSuccess(CheckInResponse checkInResponse) {
        mTvLabelCheckIns.setVisibility(View.VISIBLE);
        mCheckInsRecyclerView.setVisibility(View.VISIBLE);
        mCheckInsSeparator.setVisibility(View.VISIBLE);
        RecyclerView.Adapter mCheckInsAdapter = new CheckInsAdapter(this, new CheckInRowRowPresenter(),
                checkInResponse);
        mCheckInsRecyclerView.setAdapter(mCheckInsAdapter);
    }

    @Override
    public void onGetCheckInFailure(String errorMessage) {
        mCheckInsRecyclerView.setVisibility(View.GONE);
        mTvLabelCheckIns.setVisibility(View.GONE);
        mCheckInsSeparator.setVisibility(View.GONE);
    }

    @Override
    public void onPublishRideSuccess() {
        Intent intent = new Intent(REApplication.getAppContext(), AccountCreationSuccessActivity.class);
        intent.putExtra(REConstants.SUCCESS_ACTIVITY, REConstants.PUBLISH_RIDE);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
    }

    @Override
    public void onPublishRideFailure(String errorMessage) {
        REUtils.showErrorDialog(this, errorMessage);
    }

    @Override
    public void onUpdateRideStatusSuccess() {

    }

    @Override
    public void onUpdateRideStatusFailure(String errorMessage) {

    }
}
