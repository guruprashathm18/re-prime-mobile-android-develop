package com.royalenfield.reprime.ui.onboarding.address.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.realTimeDatabase.FirebaseManager;
import com.royalenfield.firebase.realTimeDatabase.OnStateandCityResponseCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.StateModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.presenter.MasterDataCallback;
import com.royalenfield.reprime.ui.userdatavalidation.popup.views.CustomDialog;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

public class AddLocationActivity extends REBaseActivity implements View.OnClickListener, TitleBarView.OnNavigationIconClickListener,
        MasterDataCallback {

    private EditText mLocality, mStreet, mCity, mPincode, mState;
    private ImageView imageView_location;
    private TextView mUseCurrentLocation;
    private HashMap<String, String> mAddressHashMap = new HashMap<>();
    String ADDRESS;
    boolean isfromService;
    ArrayList<StateModel> mStateData =  new ArrayList<>();
    private ArrayList<CityModel> mCityData = new ArrayList<>();
    private StateModel selectedStateModel;
    private CityModel mSelectedCity;
    private List<String> state_list = new ArrayList<>();
    private Typeface mTypefaceBold, mTypefaceRegular;

    boolean mIsProfileStateAlreadySet = false;
    boolean mIsProfileCityAlreadySet = false;


    public static Intent getIntent(Context context) {
        return new Intent(context, AddLocationActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address_location);
        initViews();

        bindData();
    }

    private void bindData() {
        if (getIntent().getExtras() != null) {
            mAddressHashMap = (HashMap<String, String>) getIntent().getSerializableExtra("ADDRESS");
            isfromService = getIntent().getBooleanExtra("isfromService",false);
        }
        if (mAddressHashMap!=null) {
            mLocality.setText(mAddressHashMap.get("name"));
            mStreet.setText(mAddressHashMap.get("street"));
            mCity.setText(mAddressHashMap.get("city"));
            mPincode.setText(mAddressHashMap.get("pin"));
            mState.setText(mAddressHashMap.get("state"));
        }
        if (isfromService){
            mUseCurrentLocation.setVisibility(View.GONE);
            imageView_location.setVisibility(View.GONE);
        }
    }

    // Initializing Views
    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.tb_add_address);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().
                getString(R.string.title_add_address));
        mLocality = findViewById(R.id.editText_address);
        mStreet = findViewById(R.id.editText_address_1);
        mPincode = findViewById(R.id.editText_address_3);
        mState = findViewById(R.id.editText_State);
        mState.setOnClickListener(this);
        mCity = findViewById(R.id.editText_City);
        mCity.setOnClickListener(this);
        mUseCurrentLocation = findViewById(R.id.textView_use_currentlocation);
        Button button_saveaddress = findViewById(R.id.button_saveaddress);
        button_saveaddress.setOnClickListener(this);

        mUseCurrentLocation.setOnClickListener(this);
        imageView_location = findViewById(R.id.imageView_location);
        //To set font programatically.
        mTypefaceBold = ResourcesCompat.getFont(this, R.font.montserrat_bold);
        mTypefaceRegular = ResourcesCompat.getFont(this, R.font.montserrat_regular);

        FirestoreManager.getInstance().getStateMasterFirestore("IN",this);

    }

    /**
     * This is for getting edittext texts and appending to one string
     */
    private void getTexts() {
         ADDRESS = mLocality.getText().toString() + "," + mStreet.getText().toString() + "\n" +
                mCity.getText().toString() + "," + mPincode.getText().toString() + "\n" +
                mState.getText().toString();
    }

    /**
     * This sends the address to previous activity via Intent data
     */
    private void saveAddress() {
        if (!mLocality.getText().toString().isEmpty() && !mStreet.getText().toString().isEmpty()
                && !mCity.getText().toString().isEmpty() && !mPincode.getText().toString().isEmpty()
                && !mState.getText().toString().isEmpty()) {
            getTexts();
            saveaddresstoPreference(ADDRESS);
            updateAddressHashMap();
            int requestCode = 1;
            Intent intent = new Intent();
            intent.putExtra("ADDRESS", mAddressHashMap);
            intent.putExtra("address", ADDRESS);
            setResult(requestCode, intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveaddresstoPreference(String address) {
        try {
            REPreference.getInstance().putString(getApplicationContext(), "address", address);
        } catch (PreferenceException e) {
            RELog.e(e);
        }
    }

    /**
     * Update the address hash map values.
     */
    private void updateAddressHashMap() {
        mAddressHashMap =  new HashMap<>();
        mAddressHashMap.put("name", mLocality.getText().toString());
        mAddressHashMap.put("street", mStreet.getText().toString());
        mAddressHashMap.put("city", mCity.getText().toString());
        mAddressHashMap.put("pin", mPincode.getText().toString());
        mAddressHashMap.put("state", mState.getText().toString());
    }

    /**
     * @param v This handles onClick actions
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_saveaddress:
                Bundle paramDoor = new Bundle();
                paramDoor.putString("eventCategory", "Motorcycle Drop");
                    paramDoor.putString("eventAction",  "Add Address");
                paramDoor.putString("eventLabel", "Save Address click");
                REUtils.logGTMEvent(REConstants.KEY_SERVICE_GTM, paramDoor);
                saveAddress();
                break;
            case R.id.textView_use_currentlocation:
                setResult(2);
                finish();
                break;
            case R.id.editText_State:
                performActionOnStateClicked();
                break;
            case R.id.editText_City:
                performActionOnCityClicked();
                break;
        }
    }

    private void performActionOnCityClicked() {
        if (!TextUtils.isEmpty(mState.getText())) {
            if (mCityData.size() > 0) {
                CustomDialog customDialog = CustomDialog.getInstance(CustomDialog.MasterDataType.CITY.mDataType
                        , mCityData, 0, 0);
                customDialog.show(getSupportFragmentManager(), "CityData");
            } else {
                REUtils.showErrorDialog(this,getResources().getString(R.string.data_not_available));
            }
        } else {
            REUtils.showErrorDialog(this,getResources().getString(R.string.plesae_select_state));
        }
    }

    private void performActionOnStateClicked() {
        if (mStateData.size() > 0) {
            //hideStateError();
            CustomDialog customDialog = CustomDialog.getInstance(CustomDialog.MasterDataType.STATE.mDataType, mStateData, 1);
            customDialog.show(getSupportFragmentManager(), "StateData");
        } else {
            REUtils.showErrorDialog(this,getResources().getString(R.string.data_not_available));
        }
    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    //List of states from Firebase
    @Override
    public void onStateDataObtained(ArrayList<StateModel> stateList) {
        mStateData.clear();
        if (stateList != null && stateList.size() > 0) {
            mStateData.addAll(stateList);
            mState.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        }
    }

    @Override
    public void onCityDataObtained(List<CityModel> cityList) {
        if (cityList != null && cityList.size() > 0) {
            mCityData.clear();
            mCityData.addAll(cityList);
            mCity.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        }


    }


    @Override
    public void onCountryModelsObtained(List<CountryModel> countryModels) {

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


    //when user selects a state from the drop down
    public void onStateSelected(int adapterPosition) {
        for (StateModel stateModel : mStateData) {
            if (stateModel.getSelected() == 1) {
                stateModel.setSelected(0);
            }
        }

        selectedStateModel = mStateData.get(adapterPosition);
        selectedStateModel.setSelected(1);
        mState.setText(selectedStateModel.getDescription());
        clearCity();
        //get list of city for selected state
        FirestoreManager.getInstance().getCityMasterFirestore(selectedStateModel.getCode(), selectedStateModel.getCountryCode(),this);
    }

    public void onCitySelected(int adapterPosition) {
        for (CityModel cityModel : mCityData) {
            if (cityModel.getSelected() == 1) {
                cityModel.setSelected(0);
            }
        }
        mSelectedCity = mCityData.get(adapterPosition);
        mSelectedCity.setSelected(1);
        mCity.setText(mSelectedCity.getCityName());
    }

    private void clearCity() {
        mSelectedCity = null;
        mCity.setText("");
        mCityData.clear();
        //mTextCityTitle.setVisibility(View.GONE);
    }
}
