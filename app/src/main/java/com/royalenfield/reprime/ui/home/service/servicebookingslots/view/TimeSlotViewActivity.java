package com.royalenfield.reprime.ui.home.service.servicebookingslots.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.proxy.serviceslot.ServiceSlotListResponse;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.service.servicebookingslots.fragment.SlotCategoryFragment;
import com.royalenfield.reprime.ui.home.service.servicebookingslots.fragment.TimeSlotFragmentView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;


/**
 * TimeSlotViewActivity shows time slots based on slotTag,SlotTags will be,
 * Morning :: 9am to 11.45
 * AfterNoon :: 12pm to 15:45 pm
 * Evening :: 16pm to 5.45pm
 * TimeSlotViewActivity contains fragment which will be replaced by TimeSlotFragmentView during runtime containing tabLayout & viewpager.
 */

public class TimeSlotViewActivity extends REBaseActivity implements SlotCategoryFragment.TimeSlotListener, View.OnClickListener {
    private TitleBarView mTitleBarView;
    private ArrayList<String> slotIDList, timeList;
    private FrameLayout mTimeSlotFragmentView;
    private List<ServiceSlotListResponse> mServiceTimeSlotResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot_view);
        initializeViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    private void initializeViews() {
        mTitleBarView = findViewById(R.id.new_calendar_topbar);
        ImageView mNavCloseBtn = findViewById(R.id.iv_navigation);
        mTimeSlotFragmentView = findViewById(R.id.timeslot_frame_layout);
      //  isFromReschedule=getIntent().getBooleanExtra("from_reschedule",false);
        mNavCloseBtn.setOnClickListener(this);
        if (getIntent().getExtras() != null && getIntent().getExtras().getString("selected_date") != null) {
            setDataToTileBarView(getIntent().getExtras().getString("selected_date"));
        }
        getTimeSlotList();
        sendDataToFragment();
    }

    /**
     * Method to pass data from TimeSlotViewActivity to TimeSlotFragmentView.
     */
    private void sendDataToFragment() {
        TimeSlotFragmentView mTimeSlotFragmentView = new TimeSlotFragmentView();
        Bundle args = new Bundle();
        args.putStringArrayList("slotList", slotIDList);
        args.putStringArrayList("timeList", timeList);
        mTimeSlotFragmentView.setArguments(args);
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.timeslot_frame_layout, mTimeSlotFragmentView).
                commit();
    }


    /**
     * Method to pass selected timeSlotValue  to parent Activity(RidesAndServiceActivity) onClick of showAllAvailableSlots.
     *
     * @param slotValue        selected timeSlotValue.
     * @param selectedPos selectedPosition from masterList.
     */

    @Override
    public void selectedTimeSlot(String slotValue, int selectedPos) {
        //if condition to pass results from TimeSlotViewActivity to RidesAndServiceActivity when timeSlot is selected onclick of Show all available slots
        if (timeList != null && timeList.size() > 0) {
            int  selectedPosition = timeList.indexOf(slotValue);
            Intent intent = new Intent();
            intent.putExtra("time", slotValue);
            intent.putExtra("position", selectedPosition);
            int REQUEST_SHOW_MORE_TIMESLOT = 7;
            setResult(REQUEST_SHOW_MORE_TIMESLOT, intent);  //intent code 6
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_navigation:
                //onClick of back send empty data
                Intent intent = new Intent();
                setResult(REConstants.timeSlotErrorCode, intent);
                finish();
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
                break;
        }
    }

    /**
     * Method to set the data to tileBarView
     *
     * @param strSelectedDate selected date in RidesAndServiceActivity
     * @return string date without year
     */
    private void setDataToTileBarView(String strSelectedDate) {
        if (strSelectedDate != null) {
            strSelectedDate = strSelectedDate.substring(0, strSelectedDate.length() - 6).trim();
            strSelectedDate = strSelectedDate.replaceAll("\n", ", ");
        }
        mTitleBarView.bindData(this, R.drawable.back_arrow, strSelectedDate);
    }


    /**
     * Method to add all available resourceIds,timeSlotValues to
     * slotIDList(always first resourceId would be considered as slotId) and timeList respectively.
     */
    private void getTimeSlotList() {
        slotIDList = new ArrayList<String>();
        timeList = new ArrayList<String>();
        mServiceTimeSlotResponse = REApplication.getInstance().
                getServiceSlotResponse();
        if (mServiceTimeSlotResponse != null && mServiceTimeSlotResponse.size() > 0) {
            for (int i = 0; i < mServiceTimeSlotResponse.size(); i++) {
                String slot;
                if (mServiceTimeSlotResponse.get(i).getAvailability()) {
                    String time = REUtils.getTimeSlots(mServiceTimeSlotResponse.get(i).getStartTimeSlot());
                    slot = mServiceTimeSlotResponse.get(i).getResourceId();
                    timeList.add(time);
                    if (slot != null) {
                        slotIDList.add(slot);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Show all Available Slots");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }
}
