package com.royalenfield.reprime.ui.userdatavalidation.popup.presenter;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.royalenfield.firestore.userinfo.ProvisioningData;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.onboarding.login.interactor.UpdateDetailInteractor;
import com.royalenfield.reprime.ui.onboarding.login.listeners.OnUpdateDetailFinishListner;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.CustomDialogInteracter;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.OnUpdateContactFinishedListener;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.TransparentPopActivity;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.UpdateAddressRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.UpdateAddressResponseModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.StateModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.AddressInfoRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.ContactInformationRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.UpdateContactRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.AddressInfo;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.ContactData;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.EmergencyData;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.UpdateContactResponseModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.views.PopUpView;
import com.royalenfield.reprime.utils.REUtils;
import com.royalenfield.workmanager.ProvisioningWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.royalenfield.reprime.utils.REConstants.KEY_UPDATE_PROVISIONING;

public class PopUpPresenter implements IPopUpPresenter, MasterDataCallback
        , UserContactResponseCallback, OnUpdateContactFinishedListener, OnUpdateDetailFinishListner {


    private static final String KEY_EMERGENCY = "KEY_EMERGENCY";
    private static final String KEY_ADDRESS = "KEY_ADDRESS";
    private static final String KEY_EMAIL = "KEY_EMAIL";
    private static final String KEY_SECONDARY = "KEY_SECONDARY";

    private final CustomDialogInteracter mPopUpInteractor;
    private PopUpView mPopUpView;
    private ContactData mUserContactData;
    HashMap<String, APIStatus> hashMap = new HashMap<>();

    LastUpdatedUserDataModel lastUpdatedModel;
    private boolean isPrimaryUpdateRequired;
    private boolean isSecondaryAddRequired;
    private boolean isPrimaryChecked;
    private UpdateDetailInteractor updateDetailInteractor;

    public PopUpPresenter(PopUpView popUpView, UpdateDetailInteractor updateDetailInteractor) {
        mPopUpView = popUpView;
        mPopUpInteractor = new CustomDialogInteracter(this);
        lastUpdatedModel = new LastUpdatedUserDataModel();
        this.updateDetailInteractor = updateDetailInteractor;
    }

    @Override
    public void onConfirmButtonClicked(String emergency, String email, String primaryPhone, CountryModel emergencyCode, CountryModel s1CountryCode
            , CountryModel selectedCountry, StateModel selectedState
            , CityModel selectedCity, String pinCode, String add1, String add2, boolean isPrimaryChecked) {

        boolean isEmergencyPhoneNumberValid;
        boolean isEmailIdValid;
        boolean isSecondaryPhone1Valid;
        boolean isPinCodeValid;
        boolean isAddressValid;
        this.isPrimaryChecked = isPrimaryChecked;
        if (mUserContactData == null)
            if(REUserModelStore.getInstance().getProfileData()!=null)
            mUserContactData = REUserModelStore.getInstance().getProfileData().getContactDetails();
        if (mUserContactData != null) {
            if (mPopUpView != null) {
                isSecondaryPhone1Valid = isPrimaryPhone1Valid(primaryPhone, s1CountryCode);
                isEmergencyPhoneNumberValid = isEmergencyPhoneValid(emergency, emergencyCode, isPrimaryChecked, primaryPhone, s1CountryCode);
                isEmailIdValid = isEmailValid(email);
                isPinCodeValid = isPinCodeValid(pinCode);
                isAddressValid = isAddressDetailsValid(add1, add2, selectedCountry, selectedState, selectedCity, pinCode);

                //Local validations are done
                if (isSecondaryPhone1Valid && isEmergencyPhoneNumberValid && isEmailIdValid && isPinCodeValid && isAddressValid) {


                    mPopUpView.showLoader();
                    boolean isAnyApiCalled = false;


                    if (lastUpdatedModel.getEmergencyPhoneNumber() != null) {
                        if (!emergency.equals(lastUpdatedModel.getEmergencyPhoneNumber())) {
                            makeUpdateEmergencyContactAPICall(emergencyCode.getDiallingcode().substring(1), emergency, false);
                            isAnyApiCalled = true;
                        } else if (!emergencyCode.getDiallingcode().equals("+" + lastUpdatedModel.getEmergencyCountryCode())) {
                            makeUpdateEmergencyContactAPICall(emergencyCode.getDiallingcode().substring(1), emergency, false);
                            isAnyApiCalled = true;
                        }
                    } else if (isEmergencyPhoneNumObtainedFromUserdataNotEmpty()) {
                        if (!emergency.equals(mUserContactData.getMobile().getEmergency().getNumber())) {
                            makeUpdateEmergencyContactAPICall(emergencyCode.getDiallingcode().substring(1), emergency, false);
                            isAnyApiCalled = true;
                        } else if (!emergencyCode.getDiallingcode().equals("+" + mUserContactData.getMobile().getEmergency().getCallingCode())) {
                            makeUpdateEmergencyContactAPICall(emergencyCode.getDiallingcode().substring(1), emergency, false);
                            isAnyApiCalled = true;
                        }
                    } else {
                        if (!TextUtils.isEmpty(emergency)) {

                            // Add Emergency
                            makeUpdateEmergencyContactAPICall(emergencyCode.getDiallingcode().substring(1), emergency, true);
                            isAnyApiCalled = true;

                        }
                    }


                    if (lastUpdatedModel.getLastUpdatedEmail() != null) {
                        if (!email.equals(lastUpdatedModel.getLastUpdatedEmail())) {
                            makeUpdateEmailContactAPICall(email);
                            isAnyApiCalled = true;
                        }
                    } else if (mUserContactData.getEmail() != null) {
                        if (!email.equals(mUserContactData.getEmail())) {
                            makeUpdateEmailContactAPICall(email);
                            isAnyApiCalled = true;
                        }
                    }

                    if (lastUpdatedModel.getLastUpdatedAddress() != null && selectedCity != null) {
                        if (isAdd1NotMatched(add1, lastUpdatedModel.getLastUpdatedAddress().getAddress1())
                                || isAdd2NotMatched(add2, lastUpdatedModel.getLastUpdatedAddress().getAddress2())
                                || isCityNotMatched(selectedCity.getCityName(), lastUpdatedModel.getLastUpdatedAddress().getCity())
                                || isRegionNotMatched(selectedState.getDescription(), lastUpdatedModel.getLastUpdatedAddress().getState())
                                || isCountryNotMatched(selectedCountry.getDescription(), lastUpdatedModel.getLastUpdatedAddress().getCountry())
                                || isPincodeNotMatched(pinCode, lastUpdatedModel.getLastUpdatedAddress().getPinCode())) {
                            makeUpdateAddressAPICall(add1, add2, selectedCountry, selectedState, selectedCity, pinCode, false);
                            isAnyApiCalled = true;
                        }

                    } else if (isAddressObtainedFromUserProfileNotEmpty() && selectedCity != null) {
                        if (isAdd1NotMatched(add1, getAddressInfo().getAddress1()) || isAdd2NotMatched(add2, getAddressInfo().getAddress2())
                                || isCityNotMatched(selectedCity.getCityName(), getAddressInfo().getCity())
                                || isRegionNotMatched(selectedState.getDescription(), getAddressInfo().getRegion())
                                || isCountryNotMatched(selectedCountry.getDescription(), getAddressInfo().getCountry())
                                || isPincodeNotMatched(pinCode, getAddressInfo().getZip())) {
                            makeUpdateAddressAPICall(add1, add2, selectedCountry, selectedState, selectedCity, pinCode, false);
                            isAnyApiCalled = true;
                        }

                    } else {
                        if (!TextUtils.isEmpty(add1) && !TextUtils.isEmpty(add2)
                                && selectedCountry != null && selectedState != null && selectedCity != null
                                && TransparentPopActivity.checkPin?!TextUtils.isEmpty(pinCode):true) {
                            // Add Address
                            makeUpdateAddressAPICall(add1, add2, selectedCountry, selectedState, selectedCity, pinCode, true);
                            isAnyApiCalled = true;
                        }
                    }

                    if (isPrimaryPhoneNumObtainedFromUserdataNotEmpty()) {
                        if (!primaryPhone.equals(mUserContactData.getMobile().getPrimary().getNumber()) && !TextUtils.isEmpty(primaryPhone)) {
                            isPrimaryUpdateRequired = true;
                        } else if (s1CountryCode != null && s1CountryCode.getDiallingcode() != null && s1CountryCode.getDiallingcode().length() > 1 && !s1CountryCode.getDiallingcode().substring(1, s1CountryCode.getDiallingcode().length()).equals(mUserContactData.getMobile().getPrimary().getCallingCode()) && !TextUtils.isEmpty(primaryPhone)) {
                            isPrimaryUpdateRequired = true;
                        } else {
                            isPrimaryUpdateRequired = false;
                        }
                    }


                    if (!isAnyApiCalled) {
                        mPopUpView.detailsAreValid(false, isPrimaryUpdateRequired);
                        mPopUpView.hideLoader();
                    }

                }
            }
        } else {
            mPopUpView.showErrorOnView(REApplication.getAppContext().getResources().getString(R.string.something_went_wrong));
        }
    }

    private AddressInfo getAddressInfo() {
        return REUserModelStore.getInstance().getProfileData().getAddressInfo().get(0).getAddressInfo();
    }

    private void isAllAPIsSuccess(String errorMsg) {

        boolean isStatusDefault = false;

        boolean isStatusFail = false;

        for (Map.Entry<String, APIStatus> map : hashMap.entrySet()) {
            if (map.getValue() == APIStatus.DEFAULT) {
                isStatusDefault = true;
                break;
            }

            if (map.getValue() == APIStatus.API_FAILURE) {
                isStatusFail = true;
                break;
            }
        }
        if (!isStatusDefault) {
            mPopUpView.hideLoader();

            if (isStatusFail) {
                mPopUpView.showTryAgainError(errorMsg);
            } else {
                mPopUpView.detailsAreValid(true, isPrimaryUpdateRequired);
            }
        }

    }

    private boolean isAddressDetailsValid(String add1, String add2, CountryModel selectedCountry, StateModel selectedState, CityModel selectedCity, String pinCode) {

        boolean isValid;
        if (!TextUtils.isEmpty(add1) || !TextUtils.isEmpty(add2)
                || selectedCountry != null || selectedState != null
                || selectedCity != null || !TextUtils.isEmpty(pinCode)) {

            boolean add1Valid;
            boolean add2Valid;
            boolean countryValid;
            boolean stateValid;
            boolean cityValid;
            boolean pincodeValid;

            if (TextUtils.isEmpty(add1)) {
                mPopUpView.showErrorOnAddress1(R.string.please_enter_add1);
                add1Valid = false;
            } else {
                add1Valid = true;
            }

            if (TextUtils.isEmpty(add2)) {
                mPopUpView.showErrorOnAddress2(R.string.please_enter_add2);
                add2Valid = false;
            } else {
                add2Valid = true;
            }

            if (selectedCountry == null) {
                mPopUpView.showErrorOnCountry(R.string.please_select_country);
                countryValid = false;
            } else {
                countryValid = true;
            }

            if (selectedState == null) {
                mPopUpView.showErrorOnState(R.string.plesae_select_state);
                stateValid = false;
            } else {
                stateValid = true;
            }

            if (selectedCity == null) {
                mPopUpView.showErrorOnCity(R.string.please_select_city);
                cityValid = false;
            } else {
                cityValid = true;
            }

            if (TextUtils.isEmpty(pinCode)) {
                mPopUpView.showErrorOnPinCode(R.string.please_enter_pincode);
                pincodeValid = false;
            } else {
                pincodeValid = true;
            }
            if(!TransparentPopActivity.checkPin)
                pincodeValid=true;

            if (add1Valid && add2Valid && countryValid && stateValid && cityValid && pincodeValid) {
                isValid = true;
            } else {
                isValid = false;
            }

        } else {
            isValid = true;
        }

        return isValid;
    }

    private void makeUpdateAddressAPICall(String add1, String add2, CountryModel selectedCountry
            , StateModel selectedState, CityModel selectedCity, String pinCode, boolean isAdd) {

        UpdateAddressRequestModel requestModel = new UpdateAddressRequestModel();

        if (!isAdd) {
            requestModel.setAddressId(REUserModelStore.getInstance().getProfileData().getAddressInfo().get(0).getAddressID());
        }
        requestModel.setDefaultShipping(true);
        requestModel.setDefaultBilling(true);

        ContactInformationRequestModel contactInformationRequestModel = new ContactInformationRequestModel();
        contactInformationRequestModel.setFName(REUserModelStore.getInstance().getProfileData().getfName());
        contactInformationRequestModel.setLName(REUserModelStore.getInstance().getProfileData().getlName());
        contactInformationRequestModel.setAddressType("HOME");
        contactInformationRequestModel.setPhoneNo(mUserContactData.getMobile().getPrimary().getNumber());
        contactInformationRequestModel.setCallingCode(mUserContactData.getMobile().getPrimary().getCallingCode());
        requestModel.setContactInformationRequestModel(contactInformationRequestModel);

        AddressInfoRequestModel addressInfoRequestModel = new AddressInfoRequestModel();
        addressInfoRequestModel.setAddress1(add1);
        addressInfoRequestModel.setAddress2(add2);
        addressInfoRequestModel.setCountry(selectedCountry.getDescription());
        addressInfoRequestModel.setCountryCode(selectedCountry.getCode());
        addressInfoRequestModel.setRegion(selectedState.getDescription());
        addressInfoRequestModel.setRegionCode(selectedState.getCode());
        addressInfoRequestModel.setCity(selectedCity.getCityName());
        addressInfoRequestModel.setZip(pinCode);
        requestModel.setAddressInfoRequestModel(addressInfoRequestModel);

        mPopUpInteractor.updateAddress(KEY_ADDRESS, requestModel, isAdd, this);

        hashMap.put(KEY_ADDRESS, APIStatus.DEFAULT);
    }

    private boolean isPincodeNotMatched(String pincode, String savedPinCode) {
        return !pincode.equals(savedPinCode);
    }

    private boolean isCountryNotMatched(String country, String savedCountry) {
        return !country.equals(savedCountry);
    }

    private boolean isRegionNotMatched(String state, String savedState) {
        return !state.equals(savedState);
    }

    private boolean isCityNotMatched(String cityName, String savedCity) {
        return !cityName.equals(savedCity);
    }

    private boolean isAdd2NotMatched(String add2, String savedAddress2) {
        return !add2.equals(savedAddress2);
    }

    private boolean isAdd1NotMatched(String add1, String savedAddress1) {
        return !add1.equals(savedAddress1);
    }

    private boolean isAddressObtainedFromUserProfileNotEmpty() {
        return REUserModelStore.getInstance().getProfileData().getAddressInfo().size() > 0;
    }

    private void makeUpdateEmailContactAPICall(String email) {
        UpdateContactRequestModel requestModel = new UpdateContactRequestModel();
        requestModel.setEmail(email);
        mPopUpInteractor.updateContact(KEY_EMAIL, requestModel, false, this);

        hashMap.put(KEY_EMAIL, APIStatus.DEFAULT);
    }

    private boolean isEmailObtainedFromUserDataNotEmpty() {
        return !TextUtils.isEmpty(mUserContactData.getEmail());
    }

    private void makeUpdateEmergencyContactAPICall(String code, String emergency, boolean isAdd) {
        UpdateContactRequestModel requestModel = new UpdateContactRequestModel();
        requestModel.setCallingCode(code);
        requestModel.setEmergency(emergency);
        mPopUpInteractor.updateContact(KEY_EMERGENCY, requestModel, isAdd, this);

        hashMap.put(KEY_EMERGENCY, APIStatus.DEFAULT);
    }

    private boolean isEmergencyPhoneNumObtainedFromUserdataNotEmpty() {
        return !TextUtils.isEmpty(mUserContactData.getMobile().getEmergency().getNumber());
    }

    private void makeUpdateSecondaryContactAPICall(String code, String secondaryPhone1Num, boolean isAdd) {

        UpdateContactRequestModel requestModel = new UpdateContactRequestModel();
        requestModel.setCallingCode(code);
        requestModel.setSecondary(secondaryPhone1Num);
        mPopUpInteractor.updateContact(KEY_SECONDARY, requestModel, isAdd, this);
        hashMap.put(KEY_SECONDARY, APIStatus.DEFAULT);
    }

    private boolean isPrimaryPhoneNumObtainedFromUserdataNotEmpty() {
        return !TextUtils.isEmpty(mUserContactData.getMobile().getPrimary().getNumber());
    }

    private boolean isPinCodeValid(String pinCode) {
        boolean isPinCodeValid;
        int min=REUtils.getGlobalValidations().getMinPin();
        int max=REUtils.getGlobalValidations().getPincode();
if(REApplication.getInstance().validPin!=null){
    min= REApplication.getInstance().validPin.getMin();
    max= REApplication.getInstance().validPin.getMax();
}
        if (TextUtils.isEmpty(pinCode)) {
            isPinCodeValid = false;
        } else if (pinCode.length() >= min&&pinCode.length() <= max) {
            isPinCodeValid = true;
        } else {
            mPopUpView.showErrorOnPinCode(R.string.please_enter_valid_pincode);
            isPinCodeValid = false;
        }
        if(!TransparentPopActivity.checkPin)
            isPinCodeValid=true;
        return isPinCodeValid;
    }

    private String getPrimaryPhoneNumber() {
        return mUserContactData.getMobile().getPrimary().getNumber();
//        return REApplication.getInstance()
//                .getUserLoginDetails().getData().getUser().getPhone();
    }

    private String getPrimaryCountryCode() {
        return mUserContactData.getMobile().getPrimary().getCallingCode();

//        return REApplication.getInstance()
//                .getUserLoginDetails().getData().getUser().getCountryCode();
    }

    private boolean isPrimaryPhone1Valid(String secondaryPhone1, CountryModel secondaryCode1) {
        boolean isSecondaryPhoneValid = false;
        int min=REUtils.getGlobalValidations().getMinPhoneNum();
        int max=REUtils.getGlobalValidations().getPhoneNumber();
        if(secondaryCode1!=null&&secondaryCode1.getValidations()!=null&&secondaryCode1.getValidations().getPhoneNumber()!=null&&secondaryCode1.getValidations().getPhoneNumber().getMin()!=null) {
            min = secondaryCode1.getValidations().getPhoneNumber().getMin();
            max=secondaryCode1.getValidations().getPhoneNumber().getMax();
        }
        if (isInputFieldEmpty(secondaryPhone1)) {
            mPopUpView.showErrorOnPrimaryNumber(R.string.enter_valid_phone_num);
        } else if (secondaryPhone1.length()<min||secondaryPhone1.length()>max) {
            mPopUpView.showErrorOnPrimaryNumber(R.string.text_valid_phone_error);
        } else if (secondaryCode1 == null) {
            mPopUpView.showErrorOnPrimaryNumber(R.string.please_select_country_Code);
        } else {
            isSecondaryPhoneValid = true;
        }
        return isSecondaryPhoneValid;
    }

    public boolean isEmailValid(String email) {
        boolean isEmailIdValid = false;
        if (isInputFieldEmpty(email)) {
            mPopUpView.showErrorOnEmail(R.string.email_id_is_manadatory);
        } else if (isEmailPatternValid(email)) {
            mPopUpView.hideEmailError();
            isEmailIdValid = true;
        } else {
            mPopUpView.showErrorOnEmail(R.string.enter_valid_email_error);
        }
        return isEmailIdValid;
    }

    private boolean isEmergencyPhoneValid(String emergency, CountryModel code, boolean isPrimaryChecked
            , String secondaryPhoneNum, CountryModel secondaryCountryCode) {

        int min=REUtils.getGlobalValidations().getMinPhoneNum();
        int max=REUtils.getGlobalValidations().getPhoneNumber();
       if(code!=null&&code.getValidations()!=null&&code.getValidations().getPhoneNumber()!=null&&code.getValidations().getPhoneNumber().getMin()!=null){
           min=code.getValidations().getPhoneNumber().getMin();
           max=code.getValidations().getPhoneNumber().getMax();
       }
        boolean isEmergencyPhoneNumberValid = false;
        if (isInputFieldEmpty(emergency)) {
            mPopUpView.showErrorOnEmergency(R.string.emergency_phone_error);
        } else if (emergency.length()<min||emergency.length()>max) {
            mPopUpView.showErrorOnEmergency(R.string.text_valid_phone_error);
        } else if (code == null) {
            mPopUpView.showErrorOnEmergency(R.string.please_select_country_Code);
        } else if (isPhoneNumberSameAsPrimary(emergency, code.getDiallingcode())) {
            mPopUpView.showErrorOnEmergency(R.string.primary_phone_is_not_allowed);
        } else if (secondaryPhoneNum != null && secondaryCountryCode != null && isPhoneNumberSameAsSecondary(emergency, code.getDiallingcode()
                , secondaryPhoneNum, secondaryCountryCode.getDiallingcode()) ) {
            mPopUpView.showErrorOnEmergency(R.string.primary_phone_is_not_allowed);
        } else {
            mPopUpView.hideEmergencyError();
            isEmergencyPhoneNumberValid = true;
        }
        return isEmergencyPhoneNumberValid;
    }

    private boolean isPhoneNumberSameAsSecondary(String emergency, String dialingCode, String secondaryPhoneNum, String secondaryCountryCode) {
        return emergency.equals(secondaryPhoneNum) && dialingCode.equals(secondaryCountryCode);
    }

    private boolean isPhoneNumberSameAsPrimary(String phoneNum, String countryCode) {
        return getPrimaryPhoneNumber() != null && getPrimaryPhoneNumber().equals(phoneNum)
                && countryCode.equals("+" + getPrimaryCountryCode());
    }


    @Override
    public void onDestroy() {
        mPopUpView = null;
    }

    private boolean isEmailPatternValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isInputFieldEmpty(String inputField) {
        return TextUtils.isEmpty(inputField);
    }

    @Override
    public void getStateData(String code) {
        mPopUpInteractor.getStateMasterFirestore(code);
    }

    @Override
    public void getCityData(String stateCode, String countryCode) {
        mPopUpInteractor.getCityMasterFirestore(stateCode, countryCode);
    }

    @Override
    public void getUserContactDetails() {
        mPopUpInteractor.getUserContactDetails(this);
    }

    @Override
    public void checkIsSwappable(String secondaryPhone, String callingCode) {
        mPopUpInteractor.checkIfNumIsSwappable(secondaryPhone, callingCode, this);
    }

    @Override
    public void onStateDataObtained(ArrayList<StateModel> stateList) {
        if (mPopUpView != null)
            mPopUpView.onStateDataObtained(stateList);
    }

    @Override
    public void onCountryModelsObtained(List<CountryModel> countryModels) {
        if (mPopUpView != null)
            mPopUpView.onCountryDataObtained(countryModels);
    }

    @Override
    public void onCityDataObtained(List<CityModel> cityList) {
        if (mPopUpView != null)
            mPopUpView.onCityObtained(cityList);
    }

    @Override
    public void onUserContactSuccess(ContactData data) {
        mUserContactData = data;

        if (mPopUpView != null) {
            if (mUserContactData != null && mUserContactData.getMobile() != null && mUserContactData.getMobile().getPrimary() != null) {
                mPopUpView.setPrimaryPhoneOnView(mUserContactData.getMobile().getPrimary().getNumber());
                mPopUpView.setPrimaryCountryCode(mUserContactData.getMobile().getPrimary().getCallingCode());
            }
            if (mUserContactData != null && mUserContactData.getMobile() != null && mUserContactData.getMobile().getSecondary() != null
                    && !TextUtils.isEmpty(mUserContactData.getMobile().getSecondary().getNumber())
                    && !TextUtils.isEmpty(mUserContactData.getMobile().getSecondary().getCallingCode())) {

                if (mUserContactData.getMobile().getSecondary().isIsVerified()) {
                    mPopUpView.setSecondaryNumOnView(mUserContactData.getMobile().getSecondary().getNumber());
                    mPopUpView.setSecondaryCountryCodeOnView(mUserContactData.getMobile().getSecondary().getCallingCode());
                } else {
                    mPopUpInteractor.deleteSecondaryNum(this);
                }
            }

            if (mUserContactData != null && mUserContactData.getMobile() != null && mUserContactData.getMobile().getEmergency() != null && !TextUtils.isEmpty(mUserContactData.getMobile().getEmergency().getNumber())) {
                mPopUpView.setEmergencyNumOnView(mUserContactData.getMobile().getEmergency().getNumber());

            }
            if(mUserContactData != null &&mUserContactData.getMobile().getEmergency()!=null&&mUserContactData.getMobile().getEmergency().getCallingCode()!=null){
                mPopUpView.setEmergencyCountryCodeOnView(mUserContactData.getMobile().getEmergency().getCallingCode());
            }
        }
    }

    public void checkPrimaryIsUpdatable(String callingCode, String phone, String email) {
        updateDetailInteractor.verifyPersonalDetail("fName", "lName", callingCode, phone, email, this);
    }

    @Override
    public void onUserContactFail(String message) {
    }

    @Override
    public void onUpdateContactSuccess(UpdateContactResponseModel body, String updateType
            , UpdateContactRequestModel updateContactRequestModel) {
        hashMap.put(updateType, APIStatus.API_SUCCESS);


        if (lastUpdatedModel != null) {
            switch (updateType) {
                case KEY_EMAIL:
                    lastUpdatedModel.setLastUpdatedEmail(updateContactRequestModel.getEmail());
                    break;

                case KEY_EMERGENCY:
                    lastUpdatedModel.setEmergencyPhoneNumber(updateContactRequestModel.getEmergency());
                    lastUpdatedModel.setEmergencyCountryCode(updateContactRequestModel.getCallingCode());
                    EmergencyData emergencyData=new EmergencyData();
                    emergencyData.setCallingCode(updateContactRequestModel.getCallingCode());
                    emergencyData.setNumber(updateContactRequestModel.getEmergency());
                    REUserModelStore.getInstance().getProfileData().getContactDetails().getMobile().setEmergency(emergencyData);
                    List<String> list=REUtils.getConnectedBike(REApplication.getAppContext());
                    if(list.size()>0) {
                        try {
                            REPreference.getInstance().putBoolean(REApplication.getAppContext(),KEY_UPDATE_PROVISIONING,true);
                        } catch (PreferenceException e) {
                            e.printStackTrace();
                        }
                        ProvisioningData provisioningData = new ProvisioningData();
                        ProvisioningData.EmergencyNoDetails emergencyNoDetails = new ProvisioningData.EmergencyNoDetails();
                        emergencyNoDetails.setCallingCode(updateContactRequestModel.getCallingCode());
                        emergencyNoDetails.setEmergencyNo(updateContactRequestModel.getEmergency());
                        ProvisioningData.PrimaryNoDetails primaryNoDetails = new ProvisioningData.PrimaryNoDetails();
                        if (mUserContactData != null) {
                            primaryNoDetails.setCallingCode(mUserContactData.getMobile().getPrimary().getCallingCode());
                            primaryNoDetails.setPrimaryNo(mUserContactData.getMobile().getPrimary().getNumber());
                        }

                        provisioningData.setNewPrimaryNoDetails(primaryNoDetails);
                        provisioningData.setNewEmergencyNoDetails(emergencyNoDetails);
                        provisioningData.setBikesOwned(list);
                        String data = new Gson().toJson(provisioningData);
                        REUtils.startWorker(data);
                    }
                    break;

                case KEY_SECONDARY:
                    lastUpdatedModel.setSecondaryPhoneNumber(updateContactRequestModel.getSecondary());
                    lastUpdatedModel.setSecondaryCountryCode(updateContactRequestModel.getCallingCode());
                    //isSecondaryUpdateRequired = true;
                    break;
            }
        }
        isAllAPIsSuccess(null);
    }

    @Override
    public void onUpdateContactFail(String messageId, String type) {
        hashMap.put(type, APIStatus.API_FAILURE);
        isAllAPIsSuccess(messageId);
    }

    @Override
    public void onUpdateAddressSuccess(UpdateAddressResponseModel body, String type
            , UpdateAddressRequestModel updateAddressRequestModel) {
        hashMap.put(type, APIStatus.API_SUCCESS);
        isAllAPIsSuccess(null);

        LastUpdateAddressModel lastUpdateAddressModel = new LastUpdateAddressModel();
        lastUpdateAddressModel.setAddress1(updateAddressRequestModel.getAddressInfoRequestModel().getAddress1());
        lastUpdateAddressModel.setAddress2(updateAddressRequestModel.getAddressInfoRequestModel().getAddress2());
        lastUpdateAddressModel.setCountry(updateAddressRequestModel.getAddressInfoRequestModel().getCountry());
        lastUpdateAddressModel.setCountryCode(updateAddressRequestModel.getAddressInfoRequestModel().getCountryCode());
        lastUpdateAddressModel.setState(updateAddressRequestModel.getAddressInfoRequestModel().getRegion());
        lastUpdateAddressModel.setStateCode(updateAddressRequestModel.getAddressInfoRequestModel().getRegionCode());
        lastUpdateAddressModel.setCity(updateAddressRequestModel.getAddressInfoRequestModel().getCity());
        lastUpdateAddressModel.setAddressId(updateAddressRequestModel.getAddressId());
        lastUpdateAddressModel.setPinCode(updateAddressRequestModel.getAddressInfoRequestModel().getZip());

        lastUpdatedModel.setLastUpdatedAddress(lastUpdateAddressModel);
    }

    @Override
    public void onUpdateAddressFail(String message, String type) {
        hashMap.put(type, APIStatus.API_FAILURE);
        isAllAPIsSuccess(message);
    }

    @Override
    public void onDeleteSecondarySuccess() {

    }

    @Override
    public void onDeleteSecondaryFail() {

    }

    @Override
    public void onSwapApprovalSuccess() {
        mPopUpView.hideLoader();
        mPopUpView.showOtpWithSwapped();
    }

    @Override
    public void onSwapApprovalFail(String message) {
        mPopUpView.hideLoader();
        mPopUpView.showErrorOnView(message);
        mPopUpView.uncheckedSwapping();
    }

    @Override
    public void onUpdateDetailFailure(String errorMessage) {
        mPopUpView.updateFailure(errorMessage);
    }

    @Override
    public void onUpdateDetailSuccess() {
        mPopUpView.updateSuccess();
    }
}
