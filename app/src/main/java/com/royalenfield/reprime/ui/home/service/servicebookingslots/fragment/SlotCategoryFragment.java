package com.royalenfield.reprime.ui.home.service.servicebookingslots.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.home.service.listener.RecyclerViewClickListener;
import com.royalenfield.reprime.ui.home.service.search.CalendarViewActivity;
import com.royalenfield.reprime.ui.home.service.servicebookingslots.adapter.TimeSlotWithIntervalAdapter;
import com.royalenfield.reprime.ui.home.service.servicebookingslots.presenter.TimeSlotWithIntervalPresenter;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REGridSpacingItemDecoration;
import com.royalenfield.reprime.utils.REUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * SlotCategoryFragment to display time slot based on slotType.
 * slotType would be,
 * Morning :: 9am to 11.45
 * AfterNoon :: 12pm to 15:45 pm
 * Evening :: 16pm to 5.45pm
 */

public class SlotCategoryFragment extends Fragment implements RecyclerViewClickListener, View.OnClickListener {
    private ArrayList timeSlotList = null;
    private List<String> slots;
    private int clickedPosition = -1;
    private TextView mTextViewNoSlots;
    private Button chooseSlot;
    private RecyclerView timeSlotRecycleView;
    private TimeSlotListener mTimeSlotListener;
    private String slotName;


    public SlotCategoryFragment() {
    }

    /**
     * SlotCategoryFragment instance to get slotName and TimeSlotList from SlotCategoryViewPagerAdapter
     *
     * @param slotName :: slotType (Morning,AfterNoon,Evening).
     * @param timeSlot ::List<String> (timeSlotList of String type).
     * @return SlotCategoryFragment instance
     */
    public static SlotCategoryFragment newInstance(String slotName, ArrayList<String> timeSlot) {
        SlotCategoryFragment mSlotCategoryFragment = new SlotCategoryFragment();
        Bundle args = new Bundle();
        args.putString("slotName", slotName);
        args.putStringArrayList("timeSlotList", timeSlot);
        mSlotCategoryFragment.setArguments(args);
        return mSlotCategoryFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mTimeSlotListener = (TimeSlotListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e.getMessage());
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            timeSlotList = getArguments().getStringArrayList("timeSlotList");
            slotName = getArguments().getString("slotName");
        }
        return inflater.inflate(R.layout.fragment_time_slot, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        getTimeSlotListBasedOnSlotTag();
    }

    /**
     * initialize the views.
     *
     * @param view : View
     */
    private void initializeViews(View view) {
        timeSlotRecycleView = view.findViewById(R.id.rv_time_slot);
        mTextViewNoSlots = view.findViewById(R.id.textView_noslots);
        chooseSlot = view.findViewById(R.id.choose_slot);
    }

    /**
     * Method to get the timeSlotList based on SlotTag.
     * SlotTags would be,
     * Morning :: 9am to 11.45
     * AfterNoon :: 12pm to 15:45 pm
     * Evening :: 16pm to 5.45pm
     */
    private void getTimeSlotListBasedOnSlotTag() {
        slots = new TimeSlotWithIntervalPresenter().getSlots(slotName, timeSlotList);
        if (slots.size() > 0) {
            bindTimeSlotsToRecyclerView(slots);
            mTextViewNoSlots.setVisibility(View.GONE);
            chooseSlot.setVisibility(View.VISIBLE);
            chooseSlot.setTextColor(getResources().getColor(R.color.white_50));
            chooseSlot.setBackground(getResources().getDrawable(R.drawable.button_border_disable));
        } else {
            timeSlotRecycleView.setVisibility(View.GONE);
            mTextViewNoSlots.setVisibility(View.VISIBLE);
            chooseSlot.setVisibility(View.GONE);
        }
    }


    /**
     * Binds the slot list to recycleView
     *
     * @param timeSlots : List<String>

     */

    private void bindTimeSlotsToRecyclerView(List<String> timeSlots) {
        timeSlotRecycleView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 4));
        timeSlotRecycleView.setItemAnimator(new DefaultItemAnimator());
        TimeSlotWithIntervalAdapter mTimeSlotWithIntervalAdapter = new TimeSlotWithIntervalAdapter(getActivity().
                getApplicationContext(), timeSlots, this);
        mTimeSlotWithIntervalAdapter.setSelectedTimeSlotEnabled(-1);
        int spacing = Math.round(0 * getResources().getDisplayMetrics().density);
        timeSlotRecycleView.addItemDecoration(new REGridSpacingItemDecoration(4, spacing, false));
        //timeSlotRecycleView.setHasFixedSize(true);
        //timeSlotRecycleView.setNestedScrollingEnabled(false);
        timeSlotRecycleView.setAdapter(mTimeSlotWithIntervalAdapter);

    }

    /**
     * on click of timeSlot value selected value & position will be returned.As per new UI no
     * need pass selected timeSlot value on click of timeSlot
     *
     * @param position : int
     */
    @Override
    public void onTimeClick(int position) {
        Bundle params = new Bundle();
        if(REBaseActivity.isFromReschedule){
            params.putString("eventCategory", "Motorcycles-Reschedule");
            params.putString("eventAction", getResources().getString(R.string.text_label_show_available_slots));
            params.putString("eventLabel", slots != null ? slots.get(position) + "" : "");
        }
        else {
            params.putString("eventCategory", "Motorcycles-Schedule a service");
            params.putString("eventAction", slots != null ? slots.get(position) + "" : "");
            params.putString("eventLabel", "Time click");
        }
       REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
        clickedPosition = position;
        chooseSlot.setBackground(getResources().getDrawable(R.drawable.button_border_enable));
        chooseSlot.setTextColor(getResources().getColor(R.color.white));
        chooseSlot.setOnClickListener(this);
    }

    @Override
    public void onItemClick(int value, boolean isSelected) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.choose_slot) {
            if (slots != null && clickedPosition != -1) {
                Bundle params=new Bundle();
                if(REBaseActivity.isFromReschedule)
                params.putString("eventCategory", "Motorcycles-Reschedule");
                else
                    params.putString("eventCategory", "Motorcycles-Schedule a service");
               if( getActivity() instanceof CalendarViewActivity)
                   params.putString("eventAction", "Calendar icon click");
else
                   params.putString("eventAction", getResources().getString(R.string.text_label_show_available_slots));
                params.putString("eventLabel", "Choose slot click");
               REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                //onClick of chooseSlot selected timeSloValue will be passed.
                mTimeSlotListener.selectedTimeSlot(slots.get(clickedPosition), clickedPosition);
            } else {
                REUtils.showErrorDialog(getContext(), "Please select slot");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTimeSlotListener = null;
    }

    /**
     * TimeSlotListener to return selected time values to TimeSlotViewActivity
     */
    public interface TimeSlotListener {
        void selectedTimeSlot(String slotValue, int selectedPosition);
    }
}
