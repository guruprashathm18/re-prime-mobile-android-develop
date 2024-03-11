package com.royalenfield.reprime.ui.userdatavalidation.popup.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.login.LoginResponse;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.editprofile.listeners.OnEditProfileFinishedListener;
import com.royalenfield.reprime.ui.onboarding.login.interactor.UpdateDetailInteractor;
import com.royalenfield.reprime.ui.userdatavalidation.detailsconfirmation.activity.DetailsConfirmationActivity;
import com.royalenfield.reprime.ui.userdatavalidation.otp.activity.OtpActivity;
import com.royalenfield.reprime.ui.userdatavalidation.popup.Constants;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.StateModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.presenter.IPopUpPresenter;
import com.royalenfield.reprime.ui.userdatavalidation.popup.presenter.PopUpPresenter;
import com.royalenfield.reprime.ui.userdatavalidation.popup.views.CustomDialog;
import com.royalenfield.reprime.ui.userdatavalidation.popup.views.PopUpView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.royalenfield.reprime.utils.REConstants.CUSTOM_COUNTRY;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_DISABLED;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;

public class TransparentPopActivity extends REBaseActivity implements PopUpView {

    @BindView(R.id.txv_emergency_error)
    TextView mTextEmergencyError;

    @BindView(R.id.edit_emergency_country_code)
    EditText mEmergencyEditCountryCode;

    @BindView(R.id.edit_emergency_phone)
    EditText mEditEmergencyPhone;

    @BindView(R.id.scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.txv_country_title)
    TextView mTextCountryTitle;

    @BindView(R.id.edit_country)
    EditText mEditCountry;

    @BindView(R.id.txv_country_error)
    TextView mTextCountryError;

    @BindView(R.id.txv_state_title)
    TextView mTextStateTitle;

    @BindView(R.id.edit_state)
    EditText mEditState;

    @BindView(R.id.txv_state_error)
    TextView mTextStateError;

    @BindView(R.id.edit_primary_country_code)
    EditText mEditPrimaryCountryCode;

    @BindView(R.id.edit_primary_phone)
    EditText mEditPrimaryPhone;

    @BindView(R.id.text_email)
    TextView mTextEmail;

    @BindView(R.id.edit_email)
    EditText mEditEmail;

    @BindView(R.id.txv_email_error)
    TextView mEmailError;

    @BindView(R.id.txv_city_title)
    TextView mTextCityTitle;

    @BindView(R.id.edit_city)
    EditText mEditCity;

    @BindView(R.id.txv_city_error)
    TextView mTextCityError;

    @BindView(R.id.txv_pin_code_title)
    TextView mTextPinCodeTitle;

    @BindView(R.id.edit_pin_code)
    EditText mEditPinCode;

    @BindView(R.id.txv_pin_code_error)
    TextView mTextPinCodeError;

    @BindView(R.id.txv_address_title1)
    TextView mAddressTitle1;

    @BindView(R.id.edit_address_title1)
    EditText mEditAddress1;

    @BindView(R.id.txv_address_title2)
    TextView mAddressTitle2;

    @BindView(R.id.edit_address_title2)
    EditText mEditAddress2;

    @BindView(R.id.txv_emergency_phone_view)
    TextView mTextEmergencyPhoneView;

    @BindView(R.id.txv_address_error1)
    TextView mAddressError1;
    @BindView(R.id.txt_header)
    TextView mTextHeader;


    @BindView(R.id.txv_primary_error)
    TextView primaryNumberError;

    @BindView(R.id.txv_address_error2)
    TextView mAddressError2;

    @BindView(R.id.tb_edit_profile)
    TitleBarView tbEditProfile;
    private static final String TAG = TransparentPopActivity.class.getSimpleName();
    private IPopUpPresenter mPopUpPresenter;
    private boolean doubleBackToExitPressedOnce = false;
    //  private EditText mEditSecondaryCountryCode1;
    private boolean isPrimary3Checked;
    private boolean isPrimary2Checked;
    private boolean isPrimary1Checked;
    @BindView(R.id.ll_city_view)
    LinearLayout llCity;
    @BindView(R.id.ll_pin_code)
    LinearLayout llPin;
    public static boolean checkPin=true;

    private ArrayList<StateModel> mStateData = new ArrayList<>();
    private ArrayList<CountryModel> mCountryData = new ArrayList<>();
    private ArrayList<CityModel> mCityData = new ArrayList<>();

    private CountryModel selectedCountryModel;
    private StateModel selectedStateModel;
    private CityModel mSelectedCity;

    private CountryModel mSelectedEmergencyCountryCode;

    private ArrayList<CountryModel> mEmergencyCountryData = new ArrayList<>();
    private ArrayList<CountryModel> mS1CountryData = new ArrayList<>();
    private CountryModel mSelectedS1CountryCode;
    private TextView mTextSecondaryPhone1;
    boolean mIsProfileStateAlreadySet = false;
    boolean mIsProfileCityAlreadySet = false;
    boolean fromEdiTprofile;
    boolean isFromConnected;
    private boolean isPrimaryCheckedRequiredByUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       // Log.e(TAG, "BUTTON CLICK");
        if (getIntent().hasExtra(REConstants.KEY_NAVIGATION_FROM)) {
            fromEdiTprofile = true;
            setTheme(R.style.Theme_FullScreen_StatusBar);
        }
        if (getIntent().hasExtra(REConstants.KEY_NAVIGATION_FROM_AUTHORIZE)) {
            isFromConnected = true;
            setTheme(R.style.Theme_FullScreen_StatusBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_pop_up);
        ButterKnife.bind(this);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (isFromConnected) {
            mTextHeader.setText(R.string.emergency_update_header);
        }
     	if(REUserModelStore.getInstance().getProfileData()==null){
			REUtils.showErrorDialog(this, getResources().getString(R.string.we_are_unable_to_fetch), () -> finish());
		}
        initViews();
      //  Log.e(TAG, "COUNTY FETCH");
        if(!fromEdiTprofile||(REUtils.getFeatures().getDefaultFeatures()!=null&&REUtils.getFeatures().getDefaultFeatures().getEditPrimaryNo().equalsIgnoreCase(FEATURE_DISABLED))){
            mEditPrimaryPhone.setFocusable(false);
            mEditPrimaryPhone.setEnabled(false);
            mEditPrimaryPhone.setTextColor(getResources().getColor(R.color.white_50));
            mEditPrimaryCountryCode.setFocusable(false);
            mEditPrimaryCountryCode.setEnabled(false);
            mEditPrimaryCountryCode.setClickable(false);
            mEditPrimaryCountryCode.setTextColor(getResources().getColor(R.color.white_50));
        }
//        if(REApplication.getInstance().featureCountry!=null&&!REApplication.getInstance().featureCountry.getEditPrimaryNo().equalsIgnoreCase(FEATURE_ENABLED)) {
//            mEditPrimaryPhone.setFocusable(false);
//            mEditPrimaryPhone.setEnabled(false);
//            mEditPrimaryCountryCode.setFocusable(false);
//            mEditPrimaryCountryCode.setEnabled(false);
//            mEditPrimaryCountryCode.setClickable(false);
//        }
         mPopUpPresenter = new PopUpPresenter(this,new UpdateDetailInteractor());
        //  mPopUpPresenter.getCountryData();
        setCountryAdapters();

    }


    private void initViews() {
        if(BuildConfig.FLAVOR.contains("EU")) {
            llCity.setVisibility(View.GONE);
            llPin.setVisibility(View.GONE);
        }
        mEditPinCode.setFilters(new InputFilter[] {new InputFilter.LengthFilter(REUtils.getGlobalValidations().getPincode())});
        mEditEmergencyPhone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(REUtils.getGlobalValidations().getPhoneNumber())});
        mEditPrimaryPhone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(REUtils.getGlobalValidations().getPhoneNumber())});

        mEditPrimaryPhone.setText(getPrimaryPhoneNumber());
        //    mEditPrimaryCountryCode.setText(getString(R.string.primary_default_country_code));
        mEditAddress1.setText(getAddress1FromProfile());
        mEditAddress1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "UDV Pop up");
                    params.putString("eventAction", "Edit details");
                    params.putString("eventLabel", "Address line 1");
                    REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);
                }
            }
        });
        mEditAddress2.setText(getAddress2FromProfile());
        mEditAddress2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "UDV Pop up");
                    params.putString("eventAction", "Edit details");
                    params.putString("eventLabel", "Address line 2");
                    REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);
                }
            }
        });
        mEditPinCode.setText(getPinCodeFromProfile());
        mEditEmail.setText(getEmail());
        mEditEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "UDV Pop up");
                    params.putString("eventAction", "Edit details");
                    params.putString("eventLabel", "email ID");
                    REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);
                }
            }
        });


        if (!TextUtils.isEmpty(mEditEmail.getText())) {
            mTextEmail.setVisibility(View.VISIBLE);
        }
        initializeSecondaryView();
        if (fromEdiTprofile||isFromConnected) {
            //  emptyView.setVisibility(View.GONE);
            mScrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tbEditProfile.bindData(this, R.drawable.ic_back_arrow,
                    getApplicationContext().getResources().getString(R.string.text_edit_profile_title));
            tbEditProfile.setTitleCaps(false);

            tbEditProfile.ivNavButton.setOnClickListener(v -> onBackPressed());

        } else {
            //   emptyView.setVisibility(View.VISIBLE);
            tbEditProfile.setVisibility(View.GONE);


        }

    }

    private void initializeSecondaryView() {
        mEditPrimaryPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideSecondary1Error();
                setSecondaryTitleVisibility();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEditPrimaryCountryCode.setOnClickListener(view -> {
            if (mS1CountryData.size() > 0) {
                CustomDialog customDialog = CustomDialog.getInstance(CustomDialog.MasterDataType.S1_COUNTRY_CODE.mDataType
                        , mS1CountryData);
                customDialog.show(getSupportFragmentManager(), REConstants.COUNTRY_CODE);
            }
        });
    }

    private String getEmail() {
        if (REUserModelStore.getInstance().getProfileData() != null)
            return REUserModelStore.getInstance().getProfileData().getContactDetails().getEmail();
        else return "";
    }

    private String getPinCodeFromProfile() {
        if (REUserModelStore.getInstance().getProfileData() != null && REUserModelStore.getInstance().getProfileData().getAddressInfo().size() > 0) {
            return REUserModelStore.getInstance().getProfileData().getAddressInfo().get(0).getAddressInfo().getZip();
        } else {
            return "";
        }
    }

    private String getAddress2FromProfile() {
        if (REUserModelStore.getInstance().getProfileData() != null && REUserModelStore.getInstance().getProfileData().getAddressInfo().size() > 0) {
            return REUserModelStore.getInstance().getProfileData().getAddressInfo().get(0).getAddressInfo().getAddress2();
        } else {
            return "";
        }
    }

    private String getAddress1FromProfile() {
        if (REUserModelStore.getInstance().getProfileData() != null && REUserModelStore.getInstance().getProfileData().getAddressInfo().size() > 0) {
            return REUserModelStore.getInstance().getProfileData().getAddressInfo().get(0).getAddressInfo().getAddress1();
        } else {
            return "";
        }
    }


    private void setEmergencyTitleVisibility() {
        if (TextUtils.isEmpty(mEditEmergencyPhone.getText())) {
            mTextEmergencyPhoneView.setVisibility(View.GONE);
        } else {
            mTextEmergencyPhoneView.setVisibility(View.VISIBLE);
        }
    }

    private void setSecondaryTitleVisibility() {
//        if (TextUtils.isEmpty(mEditSecondary1.getText())) {
//            mTextSecondaryPhone1.setVisibility(View.GONE);
//        } else {
//            mTextSecondaryPhone1.setVisibility(View.VISIBLE);
//        }
    }

    @OnTextChanged(R.id.edit_emergency_phone)
    public void onEmergencyPhoneClicked() {
        hideEmergencyError();
        setEmergencyTitleVisibility();
    }

    @OnTextChanged(R.id.edit_email)
    public void onEmailTextChanged() {

        hideEmailError();
        if (TextUtils.isEmpty(mEditEmail.getText())) {
            mTextEmail.setVisibility(View.GONE);
        } else {
            mTextEmail.setVisibility(View.VISIBLE);
        }
    }

    @OnTextChanged(R.id.edit_pin_code)
    public void onPinCodeTextChanged() {
        if (TextUtils.isEmpty(mEditPinCode.getText())) {
            mTextPinCodeTitle.setVisibility(View.GONE);
        } else {
            mTextPinCodeTitle.setVisibility(View.VISIBLE);
        }
        mTextPinCodeError.setVisibility(View.GONE);
    }
    @OnTextChanged(R.id.edit_address_title1)
    public void onAddress1TitleChanged() {
        if (TextUtils.isEmpty(mEditAddress1.getText())) {
            mAddressTitle1.setVisibility(View.GONE);
        } else {
            mAddressTitle1.setVisibility(View.VISIBLE);
        }
        mAddressError1.setVisibility(View.GONE);
    }

    @OnTextChanged(R.id.edit_address_title2)
    public void onAddress2TitleChanged() {
        if (TextUtils.isEmpty(mEditAddress2.getText())) {
            mAddressTitle2.setVisibility(View.GONE);
        } else {
            mAddressTitle2.setVisibility(View.VISIBLE);
        }
        mAddressError2.setVisibility(View.GONE);
    }

    @OnClick(R.id.edit_country)
    public void onCountryClicked() {
        performActionOnCountryClicked();
    }

    private void hideSecondary1Error() {
        primaryNumberError.setVisibility(View.GONE);
    }

    private String getPrimaryPhoneNumber() {
        if (REUserModelStore.getInstance().getProfileData() != null)
            return REUserModelStore.getInstance().getProfileData().getContactDetails().getMobile().getPrimary().getNumber();
        else
            return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    @OnClick(R.id.imgConfirm)
    public void onConfirmButtonClicked(View view) {
        Bundle params = new Bundle();
        if (!isPrimary1Checked) {

            params.putString("eventCategory", "UDV Pop up");
            params.putString("eventAction", "Edit details");
            params.putString("eventLabel", "Confirm click");
        } else {
            params.putString("eventCategory", "UDV Pop up");
            params.putString("eventAction", "Swap Phone numbers");
            params.putString("eventLabel", "Confirm click");
        }
        REUtils.logGTMEvent(REConstants.KEY_UDV_GTM, params);

        if (mEditEmergencyPhone.getText() != null && mEditEmail.getText() != null
                && mEditPrimaryPhone.getText() != null  && mEditAddress1.getText() != null && mEditAddress2.getText() != null) {
            mPopUpPresenter.onConfirmButtonClicked(REUtils.removeLeadingZeroes(mEditEmergencyPhone.getText().toString())
                    , mEditEmail.getText().toString(), REUtils.removeLeadingZeroes(mEditPrimaryPhone.getText().toString())
                    , mSelectedEmergencyCountryCode, mSelectedS1CountryCode
                    , selectedCountryModel, selectedStateModel
                    , mSelectedCity, mEditPinCode.getText().toString(), mEditAddress1.getText().toString()
                    , mEditAddress2.getText().toString(), isPrimary1Checked);
        }


    }

    public void detailsAreValid(boolean isAnyApiCalled, boolean isUpdatePrimaryNumber) {
        REUserModelStore.getInstance().setEmail(!mEditEmail.getText().equals("") ? mEditEmail.getText().toString() : "");
        REUserModelStore.getInstance().getProfileData().getContactDetails().setEmail(!mEditEmail.getText().equals("") ? mEditEmail.getText().toString() : "");

        if (isUpdatePrimaryNumber) {
            showLoading();
            mPopUpPresenter.checkPrimaryIsUpdatable(mEditPrimaryCountryCode.getText().toString(),REUtils.removeLeadingZeroes(mEditPrimaryPhone.getText().toString()),
                    mEditEmail.getText().toString());
        } else {
            if (!fromEdiTprofile&&!isFromConnected) {
                if (isAnyApiCalled) {
                    showConfirmationScreen(R.string.details_validation_title);
                } else {
                    showConfirmationScreen(R.string.thankyou_msg);
                }
            } else {
                if (REUtils.isUserLoggedIn()) {
                    REUtils.getProfileDetailsFromServer(new OnEditProfileFinishedListener() {
                        @Override
                        public void onUpdateSuccess() {

                        }

                        @Override
                        public void onUpdateFailure(String errorMessage) {

                        }

                        @Override
                        public void onGetProfileDetailsSuccess() {

                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void onGetProfileDetailsFailure(String errorCode) {

                        }
                    });
                }

            }

        }

        if (!isUpdatePrimaryNumber) {
//            Map<String, Object> popupFlags = new HashMap<>();
//            popupFlags.put(REConstants.SHOW_USER_VALIDATION_POPUP, false);
//            popupFlags.put(REConstants.SHOW_VEHICLE_ONBOARDING_POPUP, false);
          //  FirestoreManager.getInstance().updateUserSettingFirebaseField(popupFlags);
        }


    }


    @Override
    public void showOtpWithSwapped() {
        boolean isSecondaryUpdateReq = true;
        boolean isSecondaryAddReq = false;
        // showOtpScreen(mEditSecondary1.getText().toString(), isSecondaryUpdateReq, isSecondaryAddReq, isPrimary1Checked);
    }

    @Override
    public void showErrorOnView(String message) {
        REUtils.showErrorDialog(this, message);
    }

    //Removed as per QA Suggestion
    @Override
    public void uncheckedSwapping() {
        //  mPrimaryCheckbox1.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_box));
        //isPrimary1Checked = false;
    }

    @Override
    public void updateSuccess() {
        hideLoading();
        showOtpScreen(REUtils.removeLeadingZeroes(mEditPrimaryPhone.getText().toString()));
    }



    @Override
    public void updateFailure(String errorMessage) {
        REUtils.showErrorDialog(this, errorMessage);
    }

    private String getCurrentLoggedInUser() {
        LoginResponse loginResponse = REApplication.getInstance().getUserLoginDetails();
        if (loginResponse != null) {
            return REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId();
        } else {
            return null;
        }
    }

    public void showErrorOnSecondary1(int error) {
//        mTextSecondaryError1.setText(getString(error));
//        mTextSecondaryError1.setVisibility(View.VISIBLE);
//        int pos = mEditSecondary1.getTop();
//        mScrollView.scrollTo(0, pos);
    }


    @Override
    public void onStateDataObtained(List<StateModel> stateList) {
        mStateData.clear();
       // Log.e(TAG, "STATE FETCH COMPLETE");
        if (stateList != null && stateList.size() > 0) {
            mStateData.addAll(stateList);
            mEditState.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        }

        if (mStateData.size() > 0 && !mIsProfileStateAlreadySet) {

            if (!TextUtils.isEmpty(getStateFromProfileData())) {


                String userStateCode = getStateFromProfileData();
                int position = getAdapterPositionOfState(userStateCode);
                if (position >= 0) {
                    onStateSelected(position);
                    mIsProfileStateAlreadySet = true;
                }
            }
        }
    }

    private int getAdapterPositionOfState(String userStateCode) {
        int pos = 0;
        boolean isContained = false;
        for (StateModel model : mStateData) {
            if (userStateCode.equalsIgnoreCase(model.getCode())) {
                isContained = true;
                break;
            }
            pos++;
        }
        if (!isContained) {
            pos = -1;
        }
        return pos;
    }

    private String getStateFromProfileData() {
        if (REUserModelStore.getInstance().getProfileData() != null && REUserModelStore.getInstance().getProfileData().getAddressInfo().size() > 0) {
            return REUserModelStore.getInstance().getProfileData().getAddressInfo().get(0).getAddressInfo().getRegionCode();
        } else {
            return "";
        }
    }

    private void setCountryAdapters() {
        mCountryData.clear();
        mEmergencyCountryData.clear();
        mS1CountryData.clear();
        if (REApplication.getInstance().getCountryList() == null)
            REUtils.getCountryData();
        for (CountryModel model : REApplication.getInstance().getCountryList()) {
            if (model.getShowInCountryList())
                mCountryData.add(model.clone());
            mEmergencyCountryData.add(model.clone());
            mS1CountryData.add(model.clone());
        }

        CountryModel countryModel = null;
        for (CountryModel c : REApplication.getInstance().getCountryList()) {
            if (c.getCode().equalsIgnoreCase(REApplication.getInstance().getUserLoginDetails().getData().getUser().getLocality()!=null?REApplication.getInstance().getUserLoginDetails().getData().getUser().getLocality().getCountry():REApplication.getInstance().Country)) {
                countryModel = c;
                break;
            }
        }
        if(countryModel!=null&&countryModel.getValidations()!=null) {
            REApplication.getInstance().validMobile = countryModel.getValidations().getPhoneNumber();
            REApplication.getInstance().validPin = countryModel.getValidations().getPincode();
        }
        mEditCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        if(fromEdiTprofile){
            mEditPrimaryCountryCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        }

        mEmergencyEditCountryCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        mPopUpPresenter.getUserContactDetails();
        if (!TextUtils.isEmpty(getCountryFromProfileData())) {
            String userCountry = getCountryFromProfileData();
            int position = getAdapterPositionOfCountry(userCountry);
            if (position >= 0)
                onCountrySelected(position);
        } else {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String countryCodeValue = REApplication.getInstance().Country;
            if (countryCodeValue == null)
                countryCodeValue= REApplication.getInstance().Country;
           // Log.e("Country", countryCodeValue);
            for (int i = 0; i < mCountryData.size(); i++) {
                if (mCountryData.get(i).getCode().equalsIgnoreCase(countryCodeValue)) {
                    onCountrySelected(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onCountryDataObtained(List<CountryModel> countryModels) {
      //  Log.e(TAG, "COUNTY FETCH COMPLETE");
        mCountryData.clear();
        mEmergencyCountryData.clear();
        mS1CountryData.clear();
        for (CountryModel model : countryModels) {
            if (model.getShowInCountryList())
                mCountryData.add(model.clone());
            mEmergencyCountryData.add(model.clone());
            mS1CountryData.add(model.clone());
        }
        mEditCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        if(fromEdiTprofile){
            mEditPrimaryCountryCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        }
        mEmergencyEditCountryCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        mPopUpPresenter.getUserContactDetails();
        if (!TextUtils.isEmpty(getCountryFromProfileData())) {
            String userCountry = getCountryFromProfileData();
            int position = getAdapterPositionOfCountry(userCountry);
            if (position >= 0)
                onCountrySelected(position);
        }
    }

    private int getAdapterPositionOfCountry(String userCountry) {
        int pos = 0;
        boolean isContained = false;
        for (CountryModel model : mCountryData) {
            if (userCountry.equalsIgnoreCase(model.getDescription())) {
                isContained = true;
                break;
            }
            pos++;
        }
        if (!isContained) {
            pos = -1;
        }
        return pos;
    }

    private String getCountryFromProfileData() {
        if (REUserModelStore.getInstance().getProfileData() != null && REUserModelStore.getInstance().getProfileData().getAddressInfo().size() > 0) {
            return REUserModelStore.getInstance().getProfileData().getAddressInfo().get(0).getAddressInfo().getCountry();
        } else {
            return "";
        }
    }

    @Override
    public void onCityObtained(List<CityModel> cityList) {
        if (cityList != null && cityList.size() > 0) {
            mCityData.clear();
            mCityData.addAll(cityList);
            mEditCity.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        }
        else{
            CityModel cityModel=new CityModel(selectedStateModel.getDescription());
            mCityData.add(cityModel);
        }
        if (mCityData.size() > 0 && !mIsProfileCityAlreadySet) {
            if (!TextUtils.isEmpty(getCityFromProfileData())) {
                String userCity = getCityFromProfileData();
                int position = getAdapterPositionOfCity(userCity);
                if (position >= 0) {
                    onCitySelected(position);
                    mIsProfileCityAlreadySet = true;
                }
            }
        }
    }

    private int getAdapterPositionOfCity(String userCity) {
        int pos = 0;
        boolean isContained = false;
        for (CityModel model : mCityData) {
            if (userCity.equalsIgnoreCase(model.getCityName())) {
                isContained = true;
                break;
            }
            pos++;
        }
        if (!isContained) {
            pos = -1;
        }
        return pos;
    }

    private String getCityFromProfileData() {
        if (REUserModelStore.getInstance().getProfileData() != null && REUserModelStore.getInstance().getProfileData().getAddressInfo().size() > 0) {
            return REUserModelStore.getInstance().getProfileData().getAddressInfo().get(0).getAddressInfo().getCity();
        } else {
            return "";
        }
    }

    @Override
    public void showErrorOnPinCode(int errorId) {
        boolean isPinCodeValid;
        int min=REUtils.getGlobalValidations().getMinPin() ;
        if(REApplication.getInstance().validPin!=null){
            min= REApplication.getInstance().validPin.getMin();
        }
        mTextPinCodeError.setText(getString(errorId,min));
        mTextPinCodeError.setVisibility(View.VISIBLE);
    }

    @Override
    public void setSecondaryNumOnView(String number) {

    }

    @Override
    public void setSecondaryCountryCodeOnView(String callingCode) {

        int position = getAdapterPositionOfS1CountryCode("+" + callingCode);
        if (position >= 0)
            onS1CountryCodeSelected(position);
    }

    @Override
    public void setEmergencyNumOnView(String number) {
        mEditEmergencyPhone.setText(number);
    }

    @Override
    public void setEmergencyCountryCodeOnView(String callingCode) {
        int position = getAdapterPositionOfEmergencyCountryCode("+" + callingCode);
        if (position >= 0)
            onCountryCodeSelected(position);
    }

    @Override
    public void showErrorOnAddress1(int error) {
        mAddressError1.setText(getString(error));
        mAddressError1.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorOnAddress2(int error) {
        mAddressError2.setText(getString(error));
        mAddressError2.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorOnCountry(int error) {
        mTextCountryError.setText(getString(error));
        mTextCountryError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorOnState(int error) {
        mTextStateError.setText(getString(error));
        mTextStateError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorOnCity(int error) {
        mTextCityError.setText(getString(error));
        mTextCityError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoader() {
        showLoading();
    }


    @Override
    public void showTryAgainError(String errorMsg) {

        REUtils.showErrorDialog(this,errorMsg!=null?errorMsg: getString(R.string.sorry_please_try_again));
    }



    @Override
    public void hideLoader() {
        hideLoading();
    }

    @Override
    public void setPrimaryPhoneOnView(String number) {
        mEditPrimaryPhone.setText(number);
    }

    @Override
    public void setPrimaryCountryCode(String callingCode) {
        if (!callingCode.contains("+"))
            mEditPrimaryCountryCode.setText("+" + callingCode);
        else
            mEditPrimaryCountryCode.setText(callingCode);

        int position = getAdapterPositionOfS1CountryCode("+" + callingCode);
        if (position >= 0)
            onS1CountryCodeSelected(position);
    }

    private int getAdapterPositionOfEmergencyCountryCode(String callingCode) {
        int pos = 0;
        boolean isContained = false;
        for (CountryModel model : mEmergencyCountryData) {
            if (callingCode.equalsIgnoreCase(model.getDiallingcode())) {
                isContained = true;
                break;
            }
            pos++;
        }
        if (!isContained) {
            pos = -1;
        }
        return pos;
    }

    private int getAdapterPositionOfS1CountryCode(String callingCode) {
        int pos = 0;
        boolean isContained = false;
        for (CountryModel model : mS1CountryData) {
            if (callingCode.equalsIgnoreCase(model.getDiallingcode())) {
                isContained = true;
                break;
            }
            pos++;
        }
        if (!isContained) {
            pos = -1;
        }
        return pos;
    }

    @Override
    public void showErrorOnEmergency(int id) {
        mTextEmergencyError.setVisibility(View.VISIBLE);
        mTextEmergencyError.setText(getString(id));
        int pos;
        pos = mEditEmergencyPhone.getTop();
        mScrollView.scrollTo(0, pos);
    }

    @Override
    public void showErrorOnEmail(int id) {
        mEmailError.setVisibility(View.VISIBLE);
        mEmailError.setText(getString(id));
        int pos;
        pos = mEditEmergencyPhone.getTop();
        mScrollView.scrollTo(0, pos);
    }

    @Override
    public void showErrorOnPrimaryNumber(int id) {
        primaryNumberError.setVisibility(View.VISIBLE);
        primaryNumberError.setText(getString(id));
        int pos;
        pos = mEditPrimaryPhone.getTop();
        mScrollView.scrollTo(0, pos);
    }

    @Override
    public void hideEmailError() {
        mEmailError.setVisibility(View.GONE);
    }

    @Override
    public void hideEmergencyError() {
        mTextEmergencyError.setVisibility(View.GONE);
    }

    public void showConfirmationScreen(int id) {
        if (fromEdiTprofile||isFromConnected) {
            setResult(RESULT_OK);
            finish();
        } else {
            Intent intent = new Intent(this, DetailsConfirmationActivity.class);
            intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_TITLE, getString(id));
            if (REServiceSharedPreference.getVehicleData(this).size() > 1) {
                intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_MESSAGE, "You already have " + REServiceSharedPreference.getVehicleData(this).size()
                        + " Motorcycles linked to your mobile number");
            } else {
                intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_MESSAGE, "You already have " + 1 + " Motorcycle linked to your mobile number");
            }
            intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_Button, getString(R.string.show_motorcycle));
            intent.putExtra(Constants.KEY_CONFIRMATION_SCREEN_TYPE, getString(R.string.pop_up_validation_confirmation));
            startActivity(intent);
            finish();
        }
    }

    public void showOtpScreen(String secondaryPhoneNum) {
        if (mSelectedS1CountryCode != null && mSelectedS1CountryCode.getDiallingcode() != null) {
            Intent intent = new Intent(this, OtpActivity.class);
            intent.putExtra(Constants.KEY_SECONDARY_PHONE_NUMBER, secondaryPhoneNum);
            intent.putExtra(Constants.KEY_SECONDARY_COUNTRY_CODE, mSelectedS1CountryCode.getDiallingcode());
            intent.putExtra(Constants.KEY_EMAIL, mEditEmail.getText().toString());
            intent.putExtra(Constants.KEY_EMERGENCY_PHONE_NUMBER, mEditEmergencyPhone.getText().toString());
            intent.putExtra(Constants.KEY_EMERGENCY_COUNTRY_CODE, mSelectedEmergencyCountryCode.getDiallingcode());
            intent.putExtra(Constants.KEY_FROM_PROFILE, fromEdiTprofile);
            startActivityForResult(intent, REConstants.OTP_REQUEST_CODE);
            enableProfileAndNotificationIcon();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REConstants.OTP_REQUEST_CODE && resultCode == 2) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void enableProfileAndNotificationIcon() {
    }

    @Override
    protected void onDestroy() {
        mPopUpPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (fromEdiTprofile||isFromConnected) {
           finish();
        } else {
            if (doubleBackToExitPressedOnce) {

//            setResult(RESULT_OK, null);
//            super.onBackPressed();
                finishAffinity();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            REUtils.showErrorDialog(this, getApplicationContext().getResources().
                    getString(R.string.text_back_exit));
        }
    }

    @OnClick(R.id.edit_state)
    public void onStateInputClicked() {
        performActionOnStateClicked();
    }

    @OnClick(R.id.edit_city)
    public void onCityInputClicked() {
        performActionOnCityClicked();
    }

    @OnClick(R.id.edit_emergency_country_code)
    public void onTextEditEmergencyClicked() {
        performActionOnEmergencyCountryCodeClicked();
    }

    private void performActionOnEmergencyCountryCodeClicked() {
        if (mEmergencyCountryData.size() > 0) {
            CustomDialog customDialog = CustomDialog.getInstance(CustomDialog.MasterDataType.COUNTRY_CODE.mDataType
                    , mEmergencyCountryData);
            customDialog.show(getSupportFragmentManager(), REConstants.COUNTRY_CODE);
        }
    }

    private void performActionOnCityClicked() {
        if (!TextUtils.isEmpty(mEditState.getText())) {
            if (mCityData.size() > 0) {
                mTextCityError.setVisibility(View.GONE);
                CustomDialog customDialog = CustomDialog.getInstance(CustomDialog.MasterDataType.CITY.mDataType
                        , mCityData, 0, 0);
                customDialog.show(getSupportFragmentManager(), "CityData");
            } else {
                showErrorOnCity(R.string.data_not_available);
            }
        } else {
            showErrorOnState(R.string.plesae_select_state);
        }
    }

    private void performActionOnCountryClicked() {
        hideCountryError();
        CustomDialog customDialog = CustomDialog.getInstance(CustomDialog.MasterDataType.COUNTRY.mDataType, mCountryData);
        customDialog.show(getSupportFragmentManager(), "CountryData");
    }

    private void performActionOnStateClicked() {
        if (!TextUtils.isEmpty(mEditCountry.getText())) {
            if (mStateData.size() > 0) {
                hideStateError();
                CustomDialog customDialog = CustomDialog.getInstance(CustomDialog.MasterDataType.STATE.mDataType, mStateData, 0);
                customDialog.show(getSupportFragmentManager(), "StateData");
            } else {
                showErrorOnState(R.string.data_not_available);
            }
        } else {
            showErrorOnCountry(R.string.please_select_country);
        }
    }

    public void onCountrySelected(int adapterPosition) {
       // Log.e(TAG, "COUNTRY FETCH COMPLETED");
        for (CountryModel countryModel1 : mCountryData) {
            if (countryModel1.isSelected() == 1) {
                countryModel1.setSelected(0);
            }
        }
        mTextCountryTitle.setVisibility(View.VISIBLE);
        hideStateError();
        selectedCountryModel = mCountryData.get(adapterPosition);
        selectedCountryModel.setSelected(1);
        mEditCountry.setText(selectedCountryModel.getDescription());
        mTextCityError.setVisibility(View.GONE);
        clearStateAndCity();
       // Log.e(TAG, "STATE FETCH");
        mPopUpPresenter.getStateData(selectedCountryModel.getCode());
        if(selectedCountryModel.getValidations()!=null)
            REApplication.getInstance().validPin= selectedCountryModel.getValidations().getPincode();
    }

    public void hideCountryError() {
        mTextCountryError.setVisibility(View.GONE);
        mTextCountryError.setError(null);
    }

    public void hideStateError() {
        mTextStateError.setVisibility(View.GONE);
        mTextStateError.setError(null);
    }

    private void clearStateAndCity() {
        selectedStateModel = null;
        mSelectedCity = null;
        mEditState.setText("");
        mEditCity.setText("");
        mTextStateTitle.setVisibility(View.GONE);
        mTextCityTitle.setVisibility(View.GONE);
        mEditState.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow_disabled, 0);
        mEditCity.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow_disabled, 0);
        mStateData.clear();
        mCityData.clear();
    }

    public void onStateSelected(int adapterPosition) {
        for (StateModel stateModel : mStateData) {
            if (stateModel.getSelected() == 1) {
                stateModel.setSelected(0);
            }
        }
        mTextStateTitle.setVisibility(View.VISIBLE);
        mTextStateError.setVisibility(View.GONE);
        mTextCityError.setVisibility(View.GONE);
        selectedStateModel = mStateData.get(adapterPosition);
        selectedStateModel.setSelected(1);
        mEditState.setText(selectedStateModel.getDescription());
        clearCity();
        //Log.e(TAG,"CITY FETCH");
        mPopUpPresenter.getCityData(selectedStateModel.getCode(), selectedStateModel.getCountryCode());
    }

    private void clearCity() {
        mSelectedCity = null;
        mEditCity.setText("");
        mCityData.clear();
        mTextCityTitle.setVisibility(View.GONE);
    }

    public void onCitySelected(int adapterPosition) {
        for (CityModel cityModel : mCityData) {
            if (cityModel.getSelected() == 1) {
                cityModel.setSelected(0);
            }
        }
        mTextCityTitle.setVisibility(View.VISIBLE);
        mTextCityError.setVisibility(View.GONE);
        mSelectedCity = mCityData.get(adapterPosition);
        mSelectedCity.setSelected(1);
       // Log.e(TAG,"CITY FETCH COMPLETE");
        mEditCity.setText(mSelectedCity.getCityName());
    }

    public void onCountryCodeSelected(int adapterPosition) {
        for (CountryModel countryModel : mEmergencyCountryData) {
            if (countryModel.isSelected() == 1) {
                countryModel.setSelected(0);
            }
        }
        mTextEmergencyError.setVisibility(View.GONE);
        mSelectedEmergencyCountryCode = mEmergencyCountryData.get(adapterPosition);
        mSelectedEmergencyCountryCode.setSelected(1);
        mEmergencyEditCountryCode.setText(mSelectedEmergencyCountryCode.getDiallingcode());
    }

    public void onS1CountryCodeSelected(int adapterPosition) {
        for (CountryModel countryModel : mS1CountryData) {
            if (countryModel.isSelected() == 1) {
                countryModel.setSelected(0);
            }
        }
        primaryNumberError.setVisibility(View.GONE);
        mSelectedS1CountryCode = mS1CountryData.get(adapterPosition);
        mSelectedS1CountryCode.setSelected(1);
        mEditPrimaryCountryCode.setText(mSelectedS1CountryCode.getDiallingcode());
    }

}
