package com.royalenfield.reprime.ui.riderprofile.activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.royalenfield.firebase.realTimeDatabase.FirebaseManager;
import com.royalenfield.firebase.realTimeDatabase.OnFirebaseDealerResponseCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.ui.custom.views.ServiceCenterDataModel;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.service.adapter.ServiceCenterDataViewAdapter;
import com.royalenfield.reprime.ui.home.service.asynctask.DealersDistanceCalcAsyncTask;
import com.royalenfield.reprime.ui.home.service.asynctask.ServiceAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.service.listener.RecyclerViewClickListener;
import com.royalenfield.reprime.ui.home.service.search.view.SearchFilterEmptyListListener;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

/**
 * Created  JAL7KOR on 2/21/2019.
 */

public class NearestServiceCentersActivity extends REBaseActivity implements RecyclerViewClickListener,
        TitleBarView.OnNavigationIconClickListener, OnFirebaseDealerResponseCallback,
        SearchFilterEmptyListListener, ServiceAsyncTaskListeners.DealerDistanceCalc {

    private RecyclerView recyclerView;
    private ServiceCenterDataViewAdapter mServiceCenterAdapter;
    private List<DealerMasterResponse> mDealersList;
    private TextView mNoServiceCenterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_servicecenter);
        enableTheLocation();
        initViews();
    }

    /**
     * Initialising views
     */
    private void initViews() {

        TitleBarView mTitleBarView = findViewById(R.id.problem_topbar);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getString(R.string.text_title_servicecenter));
        mNoServiceCenterText = findViewById(R.id.textView_no_servicecenter);
        ImageView imageViewLocation = findViewById(R.id.ivLocation);
        Drawable mImageDisableNextchangeDrawableColor = changeDrawableColor(getApplicationContext(), R.drawable.location,
                getApplicationContext().getResources().getColor(R.color.black));
        imageViewLocation.setImageDrawable(mImageDisableNextchangeDrawableColor);

        recyclerView = findViewById(R.id.recyclerView_nearest);
        EditText filterEditText = findViewById(R.id.et_search);
        bindDataToRecyclerView();

        filterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mServiceCenterAdapter != null) {
                    mServiceCenterAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * Firebase-call to fetch the nearest service center for the user
     */
    public void getServiceCenter() {
        showLoading();
        FirebaseManager.getInstance().fetchDealerResponseFromFirebase("", this);
    }

    private Drawable changeDrawableColor(Context context, int icon, int newColor) {
        try {
            Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
            mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
            return mDrawable;
        } catch (Exception e) {
            RELog.e(e);
        }
        return null;
    }

    /**
     * Populates the searched service center from the list.
     */
    private void showSelectedServiceCenter() {
        ArrayList<ServiceCenterDataModel> mServiceCenterListViewItemList = new ArrayList<>();
        String strPickup = "";

        for (DealerMasterResponse searchdealer : mDealersList) {
            if (searchdealer.getDealerSource() != null &&
                    searchdealer.getDealerSource().equalsIgnoreCase(REConstants.DEALER_SOURCE_ONLINE)) {
                strPickup = "Pick and Drop";
            }

            mServiceCenterListViewItemList.add(new ServiceCenterDataModel(ServiceCenterDataModel.NEAR_YOU_NO_VEHICLE_TYPE,
                    searchdealer
            ));
        }

        mServiceCenterAdapter = new ServiceCenterDataViewAdapter(this, mServiceCenterListViewItemList,
                this, this);
        recyclerView.setAdapter(mServiceCenterAdapter);

    }

    private void bindDataToRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerViewServiceCenter.setItemAnimator(new DefaultItemAnimator());
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public void onItemClick(int value, boolean isSelected) {

    }

    @Override
    public void onTimeClick(int position) {

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

    @Override
    public void onFirebaseDealersListSuccess(List<DealerMasterResponse> dealersResponseArrayList) {
        mDealersList = dealersResponseArrayList;
        // Initiating asyncTask for calculating distance
        DealersDistanceCalcAsyncTask distanceCalcAsyncTask = new DealersDistanceCalcAsyncTask(mDealersList, this);
        distanceCalcAsyncTask.execute();
    }

    @Override
    public void onFirebaseDealerListFailure(String message) {
        hideLoading();
        REUtils.showErrorDialog(this, message);
    }

    @Override
    public void onFirebaseDealerDetailSuccess(DealerMasterResponse dealersDetailResponse) {

    }

    @Override
    public void onFirebaseDealerDetailFailure(String message) {

    }

    @Override
    public void onSearchFilter(boolean isEmpty) {
        if (isEmpty) {
            recyclerView.setVisibility(View.INVISIBLE);
            mNoServiceCenterText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            mNoServiceCenterText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDealersDistanceCalcComplete(List<DealerMasterResponse> dealersResponseList) {
        mDealersList = dealersResponseList;
        // Loads Near by service centers
        showSelectedServiceCenter();
        hideLoading();
    }
}
