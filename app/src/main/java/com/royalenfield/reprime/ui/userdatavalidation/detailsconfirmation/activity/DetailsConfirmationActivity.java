package com.royalenfield.reprime.ui.userdatavalidation.detailsconfirmation.activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.service.diy.activity.YoutubePlayerActivity;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.riderprofile.activity.REProfileActivity;
import com.royalenfield.reprime.ui.userdatavalidation.detailsconfirmation.presenter.DetailsConfirmationPresenter;
import com.royalenfield.reprime.ui.userdatavalidation.detailsconfirmation.presenter.IDetailsConfirmationPresenter;
import com.royalenfield.reprime.ui.userdatavalidation.detailsconfirmation.view.DetailsConfirmationView;
import com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.activity.MultipleMotorcycleActivity;
import com.royalenfield.reprime.ui.userdatavalidation.popup.Constants;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.activity.YourMotorcyclesActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.royalenfield.reprime.application.REApplication.mFirebaseAnalytics;

public class DetailsConfirmationActivity extends AppCompatActivity implements DetailsConfirmationView {

    @BindView(R.id.txv_title)
    TextView mTextTitle;

    @BindView(R.id.txv_view_my_profile)
    TextView mTextViewMyProfile;

    @BindView(R.id.txv_show_motorcycle)
    TextView mTextShowMotorcycle;

    @BindView(R.id.txv_message)
    TextView mTextMessage;

    private IDetailsConfirmationPresenter mDetailsConfirmationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_confirmation);
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle params = new Bundle();
                params.putString("eventCategory", "UDV Pop up");
                params.putString("eventAction", "Successful details submission");
                params.putString("eventLabel", "close click");
               REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);
                REApplication.getInstance().setComingFromVehicleOnboarding(false);
                startActivity(new Intent(DetailsConfirmationActivity.this, REHomeActivity.class));
                finish();
            }
        });
        ButterKnife.bind(this);
        mDetailsConfirmationPresenter = new DetailsConfirmationPresenter(this);
        initializeView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        REApplication.getInstance().setComingFromVehicleOnboarding(false);
        startActivity(new Intent(DetailsConfirmationActivity.this, REHomeActivity.class));
        finish();
    }

    private void initializeView() {
        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
        mTextTitle.setVisibility(View.VISIBLE);
        mTextTitle.setText(getTitleFromIntent());

        mDetailsConfirmationPresenter.setScreenType(getString(R.string.pop_up_validation_confirmation)
                , getScreenTypeFromIntent(), getMessageFromIntent());
        if (getScreenTypeFromIntent().equals(getString(R.string.pop_up_validation_confirmation))) {
            mTextShowMotorcycle.setVisibility(View.VISIBLE);
            mTextViewMyProfile.setVisibility(View.GONE);
        } else {
            mTextShowMotorcycle.setVisibility(View.GONE);
            mTextViewMyProfile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setMessageOnView(String intentMessage) {
        mTextMessage.setVisibility(View.VISIBLE);
        mTextMessage.setText(getMessageFromIntent());
    }

    @Override
    public void setMessageViewVisibility(boolean visibility) {
        if (visibility) {
            mTextMessage.setVisibility(View.VISIBLE);
        } else {
            mTextMessage.setVisibility(View.GONE);
        }
    }

    private String getButtonTextFromIntent() {
        return getIntent().getStringExtra(Constants.KEY_CONFIRMATION_SCREEN_Button);
    }

    private String getMessageFromIntent() {
        return getIntent().getStringExtra(Constants.KEY_CONFIRMATION_SCREEN_MESSAGE);
    }

    private String getTitleFromIntent() {
        return getIntent().getStringExtra(Constants.KEY_CONFIRMATION_SCREEN_TITLE);
    }

    private String getScreenTypeFromIntent() {
        return getIntent().getStringExtra(Constants.KEY_CONFIRMATION_SCREEN_TYPE);
    }


    @OnClick(R.id.txv_view_my_profile)
    public void onProfileClicked() {
        Bundle params = new Bundle();
        params.putString("eventCategory", "UDV");
        params.putString("eventAction", "Update vehicle details");
        params.putString("eventLabel", "View my Profile click");
       REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);
        startActivity(new Intent(this, REProfileActivity.class));
        overridePendingTransition(R.anim.slide_up, 0);
        finish();
    }


    @OnClick(R.id.txv_show_motorcycle)
    public void onButtonClicked() {

        Bundle params = new Bundle();
        params.putString("eventCategory", "UDV Pop up");
        params.putString("eventAction", "Successful details submission");
        params.putString("eventLabel", "Show Motorcycles click");
       REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);
        if (getScreenTypeFromIntent().equals(getString(R.string.pop_up_validation_confirmation))) {
            if (REServiceSharedPreference.getVehicleData(DetailsConfirmationActivity.this).size() == 1) {
                startActivity(new Intent(this, YourMotorcyclesActivity.class));
                finish();
            } else {
                startActivity(new Intent(this, MultipleMotorcycleActivity.class));
                finish();
            }
        }
    }

//    @Override
//    protected void onDestroy() {
//        mDetailsConfirmationPresenter.onDestroy();
//        super.onDestroy();
//    }
}
