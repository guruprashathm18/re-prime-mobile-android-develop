package com.royalenfield.reprime.ui.home.service.billsummary.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.royalenfield.firebase.realTimeDatabase.FirebaseManager;
import com.royalenfield.firebase.realTimeDatabase.OnFirebaseDealerResponseCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.models.response.firestore.servicehistory.ServiceHistoryResponse;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.utils.REUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

public class ServiceBillSummaryActivity extends REBaseActivity implements
        TitleBarView.OnNavigationIconClickListener, OnFirebaseDealerResponseCallback {

    private int mPOSITION;
    private TextView mAddress;
    private int mSpinnerVehiclePosition;
    List<VehicleDataModel> mVehicleDetailsList = new ArrayList<>();
    private ArrayList<ServiceHistoryResponse> mServiceHistoryResponse;

    private String mInvoiceNo, mIssueDate, mAmountPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_bill_summary);

        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Bill Summary");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        mVehicleDetailsList = REServiceSharedPreference.getVehicleData(REApplication.getAppContext());
        getIntentData();
        //Getting the history details of selected vehicle based on spinner position
        mServiceHistoryResponse = (ArrayList<ServiceHistoryResponse>) mVehicleDetailsList.get(mSpinnerVehiclePosition).getServiceHistoryResponse();
        mServiceHistoryResponse = getSortedServiceHistoryList(mServiceHistoryResponse);
        initViews();
    }

    /**
     * Gets the intent data from service history
     */
    private void getIntentData() {
        Intent intent = getIntent();
        mPOSITION = intent.getIntExtra("position", 0);
        mSpinnerVehiclePosition = intent.getIntExtra("vehicleSpinnerPosition", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * Initializing views
     */
    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.tb_add_address);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().
                getString(R.string.service_bill_summary));
        TextView mInvoiceno = findViewById(R.id.textView_invoiceno);
        TextView mIssuedate = findViewById(R.id.textView_issuedate);
        TextView mAmountpaid = findViewById(R.id.textView_amountpaid);
        mAddress = findViewById(R.id.textView_address);

        getValuesFromResponse();

        mInvoiceno.setText(mInvoiceNo != null && !mInvoiceNo.isEmpty() ? mInvoiceNo :
                getApplicationContext().getResources().getString(R.string.text_hyphen_na));
//        mIssuedate.setText(REUtils.getOrdinalDateObject(this,
//                serviceHistoryResponse.get(mPOSITION).getInvoiceDate()));
        //TODO Need to do date logic
        mIssuedate.setText(mIssueDate != null && !mIssueDate.isEmpty() ? REUtils.getOrdinalDateObjectForHistoryDate(
                getApplicationContext(), mIssueDate) : getApplicationContext().getResources().getString(R.string.text_hyphen_na));
        if (mAmountPaid != null && !mAmountPaid.isEmpty()) {
            //Rounding off the amount
            mAmountpaid.setText(String.format(getResources().getString(R.string.amount_rs),
                    getResources().getString(R.string.rupees),
                    REUtils.getRoundoffAmount(mAmountPaid),
                    getResources().getString(R.string.rupees_slash)));
        } else {
            mAmountpaid.setText(getApplicationContext().getResources().getString(R.string.text_hyphen_na));
        }
        //Gets the dealer details from firebase fetched data.
        getLastVisitedServiceCenterFromFirebase(mServiceHistoryResponse.get(mPOSITION).getBranchId());
    }

    /**
     * Initialising values from ServiceHistoryResponse
     */
    private void getValuesFromResponse() {
        if (mServiceHistoryResponse != null) {
            mInvoiceNo = mServiceHistoryResponse.get(mPOSITION).getServiceInvoiceNum();
            mIssueDate = mServiceHistoryResponse.get(mPOSITION).getInvoiceDate();
            mAmountPaid = mServiceHistoryResponse.get(mPOSITION).getBillAmount();
        }
    }

    /**
     * Fetches the dealer data from firebase and getting the particular dealer from list
     *
     * @param branchId
     */
    private void getLastVisitedServiceCenterFromFirebase(String branchId) {
        showLoading();
        FirebaseManager.getInstance().fetchDealerResponseFromFirebase(branchId != null ? branchId : "", this);
    }


    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    public void onFirebaseDealersListSuccess(List<DealerMasterResponse> dealersResponseArrayList) {
        hideLoading();
    }

    @Override
    public void onFirebaseDealerListFailure(String message) {
        hideLoading();
    }

    @Override
    public void onFirebaseDealerDetailSuccess(DealerMasterResponse dealersDetailResponse) {
        if (dealersDetailResponse != null) {
            if (dealersDetailResponse.getDealerName() != null &&
                    dealersDetailResponse.getAddress() != null && !dealersDetailResponse.getAddress().isEmpty()) {
                mAddress.setText(String.format(getResources().
                                getString(R.string.text_dealer_name_address),
                        dealersDetailResponse.getDealerName(), ",", dealersDetailResponse.getAddress()));
            } else if (dealersDetailResponse.getDealerName() == null || dealersDetailResponse.getDealerName().isEmpty()) {
                mAddress.setText(dealersDetailResponse.getAddress());
            } else if (dealersDetailResponse.getAddress() == null || dealersDetailResponse.getAddress().isEmpty()) {
                mAddress.setText(dealersDetailResponse.getDealerName());
            } else {
                mAddress.setText(REApplication.getAppContext().getResources().getString(R.string.text_hyphen_na));
            }
        }
        hideLoading();
    }

    @Override
    public void onFirebaseDealerDetailFailure(String message) {
        hideLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Sorting the service history list by descending order...based on the InvoiceDate
     * InvoiceDate format is "2020-01-27 00:00:00.0"
     *
     * @param mServiceHistoryList
     * @return
     */
    private ArrayList<ServiceHistoryResponse> getSortedServiceHistoryList(ArrayList<ServiceHistoryResponse> mServiceHistoryList) {
        //mServiceHistoryList.get(3).setInvoiceDate("2020-01-28 00:00:00.0");
        //mServiceHistoryList.get(4).setInvoiceDate("2020-01-28 01:00:00.0");
        if (mServiceHistoryList != null && mServiceHistoryList.size() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            Collections.sort(mServiceHistoryList, new Comparator<ServiceHistoryResponse>() {
                @Override
                public int compare(ServiceHistoryResponse o1, ServiceHistoryResponse o2) {
                    try {
                        if (o2.getInvoiceDate() != null && o1.getInvoiceDate() != null &&
                                !o2.getInvoiceDate().isEmpty() && !o1.getInvoiceDate().isEmpty())
                            return simpleDateFormat.parse(o2.getInvoiceDate()).compareTo(simpleDateFormat.parse(o1.getInvoiceDate()));
                        return (o2.getInvoiceDate().equals(o1.getInvoiceDate())) ? 0 : (o2.getInvoiceDate() == null || o2.getInvoiceDate().isEmpty()) ? -1 : 1;
                    } catch (ParseException e) {
                        RELog.e(e);
                        return 0;
                    }
                }
            });
        }
        return mServiceHistoryList;
    }
}
