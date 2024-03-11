package com.royalenfield.reprime.ui.home.homescreen.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.POWER_SERVICE;
import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.SensorManager.SENSOR_DELAY_GAME;
import static android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_HIGH;
import static android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_LOW;
import static android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM;
import static android.hardware.SensorManager.SENSOR_STATUS_UNRELIABLE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.royalenfield.reprime.base.REBaseActivity.manualDisconnectBase;
import static com.royalenfield.reprime.utils.REConstants.HUAWEI;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REConstants.KEY_TRIPPER_GTM;
import static com.royalenfield.reprime.utils.REConstants.MOTO;
import static com.royalenfield.reprime.utils.REConstants.NAVIGATION_TYPE_RECORD_PAUSE;
import static com.royalenfield.reprime.utils.REConstants.NAVIGATION_TYPE_RECORD_RESUME;
import static com.royalenfield.reprime.utils.REConstants.NAVIGATION_TYPE_RECORD_STOP;
import static com.royalenfield.reprime.utils.REConstants.ONEPLUS;
import static com.royalenfield.reprime.utils.REConstants.OPPO;
import static com.royalenfield.reprime.utils.REConstants.REALME;
import static com.royalenfield.reprime.utils.REConstants.RIDE_WAYPOINT_TYPE;
import static com.royalenfield.reprime.utils.REConstants.SAMSUNG;
import static com.royalenfield.reprime.utils.REConstants.SENSOR_ACCURACY_HIGH;
import static com.royalenfield.reprime.utils.REConstants.SENSOR_ACCURACY_LOW;
import static com.royalenfield.reprime.utils.REConstants.SENSOR_ACCURACY_MEDIUM;
import static com.royalenfield.reprime.utils.REConstants.SENSOR_UNRELIABLE;
import static com.royalenfield.reprime.utils.REConstants.SHARE_TYPE_BCT_ROUTE;
import static com.royalenfield.reprime.utils.REConstants.SHARE_TYPE_TBT_ROUTE;
import static com.royalenfield.reprime.utils.REConstants.VIVO;
import static com.royalenfield.reprime.utils.REConstants.WAYPOINT_NAVIGATION;
import static com.royalenfield.reprime.utils.REConstants.WAY_POINTS_LIST;
import static com.royalenfield.reprime.utils.REConstants.XIAOMI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bosch.softtec.components.core.conversion.LengthUnit;
import com.bosch.softtec.components.core.models.Distance;
import com.bosch.softtec.components.core.models.breadcrumb.BreadcrumbTrail;
import com.bosch.softtec.components.midgard.core.directions.RouteCalculationCallback;
import com.bosch.softtec.components.midgard.core.directions.models.Route;
import com.bosch.softtec.components.midgard.core.directions.models.RouteCalculationError;
import com.bosch.softtec.components.midgard.core.search.SearchCallback;
import com.bosch.softtec.components.midgard.core.search.SearchProvider;
import com.bosch.softtec.components.midgard.core.search.models.PoiCategory;
import com.bosch.softtec.components.midgard.core.search.models.Response;
import com.bosch.softtec.components.midgard.core.search.models.SearchError;
import com.bosch.softtec.components.midgard.core.search.models.SearchOptions;
import com.bosch.softtec.components.midgard.core.search.models.results.PoiSearchResult;
import com.bosch.softtec.components.midgard.core.search.models.results.SearchResult;
import com.bosch.softtec.components.nephele.extensions.ConversionExtensionsKt;
import com.bosch.softtec.components.theseus.BreadcrumbTrailManager;
import com.bosch.softtec.components.theseus.persistance.PausedBreadcrumbTrailCallback;
import com.bosch.softtec.micro.tenzing.client.model.Trip;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.royalenfield.bluetooth.BleSearchActivity;
import com.royalenfield.bluetooth.ble.BLEModel;
import com.royalenfield.bluetooth.ble.BleManagerProvider;
import com.royalenfield.bluetooth.ble.DeviceInfo;
import com.royalenfield.bluetooth.ble.IBleManager;
import com.royalenfield.bluetooth.ble.OnConnectionChange;
import com.royalenfield.bluetooth.otap.RELogger;
import com.royalenfield.bluetooth.otap.interactor.OtapInteractor;
import com.royalenfield.bluetooth.otap.listener.OnOTAPCallback;
import com.royalenfield.bluetooth.utils.BLEConstants;
import com.royalenfield.bluetooth.utils.BLEDeviceManager;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.googleSearch.SearchProviderManager;
import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.configuration.ConfigurationPrefManager;
import com.royalenfield.reprime.models.request.web.otap.OtapGetDeviceinfoApiRequest;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.request.web.tbtauthentication.TbtAuthRequest;
import com.royalenfield.reprime.models.response.web.otap.Data;
import com.royalenfield.reprime.models.response.web.otap.DeviceInfoApiItems;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.navigation.activity.BCTSaveInfoActivity;
import com.royalenfield.reprime.ui.home.navigation.activity.CrashHandler;
import com.royalenfield.reprime.ui.home.navigation.activity.TripListActivity;
import com.royalenfield.reprime.ui.home.navigation.adapter.POIAdapter;
import com.royalenfield.reprime.ui.home.navigation.asynctask.FetchTripAsyncTask;
import com.royalenfield.reprime.ui.home.navigation.asynctask.NavigationAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.navigation.fragment.NavigationAddWayPointFragment;
import com.royalenfield.reprime.ui.home.navigation.fragment.NavigationFilterScreen;
import com.royalenfield.reprime.ui.home.navigation.fragment.NavigationPOIFragment;
import com.royalenfield.reprime.ui.home.navigation.fragment.NavigationStartFragment;
import com.royalenfield.reprime.ui.home.navigation.fragment.OTAPUpdateFragment;
import com.royalenfield.reprime.ui.home.navigation.interactor.RENavigationInteractor;
import com.royalenfield.reprime.ui.home.navigation.listener.NavigationRouteListener;
import com.royalenfield.reprime.ui.home.navigation.listener.OTAPGetDeviceListApiListner;
import com.royalenfield.reprime.ui.home.navigation.listener.TBTAutenticationListner;
import com.royalenfield.reprime.ui.home.navigation.model.POIModel;
import com.royalenfield.reprime.ui.home.navigation.model.RecentRoute;
import com.royalenfield.reprime.ui.home.navigation.model.RecentRouteLocationManager;
import com.royalenfield.reprime.ui.home.navigation.presenter.ReNavigationPresenter;
import com.royalenfield.reprime.ui.home.navigation.utils.BCTUtils;
import com.royalenfield.reprime.ui.home.rides.activity.RideTermsAndConditions;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.MapFragment;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.home.service.diy.activity.DoItYourSelfActivity;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;

public class RENavigationFragment extends REBaseFragment implements RidesListeners.CurrentLocationListener,
        RENavigationMapFragment.OnTouchListener, RENavigationMapFragment.OnRENavigationListener,
        NavigationAddWayPointFragment.IPlanRideWayPointSearchListener,
        NavigationFilterScreen.ItemClickListener, View.OnClickListener, NavigationRouteListener,
        POIAdapter.POIClickListener, NavigationPOIFragment.ItemClickListener, NavigationAsyncTaskListeners.FetchTripListener,
        CompoundButton.OnCheckedChangeListener, RENavigationMapFragment.MarkerClickListener,
        OnOTAPCallback, OTAPUpdateFragment.ItemClickListener, OnConnectionChange, TBTAutenticationListner, SensorEventListener, OTAPGetDeviceListApiListner {

    private static final String TAG_NAVIGATION_FRAGMENT = RENavigationFragment.class.getName();
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUESTCODE_NAVIGATION = 1;
    private static final int REQUESTCODE_UPLOAD = 2;
    private static final int REQUESTCODE_TRIP = 3;
    private static final int REQUESTCODE_SHARE = 4;
    private static final int REQUESTCODE_BLE_LIB_STATUS = 5;
    private static final int REQUESTCODE_BCT_SHARE_ROUTE = 311;
    private static final int REQUEST_CODE = 201;

    private ArrayList<WayPointsData> mWayPointList = new ArrayList<>();
    private double mSourceLat, mSourceLon;
    private RENavigationMapFragment mMapFragment;
    private boolean mIsStopNavigation = false;
    private FragmentFrameHolder mWaypointFragmentFrame;
    private NestedScrollView mScrollView;
    private FragmentFrameHolder mMapFragmentFrame;
    private RecyclerView poiRecyclerView;
    private Button butNavStatusIcon, mNavCompassBTN;
    private ImageView imageViewHelmetPip;
    private ConstraintLayout navConstraint;
    private TextView tvGetNavDuration, tvGetNavDistance, tvToolTip, tvPipInfoText, tvPipDeviceDiconnectedInfo;
    private NavigationAddWayPointFragment addWayPointFragment;
    private List<POIModel> poiModelList = new ArrayList<>();
    private POIAdapter poiAdapter;
    private String strSelectedPOIItem;
    private View bottomSheet;
    private Button btnStartNavigation;
    private Button btnRecordStart;
    private Button btnRecordStop;
    private Button btnNavigationSave;
    private Button btnNavigationShare;
    private ToggleButton btnRecordPause;
    private String mNavType;
    private boolean mISBLEConnected = false;
    private final IBleManager manager = BleManagerProvider.INSTANCE.getInstance();
    private BluetoothAdapter mBluetoothAdapter;
    private Map<String, Object> mOtapMapData;
    private static final int MY_IGNORE_OPTIMIZATION_PERMISSION = 12;
    private static final int MY_IGNORE_OPTIMIZATION_REQUEST = 13;
    private static final int REQUEST_PERMISSION_SETTING = 14;
    private static final int REQUEST_GOTO_PERMISSION_SETTING = 181;
    private static final Handler mHandler = new Handler();
    private boolean mIsRecordEnded = false;
    private boolean mISBackground = false;
    private boolean isScanInitiatedConnection = false;
    private List<DeviceInfo> mStoredDevicesList;
    private boolean isRecordRouteDisabled = false;
    private String deeplink = "";
    private ConfigurationPrefManager configurationPrefManager;
    private boolean isBLELibActive = false;
    private String guid;
    private boolean isNavHelmetCLicked = false;
    private FragmentActivity mContext;
    private int mAPIFailureCount = 1;
    private SearchProvider searchProvider;
    private SearchOptions searchOptions;
    private com.bosch.softtec.components.core.models.LatLng mCurrentlatLng;
    private PoiCategory mPOICategory;
    private Long longDurationGlobal;
    private Double doubleDistanceInMeters;
    boolean isMilesUnitSelected = false;

    private BitmapDescriptor categoryMarker;
    private List<SearchResult> poiSearchResults;
    private Button mStopNavigationButton;
    private TextView mNavigationTXT, mNavigationReRouteTXT;
    private Button myReloadBTN, mCurrentLocBTN, mReCenterBTN, mSpeakMuteBTN;
    private boolean isMuteButtonClicked;
    private TextView mAccuracyTXT;
    // Sensors & SensorManager
    private Sensor magnetometer;
    private SensorManager mSensorManager;
    private final String TAG = RENavigationFragment.class.getSimpleName();
    private String mSensorAccuracy = "None";
    private int mAccuracyColor = R.color.red;
    private AlertDialog mCompassDialog;
    private ConstraintLayout navigation_PipLayout;
    OtapInteractor mOtapInteractor = new OtapInteractor();
    private RELogger reLogger;
    String tripStartTime = "";
    String tripEndTime = "";
    String deeplink_data;
    private RadioButton radioButtonDeepRecord, radioButtonIntelligentRecord;
    Double kLimitForOnlyIntelligentRecording, kLimitinMiles;
    private int permissionDenialCount = 0;
    private String journeyTimeb = " ";
    private String journeyKmsb = " ";
    private String bDestinationStr = " ";
    private String recordType = "Intelligent";
    ;


    public enum Controls {
        COLLAPSE, EXPAND
    }

    public enum RecordingOptionSelectionState {
        ONLY_INTELLIGENT_RECORDING,
        DEEP_RECORDING
    }

    public static RENavigationFragment newInstance() {
        return new RENavigationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        reLogger = new RELogger(mContext);
        reLogger.appendLog("Navigation home screen onCreate called ");
        try {
            if (REPreference.getInstance().getString(mContext, "RandomUUID").isEmpty()) {
                String randomUUID = UUID.randomUUID().toString();
                // Log.e("UUID", " " + randomUUID);
                REPreference.getInstance().putString(mContext, "RandomUUID", randomUUID);
            }
        } catch (PreferenceException e) {
            e.printStackTrace();
        }
        REApplication.getInstance().setLoggerFileCreated(true);
        guid = REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId();
        List<DeviceInfo> connectedDeviceList = REApplication.getInstance().getmConnectedDeviceInfo();
        boolean mIsBLEConnected = connectedDeviceList != null && connectedDeviceList.size() > 0;
        if (!mIsBLEConnected) {
            BLEDeviceManager.setDeviceConnectionsToFalse(BLEDeviceManager.getMyTBTList(mContext),
                    mContext);
        }
        FirestoreManager.getInstance().getOTAPInfo(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.navigation_fragment, container, false);
        mWayPointList.add(0, new WayPointsData(0, 0, ""));
        initViews(view);
        if (REApplication.getInstance().getUserLoginDetails() != null) {
            guid = REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId();
        }
        if (guid != null) {
            getTBTAuthenticationKey();
            //Get the list of tripper from cloud
            String token = "";
            try {
                token = REPreference.getInstance().getString(mContext, "RandomUUID");
            } catch (PreferenceException e) {
                e.printStackTrace();
            }
            OtapGetDeviceinfoApiRequest otapGetDeviceinfoApiRequest = new OtapGetDeviceinfoApiRequest(guid, token);
            // OtapGetDeviceinfoApiRequest otapGetDeviceinfoApiRequest = new OtapGetDeviceinfoApiRequest("saminusertest1","f2KZThO7n6M:APA91bFNO-kUVmEjnYI2aapyvSleWazM98RxiFXmL-BEsDCawN81Kjq_KhvZuHzaTbowecUGZ7_G_Uf_rnzrXH6HUygBwSewDpWYH4InFEYK0xmV-u9I0p-rLtn28dn15lmqAdx7zWIA");
            mOtapInteractor.getDeviceListFromApi(otapGetDeviceinfoApiRequest, this);

        }
        mStoredDevicesList = BLEDeviceManager.getMyTBTList(mContext);
        getSharedNavigationId();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mconnectionBroadcastReceiver,
                new IntentFilter("updateconnection"));
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mAuthBroadcastReceiver,
                new IntentFilter("pinAuth"));
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mDynamicCallNotfnBroadcastReceiver,
                new IntentFilter("DynamicCallNotifyToStart"));
        try {
            //All the permissions are getting here like Location,Phone,Background location(for Android 10)
            ((REBaseActivity) mContext).checkAndRequestMyPermissions(true);
        } catch (Exception e) {
            RELog.e(e);
        }
        // requestLocationPermission();
        checkBatteryOptimization();
        registerConnectionReceiver();
        // Get a reference to the SensorManager
        mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        // Get a reference to the magnetometer
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            final Context context = view.getContext();
            boolean hasFineLocationPermission =
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean hasCoarseLocationPermission =
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if (hasFineLocationPermission || hasCoarseLocationPermission) {
                CurrentLocationProvider locationProvider = new CurrentLocationProvider(mContext);
                locationProvider.requestCurrentLocation(locationResult -> {
                    final REApplication reApplication = REApplication.getInstance();
                    final CrashHandler crashHandler = reApplication.getCrashHandler();
                    final boolean hasCrashGpx = crashHandler.hasCrashed();
                    if (hasCrashGpx) {
                        showDialog(view.getContext(), crashHandler);
                    }
                });
            } else {
                RELog.d(TAG, "Neither ACCESS_FINE_LOCATION nor ACCESS_COARSE_LOCATION is granted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(Context context, CrashHandler crashHandler) {
        final View dialogView = View.inflate(context, R.layout.dialog_route_recording_detected_alert, null);
        TextView tvOk = dialogView.findViewById(R.id.textView_ok);
        RadioGroup rg_record = dialogView.findViewById(R.id.groupradio_record);
        Dialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        tvOk.setOnClickListener(v -> {
            int checkedRadioButtonId = rg_record.getCheckedRadioButtonId();

            if (checkedRadioButtonId == R.id.radio_continue) {
                Log.d("TAG", "Continue selected");
                handleContinue(crashHandler);
            } else if (checkedRadioButtonId == R.id.radio_save) {
                Log.d("TAG", "Save selected");
                handleSave();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void handleContinue(CrashHandler crashHandler) {
        CrashHandler.Data data = crashHandler.loadData();
        if (data == null) return;

        Context context = requireContext();
        boolean hasFineLocationPermission = ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean hasCoarseLocationPermission = ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (hasFineLocationPermission || hasCoarseLocationPermission) {
            CurrentLocationProvider currentLocationProvider = new CurrentLocationProvider(context);
            currentLocationProvider.requestCurrentLocation(location -> {

                ArrayList<WayPointsData> wayPointsData = new ArrayList<>();
                wayPointsData.add(new WayPointsData(location.getLatitude(), location.getLongitude(), "Origin"));
                wayPointsData.addAll(data.getWayPointsData());

                this.addWayPointFragment.setWayPointsList(wayPointsData);

                mMapFragment.continueNavigation(data, location, wayPointsData, new RouteCalculationCallback() {
                    @Override
                    public void onRouteCalculationFinished(@NonNull List<? extends Route> list) {
                        Log.d(TAG, "handleContinue/onRouteCalculationFinished: " + list);
                        FragmentActivity activity = getActivity();
                        if (activity == null) return;

                        if (list.isEmpty()) {
                            REUtils.showErrorDialog(mContext, "Unable to find the route.");
                        } else {
                            mMapFragment.chosenRoute = list.get(0);
                            activity.runOnUiThread(() -> {
                                startRecord();
                                mMapFragment.startNavigation(true);
                                mMapFragment.drawMarkers(data.getDestination(), data.getRemainingWayPoints());
                            });
                            crashHandler.removeData();
                        }
                    }

                    @Override
                    public void onRouteCalculationError(@NonNull RouteCalculationError routeCalculationError, @Nullable String s) {
                        Log.d(TAG, "handleContinue/onRouteCalculationError: " + s);
                        if (mContext == null) return;

                        if (routeCalculationError.name().equals(REConstants.TBT_API_UNAUTHORIZED)) {
                            mContext.runOnUiThread(() -> {
                                String message = mContext.getString(R.string.error_nav_unauthorized);
                                REUtils.showErrorDialog(mContext, message);
                            });
                        } else {
                            mContext.runOnUiThread(() -> {
                                String message = mContext.getString(R.string.error_tbt_api);
                                REUtils.showErrorDialog(mContext, message);
                            });
                        }
                    }
                });
            });
        }
    }

    private void handleSave() {
        REApplication reApplication = REApplication.getInstance();
        BreadcrumbTrailManager breadcrumbTrailManager = reApplication.getBreadcrumbTrailManager();
        breadcrumbTrailManager.fetchPausedBreadcrumbTrailAsync(new PausedBreadcrumbTrailCallback() {
            @Override
            public void onSuccessfullyFetched(@NonNull BreadcrumbTrail breadcrumbTrail) {
                RENavigationMapFragment.setBreadcrumbTrail(breadcrumbTrail);
                Intent bctIntent = new Intent(mContext, BCTSaveInfoActivity.class);
                bctIntent.putExtra("action", "Save");
                startActivityForResult(bctIntent, REQUESTCODE_UPLOAD);
            }

            @Override
            public void onNothingAvailable() {

            }
        });
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Tripper Navigation");
        paramsScr.putString("modelName", REUtils.getDeviceModel());
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        RELog.e("onResume");
        mISBackground = false;
        isMilesUnitSelected = REUtils.isMilesUnitSelected();
        // New logic for Autotconnect
        //Log.e("BLEstatus", "" + mISBLEConnected);
        if (REApplication.getInstance().isNavigationInProgress() && !mISBLEConnected) {
            // Log.e("BLEstatus1", "" + mISBLEConnected);
            //Log.e("BLEscanstartred", "");
            manager.scan();
        }

        if (manualDisconnectBase){
            manager.stopScan(requireContext());
            setDisConnectedImage();
        }else{
        if (mContext != null && BLEDeviceManager.isAutoConnectEnabled(mContext)) {
            scanDevice();
        }
        }

        try {
            String hasbackgroundLocation = REPreference.getInstance().getString(mContext, "Nav_Location");
            RELog.e("hasbackgroundLocation = %s", hasbackgroundLocation);
            if (hasbackgroundLocation != null && hasbackgroundLocation.equalsIgnoreCase("loc_disabled") &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                RELog.e("hasbackgroundLocation enabled ");
                REPreference.getInstance().putString(mContext, "Nav_Location", "loc_enabled");
                if (mMapFragment != null)
                    mMapFragment.enableMyLocation();
            }
            //For first time launch of navigation page by deeplink
            String hasdeplink_locn = REPreference.getInstance().getString(mContext, "Deeplink_Nav_Location");
            RELog.e("hasdeeplinkLocation = %s", hasdeplink_locn);
            if (hasdeplink_locn != null && hasdeplink_locn.equalsIgnoreCase("deeplink_loc_disabled") &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                RELog.e("hasdeeplinkLocation enabled ");
                REPreference.getInstance().putString(mContext, "Deeplink_Nav_Location", "deeplink_loc_enabled");
                if (mMapFragment != null)
                    mMapFragment.enableMyLocation();
            }
        } catch (PreferenceException e) {
            RELog.e("Preference Exception = %s", e.getMessage());
        }
        if (mIsRecordEnded) {
            Runnable mRunnable = () -> {
                popBackStopRecording();
                mIsRecordEnded = false;
            };
            mHandler.post(mRunnable);
        }
        if (!REServiceSharedPreference.getDeepLinkData(mContext).equals("")) {
            setDeepLinkRouteInfo();
        }
        try {
            if (REPreference.getInstance().getBoolean(mContext, "Is_Muted")) {
                isMuteButtonClicked = true;
                mSpeakMuteBTN.setBackgroundResource(R.drawable.speaker_off);
                if (mMapFragment != null) {
                    if (mMapFragment.textToSpeech != null && mMapFragment.textToSpeech.isSpeaking())
                        mMapFragment.textToSpeech.stop();
                }
            } else {
                isMuteButtonClicked = false;
                mSpeakMuteBTN.setBackgroundResource(R.drawable.speaker);
            }
            if (mMapFragment != null) {
                mMapFragment.isSpeakMuted = isMuteButtonClicked;
            }
        } catch (PreferenceException e) {
            e.printStackTrace();
        }
        // Register for sensor updates
        if (magnetometer != null)
            mSensorManager.registerListener(this, magnetometer, SENSOR_DELAY_GAME);
        try {
            if (doubleDistanceInMeters != null) {
                setDistance(doubleDistanceInMeters);
            }
            if (longDurationGlobal != null) {
                long duration = Math.round(longDurationGlobal);
                final SpannableStringBuilder sbDuration = new SpannableStringBuilder(String.format(mContext.getResources().getString(R.string.nav_duration),
                        REUtils.getDurationInUnits(duration)));
                Typeface mTypeFaceBold = ResourcesCompat.getFont(mContext, R.font.montserrat_bold);
                sbDuration.setSpan((new RECustomTyperFaceSpan(mTypeFaceBold)), 3, sbDuration.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvGetNavDuration.setText(sbDuration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        handleUIForPiPMode(isInPictureInPictureMode);
        try {
            REPreference.getInstance().putBoolean(getActivity(), "pip", isInPictureInPictureMode);
        } catch (PreferenceException e) {
            e.printStackTrace();
        }


    }

    private void initBle() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mStoredDevicesList = BLEDeviceManager.getMyTBTList(mContext);
            if (mBluetoothAdapter.isEnabled()) {
                initUART();
            }
            if (mContext != null && BLEDeviceManager.isAutoConnectEnabled(mContext)) {
                scanDevice();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOTAPFirestoreSuccess(Map<String, Object> map) {
        mOtapMapData = map;
        RELog.e("onOTAPFirestoreSuccess:" + map.get("firmwareVersion"));
        RELog.e("onOTAPFirestoreSuccessURL:" + map.get("firmwareUrl"));
        initBle();
    }

    @Override
    public void onOTAPFirestoreFailure(String message) {
        RELog.e("onOTAPFirestoreFailure:");
        initBle();
    }


    /**
     * The ReverseGeocodingCallback to which the results of a search request will be delivered to.
     * Communicating with BILA for getting search results
     */
    private final SearchCallback<SearchResult> searchCallback = new SearchCallback<SearchResult>() {
        @Override
        public void onSearchFinished(@NotNull Response<SearchResult> response) {
            final List<SearchResult> searchResults = response.getResults();
            if (mContext != null) {
                hideLoading();
                mContext.runOnUiThread(() -> setResults(searchResults));
            }
        }

        @Override
        public void onSearchError(@NotNull SearchError searchError, @org.jetbrains.annotations.Nullable String s) {
            if (mContext != null) {
                if (searchError.name().equals(REConstants.TBT_API_UNAUTHORIZED)) {
                    mContext.runOnUiThread(() -> {
                        hideLoading();
                        REUtils.showErrorDialog(mContext, REApplication.getAppContext().getString(R.string.error_nav_unauthorized));
                    });
                } else {
                    if (mAPIFailureCount > REConstants.NAV_API_RETRY_COUNTER) {
                        mContext.runOnUiThread(() -> {
                            hideLoading();
                            REUtils.showErrorDialog(mContext, REApplication.getAppContext().getString(R.string.error_tbt_api));
                        });
                    } else {
                        mAPIFailureCount++;
                        getPOI(mPOICategory);
                    }
                }
            }
        }
    };

    @Override
    public void showHideViews(boolean b) {

    }

    private void onLeScanResult(@NotNull DeviceInfo deviceInfo) {
        RELog.e("onLeScanResult:%s", deviceInfo.getName());
        if (mContext != null && isVisible() && !isBLELibActive && !BLEModel.getInstance().isManualDisconnect()) {
            mStoredDevicesList = BLEDeviceManager.getMyTBTList(mContext);
            if (mStoredDevicesList.size() > 0 && mStoredDevicesList.contains(deviceInfo)) {
                if (!mISBLEConnected && !isScanInitiatedConnection) {
                    RELog.e("Connecting:");
                    isScanInitiatedConnection = true;
                    manager.connect(deviceInfo.getAddress(), false);
                }
            }
        }
    }

    @Override
    public void onInstallClick() {
        checkBluetooth();
    }

    enum BCTCategory {
        SAVE, SHARE, RECONSTRUCT, TRIPLIST
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkBatteryOptimization() {
        boolean isIgnoringBatteryOptimizations = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = Objects.requireNonNull(mContext).getPackageName();
            PowerManager powerManager = (PowerManager) mContext.getSystemService(POWER_SERVICE);
            assert powerManager != null;
            isIgnoringBatteryOptimizations = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoringBatteryOptimizations) {
                showErrorDialog(mContext, getString(R.string.text_battery_optimisation), "Permission");
            }
        }
        return isIgnoringBatteryOptimizations;
    }

    private boolean checkPhonePermission() {
        boolean hasPhonePermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                hasPhonePermission = true;
            }
        } else {
            hasPhonePermission = true;
        }
        return hasPhonePermission;
    }

    private void scanDevice() {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && !BLEModel.getInstance().isScanning() && mContext != null &&
                !BLEModel.getInstance().isManualDisconnect() && !isBLELibActive) {
            mStoredDevicesList = BLEDeviceManager.getMyTBTList(mContext);
            if (!mISBLEConnected && mStoredDevicesList.size() > 0 && mContext != null &&
                    REUtils.isLocationEnabled(mContext)) {
                RELog.e("Ble scan Initiated");
                LocalBroadcastManager.getInstance(mContext).registerReceiver(mScanResultsReceiver,
                        new IntentFilter(BLEConstants.BLE_INTENT_SCAN_RESUlTS));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                manager.scan();
            }
        }
    }


    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getResources().getString(R.string.customalert_title));
        int currentVersion = Build.VERSION.SDK_INT;
        if (currentVersion >= Build.VERSION_CODES.Q) {
            builder.setMessage(getResources().getString(R.string.text_bg_location_android_10));
        } else {
            builder.setMessage(getResources().getString(R.string.text_bg_location_android_9));
        }
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.customalert_ok), (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(getResources().getString(R.string.text_package),
                    mContext.getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
        });
        builder.show();
    }

    private void displayGotoSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getResources().getString(R.string.customalert_title));
        builder.setMessage(getResources().getString(R.string.text_settings_enable_phone_permission));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.customalert_ok), (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(getResources().getString(R.string.text_package),
                    mContext.getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_GOTO_PERMISSION_SETTING);
        });
        builder.show();
    }

    private void popBackStopRecording() {
        if (mContext != null) {
            //If recording we are managing UI for save/share
            mContext.runOnUiThread(() -> {
                mContext.getSupportFragmentManager().popBackStack();
                stopRecording();
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        RELog.e("onPause");
        mISBackground = true;
        // Unregister all sensors
        if (magnetometer != null)
            mSensorManager.unregisterListener(this);
    }


    private final BroadcastReceiver aLBReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean cloud_login = intent.getBooleanExtra("cloud_login", false);
            String cloud_action = intent.getStringExtra("cloud_action");
            RELog.e(TAG_NAVIGATION_FRAGMENT, "cloud_action = " + cloud_action);
            RELog.e(TAG_NAVIGATION_FRAGMENT, "cloud_login = " + cloud_login);
            if (cloud_login) {
                BCTCategory day = BCTCategory.valueOf(cloud_action);
                switch (day) {
                    case SAVE:
                        startTripUploadActivity("Save");
                        break;
                    case SHARE:
                        startTripUploadActivity("Share");
                        break;
                    case RECONSTRUCT:
                        getSharedNavigationId();
                        break;
                    case TRIPLIST:
                        Intent listIntent = new Intent(mContext, TripListActivity.class);
                        startActivityForResult(listIntent, REQUESTCODE_TRIP);
                        mContext.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                        break;
                    default:
                        break;
                }
            } else {
                Toast.makeText(mContext, "Login Failed ", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void initViews(View view) {
        imageViewHelmetPip = view.findViewById(R.id.imageView_helmet_pip);
        tvToolTip = view.findViewById(R.id.tool_tip);
        tvToolTip.setOnClickListener(this);
        mScrollView = view.findViewById(R.id.scroll_navigation_screen);
        mWaypointFragmentFrame = view.findViewById(R.id.nav_waypoint_holder);
        // navigationShareFragmentFrame = view.findViewById(R.id.nav_share_options);
        mMapFragmentFrame = view.findViewById(R.id.nav_map_holder);
        poiRecyclerView = view.findViewById(R.id.nav_poi_recyclerView);
        navConstraint = view.findViewById(R.id.navigation_constraint);
        navConstraint.setOnClickListener(this);
        navigation_PipLayout = view.findViewById(R.id.navigation_piplayout);
        tvPipInfoText = view.findViewById(R.id.tv_nav_msg_pip);
        tvPipDeviceDiconnectedInfo = view.findViewById(R.id.tv_nav_msg_pip_ble_disconnected_info);
        tvGetNavDistance = view.findViewById(R.id.nav_distance);
        tvGetNavDuration = view.findViewById(R.id.nav_duration);
        butNavStatusIcon = view.findViewById(R.id.nav_status_icon);
        mNavCompassBTN = view.findViewById(R.id.nav_compass_BTN);
        butNavStatusIcon.setOnClickListener(this);
        mNavCompassBTN.setOnClickListener(this);
        if (isDeviceConnected() && REApplication.getInstance().isIsDeviceAuthorised()) {
            butNavStatusIcon.setBackground(ContextCompat.getDrawable(mContext, R.drawable.orange_helmet_ic));
        }
        btnStartNavigation = view.findViewById(R.id.start_navigation);
        btnRecordStart = view.findViewById(R.id.record_start);
        btnRecordStop = view.findViewById(R.id.record_stop);
        btnRecordPause = view.findViewById(R.id.record_pause);
        btnNavigationSave = view.findViewById(R.id.save_navigation);
        btnNavigationShare = view.findViewById(R.id.share_navigation);
        mStopNavigationButton = view.findViewById(R.id.stop_navigation_BTN);
        mNavigationTXT = view.findViewById(R.id.navigation_msg_TXT);
        mNavigationReRouteTXT = view.findViewById(R.id.navigation_reroute_msg_TXT);
        myReloadBTN = view.findViewById(R.id.reload_navigation_BTN);
        mCurrentLocBTN = view.findViewById(R.id.current_loc_BTN);
        mReCenterBTN = view.findViewById(R.id.recenter_navigation_BTN);
        mSpeakMuteBTN = view.findViewById(R.id.speak_mute_BTN);
        myReloadBTN.setOnClickListener(this);
        mCurrentLocBTN.setOnClickListener(this);
        mReCenterBTN.setOnClickListener(this);
        mSpeakMuteBTN.setOnClickListener(this);
        mStopNavigationButton.setOnClickListener(this);
        btnNavigationSave.setOnClickListener(this);
        btnNavigationShare.setOnClickListener(this);
        btnStartNavigation.setOnClickListener(this);
        btnRecordStart.setOnClickListener(this);
        btnRecordStop.setOnClickListener(this);
        btnRecordPause.setOnCheckedChangeListener(this);
        RecyclerView.LayoutManager mUserCreatedRidesLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false);
        poiRecyclerView.setHasFixedSize(true);
        poiRecyclerView.setLayoutManager(mUserCreatedRidesLayoutManager);
        setPOIAdapter();
        manageNavigationLabels(GONE);
        addWayPointFragment();
        bottomSheet = view.findViewById(R.id.design_bottom_sheet_test);
        setNavigationTrickModeAction();
        if (isDeviceConnected() && REApplication.getInstance().isIsDeviceAuthorised()) {
            //in TBT 2.0 release we are commenting the call notification feature
            if (null != mMapFragment)
                mMapFragment.registerTFTCallListener();
        }
//        //Enable/Disable BCT feature based on the remote configuration.
//        ConfigFeatures configFeature = REUtils.getConfigFeatures();
//        if (configFeature != null && configFeature.getRecordRoute() != null) {
//            if (configFeature.getRecordRoute().getFeatureStatus().equalsIgnoreCase(REConstants.FEATURE_STATUS_DISABLED)) {
////                isRecordRouteDisabled = true;
////                btnRecordStart.setAlpha(0.3f);
////                btnRecordStart.setText(Html.fromHtml("Record<br/><small><small><small>Coming Soon</small></small></small>"));
//            } else {
//                btnRecordStart.setAlpha(1f);
//            }
//        }

        // Enable/Disable BCT feature based on the remote configuration.
        if (!BCTUtils.INSTANCE.isBCTFeatureEnabled()) {
            isRecordRouteDisabled = true;
            btnRecordStart.setAlpha(0.3f);
            btnRecordStart.setText(Html.fromHtml("Record<br/><small><small><small>Coming Soon</small></small></small>"));
        } else {
            btnRecordStart.setAlpha(1f);
        }
        isMilesUnitSelected = REUtils.isMilesUnitSelected();
        try {
            kLimitForOnlyIntelligentRecording = Double.valueOf(BCTUtils.INSTANCE.getTrailRecordLimit());
        } catch (Exception e) {
            Log.e("kLimitForOnlyIntelligentRecording: ", "Exception >>>");
            kLimitForOnlyIntelligentRecording = 50.00;
        }
        configurationPrefManager = ConfigurationPrefManager.getInstance(mContext);
        reLogger.appendLog("ReNavigationFragment...Thisss");
    }


    private void getSharedNavigationId() {
        String sharedid = ((REHomeActivity) mContext).getSharedNavigationId();
        if (sharedid != null) {
            RELog.e(TAG_NAVIGATION_FRAGMENT, "getting sharedid in Nav Frag = " + sharedid);
            ((REHomeActivity) mContext).setSharedNavigationId();
            if (((REHomeActivity) mContext).getSharedNavigationDetails()) {
                new FetchTripAsyncTask("details", this).execute(sharedid);
            } else {
                new FetchTripAsyncTask("share", this).execute(sharedid);
            }
        }
    }

    private void setNavigationTrickModeAction() {
        bottomSheet.setVisibility(GONE);
    }

    private void getPOIItemResult(PoiCategory category) {
        mAPIFailureCount = 1;
        mPOICategory = category;
        Location mCurrentLocation = REUtils.getLocationFromModel();
        mCurrentlatLng =
                new com.bosch.softtec.components.core.models.LatLng(mCurrentLocation.getLatitude(),
                        mCurrentLocation.getLongitude(), null);
        Distance distance = new Distance(10, LengthUnit.KILOMETERS);
        searchProvider = SearchProviderManager.getInstance().searchProvider(mContext);
        searchOptions = new SearchOptions.Builder()
                .searchLimit(10)
                .searchLocale(Locale.getDefault())
                .maxDistance(distance, mCurrentlatLng)
                .build();
        RELog.e("getPOIItemResult :" + category);
        showLoading();
        getPOI(category);

    }

    private void getPOI(PoiCategory category) {
        categoryMarker = getPOIMarker(category);
        searchProvider.searchAroundLocation(category, mCurrentlatLng, searchOptions, searchCallback);
    }

    private BitmapDescriptor getPOIMarker(PoiCategory category) {
        BitmapDescriptor categoryMarker;
        switch (category) {
            case RESTAURANT:
                categoryMarker = bitmapDescriptorFromVector(mContext, R.drawable.marker_restaurant);
                break;
            case GAS_STATION:
                categoryMarker = bitmapDescriptorFromVector(mContext, R.drawable.marker_fuel_station);
                break;
            case CAFE:
                categoryMarker = bitmapDescriptorFromVector(mContext, R.drawable.marker_cafe);
                break;
            case LODGING:
                categoryMarker = bitmapDescriptorFromVector(mContext, R.drawable.marker_hotel);
                break;
            case PARK:
                categoryMarker = bitmapDescriptorFromVector(mContext, R.drawable.marker_park);
                break;
            default:
                categoryMarker = bitmapDescriptorFromVector(mContext, R.drawable.place_marker);
                break;
        }
        return categoryMarker;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void manageNavigationLabels(int iVisibilityStatus) {
        tvGetNavDistance.setVisibility(iVisibilityStatus);
        tvGetNavDuration.setVisibility(iVisibilityStatus);
    }


    private void showBottomSheet() {
        NavigationFilterScreen navFilterFragment =
                NavigationFilterScreen.newInstance();
        navFilterFragment.setRouteListener(this);
        navFilterFragment.setNavigationProgress(REApplication.getInstance().isNavigationInProgress());
        navFilterFragment.show(getChildFragmentManager(),
                NavigationFilterScreen.TAG);
    }

    private void showBottomSheetPOIListFragment() {
        NavigationPOIFragment navPOIFragment =
                NavigationPOIFragment.newInstance(strSelectedPOIItem);
        navPOIFragment.setRouteListener(this);
        navPOIFragment.show(getChildFragmentManager(),
                NavigationPOIFragment.TAG);
    }

    @Override
    public void onPOIResultItemClick(PoiSearchResult item) {
        Bundle cParams = new Bundle();
        cParams.putString("eventCategory", REConstants.KEY_TRIPPER_GTM);
        cParams.putString("eventAction", REConstants.poiItemClickedFromList);
        cParams.putString("eventLabel", item.getPoiName());
        cParams.putString("modelName", REUtils.getDeviceModel());
        REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, cParams);
        //This gets called when user selects POI from the view list option
        LatLng latLng = new LatLng(Objects.requireNonNull(item.getAddress().component1()).getLatitude(),
                Objects.requireNonNull(item.getAddress().component1()).getLongitude());
        //Update the cells in the list
        addWayPointFragment.updateWayPoint(latLng, item.getPoiName());
        //plot the marker in the map
        plotMarker();
    }

    @Override
    public void onPOIResultShowMap(String strPoiName, boolean isChecked) {
        if (!isChecked) {
            poiAdapter.setPOIItemHighlighted(-1);
            poiAdapter.notifyDataSetChanged();
            tvToolTip.setVisibility(GONE);
            strSelectedPOIItem = null;
            mMapFragment.adjustMapHeight(getMapBottomPadding());
            mMapFragment.clearMarkers();
            plotMarker(mWayPointList, false, 14);
            return;
        }
        //For loop is needed for taking the position of the item
        for (int index = 0; index < poiModelList.size(); index++) {
            if (strPoiName.equalsIgnoreCase(poiModelList.get(index).getName())) {
                poiAdapter.setPOIItemHighlighted(index);
                poiAdapter.notifyDataSetChanged();
                break;
            }
        }
        strSelectedPOIItem = strPoiName;
        fetchPOIData(strPoiName);
    }


    /**
     * Adding map fragment
     */
    private void addMapFragment() {
        try {
            mMapFragment = RENavigationMapFragment.newInstance();
            mMapFragment.setMarkerListener(this);
            mMapFragment.setListener(this);
            mMapFragment.setNavigationRouteListener(this);
            mMapFragment.setRENavigationListener(this);
            mMapFragment.setRidesListener(this);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(REConstants.WAY_POINTS_LIST, mWayPointList);
            bundle.putString(RIDE_WAYPOINT_TYPE, WAYPOINT_NAVIGATION);
            bundle.putBoolean(REConstants.IS_NAVIGATION, true);
            bundle.putBoolean(REConstants.IS_LOCATION_REQUIRED, true);
            mMapFragment.setArguments(bundle);
            if (mContext != null && mMapFragmentFrame != null) {
                mMapFragmentFrame.loadFragment(mContext, mMapFragment, MapFragment.TAG_MAP_FRAGMENT);
            }
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    /**
     * Adding the AddWayPointFragment
     */
    private void addWayPointFragment() {
        try {
            addWayPointFragment = new NavigationAddWayPointFragment();
            mWayPointList.add(1, new WayPointsData(0, 0, null));
            addWayPointFragment.setArguments(setBundleData());
            addWayPointFragment.setRouteListener(this);
            if (mContext != null && mWaypointFragmentFrame != null) {
                mWaypointFragmentFrame.loadFragment(mContext, addWayPointFragment, NavigationAddWayPointFragment.TAG);
            }
        } catch (Exception e) {
            RELog.e(e);
        }
    }


    /**
     * This sets the places results to adapter
     *
     * @param searchResults used to show POI results
     */
    private void setResults(List<SearchResult> searchResults) {
        poiSearchResults = new ArrayList<>();
        if (searchResults != null && searchResults.size() > 0) {
            poiSearchResults = searchResults;
            tvToolTip.setVisibility(VISIBLE);
            mMapFragment.adjustMapHeight(getMapBottomPadding());
            for (SearchResult searchResult : searchResults) {
                RELog.e(TAG_NAVIGATION_FRAGMENT, "test results = " + searchResult.getAddress());
                PoiSearchResult poiResult = (PoiSearchResult) searchResult;
                LatLng latLng = new LatLng(Objects.requireNonNull(poiResult.getAddress().component1()).getLatitude(),
                        Objects.requireNonNull(poiResult.getAddress().component1()).getLongitude());
                mMapFragment.showPOIMarker(latLng, ((PoiSearchResult) searchResult).getPoiName(), categoryMarker, false);
            }
        } else {
            Toast.makeText(mContext, "Result Not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sets Bundle data
     */
    private Bundle setBundleData() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(WAY_POINTS_LIST, mWayPointList);
        bundle.putString(RIDE_WAYPOINT_TYPE, WAYPOINT_NAVIGATION);
        RELog.e(TAG_NAVIGATION_FRAGMENT, "mWayPointList size = " + mWayPointList.size());
        return bundle;
    }

    private void setPOIAdapter() {
        poiModelList = new ArrayList<>();
        POIModel poiModel1 = new POIModel("Restaurant", R.drawable.ic_fork, PoiCategory.RESTAURANT);
        POIModel poiModel2 = new POIModel("Fuel Station", R.drawable.ic_fuelstation, PoiCategory.GAS_STATION);
        POIModel poiModel3 = new POIModel("Café", R.drawable.ic_coffeecup, PoiCategory.CAFE);
        POIModel poiModel4 = new POIModel("Hotel", R.drawable.ic_bed, PoiCategory.LODGING);
        POIModel poiModel5 = new POIModel("Park", R.drawable.ic_park, PoiCategory.PARK);
        poiModelList.add(poiModel1);
        poiModelList.add(poiModel2);
        poiModelList.add(poiModel3);
        poiModelList.add(poiModel4);
        poiModelList.add(poiModel5);
        poiAdapter = new POIAdapter(mContext, poiModelList, this, true);
        poiRecyclerView.setAdapter(poiAdapter);
    }

    private void fetchPOIData(String strPoiName) {
        for (POIModel poiModel : poiModelList) {
            if (strPoiName.equalsIgnoreCase(poiModel.getName())) {
                mMapFragment.clearMarkers();
                getPOIItemResult(poiModel.getPoiCategory());
                break;
            }
        }
    }

    @Override
    public void stopNavigationCalled(boolean isRecord) {
        if (mMapFragment != null) {

            mContext.runOnUiThread(() -> {
                btnRecordPause.setText(R.string.pause_label);
                btnRecordPause.setChecked(true);
            });

            mMapFragment.cancelScheduledTimer();
            mMapFragment.stopNavigation();
            mMapFragment.stopRecord(isRecord);
            mIsStopNavigation = true;
            tripEndTime = getSummaryApiTimeFormat(getCurrentTime());
            mMapFragment.createNavigationSummaryApiDetails(tripStartTime, tripEndTime);
            hideBottomNavaigationTabs(false);
        }
    }

    @Override
    public void updateCurrentLocation(LatLng latLng) {
        RELog.e(TAG_NAVIGATION_FRAGMENT, "updateCurrentLocation ");
        if (mSourceLat == 0 && mSourceLon == 0 && latLng != null) {
            mSourceLat = latLng.latitude;
            mSourceLon = latLng.longitude;
            if (mWayPointList != null && mWayPointList.size() > 0) {
                mWayPointList.set(0, new WayPointsData(mSourceLat, mSourceLon, "Origin"));
                RELog.e(TAG_NAVIGATION_FRAGMENT, "updateCurrentLocation addwaypoint called");
                setDeepLinkRouteInfo();
            }
        }
    }

    @Override
    public void setDistance(Double distanceInMeters) {
        doubleDistanceInMeters = distanceInMeters;

        if (isMilesUnitSelected) {
            Distance m = new Distance(doubleDistanceInMeters, LengthUnit.METERS);
            final SpannableStringBuilder sbDistance = new SpannableStringBuilder(String.format(mContext.getResources().getString(R.string.nav_distance),
                    REUtils.formatDistanceInMilesUnit(m)));
            Typeface mTypeFaceBold = ResourcesCompat.getFont(mContext, R.font.montserrat_bold);
            sbDistance.setSpan((new RECustomTyperFaceSpan(mTypeFaceBold)), 8, sbDistance.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvGetNavDistance.setText(sbDistance);
        } else {
            final SpannableStringBuilder sbDistance = new SpannableStringBuilder(String.format(mContext.getResources().getString(R.string.nav_distance),
                    REUtils.getDistanceInUnits(distanceInMeters)));
            Typeface mTypeFaceBold = ResourcesCompat.getFont(mContext, R.font.montserrat_bold);
            sbDistance.setSpan((new RECustomTyperFaceSpan(mTypeFaceBold)), 8, sbDistance.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvGetNavDistance.setText(sbDistance);
        }
    }


    @Override
    public void updateRoute(ArrayList<WayPointsData> wayPointDataList) {
        mWayPointList = wayPointDataList;
        manageNavigationLabels(GONE);
        poiRecyclerView.setVisibility(VISIBLE);
        navConstraint.setVisibility(VISIBLE);
        bottomSheet.setVisibility(GONE);
        if (strSelectedPOIItem != null && !strSelectedPOIItem.isEmpty()) {
            tvToolTip.setVisibility(VISIBLE);
        }

        if (mMapFragment!=null){
            mMapFragment.adjustMapHeight(getMapBottomPadding());
            enableMarkerListeners();
            mMapFragment.setMarkerListener(this);
        }

    }

    @Override
    public void updateRoute(WayPointsData wayPointsData) {
        if (null != wayPointsData) {
            LatLng latLng = new LatLng(wayPointsData.getLatitude(), wayPointsData.getLongitude());
            addWayPointFragment.updateWayPoint(latLng, wayPointsData.getPlaceName());
            plotMarker();

        }

    }

    public void updateRoute() {
        manageNavigationLabels(GONE);
        poiRecyclerView.setVisibility(VISIBLE);
        navConstraint.setVisibility(VISIBLE);
        bottomSheet.setVisibility(GONE);
        if (strSelectedPOIItem != null && !strSelectedPOIItem.isEmpty()) {
            tvToolTip.setVisibility(VISIBLE);
        }
        mMapFragment.adjustMapHeight(getMapBottomPadding());
    }

    @Override
    public void updateOnMilestoneReached(WayPointsData upcomingWayPoint) {
        addWayPointFragment.onMilestoneReached(upcomingWayPoint);
    }

    @Override
    public void showFilterOptionDialog() {
        showBottomSheet();
    }

    @Override
    public void showTripListScreen() {
        Intent listIntent = new Intent(mContext, TripListActivity.class);
        startActivityForResult(listIntent, REQUESTCODE_TRIP);
        mContext.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void showReconstructedMode() {
        //showErrorDialog(mContext, "You have selected a Recorded Route,So you cannot add or edit the location. Would you like to discard and exit the Route?", "Launch");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUESTCODE_BLE_LIB_STATUS) {
            isBLELibActive = false;
        }
        switch (requestCode) {
            case REQUESTCODE_NAVIGATION:
                if (resultCode == RESULT_OK && null != data && data.getExtras() != null) {
                    String mType = data.getExtras().getString(REConstants.NAVIGATION_TYPE_KEY);
                    if (mType != null && mType.equals(REConstants.NAVIGATION_TYPE_STOP)) {
                        //handled in same class
                    } else if (mType != null && mType.equals(NAVIGATION_TYPE_RECORD_RESUME)) {
                        resumeRecord();
                    } else if (mType != null && mType.equals(NAVIGATION_TYPE_RECORD_PAUSE)) {
                        pauseRecord();
                    } else if (mType != null && mType.equals(NAVIGATION_TYPE_RECORD_STOP)) {
                        stopRecording();
                        addWayPointFragment.disableWaypoint(true);
                    }
                }
                break;
            case REQUESTCODE_SHARE:
                reloadFragment();
                break;
            case REQUESTCODE_BCT_SHARE_ROUTE:
                reloadFragment();
                break;
            case REQUESTCODE_UPLOAD:
                String strTripId = "";
                if (resultCode == RESULT_OK && null != data) {
                    String strAction = data.getStringExtra("action");
                    assert strAction != null;
                    if (strAction.equalsIgnoreCase("Save")) {
                        onRecordSavedSuccessfully();
                    } else if (strAction.equalsIgnoreCase("Share")) {
                        strTripId = data.getStringExtra("tripId");
                        if (strTripId != null && !strTripId.isEmpty()) {
                            onRecordShare(strTripId);
                        } else {
                            onRecordSavedSuccessfully();
                        }
                    }
                }
                break;
            case REQUESTCODE_TRIP:
                if (resultCode == RESULT_OK && null != data) {
                    strTripId = data.getStringExtra(REConstants.NAVIGATION_NOTIFICATION);
                    if (strTripId != null && strTripId.length() > 0) {
                        showLoading();
                        new FetchTripAsyncTask("details", this).execute(strTripId);
                    }
                }
                break;
            case MY_IGNORE_OPTIMIZATION_PERMISSION:
                if (resultCode != RESULT_OK) {
                    checkBatteryOptimization();
                }
                break;
            case MY_IGNORE_OPTIMIZATION_REQUEST:
                if (resultCode != RESULT_OK) {
                    /*if (isDirectBatteryOptimizationSpecific()) {
                        checkBatteryOptimization();
                    } else */
                    if (isSettingSpecificBatteryOptimization() || isAppInfoSpecificBatteryOptimization()) {
                        checkBatteryOptimization();
                    } else {
                        if (!isBatteryPermission()) {
                            callBatteryOptimizationExemption();
                        }
                    }
                }
                break;
            case REQUEST_PERMISSION_SETTING:
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    displayNeverAskAgainDialog();
                }
                break;
            case REQUEST_GOTO_PERMISSION_SETTING:
                break;
        }
    }

    /**
     * Reloads this fragment
     */
    private void reloadFragment() {
       /* assert getFragmentManager() != null;
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        Fragment mNavStartFragment = new NavTest();
        trans.add(R.id.root_navigation_frame, mNavStartFragment);
        trans.addToBackStack("navtest");
        trans.commitAllowingStateLoss();*/
        //To clear Deeplink data
        REServiceSharedPreference.setDeepLinkData(mContext, "");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mContext.getSupportFragmentManager().beginTransaction().detach(this).commitAllowingStateLoss();
                mContext.getSupportFragmentManager().beginTransaction().attach(this).commitAllowingStateLoss();
            } else {
                mContext.getSupportFragmentManager().beginTransaction().detach(this).attach(this).commitAllowingStateLoss();
            }
            addMapFragment();
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void openBleSearchActivity() {
        if (REUtils.isLocationEnabled(mContext)) {
            REApplication.getInstance().setLoggerFileCreated(false);
            Intent intent = new Intent(mContext, BleSearchActivity.class);
            if (mOtapMapData != null) {
                intent.putExtra("otapmap", (Serializable) mOtapMapData);
            }
            isBLELibActive = true;
            isNavHelmetCLicked = false;
            startActivityForResult(intent, REQUESTCODE_BLE_LIB_STATUS);
            mContext.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
        } else {
            //Request Location Permission
            displayNeverAskAgainDialog();
        }
    }

    private boolean isDeviceConnected() {
        List<DeviceInfo> deviceInfoList = REApplication.getInstance().getmConnectedDeviceInfo();
        return deviceInfoList != null && deviceInfoList.size() > 0;
       // return true;
    }


    private boolean hasBackgroundLocationPermission() {
        boolean hasBackgroundPermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                hasBackgroundPermission = true;
            }
        } else {
            hasBackgroundPermission = true;
        }
        return hasBackgroundPermission;
    }


    @Override
    public void onClick(View v) {
        isDeviceConnected();
        switch (v.getId()) {
            case R.id.start_navigation:
                String journeyTime = tvGetNavDuration.getText().toString().replace("ETA ", "");
                String journeyKms = tvGetNavDistance.getText().toString().replace("Distance ", "");
                Bundle aParams = new Bundle();
                aParams.putString("eventCategory", REConstants.KEY_TRIPPER_GTM);
                aParams.putString("eventAction", "Start Navigation Clicked");
                String aDestinationStr = "";
                if (mWayPointList != null && mWayPointList.size() > 0 && mWayPointList.get(mWayPointList.size() - 1).getPlaceName() != null) {
                    aDestinationStr = mWayPointList.get(mWayPointList.size() - 1).getPlaceName();
                }
                aParams.putString("eventLabel", aDestinationStr);
                aParams.putString("Journey_time", journeyTime);
                aParams.putString("Journey_kms", journeyKms);
                REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, aParams);
                if (isDeviceConnected()) {
                    //Checking Battery Optimisation
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkPhonePermission()) {
                            if (checkBatteryOptimization())
                                startNavigation();
                        } else {
                            //Request Phone Permission
                            displayGotoSettingsDialog();
                        }
                    } else {
                        startNavigation();
                    }
                } else {
                    REUtils.showMessageDialog(mContext, mContext.getResources().
                            getString(R.string.text_hint_connect_re_display), new REUtils.OnDialogButtonsClickListener() {
                        @Override
                        public void onOkCLick() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                if (ContextCompat.checkSelfPermission(
                                        getContext(), Manifest.permission.BLUETOOTH_SCAN
                                ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    onHelmetIconSelected();
                                } else {
                                    checkBlePermissions();
                                }
                            } else {
                                onHelmetIconSelected();
                            }
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
                }
                break;
            case R.id.record_start:
                journeyTimeb = tvGetNavDuration.getText().toString().replace("ETA", "");
                journeyKmsb = tvGetNavDistance.getText().toString().replace("Distance", "");
                Bundle bParams = new Bundle();
                bParams.putString("eventCategory", REConstants.KEY_TRIPPER_GTM);
                bParams.putString("eventAction", "Start Record Clicked");
                if (mWayPointList != null && mWayPointList.size() > 0 && mWayPointList.get(mWayPointList.size() - 1).getPlaceName() != null) {
                    bDestinationStr = mWayPointList.get(mWayPointList.size() - 1).getPlaceName();
                }
                bParams.putString("eventLabel", bDestinationStr);
                bParams.putString("Journey_time", journeyTimeb);
                bParams.putString("Journey_kms", journeyKmsb);
                REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, bParams);
                if (!isRecordRouteDisabled) {
                    if (isDeviceConnected()) {
                        //Checking Battery Optimisation
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkPhonePermission()) {
                                if (checkBatteryOptimization())
                                    // startRecord();
                                    showRecordOptionDialog(getRecordingOptionSelectionState());
                            } else {
                                //Request Phone Permission
                                displayGotoSettingsDialog();
                            }
                        } else {
                            // startRecord();
                            showRecordOptionDialog(getRecordingOptionSelectionState());
                        }
                    } else {
                        REUtils.showMessageDialog(mContext, mContext.getResources().
                                getString(R.string.text_hint_connect_re_display), new REUtils.OnDialogButtonsClickListener() {
                            @Override
                            public void onOkCLick() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    if (ContextCompat.checkSelfPermission(
                                            getContext(), Manifest.permission.BLUETOOTH_SCAN
                                    ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        onHelmetIconSelected();
                                    } else {
                                        checkBlePermissions();
                                        // checkBluetooth();
                                    }
                                } else {
                                    onHelmetIconSelected();
                                }
                            }

                            @Override
                            public void onCancelClick() {

                            }
                        });
                    }
                }
                break;

       /*     case R.id.record_pause:
                btnStartNavigation.setVisibility(GONE);
                btnRecordStart.setVisibility(GONE);
                btnRecordStop.setVisibility(VISIBLE);
                btnRecordPause.setVisibility(GONE);
                btnRecordPause.setText(R.string.resume_label);
                btnNavigationShare.setVisibility(GONE);
                btnNavigationSave.setVisibility(GONE);
                break;*/

            case R.id.save_navigation:
                startTripUploadActivity("Save");
                break;
            case R.id.share_navigation:
                startTripUploadActivity("Share");
                break;
            case R.id.tool_tip:
                Bundle cParams = new Bundle();
                cParams.putString("eventCategory", REConstants.KEY_TRIPPER_GTM);
                cParams.putString("eventAction", REConstants.viewListClicked);
                cParams.putString("eventLabel", (strSelectedPOIItem != null) ? strSelectedPOIItem : "");
                cParams.putString("modelName", REUtils.getDeviceModel());
                REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, cParams);
                REUtils.preventMultipleClick(v, 1500);
                showBottomSheetPOIListFragment();
                break;
            case R.id.nav_status_icon:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ContextCompat.checkSelfPermission(
                            getContext(), Manifest.permission.BLUETOOTH_SCAN
                    ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        onHelmetIconSelected();
                    } else {
                        checkBlePermissions();
                        // checkBluetooth();
                    }
                } else {
                    onHelmetIconSelected();
                }
                break;
            case R.id.navigation_constraint:
                Bundle dParams = new Bundle();
                dParams.putString("eventCategory", REConstants.KEY_TRIPPER_GTM);
                dParams.putString("eventAction", REConstants.getDirectionClicked);
                String cDestinationStr = "";
                if (mWayPointList != null && mWayPointList.size() > 0 && mWayPointList.get(mWayPointList.size() - 1).getPlaceName() != null) {
                    cDestinationStr = mWayPointList.get(mWayPointList.size() - 1).getPlaceName();
                }
                dParams.putString("eventLabel", cDestinationStr);
                dParams.putString("modelName", REUtils.getDeviceModel());
                REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, dParams);
                // Redraws the route
                if (mWayPointList.size() >= 2 && mWayPointList.get(1).getLatitude() != 0 &&
                        mWayPointList.get(1).getLongitude() != 0) {
                    if (addWayPointFragment.isEmptyWayPointAdded()) {
                        Toast.makeText(mContext, "Choose Way Point ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mWayPointList.get(0).getLatitude() != 0 && mWayPointList.get(0).getLongitude() != 0) {
                        RecentRouteLocationManager.saveRecentRoute(new RecentRoute(mWayPointList), mContext);
                        mMapFragment.calculateStartDestinationAndWayPointLatLang(mWayPointList);
                        if (!mISBLEConnected) {
                            scanDevice();
                        }
                        //Log.e("RecentRoute",""+RecentRouteLocationManager.getRecentRoutes(mContext).get(0).getmWayPointList().toString());
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(mContext,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                //Location Permission already granted
                                mMapFragment.enableMyLocation();
                            } else {
                                //Request Location Permission
                                displayNeverAskAgainDialog();
                            }
                        }
                    }
                    mMapFragment.adjustMapHeight(getMapBottomPadding());
                } else {
                    if (addWayPointFragment.isEmptyWayPointAdded()) {
                        Toast.makeText(mContext, "Choose Way Point ", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(mContext, "Choose Drop Point ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.reload_navigation_BTN:
                reloadFragment();
                break;
            case R.id.speak_mute_BTN:
                try {
                    muteSpeakforNavigation();
                } catch (PreferenceException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.stop_navigation_BTN:
                stopNavigation();
                break;
            case R.id.nav_compass_BTN:
                showCalibrationPopup();
                break;
            case R.id.current_loc_BTN:
            case R.id.recenter_navigation_BTN:
                mMapFragment.resetUserGesture();
                mMapFragment.onCurrentLocationClick();
                break;
            case R.id.record_stop:
                stopRecording();
                break;
        }
    }

    private void muteSpeakforNavigation() throws PreferenceException {
        REPreference.getInstance().putBoolean(mContext, "Is_Muted", !isMuteButtonClicked);
        isMuteButtonClicked = !isMuteButtonClicked;// toggle the boolean flag
        mSpeakMuteBTN.setBackgroundResource(isMuteButtonClicked ? R.drawable.speaker_off : R.drawable.speaker);
        mMapFragment.isSpeakMuted = isMuteButtonClicked;
        if (isMuteButtonClicked) {
            mMapFragment.textToSpeech.stop();
        }

    }

    private void onHelmetIconSelected() {
        Bundle params = new Bundle();
        params.putString("eventCategory", REConstants.KEY_TRIPPER_GTM);
        params.putString("eventAction", REConstants.helmetIconClicked);
        params.putString("eventLabel", isDeviceConnected() ? "true" : "false");
        params.putString("modelName", REUtils.getDeviceModel());
        REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
        if (hasBackgroundLocationPermission()) {
            isNavHelmetCLicked = true;
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(
                        getContext(), Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openBleSearchActivity();
                    //   checkBlePermissions();
                } else {
                    checkBlePermissions();
                    // checkBluetooth();
                }
            } else {
                checkBluetooth();
            }*/
            checkBluetooth();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    REPreference.getInstance().putString(mContext, "Nav_Location", "loc_disabled");
                } catch (PreferenceException e) {
                    RELog.e("Exception =%s", e.getMessage());
                }
            }
            displayNeverAskAgainDialog();
        }
    }

    private void checkBlePermissions() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_SCAN)) {
            REUtils.showErrorPerMission(
                    getContext(), getString(R.string.text_ble_nearby_permission));
        } else {
            requestBlePermissions();
        }
    }

    private void requestBlePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissions(
                    new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkBluetooth();
                    //  initUART();
                } else if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    permissionDenialCount++;
                    if (permissionDenialCount == 1) {
                        checkBlePermissions();
                    }
                    if (permissionDenialCount == 2) {
                        Log.e("onRequestPermissionsResult: ", "fie access");
                        Toast.makeText(getContext(),
                                getResources().getString(R.string.text_settings_enable),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                return;
        }
    }


    private void startNavigation() {
        REApplication.getInstance().setNavigationInProgress(true);
        hideBottomNavaigationTabs(true);
        btnStartNavigation.setVisibility(GONE);
        btnRecordStart.setVisibility(GONE);
        mStopNavigationButton.setVisibility(VISIBLE);
        mNavigationTXT.setVisibility(VISIBLE);
        mNavType = REConstants.NAVIGATION_TYPE_STOP;
        myReloadBTN.setVisibility(GONE);
        mSpeakMuteBTN.setVisibility(VISIBLE);
        mCurrentLocBTN.setVisibility(GONE);
        mReCenterBTN.setVisibility(VISIBLE);
        addWayPointFragment.onNavigationStarted();
        mMapFragment.startNavigation(false);
        mMapFragment.plotReroutedRoutesOnMap();
        mMapFragment.setMyLocationEnabled(true);
        // mMapFragment.setMyLocationEnabled(false);
        mMapFragment.setMarkerListener(null);
        mMapFragment.adjustMapHeight(getMapBottomPadding());
        mMapFragment.resetUserGesture();
        mMapFragment.onCurrentLocationClick();
        // butNavStatusIcon.setBackground(ContextCompat.getDrawable(mContext, R.drawable.green_helmet_ic));
        setConnectedImage();
        tripStartTime = getSummaryApiTimeFormat(getCurrentTime());
    }

    private void stopNavigation() {
        REApplication.getInstance().setNavigationInProgress(false);
        hideBottomNavaigationTabs(false);
        myReloadBTN.setVisibility(VISIBLE);
        mSpeakMuteBTN.setVisibility(GONE);
        addWayPointFragment.onNavigationEnded();
        mStopNavigationButton.setVisibility(GONE);
        // Checking mNavType if current Navigation is with record or not
        stopNavigationCalled(mNavType.equals(REConstants.NAVIGATION_TYPE_RECORD));
    }

    private void startRecord() {
        REApplication.getInstance().setNavigationInProgress(true);
        hideBottomNavaigationTabs(true);
        RELog.e(TAG_NAVIGATION_FRAGMENT, "navigation record started");
        btnStartNavigation.setVisibility(GONE);
        btnRecordStart.setVisibility(GONE);
        btnRecordStop.setVisibility(VISIBLE);
        btnRecordPause.setVisibility(GONE);
        btnRecordPause.setText(R.string.pause_label);
        btnNavigationShare.setVisibility(GONE);
        btnNavigationSave.setVisibility(GONE);
        mNavigationTXT.setVisibility(VISIBLE);
        mSpeakMuteBTN.setVisibility(VISIBLE);
        mReCenterBTN.setVisibility(VISIBLE);
        myReloadBTN.setVisibility(GONE);
        mCurrentLocBTN.setVisibility(GONE);
        setConnectedImage();
        addWayPointFragment.onNavigationStarted();
        tripStartTime = getSummaryApiTimeFormat(getCurrentTime());
        mMapFragment.startNavigation(true);
        mMapFragment.plotReroutedRoutesOnMap();
        mMapFragment.setMyLocationEnabled(true);
        //  mMapFragment.setMyLocationEnabled(false);
        mMapFragment.setMarkerListener(null);
        mMapFragment.adjustMapHeight(getMapBottomPadding());
        mMapFragment.resetUserGesture();
        mMapFragment.onCurrentLocationClick();
        mNavType = REConstants.NAVIGATION_TYPE_RECORD;
    }

    private void stopRecording() {
        REApplication.getInstance().setNavigationInProgress(false);
        hideBottomNavaigationTabs(false);
        btnStartNavigation.setVisibility(View.GONE);
        btnRecordStart.setVisibility(GONE);
        btnRecordStop.setVisibility(GONE);
        btnRecordPause.setVisibility(GONE);
        btnRecordPause.setText(R.string.pause_label);
        btnRecordPause.setChecked(true);
        myReloadBTN.setVisibility(VISIBLE);
        mNavigationTXT.setVisibility(GONE);
        mSpeakMuteBTN.setVisibility(GONE);
        mReCenterBTN.setVisibility(GONE);
        btnNavigationShare.setVisibility(VISIBLE);
        btnNavigationSave.setVisibility(VISIBLE);
        mCurrentLocBTN.setVisibility(VISIBLE);
        stopNavigationCalled(true);
        tripEndTime = getSummaryApiTimeFormat(getCurrentTime());

        Bundle params = new Bundle();
        params.putString("eventCategory", "Tripper");
        params.putString("eventAction", "Stop Record");
        params.putString("eventLabel", bDestinationStr);
        params.putString("Journey_time", journeyTimeb);
        params.putString("Journey_kms", journeyKmsb);
        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

    }

    private void startTripUploadActivity(String strAction) {
        BreadcrumbTrail breadcrumbTrail = mMapFragment.getBreadcrumbTrail();
        if (breadcrumbTrail != null) {
            if (breadcrumbTrail.getBreadcrumbs().size() < 3) {
                Toast.makeText(mContext, "Recording Stopped.\nNo Data saved", Toast.LENGTH_SHORT).show();
            } else {
                String journeyTimeb = tvGetNavDuration.getText().toString().replace("ETA", "");
                String journeyKmsb = tvGetNavDistance.getText().toString().replace("Distance", "");
                String bDestinationStr = "";
                if (mWayPointList != null && mWayPointList.size() > 0 && mWayPointList.get(mWayPointList.size() - 1).getPlaceName() != null) {
                    bDestinationStr = mWayPointList.get(mWayPointList.size() - 1).getPlaceName();
                }

                Bundle params = new Bundle();
                params.putString("eventCategory", "Tripper");
                params.putString("eventAction", strAction + " Record");
                params.putString("eventLabel", bDestinationStr);
                params.putString("Journey_kms", journeyKmsb);
                params.putString("Journey_time", journeyTimeb);
                REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

                Intent bctIntent = new Intent(mContext, BCTSaveInfoActivity.class);
                bctIntent.putExtra("startTime", tripStartTime);
                bctIntent.putExtra("endTime", tripEndTime);
                bctIntent.putExtra("action", strAction);
                startActivityForResult(bctIntent, REQUESTCODE_UPLOAD);
            }
        }
    }

    /**
     * Checks and allows to enable bluetooth if disabled.
     */
    private void checkBluetooth() {
        try {
            // Is Bluetooth supported on this device?
            if (mBluetoothAdapter != null) {
                // Is Bluetooth turned on?
                if (!mBluetoothAdapter.isEnabled()) {
                    // Prompt user to turn on Bluetooth (logic continues in onActivityResult()).
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    openBleSearchActivity();
                }
            } else {
                // Bluetooth is not supported.
                Toast.makeText(mContext, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUART() {
        try {
            if (mContext != null) {
                manager.openGattServer(mContext);
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            //This exception come whien we reopen the UART service
            //So if Nav refreshes handling the exception
            RELog.e(e);
        }
    }

    /**
     * Opens Navigation start fragment
     */
    private void openNavigationStartFragment() {
        REApplication.getInstance().setNavigationInProgress(true);
        assert getFragmentManager() != null;
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        Fragment mNavStartFragment = new NavigationStartFragment();
        mNavStartFragment.setTargetFragment(RENavigationFragment.this, REQUESTCODE_NAVIGATION);
        Bundle args = new Bundle();
        args.putString(REConstants.NAVIGATION_TYPE_KEY, mNavType);
        mNavStartFragment.setArguments(args);
        trans.add(R.id.root_navigation_frame, mNavStartFragment);
        trans.addToBackStack("nav");
        trans.commitAllowingStateLoss();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showErrorDialog(final Context context, String message, String reqMode) {
        try {
            TextView alert_message, alert_ok, alert_cancel, mAlertDIYLinkTXT;
            ImageView mCloseIMG, mScreenshotIMG;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            assert layoutInflater != null;
            View alertLayout = layoutInflater.inflate(R.layout.dialog_battery_optimization_alert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            mCloseIMG = alertLayout.findViewById(R.id.dialog_customalert_close_IMG);
            mScreenshotIMG = alertLayout.findViewById(R.id.dialog_battery_optimization_screen_IMG);
            //setting message for alert dialog
            alert_message.setText(getBatteryOptimizationMessage(message));
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            alert_cancel = alertLayout.findViewById(R.id.textView_alert_cancel_button);
            mAlertDIYLinkTXT = alertLayout.findViewById(R.id.textView_alert_diy_link);
            if (!reqMode.equalsIgnoreCase("permission")) {
                alert_cancel.setVisibility(VISIBLE);
            }
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            if (reqMode.equalsIgnoreCase("permission")) {
                dialog.setCanceledOnTouchOutside(false);
                mCloseIMG.setVisibility(VISIBLE);
                if (BuildConfig.FLAVOR.contains("Apac") || BuildConfig.FLAVOR.contains("Rena") || BuildConfig.FLAVOR.contains("Latm") || BuildConfig.FLAVOR.contains("EU"))
                    mAlertDIYLinkTXT.setVisibility(GONE);
                else
                    mAlertDIYLinkTXT.setVisibility(VISIBLE);
                if (isSettingSpecificBatteryOptimization() || isAppInfoSpecificBatteryOptimization()) {
                    mScreenshotIMG.setVisibility(VISIBLE);
                    RequestBuilder<Drawable> requestBuilder = Glide.with(alertLayout.getContext())
                            .asDrawable()
                            .load(getBatteryOptimizationImage());
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.ic_image_loading)
                            .error(R.drawable.no_image_found)
                            .priority(Priority.HIGH);
                    requestBuilder
                            .apply(options)
                            .into(mScreenshotIMG);
                }
            }
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(v -> {
                dialog.dismiss();
                if (reqMode.equalsIgnoreCase("permission")) {
                    /*if (isDirectBatteryOptimizationSpecific()) {
                        startBatteryOptimizationActivity();
                    } else*/
                    if (isSettingSpecificBatteryOptimization()) {
                        startSettingsScreen();
                    } else if (isAppInfoSpecificBatteryOptimization()) {
                        startAppInfoScreen();
                    } else {
                        checkBatteryOptimizationPermission();
                    }
                } else {
                    reloadFragment();
                }
            });
            mCloseIMG.setOnClickListener(v -> {
                dialog.dismiss();
            });
            alert_cancel.setOnClickListener(v -> {
                dialog.dismiss();
            });
            mAlertDIYLinkTXT.setOnClickListener(v -> {
                startActivity(new Intent(getContext(), DoItYourSelfActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
            });
        } catch (Exception e) {
            RELog.e(e);
        }

    }


    private void checkBatteryOptimizationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                callBatteryOptimizationExemption();
            } else {
                ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS},
                        MY_IGNORE_OPTIMIZATION_PERMISSION);
            }
        } else {
            startAppInfoScreen();
        }
    }

    private boolean isBatteryPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private void callBatteryOptimizationExemption() {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
            startActivityForResult(intent, MY_IGNORE_OPTIMIZATION_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startBatteryOptimizationActivity() {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            startActivityForResult(intent, MY_IGNORE_OPTIMIZATION_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSettingsScreen() {
        try {
            startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAppInfoScreen() {
        try {
            //Open the specific App Info page:
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            RELog.e(e);
            //Open the generic Apps page:
            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public void setRouteDuration(Long longDuration) {
        longDurationGlobal = longDuration;
        long duration = Math.round(longDuration);
        final SpannableStringBuilder sbDuration = new SpannableStringBuilder(String.format(mContext.getResources().getString(R.string.nav_duration),
                REUtils.getDurationInUnits(duration)));
        Typeface mTypeFaceBold = ResourcesCompat.getFont(mContext, R.font.montserrat_bold);
        sbDuration.setSpan((new RECustomTyperFaceSpan(mTypeFaceBold)), 3, sbDuration.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvGetNavDuration.setText(sbDuration);
        manageNavigationLabels(VISIBLE);
        poiRecyclerView.setVisibility(GONE);
        tvToolTip.setVisibility(GONE);
        navConstraint.setVisibility(GONE);
        bottomSheet.setVisibility(VISIBLE);
        strSelectedPOIItem = null;
    }

    @Override
    public void setReconstructedRouteInfo(Location source, Location destination) {
        RELog.e(TAG_NAVIGATION_FRAGMENT, "Destination got it");
        hideLoading();
        String strDestination = REUtils.getCurrentAddressFromLocation(mContext, destination.getLatitude(), destination.getLongitude());
        WayPointsData wayPointsData = new WayPointsData();
        wayPointsData.setLatitude(destination.getLatitude());
        wayPointsData.setLongitude(destination.getLongitude());
        wayPointsData.setPlaceName(strDestination);
        //Clear already selected value and refresh the new value
        WayPointsData origin = mWayPointList.get(0);
        mWayPointList.clear();
        mWayPointList.add(0, origin);
        mWayPointList.add(1, wayPointsData);
        addWayPointFragment.waypointNotifyDataChaged();
        addWayPointFragment.setRouteConstructedMode(mMapFragment.getNavigationMode());
        mMapFragment.assignWayPointDataList(mWayPointList);
    }

    private void setDeepLinkRouteInfo() {
        deeplink_data = REServiceSharedPreference.getDeepLinkData(mContext);
        RELog.e("Jaya", "setDeepLinkRouteInfo called" + deeplink_data);
        REServiceSharedPreference.setDeepLinkData(mContext, "");
        try {
            JSONObject jsonObject = new JSONObject(deeplink_data);
            Log.e("setDeepLinkRouteInfo: ", jsonObject.toString() + "");
            if (jsonObject.getString(REConstants.SHARE_TYPE) != null && jsonObject.getString(REConstants.SHARE_TYPE).equalsIgnoreCase(SHARE_TYPE_TBT_ROUTE)) {
                populateTBTSharedRoute(jsonObject);
            } else if (jsonObject.getString(REConstants.SHARE_TYPE) != null && jsonObject.getString(REConstants.SHARE_TYPE).equalsIgnoreCase(SHARE_TYPE_BCT_ROUTE)) {
                if (jsonObject.getString(REConstants.SHARE_CODE_KEY) != null) {
                    populateBCTSharedToken(jsonObject.getString(REConstants.SHARE_CODE_KEY));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateBCTSharedToken(String sample_token) {
        //Redeem token here
        new FetchTripAsyncTask("share", this).execute(sample_token);
    }

    private void populateTBTSharedRoute(JSONObject jsonObject) {
        //if (!(REServiceSharedPreference.getDeepLinkData(mContext).equals("")) && !(mSourceLat == 0)) {
        showLoading();
        //mMapFragment.enableMyLocation();
        if (bottomSheet != null && bottomSheet.getVisibility() == View.VISIBLE) {
            bottomSheet.setVisibility(GONE);
            navConstraint.setVisibility(VISIBLE);
            mMapFragment.clearMarkers();
        }

        //String deeplink_data = REServiceSharedPreference.getDeepLinkData(mContext);
        //REServiceSharedPreference.setDeepLinkData(mContext, "");
        int destn_index = 1;
        if (mWayPointList != null)
            mWayPointList.clear();
        ArrayList<Location> locn = new ArrayList<>();
        try {
            WayPointsData wayPointsData_origin = new WayPointsData();
            wayPointsData_origin.setLatitude(mSourceLat);
            wayPointsData_origin.setLongitude(mSourceLon);
            wayPointsData_origin.setPlaceName("Origin");
            mWayPointList.add(0, wayPointsData_origin);
            String[] arrSplit_waypoint_text = null;
            String waypoints_names = "";
            if ((jsonObject.getString(REConstants.WAYPOINTS_TEXT) != null) && (!jsonObject.getString(REConstants.WAYPOINTS_TEXT).equals(""))) {
                waypoints_names = jsonObject.getString(REConstants.WAYPOINTS_TEXT);
                if (waypoints_names.contains(";")) {
                    arrSplit_waypoint_text = waypoints_names.split(";");
                }
            }
            if ((jsonObject.getString(REConstants.WAYPOINTS_LOCATION) != null) && (!jsonObject.getString(REConstants.WAYPOINTS_LOCATION).equals(""))) {
                String waypoints = jsonObject.getString(REConstants.WAYPOINTS_LOCATION);
                if (waypoints.contains(";")) {
                    String[] arrSplit_waypoints = waypoints.split(";");
                    for (String arrSplit_waypoint : arrSplit_waypoints) {
                        String[] latlng = arrSplit_waypoint.split(",");
                        String lat = latlng[0];
                        String lng = latlng[1];
                        Location loc = new Location("");
                        loc.setLatitude(Double.parseDouble(lat));
                        loc.setLongitude(Double.parseDouble(lng));
                        locn.add(loc);
                        destn_index = destn_index + 1;
                    }
                } else {
                    String[] latlng = waypoints.split(",");
                    Location loc = new Location("");
                    loc.setLatitude(Double.parseDouble(latlng[0]));
                    loc.setLongitude(Double.parseDouble(latlng[1]));
                    locn.add(loc);
                    destn_index = destn_index + 1;
                }
                if (locn.size() > 1) {
                    for (int i = 0; i < locn.size(); i++) {
                        WayPointsData wap = new WayPointsData();
                        wap.setLatitude(locn.get(i).getLatitude());
                        wap.setLongitude(locn.get(i).getLongitude());
                        if (arrSplit_waypoint_text != null)
                            wap.setPlaceName(arrSplit_waypoint_text[i]);
                        mWayPointList.add(i + 1, wap);
                    }
                } else {
                    WayPointsData wap = new WayPointsData();
                    wap.setLatitude(locn.get(0).getLatitude());
                    wap.setLongitude(locn.get(0).getLongitude());
                    wap.setPlaceName(waypoints_names);
                    mWayPointList.add(1, wap);
                }
            }
            String destn_place = jsonObject.getString(REConstants.DESTINATION_TEXT);
            String destn_loc = jsonObject.getString(REConstants.DESTINATION_LOCATION);
            String[] arrSplit_destn = destn_loc.split(",");
            WayPointsData wayPointsData_detination = new WayPointsData();
            wayPointsData_detination.setLatitude(Double.parseDouble(arrSplit_destn[0]));
            wayPointsData_detination.setLongitude(Double.parseDouble(arrSplit_destn[1]));
            wayPointsData_detination.setPlaceName(destn_place);
            mWayPointList.add(destn_index, wayPointsData_detination);

            configurationPrefManager.setFerries(jsonObject.getString(REConstants.AVOID_FERRIES).equals("1"));
            configurationPrefManager.setTollRoads(jsonObject.getString(REConstants.AVOID_TOLLROADS).equals("1"));
            configurationPrefManager.setHighways(jsonObject.getString(REConstants.AVOID_HIHWAYS).equals("1"));
            addWayPointFragment.updateDeepLinkWaypoints(mWayPointList);
        } catch (JSONException err) {
            RELog.e("Error %s", err.toString());
        }
        hideLoading();
        //}
    }

    @Override
    public void setRouteEngineFailed(String s) {
        hideLoading();
        if (s.equals(REConstants.TBT_API_UNAUTHORIZED)) {
            REUtils.showErrorDialog(mContext,
                    REApplication.getAppContext().getString(R.string.error_nav_unauthorized));
        } else {
            REUtils.showErrorDialog(mContext,
                    REApplication.getAppContext().getString(R.string.sorry_please_try_again));

        }
    }

    @Override
    public void setUpdatedRouteDistance(double distance) {

    }

    @Override
    public void setUpdatedRouteDuration(long duration) {

    }

    @Override
    public void poiItemClicked(String strPoiName, boolean isChecked) {
        if (!isChecked) {
            poiAdapter.setPOIItemHighlighted(-1);
            poiAdapter.notifyDataSetChanged();
            tvToolTip.setVisibility(GONE);
            strSelectedPOIItem = null;
            mMapFragment.adjustMapHeight(getMapBottomPadding());
            mMapFragment.clearMarkers();
            plotMarker(mWayPointList, false, 14);
            return;
        }
        strSelectedPOIItem = strPoiName;
        fetchPOIData(strPoiName);
    }

    @Override
    public void poiItemCheckLocationisEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mMapFragment.enableMyLocation();

            } else {
                //Request Location Permission
                displayNeverAskAgainDialog();
            }
        }
    }

    @Override
    public void onNavigationFilterClick() {
        //Log.e("Navigation", "Selected action item is " + hashMap);
      /*  setSelectedFilterOptions();
        this.hashMap = hashMap;*/
    }

    @Override
    public void onNavigationShare() {
        try {
            Bundle params = new Bundle();
            String aValue = "";
            if (configurationPrefManager.isFerriesEnabled()) {
                aValue += "avoidFerries=1,";
            }
            if (configurationPrefManager.isTollRoad()) {
                aValue += "avoidTollRoads=1,";
            }
            if (configurationPrefManager.isHighwaysEnabled()) {
                aValue += "avoidHighways=1,";
            }
            if (configurationPrefManager.isNightModeEnabled()) {
                aValue += "nightModeEnabled=1,";
            }
            if (configurationPrefManager.getETA()) {
                aValue += "ETAEnabled=1";
            }
            params.putString("eventCategory", REConstants.KEY_TRIPPER_GTM);
            params.putString("eventAction", REConstants.routeShareBtnClicked);
            params.putString("eventLabel", aValue);
            params.putString("modelName", REUtils.getDeviceModel());
            REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
            if (mWayPointList != null && mWayPointList.get(mWayPointList.size() - 1).getLatitude() != 0) {
                if (addWayPointFragment.isEmptyWayPointAdded()) {
                    Toast.makeText(mContext, "Choose Way Point ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!REUtils.isNetworkAvailable(mContext)) {
                    Toast.makeText(mContext, "Please check your internet connectivity!", Toast.LENGTH_SHORT).show();
                    return;
                }
                showLoading();
                StringBuilder waypoints = new StringBuilder();
                StringBuilder waypoints_place = new StringBuilder();
                if (mWayPointList != null && mWayPointList.size() > 2) {
                    for (int iCount = 1; iCount <= mWayPointList.size() - 2; iCount++) {
                        String waypoint = ";" + mWayPointList.get(iCount).getLatitude() + "," + mWayPointList.get(iCount).getLongitude();
                        waypoints.append(waypoint);
                        String waypointnames = ";" + mWayPointList.get(iCount).getPlaceName();
                        waypoints_place.append(waypointnames);
                    }
                }
                //Branchio
                LinkProperties linkProperties = new LinkProperties();
                linkProperties.addControlParameter("$android_url", "https://play.google.com/store/apps/details?id=com.royalenfield.reprime");
                linkProperties.addControlParameter("$always_deeplink", "true");
                ContentMetadata data = new ContentMetadata();
                //origin Text
                /*String originplace = REUtils.getCurrentAddressFromLocation(mContext,mWayPointList.get(0).getLatitude(),mWayPointList.get(0).getLongitude());
                data.addCustomMetadata(REConstants.ORIGIN_TEXT,originplace);*/
                //origin Location
               /* String origin_loc = mWayPointList.get(0).getLatitude()+","+mWayPointList.get(0).getLongitude();
                data.addCustomMetadata(REConstants.ORIGIN_LOCATION,origin_loc);*/
                if (waypoints.length() > 0) {
                    data.addCustomMetadata(REConstants.WAYPOINTS_LOCATION, waypoints.substring(1));
                    //destination Text
                    if (mWayPointList != null) {
                        data.addCustomMetadata(REConstants.DESTINATION_TEXT, mWayPointList.get(mWayPointList.size() - 1).getPlaceName());
                        //destination Location
                        String destn_loc = mWayPointList.get(mWayPointList.size() - 1).getLatitude() + "," + mWayPointList.get(mWayPointList.size() - 1).getLongitude();

                        data.addCustomMetadata(REConstants.DESTINATION_LOCATION, destn_loc);
                    }
                } else {
                    data.addCustomMetadata(REConstants.WAYPOINTS_LOCATION, "");
                    if (mWayPointList != null) {
                        //destination Text
                        data.addCustomMetadata(REConstants.DESTINATION_TEXT, mWayPointList.get(1).getPlaceName());
                        //destination Location
                        String destn_loc = mWayPointList.get(1).getLatitude() + "," + mWayPointList.get(1).getLongitude();
                        data.addCustomMetadata(REConstants.DESTINATION_LOCATION, destn_loc);
                    }
                }
                if (waypoints_place.length() > 0) {
                    data.addCustomMetadata(REConstants.WAYPOINTS_TEXT, waypoints_place.substring(1));
                } else {
                    data.addCustomMetadata(REConstants.WAYPOINTS_TEXT, "");
                }
                if (configurationPrefManager.isFerriesEnabled()) {
                    data.addCustomMetadata(REConstants.AVOID_FERRIES, "1");
                } else {
                    data.addCustomMetadata(REConstants.AVOID_FERRIES, "0");
                }
                if (configurationPrefManager.isTollRoad()) {
                    data.addCustomMetadata(REConstants.AVOID_TOLLROADS, "1");
                } else {
                    data.addCustomMetadata(REConstants.AVOID_TOLLROADS, "0");
                }
                if (configurationPrefManager.isHighwaysEnabled()) {
                    data.addCustomMetadata(REConstants.AVOID_HIHWAYS, "1");
                } else {
                    data.addCustomMetadata(REConstants.AVOID_HIHWAYS, "0");
                }
                data.addCustomMetadata(REConstants.SHARE_TYPE, REConstants.SHARE_TYPE_TBT_ROUTE);
                BranchUniversalObject branchUniversalObject = new BranchUniversalObject().setCanonicalIdentifier(REConstants.CANONICAL_IDENTIFIER).setContentMetadata(data).setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
                branchUniversalObject.generateShortUrl(mContext, linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {
                            deeplink = url;
                            hideLoading();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, deeplink);
                            intent.setType("text/plain");
                            startActivityForResult(Intent.createChooser(intent, "Share route via"), 256);
                        } else {
                            RELog.e(error.toString());
                            mContext.runOnUiThread(() -> {
                                hideLoading();
                                Toast.makeText(mContext, "Not able to Share !!!", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            } else {
                Toast.makeText(mContext, "Enter Destination", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("AppKill", "OndestroyView");
        /*NotificationManager nManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
        nManager.cancelAll();*/
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(aLBReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mconnectionBroadcastReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mAuthBroadcastReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mDynamicCallNotfnBroadcastReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mScanResultsReceiver);
        mWayPointList.clear();
        mSourceLat = 0.0;
        mSourceLon = 0.0;
        //To clear Deeplink data
        REServiceSharedPreference.setDeepLinkData(mContext, "");
        REApplication.getInstance().setNavigationInProgress(false);
        unRegisterConnectionReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("AppKill", "Ondestroy");
        try {
            REPreference.getInstance().putBoolean(mContext, "Is_Muted", false);
        } catch (PreferenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            Log.e("bctpersist: ", "resume btn clicked");
            btnRecordPause.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.pause), null, null, null);
            resumeRecord();
        } else {
            Log.e("bctpersist: ", "pause btn clicked isChecked not");
            btnRecordPause.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.play_test), null, null, null);
            RELog.e(TAG_NAVIGATION_FRAGMENT, "pause called");
            pauseRecord();
        }
    }

    private void pauseRecord() {
        mMapFragment.pauseRecord();
    }

    private void resumeRecord() {
        mMapFragment.resumeRecord();
    }

    @Override
    public void onRENavigationEnded(boolean isRecord) {
        if (mContext != null) {
            BreadcrumbTrail breadcrumbTrail = RENavigationMapFragment.getBreadcrumbTrail();
            if (!isRecord) {
                //If not recording we are reloading the NavigationFragment
                // Using runOnUiThread because while doing fragment backstack issue is there. Need to research
                //BreadcrumbTrail breadcrumbTrail = mMapFragment.getBreadcrumbTrail();
                RELog.e(TAG_NAVIGATION_FRAGMENT, "Recorded BCT values final result breadcrumbTrail = = " + breadcrumbTrail);
                mContext.runOnUiThread(this::reloadFragment);
            } else {
                mContext.runOnUiThread(() -> {
                    btnStartNavigation.setVisibility(View.GONE);
                    btnRecordStart.setVisibility(GONE);
                    btnRecordStop.setVisibility(GONE);
                    btnRecordPause.setVisibility(GONE);
                    btnNavigationShare.setVisibility(VISIBLE);
                    btnNavigationSave.setVisibility(VISIBLE);
                    myReloadBTN.setVisibility(VISIBLE);
                    mNavigationTXT.setVisibility(GONE);
                    mSpeakMuteBTN.setVisibility(GONE);
                    mReCenterBTN.setVisibility(GONE);
                    mCurrentLocBTN.setVisibility(VISIBLE);
                    hideBottomNavaigationTabs(false);

                    //   Toast.makeText(mContext, "" + "Recorded points is: " + breadcrumbTrail.getBreadcrumbs().size(), Toast.LENGTH_LONG).show();
                    if (breadcrumbTrail != null && breadcrumbTrail.getBreadcrumbs() != null && breadcrumbTrail.getBreadcrumbs().size() < 3) {
                        showNoRecordDataAlertDialog();
                    }
                });
            }
            REApplication.getInstance().setNavigationInProgress(false);
        }
    }

    private void showNoRecordDataAlertDialog() {
        try {
            TextView alert_message, alert_ok, alert_title;
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_title = alertLayout.findViewById(R.id.textView_alert_title);
            alert_title.setText(getResources().getString(R.string.customalert_title));
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            alert_message.setText(getResources().getString(R.string.record_stopped_no_data));
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    reloadFragment();
                }
            });
        } catch (Exception e) {
            // Timber.e(e);
        }
    }

    private void onRecordSavedSuccessfully() {
        if (mContext != null) {
            reloadFragment();
        }
    }

    private void onRecordShare(String strTripId) {
        // implemented Branch.io here
        try {
            showLoading();
            //Branchio
            LinkProperties linkProperties = new LinkProperties();
            linkProperties.addControlParameter("$android_url", "https://play.google.com/store/apps/details?id=com.royalenfield.reprime");
            linkProperties.addControlParameter("$always_deeplink", "true");
            ContentMetadata data = new ContentMetadata();

            data.addCustomMetadata(REConstants.SHARE_TYPE, REConstants.SHARE_TYPE_BCT_ROUTE);
            data.addCustomMetadata(REConstants.SHARE_CODE_KEY, strTripId);
            BranchUniversalObject branchUniversalObject = new BranchUniversalObject().setCanonicalIdentifier(REConstants.CANONICAL_IDENTIFIER).setContentMetadata(data).setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
            branchUniversalObject.generateShortUrl(mContext, linkProperties, new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError error) {
                    if (error == null) {
                        deeplink = url;
                        hideLoading();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, deeplink);
                        intent.setType("text/plain");
                        startActivityForResult(Intent.createChooser(intent, "Share BCT route via"), REQUESTCODE_BCT_SHARE_ROUTE);
                    } else {
                        RELog.e(error.toString());
                        mContext.runOnUiThread(() -> {
                            hideLoading();
                            Toast.makeText(mContext, "Not able to Share !!!", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "http://reprime.royalenfield.com/sharedId?" + strTripId);
        intent.setType("text/plain");
        startActivityForResult(Intent.createChooser(intent, "BCT Share via"), REQUESTCODE_SHARE);*/
    }

    /*@Override
    public void setRemainingDistance(Double iDistanceKM) {
        //Calculating travelled distance
        mTravelledDistance = distance - Math.round(iDistanceKM);
    }

    @Override
    public void setRemainigDuration(long remainingduration) {
        //Calculating travelled duration
        mTravelledDuration = duration - Math.round(remainingduration);
    }*/

    @Override
    public void onMarkerClick(Marker marker) {
        addWayPointFragment.clearSearch();
        //This gets called when marker is clicked in MapFragment
        addWayPointFragment.updateWayPoint(marker.getPosition(), marker.getTitle());
    }

    @Override
    public void onMapLongClick(LatLng latLng, String aPlaceName) {
        addWayPointFragment.clearSearch();
        //This gets called when user long press on map
        addWayPointFragment.updateWayPoint(latLng, aPlaceName);
        unCheckPOICategories();
    }

    @Override
    public void onMapPOIClick(LatLng latLng, String aPlaceName) {
        addWayPointFragment.clearSearch();
        //This gets called when user selects any one POI on the map
        addWayPointFragment.updateWayPoint(latLng, aPlaceName);
        unCheckPOICategories();
    }

    @Override
    public void onFetchTripSuccess(Trip trip) {
        BreadcrumbTrail breadcrumbTrail = ConversionExtensionsKt.toBreadcrumbTrail(trip);
        mMapFragment.routeReconstruct(breadcrumbTrail);
    }

    @Override
    public void onFetchTripFailure(String error) {
        hideLoading();
        Toast.makeText(mContext, "Ticket cannot be redeemed by the owner", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTouch() {
        mScrollView.requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void resetPOIOptions() {
        strSelectedPOIItem = null;
        tvToolTip.setVisibility(GONE);
        mMapFragment.adjustMapHeight(getMapBottomPadding());
        setPOIAdapter();
    }

    @Override
    public void onWayPointAdded() {
        mMapFragment.onWayPointAdded();
    }

    @Override
    public void onWayPointRemoved() {
        mMapFragment.onWayPointRemoved();
    }

    @Override
    public void onEmptyWayPointAdded() {
        mMapFragment.disableMarkerDrag();
        addWayPointFragment.disableWayPointTouchListeners();
        safePlotMarkers();
    }

    @Override
    public void onEmptyWayPointRemoved() {
        addWayPointFragment.enableWayPointTouchListeners();
    }

    @Override
    public void onNavigationWaypointRemoved() {
        plotMarker();
    }

    @Override
    public void onExpandControls() {
        mNavigationTXT.setVisibility(GONE);
        mMapFragment.adjustMapHeight(getMapBottomPadding());
    }

    @Override
    public void onCollapseControls() {
        mNavigationTXT.setVisibility(VISIBLE);
        mMapFragment.adjustMapHeight(getMapBottomPadding());
    }

    @Override
    public void clearMap() {
        if (mMapFragment!=null){
            mMapFragment.clearMarkers();
        }

    }

    @Override
    public void enableMarkerListeners() {
        mMapFragment.enableMarkerListeners();
    }

    /**
     *
     */
    private final BroadcastReceiver mconnectionBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("updateconnection".equals(intent.getAction()) && mContext != null) {
                boolean isTripperConnected = intent.getBooleanExtra("connected", false);
                String mDeviceStoredAddress = intent.getStringExtra("deviceaddress");
                mISBLEConnected = isTripperConnected;
                if (!isTripperConnected) {
                    isScanInitiatedConnection = false;
                    REApplication.getInstance().setmConnectedDeviceInfo(new ArrayList<>());
                    setConnectionStatus(false);
                    if (mContext != null) {
                        mStoredDevicesList = BLEDeviceManager.getMyTBTList(mContext);
                        if (mStoredDevicesList.size() > 0) {
                            BLEDeviceManager.updateDisconnectedDeviceStatus(mContext, mDeviceStoredAddress, mStoredDevicesList);
                        }
                    }
                    setDisConnectedImage();
                    if (mMapFragment != null && mMapFragment.isBCTRunning() || mIsStopNavigation) {
                        manager.connect(mDeviceStoredAddress, true);
                        mIsStopNavigation = false;
                    } else {
                        // chk is auto connect enabled ,pass true else pass false
                        if (BLEDeviceManager.isAutoConnectEnabled(mContext)) {
                            mStoredDevicesList = BLEDeviceManager.getMyTBTList(mContext);
                            if (mStoredDevicesList.size() > 0) {
                                for (int i = 0; i < mStoredDevicesList.size(); i++) {
                                    if (mDeviceStoredAddress != null && mStoredDevicesList.get(i).getAddress().equals(mDeviceStoredAddress)) {
                                        try {
                                            if (!(REPreference.getInstance().getBoolean(mContext, "isTripperUnpaired")))
                                                if (!manualDisconnectBase){
                                                manager.connect(mDeviceStoredAddress, true);
                                                }
                                        } catch (PreferenceException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //in TBT 2.0 release we are commenting the call notification feature
                    if (null != mMapFragment)
                        mMapFragment.unregisterTFTCallListener();
                } else {
                    if (mMapFragment != null && mDeviceStoredAddress != null) {
                        mMapFragment.updateCallNotificationAvailability(mDeviceStoredAddress);
                    }
                    isScanInitiatedConnection = true;
                    setConnectionStatus(false);
                }
            }
        }
    };

    private void setConnectionStatus(boolean b) {
        REApplication.getInstance().setIsDeviceConnected(b);
        REApplication.getInstance().setIsDeviceAuthorised(b);
    }

    private void setConnectedImage() {
        if (mContext != null) {
            if (REApplication.getInstance().isNavigationInProgress) {
                // mContext.runOnUiThread(() -> butNavStatusIcon.setBackground(ContextCompat.getDrawable(mContext, R.drawable.green_helmet_ic)));

                mContext.runOnUiThread(() -> {
                    butNavStatusIcon.setBackground(ContextCompat.getDrawable(mContext, R.drawable.green_helmet_ic));
                    imageViewHelmetPip.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.green_helmet_ic));
                    tvPipDeviceDiconnectedInfo.setVisibility(GONE);
                    tvPipInfoText.setVisibility(VISIBLE);
                });
            } else {
                mContext.runOnUiThread(() ->
                        butNavStatusIcon.setBackground(ContextCompat.getDrawable(mContext, R.drawable.orange_helmet_ic))
                );
            }
        }
    }

    private void setDisConnectedImage() {
        if (mContext != null) {
            // mContext.runOnUiThread(() -> butNavStatusIcon.setBackground(ContextCompat.getDrawable(mContext, R.drawable.red_helmet_ic)));
            mContext.runOnUiThread(() -> {
                butNavStatusIcon.setBackground(ContextCompat.getDrawable(mContext, R.drawable.red_helmet_ic));
                imageViewHelmetPip.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.red_helmet_ic));
                tvPipDeviceDiconnectedInfo.setVisibility(VISIBLE);
                tvPipInfoText.setVisibility(GONE);

            });
        }
    }

    /**
     *
     */
    private final BroadcastReceiver mAuthBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("pinAuth".equals(intent.getAction())) {
                boolean b = intent.getBooleanExtra("auth", false);
                String address = intent.getStringExtra("deviceAddress");
                if (!b) {
                    REApplication.getInstance().setNavigationInProgress(false);
                    REApplication.getInstance().setmConnectedDeviceInfo(new ArrayList<>());
                    setConnectionStatus(false);
                    setDisConnectedImage();
                    //in TBT 2.0 release we are commenting the call notification feature
                    if (null != mMapFragment)
                        mMapFragment.unregisterTFTCallListener();
                } else {
                    if (mMapFragment != null && address != null) {
                        mMapFragment.updateCallNotificationAvailability(address);
                    }
                    List<DeviceInfo> list = new ArrayList<>();
                    assert address != null;
                    list.add(new DeviceInfo("ANDROID", "N", true, true,
                            intent.getStringExtra("deviceName"), address, 0, "", "", "", ""));
                    REApplication.getInstance().setmConnectedDeviceInfo(list);
                    setConnectionStatus(true);
                    if (mContext != null) {
                        mStoredDevicesList = BLEDeviceManager.getMyTBTList(mContext);
                        if (mStoredDevicesList.size() > 0) {
                            BLEDeviceManager.updateConnectedDeviceStatus(mContext,
                                    address, mStoredDevicesList);
                        }
                    }
                    setConnectedImage();
                    //in TBT 2.0 release we are commenting the call notification feature
                    if (null != mMapFragment)
                        mMapFragment.registerTFTCallListener();
                }
            }
        }
    };
    private final BroadcastReceiver mDynamicCallNotfnBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("DynamicCallNotifyToStart".equals(intent.getAction())) {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Tripper");
                params.putString("eventAction", "Call Notification Received");
                REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

                String tripperMacAddress = intent.getStringExtra("TripperMacAddress");
                if (mMapFragment != null && tripperMacAddress != null) {
                    mMapFragment.updateCallNotificationAvailability(tripperMacAddress);
                }
            }
        }
    };
    /**
     *
     */
    private final BroadcastReceiver mScanResultsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BLEConstants.BLE_INTENT_SCAN_RESUlTS.equals(intent.getAction())) {
                DeviceInfo deviceInfo = new DeviceInfo("ANDROID", "N", false, false,
                        intent.getStringExtra(
                                BLEConstants.BLE_DEVICE_NAME), Objects.requireNonNull(intent.
                        getStringExtra(BLEConstants.BLE_DEVICE_ADDRESS)), 0, "", "", "", "");
                onLeScanResult(deviceInfo);
            }
        }
    };

    @Override
    public void setAuthenticationKey(String authKey) {
        hideLoading();
        if (authKey != null) {
            try {
                REPreference.getInstance().putString(mContext, "Azure_Key", authKey);
            } catch (PreferenceException e) {
                RELog.e(e);
            }
            RELog.e("TBTAuthKey %s", authKey);
        }
        addMapFragment();
    }

    @Override
    public void authkeyfailure() {
        hideLoading();
        addMapFragment();
    }

    private void getTBTAuthenticationKey() {
        showLoading();
        ReNavigationPresenter reNavigationPresenter = new ReNavigationPresenter(new RENavigationInteractor(), this);
        TbtAuthRequest tbtAuthRequest = new TbtAuthRequest(guid);
        reNavigationPresenter.getTbtAuthkey(tbtAuthRequest, this);
    }

    private void registerConnectionReceiver() {
        mContext.registerReceiver(myConnectionReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    private void unRegisterConnectionReceiver() {
        mContext.unregisterReceiver(myConnectionReceiver);
    }


    /**
     * Broadcast Receiver for bluetooth connection state changes
     */
    private final BroadcastReceiver myConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // It means the user has changed his bluetooth state.
            if (action != null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                //      Toast.makeText(mContext," BT State is : "+mBluetoothAdapter.getState(),Toast.LENGTH_LONG).show();
                if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    initUART();
                    if (isNavHelmetCLicked) {
                        openBleSearchActivity();
                    } else {
                        scanDevice();
                    }
                }

                if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF && BLEModel.getInstance().getBluetoothGatt() != null) {
                    manager.onBluetoothDisconnected(BLEModel.getInstance().getBluetoothGatt(), 8, 0);
                    //Log.e("BLESTATE",""+mBluetoothAdapter.getState());

                }
            }
        }
    };

    @Override
    public void updatePOIMarker(Marker aMarker, BitmapDescriptor bitmapDescriptor) {
        if (poiSearchResults != null && poiSearchResults.size() > 0) {
            for (SearchResult searchResult : poiSearchResults) {
                PoiSearchResult poiResult = (PoiSearchResult) searchResult;
                LatLng latLng = new LatLng(Objects.requireNonNull(poiResult.getAddress().component1()).getLatitude(),
                        Objects.requireNonNull(poiResult.getAddress().component1()).getLongitude());
                String poiName = ((PoiSearchResult) searchResult).getPoiName();
                if (latLng.latitude == aMarker.getPosition().latitude
                        && latLng.longitude == aMarker.getPosition().longitude) {
                    mMapFragment.showPOIMarker(latLng, poiName, bitmapDescriptor, true);
                } else {
                    mMapFragment.showPOIMarker(latLng, poiName, categoryMarker, false);
                }
            }
        } else {
            Toast.makeText(mContext, "Result Not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapMarkerDrag(LatLng latLng, String aPlaceName) {
        //This gets called when marker is dragged in MapFragment
        addWayPointFragment.updateDragWayPoint(latLng, aPlaceName);
    }

    @Override
    public void plotMarker() {
        mMapFragment.enableMarkerDrag();
        plotMarker(mWayPointList, false, 18);
        enableMarkerListeners();
    }

    private void safePlotMarkers() {
        mMapFragment.clearMarkers();
        safePlotMarkers(mWayPointList, false, 0);
    }

    /**
     * Plot the previous added way points to the map
     *
     * @param mWayPointList
     */
    private void plotMarker(ArrayList<WayPointsData> mWayPointList, boolean isClearMarkers, int camZoom) {
        try {
            if (null != mWayPointList && mWayPointList.size() > 0) {
                for (int i = 0; i < mWayPointList.size(); i++) {
                    WayPointsData wayPointsData = mWayPointList.get(i);
                    if (wayPointsData.getLatitude() != 0 && !wayPointsData.getPlaceName().equals("Origin")) {
                        LatLng latLng = new LatLng(wayPointsData.getLatitude(),
                                wayPointsData.getLongitude());
                        //plot the marker in the map
                        boolean isDestination, isZoom = false;
                        isDestination = i == mWayPointList.size() - 1 || mWayPointList.size() == 2;
                        if (addWayPointFragment.isDragMode()) {
                            //Zoom applicable for all the waypoint dragged
                            if (i == addWayPointFragment.getDragPosition())
                                isZoom = true;
                        } else {
                            //Zoom applicable for last waypoint or only destination in the list by long press
                            if (i == mWayPointList.size() - 2 || mWayPointList.size() == 2)
                                isZoom = true;
                            //Zoom applicable for all the waypoints added in search mode
                            if (addWayPointFragment.isSearchMode() && i == addWayPointFragment.getWaypointPosition())
                                isZoom = true;
                        }
                        mMapFragment.plotMarker(latLng, isClearMarkers, isDestination, camZoom, isZoom);
                    } else {
                        mMapFragment.clearMarkers();
                    }
                }
            }
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void safePlotMarkers(ArrayList<WayPointsData> mWayPointList, boolean isClearMarkers, int camZoom) {
        try {
            if (null != mWayPointList && mWayPointList.size() > 0) {
                for (int i = 0; i < mWayPointList.size(); i++) {
                    WayPointsData wayPointsData = mWayPointList.get(i);
                    if (wayPointsData.getLatitude() != 0 && !wayPointsData.getPlaceName().equals("Origin")
                            && !wayPointsData.getPlaceName().equals("Add Waypoint")) {
                        LatLng latLng = new LatLng(wayPointsData.getLatitude(),
                                wayPointsData.getLongitude());
                        //plot the marker in the map
                        boolean isDestination;
                        isDestination = i == mWayPointList.size() - 1 || mWayPointList.size() == 2;
                        mMapFragment.plotMarker(latLng, isClearMarkers, isDestination, camZoom, false);
                    }
                }
            }
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void showCalibrationPopup() {
        try {
            if (mCompassDialog != null && mCompassDialog.isShowing())
                return;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog);
            View layout = getLayoutInflater().inflate(R.layout.layout_calibration_popup, null);
            TextView mDoneTXT;
            ImageView mAnimationIMG;
            mAccuracyTXT = layout.findViewById(R.id.calibrate_popup_accuracy_TXT);
            mDoneTXT = layout.findViewById(R.id.calibrate_popup_done_TXT);
            mAnimationIMG = layout.findViewById(R.id.calibrate_popup_IMG);
            RequestBuilder<GifDrawable> requestBuilder = Glide.with(layout.getContext())
                    .asGif()
                    .load(R.drawable.calibration_animation);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_image_loading)
                    .error(R.drawable.no_image_found)
                    .priority(Priority.HIGH);
            requestBuilder
                    .apply(options)
                    .into(mAnimationIMG);
            builder.setView(layout);
            mCompassDialog = builder.create();
            mCompassDialog.setCancelable(false);
            mDoneTXT.setOnClickListener(v -> {
                mCompassDialog.dismiss();
            });
            new Handler().postDelayed(() -> {
                showCalibratedAccuracy(mSensorAccuracy, mAccuracyColor);
                mCompassDialog.show();
            }, 500);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void showCalibratedAccuracy(String accuracy, int color) {
        this.mSensorAccuracy = accuracy;
        this.mAccuracyColor = color;
        try {
            String text = "<font color=" + ContextCompat.getColor(mContext, R.color.black_light) + ">"
                    + getString(R.string.text_calibrate_accuracy) + " "
                    + "</font> <font color=" + ContextCompat.getColor(mContext, color) + "><b>"
                    + accuracy + "</b></font>";
            if (mAccuracyTXT != null)
                mAccuracyTXT.setText(Html.fromHtml(text));
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    /**
     * Called when there is a new sensor event.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
    }

    /**
     * Called when the accuracy of the registered sensor has changed.  Unlike
     * onSensorChanged(), this is only called when this accuracy value changes.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            switch (accuracy) {
                case SENSOR_STATUS_ACCURACY_HIGH:
                    showCalibratedAccuracy(SENSOR_ACCURACY_HIGH, R.color.green);
                    setCompassVisibility(false);
                    break;
                case SENSOR_STATUS_ACCURACY_MEDIUM:
                    showCalibratedAccuracy(SENSOR_ACCURACY_MEDIUM, R.color.light_green);
                    setCompassVisibility(true);
                    break;
                case SENSOR_STATUS_ACCURACY_LOW:
                    showCalibratedAccuracy(SENSOR_ACCURACY_LOW, R.color.red);
                    setCompassVisibility(true);
                    break;
                case SENSOR_STATUS_UNRELIABLE:
                    showCalibratedAccuracy(SENSOR_UNRELIABLE, R.color.red);
                    setCompassVisibility(true);
                    break;
                default:
                    break;
            }
        }
    }

    private void setCompassVisibility(boolean isVisible) {
        if (isVisible)
            mNavCompassBTN.setVisibility(VISIBLE);
        else
            mNavCompassBTN.setVisibility(GONE);
    }

    @Override
    public void unCheckPOICategories() {
        if (poiAdapter != null) {
            poiAdapter.setPOIItemHighlighted(-1);
            poiAdapter.notifyDataSetChanged();
            tvToolTip.setVisibility(GONE);
            strSelectedPOIItem = null;
            mMapFragment.adjustMapHeight(getMapBottomPadding());
        }
    }

    @Override
    public void changePipWindowText() {
        mContext.runOnUiThread(() -> tvPipInfoText.setText("Arrived at Destination"));
    }

    @Override
    public int getMapBottomPadding() {
        int toolTipHeight = 150;//Original height
        int poiCategoryHeight = poiRecyclerView.getHeight();//Original height
        int navigationTXTHeight = 165 + 20;//Original height + padding
        if (tvToolTip.getVisibility() == GONE)
            toolTipHeight = 10;
        if (poiRecyclerView.getVisibility() == GONE)
            poiCategoryHeight = 10;
        if (mNavigationTXT.getVisibility() == GONE)
            navigationTXTHeight = 10;
        return poiCategoryHeight + navConstraint.getHeight() + toolTipHeight + navigationTXTHeight;
    }

    @Override
    public String getSelectedPOIItem() {
        return strSelectedPOIItem;
    }

    @Override
    public double getStartDragMarkerTag() {
        return mMapFragment.getStartDragMarkerTag();
    }

    @Override
    public double getEndDragMarkerTag() {
        return mMapFragment.getEndDragMarkerTag();
    }

    private void loadDrawableImage(int resourceId, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide.with(mContext)
                .asDrawable()
                .load(resourceId);
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())));
        requestBuilder
                .apply(options)
                .into(imageView);
    }

    @Override
    public void showNavigationRerouteMessage(boolean isVisible) {
        if (isVisible)
            mNavigationReRouteTXT.setVisibility(VISIBLE);
        else
            mNavigationReRouteTXT.setVisibility(GONE);
    }

    @Override
    public void showDebugMessage(String text) {
        // mNavigationTXT.setText(text);
    }

    @Override
    public boolean isMapRedrawing() {
        return mMapFragment.isMapRedrawing();
    }

    private String getBatteryOptimizationMessage(String message) {
        switch (REUtils.getManufacturerName()) {
            case OPPO:
                message = getString(R.string.text_battery_optimization_oppo);
                break;
            case ONEPLUS:
                message = getString(R.string.text_battery_optimization_oneplus);
                break;
            case VIVO:
                message = getString(R.string.text_battery_optimization_vivo);
                break;
            case XIAOMI:
                message = getString(R.string.text_battery_optimization_xiaomi_redmi);
                break;
            case MOTO:
            case SAMSUNG:
            case HUAWEI:
            case REALME:
                //default message.
                message = getString(R.string.text_battery_optimisation);
                break;
        }
        //if nothing matches send default message.
        return message;
    }

    private boolean isDirectBatteryOptimizationSpecific() {
        switch (REUtils.getManufacturerName()) {
            case OPPO:
            case ONEPLUS:
            case VIVO:
            case XIAOMI:
            case MOTO:
            case SAMSUNG:
            case HUAWEI:
            case REALME:
                return true;
            default:
                return false;
        }
    }

    private boolean isSettingSpecificBatteryOptimization() {
        switch (REUtils.getManufacturerName()) {
            case VIVO:
                return true;
            default:
                return false;
        }
    }

    private boolean isAppInfoSpecificBatteryOptimization() {
        switch (REUtils.getManufacturerName()) {
            case OPPO:
            case ONEPLUS:
            case XIAOMI:
                return true;
            default:
                return false;
        }
    }

    private int getBatteryOptimizationImage() {
        int image;
        switch (REUtils.getManufacturerName()) {
            case OPPO:
                image = R.drawable.battery_setting_oppo;
                break;
            case ONEPLUS:
                image = R.drawable.battery_setting_oneplus;
                break;
            case VIVO:
                image = R.drawable.battery_setting_vivo_setting;
                break;
            case XIAOMI:
                image = R.drawable.battery_setting_redmi;
                break;
            default:
                image = R.drawable.no_image_found;
        }
        return image;
    }

    private void hideBottomNavaigationTabs(boolean val) {
        Activity activity = getActivity();
        if (activity instanceof REHomeActivity) {
            REHomeActivity myactivity = (REHomeActivity) activity;
            myactivity.mShowOrHideBottomNavigation(val);
        }
    }

    private void handleUIForPiPMode(boolean value) {
        /*getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);*/

        if (tvPipInfoText.getText().toString().equalsIgnoreCase("Arrived at Destination")) {
            stopNavigation();
        }
        tvPipInfoText.setText(getResources().getString(R.string.nav_msg));

        Activity activity = getActivity();
        if (activity instanceof REHomeActivity) {
            REHomeActivity myactivity = (REHomeActivity) activity;
            myactivity.mShowOrHideActionbaritems(value);
        }

        RENavigationRootFragment.getInstance().showorHideTitle(value);
        mScrollView.setVisibility(value ? GONE : VISIBLE);
        navigation_PipLayout.setVisibility(value ? VISIBLE : GONE);
        //send broadcast to notify pipmode
        if (value) {
            Intent appInPipMode = new Intent("app_in_pipmode");
            //appInPipMode.putExtra("type", mDisconnectType);
            LocalBroadcastManager.getInstance(REApplication.getAppContext()).sendBroadcast(appInPipMode);
        }
           /* mReCenterBTN.setVisibility(value ? GONE:VISIBLE);
            mSpeakMuteBTN.setVisibility(value ? GONE:VISIBLE);
            mStopNavigationButton.setVisibility(value ? GONE:VISIBLE);
            navigationShareFragmentFrame.setVisibility(value ? GONE:VISIBLE);
            mWaypointFragmentFrame.setVisibility(value ? GONE:VISIBLE);
            poiRecyclerView.setVisibility(value ? GONE:VISIBLE);
            butNavStatusIcon.setVisibility(value ? GONE:VISIBLE);
            tvGetNavDuration.setVisibility(value ? GONE:VISIBLE);
            tvGetNavDistance.setVisibility(value ? GONE:VISIBLE);
            //manageNavigationLabels(value ? GONE: VISIBLE);
            mMapFragment.resetUserGesture();
            mMapFragment.onCurrentLocationClick();*/

    }

    @Override
    public void getTripperDeviceListFromApiSuccess(Data response) {
        // here we will get the List of Tripper Devices from Api
        // Log.e("TRIPPERAPIGET", "" + response.toString());
        saveTripperDeviceListFromApitoPreference(response);
    }

    private void saveTripperDeviceListFromApitoPreference(Data response) {
        boolean isMyTripperDeviceConnected = false;
        //boolean isMyTripperDeviceConnected = REApplication.getInstance().ismIsDeviceConnected();
        ArrayList<DeviceInfoApiItems> deviceInfoApiItemsActive = response.getActive();
        ArrayList<DeviceInfoApiItems> deviceInfoApiItemsBricked = response.getBricked();
        List<DeviceInfo> deviceInfoList = new ArrayList<>();
        //case Active tripper
        if (deviceInfoApiItemsActive != null && deviceInfoApiItemsActive.size() > 0) {
            for (int i = 0; i < deviceInfoApiItemsActive.size(); i++) {
                DeviceInfo deviceInfo = new DeviceInfo(deviceInfoApiItemsActive.get(i).getMobileDeviceOS(), deviceInfoApiItemsActive.get(i).getBrickStatus(), true, isMyTripperDeviceConnected, deviceInfoApiItemsActive.get(i).getName(), deviceInfoApiItemsActive.get(i).getTripperID()
                        , 0, deviceInfoApiItemsActive.get(i).getDeviceToken(), deviceInfoApiItemsActive.get(i).getSerialNumber(), deviceInfoApiItemsActive.get(i).getUserGUID(), deviceInfoApiItemsActive.get(i).getOSVersion());
                deviceInfoList.add(deviceInfo);
            }
        }
        //case Bricked tripper
        if (deviceInfoApiItemsBricked != null && deviceInfoApiItemsBricked.size() > 0) {
            for (int i = 0; i < deviceInfoApiItemsBricked.size(); i++) {
                if (deviceInfoApiItemsBricked.get(i).getMobileDeviceOS() != null && deviceInfoApiItemsBricked.get(i).getMobileDeviceOS().equalsIgnoreCase("ANDROID")) {
                    DeviceInfo deviceInfo = new DeviceInfo(deviceInfoApiItemsBricked.get(i).getMobileDeviceOS(), deviceInfoApiItemsBricked.get(i).getBrickStatus(), true, isMyTripperDeviceConnected, deviceInfoApiItemsBricked.get(i).getName(), deviceInfoApiItemsBricked.get(i).getTripperID()
                            , 0, deviceInfoApiItemsBricked.get(i).getDeviceToken(), deviceInfoApiItemsBricked.get(i).getSerialNumber(), deviceInfoApiItemsBricked.get(i).getUserGUID(), deviceInfoApiItemsBricked.get(i).getOSVersion());
                    deviceInfoList.add(deviceInfo);
                }
            }
        }
        //multidevice whitelisting fix
        List<DeviceInfo> connectedDeviceList = REApplication.getInstance().getmConnectedDeviceInfo();
        if (connectedDeviceList != null && connectedDeviceList.size() > 0) {
            for (int i = 0; i < deviceInfoList.size(); i++) {
                if (deviceInfoList.get(i).getAddress().equalsIgnoreCase(connectedDeviceList.get(0).getAddress())) {
                    deviceInfoList.get(i).setConnected(REApplication.getInstance().ismIsDeviceConnected());
                }
            }
        }
        //save Tripper list
        BLEDeviceManager.updateMyTBTDevice(deviceInfoList, mContext);
    }

    @Override
    public void getTripperDeviceListFromApiFailed() {
        // Log.e("TRIPPERAPIGET", "Failed to get list");
    }

    public String getSummaryApiTimeFormat(Date value) {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String dateStr = formatter.format(value);
            return dateStr;
        } catch (Exception e) {
            return "";
        }
    }

    public Date getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        Date value = calendar.getTime();
        return value;
    }

    public RecordingOptionSelectionState getRecordingOptionSelectionState() {
        if (mMapFragment.chosenRoute != null) {
            Double routeDistanceinKiloMeter = mMapFragment.chosenRoute.getDistance().toKilometers();
            if (routeDistanceinKiloMeter >= kLimitForOnlyIntelligentRecording) {
                return RecordingOptionSelectionState.ONLY_INTELLIGENT_RECORDING;
            } else {
                return RecordingOptionSelectionState.DEEP_RECORDING;
            }
        } else {
            return RecordingOptionSelectionState.DEEP_RECORDING;
        }
    }

    public void showRecordOptionDialog(RecordingOptionSelectionState state) {
        try {
            TextView alert_message, alert_ok, alert_title, alert_cancel;
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_record_option_alert, null);
            alert_title = alertLayout.findViewById(R.id.textView_record_alert_title);
            alert_title.setText(getResources().getString(R.string.record_popup_heading));
            alert_message = alertLayout.findViewById(R.id.textView_record_alert_message);
            //setting message for alert dialog
            //alert_message.setText("please select the type of Recording");
            alert_ok = alertLayout.findViewById(R.id.textView_record_alert_okbutton);
            alert_cancel = alertLayout.findViewById(R.id.textView_alert_cancel_button);
            radioButtonDeepRecord = alertLayout.findViewById(R.id.radio_deeprecord);
            radioButtonIntelligentRecord = alertLayout.findViewById(R.id.radio_intelligentrecord);
            radioButtonIntelligentRecord.setChecked(true);
            radioButtonDeepRecord.setText(getResources().getString(R.string.deep_recording_radio_text));
            radioButtonIntelligentRecord.setText(getResources().getString(R.string.standard_recording));
            TextView termsinfo = alertLayout.findViewById(R.id.record_termsandcondition_info);
            termsinfo.setText(getResources().getString(R.string.terms_info));
            CheckBox termsandConditionCheckBox = alertLayout.findViewById(R.id.record_terms_checkBox);
            TextView terms_and_condition_text = alertLayout.findViewById(R.id.terms_and_condition_text);
            alert_ok.setAlpha(0.3f);
            // update ui for record option selection

            String messageText = getResources().getString(R.string.deep_recording_info_msg);
            Double limit = (kLimitForOnlyIntelligentRecording * 1_000);//meter - 60,000.0
            String distanceText = "";
            if (isMilesUnitSelected) {
                Distance m = new Distance(limit, LengthUnit.METERS);
                distanceText = Math.round(m.toMiles()) + "mi";
            } else {
                Distance m = new Distance(limit, LengthUnit.METERS);
                distanceText = Math.round(m.toKilometers()) + "km";
            }
            try {
                if (REPreference.getInstance().getString(REApplication.getAppContext(), REConstants.KEY_COMMA_OR_POINT,
                        REConstants.KEY_POINT).equalsIgnoreCase(REConstants.KEY_COMMA)) {
                    distanceText = distanceText.replace(".", ",");
                }
            } catch (PreferenceException e) {
                e.printStackTrace();
            }

            radioButtonDeepRecord.setText(getResources().getString(R.string.deep_recording_radio_text) + " (Max upto " + distanceText + ")");


            switch (state) {
                case DEEP_RECORDING:
                    radioButtonDeepRecord.setChecked(false);
                    radioButtonDeepRecord.setEnabled(true);
                    radioButtonIntelligentRecord.setChecked(true);

                    // alert_message.setText(getResources().getString(R.string.deep_recording_info_msg) + " " + distanceText);
                    break;
                case ONLY_INTELLIGENT_RECORDING:
                    radioButtonDeepRecord.setChecked(false);
                    radioButtonIntelligentRecord.setChecked(true);
                    radioButtonDeepRecord.setEnabled(false);
                    alert_message.setText("");
                    break;
            }


            radioButtonDeepRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        recordType = "Trail";
                        Bundle params1 = new Bundle();
                        params1.putString("eventCategory", "Tripper");
                        params1.putString("eventAction", "Trail record selected");
                        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params1);
                        Log.e("showRecordOptionDialog: ", params1.toString());
                    }
                }
            });
            radioButtonIntelligentRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        recordType = "Intelligent";
                        Bundle params2 = new Bundle();
                        params2.putString("eventCategory", "Tripper");
                        params2.putString("eventAction", "Intelligent record selected");
                        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params2);
                        Log.e("showRecordOptionDialog: ", params2.toString());
                    }
                }
            });


            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            if (radioButtonIntelligentRecord.isChecked()) {
                recordType = "Intelligent";
            } else {
                recordType = "Trail";
            }

            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (termsandConditionCheckBox.isChecked()) {
                        mMapFragment.configureRecordingOption(radioButtonDeepRecord.isChecked());
                        startRecord();
                        dialog.dismiss();

                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Tripper");
                        params.putString("eventAction", "ok - record type selection popup");
                        params.putString("eventLabel", recordType + " record");
                        REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
                        Log.e("showRecordOptionDialog: ", params.toString());

                    } else {
                        Toast.makeText(getActivity(), "Please select Terms and Conditions", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alert_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Tripper");
                    params.putString("eventAction", "cancel - record type selection popup");
                    params.putString("eventLabel", recordType + " record");
                    REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
                    Log.e("showRecordOptionDialog: ", params.toString());
                    dialog.dismiss();
                }
            });
            termsandConditionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Tripper");
                        params.putString("eventAction", "Selected the bct terms and conditions");
                        REUtils.logGTMEvent("Tripper", params);
                        alert_ok.setAlpha(1f);
                    } else {
                        alert_ok.setAlpha(0.3f);
                    }
                }
            });

            terms_and_condition_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent termsIntent = new Intent(getActivity(), RideTermsAndConditions.class);
                    startActivity(termsIntent);

                }
            });

        } catch (Exception e) {
            // Timber.e(e);
        }
    }
}
