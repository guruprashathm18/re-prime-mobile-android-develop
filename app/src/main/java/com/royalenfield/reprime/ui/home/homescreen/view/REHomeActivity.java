package com.royalenfield.reprime.ui.home.homescreen.view;

import static com.royalenfield.reprime.utils.REConstants.COMMUNITY_DEFAULT_SCREEN;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_DISABLED;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;
import static com.royalenfield.reprime.utils.REConstants.IS_NAVIGATION_DETAILS;
import static com.royalenfield.reprime.utils.REConstants.KEY_UPDATE_PROVISIONING;
import static com.royalenfield.reprime.utils.REConstants.PUSH_REMOTE_MESSAGE;
import static com.royalenfield.reprime.utils.REUtils.getCountryData;
import static com.royalenfield.reprime.utils.REUtils.isUserLoggedIn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.royalenfield.appIndexing.IAppIndex;
import com.royalenfield.appIndexing.IAppIndexManager;
import com.royalenfield.appIndexing.REAppIndexProvider;
import com.royalenfield.appIndexing.REAppIndexScreen;
import com.royalenfield.bluetooth.BleSearchActivity;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.fireStore.OnFirestoreCallback;
import com.royalenfield.firebase.fireStore.OnFirestoreVehicleDataMappingCallback;
import com.royalenfield.firebase.fireStore.OnFirestoreVehicleListCallback;
import com.royalenfield.firestore.userinfo.ProvisioningData;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.remoteconfig.ConfigFeatures;
import com.royalenfield.reprime.models.response.web.login.LoginResponse;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.homescreen.feature.FeatureList;
import com.royalenfield.reprime.ui.home.homescreen.fragments.MotorCycleFragment;
import com.royalenfield.reprime.ui.home.homescreen.fragments.RECommunityFragment;
import com.royalenfield.reprime.ui.home.homescreen.fragments.RENavigationRootFragment;
import com.royalenfield.reprime.ui.home.homescreen.interactor.HomeActivityInteractor;
import com.royalenfield.reprime.ui.home.homescreen.listener.HeaderSwipeListener;
import com.royalenfield.reprime.ui.home.homescreen.listener.REHeaderSwipeTouchListener;
import com.royalenfield.reprime.ui.home.homescreen.presenter.HomeActivityPresenter;
import com.royalenfield.reprime.ui.home.rides.activity.CreateRideActivity;
import com.royalenfield.reprime.ui.home.rides.activity.NotificationsActivity;
import com.royalenfield.reprime.ui.home.service.diy.activity.DoItYourSelfActivity;
import com.royalenfield.reprime.ui.home.service.fragment.REServicingFragment;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.LogResponse;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.fragment.REOnBoardingDialog;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.utils.REAddVehicleDialogUtil;
import com.royalenfield.reprime.ui.phoneconfigurator.listener.OnNavigateToMYO;
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils;
import com.royalenfield.reprime.ui.phoneconfigurator.view.PCWebViewActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.REProfileActivity;
import com.royalenfield.reprime.ui.riderprofile.fragment.CommunityWebFragment;
import com.royalenfield.reprime.ui.userdatavalidation.popup.Constants;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.TransparentPopActivity;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.ContactData;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * REHomeActivity is The Main Home Activity after the login.
 * It contains Viewpager with Fragments : OurWorld, Rides and Servicing Tabs.
 */
public class REHomeActivity extends REBaseActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener, HeaderSwipeListener,
        REServicingFragment.ScrollUpListener
        , BottomNavigationView.OnNavigationItemSelectedListener, OnNavigateToMYO, IAppIndex {

    private final String TAG = REHomeActivity.class.getSimpleName();
    private ImageView mNotificationsImage;
    private ImageView mRSAImage;
    private ConstraintLayout mCreateRideConstraint;
    private ConstraintLayout mServiceDIYConstraint;
    private LinearLayout mTabIndicatorLinear, lHeaderLayout;
    private boolean doubleBackToExitPressedOnce = false;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    public String selectBottomNavigation;
    private TextView tvStart;
    private TextView tvMiddle;
    private TextView tvEnd;
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private ArrayList<FeatureList> featureLists = new ArrayList<>();

    //private ScrollView mScrollViewHome;
    private Fragment fragment;
    private FrameLayout frameContainer;
    private FrameLayout frameContainerServicing;
    private Menu menu;
    public BottomNavigationView bottom_navigation;
    private ConstraintLayout constraintHeader;
    private ImageView mProfileImage,mReTitleImage,mReTitleImagePip;
    public REOnBoardingDialog reOnBoardingDialog;
    private boolean mIsServiceNotification;
    private HomeActivityPresenter homeActivityPresenter;
    //    private CloudManager cloudManager;
    private String sharedNavigationId;
    private boolean isNavigationTab = false;
    private boolean mIsNavigationDetails;
    private boolean mIsNavigationNotification = false;
    private boolean mIsNavigationDeepLink = false;
    private String sharedDeepLink;
    private static final String TAG_MOTORCYCLE = "motorctle_frag";
    private static final String TAG_NAVIGATION = "navigation_frag";
    private static final String TAG_COMMUNITY = "community_frag";
    private static final String TAG_RIDES = "rides_frag";
    private static final String TAG_CONFIGURATOR = "config_frag";
    private ConfigFeatures configFeature;
    private boolean isTBTDisabled = false;
    private IAppIndexManager appIndexManager = REAppIndexProvider.INSTANCE.getInstance();
    private JsonObject pushPayLoadJson;
    public REAppIndexScreen mReAppIndexScreen = REAppIndexScreen.OUR_WORLD;
    private boolean isFromAppIndex = false;
    public AlertDialog myGooglePolicyAlertDialog;

    @Override
    public void scrollUp() {
        //mScrollViewHome.fullScroll(ScrollView.FOCUS_UP);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.item_motorcycle:
                //  enableTheLocation();
                if (isUserLoggedIn()) {
                    if (REApplication.getInstance().mFirebaseAuth != null && REApplication.getInstance().mFirebaseAuth.getCurrentUser() != null && REApplication.getInstance().mFirebaseAuth.getCurrentUser().getUid().equalsIgnoreCase(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId())) {
                        if (selectBottomNavigation == null || !selectBottomNavigation.equals(REConstants.MOTORCYCLE)) {
							selectBottomNavigation = REConstants.MOTORCYCLE;
                            showMotorcycleScreen();

                            isNavigationTab = false;
                            cleardeeplinkdata();
                        }
                    } else {
						if (!selectBottomNavigation.equals(REConstants.MOTORCYCLE)) {
							String erroMessgae = "";
							if (REUtils.CHECK_FIREBASE_AUTH_INPROGRESS)
								erroMessgae = getResources().getString(R.string.firebase_auth_progress_fetch_data);
							else
								erroMessgae = getResources().getString(R.string.firebase_auth_error);
							REUtils.showErrorDialog(this, erroMessgae);
						}
                        return false;
                    }
                } else {
                    if (selectBottomNavigation == null || !selectBottomNavigation.equals(REConstants.MOTORCYCLE)) {
						selectBottomNavigationTab(new MotorCycleFragment().newInstance(this), true, TAG_MOTORCYCLE);
                        selectBottomNavigation = REConstants.MOTORCYCLE;
                        isNavigationTab = false;
                        cleardeeplinkdata();
                    }
                }
                return true;

            case R.id.item_community:
                //    enableTheLocation();
                if (selectBottomNavigation == null || !selectBottomNavigation.equals(REConstants.COMMUNITY)) {
                    if (REUtils.getFeatures().getDefaultFeatures() != null && REUtils.getFeatures().getDefaultFeatures().getShowOurWorldWebV2().equalsIgnoreCase(FEATURE_DISABLED)) {
                        Fragment reCommunityFragment = RECommunityFragment.newInstance();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(COMMUNITY_DEFAULT_SCREEN, mReAppIndexScreen);
                        reCommunityFragment.setArguments(bundle);
                        selectBottomNavigationTab(reCommunityFragment, true, TAG_COMMUNITY);
                        selectBottomNavigation = REConstants.COMMUNITY;
                        isNavigationTab = false;
                        cleardeeplinkdata();
                    } else {
                        String ourWorldUrl = REUtils.getMobileappbaseURLs().getOurWorldWeb();
                        Fragment reCommunityFragment = CommunityWebFragment.Companion.newInstance();
                        Bundle b = new Bundle();
                        b.putString("url", ourWorldUrl);
                        b.putString("title", getString(R.string.header_ourworld));
                        //  act.setArguments(b);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(COMMUNITY_DEFAULT_SCREEN, mReAppIndexScreen);
                        reCommunityFragment.setArguments(b);
                        selectBottomNavigationTab(reCommunityFragment, true, TAG_COMMUNITY);
                        selectBottomNavigation = REConstants.COMMUNITY;
                        isNavigationTab = false;
                        cleardeeplinkdata();
                    }
                }
                return true;

            case R.id.item_rides:
                //    enableTheLocation();
                if (selectBottomNavigation == null || !selectBottomNavigation.equals(REConstants.RIDES)) {
                    String ridesWeb = REUtils.getMobileappbaseURLs().getRidesWeb();
                    Fragment reCommunityFragment = CommunityWebFragment.Companion.newInstance();
                    Bundle b = new Bundle();
                    b.putString("url", ridesWeb);
                    b.putString("title", getString(R.string.header_rides));
                    //  act.setArguments(b);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(COMMUNITY_DEFAULT_SCREEN, mReAppIndexScreen);
                    reCommunityFragment.setArguments(b);
                    selectBottomNavigationTab(reCommunityFragment, true, TAG_RIDES);
                    selectBottomNavigation = REConstants.RIDES;
                    isNavigationTab = false;
                    cleardeeplinkdata();
                }
                return true;
            case R.id.item_configuration:
                enableTheLocation();
                Bundle params = new Bundle();
                params.putString("eventCategory", "Make your Own");
                params.putString("eventAction", "Make your own click");
                REUtils.logGTMEvent(REConstants.KEY_MYO_GTM, params);
                Intent intent = new Intent(REHomeActivity.this, PCWebViewActivity.class);
                intent.putExtra(PCUtils.PC_REACT_FLAG, PCUtils.PC_OPEN_MYO);
                startActivity(intent);
                return false;
//            case R.id.item_wonderlust:
//                enableTheLocation();
//                Intent intentWonderLust = new Intent(REHomeActivity.this, WLWebViewActivity.class);
//                intentWonderLust.putExtra(PCUtils.PC_REACT_FLAG, PCUtils.PC_OPEN_WONDERLUST);
//                startActivity(intentWonderLust);
//                return false;
            case R.id.item_navigate:
                if (selectBottomNavigation == null || !selectBottomNavigation.equals(REConstants.NAVIGATION)) {
                    showNavigationScreen();
                }
                return true;
        }
        return false;
    }

    private void showNavigationScreen() {
        if (isTBTDisabled) {
            showGoogleMapActivity();
            return;
        }
        if (REUtils.isUserLoggedIn()) {
            showNavigationMapScreen();
        } else {
            //To handle guest user login.
            Intent intent = new Intent(this, UserOnboardingActivity.class);
            intent.putExtra(REUtils.NAVIGATION_CALLING_ACTIVITY, LoginActivity.CODE_NAVIGATION_SCREEN);
            startActivityForResult(intent, LoginActivity.CODE_NAVIGATION_SCREEN);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void cleardeeplinkdata() {
        if (!REServiceSharedPreference.getDeepLinkData(getApplicationContext()).equals("")) {
            REServiceSharedPreference.setDeepLinkData(getApplicationContext(), "");
        }
    }

    @Override
    public void onNavigateMYO() {
        bottom_navigation.findViewById(R.id.item_configuration).performClick();
    }

    private void showNavigationMapScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int bgLocationSTATE = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            if (bgLocationSTATE != PackageManager.PERMISSION_GRANTED) {
              /*  REUtils.showErrorDialog(this, getString(R.string.text_bg_permission), () -> {
                    selectBottomNavigationTab(new RENavigationRootFragment(), true, TAG_NAVIGATION);
                    selectBottomNavigation = REConstants.NAVIGATION;
                    isNavigationTab = true;
                });*/
                showGooglePolicyDialog(getString(R.string.text_bg_permission));
            } else {
                selectBottomNavigationTab(new RENavigationRootFragment(), true, TAG_NAVIGATION);
                selectBottomNavigation = REConstants.NAVIGATION;
                isNavigationTab = true;
            }
        } else {
            selectBottomNavigationTab(new RENavigationRootFragment(), true, TAG_NAVIGATION);
            selectBottomNavigation = REConstants.NAVIGATION;
            isNavigationTab = true;
        }
    }

    private void showGoogleMapActivity() {
        if (checkAppInstalled()) {
            /*Open google map for current location*/
            //startActivity(new Intent(getActivity(), NavigationMapsActivity.class));
            showMap();
        } else {
            navigateToPlayStore();
        }
    }

    private void navigateToPlayStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
        startActivity(intent);
    }


    public String getSharedNavigationId() {
        return sharedNavigationId;
    }

    public boolean getSharedNavigationDetails() {
        return mIsNavigationDetails;
    }


    public String setSharedNavigationId() {
        return sharedNavigationId = null;
    }

    private boolean checkAppInstalled() {
        return REUtils.isAppInstalled(REConstants.GOOGLE_MAP_PACKAGENAME);
    }

    private void showMap() {
        //startActivity(new Intent(this, NavigationMapsActivity.class));
        Location loc = REUtils.getLocationFromModel();
        //   Log.e("LAT", loc.getLatitude() + "");
        String ltlng = "geo:37.7749,-122.4194";
        if (loc != null && loc.getLatitude() != 0.0) {
            ltlng = "geo:" + loc.getLatitude() + "," + loc.getLongitude();
        }
        Uri gmmIntentUri = Uri.parse(ltlng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private boolean isBikeSupportTBT() {
        /*check if Bike support TBT return true else false*/
        return false;
    }

    private void showMotorcycleScreen() {
        Bundle params1 = new Bundle();
        params1.putString("eventCategory", "Motorcycles");
        params1.putString("eventAction", "Motorcycles click");
        REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params1);
        if (REApplication.getInstance().getUserLoginDetails() != null && REServiceSharedPreference.getVehicleData(this) != null
                && REServiceSharedPreference.getVehicleData(this).size() > 0||REUtils.CHECK_VEHICLE_SYNCING_FAILED||REUtils.CHECK_VEHICLE_PENDING) {
            MotorCycleFragment mMotorCycleFragment = new MotorCycleFragment().newInstance(this);
            Bundle bundle = new Bundle();
            bundle.putBoolean(REConstants.IS_SERVICE_NOTIFICATION, mIsServiceNotification);
            mMotorCycleFragment.setArguments(bundle);
            selectBottomNavigationTab(mMotorCycleFragment, true, TAG_MOTORCYCLE);
        } else {
            selectBottomNavigationTab(new MotorCycleFragment().newInstance(this), true, TAG_MOTORCYCLE);
         //   if (!REUtils.CHECK_VEHICLE_PENDING&&!REUtils.CHECK_VEHICLE_SYNCING_FAILED)
             //   showVehiclePopUpInCaseOfNoBike();
        }
    }

    public void openCommunityTab() {
        bottom_navigation.findViewById(R.id.item_community).performClick();
    }

    private void selectBottomNavigationTab(Fragment fragmentInstance, boolean showHeader, String tag) {
        fragment = fragmentInstance;
        constraintHeader.setVisibility(showHeader ? View.VISIBLE : View.GONE);
        //loadFragment(fragment);
        changeFragment(fragment, tag);
    }

    public void changeFragment(Fragment fragment, String tagFragmentName) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.frame_container_home, fragmentTemp, tagFragmentName);
        } else {
            if (tagFragmentName.equals(TAG_MOTORCYCLE)) {
                fragmentTemp = fragment;
                fragmentTransaction.add(R.id.frame_container_home, fragmentTemp, tagFragmentName);
            } else
                fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
		getLogData();
		onFirebaseHTMLPageUrls();
		Log.e("RE_LOGGER","Dashboard opened");
        checkForUpdates();
        getCountryData();
        getIntentData();
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
				new IntentFilter("motorcycle_data_receiver"));
        FirestoreManager.getInstance().getSecretKey(new OnFirestoreCallback() {
            @Override
            public void onFirestoreSuccess() {
            }
            @Override
            public void onFirestoreFailure(String message) {
            }
        });
        initViews();
        String user = getCurrentLoggedInUser();
        REUtils.setUserModel();
        toBeShownOnLockScreen();
        homeActivityPresenter = new HomeActivityPresenter( new HomeActivityInteractor(),this);
        if (user != null) {
            if (REApplication.getInstance().isComingFromVehicleOnboarding()) {
                REApplication.getInstance().setComingFromVehicleOnboarding(false);
                bottom_navigation.findViewById(R.id.item_motorcycle).performClick();
            } else {
                if (getIntent().hasExtra(PUSH_REMOTE_MESSAGE)) {
                    appIndexManager.setAppIndexListener(this);
                    appIndexManager.navigate(pushPayLoadJson);
                }
//                else
//                if(REUtils.getFeatures().getDefaultFeatures()!=null&&REUtils.getFeatures().getDefaultFeatures().getMotorcycleInfo().equalsIgnoreCase(FEATURE_ENABLED))
//                    showPopupWithDelay();
            }
        }
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				enableTheLocation();
			}
		},1000);
//       TODO check for location if popup is not visible enableTheLocation();

        getDeepLinkSharedData();

        // BCT share commented as changed into deeplink
        // getCloudSharedData();
    }

	// Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broadcasted.
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Get extra data included in the Intent
			Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_container_home);
			if(f instanceof MotorCycleFragment){
				((MotorCycleFragment) f).refreshData();
			}
		}
	};


	private void getLogData() {
		try {
			ArrayList<LogResponse> responses = REApplication.getInstance().arrayListResponse;
			if (responses.size() > 0) {
				double totaltime = 0.0;
				for (int i = 0; i < responses.size(); i++) {
					totaltime += responses.get(i).getStartTime();
					Log.e("RE_LOGGER", responses.get(i).getText() + " " + responses.get(i).getStartTime());
				}
				Log.e("RE_LOGGER", totaltime + "");
				Type baseType = new TypeToken<List<LogResponse>>() {
				}.getType();
				String data = new Gson().toJson(responses);
				REUtils.sendResponseLog(REApplication.getInstance().flow, data, (double)totaltime / 1000);
			}
			REApplication.getInstance().arrayListResponse.clear();
		}
		catch (Exception e){

        }
    }


    private void toBeShownOnLockScreen() {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
            setShowWhenLocked(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            );
        }
    }

    private void checkForPendingProvisioningData() {
        try {
            boolean isPending = REPreference.getInstance().getBoolean(REApplication.getAppContext(), KEY_UPDATE_PROVISIONING);
            if (isPending) {
                buildProvisioningTableData();
            }
        } catch (PreferenceException e) {
            e.printStackTrace();
        }
    }

    private void buildProvisioningTableData() {
        try {
            ContactData contactData = REUserModelStore.getInstance().getProfileData().getContactDetails();
            List<String> list = REUtils.getConnectedBike(this);
            if (contactData != null && list.size() > 0) {
                ProvisioningData provisioningData = new ProvisioningData();
                ProvisioningData.EmergencyNoDetails emergencyNoDetails = new ProvisioningData.EmergencyNoDetails();
                emergencyNoDetails.setCallingCode(contactData.getMobile().getEmergency().getCallingCode());
                emergencyNoDetails.setEmergencyNo(contactData.getMobile().getEmergency().getNumber());
                ProvisioningData.PrimaryNoDetails primaryNoDetails = new ProvisioningData.PrimaryNoDetails();

                primaryNoDetails.setCallingCode(contactData.getMobile().getPrimary().getCallingCode());
                primaryNoDetails.setPrimaryNo(contactData.getMobile().getPrimary().getNumber());
                provisioningData.setNewPrimaryNoDetails(primaryNoDetails);
                provisioningData.setNewEmergencyNoDetails(emergencyNoDetails);

                provisioningData.setBikesOwned(list);
                String data = new Gson().toJson(provisioningData);
                REUtils.startWorker(data);
            }

        } catch (Exception e) {

        }

    }


    private void getIntentData() {

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null&&!wasLaunchedFromRecents()) {
            mIsServiceNotification = intent.getBooleanExtra(REConstants.IS_SERVICE_NOTIFICATION, false);
            Gson gson = new Gson();
            pushPayLoadJson = gson.fromJson(intent.getStringExtra(PUSH_REMOTE_MESSAGE),
                    new TypeToken<JsonObject>() {
                    }.getType());
          //  REUtils.trackACM(intent);
        }

    }

	boolean wasLaunchedFromRecents(){
		return (getIntent().getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY;
	}


//    private void initCloudManager() {
//        try {
//            Log.e("test", "Test clocud cmamnager token = " + REUserModelStore.getInstance().getJwtAccessToken());
//            cloudManager = CloudManager.Companion.createInstance(CloudConfigurationProvider.INSTANCE.getCloudConfiguration());
//            JsonWebToken jsonWebToken = new JsonWebToken(REUserModelStore.getInstance().getJwtAccessToken());
//            cloudManager.setJsonWebToken(jsonWebToken);
//            Duration duration = new Duration(50, TimeUnit.SECONDS);
//            NetworkTimeoutConfiguration networkTimeoutConfiguration = new NetworkTimeoutConfiguration(duration, duration, duration, duration);
//            cloudManager.setNetworkTimeoutConfiguration(networkTimeoutConfiguration);
//            REApplication.getInstance().setCloudManager(cloudManager);
//            Log.e("test", "Test clocud mamnager login called" + REApplication.getInstance().getCloudManager());
//        } catch (CloudException e) {
//            Log.e("test", "Cloud Exception = " + e.getHttpErrorMessage());
//        }
//    }

    private void getDeepLinkSharedData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mIsNavigationDeepLink = intent.getBooleanExtra(REConstants.IS_NAVIGATION_DEEPLINK, false);
            Log.e("BCT Deeplink data", "" + mIsNavigationDeepLink);

        }
      /*  if (!(REServiceSharedPreference.getDeepLinkData(this).equals(""))) {
            bottom_navigation.findViewById(R.id.item_navigate).performClick();
        } else if (intent != null && mIsNavigationDeepLink) {
            sharedDeepLink = intent.getStringExtra(REConstants.NAVIGATION_DEEPLINK);
            bottom_navigation.findViewById(R.id.item_navigate).performClick();
        }*/

        if (intent != null && mIsNavigationDeepLink) {
            sharedDeepLink = intent.getStringExtra(REConstants.NAVIGATION_DEEPLINK);
            Log.e("BCT Deeplink data", "" + sharedDeepLink);
            bottom_navigation.findViewById(R.id.item_navigate).performClick();
        }
    }

    private void getCloudSharedData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mIsNavigationNotification = intent.getBooleanExtra(REConstants.IS_NAVIGATION_NOTIFICATION, false);
            mIsNavigationDetails = intent.getBooleanExtra(IS_NAVIGATION_DETAILS, false);
            sharedNavigationId = intent.getStringExtra(REConstants.NAVIGATION_NOTIFICATION);
            //  Log.e("test", "shreadid = " + sharedNavigationId);
        }

        if (intent != null && mIsNavigationNotification) {
            sharedNavigationId = intent.getStringExtra(REConstants.NAVIGATION_NOTIFICATION);
            bottom_navigation.findViewById(R.id.item_navigate).performClick(); // Directing to Navigation page
        }
    }


    private String getCurrentLoggedInUser() {
        LoginResponse loginResponse = REApplication.getInstance().getUserLoginDetails();
        if (loginResponse != null) {
            return REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId();
        } else {
            return null;
        }
    }

//    private void showPopUpWithSomeDelay() {
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                /*showing  on boarding popup for first log in*/
//                showRequiredPopUPDialog();
//            }
//        }, 3000);
//    }

    public void showHideREHeader(boolean value) {
        constraintHeader.setVisibility(value ? View.VISIBLE : View.GONE);
    }

//    private void showRequiredPopUPDialog() {
//        try {
//            if (REApplication.getInstance().getUserLoginDetails() != null
//                    && REApplication.getInstance().getIsLoggedInUser()) {
//                if (REServiceSharedPreference.getVehicleData(this)!=null&&REServiceSharedPreference.getVehicleData(this).size() > 0) {
//                    if (REApplication.getInstance().getUserSettingFirestoreModel().isShowUserValidationPopup()) {
//                        showUserDataValidationPopUp();
//                    }
//                } else {
//                    if (REApplication.getInstance().getUserSettingFirestoreModel()!=null&&REApplication.getInstance().getUserSettingFirestoreModel().isShowVehicleOnboardingPopup()) {
//                        showVehicleOnBoardingPopUp();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            RELog.e(e);
//        }
//
//    }

    private void showVehicleOnBoardingPopUp() {
//        Map<String, Object> popupFlags = new HashMap<>();
//        popupFlags.put(REConstants.SHOW_USER_VALIDATION_POPUP, false);
//        popupFlags.put(REConstants.SHOW_VEHICLE_ONBOARDING_POPUP, false);
      //  FirestoreManager.getInstance().updateUserSettingFirebaseField(popupFlags);
        reOnBoardingDialog = REAddVehicleDialogUtil.showOnBoardingDialog(this, getSupportFragmentManager());

        try {
            getSupportFragmentManager().executePendingTransactions();
        } catch (Exception e) {
            RELog.e(e);
        }
        if (!reOnBoardingDialog.isAdded()) {
            if (reOnBoardingDialog.getDialog() != null && reOnBoardingDialog.getDialog().isShowing()) {
                //do nothing
            } else {
                reOnBoardingDialog.show(getSupportFragmentManager(), getString(R.string.add_vehicle_Dialog));
            }
        }
    }

    public void showVehiclePopUpInCaseOfNoBike() {
        reOnBoardingDialog = REAddVehicleDialogUtil.showOnBoardingDialog(this, getSupportFragmentManager());
        if (!reOnBoardingDialog.isAdded()) {

            if (reOnBoardingDialog.getDialog() != null && reOnBoardingDialog.getDialog().isShowing()) {
                //do nothing
            } else {
                reOnBoardingDialog.show(getSupportFragmentManager(), getString(R.string.add_vehicle_Dialog));
            }
        }
    }

    private void showUserDataValidationPopUp() {
        startActivityForResult(new Intent(this, TransparentPopActivity.class)
                , Constants.POP_UP_REQUEST_CODE);
        // disable the profile and notification icon
        new Handler().postDelayed(() -> {
            mProfileImage.setAlpha((float) 0.5);
            mNotificationsImage.setAlpha((float) 0.5);
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && requestCode == LoginActivity.CODE_PROFILE_ACTIVITY) {
                //            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_MOTORCYCLE);
                //            if(fragment != null)
                //                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                showProfileActivity();
            } else if (resultCode == RESULT_OK && requestCode == LoginActivity.CODE_NOTIFICATION_ACTIVITY) {
                startExplicitActivity(NotificationsActivity.class);
            } else if (resultCode == RESULT_OK && requestCode == Constants.POP_UP_REQUEST_CODE) {
                finish();
            } else if (resultCode == RESULT_OK && requestCode == LoginActivity.CODE_NAVIGATION_SCREEN) {
                showNavigationScreen();
            }
        } catch (Exception e) {
            RELog.e(e);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getDeepLinkSharedData();
        this.doubleBackToExitPressedOnce = false;
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(REConstants.PUSH_NOTIFICATION));
        mProfileImage.setAlpha((float) 1.0);
        mNotificationsImage.setAlpha((float) 1.0);
        if (selectBottomNavigation != null) {
            if (selectBottomNavigation.equals(REConstants.MOTORCYCLE) && !isFromAppIndex) {
                bottom_navigation.findViewById(R.id.item_motorcycle).performClick();
            } else if (selectBottomNavigation.equals(REConstants.COMMUNITY)) {
                bottom_navigation.findViewById(R.id.item_community).performClick();
            } else if (selectBottomNavigation.equals(REConstants.CONFIGURATION)) {
                bottom_navigation.findViewById(R.id.item_configuration).performClick();
            } else if (selectBottomNavigation.equals(REConstants.NAVIGATION)) {
                hideLoading();
                bottom_navigation.findViewById(R.id.item_navigate).performClick();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String user = getCurrentLoggedInUser();
        if (user != null) {
            REUtils.setUserModel();
        }
        getDeepLinkSharedData();
        if (intent != null && intent.getExtras() != null) {
            mIsServiceNotification = intent.getBooleanExtra(REConstants.IS_SERVICE_NOTIFICATION, false);
            Gson gson = new Gson();
            pushPayLoadJson = gson.fromJson(intent.getStringExtra(PUSH_REMOTE_MESSAGE),
                    new TypeToken<JsonObject>() {
                    }.getType());
        }
        //If any push notification arrives App will be indexed to respective screen with data
        appIndexManager.setAppIndexListener(this);
        appIndexManager.navigate(pushPayLoadJson);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {

		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
		super.onDestroy();
    }


    /**
     * Initialising views
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        //mScrollViewHome = findViewById(R.id.scroll_view_home);
        ImageView mBluetoothScan = findViewById(R.id.bluetooth_scan);
        mBluetoothScan.setOnClickListener(this);
        mNotificationsImage = findViewById(R.id.imageview_notifications);
        mNotificationsImage.setOnClickListener(this);
        mRSAImage = findViewById(R.id.imageView_rsa);
        mRSAImage.setOnClickListener(this);
        mTabIndicatorLinear = findViewById(R.id.linear_tabindicator);
        mCreateRideConstraint = findViewById(R.id.createride_constraint);
        mServiceDIYConstraint = findViewById(R.id.service_diy);
        mCreateRideConstraint.setOnClickListener(this);
        mServiceDIYConstraint.setOnClickListener(this);
        mProfileImage = findViewById(R.id.imageView_profile);
        mProfileImage.setOnClickListener(this);
        mReTitleImage = findViewById(R.id.imageView_retitle_image);
        mReTitleImagePip = findViewById(R.id.imageView_retitle_image_pip);
        lHeaderLayout = findViewById(R.id.ll_header);
        tvStart = findViewById(R.id.tv_start);
        tvMiddle = findViewById(R.id.tv_middle);
        tvEnd = findViewById(R.id.tv_end);
        constraintHeader = findViewById(R.id.constraint_header);
        frameContainer = findViewById(R.id.frame_container_home);
        frameContainerServicing = findViewById(R.id.frame_container_servicing);
        //Sets the touch listener for the home screen tab navigations.
        lHeaderLayout.setOnTouchListener(new REHeaderSwipeTouchListener(getApplicationContext(), this));
        tvStart.setOnTouchListener(new REHeaderSwipeTouchListener(getApplicationContext(), this));
        tvMiddle.setOnTouchListener(new REHeaderSwipeTouchListener(getApplicationContext(), this));
        tvEnd.setOnTouchListener(new REHeaderSwipeTouchListener(getApplicationContext(), this));
        // setupHomeScreenViewPager();
        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setItemIconTintList(null);
        bottom_navigation.setOnNavigationItemSelectedListener(this);
        // get menu from navigationView
        Menu menu = bottom_navigation.getMenu();
        configFeature = REUtils.getConfigFeatures();
        // find MenuItem you want to change
        MenuItem nav_Navigation = menu.findItem(R.id.item_navigate);
        if (configFeature != null && configFeature.getTBT() != null) {
            if (configFeature.getTBT().getTbtReleaseFeatureStatus().equalsIgnoreCase(REConstants.FEATURE_STATUS_DISABLED)) {
                isTBTDisabled = true;
            }
        }
//        if (configFeature != null && configFeature.getMyo() != null) {
//            if (configFeature.getMyo().getFeatureStatus().equalsIgnoreCase(REConstants.FEATURE_STATUS_DISABLED)) {
        MenuItem nav_myo = menu.findItem(R.id.item_configuration);
        nav_myo.setTitle(getResources().getString(R.string.store));
        //   nav_Navigation.setIcon(getResources().getDrawable(R.drawable.ic_bottom_location_final));
        //      }
        if (isTBTDisabled) {
            nav_Navigation.setTitle(getResources().getString(R.string.tab_re_maps));
            nav_Navigation.setIcon(getResources().getDrawable(R.drawable.ic_bottom_location_final));
        }
        // load the motorcycle fragment by default
        setDefaultHomePage();
    }

    private void setDefaultHomePage() {
        if (REApplication.getInstance().featureCountry == null) {
            REApplication.getInstance().featureCountry = REUtils.getFeatures().getDefaultFeatures();
        }
        if (REApplication.getInstance().featureCountry != null && REApplication.getInstance().featureCountry.getMotorcycleInfo().equalsIgnoreCase(FEATURE_DISABLED)) {
            bottom_navigation.getMenu().removeItem(R.id.item_motorcycle);
        }
        if (REApplication.getInstance().featureCountry != null && REApplication.getInstance().featureCountry.getCommunity().equalsIgnoreCase(FEATURE_DISABLED)) {
            bottom_navigation.getMenu().removeItem(R.id.item_community);
        } else {
            if (REUtils.getFeatures().getDefaultFeatures() != null && REUtils.getFeatures().getDefaultFeatures().getShowOurWorldWebV2().equalsIgnoreCase(FEATURE_DISABLED)) {

                Menu menu = bottom_navigation.getMenu();
                MenuItem menuItem = menu.getItem(0);
                menuItem.setTitle(getString(R.string.tab_re_community_tab));
                menuItem.setIcon(R.drawable.community_selector);

            }

        }
        if (REApplication.getInstance().featureCountry != null && REApplication.getInstance().featureCountry.getMIY().equalsIgnoreCase(FEATURE_DISABLED)) {
            bottom_navigation.getMenu().removeItem(R.id.item_configuration);
        }
        if (REUtils.getFeatures().getDefaultFeatures() != null && REUtils.getFeatures().getDefaultFeatures().getShowRideWebV2().equalsIgnoreCase(FEATURE_DISABLED)) {
            bottom_navigation.getMenu().removeItem(R.id.item_rides);
        }

        if(bottom_navigation.getMenu().size()<=1)
            bottom_navigation.setVisibility(View.GONE);
        if (!REServiceSharedPreference.getDeepLinkData(this).equals("")) {
            bottom_navigation.findViewById(R.id.item_navigate).performClick();
        } else {
             if (!mIsNavigationNotification && REApplication.getInstance().isComingFromVehicleOnboarding()) {
                REApplication.getInstance().setComingFromVehicleOnboarding(false);
                bottom_navigation.findViewById(R.id.item_community).performClick();
            } else if (!mIsNavigationNotification) {
                if (selectBottomNavigation != null && selectBottomNavigation.equals(REConstants.NAVIGATION)) {
                    return;
                } else {
                    if (REApplication.getInstance().featureCountry != null && REApplication.getInstance().featureCountry.getCommunity().equalsIgnoreCase(FEATURE_ENABLED))
                        bottom_navigation.findViewById(R.id.item_community).performClick();
                    else
                        bottom_navigation.findViewById(R.id.item_navigate).performClick();
                }
            } else if (mIsNavigationNotification) {
                bottom_navigation.findViewById(R.id.item_navigate).performClick();
            }
        }
    }

    /**
     * Sets the Home screen view pager tabs for the app.
     */
    public void setupHomeScreenViewPager() {
        lHeaderLayout.setVisibility(View.VISIBLE);
        //Viewpager Initialization
//        mViewPager = findViewById(R.id.view_pager_home);
//        mViewPager.setOffscreenPageLimit(2);
//        mViewPager.addOnPageChangeListener(this);
        // Initialising ViewPager adapter
        //initViewPagerAdapter();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_navigation_menu, menu);
        this.menu = menu;
        return true;
    }


    /**
     * Show Notification icon in header and Hide DIY, RSA icons
     */
    public void showNotificationIcon() {
        hideIconAnim(mRSAImage);
        showIconAnim(mNotificationsImage);
    }


    /**
     * Show DIY & RSA icons and Hide notification icon
     */
    public void showDIYAndRSA() {
        hideIconAnim(mNotificationsImage);
        showIconAnim(mRSAImage);
    }


    /**
     * Does fade hide Animation
     *
     * @param mView : Constraint Layout
     */
    private void hideIconAnim(final ImageView mView) {
        mView.setVisibility(View.GONE);
    }

    /**
     * Does fade showAnimation
     *
     * @param mView : COnstraintLayout
     */
    private void showIconAnim(final ImageView mView) {
        mView.setVisibility(View.VISIBLE);
    }

    /**
     * Starts the Create Ride activity.
     */
    private void startExplicitActivity(Class activity) {
        startActivity(new Intent(getApplicationContext(), activity));
        overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_profile:
                showUserProfile();
                break;
            case R.id.imageview_notifications:
                showNotification();
                break;
            case R.id.imageView_rsa:
                checkAndRequestMyPermissions(false);
                break;
            case R.id.createride_constraint:
                startExplicitActivity(CreateRideActivity.class);
                break;
            case R.id.service_diy:
                showDIYScreen();
                break;
            case R.id.bluetooth_scan:
                startExplicitActivity(BleSearchActivity.class);
                break;
            case R.id.add_vehicle_no:
            case R.id.cancel:
                bottom_navigation.findViewById(R.id.item_community).performClick();
                break;
        }
    }


    public void mShowOrHideBottomNavigation(boolean value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bottom_navigation.getMenu().size() <= 1)
                    bottom_navigation.setVisibility(View.GONE);
                else
                    bottom_navigation.setVisibility(value ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void showDIYScreen() {
        if (REUtils.isAppInstalled(REConstants.YOUTUBE_PACKAGENAME)) {
            startExplicitActivity(DoItYourSelfActivity.class);
        } else {
            REUtils.showErrorDialog(this, getApplicationContext().
                    getResources().getString(R.string.diy_youtube_install_error));
        }
    }

    private void showNotification() {
        if (REUtils.isUserLoggedIn()) {
            startExplicitActivity(NotificationsActivity.class);
        } else {
            startActivityForResult(new Intent(this, UserOnboardingActivity.class), LoginActivity.CODE_NOTIFICATION_ACTIVITY);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void showUserProfile() {
        if (REUtils.isUserLoggedIn()) {
            showProfileActivity();
        } else {
            REUtils.refreshVehicle = true;
            startActivityForResult(new Intent(this, UserOnboardingActivity.class), LoginActivity.CODE_PROFILE_ACTIVITY);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void showProfileActivity() {
        startActivity(new Intent(this, REProfileActivity.class));
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }


    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onSwipeLeft() {

    }

    @Override
    public void onSwipeRight() {
    }

    @Override
    public void onClick(int view) {

    }

    @Override
    public void showOurWorld(JsonObject payloadJson) {
        isFromAppIndex = true;
        mReAppIndexScreen = REAppIndexScreen.OUR_WORLD;

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                bottom_navigation.findViewById(R.id.item_community).performClick();
            }
        });
    }

    @Override
    public void showRides(JsonObject payloadJson) {
        isFromAppIndex = true;
        mReAppIndexScreen = REAppIndexScreen.RIDES;
        if (REUtils.getFeatures().getDefaultFeatures() != null && REUtils.getFeatures().getDefaultFeatures().getShowRideWebV2().equalsIgnoreCase(FEATURE_DISABLED)) {
			runOnUiThread(() -> bottom_navigation.findViewById(R.id.item_community).performClick());
             }
		else
         	runOnUiThread(() -> bottom_navigation.findViewById(R.id.item_rides).performClick());
	}

    @Override
    public void showMotorCycle(JsonObject payloadJson) {
        isFromAppIndex = true;
        mIsServiceNotification = false;
        mReAppIndexScreen = REAppIndexScreen.MOTORCYCLE;
		runOnUiThread(() -> bottom_navigation.findViewById(R.id.item_motorcycle).performClick());

    }

    @Override
    public void showSpecificMotorCycle(JsonObject payloadJson) {
        mReAppIndexScreen = REAppIndexScreen.SPECIFIC_MOTORCYCLE;
        updateSpecificMotorcycleWithdata(payloadJson);
    }

    @Override
    public void showDRSA(@org.jetbrains.annotations.Nullable JsonObject payloadJson) {
        mReAppIndexScreen = REAppIndexScreen.DRSA;
        updateSpecificMotorcycleWithdata(payloadJson);
    }

    @Override
    public void showOwnerManual(@org.jetbrains.annotations.Nullable JsonObject payloadJson) {
        mReAppIndexScreen = REAppIndexScreen.OWNERS_MANUAL;
        updateSpecificMotorcycleWithdata(payloadJson);
    }

    @Override
    public void showService(JsonObject payloadJson) {
        isFromAppIndex = true;
        mIsServiceNotification = false;
        mReAppIndexScreen = REAppIndexScreen.SERVICE;
        updateSpecificMotorcycleWithdata(payloadJson);
        // updateServiceStatus(payloadJson);
    }

    @Override
    public void showNavigation(JsonObject payloadJson) {
        isFromAppIndex = true;
		runOnUiThread(() -> bottom_navigation.findViewById(R.id.item_navigate).performClick());
	}

    @Override
    public void showMakeItYours(JsonObject payloadJson) {
        isFromAppIndex = true;
		runOnUiThread(() -> bottom_navigation.findViewById(R.id.item_configuration).performClick());
    }

    //if the user presses the home or recent button from the Android device
    @Override
    public void onUserLeaveHint() {
        try {
            Display d = getWindowManager()
                    .getDefaultDisplay();
            Point p = new Point();
            d.getSize(p);
            int width = p.x;
            int height = p.y;

            Rational ratio
                    = new Rational(width, height);
            Rect rect = new Rect(0, 0, 0, 0);
            PictureInPictureParams.Builder
                    pip_Builder
                    = new PictureInPictureParams
                    .Builder();
            pip_Builder.setAspectRatio(ratio).build();
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) && (REApplication.getInstance().isNavigationInProgress()) && REUtils.isNetworkAvailable(getApplicationContext())) {
                // Start the pip screen for the app from Navigation module.
                enterPictureInPictureMode(pip_Builder.build());
            }
        } catch (Exception e) {
            Log.e("onPictureInPictureModeChanged", "CRASHINGGG >>>>");
            e.printStackTrace();
        }
    }


    // If app is in picture in picture mode, hide the header view
    public void mShowOrHideActionbaritems(boolean value) {
        constraintHeader.setVisibility(value ? View.GONE : View.VISIBLE);
    }

    private void updateSpecificMotorcycle(JsonObject payloadJson) {
        try {
            String chassisNumber = "", regNo = "";
            if (payloadJson.has(REConstants.NOTIF_CHASSIS_NUMBER)) {
                chassisNumber = payloadJson.get(REConstants.NOTIF_CHASSIS_NUMBER).isJsonNull() ? "" : payloadJson.get(REConstants.NOTIF_CHASSIS_NUMBER).getAsString();

            }
            Intent registrationComplete = new Intent(REConstants.PUSH_NOTIFICATION_SPECIFIC_MOTORCYCLE);
            registrationComplete.putExtra(REConstants.CHASSIS_NO, chassisNumber);
            if (!chassisNumber.isEmpty()) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_container_home);
                if (f instanceof MotorCycleFragment) {
                    ((MotorCycleFragment) f).getServiceData(chassisNumber, "", "");


                }

            }
        } catch (Exception e) {
            RELog.e(e);
        }
    }



    /**
     * Listener for the updating the service jobCart status by PushNotification.
     */
    public interface OnPushNotificationUpdateListener {
        void updateJobCartStatus(String jobCartStatus, String regNo);
    }

    @Override
    public void onBackPressed() {

        if (!isNavigationTab) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1)
                getSupportFragmentManager().popBackStackImmediate();
            else {

                FragmentManager fm = getSupportFragmentManager();
                for (Fragment frag : fm.getFragments()) {
                    if (frag instanceof MotorCycleFragment) {
                        if (frag.isVisible()) {
                            FragmentManager childFm = frag.getChildFragmentManager();
                            if (childFm.getBackStackEntryCount() > 0) {
                                childFm.popBackStack();
                                return;
                            } else {
                                doubleBackPress();
                                return;
                            }
                        }
                    }
                }

                doubleBackPress();

            }


        } else {
            if (doubleBackToExitPressedOnce) {
                REApplication.getInstance().clearAllModelStore();
                finish();
                return;
            }
            String aMessage = "";
            if (isNavigationTab && REApplication.getInstance().isNavigationInProgress()) {
                aMessage = getString(R.string.text_nav_back_exit);
                this.doubleBackToExitPressedOnce = false;
            } else {
                aMessage = getString(R.string.text_back_exit);
                this.doubleBackToExitPressedOnce = true;
            }
            REUtils.showMessageDialog(this, aMessage, new REUtils.OnDialogButtonsClickListener() {
                @Override
                public void onOkCLick() {
                    if (isNavigationTab && REApplication.getInstance().isNavigationInProgress()) {
                        REApplication.getInstance().clearAllModelStore();
                        REApplication.getInstance().setNavigationInProgress(false);
                        finish();
                    }
                }

                @Override
                public void onCancelClick() {

                }
            });
        }
    }


    private void doubleBackPress() {
        if (doubleBackToExitPressedOnce) {
            REApplication.getInstance().clearAllModelStore();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        REUtils.showErrorDialog(this, getApplicationContext().getResources().
                getString(R.string.text_back_exit));
    }

    public void setDoublePressToExitFlag(boolean value) {
        doubleBackToExitPressedOnce = value;
    }

    public void showGooglePolicyDialog(String message) {
        try {
            if (myGooglePolicyAlertDialog != null && myGooglePolicyAlertDialog.isShowing()) return;
            TextView alert_message, alert_ok;
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            myGooglePolicyAlertDialog = alert.create();
            myGooglePolicyAlertDialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myGooglePolicyAlertDialog.dismiss();
                    selectBottomNavigationTab(new RENavigationRootFragment(), true, TAG_NAVIGATION);
                    selectBottomNavigation = REConstants.NAVIGATION;
                    isNavigationTab = true;
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    public void updateSpecificMotorcycleWithdata(JsonObject data){
        isFromAppIndex = true;
        mIsServiceNotification = false;
		runOnUiThread(() -> bottom_navigation.findViewById(R.id.item_motorcycle).performClick());
		updateSpecificMotorcycle(data);
    }

    public void initAppIndexManager(JsonObject jsonObject) {
        appIndexManager.setAppIndexListener(this);
        appIndexManager.navigate(jsonObject);
    }

    @Override
    public void openWebView(@org.jetbrains.annotations.Nullable JsonObject payloadJson) {
        if (payloadJson.has(REConstants.NOTIF_CAMPAIGN_URL)) {
            String url = payloadJson.get(REConstants.NOTIF_CAMPAIGN_URL).getAsString();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }

    }
}
