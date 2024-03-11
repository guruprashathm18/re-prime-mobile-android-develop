package com.royalenfield.reprime.ui.home.service.servicebookingstatus.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.fireStore.OnPaymentCallback;
import com.royalenfield.firebase.realTimeDatabase.FirebaseManager;
import com.royalenfield.firebase.realTimeDatabase.OnFirebaseDealerResponseCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.models.response.proxy.servicebooking.ServiceBookingResponse;
import com.royalenfield.reprime.models.response.proxy.vehicleserviceinprogresslist.VehicleServiceProgressListResponse;
import com.royalenfield.reprime.ui.home.service.REServicingRootActivity;
import com.royalenfield.reprime.ui.home.service.asynctask.GetModelAsyncTask;
import com.royalenfield.reprime.ui.home.service.asynctask.ServiceAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.service.fragment.REServicingFragment;
import com.royalenfield.reprime.ui.home.service.rsa.listner.IRETelephoneManager;
import com.royalenfield.reprime.ui.home.service.serviceappointment.interactor.ServiceAppointmentInteractor;
import com.royalenfield.reprime.ui.home.service.serviceappointment.presenter.ServiceAppointmentPresenter;
import com.royalenfield.reprime.ui.home.service.serviceappointment.view.ServiceAppointmentView;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.adapter.ServiceJobCardStatusList;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.adapter.ServiceStatusAdapter;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor.ServiceProgressDetailsInteractor;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.presenter.ServiceProgressDetailsPresenter;
import com.royalenfield.reprime.ui.riderprofile.activity.REWebViewActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REConstants.SETTING_ACTIVITY_INPUT_TYPE;

/**
 * ServiceAppointmentStatusAndDetailsFragment is to show the Booking status & to cancel/payment
 */
public class ServiceAppointmentStatusAndDetailsFragment extends REBaseFragment implements
        View.OnClickListener, IRETelephoneManager, OnFirebaseDealerResponseCallback, ServiceAppointmentView,
        ServiceAsyncTaskListeners.GetModelFromRegNo, ServiceAppointmentDetailsAndStatusView,
        OnPaymentCallback {

    private static final String TAG = ServiceAppointmentStatusAndDetailsFragment.class.getSimpleName();
    private String[] mStatusList = {"Service request received", "Vehicle Inward", "Bike being serviced",
            "Service completed", "Proceed to payment", "Bike Ready for delivery"};
    private static final int SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS = 3;
    private List<ServiceJobCardStatusList> mServiceJobCardStatusListList;
    private RecyclerView mRecyclerViewServicestatus;
    private Button mButtonCancelBooking;
    private TextView mMotorcycle, mReschedule, mServiceCentername, mAmountTobePaid, mAmount,
            mMotorcyclePickupAddress, mMotorcyclePickupTime, mMotorcycleServiceType, mMotorcycleIssue;
    private String mJobCardStatus, mServiceCenterNo;
    private List<VehicleServiceProgressListResponse> mServiceProgressResponse;
    private ServiceAppointmentPresenter mServiceAppointmentPresenter;
    private String mRegistrationNumber = "";
    private List<ServiceBookingResponse> mServiceBookingResponse;
    private TextView mPaymentSuccessText;
    private String mPaymentStatus;
    private String mAmountPayable;
    private View mViewLine;

    public ServiceAppointmentStatusAndDetailsFragment() {
        // Required empty public constructor
    }

    public static ServiceAppointmentStatusAndDetailsFragment newInstance() {
        return new ServiceAppointmentStatusAndDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_serviceappointmentstatus, container, false);
        mServiceAppointmentPresenter = new ServiceAppointmentPresenter(this,
                new ServiceAppointmentInteractor());
        initViews(view);
        setPushNotificationUpdateListener();
        return view;
    }

    /**
     * Set the push notification update listener.
     */
    private void setPushNotificationUpdateListener() {
        ((REServicingRootActivity) Objects.requireNonNull(getActivity())).setPushNotificationUpdateListener((jobCartStatus, regNo) -> {
            Log.d(TAG, "UpdateJobCartStatus Changed");
            if (jobCartStatus != null && !jobCartStatus.equals("Delivered")) {
                mJobCardStatus = jobCartStatus;
                mRegistrationNumber = regNo;
                initializeRecyclerList(mJobCardStatus);
                serviceInvoicedUI();
            } else {
                openServicingFragment(false);
            }
        });
    }


    /**
     * Initialising views
     */
    private void initViews(View view) {
        NestedScrollView scrollView = view.findViewById(R.id.scroll_status);
        scrollView.fullScroll(View.FOCUS_UP);
        mViewLine = view.findViewById(R.id.line_view);
        mRecyclerViewServicestatus = view.findViewById(R.id.recyclerView_servicestatus);
        mRecyclerViewServicestatus.setNestedScrollingEnabled(false);
        mButtonCancelBooking = view.findViewById(R.id.button_cancelbooking);
        mButtonCancelBooking.setOnClickListener(this);
        mMotorcycle = view.findViewById(R.id.textView_motorcycle);
        mReschedule = view.findViewById(R.id.textview_reschedule);
        mReschedule.setOnClickListener(this);
        mServiceCentername = view.findViewById(R.id.textView_serviceCenter_name);
        ImageView mCallServicecenter = view.findViewById(R.id.imageView_call_servicecenter);
        mCallServicecenter.setOnClickListener(this);
        mMotorcyclePickupAddress = view.findViewById(R.id.textView_motorcycle_pickupaddress);
        mAmountTobePaid = view.findViewById(R.id.textView_amounttobepaid);
        mAmount = view.findViewById(R.id.textView_amount);
        mMotorcyclePickupTime = view.findViewById(R.id.textView_motorcycle_pickuptime);
        mMotorcycleServiceType = view.findViewById(R.id.textView_motorcycle_serviceType);
        mMotorcycleIssue = view.findViewById(R.id.textView_motorcycle_issue);
        mPaymentSuccessText = view.findViewById(R.id.text_paymemtsuccess);
        if (isBookingOrReschedule()) {
            String mBookingNo = mServiceBookingResponse.get(0).getBookingno();
            showLoading();
            mServiceAppointmentPresenter.getServiceStatus(mBookingNo);
        } else {
            bindData();
        }
    }

    /**
     * Checks the object whether there is any booking in this session
     *
     * @return : boolean
     */
    private boolean isBookingOrReschedule() {
        mServiceBookingResponse = REApplication.getInstance().getServiceBookingResponse();
        return mServiceBookingResponse != null && mServiceBookingResponse.size() > 0;
    }

    /**
     * Initializing ServiceStatus recyclerView with jobcardstatus
     *
     * @param jobcardStatus : JobcardStatuNets recevied from server
     */
    private void initializeRecyclerList(String jobcardStatus) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewServicestatus.setLayoutManager(mLayoutManager);
        mRecyclerViewServicestatus.setItemAnimator(new DefaultItemAnimator());
        if (jobcardStatus != null && !jobcardStatus.isEmpty()) {
            setServiceStatus(jobcardStatus);
            ServiceStatusAdapter adapter = new ServiceStatusAdapter(getActivity(), mServiceJobCardStatusListList);
            mRecyclerViewServicestatus.setAdapter(adapter);
        } else {
            mServiceJobCardStatusListList = new ArrayList<>();
            mServiceJobCardStatusListList.add(new ServiceJobCardStatusList(REConstants.SERVICE_REQUEST_RECEIVED, true));
            for (int i = 1; i < mStatusList.length; i++) {
                mServiceJobCardStatusListList.add(new ServiceJobCardStatusList(mStatusList[i], false));
            }
            mServiceJobCardStatusListList.get(0).setService_status(true);
            //Setting false to all the other status
            for (int i = 1; i < mStatusList.length; i++) {
                mServiceJobCardStatusListList.get(i).setService_status(false);
            }
            ServiceStatusAdapter adapter = new ServiceStatusAdapter(getContext(), mServiceJobCardStatusListList);
            mRecyclerViewServicestatus.setAdapter(adapter);
        }
    }


    /**
     * Fetches the dealer data from firebase and getting the particular dealer from list
     *
     * @param branchId : String
     */
    private void getLastVisitedServiceCenterFromFirebase(String branchId) {
        if (branchId != null && !branchId.isEmpty()) {
            showLoading();
            FirebaseManager.getInstance().fetchDealerResponseFromFirebase(branchId, this);
        }
    }

    /**
     * Getting dealer address using BranchId
     */
    private void getDealerAddress() {
        //Getting data from firebase database
        for (VehicleServiceProgressListResponse serviceAppointmentListResponse : mServiceProgressResponse) {
            getLastVisitedServiceCenterFromFirebase(serviceAppointmentListResponse.getDealerInfo());
        }
    }


    /**
     * Handles the cancellation of the booking or start the payment if job status invoiced.
     */
    private void cancelBookingOrProceedToPay() {
        if (mJobCardStatus != null && !mJobCardStatus.isEmpty() &&
                mJobCardStatus.equalsIgnoreCase(REConstants.READY_FOR_INVOICING)) {
            //Proceed to pay click comes here
            if (mAmountPayable != null && !mAmountPayable.isEmpty()) {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Motorcycle-Service status");
                params.putString("eventAction", "Proceed to Payment");
                params.putString("eventLabel", "make payment");
               REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                mServiceAppointmentPresenter.generatePaymentMessage(mAmountPayable,
                        mServiceProgressResponse.get(0).getAppointmentNumber(), "INR");
            } else {
                REUtils.showErrorDialog(getContext(), "Please try after some time !");
            }
        } else if (mJobCardStatus != null && mJobCardStatus.isEmpty()) {
            openCancelConfirmationScreen();
        } else if (mJobCardStatus == null) {
            openCancelConfirmationScreen();
        }
    }

    private void openCancelConfirmationScreen() {
        //Cancel booking comes here
        Bundle params=new Bundle();
        params.putString("eventCategory", "Motorcycles-Service status");
        params.putString("eventAction", "Service Request Received");
        params.putString("eventLabel", "Cancel booking clicked");
       REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
        Intent intent = new Intent(getContext(), ServiceCancelConfirmationActivity.class);
        startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.slide_up, 0);
    }

    /**
     * Opens Servicing fragment
     */
    private void openServicingFragment(boolean isReschedule) {
        if (getFragmentManager() != null) {
            FragmentTransaction mFragmentTransaction = getFragmentManager()
                    .beginTransaction();
            Fragment mServicingFragment = REServicingFragment.newInstance();
            Bundle args = new Bundle();
            args.putBoolean(REConstants.KEY_SERVICE_ISRESCHEDULE, isReschedule);
            mServicingFragment.setArguments(args);
            mFragmentTransaction.replace(R.id.root_frame, mServicingFragment);
            mFragmentTransaction.commit();
        }
    }

    /**
     * Changes in UI is done if service status is in invoice
     */
    private void serviceInvoicedUI() {
        if (mJobCardStatus != null && !mJobCardStatus.isEmpty()
                && !mJobCardStatus.equals(REConstants.READY_FOR_INVOICING) && !mJobCardStatus.equals(REConstants.INVOICED)) {
            getServiceEstimate();
            disableRescheduleAndCancel();
        } else if (mJobCardStatus != null && !mJobCardStatus.isEmpty() && mJobCardStatus.equals(REConstants.READY_FOR_INVOICING)) {
          //  Log.e("ServiceTest", "serviceInvoicedUI :" + mPaymentStatus);
            getServiceEstimate();
        } else if (mJobCardStatus != null && !mJobCardStatus.isEmpty() && mJobCardStatus.equals(REConstants.INVOICED)) {
            getServiceEstimate();
            disableRescheduleAndCancel();
        } else {
            mAmountTobePaid.setVisibility(View.GONE);
            mAmount.setVisibility(View.GONE);
            enableRescheduleAndCancel();
        }

    }

    private void getPaymentStatusFromFirestore() {
        if (mJobCardStatus != null && !mJobCardStatus.isEmpty() && mJobCardStatus.equals(REConstants.READY_FOR_INVOICING)) {
            FirestoreManager.getInstance().getPaymentStatus(this);
        }
    }

    /**
     * API call for getting service estimate
     */
    private void getServiceEstimate() {
        mServiceAppointmentPresenter.getServiceEstimate(mRegistrationNumber);
        Log.d("API", "getServiceEstimate() API called");
    }

    /**
     * Disables the reschedule and cancel button
     */
    private void disableRescheduleAndCancel() {
        mButtonCancelBooking.setVisibility(View.GONE);
        disableReschedule();
    }

    private void disableReschedule() {
        mReschedule.setVisibility(View.INVISIBLE);
    }

    /**
     * Enables the reschedule and cancel button
     */
    private void enableRescheduleAndCancel() {
        mViewLine.setVisibility(View.VISIBLE);
        mButtonCancelBooking.setVisibility(View.VISIBLE);
        mButtonCancelBooking.setEnabled(true);
        mButtonCancelBooking.setBackground(getActivity().getDrawable(R.drawable.button_border_enable));
        mReschedule.setVisibility(View.VISIBLE);
        mReschedule.setEnabled(true);
        mReschedule.setTextColor(getActivity().getResources().getColor(R.color.white));
        mReschedule.setCompoundDrawablesWithIntrinsicBounds(null, null,
                getResources().getDrawable(R.drawable.ic_next_arrow), null);
    }

    /**
     * Enables the reschedule and cancel button
     */
    private void enablePayment() {
        mReschedule.setVisibility(View.GONE);
        mButtonCancelBooking.setVisibility(View.VISIBLE);
        mViewLine.setVisibility(View.GONE);
        mButtonCancelBooking.setEnabled(true);
        mButtonCancelBooking.setText(R.string.service_proceedtopay);
        if (getActivity() != null) {
            mButtonCancelBooking.setBackground(getActivity().getDrawable(R.drawable.button_border_enable));
        }
    }

    /**
     * loading appointment dafta by setting text
     */
    private void loadAppointmentBookingData() {
        for (VehicleServiceProgressListResponse serviceAppointmentListResponse : mServiceProgressResponse) {
            String customerRemarks = serviceAppointmentListResponse.getCustomerRemarks();
            String pickupAddress = serviceAppointmentListResponse.getPickupAddres();
            String modelName = serviceAppointmentListResponse.getModelName();
            String serviceType = serviceAppointmentListResponse.getServiceBookingType();
            String appointmentDate;
            if (isBookingOrReschedule()) {
                GetModelAsyncTask getModelAsyncTask = new GetModelAsyncTask(mRegistrationNumber, this);
                getModelAsyncTask.execute();
                if (serviceType.equalsIgnoreCase(REConstants.SERVICE_BOOKING_TYPE_DOORSTEP))
                    appointmentDate = REUtils.getOrdinalDateObject(getContext(),
                            serviceAppointmentListResponse.getAppointmentDate(), false);
                else
                appointmentDate = REUtils.getOrdinalDateObject(getContext(),
                        serviceAppointmentListResponse.getAppointmentDate(), true);
            } else {
                if (modelName == null || modelName.isEmpty()) {
                    setModelName();
                    if (serviceType.equalsIgnoreCase(REConstants.SERVICE_BOOKING_TYPE_DOORSTEP))
                        appointmentDate = REUtils.getOrdinalDateObject(getContext(),
                                serviceAppointmentListResponse.getAppointmentDate(), false);
                    else
                    appointmentDate = REUtils.getOrdinalDateObject(getContext(),
                            serviceAppointmentListResponse.getAppointmentDate(), true);
                } else {
//                    mMotorcycle.setText(modelName != null && !modelName.isEmpty() ? modelName :
//                            getResources().getString(R.string.text_hyphen_na));
                    setModelName();
                    appointmentDate = serviceAppointmentListResponse.getAppointmentDate();
                }
            }
            mMotorcycleIssue.setText(customerRemarks != null && !customerRemarks.isEmpty() ? customerRemarks :
                    getResources().getString(R.string.text_hyphen_na));
            mMotorcyclePickupAddress.setText(pickupAddress != null && !pickupAddress.isEmpty() ? pickupAddress :
                    getResources().getString(R.string.text_hyphen_na));
            mMotorcyclePickupTime.setText(appointmentDate != null && !appointmentDate.isEmpty() ? appointmentDate :
                    getResources().getString(R.string.text_hyphen_na));
            String serviceTypeTag;
            if (serviceType.equalsIgnoreCase(REConstants.SERVICE_BOOKING_TYPE_SELFDROP))
                serviceTypeTag = getResources().getString(R.string.text_label_bike_self_drop);
                else if (serviceType.equalsIgnoreCase(REConstants.SERVICE_BOOKING_TYPE_PICKUPANDDROP))
                serviceTypeTag = getResources().getString(R.string.text_label_bike_pickup);
                else
                serviceTypeTag = getResources().getString(R.string.text_label_bike_doorstep);
            mMotorcycleServiceType.setText(serviceTypeTag != null && !serviceTypeTag.isEmpty() ? serviceTypeTag :
                    getResources().getString(R.string.text_hyphen_na));
            mJobCardStatus = (String) serviceAppointmentListResponse.getJobcardStatus();
        }
    }

    private void setModelName() {
        GetModelAsyncTask getModelAsyncTask = new GetModelAsyncTask(mRegistrationNumber, this);
        getModelAsyncTask.execute();
    }

    /**
     * Sets dealer address and mobile number
     */
    private void setDealerData(DealerMasterResponse dealersResponse) {
        if (getActivity() != null && getContext() != null) {
            String dealerAddress = dealersResponse.getAddress();
            mServiceCentername.setText(String.format(getResources().getString(R.string.text_dealer_name_address),
                    dealersResponse.getDealerName(), ",", dealerAddress != null && !dealerAddress.isEmpty() ?
                            dealerAddress : ""));
            if (dealersResponse.getPhone() != null) {
                mServiceCenterNo = dealersResponse.getPhone();
            }
        }
    }

    /**
     * Opens booking screen
     */
    private void openServiceFragment() {
        Bundle params=new Bundle();
        params.putString("eventCategory", "Motorcycles-Service status");
        params.putString("eventAction", "Service Request Received");
        params.putString("eventLabel", "Reschedule clicked");
       REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
        //Checking if there is REServicing Fragment or not if it is there we are setting result
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_OK,
                    new Intent().putExtra(REConstants.KEY_SERVICE_ISRESCHEDULE, true)
            );
        } else {
            openServicingFragment(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancelbooking:
                cancelBookingOrProceedToPay();
                break;
            case R.id.textview_reschedule:
                openServiceFragment();
                break;
            case R.id.imageView_call_servicecenter:
                Bundle params=new Bundle();
                params.putString("eventCategory", "Motorcycles-Service status");
                if(mServiceJobCardStatusListList!=null)
                for(ServiceJobCardStatusList list:mServiceJobCardStatusListList){
                    if(list.getService_status()){
                        params.putString("eventAction",list.getService_name());


                    }
                    else {
                        break;
                    }
                }

                params.putString("eventLabel", "Call Click");

               REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                if (mServiceCenterNo != null && !mServiceCenterNo.equals("")) {
                    if (getActivity() != null) {
                        ((REBaseActivity) getActivity()).checkAndRequestCallPermissions(getContext(),
                                getActivity(), mServiceCenterNo,
                                SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS, this);
                    }
                } else {
                    REUtils.showErrorDialog(getContext(), "Contact number unavailable");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REConstants.REQUESTCODE_BOOKINGCANCELLED && data != null) {
            openServicingFragment(false);
        } else if (requestCode == REConstants.REQUESTCODE_PAYMENTSTATUS && data != null) {
            mPaymentStatus = data.getStringExtra(REConstants.PAYMENT_STATUS_MESSAGE);
            paymentStatusMessage();
        }
    }

    /**
     * API call for fetching the getVehicleServiceProgressList data
     */
    private void getVehicleServiceProgressList() {
        ServiceProgressDetailsPresenter mServiceProgressDetailsPresenter = new ServiceProgressDetailsPresenter(
                this, new ServiceProgressDetailsInteractor());
        String mobile = REUserModelStore.getInstance().getPhoneNo();
        if (mobile != null && !mobile.isEmpty()) {
            showLoading();
            mServiceProgressDetailsPresenter.getVehicleServiceInProgressList(REUtils.trimCountryCodeFromMobile(mobile));
        }
    }

    private void bindData() {
        try {
            mServiceProgressResponse = REApplication.getInstance().getServiceProgressListResponse();
            if (mServiceProgressResponse != null && mServiceProgressResponse.size() > 0) {
                mRegistrationNumber = REApplication.getInstance().getServiceProgressListResponse().get(0).getRegistrationNumber();
            }
            loadAppointmentBookingData();
            initializeRecyclerList(mJobCardStatus);
            //Initialising jobcardStatus view
            serviceInvoicedUI();
            // Getting dealer address using BranchId
            getDealerAddress();
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void paymentStatusMessage() {
        if (getActivity() != null) {
            if (mPaymentStatus != null && mPaymentStatus.equals("Successful Transaction")) {
                mPaymentSuccessText.setVisibility(View.VISIBLE);
                mPaymentSuccessText.setText(getResources().getString(R.string.text_payment_success));
                disableRescheduleAndCancel();
            } else {
                if (mPaymentStatus != null && !mPaymentStatus.isEmpty()) {
                    mPaymentSuccessText.setVisibility(View.VISIBLE);
                    mPaymentSuccessText.setText(getResources().getString(R.string.text_payment_failure));
                }
                enablePayment();
            }
        }
    }

    /**
     * Conditions for service status for displaying in ServiceStatusAdapter
     *
     * @param jobcardStatus : JobcardStatus recevied from server
     */
    private void setServiceStatus(String jobcardStatus) {
        mServiceJobCardStatusListList = new ArrayList<>();

        for (String s : mStatusList) {
            mServiceJobCardStatusListList.add(new ServiceJobCardStatusList(s, false));
        }
        if (jobcardStatus.equalsIgnoreCase(REConstants.PRE_JOBCARD)) {
            setTrueStatus(2);
        } else if (jobcardStatus.equalsIgnoreCase(REConstants.JC_OPENING) || jobcardStatus.equalsIgnoreCase(REConstants.JC_ESTIMATE)
                || jobcardStatus.equalsIgnoreCase(REConstants.IN_TRAY_WORKSHOP) || jobcardStatus.equalsIgnoreCase(REConstants.FQI)) {
            setTrueStatus(3);
        } else if (jobcardStatus.equalsIgnoreCase(REConstants.READY_FOR_DELIVERY)) {
            setTrueStatus(4);
        } else if (jobcardStatus.equalsIgnoreCase(REConstants.READY_FOR_INVOICING)) {
            setTrueStatus(5);
        } else if (jobcardStatus.equalsIgnoreCase(REConstants.INVOICED)) {
            setTrueStatus(6);
        }
    }

    /**
     * Sets the status to true based on the JobCard status
     *
     * @param size : int
     */
    private void setTrueStatus(int size) {
        for (int i = 0; i < size; i++) {
            mServiceJobCardStatusListList.get(i).setService_status(true);
        }
    }



    @Override
    public void simError(String message) {
        REUtils.showErrorDialog(getContext(), message);
    }

    @Override
    public void onFirebaseDealersListSuccess(List<DealerMasterResponse> dealersResponseArrayList) {

    }

    @Override
    public void onFirebaseDealerListFailure(String message) {

    }

    @Override
    public void onFirebaseDealerDetailSuccess(DealerMasterResponse dealersDetailResponse) {
        setDealerData(dealersDetailResponse);
        hideLoading();
    }

    @Override
    public void onFirebaseDealerDetailFailure(String message) {
        hideLoading();
    }

    @Override
    public void onServiceEstimateSuccess(String totalEstimateAmount) {
        Log.d("API", "getServiceEstimate() API success");
        if (totalEstimateAmount != null && !totalEstimateAmount.equals("0.00")) {
            getPaymentStatusFromFirestore();
        } else if (totalEstimateAmount != null && totalEstimateAmount.equals("0.00")) {
            //Disabling payment as this is free service & the amount is zero
            disableRescheduleAndCancel();
        } else {
            getPaymentStatusFromFirestore();
        }
        mAmountPayable = totalEstimateAmount;
        hideLoading();
        mAmountTobePaid.setVisibility(View.VISIBLE);
        mAmount.setVisibility(View.VISIBLE);
        if (totalEstimateAmount != null && !totalEstimateAmount.equals("")) {
            mAmount.setText(String.format(getResources().getString(R.string.amount_rs),
                    getResources().getString(R.string.rupees),
                    totalEstimateAmount, getResources().getString(R.string.rupees_slash)));
        } else {
            mAmount.setText(getResources().getString(R.string.text_hyphen_na));
        }
//        handleInvoicedCase();
    }

    @Override
    public void onServiceEstimateFailure(String errorMessage) {
        Log.d("API", "getServiceEstimate() API failure");
        getPaymentStatusFromFirestore();
        mAmountTobePaid.setVisibility(View.VISIBLE);
        mAmount.setVisibility(View.VISIBLE);
        mAmount.setText(getResources().getString(R.string.text_hyphen_na));
        hideLoading();
//        handleInvoicedCase();
    }

    @Override
    public void onServiceStatusSuccess() {
        hideLoading();
        bindData();
    }

    @Override
    public void onServiceStatusFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialog(getContext(), REUtils.getErrorMessageFromCode(0));
    }

    @Override
    public void onGeneratePaymentSuccess(String message) {
        hideLoading();
        Intent intent = new Intent(getContext(), REWebViewActivity.class);
        intent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, REConstants.TYPE_PAYMENT);
        intent.putExtra(REConstants.PAYMENT_CHECKSUM, message);
        startActivity(intent);
    }

    @Override
    public void onGeneratePaymentFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialog(getContext(), REUtils.getErrorMessageFromCode(0));
    }

    @Override
    public void onGetModelComplete(String modelName) {
        mMotorcycle.setText(modelName != null && !modelName.isEmpty() ? modelName : getResources().getString(R.string.text_hyphen_na));
    }

    @Override
    public void onServiceInProgressuccess() {
        hideLoading();
        bindData();
    }

    @Override
    public void onServiceInProgressFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialog(getContext(), REUtils.getErrorMessageFromCode(0));
    }

    @Override
    public void onPaymentStatus(String message, String caseId) {
      //  Log.e("ServiceTest", "getPaymentStatus :" + message);
        if (message != null && !message.equals("") && caseId != null && !caseId.equals("")) {
            mServiceProgressResponse = REApplication.getInstance().getServiceProgressListResponse();
            if (mServiceProgressResponse != null && mServiceProgressResponse.size() > 0) {
                String mCaseId = mServiceProgressResponse.get(0).getAppointmentNumber();
                if (mCaseId != null && mCaseId.equals(caseId)) {
                    //Checking the caseID before updating
                    mPaymentStatus = message;
                    paymentStatusMessage();
                } else {
                    paymentStatusMessage();
                }
            } else {
                paymentStatusMessage();
            }
        } else {
            //Firestore event failure case
            paymentStatusMessage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Service Status Screen");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }
}
