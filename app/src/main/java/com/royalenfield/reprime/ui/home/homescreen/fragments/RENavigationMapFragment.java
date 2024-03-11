package com.royalenfield.reprime.ui.home.homescreen.fragments;

import static android.content.Context.BATTERY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.royalenfield.reprime.utils.REConstants.BCT_DISTANCE;
import static com.royalenfield.reprime.utils.REConstants.BCT_DURATION;
import static com.royalenfield.reprime.utils.REConstants.BCT_SPEED;
import static com.royalenfield.reprime.utils.REConstants.KEY_TRIPPER_GTM;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bosch.softtec.components.core.conversion.LengthUnit;
import com.bosch.softtec.components.core.models.Distance;
import com.bosch.softtec.components.core.models.Duration;
import com.bosch.softtec.components.core.models.Interval;
import com.bosch.softtec.components.core.models.LatLng;
import com.bosch.softtec.components.core.models.Speed;
import com.bosch.softtec.components.core.models.breadcrumb.Breadcrumb;
import com.bosch.softtec.components.core.models.breadcrumb.BreadcrumbAddedListener;
import com.bosch.softtec.components.core.models.breadcrumb.BreadcrumbTrail;
import com.bosch.softtec.components.core.models.breadcrumb.PausedBreadcrumbTrailBehavior;
import com.bosch.softtec.components.midgard.core.directions.RouteCalculationCallback;
import com.bosch.softtec.components.midgard.core.directions.RouteProvider;
import com.bosch.softtec.components.midgard.core.directions.models.EdgeInfo;
import com.bosch.softtec.components.midgard.core.directions.models.Leg;
import com.bosch.softtec.components.midgard.core.directions.models.ManeuverType;
import com.bosch.softtec.components.midgard.core.directions.models.RoadPreference;
import com.bosch.softtec.components.midgard.core.directions.models.Roundabout;
import com.bosch.softtec.components.midgard.core.directions.models.Route;
import com.bosch.softtec.components.midgard.core.directions.models.RouteCalculationError;
import com.bosch.softtec.components.midgard.core.directions.models.RouteCalculationMode;
import com.bosch.softtec.components.midgard.core.directions.models.RouteData;
import com.bosch.softtec.components.midgard.core.directions.models.RouteOptions;
import com.bosch.softtec.components.midgard.core.directions.models.RoutePurpose;
import com.bosch.softtec.components.midgard.core.directions.models.RouteTrace;
import com.bosch.softtec.components.midgard.core.directions.models.Step;
import com.bosch.softtec.components.midgard.core.directions.models.WayPoint;
import com.bosch.softtec.components.midgard.core.navigation.NavigationListener;
import com.bosch.softtec.components.midgard.core.navigation.NavigationProvider;
import com.bosch.softtec.components.midgard.core.navigation.milestone.Milestone;
import com.bosch.softtec.components.midgard.core.navigation.milestone.MilestoneListener;
import com.bosch.softtec.components.midgard.core.navigation.model.FasterRouteConfiguration;
import com.bosch.softtec.components.midgard.core.navigation.model.LegProgress;
import com.bosch.softtec.components.midgard.core.navigation.model.MilestoneConfiguration;
import com.bosch.softtec.components.midgard.core.navigation.model.NavigationNotification;
import com.bosch.softtec.components.midgard.core.navigation.model.NavigationOptions;
import com.bosch.softtec.components.midgard.core.navigation.model.OffRouteConfiguration;
import com.bosch.softtec.components.midgard.core.navigation.model.RemainingRouteData;
import com.bosch.softtec.components.midgard.core.navigation.model.RouteProgress;
import com.bosch.softtec.components.midgard.core.navigation.model.TrafficAwareReroutingConfiguration;
import com.bosch.softtec.components.midgard.core.navigation.offroute.OffRouteListener;
import com.bosch.softtec.components.midgard.core.navigation.routeprogress.RouteProgressListener;
import com.bosch.softtec.components.midgard.core.navigation.traffic.TrafficChange;
import com.bosch.softtec.components.midgard.core.navigation.traffic.TrafficListener;
import com.bosch.softtec.components.midgard.core.navigation.traffic.TrafficType;
import com.bosch.softtec.components.midgard.directions.google.GoogleDirectionsApiCredentials;
import com.bosch.softtec.components.midgard.directions.google.GoogleDirectionsConfiguration;
import com.bosch.softtec.components.midgard.directions.google.GoogleRouteProvider;
import com.bosch.softtec.components.theseus.BreadcrumbTrailManager;
import com.bosch.softtec.components.theseus.record.BreadcrumbTrailRecordingOptions;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.royalenfield.bluetooth.NavigationUtilsKt;
import com.royalenfield.bluetooth.ble.BleManagerProvider;
import com.royalenfield.bluetooth.ble.DeviceInfo;
import com.royalenfield.bluetooth.ble.IBleManager;
import com.royalenfield.bluetooth.client.CRCCalculator;
import com.royalenfield.bluetooth.client.NavProtocol;
import com.royalenfield.bluetooth.otap.RELogger;
import com.royalenfield.bluetooth.otap.TestLogger;
import com.royalenfield.bluetooth.util.TbtType;
import com.royalenfield.bluetooth.utils.BLEDeviceManager;
import com.royalenfield.extensions.DistanceExtensionsKt;
import com.royalenfield.extensions.PolylineOptionsBuilder;
import com.royalenfield.extensions.RouteTraceOptions;
import com.royalenfield.mapboxNavigation.NavigationProviderGenerator;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.configuration.ConfigurationPrefManager;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.notification.listener.PhoneStateListener;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.rest.web.RESSLClient;
import com.royalenfield.reprime.ui.home.homescreen.helpers.ClusterRenderer;
import com.royalenfield.reprime.ui.home.homescreen.models.ClusterMarker;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.navigation.activity.CrashHandler;
import com.royalenfield.reprime.ui.home.navigation.activity.NavigationSummaryActivity;
import com.royalenfield.reprime.ui.home.navigation.interactor.RENavigationInteractor;
import com.royalenfield.reprime.ui.home.navigation.listener.NavigationRouteListener;
import com.royalenfield.reprime.ui.home.navigation.listener.SaveTripSummaryDetailsApiRequest;
import com.royalenfield.reprime.ui.home.navigation.listener.SaveTripSummaryEndPoint;
import com.royalenfield.reprime.ui.home.navigation.listener.SaveTripSummaryStartPoint;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.MapFragment;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REConstantsInterface;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link RENavigationMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RENavigationMapFragment extends REBaseFragment implements OnMapReadyCallback, NavigationListener, RouteCalculationCallback,
        BreadcrumbAddedListener, RouteProgressListener, OffRouteListener, MilestoneListener, TrafficListener, TextToSpeech.OnInitListener,
        GoogleMap.OnMarkerClickListener, LocationListener, GoogleMap.OnPolylineClickListener, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnPoiClickListener, PhoneStateListener, REConstantsInterface {

    public static final String TAG_MAP_FRAGMENT = RENavigationMapFragment.class.getName();
    private static final long MIN_TIME_BW_UPDATES = 0;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    private static final long RIGHT_BEFORE_DURATION_SECONDS = 10L;
    private static final long SOME_TIME_BEFORE_DURATION_SECONDS = 70L;
    private static final long LONG_TIME_BEFORE_DURATION_SECONDS = 150L;
    private final String TAG = RENavigationMapFragment.class.getSimpleName();
    private OnTouchListener mListener;
    private RidesListeners.CurrentLocationListener mCurrentLocationListener;
    private static RidesListeners.RouteProgressListener mRouteProgressListener;

    private NavigationRouteListener navigationRouteListener;
    private OnRENavigationListener mRENavListener;
    private MarkerClickListener mMarkerClickListener;
    private NavigationProvider mNavigationProvider;
    public TextToSpeech textToSpeech;
    private ConfigurationPrefManager configurationPrefManager;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private static BreadcrumbTrail lastRecordedBreadcrumbTrail;
    private double mSourceLat, mSourceLon, mDestLat, mDestLon;
    private Location mStartLocation;
    public Route chosenRoute;
    private BreadcrumbTrailManager breadcrumbTrailManager;
    private ArrayList<WayPoint> mAddedWayPoints = new ArrayList<>();
    private int isNight = 0;
    private boolean isRouteReconstruction = false;
    private PausedBreadcrumbTrailBehavior selectedPausedBreadcrumbTrailBehavior = PausedBreadcrumbTrailBehavior.DISCARD;
    //private ArrayList<Breadcrumb> recordedBreadcrumbs = new ArrayList<>();
    private boolean mIsRecording;
    private boolean offRouteDetected = false;
    private int mBatteryLevel = -1;
    private Timer timer = null;
    private IBleManager manager = BleManagerProvider.INSTANCE.getInstance();
    private Queue<byte[]> messageByteArrayQueue = manager.getQueue();
    private BatteryManager mBatteryManager;
    private RouteCalculationMode myRouteCalculationMode = RouteCalculationMode.CAR;
    private List<String> mTwoWheelerCountriesList = new ArrayList<>();
    private boolean isDestroyed = false;
    private Handler mHandler;
    private Runnable mRunnable;
    private int locationCount = 0;
    private FragmentActivity mContext;
    private int mAPIFailureCount = 1;
    private List<Route> mRouteList = null;
    private boolean isWayPointAdded = false;
    private RELogger reLogger;
    private Polyline livePolyline;
    private Marker mLiveBikeMarker;
    private REUtteranceListener mUtteranceListener;
    private List<WayPoint> currentRemainingWayPoints;
    private ArrayList<WayPointsData> mWayPointDataList = new ArrayList<>();
    private SupportMapFragment supportMapFragment;
    private com.bosch.softtec.components.core.models.Location mLiveLocation;
    private Location mCurrentLocation;
    private boolean isUserGestureFound = false, isCameraMoving = false;
    public double mStartDragMarkerTag = 0.0, mEndDragMarkerTag = 0.0;
    private boolean enableMarkerDrag = true;
    private RouteProgress currentRouteProgress;
    private ClusterManager<ClusterMarker> mClusterManager;
    private ClusterRenderer mClusterRenderer;
    private com.bosch.softtec.components.core.models.Location currentLocation;
    private byte mCurrentCallStatus = CALL_ENDED;
    private boolean mCanSendTBT = false;
    public boolean isSpeakMuted = false;
    boolean isMilesUnitSelected = false;
    private SharedPreferences sharedpreferences;
    public boolean isCallNotificationAvailable = false;
    public String lastConnectedTripperSerialNo;
    public String tripSummaryTripperSerialNo;
    private SaveTripSummaryStartPoint startpoint;
    private SaveTripSummaryEndPoint endpoint;
    private String tripStartTime = "";
    private String tripEndTime = "";
    private String tripDistance = "";
    private String tripDuration = "";
    private String tripSpeed = "";
    private RecordingOptionState currentRecordingOptionState = RecordingOptionState.NONE;
    private Boolean isDeepRecordingEnabled = true;
    private BreadcrumbTrailRecordingOptions defaultRecordingOptions = new BreadcrumbTrailRecordingOptions.Builder()
            .interval(1_000)
            .fastestInterval(1_000)
            .smallestDisplacement(10F)
            .priority(BreadcrumbTrailRecordingOptions.PRIORITY_HIGH_ACCURACY)
            .build();
    private BreadcrumbTrailRecordingOptions shortRecordingOptions = new BreadcrumbTrailRecordingOptions.Builder()
            .interval(1_000)
            .fastestInterval(1_000)
            .smallestDisplacement(20F)
            .priority(BreadcrumbTrailRecordingOptions.PRIORITY_HIGH_ACCURACY)
            .build();
    private BreadcrumbTrailRecordingOptions longRecordingOptions = new BreadcrumbTrailRecordingOptions.Builder()
            .interval(1_000)
            .fastestInterval(1_000)
            .smallestDisplacement(150F)
            .priority(BreadcrumbTrailRecordingOptions.PRIORITY_HIGH_ACCURACY)
            .build();

    private TestLogger testLogger = new TestLogger();
    private String countryCodeTarasmu = null;

    public RENavigationMapFragment() {
        // Required empty public constructor
    }

    public void continueNavigation(
            CrashHandler.Data data,
            Location currentLocation,
            ArrayList<WayPointsData> wayPointsData,
            RouteCalculationCallback routeCalculationCallback) {
        selectedPausedBreadcrumbTrailBehavior = PausedBreadcrumbTrailBehavior.RESUME;
        isDeepRecordingEnabled = data.isDeepRecordingEnabled();

        com.bosch.softtec.components.core.models.Location origin
                = new com.bosch.softtec.components.core.models.Location(currentLocation);
        LatLng destination = data.getDestination();
        List<WayPoint> remainingWayPoints = data.getRemainingWayPoints();

        mWayPointDataList = wayPointsData;

        RouteData routeData = new RouteData(origin, destination, RoutePurpose.INITIAL, remainingWayPoints);
        RouteOptions routeOptions = setFilteredRouteOptions();
        getRouteProvider().calculateRoutes(routeData, routeOptions, routeCalculationCallback);
    }

    void drawMarkers(LatLng destination, List<WayPoint> remainingWayPoints) {
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) return;

        fragmentActivity.runOnUiThread(() -> {

            com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(destination.getLatitude(), destination.getLongitude());
            addMarker(latLng, true, false);

            for (WayPoint wayPoint : remainingWayPoints) {
                LatLng wayPointLatLng = wayPoint.getLatLng();
                latLng = new com.google.android.gms.maps.model.LatLng(wayPointLatLng.getLatitude(), wayPointLatLng.getLongitude());
                addMarker(latLng, false, false);
            }
        });
    }

    private void addMarker(com.google.android.gms.maps.model.LatLng latLng,
                           boolean isDestination, boolean isDraggable) {
        BitmapDescriptor bitmapDescriptor;
        if (isDestination) {
            bitmapDescriptor = BitmapDescriptorFactory.defaultMarker();
        } else {
            bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
        }
        MarkerOptions marker = new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptor)
                .draggable(isDraggable);
        Marker aMarker = googleMap.addMarker(marker);
        aMarker.setTag(latLng.latitude);
    }


    private enum RecordingOptionState {
        NONE,
        SHORT,
        LONG
    }

    private enum MapEvents {
        LONG_CLICK,
        DRAG
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RENavigationMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RENavigationMapFragment newInstance() {
        return new RENavigationMapFragment();
    }

    public static MapFragment newInstance(RidesListeners.CurrentLocationListener locationListener,
                                          RidesListeners.RouteProgressListener routeProgressListener) {
        mRouteProgressListener = routeProgressListener;
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        reLogger = new RELogger(mContext);
        assert mContext != null;
        mNavigationProvider = NavigationProviderGenerator.INSTANCE.getNavigationProvider(mContext);
        configurationPrefManager = ConfigurationPrefManager.getInstance(mContext);
        //handlePip(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        isMilesUnitSelected = REUtils.isMilesUnitSelected();
    }

    private RouteProvider getRouteProvider() {
        GoogleDirectionsConfiguration urlOverrideConfiguration =
                null;
        try {
            urlOverrideConfiguration = new GoogleDirectionsConfiguration.Builder().authenticationHeader(REUtils.getTBTAuthHeaders()).
                    googleDirectionsApiBaseUrl(REUtils.getMobileappbaseURLs().getTbtURL()).
                    googleDirectionsEndpoint("tbt/directions/json").
                    googleRoadsApiBaseUrl(REUtils.getMobileappbaseURLs().getTbtURL()).
                    googleSnapToRoadsEndpoint("tbt/v1/snapToRoads").
                    sslConfiguration(RESSLClient.createSslConfiguration()).
                    build();
        } catch (GeneralSecurityException e) {
            RELog.e(e);
        }
        return (new GoogleRouteProvider(new GoogleDirectionsApiCredentials(REConstants.NAVIGATION_DUMMY_KEY),
                urlOverrideConfiguration));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
        cancelScheduledTimer();
        stopNavigation();
        //   handlePip(false);
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            RELog.d(TAG, "TTS Destroyed");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_createride, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_create_ride);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
        sharedpreferences = mContext.getSharedPreferences("RE_APP", Context.MODE_PRIVATE);
        textToSpeech = new TextToSpeech(mContext, this);
        mUtteranceListener = new REUtteranceListener(mContext);
        textToSpeech.setOnUtteranceProgressListener(mUtteranceListener);
        mNavigationProvider = NavigationProviderGenerator.INSTANCE.getNavigationProvider(mContext);
        TouchableWrapper frameLayout = new TouchableWrapper(view.getContext());
        frameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((ViewGroup) view).addView(frameLayout,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBatteryManager = (BatteryManager) mContext.getSystemService(BATTERY_SERVICE);
        isMilesUnitSelected = REUtils.isMilesUnitSelected();
        getCountriesList();
        return view;
    }

    private void getCountriesList() {
        if (!REUtils.getTwoWheelerCountries().isEmpty()) {
            mTwoWheelerCountriesList = Arrays.asList(REUtils.getTwoWheelerCountries().split(","));
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.clear(); //clear old markers
            this.googleMap = googleMap;
            mClusterManager = new ClusterManager<>(mContext, this.googleMap);
            mClusterRenderer = new ClusterRenderer(mContext, this.googleMap, mClusterManager);
            mClusterManager.setRenderer(mClusterRenderer);
            NonHierarchicalDistanceBasedAlgorithm<ClusterMarker> algorithm = new NonHierarchicalDistanceBasedAlgorithm<>();
            mClusterManager.setAlgorithm(algorithm);
            this.googleMap.setOnPolylineClickListener(this);
            enableMarkerListeners();
            enableMyLocation();
            UiSettings mUiSettings = this.googleMap.getUiSettings();
            mUiSettings.setCompassEnabled(false);
            mUiSettings.setRotateGesturesEnabled(true);
            mUiSettings.setTiltGesturesEnabled(true);
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setMyLocationButtonEnabled(false);
            mUiSettings.setScrollGesturesEnabled(true);
            mUiSettings.setMapToolbarEnabled(false);
            if (mRENavListener != null) {
                adjustMapHeight(mRENavListener.getMapBottomPadding());
            }
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.setOnMyLocationButtonClickListener(() -> {
                onCurrentLocationClick();
                return true;
            });
            this.googleMap.setOnCameraMoveStartedListener(reason -> {
                if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    //map is touched..do something
                    isUserGestureFound = true;
                    isCameraMoving = true;
                }
            });
            this.googleMap.setOnCameraIdleListener(() -> {
                isCameraMoving = false;
                mClusterManager.onCameraIdle();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int dpToPx(float value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics()
        );
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    void enableMyLocation() {
        if (mContext != null) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission to access the location is missing.
                //requestForLocationPermission();
            } else if (googleMap != null) {
                // Access to the location has been granted to the app.
                googleMap.setMyLocationEnabled(true);
                // boolean statusOfGPS = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                // Log.e("test","statusOfGPS = "+statusOfGPS);
                //forms the location request
                if (!REUtils.isLocationEnabled(mContext)) {
                    enableGpsSettingsForLocation();
                } else {
                    getCurrentLocation(NETWORK_PROVIDER);
                }
            }
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
        SettingsClient client = LocationServices.getSettingsClient(mContext);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(mContext, locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
        });
        task.addOnFailureListener(mContext, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    startIntentSenderForResult(resolvable.getResolution().getIntentSender(),
                            REConstants.REQUEST_CHECK_SETTINGS,
                            null, 0, 0, 0, null);
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
                    getCurrentLocation(NETWORK_PROVIDER);
                    break;
                case Activity.RESULT_CANCELED:
                    REUserModelStore.getInstance().setLatitude(0);
                    REUserModelStore.getInstance().setLongitude(0);
                    hideLoading();
                    break;
            }
        }
    }

    public static BreadcrumbTrail getBreadcrumbTrail() {
        return lastRecordedBreadcrumbTrail;
    }

    public static void setBreadcrumbTrail(BreadcrumbTrail lastRecordedBreadcrumbTrail) {
        RENavigationMapFragment.lastRecordedBreadcrumbTrail = lastRecordedBreadcrumbTrail;
    }

    void routeReconstruct(BreadcrumbTrail breadcrumbTrail) {
        isRouteReconstruction = true;
        lastRecordedBreadcrumbTrail = breadcrumbTrail;
        RouteOptions routeOptions = setFilteredRouteOptions();
        RELog.e("bread crumb trail = " + breadcrumbTrail.getDateStart());
        getRouteProvider().calculateRouteFromBreadcrumbTrail(breadcrumbTrail, routeOptions, this);
    }

    private void updateDistanceAndDuration(Double distance, long duration) {
        if (mCurrentLocationListener != null) {
            mCurrentLocationListener.setDistance(distance);
        }

        if (navigationRouteListener != null) {
            navigationRouteListener.setRouteDuration(duration);
        }
    }

    /**
     * Gets the current location from different providers
     *
     * @param provider : String
     */
    private void getCurrentLocation(String provider) {
        try {
            mContext = getActivity();
            assert mContext != null;
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                showLoading();
                locationCount = locationCount + 1;
                locationManager = (LocationManager) REApplication.getAppContext()
                        .getSystemService(LOCATION_SERVICE);
                assert locationManager != null;
                locationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                mHandler = new Handler();
                mRunnable = () -> {
                    hideLoading();
                    mHandler.removeCallbacks(mRunnable);
                    if (locationCount == 2) {
                        locationCount = 0;//Reset the counter.
                        //Show the location error popup.
                        REUtils.showErrorDialog(mContext, getString(R.string.text_navigation_refresh));
                        return;//Control exit the method...
                    }
                    getCurrentLocation(GPS_PROVIDER);
                };
                mHandler.postDelayed(mRunnable, 20000);
            }
            RELog.e("getCurrentLocationCalled");
        } catch (Exception e) {
            RELog.e(e);
        }
    }


    /**
     * For adding the EndLocation Marker on the map
     *
     * @param currentLatLang {@link LatLng}
     */
    void showPOIMarker(@NonNull com.google.android.gms.maps.model.LatLng currentLatLang, String poiName,
                       BitmapDescriptor marker, boolean isDraggable) {
        if (googleMap != null) {
            this.googleMap.setOnMarkerClickListener(this);
            googleMap.addMarker(new MarkerOptions().icon(marker).
                    position(currentLatLang).title(poiName).draggable(isDraggable));
            this.googleMap.setOnMarkerDragListener(this);
        }
    }

    void clearMarkers() {
        try {
            if (googleMap != null) {
                if (livePolyline != null) {
                    livePolyline.remove();
                    livePolyline = null;
                }
                googleMap.clear();
            }

        } catch (Exception e) {
            RELog.e(e);
        }
    }

    /**
     * Calculates the Stat, Destination and Added way points list for the directions API request.
     * Called first time and update route as well
     */
    void calculateStartDestinationAndWayPointLatLang(ArrayList<WayPointsData> wayPointDataList) {
        showLoading();
        if (wayPointDataList != null && wayPointDataList.size() >= 2 && wayPointDataList.
                get(wayPointDataList.size() - 1).getLatitude() != 0) {
            mSourceLat = wayPointDataList.get(0).getLatitude();
            mSourceLon = wayPointDataList.get(0).getLongitude();
            mDestLat = wayPointDataList.get(wayPointDataList.size() - 1).getLatitude();
            mDestLon = wayPointDataList.get(wayPointDataList.size() - 1).getLongitude();
            calculateAndDrawRoute(wayPointDataList);
        }
    }


    private void calculateAndDrawRoute(ArrayList<WayPointsData> wayPointDataList) {
        assignWayPointDataList(wayPointDataList);
        mAPIFailureCount = 1;
        drawRoute();
        this.googleMap.setOnMarkerClickListener(null);
    }

    public void assignWayPointDataList(ArrayList<WayPointsData> wayPointDataList) {
        mWayPointDataList = new ArrayList<>();
        mWayPointDataList = wayPointDataList;
        mAddedWayPoints = new ArrayList<>();
        for (int i = 1; i < (wayPointDataList != null ? wayPointDataList.size() : 0) - 1; i++) {
            if (wayPointDataList != null) {
                LatLng latLng = new LatLng(wayPointDataList.get(i).getLatitude(), wayPointDataList.get(i).getLongitude(), null);
                mAddedWayPoints.add(new WayPoint(latLng, false));
                // mAddedWayPoints.add(new WayPoint(latLng, true));
            }

        }
    }

    /**
     * Draws the routes if source and destination location is given.
     */
    private void drawRoute() {
        if (mSourceLat != 0 && mSourceLon != 0 && mDestLat != 0 && mDestLon != 0) {
            //Creates start location object.
            mStartLocation = new Location("");
            mStartLocation.setLatitude(mSourceLat);
            mStartLocation.setLongitude(mSourceLon);
            calculateRoutes(new com.bosch.softtec.components.core.models.Location(mStartLocation),
                    new LatLng(mDestLat, mDestLon, null), mAddedWayPoints);
        }
    }

    /**
     * Directions API call to get the Route details for the given start , destination and stop overs.
     *
     * @param currentLocation current Location with latitude and longitude {@link
     *                        com.bosch.softtec.components.core.models.Location}.
     * @param destination     destination latitude and longitude {@link LatLng}
     * @param addedWayPoints  way point array.
     */
    private void calculateRoutes(com.bosch.softtec.components.core.models.Location currentLocation,
                                 LatLng destination,
                                 ArrayList<WayPoint> addedWayPoints) {

        RouteData routeData = new RouteData(currentLocation, destination, RoutePurpose.INITIAL, addedWayPoints);
        RouteOptions routeOptions = setFilteredRouteOptions();
        getRouteProvider().calculateRoutes(routeData, routeOptions, this);
    }

    @SuppressLint("RestrictedApi")
    private RouteOptions setFilteredRouteOptions() {
        List<RoadPreference> enumValues = new ArrayList<>();

        boolean isLiveTraffic = configurationPrefManager.isLiveTraffic();
//       enumValues.add(RoadPreference.AVOID_FERRIES);
        if (configurationPrefManager.isHighwaysEnabled()) {
            enumValues.add(RoadPreference.AVOID_HIGHWAYS);
        }
        if (configurationPrefManager.isTollRoad()) {
            enumValues.add(RoadPreference.AVOID_TOLL_ROADS);
        }
        if (configurationPrefManager.isFerriesEnabled()) {
            enumValues.add(RoadPreference.AVOID_FERRIES);
        }
        isNight = configurationPrefManager.isNightModeEnabled() ? 1 : 0;

        return (new RouteOptions.Builder())
                .useLiveTraffic(isLiveTraffic).routeLimit(3)
                .routeCalculationMode(myRouteCalculationMode)
                .roadPreferences(enumValues)
                .mergeContinueManeuver(false).build();
    }

    boolean getNavigationMode() {
        return isRouteReconstruction;
    }

    /**
     * For moving the Camera.
     *
     * @param location {@link Location}
     */
    private void animateCamera(Location location) {
        if (location != null) {
            com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.
                    LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    latLng, 14);
            googleMap.animateCamera(cameraUpdate);
        }
    }


    /**
     * Methods to Start the navigation for the chosen route
     */
    void startNavigation(boolean isRecording) {
        RELog.v("NAV", "startNavigationCalled");
        mIsRecording = isRecording;
        // handlePip(true);
        try {
            mCanSendTBT = true;
            initBreadcrumbTrailManager();
            registerNavigationListener();
            NavigationOptions navigationOptions = createNavigationOptions();
            isNight = configurationPrefManager.isNightModeEnabled() ? 1 : 0;
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mNavigationProvider.startNavigation(chosenRoute, navigationOptions);
            this.googleMap.setOnPolylineClickListener(null);
            disableMarkerListeners();
        } catch (IllegalArgumentException ignored) {
            Log.e("startNavigation: ", ignored.toString());
            RELog.e(ignored.toString());
        } catch (Exception e) {
            RELog.e(e);
        }
    }

   /* private void handlePip(boolean isNavActive) {
       // Log.e( "handlePip: >>>",isNavActive+"" );
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
                && REApplication.getInstance().ismIsDeviceConnected() &&
                REUtils.isNetworkAvailable(mContext)) {
            Rational rational = new Rational(526, 300);
            PictureInPictureParams.Builder
                    pip_Builder
                    = new PictureInPictureParams
                    .Builder();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pip_Builder.setAutoEnterEnabled(true)
                        .setAspectRatio(rational).build();
            } else {
                pip_Builder.setAspectRatio(rational).build();
            }
            mContext.enterPictureInPictureMode(pip_Builder.build());
        }
    }*/

    /**
     * Methods to get  the recording option deep/intelligent
     */
    void configureRecordingOption(boolean isDeepRecordingSelected) {
        this.isDeepRecordingEnabled = isDeepRecordingSelected;
    }

    void pauseRecord() {
        Log.e("bctpresist: ", "pause recording");
        if (breadcrumbTrailManager != null) {
            breadcrumbTrailManager.pause(true, breadcrumbTrail -> {
                lastRecordedBreadcrumbTrail = breadcrumbTrail;
                return null;
            });
        }
    }

    void resumeRecord() {
        // handlePip(true);
        selectedPausedBreadcrumbTrailBehavior = PausedBreadcrumbTrailBehavior.RESUME;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        currentRecordingOptionState = RecordingOptionState.NONE;
        if (isDeepRecordingEnabled) {
            breadcrumbTrailManager.start(defaultRecordingOptions, selectedPausedBreadcrumbTrailBehavior);
        } else {
            breadcrumbTrailManager.start(shortRecordingOptions, selectedPausedBreadcrumbTrailBehavior);
        }
    }

    public void stopRecord(boolean isRecord) {
        try {
            //  handlePip(false);
            mContext = getActivity();
            assert mContext != null;
            mContext.runOnUiThread(() -> {
                //showLoading();
                if (breadcrumbTrailManager != null) {
                    // there is an issue for Stopping Record in paused state, so we are resuming it before calling breadcrumbTrailManager.stop
                    if (!breadcrumbTrailManager.isRecording()) {
                        resumeRecord();
                    }
                    breadcrumbTrailManager.stop(false, breadcrumbTrail -> {
                        lastRecordedBreadcrumbTrail = breadcrumbTrail;
                        mRENavListener.onRENavigationEnded(isRecord);
                        if (!isRecord)
                            showNavigationSummaryScreen(breadcrumbTrail, isRecord);
                        return null;
                    });
                }
            });
        } catch (Exception e) {
            RELog.e(TAG, "stopRecord:" + e.getMessage() + ": cause:" + e.getCause());
            reLogger.appendLog("stopRecord:" + e.getMessage() + ": cause:" + e.getCause());
        }
    }

    private void showNavigationSummaryScreen(BreadcrumbTrail breadcrumbTrail, boolean isRecord) {
        try {
            //  handlePip(false);
            if (mContext != null) {
                Intent navigationSummaryIntent = new Intent(mContext, NavigationSummaryActivity.class);
                if (breadcrumbTrail != null) {
                    navigationSummaryIntent = createBCTData(navigationSummaryIntent, breadcrumbTrail);
                    reLogger.appendLog("NavigationSummaryActivity called:" + breadcrumbTrail.getBreadcrumbs().size());
                    RELog.e(TAG, "Recorded BCT values final result value = = " + breadcrumbTrail.getBreadcrumbs().size());
                    saveSummaryDetails(breadcrumbTrail);

                }
                hideLoading();
                navigationSummaryIntent.putExtra("action", "nav_end");
                startActivity(navigationSummaryIntent);
                mRENavListener.onRENavigationEnded(isRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RELog.e(TAG, "showNavigationSummaryScreen:" + e.getMessage() + ": cause:" + e.getCause());
            reLogger.appendLog("showNavigationSummaryScreen:" + e.getMessage() + ": cause:" + e.getCause());
            hideLoading();
            if (mContext != null) {
                Intent navigationSummaryIntent = new Intent(mContext, NavigationSummaryActivity.class);
                navigationSummaryIntent.putExtra("action", "nav_end");
                startActivity(navigationSummaryIntent);
                mRENavListener.onRENavigationEnded(isRecord);
            }
        }
    }

    /**
     * Create and build BCT intent data from various sources.
     *
     * @param aIntent
     * @param breadcrumbTrail
     * @return
     */
    private Intent createBCTData(Intent aIntent, BreadcrumbTrail breadcrumbTrail) {
        if (breadcrumbTrail != null) {
            Long strlngDuration = breadcrumbTrail.getRideSummary().getTotalDuration().toMinutes();
            Double strDuration = strlngDuration.doubleValue();

            double strDistance = breadcrumbTrail.getRideSummary().getTotalDistance().toMeters();
            double speed = Objects.requireNonNull(breadcrumbTrail.getRideSummary().getAverageSpeed()).getSpeedValue();
            //Log.e("BCTcreateBCTData",""+strDistance+ " "+strDuration);
            aIntent.putExtra(BCT_DISTANCE, strDistance);
            aIntent.putExtra(BCT_DURATION, strDuration);
            aIntent.putExtra(BCT_SPEED, speed);
        }
        return aIntent;
    }

    boolean isBCTRunning() {
        if (breadcrumbTrailManager != null) {
            return breadcrumbTrailManager.isRecording();
        }
        return false;
    }

    /**
     * Methods to Stop the navigation for the chosen route
     */
    void stopNavigation() {
        try {
            //   handlePip(false);
            if (mNavigationProvider != null) {
                RELog.v("NAV", "stopNavigationCalled");
                reLogger.appendLog("mNavigationProvider.stopNavigation()");
                mNavigationProvider.stopNavigation();
                offRouteDetected = false;
                unregisterNavigationListener();
            }

        } catch (IllegalArgumentException ignored) {
        } catch (Exception e) {
            RELog.d(TAG_MAP_FRAGMENT, "App crashed");
        }
    }

    private void initBreadcrumbTrailManager() {
        REApplication application = REApplication.getInstance();
        breadcrumbTrailManager = application.getBreadcrumbTrailManager();
        breadcrumbTrailManager.setBreadcrumbAddedListener(this);
        lastRecordedBreadcrumbTrail = null;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        currentRecordingOptionState = RecordingOptionState.NONE;
        if (isDeepRecordingEnabled) {
            breadcrumbTrailManager.start(defaultRecordingOptions, selectedPausedBreadcrumbTrailBehavior);
        } else {
            breadcrumbTrailManager.start(shortRecordingOptions, selectedPausedBreadcrumbTrailBehavior);
        }
    }


    /**
     * Unregistering the navigation listeners.
     */
    private void unregisterNavigationListener() {
        mNavigationProvider.removeOffRouteListener(this);
        mNavigationProvider.removeRouteProgressListener(this);
        mNavigationProvider.removeMilestoneListener(this);
        mNavigationProvider.removeNavigationListener(this);
        mNavigationProvider.removeTrafficListener(this);
        mNavigationProvider.removeImportantNavigationRecordingPointListener(breadcrumbTrailManager);
    }

    /**
     * Registering the navigation listeners.
     */
    private void registerNavigationListener() {
        mNavigationProvider.addOffRouteListener(this);
        mNavigationProvider.addRouteProgressListener(this);
        mNavigationProvider.addMilestoneListener(this);
        mNavigationProvider.addNavigationListener(this);
        mNavigationProvider.addTrafficListener(this);
        mNavigationProvider.addImportantNavigationRecordingPointListener(breadcrumbTrailManager);
    }

    private NavigationOptions createNavigationOptions() {
        Distance offRouteDistance = new Distance(configurationPrefManager.getOFF_ROUTE_TriggerDistance(), LengthUnit.METERS);
        Distance offRouteContinuousCheckDistance = new Distance(25, LengthUnit.METERS);
        Duration maximumDurationContinuousOffRoute = new Duration(configurationPrefManager.getOffTime(), TimeUnit.SECONDS);

        OffRouteConfiguration offRouteConfiguration = new OffRouteConfiguration(offRouteDistance, getRouteProvider(),
                offRouteContinuousCheckDistance, maximumDurationContinuousOffRoute);

        Duration durationRightBeforeManeuver = new Duration(RIGHT_BEFORE_DURATION_SECONDS, TimeUnit.SECONDS);
        Duration durationSomeTimeBeforeManeuver = new Duration(SOME_TIME_BEFORE_DURATION_SECONDS, TimeUnit.SECONDS);
        Duration durationLongTimeBeforeManeuver = new Duration(LONG_TIME_BEFORE_DURATION_SECONDS, TimeUnit.SECONDS);
        MilestoneConfiguration milestoneConfiguration = new MilestoneConfiguration(
                durationRightBeforeManeuver,
                durationSomeTimeBeforeManeuver,
                durationLongTimeBeforeManeuver);

        TrafficAwareReroutingConfiguration trafficAwareReroutingConfiguration = createTrafficAwareReroutingConfiguration();

        return new NavigationOptions.Builder()
                .enableOffRouteDetectedRerouting(offRouteConfiguration)
                .milestoneConfiguration(milestoneConfiguration)
                .enableTrafficAwareRerouting(trafficAwareReroutingConfiguration)
                .navigationNotification(getForegroundServiceNotification())
                .build();
    }

    private NavigationNotification getForegroundServiceNotification() {
        Intent intent = new Intent(mContext, REHomeActivity.class);
        intent.putExtra("ForegroundNotification", true);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        String notificationChannelId = mContext.getPackageName();
        NotificationChannel chan = new NotificationChannel(notificationChannelId, "RE_Channel", NotificationManager.IMPORTANCE_NONE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);

        return new NavigationNotification() {
            @Override
            public @NotNull Notification getNotification() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return new NotificationCompat.Builder(mContext, notificationChannelId)
                            .setSmallIcon(R.drawable.ic_small_notification)
                            .setContentTitle("Royal Enfield")
                            .setContentText(getResources().getString(R.string.nav_msg))
                            .setContentIntent(contentIntent)
                            .setAutoCancel(false) // otherwise Notification can be simply swiped away, you don't want that
                            .build();
                } else {
                    return new NotificationCompat.Builder(mContext).setContentTitle("Royal Enfield").setSmallIcon(R.drawable.ic_small_notification)
                            .setContentText("Navigation is being displayed on the Tripper Device").
                            setContentIntent(contentIntent).build();
                }
            }

            @Override
            public int getNotificationId() {
                return 16;
            }

            @Override
            public void updateNotification(@NotNull RouteProgress routeProgress) {

            }
        };
    }

    private TrafficAwareReroutingConfiguration createTrafficAwareReroutingConfiguration() {
        Interval updateRouteInterval = new Interval(5, TimeUnit.MINUTES);

        Interval checkForFasterRouteInterval = new Interval(10, TimeUnit.MINUTES);
        Duration minimumDurationDifference = new Duration(10, TimeUnit.MINUTES);
        FasterRouteConfiguration fasterRouteOptions = new FasterRouteConfiguration(checkForFasterRouteInterval, minimumDurationDifference);

        return new TrafficAwareReroutingConfiguration(
                getRouteProvider(),
                updateRouteInterval,
                fasterRouteOptions
        );
    }

    public void onArriveAtDestination() {
        RELog.e("onArriveAtDestination");
        try {
            if (!(REPreference.getInstance().getBoolean(mContext, "pip")))
                mRENavListener.stopNavigationCalled(mIsRecording);
            else
                mRENavListener.changePipWindowText();
        } catch (IllegalArgumentException | PreferenceException e) {
            RELog.e(e);
        }

    }

    private void onManeuverFinished(RouteProgress routeProgress) {
        if (routeProgress != null && mContext != null) {
            String mRemainingDistance = "";
            if (routeProgress.getCurrentLegProgress() != null &&
                    routeProgress.getCurrentLegProgress().getCurrentStepProgress() != null) {
                Distance mDistance = routeProgress.getCurrentLegProgress().
                        getCurrentStepProgress().getRemainingDistance();
                if (isMilesUnitSelected) {
                    mRemainingDistance = DistanceExtensionsKt.prettifyImperialForVoice(mDistance,
                            Objects.requireNonNull(mContext), false);
                } else {
                    mRemainingDistance = DistanceExtensionsKt.prettifyMetricForVoice(mDistance,
                            Objects.requireNonNull(mContext), false);
                }
            }
            if (!mRemainingDistance.isEmpty()) {
                speak(String.format(getResources().getString(R.string.navigation_follow_route_x),
                        mRemainingDistance));
            }
        }
    }

    private void onBeforeManeuver(RouteProgress routeProgress) {
        if (mContext != null && routeProgress != null) {
            String instruction = "";
            if (routeProgress.getCurrentLegProgress() != null && routeProgress.getCurrentLegProgress()
                    .getCurrentStep() != null && routeProgress.getCurrentLegProgress()
                    .getCurrentStep().getManeuver() != null) {
                instruction = routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getVisualInstruction();
                RELog.e("onBeforeManeuver", "Instruction :" + instruction);
            }
            String mRemainingDistance = "";
            Distance mRouteRemainingDistance = null;
            if (routeProgress.getCurrentLegProgress() != null && routeProgress.getCurrentLegProgress().getCurrentStepProgress() != null) {
                mRouteRemainingDistance = routeProgress.
                        getCurrentLegProgress().getCurrentStepProgress().getRemainingDistance();
                RELog.e("onBeforeManeuver", "mRouteRemainingDistance :" + mRouteRemainingDistance);
            }
            if (mRouteRemainingDistance != null) {
                if (isMilesUnitSelected) {
                    mRemainingDistance = DistanceExtensionsKt.prettifyImperialForVoice(mRouteRemainingDistance, mContext, false);

                } else {
                    mRemainingDistance = DistanceExtensionsKt.prettifyMetricForVoice(mRouteRemainingDistance, mContext, false);
                }
                RELog.e("onBeforeManeuver", "RemainingDistance :" + mRemainingDistance);
            }
            speak(mRemainingDistance + instruction);
        }
    }


    /**
     * Draws the route for given polyline on the map.
     *
     * @param route
     * @param isAlternateRoute
     */
    private void drawRouteTrace(Route route, boolean isAlternateRoute) {
        RouteTrace routeTrace = route.getRouteTrace();
        PolylineOptionsBuilder polylineOptionsBuilder = new PolylineOptionsBuilder();
        RouteTraceOptions routeTraceOptions = new RouteTraceOptions(false, false, 2.0F);
        if (!REApplication.getInstance().isNavigationInProgress()) {
            //Check if it is alternate route or not on user selection
            if (!chosenRoute.equals(route)) {
                isAlternateRoute = true;
            }
        }
        List<PolylineOptions> polylineOptionsList = polylineOptionsBuilder.buildPolylineOptionsForLiveTraffic(routeTrace, routeTraceOptions);
        for (PolylineOptions polylineOptions : polylineOptionsList) {
            livePolyline = googleMap.addPolyline(polylineOptions);
            livePolyline.setClickable(true);
            livePolyline.setWidth(20);
            livePolyline.setJointType(JointType.ROUND);
            livePolyline.setGeodesic(true);
            if (isAlternateRoute) {
                livePolyline.setColor(ContextCompat.getColor(mContext, R.color.map_gray));
                livePolyline.setZIndex(0);
            } else {
                livePolyline.setColor(ContextCompat.getColor(mContext, R.color.map_blue));
                livePolyline.setZIndex(1);
            }
            livePolyline.setTag(route);
        }
        if (!isAlternateRoute) {
            showEndLocationMarker(new com.google.android.gms.maps.model.LatLng(mDestLat, mDestLon));
            if (!REApplication.getInstance().isNavigationInProgress())
                showStartLocationMarker(mStartLocation);
            showWaypointMarker();
            animateCameraForLatLngBounds(routeTrace);
        }
    }

    public void enableMarkerListeners() {
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnMapLongClickListener(this);
        this.googleMap.setOnMarkerDragListener(this);
        this.googleMap.setOnPoiClickListener(this);
    }

    public void disableMarkerListeners() {
        this.googleMap.setOnMarkerClickListener(null);
        this.googleMap.setOnMapLongClickListener(null);
        this.googleMap.setOnMarkerDragListener(null);
        this.googleMap.setOnPoiClickListener(null);
    }

    public void enableMarkerDrag() {
        enableMarkerDrag = true;
        googleMap.setOnMarkerDragListener(this);
    }

    public void disableMarkerDrag() {
        enableMarkerDrag = false;
        googleMap.setOnMarkerDragListener(null);
    }

    /**
     * Animated the camera focus for the plotted route.
     *
     * @param routeTrace {@link RouteTrace} drawn route trace.
     */
    private void animateCameraForLatLngBounds(RouteTrace routeTrace) {
        LatLngBounds latLngBounds = REUtils.getLatLngBounds(routeTrace);
        int padding = 90;
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);
        try {
            googleMap.animateCamera(cu);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    /**
     * For adding the EndLocation Marker on the map
     *
     * @param currentLatLang {@link LatLng}
     */
    private void showEndLocationMarker(@NonNull com.google.android.gms.maps.model.LatLng currentLatLang) {
        if (googleMap != null)
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).
                    position(currentLatLang));
    }

    /**
     * For adding the StartLocation Marker on the map
     */
    private void showStartLocationMarker(@NonNull Location startLocation) {
        if (googleMap != null)
            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.
                    defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(new com.google.android.gms.maps.model.
                    LatLng(startLocation.getLatitude(), startLocation.getLongitude())).draggable(false));
    }

    private void showWaypointMarker() {
        try {
            if (googleMap != null && mAddedWayPoints != null && mAddedWayPoints.size() > 0) {
                mClusterManager.clearItems();
                for (int iCount = 0; iCount < mAddedWayPoints.size(); iCount++) {
                    com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.
                            LatLng(mAddedWayPoints.get(iCount).getLatLng().getLatitude(),
                            mAddedWayPoints.get(iCount).getLatLng().getLongitude());
                    mClusterManager.addItem(new ClusterMarker(latLng));
                }
            }
        } catch (Exception e) {
            RELog.e(TAG, e.getMessage());
        }
    }

    /**
     * This method is used for TTS
     *
     * @param text : String
     */
    private void speak(String text) {
        if (!isSpeakMuted) {
            try {
                RELog.e("TTS", "SpeakText " + text);
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, mUtteranceListener.getUtteranceId());
            } catch (Exception e) {
                RELog.e("TTS", "SpeakText %s: " + e.getMessage() + "& Cause:" + e.getCause());
            }
        }
    }

    /**
     * Listener for the touch on map.
     *
     * @param listener {@link OnTouchListener}
     */
    public void setListener(OnTouchListener listener) {
        mListener = listener;
    }

    void setRidesListener(RidesListeners.CurrentLocationListener listener) {
        mCurrentLocationListener = listener;
    }

    void setNavigationRouteListener(NavigationRouteListener navRouteListener) {
        navigationRouteListener = navRouteListener;
    }

    void setMarkerListener(MarkerClickListener markerListener) {
        mMarkerClickListener = markerListener;
    }

    void setRENavigationListener(OnRENavigationListener navListener) {
        mRENavListener = navListener;
    }

    @Override
    public void onNavigationStarted(@NotNull Route route) {
        RELog.v(TAG_MAP_FRAGMENT, "onNavigationStarted");
    }

    @Override
    public void onNavigationStopped(@NotNull Route route) {
        RELog.v(TAG_MAP_FRAGMENT, "onNavigationStopped");
        //  mRENavListener.onRENavigationEnded();
        cancelScheduledTimer();
    }

    @Override
    public void onRouteCalculationError(@NotNull RouteCalculationError routeCalculationError, @Nullable String s) {
        try {
            RELog.e(TAG, "onRouteCalculationError: " + routeCalculationError.toString());
        } catch (Exception e) {
            RELog.e(e);
        }
        if (mContext != null) {
            if (routeCalculationError.name().equals(REConstants.TBT_API_UNAUTHORIZED)) {
                mContext.runOnUiThread(() -> {
                    hideLoading();
                    REUtils.showErrorDialog(mContext, REApplication.getAppContext().getString(R.string.error_nav_unauthorized));
                });
            } else if (routeCalculationError.name().equals("MISC")) {
                hideLoading();
                REUtils.showErrorDialog(mContext, "Please try later");

            } else {
                if (mAPIFailureCount > REConstants.NAV_API_RETRY_COUNTER) {
                    mContext.runOnUiThread(() -> {
                        hideLoading();
                        REUtils.showErrorDialog(mContext, REApplication.getAppContext().getString(R.string.error_tbt_api));
                    });
                } else {
                    mAPIFailureCount++;
                    drawRoute();
                }
            }
            /*mContext.runOnUiThread(() -> {
                navigationRouteListener.
                        setRouteEngineFailed(routeCalculationError.name());
            });*/
        }
    }

    /* public void PrintRoute(Route route){

         List<Leg> r1 = route.getLegs();
         for(int i=0; i<r1.size();i++){
             Leg l = r1.get(i);
             List<Step> s1 = l.getSteps();
             for (int j=0;j<s1.size();j++){
                 Step s = s1.get(j);
                 Double distance = s.getDistance().toMeters();
                 String instuction = s.getManeuver().getVoiceInstruction();
                 Log.e("ROUTEDetails","Dis "+distance+" Instructn "+instuction);
             }
         }

     }*/
    @Override
    public void onRouteCalculationFinished(@NotNull List<? extends Route> list) {
        mRouteList = new ArrayList<>();
        mRouteList.addAll(list);

        Collections.reverse(mRouteList);
        hideLoading();
        if (mContext != null) {
            mContext.runOnUiThread(() -> {
                if (mRouteList.size() > 0) {
                    chosenRoute = mRouteList.get(mRouteList.size() - 1);
                    // PrintRoute(chosenRoute);
                    plotAlternateRoutes();
                } else {
                    mContext.runOnUiThread(() -> {
                        REUtils.showErrorDialog(mContext, "Unable to find the route.");
                    });
                }
            });
        }
    }

    private void plotAlternateRoutes() {
        clearMarkers();
        for (int i = 0; i < mRouteList.size(); i++) {
            plotRoutesOnMap(mRouteList.get(i), false);
        }
    }

    /**
     * <P>This method will loop to plot multiple routes in the map.
     * User will choose one from the three(as of now) suggested routes.
     * First route will be highlighted default to the user as blue color rest are greyed out.
     * </P>
     *
     * @param route
     * @param isShadowRoute
     */
    private void plotRoutesOnMap(Route route, boolean isShadowRoute) {
        if (isRouteReconstruction) {
            mStartLocation = new Location("");
            mStartLocation.setLatitude(route.getRouteData().getOrigin().getLatitude());
            mStartLocation.setLongitude(route.getRouteData().getOrigin().getLongitude());
            mDestLat = route.getRouteData().getDestination().getLatitude();
            mDestLon = route.getRouteData().getDestination().getLongitude();
            if (navigationRouteListener != null) {
                Location destination = new Location("");
                destination.setLatitude(mDestLat);
                destination.setLongitude(mDestLon);
                //navigationRouteListener.setTripTimeInfo(chosenRoute);
                navigationRouteListener.setReconstructedRouteInfo(mStartLocation, destination);
            }
        }
        //    Log.e( "plotRoutesOnMap: ",route.getRouteData().getWayPoints().size()+"" );
        drawRouteTrace(route, isShadowRoute);
        updateDistanceAndDuration(route.getDistance().toMeters(),
                route.getDuration().toMinutes());
        RELog.e("RouteDistance", "Distance = " + route.getDistance().toKilometers());
        adjustMapHeight(mRENavListener.getMapBottomPadding());
    }

    /**
     * draw the live route in map with bike
     */
    public void plotReroutedRoutesOnMap() {
        clearMarkers();
        plotRoutesOnMap(chosenRoute, true);
        plotRoutesOnMap(chosenRoute, false);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_live_loc_on);
        com.google.android.gms.maps.model.LatLng latLng = new
                com.google.android.gms.maps.model.LatLng(chosenRoute.getRouteData().getOrigin().getLatitude(),
                chosenRoute.getRouteData().getOrigin().getLongitude());
        MarkerOptions marker = new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptor)
                .draggable(false);
        mLiveBikeMarker = googleMap.addMarker(marker);
    }

    private void updateLiveRoute(com.bosch.softtec.components.core.models.Location location, @NotNull RouteProgress routeProgress) {
        if (livePolyline != null) {
            com.bosch.softtec.components.midgard.core.directions.models.Polyline remainingPolyline = routeProgress.getRemainingPolyline();
            List<com.google.android.gms.maps.model.LatLng> points = new ArrayList<>();
            for (LatLng latLng : remainingPolyline.getPoints()) {
                points.add(new com.google.android.gms.maps.model.LatLng(latLng.getLatitude(), latLng.getLongitude()));
            }
            livePolyline.setPoints(points);
            livePolyline.setZIndex(1);
            com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(location.getLatitude(), location.getLongitude());
            plotLiveMarker(latLng, 17, location);
            updateDistanceAndDuration(routeProgress.getRemainingDistance().toMeters(), routeProgress.getRemainingDuration().toMinutes());
            RELog.e("RemainingRouteDistance", "Remaining Distance = " + routeProgress.getRemainingDistance().toKilometers());
        }
    }

    //Breadcrumb testBreadcrumb;
    @Override
    public void onBreadcrumbAdded(@NotNull Breadcrumb breadcrumb) {
        //recordedBreadcrumbs.add(breadcrumb);

        //New changes below
       /* if (testBreadcrumb == null){
            testBreadcrumb = breadcrumb;
        } else {
            Double breadcrumb_distance  = testBreadcrumb.getLocation().distanceTo(breadcrumb.getLocation()).toMeters();
            Log.e("breadCrmbDistance","Val is "+breadcrumb_distance);
            testBreadcrumb = breadcrumb;
        }*/
    }

    @Override
    public void onMilestoneReached(@NotNull Milestone milestone, @NotNull RouteProgress routeProgress) {
        RELog.v(TAG_MAP_FRAGMENT, "onMilestoneReached");
        mRENavListener.updateOnMilestoneReached(getUpcomingWayPoint());
        switch (milestone) {
            case RIGHT_BEFORE_MANEUVER:
            case SOME_TIME_BEFORE_MANEUVER:
                RELog.v(TAG_MAP_FRAGMENT, "onBeforeManeuver");
                onBeforeManeuver(routeProgress);
                break;
            case FINISHED_MANEUVER:
                onManeuverFinished(routeProgress);
                RELog.v(TAG_MAP_FRAGMENT, "onManeuverFinished");
                break;
            case ARRIVED_AT_DESTINATION:
                onArriveAtDestination();
                break;
            case ARRIVED_AT_WAY_POINT:
                //WayPoint lastVisitedWayPoint = routeProgress.getLastVisitedWayPoint();
                // only during TBT navigation and not for BCT Reconstruction
               /* if (lastVisitedWayPoint != null && lastVisitedWayPoint.isStopOver()) {
                    updateWayPointReachInTripper();
                }*/
                if (!isRouteReconstruction) {
                    updateWayPointReachInTripper();
                }
                break;
        }
    }

    private byte[] createOffRouteFrame() {
        getBatterPercentage();
        int mTBTID_1;
        if (isLowBattery()) {
            mTBTID_1 = TbtType.LOW_BATTERY_NOTOFICATION;
        } else if (!REUtils.isNetworkAvailable(mContext)) {
            mTBTID_1 = TbtType.MOBILE_DATA;
        } else {
            mTBTID_1 = TbtType.RE_ROUTE_INDICATION;
        }
        byte[] frame = NavProtocol.INSTANCE.getNavMessageFrame(
                1, 1,
                mTBTID_1,
                0,
                0,
                1,
                -1,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                isNight
        );
        return frame;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onOffRouteDetected(@NotNull Route route) {
        try {
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            testLogger.appendLog(currentTime + "onOffRouteDetected");
            RELog.v(TAG, "onOffRouteDetected");
            isNight = configurationPrefManager.isNightModeEnabled() ? 1 : 0;
            RELog.e("onOffRouteDetected", "onOffRouteDetected distance:" + route.getDistance().toMeters());
            RELog.e("onOffRouteDetected", "onOffRouteDetected duration:" + route.getDuration().toMinutes());
            reLogger.appendLog("onOffRouteDetected distance:" + route.getDistance().toMeters());
            reLogger.appendLog("onOffRouteDetected duration:" + route.getDuration().toMinutes());
            navigationRouteListener.setUpdatedRouteDistance(route.getDistance().toMeters());
            navigationRouteListener.setUpdatedRouteDuration(route.getDuration().toMinutes());
            navigationRouteListener.showNavigationRerouteMessage(true);
            navigationRouteListener.showDebugMessage("onOffRouteDetected");
            offRouteDetected = true;
            if (!isDeepRecordingEnabled) {
                currentRecordingOptionState = RecordingOptionState.NONE;
                breadcrumbTrailManager.updateRecordingOptions(shortRecordingOptions);
            }
            timer = new Timer();
            class RetryTask extends TimerTask {
                @Override
                public void run() {
                    if (!offRouteDetected) {
                        cancelScheduledTimer();
                    } else if (mCurrentCallStatus == CALL_INCOMING && isCallNotificationAvailable && configurationPrefManager != null && configurationPrefManager.getEnableCallNotification()) {
                        mCanSendTBT = false;
                        messageByteArrayQueue.clear();
                        notifyCallToTFT(CALL_INCOMING);
                    } else {
                        if (REApplication.getInstance().ismIsDeviceConnected() &&
                                REApplication.getInstance().isIsDeviceAuthorised()) {
                            messageByteArrayQueue.offer(createOffRouteFrame());
                        } else {
                            messageByteArrayQueue.clear();
                        }
                    }
                }
            }
            timer.scheduleAtFixedRate(new RetryTask(), 0, 1000);
        } catch (Exception e) {
            RELog.e(RENavigationMapFragment.class.getSimpleName(), "onOffRouteDetected():" + e.getMessage()
                    + ":Cause" + e.getCause());
            reLogger.appendLog("onOffRouteDetected():" + e.getMessage() + ":Cause" + e.getCause());
        }
    }

    void cancelScheduledTimer() {
        try {
            if (null != timer) {
                timer.cancel();
                timer = null;
            }
        } catch (Exception e) {
            RELog.e(RENavigationMapFragment.class.getSimpleName(), "cancelScheduledTimer():" + e.getMessage()
                    + ":Cause" + e.getCause());
            reLogger.appendLog("cancelScheduledTimer():" + e.getMessage() + ":Cause" + e.getCause());
        }
    }

    private void getBatterPercentage() {
        if (mBatteryManager != null) {
            mBatteryLevel = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            RELog.e("BatteryLevel", "Percentage :" + mBatteryLevel);
        }
    }

    @Override
    public void onRerouted(@NotNull Route route) {
        try {
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            testLogger.appendLog(currentTime + "onRerouted");
            offRouteDetected = false;
            chosenRoute = route;
            RELog.v(TAG, "onRerouted");
            //update the chosen route with rerouted data and draw live route
            mContext.runOnUiThread(() -> {
                navigationRouteListener.setUpdatedRouteDistance(route.getDistance().toMeters());
                navigationRouteListener.setUpdatedRouteDuration(route.getDuration().toMinutes());
                navigationRouteListener.showNavigationRerouteMessage(false);
                navigationRouteListener.showDebugMessage("onRerouted");
                plotReroutedRoutesOnMap();
            });
            if (!route.getLegs().isEmpty() && !route.getLegs().get(0).getSteps().isEmpty()) {
                Leg firstLeg = route.getLegs().get(0);
                Step firstStep = firstLeg.getSteps().get(0);
                String message = firstStep.getManeuver().getVisualInstruction();
                speak(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackOnRoute(@NotNull Route route) {
        try {
            offRouteDetected = false;
            RELog.v(TAG, "onBackOnRoute");
            mContext.runOnUiThread(() -> {
                navigationRouteListener.showNavigationRerouteMessage(false);
            });
            if (!route.getLegs().isEmpty() && !route.getLegs().get(0).getSteps().isEmpty()) {
                Leg firstLeg = route.getLegs().get(0);
                Step firstStep = firstLeg.getSteps().get(0);
                String message = firstStep.getManeuver().getVisualInstruction();
                speak(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReroutingAttemptFailed(@NotNull RouteCalculationError routeCalculationError, @Nullable String s) {
        RELog.v(TAG, "onReroutingAttemptFailed");
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRouteProgressChanged(com.bosch.softtec.components.core.models.@NotNull Location location, @NotNull RouteProgress routeProgress) {
        RELog.v(TAG, "onRouteProgressChanged");

//        Date currentTime = Calendar.getInstance().getTime();
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String remainingDistance = String.valueOf(routeProgress.getCurrentLegProgress().getRemainingDistance().toMeters());
        String remainingDuration = String.valueOf(routeProgress.getCurrentLegProgress().getRemainingDuration());
        testLogger.appendLog(currentTime + " onRouteProgressChanged " + location.getLatitude() + " " + location.getLongitude());
        testLogger.appendLog("Remaining Distance :" + "" + remainingDistance);
        testLogger.appendLog("Remaining Duration :" + "" + remainingDuration);

        currentLocation = location;
        updateLiveRoute(location, routeProgress);

        RemainingRouteData remainingRouteData = routeProgress.getRemainingRouteData();
        CrashHandler.Data data = new CrashHandler.Data(
                remainingRouteData.getDestination(),
                remainingRouteData.getWayPoints(),
                mWayPointDataList,
                mIsRecording,
                isDeepRecordingEnabled,
                lastRecordedBreadcrumbTrail
        );
        REApplication.getInstance().getCrashHandler().setData(data);

        try {
            int oldRemainingWayPointCount = currentRemainingWayPoints == null ? 0 : currentRemainingWayPoints.size();
            int newRemainingWayPointCount = remainingRouteData.getWayPoints().size();
            currentRemainingWayPoints = remainingRouteData.getWayPoints();
            currentRouteProgress = routeProgress;
            if (oldRemainingWayPointCount != newRemainingWayPointCount) {
                mRENavListener.updateOnMilestoneReached(getUpcomingWayPoint());
            }
            Step currentStep = routeProgress.getCurrentLegProgress().getCurrentStep();

            ManeuverType maneuverType = currentStep.getManeuver().getManeuverType();
            //RELog.e("ManeuverType:" + maneuverType.name());
            Roundabout roundabout = currentStep.getManeuver().getRoundabout();

            EdgeInfo currentEdgeInfo = routeProgress.getCurrentLegProgress().getCurrentStepProgress().getCurrentEdgeInfo();
            Double currentMaxSpeed = getCurrentMaxSpeed(currentEdgeInfo);
            long remainingDurationCurrentStepSeconds =
                    routeProgress.getCurrentLegProgress().getCurrentStepProgress().getRemainingDuration().toSeconds();

            String instruction = "";
            if (remainingDurationCurrentStepSeconds > LONG_TIME_BEFORE_DURATION_SECONDS) {
                instruction = getString(R.string.navigation_follow_route);
            } else {
                instruction = currentStep.getManeuver().getVisualInstruction();
            }

            RELog.e("BCTTest", "instruction :" + currentStep.getManeuver().getVisualInstruction());
            //Updates the values to RENavigationFragment
            if (mRouteProgressListener != null) {
                mRouteProgressListener.setRemainingDistance(routeProgress.getRemainingDistance().toMeters());
                mRouteProgressListener.setRemainigDuration(routeProgress.getRemainingDuration().toMinutes());
            }
            //Stop sending TBT messages if a call is in incoming state.
            if (configurationPrefManager != null && configurationPrefManager.getEnableCallNotification()) {
                if (isCallNotificationAvailable) {
                    if (mCurrentCallStatus == CALL_INCOMING) {
                        mCanSendTBT = false;
                        messageByteArrayQueue.clear();
                        notifyCallToTFT(CALL_INCOMING);
                        return;
                    }
                }
            }
            //Continue TBT messages only if call get answered/rejected/ended/missed && TFT get that state notified.
            if ((mCurrentCallStatus == CALL_ACCEPTED || mCurrentCallStatus == CALL_REJECTED || mCurrentCallStatus == CALL_ENDED || mCurrentCallStatus == CALL_MISSED) && mCanSendTBT) {
                parseRouteProgress(routeProgress);
            }
            // parseRouteProgress(routeProgress);

        } catch (Exception e) {
            RELog.e(TAG, "onRouteProgressChanged called");
        }
    }

    private WayPointsData getUpcomingWayPoint() {
        if (!REApplication.getInstance().isNavigationInProgress()) {
            return getLastWayPoint();
        }
        if (mWayPointDataList.size() == 2) {
            return getLastWayPoint();
        }
        if (currentRemainingWayPoints != null && currentRemainingWayPoints.size() > 0) {
            WayPoint upcomingWP = currentRemainingWayPoints.get(0); //take the first waypoint from the remaining waypoint list
            for (WayPointsData wayPoint : mWayPointDataList) {
                if (upcomingWP.getLatLng().getLatitude() == wayPoint.getLatitude() &&
                        upcomingWP.getLatLng().getLongitude() == wayPoint.getLongitude()) {
                    return wayPoint;
                }
            }
        }
        return getLastWayPoint();
    }

    /**
     * Get the destination as last waypoint
     *
     * @return
     */
    private WayPointsData getLastWayPoint() {
        Log.e("WPsizee", "size" + mWayPointDataList.size());
        return mWayPointDataList.get(mWayPointDataList.size() - 1);
    }

    private Double getCurrentMaxSpeed(EdgeInfo currentEdgeInfo) {
        if (currentEdgeInfo.getMaxSpeed() == null) {
            return Speed.UNKNOWN;
        } else if (currentEdgeInfo.getMaxSpeed().isMetric()) {
            return currentEdgeInfo.getMaxSpeed().toKilometersPerHour();
        } else {
            return currentEdgeInfo.getMaxSpeed().toMilesPerHour();
        }
    }

    @Override
    public void onFasterRouteFound(@NotNull Route route, @NotNull Duration duration) {
        int minutes = (int) duration.toMinutes();
        String minutesString = mContext.getResources().getQuantityString(R.plurals.plurals_x_minutes, minutes, minutes);
        RELog.e("TTS", "onFasterRouteFound");
        RELog.e("TTS", "minutes :" + minutesString);
        speak(String.format(getResources().getString(R.string.navigation_x_faster_route_found_speak),
                minutesString));
    }

    @Override
    public void onTrafficChange(@NotNull Route route, @NotNull TrafficChange trafficChange) {
        RELog.v(TAG, "onTrafficChange");
        TrafficType trafficType = trafficChange.getTrafficType();
    }

    @SuppressLint("MissingPermission")
    private void parseRouteProgress(RouteProgress routeProgress) {
        Distance distance = routeProgress.getCurrentLegProgress().getCurrentStep().getDistance();
        int previousTbt1RemainingDistance = DistanceExtensionsKt.prettifyMetricNumber(distance);

        long remainingDurationCurrentStepSeconds =
                routeProgress.getCurrentLegProgress().getCurrentStepProgress().getRemainingDuration().toSeconds();

        LegProgress legProgress = routeProgress.getCurrentLegProgress();

        Step nextstep = routeProgress.getCurrentLegProgress().getNextStep();

        String currentInstruction = routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getVisualInstruction();

        int tbt1Id = NavigationUtilsKt.getRemappedManeuverId(routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver(),
                routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getManeuverType());

//
//        if (!currentInstruction.equals(previousInstruction)) {
//            String string = routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getManeuverType().name() + "::" + currentInstruction;
//            Log.d("Maneuver_TAG_INPUT", string);
//            FileUtils.log(string, "RE_logs");
//        }
        String previousInstruction = currentInstruction;
/*
        if (remainingDurationCurrentStepSeconds > LONG_TIME_BEFORE_DURATION) {
            tbt1Id = TbtType.STRAIGHT;
        }
*/

        routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getVisualInstruction();
        RELog.d(TAG, routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getManeuverType().name()
                + " -> " + tbt1Id + "   " + routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getVisualInstruction());

        int tbt1RoundaboutExitNo = 0;
        if (routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getRoundabout() != null &&
                routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getRoundabout() instanceof Roundabout) {
            tbt1RoundaboutExitNo = routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getRoundabout().getWantedExitNumber();
        }

        tbt1Id += tbt1RoundaboutExitNo;

        RELog.d("NAV_INFO", "exit no = " + tbt1RoundaboutExitNo);
        int tbt2Id = 0;
        if (nextstep != null) {
            tbt2Id = NavigationUtilsKt.getRemappedManeuverId(nextstep.getManeuver(),
                    nextstep.getManeuver().getManeuverType());
        }

        int tbt2RoundaboutExitNo = 0;
        if (nextstep != null && nextstep.getManeuver().getRoundabout() != null &&
                nextstep.getManeuver().getRoundabout() instanceof Roundabout) {
            tbt2RoundaboutExitNo = nextstep.getManeuver().getRoundabout().getWantedExitNumber();
        }
        tbt2Id += tbt2RoundaboutExitNo;


        Distance tbt1_remDistance = routeProgress.getCurrentLegProgress().getCurrentStepProgress().getRemainingDistance();
        // value in meters
        int tbt1dist = DistanceExtensionsKt.prettifyMetricNumber(tbt1_remDistance);
        if (isMilesUnitSelected) {
            // value in feet
            // tbt1dist = (int) tbt1_remDistance.toFeet();
            tbt1dist = DistanceExtensionsKt.prettifyImpericalNumber(tbt1_remDistance);
        }
        // decremental value  should be in meters
        int decrementLevelTbt1RemainingDistance = DistanceExtensionsKt.getDecrementValueForDistance((int) tbt1_remDistance.toMeters());
        int tbt1Progress = DistanceExtensionsKt.getTbt1ProgressLevel((int) tbt1_remDistance.toMeters(), tbt1Id);
        if (tbt1Progress >= 5) { // send the tbt2 also
            tbt2Id = -1;
        }

        Distance tbt2_remDistance = routeProgress.getCurrentLegProgress().getCurrentStepProgress().getRemainingDistance();
        int tbt2dist = DistanceExtensionsKt.prettifyMetricNumber(tbt2_remDistance);
        if (isMilesUnitSelected) {
            // value in feet
            tbt2dist = DistanceExtensionsKt.prettifyImpericalNumber(tbt2_remDistance);
        }

        int tbt2Progress = DistanceExtensionsKt.getTbt2ProgressLevel((int) tbt2_remDistance.toMeters(), configurationPrefManager);
        int tbt1units = 1;
        if (isMilesUnitSelected) {
            tbt1units = 4;
            if (tbt1dist > 550) {
                //int tbt1distinmiles = (int)tbt1_remDistance.toMiles();
                tbt1dist = (int) (tbt1_remDistance.toMiles() * 10);
                tbt1units = 3;
            }
        } else {
            tbt1units = 1;
            if (tbt1dist > 999) {
                tbt1dist = tbt1dist / 100;
                tbt1units = 2;
            }
        }
        Distance remTotalDistance = routeProgress.getRemainingDistance();
        int totalDist = DistanceExtensionsKt.prettifyMetricNumber(remTotalDistance);
        int totalDistUnits = 1;
        if (isMilesUnitSelected) {
            totalDistUnits = 4;
            totalDist = DistanceExtensionsKt.prettifyImpericalNumber(remTotalDistance);
            if (totalDist > 550) {
                // int totalDistinmiles = (int)remTotalDistance.toMiles();
                totalDist = (int) (remTotalDistance.toMiles() * 10);
                totalDistUnits = 3;
            }
        } else {
            totalDistUnits = 1;
            if (totalDist > 999) {
                totalDist = totalDist / 100;
                totalDistUnits = 2;
            }
        }
        int tbt2units = 1;
        if (isMilesUnitSelected) {
            tbt2units = 4;
            if (tbt2dist > 550) {
                //int tbt2distinmiles = (int)tbt2_remDistance.toMiles();
                tbt2dist = (int) (tbt2_remDistance.toMiles() * 10);
                tbt2units = 3;
            }
        } else {
            tbt2units = 1;

            if (tbt2dist > 999) {
                tbt2units = 2;
            }
        }
        isNight = configurationPrefManager.isNightModeEnabled() ? 1 : 0;
        RELog.d("TAG", "\ttbt_1_id\t" + tbt1Id +
                "\ttbt_1_dist \t  " + tbt1dist +
                "\ttbt_1_units \t " + tbt1units +
                "\ttbt_2_id \t    " + tbt2Id +
                "\ttbt_2_dist \t    " + tbt2dist +
                "\ttbt2units \t    " + tbt2units +
                "\ttbt2Progress \t    " + tbt2Progress +
                "\ttotal_dist \t  " + totalDist +
                "\ttotal_dist_units\t" + totalDistUnits +
                "\tisNight\t" + isNight);
        int hours = 0;
        int min = 0;
        boolean eat = true;
        int eta_format = 0;

        RELog.e("test", "getETA value =" + configurationPrefManager.getETA());
        RELog.e("test", "get nightmode value =" + configurationPrefManager.isNightModeEnabled());
        if (configurationPrefManager != null && configurationPrefManager.getETA()) {
            totalDistUnits = 0;

            Date currentTime = Calendar.getInstance().getTime();
            RELog.e("ETA", "getRemainingDuration :" + routeProgress.getRemainingDuration());
            currentTime.setTime(currentTime.getTime() + routeProgress.getRemainingDuration().toMillis());

            hours = currentTime.getHours();
            min = currentTime.getMinutes();

            RELog.e("ETA", "Hours :" + hours + " Min :" + min);

            boolean is24HourFormat = android.text.format.DateFormat.is24HourFormat(mContext);
            if (is24HourFormat) {
                eta_format = 0;
            } else {
                if (hours == 0) {
                    hours = 12;
                    eta_format = 1;
                } else if (hours < 12) {
                    eta_format = 1;
                } else if (hours == 12) {
                    eta_format = 2;
                } else {
                    hours -= 12;
                    eta_format = 2;
                }
            }

            //min = Math.round(routeProgress.getRemainingDuration().toMillis() % 60);

        }

        if (tbt1Id == -1) {
            return;
        }

        if (!offRouteDetected) {

            int currentTbt1RemainingDistance = 0;
            int v = (previousTbt1RemainingDistance - currentTbt1RemainingDistance);
            Distance distance1 = routeProgress.getCurrentLegProgress().getCurrentStep().getDistance();
            if (v >= decrementLevelTbt1RemainingDistance ||
                    (previousTbt1RemainingDistance == DistanceExtensionsKt.prettifyMetricNumber(distance1))
            ) {

                RELog.d("Maneuver_TAG_OUTPUT", "\ttbt_1_id\t" + tbt1Id +
                        "\ttbt_1_dist \t  " + tbt1dist +
                        "\ttbt_1_units \t " + tbt1units +
                        "\ttbt1Progress \t " + tbt1Progress +
                        "\ttbt_2_id \t    " + tbt2Id +
                        "\ttbt2Progress \t    " + tbt2Progress +
                        "\ttotal_dist \t  " + totalDist +
                        "\ttotal_dist_units\t" + totalDistUnits +
                        "\tMapFragment.isNight\t" + isNight +
                        "\teta_format\t" + eta_format +
                        "\thours\t" + hours +
                        "\tmin\t" + min +
                        "\tisNight\t" + isNight);
                RELog.e("Nav_Info", "tbt1Id :" + tbt1Id);
                getBatterPercentage();
                int mTBTID_1;
                if (isLowBattery()) {
                    mTBTID_1 = TbtType.LOW_BATTERY_NOTOFICATION;
                } else {
                    mTBTID_1 = tbt1Id;
                }
                RELog.e("Nav_Info", "mTBTID_1 :" + mTBTID_1);
//                if (!REUtils.isNetworkAvailable(mContext)) {
//                    mTBTID_1 = TbtType.MOBILE_DATA;
//                }
                byte[] frame = NavProtocol.INSTANCE.getNavMessageFrame(
                        1, 1,
                        mTBTID_1,
                        tbt1dist,
                        tbt1Progress,
                        tbt1units,
                        tbt2Id,
                        tbt2dist,
                        tbt2units,
                        tbt2Progress,
                        totalDist,
                        totalDistUnits,
                        eta_format,
                        hours,
                        min,
                        isNight
                );

                //Changing Route Options will be here,
                if (!isDeepRecordingEnabled) {
                    RecordingOptionState mState = RecordingOptionState.NONE;
                    BreadcrumbTrailRecordingOptions recordingOptions;
                    // Condition to update recording options based on distance to current step

                    LegProgress currentLegProgress = routeProgress.getCurrentLegProgress();
                    if (currentLegProgress.getCurrentStep().getDistance().toMeters() <= 800) {
                        mState = RecordingOptionState.SHORT;
                        recordingOptions = shortRecordingOptions;
                        // Log.e( "Current step check: ", currentLegProgress.getCurrentStep().getDistance().toMeters()+" Short" );
                    } else {
                        mState = RecordingOptionState.LONG;
                        recordingOptions = longRecordingOptions;
                        //   Log.e( "Current step check: ", currentLegProgress.getCurrentStep().getDistance().toMeters()+" Long" );
                    }

                    if (mState != currentRecordingOptionState) {
                        breadcrumbTrailManager.updateRecordingOptions(recordingOptions);
                        this.currentRecordingOptionState = mState;
                    }
                }
                if (REApplication.getInstance().ismIsDeviceConnected() &&
                        REApplication.getInstance().isIsDeviceAuthorised()) {
                    messageByteArrayQueue.offer(frame);
                } else {
                    messageByteArrayQueue.clear();
                }

                previousTbt1RemainingDistance = currentTbt1RemainingDistance;

                RELog.d("decrement : ", " -- " + decrementLevelTbt1RemainingDistance);
                RELog.d("decrement : 1 ", " -- " + v);
            }

        }

    }

    private void updateWayPointReachInTripper() {
        try {
            speak("Waypoint Reached");
            new Handler(Looper.getMainLooper()).post(() -> new CountDownTimer(10000, 1000) {
                public void onTick(long millisUntilFinished) {
                    RELog.e(TAG, "Waypoint Reached");
                    if (null != currentRouteProgress)
                        sendWayPointReachedTBTSignal(currentRouteProgress);
                }

                public void onFinish() {
                    offRouteDetected = false;
                }
            }.start());
        } catch (Exception e) {
            RELog.e(TAG, "Waypoint Reached:" + e.getCause());
        }
    }

    private void sendWayPointReachedTBTSignal(RouteProgress routeProgress) {
        offRouteDetected = true;
        Distance distance = routeProgress.getCurrentLegProgress().getCurrentStep().getDistance();
        int previousTbt1RemainingDistance = DistanceExtensionsKt.prettifyMetricNumber(distance);
        int tbt1Id = TbtType.ARRIVED_AT_DESTINATION;
        routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getVisualInstruction();
        RELog.d(TAG, routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getManeuverType().name()
                + " -> " + tbt1Id + "   " + routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getVisualInstruction());

        int tbt1RoundaboutExitNo = 0;
        if (routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getRoundabout() != null &&
                routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getRoundabout() instanceof Roundabout) {
            tbt1RoundaboutExitNo = routeProgress.getCurrentLegProgress().getCurrentStep().getManeuver().getRoundabout().getWantedExitNumber();
        }
        tbt1Id += tbt1RoundaboutExitNo;
        RELog.d("NAV_INFO", "exit no = " + tbt1RoundaboutExitNo);
        int tbt2Id = 0;
        int tbt2RoundaboutExitNo = 0;
        Step nextstep = routeProgress.getCurrentLegProgress().getNextStep();
        if (nextstep != null && nextstep.getManeuver().getRoundabout() != null &&
                nextstep.getManeuver().getRoundabout() instanceof Roundabout) {
            tbt2RoundaboutExitNo = nextstep.getManeuver().getRoundabout().getWantedExitNumber();
        }
        tbt2Id += tbt2RoundaboutExitNo;
        int tbt1dist = 0;
        int decrementLevelTbt1RemainingDistance = DistanceExtensionsKt.getDecrementValueForDistance(tbt1dist);
        int tbt1Progress = DistanceExtensionsKt.getTbt1ProgressLevel(tbt1dist, tbt1Id);
        if (tbt1Progress >= 5) { // send the tbt2 also
            tbt2Id = -1;
        }
        int tbt2dist = 0;
        int tbt2Progress = DistanceExtensionsKt.getTbt2ProgressLevel(tbt2dist, configurationPrefManager);
        int tbt1units = 1;
        if (tbt1dist > 999) {
            tbt1dist = tbt1dist / 100;
            tbt1units = 2;
        }
        if (isMilesUnitSelected) {
            tbt1units = 4;
        }
        Distance remTotalDistance = routeProgress.getRemainingDistance();
        Distance tbt2_remDistance = routeProgress.getCurrentLegProgress().getCurrentStepProgress().getRemainingDistance();
        int totalDist = DistanceExtensionsKt.prettifyMetricNumber(remTotalDistance);
        int totalDistUnits = 1;
        if (isMilesUnitSelected) {
            totalDistUnits = 4;
            totalDist = DistanceExtensionsKt.prettifyImpericalNumber(remTotalDistance);
            if (totalDist > 550) {
                // int totalDistinmiles = (int)remTotalDistance.toMiles();
                totalDist = (int) (remTotalDistance.toMiles() * 10);
                totalDistUnits = 3;
            }
        } else {
            totalDistUnits = 1;
            if (totalDist > 999) {
                totalDist = totalDist / 100;
                totalDistUnits = 2;
            }
        }
        int tbt2units = 1;
        if (isMilesUnitSelected) {
            tbt2units = 4;
            if (tbt2dist > 550) {
                //int tbt2distinmiles = (int)tbt2_remDistance.toMiles();
                tbt2dist = (int) (tbt2_remDistance.toMiles() * 10);
                tbt2units = 3;
            }
        } else {
            tbt2units = 1;

            if (tbt2dist > 999) {
                tbt2units = 2;
            }
        }
        isNight = configurationPrefManager.isNightModeEnabled() ? 1 : 0;
        RELog.d("TAG", "\ttbt_1_id\t" + tbt1Id +
                "\ttbt_1_dist \t  " + tbt1dist +
                "\ttbt_1_units \t " + tbt1units +
                "\ttbt_2_id \t    " + tbt2Id +
                "\ttbt_2_dist \t    " + tbt2dist +
                "\ttbt2units \t    " + tbt2units +
                "\ttbt2Progress \t    " + tbt2Progress +
                "\ttotal_dist \t  " + totalDist +
                "\ttotal_dist_units\t" + totalDistUnits +
                "\tisNight\t" + isNight);
        int hours = 0;
        int min = 0;
        int eta_format = 0;
        RELog.e("test", "getETA value =" + configurationPrefManager.getETA());
        RELog.e("test", "get nightmode value =" + configurationPrefManager.isNightModeEnabled());
        if (configurationPrefManager != null && configurationPrefManager.getETA()) {
            totalDistUnits = 0;
            Date currentTime = Calendar.getInstance().getTime();
            RELog.e("ETA", "getRemainingDuration :" + routeProgress.getRemainingDuration());
            currentTime.setTime(currentTime.getTime() + routeProgress.getRemainingDuration().toMillis());
            hours = currentTime.getHours();
            min = currentTime.getMinutes();
            RELog.e("ETA", "Hours :" + hours + " Min :" + min);
            boolean is24HourFormat = android.text.format.DateFormat.is24HourFormat(mContext);
            if (is24HourFormat) {
                eta_format = 0;
            } else {
                if (hours == 0) {
                    hours = 12;
                    eta_format = 1;
                } else if (hours < 12) {
                    eta_format = 1;
                } else if (hours == 12) {
                    eta_format = 2;
                } else {
                    hours -= 12;
                    eta_format = 2;
                }
            }
        }
        if (tbt1Id == -1) {
            return;
        }
        int currentTbt1RemainingDistance = 0;
        int v = (previousTbt1RemainingDistance - currentTbt1RemainingDistance);
        Distance distance1 = routeProgress.getCurrentLegProgress().getCurrentStep().getDistance();
        if (v >= decrementLevelTbt1RemainingDistance ||
                (previousTbt1RemainingDistance == DistanceExtensionsKt.prettifyMetricNumber(distance1))
        ) {
            RELog.d("Maneuver_TAG_OUTPUT", "\ttbt_1_id\t" + tbt1Id +
                    "\ttbt_1_dist \t  " + tbt1dist +
                    "\ttbt_1_units \t " + tbt1units +
                    "\ttbt1Progress \t " + tbt1Progress +
                    "\ttbt_2_id \t    " + tbt2Id +
                    "\ttbt2Progress \t    " + tbt2Progress +
                    "\ttotal_dist \t  " + totalDist +
                    "\ttotal_dist_units\t" + totalDistUnits +
                    "\tMapFragment.isNight\t" + isNight +
                    "\teta_format\t" + eta_format +
                    "\thours\t" + hours +
                    "\tmin\t" + min +
                    "\tisNight\t" + isNight);
            RELog.e("Nav_Info", "tbt1Id :" + tbt1Id);
            getBatterPercentage();
            int mTBTID_1;
            if (isLowBattery()) {
                mTBTID_1 = TbtType.LOW_BATTERY_NOTOFICATION;
            } else {
                mTBTID_1 = tbt1Id;
            }
            RELog.e("Nav_Info", "mTBTID_1 :" + mTBTID_1);
            byte[] frame = NavProtocol.INSTANCE.getNavMessageFrame(
                    1, 1,
                    mTBTID_1,
                    tbt1dist,
                    tbt1Progress,
                    tbt1units,
                    tbt2Id,
                    tbt2dist,
                    tbt2units,
                    tbt2Progress,
                    totalDist,
                    totalDistUnits,
                    eta_format,
                    hours,
                    min,
                    isNight
            );
            if (REApplication.getInstance().ismIsDeviceConnected() &&
                    REApplication.getInstance().isIsDeviceAuthorised()) {
                messageByteArrayQueue.offer(frame);
            } else {
                messageByteArrayQueue.clear();
            }
            RELog.d("decrement : ", " -- " + decrementLevelTbt1RemainingDistance);
            RELog.d("decrement : 1 ", " -- " + v);
        }
    }

    private boolean isLowBattery() {
        return mBatteryLevel > 0 && mBatteryLevel <= 10;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int ttsLang = textToSpeech.setLanguage(Locale.US);
            if (ttsLang == TextToSpeech.LANG_MISSING_DATA || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                REUtils.showErrorDialog(mContext, "Language not supported fot TTS");
//                Toast.makeText(mContext, "Language not supported fot TTS", Toast.LENGTH_SHORT).show();
            }
        } else {
            RELog.e(TAG_MAP_FRAGMENT, "TTS initialization failed");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mRENavListener.getSelectedPOIItem() == null) {
            return true;
        }
        mRENavListener.updateRoute();
        if (mMarkerClickListener != null) {
            clearMarkers();
            BitmapDescriptor bitmapDescriptor;
            if (isWayPointAdded) {
                bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            } else {
                bitmapDescriptor = BitmapDescriptorFactory.defaultMarker();
            }
            mMarkerClickListener.onMarkerClick(marker);
            //Update the selected marker and replace old one based on the marker type.
            mMarkerClickListener.updatePOIMarker(marker, bitmapDescriptor);
        }
        return true;
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        RELog.e(TAG_MAP_FRAGMENT, polyline.getId());
        chosenRoute = (Route) polyline.getTag();
        googleMap.clear();
        List<Route> aRouteList = new ArrayList<>();
        for (Route aRoute : mRouteList) {
            if (!aRoute.equals(chosenRoute)) {
                aRouteList.add(aRoute);
            }
        }
        aRouteList.add(chosenRoute);
        mRouteList = new ArrayList<>();
        mRouteList.addAll(aRouteList);
        plotAlternateRoutes();
    }

    @Override
    public void onFasterRouteCalculationFailed(@NotNull RouteCalculationError routeCalculationError, @Nullable String s) {
        RELog.e("TTS", "onFasterRouteCalculationFailed");
    }

    @Override
    public void onRouteTrafficUpdateFailed(@NotNull RouteCalculationError routeCalculationError, @Nullable String s) {
        RELog.e("TTS", "onRouteTrafficUpdateFailed");
    }

    @Override
    public void onMapLongClick(com.google.android.gms.maps.model.LatLng latLng) {
        if (!isRouteReconstruction) {
            mRENavListener.updateRoute();
            if (mMarkerClickListener != null) {
                getPlaceName(latLng, MapEvents.LONG_CLICK);
            }
        }
    }

    /**
     * Plot the marker based on it is way point or destination.
     * From map long tap or POI view items list tap.
     *
     * @param latLng
     */
    public void plotMarker(com.google.android.gms.maps.model.LatLng latLng, boolean isClearMarkers,
                           boolean isDestination, int camZoom, boolean isZoom) {
        if (isClearMarkers)
            clearMarkers();
        addMarker(latLng, isDestination, enableMarkerDrag);
        if (isZoom)
            animateCamera(latLng, camZoom, false);
    }

    public void plotLiveMarker(com.google.android.gms.maps.model.LatLng latLng, int camZoom,
                               com.bosch.softtec.components.core.models.Location location) {
        try {
            mLiveLocation = location;
            animateCamera(latLng, camZoom, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onWayPointAdded() {
        isWayPointAdded = true;
        mRENavListener.unCheckPOICategories();
    }

    public void onWayPointRemoved() {
        isWayPointAdded = false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        if (marker.getTag() != null)
            mStartDragMarkerTag = (double) marker.getTag();
        mRENavListener.updateRoute();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker.getTag() != null)
            mEndDragMarkerTag = (double) marker.getTag();
        if (mMarkerClickListener != null) {
            BitmapDescriptor bitmapDescriptor;
            if (isWayPointAdded) {
                bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            } else {
                bitmapDescriptor = BitmapDescriptorFactory.defaultMarker();
            }
            marker.setIcon(bitmapDescriptor);
            com.google.android.gms.maps.model.LatLng latLng = marker.getPosition();
            getPlaceName(latLng, MapEvents.DRAG);
        }
        mRENavListener.unCheckPOICategories();
    }

    public void setMyLocationEnabled(boolean isEnable) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(isEnable);
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        if (!isRouteReconstruction) {
            mRENavListener.updateRoute();
            if (mMarkerClickListener != null) {
                com.google.android.gms.maps.model.LatLng latLng = pointOfInterest.latLng;
                mMarkerClickListener.onMapPOIClick(latLng, pointOfInterest.name);
                mRENavListener.plotMarker();
            }
        }
    }

    public interface OnTouchListener {
        void onTouch();
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

    public interface OnRENavigationListener {
        void stopNavigationCalled(boolean mIsRecording);

        void onRENavigationEnded(boolean isRecord);

        void updateRoute();

        void updateOnMilestoneReached(WayPointsData upcomingWayPoint);

        int getMapBottomPadding();

        String getSelectedPOIItem();

        void plotMarker();

        void unCheckPOICategories();

        void changePipWindowText();
    }


    public interface MarkerClickListener {
        void onMarkerClick(Marker marker);

        void onMapLongClick(com.google.android.gms.maps.model.LatLng latLng, String aPlaceName);

        void updatePOIMarker(Marker aMarker, BitmapDescriptor bitmapDescriptor);

        void onMapMarkerDrag(com.google.android.gms.maps.model.LatLng latLng, String aPlaceName);

        void onMapPOIClick(com.google.android.gms.maps.model.LatLng latLng, String aPlaceName);
    }

    /**
     * Get the country code from the location
     *
     * @param lat :double
     * @param lng :double
     * @return : String
     */
   /* public String getCountryCode(double lat, double lng) {
        String aCountryCode = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        Address aAddress;
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (null != addresses && addresses.size() > 0) {
                aAddress = addresses.get(0);
                if (null != aAddress)
                    aCountryCode = aAddress.getCountryCode();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            RELog.e(e);
        }
        return aCountryCode;
    }*/
    public String getCountryCode(double lat, double lng) throws IOException {
        String aCountryCode = null;
        List<Address> addresses = null;
        Geocoder geocoder = null;
        if (getActivity() != null) {
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            Address aAddress;

            if (Build.VERSION.SDK_INT >= 33) {
                geocoder.getFromLocation(lat,
                        lng,
                        1,
                        new Geocoder.GeocodeListener() {
                            @Override
                            public void onGeocode(@NonNull List<Address> addresses) {
                                String anewCountryCode = addresses.get(0).getCountryCode();
                                countryCode(anewCountryCode);
                            }
                        });
            } else {
                addresses = geocoder.getFromLocation(lat, lng, 1);
                if (null != addresses && addresses.size() > 0) {
                    aAddress = addresses.get(0);
                    if (null != aAddress)
                        aCountryCode = aAddress.getCountryCode();
                    return aCountryCode;
                }
            }
        }
            return aCountryCode;
        }


        private void countryCode (String aCountryCode){
            countryCodeTarasmu = aCountryCode;
        }


        /**
         * Get the mode of drive based on the country reserved
         *
         * @param aCountryCode : String
         * @return : RouteCalculationMode
         */
        private RouteCalculationMode getDriveMode (String aCountryCode){
            if (aCountryCode == null)
                return RouteCalculationMode.CAR;
            if (null != mTwoWheelerCountriesList && mTwoWheelerCountriesList.size() > 0) {
                if (mTwoWheelerCountriesList.contains(aCountryCode)) {
                    return RouteCalculationMode.MOTORBIKE;
                } else {
                    return RouteCalculationMode.CAR;
                }
            } else {
                return RouteCalculationMode.CAR;
            }
        }

        /**
         * Called when the location has changed.
         *
         * <p> There are no restrictions on the use of the supplied Location object.
         *
         * @param location The new location, as a Location object.
         */
        @Override
        public void onLocationChanged (Location location){
            // try {
            mCurrentLocation = location;
            RELog.e("NavigationMap", "onLocationChanged :" + location.getLatitude());
            if (locationManager != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                mHandler.removeCallbacks(mRunnable);
                locationCount = 0;//Reset the location counter.
                //stops the current location updates.
                locationManager.removeUpdates(this);
                locationManager = null;
                REUserModelStore.getInstance().setLatitude(location.getLatitude());
                REUserModelStore.getInstance().setLongitude(location.getLongitude());
                if (googleMap != null) {
                    com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(location.getLatitude(), location.getLongitude());
                    animateCamera(latLng, 14, false);
                }
                if (mDestLat == 0 && mDestLon == 0 && location != null && mCurrentLocationListener != null) {
                    // Updating current location edit text.
                    mCurrentLocationListener.updateCurrentLocation(new com.google.android.gms.maps.model.
                            LatLng(location.getLatitude(), location.getLongitude()));
                }
                if (location.getLatitude() != 0 && location.getLongitude() != 0)
                //Take the drive mode of current country from the location...

                {
                    try {
                        String countryCode = getCountryCode(location.getLatitude(), location.getLongitude());
                        if (countryCode != null) {
                            myRouteCalculationMode = getDriveMode(countryCode);
                        } else {
                            myRouteCalculationMode = getDriveMode(countryCodeTarasmu);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                hideLoading();
            }

        }

        @Override
        public void onStatusChanged (String provider,int status, Bundle extras){
        }

        @Override
        public void onProviderEnabled (String provider){
        }

        @Override
        public void onProviderDisabled (String provider){
        }

        private void getPlaceName (com.google.android.gms.maps.model.LatLng latLng, MapEvents
        mapEvents){
            String aLocation = latLng.latitude + "," + latLng.longitude;
            try {
                String aPlaceName = null;
                Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                Address aAddress;
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (null != addresses && addresses.size() > 0) {
                        aAddress = addresses.get(0);
                        if (null != aAddress)
                            aPlaceName = aAddress.getFeatureName();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mapEvents.equals(MapEvents.LONG_CLICK)) {
                    mMarkerClickListener.onMapLongClick(latLng, aPlaceName);
                } else if (mapEvents.equals(MapEvents.DRAG)) {
                    mMarkerClickListener.onMapMarkerDrag(latLng, aPlaceName);
                }
                mRENavListener.plotMarker();

          /*  new GoogleMapAPIInteractor().getPlaceName(aLocation, new NavigationListeners.OnPlaceFinishedListener() {
                @Override
                public void onPlaceSuccess(String formattedAddress) {

                    if (mapEvents.equals(MapEvents.LONG_CLICK)) {
                        mMarkerClickListener.onMapLongClick(latLng, formattedAddress);
                    } else if (mapEvents.equals(MapEvents.DRAG)) {
                        mMarkerClickListener.onMapMarkerDrag(latLng, formattedAddress);
                    }
                    mRENavListener.plotMarker();
                }

                @Override
                public void onPlaceFailure(String error) {
                    String aPlaceName = null;
                    Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                    Address aAddress;
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (null != addresses && addresses.size() > 0) {
                            aAddress = addresses.get(0);
                            if (null != aAddress)
                                aPlaceName = aAddress.getFeatureName();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mapEvents.equals(MapEvents.LONG_CLICK)) {
                        mMarkerClickListener.onMapLongClick(latLng, aPlaceName);
                    } else if (mapEvents.equals(MapEvents.DRAG)) {
                        mMarkerClickListener.onMapMarkerDrag(latLng, aPlaceName);
                    }
                    mRENavListener.plotMarker();
                }
            });*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void adjustMapHeight ( int value){
            //mMarkerOffset = (~(value - 1));
            googleMap.setPadding(2, value, 0, value);
        }

        public void animateCamera (com.google.android.gms.maps.model.LatLng latLng,int reqZoom,
        boolean isLivePlot){
            // Save current zoom
            float originalZoom = googleMap.getCameraPosition().zoom;
            if (isLivePlot) {
                if (!isUserGestureFound) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, reqZoom,
                            googleMap.getCameraPosition().tilt, mLiveLocation.getBearing()));
                    googleMap.moveCamera(cameraUpdate);
                }
                mLiveBikeMarker.setPosition(latLng);
            } else {
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(originalZoom));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, reqZoom);
                googleMap.animateCamera(cameraUpdate);
            }
        }

        public void onCurrentLocationClick () {
            animateCamera(getCurrentLocation(), 17, false);
        }

        public void resetUserGesture () {
            isUserGestureFound = false;
        }

        public boolean isWayPointAdded () {
            return isWayPointAdded;
        }

        public double getStartDragMarkerTag () {
            return mStartDragMarkerTag;
        }

        public double getEndDragMarkerTag () {
            return mEndDragMarkerTag;
        }

        private com.google.android.gms.maps.model.LatLng getCurrentLocation () {
            try {
                if (currentLocation != null)
                    return new com.google.android.gms.maps.model.LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                else if (mLiveBikeMarker != null) {
                    com.google.android.gms.maps.model.LatLng latLng = mLiveBikeMarker.getPosition();
                    return new com.google.android.gms.maps.model.LatLng(latLng.latitude, latLng.longitude);
                } else {
                    Location aLocation = googleMap.getMyLocation();
                    return new com.google.android.gms.maps.model.LatLng(aLocation.getLatitude(), aLocation.getLongitude());
                }
            } catch (Exception e) {
                RELog.e(TAG + ":" + e);
                if (mWayPointDataList != null && mWayPointDataList.size() > 0) {
                    WayPointsData wayPointsData = mWayPointDataList.get(0);
                    return new com.google.android.gms.maps.model.LatLng(wayPointsData.getLatitude(), wayPointsData.getLongitude());
                } else {
                    return new com.google.android.gms.maps.model.LatLng(0.0, 0.0);
                }
            }
        }

        public boolean isMapRedrawing () {
            return offRouteDetected || isCameraMoving;
        }

        @Override
        public void onIncomingCall () {
            //Toast.makeText(getContext(), "RE:Incoming Call State", Toast.LENGTH_SHORT).show();
            if (configurationPrefManager != null && configurationPrefManager.getEnableCallNotification()) {
                if (isCallNotificationAvailable) {
                    notifyCallToTFT(CALL_INCOMING);
                }
            }
        }

        @Override
        public void onCallAccepted () {
            //Toast.makeText(getContext(), "RE:Answered state.", Toast.LENGTH_SHORT).show();
            notifyCallToTFT(CALL_ACCEPTED);
        }

        @Override
        public void onCallRejected () {
            //Toast.makeText(getContext(), "RE:Call Rejected State", Toast.LENGTH_SHORT).show();
            notifyCallToTFT(CALL_REJECTED);
        }

        @Override
        public void onCallEnded () {
            //Toast.makeText(getContext(), "RE:Call Ended State", Toast.LENGTH_SHORT).show();
            notifyCallToTFT(CALL_ENDED);
        }

        @Override
        public void onCallMissed () {
            //Toast.makeText(getContext(), "RE:Call Missed State", Toast.LENGTH_SHORT).show();
            notifyCallToTFT(CALL_MISSED);
        }

        /**
         * register phone call state change listener.
         */
        //in TBT 2.0 release we are commenting the call notification feature
        public void registerTFTCallListener () {
            if (REApplication.getInstance().ismIsDeviceConnected() &&
                    REApplication.getInstance().isIsDeviceAuthorised()) {
                REApplication.getInstance().setPhoneStateListener(this);
            }
        }

        /**
         * Unregister phone call state change listener.
         */
        //in TBT 2.0 release we are commenting the call notification feature
        public void unregisterTFTCallListener () {
            REApplication.getInstance().setPhoneStateListener(null);
            clearCallToTFT();
        }

        /**
         * Update the call status to TFT device display
         */
        @SuppressLint({"RELogArgCount", "BinaryOperationInRELog"})
        private void notifyCallToTFT ( byte byteValue){
            try {
                //Stop TBT for a moment to update Call state to TFT.
                mCanSendTBT = false;
                mCurrentCallStatus = byteValue;
                if (REApplication.getInstance().ismIsDeviceConnected() &&
                        REApplication.getInstance().isIsDeviceAuthorised()) {
                    byte[] byteArray = new byte[20];
                    byteArray[0] = 0x40;
                    byteArray[1] = byteValue;
                    byte[] temp = new byte[18];
                    for (int i = 0; i <= 17; i++) {
                        temp[i] = byteArray[i];
                        RELog.e(TAG, "Call ByteArray:" + byteArray[i]);
                    }
                    byte[] crc = CRCCalculator.calculateCRC(temp);
                    byteArray[18] = crc[0];
                    byteArray[19] = crc[1];
                    messageByteArrayQueue.offer(byteArray);
                }
                //After notify the call status to TFT, update the mCanSendTBT boolean.
                if (mCurrentCallStatus == CALL_ACCEPTED || mCurrentCallStatus == CALL_REJECTED
                        || mCurrentCallStatus == CALL_ENDED || mCurrentCallStatus == CALL_MISSED) {
                    mCanSendTBT = true;
                }
                RELog.e(TAG, "notifyCallToTFT:done");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Clear the call notifications in TFT display
         */
        private void clearCallToTFT () {
            messageByteArrayQueue.clear();
        }

        /**
         * Get teh current OS version of TFT display
         */
        private String getCurrentTripperSerialNo () {
            String value = "";
            if (sharedpreferences != null) {
                value = sharedpreferences.getString("currentTripperUniqueId", "");
            }
            return value;
        }

        private boolean compareVersions (String aFireStoreVer, String aDeviceVer){
            double aFireStoreVal = Double.parseDouble(aFireStoreVer);
            double aDevVal = Double.parseDouble(aDeviceVer);
            return Double.compare(aFireStoreVal, aDevVal) > 0 || Double.compare(aFireStoreVal, aDevVal) == 0;
        }

        public void updateCallNotificationAvailability (String connectedTripperId){
            String callNotificationBaseVesrion = "12.10";
            isCallNotificationAvailable = false;
            List<DeviceInfo> items = BLEDeviceManager.getMyTBTList(mContext);
            for (int i = 0; i < items.size(); i++) {
                DeviceInfo mDeviceInfo = items.get(i);
                if (mDeviceInfo.getAddress().equalsIgnoreCase(connectedTripperId)) {
                    if (mDeviceInfo.getOSVersion() != null) {
                        String version = mDeviceInfo.getOSVersion();
                        lastConnectedTripperSerialNo = mDeviceInfo.getSerialNumber();
                        storeTripperUniqueId(lastConnectedTripperSerialNo);

                        //String version = "07.06";
                        if (compareVersions(version, callNotificationBaseVesrion)) {
                            isCallNotificationAvailable = true;
                        }
                    }
                }
            }
        }

        public void createNavigationSummaryApiDetails (String start_time, String end_time){
            startpoint = new SaveTripSummaryStartPoint(mWayPointDataList.get(0).getLatitude(), mWayPointDataList.get(0).getLongitude());
            endpoint = new SaveTripSummaryEndPoint(mWayPointDataList.get(mWayPointDataList.size() - 1).getLatitude(), mWayPointDataList.get(mWayPointDataList.size() - 1).getLongitude());
            tripStartTime = start_time;
            tripEndTime = end_time;
        }

        public void saveSummaryDetails (BreadcrumbTrail bct){
        /*String dist = String.valueOf(intent.getDoubleExtra(BCT_DISTANCE,0.0));
        String dur = String.valueOf(intent.getDoubleExtra(BCT_DURATION,0.0));
        String speed = String.valueOf(intent.getDoubleExtra(BCT_SPEED,0.0));*/
            String dist = (bct.getRideSummary().getTotalDistance() != null) ? String.valueOf(bct.getRideSummary().getTotalDistance().toMeters()) : "0";
            String dur = bct.getRideSummary().getTotalDuration() != null ? String.valueOf(bct.getRideSummary().getTotalDuration().toMinutes()) : "0";
            String speed = bct.getRideSummary().getAverageSpeed() != null ? String.valueOf(bct.getRideSummary().getAverageSpeed().toKilometersPerHour()) : "0";
            try {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Tripper");
                params.putString("eventAction", "Stop Record");
                params.putString("eventLabel", mWayPointDataList.get(mWayPointDataList.size() - 1).getPlaceName());
                params.putString("Journey_kms", dist);
                params.putString("Journey_time", dur);
                REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (lastConnectedTripperSerialNo != null && !lastConnectedTripperSerialNo.isEmpty()) {
                tripSummaryTripperSerialNo = lastConnectedTripperSerialNo;
            } else {
                tripSummaryTripperSerialNo = getCurrentTripperSerialNo();
            }

            SaveTripSummaryDetailsApiRequest saveTripSummaryDetailsApiRequest = new SaveTripSummaryDetailsApiRequest(REUtils.getGuid(), tripSummaryTripperSerialNo, startpoint, endpoint, "Tripper", "Android", speed, dur, dist, tripStartTime, tripEndTime);
            Log.e("Valll1", "" + saveTripSummaryDetailsApiRequest.toString());
            RENavigationInteractor reNavigationInteractor = new RENavigationInteractor();
            reNavigationInteractor.saveNavigationSummaryDetails(saveTripSummaryDetailsApiRequest);
            Log.e("Valll", "1 " + dist + " 2 " + dur + " 3 " + speed);
        }

        private void storeCurrentSoftwareVersion (String cVersion){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("currentSoftwareVersion", cVersion);
            editor.apply();
        }

        private void storeTripperUniqueId (String uniqueId){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("currentTripperUniqueId", uniqueId);
            editor.apply();
        }
    }
