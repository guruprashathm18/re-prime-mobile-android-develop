package com.royalenfield.reprime.ui.splash.activity;

import static com.royalenfield.reprime.application.REApplication.mFirebaseAnalytics;
import static com.royalenfield.reprime.preference.REPreference.DEFAULT_STRING;
import static com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback.generateLogs;
import static com.royalenfield.reprime.utils.REConstants.APP_VERSION_DATA;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_DISABLED;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;
import static com.royalenfield.reprime.utils.REConstants.NOTIF_DATA_PARAMS;
import static com.royalenfield.reprime.utils.REConstants.PUSH_REMOTE_MESSAGE;
import static com.royalenfield.reprime.utils.REConstants.REMOTE_DATA;
import static com.royalenfield.reprime.utils.REConstants.REMOTE_VERSION;
import static com.royalenfield.reprime.utils.REUtils.getCountryData;
import static com.royalenfield.reprime.utils.REUtils.getRandomNumberInRange;
import static com.royalenfield.reprime.utils.REUtils.navigateToSplash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.remoteconfig.internal.DefaultsXmlParser;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.fireStore.OnFirestoreVehicleDataMappingCallback;
import com.royalenfield.firebase.fireStore.OnFirestoreVehicleListCallback;
import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.remoteconfig.RemoteConfigData;
import com.royalenfield.reprime.models.response.web.login.LoginResponse;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.homescreen.interactor.HomeActivityInteractor;
import com.royalenfield.reprime.ui.home.homescreen.listener.FirebaseCustomTokenListener;
import com.royalenfield.reprime.ui.home.homescreen.presenter.HomeActivityPresenter;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.LogResponse;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.onboarding.useronboarding.AppSignatureHelper;
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity;
import com.royalenfield.reprime.ui.onboarding.verifyauthtoken.interactor.VerifyAuthTokenInteractor;
import com.royalenfield.reprime.ui.onboarding.verifyauthtoken.view.VerifyTokenView;
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils;
import com.royalenfield.reprime.ui.phoneconfigurator.view.PaymentWebViewActivity;
import com.royalenfield.reprime.ui.splash.presenter.FirebaseAuthListner;
import com.royalenfield.reprime.ui.splash.presenter.FirebaseConfigListner;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

/**
 * Main splash screen which comes first after opening the app
 * and redirects to scrollable splash after two seconds
 */

public class MainSplashScreenActivity extends REBaseActivity implements VerifyTokenView {

    private static final long SPLASH_TIMEOUT = 3000;
    VerifyAuthTokenInteractor mVerifyAuthTokenInteractor;
    private boolean mIsServiceNotification = false;
   private boolean mIsNavigationNotification;
    private String sharedNavigationId;
    private boolean isBalancePaymentDeepLink;
    private String paymentURL;
    private ImageView imgLogo;
    private ConstraintLayout mainConstraint;
    private boolean mIsNavigationDeepLink;
    private String sharedDeepLink;
    private String deepLinkData=null;
    private int failureCount=0;
	private long startTime;
	private long startTimeGenerateToken;
	@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		REBaseActivity.isUserActive=true;
		REApplication.getInstance().setRemoteConfigData(null);
        setContentView(R.layout.activity_main_splash);
	  findViews();
        getIntentData();
        dispatchIntent(getIntent());
        setDynamicLink();
        Log.e("remoteconfig","START");
        onFirebaseHTMLPageUrls();
		Log.e("RE_LOGGER","remote config data version start");

		REApplication.getInstance().arrayListResponse.clear();
		//REApplication.getInstance().arrayList=new ArrayList<>();
		startTime=System.currentTimeMillis();
        FirestoreManager.getInstance().getRemoteConfigDataVersion();

		AppSignatureHelper appSignature = new AppSignatureHelper(this);
		appSignature.getAppSignatures();
        new Handler().postDelayed(() -> {
            imgLogo.setVisibility(View.GONE);
            mainConstraint.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.black));
         //   showLoading();
                Log.e("from","splash");
                startMainSplashIntent();

        }, SPLASH_TIMEOUT);

    }


    private void setDynamicLink() {

        Log.e( "setDynamicLink: ","working >>>>>>>" );
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                    //    Log.e( "setDynamicLink: ",pendingDynamicLinkData.getLink().toString() );
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        if (deepLink != null) {
                            String source = String.valueOf(deepLink.getQueryParameters("utm_source"));
                            String medium = String.valueOf(deepLink.getQueryParameters("utm_medium"));
                            String campaign = String.valueOf(deepLink.getQueryParameters("utm_campaign"));
                            if (source != null || medium != null || campaign != null) {
                                //add the utms paramters as event properties in params object
                                Bundle params = new Bundle();
                                params.putString(FirebaseAnalytics.Param.SOURCE, source);
                                params.putString(FirebaseAnalytics.Param.MEDIUM, medium);
                                params.putString(FirebaseAnalytics.Param.CAMPAIGN, campaign);
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.CAMPAIGN_DETAILS, params);
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, params);
                            }
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });

    }



	private void findViews() {
        imgLogo = findViewById(R.id.imageView3);
        mainConstraint = findViewById(R.id.main_constraint);
    }

    private void getIntentData() {
        Log.e( "getIntentData: ", ">>>>>>>>>");
        if (getIntent() != null && getIntent().getExtras() != null) {
            if(getIntent().hasExtra(NOTIF_DATA_PARAMS)) {
                deepLinkData = getIntent().getExtras().getString(NOTIF_DATA_PARAMS);
            }
            mIsServiceNotification = getIntent().getBooleanExtra(REConstants.IS_SERVICE_NOTIFICATION, false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(getIntent() != null ? getIntent().getData() : null).init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // if activity is in foreground (or in backstack but partially visible) launching the same
        // activity will skip onStart, handle this case with reInitSession
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit();
    }

    private Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
        @Override
        public void onInitFinished(JSONObject linkProperties, BranchError error) {
            // do stuff with deep link data (nav to page, display content, etc)
            try {
                if (linkProperties != null && linkProperties.has(NOTIF_DATA_PARAMS)) {
                    String data = linkProperties.getString(NOTIF_DATA_PARAMS) ;
                    deepLinkData=data;
                }
                if (linkProperties != null && linkProperties.has("linkType") && linkProperties.getString("linkType").equalsIgnoreCase("bikepayment")) {
                    isBalancePaymentDeepLink = true;
                    paymentURL = linkProperties.getString("paymentURL") + linkProperties.get("paymentkey");
                    RELog.e(paymentURL);
                } else if (linkProperties != null && linkProperties.has(REConstants.DESTINATION_LOCATION)) {
                    mIsNavigationDeepLink = true;
                    sharedDeepLink = linkProperties.toString();
                    RELog.e("DEEPLINK DATA TBT: "+sharedDeepLink);
                    REServiceSharedPreference.setDeepLinkData(MainSplashScreenActivity.this, sharedDeepLink);
                } else if (linkProperties != null && linkProperties.has(REConstants.SHARE_CODE_KEY)) {
                    mIsNavigationDeepLink = true;
                    sharedDeepLink = linkProperties.toString();
                    RELog.e("DEEPLINK DATA BCT: "+sharedDeepLink);
                    REServiceSharedPreference.setDeepLinkData(MainSplashScreenActivity.this, sharedDeepLink);
                } else {
                    //  isBalancePaymentDeepLink=true;
                    REServiceSharedPreference.setDeepLinkData(MainSplashScreenActivity.this, "");
                }
            } catch (Exception e) {
                RELog.e(e);
            }
        }
    };

    public void dispatchIntent(Intent intent) {
        final Uri uri = intent.getData();

        if (uri != null) {

            final String scheme = Objects.requireNonNull(uri.getScheme()).toLowerCase();

            if (("http".equals(scheme) || "https".equals(scheme))) {
                mIsNavigationNotification = true;
                sharedNavigationId = uri.getQuery();
            }
            RELog.e(" sharedNavigationId = %s", sharedNavigationId);
        }
    }


    private void startMainSplashIntent() {
        REApplication.getInstance().setCountryData();
        if (isBalancePaymentDeepLink) {
            gotoPaymentWebview();
            isBalancePaymentDeepLink = false;
        } else {
            if (REUtils.isUserLoggedIn()) {
				REApplication.getInstance().arrayListResponse.clear();
				FirebaseCrashlytics.getInstance().setUserId("guest");
                showLoading();
                new Handler().post(() -> getCountryData());
                checkUserSession();
            } else {
				Log.e("RE_LOGGER","Open Login/signup Screen");
                hideLoading();
                openSplashScreenOrHome(UserOnboardingActivity.class);
                finish();
            }
        }
    }

    private void gotoPaymentWebview() {
        finish();
        Intent intent = new Intent(this, PaymentWebViewActivity.class);
        intent.putExtra(PCUtils.PC_REACT_FLAG, PCUtils.PC_BALANCE_PAMENT);
        intent.putExtra(PCUtils.URL_PAYMENT, paymentURL);
        intent.putExtra(PCUtils.PC_BIKE_ID, "");
        intent.putExtra(PCUtils.PC_CONFIG_ID, "");
        startActivity(intent);
    }

    private void checkUserSession() {
        String mJWTTokwn = null;
        try {
            mJWTTokwn = REApplication.getInstance()
                    .getUserTokenDetails();
        } catch (Exception e) {
            RELog.e(e);
        }
        if (mJWTTokwn != null && !mJWTTokwn.isEmpty() && !mJWTTokwn.equals(DEFAULT_STRING)) {
            // REApplication.getInstance().clearAllModelStore();
            // API call for verifying JWT token
            mVerifyAuthTokenInteractor = new VerifyAuthTokenInteractor(this);
            if (!REUtils.checkIfTokenValid()) {
				startTimeGenerateToken=System.currentTimeMillis();
                mVerifyAuthTokenInteractor.generateToken();
            } else {
              //  mVerifyAuthTokenInteractor.verifyAuthToken();
                onVerifyTokenSuccess();
            }

        } else {
            hideLoading();
            openSplashScreenOrHome(UserOnboardingActivity.class);
            finish();
        }
    }


    /**
     * Opens splash where user can login/signup
     */
    private void openSplashScreenOrHome(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (mIsServiceNotification) {
            intent.putExtra(REConstants.IS_SERVICE_NOTIFICATION, mIsServiceNotification);
        } /*else if (mIsNavigationNotification) {
            intent.putExtra(REConstants.IS_NAVIGATION_NOTIFICATION, true);
            intent.putExtra(REConstants.NAVIGATION_NOTIFICATION, sharedNavigationId);
        }*/ else if (mIsNavigationDeepLink) {
            intent.putExtra(REConstants.IS_NAVIGATION_DEEPLINK, true);
            intent.putExtra(REConstants.NAVIGATION_DEEPLINK, sharedDeepLink);
        }
        startActivity(intent);
    }

    private void setServiceNotification(Intent intent) {
        if (mIsServiceNotification) {
            intent.putExtra(REConstants.IS_SERVICE_NOTIFICATION, mIsServiceNotification);
        }
    }

    @Override
    public void onVerifyTokenSuccess() {
		onVerifySuccess();
            getUserDataInBackground();
    }

    private void onVerifySuccess() {
        LoginResponse data = REApplication.getInstance().getUserLoginDetails();
        if (data != null &&data.getData()!=null&& data.getData().getUser() != null) {
			FirebaseCrashlytics.getInstance().setUserId(data.getData().getUser().getUserId());
            String showDataSanitation=FEATURE_ENABLED;
            try {
                showDataSanitation = REApplication.getInstance().featureCountry.getShowDataSanitization();
                if (showDataSanitation == null) {
                    if (REApplication.getInstance().Country.equalsIgnoreCase(REConstants.COUNTRY_INDIA))
                        showDataSanitation = FEATURE_ENABLED;
                    else
                        showDataSanitation = FEATURE_DISABLED;
                }
            } catch (Exception e) {
                //  e.printStackTrace();
            }
            if (showDataSanitation!=null&&showDataSanitation.equalsIgnoreCase(FEATURE_ENABLED) &&
                    data.getData().getUser().getVerifiedAccount().equalsIgnoreCase(REConstants.KEY_ACCOUNT_PENDING) || data.getData().getUser().getVerifiedAccount().equalsIgnoreCase(REConstants.KEY_ACCOUNT_VERIFIED)) {
                clearDataAndGotoSplash();
            } else {
                try {
                    saveTokenOnServer();
                    //register the fcm token to Acm
                  //  pushAcmKeystoServer();
                    sendFCMTokentoACMServer();
                } catch (PreferenceException e) {
                    RELog.e(e);
                }
                if (REUtils.getFeatures().getDefaultFeatures() != null && REUtils.getFeatures().getDefaultFeatures().getCommunity().equalsIgnoreCase(FEATURE_ENABLED))
                    FirestoreManager.getInstance().getAllRidesInfoFromFirestore();
//                if (REUtils.getFeatures().getDefaultFeatures() != null && REUtils.getFeatures().getDefaultFeatures().getMotorcycleInfo().equalsIgnoreCase(FEATURE_ENABLED))
//                    getVehicleDetails();
//                else
                    gotoHomeActivity();

            }
        } else {
            clearDataAndGotoSplash();
        }
    }

    private void clearDataAndGotoSplash() {
        REApplication.getInstance().clearAllModelStore();
        try {
            REPreference.getInstance().removeAll(MainSplashScreenActivity.this);
        } catch (Exception e) {
            RELog.e(e);
        }
        openSplashScreenOrHome(UserOnboardingActivity.class);
        finish();
    }

    public void gotoHomeActivity() {
        REApplication.getInstance().setLoggedInUser(true);
        Intent intent = new Intent(getApplicationContext(), REHomeActivity.class);
        if (getIntent().hasExtra(PUSH_REMOTE_MESSAGE)){
            intent.putExtra(PUSH_REMOTE_MESSAGE, getIntent().getStringExtra(PUSH_REMOTE_MESSAGE));
         //   REUtils.trackACM(getIntent());
        }

        else if(deepLinkData!=null)
            intent.putExtra(PUSH_REMOTE_MESSAGE,deepLinkData);
        deepLinkData=null;
        setServiceNotification(intent);
        //setDeepLink(intent);
        if (mIsNavigationDeepLink) {
            intent.putExtra(REConstants.IS_NAVIGATION_DEEPLINK, true);
            intent.putExtra(REConstants.NAVIGATION_DEEPLINK, sharedDeepLink);
        }
       /* if (mIsNavigationNotification) { // Sending Trip ID (Redeem trip flow)
            intent.putExtra(REConstants.IS_NAVIGATION_NOTIFICATION,true);
            intent.putExtra(REConstants.NAVIGATION_NOTIFICATION, sharedNavigationId);
            intent.putExtra(IS_NAVIGATION_DETAILS, false);
        }*/
        startActivity(intent);
        finish();
    }

    @Override
    public void onVerifyTokenFailure() {
        onVerifyFailure();
    }

    private void onVerifyFailure() {
        hideLoading();
                REApplication.getInstance().clearAllModelStore();
        try {
            //Clearing preference
            REPreference.getInstance().removeAll(getApplicationContext());
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        hideLoading();
        openSplashScreenOrHome(UserOnboardingActivity.class);
        finish();

    }


    @Override
    public void onGenerateTokenSuccess(long time,String reqId) {
		REUtils.sendResponseLog("Generate token API", reqId, (double)time / 1000);

		onVerifyTokenSuccess();

    }

    @Override
    public void onGenerateTokenFailure() {
        onVerifyFailure();
    }


}
