package com.royalenfield.reprime.ui.home.rides.fragment.ridestour;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.ui.home.rides.adapter.RidesTourItineraryAdapter;
import com.royalenfield.reprime.ui.home.rides.presenter.RidesTourPresenter;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RERecyclerTouchListener;

import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_MARQUEE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_POPULAR;

/**
 * ItineraryFragment holds the itenerary of the ride
 */
public class ItineraryFragment extends REBaseFragment {

    private RecyclerView mRecyclerViewInspirationRideList;
    private int mPosition;
    private String mRideType;
    private TextView mTextTitle;

    public static ItineraryFragment newInstance() {
        return new ItineraryFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ridertour_key_places, container, false);
        getBundleData();
        mTextTitle = view.findViewById(R.id.tv_label_ride_members);
        setTitle();
        mRecyclerViewInspirationRideList = view.findViewById(R.id.rides_keyplaces_list);
        mRecyclerViewInspirationRideList.addOnItemTouchListener(new RERecyclerTouchListener());
        mRecyclerViewInspirationRideList.setNestedScrollingEnabled(false);
        showItineraryRidesList();
        return view;
    }

    /**
     * Sets title based on Type
     */
    private void setTitle() {
        if (mRideType.equals(RIDE_TYPE_POPULAR) || mRideType.equals(RIDE_TYPE_MARQUEE)) {
            mTextTitle.setText(R.string.text_itinerary);
        } else {
            mTextTitle.setText(R.string.text_events_and_schedules);
        }
    }

    /**
     * Gets and sets the bundle data
     */
    private void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mPosition = bundle.getInt(REConstants.RIDES_LIST_POSITION, 0);
            mRideType = bundle.getString(RIDE_TYPE, null);
        }
    }


    /***
     * This function used to populate the inspiration coastal rides itinerary View model
     */
    private void showItineraryRidesList() {
        RidesTourItineraryAdapter customAdapter = new RidesTourItineraryAdapter(getContext(), new RidesTourPresenter(),
                mRideType, mPosition, REConstants.ITINERARY);
        mRecyclerViewInspirationRideList.setAdapter(customAdapter); // set the Adapter to RecyclerView

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewInspirationRideList.setLayoutManager(mLayoutManager);
        mRecyclerViewInspirationRideList.setItemAnimator(new DefaultItemAnimator());
    }
}