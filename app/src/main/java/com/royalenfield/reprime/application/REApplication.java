package com.royalenfield.reprime.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.widget.Toast;

import com.royalenfield.bluetooth.ble.DeviceInfo;
import com.royalenfield.reprime.ui.onboarding.LogResponse;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.royalenfield.reprime.utils.RELog;

import androidx.annotation.NonNull;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.bosch.softtec.components.theseus.BreadcrumbTrailManager;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.remoteconfig.internal.DefaultsXmlParser;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.models.response.remoteconfig.RemoteConfigData;

import com.royalenfield.firebase.fireStore.OnFirestoreCallback;
import com.royalenfield.reprime.rest.connected.REConnectedAPI;
import com.royalenfield.bluetooth.ble.DeviceInfo;
import com.bosch.softtec.components.nephele.CloudManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neolane.android.v1.Neolane;
import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.GetCustomTokenModel;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.UserSettingFirestoreModel;
import com.royalenfield.reprime.models.response.proxy.auth.DMSLoginResponse;
import com.royalenfield.reprime.models.response.proxy.servicebooking.ServiceBookingResponse;
import com.royalenfield.reprime.models.response.proxy.serviceslot.PickupandDoorstepServiceSlot;
import com.royalenfield.reprime.models.response.proxy.serviceslot.ServiceSlotListResponse;
import com.royalenfield.reprime.models.response.proxy.serviceslot.ServiceSlotResponse;
import com.royalenfield.reprime.models.response.proxy.userdetails.DMSProfileData;
import com.royalenfield.reprime.models.response.proxy.vehicledetails.DMSVehicleDetailsResponse;
import com.royalenfield.reprime.models.response.proxy.vehicleserviceinprogresslist.VehicleServiceProgressListResponse;
import com.royalenfield.reprime.models.response.remoteconfig.Feature;
import com.royalenfield.reprime.models.response.web.login.LoginResponse;
import com.royalenfield.reprime.models.response.web.profile.ProfileData;
import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;
import com.royalenfield.reprime.network.listener.INetworkStateListener;
import com.royalenfield.reprime.network.receiver.NetworkConnectionReceiver;
import com.royalenfield.reprime.notification.listener.PhoneStateListener;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.rest.googlemaps.REGoogleMapAPI;
import com.royalenfield.reprime.rest.web.REWebsiteAPI;
import com.royalenfield.reprime.rest.webservice.RENotificationAPI;
import com.royalenfield.reprime.rest.webservice.REServiceAPI;
import com.royalenfield.reprime.rest.youtube.REYoutubeAPI;
import com.royalenfield.reprime.ui.home.navigation.activity.CrashHandler;
import com.royalenfield.reprime.ui.home.navigation.activity.CustomUncaughtExceptionHandler;
import com.royalenfield.reprime.ui.home.service.specificissue.model.IssuesModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.Pincode;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RELocation;
import com.royalenfield.reprime.utils.REUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.branch.referral.Branch;
import com.royalenfield.reprime.utils.RELog;
import com.salesforce.marketingcloud.MCLogListener;
import com.salesforce.marketingcloud.MarketingCloudConfig;
import com.salesforce.marketingcloud.MarketingCloudSdk;
import com.salesforce.marketingcloud.notifications.NotificationCustomizationOptions;
import com.salesforce.marketingcloud.sfmcsdk.InitializationStatus;
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk;
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdkModuleConfig;
import com.salesforce.marketingcloud.sfmcsdk.components.logging.LogLevel;
import com.salesforce.marketingcloud.sfmcsdk.components.logging.LogListener;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.royalenfield.reprime.utils.REConstants.APP_VERSION_DATA;
import static com.royalenfield.reprime.utils.REConstants.COUNTRY_KEY;
import static com.royalenfield.reprime.utils.REConstants.JWT_TOKEN_KEY;
import static com.royalenfield.reprime.utils.REConstants.REMOTE_DATA;
import static com.royalenfield.reprime.utils.REConstants.REMOTE_VERSION;
import static com.royalenfield.reprime.utils.REConstants.USERDATA;


/**
 * @author BOP1KOR on 12/7/2018.
 * <p>
 * Application class.
 */
public class REApplication extends Application implements LifecycleObserver , OnMapsSdkInitializedCallback {
    //Singleton application instance.
    public static boolean CODE_STUB = false;
    public static boolean CODE_STUB_DEMO = false;
	public String flow="guest";
	private REConnectedAPI mREConnectedApiInstance;

    private static REApplication mInstance;
    private static Context appContext;
    private static LruCache<String, Bitmap> mMemoryCache;
    private static FirebaseDatabase firebaseInstance;
    public static FirebaseFirestore mFireStoreInstance;
    public static FirebaseAnalytics mFirebaseAnalytics;
    public FirebaseAuth mFirebaseAuth;
    private REWebsiteAPI mREWebsiteApiInstance;
    private REServiceAPI mREServiceApiInstance;
    private RENotificationAPI mRENotificationApiInstance;
    private REYoutubeAPI mREYoutubeAPI;
    //DMS Api login auth response
    private DMSLoginResponse mDmsLoginResponse;
    private DMSProfileData mProfileData;
    private List<ServiceBookingResponse> mServiceBookingResponse;
    private List<ServiceSlotListResponse> mServiceSlotResponse;
    private List<DMSVehicleDetailsResponse> mDMSVehicleDetails;
    private List<DealerMasterResponse> mDealersResponse;
    private List<PickupandDoorstepServiceSlot> mPickupandDoorstepServiceSlot;
    //Issues selected response
    private ArrayList<IssuesModel> mSelectedIssues;
    //Issues description
    private String userDescribedIssues;
    private Map<String, String> mapUserDescribedIssues = new HashMap<>();
    private ArrayList<Map<String, String>> listOfUserDescribedIssues = new ArrayList<>();
    // create map to store
    private Map<String, ArrayList<IssuesModel>> issueModelStore = new LinkedHashMap<>();
    //Web USer ProfileData
    private ProfileData mUserProfileData;
    private RELocation mRELocationInstance;
    private long dmsLoginTokenStartTime = 0;
    private List<VehicleServiceProgressListResponse> mServiceProgressListResponse;
    private INetworkStateListener connectionListener;
    private NetworkConnectionReceiver mConnectivityReceiver;
    //App foreground status.
    private boolean mIsForeground;
    //TO avoid null returns, initialize with empty values
    private String strDescriptionValue = "";

    private String mServiceCenterNo;
    private boolean comingFromVehicleOnboarding = false;

    private AddVehicleResponse addVehicleResponse;
    private boolean isLoggedInUser = false;
    private boolean isLoggerFileCreated = false;
    private REGoogleMapAPI mREGoogleMapAPI;
    private UserSettingFirestoreModel userSettingFirestoreModel;
    //This boolean value is used for profile image cache flag
    private boolean mIsProfileImageCached = false;
    private List<DeviceInfo> mConnectedDeviceInfo;
    private boolean mIsDeviceConnected = false;
    private boolean mIsDeviceAuthorised = false;
    private CloudManager cloudManager;
    private GetCustomTokenModel getCustomTokenModel;
    public String JWTToken = null;
    private RemoteConfigData remoteConfigData;
    public static Gson mGson;
    public boolean isNavigationInProgress = false;
    public boolean isCheckedOnce=false;
    public String Country = REConstants.COUNTRY_INDIA;
    public Feature featureCountry;
    public Feature defaultFeatures;
    public Pincode validMobile;
    public Pincode validPin;
    public boolean isConnectedEnabled=true;
    public boolean isWanderlustEnabled=false;
    public boolean isTripsEnabled=false;
    private PhoneStateListener phoneStateListener;
    private boolean isIncomingCall = false, isCallAccepted = false;
    public String healthAlertTimeStamp;
    public String secret;
    private String key;
	private ConcurrentHashMap<String, Trace> traces = new ConcurrentHashMap<>();

    private BreadcrumbTrailManager breadcrumbTrailManager;
    private CrashHandler crashHandler;
	public static FirebasePerformance mFirebasePerformance;
public Trace trace;
	public ArrayList<LogResponse> arrayListResponse = new ArrayList<LogResponse>();


    public void setUserSettingFirestoreModel(UserSettingFirestoreModel userSettingFirestoreModel) {
        this.userSettingFirestoreModel = userSettingFirestoreModel;
    }

    /**
     * Returns the application instance.
     *
     * @return {@link Application} instance.
     */
    public static REApplication getInstance() {
        if (mInstance == null) {
            mInstance = new REApplication();
        }
        return mInstance;
    }

    /**
     * Global Context for application
     *
     * @return Context
     */
    public static Context getAppContext() {
        return appContext;
    }

    /**
     * LruCache for storing mapImages
     *
     * @return mMemoryCache
     */
    public static LruCache<String, Bitmap> getMemoryCache() {
        return mMemoryCache;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (BuildConfig.DEBUG) {
//            RELog.plant(new RELog.DebugTree());
//        }
        mInstance = this;
        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
		FirebaseCrashlytics.getInstance().setUserId("guest");
        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseInstance.setPersistenceEnabled(false);
        mFireStoreInstance = FirebaseFirestore.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
		mFirebasePerformance=FirebasePerformance.getInstance();
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mREWebsiteApiInstance = new REWebsiteAPI();
        mREServiceApiInstance = new REServiceAPI();
        mRENotificationApiInstance = new RENotificationAPI();
        mREYoutubeAPI = new REYoutubeAPI();
        appContext = getApplicationContext();
        mREGoogleMapAPI = new REGoogleMapAPI();
        mRELocationInstance = new RELocation();
        try {
            mREConnectedApiInstance = new REConnectedAPI();
        }

        catch (Exception e){
         //   e.printStackTrace();
        }
		initSFSDK();
        mConnectedDeviceInfo = new ArrayList<>();
        getCustomTokenModel = new GetCustomTokenModel();
        // Branch.io logging for debugging
        if (BuildConfig.DEBUG)
            Branch.enableLogging();
        if (BuildConfig.FLAVOR.contains("production")) {
            Branch.disableTestMode();
        } else {
            Branch.enableTestMode();
        }
        // Branch.io object initialization
        Branch.getAutoInstance(this);
        //This is for caching map images
        int cacheSize = 1024; // 1MB
        //TODO Need to check the cache size
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };
        mGson = new Gson();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        //Register for Connection state change
        mConnectivityReceiver = new NetworkConnectionReceiver();
        registerReceiver(mConnectivityReceiver, new IntentFilter(INetworkStateListener.CONNECTION_CHANGE));


        if (REUtils.isDeviceRooted()) {
            Toast.makeText(this, "Your device is rooted", Toast.LENGTH_LONG).show();
            return;
        }
      //  Neolane.getInstance();
//        if (BuildConfig.DEBUG) {
//            RELog.plant(new RELog.DebugTree());
//        }
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, this);
        breadcrumbTrailManager = new BreadcrumbTrailManager(this);

            crashHandler = new CrashHandler(this, breadcrumbTrailManager);

        Thread.UncaughtExceptionHandler defaultUce = Thread.getDefaultUncaughtExceptionHandler();
        CustomUncaughtExceptionHandler customUce =
                new CustomUncaughtExceptionHandler(crashHandler, defaultUce);
        Thread.setDefaultUncaughtExceptionHandler(customUce);
    }

	private void initSFSDK() {
		if(BuildConfig.DEBUG) {
			SFMCSdk.Companion.setLogging(LogLevel.DEBUG, new LogListener.AndroidLogger());
			MarketingCloudSdk.setLogLevel(MCLogListener.VERBOSE);
			MarketingCloudSdk.setLogListener(new MCLogListener.AndroidLogListener());
		}
		MarketingCloudConfig.Builder builder = MarketingCloudConfig.Companion.builder();
		builder.setApplicationId(getResources().getString(R.string.MC_APP_ID));
		builder.setAccessToken(getResources().getString(R.string.MC_ACCESS_TOKEN));
		builder.setSenderId(getResources().getString(R.string.MC_SENDER_ID));
		builder.setMid(getResources().getString(R.string.MC_MID));
		builder.setAnalyticsEnabled(true);
		builder.setDelayRegistrationUntilContactKeyIsSet(true);
		builder.setMarketingCloudServerUrl(getResources().getString(R.string.MC_SERVER_URL));
		builder.setNotificationCustomizationOptions(NotificationCustomizationOptions.create(R.drawable.ic_noti_test));
		SFMCSdkModuleConfig.Builder builderSFMC=new SFMCSdkModuleConfig.Builder();
		builderSFMC.setPushModuleConfig(builder.build(getAppContext()));
		SFMCSdk.configure(getAppContext(),builderSFMC.build(),initializationStatus -> {
			if(initializationStatus.getStatus()== InitializationStatus.SUCCESS){
			}
			return null;
		});
		SFMCSdk.Companion.requestSdk(sfmcSdk ->{
			//	sfmcSdk.identity.setProfileId("");

			sfmcSdk.mp(pushModuleInterface ->{
				pushModuleInterface.getPushMessageManager().enablePush();
			});
		});
	}


	public void setCountryData(){
        if (REUtils.getFeatures() != null) {
            featureCountry = REUtils.getFeatures().getIN();
            defaultFeatures=REUtils.getFeatures().getDefaultFeatures();
            if (BuildConfig.FLAVOR.contains("Apac")||BuildConfig.FLAVOR.contains("Rena")||BuildConfig.FLAVOR.contains("Latm")||BuildConfig.FLAVOR.contains("EU")) {
                Country=REConstants.DEFAULT_HEADER;
                featureCountry =   REUtils.getFeatures().getDefaultFeatures();
            }
        }
    }

    public SharedPreferences getEncryptedSharedPreference(Context context) throws GeneralSecurityException, IOException {

        MasterKey masterKey = new MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "re_secret_shared_prefs",
                masterKey, // masterKey created above
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        return sharedPreferences;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        //App in background
        Log.d("OnLifecycleEvent", "onAppBackgrounded");
        REBaseActivity.APP_LIFE_FLAG = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        // App in foreground
        Log.d("OnLifecycleEvent", "onAppForegrounded");
        Intent intent = new Intent(REBaseActivity.APP_LIFE_CYCLE);
        // send local broadcast
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    /**
     * Incrementally release memory based on current system constraints.
     * Using this callback to release your resources helps provide a more responsive system overall,
     * but also directly benefits the user experience for your app by allowing the system to keep your process alive longer.
     * That is, if you don't trim your resources based on memory levels defined by this callback,
     * the system is more likely to kill your process while it is cached in the least-recently used (LRU) list,
     * thus requiring your app to restart and restore all state when the user returns to it
     * <p>
     * Called when the operating system has determined that it is a good time for a process to trim unneeded memory from its process.
     * This will happen for example when it goes in the background and there is not enough memory to keep as many background processes running as desired.
     *
     * @param level : memory level
     */
    @Override
    public void onTrimMemory(int level) {
        //Clearing cache
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
        super.onTrimMemory(level);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //Unregister Connection state change.
        if (mConnectivityReceiver != null) {
            unregisterReceiver(mConnectivityReceiver);
        }
    }

    /**
     * Returns the network state change listener.
     *
     * @return {@link INetworkStateListener} instance.
     */
    public INetworkStateListener getNetworkChangeListener() {
        return connectionListener;
    }

    /**
     * Sets the network state change listener.
     *
     * @param listener {@link INetworkStateListener} instance.
     */
    public void setNetworkChangeListener(INetworkStateListener listener) {
        connectionListener = listener;
    }

    /**
     * Set the App foreground status.
     *
     * @param isAppInForeground true if in foreground otherwise false.
     */
    public void setIsAppInForeground(boolean isAppInForeground) {
        this.mIsForeground = isAppInForeground;
    }

    /**
     * Gets the DMS login API hitting time
     *
     * @return dmsLoginTokenStartTime
     */
    public long getDmsLoginTokenStartTime() {
        return dmsLoginTokenStartTime;
    }

    /**
     * Sets the time when we get  DMS login response
     *
     * @param dmsLoginTokenStartTime DMS token start time.
     */
    public void setDmsLoginTokenStartTime(long dmsLoginTokenStartTime) {
        this.dmsLoginTokenStartTime = dmsLoginTokenStartTime;
    }

    /**
     * Provides the {@link REWebsiteAPI} instance.
     *
     * @return {@link REWebsiteAPI} instance.
     */
    public REWebsiteAPI getREWebsiteApiInstance() {
        return mREWebsiteApiInstance;
    }

    /**
     * Provides {@link REServiceAPI} instance.
     *
     * @return {@link REServiceAPI} instance.
     */
    public REServiceAPI getREServiceApiInstance() {
        return mREServiceApiInstance;
    }

    /**
     * Provides {@link RENotificationAPI} instance.
     *
     * @return {@link RENotificationAPI} instance.
     */
    public RENotificationAPI getRENotificationApiInstance() {
        return mRENotificationApiInstance;
    }


    /**
     * Returns the user login details.
     *
     * @return {@link LoginResponse} details.
     */
    public LoginResponse getUserLoginDetails() {
        try {
            String data = REPreference.getInstance().getString(REApplication.getAppContext(), USERDATA);
            if (!data.equals(""))
                return new Gson().fromJson(data, LoginResponse.class);
        } catch (PreferenceException ex) {
            RELog.e(ex);
        }
        return null;
    }

    public String getUserTokenDetails() {
        try {
            if(JWTToken==null){
                String data = REApplication.getInstance().getEncryptedSharedPreference(REApplication.getAppContext()).getString(JWT_TOKEN_KEY,null);
                if (data!=null&&!data.equals("")) {
                    JWTToken = data;
                }
                //  return JWTToken;
            }
            return JWTToken;

        } catch (GeneralSecurityException | IOException ex) {
            RELog.e(ex);
        }
        return null;
    }


    /**
     * Returns the DMS login response which contains auth token to validate every API.
     *
     * @return {@link DMSLoginResponse}
     */
    public DMSLoginResponse getDmsLoginResponse() {
        return mDmsLoginResponse;
    }

    /**
     * Stores DMS Login API response in models.
     *
     * @param mDmsLoginResponse {@link DMSLoginResponse}
     */
    public void setDmsLoginResponse(DMSLoginResponse mDmsLoginResponse) {
        this.mDmsLoginResponse = mDmsLoginResponse;
    }

    /**
     * Returns the Profile data returned by DMS API for the rider.
     *
     * @return {@link DMSProfileData}
     */
    public DMSProfileData getDmsProfileData() {
        return mProfileData;
    }

    /**
     * Stores Rider ProfileData fetched from the DMS API server.
     *
     * @param profileData {@link DMSProfileData}
     */
    public void setDmsProfileData(DMSProfileData profileData) {
        this.mProfileData = profileData;
    }

    /**
     * Returns ServiceBookingResponse API response
     *
     * @return {@link List<ServiceBookingResponse>}
     */
    public List<ServiceBookingResponse> getServiceBookingResponse() {
        return mServiceBookingResponse;
    }

    /**
     * Stores ServiceHistory API response in model
     *
     * @param mServiceBookingResponse {@link ServiceBookingResponse}
     */
    public void setServiceBookingResponse(
            List<ServiceBookingResponse> mServiceBookingResponse) {
        this.mServiceBookingResponse = mServiceBookingResponse;
    }

    /**
     * Returns ServiceSlotResponse API response
     *
     * @return {@link List<ServiceSlotResponse>} mServiceSlotResponse.
     */
    public List<ServiceSlotListResponse> getServiceSlotResponse() {
        return mServiceSlotResponse;
    }

    /**
     * Stores ServiceHistory API response in model
     *
     * @param mServiceSlotResponse {@link ServiceBookingResponse}
     */
    public void setServiceSlotResponse(List<ServiceSlotListResponse> mServiceSlotResponse) {
        this.mServiceSlotResponse = mServiceSlotResponse;
    }
    /**
     * Returns PickupandDoorstepServiceSlot API response
     *
     * @return {@link List<PickupandDoorstepServiceSlot>} mPickupandDoorstepServiceSlot.
     */
    public List<PickupandDoorstepServiceSlot> getPickupandDoorstepServiceSlotResponse() {
        return mPickupandDoorstepServiceSlot;
    }

    /**
     * Stores PickupandDoorstepServiceSlot API response in model
     *
     * @param mPickupandDoorstepServiceSlot {@link PickupandDoorstepServiceSlot}
     */
    public void setPickupandDoorstepServiceSlotResponse(List<PickupandDoorstepServiceSlot> mPickupandDoorstepServiceSlot) {
        this.mPickupandDoorstepServiceSlot = mPickupandDoorstepServiceSlot;
    }

    /**
     * Returns Vehicle Details response.
     *
     * @return {@link DMSVehicleDetailsResponse} Object.
     */
    public List<DMSVehicleDetailsResponse> getVehicleDetails() {
        return mDMSVehicleDetails;
    }

    /**
     * Stores Vehicle Details in model.
     *
     * @param dmsVehicleDetailsResponse {@link DMSVehicleDetailsResponse} Object.
     */
    public void setVehicleDetails(List<DMSVehicleDetailsResponse> dmsVehicleDetailsResponse) {
        mDMSVehicleDetails = dmsVehicleDetailsResponse;
    }

    /**
     * Returns ServiceAppointmentList Response
     *
     * @return {@link VehicleServiceProgressListResponse} Object.
     */
    public List<VehicleServiceProgressListResponse> getServiceProgressListResponse() {
        return mServiceProgressListResponse;
    }

    /**
     * Stores ServiceAppointmentList Details in model
     *
     * @param mServiceAppointmentListResponse {@link VehicleServiceProgressListResponse} Object.
     */
    public void setServiceProgressListResponse
    (List<VehicleServiceProgressListResponse> mServiceAppointmentListResponse) {
        this.mServiceProgressListResponse = mServiceAppointmentListResponse;
    }

    /**
     * Provides the Youtube API instance.
     *
     * @return {@link REYoutubeAPI} instance.
     */
    public REYoutubeAPI getREYoutubeAPI() {
        return mREYoutubeAPI;
    }

    /**
     * Returns the DealersResponse from DMS API
     *
     * @return {@link List<DealerMasterResponse>}
     */
    public List<DealerMasterResponse> getDealersResponse() {
        return mDealersResponse;
    }

    /**
     * Stores Dealers response fetched from DMS API
     *
     * @param mDealersResponse {@link DealerMasterResponse}
     */
    public void setDealersResponse(List<DealerMasterResponse> mDealersResponse) {
        this.mDealersResponse = mDealersResponse;
    }

    public String getServiceCenterNo() {
        return mServiceCenterNo;
    }

    public void setServiceCenterNo(String mServiceCenterNo) {
        this.mServiceCenterNo = mServiceCenterNo;
    }

    /**
     * Returns the Issues Model Array list
     *
     * @return {@link IssuesModel}
     */
    public ArrayList<IssuesModel> getSelectedIssues() {
        return mSelectedIssues;
    }

    /**
     * Stores IssuesModel ArrayList which is selected
     *
     * @param strKey        key
     * @param mListOfIssues {@link ArrayList<IssuesModel>}
     */
    public void setSelectedIssues(String strKey, ArrayList<IssuesModel> mListOfIssues) {
        this.mSelectedIssues = mListOfIssues;
        addAllSelectedServiceIssues(strKey, mSelectedIssues);
    }


    public Map<String, String> getMapUserDescribedIssues() {
        return mapUserDescribedIssues;
    }

    public void setMapUserDescribedIssues(Map<String, String> mapUserDescribedIssues) {
        this.mapUserDescribedIssues = mapUserDescribedIssues;
    }

    public ArrayList<Map<String, String>> getListOfUserDescribedIssues() {
        return listOfUserDescribedIssues;
    }

    public void setListOfUserDescribedIssues
            (ArrayList<Map<String, String>> listOfUserDescribedIssues) {
        this.listOfUserDescribedIssues = listOfUserDescribedIssues;
    }

    /**
     * Stores the user described issues in a string.
     */
    public void setUserDescribedIssues(String strKey, String userDescIssues, int position) {
        this.userDescribedIssues = userDescIssues;
        addAllUserDescribedIssues(strKey, userDescIssues.trim(), position);
    }


    /**
     * Stores the user described issues from each fragment to a map.
     */
    public void addAllUserDescribedIssues(String strKeyIssueType, String userDescIssue,
                                          int position) {
        String[] issueKeys = {"engine", "chassis", "battery", "Electrical", "Aesthetics", "Suspension", "Wheels"};
        //Since we are storing the user described issue on text changed, initialize the map
        //for every fragment with null. Add map to an array list. The array list contains
        //the map of each fragment which in turn contains the string values of issues
        //with respect to that particular fragment.
        if (mapUserDescribedIssues.size() == 0) {
            for (int i = 0; i < 7; i++) {
                //Create a new map for every fragment and initialize it with null.
                mapUserDescribedIssues = new HashMap<>();
                mapUserDescribedIssues.put(issueKeys[i], null);
                //Add each map to an array list.
                listOfUserDescribedIssues.add(mapUserDescribedIssues);
            }
        }
        Map<String, String> getsome = listOfUserDescribedIssues.get(position);
        for (Map.Entry<String, String> entry : getsome.entrySet()) {
            if (entry.getKey().equals(strKeyIssueType)) {
                //create a map and initialize it with issues of a particular fragment.
                mapUserDescribedIssues = new HashMap<>();
                mapUserDescribedIssues.put(strKeyIssueType, userDescIssue.trim());
                //Replace the map in the array list with map containing the user described issues.
                listOfUserDescribedIssues.set(position, mapUserDescribedIssues);
            }
        }
    }

    /**
     * Get the user described issues of a particular issue fragment.
     */
    public String getAllUSerDescribedIssues(String strKeyStore) {
        for (int iCount = 0; iCount < listOfUserDescribedIssues.size(); iCount++) {
            Map<String, String> getsome = listOfUserDescribedIssues.get(iCount);
            for (Map.Entry<String, String> entry : getsome.entrySet()) {
                String key = entry.getKey();
                if (key.equalsIgnoreCase(strKeyStore) && entry.getValue() != null) {
                    return entry.getValue();
                }
            }
        }
        return "";
    }

    /**
     * Get the user described issues of all the issue fragment.
     */
    public String getAllUSerDescribedValues() {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int iCount = 0; iCount < listOfUserDescribedIssues.size(); iCount++) {
            Map<String, String> getsome = listOfUserDescribedIssues.get(iCount);
            for (Map.Entry<String, String> entry : getsome.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().equals("")) {
                    stringBuilder.append(entry.getValue()).append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }


    /**
     * Stores IssuesModel ArrayList which is selected.
     *
     * @param listOfIssues list of issue added.
     */
    public void addAllSelectedServiceIssues(String
                                                    strKeyIssueType, ArrayList<IssuesModel> listOfIssues) {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> issueList = REUtils.getSelectedIssues(false, getAppContext());
        for (String model : issueList) {
            builder.append(model + ", ");
        }
        Bundle params = new Bundle();
        params.putString("eventCategory", "Motorcycles-Schedule a service");
        params.putString("eventAction", "Any specific issue click");
        params.putString("eventLabel", strKeyIssueType + "|" + builder.toString());
        REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
        // put selected service issues into map
        issueModelStore.put(strKeyIssueType.toLowerCase(), listOfIssues);
    }

    /**
     * Returns the IssuesModel ArrayList
     *
     * @return {@link IssuesModel}
     */
    public Map<String, ArrayList<IssuesModel>> getAllSelectedServiceIssues() {
        return issueModelStore;
    }


    /**
     * Copy back the saved issues to selection list.
     */
    public void copySavedItemsToSelectedList(Map<String, ArrayList<IssuesModel>> arrayListMap) {
        issueModelStore.clear();
        for (Map.Entry<String, ArrayList<IssuesModel>> entry : arrayListMap.entrySet()) {
            issueModelStore.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Provide RELocation instance.
     *
     * @return {@link RELocation}
     */
    public RELocation getRELocationInstance() {
        return mRELocationInstance;
    }

    public void setmRELocationInstance(RELocation mRELocationInstance) {
        this.mRELocationInstance = mRELocationInstance;
    }

    /**
     * Provides the Google Map API instance.
     *
     * @return {@link REGoogleMapAPI} instance.
     */
    public REGoogleMapAPI getREGoogleMapAPIInstance() {
        return mREGoogleMapAPI;
    }

    public void setREGoogleMapAPI(REGoogleMapAPI mREGoogleMapAPI) {
        this.mREGoogleMapAPI = mREGoogleMapAPI;
    }

    /**
     * Clears all the model stores....
     */
    public void clearAllModelStore() {
        cloudManager = null;
        mDmsLoginResponse = null;
        mProfileData = null;
        mDMSVehicleDetails = null;
        mDealersResponse = null;
        mUserProfileData = null;
        issueModelStore.clear();
        mapUserDescribedIssues.clear();
        listOfUserDescribedIssues.clear();
        REUserModelStore.clearModelStore();
        mConnectedDeviceInfo.clear();
    }

    public String getDescriptionValue() {
        return strDescriptionValue;
    }

    public void setDescriptionValue(String strDescriptionValue) {
        this.strDescriptionValue = strDescriptionValue;
    }

    public boolean isComingFromVehicleOnboarding() {
        return comingFromVehicleOnboarding;
    }

    public void setComingFromVehicleOnboarding(boolean comingFromVehicleOnboarding) {
        this.comingFromVehicleOnboarding = comingFromVehicleOnboarding;
    }

    public AddVehicleResponse getAddVehicleResponse() {
        return addVehicleResponse;
    }

    public void setAddVehicleResponse(AddVehicleResponse addVehicleResponse) {
        this.addVehicleResponse = addVehicleResponse;
    }


    public boolean getIsLoggedInUser() {
        return isLoggedInUser;
    }

    public void setLoggedInUser(boolean loggedInUser) {
        isLoggedInUser = loggedInUser;
    }

    /**
     * Gets the boolean value for Logger file created in phone
     *
     * @return : boolean
     */
    public boolean getLoggerFileCreated() {
        return isLoggerFileCreated;
    }

    public void setLoggerFileCreated(boolean value) {
        isLoggerFileCreated = value;
    }
    /**
     * Gets the boolean value for profile image cache
     *
     * @return : boolean
     */
    public boolean getIsProfileImageCached() {
        return mIsProfileImageCached;
    }

    /**
     * Sets the profile
     *
     * @param mIsProfileImageCached : boolean
     */
    public void setIsProfileImageCached(boolean mIsProfileImageCached) {
        this.mIsProfileImageCached = mIsProfileImageCached;
    }


    public CloudManager getCloudManager() {
        if (cloudManager == null) {
            REUtils.initCloudManager();
        }
        return cloudManager;
    }

    public void setCloudManager(CloudManager cldManager) {
        cloudManager = cldManager;
    }

    public List<DeviceInfo> getmConnectedDeviceInfo() {
        return mConnectedDeviceInfo;
    }

    public void setmConnectedDeviceInfo(List<DeviceInfo> mConnectedDeviceInfo) {
        this.mConnectedDeviceInfo = mConnectedDeviceInfo;
    }

    public boolean ismIsDeviceConnected() {
        return mIsDeviceConnected;
    }

    public void setIsDeviceConnected(boolean mIsDeviceConnected) {
        this.mIsDeviceConnected = mIsDeviceConnected;
    }

    public boolean isIsDeviceAuthorised() {
        return mIsDeviceAuthorised;
    }

    public void setIsDeviceAuthorised(boolean mIsDeviceAuthorised) {
        this.mIsDeviceAuthorised = mIsDeviceAuthorised;
    }

    public List<CountryModel> getCountryList() {

        Type type = new TypeToken<List<CountryModel>>() {
        }.getType();
        try {
            String data = REPreference.getInstance().getString(REApplication.getAppContext(), COUNTRY_KEY);
            List<CountryModel> countryList = mGson.fromJson(data, type);
            return countryList;
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        return new ArrayList<>();
    }

    public void setCountryList(List<CountryModel> countryData) {

        try {

            REPreference.getInstance().putString(REApplication.getAppContext(), COUNTRY_KEY,
                    new Gson().toJson(countryData));
        } catch (PreferenceException e) {
            RELog.e(e);
        }
    }
    public void setNavigationInProgress(boolean inProgress) {
        this.isNavigationInProgress = inProgress;
    }

    public boolean isNavigationInProgress() {
        return this.isNavigationInProgress;
    }
    /**
     * Provides the Connected API instance.
     *
     * @return {@link REConnectedAPI} instance.
     */
    public REConnectedAPI getREConnectedAPI() {
        if(mREConnectedApiInstance!=null)
            return mREConnectedApiInstance;
        else{
            mREConnectedApiInstance=new REConnectedAPI();
        }
        return mREConnectedApiInstance;
    }

    public void setCountryHeaderDefault(){
        if (BuildConfig.FLAVOR.contains("Apac")||BuildConfig.FLAVOR.contains("Rena")||BuildConfig.FLAVOR.contains("Latm")||BuildConfig.FLAVOR.contains("EU")) {
            Country=REConstants.DEFAULT_HEADER;
        }
        else
            Country=REConstants.COUNTRY_INDIA;
    }
    public PhoneStateListener getPhoneStateListener() {
        return phoneStateListener;
    }

    public void setPhoneStateListener(PhoneStateListener phoneStateListener) {
        this.phoneStateListener = phoneStateListener;
    }

    public boolean isIncomingCall() {
        return isIncomingCall;
    }

    public void setIncomingCall(boolean incomingCall) {
        isIncomingCall = incomingCall;
    }

    public boolean isCallAccepted() {
        return isCallAccepted;
    }

    public void setCallAccepted(boolean callAccepted) {
        isCallAccepted = callAccepted;
    }

    public void setAlertTimestamp(String timestamp){
        healthAlertTimeStamp=timestamp;
    }

    public void setSecret(String secretKey){
        secret=secretKey;
    }

    public String getSecret(){
        if(secret==null){
            FirestoreManager.getInstance().getSecretKey(new OnFirestoreCallback() {
                @Override
                public void onFirestoreSuccess() {

                }

                @Override
                public void onFirestoreFailure(String message) {

                }
            });
            return null;
        }
        else

            return secret;
    }


public RemoteConfigData getRemoteConfigData()  {
        if(remoteConfigData==null){
            try {
                String  remoteConfigDataLocal = FirestoreManager.getInstance().getStringSharedpreference(REApplication.getAppContext(), REMOTE_DATA);
                String version = FirestoreManager.getInstance().getStringSharedpreference(REApplication.getAppContext(), REMOTE_VERSION);
                RemoteConfigData dataset = new Gson().fromJson(remoteConfigDataLocal, RemoteConfigData.class);
				remoteConfigData=dataset;
                 if(remoteConfigData==null||version.isEmpty()||version.equalsIgnoreCase("0.0")||Integer.parseInt(version)< Integer.parseInt(getApplicationContext().getString(R.string.remote_config_version))){
                    Map<String, String> xmlDefaults = DefaultsXmlParser.getDefaultsFromXml(this, R.xml.remote_config_defaults);
                    final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                    final RemoteConfigData  defaultData= mapper.convertValue(xmlDefaults, RemoteConfigData.class);
                    FirestoreManager.getInstance().saveToSharedPreference("0.0", defaultData);
                   remoteConfigData=defaultData;
					 Log.e("REMOTECONFIG","FETCH DEFAULT");
                }
				 else{
					 Log.e("REMOTECONFIG","FETCH LATEST");
				 }

            } catch (Exception e) {
              //  e.printStackTrace();
            }
            return remoteConfigData;
        }
        else return  remoteConfigData;
}
public void setRemoteConfigData(RemoteConfigData data){
		remoteConfigData=data;
}

    public BreadcrumbTrailManager getBreadcrumbTrailManager() {
        return breadcrumbTrailManager;
    }

    public CrashHandler getCrashHandler() {
        return crashHandler;
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
              //  Timber.tag("onMapsSdkInitialized").v("LATEST");
                break;
            case LEGACY:
              //  Timber.tag("onMapsSdkInitialized").v("LEGACY");
                break;
        }
    }
	public void startTrace(String traceType){
		Trace trace = traces.get(traceType);
		if (trace == null) {
			trace = FirebasePerformance.getInstance().newTrace(traceType);
			Trace oldTrace = traces.put(traceType, trace);
			if (oldTrace == null) {
				trace.start();
			}
		}
}
	public void endTrace(String traceType){

		Trace trace = traces.remove(traceType);
		if (trace != null) {
			trace.stop();
		}

	}
	public enum TRACES{
		app_launch,
		user_login,
		user_signup
	}
}
