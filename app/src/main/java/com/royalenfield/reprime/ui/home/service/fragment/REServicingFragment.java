package com.royalenfield.reprime.ui.home.service.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.royalenfield.firebase.realTimeDatabase.FirebaseManager;
import com.royalenfield.firebase.realTimeDatabase.OnFirebaseDealerResponseCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.request.proxy.service.ServiceCenterRequest;
import com.royalenfield.reprime.models.request.proxy.service.servicebooking.ServiceBookingRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.models.response.proxy.servicebooking.ServiceBookingResponse;
import com.royalenfield.reprime.models.response.proxy.serviceslot.PickupandDoorstepServiceSlot;
import com.royalenfield.reprime.models.response.proxy.serviceslot.ServiceSlotListResponse;
import com.royalenfield.reprime.models.response.proxy.vehicleserviceinprogresslist.VehicleServiceProgressListResponse;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.custom.views.ServiceCenterDataModel;
import com.royalenfield.reprime.ui.home.service.GetDetailsForServiceBooking;
import com.royalenfield.reprime.ui.home.service.adapter.ServiceCenterDataViewAdapter;
import com.royalenfield.reprime.ui.home.service.adapter.TimeSlotAdapter;
import com.royalenfield.reprime.ui.home.service.asynctask.GetModelAsyncTask;
import com.royalenfield.reprime.ui.home.service.asynctask.ServiceAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.service.diy.activity.DoItYourSelfActivity;
import com.royalenfield.reprime.ui.home.service.history.interactor.ServiceBookingInteractor;
import com.royalenfield.reprime.ui.home.service.history.listener.ServiceBookingListener;
import com.royalenfield.reprime.ui.home.service.history.presenter.ServiceBookingPresenter;
import com.royalenfield.reprime.ui.home.service.history.views.ServiceHistoryActivity;
import com.royalenfield.reprime.ui.home.service.listener.IServiceStatusListener;
import com.royalenfield.reprime.ui.home.service.listener.OnServiceRescheduleResponseListener;
import com.royalenfield.reprime.ui.home.service.listener.RecyclerViewClickListener;
import com.royalenfield.reprime.ui.home.service.rsa.listner.IRETelephoneManager;
import com.royalenfield.reprime.ui.home.service.search.CalendarViewActivity;
import com.royalenfield.reprime.ui.home.service.search.adapter.DistanceCalculationHelper;
import com.royalenfield.reprime.ui.home.service.search.view.SearchServiceCentreActivity;
import com.royalenfield.reprime.ui.home.service.servicebookingslots.view.TimeSlotViewActivity;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor.ServiceAppointmentRescheduleInteractor;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor.ServiceProgressDetailsInteractor;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.presenter.ServiceProgressDetailsPresenter;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.views.ServiceAppointmentDetailsAndStatusView;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.views.ServiceAppointmentStatusAndDetailsFragment;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.home.service.specificissue.AnySpecificIssueActivity;
import com.royalenfield.reprime.ui.onboarding.address.activity.AddLocationActivity;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.splash.activity.RESuccessSplashActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.application.REApplication.getAppContext;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

/**
 * REServicing Fragment
 * This fragment allows the user to book/reschedule Service
 */
public class REServicingFragment extends REBaseFragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, IServiceStatusListener, ServiceBookingListener, IRETelephoneManager,
        RecyclerViewClickListener, OnServiceRescheduleResponseListener, OnFirebaseDealerResponseCallback,
        ServiceAppointmentDetailsAndStatusView, ServiceAsyncTaskListeners.DealerDistanceCalc,
        ServiceAsyncTaskListeners.GetModelFromRegNo {

    private static final String TAG = REServicingFragment.class.getSimpleName();

    //Request codes for the onActivityResult
    private static final int REQUEST_CODE_ADDRESS = 1;
    private static final int REQUEST_CODE_CALENDAR_DATE_SLOT = 2;
    private static final int REQUEST_CODE_SEARCH_SELECTED_SERVICE_CENTER = 3;
    private static final int REQUEST_CODE_SPECIFIC_ISSUE = 4;
    private static final int REQUEST_CODE_MODIFY_ADDRESS = 5;
    private static final int REQUEST_CODE_SERVICE_RESCHEDULE = 6;
    private static final int REQUEST_SHOW_MORE_TIMESLOT = 7;

    private RecyclerView recyclerViewServiceCenter, recyclerView_timeslots;
    private ServiceCenterDataViewAdapter mServiceCenterAdapter;
    private ArrayList<ServiceCenterDataModel> mServiceCenterListViewItemList = new ArrayList<>();
    private String strSelectedDate, strAppointmentDate, mRegistrationNo, mChassisNo, mSlotId,
            mPickupTime, mEngineNumber, mAppointmentDate, mSelectedDealerBranchId;
    private String mCalendarSelectedDate = "";
    //Pickup address.
    private String mPickupAddress = "";
    private String mCustomerRemarks = "";
    private String add = "";
    private String flat = "";
    private String mServiceCenterNo, crmStatus;
    private static final int SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS = 3;

    //Views
    private Spinner mSpinner;
    private TextView tvDateTomorrow, tvDateDayAfter, viewServiceHistory, tvTimeLabel, tvSearchServiceCenter,
            tvSpecificIssue, tvAddIssueDesc, tvAddModifyAddress, tvServiceOptionLabel, mScheduleService,
            mLServiceCenter, mRevertToOriginalRequest,
            tvShowAllSlots, mSpinnerItemView;
    private View lineServiceBorder, mViewBorderline, mViewRescheduleLine;

    private LinearLayout layoutPickupAddress;
    private Button btn_book_service;
    private RadioGroup rgServiceOption;
    private TimeSlotAdapter timeSlotAdapter;
    private ImageView ivModifyAddressArrow;

    private ServiceBookingPresenter mServiceBookingPresenter;

    //Interactors.
    private ServiceAppointmentRescheduleInteractor mRescheduleBookingInteractor;

    //Data sets.
    private List<String> timeList = new ArrayList<>();
    private List<ServiceSlotListResponse> mServiceTimeSlotResponse;

    //Dealers response data
    private List<DealerMasterResponse> mDealersResponseList = new ArrayList<>();

    //Vehicles response data
//    private List<VehicleDetailsResponse> mVehicleDetailsList = new ArrayList<>();
    List<VehicleDataModel> mVehicleDetailsList = new ArrayList<>();
    //Selected dealer response.
    private DealerMasterResponse mSelectedDealerResponse;

    List<PickupandDoorstepServiceSlot> mpPickupandDoorstepServiceSlots;

    //Model Store
    private REUserModelStore mREUserModelStore;
    //Flag tells is self drop or pickup selected.
    private boolean isPickupSelected = false;
    private boolean isDoorStepSelected = false;
    //For setting the custom font.
    private Typeface mTypefaceBold, mTypefaceRegular;
    //Flag tells is the Service screen for reschedule or normal booking.
    private boolean mFromReschedule = false;
    private RadioButton mRadioButtonSelfDrop, mRadioButtonPickUp, mRadioButtonDoorStep;
    private Guideline guideLine;
    private ConstraintLayout mConstraintVehicleRegistered;
    private int mVehicleSpinnerPosition;
    private List<String> bikeList = new ArrayList<>();
    //Listener for the scrolling to top of the screen.
    private ScrollUpListener scrollUpListener;
    private ConstraintLayout mErrorConstraint;
    private String mModelName, truncatedModelName, registrationNo;
    private List<VehicleServiceProgressListResponse> mServiceProgressResponse;
    private HashMap<String, String> mAddressHashMap = new HashMap<>();
    private FragmentActivity mContext;


    public REServicingFragment() {
        // Required empty public constructor
    }

    public static REServicingFragment newInstance() {
        return new REServicingFragment();
    }

    /**
     * Broadcast Receiver for listening the Firestore Vehicle & ServiceHistory updates
     */
    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (REConstants.FIRESTORE_UPDATE.equals(intent.getAction())) {
                // Recreating fragment
                if (getFragmentManager() != null) {
                    FragmentTransaction mFragmentTransaction = getFragmentManager()
                            .beginTransaction();
                    Fragment mServicingFragment = REServicingFragment.newInstance();
                    Bundle args = new Bundle();
                    args.putBoolean(REConstants.KEY_SERVICE_ISRESCHEDULE, false);
                    mServicingFragment.setArguments(args);
                    mFragmentTransaction.replace(R.id.root_frame, mServicingFragment);
                    mFragmentTransaction.commitAllowingStateLoss();
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            mFromReschedule = getArguments().getBoolean(REConstants.KEY_SERVICE_ISRESCHEDULE);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicing, container, false);
        mContext = getActivity();
        //Load the model store data ..
        mREUserModelStore = REUserModelStore.getInstance();
        //Interactor
        mRescheduleBookingInteractor = new ServiceAppointmentRescheduleInteractor();
        //Initialize presenter
        mServiceBookingPresenter = new ServiceBookingPresenter(this, new ServiceBookingInteractor());
        //Initialize views.
        initViews(view);
        getVehicleDetailsFromModelStore();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REConstants.FIRESTORE_UPDATE));
        return view;

    }

    /**
     * Fetches the list from REUserModelStore and sets the UI
     */
    private void getVehicleDetailsFromModelStore() {
//        mVehicleDetailsList = mREUserModelStore.getVehicleDetailsList();
        mVehicleDetailsList = REServiceSharedPreference.getVehicleData(getContext());
        getVehicleServiceProgressList();
    }

    @Override
    public void onResume() {
        super.onResume();
   /*     try {
            updatePickupAddressView();
        } catch (PreferenceException e) {
            RELog.e(e);
        }*/
    }

    @Override
    public void onDestroy() {
        // Clearing cache
        if (REApplication.getMemoryCache() != null) {
            REApplication.getMemoryCache().evictAll();
        }
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).
                unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onDestroy();
    }

    /**
     * Initializes all the View id's.
     */
    private void initViews(View view) {
        guideLine = view.findViewById(R.id.guideline12);
        mConstraintVehicleRegistered = view.findViewById(R.id.vehicle_registered);
        mErrorConstraint = view.findViewById(R.id.error_constraint);
        Button mRetryButton = view.findViewById(R.id.button_retry);
        mRetryButton.setOnClickListener(this);
        //To set font programatically.
        mTypefaceBold = ResourcesCompat.getFont(getContext(), R.font.montserrat_bold);
        mTypefaceRegular = ResourcesCompat.getFont(getContext(), R.font.montserrat_regular);

        mViewRescheduleLine = view.findViewById(R.id.view_line_reschedule);
        mRevertToOriginalRequest = view.findViewById(R.id.tv_revert_to_original_request);
        mRevertToOriginalRequest.setOnClickListener(this);
        mScheduleService = view.findViewById(R.id.textView_schedule_service);
        if (mFromReschedule) {
            mScheduleService.setText(getResources().getString(R.string.text_title_reschedule_service));
            REBaseActivity.isFromReschedule = mFromReschedule;
        } else
            REBaseActivity.isFromReschedule = false;
        mViewBorderline = view.findViewById(R.id.tv_view_borderline);

        mLServiceCenter = view.findViewById(R.id.textView_servicecenter);

        tvServiceOptionLabel = view.findViewById(R.id.tv_label_motorcycle_service_option);
        rgServiceOption = view.findViewById(R.id.rd_bike_service_option);
        lineServiceBorder = view.findViewById(R.id.tv_label_search_service_line);

        // Get reference of widgets from XML layout
        mSpinner = view.findViewById(R.id.spinner);
        tvTimeLabel = view.findViewById(R.id.tv_time_motorcycle);
        tvDateTomorrow = view.findViewById(R.id.tv_date_tomorrow);
        tvDateTomorrow.setTypeface(mTypefaceRegular);
        tvDateDayAfter = view.findViewById(R.id.tv_date_day_after);
        tvDateDayAfter.setTypeface(mTypefaceRegular);
        tvSpecificIssue = view.findViewById(R.id.tv_label_specific_issue);
        tvAddIssueDesc = view.findViewById(R.id.motorcycle_issue);
        viewServiceHistory = view.findViewById(R.id.tv_view_service_history);
        tvSearchServiceCenter = view.findViewById(R.id.tv_label_search_service_center);
        btn_book_service = view.findViewById(R.id.btn_book_service);
        recyclerViewServiceCenter = view.findViewById(R.id.recyclerView_service_center);
        ImageView imageViewCalendar = view.findViewById(R.id.ivCalendar);

        //show available slots
        tvShowAllSlots = view.findViewById(R.id.tv_time_slots_label);

        mRadioButtonSelfDrop = view.findViewById(R.id.rd_selfdrop);
        mRadioButtonSelfDrop.setOnClickListener(this);
        mRadioButtonPickUp = view.findViewById(R.id.rd_pickup);
        mRadioButtonPickUp.setEnabled(false);
        //mRadioButtonPickUp.setOnClickListener(this);
        mRadioButtonDoorStep = view.findViewById(R.id.rd_doorstep);
        mRadioButtonDoorStep.setEnabled(false);
        //mRadioButtonDoorStep.setOnClickListener(this);
        //pickup address view ids
        layoutPickupAddress = view.findViewById(R.id.layout_service_pickup_address);
        tvAddModifyAddress = view.findViewById(R.id.add_modify_address);
        tvAddModifyAddress.setOnClickListener(this);
        ivModifyAddressArrow = view.findViewById(R.id.iv_modify_address_next);
        ivModifyAddressArrow.setBackground(getResources().getDrawable(R.drawable.next_arrow_copy_5));
        ivModifyAddressArrow.setOnClickListener(this);

        recyclerView_timeslots = view.findViewById(R.id.recyclerView_time_slots);
        recyclerView_timeslots.setHasFixedSize(true);
        recyclerView_timeslots.setNestedScrollingEnabled(false);

        tvDateTomorrow.setOnClickListener(this);
        tvDateDayAfter.setOnClickListener(this);
        viewServiceHistory.setOnClickListener(this);
        tvSearchServiceCenter.setOnClickListener(this);
        tvSpecificIssue.setOnClickListener(this);
        imageViewCalendar.setOnClickListener(this);
        btn_book_service.setOnClickListener(this);
        tvShowAllSlots.setOnClickListener(this);

        recyclerView_timeslots.setVisibility(View.GONE);
        tvAddModifyAddress.setVisibility(View.GONE);
        layoutPickupAddress.setVisibility(View.GONE);
        ivModifyAddressArrow.setVisibility(View.GONE);

        tvTimeLabel.setVisibility(View.GONE);
        tvServiceOptionLabel.setVisibility(View.GONE);
        rgServiceOption.setVisibility(View.GONE);
        lineServiceBorder.setVisibility(View.GONE);
        tvAddIssueDesc.setVisibility(View.GONE);
        btn_book_service.setEnabled(false);

        bindDataToRecyclerView();
    }

    /**
     * Binds data to the recycler view.
     */
    private void bindDataToRecyclerView() {
        recyclerViewServiceCenter.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        ((SimpleItemAnimator) recyclerViewServiceCenter.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    /**
     * update UI when vehicle is changed
     */
    private void onMotorcycleChanged() {
        setTomorrowAndDayAfterDate();
        disableTomorrowAndDayAfterTomorrowDatePicker();
        mCalendarSelectedDate = "";
        tvTimeLabel.setVisibility(View.GONE);
        tvServiceOptionLabel.setVisibility(View.GONE);
        rgServiceOption.setVisibility(View.GONE);
        lineServiceBorder.setVisibility(View.GONE);

        //Add address layout visibility
        tvShowAllSlots.setVisibility(View.GONE);
        recyclerView_timeslots.setVisibility(View.GONE);
        tvAddModifyAddress.setVisibility(View.GONE);
        layoutPickupAddress.setVisibility(View.GONE);
        ivModifyAddressArrow.setVisibility(View.GONE);
        tvDateTomorrow.setVisibility(View.VISIBLE);
        tvDateDayAfter.setVisibility(View.VISIBLE);
        setGuidelinePercentage(View.VISIBLE);

        mViewBorderline.setVisibility(View.VISIBLE);
        //tvAddIssueDesc.setVisibility(View.GONE);
        //tvSpecificIssue.setVisibility(View.VISIBLE);
        //tvSpecificIssue.setText(getResources().getString(R.string.text_label_specific_issue));
    }

    /**
     * update UI when Radio Button is changed
     */
    private void onRadioButtonChanged() {
        setTomorrowAndDayAfterDate();
        disableTomorrowAndDayAfterTomorrowDatePicker();
        mCalendarSelectedDate = "";
        tvTimeLabel.setVisibility(View.GONE);

        //Add address layout visibility
        tvShowAllSlots.setVisibility(View.GONE);
        recyclerView_timeslots.setVisibility(View.GONE);

        tvDateTomorrow.setVisibility(View.VISIBLE);
        tvDateDayAfter.setVisibility(View.VISIBLE);
        setGuidelinePercentage(View.VISIBLE);
        mViewBorderline.setVisibility(View.VISIBLE);
        disableBookService();
    }

    /**
     * Disabling View service history text by changing color.
     */
    private void disableServiceHistory() {
        viewServiceHistory.setVisibility(View.VISIBLE);
        viewServiceHistory.setTextColor(REApplication.getAppContext().getResources().getColor(R.color.white_30));
        viewServiceHistory.setCompoundDrawablesWithIntrinsicBounds(null, null,
                getResources().getDrawable(R.drawable.next_arrow_grey), null);
        viewServiceHistory.setEnabled(false);
    }

    /**
     * Enabling view service history text by changing background color
     */
    private void enableServiceHistory() {
        viewServiceHistory.setVisibility(View.VISIBLE);
        viewServiceHistory.setTextColor(REApplication.getAppContext().getResources().getColor(R.color.white));
        viewServiceHistory.setEnabled(true);
        viewServiceHistory.setCompoundDrawablesWithIntrinsicBounds(null, null, REApplication.getAppContext().
                getResources().getDrawable(R.drawable.next_arrow_copy_5), null);
    }

    /**
     * Sets the tomorrow and day after tomorrow date
     */
    private void setTomorrowAndDayAfterDate() {
        String dateTomorrow = REUtils.getFormattedDate(getActivity(), REConstants.DATE_TOMORROW);
        String dateDayAfterTomorrrow = REUtils.getFormattedDate(getActivity(), REConstants.DATE_DAY_AFTER_TOMORROW);
        String nextdate_DayAfterTomorrrow = REUtils.getFormattedDate(getActivity(), REConstants.NEXT_DATE_DAY_AFTER_TOMORROW);

        if (isCutOfftime()) {
            tvDateTomorrow.setText(dateDayAfterTomorrrow);
            tvDateDayAfter.setText(nextdate_DayAfterTomorrrow);
        } else {
            tvDateTomorrow.setText(dateTomorrow);
            tvDateDayAfter.setText(dateDayAfterTomorrrow);
        }

        tvDateTomorrow.setVisibility(View.VISIBLE);
        tvDateDayAfter.setVisibility(View.VISIBLE);
        setGuidelinePercentage(View.VISIBLE);
    }

    /**
     * Disables the Today and Tomorrow both view unselected.
     */
    private void disableTomorrowAndDayAfterTomorrowDatePicker() {
        tvDateTomorrow.setSelected(false);
        tvDateTomorrow.setTextAppearance(getActivity(), R.style.CalendarDateStyle);

        tvDateDayAfter.setSelected(false);
        tvDateDayAfter.setTextAppearance(getActivity(), R.style.CalendarDateStyle);
    }

    /**
     * Makes Tomorrow Date view highlighted. and Tomorrow view disabled.
     */
    private void showTomorrowDateHighlighted() {
        tvDateTomorrow.setSelected(true);
        tvDateDayAfter.setSelected(false);

        tvDateTomorrow.setTextAppearance(getActivity(), R.style.CalendarDateStyle_Highlighted);
        tvDateDayAfter.setTextAppearance(getActivity(), R.style.CalendarDateStyle);
    }

    /**
     * Changes the style of date box and call API for getting time slots
     */
    private void selectTomorrowDate() {
        if ((mSelectedDealerResponse != null && mSelectedDealerResponse.getBranchCode().length() > 0) || mFromReschedule) {
            showTomorrowDateHighlighted();
            // calendarSelectedDate will be empty if there is no slot selected in calendar
            if (mCalendarSelectedDate.isEmpty()) {
                if (isCutOfftime())
                    strAppointmentDate = REUtils.getCurrentDate(REConstants.DATE_DAY_AFTER_TOMORROW, "yyyy-MM-dd");
                else
                    strAppointmentDate = REUtils.getCurrentDate(REConstants.DATE_TOMORROW, "yyyy-MM-dd");
            } else {
                // Getting date from calendar when slot is selected
                strAppointmentDate = mCalendarSelectedDate;
            }
            String selecteddate = REUtils.parseDateTo_ddMMMyyyy(strAppointmentDate, "yyyy-MM-dd");
            if (isDoorStepSelected) {
                //check date contains doorstep availability
                int postion_doorstep = -1;
                if (mpPickupandDoorstepServiceSlots != null) {
                    for (int i = 0; i < mpPickupandDoorstepServiceSlots.size(); i++) {
                        if (mpPickupandDoorstepServiceSlots.get(i).getAvailableDate().equals(selecteddate)) {
                            postion_doorstep = i;
                        }
                    }
                    if (postion_doorstep != -1 && mpPickupandDoorstepServiceSlots.get(postion_doorstep).getDoorStepAvailability()) {
                        enableBookService();
                        mAppointmentDate = strAppointmentDate + "T00:00:00";
                    } else {
                        REUtils.showErrorDialog(getContext(), getResources().getString(R.string.text_no_doorstepavailabiity));
                        disableBookService();
                    }
                } else {
                    REUtils.showErrorDialog(getContext(), getResources().getString(R.string.text_no_doorstepavailabiity));
                    disableBookService();
                }
            } else if (isPickupSelected) {
                int postion_pickup = -1;
                if (mpPickupandDoorstepServiceSlots != null) {
                    for (int i = 0; i < mpPickupandDoorstepServiceSlots.size(); i++) {
                        if (mpPickupandDoorstepServiceSlots.get(i).getAvailableDate().equals(selecteddate)) {
                            postion_pickup = i;
                        }
                    }
                    if (postion_pickup != -1 && mpPickupandDoorstepServiceSlots.get(postion_pickup).getPickupAvailability()) {
                        getServiceTimeSlots(strAppointmentDate);
                    } else {
                        REUtils.showErrorDialog(getContext(), getResources().getString(R.string.text_no_pickupavailabiity));
                        disableBookService();
                    }
                } else {
                    REUtils.showErrorDialog(getContext(), getResources().getString(R.string.text_no_pickupavailabiity));
                    disableBookService();
                }
            } else {
                getServiceTimeSlots(strAppointmentDate);
            }
        } else {
            REUtils.showErrorDialog(getContext(), "Please select a service centre!");
        }
    }

    /**
     * Makes DayAfterTomorrow Date view highlighted. and Today view disabled.
     */
    private void showDayAfterTomorrowDateHighlighted() {
        tvDateTomorrow.setSelected(false);
        tvDateDayAfter.setSelected(true);
        tvDateTomorrow.setTextAppearance(getActivity(), R.style.CalendarDateStyle);
        tvDateDayAfter.setTextAppearance(getActivity(), R.style.CalendarDateStyle_Highlighted);
    }

    /**
     * Changes the style of date box and call API for getting time slots
     */
    private void selectDayAfterDate() {
        if ((mSelectedDealerResponse != null && mSelectedDealerResponse.getBranchCode().length() > 0) || mFromReschedule) {
            showDayAfterTomorrowDateHighlighted();
            if (isCutOfftime())
                strAppointmentDate = REUtils.getCurrentDate(REConstants.NEXT_DATE_DAY_AFTER_TOMORROW, "yyyy-MM-dd");
            else
                strAppointmentDate = REUtils.getCurrentDate(REConstants.DATE_DAY_AFTER_TOMORROW, "yyyy-MM-dd");
            String selecteddate = REUtils.parseDateTo_ddMMMyyyy(strAppointmentDate, "yyyy-MM-dd");
            if (isDoorStepSelected) {
                //check date contains doorstep availability
                int postion_doorstep = -1;
                if (mpPickupandDoorstepServiceSlots != null) {
                    for (int i = 0; i < mpPickupandDoorstepServiceSlots.size(); i++) {
                        if (mpPickupandDoorstepServiceSlots.get(i).getAvailableDate().equals(selecteddate)) {
                            postion_doorstep = i;
                        }
                    }
                    if (postion_doorstep != -1 && mpPickupandDoorstepServiceSlots.get(postion_doorstep).getDoorStepAvailability()) {
                        enableBookService();
                        mAppointmentDate = strAppointmentDate + "T00:00:00";
                    } else {
                        REUtils.showErrorDialog(getContext(), getResources().getString(R.string.text_no_doorstepavailabiity));
                        disableBookService();
                    }
                } else {
                    REUtils.showErrorDialog(getContext(), getResources().getString(R.string.text_no_doorstepavailabiity));
                    disableBookService();
                }
            } else if (isPickupSelected) {
                int postion_pickup = -1;
                if (mpPickupandDoorstepServiceSlots != null) {
                    for (int i = 0; i < mpPickupandDoorstepServiceSlots.size(); i++) {
                        if (mpPickupandDoorstepServiceSlots.get(i).getAvailableDate().equals(selecteddate)) {
                            postion_pickup = i;
                        }
                    }
                    if (postion_pickup != -1 && mpPickupandDoorstepServiceSlots.get(postion_pickup).getPickupAvailability()) {
                        getServiceTimeSlots(strAppointmentDate);
                    } else {
                        REUtils.showErrorDialog(getContext(), getResources().getString(R.string.text_no_pickupavailabiity));
                        disableBookService();
                    }
                } else {
                    REUtils.showErrorDialog(getContext(), getResources().getString(R.string.text_no_pickupavailabiity));
                    disableBookService();
                }
            } else {
                getServiceTimeSlots(strAppointmentDate);
            }
        } else {
            REUtils.showErrorDialog(getContext(), "Please select a service centre!");
        }
    }

    /**
     * Handling calendar click
     */
    private void selectDayFromCalendar() {
        if (mSelectedDealerResponse != null || mFromReschedule) {
            Intent calendarIntent = new Intent(getContext(), CalendarViewActivity.class);
            if (mFromReschedule) {
                calendarIntent.putExtra("branchId", mSelectedDealerBranchId);
            } else {
                calendarIntent.putExtra("branchId", mSelectedDealerResponse.getBranchCode());
            }
            if (isCutOfftime())
                calendarIntent.putExtra("isCutOffTime", true);
            else
                calendarIntent.putExtra("isCutOffTime", false);
            if (isDoorStepSelected) {
                calendarIntent.putExtra("isDoorStepSelected", true);
            } else {
                calendarIntent.putExtra("isDoorStepSelected", false);
            }
            if (isPickupSelected) {
                calendarIntent.putExtra("isPickupSelected", true);
            } else {
                calendarIntent.putExtra("isPickupSelected", false);
            }
            REServicingFragment.this.startActivityForResult(calendarIntent, REQUEST_CODE_CALENDAR_DATE_SLOT);
            if (getActivity() != null)
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
        } else {
            REUtils.showErrorDialog(getContext(), "Please select a service centre!");
        }
    }

    /**
     * to set guideline percentage dyanamically
     *
     * @param i
     */
    private void setGuidelinePercentage(int i) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        if (i == View.GONE) {
            params.guidePercent = 0.45f;
            guideLine.setLayoutParams(params);
        } else {
            params.guidePercent = 0.861f;
            guideLine.setLayoutParams(params);
        }
    }

    /**
     * API-call to get the time slots for a selected date using the selected dealers branch ID.
     *
     * @param strSelectedDate selected date
     */
    private void getServiceTimeSlots(String strSelectedDate) {
        showLoading();
        if (!mFromReschedule) {
            mServiceBookingPresenter.getBookingSlots(strSelectedDate, mSelectedDealerResponse.getBranchCode());
        } else {
            mServiceBookingPresenter.getBookingSlots(strSelectedDate, mSelectedDealerBranchId);
        }
    }

    /**
     * Method to start TimeSlotViewActivity on click of showAvailableSlots.
     */
    private void showAllAvailableSlots() {
        Bundle params = new Bundle();
        if (mFromReschedule)
            params.putString("eventCategory", "Motorcycles-Reschedule");

        else
            params.putString("eventCategory", "Motorcycles-Schedule a service");

        params.putString("eventAction", getResources().getString(R.string.text_label_show_available_slots));
        REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
        Intent mIntent = new Intent(getContext(), TimeSlotViewActivity.class);
        mIntent.putExtra("selected_date", strSelectedDate);
        REServicingFragment.this.startActivityForResult(mIntent, REQUEST_SHOW_MORE_TIMESLOT);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
    }

    /**
     * Service pickup address handling.
     */
    private void handleAddModifyAddress() {
        if (mPickupAddress.isEmpty()) {
            //Open Add new address screen.
            // Intent addAddressIntent = AddAddressActivity.getIntent(getContext());
            // addAddressIntent.putExtra("IsEditProfileOrIsRidesAndService", true);
            Bundle paramDoor = new Bundle();
            paramDoor.putString("eventCategory", "Motorcycle Drop");
            if (isPickupSelected)
                paramDoor.putString("eventAction", mRadioButtonPickUp.getText().toString());
            else
                paramDoor.putString("eventAction", mRadioButtonDoorStep.getText().toString());
            paramDoor.putString("eventLabel", "Add Address click");
            REUtils.logGTMEvent(REConstants.KEY_SERVICE_GTM, paramDoor);
            Intent addAddressIntent = AddLocationActivity.getIntent(getContext());
            addAddressIntent.putExtra("isfromService", true);
            //addAddressIntent.putExtra("ADDRESS", mAddressHashMap);
            REServicingFragment.this.startActivityForResult(addAddressIntent, REQUEST_CODE_ADDRESS);
            if (getActivity() != null)
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
        } else {
           /* Intent modifyAddressIntent = ModifyAddressActivity.getIntent(getContext());
            modifyAddressIntent.putExtra("address", add);
            modifyAddressIntent.putExtra("flatNo", flat);
            modifyAddressIntent.putExtra("IsEditProfileOrIsRidesAndService", true);
            REServicingFragment.this.startActivityForResult(modifyAddressIntent, REQUEST_CODE_MODIFY_ADDRESS);*/

            Bundle paramDoor = new Bundle();
            paramDoor.putString("eventCategory", "Motorcycle Drop");
            if (isPickupSelected)
                paramDoor.putString("eventAction", mRadioButtonPickUp.getText().toString());
            else
                paramDoor.putString("eventAction", mRadioButtonDoorStep.getText().toString());
            paramDoor.putString("eventLabel", "Modify Address click");
            REUtils.logGTMEvent(REConstants.KEY_SERVICE_GTM, paramDoor);
            Intent addAddressIntent = AddLocationActivity.getIntent(getContext());
            addAddressIntent.putExtra("isfromService", true);
            addAddressIntent.putExtra("ADDRESS", mAddressHashMap);
            REServicingFragment.this.startActivityForResult(addAddressIntent, REQUEST_CODE_ADDRESS);
            if (getActivity() != null)
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
        }
    }

    /**
     * Enables the pickup address view. if user selects the pickup mode for the service booking.
     */
    private void enablePickupAddressView() {
        mPickupAddress = "";
        try {
            String address = REPreference.getInstance().getString(REApplication.getAppContext(), "address");
            if (address != null && !address.isEmpty() && !mFromReschedule)
                mPickupAddress = address;
        } catch (PreferenceException e) {
            RELog.e(e);
        }

        if (REApplication.getInstance().getServiceProgressListResponse().get(0).getPickupAddres() != null &&
                !REApplication.getInstance().getServiceProgressListResponse().get(0).getPickupAddres().isEmpty()) {
            mPickupAddress = REApplication.getInstance().getServiceProgressListResponse().get(0).getPickupAddres();
        }
        layoutPickupAddress.setVisibility(View.VISIBLE);
        tvAddModifyAddress.setVisibility(View.VISIBLE);
        if (!mPickupAddress.equals("") && isPickupSelected) {
            ivModifyAddressArrow.setVisibility(View.VISIBLE);
            //checkFlatAndAddress(flat, add);
            addAddress(mPickupAddress);
        } else if (!mPickupAddress.equals("") && isDoorStepSelected) {
            ivModifyAddressArrow.setVisibility(View.VISIBLE);
            addAddress(mPickupAddress);
        } else {
            ivModifyAddressArrow.setVisibility(View.GONE);
            addAddress("");
        }
    }

    /**
     * @param iTimeSelectedPosition Selected position from timeSlotRecycleView (initially iTimeSelectedPosition=-1 &&
     *                              onClick of showAllAvailable slots need to set iTimeSelectedPosition=-1;
     */
    private void getTimeSlotValues(int iTimeSelectedPosition) {
        timeList.clear();
        if (iTimeSelectedPosition != -1) {
            if (mServiceTimeSlotResponse != null && mServiceTimeSlotResponse.size() > 0) {
                String time = REUtils.getTimeSlots(mServiceTimeSlotResponse.get(iTimeSelectedPosition).getStartTimeSlot());
                timeList.add(time);
                iTimeSelectedPosition = 0;
            }
        } else {
            for (int i = 0; i < mServiceTimeSlotResponse.size(); i++) {
                if (mServiceTimeSlotResponse.get(i).getAvailability()) {
                    String time = REUtils.getTimeSlots(mServiceTimeSlotResponse.get(i).getStartTimeSlot());
                    timeList.add(time);
                }
            }
        }
        bindTimeSlotsToRecyclerView(timeList, iTimeSelectedPosition);
    }

    /**
     * Binds the time slots data list to the {@link RecyclerView}
     *
     * @param timeSlots time slot list.
     */
    private void bindTimeSlotsToRecyclerView(List<String> timeSlots, int iSelectedTimePos) {
        recyclerView_timeslots.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView_timeslots.setItemAnimator(new DefaultItemAnimator());
        tvTimeLabel.setVisibility(View.VISIBLE);
        timeSlotAdapter = new TimeSlotAdapter(getContext(), timeSlots, this);
        timeSlotAdapter.setSelectedTimeSlotEnabled(iSelectedTimePos);
        recyclerView_timeslots.setAdapter(timeSlotAdapter);
        recyclerView_timeslots.setVisibility(View.VISIBLE);
        tvShowAllSlots.setVisibility(View.VISIBLE);
        mViewBorderline.setVisibility(View.VISIBLE);
    }

    /**
     * @param iTimeSelectedPosition selected position from timeSlotRecycleView (initially iTimeSelectedPosition=-1 &&
     *                              onClick of showAllAvailable slots need to set iTimeSelectedPosition=-1;
     */

    private void setTimeSlotsToRecyclerView(int iTimeSelectedPosition) {
        mServiceTimeSlotResponse = REApplication.getInstance().
                getServiceSlotResponse();
        if (mServiceTimeSlotResponse != null && mServiceTimeSlotResponse.size() > 0) {
            getTimeSlotValues(iTimeSelectedPosition);
        } else {
            REUtils.showErrorDialog(getContext(), "Time slots are not available");
        }
    }

    /**
     * Fetches timeSlot information from CalendarViewActivity and TimeSlotViewActivity
     *
     * @param data
     */
    private void getSelectedTimeSlot(Intent data) {
        if (data != null) {
            if (data.getStringExtra("date") != null) {
                if (isDoorStepSelected) {
                    String strDate = data.getStringExtra("date");
                    mCalendarSelectedDate = data.getStringExtra("rawdate");
                    String date_sel = data.getStringExtra("date_sel");
                    tvDateDayAfter.setVisibility(View.GONE);
                    setGuidelinePercentage(View.GONE);
                    tvDateTomorrow.setText(strDate.replace(",", "\n"));
                    tvDateTomorrow.setSelected(true);
                    tvDateTomorrow.setTextAppearance(getActivity(), R.style.CalendarDateStyle_Highlighted);
                    enableBookService();
                    strSelectedDate = tvDateTomorrow.getText().toString().trim();
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                        String appointmentdate = dateFormat1.format(dateFormat.parse(date_sel));
                        mAppointmentDate = appointmentdate + "T00:00:00";
                    } catch (Exception e) {
                        RELog.e(e);
                    }
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Motorcycles-Schedule a service");
                    params.putString("eventAction", "Calendar icon click");
                    params.putString("eventLabel", date_sel);
                    REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                } else {
                    String strDate = data.getStringExtra("date");
                    String strpos = data.getStringExtra("position");
                    String strSlotid = data.getStringExtra("slot_id");
                    String strTime = data.getStringExtra("time");
                    mCalendarSelectedDate = data.getStringExtra("rawdate");
                    Bundle params1 = new Bundle();

                    if (mFromReschedule) {
                        params1.putString("eventCategory", "Motorcycles");
                        params1.putString("eventAction", "Reschedule");
                        params1.putString("eventLabel", mCalendarSelectedDate);
                    }
                    REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params1);
                    mSlotId = strSlotid;
                    int strposition = Integer.parseInt(strpos);
                    tvDateDayAfter.setVisibility(View.GONE);
                    setGuidelinePercentage(View.GONE);
                    tvDateTomorrow.setText(strDate.replace(",", "\n"));
                    tvDateTomorrow.setSelected(true);
                    tvDateTomorrow.setTextAppearance(getActivity(), R.style.CalendarDateStyle_Highlighted);

                    enableBookService();

                    mServiceTimeSlotResponse = REApplication.getInstance().
                            getServiceSlotResponse();
                    mAppointmentDate = mServiceTimeSlotResponse.get(strposition).getStartTimeSlot();
                    mPickupTime = strTime;
                    strSelectedDate = tvDateTomorrow.getText().toString().trim();

                    setTimeSlotsToRecyclerView(strposition);

                }
            }
        }
    }

    /**
     * Populates the searched service center from the list.
     */
    private void showSelectedServiceCenter(ArrayList<DealerMasterResponse> searchedDealerList) {
        if (searchedDealerList.size() > 0) {
            int iServiceType;
            mServiceCenterListViewItemList.clear();
            if (mVehicleDetailsList != null && mVehicleDetailsList.size() > 0) {
                iServiceType = ServiceCenterDataModel.SEARCH_SC_TYPE;
            } else {
                iServiceType = ServiceCenterDataModel.NEAR_YOU_NO_VEHICLE_TYPE;
            }
            List<DealerMasterResponse> lastVisitedServiceCenter = getDealerWithDistance(searchedDealerList.get(0));
            mServiceCenterListViewItemList.add(new ServiceCenterDataModel(iServiceType,
                    lastVisitedServiceCenter.get(0)
            ));
            mServiceCenterAdapter = new ServiceCenterDataViewAdapter(getContext(), mServiceCenterListViewItemList,
                    this, null);
            if (mVehicleDetailsList != null && mVehicleDetailsList.size() > 0) {
                recyclerViewServiceCenter.setAdapter(mServiceCenterAdapter);
            }
        }
    }

    private void hideServiceOptionLabel() {
        layoutPickupAddress.setVisibility(View.GONE);
        tvAddModifyAddress.setVisibility(View.GONE);
        ivModifyAddressArrow.setVisibility(View.GONE);
        tvServiceOptionLabel.setVisibility(View.GONE);
        rgServiceOption.setVisibility(View.GONE);
        lineServiceBorder.setVisibility(View.GONE);
    }

    private void updatePickupAddressView() throws PreferenceException {
        flat = REPreference.getInstance().getString(REApplication.getAppContext(), "flatNo");
        add = REPreference.getInstance().getString(REApplication.getAppContext(), "address");
        mPickupAddress = REPreference.getInstance().getString(REApplication.getAppContext(), REConstants.PREF_ADDRESS_KEY);
        if (isPickupSelected) {
            checkFlatAndAddress(flat, add);
        }
    }

    /**
     * This is to add address dynamically
     *
     * @param address
     */
    private void addAddress(String address) {
        mPickupAddress = address;
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView pickupAddressLabel;
        final View rowView = inflater.inflate(R.layout.layout_service_pickup_address, null);
        pickupAddressLabel = rowView.findViewById(R.id.label_pickup_address_text);
        TextView pickup_address = rowView.findViewById(R.id.tv_pickup_address);
        if (!address.isEmpty()) {
            if (layoutPickupAddress.getVisibility() == View.VISIBLE) {
                ivModifyAddressArrow.setVisibility(View.VISIBLE);
            }
            tvAddModifyAddress.setText(R.string.text_modifyaddress);
            pickupAddressLabel.setText(R.string.text_address_title);
            pickup_address.setText(address);
        } else {
            ivModifyAddressArrow.setVisibility(View.GONE);
            tvAddModifyAddress.setText(R.string.text_new_addaddress);
            pickupAddressLabel.setText("");
            pickup_address.setText(R.string.text_noaddress_added);
        }
        layoutPickupAddress.removeAllViews();
        layoutPickupAddress.addView(rowView, layoutPickupAddress.getChildCount());
    }

    /**
     * get the selected timeSlot on itemClick(timeSlot recycleView).
     *
     * @param position
     */
    private void getTimeSlotOnTimeClick(int position) {

        mPickupTime = REUtils.getTimeSlots(mServiceTimeSlotResponse.get(position).getStartTimeSlot());
        mSlotId = mServiceTimeSlotResponse.get(position).getResourceId();
        mAppointmentDate = mServiceTimeSlotResponse.get(position).getStartTimeSlot();
        timeSlotAdapter.notifyDataSetChanged();
        enableBookService();
    }

    @Override
    public void onFirebaseDealersListSuccess(List<DealerMasterResponse> dealersResponseArrayList) {
        hideLoading();
//        if (mDealersResponseList != null) mDealersResponseList.clear();
//        mDealersResponseList = dealersResponseArrayList;
//        DealersDistanceCalcAsyncTask distanceCalcAsyncTask = new DealeDersDistanceCalcAsyncTask(mDealersResponseList,
//                this);
//        distanceCalcAsyncTask.execute();
    }

    @Override
    public void onFirebaseDealerListFailure(String message) {
        hideLoading();
    }


    /**
     * Enables the service center recyclerview if API is success
     */
    private void serviceCenterRecyclerViewVisible() {
        mLServiceCenter.setVisibility(View.VISIBLE);
        recyclerViewServiceCenter.setVisibility(View.VISIBLE);
        tvSearchServiceCenter.setVisibility(View.VISIBLE);
    }

    /**
     * Displays the Nearest Service center for the User
     */
    private void showNearByServiceCenterView() {
        mSelectedDealerResponse = null;

        // TODO Need to check once API works
        //Disable view history label.
//        viewServiceHistory.setTextColor(getResources().getColor(R.color.white_30));
//        viewServiceHistory.setCompoundDrawablesWithIntrinsicBounds(null, null,
//                getResources().getDrawable(R.drawable.next_arrow_grey), null);
//        viewServiceHistory.setEnabled(false);
        //Prepares the near by service center list/
        prepareNearByServiceCenterList();
        //Setup the adapter for the near by service center.
        setupNearByServiceCenterAdapter();
    }

    /**
     * Checks the Service type and prepares the Near by service center list.
     */
    private void prepareNearByServiceCenterList() {
        int iServiceType;
        mServiceCenterListViewItemList.clear();
        if (mVehicleDetailsList != null && mVehicleDetailsList.size() > 0) {
            iServiceType = ServiceCenterDataModel.NEAR_YOU_TYPE;
        } else {
            iServiceType = ServiceCenterDataModel.NEAR_YOU_NO_VEHICLE_TYPE;
        }
        List<DealerMasterResponse> subDealersList;
        if (mDealersResponseList.size() > 2) {
            subDealersList = mDealersResponseList.subList(0, 2);
        } else {
            subDealersList = mDealersResponseList;
        }
        for (DealerMasterResponse dealersResponse : subDealersList) {
            mServiceCenterListViewItemList.add(new ServiceCenterDataModel(iServiceType,
                    dealersResponse
            ));
        }
    }


    /**
     * Setup the near by Service center adapter.
     */
    private void setupNearByServiceCenterAdapter() {
        mServiceCenterAdapter = new ServiceCenterDataViewAdapter(getContext(),
                mServiceCenterListViewItemList, this, null);
        if (mVehicleDetailsList != null && mVehicleDetailsList.size() > 0) {
            recyclerViewServiceCenter.setAdapter(mServiceCenterAdapter);
        }
    }

    /**
     * Displays the last visited Service center for the User.
     *
     * @param lastVisitedServiceCenter last visited service center list.
     */
    private void showLastVisitedServiceCenter(List<DealerMasterResponse> lastVisitedServiceCenter) {
        mServiceCenterListViewItemList.clear();
        mDealersResponseList = lastVisitedServiceCenter;
        tvSearchServiceCenter.setVisibility(View.VISIBLE);
        enableServiceHistory();
        for (DealerMasterResponse dealersResponse : mDealersResponseList) {
            mServiceCenterListViewItemList.add(new ServiceCenterDataModel(ServiceCenterDataModel.LAST_VISITED_TYPE,
                    dealersResponse
            ));
        }

        mServiceCenterAdapter = new ServiceCenterDataViewAdapter(getContext(), mServiceCenterListViewItemList,
                this, null);
        recyclerViewServiceCenter.setAdapter(mServiceCenterAdapter);
    }

    /**
     * Handles the Selected service center from the service center list.
     *
     * @param data Intent data.
     */
    private void handleSelectedServiceCenter(Intent data) {
        if (data.getStringExtra("sc_index") != null) {
            ArrayList<DealerMasterResponse> searchedFilteredDealerList = data.
                    getParcelableArrayListExtra(REConstants.DEALERS_FILTERED_LIST_EXTRA);
            if (mVehicleDetailsList != null && mVehicleDetailsList.size() > 0) {
                onMotorcycleChanged();
            }
            mSelectedDealerResponse = null;
            showSelectedServiceCenter(searchedFilteredDealerList);
            hideServiceOptionLabel();
            disableBookService();
        }
    }

    /**
     * Handles the Added specific issues of the Bike.
     */
    private void handleSpecificIssues() {
        if (REUtils.getSelectedIssues(true, getAppContext()).size() > 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            StringBuilder issueSelected = new StringBuilder();
            for (String issue : REUtils.getSelectedIssues(true, getAppContext())) {
                issueSelected.append(issue).append("\n");
            }
            String issueDescription = REApplication.getInstance().getDescriptionValue();
            mCustomerRemarks = issueSelected.toString() + issueDescription;
            tvAddIssueDesc.setVisibility(View.VISIBLE);
            mCustomerRemarks = mCustomerRemarks.trim();
            StyleSpan spanBold = new StyleSpan(android.graphics.Typeface.BOLD);
            spannableStringBuilder.append(getResources().getString(R.string.issue_desc));
            spannableStringBuilder.append("\n");
            spannableStringBuilder.append(mCustomerRemarks);
            spannableStringBuilder.setSpan(spanBold, 0, getResources().getString(R.string.issue_desc).length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            tvAddIssueDesc.setText(spannableStringBuilder);
            tvSpecificIssue.setText(getResources().getString(R.string.text_label_modify_issues));
        } else {
            if (REApplication.getInstance().getDescriptionValue() != null
                    && !REApplication.getInstance().getDescriptionValue().equals("") &&
                    !REApplication.getInstance().getDescriptionValue().isEmpty()) {
                tvAddIssueDesc.setVisibility(View.VISIBLE);
                mCustomerRemarks = REApplication.getInstance().getDescriptionValue();
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                StyleSpan spanBold = new StyleSpan(android.graphics.Typeface.BOLD);
                spannableStringBuilder.append(getResources().getString(R.string.issue_desc));
                spannableStringBuilder.append("\n");
                spannableStringBuilder.append(mCustomerRemarks);
                spannableStringBuilder.setSpan(spanBold, 0, getResources().getString(R.string.issue_desc).length(),
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                tvAddIssueDesc.setText(spannableStringBuilder);
                tvSpecificIssue.setText(getResources().getString(R.string.text_label_modify_issues));
            } else {
                mCustomerRemarks = "";
                tvAddIssueDesc.setVisibility(View.GONE);
                tvSpecificIssue.setText(getResources().getString(R.string.text_label_specific_issue));
            }
        }
    }

    /**
     * Enables the Reschedule UI for the Service.
     */
    private void enableRescheduleUi() {
        tvServiceOptionLabel.setVisibility(View.VISIBLE);
        rgServiceOption.setVisibility(View.VISIBLE);
        lineServiceBorder.setVisibility(View.VISIBLE);
        mViewRescheduleLine.setVisibility(View.VISIBLE);
        mRevertToOriginalRequest.setVisibility(View.VISIBLE);
        mScheduleService.setText(R.string.text_title_reschedule_service);
        //Disable bike selection spinner.
        mSpinnerItemView.setBackgroundColor(getResources().getColor(R.color.white_30));
        mSpinner.setBackgroundColor(getResources().getColor(R.color.white_30));
        mSpinner.setEnabled(false);
        //Disable view history label.
        disableServiceHistory();
        //Disable search service center label.
        tvSearchServiceCenter.setTextColor(getResources().getColor(R.color.white_30));
        tvSearchServiceCenter.setCompoundDrawablesWithIntrinsicBounds(null, null,
                getResources().getDrawable(R.drawable.next_arrow_grey), null);
        tvSearchServiceCenter.setEnabled(false);
        //adding motorcycle issues from service in reschedule flow
        mServiceProgressResponse = REApplication.getInstance().getServiceProgressListResponse();
        if (mServiceProgressResponse != null && mServiceProgressResponse.size() > 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            StyleSpan spanBold = new StyleSpan(android.graphics.Typeface.BOLD);
            spannableStringBuilder.append(getResources().getString(R.string.issue_desc));
            spannableStringBuilder.append("\n");
            spannableStringBuilder.setSpan(spanBold, 0, getResources().getString(R.string.issue_desc).length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            for (VehicleServiceProgressListResponse serviceAppointmentListResponse : mServiceProgressResponse) {
                String customerRemarks = serviceAppointmentListResponse.getCustomerRemarks();
                //String customerRemarks = "";
                if (customerRemarks != null && !customerRemarks.isEmpty()) {
                    spannableStringBuilder.append(customerRemarks);
                    tvAddIssueDesc.setText(spannableStringBuilder);
                    tvAddIssueDesc.setVisibility(View.VISIBLE);
                } else {
                    tvAddIssueDesc.setVisibility(View.GONE);
                }
                tvSpecificIssue.setText(customerRemarks != null && !customerRemarks.isEmpty() ? getResources().getString(R.string.text_label_modify_issues) :
                        getResources().getString(R.string.text_label_specific_issue));
            }
        }
        tvSpecificIssue.setTextColor(getResources().getColor(R.color.white_30));
        tvSpecificIssue.setCompoundDrawablesWithIntrinsicBounds(null, null,
                getResources().getDrawable(R.drawable.next_arrow_grey), null);
        tvSpecificIssue.setEnabled(false);
        btn_book_service.setText(R.string.text_reschedule_now);

        if (REApplication.getInstance().getServiceProgressListResponse().get(0).getPickupAddres() != null &&
                !REApplication.getInstance().getServiceProgressListResponse().get(0).getPickupAddres().isEmpty()) {
            if (REApplication.getInstance().getServiceProgressListResponse().get(0).getServiceBookingType().equals(REConstants.SERVICE_BOOKING_TYPE_PICKUPANDDROP)) {
                // case when pickupdrop is selected when service bookin done
                isPickupSelected = true;
                isDoorStepSelected = false;
                mRadioButtonPickUp.setTypeface(mTypefaceRegular);
                mRadioButtonPickUp.setChecked(true);
                mRadioButtonPickUp.setEnabled(false);
                mRadioButtonSelfDrop.setTypeface(mTypefaceRegular);
                mRadioButtonSelfDrop.setChecked(false);
                mRadioButtonSelfDrop.setEnabled(false);
                mRadioButtonDoorStep.setTypeface(mTypefaceRegular);
                mRadioButtonDoorStep.setChecked(false);
                mRadioButtonDoorStep.setEnabled(false);
                if (mSelectedDealerResponse != null && mSelectedDealerResponse.getDoorStepServiceAvailable().equalsIgnoreCase(REConstants.SERVICE_AVAILABLE_YES))
                    mRadioButtonDoorStep.setVisibility(View.VISIBLE);
                else
                    mRadioButtonDoorStep.setVisibility(View.GONE);
            } else if (REApplication.getInstance().getServiceProgressListResponse().get(0).getServiceBookingType().equals(REConstants.SERVICE_BOOKING_TYPE_DOORSTEP)) {
                // case when doorstep is selected when service bookin done
                isPickupSelected = false;
                isDoorStepSelected = true;
                mRadioButtonDoorStep.setTypeface(mTypefaceRegular);
                mRadioButtonDoorStep.setChecked(true);
                mRadioButtonDoorStep.setEnabled(false);
                mRadioButtonPickUp.setTypeface(mTypefaceRegular);
                mRadioButtonPickUp.setChecked(false);
                mRadioButtonPickUp.setEnabled(false);
                mRadioButtonSelfDrop.setTypeface(mTypefaceRegular);
                mRadioButtonSelfDrop.setChecked(false);
                mRadioButtonSelfDrop.setEnabled(false);
                if (mSelectedDealerResponse != null && mSelectedDealerResponse.getPickUpServiceAvailable().equalsIgnoreCase(REConstants.SERVICE_AVAILABLE_YES))
                    mRadioButtonPickUp.setVisibility(View.VISIBLE);
                else
                    mRadioButtonPickUp.setVisibility(View.GONE);
            }
            //Disable Modify address label and arrow image.
            tvAddModifyAddress.setTextColor(getResources().getColor(R.color.white_30));
          /*  tvAddModifyAddress.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.next_arrow_grey), null);*/
            tvAddModifyAddress.setEnabled(false);
            ivModifyAddressArrow.setBackground(getResources().getDrawable(R.drawable.next_arrow_grey));
            ivModifyAddressArrow.setEnabled(false);
            enablePickupAddressView();
        }
        // case when selfdrop is selected when service bookin done
        else {
            isPickupSelected = false;
            isDoorStepSelected = false;
            mRadioButtonSelfDrop.setTypeface(mTypefaceRegular);
            mRadioButtonSelfDrop.setChecked(true);
            mRadioButtonSelfDrop.setEnabled(false);
            mRadioButtonPickUp.setTypeface(mTypefaceRegular);
            mRadioButtonPickUp.setChecked(false);
            mRadioButtonPickUp.setEnabled(false);
            mRadioButtonDoorStep.setTypeface(mTypefaceRegular);
            mRadioButtonDoorStep.setChecked(false);
            mRadioButtonDoorStep.setEnabled(false);
            if (mSelectedDealerResponse != null && mSelectedDealerResponse.getDoorStepServiceAvailable().equalsIgnoreCase(REConstants.SERVICE_AVAILABLE_YES))
                mRadioButtonDoorStep.setVisibility(View.VISIBLE);
            else
                mRadioButtonDoorStep.setVisibility(View.GONE);
            if (mSelectedDealerResponse != null && mSelectedDealerResponse.getPickUpServiceAvailable().equalsIgnoreCase(REConstants.SERVICE_AVAILABLE_YES))
                mRadioButtonPickUp.setVisibility(View.VISIBLE);
            else
                mRadioButtonPickUp.setVisibility(View.GONE);
        }

        mServiceCenterAdapter = new ServiceCenterDataViewAdapter(getContext(), mServiceCenterListViewItemList,
                null, null);
        recyclerViewServiceCenter.setAdapter(mServiceCenterAdapter);
    }

    /**
     * API call for fetching the getVehicleServiceProgressList data
     */
    private void getVehicleServiceProgressList() {
        ServiceProgressDetailsPresenter mServiceProgressDetailsPresenter = new ServiceProgressDetailsPresenter(
                this, new ServiceProgressDetailsInteractor());
        String mobile = REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone();
        if (mobile != null && !mobile.isEmpty()) {
            showLoading();
            mServiceProgressDetailsPresenter.getVehicleServiceInProgressList(REUtils.trimCountryCodeFromMobile(mobile));
        } else {
            //showErrorLayout();
            showErrorAlert();
        }
    }

    /**
     * Populate bike details list to the spinner.
     */
    private void populateBikeListSpinner() {
        if (mVehicleDetailsList != null && mVehicleDetailsList.size() > 0) {
            // Vehicle Layout UI is initialised and set to visible
            mConstraintVehicleRegistered.setVisibility(View.VISIBLE);
            if (mFromReschedule) {
                mServiceProgressResponse = REApplication.getInstance().getServiceProgressListResponse();

                if (mServiceProgressResponse != null && mServiceProgressResponse.size() > 0) {
                    //for getting available dates for doorstep and pickudrop service
                    String dealerid = mServiceProgressResponse.get(0).getDealerInfo();
                    getPickupAndDoorstepServiceSlotsApi(dealerid);

                    registrationNo = mServiceProgressResponse.get(0).getRegistrationNumber();

//                    mModelName = mServiceProgressResponse.get(0).getModelName();
//                    if (mModelName == null || mModelName.isEmpty()) {
                    GetModelAsyncTask getModelAsyncTask = new GetModelAsyncTask(registrationNo, this);
                    getModelAsyncTask.execute();
//                    } else {
//                        truncatedModelName = REUtils.bikeNameInSpinner(mModelName);
//                        bikeList.add(truncatedModelName + " (" + registrationNo + ")");
//                        mSelectedDealerBranchId = mServiceProgressResponse.get(0).getDealerInfo();
//                    }
                }
            } else {
                for (int i = 0; i < mVehicleDetailsList.size(); i++) {
                    String modelCode = mVehicleDetailsList.get(i).getModelCode();
                    mModelName = mVehicleDetailsList.get(i).getMapVehicleData().get(modelCode);
//                    truncatedModelName = REUtils.bikeNameInSpinner(mModelName);
                    if (mModelName != null && (mVehicleDetailsList.get(i).getRegistrationNo()) != null)
                        bikeList.add(mModelName + " (" + mVehicleDetailsList.get(i).getRegistrationNo() + ")");
                    else
                        bikeList.add("NA");

                }
            }
            //Set Spinner adapter for the bike list..
            setAdapter();
        } else {
            // No vehicle registered for the user account
            openNoVehicleServiceFragment();
        }
    }

    private void setAdapter() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), R.layout.spinner_item, bikeList);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Opens NoVehicle Service Fragment
     */
    private void openNoVehicleServiceFragment() {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.root_frame, new REServicingNoVehicleFragment());
        trans.commitAllowingStateLoss();
    }

    /**
     * Checks For the Book or Reschedule API call to made.
     */
    private void bookOrRescheduleService() throws PreferenceException {
        if (isPickupSelected && mPickupAddress.isEmpty()) {
            REUtils.showErrorDialog(getContext(), "Please add pickup address");
        } else if (!isDoorStepSelected && (mPickupTime == null || mPickupTime.isEmpty())) {
            REUtils.showErrorDialog(getContext(), "Please select a time slot");
        } else if (isDoorStepSelected && mPickupAddress.isEmpty()) {
            REUtils.showErrorDialog(getContext(), "Please add address");
        } else {
            if (btn_book_service.getText().toString().equalsIgnoreCase("Book Service")) {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Motorcycles-Schedule a service");
                params.putString("eventAction", "Book service click");
                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                bookService();
            } else {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Motorcycles-Reschedule");
                params.putString("eventAction", "Reschedule now cliked");
                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                rescheduleService();
            }
        }
    }

    /**
     * API-Call for doing the Service booking for the bike
     *
     * @param : ServiceBookingRequest{@link ServiceBookingRequest} List object.
     */
    private void doServiceBookingForVehicle(ServiceBookingRequest serviceBookingRequest) {
        showLoading();
        mServiceBookingPresenter.doServiceBooking(serviceBookingRequest);
    }

    /**
     * API-Call : TO Book a Service.
     */
    private void bookService() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String date1 = dateFormat1.format(dateFormat.parse(mAppointmentDate));
            Date d1 = dateFormat1.parse(date1);
            String date2 = dateFormat1.format(calendar.getTime());
            Date d2 = dateFormat1.parse(date2);
            if (isCutOfftime() && d1.compareTo(d2) == 0) {
                REUtils.showErrorDialog(getContext(), getResources().getString(R.string.text_servicecenter_cutofftime_message));
                setTomorrowAndDayAfterDate();
                selectTomorrowDate();
            } else {
                // For Doorstep Service No Pickup Time Required
                if (isDoorStepSelected) {
                    mPickupTime = "";
                }
                ServiceBookingRequest serviceBookingRequestArray = GetDetailsForServiceBooking.serviceBooking(getContext(), mREUserModelStore,
                        mAppointmentDate, mSelectedDealerResponse, mCustomerRemarks, isPickupSelected, mRegistrationNo, mChassisNo,
                        mSlotId, mPickupTime, mEngineNumber, isDoorStepSelected);
                doServiceBookingForVehicle(serviceBookingRequestArray);
            }
        } catch (Exception e) {
        }
    }


    /**
     * API-Call to Reschedule the Service.
     */
    private void rescheduleService() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String date1 = dateFormat1.format(dateFormat.parse(mAppointmentDate));
            Date d1 = dateFormat1.parse(date1);
            String date2 = dateFormat1.format(calendar.getTime());
            Date d2 = dateFormat1.parse(date2);
            if (isCutOfftime() && d1.compareTo(d2) == 0) {
                REUtils.showErrorDialog(getContext(), getResources().getString(R.string.text_servicecenter_cutofftime_message));
                setTomorrowAndDayAfterDate();
                selectTomorrowDate();
            } else {
                String appointmentId = null;
                List<VehicleServiceProgressListResponse> mVehicleServiceInProgress = REApplication.getInstance().
                        getServiceProgressListResponse();
                if (mVehicleServiceInProgress != null && mVehicleServiceInProgress.size() > 0) {
                    appointmentId = mVehicleServiceInProgress.get(0).getAppointmentNumber();
                }
                mCustomerRemarks = "";
                for (VehicleServiceProgressListResponse serviceAppointmentListResponse : mVehicleServiceInProgress) {
                    mCustomerRemarks = serviceAppointmentListResponse.getCustomerRemarks();

                }
                String appointmentDate = mAppointmentDate;
                String attachmentKey = "";
                String customerRemarks = mCustomerRemarks;
                String dropAddress = "";
                String slotId = mSlotId;
                String isPickUpDrop = "No";
                String pickupAddress = "";
                if (isPickupSelected) {
                    if (mVehicleServiceInProgress != null && mVehicleServiceInProgress.size() > 0) {
                        pickupAddress = mVehicleServiceInProgress.get(0).getPickupAddres();
                    }
                    isPickUpDrop = "Yes";
                }
                if (isDoorStepSelected) {
                    if (mVehicleServiceInProgress != null && mVehicleServiceInProgress.size() > 0) {
                        pickupAddress = mVehicleServiceInProgress.get(0).getPickupAddres();
                    }
                    isPickUpDrop = "No";
                }
                String serviceType = "2";
                String creationSource = "W";
                String BranchID = mSelectedDealerBranchId;
                String isdummyslots = REUtils.isDummySlotsEnabled();
                boolean isDummySlots = Boolean.parseBoolean(isdummyslots);
                showLoading();
                mRescheduleBookingInteractor.rescheduleServiceAppointment(appointmentId, appointmentDate, attachmentKey,
                        customerRemarks, "",
                        slotId, isPickUpDrop, pickupAddress, serviceType, creationSource, BranchID, mRegistrationNo,
                        isDummySlots, this);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Handles the service history failure case and no service history case
     */
    private void handleNoServiceHistoryOrFailure() {
        //Disable view history label.
        disableServiceHistory();
        //API call to fetch all the nearest service center based on its current location.
        //API call for nearest service center
        getNearestServiceCenterList();
    }

    private void getNearestServiceCenterList() {
        mServiceBookingPresenter = new
                ServiceBookingPresenter(this, new ServiceBookingInteractor());
        Location currentlocation = REUtils.getLocationFromModel();
        showLoading();
        mServiceBookingPresenter.getServiceCenters(new ServiceCenterRequest(String.valueOf(currentlocation.getLatitude()),
                String.valueOf(currentlocation.getLongitude()), 500));
       /* mServiceBookingPresenter.getServiceCenters(new ServiceCenterRequest("16.726903",
                "81.67165385", 500));*/
    }

    /**
     * Handles the Service history response for the Vehicle.
     */
    private void handleServiceHistoryResponse(int position) {
        String lastVisitedServiceCenterDealerId = mVehicleDetailsList.get(position).getServiceHistoryResponse().get(0).getBranchId();
        getLastVisitedServiceCenterFromFirebase(lastVisitedServiceCenterDealerId);
    }

    /**
     * Fetches the dealer data from firebase and getting the particular dealer from list
     *
     * @param branchId
     */
    private void getLastVisitedServiceCenterFromFirebase(String branchId) {
        if (branchId != null && !branchId.isEmpty()) {
            showLoading();
            FirebaseManager.getInstance().fetchDealerResponseFromFirebase(branchId,
                    this);
        } else {
            getNearestServiceCenterList();
        }
    }


    /**
     * Calculates the distance for the one Dealer and returns the List<DealerResponse></DealerResponse>
     *
     * @param dealersResponse : DealersResponse
     * @return : List<DealersResponse>
     */
    private List<DealerMasterResponse> getDealerWithDistance(DealerMasterResponse dealersResponse) {
        List<DealerMasterResponse> lastVisitedServiceCenter = new ArrayList<>();
        if (dealersResponse.getLatlng() != null) {
            if (!dealersResponse.getLatlng().equals("") && !dealersResponse.getLatlng().equals("0")) {
                lastVisitedServiceCenter.add(dealersResponse);
                lastVisitedServiceCenter = DistanceCalculationHelper.getInstances().
                        getDistanceBetweenServiceCenter(lastVisitedServiceCenter, true);
            } else {
                lastVisitedServiceCenter.add(dealersResponse);
            }
        } else {
            lastVisitedServiceCenter.add(dealersResponse);
        }
        return lastVisitedServiceCenter;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CALENDAR_DATE_SLOT && data != null) {
            getSelectedTimeSlot(data);
        } else if (resultCode == REQUEST_CODE_SEARCH_SELECTED_SERVICE_CENTER && data != null) {
            handleSelectedServiceCenter(data);
        } else if (requestCode == REQUEST_CODE_SPECIFIC_ISSUE) {
            handleSpecificIssues();
        } else if (requestCode == REQUEST_CODE_ADDRESS && data != null) {
            mAddressHashMap = new HashMap<>();
            mAddressHashMap = (HashMap<String, String>) data.getSerializableExtra("ADDRESS");
            //flat = data.getStringExtra("flatNo");
            add = data.getStringExtra("address");
            checkFlatAndAddress("", add);


        } else if (requestCode == REQUEST_CODE_MODIFY_ADDRESS && data != null) {
            flat = data.getStringExtra("flatNo");
            add = data.getStringExtra("address");
            checkFlatAndAddress(flat, add);
        } else if (requestCode == REQUEST_SHOW_MORE_TIMESLOT && data != null) {
            //condition to handel new timeSlot implementation onClick of showAllAvailableSlots and resultCode is 6.
            if (data.getExtras() != null && data.getExtras().get("position") != null) {
                getTimeSlotOnTimeClick((Integer) data.getExtras().get("position"));
                setTimeSlotsToRecyclerView((Integer) data.getExtras().get("position"));
            }
        } else if (requestCode == REQUEST_CODE_SERVICE_RESCHEDULE && data != null && resultCode == Activity.RESULT_OK) {
            mFromReschedule = data.getBooleanExtra(REConstants.KEY_SERVICE_ISRESCHEDULE, false);
            populateBikeListSpinner();
        }
    }


    @Override
    public void onClick(View v) {
        Bundle params = new Bundle();
        switch (v.getId()) {
            case R.id.tv_view_service_history:

                params.putString("eventCategory", "Motorcycles");
                params.putString("eventAction", "Schedule a service");
                params.putString("eventLabel", "View service history click");
                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                Intent intent = new Intent(getContext(), ServiceHistoryActivity.class);
                intent.putExtra("position", mVehicleSpinnerPosition);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.tv_date_tomorrow:
                if (tvDateTomorrow != null && tvDateTomorrow.getText() != null) {
                    strSelectedDate = tvDateTomorrow.getText().toString().trim();
                }

                params.putString("eventLabel", strSelectedDate);
                params.putString("eventAction", "Date of service click");
                if (mFromReschedule) {

                    params.putString("eventCategory", "Motorcycles-Reschedule");

                } else {
                    params.putString("eventCategory", "Motorcycles-Schedule a service");
                }

                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                selectTomorrowDate();
                break;
            case R.id.tv_date_day_after:
                if (tvDateDayAfter != null && tvDateDayAfter.getText() != null) {
                    strSelectedDate = tvDateDayAfter.getText().toString().trim();
                }
                params.putString("eventLabel", strSelectedDate);
                params.putString("eventAction", "Date of service click");
                if (mFromReschedule) {

                    params.putString("eventCategory", "Motorcycles-Reschedule");

                } else {
                    params.putString("eventCategory", "Motorcycles-Schedule a service");
                }
                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                selectDayAfterDate();
                break;
            case R.id.tv_label_search_service_center:
                params.putString("eventCategory", "Motorcycles");
                params.putString("eventAction", "Schedule a service");
                params.putString("eventLabel", "Search service centre click");
                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                //Intent intent1 = new Intent(getContext(), SearchActivity.class);
                Intent intent1 = new Intent(getContext(), SearchServiceCentreActivity.class);
                intent1.putExtra(REConstants.SEARCH_ACTIVITY_INPUT_TYPE, REConstants.SEARCH_ACTIVITY_DEALER_LIST);
                REServicingFragment.this.startActivityForResult(intent1, 3);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                break;
            case R.id.tv_label_specific_issue:
                params.putString("eventCategory", "Motorcycles-Schedule a service");
                params.putString("eventAction", tvSpecificIssue.getText().toString() + " click");
                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                REServicingFragment.this.startActivityForResult(new Intent(getContext(), AnySpecificIssueActivity.class),
                        4);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                break;
            case R.id.ivCalendar:
                Bundle params1 = new Bundle();

                if (mFromReschedule) {
                    params1.putString("eventCategory", "Motorcycles-Reschedule");
                } else {
                    params1.putString("eventCategory", "Motorcycles-Schedule a service");
                    params1.putString("eventAction", "Calendar icon click");
                }
                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params1);
                selectDayFromCalendar();
                break;
            case R.id.tv_time_slots_label:
                //onclick of showAllAvailable slots intent data has been passed to TimeSlotViewActivity.
                showAllAvailableSlots();
                break;
            case R.id.play_video:
                startActivity(new Intent(getContext(), DoItYourSelfActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                break;
            case R.id.btn_book_service:
                try {

                    bookOrRescheduleService();
                } catch (PreferenceException e) {
                    RELog.e(e);
                }
                break;
            case R.id.add_modify_address:
            case R.id.iv_modify_address_next:
                handleAddModifyAddress();
                break;
            case R.id.tv_revert_to_original_request:
                Bundle params2 = new Bundle();
                params2.putString("eventCategory", "Motorcycles-Reschedule");
                params2.putString("eventAction", "Revert to original request clicked");
                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params2);
                clearMotorcycleIssueData();
                openServiceStatusFragment();
                break;
            case R.id.rd_selfdrop:
                mRadioButtonSelfDrop.setTypeface(mTypefaceBold);
                mRadioButtonPickUp.setTypeface(mTypefaceRegular);
                mRadioButtonDoorStep.setTypeface(mTypefaceRegular);
                isPickupSelected = false;
                isDoorStepSelected = false;
                tvAddModifyAddress.setVisibility(View.GONE);
                layoutPickupAddress.setVisibility(View.GONE);
                ivModifyAddressArrow.setVisibility(View.GONE);
                Bundle paramsSelf = new Bundle();
                paramsSelf.putString("eventCategory", "Motorcycle Drop");
                paramsSelf.putString("eventAction", mRadioButtonSelfDrop.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_SERVICE_GTM, paramsSelf);
                onRadioButtonChanged();
                break;
            case R.id.rd_pickup:
                mRadioButtonPickUp.setTypeface(mTypefaceBold);
                mRadioButtonSelfDrop.setTypeface(mTypefaceRegular);
                mRadioButtonDoorStep.setTypeface(mTypefaceRegular);
                isPickupSelected = true;
                isDoorStepSelected = false;
                Bundle paramsPickup = new Bundle();
                paramsPickup.putString("eventCategory", "Motorcycle Drop");
                paramsPickup.putString("eventAction", mRadioButtonPickUp.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_SERVICE_GTM, paramsPickup);
                onRadioButtonChanged();
                try {
                    enablePickupAddressView();
                } catch (Exception e) {
                    RELog.e(e);
                }
                break;
            case R.id.rd_doorstep:
                mRadioButtonDoorStep.setTypeface(mTypefaceBold);
                mRadioButtonPickUp.setTypeface(mTypefaceRegular);
                mRadioButtonSelfDrop.setTypeface(mTypefaceRegular);
                isDoorStepSelected = true;
                isPickupSelected = false;
                Bundle paramDoor = new Bundle();
                paramDoor.putString("eventCategory", "Motorcycle Drop");
                paramDoor.putString("eventAction", mRadioButtonDoorStep.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_SERVICE_GTM, paramDoor);
                onRadioButtonChanged();
                try {
                    enablePickupAddressView();
                } catch (Exception e) {
                    RELog.e(e);
                }
                break;
            case R.id.button_retry:
                //API call for  getVehicleServiceProgressList
                getVehicleServiceProgressList();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mRegistrationNo = "";
        mVehicleSpinnerPosition = position;
        if (mVehicleDetailsList != null && mVehicleDetailsList.size() > 0) {
            mRegistrationNo = mVehicleDetailsList.get(position).getRegistrationNo();
            mChassisNo = mVehicleDetailsList.get(position).getChaissisNo();
            mEngineNumber = mVehicleDetailsList.get(position).getEngineNo();
        }
        try {
            mSpinnerItemView = ((TextView) view);
            mSpinnerItemView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().
                    getDrawable(R.drawable.path_2), null);
            mSpinnerItemView.setTextColor(getResources().getColor(R.color.black));
//        To support all versions use
            mSpinnerItemView.setTypeface(mTypefaceBold);
        } catch (Exception e) {
            RELog.e(e);
        }
        //Fetch the last visited service center for the hardcoded registration number
        if (mFromReschedule && mVehicleDetailsList != null) {
            getLastVisitedServiceCenterFromFirebase(REApplication.getInstance().getServiceProgressListResponse().
                    get(0).getDealerInfo());
        } else {
            if (mVehicleDetailsList != null && mVehicleDetailsList.get(position).getServiceHistoryResponse() != null &&
                    mVehicleDetailsList.get(position).getServiceHistoryResponse().size() > 0) {
                handleServiceHistoryResponse(position);
                enableServiceHistory();
            } else {
                disableServiceHistory();
                handleNoServiceHistoryOrFailure();
            }
            clearMotorcycleIssueData();
        }
        onMotorcycleChanged();
        tvAddIssueDesc.setVisibility(View.GONE);
        tvSpecificIssue.setVisibility(View.VISIBLE);
        tvSpecificIssue.setText(getResources().getString(R.string.text_label_specific_issue));
    }

    private void clearMotorcycleIssueData() {
        //Clears the added issue list for the previous selected bike
        REApplication.getInstance().getAllSelectedServiceIssues().clear();
        REApplication.getInstance().getMapUserDescribedIssues().clear();
        REApplication.getInstance().getListOfUserDescribedIssues().clear();
        REApplication.getInstance().setDescriptionValue("");
        REServiceSharedPreference.saveAllSelectedServiceIssues(getAppContext(), new LinkedHashMap<>());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDealersDistanceCalcComplete(List<DealerMasterResponse> dealersResponseList) {
        mDealersResponseList = dealersResponseList;
        showNearByServiceCenterView();
        serviceCenterRecyclerViewVisible();
        if (mFromReschedule) {
            try {
                enableRescheduleUi();
            } catch (Exception e) {
                RELog.e(e);
            }
        }
        hideLoading();
    }

    @Override
    public void onGetModelComplete(String modelName) {
        mModelName = modelName;
//        truncatedModelName = REUtils.bikeNameInSpinner(mModelName);
        if (mModelName != null && registrationNo != null)
            bikeList.add(mModelName + " (" + registrationNo + ")");
        else
            bikeList.add("NA");
        mSelectedDealerBranchId = mServiceProgressResponse.get(0).getDealerInfo();
        setAdapter();
    }

    /**
     * Handles the scroll to top for the screen.
     */
    public interface ScrollUpListener {
        void scrollUp();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScrollUpListener)
            scrollUpListener = (ScrollUpListener) context;
    }

    @Override
    public void onServiceBookingSuccess() {
        List<ServiceBookingResponse> serviceBookingResponseList = REApplication.getInstance().getServiceBookingResponse();
        hideLoading();
        if (serviceBookingResponseList != null && serviceBookingResponseList.size() > 0) {
            Intent serviceBookedIntent = RESuccessSplashActivity.getIntent(getContext(),
                    this, getResources().getString(R.string.text_message_servicebooked));
            startActivity(serviceBookedIntent);
            getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
        }
        clearMotorcycleIssueData();
    }

    @Override
    public void onServiceBookingFailure(String errorMessage) {
        hideLoading();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Book a Service Alert");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        REUtils.showErrorDialog(getContext(), errorMessage);
    }

    @Override
    public void onServiceTimeSlotSuccess() {
        mPickupTime = "";
        tvTimeLabel.setVisibility(View.VISIBLE);
        disableBookService();
        setTimeSlotsToRecyclerView(-1);
        tvShowAllSlots.setVisibility(View.VISIBLE);
        hideLoading();
    }

    private void disableBookService() {
        btn_book_service.setEnabled(false);
        btn_book_service.setBackgroundResource(R.drawable.button_border_disable);
        btn_book_service.setTextColor(getResources().getColor(R.color.white_50));
    }

    private void enableBookService() {
        btn_book_service.setBackgroundResource(R.drawable.button_border_enable);
        btn_book_service.setTextColor(getResources().getColor(R.color.white));
        btn_book_service.setEnabled(true);
    }

    @Override
    public void onServiceTimeSlotFailure(String errorMessage) {
        tvTimeLabel.setVisibility(View.GONE);
        disableTomorrowAndDayAfterTomorrowDatePicker();
        tvShowAllSlots.setVisibility(View.GONE);
        hideLoading();
        if (recyclerView_timeslots != null && timeSlotAdapter != null) {
            recyclerView_timeslots.setAdapter(null);
        }
        REUtils.showErrorDialog(getContext(), errorMessage);
    }

    @Override
    public void onServiceCenterFetchSuccess(List<DealerMasterResponse> response) {
        hideLoading();
        if (mDealersResponseList != null) mDealersResponseList.clear();
        mDealersResponseList = response;
        onDealersDistanceCalcComplete(response);
    }

    @Override
    public void onServiceCenterFetchFailed(String errorMessage) {
        hideLoading();
    }


    @Override
    public void onRescheduleSuccess(String bookingNo) {
        hideLoading();
        Intent serviceBookedIntent = RESuccessSplashActivity.getIntent(getContext(), this,
                getResources().getString(R.string.text_message_servicerescheduled));
        startActivity(serviceBookedIntent);
        if (getActivity() != null)
            getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
    }

    @Override
    public void onRescheduleFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialog(getContext(), errorMessage);
    }

    @Override
    public void onItemClick(int value, boolean isSelected) {
        onMotorcycleChanged();
        hideServiceOptionLabel();
        if (mDealersResponseList != null && mDealersResponseList.size() > 0) {
            if (isSelected) {

                tvServiceOptionLabel.setVisibility(View.VISIBLE);
                rgServiceOption.setVisibility(View.VISIBLE);
                lineServiceBorder.setVisibility(View.VISIBLE);
                //mSelectedDealerResponse = mDealersResponseList.get(value);
                mSelectedDealerResponse = mServiceCenterListViewItemList.get(value).getDealerMasterResponse();
                mSelectedDealerBranchId = mSelectedDealerResponse.getBranchCode();
                //Api call to get the available Dates for Pickup & Drop and DoorStep Service
                getPickupAndDoorstepServiceSlotsApi(mSelectedDealerBranchId);
                if (isPickupSelected) {
                    try {
                        enablePickupAddressView();
                    } catch (Exception e) {
                        RELog.e(e);
                    }
                }

                if (mSelectedDealerResponse.getDoorStepServiceAvailable() != null &&
                        mSelectedDealerResponse.getDoorStepServiceAvailable().equalsIgnoreCase(REConstants.SERVICE_AVAILABLE_YES)) {
                    mRadioButtonDoorStep.setVisibility(View.VISIBLE);

                } else {
                    mRadioButtonDoorStep.setVisibility(View.GONE);
                }
                if (mSelectedDealerResponse.getPickUpServiceAvailable() != null &&
                        mSelectedDealerResponse.getPickUpServiceAvailable().equalsIgnoreCase(REConstants.SERVICE_AVAILABLE_YES)) {
                    mRadioButtonPickUp.setVisibility(View.VISIBLE);
                } else {
                    mRadioButtonPickUp.setVisibility(View.GONE);
                }
                mRadioButtonSelfDrop.setChecked(true);
                Bundle paramsPickup = new Bundle();
                paramsPickup.putString("eventCategory", "Motorcycle Drop");
                paramsPickup.putString("eventAction", mRadioButtonSelfDrop.getText().toString());
                REUtils.logGTMEvent(REConstants.KEY_SERVICE_GTM, paramsPickup);
            } else {
                mSelectedDealerResponse = null;
                disableBookService();
            }
        }
    }

    private void getPickupAndDoorstepServiceSlotsApi(String dealerid) {
        mServiceBookingPresenter = new
                ServiceBookingPresenter(this, new ServiceBookingInteractor());
        showLoading();
        mServiceBookingPresenter.getPickupAndDoorstepServiceSlots(dealerid);
    }

    @Override
    public void ongetPickupAndDoorstepServiceSlotsSuccess() {
        hideLoading();
        mpPickupandDoorstepServiceSlots = REApplication.getInstance().getPickupandDoorstepServiceSlotResponse();
    }

    @Override
    public void ongetPickupAndDoorstepServiceSlotsFailure() {
        hideLoading();
    }

    private void showOfflineServiceDialog(String mServiceCenterNo) {
        REUtils.showOnlyOfflineServicDialog(getActivity(), getString(R.string.text_servicecenter_offline_title), getString(R.string.text_servicecenter_offline_msg), new REUtils.OnDialogButtonClickListener() {
            @Override
            public void onOkCLick() {
                if (mServiceCenterNo != null && !mServiceCenterNo.equals("")) {
                    if (getActivity() != null) {
                        ((REBaseActivity) getActivity()).checkAndRequestCallPermissions(getContext(),
                                getActivity(), mServiceCenterNo,
                                SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS, REServicingFragment.this);
                    }
                } else {
                    REUtils.showErrorDialog(getContext(), "Contact number unavailable");
                }
            }
        });
    }

    @Override
    public void onTimeClick(int position) {
        Bundle params = new Bundle();
        if (mFromReschedule) {
            params.putString("eventCategory", "Motorcycles-Reschedule");
            params.putString("eventAction", "Time of motorcycles drop click");
        } else {
            params.putString("eventCategory", "Motorcycles-Schedule a service");
            params.putString("eventAction", "Time of motorcycles drop click");
        }
        params.putString("eventLabel", REUtils.getTimeSlots(mServiceTimeSlotResponse.get(position).getStartTimeSlot()));
        ;

        REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
        getTimeSlotOnTimeClick(position);
    }

    @Override
    public void simError(String message) {
        REUtils.showErrorDialog(getContext(), message);
    }

    @Override
    public void openServiceStatusFragment() {
        if (getFragmentManager() != null) {
            FragmentTransaction trans = getFragmentManager().beginTransaction();
            Fragment mStatusFragment = ServiceAppointmentStatusAndDetailsFragment.newInstance();
            mStatusFragment.setTargetFragment(REServicingFragment.this, REQUEST_CODE_SERVICE_RESCHEDULE);
            trans.replace(R.id.root_frame, new ServiceAppointmentStatusAndDetailsFragment());
            trans.commitAllowingStateLoss();
        }
        if (scrollUpListener != null)
            scrollUpListener.scrollUp();
    }

    private void checkFlatAndAddress(String flat, String add) {
        if (flat.equals("")) {
            addAddress(add);
        } else {
            addAddress("#" + flat + ", " + add);
        }
    }

    @Override
    public void onFirebaseDealerDetailSuccess(DealerMasterResponse dealersDetailResponse) {
        if (dealersDetailResponse != null) {
            tvSearchServiceCenter.setVisibility(View.VISIBLE);
            //Check whether it is from reschedule and proceed

            List<DealerMasterResponse> lastVisitedServiceCenter = getDealerWithDistance(dealersDetailResponse);
            mSelectedDealerResponse = null;
            if (mFromReschedule) {
                mSelectedDealerResponse = dealersDetailResponse;
                mServiceCenterListViewItemList.add(new ServiceCenterDataModel(ServiceCenterDataModel.SEARCH_SC_TYPE,
                        dealersDetailResponse
                ));
                try {
                    enableRescheduleUi();
                } catch (Exception e) {
                    RELog.e(e);
                }
            } else {
                if (lastVisitedServiceCenter != null && lastVisitedServiceCenter.size() > 0) {
                    showLastVisitedServiceCenter(lastVisitedServiceCenter);
                }

            }
            hideLoading();
        } else {
            REUtils.showErrorDialog(getContext(), REApplication.getAppContext().getResources().getString(R.string.system_down));
            hideLoading();
        }
    }

    @Override
    public void onFirebaseDealerDetailFailure(String message) {
//        tvSearchServiceCenter.setVisibility(View.GONE);
        if (mFromReschedule) {
            try {
                enableRescheduleUi();
            } catch (Exception e) {
                RELog.e(e);
            }
            hideLoading();
        } else {
            //API call to fetch all the nearest service center based on its current location.
//            showLoading();
//            FirebaseManager.getInstance().fetchDealerResponseFromFirebase("", this);
            getNearestServiceCenterList();
        }
    }

    @Override
    public void onServiceInProgressuccess() {
        mErrorConstraint.setVisibility(View.GONE);
        if (getActivity() != null) {
            if (REApplication.getInstance().getServiceProgressListResponse() != null
                    && REApplication.getInstance().getServiceProgressListResponse().size() > 0) {
                String isServiceInProgress = REApplication.getInstance().getServiceProgressListResponse().get(0).getIsServiceInProgress();
                if (isServiceInProgress != null && !mFromReschedule && isServiceInProgress.equals("Yes")) {
                    openServiceStatusFragment();
                } else {
                    populateBikeListSpinner();
                }
            }

        }
        hideLoading();
    }

    @Override
    public void onServiceInProgressFailure(String errorMessage) {
        hideLoading();
        //showErrorLayout();
        showErrorAlert();
    }

    private void showErrorLayout() {
        mConstraintVehicleRegistered.setVisibility(View.GONE);
        mErrorConstraint.setVisibility(View.VISIBLE);
    }

    private String getcurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String currentTime = df.format(cal.getTime());
        return currentTime;
    }

    private boolean isCutOfftime() {
        boolean value = false;
        try {
            //Log.e("FirestoreValue_Cutoff",REUtils.servicebookingCutOffTime());
            String currenttime = getcurrentTime();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date date1 = sdf.parse(currenttime);
            // Date date2 = sdf.parse("10:10:AM");
            Date date2 = sdf.parse(REUtils.servicebookingCutOffTime());
            if (date1.compareTo(date2) >= 0) {
                value = true;
            } else {
                value = false;
            }
        } catch (Exception e) {
        }
        return value;
    }

    private void showErrorAlert() {
        REUtils.showErrorDialogWithTwoButtons(mContext,true, getString(R.string.txt_something_went_wrong_error),
                "Retry", "Cancel", new REUtils.OnDialogButtonsClickListener() {
                    @Override
                    public void onOkCLick() {
                        //API call to get getVehicleServiceProgressList
                        getVehicleServiceProgressList();
                    }

                    @Override
                    public void onCancelClick() {
                        goBack();
                    }
                });
    }

    private void goBack() {
        REApplication.getInstance().setServiceBookingResponse(null);
        REApplication.getInstance().setDescriptionValue("");
        try {
            REPreference.getInstance().putString(mContext, "address", "");
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        mContext.finish();
        mContext.overridePendingTransition(0, R.anim.slide_out_right);
    }
}
