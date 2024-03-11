package com.royalenfield.reprime.ui.home.rides;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.Calendar;

public class CalenderViewRides extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener {

    private String selectedEndDate = "";
    private String selectedRawEndDate = "";
    private boolean mRidesEndDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarview);
        mRidesEndDate = getIntent().getBooleanExtra("ridesEndDate", false);
        initViews();
    }

    /**
     * Initialising views
     */
    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.calendar_topbar);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.pick_service_date));
        CalendarView calendarView = findViewById(R.id.calendarView);
        // for graying out past dates and current date+10 in days
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.DATE, 1);
        calendarView.setMinDate(minDate.getTimeInMillis());
        calendarView.setOnDateChangeListener((calendarView1, year, month, dayOfMonth) -> {
            // This gives the ride date format
            //ridesDateFormat(year, month, dayOfMonth);
            // This date format is for service booking
            int iMonth = month + 1;

            selectedRawEndDate = dayOfMonth + "-" + iMonth + "-" + year;

            //This creates date with ordinal format for displaying in date box UI plan ride
            createDateInOrdinalFormat(year, month, dayOfMonth);

            setDateInPlanRides();

        });
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
        selectedEndDate = strDays[dayOfWeek - 1] + "\n" + dayOfMonth + REUtils.getOrdinalFor(dayOfMonth) + " " +
                monthName[month] + " " + year;
    }

    /**
     * This sends back the date result to Activity
     */
    private void setDateInPlanRides() {
        Intent intent = new Intent();
        intent.putExtra("endDate", selectedEndDate);
        intent.putExtra("rawEndDate", selectedRawEndDate);
        setResult(3, intent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
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

}


