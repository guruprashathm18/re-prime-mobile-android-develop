package com.royalenfield.reprime.ui.riderprofile.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.Nullable;

/**
 * A simple {@link androidx.fragment.app.Fragment} subclass.
 */
public class RidesProfileFragment extends REBaseFragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = RidesProfileFragment.class.getSimpleName();
    private Spinner mSpinner;
    private FragmentFrameHolder mProfileRidesFrame;
    private int mSpinnerSelectedPosition = -1;
    private static final int TYPE_PENDING_RIDE = 0;
    private static final int TYPE_UPCOMING_RIDE = 1;
    private static final int TYPE_PAST_RIDE = 2;
    private static final int TYPE_REJECTED_RIDE = 3;


    public RidesProfileFragment() {
        // Required empty public constructor
    }

    public static RidesProfileFragment newInstance() {
        // Required empty public constructor
        return new RidesProfileFragment();
    }

    /**
     * This broadcast receives all the pending rides snapshots from profile rides
     */
    private BroadcastReceiver mProfilePendingRidesBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (REConstants.FIRESTORE_PENDING_RIDE.equals(intent.getAction())) {
                    if (mSpinnerSelectedPosition == TYPE_PENDING_RIDE && mSpinner != null) {
                        //Set spinner
                        bindData(TYPE_PENDING_RIDE);
                    }
                }
            }
        }
    };

    /**
     * This broadcast receives all the upcoming rides snapshots from profile rides
     */
    private BroadcastReceiver mProfileUpcomingRidesBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (REConstants.FIRESTORE_UPCOMING_RIDE.equals(intent.getAction())) {
                    if (mSpinnerSelectedPosition == TYPE_UPCOMING_RIDE && mSpinner != null) {
                        bindData(TYPE_UPCOMING_RIDE);
                    }
                }
            }
        }
    };

    /**
     * This broadcast receives all the past rides snapshots from profile rides
     */
    private BroadcastReceiver mProfilePastRidesBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (REConstants.FIRESTORE_PAST_RIDE.equals(intent.getAction())) {
                    if (mSpinnerSelectedPosition == TYPE_PAST_RIDE && mSpinner != null) {
                        bindData(TYPE_PAST_RIDE);
                    }
                }
            }
        }
    };

    /**
     * This broadcast receives all the rejected rides snapshots from profile rides
     */
    private BroadcastReceiver mProfileRejectedRidesBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (REConstants.FIRESTORE_REJECTED_RIDE.equals(intent.getAction())) {
                    if (mSpinnerSelectedPosition == TYPE_REJECTED_RIDE && mSpinner != null) {
                        bindData(TYPE_REJECTED_RIDE);
                    }
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rider_profile, container, false);
        initViews(rootView);
        registerProfileRidesBroadcast();
        return rootView;
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Loading the UI in Handler because it lags if we load in MainThread
        new Handler().postDelayed(() -> {
            //Binds data to views.....
            bindData(0); //Setting first position to spinner
        },1000);
    }

    /**
     * Broadcast receivers for listening the profile rides update
     */
    private void registerProfileRidesBroadcast() {
        if (getContext() != null) {
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mProfilePastRidesBroadcast,
                    new IntentFilter(REConstants.FIRESTORE_PAST_RIDE));
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mProfileUpcomingRidesBroadcast,
                    new IntentFilter(REConstants.FIRESTORE_UPCOMING_RIDE));
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mProfilePendingRidesBroadcast,
                    new IntentFilter(REConstants.FIRESTORE_PENDING_RIDE));
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mProfileRejectedRidesBroadcast,
                    new IntentFilter(REConstants.FIRESTORE_REJECTED_RIDE));
        }
    }

    /**
     * Initialising views and fragments
     *
     * @param rootView : rootView
     */
    private void initViews(View rootView) {
        mProfileRidesFrame = rootView.findViewById(R.id.fl_upcoming_rides);
        mSpinner = rootView.findViewById(R.id.spinner_rides_list);
    }

    /**
     * Binds the rides profile data to views.....
     */
    private void bindData(int position) {
        if (getContext() != null) {
            String[] rideList = getResources().getStringArray(R.array.profile_rides_types);
            //Set Spinner adapter for the bike list..
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getContext(), R.layout.layout_spinner_item, rideList);
            mSpinner.setAdapter(adapter);
            mSpinner.setSelection(position);
            mSpinner.setOnItemSelectedListener(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Unregister the broadcast reciever
        if (getContext() != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mProfilePastRidesBroadcast);
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mProfileUpcomingRidesBroadcast);
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mProfilePendingRidesBroadcast);
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mProfileRejectedRidesBroadcast);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSpinnerSelectedPosition = position;
        ProfileRidesFragment mProfileRidesFragment = ProfileRidesFragment.newInstance();
        Bundle args = new Bundle();
        switch (position) {
            case TYPE_PENDING_RIDE:
                args.putString(REConstants.RIDECUSTOM_VIEWTYPE, REConstants.PENDINGRIDE_TYPE);
                break;
            case TYPE_UPCOMING_RIDE:
                args.putString(REConstants.RIDECUSTOM_VIEWTYPE, REConstants.UPCOMIMGRIDE_TYPE);
                break;
            case TYPE_PAST_RIDE:
                args.putString(REConstants.RIDECUSTOM_VIEWTYPE, REConstants.PASTRIDE_TYPE);
                break;
            case TYPE_REJECTED_RIDE:
                args.putString(REConstants.RIDECUSTOM_VIEWTYPE, REConstants.REJECTEDRIDE_TYPE);
                break;
            default:
                break;
        }
        Bundle  params = new Bundle();
        params.putString("eventCategory", "User Profile");
        params.putString("eventAction", "Rides clicked");
        params.putString("eventLabel", args.getString(REConstants.RIDECUSTOM_VIEWTYPE));
        REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);
        mProfileRidesFragment.setArguments(args);
        mProfileRidesFrame.loadFragment(getActivity(), mProfileRidesFragment, null);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
