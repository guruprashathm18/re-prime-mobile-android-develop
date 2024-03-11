package com.royalenfield.reprime.ui.home.rides.fragment.dealer;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.home.rides.fragment.popular.PopularRideFragment;
import com.royalenfield.reprime.ui.home.rides.fragment.ridecategory.CategoryRideFragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RidesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RidesFragment extends REBaseFragment {

    public static int INT_MARQUEE_RIDE = 1;
    public static int INT_DEALER_RIDE = 2;
    public static int INT_USER_RIDE = 3;
    private final String TAG = RidesFragment.class.getSimpleName();

    //Views
    private FragmentFrameHolder mDealerRidesFrame;
    private FragmentFrameHolder mPopularRidesFrame;
    private FragmentFrameHolder mMarqueeRidesFrame;
    private FragmentFrameHolder mUserCreatedRidesFrame;
    private FragmentFrameHolder mInspirationRidesFrame;
    //Fragments
    //private DealerRidesFragment mDealerRidesFragment;
    private PopularRideFragment mPopularRidesFragment;
    //private MarqueeRidesFragment mMarqueeRidesFragment;
    //private UserCreatedRidesFragment mUserCreatedRidesFragment;
    private CategoryRideFragment mCategoryRideFragment;

    public RidesFragment() {
        // Required empty public constructor
    }


    public static RidesFragment newInstance(String param1, String param2) {
        return new RidesFragment();
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
        mDealerRidesFrame = view.findViewById(R.id.fl_dealer_rides);
        mPopularRidesFrame = view.findViewById(R.id.fl_marquee_rides_highlight);
        mMarqueeRidesFrame = view.findViewById(R.id.fl_marquee_rides_tour);
        mUserCreatedRidesFrame = view.findViewById(R.id.fl_user_created_rides);
        mInspirationRidesFrame = view.findViewById(R.id.fl_rides_category);

        mPopularRidesFragment = PopularRideFragment.newInstance();
        mPopularRidesFrame.loadFragment(getActivity(), mPopularRidesFragment, null);

    }

    /***
     * load the fragment one by one without interrupting other fragments
     * @param message used to handle which fragment will be loaded
     */
/*    public void displayReceivedData(int message) {
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
