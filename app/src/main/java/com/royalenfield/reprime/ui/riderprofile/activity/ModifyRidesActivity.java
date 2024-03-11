package com.royalenfield.reprime.ui.riderprofile.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.request.web.rides.CreateRideRequest;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.CalenderViewRides;
import com.royalenfield.reprime.ui.home.rides.activity.JoinRideStatusActivity;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.AddWayPointFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.CreateRideTitleInfo;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.CustomRideCategory;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.MapFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.TerrainFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.planride.RideEndTimeFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.planride.RideStartTimeFragment;
import com.royalenfield.reprime.ui.home.rides.interactor.ModifyRideInteractor;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.home.rides.presenter.ModifyRidePresenter;
import com.royalenfield.reprime.ui.home.service.search.CalendarViewActivity;
import com.royalenfield.reprime.ui.riderprofile.interactor.IModifyRideCreator;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;

import com.royalenfield.reprime.utils.RELog;

public class ModifyRidesActivity extends REBaseActivity implements View.OnClickListener, CustomRideCategory.
        OnFragmentInteractionListener, TerrainFragment.OnFragmentInteractionListener,
        CreateRideTitleInfo.OnFragmentInteractionListener, TitleBarView.OnNavigationIconClickListener,
        RidesListeners.RideGetSelectedTimeListener, AddWayPointFragment.IPlanRideWayPointSearchListener,
        MapFragment.OnTouchListener, RidesListeners.CurrentLocationListener, IModifyRideCreator.MainView {

    private static final int REQUEST_CODE_CALENDAR_DATE_SLOT = 2;
    private static final int REQUEST_CODE_CALENDAR_END_DATE_SLOT = 3;
    private static final int REQUEST_CODE_IMAGE_CROP = 1095;
    private ArrayList<WayPointsData> mInitialStartDestinationPoints = new ArrayList<>();
    private RidesListeners.RideTimeListener mRideTimeListener;
    private RidesListeners.RideTimeListener mRideEndTimeListener;
    private TextView mDateTomorrow, mDateDayAfter, mEndDateTomorrow, mEndDateDayAfter, mTvUploadRidePhoto;
    private TextView mTextDurationDec, mTextDurationInc, mTextDuration;
    private ConstraintLayout mDurationConstraint;
    //For setting the custom font.
    private Typeface mTypefaceBold;
    private Typeface mTypefaceRegular;
    private View guideLine, guideLineEndDate;
    private ImageView mCalendarImage, mEndDateCalenderImage;
    private String strDateSelected, strEndDateSelected, mRideEndDate, formattedStartDate, formattedEndDate;
    private String mRideStartTime, mRideEndTime;
    private Button mSaveRide;
    private String filePath;
    private ImageView mCameraAddImage, mShareRideImage, mEditRideImage;
    private int mDurationClickCount = 1;
    private MapFragment mMapFragment;
    private FrameLayout mFrameShareRideImage;
    private ProfileRidesResponse profileRideResponse;
    private String mTerrainType, mDifficultyLevel, mRideTitle, mRideDesc;
    private String strDistance;
    private NestedScrollView mScrollView;
    private ModifyRidePresenter mModifyRidePresenter;
    private boolean isStartTimeSelected = false;
    private boolean isEndTimeSelected = false;
    private int dateDifference = 0;
    private int mCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_rides);
        try {
            getRideType();
            initViews();
            mModifyRidePresenter = new ModifyRidePresenter(this, new ModifyRideInteractor());
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (IndexOutOfBoundsException e) {
            finish();
            overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
            Toast.makeText(getApplicationContext(), "Ride status updated. Please modify from upcoming rides", Toast.LENGTH_SHORT).show();
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void getRideType() {
        String mRideTourType = getIntent().getStringExtra(REConstants.RIDECUSTOM_VIEWTYPE);
        int mRidePosition = getIntent().getIntExtra(REConstants.RIDES_LIST_POSITION, 0);
        switch (mRideTourType) {
            case REConstants.PENDINGRIDE_TYPE:
                profileRideResponse = REUserModelStore.getInstance().getPendingRideResponse().get(mRidePosition);
                break;
            case REConstants.UPCOMIMGRIDE_TYPE:
                profileRideResponse = REUserModelStore.getInstance().getUpcomingRideResponse().get(mRidePosition);
                break;
        }
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
        mInitialStartDestinationPoints.clear();
        double dStartLattitude = profileRideResponse.getStartPointCoordinates().get(0);
        double dStartLongitude = profileRideResponse.getStartPointCoordinates().get(1);
        double dDestLattitude = profileRideResponse.getEndPointCoordinates().get(0);
        double dDestLongitude = profileRideResponse.getEndPointCoordinates().get(1);
        mInitialStartDestinationPoints.add(mInitialStartDestinationPoints.size(), new WayPointsData(dStartLattitude,
                dStartLongitude, profileRideResponse.getStartPoint()));
        if (profileRideResponse.getWaypoints() != null && profileRideResponse.getWaypoints().size() > 0) {
            mInitialStartDestinationPoints.addAll(profileRideResponse.getWaypoints());
        }
        mInitialStartDestinationPoints.add(mInitialStartDestinationPoints.size(), new WayPointsData(dDestLattitude,
                dDestLongitude, profileRideResponse.getEndPoint()));

        bundle.putParcelableArrayList(REConstants.WAY_POINTS_LIST, mInitialStartDestinationPoints);
        bundle.putString(REConstants.RIDE_WAYPOINT_TYPE, REConstants.WAYPOINT_MODIFYRIDE);
        bundle.putBoolean(REConstants.IS_LOCATION_REQUIRED, false);
        bundle.putBoolean(REConstants.IS_NAVIGATION, false);
        strDistance = profileRideResponse.getRideInfo().getTotalDistance();
        return bundle;
    }

    /**
     * Initializes all the views.
     */
    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.plan_ride_header);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.title_modify_rides));

        mTypefaceBold = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_bold);
        mTypefaceRegular = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);

        mScrollView = findViewById(R.id.scroll_home_screen);
        mCameraAddImage = findViewById(R.id.camera_add_image);
        mCameraAddImage.setOnClickListener(this);
        mEditRideImage = findViewById(R.id.ivEditIcon);
        mEditRideImage.setOnClickListener(this);
        mShareRideImage = findViewById(R.id.share_ride_image);
        mFrameShareRideImage = findViewById(R.id.frame_share_ride_image);
        mTvUploadRidePhoto = findViewById(R.id.tv_upload_ride_photo);
        mDateTomorrow = findViewById(R.id.tv_date_tomorrow);
        mDateTomorrow.setOnClickListener(this);
        mDateDayAfter = findViewById(R.id.tv_end_date_day_after);
        mDateDayAfter.setOnClickListener(this);
        mDateDayAfter.setVisibility(View.GONE);
        mEndDateTomorrow = findViewById(R.id.tv_end_date_tomorrow);
        mEndDateTomorrow.setOnClickListener(this);
        mEndDateDayAfter = findViewById(R.id.tv_date_day_after);
        mEndDateDayAfter.setOnClickListener(this);
        mEndDateDayAfter.setVisibility(View.GONE);
        mCalendarImage = findViewById(R.id.ivCalendar);
        mCalendarImage.setOnClickListener(this);
        mEndDateCalenderImage = findViewById(R.id.ivEndDateCalendar);
        mEndDateCalenderImage.setOnClickListener(this);
        guideLine = findViewById(R.id.guideline12);
        guideLineEndDate = findViewById(R.id.guideline12EndDate);
        mSaveRide = findViewById(R.id.btn_publish_ride);
        mSaveRide.setOnClickListener(this);
        mTextDurationDec = findViewById(R.id.tv_duration_dec);
        mTextDurationDec.setOnClickListener(this);
        mTextDurationInc = findViewById(R.id.tv_duration_inc);
        mTextDurationInc.setOnClickListener(this);
        mTextDuration = findViewById(R.id.tv_duration);
        mDurationConstraint = findViewById(R.id.constraint_duration);

        loadModifyFirestoreResponse();
    }

    private void loadModifyFirestoreResponse() {
        addMapFragment();
        addWayPointFragment();
        refreshFirestoreInformation();
        if (profileRideResponse.getRideImages() != null && profileRideResponse.getRideImages().size() > 0) {
            REUtils.loadImageWithGlide(this, REUtils.getMobileappbaseURLs().getAssetsURL() +
                    profileRideResponse.getRideImages().get(0).getSrcPath(), mShareRideImage);

            mFrameShareRideImage.setForeground(getDrawable(R.drawable.foreground_no_gradient));
            mCameraAddImage.setVisibility(View.INVISIBLE);
            mTvUploadRidePhoto.setVisibility(View.INVISIBLE);
            mEditRideImage.setVisibility(View.VISIBLE);
        }
    }

    private void refreshFirestoreInformation() {
        showDateInformation();
        setDurationText(profileRideResponse.getRideInfo().getDurationInDays());
        addRideStartTimeFragment();
        addRideEndTimeFragment();
        loadRideInfoFragment();
        loadLevelDifficultyFragment();
        loadTerrainCategoryFragment();
    }

    /**
     * Adding map fragment at the first time.
     */
    private void addMapFragment() {
        //MapFragment
        FragmentFrameHolder mPlanRideFrame = findViewById(R.id.fl_planride_mapview);
        mMapFragment = MapFragment.newInstance(this);
        mMapFragment.setListener(this);
        mMapFragment.setArguments(setBundleData());
        mPlanRideFrame.loadFragment(this, mMapFragment, MapFragment.TAG_MAP_FRAGMENT);
    }

    /**
     * Makes Today Date view highlighted. and Tomorrow view disabled.
     */
    private void showDateHighlighted(TextView selectedDateView, TextView unselectedDateView) {
        selectedDateView.setBackgroundColor(getResources().getColor(R.color.white));
        selectedDateView.setTextColor(getResources().getColor(R.color.black));
        unselectedDateView.setBackgroundColor(getResources().getColor(R.color.transpaent_50));
        unselectedDateView.setTextColor(getResources().getColor(R.color.white));
        unselectedDateView.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        selectedDateView.setTypeface(mTypefaceBold);
        unselectedDateView.setTypeface(mTypefaceRegular);
    }

    /**
     * Opening calendar activity
     */
    private void selectDayFromCalendar() {
        Intent calendarIntent = new Intent(getApplicationContext(), CalendarViewActivity.class);
        calendarIntent.putExtra("rides", true);
        startActivityForResult(calendarIntent, 2);
        overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
    }

    /**
     * Opening calendar activity for end date
     */
    private void selectEndDayFromCalendar() {
        Intent calendarIntent = new Intent(getApplicationContext(), CalenderViewRides.class);
        calendarIntent.putExtra("ridesEndDate", true);
        startActivityForResult(calendarIntent, 3);
        overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
    }

    /**
     * Adding RideStartTimeFragment.
     */
    private void addRideStartTimeFragment() {
        FragmentFrameHolder mRideTimeFrame = findViewById(R.id.fl_time_holder);
        RideStartTimeFragment mRideStartTImeFragment = RideStartTimeFragment.newInstance(this);
        setListener(mRideStartTImeFragment);
        mRideTimeFrame.loadFragment(this, mRideStartTImeFragment, null);
        mRideTimeListener.selectTimeForModifyRide(convert12HourTo24Hour());
    }

    /**
     * Adding RideEndTimeFragment.
     */
    private void addRideEndTimeFragment() {
        FragmentFrameHolder mRideEndTimeFrame = findViewById(R.id.fl_end_time_holder);
        RideEndTimeFragment mRideEndTimeFragment = RideEndTimeFragment.newInstance(this);
        setListener(mRideEndTimeFragment);
        mRideEndTimeFrame.loadFragment(this, mRideEndTimeFragment, null);
        mRideEndTimeListener.selectEndTimeForModifyRide(convert12HourTo24HourEndTime());
    }

    /**
     * Listener for enabling/disabling RideEndTimeFragment
     * R
     *
     * @param fragment: RideEndTimeFragment
     */
    private void setListener(RideEndTimeFragment fragment) {
        mRideEndTimeListener = fragment;
    }


    /**
     * Listener for enabling/disabling RideStartTimeFragment
     * R
     *
     * @param fragment: RideStartTimeFragment
     */
    public void setListener(RideStartTimeFragment fragment) {
        mRideTimeListener = fragment;
    }

    /**
     * Setting text for Ride Duration
     */
    private void setDurationText(int mDurationClickCount) {
        if (mDurationClickCount <= 1) {
            mTextDuration.setText(String.format(getApplicationContext().getResources().
                    getString(R.string.text_duration), String.valueOf(mDurationClickCount), getApplicationContext().getResources().getString(R.string.text_day)));
        } else {
            mTextDuration.setText(String.format(getApplicationContext().getResources().
                            getString(R.string.text_duration),
                    String.valueOf(mDurationClickCount), getApplicationContext().getResources().getString(R.string.text_days)));
        }
        getRideEndDate();
    }

    private void loadRideInfoFragment() {
        //Views
        FragmentFrameHolder mDealerRidesFrame = findViewById(R.id.layout_ride_info);
        CreateRideTitleInfo rideHeader = CreateRideTitleInfo.newInstance(profileRideResponse.getRideName(),
                profileRideResponse.getRideInfo().getRideDetails());
        mDealerRidesFrame.loadFragment(this, rideHeader, null);

        mRideTitle = profileRideResponse.getRideName();
        mRideDesc = profileRideResponse.getRideInfo().getRideDetails();
    }

    private void loadLevelDifficultyFragment() {
        //Views
        FragmentFrameHolder mDealerRidesFrame = findViewById(R.id.layout_level_of_difficulty);
        //Initializing DealerRidesList
        String frgTitle = getResources().getString(R.string.level_of_difficulty);
        CustomRideCategory rideCategory = CustomRideCategory.newInstance("level_of_difficulty", frgTitle,
                "Easy", "Medium", "Hard");
        mDealerRidesFrame.loadFragment(this, rideCategory, null);
        rideCategory.categoryIsLike(profileRideResponse.getRideInfo().getDifficulty());

        mDifficultyLevel = profileRideResponse.getRideInfo().getDifficulty();
    }


    private void loadTerrainCategoryFragment() {
        //Views
        FragmentFrameHolder mDealerRidesFrame = findViewById(R.id.layout_type_of_terrain);
        //Initializing DealerRidesList
        TerrainFragment terrainFragment = TerrainFragment.newInstance();
        mDealerRidesFrame.loadFragment(this, terrainFragment, null);
        String selectedCategory;
        if (profileRideResponse.getRideInfo().getTerrainType().contains("-")) {
            selectedCategory = profileRideResponse.getRideInfo().getTerrainType().replace("-", "");
        } else {
            selectedCategory = profileRideResponse.getRideInfo().getTerrainType();
        }
        terrainFragment.categoryIsLike(selectedCategory);

        mTerrainType = selectedCategory;
    }

    private void showDateInformation() {
        strDateSelected = profileRideResponse.getRideInfo().getStartDate();
        strEndDateSelected = profileRideResponse.getRideInfo().getEndDate();
        String dateTomorrow = REUtils.getDateFormatted(this, profileRideResponse.getRideInfo().getRideStartDateIso());
        mDateTomorrow.setText(dateTomorrow);
        showDateHighlighted(mDateTomorrow, mDateDayAfter);
        mDateTomorrow.setVisibility(View.VISIBLE);
        mDateDayAfter.setVisibility(View.GONE);
        setGuidelinePercentage(View.GONE);
        String endDateTomorrow = REUtils.getDateFormatted(this, profileRideResponse.getRideInfo().getRideEndDateIso());
        mEndDateTomorrow.setText(endDateTomorrow);
        showDateHighlighted(mEndDateTomorrow, mEndDateDayAfter);
        mEndDateTomorrow.setVisibility(View.VISIBLE);
        mEndDateDayAfter.setVisibility(View.GONE);
        setGuidelinePercentageEndDate(View.GONE);

        mDurationClickCount = profileRideResponse.getRideInfo().getDurationInDays();
        getRideEndDate();
    }

    /**
     * Fetches selected date information from CalendarViewActivity
     *
     * @param data used to show date information
     */
    private void getSelectedDate(Intent data) {
        if (data != null) {
            if (data.getStringExtra("date") != null) {
                String strDate = data.getStringExtra("date");
                String strRawDate = data.getStringExtra("rawdate");
                System.out.println("Calendar selected Date: " + strRawDate);
                mDateDayAfter.setVisibility(View.GONE);
                setGuidelinePercentage(View.GONE);
                mDateTomorrow.setText(strDate.replace(",", "\n"));
                mDateTomorrow.setBackgroundColor(getResources().getColor(R.color.white));
                mDateTomorrow.setTextColor(getResources().getColor(R.color.black));
                strDateSelected = strRawDate;
                getRideEndDate();
            }
        }
    }

    /**
     * Fetches selected date information from CalendarViewRides
     *
     * @param data used to show end date information
     */
    private void getSelectedEndDate(Intent data) {
        if (data != null) {
            if (data.getStringExtra("endDate") != null) {
                String strDate = data.getStringExtra("endDate");
                String strRawDate = data.getStringExtra("rawEndDate");
                System.out.println("Calendar selected Date: " + strRawDate);
                mEndDateDayAfter.setVisibility(View.GONE);
                setGuidelinePercentageEndDate(View.GONE);
                mEndDateTomorrow.setText(strDate.replace(",", "\n"));
                mEndDateTomorrow.setBackgroundColor(getResources().getColor(R.color.white));
                mEndDateTomorrow.setTextColor(getResources().getColor(R.color.black));
                strEndDateSelected = strRawDate;
                getRideEndDate();

            }
        }
    }

    /**
     * to set guideline percentage dynamically
     *
     * @param i : int
     */
    private void setGuidelinePercentage(int i) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        if (i == View.GONE) {
            params.guidePercent = 0.45f;
            guideLine.setLayoutParams(params);
        } else {
            params.guidePercent = 0.861f;
            guideLine.setLayoutParams(params);
        }
    }

    /**
     * to set guideline percentage for end date dynamically
     *
     * @param i : int
     */
    private void setGuidelinePercentageEndDate(int i) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLineEndDate.getLayoutParams();
        if (i == View.GONE) {
            params.guidePercent = 0.45f;
            guideLineEndDate.setLayoutParams(params);
        } else {
            params.guidePercent = 0.861f;
            guideLineEndDate.setLayoutParams(params);
        }
    }

    /**
     * To hide recommended route fragment
     */
    public void hideRecommendedRouteFragment() {
        // mRecommendedRouteFrame.setVisibility(View.GONE);
        disableViews();
    }

    /**
     * Enabling views if destination is enteredl
     */
    public void showRecommendedRouteFragment() {
        //mRecommendedRouteFrame.setVisibility(View.VISIBLE);
        enableViews();
    }

    /**
     * Enabling views if destination is enteredl
     */
    private void enableViews() {
        showDateInformation();
        enableDatePicker();
        enableDayDuration();
        showDateHighlighted(mDateTomorrow, mDateDayAfter);
        showDateHighlighted(mEndDateTomorrow, mEndDateDayAfter);
        //mRideTimeListener.rideTimeEnable();
        addRideStartTimeFragment();
        //addRideEndTimeFragment();
        mDateTomorrow.setVisibility(View.VISIBLE);
        mDateDayAfter.setVisibility(View.GONE);
        mEndDateTomorrow.setVisibility(View.VISIBLE);
        mEndDateDayAfter.setVisibility(View.GONE);
        setGuidelinePercentage(View.GONE);
        setGuidelinePercentageEndDate(View.GONE);
        enableNextButton();
    }

    /**
     * Disabling views if destination is cleared
     */
    private void disableViews() {
        showDateInformation();
        disableDatePicker();
        disableDayDuration();
        mRideTimeListener.rideTimeDisable();
        mRideEndTimeListener.rideEndTimeDisable();
        strDateSelected = "";
        strEndDateSelected = "";
        mDateTomorrow.setVisibility(View.VISIBLE);
        mDateDayAfter.setVisibility(View.GONE);
        mEndDateTomorrow.setVisibility(View.VISIBLE);
        mEndDateDayAfter.setVisibility(View.GONE);
        setGuidelinePercentage(View.GONE);
        setGuidelinePercentageEndDate(View.GONE);
        disableNextButton();
    }

    /**
     * Disables the Today and Tomorrow both view unselected.
     */
    private void disableDatePicker() {
        Drawable mCalendarDisableDrawable = REUtils.changeDrawableColor(getApplicationContext(), R.drawable.ic_calendar,
                getApplicationContext().getResources().getColor(R.color.white_50));
        mCalendarImage.setImageDrawable(mCalendarDisableDrawable);
        mEndDateCalenderImage.setImageDrawable(mCalendarDisableDrawable);
        mDateDayAfter.setBackgroundColor(getResources().getColor(R.color.black_50));
        mDateDayAfter.setTextColor(getResources().getColor(R.color.white_50));
        mDateDayAfter.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mDateTomorrow.setBackgroundColor(getResources().getColor(R.color.black_50));
        mDateTomorrow.setTextColor(getResources().getColor(R.color.white_50));
        mDateTomorrow.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mDateTomorrow.setTypeface(mTypefaceRegular);
        mDateDayAfter.setTypeface(mTypefaceRegular);
        mDateDayAfter.setEnabled(false);
        mDateTomorrow.setEnabled(false);

        mEndDateDayAfter.setBackgroundColor(getResources().getColor(R.color.black_50));
        mEndDateDayAfter.setTextColor(getResources().getColor(R.color.white_50));
        mEndDateDayAfter.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mEndDateTomorrow.setBackgroundColor(getResources().getColor(R.color.black_50));
        mEndDateTomorrow.setTextColor(getResources().getColor(R.color.white_50));
        mEndDateTomorrow.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mEndDateTomorrow.setTypeface(mTypefaceRegular);
        mEndDateDayAfter.setTypeface(mTypefaceRegular);
        mEndDateDayAfter.setEnabled(false);
        mEndDateTomorrow.setEnabled(false);
        mCalendarImage.setEnabled(false);
        mEndDateCalenderImage.setEnabled(false);
    }

    /**
     * Disables the Today and Tomorrow both view unselected.
     */
    private void enableDatePicker() {
        mCalendarImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_calendar));
        mEndDateCalenderImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_calendar));
        mDateTomorrow.setBackground(getResources().getDrawable(R.drawable.button_border_enable));
        mDateTomorrow.setTextColor(getResources().getColor(R.color.white));
        mDateDayAfter.setBackground(getResources().getDrawable(R.drawable.button_border_enable));
        mDateDayAfter.setTextColor(getResources().getColor(R.color.white));
        mDateDayAfter.setEnabled(true);
        mDateTomorrow.setEnabled(true);
        mCalendarImage.setEnabled(true);

        mEndDateTomorrow.setBackground(getResources().getDrawable(R.drawable.button_border_enable));
        mEndDateTomorrow.setTextColor(getResources().getColor(R.color.white));
        mEndDateDayAfter.setBackground(getResources().getDrawable(R.drawable.button_border_enable));
        mEndDateDayAfter.setTextColor(getResources().getColor(R.color.white));
        mEndDateDayAfter.setEnabled(true);
        mEndDateTomorrow.setEnabled(true);
        mEndDateCalenderImage.setEnabled(true);

        mDateTomorrow.setTypeface(mTypefaceRegular);
        mDateDayAfter.setTypeface(mTypefaceRegular);
        mEndDateTomorrow.setTypeface(mTypefaceRegular);
        mEndDateDayAfter.setTypeface(mTypefaceRegular);

    }

    /**
     * Disables the Today and Tomorrow both view unselected.
     */
    private void enableDayDuration() {
        mTextDurationInc.setTextColor(getResources().getColor(R.color.white));
        mTextDurationDec.setTextColor(getResources().getColor(R.color.white));
        mTextDuration.setTextColor(getResources().getColor(R.color.white));
        mDurationConstraint.setBackground(getResources().getDrawable(R.drawable.button_border_enable));
        mRideTimeListener.rideTimeEnable();
        mRideEndTimeListener.rideEndTimeEnable();
        mTextDurationInc.setEnabled(true);
        mTextDurationDec.setEnabled(true);
    }

    /**
     * Disables the Today and Tomorrow both view unselected.
     */
    private void disableDayDuration() {
        mTextDurationInc.setTextColor(getResources().getColor(R.color.white_50));
        mTextDurationDec.setTextColor(getResources().getColor(R.color.white_50));
        mTextDuration.setTextColor(getResources().getColor(R.color.white_50));
        mDurationConstraint.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mTextDurationInc.setEnabled(false);
        mTextDurationDec.setEnabled(false);
    }

    private void disableNextButton() {
        mSaveRide.setEnabled(false);
        mSaveRide.setBackgroundResource(R.drawable.button_border_disable);
        mSaveRide.setTextColor(getResources().getColor(R.color.white_50));
    }

    private void enableNextButton() {
        mSaveRide.setBackgroundResource(R.drawable.button_border_enable);
        mSaveRide.setTextColor(getResources().getColor(R.color.white));
        mSaveRide.setEnabled(true);
    }

    private void createRideRequest() {

        CreateRideRequest createRideRequest = new CreateRideRequest();
        createRideRequest.setRideId(profileRideResponse.get_id());
        createRideRequest.setRideName(mRideTitle);
        //createRideRequest.setCreatedOn(profileRideResponse.getCreatedOn());

        double slat = mInitialStartDestinationPoints.get(0).getLatitude();
        double slon = mInitialStartDestinationPoints.get(0).getLongitude();
        ArrayList<Double> startPointCoordinates = new ArrayList<>();
        startPointCoordinates.add(slat);
        startPointCoordinates.add(slon);
        double elat = mInitialStartDestinationPoints.get(mInitialStartDestinationPoints.size() - 1).getLatitude();
        double elon = mInitialStartDestinationPoints.get(mInitialStartDestinationPoints.size() - 1).getLongitude();
        ArrayList<Double> endPointCoordinates = new ArrayList<>();
        endPointCoordinates.add(elat);
        endPointCoordinates.add(elon);

        //Creating temporary way points
        ArrayList<WayPointsData> wayPointArrayList = new ArrayList<>();
        if (mInitialStartDestinationPoints.size() > 2) {
            for (int iCount = 1; iCount <= mInitialStartDestinationPoints.size() - 2; iCount++) {
                WayPointsData waypoint = new WayPointsData();
                waypoint.setLatitude(mInitialStartDestinationPoints.get(iCount).getLatitude());
                waypoint.setLongitude(mInitialStartDestinationPoints.get(iCount).getLongitude());
                waypoint.setPlaceName(mInitialStartDestinationPoints.get(iCount).getPlaceName());
                wayPointArrayList.add(waypoint);
            }
        }

        //Creating RideRequest
        createRideRequest.setStartPoint(mInitialStartDestinationPoints.get(0).getPlaceName());
        createRideRequest.setStartPointCoordinates(startPointCoordinates);
        createRideRequest.setDestination(mInitialStartDestinationPoints.get(mInitialStartDestinationPoints.size() - 1).getPlaceName());
        createRideRequest.setEndPointCoordinates(endPointCoordinates);
        createRideRequest.setWaypoints(wayPointArrayList);
        createRideRequest.setStartDate(strDateSelected);
        createRideRequest.setEndDate(strEndDateSelected);
        createRideRequest.setDifficulty(mDifficultyLevel);
        createRideRequest.setRideStatus("UPCOMING");
        if (formattedStartDate != null && formattedStartDate.length() > 0 && formattedEndDate != null && formattedEndDate.length() > 0) {
            dateDifference = (int) getDateDiff(new SimpleDateFormat("dd-MM-yyyy"), formattedStartDate, formattedEndDate);
        }
        if (dateDifference == 0)
            mCount = 1;
        else
            mCount = dateDifference;
        // createRideRequest.setDurationInDays(String.valueOf(mDurationClickCount));
        createRideRequest.setDurationInDays(String.valueOf(mCount));
        if (strDistance != null && !strDistance.isEmpty() && !strDistance.equals("null")) {
            createRideRequest.setTotalDistance(String.valueOf(Math.round(Float.valueOf(strDistance))));
        }
        createRideRequest.setRideDetails(mRideDesc);
        createRideRequest.setTerrainType(mTerrainType);
        createRideRequest.setRideType(profileRideResponse.getRideType());
        createRideRequest.setRideCategory(profileRideResponse.getRideCategory().toString());
//        if (!isStartTimeSelected)
//            getSelectedTime(mRideStartTime);
        createRideRequest.setStartTime(mRideStartTime);
//        if (!isEndTimeSelected)
//            getSelectedEndTime(mRideEndTime);
        createRideRequest.setEndTime(mRideEndTime);
        createRideRequest.setRideCategory(profileRideResponse.getRideCategory().toString());
/*


        List<String> hashTags = profileRideResponse.getHashTags();
        createRideRequest.setHashTags(hashTags);*/

        mModifyRidePresenter.callModifyRideDetail(createRideRequest, filePath);

    }

    @Override
    public void updateCurrentLocation(LatLng latLng) {

    }

    @Override
    public void setDistance(Double iDistanceKM) {
        strDistance = String.valueOf(iDistanceKM);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.tv_date_tomorrow:
                if (mDateTomorrow != null && mDateTomorrow.getText() != null) {
                    strDateSelected = REUtils.getCurrentDate( "dd-MM-yyyy", profileRideResponse.getRideInfo().getDurationInDays());
                    showDateHighlighted(mDateTomorrow, mDateDayAfter);
                }
                break;*/
            case R.id.camera_add_image:
            case R.id.ivEditIcon:
                REUtils.selectImage(ModifyRidesActivity.this, ModifyRidesActivity.this);
                break;
            case R.id.ivCalendar:
                selectDayFromCalendar();
                break;
            case R.id.ivEndDateCalendar:
                selectEndDayFromCalendar();
                break;
            case R.id.tv_duration_inc:
                if (mDurationClickCount != 30) {
                    mDurationClickCount = mDurationClickCount + 1;
                    setDurationText(mDurationClickCount);
                }
                break;
            case R.id.tv_duration_dec:
                if (mDurationClickCount != 1) {
                    mDurationClickCount = mDurationClickCount - 1;
                    setDurationText(mDurationClickCount);
                }
                break;
            case R.id.btn_publish_ride:
                if (!isStartTimeSelected)
                    convert24HourTo12Hour();
                if (!isEndTimeSelected)
                    convert24HourTo12HourEndTime();

                if (strDateSelected != null && strDateSelected.length() > 0 && strEndDateSelected != null && strEndDateSelected.length() > 0) {
                    formattedStartDate = startDateFormatting(strDateSelected);
                    formattedEndDate = endDateFormatting(strEndDateSelected);
                    int isValidDate = 0;
                    try {
                        if (formattedStartDate != null && formattedStartDate.length() > 0 && formattedEndDate != null && formattedEndDate.length() > 0)
                            isValidDate = compareDates(formattedStartDate + " " + mRideStartTime, formattedEndDate + " " + mRideEndTime);
                    } catch (java.text.ParseException e) {
                        RELog.e(e);
                    }

                    if (!checkMandatoryFields() && (isValidDate == 1 || isValidDate == 2)) {
                        createRideRequest();
                    } else if (isValidDate == 3) {
                        Toast.makeText(this, "Please select an end date which is after or on the start date.", Toast.LENGTH_LONG).show();
                    } else if (isValidDate == 4 || isValidDate == 5 || isValidDate == 6 || isValidDate == 7) {
                        Toast.makeText(this, "Please select an end time after the start time you chose.", Toast.LENGTH_LONG).show();
                    }
//                else {
//                    if (strDateSelected.compareToIgnoreCase(strEndDateSelected) > 0) {
//                        Toast.makeText(this, "Ride start date must be lesser than or equal to ride end date", Toast.LENGTH_LONG).show();
//                    }
//                }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE_CALENDAR_DATE_SLOT && data != null) {
                getSelectedDate(data);
            } else if (requestCode == REQUEST_CODE_CALENDAR_END_DATE_SLOT && data != null) {
                getSelectedEndDate(data);
            }
            if (resultCode == RESULT_OK && requestCode == REConstants.REQUEST_CHECK_GALLERY && data != null) {
                Uri imageUri = data.getData();
                filePath = REUtils.getRealPathFromURI(imageUri, ModifyRidesActivity.this);
                Intent cropIntent = new Intent(getApplicationContext(), RideImageCropActivity.class);
                cropIntent.putExtra("imageuri", imageUri);
                cropIntent.putExtra("filepath", filePath);
                cropIntent.putExtra("imagemode", "gallery");
                startActivityForResult(cropIntent, REQUEST_CODE_IMAGE_CROP);
//              onCaptureImageResult(BitmapFactory.decodeFile(filePath));
            } else if (requestCode == REConstants.REQUEST_CHECK_CAMERA && data != null && data.getExtras() != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                if (thumbnail != null) {
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = REUtils.getImageUri(getApplicationContext(), thumbnail);
                    filePath = REUtils.getRealPathFromURI(tempUri, ModifyRidesActivity.this);
                    Intent cropIntent = new Intent(getApplicationContext(), RideImageCropActivity.class);
                    cropIntent.putExtra("imageuri", tempUri);
                    cropIntent.putExtra("filepath", filePath);
                    cropIntent.putExtra("thumbnail", thumbnail);
                    cropIntent.putExtra("imagemode", "camera");
                    startActivityForResult(cropIntent, REQUEST_CODE_IMAGE_CROP);
                    //onCaptureImageResult(thumbnail);
                }
            } else if (requestCode == REQUEST_CODE_IMAGE_CROP && data != null) {
                if (resultCode == RESULT_OK) {
                    Bitmap bmp = null;
                    String filename = data.getStringExtra("BitmapImage");
                    filePath = data.getStringExtra("filepathcropped");

                    try {
                        FileInputStream is = this.openFileInput(filename);
                        bmp = BitmapFactory.decodeStream(is);
                        is.close();
                        if (bmp != null)
                            onCaptureImageResult(bmp);
                    } catch (Exception e) {
                        RELog.e(e);
                    }
                }

                if (resultCode == RESULT_CANCELED) {
                    if (!Objects.requireNonNull(data.getStringExtra("filepathcropped")).isEmpty()) {
                        filePath = data.getStringExtra("filepathcropped");
                    } else {
                        filePath = "";
                    }
                }

            }

        } catch (OutOfMemoryError e) {
            RELog.e(e.getMessage());
        } catch (NullPointerException e) {
            RELog.e(e.getMessage());
        } catch (Exception e) {
            RELog.e(e.getMessage());
        }

    }

    /**
     * @param thumbnail : Bitmap
     */
    private void onCaptureImageResult(Bitmap thumbnail) {
        if (thumbnail != null) {
            mShareRideImage.setImageBitmap(thumbnail);
            mFrameShareRideImage.setForeground(getDrawable(R.drawable.foreground_no_gradient));
            mCameraAddImage.setVisibility(View.INVISIBLE);
            mTvUploadRidePhoto.setVisibility(View.INVISIBLE);
            mEditRideImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFragmentInteraction(String strType, String uri) {
        if (strType.equalsIgnoreCase("level_of_difficulty")) {
            if (uri.equalsIgnoreCase("Hard")) {
                mDifficultyLevel = "HARD";
            } else {
                mDifficultyLevel = uri.toUpperCase();
            }
        }
    }

    @Override
    public void onTerrainFragmentInteraction(String uri) {
        mTerrainType = uri.toUpperCase();
        if (mTerrainType.contains(" ")) {
            mTerrainType = mTerrainType.replace(" ", "-");
        }
    }

    @Override
    public void onCreateRideHeaderDetails(String title, String desc) {
        mRideTitle = title;
        mRideDesc = desc;
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
    public void getSelectedTime(String time) {
        isStartTimeSelected = true;
        mRideStartTime = time;
        mRideTimeListener.changeSelectedTimeImage(mRideStartTime);
        //mRideTimeListener.changeSelectedEndTimeImage(mRideEndTime);
        convert24HourTo12Hour();
        enableNextButton();
    }

    @Override
    public void getSelectedEndTime(String time) {
        isEndTimeSelected = true;
        mRideEndTime = time;
        //mRideTimeListener.changeSelectedTimeImage(mRideStartTime);
        mRideEndTimeListener.changeSelectedEndTimeImage(mRideEndTime);
        convert24HourTo12HourEndTime();
        enableNextButton();
    }

    @Override
    public void updateRoute(ArrayList<WayPointsData> wayPointDataList) {
        mInitialStartDestinationPoints = wayPointDataList;
        // Redraws the route
        //mMapFragment.calculateStartDestinationAndWayPointLatLang(wayPointDataList, false);
    }

    @Override
    public void onTouch() {
        mScrollView.requestDisallowInterceptTouchEvent(true);
    }


    private void getRideEndDate() {
        if (strDateSelected != null && !strDateSelected.isEmpty() && strEndDateSelected != null && !strEndDateSelected.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date journeyDate = new java.sql.Date(sdf.parse(strDateSelected).getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(journeyDate);

                //cal.add(Calendar.DAY_OF_MONTH, mDurationClickCount);
                int duration = profileRideResponse.getRideInfo().getDurationInDays();
                cal.add(Calendar.DAY_OF_MONTH, duration);
                mRideEndDate = sdf.format(cal.getTime());


            } catch (Exception e) {
                RELog.e(e);
            }
        }
    }

    private String convert12HourTo24Hour() {
        String mStartTime;
        Date date = null;
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        if (profileRideResponse != null) {
            mStartTime = profileRideResponse.getRideInfo().getStartTime();
            if (mStartTime != null && !mStartTime.isEmpty()) {
                String format1 = "hh:mm a";
                String format2 = "hh:mm aa";
                SimpleDateFormat parseFormat;
                if (mStartTime.contains("a.m.") || mStartTime.contains("p.m.")) {
                    parseFormat = new SimpleDateFormat(format2, Locale.UK);
                } else {
                    parseFormat = new SimpleDateFormat(format1, Locale.US);
                }
                try {
                    date = parseFormat.parse(mStartTime);
                } catch (java.text.ParseException e) {
                    RELog.e(e);
                }
            }
        }
        if (date == null) {
            mRideStartTime = "-1";
        } else {
            mRideStartTime = displayFormat.format(date);
        }
        return mRideStartTime;
    }

    private String convert12HourTo24HourEndTime() {
        String mEndTime;
        Date date = null;
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        if (profileRideResponse != null) {
            mEndTime = profileRideResponse.getRideInfo().getEndTime();
            if (mEndTime != null && !mEndTime.isEmpty()) {
                String format1 = "hh:mm a";
                String format2 = "hh:mm aa";
                SimpleDateFormat parseFormat;
                if (mEndTime.contains("a.m.") || mEndTime.contains("p.m.")) {
                    parseFormat = new SimpleDateFormat(format2, Locale.UK);
                } else {
                    parseFormat = new SimpleDateFormat(format1, Locale.US);
                }
                try {
                    date = parseFormat.parse(mEndTime);
                } catch (java.text.ParseException e) {
                    RELog.e(e);
                }
            }
        }
        if (date == null) {
            mRideEndTime = "-1";
        } else {
            mRideEndTime = displayFormat.format(date);
        }
        return mRideEndTime;
    }

    private void convert24HourTo12Hour() {
        //Input date in String format
        String input = strDateSelected + " " + mRideStartTime;
        //Date/time pattern of input date
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        //Date/time pattern of desired output date
        DateFormat outputformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date;
        String output;
        try {
            //Conversion of input String to date
            date = df.parse(input);
            //old date format to new date format
            output = outputformat.format(date);
            mRideStartTime = output;

            System.out.println(output);
        } catch (ParseException | java.text.ParseException pe) {
            RELog.e(pe);
        }
    }

    private void convert24HourTo12HourEndTime() {
        //Input date in String format
        String input = strEndDateSelected + " " + mRideEndTime;
        //Date/time pattern of input date
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        //Date/time pattern of desired output date
        DateFormat outputformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date = null;
        String output = null;
        try {
            //Conversion of input String to date
            date = df.parse(input);
            //old date format to new date format
            output = outputformat.format(date);

            mRideEndTime = output;
            System.out.println(output);
        } catch (ParseException | java.text.ParseException pe) {
            RELog.e(pe);
        }
    }

    @Override
    public boolean getSelectedDate() {
        if (strDateSelected != null && strDateSelected.length() > 0) {
            return true;
        } else {
            Toast.makeText(this, "Please Select Date", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public boolean getSelectedEndDate() {
        if (strDateSelected != null && strDateSelected.length() > 0) {
            return true;
        } else {
            Toast.makeText(this, "Please Select End Date", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equalsIgnoreCase("NA") || str.length() == 0;
    }


    private boolean checkMandatoryFields() {

        boolean isPresent = isEmpty(mRideTitle);
        if (isPresent) {
            Toast.makeText(this, "Please Select Ride Title", Toast.LENGTH_LONG).show();
            return isPresent;
        }

        isPresent = isEmpty(mRideDesc);
        if (isPresent) {
            Toast.makeText(this, "Please Select Ride Description", Toast.LENGTH_LONG).show();
            return isPresent;
        }

        isPresent = isEmpty(strDateSelected);
        if (isPresent) {
            Toast.makeText(this, "Please Select Ride Date", Toast.LENGTH_LONG).show();
            return isPresent;
        }

        isPresent = isEmpty(strEndDateSelected);
        if (isPresent) {
            Toast.makeText(this, "Please Select Ride End Date", Toast.LENGTH_LONG).show();
            return isPresent;
        }

        isPresent = isEmpty(strDistance);
        if (isPresent) {
            Toast.makeText(this, "Please Select Destination", Toast.LENGTH_LONG).show();
            return isPresent;
        }
        return isPresent;
    }

    @Override
    protected void onDestroy() {
        if (mModifyRidePresenter != null) {
            mModifyRidePresenter.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onModifyRideSuccess() {
        hideLoading();
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
        Intent rideStatus = new Intent(this, JoinRideStatusActivity.class);
        rideStatus.putExtra(REConstants.TEXT_SPLASH_DESC_SUCCESS, getResources().getString(R.string.label_ride_info));
        rideStatus.putExtra("msg", "modify");
        startActivity(rideStatus);
    }

    @Override
    public void onResponseFailure(String throwable) {
        hideLoading();
        REUtils.showErrorDialog(this, throwable);
    }

    /**
     * Get a diff between two dates
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return the diff value, in the days
     */
    public static long getDateDiff(SimpleDateFormat format, String startDate, String endDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(endDate).getTime() - format.parse(startDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            RELog.e(e);
            return 0;
        }
    }

    /**
     * Validation checks for start date/time and end date/time
     */
    private int compareDates(String formattedStartDate, String formattedEndDate) throws java.text.ParseException {
        int isValid = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        Date date1 = sdf.parse(formattedStartDate);
        Date date2 = sdf.parse(formattedEndDate);

        if (date1.compareTo(date2) < 0)
            isValid = 1;
        if (date1.compareTo(date2) == 0 && date1.getTime() < date2.getTime())
            isValid = 2;
        if (date1.compareTo(date2) > 0) {
            if (date1.getDate() > date2.getDate()||date1.getMonth() > date2.getMonth())
                isValid = 3;
            else
                isValid = 6;
        }
//        if (date1.compareTo(date2) > 0) {
//            if (date1.getTime() >= date2.getTime())
//                isValid = 6;
//            else
//                isValid = 3;
//        }
        if (date1.compareTo(date2) == 0 && date1.getTime() > date2.getTime())
            isValid = 4;
        if (date1.compareTo(date2) == 0 && date1.getTime() == date2.getTime())
            isValid = 5;
        if (date1.compareTo(date2) > 0 && date1.getTime() > date2.getTime())
            isValid = 6;
        if (date1.compareTo(date2) > 0 && date1.getTime() < date2.getTime())
            isValid = 7;
        return isValid;
    }

    /**
     * End date formatting
     */

    private String endDateFormatting(String strEndDateSelected) {
        String endDate = strEndDateSelected;
        String dateEnd = "", monthEnd = "", yearEnd = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = sdf.parse(endDate);
        } catch (java.text.ParseException e) {
            RELog.e(e);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        dateEnd = checkDigit(cal.get(Calendar.DATE));
        monthEnd = checkDigit(cal.get(Calendar.MONTH) + 1);
        yearEnd = checkDigit(cal.get(Calendar.YEAR));
        endDate = dateEnd + "-" + monthEnd + "-" + yearEnd;
        return endDate;
    }

    /**
     * Start date formatting
     */
    private String startDateFormatting(String strDateSelected) {
        String startDate = strDateSelected;
        String dateStart = "", monthStart = "", yearStart = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = sdf.parse(startDate);
        } catch (java.text.ParseException e) {
            RELog.e(e);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        dateStart = checkDigit(cal.get(Calendar.DATE));
        monthStart = checkDigit(cal.get(Calendar.MONTH) + 1);
        yearStart = checkDigit(cal.get(Calendar.YEAR));
        startDate = dateStart + "-" + monthStart + "-" + yearStart;
        return startDate;
    }

    /**
     * Adds zero to date and month if it is <=9 for start and end date
     */
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}
