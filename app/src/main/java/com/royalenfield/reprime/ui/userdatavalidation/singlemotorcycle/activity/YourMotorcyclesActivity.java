package com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.userdatavalidation.detailsconfirmation.activity.DetailsConfirmationActivity;
import com.royalenfield.reprime.ui.userdatavalidation.popup.Constants;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.interactor.YourMotorcycleInteractor;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.presenter.YourMotorcyclePresenter;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.view.UpdateVehicleData;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.views.IYourMotorcyclePresenter;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.views.YourMotorcycleView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YourMotorcyclesActivity extends AppCompatActivity
        implements YourMotorcycleView, View.OnClickListener {

    @BindView(R.id.txvMotorcycleTitle)
    TextView mTextMotorcycleTitle;

    @BindView(R.id.textInputEngineNum)
    TextInputLayout mTextInputEngine;

    @BindView(R.id.textEditEngineNum)
    TextInputEditText mTextEditEngine;

    @BindView(R.id.textInputRegistrationNum)
    TextInputLayout mTextInputRegistrationNum;

    @BindView(R.id.textEditRegistrationNum)
    TextInputEditText mTextEditRegistrationNum;

    @BindView(R.id.checkbox_own_motorcycle)
    ImageView mCheckboxOwnMotorcycle;

    @BindView(R.id.imgMotorcycle)
    ImageView mImageMotorcycle;

    private boolean isChecked;

    private IYourMotorcyclePresenter mYourMotorcyclePresenter;
    private Button mBtnConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_motorcycle);
        ButterKnife.bind(this);

        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(this);

        mYourMotorcyclePresenter = new YourMotorcyclePresenter(this, new YourMotorcycleInteractor());
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.tvTitle);
            textView.setText(getString(R.string.your_motorcycles));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        initializeView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        REApplication.getInstance().setComingFromVehicleOnboarding(false);
        startActivity(new Intent(YourMotorcyclesActivity.this, REHomeActivity.class));
        finish();
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            REApplication.getInstance().setComingFromVehicleOnboarding(false);
            startActivity(new Intent(this, REHomeActivity.class));
            finish();
        }
        return true;
    }

    private void initializeView() {
        VehicleDataModel vehicleData = getVehicleDataFromSharedPreference();
        mTextEditEngine.setText(vehicleData.getEngineNo());
        mTextEditRegistrationNum.setText(vehicleData.getRegistrationNo().trim());
        mBtnConfirm.setText(getString(R.string.confirm));
        DisplayMetrics displayMetrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int h=(int)width*2/3;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width,h);
        mImageMotorcycle.setLayoutParams(param);
        REUtils.getFirebaseReactUrl(this, vehicleData.getModelCode(), mImageMotorcycle, mTextMotorcycleTitle,true,null);

//        mTextMotorcycleTitle.setText(vehicleData.getModelName());
//        Glide.with(this)
//                .load(vehicleData.getImageUrl()).placeholder(R.drawable.classic_500_chrome)
//                .fitCenter()
//                .into(mImageMotorcycle);

        mTextEditRegistrationNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "UDV-Show Motorcycles");
                    params.putString("eventAction", vehicleData.getModelName());
                    params.putString("eventLabel", "Update Registration number click");
                    REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);
                }
            }
        });
        mTextEditRegistrationNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               hideRegistartionError();
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                mYourMotorcyclePresenter.isRegistrationNumberValid(editable.toString());
            }
        });

        mCheckboxOwnMotorcycle.setOnClickListener(this);
    }

    private VehicleDataModel getVehicleDataFromSharedPreference() {
        if (REServiceSharedPreference.getVehicleData(this) != null) {
            return REServiceSharedPreference.getVehicleData(this).get(0);
        } else {
            return new VehicleDataModel();
        }

    }


    public void showConfirmationScreen(int id) {
        showConfirm(id);
    }

    public void showConfirm(int id) {
        Intent intent = new Intent(YourMotorcyclesActivity.this
                , DetailsConfirmationActivity.class);
        intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_TITLE, getString(id));
        intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_MESSAGE, "You already have 1 Motorcycle linked to your mobile number");
        intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_Button, getString(R.string.view_my_profile));
        intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_TYPE, getString(R.string.motorcycle_screen_type));
        startActivity(intent);
        finish();
    }

    @Override
    public void showEmptyError() {
        mTextInputRegistrationNum.setError(getString(R.string.enter_registration_num));
    }

    @Override
    public void showNumRangeError() {
        mTextInputRegistrationNum.setError(getString(R.string.registrationNumRangeError));
    }

    @Override
    public void showAlphaNumericError() {
        mTextInputRegistrationNum.setError(getString(R.string.show_num_error));
    }

    @Override
    public void showNumberError() {
        mTextInputRegistrationNum.setError(getString(R.string.show_num_error));
    }

    @Override
    public void showLettersError() {
        mTextInputRegistrationNum.setError(getString(R.string.show_num_error));
    }

    @Override
    public void hideRegistartionError() {
        mTextInputRegistrationNum.setError(null);
        mTextInputRegistrationNum.setErrorEnabled(false);
    }

    @Override
    protected void onDestroy() {
        mYourMotorcyclePresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_confirm:
                Bundle params = new Bundle();
                params.putString("eventCategory", "UDV-Show Motorcycles");
                params.putString("eventAction", "Confirm click");
               REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);
                VehicleDataModel vehicleData = getVehicleDataFromSharedPreference();

                if (mTextEditRegistrationNum.getText() != null) {

                    UpdateVehicleData updateVehicleData = new UpdateVehicleData(vehicleData.getEngineNo()
                            , vehicleData.getModelName()
                            , vehicleData.getRegistrationNo(), isChecked,vehicleData.getChaissisNo());


                    mYourMotorcyclePresenter.onConfirmButtonClicked(updateVehicleData, mTextEditRegistrationNum.getText().toString().trim());
                }
                break;

            case R.id.checkbox_own_motorcycle:
                if (isChecked) {
                    mCheckboxOwnMotorcycle.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_box));
                    isChecked = false;
                } else {
                    VehicleDataModel vehicleData2 = getVehicleDataFromSharedPreference();
                    Bundle params1 = new Bundle();
                    params1.putString("eventCategory", "UDV-Show Motorcycles");
                    params1.putString("eventAction", vehicleData2.getModelName());
                    params1.putString("eventLabel", "I don't own this motorcycle click");
                   REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params1);
                    mCheckboxOwnMotorcycle.setImageDrawable(getResources().getDrawable(R.drawable.checkbox));
                    isChecked = true;
                }
                break;
        }
    }
}
