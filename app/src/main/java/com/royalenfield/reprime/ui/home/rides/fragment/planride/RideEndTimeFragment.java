package com.royalenfield.reprime.ui.home.rides.fragment.planride;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.ui.home.rides.adapter.RideEndTimeAdapter;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;

/**
 * This is the fragment which holds the time fragment
 */
public class RideEndTimeFragment extends REBaseFragment implements RidesListeners.RideTimeListener {

    private static RidesListeners.RideGetSelectedTimeListener mSelectedTimeListener;
    private final ArrayList<String> mTimes = new ArrayList<>();
    private RecyclerView mRideEndTimeRecyclerview;
    private RideEndTimeAdapter rideEndTimeAdapter;
    private ImageView mImageEndTimeMrng, mImageEndTimeNight;
    private int mSelectedEndTime = -1;

    public static RideEndTimeFragment newInstance(RidesListeners.RideGetSelectedTimeListener listener) {
        mSelectedTimeListener = listener;
        return new RideEndTimeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ride_endtime, container, false);
        initViews(rootView);
        if (mSelectedEndTime > -1) {
            highlightSelectedEndTime(mSelectedEndTime);
        }
        return rootView;
    }

    /**
     * Initialising views
     *
     * @param rootView :View
     */
    private void initViews(View rootView) {
        mImageEndTimeMrng = rootView.findViewById(R.id.imageView_mrng_endTime);
        mImageEndTimeNight = rootView.findViewById(R.id.imageView_night_EndTime);
        mRideEndTimeRecyclerview = rootView.findViewById(R.id.recyclerView_ride_endtime);
        mRideEndTimeRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false));
        ((SimpleItemAnimator) mRideEndTimeRecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        for (int i = 0; i <= 9; i++) {
            mTimes.add("0" + i + ":00");
        }
        for (int i = 10; i <= 23; i++) {
            mTimes.add(i + ":00");
        }
        rideEndTimeAdapter = new RideEndTimeAdapter(getContext(), mTimes, true, mSelectedTimeListener, -1);
        mRideEndTimeRecyclerview.setAdapter(rideEndTimeAdapter);

    }

    private void highlightSelectedEndTime(int time) {
        //mAdapter = new RideStartTimeAdapter(getContext(), mTimes, true, mSelectedTimeListener, time - 1);
        rideEndTimeAdapter = new RideEndTimeAdapter(getContext(), mTimes, true, mSelectedTimeListener, time);
        mRideEndTimeRecyclerview.setAdapter(rideEndTimeAdapter);
        if (time > -1) {
            mRideEndTimeRecyclerview.scrollToPosition(time - 1);
            if (time <= 06 || time >= 18) {
                mImageEndTimeMrng.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_morning_disabled));
                mImageEndTimeNight.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_night_enabled));
            } else {
                mImageEndTimeMrng.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_morning));
                mImageEndTimeNight.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_night));
            }
        } else {
            mImageEndTimeMrng.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_morning_disabled));
            mImageEndTimeNight.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_night));
        }
    }


    @Override
    public void rideTimeEnable() {

    }

    @Override
    public void rideEndTimeEnable() {
        highlightSelectedEndTime(-1);
    }

    @Override
    public void rideTimeDisable() {

    }

    @Override
    public void rideEndTimeDisable() {
        rideEndTimeAdapter = new RideEndTimeAdapter(getContext(), mTimes, false, mSelectedTimeListener, -1);
        mRideEndTimeRecyclerview.setAdapter(rideEndTimeAdapter);
        Drawable mTimeMrngDrawable = REUtils.changeDrawableColor(getContext(), R.drawable.ic_time_morning,
                getContext().getResources().getColor(R.color.white_50));
        mImageEndTimeMrng.setImageDrawable(mTimeMrngDrawable);
        Drawable mTimeNightDrawable = REUtils.changeDrawableColor(getContext(), R.drawable.ic_time_night,
                getContext().getResources().getColor(R.color.white_50));
        mImageEndTimeNight.setImageDrawable(mTimeNightDrawable);
    }

    @Override
    public void changeSelectedTimeImage(String time) {

    }

    @Override
    public void changeSelectedEndTimeImage(String time) {
        String[] selectedTime = time.split(":");
        int iTime = Integer.parseInt(selectedTime[0]);
        if (iTime >= 06 && iTime <= 17) {
            mImageEndTimeMrng.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_morning));
            mImageEndTimeNight.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_night));
        } else {
            mImageEndTimeMrng.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_morning_disabled));
            mImageEndTimeNight.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_night_enabled));
        }

    }

    @Override
    public void selectTimeForModifyRide(String time) {

    }

    @Override
    public void selectEndTimeForModifyRide(String time) {
        String[] selectedTime = time.split(":");
        mSelectedEndTime = Integer.parseInt(selectedTime[0]);
    }
}
