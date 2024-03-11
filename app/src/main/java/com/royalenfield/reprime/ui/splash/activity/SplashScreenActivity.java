package com.royalenfield.reprime.ui.splash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.base.viewpager.AutoScrollViewPager;
import com.royalenfield.reprime.models.response.remoteconfig.ConfigFeatures;
import com.royalenfield.reprime.models.response.remoteconfig.Feature;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.splash.fragments.SplashScreenFragment;
import com.royalenfield.reprime.ui.splash.presenter.FirebaseAuthListner;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REUtils.getCountryData;
import static com.royalenfield.reprime.utils.REUtils.getRandomNumberInRange;

/**
 * @author BOP1KOR on 11/19/2018.
 * <p>
 * Splash screen for this application..
 */
public class SplashScreenActivity extends REBaseActivity implements View.OnClickListener, FirebaseAuthListner {
    private final long PERIOD = 2000; // time in milliseconds between successive task executions.
private Bundle params=new Bundle();

    private List<Integer> splashArr=new ArrayList<>();
    boolean isTBTDisabled=false;
    private int failureCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        setContentView(R.layout.activity_splash_screen);
        REApplication.getInstance().setCountryHeaderDefault();
		REApplication.getInstance().endTrace(String.valueOf(REApplication.TRACES.app_launch));
		checkForUpdates();
//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
//        //make fully Android Transparent Status bar
//        if (Build.VERSION.SDK_INT >= 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
       ConfigFeatures configFeature = REUtils.getConfigFeatures();
        // find MenuItem you want to change
         if (configFeature != null && configFeature.getTBT() != null) {
            if (configFeature.getTBT().getTbtReleaseFeatureStatus()!=null&&configFeature.getTBT().getTbtReleaseFeatureStatus().equalsIgnoreCase(REConstants.FEATURE_STATUS_DISABLED)) {
                isTBTDisabled = true;
            }
        }

        if(REApplication.getInstance().featureCountry==null){
            REApplication.getInstance().featureCountry = REUtils.getFeatures().getDefaultFeatures();
        }
        Feature feature=REApplication.getInstance().featureCountry;
        if(feature!=null){
            if(feature.getCommunity().equalsIgnoreCase(FEATURE_ENABLED))
                splashArr.add( R.drawable.splash_rider1);
            if(feature.getMotorcycleInfo().equalsIgnoreCase(FEATURE_ENABLED))
            splashArr.add(R.drawable.splash_service);
            if(!isTBTDisabled)
                splashArr.add(R.drawable.splash_tripper);
        }
        initViews();
    }

    /**
     * Initializes the all layout views and set the viewpager.
     */
    private void initViews() {
        //REServiceSharedPreference.saveVehicleData(this, null);
        Button lBtLoginButton = findViewById(R.id.btn_login);
        Button lBtSignUpButton = findViewById(R.id.btn_sign_up);
        TextView textContinueAsGuest = findViewById(R.id.text_continue_as_guest);
        View continueAsGuest=findViewById(R.id.continue_guest_layout);
        continueAsGuest.setVisibility(View.GONE);
        if(REUtils.getFeatures().getDefaultFeatures()!=null&&REUtils.getFeatures().getDefaultFeatures().getGuestLogin().equalsIgnoreCase(FEATURE_ENABLED))
            continueAsGuest.setVisibility(View.VISIBLE);
        lBtLoginButton.setOnClickListener(this);
        lBtSignUpButton.setOnClickListener(this);
        textContinueAsGuest.setOnClickListener(this);
        AutoScrollViewPager mViewPager = findViewById(R.id.view_pager_splash_screen);
        AutoScrollPagerAdapter autoScrollPagerAdapter =
                new AutoScrollPagerAdapter(getSupportFragmentManager());
        //   AutoScrollViewPager viewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(autoScrollPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);
        // start auto scroll
        mViewPager.startAutoScroll();
        // set auto scroll time in mili
        mViewPager.setInterval(PERIOD);
        // enable recycling using true
        mViewPager.setCycle(true);
        mViewPager.setBorderAnimation(false);
        if(splashArr.size()<=1){
            tabs.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                loginIntent.putExtra(REConstants.IS_SERVICE_NOTIFICATION, getIntent().
                        getBooleanExtra(REConstants.IS_SERVICE_NOTIFICATION, false));
                loginIntent.putExtra(REConstants.IS_NAVIGATION_NOTIFICATION, getIntent().
                        getBooleanExtra(REConstants.IS_NAVIGATION_NOTIFICATION, false));
                loginIntent.putExtra(REConstants.NAVIGATION_NOTIFICATION, getIntent().
                        getStringExtra(REConstants.NAVIGATION_NOTIFICATION));
                loginIntent.putExtra(REConstants.IS_NAVIGATION_DEEPLINK, getIntent().
                        getBooleanExtra(REConstants.IS_NAVIGATION_DEEPLINK, false));
                loginIntent.putExtra(REConstants.NAVIGATION_DEEPLINK, getIntent().
                        getStringExtra(REConstants.NAVIGATION_DEEPLINK));
                startActivityForResult(loginIntent, LoginActivity.CODE_SPLASH_SCREEN_ACTIVITY);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                params.putString("eventCategory", "Login");
                params.putString("eventAction", "Login Home click");
               REUtils.logGTMEvent(REConstants.KEY_LOGIN_GTM, params);
                break;
            case R.id.btn_sign_up:
                Log.e("SIGNUP","SIGNUP CLICK");
//                Intent signupIntent = new Intent(this, SignUpActivity.class);
//                startActivityForResult(signupIntent, LoginActivity.CODE_SPLASH_SCREEN_ACTIVITY);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                params.putString("eventCategory", "Sign up");
//                params.putString("eventAction", "SignUp Home click");
//               REUtils.logGTMEvent(REConstants.KEY_SIGNUP_GTM, params);

                //showMap();
                break;
            case R.id.text_continue_as_guest:
                 params = new Bundle();
                params.putString("eventCategory", "Guest ");
                params.putString("eventAction", "Continue as Guest click");
                REUtils.logGTMEvent(REConstants.KEY_GUEST_GTM,params);
                showLoading();
                REUtils.annonymasSignInWithFirebase(SplashScreenActivity.this, SplashScreenActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LoginActivity.CODE_SPLASH_SCREEN_ACTIVITY) {
                REApplication.getInstance().setLoggedInUser(true);
                Intent intent = new Intent(this, REHomeActivity.class);
                intent.putExtra(REConstants.IS_SERVICE_NOTIFICATION, getIntent().
                        getBooleanExtra(REConstants.IS_SERVICE_NOTIFICATION, false));
                intent.putExtra(REConstants.IS_NAVIGATION_NOTIFICATION, getIntent().
                        getBooleanExtra(REConstants.IS_NAVIGATION_NOTIFICATION, false));
                intent.putExtra(REConstants.NAVIGATION_NOTIFICATION, getIntent().
                        getStringExtra(REConstants.NAVIGATION_NOTIFICATION));
                intent.putExtra(REConstants.IS_NAVIGATION_DEEPLINK, getIntent().
                        getBooleanExtra(REConstants.IS_NAVIGATION_DEEPLINK, false));
                intent.putExtra(REConstants.NAVIGATION_DEEPLINK, getIntent().
                        getStringExtra(REConstants.NAVIGATION_DEEPLINK));
                startActivity(intent);
                finish();
            }
        }
    }

    private class AutoScrollPagerAdapter extends FragmentPagerAdapter {
        AutoScrollPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SplashScreenFragment.newInstance(splashArr.get(position));
        }

        @Override
        public int getCount() {
            return splashArr.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "App Launch");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }

    @Override
    public void onFirebaseAuthSuccess() {
        hideLoading();
        Intent guestUserIntent = new Intent(this, REHomeActivity.class);
        guestUserIntent.putExtra(REUtils.USER_LOGGED_IN, false);
        startActivity(guestUserIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onFirebaseAuthFailure(Exception e) {

        failureCount++;
        int randNum=getRandomNumberInRange(1,3);
        if(failureCount<=1&&(e instanceof FirebaseNetworkException ||e instanceof FirebaseTooManyRequestsException)) {
            new Handler().postDelayed(() -> REUtils.annonymasSignInWithFirebase(SplashScreenActivity.this,SplashScreenActivity.this), randNum*1000);

        }
        else {
            hideLoading();
            Intent guestUserIntent = new Intent(this, REHomeActivity.class);
            guestUserIntent.putExtra(REUtils.USER_LOGGED_IN, false);
            startActivity(guestUserIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
//                failureCount++;
//        RELog.e("" + failureCount);
//        int randNum=getRandomNumberInRange(1,3);
//        if(failureCount<3) {
//            new Handler().postDelayed(() -> REUtils.annonymasSignInWithFirebase(SplashScreenActivity.this,SplashScreenActivity.this), randNum*1000);
//
//        }
//        else {
//            hideLoading();
//            REUtils.showErrorDialog(this, getResources().getString(R.string.something_went_wrong), () -> finish());
//        }
    }

}


