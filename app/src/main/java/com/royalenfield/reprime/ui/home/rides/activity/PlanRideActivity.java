package com.royalenfield.reprime.ui.home.rides.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.gms.maps.model.LatLng;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.request.web.rides.CreateRideRequest;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.CalenderViewRides;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.AddWayPointFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.MapFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.planride.RideEndTimeFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.planride.RideStartTimeFragment;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.home.service.search.CalendarViewActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.royalenfield.reprime.utils.RELog;

/**
 * This activity is used to plan a ride
 */
public class PlanRideActivity extends REBaseActivity implements TitleBarView.
        OnNavigationIconClickListener, View.OnClickListener, RidesListeners.CurrentLocationListener,
        RidesListeners.RideGetSelectedTimeListener,
        AddWayPointFragment.IPlanRideWayPointSearchListener, MapFragment.OnTouchListener {

    private static final int REQUEST_CODE_RIDE_DATA = 1;
    private static final int REQUEST_CODE_CALENDAR_DATE_SLOT = 2;
    private static final int REQUEST_CODE_CALENDAR_END_DATE_SLOT = 3;
    //private FragmentFrameHolder mRecommendedRouteFrame;
    private TextView mDateTomorrow, mDateDayAfter, mEndDateTomorrow, mEndDateDayAfter;
    private ConstraintLayout mDurationConstraint;
    private TextView mTextDurationDec, mTextDurationInc, mTextDuration;
    private Button mSaveRide;
    private ImageView mCalendarImage, mEndDateCalenderImage;
    private RidesListeners.RideTimeListener mRideTimeListener;
    private RidesListeners.RideTimeListener mRideEndTimeListener;
    //For setting the custom font.
    private Typeface mTypefaceBold;
    private Typeface mTypefaceRegular;
    private String strDateSelected, strEndDateSelected, formattedStartDate, formattedEndDate;
    private View guideLine, guideLineEndDate;
    private int mDurationClickCount = 1;
    private int mCount = 1;
    private String mRideStartTime, mRideEndTime;
    private MapFragment mMapFragment;
    private ArrayList<WayPointsData> mInitialStartDestinationPoints = new ArrayList<>();
    private String strDistance;
    private NestedScrollView mScrollView;
    private CreateRideRequest mCreateRideRequest;
    private String mHashTags, mRideTitle, mRideDesc;
    private String mImageFile;
    int dateDifference = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_ride);
        initViews();
    }

    /**
     * Initializing views
     */
    private void initViews() {
        mScrollView = findViewById(R.id.scroll_home_screen);
        TitleBarView mTitleBarView = findViewById(R.id.plan_ride_header);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.label_create_ride));
        //To set font programatically.
        mTypefaceBold = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_bold);
        mTypefaceRegular = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
        mDateTomorrow = findViewById(R.id.tv_date_tomorrow);
        mDateTomorrow.setTypeface(mTypefaceRegular);
        mDateTomorrow.setOnClickListener(this);
        mEndDateTomorrow = findViewById(R.id.tv_end_date_tomorrow);
        mEndDateTomorrow.setTypeface(mTypefaceRegular);
        mEndDateTomorrow.setOnClickListener(this);
        mDateDayAfter = findViewById(R.id.tv_date_day_after);
        mDateDayAfter.setTypeface(mTypefaceRegular);
        mDateDayAfter.setOnClickListener(this);
        mEndDateDayAfter = findViewById(R.id.tv_end_date_day_after);
        mEndDateDayAfter.setTypeface(mTypefaceRegular);
        mEndDateDayAfter.setOnClickListener(this);
        mDurationConstraint = findViewById(R.id.constraint_duration);
        mTextDurationDec = findViewById(R.id.tv_duration_dec);
        mTextDurationDec.setOnClickListener(this);
        mTextDurationInc = findViewById(R.id.tv_duration_inc);
        mTextDurationInc.setOnClickListener(this);
        mTextDuration = findViewById(R.id.tv_duration);
        mSaveRide = findViewById(R.id.btn_ride_register);
        mSaveRide.setOnClickListener(this);
        mCalendarImage = findViewById(R.id.ivCalendar);
        mCalendarImage.setOnClickListener(this);
        mEndDateCalenderImage = findViewById(R.id.ivEndDateCalendar);
        mEndDateCalenderImage.setOnClickListener(this);

        guideLine = findViewById(R.id.guideline12);
        guideLineEndDate = findViewById(R.id.guideline12EndDate);
        showDateInformation();
        initFragments();

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
     * Initialising fragments
     */
    private void initFragments() {
        // Get data from CreateRideActivity
        getIntentData();
        //Add CreateMapFragment at the first time.
        addMapFragment();
        //AddWayPointFragment
        addWayPointFragment();
        //RideStartTimeFragment
        addRideStartTimeFragment();
        addRideEndTimeFragment();
        //RecommendedRoutesFragment
        //addRecommendedRouteFragment();
        disableNextButton();
    }

    /**
     * Adding RideStartTimeFragment.
     */
    private void addRideStartTimeFragment() {
        FragmentFrameHolder mRideTimeFrame = findViewById(R.id.fl_time_holder);
        RideStartTimeFragment mRideStartTImeFragment = RideStartTimeFragment.newInstance(this);
        setListener(mRideStartTImeFragment);
        mRideTimeFrame.loadFragment(this, mRideStartTImeFragment, null);
    }

    /**
     * Adding RideEndTimeFragment.
     */
    private void addRideEndTimeFragment() {
        FragmentFrameHolder mRideEndTimeFrame = findViewById(R.id.fl_end_time_holder);
        RideEndTimeFragment mRideEndTimeFragment = RideEndTimeFragment.newInstance(this);
        setListener(mRideEndTimeFragment);
        mRideEndTimeFrame.loadFragment(this, mRideEndTimeFragment, null);
    }

    /**
     * Adding the AddWayPointFragment
     */
    private void addWayPointFragment() {
        FragmentFrameHolder locationRideFrame = findViewById(R.id.fl_location_holder);
        AddWayPointFragment addWayPointFragment = AddWayPointFragment.newInstance();
        addWayPointFragment.setArguments(setBundleData());
        locationRideFrame.loadFragment(this, addWayPointFragment, null);
    }

    /**
     * Gets Intent data from CreateRideActivity
     */
    private void getIntentData() {
        //Added initial way point list
        if (getIntent() != null) {
            mInitialStartDestinationPoints = getIntent().getParcelableArrayListExtra(REConstants.WAY_POINTS_LIST);
            strDistance = getIntent().getStringExtra("distance");
        }
    }

    /**
     * Sets Bundle data
     */
    private Bundle setBundleData() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(REConstants.WAY_POINTS_LIST, mInitialStartDestinationPoints);
        bundle.putString(REConstants.RIDE_WAYPOINT_TYPE, REConstants.WAYPOINT_PLANRIDE);
        bundle.putBoolean(REConstants.IS_LOCATION_REQUIRED, false);
        bundle.putBoolean(REConstants.IS_NAVIGATION, false);
        return bundle;
    }

    /**
     * Listener for enabling/disabling RideStartTimeFragment
     * R
     *
     * @param fragment: RideStartTimeFragment
     */
    private void setListener(RideStartTimeFragment fragment) {
        try {
            mRideTimeListener = fragment;
        } catch (ClassCastException e) {
            RELog.e(e);
        }
    }

    /**
     * Listener for enabling/disabling RideEndTimeFragment
     * R
     *
     * @param endTimeFragment: RideEndTimeFragment
     */
    private void setListener(RideEndTimeFragment endTimeFragment) {
        try {
            mRideEndTimeListener = endTimeFragment;
        } catch (ClassCastException e) {
            RELog.e(e);
        }
    }

    /**
     * To hide recommended route fragment
     */
    public void hideRecommendedRouteFragment() {
        disableViews();
    }

    /**
     * Disabling views if destination is cleared
     */
    private void disableViews() {
        showDateInformation();
        mDateTomorrow.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mDateTomorrow.setTextColor(getResources().getColor(R.color.white_50));
        mEndDateTomorrow.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mEndDateTomorrow.setTextColor(getResources().getColor(R.color.white_50));
        mDateDayAfter.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mDateDayAfter.setTextColor(getResources().getColor(R.color.white_50));
        mEndDateDayAfter.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mEndDateDayAfter.setTextColor(getResources().getColor(R.color.white_50));
        mDurationConstraint.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mTextDurationInc.setTextColor(getResources().getColor(R.color.white_50));
        mTextDurationDec.setTextColor(getResources().getColor(R.color.white_50));
        mTextDuration.setTextColor(getResources().getColor(R.color.white_50));
        mSaveRide.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mSaveRide.setTextColor(getResources().getColor(R.color.white_50));
        Drawable mCalendarDisableDrawable = REUtils.changeDrawableColor(getApplicationContext(), R.drawable.ic_calendar,
                getApplicationContext().getResources().getColor(R.color.white_50));
        mCalendarImage.setImageDrawable(mCalendarDisableDrawable);
        mEndDateCalenderImage.setImageDrawable(mCalendarDisableDrawable);
        mRideTimeListener.rideTimeDisable();
        mRideEndTimeListener.rideEndTimeDisable();
        disableTodayTomorrowDatePicker();
        strDateSelected = "";
        strEndDateSelected = "";
        mDateTomorrow.setVisibility(View.VISIBLE);
        mEndDateTomorrow.setVisibility(View.VISIBLE);
        mDateDayAfter.setVisibility(View.VISIBLE);
        mEndDateDayAfter.setVisibility(View.VISIBLE);
        setGuidelinePercentage(View.VISIBLE);
        setGuidelinePercentageEndDate(View.VISIBLE);
        disableClicks();
        disableNextButton();
    }

    /**
     * Disabling clicks for views
     */
    private void disableClicks() {
        mCalendarImage.setEnabled(false);
        mEndDateCalenderImage.setEnabled(false);
        mDateTomorrow.setEnabled(false);
        mEndDateTomorrow.setEnabled(false);
        mDateDayAfter.setEnabled(false);
        mEndDateDayAfter.setEnabled(false);
        mTextDurationInc.setEnabled(false);
        mTextDurationDec.setEnabled(false);
        mTextDuration.setEnabled(false);
    }

    /**
     * Enabling views if destination is enteredl
     */
    public void showRecommendedRouteFragment() {
        enableViews();
    }

    /**
     * Enabling views if destination is enteredl
     */
    private void enableViews() {
        showDateInformation();
        mDateTomorrow.setBackground(getResources().getDrawable(R.drawable.button_border_enable));
        mDateTomorrow.setTextColor(getResources().getColor(R.color.white));
        mEndDateTomorrow.setBackground(getResources().getDrawable(R.drawable.button_border_enable));
        mEndDateTomorrow.setTextColor(getResources().getColor(R.color.white));
        mDateDayAfter.setBackground(getResources().getDrawable(R.drawable.button_border_enable));
        mDateDayAfter.setTextColor(getResources().getColor(R.color.white));
        mEndDateDayAfter.setBackground(getResources().getDrawable(R.drawable.button_border_enable));
        mEndDateDayAfter.setTextColor(getResources().getColor(R.color.white));
        mDurationConstraint.setBackground(getResources().getDrawable(R.drawable.button_border_enable));
        mTextDurationInc.setTextColor(getResources().getColor(R.color.white));
        mTextDurationDec.setTextColor(getResources().getColor(R.color.white));
        mTextDuration.setTextColor(getResources().getColor(R.color.white));
        mCalendarImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_calendar));
        mEndDateCalenderImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_calendar));
        mRideTimeListener.rideTimeEnable();
        mRideEndTimeListener.rideEndTimeEnable();
        enableClicks();
        if (strDateSelected != null && mRideStartTime != null && strEndDateSelected != null && mRideEndTime != null && strDateSelected.length() > 0
                && mRideStartTime.length() > 0 && strEndDateSelected.length() > 0 && mRideEndTime.length() > 0) {
//            if (strDateSelected.compareToIgnoreCase(strEndDateSelected) <= 0)
            enableNextButton();
//            if (strDateSelected.compareToIgnoreCase(strEndDateSelected) > 0)
//                Toast.makeText(this, "Ride start date must be lesser than or equal to ride end date", Toast.LENGTH_LONG).show();
        } else {
            disableNextButton();
        }

    }

    /**
     * Enabling clicks for views
     */
    private void enableClicks() {
        mCalendarImage.setEnabled(true);
        mEndDateCalenderImage.setEnabled(true);
        mDateTomorrow.setEnabled(true);
        mEndDateTomorrow.setEnabled(true);
        mDateDayAfter.setEnabled(true);
        mEndDateDayAfter.setEnabled(true);
        mTextDurationInc.setEnabled(true);
        mTextDurationDec.setEnabled(true);
        mTextDuration.setEnabled(true);
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
     * Opening calendar activity
     */
    private void selectEndDayFromCalendar() {
        Intent calendarIntent = new Intent(getApplicationContext(), CalenderViewRides.class);
        calendarIntent.putExtra("ridesEndDate", true);
        startActivityForResult(calendarIntent, 3);
        overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
    }

    /**
     * Fetches selected date information from CalendarViewActivity
     *
     * @param data : Intent
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
                showDateHighlighted(mDateTomorrow, mDateDayAfter);
            }
        }
    }

    /**
     * Fetches selected date information from CalendarViewRides
     *
     * @param data : Intent
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
                showDateHighlighted(mEndDateTomorrow, mEndDateDayAfter);
            }
        }
    }

    private void showDateInformation() {

        String dateTomorrow = REUtils.getFormattedDate(this, REConstants.DATE_TOMORROW);
        String dateDayAfterTomorrrow = REUtils.getFormattedDate(this, REConstants.DATE_DAY_AFTER_TOMORROW);

        mDateTomorrow.setText(dateTomorrow);
        mEndDateTomorrow.setText(dateTomorrow);
        mDateDayAfter.setText(dateDayAfterTomorrrow);
        mEndDateDayAfter.setText(dateDayAfterTomorrrow);

        mDateTomorrow.setVisibility(View.VISIBLE);
        mEndDateTomorrow.setVisibility(View.VISIBLE);
        mDateDayAfter.setVisibility(View.VISIBLE);
        mEndDateDayAfter.setVisibility(View.VISIBLE);
        setGuidelinePercentage(View.VISIBLE);
        setGuidelinePercentageEndDate(View.VISIBLE);
    }

    /**
     * Disables the Today and Tomorrow both view unselected.
     */
    private void disableTodayTomorrowDatePicker() {
        mDateDayAfter.setBackgroundColor(getResources().getColor(R.color.black_50));
        mDateDayAfter.setTextColor(getResources().getColor(R.color.white_50));
        mDateDayAfter.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mEndDateDayAfter.setBackgroundColor(getResources().getColor(R.color.black_50));
        mEndDateDayAfter.setTextColor(getResources().getColor(R.color.white_50));
        mEndDateDayAfter.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mDateTomorrow.setBackgroundColor(getResources().getColor(R.color.black_50));
        mDateTomorrow.setTextColor(getResources().getColor(R.color.white_50));
        mDateTomorrow.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mDateTomorrow.setTypeface(mTypefaceRegular);
        mEndDateTomorrow.setBackgroundColor(getResources().getColor(R.color.black_50));
        mEndDateTomorrow.setTextColor(getResources().getColor(R.color.white_50));
        mEndDateTomorrow.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        mEndDateTomorrow.setTypeface(mTypefaceRegular);
        mDateDayAfter.setTypeface(mTypefaceRegular);
        mEndDateDayAfter.setTypeface(mTypefaceRegular);
    }

    /**
     * to set guideline percentage dyanamically
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
     * to set guideline percentage for end date dyanamically
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
     * Setting text for Ride Duration
     */
    private void setDurationText() {
        mCount = mDurationClickCount;
        if (mDurationClickCount <= 1) {
            mTextDuration.setText(String.format(getApplicationContext().getResources().
                    getString(R.string.text_duration), String.valueOf(mCount), getApplicationContext().getResources().getString(R.string.text_day)));
        } else {
            mTextDuration.setText(String.format(getApplicationContext().getResources().
                            getString(R.string.text_duration),
                    String.valueOf(mCount), getApplicationContext().getResources().getString(R.string.text_days)));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date_tomorrow:
                if (mDateTomorrow != null && mDateTomorrow.getText() != null) {
                    strDateSelected = REUtils.getCurrentDate(REConstants.DATE_TOMORROW, "dd-MM-yyyy");
                    showDateHighlighted(mDateTomorrow, mDateDayAfter);
                }
                break;
            case R.id.tv_date_day_after:
                if (mDateDayAfter != null && mDateDayAfter.getText() != null) {
                    strDateSelected = REUtils.getCurrentDate(REConstants.DATE_DAY_AFTER_TOMORROW, "dd-MM-yyyy");
                    showDateHighlighted(mDateDayAfter, mDateTomorrow);
                }
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
                    setDurationText();
                }
                break;
            case R.id.tv_duration_dec:
                if (mDurationClickCount != 1) {
                    mDurationClickCount = mDurationClickCount - 1;
                    setDurationText();
                }
                break;
            case R.id.tv_end_date_tomorrow:
                if (mEndDateTomorrow != null && mEndDateTomorrow.getText() != null) {
                    strEndDateSelected = REUtils.getCurrentDate(REConstants.DATE_TOMORROW, "dd-MM-yyyy");
                    showDateHighlighted(mEndDateTomorrow, mEndDateDayAfter);
                }
                break;
            case R.id.tv_end_date_day_after:
                if (mEndDateDayAfter != null && mEndDateDayAfter.getText() != null) {
                    strEndDateSelected = REUtils.getCurrentDate(REConstants.DATE_DAY_AFTER_TOMORROW, "dd-MM-yyyy");
                    showDateHighlighted(mEndDateDayAfter, mEndDateTomorrow);
                }
                break;
            case R.id.btn_ride_register:
                if (strDateSelected != null && strEndDateSelected != null  && strDateSelected.length() > 0&& strEndDateSelected.length() > 0) {
                    formattedStartDate = startDateFormatting(strDateSelected);
                    formattedEndDate = endDateFormatting(strEndDateSelected);
                    int isValidDate = 0;
                    try {
                        if (formattedStartDate != null && formattedStartDate.length() > 0 && formattedEndDate != null && formattedEndDate.length() > 0)
                            isValidDate = compareDates(formattedStartDate + " " + mRideStartTime, formattedEndDate + " " + mRideEndTime);
                    } catch (java.text.ParseException e) {
                        RELog.e(e);
                    }
                    if (strDateSelected != null && mRideStartTime != null && mRideEndTime != null && strDateSelected.length() > 0
                            && mRideStartTime.length() > 0 && strEndDateSelected != null && strEndDateSelected.length() > 0 && mRideEndTime.length() > 0 && (isValidDate == 1 || isValidDate == 2)) {
                        Intent intent = new Intent(getApplicationContext(), ShareYourRideActivity.class);
                        intent.putParcelableArrayListExtra("RideRoute", mInitialStartDestinationPoints);
                        intent.putExtra("StartDate", strDateSelected);
                        intent.putExtra("EndDate", strEndDateSelected);
                        intent.putExtra("StartTime", mRideStartTime);
                        intent.putExtra("EndTime", mRideEndTime);
                        if (formattedStartDate != null && formattedStartDate.length() > 0 && formattedEndDate != null && formattedEndDate.length() > 0) {
                            dateDifference = (int) getDateDiff(new SimpleDateFormat("dd-MM-yyyy"), formattedStartDate, formattedEndDate);
                            if (dateDifference == 0)
                                mCount = 1;
                            else
                                mCount = dateDifference;
                            intent.putExtra("Duration", mCount);
                        }
                        intent.putExtra("distance", strDistance);
                        intent.putExtra("ride_data", mCreateRideRequest);
                        intent.putExtra("ride_hastags", mHashTags);
                        intent.putExtra("ride_image", mImageFile);
                        intent.putExtra("ride_title", mRideTitle);
                        intent.putExtra("ride_desc", mRideDesc);
                        startActivityForResult(intent, REQUEST_CODE_RIDE_DATA);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                    } else if (isValidDate == 3) {
                        Toast.makeText(this, "Please select an end date which is after or on the start date.", Toast.LENGTH_LONG).show();
                    } else if (isValidDate == 4 || isValidDate == 5 || isValidDate == 6 || isValidDate == 7) {
                        Toast.makeText(this, "Please select an end time after the start time you chose.", Toast.LENGTH_LONG).show();
                    }
                      else {
                        Toast.makeText(this, "Please enter Ride Start Time", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CALENDAR_DATE_SLOT && data != null) {
            getSelectedDate(data);
            // getSelectedEndDate(data);
        } else if (requestCode == REQUEST_CODE_CALENDAR_END_DATE_SLOT && data != null) {
            getSelectedEndDate(data);
            //getSelectedEndDate(data);
        } else if (requestCode == REQUEST_CODE_RIDE_DATA && data != null && resultCode == Activity.RESULT_CANCELED) {
            try {
                mCreateRideRequest = (CreateRideRequest) data.getSerializableExtra("ride_data");
                mHashTags = data.getStringExtra("ride_hastags");
                mImageFile = data.getStringExtra("ride_image");
                mRideTitle = data.getStringExtra("ride_title");
                mRideDesc = data.getStringExtra("ride_desc");
            } catch (ClassCastException e) {
                RELog.e(e);
            } catch (Exception e) {
                RELog.e(e);
            }

        } else if (requestCode == REQUEST_CODE_RIDE_DATA && data != null && resultCode == Activity.RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void updateCurrentLocation(LatLng latLng) {

    }

    @Override
    public void setDistance(Double iDistanceKM) {
        strDistance = String.valueOf(iDistanceKM);
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

    @Override
    public void getSelectedTime(String time) {
        mRideStartTime = time;
        mRideTimeListener.changeSelectedTimeImage(mRideStartTime);
//        mRideTimeListener.changeSelectedEndTimeImage(mRideEndTime);
        convert24HourTo12Hour();
        //enableNextButton();
    }

    @Override
    public void getSelectedEndTime(String time) {
        mRideEndTime = time;
        //mRideTimeListener.changeSelectedEndTimeImage(mRideEndTime);
        mRideEndTimeListener.changeSelectedEndTimeImage(mRideEndTime);
        String input = strEndDateSelected + " " + mRideEndTime;
        convert24HourTo12HourEndTime();
        enableNextButton();
    }

    @Override
    public boolean getSelectedDate() {
        if (strDateSelected != null && strDateSelected.length() > 0) {
            return true;
        } else {
            Toast.makeText(this, "Please Select Ride Start Date", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public boolean getSelectedEndDate() {
        if (strEndDateSelected != null && strEndDateSelected.length() > 0) {
            return true;
        } else {
            Toast.makeText(this, "Please Select Ride End Date", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void updateRoute(ArrayList<WayPointsData> wayPointDataList) {
        mInitialStartDestinationPoints = wayPointDataList;
        // Redraws the route
        mMapFragment.calculateStartDestinationAndWayPointLatLang(wayPointDataList, false);
    }

    @Override
    public void onTouch() {
        mScrollView.requestDisallowInterceptTouchEvent(true);
    }

    private void convert24HourTo12Hour() {
        //Input date in String format
        String input = strDateSelected + " " + mRideStartTime;
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
}
