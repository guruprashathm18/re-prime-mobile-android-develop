package com.royalenfield.reprime.ui.home.navigation.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.gms.maps.model.LatLng;
import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.response.remoteconfig.ConfigFeatures;
import com.royalenfield.reprime.ui.helper.SimpleItemTouchHelperCallback;
import com.royalenfield.reprime.ui.home.homescreen.fragments.RENavigationFragment;
import com.royalenfield.reprime.ui.home.navigation.activity.NavigationWayPointSearchActivity;
import com.royalenfield.reprime.ui.home.navigation.adapter.NavigationWayPointAdapter;
import com.royalenfield.reprime.ui.home.navigation.model.RecentLocation;
import com.royalenfield.reprime.ui.home.navigation.model.RecentRouteLocationManager;
import com.royalenfield.reprime.ui.home.navigation.utils.BCTUtils;
import com.royalenfield.reprime.ui.home.rides.activity.PlanRideActivity;
import com.royalenfield.reprime.ui.home.service.diy.activity.DoItYourSelfActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.ModifyRidesActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.royalenfield.reprime.utils.REConstants.KEY_TRIPPER_GTM;
import static com.royalenfield.reprime.utils.REConstants.NAVIGATION_SEARCH_ACTIVITY;
import static com.royalenfield.reprime.utils.REConstants.RIDE_WAYPOINT_TYPE;
import static com.royalenfield.reprime.utils.REConstants.WAYPOINT_NAVIGATION;

/**
 * Fragment for Adding waypoints
 */
public class NavigationAddWayPointFragment extends REBaseFragment implements View.OnClickListener,
        NavigationWayPointAdapter.OnEditTextFocusListener {

    public static final String TAG = NavigationAddWayPointFragment.class.getSimpleName();
    private static final int REQUESTCODE_WAYPOINT = 1;
    private static final int REQUESTCODE_DESTINATION_WAYPOINT = 2;
    private static final int REQUESTCODE_SOURCE_WAYPOINT = 3;
    private IPlanRideWayPointSearchListener mPlanRideWayPointSearchListener;
    private ArrayList<WayPointsData> mWayPointsList = new ArrayList<>();
    private NavigationWayPointAdapter mAdapter;
    private String mRideType;
    private TextView mAddWayPoint, mMyRoutes, mDIYTxt;
    private ConstraintLayout optionsConstraint;
    private View mDivider;
    private ConstraintLayout waypointLayout;
    private RecyclerView mWayPointRecyclerView;
    private boolean isReconstructedRoute;
    private ConfigFeatures configFeature;
    private boolean isMyRoutesDisabled = false;
    private ItemTouchHelper mItemTouchHelper;
    private FragmentActivity mContext;
    public static final String WAY_POINT_HINT = "Add Waypoint";
    private boolean isWayPointAdded = false, isFromSearch = false;
    private int wayPointPosition = 0;
    private LinearLayoutManager llm;
    private RENavigationFragment.Controls mControls = RENavigationFragment.Controls.EXPAND;
    private RelativeLayout mWayPointExpandLAY;
    private ImageView mWayPointExpandIMG;
    private boolean isExpand = false;
    private ArrayList<WayPointsData> mUpcomingWayPointList = new ArrayList<>();
    private boolean isEmptyWayPointAdded = false;
    private Handler mCollapseHandler;
    private boolean isDragMode = false;
    private int dragPosition = 0;

    public static NavigationAddWayPointFragment newInstance() {
        return new NavigationAddWayPointFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.navigation_fragment_add_waypoint, container, false);
        mContext = this.getActivity();
        initViews(rootView);
        return rootView;
    }

    public void setRouteListener(IPlanRideWayPointSearchListener routeListener) {
        mPlanRideWayPointSearchListener = routeListener;
    }

    /**
     * Initialising views
     *
     * @param rootView :View
     */
    private void initViews(View rootView) {
        getBundleData();
        waypointLayout = rootView.findViewById(R.id.waypoint_fragment);
        mDivider = rootView.findViewById(R.id.tv_divider_location);
        mWayPointRecyclerView = rootView.findViewById(R.id.recyclerView_waypoints);
        llm = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mWayPointRecyclerView.setLayoutManager(llm);
        ((SimpleItemAnimator) Objects.requireNonNull(mWayPointRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        mAdapter = new NavigationWayPointAdapter(getActivity(), mWayPointsList, this, false);
        mWayPointRecyclerView.setAdapter(mAdapter);
        optionsConstraint = rootView.findViewById(R.id.options_constraint);
        mAddWayPoint = rootView.findViewById(R.id.textView_addwaypoint);
        mMyRoutes = rootView.findViewById(R.id.tv_myRoutes);
        mDIYTxt = rootView.findViewById(R.id.textView_diy_videos);
        if (BuildConfig.FLAVOR.contains("Apac") || BuildConfig.FLAVOR.contains("Rena") || BuildConfig.FLAVOR.contains("Latm") || BuildConfig.FLAVOR.contains("EU"))
            mDIYTxt.setVisibility(GONE);
        else
            mDIYTxt.setVisibility(VISIBLE);
        mWayPointExpandLAY = rootView.findViewById(R.id.add_waypoint_expand_LAY);
        mWayPointExpandIMG = rootView.findViewById(R.id.add_waypoint_expand_IMG);
        mWayPointExpandLAY.setVisibility(View.GONE);
        createListeners();
        //manageViews();
        //Enable/Disable My Routes feature based on the remote configuration.
//        configFeature = REUtils.getConfigFeatures();
//        if (configFeature != null && configFeature.getRecordRoute() != null) {
//            if (configFeature.getRecordRoute().getFeatureStatus().equalsIgnoreCase(REConstants.FEATURE_STATUS_DISABLED)) {
//                // TODO Need to uncomment once BCT is enabled in Firebase remote config
////                isMyRoutesDisabled = true;
////                mMyRoutes.setAlpha(0.4f);
//            } else {
//                mMyRoutes.setAlpha(1f);
//            }
//        }

        // Enable/Disable My Routes feature based on the remote configuration.
        if (!BCTUtils.INSTANCE.isBCTFeatureEnabled()) {
            isMyRoutesDisabled = true;
            mMyRoutes.setAlpha(0.4f);
        } else {
            mMyRoutes.setAlpha(1f);
        }
    }

    /**
     * Create all the listeners here
     */
    private void createListeners() {
        mAddWayPoint.setOnClickListener(this);
        mMyRoutes.setOnClickListener(this);
        mDIYTxt.setOnClickListener(this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter, mWayPointsList);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mWayPointRecyclerView);
        mWayPointExpandLAY.setOnClickListener(this);
    }

    public void waypointNotifyDataChaged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This method manages the views based on the ride type passed
     */
    private void manageViews() {
        switch (mRideType) {
            case WAYPOINT_NAVIGATION:
                waypointLayout.setBackgroundColor(Color.TRANSPARENT);
                mAddWayPoint.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mWayPointsList = bundle.getParcelableArrayList(REConstants.WAY_POINTS_LIST);
            mRideType = bundle.getString(RIDE_WAYPOINT_TYPE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.textView_addwaypoint) {
            Bundle params = new Bundle();
            params.putString("eventCategory", REConstants.KEY_TRIPPER_GTM);
            params.putString("eventAction", REConstants.addWaypointClicked);
            if (!REUtils.isLocationEnabled(getActivity())) {
                Toast.makeText(getActivity(), "Please enable location and try again", Toast.LENGTH_SHORT).show();
            } else {
                if (!isReconstructedRoute) {
                    if (mWayPointsList != null & mWayPointsList.size() > 0 && mWayPointsList.get(mWayPointsList.size() - 1).getPlaceName() != null) {
                        params.putString("eventLabel", mWayPointsList.get(mWayPointsList.size() - 1).getPlaceName());
//                        params.putString("modelName", REUtils.getDeviceModel());
                        REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
                        if (mWayPointsList.size() < 25) {
                            if (!isEmptyWayPointAdded())
                                addEmptyWayPointCell();
                        } else {
                            REUtils.showErrorDialog(getContext(), getString(R.string.hint_waypoint_limit_exceeded));
                        }
                    } else {
                        params.putString("eventLabel", "");
//                        params.putString("modelName", REUtils.getDeviceModel());
                        REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
                        REUtils.showErrorDialog(getContext(), getString(R.string.text_error_no_destination));
                    }
                } else {
                    mPlanRideWayPointSearchListener.showReconstructedMode();
                }
            }
        } else if (v.getId() == R.id.tv_myRoutes) {
            Log.e("WpMyRoutesButtonclick", ""+mWayPointsList.size());
            Bundle params = new Bundle();
            params.putString("eventCategory", REConstants.KEY_TRIPPER_GTM);
            params.putString("eventAction", REConstants.myRoutesClicked);
            String aDestinationStr = "";
            if (mWayPointsList != null & mWayPointsList.size() > 0 && mWayPointsList.get(mWayPointsList.size() - 1).getPlaceName() != null) {
                aDestinationStr = mWayPointsList.get(mWayPointsList.size() - 1).getPlaceName();
            }
            params.putString("eventLabel", aDestinationStr);
//            params.putString("modelName", REUtils.getDeviceModel());
            REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
            if (!isMyRoutesDisabled) {
                mPlanRideWayPointSearchListener.showTripListScreen();
            }
        } else if (v.getId() == R.id.textView_diy_videos) {
            startActivity(new Intent(getContext(), DoItYourSelfActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
        } else if (v.getId() == R.id.add_waypoint_expand_LAY) {
            REUtils.preventMultipleClick(v, 1000);
            /*if (mPlanRideWayPointSearchListener.isMapRedrawing())
                return;*/
            if (!isExpand) {
                expandControls();
            } else {
                collapseControls();
            }
        }
    }

    private void invokeWayPointSearchScreen() {
        Intent intent = new Intent(getContext(), NavigationWayPointSearchActivity.class);
        intent.putExtra("source_start", NAVIGATION_SEARCH_ACTIVITY);
        intent.putExtra("waypoint_type", "waypoint");
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, REQUESTCODE_WAYPOINT);
        mContext.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            isFromSearch = true;
            if (resultCode != 0) {
                String mPLace = null;
                double latitude = 0, longitude = 0;
                boolean isRecentLocation = false;
                boolean isRecentRoute = false;
                if (data != null) {
                    mPLace = data.getStringExtra("place");
                    latitude = data.getDoubleExtra("latitude", 0.0);
                    longitude = data.getDoubleExtra("longitude", 0.0);
                    isRecentLocation = data.getBooleanExtra("isRecentLocation",false);
                    isRecentRoute = data.getBooleanExtra("isRecentRoute",false);
                }
                try{

                    if (isRecentRoute&&resultCode == REQUESTCODE_WAYPOINT){
                        mWayPointsList.clear();
                        mWayPointsList.removeAll(mWayPointsList);
                        clearSearch();
                        mAdapter.notifyDataSetChanged();
                    }

                    if (isRecentRoute) {
                        populateRecentRoute(data.getParcelableArrayListExtra("isRecentRouteData"));
                    return;
                    }

                    if (!isRecentLocation && !isRecentRoute) {
                        ArrayList<RecentLocation> llist  = RecentRouteLocationManager.getRecentLocationList(getActivity());
                        //List<String> newList = llist.stream().distinct().collect(Collectors.toList());

                        LatLng latLng = new LatLng(latitude, longitude);
                        RecentLocation recentLocation = new RecentLocation(mPLace, mPLace, latLng);
                        if (!llist.contains(recentLocation)) {
                            RecentRouteLocationManager.saveRecentLocationList(recentLocation, getActivity());
                            Log.e("RECENTROUTESAVED", "Success");
                        }
                    }
                   /*List<RecentLocation> llist  = RecentRouteLocationManager.getRecentLocationList(getActivity());
                    Log.e("RECENTROUTESAVED1","Success");
                    Log.e("RECENTROUTESAVED1","listsize"+llist.size());
                    Log.e("RECENTROUTESAVED1","Success"+llist.get(0).getLocationName());*/
                } catch(Exception e){
                    Log.e("RECENTROUTESAVED","FAil"+e.getMessage());
                }
                // Updating waypoints
                WayPointsData wayPointData = new WayPointsData(latitude, longitude, mPLace);
                if (resultCode == REQUESTCODE_WAYPOINT && mPLace != null) {
                    //Just to plot marker we are enabling this
                    isWayPointAdded = true;
                    if (mPlanRideWayPointSearchListener != null)
                        mPlanRideWayPointSearchListener.onWayPointAdded();
                }
                //Plot marker in the map location based on the type of the way point.
                if (mPlanRideWayPointSearchListener != null)
                    mPlanRideWayPointSearchListener.updateRoute(wayPointData);
                if (resultCode == REQUESTCODE_WAYPOINT && mPLace != null) {
                    removeEmptyWayPointCell();
                    mWayPointsList.add(wayPointPosition, wayPointData);
                    mAdapter.notifyDataSetChanged();
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Tripper");
                    params.putString("eventAction", wayPointData.getPlaceName()+" as waypoint");
                    REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

                } else if (resultCode == REQUESTCODE_DESTINATION_WAYPOINT && mPLace != null) {

                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Tripper");
                    params.putString("eventAction", wayPointData.getPlaceName() + " as destination");
                    REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

                    mWayPointsList.set(mWayPointsList.size() - 1, wayPointData);
                    mAdapter.notifyDataSetChanged();
                    if (getContext() instanceof PlanRideActivity) {
                        ((PlanRideActivity) getContext()).showRecommendedRouteFragment();
                    }
                    if (getContext() instanceof ModifyRidesActivity) {
                        ((ModifyRidesActivity) getContext()).showRecommendedRouteFragment();
                    }
                    if (mPlanRideWayPointSearchListener != null)
                        mPlanRideWayPointSearchListener.resetPOIOptions();
                } else if (resultCode == REQUESTCODE_SOURCE_WAYPOINT && mPLace != null) {
                    mWayPointsList.set(0, wayPointData);
                    mAdapter.notifyDataSetChanged();
                }
                setHeightBasedOnRows();
                if (mPlanRideWayPointSearchListener != null)
                    mPlanRideWayPointSearchListener.updateRoute(mWayPointsList);
            }
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void populateRecentRoute( ArrayList<WayPointsData> route) {
        mAdapter = new NavigationWayPointAdapter(getActivity(), mWayPointsList, this, false);
        mWayPointRecyclerView.setAdapter(mAdapter);

        mWayPointsList.clear();
        mAdapter.notifyDataSetChanged();

        for (int i=0;i<route.size();i++){
            Log.e("Route clicked1",""+route.get(i).getPlaceName());

        }


       /* if (route.size()==2){
            WayPointsData wayPointSource = new WayPointsData(route.get(0).getLatitude(), route.get(0).getLongitude(), route.get(0).getPlaceName());
            mWayPointsList.set(0, wayPointSource);
            WayPointsData wayPointDestination = new WayPointsData(route.get(1).getLatitude(), route.get(1).getLongitude(), route.get(1).getPlaceName());
            mWayPointsList.set(1, wayPointDestination);
        }
        if (route.size()>2){
            int dest_index = 1;
            WayPointsData wayPointSource = new WayPointsData(route.get(0).getLatitude(), route.get(0).getLongitude(), route.get(0).getPlaceName());
            mWayPointsList.set(0, wayPointSource);
            for (int i=1;i<route.size()-1;i++){
                addEmptyWayPointCell();
                WayPointsData wayPoint = new WayPointsData(route.get(i).getLatitude(), route.get(i).getLongitude(), route.get(i).getPlaceName());
                mWayPointsList.set(i, wayPoint);
                dest_index = dest_index+1;
            }
            WayPointsData wayPointDestination = new WayPointsData(route.get(dest_index).getLatitude(), route.get(dest_index).getLongitude(), route.get(dest_index).getPlaceName());
            mWayPointsList.set(dest_index, wayPointDestination);
        }*/

        mWayPointsList=route;
        mAdapter = new NavigationWayPointAdapter(getActivity(), mWayPointsList, this, false);
        mWayPointRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        setHeightBasedOnRows();
        mPlanRideWayPointSearchListener.updateRoute(mWayPointsList);

    }

    private void addEmptyWayPointCell() {
        //Add an empty Add Waypoint cell behind destination
        mWayPointsList.add(mWayPointsList.size() - 1, new WayPointsData(0, 0, WAY_POINT_HINT));
        mAdapter.notifyDataSetChanged();
        setHeightBasedOnRows();
        mPlanRideWayPointSearchListener.updateRoute(mWayPointsList);
        isWayPointAdded = true;
        isEmptyWayPointAdded = true;
        mPlanRideWayPointSearchListener.onWayPointAdded();
        mPlanRideWayPointSearchListener.onEmptyWayPointAdded();
        mWayPointRecyclerView.post(() -> mWayPointRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1));
    }

    private void removeEmptyWayPointCell() {
        //remove the Add Waypoint cell behind destination
        mWayPointsList.remove(wayPointPosition);
        mAdapter.notifyDataSetChanged();
        isEmptyWayPointAdded = false;
        if (mPlanRideWayPointSearchListener != null) {
            mPlanRideWayPointSearchListener.onEmptyWayPointRemoved();
        }
    }

    private void setHeightBasedOnRows() {
        if (mControls.equals(RENavigationFragment.Controls.COLLAPSE)) {
            ViewGroup.LayoutParams params = mWayPointRecyclerView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mWayPointRecyclerView.setLayoutParams(params);
            mWayPointRecyclerView.setAdapter(mAdapter);
        } else {
            if (mWayPointsList.size() >= 4) {
                ViewGroup.LayoutParams params = mWayPointRecyclerView.getLayoutParams();
                params.height = 400;
                mWayPointRecyclerView.setLayoutParams(params);
            } else {
                ViewGroup.LayoutParams params = mWayPointRecyclerView.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mWayPointRecyclerView.setLayoutParams(params);
            }
        }
    }

    /**
     * This method is used to disable adding waypoints if it is recording flow
     *
     * @param isRecord : boolean
     */
    public void disableWaypoint(boolean isRecord) {
        if (isRecord) {
            mAddWayPoint.setOnClickListener(null);
            mMyRoutes.setOnClickListener(null);
            mAdapter = new NavigationWayPointAdapter(getActivity(), mWayPointsList, this, true);
            mWayPointRecyclerView.setAdapter(mAdapter);
        }
    }

    public void setRouteConstructedMode(boolean reconstructedRoute) {
        isReconstructedRoute = reconstructedRoute;
        setHeightBasedOnRows();
    }

    @Override
    public void onEditTextFocusChange(boolean isDestination, boolean isWayPoint, int position) {
        wayPointPosition = position;
        if (!isReconstructedRoute) {
            Intent intent = new Intent(getContext(), NavigationWayPointSearchActivity.class);
            intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
            if (isDestination) {
                intent.putExtra("waypoint_type", "destination");
                intent.putExtra("source_start", NAVIGATION_SEARCH_ACTIVITY);
                startActivityForResult(intent, REQUESTCODE_DESTINATION_WAYPOINT);
            } else if (isWayPoint) {
                invokeWayPointSearchScreen();
            } else {
                intent.putExtra("waypoint_type", "source");
                intent.putExtra("source_start", NAVIGATION_SEARCH_ACTIVITY);
                startActivityForResult(intent, REQUESTCODE_SOURCE_WAYPOINT);
            }
        } else {
            mPlanRideWayPointSearchListener.showReconstructedMode();
        }
    }

    @Override
    public void onNavigationWaypointRemoved(int position, boolean isEmptyWayPoint) {
        setHeightBasedOnRows();
        mPlanRideWayPointSearchListener.onNavigationWaypointRemoved();
        mPlanRideWayPointSearchListener.updateRoute(mWayPointsList);
        if (mWayPointsList.size() == 2) {//if only destination is there
            isWayPointAdded = false;
            mPlanRideWayPointSearchListener.onWayPointRemoved();
        }
        if (isEmptyWayPoint) {//detect if any empty waypoint removed
            isEmptyWayPointAdded = false;
            mPlanRideWayPointSearchListener.onEmptyWayPointRemoved();
        }
    }

    @Override
    public void onFilterIconClicked() {
        mPlanRideWayPointSearchListener.showFilterOptionDialog();
    }

    public void hideOptionsViews() {
//        mAddWayPoint.setVisibility(View.GONE);
//        mMyRoutes.setVisibility(View.GONE);
//        mDIYTxt.setVisibility(View.GONE);

        optionsConstraint.setVisibility(GONE);
    }

    public void collapseControls() {
        isExpand = false;
        mControls = RENavigationFragment.Controls.COLLAPSE;
        mAdapter.updateWayPoints(getUpcomingWayPointList());
        mAdapter.collapseControls();
        setHeightBasedOnRows();
        mWayPointExpandIMG.setImageDrawable(mContext.getDrawable(R.drawable.ic_svg_right_arrow_button));
        mPlanRideWayPointSearchListener.onCollapseControls();
        //cancelCollapseTimer();
    }

    public void expandControls() {
        isExpand = true;
        mControls = RENavigationFragment.Controls.EXPAND;
        mAdapter.updateWayPoints(mWayPointsList);
        mAdapter.expandControls();
        setHeightBasedOnRows();
        mWayPointExpandIMG.setImageDrawable(mContext.getDrawable(R.drawable.ic_svg_expand_down_button));
        mPlanRideWayPointSearchListener.onExpandControls();
        mWayPointRecyclerView.post(() -> mWayPointRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1));
        //startCollapseTimer();
    }

    private void startCollapseTimer() {
        try {
            cancelCollapseTimer();
            if (mPlanRideWayPointSearchListener.isMapRedrawing())
                return;
            mCollapseHandler = new Handler();
            mCollapseHandler.postDelayed(mCollapseRunnable, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable mCollapseRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mPlanRideWayPointSearchListener.isMapRedrawing())
                mWayPointExpandLAY.callOnClick();
        }
    };

    private void cancelCollapseTimer() {
        try {
            if (mCollapseHandler != null)
                mCollapseHandler.removeCallbacks(mCollapseRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onNavigationStarted() {
        hideOptionsViews();
        disableWayPointTouchListeners();
        if (mWayPointsList.size() > 2)
            mWayPointExpandLAY.setVisibility(View.VISIBLE);
        else
            mWayPointExpandLAY.setVisibility(View.GONE);
        mAdapter.setDisableWaypoint(true);
        collapseControls();
    }

    public void onNavigationEnded() {
        //enableWayPointTouchListeners();
        //mWayPointExpandLAY.setVisibility(View.GONE);
        //expandControls();
        //mAdapter.setNavigationStarted(false);
    }

    /**
     * This method receives the upComing waypoint
     *
     * @param upcomingWayPoint
     */
    public void onMilestoneReached(WayPointsData upcomingWayPoint) {
        mUpcomingWayPointList = new ArrayList<>();
        mUpcomingWayPointList.add(upcomingWayPoint);
        if (mControls.equals(RENavigationFragment.Controls.COLLAPSE)) {
            mContext.runOnUiThread(() -> {
                mAdapter.updateWayPoints(mUpcomingWayPointList);
                mAdapter.notifyDataSetChanged();
            });
        }
    }

    private ArrayList<WayPointsData> getUpcomingWayPointList() {
        if (mUpcomingWayPointList.size() > 0) {
            return mUpcomingWayPointList;
        } else {
            //to show only the destination on the live route
            ArrayList<WayPointsData> aWayPointsList = new ArrayList<>();
            aWayPointsList.add(mWayPointsList.get(mWayPointsList.size() - 1));
            return aWayPointsList;
        }
    }

    public interface IPlanRideWayPointSearchListener {
        void updateRoute(ArrayList<WayPointsData> wayPointResult);

        void updateRoute(WayPointsData wayPointsData);

        void showFilterOptionDialog();

        void showTripListScreen();

        void showReconstructedMode();

        void resetPOIOptions();

        void onWayPointAdded();

        void onWayPointRemoved();

        void clearMap();

        void enableMarkerListeners();

        void onEmptyWayPointAdded();

        void onEmptyWayPointRemoved();

        void onExpandControls();

        void onCollapseControls();

        void onNavigationWaypointRemoved();

        double getStartDragMarkerTag();

        double getEndDragMarkerTag();

        void plotMarker();

        boolean isMapRedrawing();
    }

    @Override
    public void onNavigationWaypointSwapped() {
        setHeightBasedOnRows();
        mPlanRideWayPointSearchListener.clearMap();
        mPlanRideWayPointSearchListener.updateRoute(mWayPointsList);
        mPlanRideWayPointSearchListener.plotMarker();
    }
    public void setDestinationFromFavourite(WayPointsData wayPointData){

        mWayPointsList.set(mWayPointsList.size() - 1, wayPointData);
        mAdapter.notifyDataSetChanged();

        if (mPlanRideWayPointSearchListener != null)
            mPlanRideWayPointSearchListener.resetPOIOptions();
    }
    public void updateDeepLinkWaypoints(ArrayList<WayPointsData> mWayPointList) {
        mAdapter.notifyDataSetChanged();
        setHeightBasedOnRows();
        mPlanRideWayPointSearchListener.clearMap();
        mPlanRideWayPointSearchListener.updateRoute(mWayPointList);
    }

    public void updateWayPoint(LatLng latLng, String aPlaceName) {
        isDragMode = false;
        enableWayPointTouchListeners();
        if (isWayPointAdded && isFromSearch) {
            mWayPointsList.set(wayPointPosition, new WayPointsData(latLng.latitude, latLng.longitude, aPlaceName));
        } else if (isWayPointAdded) {
            mWayPointsList.set(mWayPointsList.size() - 2, new WayPointsData(latLng.latitude, latLng.longitude, aPlaceName));
        } else {
            mWayPointsList.set(mWayPointsList.size() - 1, new WayPointsData(latLng.latitude, latLng.longitude, aPlaceName));
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public boolean isEmptyWayPointAdded() {
        if (mWayPointsList == null)
            return false;
        for (int i = 0; i < mWayPointsList.size(); i++) {
            if (mWayPointsList.get(i).getPlaceName() == null)
                return false;
            if (mWayPointsList.get(i).getPlaceName().equals(WAY_POINT_HINT))
                return true;
        }
        return false;
    }

    public void enableWayPointTouchListeners() {
        mItemTouchHelper.attachToRecyclerView(mWayPointRecyclerView);
    }

    public void disableWayPointTouchListeners() {
        mItemTouchHelper.attachToRecyclerView(null);
    }

    public void clearSearch() {
        isFromSearch = false;
    }

    public boolean isSearchMode() {
        return isFromSearch;
    }

    public int getWaypointPosition() {
        return wayPointPosition;
    }

    public void updateDragWayPoint(LatLng latLng, String aPlaceName) {
        isDragMode = true;
        enableWayPointTouchListeners();
        if (isWayPointAdded && isFromSearch) {
            mWayPointsList.set(wayPointPosition, new WayPointsData(latLng.latitude, latLng.longitude, aPlaceName));
        } else if (isWayPointAdded) {
            mWayPointsList.set(getDragWaypointPosition(), new WayPointsData(latLng.latitude, latLng.longitude, aPlaceName));
        } else {
            mWayPointsList.set(mWayPointsList.size() - 1, new WayPointsData(latLng.latitude, latLng.longitude, aPlaceName));
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public int getDragWaypointPosition() {
        for (int i = 0; i < mWayPointsList.size(); i++) {
            if (Double.compare(mWayPointsList.get(i).getLatitude(), mPlanRideWayPointSearchListener.getStartDragMarkerTag()) == 0) {
                dragPosition = i;
                return i;
            }
        }
        return mWayPointsList.size() - 2;
    }

    public boolean isDragMode() {
        return isDragMode;
    }

    public int getDragPosition() {
        return dragPosition;
    }

    public void setWayPointsList(ArrayList<WayPointsData> mWayPointsList) {
        this.mWayPointsList = mWayPointsList;
        this.mAdapter.updateWayPoints(mWayPointsList);
        this.mAdapter.notifyDataSetChanged();
    }
}
