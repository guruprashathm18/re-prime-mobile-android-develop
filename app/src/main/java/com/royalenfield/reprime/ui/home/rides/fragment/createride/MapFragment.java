package com.royalenfield.reprime.ui.home.rides.fragment.createride;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
//import com.royalenfield.mapboxNavigation.NavigationProviderGenerator;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.ui.home.rides.asynctask.DecodePolyLineAsyncTask;
import com.royalenfield.reprime.ui.home.rides.asynctask.RidesAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.rides.interactor.GoogleMapAPIInteractor;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;
import com.squareup.okhttp.Route;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static android.content.Context.LOCATION_SERVICE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_WAYPOINT_TYPE;

//import com.bosch.softtec.components.midgard.core.common.conversion.LengthUnit;
//import com.bosch.softtec.components.midgard.core.common.models.Distance;
//import com.bosch.softtec.components.midgard.core.common.models.Duration;
//import com.bosch.softtec.components.midgard.core.common.models.Interval;
//import com.bosch.softtec.components.midgard.core.common.models.LatLng;
//import com.bosch.softtec.components.midgard.core.directions.RouteCalculationCallback;
//import com.bosch.softtec.components.midgard.core.directions.RouteProvider;
//import com.bosch.softtec.components.midgard.core.directions.models.*;
//import com.bosch.softtec.components.midgard.navigation.NavigationListener;
//import com.bosch.softtec.components.midgard.navigation.NavigationProvider;
//import com.bosch.softtec.components.midgard.navigation.milestone.Milestone;
//import com.bosch.softtec.components.midgard.navigation.milestone.MilestoneListener;
//import com.bosch.softtec.components.midgard.navigation.model.*;
//import com.bosch.softtec.components.midgard.navigation.offroute.OffRouteListener;
//import com.bosch.softtec.components.midgard.navigation.routeprogress.RouteProgressListener;
//import com.bosch.softtec.components.midgard.navigation.traffic.TrafficChange;
//import com.bosch.softtec.components.midgard.navigation.traffic.TrafficListener;
//import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
//import com.royalenfield.mapboxNavigation.NavigationProviderGenerator;
//import static com.royalenfield.bluetooth.NavigationUtilsKt.getRemappedManeueverId;

public class MapFragment extends REBaseFragment
        implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, RidesListeners.OnDirectionsCallFinishedListener,
        RidesAsyncTaskListeners.DirectionPolylineDecodeListener

//        , NavigationListener
//        ,RouteProgressListener, OffRouteListener, MilestoneListener, TrafficListener
{
    public static final String TAG_MAP_FRAGMENT = MapFragment.class.getName();

    private static final long RIGHT_BEFORE_DURATION_SECONDS = 10L;
    private static final long SOME_TIME_BEFORE_DURATION_SECONDS = 70L;
    private static final long LONG_TIME_BEFORE_DURATION_SECONDS = 150L;
    private static final long MAXIMUM_LENGTH_SHOW_UPCOMING_MANEUVER_METERS = 500;

    private static final long MIN_TIME_BW_UPDATES = 0;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final int REQUEST_CHECK_SETTINGS = 1111;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;
    private static RidesListeners.CurrentLocationListener mCurrentLocationListener;
    private final String TAG = MapFragment.class.getSimpleName();
    private ArrayList<WayPointsData> mWayPointDataList = new ArrayList<>();
    private ArrayList<LatLng> mAddedWayPoints = new ArrayList<>();
    private GoogleMap googleMap;
    private UiSettings mUiSettings;
    private boolean firstTimeFlag = true;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker endLocationMarker;
    private Location currentLocation;
    private double mSourceLat, mSourceLon;
    private double mDestLat, mDestLon;
    private boolean isNavigation = false;
    private String mRideType = " ";
    private boolean isLocationRequired = true;
    private LocationManager locationManager;
    private ArrayList<WayPointsData> mRidesWaypoints;

    private Route chosenRoute;
    //private RouteProgress lastReceivedRouteProgress = null;
    /**
     * LocationCallback
     */
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult");
            if (locationResult.getLastLocation() == null)
                return;
            currentLocation = locationResult.getLastLocation();
            if (firstTimeFlag && googleMap != null) {
                animateCamera(currentLocation);
                firstTimeFlag = false;
            }
            if (mDestLat == 0 && mDestLon == 0)
                // Updating current location edit text.
                mCurrentLocationListener.updateCurrentLocation(new com.google.android.gms.maps.model.LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            //stops the current location updates.
            stopCurrentLocationUpdates();
        }
    };
    private Location mStartLocation;
    private Marker startLocationMarker;
    /**
     * RouteCalculation Callback for getting the Route list for the Given Start, Destination and way-point inputs
     */
//    RouteCalculationCallback routeCalculationCallback = new RouteCalculationCallback() {
//        @Override
//        public void onRouteCalculationFinished(@NotNull final List<? extends Route> list) {
//            if (getActivity() != null) {
//                getActivity().runOnUiThread(() -> {
//                    if (list.size() > 0) {
//                        //Chosen route as of now we are not plotting multiple routes. so whichever is the first route we are selecting it.
//                        chosenRoute = list.get(0);
//                        drawRouteTrace(list.get(0).getRouteTrace());
//                    } else {
//                        Toast.makeText(getActivity(), "Unable to find the route.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void onRouteCalculationError(@NotNull RouteCalculationError routeCalculationError, @Nullable String s) {
//            if (getActivity() != null) {
//                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Unable to find the route.", Toast.LENGTH_SHORT).show());
//            }
//        }
//    };
    private OnTouchListener mListener;
//    private NavigationProvider mNavigationProvider;
//    private RouteProvider mRouteProvider;

    public static MapFragment newInstance(RidesListeners.CurrentLocationListener listener) {
        mCurrentLocationListener = listener;
        return new MapFragment();
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    private void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mWayPointDataList = bundle.getParcelableArrayList(REConstants.WAY_POINTS_LIST);
            mRideType = bundle.getString(RIDE_WAYPOINT_TYPE);
            isLocationRequired = bundle.getBoolean(REConstants.IS_LOCATION_REQUIRED, true);
            isNavigation = bundle.getBoolean(REConstants.IS_NAVIGATION, false);
        }
    }

    /**
     * Calculates the Stat, Destination and Added way points list for the directions API request.
     * Called first time and update route as well
     */
    public void calculateStartDestinationAndWayPointLatLang(ArrayList<WayPointsData> wayPointDataList, boolean isnavigation) {
        isNavigation = isnavigation;
        if (wayPointDataList != null && wayPointDataList.size() >= 2 && wayPointDataList.
                get(wayPointDataList.size() - 1).getLatitude() != 0) {
            mSourceLat = wayPointDataList.get(0).getLatitude();
            mSourceLon = wayPointDataList.get(0).getLongitude();
            mDestLat = wayPointDataList.get(wayPointDataList.size() - 1).getLatitude();
            mDestLon = wayPointDataList.get(wayPointDataList.size() - 1).getLongitude();
            calculateAndDrawRoute(wayPointDataList);
        } else if (wayPointDataList != null && wayPointDataList.size() >= 2 &&
                wayPointDataList.get(wayPointDataList.size() - 1).getLatitude() == 0) {
            //If destination is removed
            //Clearing the dest lat and removing previous route
            googleMap.clear();
            mDestLat = 0;
            calculateAndDrawRoute(wayPointDataList);
        } else {
            googleMap.clear();
            animateCamera(currentLocation);
        }
    }

    private void calculateAndDrawRoute(ArrayList<WayPointsData> wayPointDataList) {
        mAddedWayPoints = new ArrayList<>();
        //TODO Enable for Navigation flow
        /*for (int i = 1; i < (wayPointDataList != null ? wayPointDataList.size() : 0) - 1; i++) {
            LatLng latLng = new LatLng(wayPointDataList.get(i).getLatitude(), wayPointDataList.get(i).getLongitude(), null);
            mAddedWayPoints.add(new WayPoint(latLng, false));
        }*/
        drawRoute(wayPointDataList);
    }

    /**
     * For moving the Camera.
     *
     * @param location {@link Location}
     */
    private void animateCamera(Location location) {
        if (location != null) {
            com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    latLng, 14);
            //if (mWayPointDataList.size() == 1)
            googleMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mNavigationProvider = NavigationProviderGenerator.INSTANCE.getNavigationProvider(getActivity());
//        mRouteProvider = RouteProviderGenerator.INSTANCE.getRouteProvider(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getBundleData();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_createride, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_create_ride);
        supportMapFragment.getMapAsync(this);
        TouchableWrapper frameLayout = new TouchableWrapper(view.getContext());
        frameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((ViewGroup) view).addView(frameLayout,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

//    private NavigationOptions createNavigationOptions() {
//        Distance offRouteDistance = new Distance(50, LengthUnit.METER);
//        OffRouteConfiguration offRouteConfiguration = new OffRouteConfiguration(offRouteDistance, mRouteProvider);
//        Duration durationRightBeforeManeuver = new Duration(RIGHT_BEFORE_DURATION_SECONDS, TimeUnit.SECONDS);
//        Duration durationSomeTimeBeforeManeuver = new Duration(SOME_TIME_BEFORE_DURATION_SECONDS, TimeUnit.SECONDS);
//        Duration durationLongTimeBeforeManeuver = new Duration(LONG_TIME_BEFORE_DURATION_SECONDS, TimeUnit.SECONDS);
//        MilestoneConfiguration milestoneConfiguration = new MilestoneConfiguration(
//                durationRightBeforeManeuver,
//                durationSomeTimeBeforeManeuver,
//                durationLongTimeBeforeManeuver);
//        TrafficAwareReroutingConfiguration trafficAwareReroutingConfiguration = new TrafficAwareReroutingConfiguration(
//                mRouteProvider,
//                new Interval(5, TimeUnit.MINUTES),
//                new Interval(10, TimeUnit.MINUTES)
//        );
//        return new NavigationOptions.Builder()
//                .enableOffRouteDetectedRerouting(offRouteConfiguration)
//                .milestoneConfiguration(milestoneConfiguration)
//                .enableTrafficAwareRerouting(trafficAwareReroutingConfiguration)
//                .build();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView Navigation Stopped");
        stopNavigation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.clear(); //clear old markers
        this.googleMap = googleMap;
        //googleMap.setOnMarkerClickListener(this);
        if (isLocationRequired) {
            enableMyLocation();
        }
        UiSettings mUiSettings = this.googleMap.getUiSettings();
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setRotateGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        //mUiSettings.setZoomControlsEnabled(true);
        this.googleMap.setOnMapLongClickListener(this);
        //Draws the routes
        calculateStartDestinationAndWayPointLatLang(mWayPointDataList, isNavigation);
    }

    /**
     * Unregistering the navigation listeners.
     */
    private void unregisterNavigationListener() {
//        mNavigationProvider.removeOffRouteListener(this);
//        mNavigationProvider.removeRouteProgressListener(this);
//        mNavigationProvider.removeMilestoneListener(this);
//        mNavigationProvider.removeNavigationListener(this);
    }

    /**
     * Registering the navigation listeners.
     */
    private void registerNavigationListener() {
//        mNavigationProvider.addOffRouteListener(this);
//        mNavigationProvider.addRouteProgressListener(this);
//        mNavigationProvider.addMilestoneListener(this);
//        mNavigationProvider.addNavigationListener(this);
    }

    /**
     * Draws the routes if source and destination location is given.
     */
    private void drawRoute(ArrayList<WayPointsData> wayPointDataList) {
        if (mSourceLat != 0 && mSourceLon != 0 && mDestLat != 0 && mDestLon != 0) {
            //Creates start location object.
            mStartLocation = new Location("");
            mStartLocation.setLatitude(mSourceLat);
            mStartLocation.setLongitude(mSourceLon);
            if (isNavigation) {
                /*calculateRoutes(new com.bosch.softtec.components.core.models.Location(mStartLocation),
                        new LatLng(mDestLat, mDestLon, null), mAddedWayPoints);*/
            } else {
                showLoading();
                //Directions API call to Bila for getting the route options
                StringBuilder waypoints = new StringBuilder();
                mRidesWaypoints = new ArrayList<>();
                mRidesWaypoints = wayPointDataList;
                if (wayPointDataList != null && wayPointDataList.size() > 2) {
                    for (int iCount = 1; iCount <= wayPointDataList.size() - 2; iCount++) {
                        String waypoint = "|" + wayPointDataList.get(iCount).getLatitude() + "," + wayPointDataList.get(iCount).getLongitude();
                        waypoints.append(waypoint);
                    }
                }
                new GoogleMapAPIInteractor().getDirections(mStartLocation.getLatitude() + "," + mStartLocation.getLongitude(),
                        mDestLat + "," + mDestLon, String.valueOf(waypoints), this);
            }
        }
    }

    /**
     * Methods to Start the navigation for the chosen route
     */
    public void startNavigation() {
        registerNavigationListener();
//        NavigationOptions navigationOptions = createNavigationOptions();
//        mNavigationProvider.startNavigation(chosenRoute, navigationOptions);
    }

    /**
     * Methods to Stop the navigation for the chosen route
     */
    public void stopNavigation() {
//        mNavigationProvider.stopNavigation();
        unregisterNavigationListener();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            requestForLocationPermission();
        } else if (googleMap != null) {
            // Access to the location has been granted to the app.
            googleMap.setMyLocationEnabled(true);
            // boolean statusOfGPS = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            // Log.e("test","statusOfGPS = "+statusOfGPS);
            //forms the location request
            if (!REUtils.isLocationEnabled(getContext())) {
                enableGpsSettingsForLocation();
            } else {
                getCurrentLocation();
            }
        }
    }

    /**
     * Gets the current location from NetworkProvider
     */
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            showLoading();
            locationManager = (LocationManager) REApplication.getAppContext()
                    .getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            if (locationManager != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                                currentLocation = location;
                                REUserModelStore.getInstance().setLatitude(currentLocation.getLatitude());
                                REUserModelStore.getInstance().setLongitude(currentLocation.getLongitude());

                                if (googleMap != null) {
                                    animateCamera(currentLocation);
                                    //firstTimeFlag = false;
                                }
                                if (mDestLat == 0 && mDestLon == 0 && currentLocation != null) {
                                    // Updating current location edit text.
                                    mCurrentLocationListener.updateCurrentLocation(new com.google.android.gms.maps.model.
                                            LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                                }
                                //stops the current location updates.
                                locationManager.removeUpdates(this);
                                locationManager = null;
                                hideLoading();
                            }
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
        }
    }


    /**
     * Enables the GPS Location for the App.
     **/
    private void enableGpsSettingsForLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(getActivity(), locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
        });
        task.addOnFailureListener(getActivity(), e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(getActivity(),
                            REConstants.REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REConstants.REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    getCurrentLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    REUserModelStore.getInstance().setLatitude(0);
                    REUserModelStore.getInstance().setLongitude(0);
                    hideLoading();
                    break;
            }
        }
    }

    @Override
    public void onDirectionsSuccess(List<com.royalenfield.reprime.models.response.googlemap.directions.Route> routes) {
        try {
            double distance;
            String[] distanceValues = null;
            if (routes != null && routes.size() > 0) {
                if (mCurrentLocationListener != null) {
                    for (int i = 0; i < routes.size(); i++) {
                        List<com.royalenfield.reprime.models.response.googlemap.directions.Leg> legs = routes.get(i).getLegs();
                        distanceValues = new String[legs.size()];
                        for (int j = 0; j < legs.size(); j++) {
                            distanceValues[j] = legs.get(j).getDistance().getText();
                        }
                    }
                    distance = Double.parseDouble(REUtils.getDistance(distanceValues));
                    mCurrentLocationListener.setDistance(distance);
                }
                String point = routes.get(0).getOverview_polyline().getPoints();
                if (point != null) {
                    new DecodePolyLineAsyncTask(point, this).execute();
                } else {
                    hideLoading();
                    Toast.makeText(getActivity(), "Unable to find the route.", Toast.LENGTH_SHORT).show();
                }
            } else {
                hideLoading();
            }
        } catch (Exception e) {
            hideLoading();
            RELog.e(e);
        }
    }


    @Override
    public void onDirectionsFailure(String error) {
        hideLoading();
        Toast.makeText(getActivity(), "Unable to find the route.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDecodeCompleted(List<com.google.android.gms.maps.model.LatLng> latLngList, LatLngBounds.Builder builder) {
        if (latLngList != null && latLngList.size() > 0) {
            drawPolyLine(latLngList);
        }
        showEndLocationMarker(new com.google.android.gms.maps.model.LatLng(mDestLat, mDestLon));
        showStartLocationMarker(mStartLocation);
        showWaypointMarker(mRidesWaypoints);
        int padding = 0;
        /**create the camera with bounds and padding to set into map*/
        if (builder != null) {
            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), padding);
            googleMap.animateCamera(cu);
        }
        hideLoading();
    }

    /**
     * Draws PolyLine on the map
     *
     * @param latLngList
     */
    private void drawPolyLine(List<com.google.android.gms.maps.model.LatLng> latLngList) {
        PolylineOptions polylineOptions = new PolylineOptions();
        int routeTraceColor = 0xFF0098D9;
        float zIndex = 1.0F;
        RoundCap roundCap = new RoundCap();
        polylineOptions.color(routeTraceColor).zIndex(zIndex).startCap(roundCap).
                endCap(roundCap).width(18.0F).geodesic(true).clickable(true);
        polylineOptions.addAll(latLngList);
        googleMap.clear();
        googleMap.addPolyline(polylineOptions);
    }

    /**
     * Request for the runtime permissions.
     */
    private void requestForLocationPermission() {
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    /**
     * Starts the location update.
     */
    private void startCurrentLocationUpdates() {
        //forms the location request
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        enableGpsSettingsForLocation(locationRequest);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //// Check if the Location permission has been granted
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission is already available.
                if (fusedLocationProviderClient != null) {
                    if (googleMap != null) {
                        googleMap.setMyLocationEnabled(true);
                    }
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
                }
            } else {
                // Permission is missing and must be requested.
                requestForLocationPermission();
            }
        } else {
            // Runtime permission not required.
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    /**
     * Stops the location updates.
     */
    private void stopCurrentLocationUpdates() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    /**
     * Enables the GPS Location for the App.
     *
     * @param mLocationRequest LocationRequest
     */
    private void enableGpsSettingsForLocation(LocationRequest mLocationRequest) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });
        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(),
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    /**
     * For adding the EndLocation Marker on the map
     *
     * @param currentLatLang {@link LatLng}
     */
    private void showEndLocationMarker(@NonNull com.google.android.gms.maps.model.LatLng currentLatLang) {
        if (googleMap != null)
            endLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(currentLatLang).draggable(true));
    }

    /**
     * For adding the StartLocation Marker on the map
     */
    private void showStartLocationMarker(@NonNull Location startLocation) {
        if (googleMap != null)
            startLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(new com.google.android.gms.maps.model.LatLng(startLocation.getLatitude(), startLocation.getLongitude())).draggable(true));
    }

    private void showWaypointMarker(ArrayList<WayPointsData> wayPointsData) {
        if (googleMap != null && wayPointsData != null && wayPointsData.size() > 0) {
            for (int iCount = 1; iCount <= wayPointsData.size() - 2; iCount++) {
                googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).position(new com.google.android.gms.maps.model.
                        LatLng(wayPointsData.get(iCount).getLatitude(), wayPointsData.get(iCount).getLongitude())).draggable(false));
            }
        }
    }

    @Override
    public void onMapLongClick(com.google.android.gms.maps.model.LatLng latLng) {
        Log.d(TAG, "onMarkerDragEnd");
        /*if (latLng != null) {
            endLocationMarker.remove();
            endLocationMarker = null;
            showEndLocationMarker(latLng);
        }*/
    }

    /**
     * Directions API call to get the Route details for the given start , destination and stop overs.
     *
     * @param currentLocation current Location with latitude and longitude {@link com.bosch.softtec.components.midgard.core.common.models.Location}.
     * @param destination     destination latitude and longitude {@link LatLng}
     * @param addedWayPoints  way point array.
     */
//    private void calculateRoutes(com.bosch.softtec.components.midgard.core.common.models.Location currentLocation,
//                                 com.bosch.softtec.components.midgard.core.common.models.LatLng destination,
//                                 ArrayList<com.bosch.softtec.components.midgard.core.common.models.LatLng> addedWayPoints) {
//
//        RouteData routeData = new RouteData(currentLocation, destination, RoutePurpose.INITIAL, addedWayPoints);
//        RouteOptions routeOptions = (new RouteOptions.Builder())
//                .useLiveTraffic(true).routeLimit(3)
//                .routeCalculationMode(RouteCalculationMode.CAR)
//                .roadPreferences(RoadPreference.AVOID_TOLL_ROADS, RoadPreference.AVOID_FERRIES)
//                .mergeContinueManeuver(true).build();
//
//        mRouteProvider.calculateRoutes(routeData, routeOptions, routeCalculationCallback);
//    }

    /**
     * Draws the route for given polyline on the map.
     *
     * @param routeTrace @RouteTrace
     */
//    private void drawRouteTrace(RouteTrace routeTrace) {
//        PolylineOptionsBuilder polylineOptionsBuilder = new PolylineOptionsBuilder();
//        List<PolylineOptions> polylineOptionsList = polylineOptionsBuilder.buildPolylineOptionsWithoutLiveTraffic(routeTrace, true);
//        for (PolylineOptions polylineOptions : polylineOptionsList) {
//            googleMap.addPolyline(polylineOptions);
//        }
//        showEndLocationMarker(new com.google.android.gms.maps.model.LatLng(mDestLat, mDestLon));
//        showStartLocationMarker(mStartLocation);
//        animateCameraForLatLngBounds(routeTrace);
//    }

    /**
     * Animated the camera focus for the plotted route.
     *
     * @param routeTrace {@link RouteTrace} drawn route trace.
     */
//    private void animateCameraForLatLngBounds(RouteTrace routeTrace) {
//        LatLngBounds latLngBounds = REUtils.getLatLngBounds(routeTrace);
//        /**initialize the padding for map boundary*/
//        int padding = 90;
//        /**create the camera with bounds and padding to set into map*/
//        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);
//        /**call the map call back to know map is loaded or not*/
//        googleMap.animateCamera(cu);
//    }

    /**
     * Will be triggered whenever the RouteProgress changes.
     *
     * @param location      {@link com.bosch.softtec.components.midgard.core.common.models.Location}
     * @param routeProgress {@link RouteProgress}
     */
//    @Override
//    public void onRouteProgressChanged(com.bosch.softtec.components.midgard.core.common.models.@NotNull Location location, @NotNull RouteProgress routeProgress) {
//        Log.v(TAG, "onRouteProgressChanged");
//        Step currentStep = routeProgress.getCurrentLegProgress().getCurrentStep();
//        ManeuverType maneuverType = currentStep.getManeuver().getManeuverType();
//        Roundabout roundabout = currentStep.getManeuver().getRoundabout();
//
//        EdgeInfo currentEdgeInfo = routeProgress.getCurrentLegProgress().getCurrentStepProgress().getCurrentEdgeInfo();
//        long remainingDurationCurrentStepSeconds =
//                routeProgress.getCurrentLegProgress().getCurrentStepProgress().getRemainingDuration().toSeconds();
//        String instruction = "";
//        if (remainingDurationCurrentStepSeconds > LONG_TIME_BEFORE_DURATION_SECONDS) {
//            instruction = getString(R.string.navigation_follow_route);
//        } else {
//            instruction = currentStep.getManeuver().getVisualInstruction();
//        }
//
//
//        int tbt_ID1 = getRemappedManeueverId(currentStep.getManeuver(), currentStep.getManeuver().getManeuverType());
//
//        Distance distance1 = routeProgress.getCurrentLegProgress().getCurrentStepProgress().getRemainingDistance();
//        int tbt_ID1_distance = (int) distance1.toMeter();
//
//
//        int tbt2Id;
//
//        tbt2Id = getRemappedManeueverId(routeProgress.getCurrentLegProgress().getNextStep().getManeuver(), routeProgress.getCurrentLegProgress().getNextStep().getManeuver().getManeuverType());
//
//        int tbt1Progress = getProgressLevel(tbt_ID1_distance);
//        if (tbt1Progress >= 5) { // send the tbt2 also
//            tbt2Id = -1;
//        }
//        int tbt1units = 1;
//        if (tbt_ID1_distance > 999) {
//            tbt_ID1_distance = tbt_ID1_distance / 100;
//            tbt1units = 2;
//        }
//
//
//        Distance distance2 = routeProgress.getRemainingDistance();
//        int total_dis = (int) distance2.toMeter();
//
//
//        int totalDistUnits = 1;
//
//        if (total_dis > 999) {
//            total_dis = total_dis / 100;
//            totalDistUnits = 2;
//        }
//
////        BleManager.Companion.parseAndPush(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
//        BleManager.Companion.parseAndPush(1, 1, tbt_ID1, tbt_ID1_distance, tbt1Progress, tbt1units, tbt2Id, 0, 0, total_dis, totalDistUnits, 1, 1, 1, 0);
//
//
//    }


    /**
     * Is triggered when the user is offroute.
     *
     * @param route {@link Route}
     */
//    @Override
//    public void onOffRouteDetected(@NotNull Route route) {
//        Log.v(TAG, "onOffRouteDetected");
//    }
//
//    /**
//     * Is triggered when there is a new Route calculated after the user was offroute.
//     *
//     * @param route {@link Route}
//     */
//    @Override
//    public void onRerouted(@NotNull Route route) {
//        Log.v(TAG, "onRerouted");
//    }
//
//    /**
//     * Is triggered when a reroute calculation has failed (e.g. because there is no internet connection).
//     * The navigation engine will try again to reroute. You will receive probably multiple of these callbacks until you get the onRerouted-callback.
//     *
//     * @param routeCalculationError {@link RouteCalculationError}
//     * @param s                     error message.
//     */
//    @Override
//    public void onReroutingAttemptFailed(@NotNull RouteCalculationError routeCalculationError, @Nullable String s) {
//        Log.v(TAG, "onReroutingAttemptFailed");
//    }
//
//    /**
//     * Will be triggered whenever a Milestone is hit. Providing the milestone type as well as the current routeProgress.
//     *
//     * @param milestone     {@link com.mapbox.services.android.navigation.v5.milestone.Milestone}
//     * @param routeProgress {@link com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress}
//     */
//    @Override
//    public void onMilestoneReached(@NotNull Milestone milestone, @NotNull RouteProgress routeProgress) {
//        Log.v(TAG, "onMilestoneReached");
//    }
//
//    @Override
//    public void onNavigationStarted(@NotNull Route route) {
//        Toast.makeText(getActivity(), "Navigation Started", Toast.LENGTH_SHORT).show();
//        Log.v(TAG, "onNavigationStarted");
//    }
//
//    @Override
//    public void onNavigationStopped(@NotNull Route route) {
//        Toast.makeText(getActivity(), "Navigation Stopped", Toast.LENGTH_SHORT).show();
//        Log.v(TAG, "onNavigationStopped");
//    }
//
//    @Override
//    public void onFasterRouteFound(@NotNull Route route, @NotNull Duration duration) {
//        Log.v(TAG, "onFasterRouteFound");
//    }
//
//    @Override
//    public void onTrafficChange(@NotNull Route route, @NotNull TrafficChange trafficChange) {
//        Log.v(TAG, "onTrafficChange");
//    }

    /**
     * Interface to listens the touch occured in map view.
     */
    public interface OnTouchListener {
        void onTouch();
    }

    /**
     * Listener for the touch on map.
     *
     * @param listener {@link OnTouchListener}
     */
    public void setListener(OnTouchListener listener) {
        mListener = listener;
    }

    /**
     * To handle the touch for the map and scroll view.
     */
    public class TouchableWrapper extends FrameLayout {
        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mListener.onTouch();
                    break;
                case MotionEvent.ACTION_UP:
                    mListener.onTouch();
                    break;
            }
            return super.dispatchTouchEvent(event);
        }
    }


}
