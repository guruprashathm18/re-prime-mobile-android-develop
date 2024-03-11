package com.royalenfield.reprime.ui.home.rides.fragment.dealer;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.remoteconfig.ConfigFeatures;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.home.rides.fragment.popular.PopularRideFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.usercreatedrides.UserCreatedRidesHomeFragment;
import com.royalenfield.reprime.ui.riderprofile.fragment.CustomRidesHomeFragment;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;


/**
 * Rides Fragment contains Popular, Marquee, User, Dealer rides
 * A simple {@link Fragment} subclass.
 * Use the {@link RERidesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RERidesFragment extends REBaseFragment {

    ConstraintLayout mRides;

    public RERidesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RERidesFragment.
     */
    public static RERidesFragment newInstance() {
        return new RERidesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rides, container, false);
        initViews(rootView);
        return rootView;
    }

    /**
     * Initialize the Views.
     *
     * @param view root view.
     */
    private void initViews(View view) {
        mRides = view.findViewById(R.id.constraint_rides);
        //ConfigFeatures configFeature = REUtils.getConfigFeatures();
      /*  if (configFeature != null && configFeature.getCommunityrides() != null &&
                configFeature.getCommunityrides().getReleaseFeatureStatus().
                        equalsIgnoreCase(REConstants.FEATURE_STATUS_DISABLED)) {*/
        if (REUtils.getFeatures().getDefaultFeatures().getIs_community_rides_enabled().equalsIgnoreCase(REConstants.FEATURE_DISABLED)) {
            mRides.setVisibility(View.GONE);
        } else {
            mRides.setVisibility(View.VISIBLE);
            loadRidesFragments(view);
        }
    }

    private void loadRidesFragments(View view) {
        //Views
      //  Log.e("test", "rides fragment called");
        FragmentFrameHolder mDealerRidesFrame = view.findViewById(R.id.fl_dealer_rides);
        FragmentFrameHolder mPopularRidesFrame = view.findViewById(R.id.fl_marquee_rides_highlight);
        FragmentFrameHolder mMarqueeRidesFrame = view.findViewById(R.id.fl_marquee_rides_tour);
        FragmentFrameHolder mUserCreatedRidesFrame = view.findViewById(R.id.fl_user_created_rides);


        //Initialising popular rides fragment
        PopularRideFragment mPopularRidesFragment = PopularRideFragment.newInstance();
        //mPopularRidesFrame.loadFragment(getActivity(), mPopularRidesFragment, null);

        //Initializing MarqueeRidesList
        CustomRidesHomeFragment mPreviousRidesFragment = new CustomRidesHomeFragment();
        Bundle args = new Bundle();
        args.putString(REConstants.RIDECUSTOM_VIEWTYPE, REConstants.MARQUEE_RIDE);
        mPreviousRidesFragment.setArguments(args);
        mMarqueeRidesFrame.loadFragment(getActivity(), mPreviousRidesFragment, null);

        FirestoreManager.getInstance().removeDealerRidesListener();

        //Initializing DealerRidesList
        DealerRidesHomeFragment mDealerRidesFragment = new DealerRidesHomeFragment();
        mDealerRidesFrame.loadFragment(getActivity(), mDealerRidesFragment, null);

        FirestoreManager.getInstance().removeUserRidesListener();

        //Initializing UserCreatedRidesList
        UserCreatedRidesHomeFragment mUserCreatedRidesFragment = new UserCreatedRidesHomeFragment();
        mUserCreatedRidesFrame.loadFragment(getActivity(), mUserCreatedRidesFragment, null);
    }


    /***
     * load the fragment one by one without interrupting other fragments
     * @param message used to handle which fragment will be loaded
     */
    /*public void displayReceivedData(int message) {
        switch (message) {
            case 1:
                mMarqueeRidesFragment = MarqueeRidesFragment.newInstance();
                mMarqueeRidesFrame.loadFragment(getActivity(), mMarqueeRidesFragment, null);
                break;
            case 2:
                mDealerRidesFragment = DealerRidesFragment.newInstance("", "");
                mDealerRidesFrame.loadFragment(getActivity(), mDealerRidesFragment, null);
                break;
            case 3:
                //TODO API implementation
                mUserCreatedRidesFragment = UserCreatedRidesFragment.newInstance();
                mUserCreatedRidesFrame.loadFragment(getActivity(), mUserCreatedRidesFragment, null);
                //TODO API implementation
                //Once the API implementation is ready we will spilt the switch case
                mCategoryRideFragment = CategoryRideFragment.newInstance();
                mInspirationRidesFrame.loadFragment(getActivity(), mCategoryRideFragment, null);
                break;
        }
    }*/
}
