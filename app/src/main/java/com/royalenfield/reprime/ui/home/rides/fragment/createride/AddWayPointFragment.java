package com.royalenfield.reprime.ui.home.rides.fragment.createride;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.ui.home.rides.activity.PlanRideActivity;
import com.royalenfield.reprime.ui.home.rides.activity.WayPointSearchActivity;
import com.royalenfield.reprime.ui.home.rides.adapter.WayPointAdapter;
import com.royalenfield.reprime.ui.riderprofile.activity.ModifyRidesActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;

/**
 * Fragment for Adding waypoints
 */
public class AddWayPointFragment extends REBaseFragment implements View.OnClickListener,
        WayPointAdapter.OnEditTextFocusListener {

    private static final int REQUESTCODE_WAYPOINT = 1;
    private static final int REQUESTCODE_DESTINATION_WAYPOINT = 2;
    private static final int REQUESTCODE_SOURCE_WAYPOINT = 3;
    private IPlanRideWayPointSearchListener mPlanRideWayPointSearchListener;
    private ArrayList<WayPointsData> mWayPointsList = new ArrayList<>();
    private WayPointAdapter mAdapter;
    private String mRideType;
    private TextView mTextRideTitle, mAddWayPoint;
    private View mDivider;

    public static AddWayPointFragment newInstance() {
        return new AddWayPointFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IPlanRideWayPointSearchListener)
            mPlanRideWayPointSearchListener = (IPlanRideWayPointSearchListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_waypoint, container, false);
        initViews(rootView);
        return rootView;
    }

    /**
     * Initialising views
     *
     * @param rootView :View
     */
    private void initViews(View rootView) {
        getBundleData();
        updateRouteForInRide();
        mTextRideTitle = rootView.findViewById(R.id.textView_ride);
        mDivider = rootView.findViewById(R.id.tv_divider_location);
        RecyclerView mWayPointRecyclerView = rootView.findViewById(R.id.recyclerView_waypoints);
        mWayPointRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false));
        ((SimpleItemAnimator) mWayPointRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter = new WayPointAdapter(getContext(), mWayPointsList, this, mRideType);
        mWayPointRecyclerView.setAdapter(mAdapter);
        mAddWayPoint = rootView.findViewById(R.id.textView_addwaypoint);
        mAddWayPoint.setOnClickListener(this);
        manageViews();

    }

    /**
     * This method manages the views based on the ride type passed
     */
    private void manageViews() {
        switch (mRideType) {
            case REConstants.WAYPOINT_INRIDE:
                mTextRideTitle.setText(R.string.your_ride);
                mAddWayPoint.setVisibility(View.GONE);
                break;
            case REConstants.WAYPOINT_UPCOMINGRIDE:
                mTextRideTitle.setText(R.string.your_ride);
                mAddWayPoint.setVisibility(View.GONE);
                mDivider.setVisibility(View.GONE);
                break;
            case REConstants.WAYPOINT_PLANRIDE:
                mTextRideTitle.setText(R.string.text_rideplan_waypoint);
                mAddWayPoint.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * This method updates the route for In-Ride map
     */
    private void updateRouteForInRide() {
        if (mRideType.equals(REConstants.WAYPOINT_INRIDE)) {
            mPlanRideWayPointSearchListener.updateRoute(mWayPointsList);
        }
    }


    private void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mWayPointsList = bundle.getParcelableArrayList(REConstants.WAY_POINTS_LIST);
            mRideType = bundle.getString(REConstants.RIDE_WAYPOINT_TYPE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.textView_addwaypoint) {
            if(mWayPointsList.size() < 25) {
            Intent intent = new Intent(getContext(), WayPointSearchActivity.class);
                intent.putExtra("waypoint_type", "waypoint");
                intent.putExtra("navigation_type", "waypoint");
            startActivityForResult(intent, REQUESTCODE_WAYPOINT);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
            } else {
                REUtils.showErrorDialog(getActivity(),"Limit Reached, You cannot add a waypoint");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String mPLace = null;
        double latitude = 0, longitude = 0;
        if (data != null) {
            mPLace = data.getStringExtra("place");
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
        }
        // Updating waypoints
        WayPointsData wayPointData =
                new WayPointsData(latitude, longitude, mPLace);
        if (resultCode == REQUESTCODE_WAYPOINT && mPLace != null) {
            mWayPointsList.add(mWayPointsList.size() - 1, wayPointData);
            mAdapter.notifyDataSetChanged();
        } else if (resultCode == REQUESTCODE_DESTINATION_WAYPOINT && mPLace != null) {
            mWayPointsList.set(mWayPointsList.size() - 1, wayPointData);
            mAdapter.notifyDataSetChanged();
            if (getContext() instanceof PlanRideActivity) {
                ((PlanRideActivity) getContext()).showRecommendedRouteFragment();
            }
            if (getContext() instanceof ModifyRidesActivity) {
                ((ModifyRidesActivity) getContext()).showRecommendedRouteFragment();
            }
        } else if (resultCode == REQUESTCODE_SOURCE_WAYPOINT && mPLace != null) {
            mWayPointsList.set(0, wayPointData);
            mAdapter.notifyDataSetChanged();
        }
        /*if (!mRideType.equals(WAYPOINT_MODIFYRIDE))*/
        mPlanRideWayPointSearchListener.updateRoute(mWayPointsList);

    }

    @Override
    public void onEditTextFocusChange(boolean isDestination) {
        Intent intent = new Intent(getContext(), WayPointSearchActivity.class);
        if (isDestination) {
            intent.putExtra("waypoint_type", "destination");
            intent.putExtra("navigation_type", "waypoint");
            startActivityForResult(intent, REQUESTCODE_DESTINATION_WAYPOINT);
        } else {
            intent.putExtra("waypoint_type", "source");
            intent.putExtra("navigation_type", "waypoint");
            startActivityForResult(intent, REQUESTCODE_SOURCE_WAYPOINT);
        }

    }

    public interface IPlanRideWayPointSearchListener {
        void updateRoute(ArrayList<WayPointsData> wayPointResult);
    }


}
