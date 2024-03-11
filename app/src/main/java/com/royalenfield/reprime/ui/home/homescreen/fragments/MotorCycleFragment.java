package com.royalenfield.reprime.ui.home.homescreen.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.royalenfield.appIndexing.REAppIndexScreen;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.fireStore.OnAuthorizationCallback;
import com.royalenfield.firebase.fireStore.OnFirestoreCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.remoteconfig.ConfigFeatures;
import com.royalenfield.reprime.models.response.web.login.LoginResponse;
import com.royalenfield.reprime.models.response.web.profile.ProfileData;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.fragments.FindMyMotorcycleFragment;
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.OBDDataPoints;
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.OBDResponseData;
import com.royalenfield.reprime.ui.home.connected.motorcycle.fragment.PairingMotorcycleDialog;
import com.royalenfield.reprime.ui.home.connected.motorcycle.fragment.VehicleSettingsFragment;
import com.royalenfield.reprime.ui.home.connected.motorcycle.interactor.MotorcycleInteractor;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.PairingMotorcycleResponseModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.ProvisionUpdateStatusRequestModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.SettingResponseModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.presenter.MotorcyclePresenter;
import com.royalenfield.reprime.ui.home.homescreen.activity.UploadKYCActivity;
import com.royalenfield.reprime.ui.home.homescreen.customviews.SegmentedProgressBar;
import com.royalenfield.reprime.ui.home.homescreen.view.CustomerCareActivity;
import com.royalenfield.reprime.ui.home.homescreen.view.HelpAndSupportActivity;
import com.royalenfield.reprime.ui.home.homescreen.view.IMotorcycleFragmentView;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.homescreen.viewpager.MotorcycleViewPagerAdapter;
import com.royalenfield.reprime.ui.home.service.history.views.ServiceHistoryActivity;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.motorcyclehealthalert.fragment.HealthAlertFragment;
import com.royalenfield.reprime.ui.onboarding.AccountCreationSuccessActivity;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity;
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils;
import com.royalenfield.reprime.ui.riderprofile.activity.REProfileActivity;
import com.royalenfield.reprime.ui.triplisting.view.TripListingFragment;
import com.royalenfield.reprime.ui.riderprofile.activity.REWebViewActivity;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.TransparentPopActivity;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.EmergencyData;
import com.royalenfield.reprime.ui.wonderlust.view.WLWebViewActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

import static android.app.Activity.RESULT_OK;
import static com.royalenfield.reprime.base.REBaseActivity.CALL_PERMISSIONS_REQUESTS;
import static com.royalenfield.reprime.utils.REConstants.D_RSA;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;
import static com.royalenfield.reprime.utils.REConstants.KEY_D_RSA_URL;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REConstants.SETTING_ACTIVITY_INPUT_TYPE;

public class MotorCycleFragment extends REBaseFragment implements View.OnClickListener,
        ViewPager.OnPageChangeListener, OnAuthorizationCallback, IMotorcycleFragmentView {

    private WrappingViewPager viewPager;
    private ImageView[] ivArrayDotsPager;
    private LinearLayout llPagerDots;
    private List<VehicleDataModel> vehicleDataList;
    private TextView txtBikeModel;
    private TextView txtAuthorize;
    private ViewGroup llRoot;
    private TextView txtRegNo, penTxtRegNo;
    private TextView mTvHeading;
    private ImageView imgBattery;
    private TextView txtBatteryStatus;
    private ImageView imgFuel;
    private ImageView imgIgnitionIcon;
    private TextView txtFuelStatus;
    private TextView txtMeterBlock;
    private TextView txtIgnitionStatus;
    private LinearLayout llLocate;
    private LinearLayout llFuelBattery;
    private LinearLayout llVehicleDetails;
    private LinearLayout llConnectStatus;
    public ImageView imgLocate;
    private MotorcycleViewPagerAdapter motorCyclePageAdapter;
    private ScrollView scrollView;
    private TextView vehicleDetail;
    //health alert
    private TextView healthAlert;
    private String deviceId;
    private MotorcyclePresenter motorcyclePresenter;
    private Handler handler;
    private PairingMotorcycleDialog pairingDialog;
    private PairingMotorcycleResponseModel.GetDeviceData deviceData;
    private Boolean variantConnected;
    private SettingResponseModel deviceSetting;
    private boolean mIsServiceNotification = false;
    private String chessisNumber="";
    private Runnable runnable;
    private  TextView txtMeter;
    long no1;
    private Handler handlerDigit = new Handler();
    private int val = 0;
    private int selectedPage = 0;
    private Dialog consentDialog;
    private boolean isConsentTaken = true;
    private LinearLayout motorcycleChildView;
    private  ImageView imgRSA,imgService;
    private TextView txtSupport;
    private TextView txtVehicleAlertsCount;
    private SegmentedProgressBar segmentedProgressBar;
    private ImageView relFuel;
    private ImageView relBat;
    private  TextView ignitiontxt;
	private ImageView imgWing;
    private ImageView imgGps;
    private ImageView imgGsm;
    String jobCartStatus = "", regNo = "";
    public MotorCycleFragment() {
    }

    public static MotorCycleFragment newInstance(REHomeActivity reHomeActivity) {
        // Required empty public constructor
        MotorCycleFragment motorCycleFragment = new MotorCycleFragment();
        return motorCycleFragment;
    }


    public void getServiceData(String chesis,String jobCard, String regNumber){
        chessisNumber = chesis;
        jobCartStatus = jobCard;
        regNo = regNumber;
		gotoSpecificMotorcycleFromPushNotification(vehicleDataList);
    }


    @Override
    public void onResume() {
        super.onResume();
         }

    public void fetchConnectedStatus() {
        if (vehicleDataList.size() > 0 && viewPager != null) {
            FirestoreManager.getInstance().readConnectedInfoFireStore
                    (vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo(),
                            this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("BROADCAST","DETACH");
        motorcyclePresenter.disconnectStompClient();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         Log.e("BROADCASE","REGISTER");
        getBundleData();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Motorcycle");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
       // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.layout_motorcycles, container, false);
        motorcyclePresenter = new MotorcyclePresenter(this, new MotorcycleInteractor());
        initViews(rootView);
        return rootView;
    }

    private void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mIsServiceNotification = bundle.getBoolean(REConstants.IS_SERVICE_NOTIFICATION);
        }
    }



    private void initViews(View rootView) {
        handler = new Handler();
        initiateRunnable();
        deviceId = "it_170820342";
        mTvHeading = rootView.findViewById(R.id.tv_actionbar_title);
        vehicleDetail = rootView.findViewById(R.id.vehicle_detail_txt);
        mTvHeading.setText(getString(R.string.my_motorcycles));
        llRoot = rootView.findViewById(R.id.llRoot);
		llRoot.setVisibility(View.GONE);
        scrollView = rootView.findViewById(R.id.scrollView2);
        rootView.findViewById(R.id.imgNavigate).setOnClickListener(this);
        imgService= rootView.findViewById(R.id.imgService);
        imgService.setOnClickListener(this);
       imgRSA= rootView.findViewById(R.id.imgRSA);
        imgRSA.setOnClickListener(this);
        rootView.findViewById(R.id.txtServiceHistory).setOnClickListener(this);
        txtSupport=rootView.findViewById(R.id.txtGuides);
        txtSupport.setOnClickListener(this);
        txtBikeModel = rootView.findViewById(R.id.txtBikeModel);
        txtRegNo = rootView.findViewById(R.id.txtRegNo);
        txtAuthorize = rootView.findViewById(R.id.txtAuthorize);
        txtAuthorize.setOnClickListener(this);
        txtMeter=rootView.findViewById(R.id.txtMeter);
        penTxtRegNo = rootView.findViewById(R.id.pen_txtRegNo);
        viewPager = rootView.findViewById(R.id.viewpager);
        llPagerDots = rootView.findViewById(R.id.pager_dots);
        motorcycleChildView = rootView.findViewById(R.id.ll_motorcycle_container);
        txtVehicleAlertsCount=rootView.findViewById(R.id.txtVehicleAlertsCount);
        rootView.findViewById(R.id.txtTripAnalysis).setOnClickListener(this);
        rootView.findViewById(R.id.txtVehicleAlerts).setOnClickListener(this);
        rootView.findViewById(R.id.txtVehicleSettings).setOnClickListener(this);
        segmentedProgressBar=rootView.findViewById(R.id.segmented_progressbar);
        relFuel=rootView.findViewById(R.id.info_fuel);
        relBat=rootView.findViewById(R.id.info_bat);
        relFuel.setOnClickListener(this);
        relBat.setOnClickListener(this);

        initConnectedMotorcycleViews(rootView);

        /*temp code for test*/
        //setConnectedUIChanges(false);
        //txtAuthorize.setVisibility(View.GONE);

        /*set MotorCycle list in ViewPager*/
        setViewPager();
	//	gotoSpecificMotorcycleFromPushNotification(vehicleDataList);
	//	handleServiceNotification();
        /*call OBD Data and read settings*/
        //motorcyclePresenter.fetchOBDDataPoints(deviceId);
        //readDeviceSettingsFromJDBC(REUserModelStore.getInstance().getUserId(), deviceId);
    }

    private void initConnectedMotorcycleViews(View rootView) {
        imgBattery = rootView.findViewById(R.id.imgBattery);
        txtBatteryStatus = rootView.findViewById(R.id.txtBatteryStatus);
        imgFuel = rootView.findViewById(R.id.imgFuel);
        txtFuelStatus = rootView.findViewById(R.id.txtFuelStatus);
        txtMeterBlock = rootView.findViewById(R.id.txtMeterBlock);
        llLocate = rootView.findViewById(R.id.llLocate);
        llFuelBattery = rootView.findViewById(R.id.llFuelBattery);
        ignitiontxt=rootView.findViewById(R.id.txt_ignition);
		ignitiontxt.setVisibility(View.GONE);
		imgWing=rootView.findViewById(R.id.img_wing);
        llVehicleDetails = rootView.findViewById(R.id.llVehicleDetails);
        imgLocate = rootView.findViewById(R.id.imgLocate);
        /*Connected Status*/
        llConnectStatus = rootView.findViewById(R.id.llConnectStatus);
        txtIgnitionStatus = rootView.findViewById(R.id.txtIgnitionStatus);
        imgIgnitionIcon = rootView.findViewById(R.id.imgIgnitionIcon);
        imgGps=rootView.findViewById(R.id.img_gps);
        imgGsm=rootView.findViewById(R.id.img_gsm);
    }

    private void setViewPager() {
		if(REUtils.isUserLoggedIn()) {
			if (REUtils.CHECK_VEHICLE_SYNCING_INPROGRESS) {
				vehicleDetail.setVisibility(View.VISIBLE);
				vehicleDetail.setText(getString(R.string.we_are_working));
				scrollView.setVisibility(View.GONE);
			} else {
				vehicleDataList = REServiceSharedPreference.getVehicleData(REApplication.getAppContext());
				Log.e("DATA_SHARED",vehicleDataList.size()+"");
				if (vehicleDataList != null && vehicleDataList.size() > 0) {
					vehicleDetail.setVisibility(View.GONE);
					llRoot.setVisibility(View.VISIBLE);
					scrollView.setVisibility(View.VISIBLE);
					motorCyclePageAdapter = new MotorcycleViewPagerAdapter(getContext(), vehicleDataList, this);
					viewPager.setAdapter(motorCyclePageAdapter);
					setupIndicatorDots();
					if (ivArrayDotsPager != null && ivArrayDotsPager.length > 0 && getActivity() != null && !getActivity().isFinishing())
						ivArrayDotsPager[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.page_indicator_selected));
					viewPager.setOffscreenPageLimit(1);
					viewPager.addOnPageChangeListener(this);
					if (selectedPage > 0) {
						viewPager.setCurrentItem(selectedPage);
					} else {
						setViewPagerData(vehicleDataList.get(0));
					}

				} else {
					if (REUtils.CHECK_VEHICLE_SYNCING_FAILED) {
						vehicleDetail.setVisibility(View.VISIBLE);
						customTextViewVehicleFailed(vehicleDetail);
						//vehicleDetail.setText(getString(R.string.sync_issue));
						scrollView.setVisibility(View.GONE);
					} else if (REUtils.CHECK_VEHICLE_PENDING) {
						vehicleDetail.setVisibility(View.VISIBLE);
						vehicleDetail.setText(getString(R.string.we_are_working));
						scrollView.setVisibility(View.GONE);
					} else if ((vehicleDataList == null || vehicleDataList.size() == 0) && ((REHomeActivity) getActivity()).selectBottomNavigation != null && ((REHomeActivity) getActivity()).selectBottomNavigation.equals(REConstants.MOTORCYCLE)) {
						vehicleDetail.setVisibility(View.GONE);
						scrollView.setVisibility(View.GONE);
						((REHomeActivity) getActivity()).showVehiclePopUpInCaseOfNoBike();
					}
				}
			}
		}
		else{
			vehicleDetail.setVisibility(View.GONE);
			scrollView.setVisibility(View.GONE);
			((REHomeActivity) getActivity()).showVehiclePopUpInCaseOfNoBike();
		}
    }

    private void setupIndicatorDots() {
        ivArrayDotsPager = new ImageView[vehicleDataList.size()];
        for (int i = 0; i < ivArrayDotsPager.length; i++) {
            if (getActivity() != null&&isAdded()) {
                ivArrayDotsPager[i] = new ImageView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 0, 5, 0);
                ivArrayDotsPager[i].setLayoutParams(params);
                ivArrayDotsPager[i].setImageResource(R.drawable.page_indicator_unselected);
                //ivArrayDotsPager[i].setAlpha(0.4f);
                ivArrayDotsPager[i].setOnClickListener(view -> view.setAlpha(1));
                llPagerDots.addView(ivArrayDotsPager[i]);
                llPagerDots.bringToFront();

                llPagerDots.setVisibility(vehicleDataList.size() > 1 ? View.VISIBLE : View.GONE);
            }
        }
    }


    @Override
    public void onClick(View view) {
        Bundle params = new Bundle();
        switch (view.getId()) {

            case R.id.imgService:
                params = new Bundle();
                params.putString("eventCategory", "Motorcycles");
                params.putString("eventAction", "Service clicked");
                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                showService(vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo());
                break;
            case R.id.txtServiceHistory:

                showServiceHistory();
                break;
            case R.id.txtGuides:
                params = new Bundle();
                params.putString("eventCategory", "Motorcycles");
                params.putString("eventAction", "Support clicked");
                params.putString("modelName", txtBikeModel.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                Intent intentHelp=new Intent(getActivity(), HelpAndSupportActivity.class);
                intentHelp.putExtra("URL",vehicleDataList.get(viewPager.getCurrentItem()).getOwnerManual());
                intentHelp.putExtra("TYPE",((REHomeActivity)getActivity()).mReAppIndexScreen);
                ( (REHomeActivity) getActivity()).mReAppIndexScreen=REAppIndexScreen.OUR_WORLD;
                startActivity(intentHelp);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.imgRSA:
                params = new Bundle();
                params.putString("eventCategory", "Motorcycles");

                ConfigFeatures configFeature=  REUtils.getConfigFeatures();
                // find MenuItem you want to change
                if (configFeature != null && configFeature.getDrsa() != null) {
                    if (configFeature.getDrsa().getFeatureStatus().equalsIgnoreCase(REConstants.FEATURE_STATUS_DISABLED)) {
                        params.putString("eventAction", "RoadSide Assistance clicked");
                        ((REHomeActivity)getActivity()).checkAndRequestCallPermissions(getContext(), getActivity(),
                                REUtils.getREKeys().getCustomerCare(),
                                CALL_PERMISSIONS_REQUESTS, ((REHomeActivity)getActivity()));
                    }
                    else{
                        params.putString("eventAction", "DRSA clicked");
                        Intent  intentWeb = new Intent(getActivity(), REWebViewActivity.class);
                        intentWeb.putExtra(SETTING_ACTIVITY_INPUT_TYPE, D_RSA);
                        String phone = REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone();
                        intentWeb.putExtra(KEY_D_RSA_URL, REConstants.DRSA_URL+vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo()+ "/" + phone);
                        // intentWeb.putExtra(KEY_D_RSA_URL, REConstants.DRSA_URL+vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo());
                //        intentWeb.putExtra(KEY_D_RSA_URL, "https://plspps.europassistance.in:444/reindex.html?ME3J3CTS7790K5456");

                        startActivity(intentWeb);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                    }

                    REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                }


                break;
            case R.id.imgMotorcycle:
                Intent intent = new Intent(getActivity(), REProfileActivity.class);
                intent.putExtra("NAVIGATE_FROM", "DASHBOARD");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //startExplicitActivity(REProfileActivity.class);
                break;
            case R.id.txtConnect:
            case R.id.txtAuthorize:
                params = new Bundle();
                params.putString("eventCategory", "Connected Module");
                params.putString("eventAction", "Authorize clicked");
                params.putString("modelName",txtBikeModel.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
             ProfileData profileData= REUserModelStore.getInstance().getProfileData();
                EmergencyData emergencyData=profileData.getContactDetails().getMobile().getEmergency();
             if(profileData!=null&&emergencyData!=null&&emergencyData.getNumber()!=null&&!emergencyData.getNumber().equals("")){
                 getPairingKeyAndShowDialog();
             }
             else{
                 Intent i=new Intent(getActivity(), TransparentPopActivity.class);
                 i.putExtra(REConstants.KEY_NAVIGATION_FROM_AUTHORIZE,"");
                 startActivityForResult(i,1);
                 Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);

             }


                //showPairingDialog();
                /*sample Check Firestore*/
                //FirestoreManager.createConnectedInfoToFirestore("123456", "123456");
                //FirestoreManager.readConnectedInfoFireStore("123456")
                break;
            case R.id.imgNavigate:
                showNavigationMapScreen();
                break;
            case R.id.imgLocate:
                params = new Bundle();
                params.putString("eventCategory", "Connected Module");
                params.putString("eventAction", "Locate clicked");
                params.putString("modelName",txtBikeModel.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
                VehicleDataModel dataModel = vehicleDataList.get(viewPager.getCurrentItem());
new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        loadFragment(FindMyMotorcycleFragment.newInstance(dataModel.getChaissisNo(),dataModel.getModelName(),dataModel.getRegistrationNo(), deviceSetting));

    }
},500);
                 break;
            case R.id.txtTripAnalysis:
                params = new Bundle();
                params.putString("eventCategory", "Connected Module");
                params.putString("eventAction", "Trip summary clicked");
                params.putString("modelName",txtBikeModel.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
                VehicleDataModel dataModelTrip = vehicleDataList.get(viewPager.getCurrentItem());
                loadFragment(TripListingFragment.getInstance(dataModelTrip.getModelName(), dataModelTrip.getRegistrationNo()));
                break;
            case R.id.txtVehicleAlerts:
                params = new Bundle();
                params.putString("eventCategory", "Connected Module");
                params.putString("eventAction", "Vehicle alerts clicked");
                params.putString("modelName",txtBikeModel.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
                VehicleDataModel vehicleData = vehicleDataList.get(viewPager.getCurrentItem());
                loadFragment(HealthAlertFragment.getInstance(vehicleData.getModelName(), vehicleData.getRegistrationNo(),vehicleData.getChaissisNo()));
                break;
            case R.id.txtVehicleSettings:
                params = new Bundle();
                params.putString("eventCategory", "Connected Module");
                params.putString("eventAction", "Vehicle settings clicked");
                params.putString("modelName",txtBikeModel.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
                VehicleDataModel vehicleDataSetting = vehicleDataList.get(viewPager.getCurrentItem());
                loadFragment(VehicleSettingsFragment.getInstance(this,
                        vehicleDataSetting.getModelName(),vehicleDataSetting.getRegistrationNo(), deviceSetting));
                break;
            case R.id.info_fuel:
                 params = new Bundle();
                params.putString("eventCategory", "Connected Module");
                params.putString("eventAction", "Fuel info clicked");
                params.putString("modelName",txtBikeModel.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
                REUtils.showPopupDialog(getActivity(), getResources().getString(R.string.str_info_fuel), new REUtils.OnDialogButtonClickListener() {
                    @Override
                    public void onOkCLick() {
                        Bundle   params = new Bundle();
                        params.putString("eventCategory", "Connected Module");
                        params.putString("eventAction", "Close fuel info alert");
                        params.putString("modelName",txtBikeModel.getText().toString());
                        REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
                    }
                });
                break;
            case R.id.info_bat:
                params = new Bundle();
                params.putString("eventCategory", "Connected Module");
                params.putString("eventAction", "Battery info clicked");
                params.putString("modelName",txtBikeModel.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
                REUtils.showPopupDialog(getActivity(), getResources().getString(R.string.str_info_bat), new REUtils.OnDialogButtonClickListener() {
                    @Override
                    public void onOkCLick() {
                        Bundle   params = new Bundle();
                        params.putString("eventCategory", "Connected Module");
                        params.putString("eventAction", "Close battery info alert");
                        params.putString("modelName",txtBikeModel.getText().toString());
                        REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
                    }
                });
                break;
            default:
                break;
        }
    }

    public void getPairingKeyAndShowDialog() {
//		Intent i=new Intent(getActivity(), UploadKYCActivity.class);
//				i.putExtra("CHESIS",vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo());
//		startActivityForResult(i,1001);
		txtAuthorize.setVisibility(View.GONE);
		showLoading();
		motorcyclePresenter.getPairingKey(vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo(),
				REApplication.getInstance()
						.getUserTokenDetails(),REApplication.getInstance().getUserLoginDetails().getData().getUser().getCallingCode(),REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone(),REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()
				//REPreference.getInstance().getString(REApplication.getAppContext(), JWT_TOKEN_KEY)
		);
	}

   private void updateListItem() {
        vehicleDataList.get(viewPager.getCurrentItem()).setIsConnected(true);
        motorCyclePageAdapter.notifyDataSetChanged();
        motorcyclePresenter.fetchOBDDataPoints(deviceId);
    }

    private void setConnectedUIChanges(boolean value) {
        llLocate.setVisibility(value ? View.VISIBLE : View.GONE);
        llFuelBattery.setVisibility(value ? View.VISIBLE : View.GONE);
        ignitiontxt.setVisibility(value ? View.VISIBLE : View.GONE);
		imgWing.setVisibility(value?View.GONE:View.GONE);
        llVehicleDetails.setVisibility(value ? View.VISIBLE : View.GONE);
        //llConnectStatus.setVisibility(value ? View.VISIBLE : View.GONE);
        imgGsm.setVisibility(value ? View.VISIBLE : View.GONE);
        imgGps.setVisibility(value ? View.VISIBLE : View.GONE);
        llFuelBattery.animate().alpha(value ? 1.0f : 0.0f).setDuration(500);
        //llConnectStatus.animate().alpha(value ? 1.0f : 0.0f).setDuration(1000);

        imgLocate.setOnClickListener(this);
        if (value) {
            //setBatteryStatus(false);
            //setFuelStatus(false);
            //setMeterCount(REApplication.CODE_STUB_DEMO ? "123456" : "000000");
            // disableOBDPoints();
            //meterReading=(REApplication.CODE_STUB_DEMO ? 123456 : 000000);
            //setDigitText();
            //setConnectedStatus(true);
        }
    }


    private void setBatteryStatus(boolean batteryStatus) {
      //  txtBatteryStatus.setText(batteryStatus ? R.string.text_good : R.string.text_low);
       int colorResource = batteryStatus ? R.color.colorIndicatorRed : R.color.colorIndicatorGreen;
        imgBattery.setColorFilter(getActivity().getResources().getColor(colorResource), PorterDuff.Mode.SRC_IN);
    }

    private void setFuelStatus(boolean fuelStatus) {
        int colorResource = fuelStatus? ContextCompat.getColor(getActivity(),R.color.colorIndicatorRed):ContextCompat.getColor(getActivity(),R.color.colorIndicatorGreen);

        segmentedProgressBar.setFillColor(colorResource);
        txtFuelStatus.setText(fuelStatus ? R.string.text_good : R.string.text_low);
        imgFuel.setColorFilter(colorResource, PorterDuff.Mode.SRC_IN);
    }

    private void setMeterCount(String meterValue) {
        if (!meterValue.isEmpty() && meterValue.length() <= 6) {
            meterValue = String.format(Locale.getDefault(), "%06d", Integer.valueOf(meterValue));
        }
        if (!meterValue.isEmpty() && meterValue.length() > 6) {
            meterValue = "999999";
        }
        txtMeterBlock.setText(meterValue);
    }


    private void setConnectedStatus(boolean value) {
        txtIgnitionStatus.setText(value ? getString(R.string.text_connected) : getString(R.string.text_not_connected));
        int resDrawableId = value ? R.drawable.ic_ignition_on_dot : R.drawable.ic_ignition_off;
        imgIgnitionIcon.setImageDrawable(getActivity().getResources().getDrawable(resDrawableId));
        int resColorId = value ? R.color.color_light_green : R.color.color_light_red;
        txtIgnitionStatus.setTextColor(getActivity().getResources().getColor(resColorId));
    }

    private void showServiceHistory() {
        Intent intent = new Intent(getContext(), ServiceHistoryActivity.class);
        intent.putExtra("position", 0);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void showService(String chesis) {
        if (REApplication.getInstance().getUserLoginDetails() != null) {
            openServiceScreen(chesis);
        } else {
            chessisNumber=chesis;
            startActivityForResult(new Intent(getActivity(), UserOnboardingActivity.class), LoginActivity.CODE_SERVICING_ACTIVITY);
        }
    }

    private void openServiceScreen(String chessisNumber) {
        ConfigFeatures configFeature = REUtils.getConfigFeatures();
        if (configFeature != null && configFeature.getServiceBooking() != null &&
                configFeature.getServiceBooking().getFeatureStatus().
                        equalsIgnoreCase(REConstants.FEATURE_STATUS_DISABLED)) {
            REUtils.showErrorDialog(getActivity(), configFeature.getServiceBooking().getMessage());
        } else {
            Intent intentWonderLust = new Intent(getActivity(), WLWebViewActivity.class);
            intentWonderLust.putExtra(PCUtils.PC_REACT_FLAG, PCUtils.PC_OPEN_SERVICE);
            intentWonderLust.putExtra(PCUtils.PC_CHESSIS_NO, chessisNumber);
            startActivity(intentWonderLust);
           // startExplicitActivity(REServicingRootActivity.class);
        }
    }

    private void showNavigationMapScreen() {
            showGoogleMapActivity();
    }

    private void showGoogleMapActivity() {
        if (checkAppInstalled()) {
            /*Open google map for current location*/
            //startActivity(new Intent(getActivity(), NavigationMapsActivity.class));
            showMap();
        }
        /*else {
            navigateToPlayStore();
        }*/
    }

    private void showMap() {
        //startActivity(new Intent(this, NavigationMapsActivity.class));
        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void navigateToPlayStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(REConstants.GOOGLE_MAP_PLAY_STORE_PATH));
        startActivity(intent);
    }

    private boolean checkAppInstalled() {
        return REUtils.isAppInstalled(REConstants.GOOGLE_MAP_PACKAGENAME);
    }

    private boolean isBikeSupportTBT() {
        /*check if Bike support TBT return true else false*/
        return false;
    }

    private void startExplicitActivity(Class activity) {
        startActivity(new Intent(getActivity(), activity));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LoginActivity.CODE_SERVICING_ACTIVITY) {
            openServiceScreen(chessisNumber);
        }
        else if(resultCode == RESULT_OK && requestCode == 1) {
            ProfileData profileData= REUserModelStore.getInstance().getProfileData();
            EmergencyData emergencyData=profileData.getContactDetails().getMobile().getEmergency();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getPairingKeyAndShowDialog();
                }
            },500);

         //   Toast.makeText(getActivity(),"comeback "+emergencyData.getNumber(),Toast.LENGTH_SHORT).show();
        }
		else if(resultCode==RESULT_OK&&requestCode==1001){
			        try {
            txtAuthorize.setVisibility(View.GONE);
            showLoading();
            motorcyclePresenter.getPairingKey(vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo(),
                    REApplication.getInstance()
                            .getUserTokenDetails(),REApplication.getInstance().getUserLoginDetails().getData().getUser().getCallingCode(),REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone(),REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()
                    //REPreference.getInstance().getString(REApplication.getAppContext(), JWT_TOKEN_KEY)
            );
            /*PreferenceException*/
        } catch (Exception e) {
          //  e.printStackTrace();
        }
		}
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
       // motorcyclePresenter.disconnectStompClient();
        selectedPage = position;
        for (ImageView imageView : ivArrayDotsPager) {
            imageView.setImageResource(R.drawable.page_indicator_unselected);
        }
        ivArrayDotsPager[position].setImageResource(R.drawable.page_indicator_selected);
        handler.removeCallbacks(runnable);
    //    showLoading();
        setViewPagerData(vehicleDataList.get(position));

        //motorcyclePresenter.unSubscribeDeviceId(deviceId);
        //motorcyclePresenter.fetchOBDDataPoints(deviceId);
        //sendEchoForData();
        //readDeviceSettingsFromJDBC(REUserModelStore.getInstance().getUserId(), deviceId);
    }

    private void readDeviceSettingsFromJDBC(String userId, String deviceId) {
            motorcyclePresenter.readSettings(userId, deviceId);
    }

    private void setViewPagerData(VehicleDataModel vehicleData) {
        // REUtils.getFirebaseReactUrl(getContext(), vehicleData.getModelCode(), null, txtBikeModel);
        if (vehicleData.getModelCode() != null) {
            if (vehicleData.getVehicleUpdateStatus() != null) {
                if (vehicleData.getVehicleUpdateStatus().equalsIgnoreCase("VERIFIED")) {
                    txtRegNo.setText(vehicleData.getRegistrationNo());
                    penTxtRegNo.setText("");
                } else {
                    String registrationNumber = "(Approval Awaited)";
                    txtRegNo.setText(vehicleData.getRegistrationNo());
                    if (vehicleData.getRegistrationNo() != null && vehicleData.getRegistrationNo().length() > 0)
                        penTxtRegNo.setText(registrationNumber);
                    else penTxtRegNo.setText("");

                }
            } else {
                txtRegNo.setText(vehicleData.getRegistrationNo());
                penTxtRegNo.setText("");
            }
            txtBikeModel .setText(vehicleData.getDisplayName() != null ? vehicleData.getDisplayName() : "");
            //setVehicleData(vehicleData);

        /*code for get model name and variant model from FireBase
        code commented cause its already fetch while getting vehicle list*/
            REUtils.getFirebaseReactUrl(getContext(), vehicleData.getModelCode(), null, null,true,
                    dataSnapshot -> {
                        setViewOfVehicle(vehicleData, dataSnapshot);
                    });
        }
        //setConnectedUIChanges(vehicleDataList.get(viewPager.getCurrentItem()).isVehicleAuthorized());
    }
    private void setViewOfVehicle(VehicleDataModel vehicleData, DocumentSnapshot dataSnapshot) {
        if(dataSnapshot.exists()) {
            String imagePath =  dataSnapshot.getString("imageUrl");
            String bikeName =  dataSnapshot.getString("displayName");
            String ownerManual =  dataSnapshot.getString("ownerManual");
            variantConnected = vehicleData.getIsConnected();
            RELog.i("TAG","BikeName: " + bikeName);
            RELog.i("TAG","ImagePath: " + imagePath);
            RELog.i("TAG","variantConnected: " + variantConnected);
            //variantConnected = true;R
            bikeName = bikeName == null ? vehicleData.getModelName() : bikeName;
            if (vehicleDataList.size() > 0) {
                vehicleDataList.get(viewPager.getCurrentItem()).setOwnerManual(ownerManual);
                vehicleDataList.get(viewPager.getCurrentItem()).setModelName(bikeName);
                vehicleDataList.get(viewPager.getCurrentItem()).setImageUrl(imagePath);
                vehicleDataList.get(viewPager.getCurrentItem()).setIsConnected(variantConnected);
                txtBikeModel.setText(bikeName);
            }
            motorCyclePageAdapter.notifyDataSetChanged();
            //showAuthorizeButton();

            if (variantConnected != null && variantConnected && REUtils.getFeatures().getDefaultFeatures() != null && REUtils.getFeatures().getDefaultFeatures().getConnectedBike().equalsIgnoreCase(FEATURE_ENABLED) && REApplication.getInstance().isConnectedEnabled) {
                fetchConnectedStatus();
                disableOBDPoints();

            } else {
                txtAuthorize.setVisibility(View.GONE);
                setConnectedUIChanges(false);
                hideLoading();
            }
        }
        txtBikeModel .setText(vehicleData.getDisplayName() != null ? vehicleData.getDisplayName() : vehicleData.getModelName());
        if(getActivity()!=null&& ( (REHomeActivity) getActivity()).mReAppIndexScreen.equals(REAppIndexScreen.OWNERS_MANUAL)){
            txtSupport.performClick();
        }
        //setConnectedUIChanges(vehicleDataList.get(viewPager.getCurrentItem()).isVehicleAuthorized());
    }

    private void showAuthorizeButton() {
        VehicleDataModel vehicleData = vehicleDataList.get(viewPager.getCurrentItem());
        txtAuthorize.setVisibility((vehicleData.getIsConnected() != null && vehicleData.getIsConnected() && !vehicleData.isVehicleAuthorized()) ? View.VISIBLE : View.GONE);


    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void loadFragment(Fragment fragment) {

        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragmentManager.beginTransaction();
        childFragTrans.add(R.id.ll_motorcycle_container, fragment);
        childFragTrans.addToBackStack(null);
        childFragTrans.commit();

    }

    @Override
    public void onDataSuccess(OBDResponseData payload) {
        List<OBDDataPoints> obdDataPoints = Objects.requireNonNull(payload.getData()).getObdDataPoints();

        getActivity().runOnUiThread(() -> {
            if (this.isVisible()) {
                if (obdDataPoints != null) {
                    updateUIWithOBDDataPoints(payload);
                    if(this.isVisible())
                    fetchDataAfterTenSec();
                }
            }
        });
        hideLoading();
    }



    /**
     * Handles service notification case
     */
    private void handleServiceNotification() {
        if (mIsServiceNotification) {
            mIsServiceNotification = false;
            showService(chessisNumber);
        }
        if (!jobCartStatus.isEmpty()){
            showService(chessisNumber);
            jobCartStatus ="";
        }
    }
public void refreshData(){
	vehicleDataList = REServiceSharedPreference.getVehicleData(REApplication.getAppContext());
	if (vehicleDataList != null) {
		vehicleDataList.clear();
		if (llPagerDots.getChildCount() > 0) {
			llPagerDots.removeAllViews();
			llPagerDots.invalidate();
		}
		Log.e("DATA_REFRESH_AGAIN",vehicleDataList.size()+"");
		setViewPager();
		gotoSpecificMotorcycleFromPushNotification(vehicleDataList);
	}
}

    private void gotoSpecificMotorcycleFromPushNotification(List<VehicleDataModel> listVehicleModel){
		if(listVehicleModel!=null&&!listVehicleModel.isEmpty()) {
			handleServiceNotification();
			int pos = -1;
			if (vehicleDataList == null || vehicleDataList.isEmpty())
				vehicleDataList = REServiceSharedPreference.getVehicleData(REApplication.getAppContext());

			if (!chessisNumber.isEmpty() && vehicleDataList != null && !vehicleDataList.isEmpty()) {
				for (int i = 0; i < vehicleDataList.size(); i++) {
					VehicleDataModel vehicleDataModel = vehicleDataList.get(i);
					if (chessisNumber.equalsIgnoreCase(vehicleDataModel.getChaissisNo())) {
						pos = i;
					}
				}
			}
			chessisNumber = "";
			if (pos != -1) {
				showLoading();
				viewPager.setCurrentItem(pos);
				if (getActivity() instanceof REHomeActivity) {
					if (((REHomeActivity) getActivity()).mReAppIndexScreen.equals(REAppIndexScreen.DRSA)) {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								imgRSA.performClick();
								((REHomeActivity) getActivity()).mReAppIndexScreen = REAppIndexScreen.OUR_WORLD;
							}
						}, 500);

					} else if (((REHomeActivity) getActivity()).mReAppIndexScreen.equals(REAppIndexScreen.SERVICE)) {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								imgService.performClick();
								((REHomeActivity) getActivity()).mReAppIndexScreen = REAppIndexScreen.OUR_WORLD;
							}
						}, 500);

					}

				}
			}


			hideLoading();
		}
    }


    private void updateUIWithOBDDataPoints(OBDResponseData obdDataPoints) {

        List<OBDDataPoints> listData=obdDataPoints.getData().getObdDataPoints();
        if (listData.size() > 0&&vehicleDataList!=null&&vehicleDataList.size()>0) {
            if (listData.get(0).getVehiclenumber()!=null&&listData.get(0).getVehiclenumber().equals(vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo())) {

                OBDDataPoints object = listData.get(0);
                if(object.getObdDistance()!=null) {
                    txtMeter.setText(String.format("%06d", (long) (Double.parseDouble(object.getObdDistance()))));
                }

                    setBatteryStatus(obdDataPoints.getData().getLowBattery());

                    setGpsandGsmStatus(object.getGpsStatus(),object.getTs());


                if(object.getObdDistance() != null)
                vehicleDataList.get(viewPager.getCurrentItem()).setOdoValue((long) (Double.parseDouble(object.getObdDistance())));
                vehicleDataList.get(viewPager.getCurrentItem()).setAlertCount(obdDataPoints.getData().getHealthAlertsCount());
				vehicleDataList.get(viewPager.getCurrentItem()).setLowBat(obdDataPoints.getData().getLowBattery());
				vehicleDataList.get(viewPager.getCurrentItem()).setLowFuel(obdDataPoints.getData().getLowFuel());

				vehicleDataList.get(viewPager.getCurrentItem()).setGpsStatus(object.getGpsStatus());
                vehicleDataList.get(viewPager.getCurrentItem()).setTimeStamp(object.getTs());
                vehicleDataList.get(viewPager.getCurrentItem()).setGsmStatus(object.getGsmStatus());
                vehicleDataList.get(viewPager.getCurrentItem()).setTripFstate(object.getTripfstate());
                vehicleDataList.get(viewPager.getCurrentItem()).setHaltStatus(obdDataPoints.getData().getHaltStatus());

                setAlertCount(obdDataPoints.getData().getHealthAlertsCount());
                setIgnitionStatus(obdDataPoints.getData().getHaltStatus());

                    double tankLvlInt=0.0;
                    String tankLevel = object.getTankLevel();
                    if(tankLevel!=null&&!tankLevel.equalsIgnoreCase("NA")) {
                         tankLvlInt=Double.parseDouble(tankLevel);
                        if (tankLvlInt > 7.0)
                            tankLvlInt = 1.0;
                        if (object.getTripfstate() == 1) {
                            tankLvlInt = 1.0;
                            object.setLowFuel(1);
                        }
                        segmentedProgressBar.setCompletedSegments((int) tankLvlInt);
                    }
                    else{
                        segmentedProgressBar.setCompletedSegments(0);
                    }
					setBatteryStatus(obdDataPoints.getData().getLowBattery());
                    setFuelStatus(obdDataPoints.getData().getLowFuel());
                    vehicleDataList.get(viewPager.getCurrentItem()).setFuelBar(String.valueOf(tankLvlInt));
                    vehicleDataList.get(viewPager.getCurrentItem()).setFuel(object.getLowFuel());



                if( vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo()!=null) {
                    Gson gson = new Gson();
                    String data= gson.toJson(vehicleDataList.get(viewPager.getCurrentItem()));
                    try {
                        REPreference.getInstance().putString(getActivity(), vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo(),data);
                    } catch (PreferenceException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
				if(listData.get(0).getVehiclenumber()!=null)
                fetchConnectedStatus();
            }
        }

            else {
            disableOBDPoints();
        }

    }

    private void setIgnitionStatus(Boolean haltStatus) {
        if(haltStatus!=null) {
            int colorResourceHaltStatus = haltStatus ? getResources().getColor(R.color.colorIndicatorRed) : getResources().getColor(R.color.colorIndicatorGreen);
            ignitiontxt.setTextColor(colorResourceHaltStatus);

            if (haltStatus)
                ignitiontxt.setText(getResources().getString(R.string.text_ignition_off));
            else {
                ignitiontxt.setText(getResources().getString(R.string.text_ignition_on));
            }
        }
        else
            ignitiontxt.setText("");
    }

    private void setGpsandGsmStatus(int gps, String gsm) {
        if(gps>=0) {
            int colorResource = gps == 0 ? R.color.colorIndicatorRed : R.color.colorIndicatorGreen;
            imgGps.setColorFilter(getActivity().getResources().getColor(colorResource), PorterDuff.Mode.SRC_IN);
        }
        else{
            int colorResource =R.color.colorIndicatorGrey ;
            imgGps.setColorFilter(getActivity().getResources().getColor(colorResource), PorterDuff.Mode.SRC_IN);

        }
        if(gsm!=null) {
            try {
           long   timestmaplong= Long.parseLong(gsm);
               RELog.e (System.currentTimeMillis()/1000-timestmaplong+"");
                int colorResourceGsm = (System.currentTimeMillis()/1000)-timestmaplong > Integer.parseInt(REUtils.getConnectedFeatureKeys().getGsmThreshold()) ? R.color.colorIndicatorRed : R.color.colorIndicatorGreen;
                imgGsm.setColorFilter(getActivity().getResources().getColor(colorResourceGsm), PorterDuff.Mode.SRC_IN);

            } catch (NumberFormatException e) {
                System.out.println("Input String cannot be parsed to Long.");
            }
             }
        else{
            int colorResource =R.color.colorIndicatorGrey ;
            imgGsm.setColorFilter(getActivity().getResources().getColor(colorResource), PorterDuff.Mode.SRC_IN);

        }
    }

    private void setAlertCount(int alertCount) {
        if(alertCount>0){
            txtVehicleAlertsCount.setVisibility(View.VISIBLE);
         //   txtVehicleAlertsCount.setText(alertCount+"");
        }
        else{
            txtVehicleAlertsCount.setVisibility(View.GONE);
        }
    }

    private void disableOBDPoints() {
        ignitiontxt.setText("");
       String chessis= vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo();
        try {
          String dta=  REPreference.getInstance().getString(getActivity(),chessis);
          Gson gson=new Gson();
            VehicleDataModel datapoints=gson.fromJson(dta,VehicleDataModel.class);
            if(datapoints!=null) {
                String tankLevel = datapoints.getFuelBar();
				double tankLvlInt=0.0;
                if(tankLevel!=null&&!tankLevel.equalsIgnoreCase("NA")){
                     tankLvlInt=Double.parseDouble(tankLevel);
                    if (tankLvlInt > 7.0)
                        tankLvlInt = 1;
					if (datapoints.getTripFstate() == 1) {
						tankLvlInt = 1.0;
						datapoints.setFuel(1);
					}
                }

                vehicleDataList.get(viewPager.getCurrentItem()).setBatVolt(datapoints.getBatVolt());
                vehicleDataList.get(viewPager.getCurrentItem()).setOdoValue(datapoints.getOdoValue());
                vehicleDataList.get(viewPager.getCurrentItem()).setFuel(datapoints.getFuel());
                vehicleDataList.get(viewPager.getCurrentItem()).setFuelBar(tankLvlInt+"");
                vehicleDataList.get(viewPager.getCurrentItem()).setAlertCount(datapoints.getAlertCount());
				vehicleDataList.get(viewPager.getCurrentItem()).setLowFuel(datapoints.isLowFuel());
				vehicleDataList.get(viewPager.getCurrentItem()).setLowBat(datapoints.isLowBat());
                vehicleDataList.get(viewPager.getCurrentItem()).setGpsStatus(datapoints.getGpsStatus());
                vehicleDataList.get(viewPager.getCurrentItem()).setTimeStamp(datapoints.getTimeStamp());
                vehicleDataList.get(viewPager.getCurrentItem()).setGsmStatus(datapoints.getGsmStatus());
                vehicleDataList.get(viewPager.getCurrentItem()).setTripFstate(datapoints.getTripFstate());
                vehicleDataList.get(viewPager.getCurrentItem()).setHaltStatus(datapoints.isHaltStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtMeter.setText(String.format("%06d",vehicleDataList.get(viewPager.getCurrentItem()).getOdoValue()));

        int colorResource = R.color.colorIndicatorGrey;
        Integer fuel=vehicleDataList.get(viewPager.getCurrentItem()).getFuel();
        String fuelBar=  vehicleDataList.get(viewPager.getCurrentItem()).getFuelBar();
        if(fuelBar!=null&&!fuelBar.equals("NA")){
            segmentedProgressBar.setCompletedSegments((int)Double.parseDouble(fuelBar));
        }
        else{
            segmentedProgressBar.setCompletedSegments(0);
        }
        if(fuel!=null)
            setFuelStatus(vehicleDataList.get(viewPager.getCurrentItem()).isLowFuel());
        else
            imgFuel.setColorFilter(getActivity().getResources().getColor(colorResource), PorterDuff.Mode.SRC_IN);

        if(vehicleDataList.get(viewPager.getCurrentItem()).isLowBat()!=null) {
            txtBatteryStatus.setText("");
            setBatteryStatus(vehicleDataList.get(viewPager.getCurrentItem()).isLowBat());
        }
        else{
            imgBattery.setColorFilter(getActivity().getResources().getColor(colorResource), PorterDuff.Mode.SRC_IN);
            txtBatteryStatus.setText( "");

        }
        setGpsandGsmStatus(vehicleDataList.get(viewPager.getCurrentItem()).getGpsStatus(),vehicleDataList.get(viewPager.getCurrentItem()).getTimeStamp());
         setAlertCount(vehicleDataList.get(viewPager.getCurrentItem()).getAlertCount());
        setIgnitionStatus(vehicleDataList.get(viewPager.getCurrentItem()).isHaltStatus());
    }

    private void fetchDataAfterTenSec() {
        handler.removeCallbacks(runnable);
		if(REUtils.getConnectedFeatureKeys().getObdRefreshInterval()!=null)
        handler.postDelayed(runnable, Integer.parseInt(REUtils.getConnectedFeatureKeys().getObdRefreshInterval())*1000);
    }

    private void initiateRunnable() {
        runnable = this::sendEchoForData;
    }   


    private void sendEchoForData() {
        RELog.d("TAG","10sec call for OBD");
        if (motorcyclePresenter.getStompClient().isConnected() && !deviceId.isEmpty())
            motorcyclePresenter.getDataInEveryTenSec(deviceId);
        else if (deviceId != null && !isLoaderShowing()) {
            showLoading();
        }
        motorcyclePresenter.fetchOBDDataPoints(deviceId);

    }

    @Override
    public void onDataFailure(@NotNull String errorMessage) {
        hideLoading();
        if (this.isVisible()) {
            REUtils.showToastMsg(getContext(), errorMessage);
        }
    }

    @Override
    public void updateDeviceData(@NotNull PairingMotorcycleResponseModel.GetDeviceData deviceData) {
        this.deviceData = deviceData;
        updateDeviceId(deviceData.deviceImei);
        showPairingDialog();
        hideLoading();
    }

    @Override
    public void updateSettingsData(@NotNull SettingResponseModel settings) {
        this.deviceSetting = settings;
    }

    @Override
    public void pairingDataFail(String msg) {
        txtAuthorize.setVisibility(View.VISIBLE);
        if (this.isVisible()) {
            REUtils.showErrorDialog(getContext(), msg!=null?msg:getString(R.string.something_went_wrong));
        }
        hideLoading();
    }

    private void showPairingDialog() {
        if(pairingDialog==null||!pairingDialog.isVisible()) {
            pairingDialog = new PairingMotorcycleDialog(this,
                    vehicleDataList.get(viewPager.getCurrentItem()), deviceData);
            pairingDialog.setCancelable(false);
            pairingDialog.show(getChildFragmentManager(), getString(R.string.pairing_motorcycles));
        }
       Toast.makeText(getActivity(),getResources().getString(R.string.pairing_key_success),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthorizeSuccess(String deviceIMei, boolean consentValue) {
        updateDeviceId(deviceIMei);
        isConsentTaken = consentValue;
        readDeviceSettingsFromJDBC(REUserModelStore.getInstance().getUserId(), deviceId);
        setAuthorizedVehicle(true);
        new Handler().postDelayed(() -> FirestoreManager.getInstance().getConnnectedDocument(vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo(), new OnFirestoreCallback() {
            @Override
            public void onFirestoreSuccess() {
new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        motorcyclePresenter.fetchOBDDataPoints(deviceId);
    }
},500);

            }

            @Override
            public void onFirestoreFailure(String message) {
                REApplication.getInstance().setAlertTimestamp(null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        motorcyclePresenter.fetchOBDDataPoints(deviceId);
                    }
                },500);

            }
        }),200);

    }

    public void setAuthorizedVehicle(boolean authorized) {
        if (vehicleDataList.size() > 0) {
            vehicleDataList.get(viewPager.getCurrentItem()).setVehicleAuthorized(authorized);
            setConnectedUIChanges(authorized);
            showAuthorizeButton();
        }
    }

    private void updateDeviceId(String deviceIMei) {
        if (!"null".equalsIgnoreCase(deviceIMei)) {

            this.deviceId = deviceIMei;


            updateAllDeviceId(this.deviceId);
        }
    }

    private void updateAllDeviceId(String deviceId) {
        REApplication.getInstance().getREConnectedAPI().updateDeviceId(deviceId);
        if (vehicleDataList.size() > 0) {
            vehicleDataList.get(viewPager.getCurrentItem()).setDeviceId(deviceId);
        }
    }

    @Override
    public void onAuthorizeFailure(String message) {
        setAuthorizedVehicle(false);
        hideLoading();
    }

    private void showConsentDialog(final Activity activity) {
        if (consentDialog != null) {
            consentDialog.dismiss();
            consentDialog = null;
        }
        consentDialog = new Dialog(activity, R.style.blurTheme);
        consentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        consentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        consentDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        consentDialog.getWindow().setGravity(Gravity.CENTER);
        consentDialog.setCancelable(false);
        consentDialog.setContentView(R.layout.dialog_consent_management);
        Switch switchToggleLocation = consentDialog.findViewById(R.id.switchToggleLocation);
        Switch switchToggleNotification = consentDialog.findViewById(R.id.switchToggleNotification);
        switchToggleLocation.setChecked(deviceSetting.getLocationAccessFlag());
        switchToggleNotification.setChecked(deviceSetting.getNotificationFlag());
        TextView mPrivacyPolicy = consentDialog.findViewById(R.id.privacypolicy_tv);
        CheckBox mCheckPrivacy = consentDialog.findViewById(R.id.privacypolicy_check);
        TextView confirm=consentDialog.findViewById(R.id.txtConfirm);
        mPrivacyPolicy.setMovementMethod(new ScrollingMovementMethod());
        customTextView(mPrivacyPolicy);
        mCheckPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     confirm.setEnabled(isChecked);
            }
        });
        confirm.setOnClickListener(v -> {
            //consentDialog.dismiss();
            deviceSetting.setLocationAccessFlag(switchToggleLocation.isChecked());
            deviceSetting.setNotificationFlag(switchToggleNotification.isChecked());
            motorcyclePresenter.updateSettings(
                    REUserModelStore.getInstance().getUserId(), deviceId,
                    deviceSetting.getLocationAccessFlag(), deviceSetting.getNotificationFlag());
        });
        consentDialog.show();
    }

    /**
     * @param view This handles the different clicks in a textview and fonts
     */
    private void customTextView(TextView view) {
        Typeface mTypefaceMontserratRegular = ResourcesCompat.getFont(getContext(),
                R.font.montserrat_regular);
        Typeface typeface_montserrat_semibold = ResourcesCompat.getFont(getContext(),
                R.font.montserrat_semibold);

        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                getContext().getString(R.string.tnc_consent));
        spanTxt.setSpan(new RECustomTyperFaceSpan(mTypefaceMontserratRegular), 0,
                getContext().getString(R.string.tnc_consent).length(), 0);

        spanTxt.append(" ");
        spanTxt.append(getContext().getString(R.string.eula));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(getContext(), REWebViewActivity.class);
                intent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, getResources().getString(R.string.term_of_condition));
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);


            }
        }, spanTxt.length() - getContext().getString(R.string.eula)
                .length(), spanTxt.length(), 0);
        spanTxt.setSpan(new RECustomTyperFaceSpan(typeface_montserrat_semibold), spanTxt.length()
                - getContext().getString(R.string.eula)
                .length(), spanTxt.length(), 0);

        spanTxt.append(" ");


          spanTxt.append(getContext().getString(R.string.and_the));

        spanTxt.setSpan(new RECustomTyperFaceSpan(mTypefaceMontserratRegular), spanTxt.length() -
                getContext().getString(R.string.and_the)
                        .length(), spanTxt.length(), 0);

        spanTxt.append(" ");
        spanTxt.append(getContext().getString(R.string.text_privacy_policies));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(getContext(), REWebViewActivity.class);
                intent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, getResources().getString(R.string.text_privacy_policies_camel));
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
            }
        }, spanTxt.length() - getContext().getString(R.string.text_privacy_policies)
                .length(), spanTxt.length(), 0);

        spanTxt.setSpan(new RECustomTyperFaceSpan(typeface_montserrat_semibold), spanTxt.length() -
                getContext().getString(R.string.text_privacy_policies)
                        .length(), spanTxt.length(), 0);
        spanTxt.append(". ");
                  spanTxt.append(getContext().getString(R.string.consent_post_tnc));

        spanTxt.setSpan(new RECustomTyperFaceSpan(mTypefaceMontserratRegular), spanTxt.length() -
                getContext().getString(R.string.consent_post_tnc)
                        .length(), spanTxt.length(), 0);

        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setTextColor(getContext().getResources().getColor(R.color.white));
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }



	private void customTextViewVehicleFailed(TextView view) {
		Typeface mTypefaceMontserratRegular = ResourcesCompat.getFont(getContext(),
				R.font.montserrat_regular);


		SpannableStringBuilder spanTxt = new SpannableStringBuilder(
				getContext().getString(R.string.vehile_failure));
		spanTxt.setSpan(new RECustomTyperFaceSpan(mTypefaceMontserratRegular), 0,
				getContext().getString(R.string.vehile_failure).length(), 0);

		spanTxt.append(" ");
		spanTxt.append(REConstants.EXTRA_EMAIL);
		spanTxt.setSpan(new ClickableSpan() {
			@Override
			public void onClick(@NonNull View widget) {
				sendEmail();

			}
		}, spanTxt.length() - REConstants.EXTRA_EMAIL
				.length(), spanTxt.length(), 0);
		spanTxt.setSpan(new RECustomTyperFaceSpan(mTypefaceMontserratRegular), spanTxt.length()
				- REConstants.EXTRA_EMAIL
				.length(), spanTxt.length(), 0);
		spanTxt.append(".");
		view.setMovementMethod(LinkMovementMethod.getInstance());
		view.setTextColor(getContext().getResources().getColor(R.color.white));
		view.setText(spanTxt, TextView.BufferType.SPANNABLE);
	}

	@Override
    public void consentSettingsUpdated(String msg) {
        if (consentDialog != null) {
            //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            VehicleDataModel currentVehicle = vehicleDataList.get(viewPager.getCurrentItem());
            FirestoreManager.createConnectedInfoToFirestore(currentVehicle.getChaissisNo(), currentVehicle.getDeviceId(), true, null);
            consentDialog.dismiss();
            consentDialog = null;
        }
    }

    @Override
    public void checkConsentOfDevice() {
        if (!isConsentTaken)
            showConsentDialog(getActivity());

    }

	private void sendEmail() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{REConstants.EXTRA_EMAIL});
//        i.putExtra(Intent.EXTRA_SUBJECT, REConstants.EXTRA_SUBJECT);
//        i.putExtra(Intent.EXTRA_TEXT, REConstants.EXTRA_TEXT);
		try {
			startActivity(Intent.createChooser(i, getString(R.string.text_send_mail)));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getActivity(), R.string.err_not_installed_email_app, Toast.LENGTH_SHORT).show();
		}
	}

    public void provisionUpdateStatus() {
        LoginResponse loginResponse = REApplication.getInstance().getUserLoginDetails();
        if (loginResponse != null) {
            String phone = REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone();
            String guid = REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId();

            if (!isLoaderShowing()) {
                showLoading();
            }
            motorcyclePresenter.provisionUpdateStatus(new ProvisionUpdateStatusRequestModel(vehicleDataList.get(viewPager.getCurrentItem()).getChaissisNo(),guid, phone, true));

        }

    }

    @Override
    public void onProvisionUpdateStatusSuccess(String msg) {
        if (pairingDialog != null)
            pairingDialog.dismiss();

        Bundle params = new Bundle();
        params.putString("eventCategory", "Connected Module");
        params.putString("eventAction", "Connect clicked");
        params.putString("eventLabel", "Success");
        params.putString("modelName",txtBikeModel.getText().toString());
        REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
        hideLoading();
		if (llPagerDots.getChildCount() > 0) {
			llPagerDots.removeAllViews();
			llPagerDots.invalidate();
		}
setViewPager();
        Intent mIntent = new Intent(getContext(), AccountCreationSuccessActivity.class);
        mIntent.putExtra(REConstants.SUCCESS_ACTIVITY, REConstants.AUTHORIZED_ACTIVITY);
        startActivity(mIntent);
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
    }

    @Override
    public void onProvisionUpdateStatusFailure(String msg) {
        hideLoading();
        Bundle params = new Bundle();
        params.putString("eventCategory", "Connected Module");
        params.putString("eventAction", "Connect Clicked");
        params.putString("eventLabel", "Fail");
        params.putString("errorMessage",msg);
        params.putString("modelName",txtBikeModel.getText().toString());
        REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
        if(msg==null)
            msg=getResources().getString(R.string.something_went_wrong);
        REUtils.showErrorDialog(getActivity(), msg);
    }

}