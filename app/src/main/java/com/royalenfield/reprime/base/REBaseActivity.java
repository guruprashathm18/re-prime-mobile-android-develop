package com.royalenfield.reprime.base;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;
import static com.royalenfield.reprime.preference.REPreference.DEFAULT_STRING;
import static com.royalenfield.reprime.utils.REConstants.JWT_TOKEN_KEY;
import static com.royalenfield.reprime.utils.REConstants.NAVIGATION_BG_LOC_PERMISSION_REQUEST;
import static com.royalenfield.reprime.utils.REConstants.NAVIGATION_CALL_PERMISSIONS_REQUESTS;
import static com.royalenfield.reprime.utils.REConstants.REQUEST_UPDATE;
import static com.royalenfield.reprime.utils.REConstants.REQUEST_UPDATE_IMMIDIATE;
import static com.royalenfield.reprime.utils.REUtils.isFCMTokenSent;
import static com.royalenfield.reprime.utils.REUtils.isUserLoggedIn;
import static com.royalenfield.reprime.utils.REUtils.navigateToSplash;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.royalenfield.firebase.fcm.FCMTokenRegistrationInteractor;
import com.royalenfield.firebase.fcm.FCMTokenRegistrationListener;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.fireStore.OnFirestoreVehicleDataMappingCallback;
import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.proxy.firebase.FirebaseTokenRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.profile.ProfileData;
import com.royalenfield.reprime.network.listener.INetworkStateListener;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.homescreen.interactor.HomeActivityInteractor;
import com.royalenfield.reprime.ui.home.homescreen.listener.FirebaseCustomTokenListener;
import com.royalenfield.reprime.ui.home.homescreen.listener.OnRealtimeVehicledetailListner;
import com.royalenfield.reprime.ui.home.homescreen.presenter.HomeActivityPresenter;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.rides.activity.InRideActivity;
import com.royalenfield.reprime.ui.home.service.REServicingRootActivity;
import com.royalenfield.reprime.ui.home.service.rsa.RETelephonyManager;
import com.royalenfield.reprime.ui.home.service.rsa.listner.IRETelephoneManager;
import com.royalenfield.reprime.ui.home.service.search.SearchActivity;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.LogResponse;
import com.royalenfield.reprime.ui.onboarding.address.activity.AddAddressActivity;
import com.royalenfield.reprime.ui.onboarding.editprofile.listeners.OnEditProfileFinishedListener;
import com.royalenfield.reprime.ui.onboarding.editprofile.listeners.OnUserInfoFirebaseListener;
import com.royalenfield.reprime.ui.onboarding.facebooklogin.FBUser;
import com.royalenfield.reprime.ui.onboarding.facebooklogin.GetUserCallback;
import com.royalenfield.reprime.ui.onboarding.facebooklogin.UserRequest;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.onboarding.userprofile.UserProfileActivity;
import com.royalenfield.reprime.ui.onboarding.verifyauthtoken.interactor.VerifyAuthTokenInteractor;
import com.royalenfield.reprime.ui.onboarding.verifyauthtoken.view.VerifyTokenView;
import com.royalenfield.reprime.ui.riderprofile.activity.NearestServiceCentersActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.REProfileActivity;
import com.royalenfield.reprime.ui.riderprofile.interactor.LogoutInteractor;
import com.royalenfield.reprime.ui.riderprofile.presenter.LogoutPresenter;
import com.royalenfield.reprime.ui.riderprofile.views.LogoutView;
import com.royalenfield.reprime.ui.splash.activity.MainSplashScreenActivity;
import com.royalenfield.reprime.ui.splash.presenter.FirebaseAuthListner;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REDialogUtils;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;
import com.salesforce.marketingcloud.registration.RegistrationManager;
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;
import static com.royalenfield.reprime.preference.REPreference.DEFAULT_STRING;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;
import static com.royalenfield.reprime.utils.REConstants.JWT_TOKEN_KEY;
import static com.royalenfield.reprime.utils.REConstants.NAVIGATION_BG_LOC_PERMISSION_REQUEST;
import static com.royalenfield.reprime.utils.REConstants.NAVIGATION_CALL_PERMISSIONS_REQUESTS;
import static com.royalenfield.reprime.utils.REConstants.REQUEST_UPDATE;
import static com.royalenfield.reprime.utils.REConstants.REQUEST_UPDATE_IMMIDIATE;
import static com.royalenfield.reprime.utils.REUtils.getRandomNumberInRange;
import static com.royalenfield.reprime.utils.REUtils.isFCMTokenSent;
import static com.royalenfield.reprime.utils.REUtils.isUserLoggedIn;
import static com.royalenfield.reprime.utils.REUtils.navigateToSplash;

public class REBaseActivity extends AppCompatActivity implements GetUserCallback.IGetUserResponse,
        REMvpView, IRETelephoneManager, INetworkStateListener, FCMTokenRegistrationListener, LocationListener, VerifyTokenView , FirebaseCustomTokenListener ,OnUserInfoFirebaseListener {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 12;
    public static final int CALL_PERMISSIONS_REQUESTS = 2;
    private static final int SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS = 3;
    private static final int REQUEST_CHECK_GALLERY = 99;
    private static final int PICK_IMAGE = 100;
    private static final int LOCATION_SETTINGS_REQUEST = 101;
    private static final int REQUEST_NOTIFICATION = 105;
    private Location currentLocation;
    public static final String APP_LIFE_CYCLE = "APP_LIFE_CYCLE";
    public static boolean APP_LIFE_FLAG = false;
    public static boolean isFromReschedule = false;
    private FusedLocationProviderClient client;
    private static final String TAG = REBaseActivity.class.getSimpleName();

    //The CallbackManager manages the callbacks into the FacebookSdk from an Activity's or Fragment's onActivityResult() method.
    protected CallbackManager callbackManager;

    //Views
    protected LoginButton loginButton;

    protected com.facebook.login.LoginManager fbLoginManager;
    //Common progress dialog for app
    private ProgressDialog mProgressDialog;
    private Dialog mNoInternetDialog;
    private LocationManager locationManager;
    private String mCallNumber;
    private Handler mHandler;
    private Runnable mRunnable;
public static boolean isUserActive=true;
    private boolean isNavigationPermissions = false;

    AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener updateListener;
    private int APP_UPDATE_TYPE_SUPPORTED = AppUpdateType.FLEXIBLE;
    SharedPreferences sharedpreferences;
    public static final String updatePreference = "updatepreference";
    public static final String KEY_CLOSE_TIME = "closeTimeKey";
private HomeActivityPresenter homeActivityPresenter;
	private int failureCount=0;
	private long currTime;
	private LogoutPresenter mLogoutPresenter;
	public static Context contextMain;
    public static Boolean manualDisconnectBase=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate Called");
		contextMain=this;
        appUpdateManager = AppUpdateManagerFactory.create(this);
        //creates callbackManager to handle login responses
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_base);
		if(this instanceof REHomeActivity){
Log.e("HOME","ACTIVITY");
		}
		else{
			Log.e("HOME","SPLASH");
		}
	//	enableNotifictionPermission();
	//	enableNotifictionPermission();
        loginButton = findViewById(R.id.btn_fb_login_button);
		homeActivityPresenter = new HomeActivityPresenter( new HomeActivityInteractor(),this);
	//	mLogoutPresenter = new LogoutPresenter(this, new LogoutInteractor());
		//send device token to server when if it is not sent already and failure cases
        try {
            if (isUserLoggedIn() && REApplication.getInstance().getUserLoginDetails().getData().getJwtAccessToken() != null) {
                REApplication.getInstance().getEncryptedSharedPreference(REApplication.getAppContext()).edit()
                        .putString(JWT_TOKEN_KEY, REApplication.getInstance().getUserLoginDetails().getData().getJwtAccessToken())
                        .commit();
            }
            if (isUserLoggedIn() && !isFCMTokenSent(this) && REApplication.getInstance().getRemoteConfigData() != null) {
//                if(REApplication.getInstance().getUserLoginDetails().getData().getJwtAccessToken()!=null){
//                    REApplication.getInstance().getEncryptedSharedPreference(REApplication.getAppContext()).edit()
//                            .putString(JWT_TOKEN_KEY, REApplication.getInstance().getUserLoginDetails().getData().getJwtAccessToken())
//                            .commit();
//                }
                sendFCMTokenToServer();
                //register the fcm token to Acm
				//  pushAcmKeystoServer();
                sendFCMTokentoACMServer();
            }
        } catch (PreferenceException | GeneralSecurityException | IOException e) {
            RELog.e(e);
        }
        // register local broadcast
        IntentFilter filter = new IntentFilter(REBaseActivity.APP_LIFE_CYCLE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }

    private void enableNotifictionPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
//			performAction(...);
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            //showInContextUI(...);
        } else {
            // You can directly ask for the permission.
            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_NOTIFICATION);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause Called");
        REApplication.getInstance().setIsAppInForeground(false);
        //Unregister network state change listener.
        REApplication.getInstance().setNetworkChangeListener(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            //FLEXIBLE:
                            // If the update is downloaded but not installed,
                            // notify the user to complete the update.
                            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                if (APP_UPDATE_TYPE_SUPPORTED == AppUpdateType.FLEXIBLE)
                                    popupSnackbarForCompleteUpdateAndUnregister();
                            }

                            //IMMEDIATE:
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                if (APP_UPDATE_TYPE_SUPPORTED == AppUpdateType.IMMEDIATE)
                                    handleImmediateUpdate(appUpdateManager, appUpdateInfo);
                            }
                        });

    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && updateListener != null)
            appUpdateManager.unregisterListener(updateListener);
    }

    public void checkForUpdates() {
        //1
        com.google.android.play.core.tasks.Task appUpdateInfo = appUpdateManager.getAppUpdateInfo();
        appUpdateInfo.addOnSuccessListener(result -> handleUpdate(appUpdateManager, (AppUpdateInfo) appUpdateInfo.getResult()));

    }

    private void handleImmediateUpdate(AppUpdateManager manager, AppUpdateInfo info) {

        //1
        if ((info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ||
                //2
                info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) &&
                //3
                info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
            //4
            try {
                manager.startUpdateFlowForResult(info, AppUpdateType.IMMEDIATE, this, REQUEST_UPDATE_IMMIDIATE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleFlexibleUpdate(AppUpdateManager manager, AppUpdateInfo info) {
        if ((info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ||
                info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) &&
                info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
            setUpdateAction(manager, info);
        }
    }

    private void setUpdateAction(AppUpdateManager manager, AppUpdateInfo info) {
        //1
        updateListener = state -> {
            {

                if (state.installStatus() == InstallStatus.DOWNLOADED)
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister();
                if (state.installStatus() == InstallStatus.CANCELED) {
                    Toast.makeText(REBaseActivity.this, "CANCELLED", Toast.LENGTH_SHORT).show();

                }
            }
        };


        //7
        manager.registerListener(updateListener);
        //8
        try {
            manager.startUpdateFlowForResult(info, AppUpdateType.FLEXIBLE, this, REQUEST_UPDATE);

        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

    }


    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    private void popupSnackbarForCompleteUpdateAndUnregister() {
        Snackbar snackbar =
                Snackbar.make(findViewById(android.R.id.content), "Update Downloaded", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.reddish));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }


    private void handleUpdate(AppUpdateManager manager, AppUpdateInfo info) {

        try {
            int remoteConfigVC = Integer.valueOf(REApplication.getInstance().getRemoteConfigData().getRe_prime_app_minimum_android_version());
            int versionCode = BuildConfig.VERSION_CODE;
            if (versionCode >= remoteConfigVC) {
                APP_UPDATE_TYPE_SUPPORTED = AppUpdateType.FLEXIBLE;
            } else {
                APP_UPDATE_TYPE_SUPPORTED = AppUpdateType.IMMEDIATE;
            }
        } catch (Exception e) {
            RELog.e(e);
        }
        if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            if (APP_UPDATE_TYPE_SUPPORTED == AppUpdateType.IMMEDIATE) {
                //handleImmediateUpdate(manager, info);
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            info,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            REQUEST_UPDATE_IMMIDIATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else if (APP_UPDATE_TYPE_SUPPORTED == AppUpdateType.FLEXIBLE) {
                if (!REApplication.getInstance().isCheckedOnce) {
                    //if(REUtils.getFlexibleAppUpdateTime())
                    sharedpreferences = getSharedPreferences(updatePreference,
                            Context.MODE_PRIVATE);
                    long closeTime = 0;
                    if (sharedpreferences.contains(KEY_CLOSE_TIME)) {
                        closeTime = sharedpreferences.getLong(KEY_CLOSE_TIME, 0);
                    }
                    if (closeTime != 0) {
                        if ((System.currentTimeMillis() - closeTime) > (REUtils.getFlexibleAppUpdateTime() * 60 * 60 * 1000)) {
                            handleFlexibleUpdate(manager, info);
                            REApplication.getInstance().isCheckedOnce = true;
                        }
                    } else {
                        handleFlexibleUpdate(manager, info);
                        REApplication.getInstance().isCheckedOnce = true;
                    }
                }
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
		if(!isUserActive){
			if(REUtils.isUserLoggedIn()) {
				isUserActive=true;
				Intent intent = new Intent(REBaseActivity.this, PopActivity.class);
				startActivity(intent);
			}
		}
        checkNewAppVersionState();
        Log.d(TAG, "onResume Called");
        if (this instanceof MainSplashScreenActivity) {
            //No action required for MainSplashScreenActivity
        } else if (REUtils.isUserLoggedIn()) {
            if (REApplication.getInstance()
                    .getUserTokenDetails() == null) {
                navigateToMainSplash();
            } else if (REApplication.getInstance()
                    .getUserTokenDetails().equals("")) {
                navigateToMainSplash();
            }
        }
        REApplication.getInstance().setIsAppInForeground(true);
        //Register for network state change.
        REApplication.getInstance().setNetworkChangeListener(this);
        //Checks the network state when activity is resumed
        if (REUtils.isNetworkAvailable(this)) {
            onNetworkConnect();
        } else {
            onNetworkDisconnect();
        }
    }

    /**
     * This methos is used to navigate to MainSplash if data objects are cleared by system
     */
    private void navigateToMainSplash() {
        //We are setting the available data back to modelstore

        REUserModelStore.getInstance().setUserId(REUtils.getUserId());
        //Navigating to MainSplash as system would have clear the app data.
        Intent intent = new Intent(this, MainSplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * For Setting up user facebook login.
     */
    protected void signInWithFacebook() {
        fbLoginManager = com.facebook.login.LoginManager.getInstance();
        //To respond to a login result
        fbLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //If login succeeds, the LoginResult parameter has the new AccessToken, and the most recently granted or declined permissions.
                String accessToken = loginResult.getAccessToken().getToken();
                Log.d(TAG, "accessToken : " + accessToken);
                getProfile();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook sign in cancelled");
                Toast.makeText(REBaseActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                // here write code when get error
                Log.d(TAG, "facebook sign in error");
                Toast.makeText(REBaseActivity.this, "FacebookException", Toast.LENGTH_SHORT).show();
            }
        });
        //If already login
        if (AccessToken.getCurrentAccessToken() != null) {
            Log.d(TAG, "getCurrentAccessToken : " + AccessToken.getCurrentAccessToken());
            Log.d(TAG, "getUserId : " + AccessToken.getCurrentAccessToken().getUserId());
        }
    }

    private void getProfile() {
        UserRequest.makeUserRequest(new GetUserCallback(REBaseActivity.this).getCallback());
    }

    /**
     * Sets the viewpager in REHomeActivity
     */
    public void setDataForCurrentActivity() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        if (this instanceof REHomeActivity) {
            ((REHomeActivity) this).setupHomeScreenViewPager();
        } else if (this instanceof SearchActivity) {
            ((SearchActivity) this).getServiceCenterListFromFCM();
        } else if (this instanceof REServicingRootActivity) {
            ((REServicingRootActivity) this).loadFragment();
        } else if (this instanceof NearestServiceCentersActivity) {
            ((NearestServiceCentersActivity) this).getServiceCenter();
        } else if (this instanceof AddAddressActivity) {
            ((AddAddressActivity) this).loadMapWithCurrentLocation();
        } else if (this instanceof InRideActivity) {
            ((InRideActivity) this).bindData();
        }
    }


    public void showPermissionsErrorDialog(String message, boolean isFromLocation) {
        try {
            TextView mAlertMessage, mAlertOk;
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            mAlertMessage = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            mAlertMessage.setText(message);
            mAlertOk = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            mAlertOk.setOnClickListener(v -> {
                dialog.dismiss();
                if (isFromLocation) {
                    enableTheLocation();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }


    /**
     * Permission check for call functionality
     */
    public void checkAndRequestCallPermissions(Context context, Activity activity, String number, int type,
                                               IRETelephoneManager telephoneManager) {

        mCallNumber = number;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCALL = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE);
            int permissionPhoneSTATE = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_PHONE_STATE);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionCALL != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
            }
            if (permissionPhoneSTATE != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        type);
            } else {
                new RETelephonyManager().startCallIntent(activity,
                        telephoneManager, number);
            }
        } else {
            // Runtime permission not required.
            new RETelephonyManager().startCallIntent(activity,
                    telephoneManager, number);
        }
    }

    private void handleCallPermissions(int[] grantResults, boolean isFromNavigation) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (!isFromNavigation)
                callNumber(mCallNumber);
            else {
                //After getting the Location permission , ask Background Location permission for Android 10 an 11
                getBackgroundLocationPermission();
                try {
                    REPreference.getInstance().putString(this, "Deeplink_Nav_Location", "deeplink_loc_disabled");
                } catch (PreferenceException e) {
                    RELog.e(e);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissionHandling(Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION, false);
            } else {
                permissionHandling(Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                        "", false);
            }
        }
    }

    private void handleLocationPermissions(int requestCode, int[] grantResults, boolean isFromNavigation) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (isFromNavigation) {
                try {
                    REPreference.getInstance().putString(this, "Deeplink_Nav_Location", "deeplink_loc_disabled");
                } catch (PreferenceException e) {
                    RELog.e(e);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissionHandling(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        "", "", true);
            } else {
                permissionHandling(Manifest.permission.ACCESS_FINE_LOCATION, "",
                        "", "", true);
            }
        }
    }


    /**
     * Permission handling if it is denied
     *
     * @param permission1:      String
     * @param permission2:      String
     * @param isFromLocation:   boolean
     */
    public void permissionHandling(String permission1, String permission2, String permission3, String permission4, boolean isFromLocation) {

        if ((ActivityCompat.shouldShowRequestPermissionRationale(this, permission1) || (ActivityCompat.shouldShowRequestPermissionRationale(this, permission2)) ||
                (ActivityCompat.shouldShowRequestPermissionRationale(this, permission3)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, permission4)))) {
            showPermissionsErrorDialog(getResources().getString(R.string.text_permission_necessary), isFromLocation);
        } else {
            //permission is denied (and never ask again is  checked)
            //shouldShowRequestPermissionRationale will return false
            if (isFromLocation) {
                Toast.makeText(this, getResources().getString(R.string.text_bg_location_permission), Toast.LENGTH_SHORT).show();
                setDataForCurrentActivity();
            } else {
                Toast.makeText(this, getResources().getString(R.string.text_settings_enable), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Makes call to the given number
     *
     * @param mCallNumber : String
     */
    private void callNumber(String mCallNumber) {
        new RETelephonyManager().startCallIntent(this, this,
                mCallNumber);
    }

    /**
     * For Android 11 get Background Location permission separately.
     */
    public void getBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int bgLocationSTATE = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            if (bgLocationSTATE != PackageManager.PERMISSION_GRANTED) {
                List<String> permissionList = new ArrayList<>();
                permissionList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                ActivityCompat.requestPermissions(this,
                        permissionList.toArray(new String[permissionList.size()]),
                        NAVIGATION_BG_LOC_PERMISSION_REQUEST);
            }
        }
    }

    /**
     * Checks the Runtime permissions.
     */
    public void checkAndRequestMyPermissions(boolean isFromNavigation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCALL = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE);
            int permissionPhoneSTATE = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionCALL != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
            }
            if (permissionPhoneSTATE != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            //From Navigation , checks for Location permission and if Android 10 checks background location permsiion
            if (isFromNavigation) {
                int permissionLocationSTATE = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocationSTATE != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }

            if (!listPermissionsNeeded.isEmpty()) {
                if (isFromNavigation)
                    //Request permission for navigation call states
                    ActivityCompat.requestPermissions(this,
                            listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                            NAVIGATION_CALL_PERMISSIONS_REQUESTS);
                else
                    ActivityCompat.requestPermissions(this,
                            listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                            CALL_PERMISSIONS_REQUESTS);
            } else {
                if (!isFromNavigation)
                    new RETelephonyManager().startCallIntent(this,
                            this, REUtils.getREKeys().getCustomerCare());
            }
        } else {
            // Runtime permission not required.
            if (!isFromNavigation)
                new RETelephonyManager().startCallIntent(this,
                        this, REUtils.getREKeys().getCustomerCare());
        }
    }


    public void checkPermissions() {
        checkAndRequestMyPermissions(false);
    }

    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    public void enableTheLocation() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    requestForLocationPermission();
            }
        } else {
            // Access to the location has been granted to the app.
            if (!isLocationEnabled(getApplicationContext())) {
                //  Log.e("ENABLE LOCTION","TRUE");
                enableGps();
            } else if (isLocationEnabled(getApplicationContext())) {
                getCurrentLocation();
            }
        }
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (this instanceof REHomeActivity) {
            } else {
                showLoading();
            }
            client = LocationServices.getFusedLocationProviderClient(this);
            locationManager = (LocationManager) REApplication.getAppContext()
                    .getSystemService(LOCATION_SERVICE);
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        //    Log.e("LOC", "SUCCESS");
                        currentLocation = location;
                        saveLocationToModel();
                    }
                }
            });
            // if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            //Handler is to handle the location failure case.If location is not coming then it will proceed by binding data.
            mHandler = new Handler();
            mRunnable = new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(getApplicationContext(), "Couldn't get location", Toast.LENGTH_SHORT).show();
                    hideLoading();
//                    REUserModelStore.getInstance().setLatitude(0);
//                    REUserModelStore.getInstance().setLongitude(0);
                    setDataForCurrentActivity();
                    mHandler.removeCallbacks(mRunnable);
                }
            };
            mHandler.postDelayed(mRunnable, 20000);
        }
    }


    /**
     * Enables the GPS.
     */
    public void enableGps() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                getCurrentLocation();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(REBaseActivity.this,
                                LOCATION_SETTINGS_REQUEST);
                        getCurrentLocation();
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    /**
     * Save the user current location to preference.
     */
    private void saveLocationToModel() {
        if (currentLocation != null) {
            REUserModelStore.getInstance().setLatitude(currentLocation.getLatitude());
            REUserModelStore.getInstance().setLongitude(currentLocation.getLongitude());
        }
    }

    /**
     * Hides the no internet dialog
     */
    private void hideNoInternetDialog() {
        if (mNoInternetDialog != null) {
            mNoInternetDialog.cancel();
        }
    }

    /**
     * To Show a toast message for the all app screens.
     *
     * @param message toast message to be shown.
     */
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * The following method takes a View in which the user should type something,
     * calls requestFocus() to give it focus, then showSoftInput() to open the input method
     *
     * @param view the view which is visible in foreground.
     */
    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public int getScreenDimension() {
        Display display = getWindowManager().getDefaultDisplay();
        String displayName = display.getName();
        Log.i(TAG, "displayName  = " + displayName);
        // display size in pixels
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i(TAG, "width        = " + width);
        Log.i(TAG, "height       = " + height);
        return width;
    }

    /**
     * To hide the keyboard
     *
     * @param activity view in the foreground.
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE) {
            if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                //  L.d("Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
                if (resultCode == RESULT_CANCELED) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putLong(KEY_CLOSE_TIME, System.currentTimeMillis());
                    editor.commit();
                }
                unregisterInstallStateUpdListener();
            }
        } else if (requestCode == REQUEST_UPDATE_IMMIDIATE) {
            if (requestCode != RESULT_OK) {
                if (resultCode == RESULT_CANCELED) {
                    //if you want to request the update again just call checkUpdate()
                    Toast.makeText(REBaseActivity.this, "App update cancelled.", Toast.LENGTH_LONG).show();
                    finish();
                } else if (resultCode == RESULT_IN_APP_UPDATE_FAILED) {
                    Toast.makeText(REBaseActivity.this, "App update failed.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //Success Perform Task Here
                    getCurrentLocation();
                    if (isNavigationPermissions) {
                        isNavigationPermissions = false;
                        checkAndRequestMyPermissions(true);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    REUserModelStore.getInstance().setLatitude(0);
                    REUserModelStore.getInstance().setLongitude(0);
                    setDataForCurrentActivity();
                    break;
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCompleted(FBUser user) {
        Intent intent = new Intent(REBaseActivity.this, UserProfileActivity.class);
        try {
            URL profile_picture = new URL("https://graph.facebook.com/" + user.getId() + "/picture?width=550&height=550");
            intent.putExtra("IMAGE_URL", profile_picture.toString());
        } catch (MalformedURLException e) {
            RELog.e(e);
        }
        intent.putExtra("USER_EMAIL", user.getEmail());
        intent.putExtra("PROFILE_NAME", user.getName());
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoading() {
        //   Log.e("VehicleDetails:", "Base ACtivity show loading called");
        hideLoading();
        mProgressDialog = REDialogUtils.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        //   Log.e("VehicleDetails:", "Base ACtivity hide loading called");
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
            mProgressDialog = null;
            REDialogUtils.removeHandlerCallbacks();
        }
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
            mProgressDialog = null;
            REDialogUtils.removeHandlerCallbacks();
        }
        // unregister local broadcast
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    /**
     * Request for the runtime permissions.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestForLocationPermission() {
        // Request the permission. The result will be received in onRequestPermissionResult().
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.POST_NOTIFICATIONS}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            switch (requestCode) {
                case PERMISSION_REQUEST_BACKGROUND_LOCATION:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            for (int i = 0, len = permissions.length; i < len; i++) {
                                String permission = permissions[i];
                                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                            Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                                        showBackgroundLocationErrorDialog(this,
                                                getResources().getString(R.string.text_permission_necessary) +
                                                        getResources().getString(R.string.text_permission_bg_location));
                                    } else {
                                        Toast.makeText(this, R.string.text_bg_location_permission, Toast.LENGTH_SHORT).show();
                                        //displayNeverAskAgainDialog();
                                    }
                                    // Log.e("nav123", "setting permission as loc_disabled");
                                    REPreference.getInstance().putString(this, "Nav_Location", "loc_disabled");
                                }
                            }
                        } /*else {
                            REPreference.getInstance().putString(this, "Nav_Location", "loc_enabled");
                        }*/
                    }
                    break;
                case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                    // Request for Location permission.
                    if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (!REUtils.isLocationEnabled(getApplicationContext())) {
                            //  Log.e("ENABLE LOCTION","FALSE");
                            enableGps();
                        } else {
                            getCurrentLocation();
                        }
                    }
                    break;
                /*case MY_PERMISSIONS_READ_CONTACTS:
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //permission granted
                        Intent intent = new Intent(this, InviteFriendsActivity.class);
                        startActivityForResult(intent, PICK_CONTACTS);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                    } else {
                        //permission denied
                        permissionHandling(Manifest.permission.READ_CONTACTS, "", false);
                    }
                    break;*/


                case REConstants.REQUEST_CHECK_GALLERY:
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted
                        REUtils.launchGallery(REBaseActivity.this);
                    } else {
                        // permission denied
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            permissionHandling(Manifest.permission.READ_MEDIA_IMAGES, "", "", "", false);
                        else
                            permissionHandling(Manifest.permission.WRITE_EXTERNAL_STORAGE, "", "", "", false);

                    }
                    break;


                case REConstants.REQUEST_CHECK_CAMERA:
                    // If request is cancelled, the result arrays are empty.
                    String readImagePermission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_MEDIA_IMAGES;
                    Map<String, Integer> perms = new HashMap<>();
                    perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                    perms.put(readImagePermission, PackageManager.PERMISSION_GRANTED);
                    if (grantResults.length > 0) {
                        for (int i = 0; i < permissions.length; i++) {
                            perms.put(permissions[i], grantResults[i]);
                        }
                    }
                    if (perms != null && perms.size() > 0 && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(readImagePermission) == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted
                        REUtils.launchCamera(REBaseActivity.this, false);
                    } else {
                        REUtils.launchCamera(REBaseActivity.this, false);
                        // permissionHandling(Manifest.permission.CAMERA, readImagePermission, "", "", false);
                    }
                    break;

                case CALL_PERMISSIONS_REQUESTS:
                case SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS:
                case REConstants.RIDER_INVITED_CALL_PERMISSIONS_REQUESTS:
                    handleCallPermissions(grantResults, false);
                    break;
                case NAVIGATION_CALL_PERMISSIONS_REQUESTS:
                    handleCallPermissions(grantResults, true);
                    break;
                case NAVIGATION_BG_LOC_PERMISSION_REQUEST:
                    handleLocationPermissions(requestCode, grantResults, true);
                    break;
            }
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    @Override
    public void simError(String message) {
        REUtils.showErrorDialog(this, message);
    }

    @Override
    public void onNetworkConnect() {
        hideNoInternetDialog();
        Intent intent = new Intent("NO_INTERNET_KEY");
        sendBroadcast(intent);
    }

    @Override
    public void onNetworkDisconnect() {
        try {
            hideNoInternetDialog();
            mNoInternetDialog = REDialogUtils.showNoInternetDialog(this);
            mNoInternetDialog.show();
        } catch (Exception e) {
            //  Log.e("test", "exception raised");
        }


    }

    private void sendFCMTokenToServer() throws PreferenceException {
        if (!REUtils.isFCMTokenSent(this) && REUtils.checkIfTokenValid()) {
            saveTokenOnServer();
        }
    }


    public void sendFCMTokentoACMServer()  {
		initSFSDK();

    }
	private void initSFSDK() {

		SFMCSdk.Companion.requestSdk(sfmcSdk ->{
			String mMobileNo = REApplication.getInstance()
					.getUserLoginDetails().getData().getUser().getPhone();
			String gUid = REApplication.getInstance().getUserLoginDetails()
					.getData().getUser().getUserId();
sfmcSdk.mp(pushModuleInterface->{
	RegistrationManager.Editor success = pushModuleInterface.getRegistrationManager().edit();
		success.setContactKey(gUid);
	success.setAttribute("FirstName", REUserModelStore.getInstance().getFname());
	success.setAttribute("LastName", REUserModelStore.getInstance().getLname());
	success.setAttribute("Region", REUtils.getRegion());
	success.setAttribute("Environment", BuildConfig.FLAVOR.toUpperCase(Locale.ROOT));
	if (REUserModelStore.getInstance().getProfileData() != null && REUserModelStore.getInstance().getProfileData().getAddressInfo().size() > 0) {
		success.setAttribute("State", REUserModelStore.getInstance().getProfileData().getAddressInfo().get(0).getAddressInfo().getRegion());
		success.setAttribute("City", REUserModelStore.getInstance().getProfileData().getAddressInfo().get(0).getAddressInfo().getCity());
		}
	TelephonyManager manager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
	if (Objects.requireNonNull(manager).getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
		success.setAttribute("DeviceType", "Tablet");
	} else {
		success.setAttribute("DeviceType", "Phone");
			}
	success.setAttribute("PhoneNumber", mMobileNo);
	boolean isSuccess=success.commit();
if(isSuccess){
	sfmcSdk.mp(test->{
		//Log.e("token",test.getRegistrationManager().getAttributes().get("mobilenumber"));
	});
}
});
		});
	}


	public void saveTokenOnServer() throws PreferenceException {
        String token = REUtils.getFcmRegistrationTokenFromPref(this);
        String mMobileNo = REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getPhone();
        String gUid = REApplication.getInstance().getUserLoginDetails()
                .getData().getUser().getUserId();
        String versionName = BuildConfig.VERSION_NAME;
        String buildNumber = String.valueOf(BuildConfig.VERSION_CODE);
        FirebaseTokenRequest firebaseTokenRequest = new FirebaseTokenRequest(mMobileNo, token, "Android",
                mMobileNo, gUid, versionName, buildNumber, Build.VERSION.RELEASE + "");
        if (token != null && !token.isEmpty() && mMobileNo != null && !mMobileNo.isEmpty()) {
            FCMTokenRegistrationInteractor notificationServiceInteractor = new FCMTokenRegistrationInteractor();
            notificationServiceInteractor.sendNotification(firebaseTokenRequest, this);
        }
    }


    @Override
    public void onFCMTokenSentSuccess() {
        //  Log.e(TAG, "FCM Token sent success");
        REUtils.setFCMTokenSent(true);
    }

    @Override
    public void onFCMTokenSentFailure(String errorMessage) {
        // Log.e(TAG, "FCM Token sent Failure");
        REUtils.setFCMTokenSent(false);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("ServiceCenter", "Fetching location :" + location);
        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            mHandler.removeCallbacks(mRunnable);
            currentLocation = location;
            saveLocationToModel();
            setDataForCurrentActivity();
            locationManager.removeUpdates(this);
            hideLoading();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    /**
     * Broadcast receiver to receive the datam
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (REBaseActivity.APP_LIFE_FLAG) {
                String mJWTTokwn = null;
                try {
                    mJWTTokwn = REApplication.getInstance()
                            .getUserTokenDetails();
                } catch (Exception e) {
                    RELog.e(e);
                }
                if (mJWTTokwn != null && !mJWTTokwn.isEmpty() && !mJWTTokwn.equals(DEFAULT_STRING)) {
                    // API call for verifying JWT token


                    if (!REUtils.checkIfTokenValid() && REUtils.isNetworkAvailable(REBaseActivity.this)) {
                        showLoading();
                        new VerifyAuthTokenInteractor(REBaseActivity.this).generateToken();

                    }
                }
            }
        }
    };


    @Override
    public void onVerifyTokenSuccess() {
        hideLoading();
        Log.d("OnLifecycleEvent", "onVerifyTokenSuccess");
    }

    @Override
    public void onVerifyTokenFailure() {
        hideLoading();
        //  navigateToSplash();
        Log.d("OnLifecycleEvent", "onVerifyTokenFailure");
    }

    @Override
    public void onGenerateTokenSuccess(long time,String reqId) {
        hideLoading();
    }

    @Override
    public void onGenerateTokenFailure() {
        hideLoading();
        navigateToSplash();
    }

    public void showBackgroundLocationErrorDialog(Activity activity, String message) {
        try {
            TextView mAlertMessage, mAlertOk;
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            mAlertMessage = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            mAlertMessage.setText(message);
            mAlertOk = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            mAlertOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(activity, new String[]{
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            PERMISSION_REQUEST_BACKGROUND_LOCATION);
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    protected void onFirebaseHTMLPageUrls() {

        FirestoreManager.getInstance().getHtmlData(this, REApplication.getInstance().Country);
    }

	private void doFirebaseAuthLogin(String token) {
		Log.e("RE_LOGGER","Firebase auth start");
		long startTimee = System.currentTimeMillis();
		REUtils.signInWithFirebaseCustomToken(token + "", new FirebaseAuthListner() {

			@Override
			public void onFirebaseAuthSuccess() {
				Log.e("NEWFLOW","Auth login end");
				Log.e("RE_LOGGER","Firebase auth end");
				REUtils.CHECK_FIREBASE_AUTH_INPROGRESS =false;
					REUtils.sendResponseLog("Sign in auth Firebase", null, (double)(System.currentTimeMillis()-startTimee) / 1000f);

				REApplication.getInstance().featureCountry = REUtils.getFeatures().getDefaultFeatures();
				//onVerifySuccess();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						FirestoreManager.getInstance().getUserProfileDataListener(REBaseActivity.this);
					}
				},500);
				if (REUtils.getFeatures().getDefaultFeatures() != null && REUtils.getFeatures().getDefaultFeatures().getMotorcycleInfo().equalsIgnoreCase(FEATURE_ENABLED))
                    getVehicleDetails();
			}

			@Override
			public void onFirebaseAuthFailure(Exception e) {
				failureCount++;
				int randNum = getRandomNumberInRange(1, 3);
				if (failureCount <= 1 && (e instanceof FirebaseNetworkException || e instanceof FirebaseTooManyRequestsException)) {
					new Handler().postDelayed(() ->doFirebaseAuthLogin(token), randNum * 1000);
				}
				else {
					REUtils.CHECK_FIREBASE_AUTH_INPROGRESS =false;
					REApplication.getInstance().featureCountry = REUtils.getFeatures().getDefaultFeatures();
				}
			}
		});

	}

	private void getVehicleDetails() {
		  currTime=System.currentTimeMillis();
		REUtils.CHECK_VEHICLE_SYNCING_INPROGRESS=true;
		Log.e("NEWFLOW","vehicle details start");
				FirestoreManager.getInstance().getRealTimeVehicleDetails(new OnRealtimeVehicledetailListner() {
					@Override
					public void onVehicleDetailSuccessFireStore(List<VehicleDataModel> list) {
						if (list.size() > 0) {
							REUtils.getModelNameFromModelCode(new OnFirestoreVehicleDataMappingCallback() {
								@Override
								public void onVehicleMappingSuccess(List<VehicleDataModel> vehicleModel, int position) {
									if (vehicleModel.size() - 1 == position) {
										Log.e("NEWFLOW", "vehicle end with mapping");
										Log.e("SUCCESS", "MAPPING");
										Log.e("DATA_REFRESH",vehicleModel.size()+"");
										REServiceSharedPreference.clearBookingInfoPreferences(REBaseActivity.this);
										REServiceSharedPreference.saveVehicleData(REBaseActivity.this, vehicleModel);
										FirestoreManager.getInstance().getServiceHistory();
										REUtils.CHECK_VEHICLE_SYNCING_INPROGRESS=false;
										refreshMotorcycleData();
										if(currTime!=0){
											REUtils.sendResponseLog("Vehicle details Firebase", null, (double)(System.currentTimeMillis()-currTime) / 1000);

										}
										currTime=0;
									} else {
										position = position + 1;
										REUtils.getModelNameFromModelCode(this, list, position);
									}
								}
							}, REServiceSharedPreference.getVehicleData(REBaseActivity.this), 0);


						}
						else{
							if(currTime!=0){
								REUtils.sendResponseLog("Vehicle details Firebase", null, (double)(System.currentTimeMillis()-currTime) / 1000);
							}
							currTime=0;
							REUtils.CHECK_VEHICLE_SYNCING_INPROGRESS=false;
							refreshMotorcycleData();
						}
					}

				});
			}
	private void refreshMotorcycleData() {
		Intent intent = new Intent("motorcycle_data_receiver");
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}


	public void getUserDataInBackground(){
		Log.e("NEWFLOW","custom token api start");
		REUtils.CHECK_FIREBASE_AUTH_INPROGRESS =true;
		homeActivityPresenter.getFirebaseCustomToken();
	}



	@Override
	public void onFirebaseCustomTokenSuccess(String token, String reqId) {
		Log.e("NEWFLOW","custom token api end");

		doFirebaseAuthLogin(token);
	}

	@Override
	public void onFirebaseCustomFailure(String error) {
		REUtils.CHECK_FIREBASE_AUTH_INPROGRESS =false;
		REApplication.getInstance().featureCountry = REUtils.getFeatures().getDefaultFeatures();
	}

	@Override
	public void onFirebaseAuthSuccess() {
		REUtils.CHECK_FIREBASE_AUTH_INPROGRESS =false;
		REApplication.getInstance().featureCountry = REUtils.getFeatures().getDefaultFeatures();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				FirestoreManager.getInstance().getUserProfileDataListener(REBaseActivity.this);
			}
		},500);

		FirestoreManager.getInstance().getAllRidesInfoFromFirestore();
		if (REUtils.getFeatures().getDefaultFeatures() != null && REUtils.getFeatures().getDefaultFeatures().getMotorcycleInfo().equalsIgnoreCase(FEATURE_ENABLED))
			getVehicleDetails();
	}

	@Override
	public void onGetProfileDetailsSuccess(ProfileData data) {
		if(data!=null&&data.getAccountStatus()!=null)
			if(!data.getAccountStatus().getActive()){
			isUserActive=false;
			new Handler().postDelayed(() -> {

				if(REUtils.isUserLoggedIn()) {
					Intent intent = new Intent(REBaseActivity.this, PopActivity.class);
					startActivity(intent);
				}
			},1000);

		}
		else{
				isUserActive=true;
			}
	}


}
