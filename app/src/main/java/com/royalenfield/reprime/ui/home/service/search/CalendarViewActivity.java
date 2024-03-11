package com.royalenfield.reprime.ui.home.service.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.models.response.proxy.serviceslot.PickupandDoorstepServiceSlot;
import com.royalenfield.reprime.models.response.proxy.serviceslot.ServiceSlotListResponse;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.service.history.interactor.ServiceBookingInteractor;
import com.royalenfield.reprime.ui.home.service.history.listener.ServiceBookingListener;
import com.royalenfield.reprime.ui.home.service.history.presenter.ServiceBookingPresenter;
import com.royalenfield.reprime.ui.home.service.listener.RecyclerViewClickListener;
import com.royalenfield.reprime.ui.home.service.servicebookingslots.fragment.SlotCategoryFragment;
import com.royalenfield.reprime.ui.home.service.servicebookingslots.fragment.TimeSlotFragmentView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.application.REApplication.mFirebaseAnalytics;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

/**
 * Created by JAL7KOR on 1/8/2019.
 */

public class CalendarViewActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener,
        ServiceBookingListener, RecyclerViewClickListener, SlotCategoryFragment.TimeSlotListener {

    private String selectedDate = "";
    private String selectedRawDate = "";
    private String selectedRawDateForService = "";
    private List<String> timeList, slotIDList;
    private String strSelectedBranch;
    private TextView mTextViewNoSlots;
    private boolean mRides = false;
    private boolean iscutOffTime = false;
    private boolean isDoorStepSelected = false;
    private boolean isPickupSelected = false;
    List<PickupandDoorstepServiceSlot> mpPickupandDoorstepServiceSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarview);
        mRides = getIntent().getBooleanExtra("rides", false);
        iscutOffTime = getIntent().getBooleanExtra("isCutOffTime",false);
        isDoorStepSelected = getIntent().getBooleanExtra("isDoorStepSelected",false);
        isPickupSelected = getIntent().getBooleanExtra("isPickupSelected",false);
        initViews();
        mpPickupandDoorstepServiceSlots = REApplication.getInstance().getPickupandDoorstepServiceSlotResponse();
        // Getting data from intent
        if (getIntent().getStringExtra("branchId") != null) {
            strSelectedBranch = getIntent().getStringExtra("branchId");
        }
    }

    /**
     * Initialising views
     */
    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.calendar_topbar);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.pick_service_date));
        mTextViewNoSlots = findViewById(R.id.textView_noslots);
        CalendarView calendarView = findViewById(R.id.calendarView);
        // for graying out past dates and current date+10 in days
        Calendar minDate = Calendar.getInstance();
        if (iscutOffTime)
             minDate.add(Calendar.DATE, 2);
        else
            minDate.add(Calendar.DATE, 1);
        calendarView.setMinDate(minDate.getTimeInMillis());
        // Setting Max date if it is servicing
        if (!mRides) {
            Calendar maxDate = Calendar.getInstance();
            if (iscutOffTime)
                maxDate.add(Calendar.DATE, 11);
            else
                maxDate.add(Calendar.DATE, 10);
            calendarView.setMaxDate(maxDate.getTimeInMillis());
        }

        calendarView.setOnDateChangeListener((calendarView1, year, month, dayOfMonth) -> {
            // This gives the ride date format
            //ridesDateFormat(year, month, dayOfMonth);
            // This date format is for service booking
            int iMonth = month + 1;
            if (!mRides) {
                selectedRawDate = iMonth + "-" + dayOfMonth + "-" + year;
            } else {
                selectedRawDate = dayOfMonth + "-" + iMonth + "-" + year;
            }
            //This creates date with ordinal format for displaying in date box UI in service booking and plan ride
            createDateInOrdinalFormat(year, month, dayOfMonth);
            if (!mRides) {
                // API call for getting service booking time slots
                if (isDoorStepSelected){
                    proceedtoBookingWithoutTimeslot();
                } else if(isPickupSelected) {
                    checkPickupServiceAvailable(slotsDateFormat(year, month + 1, dayOfMonth));
                } else {
                    getServiceTimeSlots(slotsDateFormat(year, month + 1, dayOfMonth));
                }
            } else {
                setDateInPlanRides();
            }
        });
    }

    private void proceedtoBookingWithoutTimeslot() {
        String selecteddate = REUtils.parseDateTo_ddMMMyyyy(selectedRawDate,"M-dd-yyyy");
        int postion_doorstep = -1;
        if (mpPickupandDoorstepServiceSlots != null) {
            for (int i = 0; i < mpPickupandDoorstepServiceSlots.size(); i++) {
                if (mpPickupandDoorstepServiceSlots.get(i).getAvailableDate().equals(selecteddate)) {
                    postion_doorstep = i;
                }
            }
            if (postion_doorstep != -1 && mpPickupandDoorstepServiceSlots.get(postion_doorstep).getDoorStepAvailability()) {
                Intent intent = new Intent();
                intent.putExtra("date", selectedDate);
                intent.putExtra("rawdate", selectedRawDateForService);
                intent.putExtra("date_sel", selecteddate);
                setResult(2, intent);
                finish();
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
            } else {
                REUtils.showErrorDialog(this, getResources().getString(R.string.text_no_doorstepavailabiity));
            }
        } else{
            REUtils.showErrorDialog(this, getResources().getString(R.string.text_no_doorstepavailabiity));
        }
    }

    private void checkPickupServiceAvailable(String strSelectedDate){
        String selecteddate = REUtils.parseDateTo_ddMMMyyyy(selectedRawDate,"M-dd-yyyy");
        int postion_pickup = -1;
        if (mpPickupandDoorstepServiceSlots != null) {
            for (int i = 0; i < mpPickupandDoorstepServiceSlots.size(); i++) {
                if (mpPickupandDoorstepServiceSlots.get(i).getAvailableDate().equals(selecteddate)) {
                    postion_pickup = i;
                }
            }
            if (postion_pickup != -1 && mpPickupandDoorstepServiceSlots.get(postion_pickup).getPickupAvailability()) {
                getServiceTimeSlots(strSelectedDate);
            } else {
                REUtils.showErrorDialog(this, getResources().getString(R.string.text_no_pickupavailabiity));
            }
        } else{
            REUtils.showErrorDialog(this, getResources().getString(R.string.text_no_pickupavailabiity));
        }
    }
    /**
     * This creates date in ordinal format
     *
     * @param year:      year
     * @param month      : month in year
     * @param dayOfMonth : day in month
     */
    private void createDateInOrdinalFormat(int year, int month, int dayOfMonth) {
        String[] monthName = getApplicationContext().getResources().getStringArray(R.array.month_name_arrays);
        String[] strDays = getApplicationContext().getResources().getStringArray(R.array.week_day_name);
        //Format for sending back to previous activity
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        selectedDate = strDays[dayOfWeek - 1] + "\n" + dayOfMonth + REUtils.getOrdinalFor(dayOfMonth) + " " +
                monthName[month] + " " + year;
    }

    /**
     * Adds zero to date and month if it is <=9
     *
     * @param year        : int
     * @param month:      int
     * @param dayOfMonth: int
     * @return String
     */
    private String slotsDateFormat(int year, int month, int dayOfMonth) {
        String date;
        date = year + "-" + (month >= 0 && month <= 9 ? "0" + (month) : month) + "-" +
                (dayOfMonth >= 0 && dayOfMonth <= 9 ? "0" + (dayOfMonth) : dayOfMonth);
        selectedRawDateForService = date;
        return date;
    }

    /**
     * This sends back the date result to Activity
     */
    private void setDateInPlanRides() {
        Intent intent = new Intent();
        intent.putExtra("date", selectedDate);
        intent.putExtra("rawdate", selectedRawDate);
        setResult(2, intent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }


    /**
     * API call for getting sevice booking time slots
     *
     * @param strSelectedDate : Date selected
     */
    private void getServiceTimeSlots(String strSelectedDate) {
if(!REBaseActivity.isFromReschedule) {
    Bundle params = new Bundle();
    params.putString("eventCategory", "Motorcycles-Schedule a service");
    params.putString("eventAction", "Calendar icon click");
    params.putString("eventLabel", strSelectedDate);
    REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
}
        ServiceBookingPresenter serviceBookingPresenter = new ServiceBookingPresenter(this, new ServiceBookingInteractor());
        if (strSelectedBranch != null && strSelectedBranch.length() > 0) {
            showLoading();
            serviceBookingPresenter.getBookingSlots(strSelectedDate, strSelectedBranch);
        }
    }

    /**
     * Method to pass Bundle data to TimeSlotFragmentView
     */
    private void sendDataToFragment() {
        TimeSlotFragmentView mTimeSlotFragmentView = new TimeSlotFragmentView();
        Bundle args = new Bundle();
        args.putString("selected_date", selectedDate);
        args.putStringArrayList("slotList", (ArrayList<String>) slotIDList);
        args.putStringArrayList("timeList", (ArrayList<String>) timeList);
        args.putString("strRawDate", selectedRawDate);
        args.putBoolean("activityForResult", false);
        mTimeSlotFragmentView.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.time_slot_view, mTimeSlotFragmentView).commit();
    }


    /**
     * Method to add all available resourceIds,timeSlotValues to
     * slotIDList(always first resourceId would be considered as slotId) and timeList respectively.
     */
    private void getTimeSlotList() {
        List<ServiceSlotListResponse> serviceSlotResponseList = REApplication.getInstance().
                getServiceSlotResponse();
        timeList = new ArrayList<>();
        slotIDList = new ArrayList<>();
        if (serviceSlotResponseList != null && serviceSlotResponseList.size() > 0) {
            for (int iTimeCount = 0; iTimeCount < serviceSlotResponseList.size(); iTimeCount++) {
                String slot;
                if (serviceSlotResponseList.get(iTimeCount).getAvailability()) {
                    String time = getTimeSlots(serviceSlotResponseList.get(iTimeCount).getStartTimeSlot());
                    slot = serviceSlotResponseList.get(iTimeCount).getResourceId();
                    if (slot != null) {
                        timeList.add(time);
                        slotIDList.add(slot);
                    }
                }
            }
        }
        if (timeList != null && slotIDList != null && timeList.size() > 0 && slotIDList.size() > 0) {
//            mTextMotorcycleDrop.setVisibility(View.VISIBLE);
            sendDataToFragment();
            mTextViewNoSlots.setVisibility(View.GONE);
        } else {
            sendDataToFragment();
            // mTextViewNoSlots to display alert  saying no slots available.
            mTextViewNoSlots.setVisibility(View.VISIBLE);
        }
    }

    private String getTimeSlots(String strTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
        String time = "";
        try {
            Date date = inputFormat.parse(strTime);
            time = outputFormat.format(date);
        } catch (ParseException e) {
            RELog.e(e);
        }
        return time;
    }

    @Override
    public void onNavigationIconClick() {
        Intent intent = new Intent();
        setResult(REConstants.timeSlotErrorCode, intent);
        finish();//finishing activity
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onServiceBookingFailure(String errorMessage) {

    }


    @Override
    public void onServiceBookingSuccess() {

    }

    @Override
    public void onServiceTimeSlotSuccess() {
        hideLoading();
        getTimeSlotList();
    }

    @Override
    public void onServiceTimeSlotFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialog(this, errorMessage);
        getTimeSlotList();
    }

    @Override
    public void onServiceCenterFetchSuccess(List<DealerMasterResponse> response) {

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

    @Override
    public void onItemClick(int value, boolean isSelected) {
    }

    @Override
    public void onTimeClick(int position) {
    }

    /**
     * Method used to return intent data to RidesAndServiceActivity onclick of chooseSlot.
     *
     * @param slotValue        timeSlotValue
     * @param selectedPos selectedPosition of master list
     */
    @Override
    public void selectedTimeSlot(String slotValue, int selectedPos) {
        int  selectedPosition = timeList.indexOf(slotValue);
        Intent intent = new Intent();
        intent.putExtra("position", "" + selectedPosition);
        intent.putExtra("date", selectedDate);
        intent.putExtra("rawdate", selectedRawDateForService);
        intent.putExtra("time", timeList.get(selectedPosition));
        intent.putExtra("slot_id", slotIDList.get(selectedPosition));
        setResult(2, intent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Calendar");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }
}


