package com.royalenfield.reprime.ui.home.rides.fragment.ridestour;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.home.rides.adapter.RidesTourKeyPlacesAdapter;
import com.royalenfield.reprime.ui.home.rides.presenter.RidesTourPresenter;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RERecyclerTouchListener;

import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE;

/**
 * RidesTourKeyPlacesFragment holds the waypoints of the ride
 */
public class RidesTourKeyPlacesFragment extends REBaseFragment {

    private int mPosition;
    private String mRideType,mRideTitle;
    private View mSeperator;
    private TextView mTitleHeader;

    public RidesTourKeyPlacesFragment() {
        // Required empty public constructor
    }

    public static RidesTourKeyPlacesFragment newInstance() {
        return new RidesTourKeyPlacesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ridertour_key_places, container, false);
        getBundleData();
        initViews(view);
        return view;
    }

    /**
     * Gets and sets the bundle data
     */
    private void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mPosition = bundle.getInt(REConstants.RIDES_LIST_POSITION, 0);
            mRideType = bundle.getString(RIDE_TYPE, "");
            mRideTitle = bundle.getString("RIDE_TITLE", "");
        }
    }


    /**
     * Initialising views
     *
     * @param view : View
     */
    private void initViews(View view) {
        mTitleHeader = view.findViewById(R.id.tv_label_ride_members);
        mTitleHeader.setText("Key Places of "+mRideTitle +" :");
        mSeperator = view.findViewById(R.id.rider_invited_separator);
        manageViews();
        RecyclerView mRideTourPlacesRecyclerView = view.findViewById(R.id.rides_keyplaces_list);
        mRideTourPlacesRecyclerView.setNestedScrollingEnabled(false);
        mRideTourPlacesRecyclerView.addOnItemTouchListener(new RERecyclerTouchListener());
        RecyclerView.LayoutManager mCoastalPlacesLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        mRideTourPlacesRecyclerView.setLayoutManager(mCoastalPlacesLayoutManager);

        RidesTourKeyPlacesAdapter ridesTourKeyPlacesAdapter = new RidesTourKeyPlacesAdapter(getActivity(),
                new RidesTourPresenter(), mRideType, mPosition, REConstants.KEY_PLACES);
        mRideTourPlacesRecyclerView.setAdapter(ridesTourKeyPlacesAdapter);
    }

    /**
     * Manage views base on type
     */
    private void manageViews() {
        if (mRideType.equals(REConstants.RIDE_TYPE_POPULAR)) {
            if (RERidesModelStore.getInstance().getPopularRidesResponse().get(mPosition).getWaypoints() != null &&
                    RERidesModelStore.getInstance().getPopularRidesResponse().get(mPosition).getWaypoints().size() == 0) {
                mTitleHeader.setVisibility(View.GONE);
                mSeperator.setVisibility(View.GONE);
            }
        } else if (mRideType.equals(REConstants.RIDE_TYPE_MARQUEE)) {
            if (RERidesModelStore.getInstance().getMarqueeRidesResponse().get(mPosition).getWaypoints() != null &&
                    RERidesModelStore.getInstance().getMarqueeRidesResponse().get(mPosition).getWaypoints().size() == 0) {
                mTitleHeader.setVisibility(View.GONE);
                mSeperator.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
