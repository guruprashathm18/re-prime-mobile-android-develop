package com.royalenfield.reprime.ui.home.service.servicebookingslots.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.ui.home.service.search.CalendarViewActivity;
import com.royalenfield.reprime.ui.home.service.servicebookingslots.adapter.SlotCategoryViewPagerAdapter;
import com.royalenfield.reprime.ui.home.service.servicebookingslots.view.TimeSlotViewActivity;
import com.royalenfield.reprime.ui.riderprofile.viewpager.RiderProfileViewPager;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;

import static com.royalenfield.reprime.application.REApplication.mFirebaseAnalytics;

/**
 * TimeSlotFragmentView :: contains TabLayout and ViewPager.
 * shows time slots based on slotTag,SlotTags would be,
 * Morning :: 9am to 11.45
 * AfterNoon :: 12pm to 15:45 pm
 * Evening :: 16pm to 5.45pm
 */

public class TimeSlotFragmentView extends REBaseFragment implements ViewPager.OnPageChangeListener {
    private TabLayout timeSlotTabLayout;
    private RiderProfileViewPager timeSlotViewPager;
    private SlotCategoryViewPagerAdapter mSlotCategoryViewPagerAdapter;
    private ArrayList<String> timeList;

    public TimeSlotFragmentView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_slot_view, container, false);
        initializeViews(view);
        fetchData(getArguments());
        //If slot response is not null then only adapter will be set for viewpager
        if (timeList != null && timeList.size() > 0) {
            addTabs();
            Bundle params=new Bundle();
            if (REBaseActivity.isFromReschedule)
                params.putString("eventCategory", "Motorcycles-Reschedule");
            else
                params.putString("eventCategory", "Motorcycles-Schedule a service");
            if( getActivity() instanceof CalendarViewActivity)
                params.putString("eventAction", "Calendar icon click");
            else
                params.putString("eventAction",getResources().getString(R.string.text_label_show_available_slots));
            params.putString("eventLabel", "Morning");
           REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
            highLightCurrentTab(0);
            onTabAttachListeners();
        }
        return view;

    }

    /**
     * Initialising views
     *
     * @param view : View
     */
    private void initializeViews(View view) {
        timeSlotTabLayout = view.findViewById(R.id.time_tabLayout);
        timeSlotViewPager = view.findViewById(R.id.new_calender_viewPage);
        timeSlotViewPager.setPagingEnabled(true);
        timeSlotViewPager.setOffscreenPageLimit(3);
    }

    /**
     * Fetch data from bundle
     * List of resourceIDs.
     * List of timeSlotValues
     *
     * @param arguments : Bundle
     */
    private void fetchData(Bundle arguments) {
        if (arguments != null) {
            timeList = arguments.getStringArrayList("timeList");
        }
    }


    /**
     * For adding tabs to tab layout.
     * Morning,AfterNoon and Evening.
     */
    private void addTabs() {
        timeSlotTabLayout.addTab(timeSlotTabLayout.newTab().setText(getResources().getString(R.string.Morning)));
        timeSlotTabLayout.addTab(timeSlotTabLayout.newTab().setText(getResources().getString(R.string.Afternoon)));
        timeSlotTabLayout.addTab(timeSlotTabLayout.newTab().setText(getResources().getString(R.string.Evening)));
        mSlotCategoryViewPagerAdapter = new SlotCategoryViewPagerAdapter(getActivity(),
                getChildFragmentManager(), timeSlotTabLayout.getTabCount(), timeList, timeSlotTabLayout);
        timeSlotTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        timeSlotViewPager.setAdapter(mSlotCategoryViewPagerAdapter);
    }

    /**
     * Method to add addOnTabSelectedListener to tabLayout.
     */
    private void onTabAttachListeners() {

        timeSlotViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(timeSlotTabLayout));
        timeSlotTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Bundle params = new Bundle();
                timeSlotViewPager.setCurrentItem(tab.getPosition());
                mSlotCategoryViewPagerAdapter.getItem(timeSlotTabLayout.getSelectedTabPosition());
                highLightCurrentTab(timeSlotTabLayout.getSelectedTabPosition());
                //This refreshes all the fragments
                mSlotCategoryViewPagerAdapter.notifyDataSetChanged();
                switch (tab.getPosition()) {
                    case 0:
                        if (REBaseActivity.isFromReschedule)
                            params.putString("eventCategory", "Motorcycles-Reschedule");
                        else
                            params.putString("eventCategory", "Motorcycles-Schedule a service");
                        params.putString("eventAction", getResources().getString(R.string.text_label_show_available_slots));
                        if( getActivity() instanceof CalendarViewActivity)
                            params.putString("eventAction", "Calendar icon click");
                        else
                            params.putString("eventAction", getResources().getString(R.string.text_label_show_available_slots));
                       REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                        break;
                    case 1:
                        if (REBaseActivity.isFromReschedule)
                            params.putString("eventCategory", "Motorcycles-Reschedule");
                        else
                            params.putString("eventCategory", "Motorcycles-Schedule a service");
                        if( getActivity() instanceof CalendarViewActivity)
                            params.putString("eventAction", "Calendar icon click");
                        else
                            params.putString("eventAction", getResources().getString(R.string.text_label_show_available_slots));
                        params.putString("eventLabel", "Afternoon");
                       REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);

                        break;
                    case 2:
                        if (REBaseActivity.isFromReschedule)
                            params.putString("eventCategory", "Motorcycles-Reschedule");
                        else
                            params.putString("eventCategory", "Motorcycles-Schedule a service");
                        if( getActivity() instanceof CalendarViewActivity)
                            params.putString("eventAction", "Calendar icon click");
                        else
                            params.putString("eventAction", getResources().getString(R.string.text_label_show_available_slots));
                        params.putString("eventLabel", "Evening");
                       REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /**
     * Highlights current tab if tab is changed
     *
     * @param position : int
     */
    private void highLightCurrentTab(int position) {
        for (int i = 0; i < timeSlotTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = timeSlotTabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mSlotCategoryViewPagerAdapter.getTabView(i));
        }
        TabLayout.Tab tab = timeSlotTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mSlotCategoryViewPagerAdapter.getSelectedTabView(position));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        timeSlotViewPager.setCurrentItem(position);
        mSlotCategoryViewPagerAdapter.getItem(timeSlotTabLayout.getSelectedTabPosition());
        highLightCurrentTab(timeSlotTabLayout.getSelectedTabPosition());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
