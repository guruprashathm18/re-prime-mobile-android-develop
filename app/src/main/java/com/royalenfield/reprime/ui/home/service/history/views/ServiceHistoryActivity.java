package com.royalenfield.reprime.ui.home.service.history.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.firestore.servicehistory.ServiceHistoryResponse;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.service.history.adapter.ServiceHistoryAdapter;
import com.royalenfield.reprime.ui.home.service.history.presenter.ServiceHistoryRowPresenter;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RERecyclerViewSeperatorDecoration;
import com.royalenfield.reprime.utils.REUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

public class ServiceHistoryActivity extends REBaseActivity implements
        TitleBarView.OnNavigationIconClickListener, ServiceHistoryView {

    private int mVehicleSpinnerPosition;
    private List<VehicleDataModel> mVehicleServiceHistoryResponse;

    /**
     * Broadcast Receiver for listening the Firestore Vehicle & ServiceHistory updates
     */
    BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (REConstants.FIRESTORE_UPDATE.equals(intent.getAction())) {
                loadData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicehistory);

        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Service History");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        loadData();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REConstants.FIRESTORE_UPDATE));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    /**
     * Binds the serviceHistory data based on selection
     */
    private void loadData() {
        mVehicleServiceHistoryResponse = REServiceSharedPreference.getVehicleData(REApplication.getAppContext());
        initViews();
        getIntentData();
        initRecyclerView();
    }

    /**
     * Get the registration number from the intent passed for selected bike.
     */
    private void getIntentData() {
        if (getIntent() != null) {
            mVehicleSpinnerPosition = getIntent().getIntExtra("position", 0);
        }
    }

    /**
     * Initializing views
     */
    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.tb_add_address);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().
                getString(R.string.servicehistory_title));
    }

    /**
     * Initialising recyclerView and setting the adapter with API response
     */
    private void initRecyclerView() {
        try {
            RecyclerView recyclerViewHistorylist = findViewById(R.id.recyclerview_servicehistory);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewHistorylist.setLayoutManager(mLayoutManager);
            recyclerViewHistorylist.setItemAnimator(new DefaultItemAnimator());
            //Setting item decoration for recyclerView
            RERecyclerViewSeperatorDecoration decoration = new RERecyclerViewSeperatorDecoration(this,
                    getApplicationContext().getResources().getColor(R.color.white_50), 1f);
            recyclerViewHistorylist.addItemDecoration(decoration);
            //Extracting ServiceHistory details from the selected vehicle
            ArrayList<ServiceHistoryResponse> mServiceHistoryList = (ArrayList<ServiceHistoryResponse>)
                    mVehicleServiceHistoryResponse.get(mVehicleSpinnerPosition).getServiceHistoryResponse();
            mServiceHistoryList = getSortedServiceHistoryList(mServiceHistoryList);
            //Initializing adapter
            ServiceHistoryAdapter mServiceHistoryAdapter = new ServiceHistoryAdapter(ServiceHistoryActivity.this,
                    getApplicationContext(), new ServiceHistoryRowPresenter(), mServiceHistoryList, mVehicleSpinnerPosition);
            recyclerViewHistorylist.setAdapter(mServiceHistoryAdapter);

        } catch (Exception e) {
            RELog.e(e);
        }
    }

    /**
     * Sorting the service history list by descending order...based on the InvoiceDate
     * InvoiceDate format is "2020-01-27 00:00:00.0"
     *
     * @param mServiceHistoryList
     * @return
     */
    public static ArrayList<ServiceHistoryResponse> getSortedServiceHistoryList(ArrayList<ServiceHistoryResponse> mServiceHistoryList) {
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

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onServiceHistorySuccess() {
        initRecyclerView();
        hideLoading();
    }

    @Override
    public void onServiceHistoryFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialog(this, errorMessage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
