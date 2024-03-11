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
import com.royalenfield.reprime.ui.home.rides.adapter.RideStartTimeAdapter;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;

/**
 * This is the fragment which holds the time fragment
 */
public class RideStartTimeFragment extends REBaseFragment implements RidesListeners.RideTimeListener {

    private static RidesListeners.RideGetSelectedTimeListener mSelectedTimeListener;
    private final ArrayList<String> mTimes = new ArrayList<>();
    private RecyclerView mRideTimeRecyclerview;
    private RideStartTimeAdapter mAdapter;
    private ImageView mImageTimeMrng, mImageTimeNight;
    private int mSelectedTime = -1;

    public static RideStartTimeFragment newInstance(RidesListeners.RideGetSelectedTimeListener listener) {
        mSelectedTimeListener = listener;
        return new RideStartTimeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ride_starttime, container, false);
        initViews(rootView);
        if (mSelectedTime > -1) {
            highlightSelectedTime(mSelectedTime);
        }
        return rootView;
    }

    /**
     * Initialising views
     *
     * @param rootView :View
     */
    private void initViews(View rootView) {
        mImageTimeMrng = rootView.findViewById(R.id.imageView_mrng_time);
        mImageTimeNight = rootView.findViewById(R.id.imageView_night_time);
        mRideTimeRecyclerview = rootView.findViewById(R.id.recyclerView_ride_starttime);
        mRideTimeRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false));
        ((SimpleItemAnimator) mRideTimeRecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        for (int i = 0; i <= 9; i++) {
            mTimes.add("0" + i + ":00");
        }
        for (int i = 10; i <= 23; i++) {
            mTimes.add(i + ":00");
        }
        mAdapter = new RideStartTimeAdapter(getContext(), mTimes, true, mSelectedTimeListener, -1);
        mRideTimeRecyclerview.setAdapter(mAdapter);

    }

    private void highlightSelectedTime(int time) {
        //mAdapter = new RideStartTimeAdapter(getContext(), mTimes, true, mSelectedTimeListener, time - 1);
        mAdapter = new RideStartTimeAdapter(getContext(), mTimes, true, mSelectedTimeListener, time);
        mRideTimeRecyclerview.setAdapter(mAdapter);
        if (time > -1) {
            mRideTimeRecyclerview.scrollToPosition(time - 1);
            if (time <= 06 || time >= 18) {
                mImageTimeMrng.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_morning_disabled));
                mImageTimeNight.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_night_enabled));
            } else {
                mImageTimeMrng.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_morning));
                mImageTimeNight.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_night));
            }
        } else {
            mImageTimeMrng.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_morning_disabled));
            mImageTimeNight.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_night));
        }
    }


    @Override
    public void rideTimeEnable() {
        highlightSelectedTime(-1);
    }

    @Override
    public void rideEndTimeEnable() {

    }

    @Override
    public void rideTimeDisable() {
        mAdapter = new RideStartTimeAdapter(getContext(), mTimes, false, mSelectedTimeListener, -1);
        mRideTimeRecyclerview.setAdapter(mAdapter);
        Drawable mTimeMrngDrawable = REUtils.changeDrawableColor(getContext(), R.drawable.ic_time_morning,
                getContext().getResources().getColor(R.color.white_50));
        mImageTimeMrng.setImageDrawable(mTimeMrngDrawable);
        Drawable mTimeNightDrawable = REUtils.changeDrawableColor(getContext(), R.drawable.ic_time_night,
                getContext().getResources().getColor(R.color.white_50));
        mImageTimeNight.setImageDrawable(mTimeNightDrawable);
    }

    @Override
    public void rideEndTimeDisable() {

    }

    @Override
    public void changeSelectedTimeImage(String time) {
        String[] selectedTime = time.split(":");
        int iTime = Integer.parseInt(selectedTime[0]);
        if (iTime >= 06 && iTime <= 17) {
            mImageTimeMrng.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_morning));
            mImageTimeNight.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_night));
        } else {
            mImageTimeMrng.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_morning_disabled));
            mImageTimeNight.setImageDrawable(getResources().getDrawable(R.drawable.ic_time_night_enabled));
        }

    }

    @Override
    public void changeSelectedEndTimeImage(String time) {

    }

    @Override
    public void selectTimeForModifyRide(String time) {
        String[] selectedTime = time.split(":");
        mSelectedTime = Integer.parseInt(selectedTime[0]);
    }

    @Override
    public void selectEndTimeForModifyRide(String time) {

    }
}
