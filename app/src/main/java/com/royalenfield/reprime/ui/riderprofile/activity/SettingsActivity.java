package com.royalenfield.reprime.ui.riderprofile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.riderprofile.adapter.SettingsAdapter;
import com.royalenfield.reprime.ui.riderprofile.interactor.LogoutInteractor;
import com.royalenfield.reprime.ui.riderprofile.presenter.LogoutPresenter;
import com.royalenfield.reprime.ui.riderprofile.views.LogoutView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RERecyclerViewSeperatorDecoration;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.FEATURE_DISABLED;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;
import static com.royalenfield.reprime.utils.REConstants.INPUT_PROFILE_ACTIVITY;
import static com.royalenfield.reprime.utils.REConstants.INPUT_SPLASH_ACTIVITY;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REConstants.KEY_SETTINGS_GTM;
import static com.royalenfield.reprime.utils.REConstants.OUR_WORLD_EVENTS;
import static com.royalenfield.reprime.utils.REConstants.RIDES_LIST_POSITION;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_MARQUEE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_POPULAR;
import static com.royalenfield.reprime.utils.REConstants.SETTING_ACTIVITY_POLICIES;

/**
 * This Activity is for UserSettings
 */
public class SettingsActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener, View.OnClickListener, LogoutView {
    private LogoutPresenter mLogoutPresenter;
    private String mInputType;
    private int mPosition;
    private TitleBarView mTitleBarView;
    private RecyclerView mRecyclerView;
    private boolean isRegistrationPolicies = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Settings screen");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        initializeViews();
    }

    /**
     * Initializes all the views for the UI....
     */
    private void initializeViews() {
        getIntentData();
        mTitleBarView = findViewById(R.id.titleBarView_settings);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.text_label_settings).toUpperCase());
        //instantiate presenter.
        mLogoutPresenter = new LogoutPresenter(this, new LogoutInteractor());
        //Views and Adapter.
        mRecyclerView = findViewById(R.id.recyclerView_settings_list);

        //use this settings to improve performance if you know that changes
        //in content do not change the layout size of the recyclerView
        mRecyclerView.setHasFixedSize(true);
        //use a linear layout manager.
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RERecyclerViewSeperatorDecoration decoration = new RERecyclerViewSeperatorDecoration(this,
                getApplicationContext().getResources().getColor(R.color.white_30), 1f);
        mRecyclerView.addItemDecoration(decoration);
        //Setting Adapter
//        mRecyclerView.setAdapter(new SettingsAdapter(getApplicationContext(), SettingsActivity.this,
//                getApplicationContext().getResources().getStringArray(R.array.list_settings)));
        if(REApplication.getInstance().getUserLoginDetails().getData().getUser().getLocality()!=null)
        REApplication.getInstance().Country= REApplication.getInstance().getUserLoginDetails().getData().getUser().getLocality().getCountry().toUpperCase();
        else
            REApplication.getInstance().Country=REConstants.COUNTRY_INDIA;
        REApplication.getInstance().featureCountry=REUtils.getFeatureMap().get(REApplication.getInstance().Country);
        if( REApplication.getInstance().featureCountry==null)
            REApplication.getInstance().featureCountry=REUtils.getFeatures().getDefaultFeatures();
        setAdapter();
        Button mLogout = findViewById(R.id.button_logout);
        if (isRegistrationPolicies) {
            mLogout.setVisibility(View.GONE);
        }
        mLogout.setOnClickListener(this);
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mInputType = bundle.getString(REConstants.SETTING_ACTIVITY_INPUT_TYPE, null);
            mPosition = bundle.getInt(RIDES_LIST_POSITION, 0);
            isRegistrationPolicies = bundle.getBoolean(SETTING_ACTIVITY_POLICIES);
        }
    }

    private void setAdapter() {
       String[] array= getApplicationContext().getResources().getStringArray(R.array.list_settings);
        String[] consentarray= getApplicationContext().getResources().getStringArray(R.array.consent_array);

        List<String> updatedList = new ArrayList<>();
       String isFaqEnabled= REApplication.getInstance().featureCountry.getFaq();

           for(String data:array) {
               if(isFaqEnabled.equalsIgnoreCase(FEATURE_DISABLED)) {
                   if (data.equalsIgnoreCase("Faqs")) {
                       continue;
                   }
               }
               updatedList.add(data);
           }
        if(REUtils.getFeatures().getDefaultFeatures()!=null&&REUtils.getFeatures().getDefaultFeatures().getShowConsentControlsV2().equalsIgnoreCase(FEATURE_ENABLED)) {

            for (String data : consentarray) {
                updatedList.add(data);
            }
           }

        updatedList.add(getResources().getString(R.string.app_setting));
        updatedList.add(getResources().getString(R.string.text_contact_us));

        String[] intArray = new String[updatedList.size()];
        intArray = updatedList.toArray(intArray);
        //Setting Adapter
        if (mInputType.equals(INPUT_SPLASH_ACTIVITY) || mInputType.equals(INPUT_PROFILE_ACTIVITY)) {
            mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.text_label_settings).toUpperCase());
            mRecyclerView.setAdapter(new SettingsAdapter(this, SettingsActivity.this,intArray
                   , mInputType));
        } else if (mInputType.equals(RIDE_TYPE_MARQUEE) || mInputType.equals(RIDE_TYPE_POPULAR) || mInputType.equals(OUR_WORLD_EVENTS)) {
            mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.text_label_policies).toUpperCase());
            mRecyclerView.setAdapter(new SettingsAdapter(this, SettingsActivity.this,
                    mPosition, mInputType));

        }
    }

    @Override
    public void onNavigationIconClick() {
        Bundle params = new Bundle();
        params.putString("eventCategory", "Settings");
        params.putString("eventAction", "Close clicked");
        REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_logout) {
            Bundle params = new Bundle();
            params.putString("eventCategory", "Settings");
            params.putString("eventAction", "Logout clicked");
            REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
            REUtils.showConfirmationDialog(SettingsActivity.this, getResources().getString(R.string.logout), getResources().getString(R.string.do_you_want_to_logout), () -> mLogoutPresenter.logout());
        }
    }

    @Override
    public void onLogoutSuccess() {
        gotoSignin();
    }

    @Override
    public void onLogoutFailure(String errorMessage) {
        // REUtils.showErrorDialog(this, errorMessage);
        gotoSignin();
    }

    public void forgotMeConfirmtion(){
        REUtils.showForgetConfirmationDialog(SettingsActivity.this, getResources().getString(R.string.forget_me), getResources().getString(R.string.delete_confirm), () ->mLogoutPresenter.forgetMe());

    }



    public void gotoSignin() {
        REUtils.CHECK_VEHICLE_PENDING = false;
		REUtils.CHECK_VEHICLE_SYNCING_FAILED = false;
        FirestoreManager.getInstance().removeFirestoreEvents();
        try {
            REPreference.getInstance().removeAll(getApplicationContext());
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        REUtils.setFCMTokenSent(false);
        REUtils.navigateToSplash();
    }


    @Override
    public void onForgotSuccess() {
		isUserActive=true;
        mLogoutPresenter.logout();
    }

    @Override
    public void onForgotFailure(String errorMessage) {
        REUtils.showErrorDialog(this, errorMessage);
    }
}
