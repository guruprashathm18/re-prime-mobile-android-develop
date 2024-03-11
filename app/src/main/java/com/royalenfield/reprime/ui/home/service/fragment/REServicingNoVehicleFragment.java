package com.royalenfield.reprime.ui.home.service.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.royalenfield.firebase.realTimeDatabase.FirebaseManager;
import com.royalenfield.firebase.realTimeDatabase.OnFirebaseDealerResponseCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.ui.custom.views.ServiceCenterDataModel;
import com.royalenfield.reprime.ui.home.service.adapter.ServiceCenterDataViewAdapter;
import com.royalenfield.reprime.ui.home.service.asynctask.DealersDistanceCalcAsyncTask;
import com.royalenfield.reprime.ui.home.service.asynctask.ServiceAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.service.listener.RecyclerViewClickListener;
import com.royalenfield.reprime.ui.home.service.search.SearchActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment is loaded if there is no vehicle registered for the user account
 */
public class REServicingNoVehicleFragment extends REBaseFragment implements
        RecyclerViewClickListener, View.OnClickListener, OnFirebaseDealerResponseCallback,
        ServiceAsyncTaskListeners.DealerDistanceCalc {

    private static final int REQUEST_CODE_SEARCH_SELECTED_SERVICE_CENTER = 3;
    private RecyclerView mNovehicleServiceCenterRecyclerView;
    private TextView mNovehicleNearestServiceCenterText, mNovehicleSearchServcieCenterText;
    private View mNoVehicleServiceHistoryLine;
    private ServiceCenterDataViewAdapter mServiceCenterAdapter;
    private ArrayList<ServiceCenterDataModel> mServiceCenterListViewItemList = new ArrayList<>();
    private List<DealerMasterResponse> mDealersResponseList = new ArrayList<>();


    public REServicingNoVehicleFragment() {
        // Required empty public constructor
    }

    public static REServicingNoVehicleFragment newInstance() {
        return new REServicingNoVehicleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servicing_novehicle, container, false);
        initViews(view);
        return view;
    }

    /**
     * Initialising views
     *
     * @param view : View
     */
    private void initViews(View view) {
        mNoVehicleServiceHistoryLine = view.findViewById(R.id.tv_view_service_history_line_novehicle);
        mNovehicleSearchServcieCenterText = view.findViewById(R.id.tv_label_search_service_center_novehicle);
        mNovehicleNearestServiceCenterText = view.findViewById(R.id.tv_label_service_center_novehicle);
        mNovehicleServiceCenterRecyclerView = view.findViewById(R.id.recyclerView_service_center_novehicle);
        mNovehicleServiceCenterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ((SimpleItemAnimator) mNovehicleServiceCenterRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mNovehicleSearchServcieCenterText.setOnClickListener(this);
        mNovehicleSearchServcieCenterText = view.findViewById(R.id.tv_label_search_service_center_novehicle);
        //API call for getting service centers
        searchServiceCenter();
    }

    /**
     * Displays the Nearest Service center for the User
     */
    private void showNearByServiceCenterView() {
        int iServiceType = ServiceCenterDataModel.NEAR_YOU_NO_VEHICLE_TYPE;
        List<DealerMasterResponse> dealersResponseArrayList = new ArrayList<>();
        dealersResponseArrayList.addAll(mDealersResponseList);
        if (dealersResponseArrayList.size() > 2) {
            List<DealerMasterResponse> subDealersList = dealersResponseArrayList.subList(0, 2);
            for (DealerMasterResponse dealersResponse : subDealersList) {
                mServiceCenterListViewItemList.add(new ServiceCenterDataModel(iServiceType,
                        dealersResponse
                ));
            }
        }
        //Setting adapter to recyclerview
        mServiceCenterAdapter = new ServiceCenterDataViewAdapter(getContext(),
                mServiceCenterListViewItemList, this, null);
        // For no vehicle case
        noVehicleServiceCenterLayoutVisible();
        mNovehicleServiceCenterRecyclerView.setAdapter(mServiceCenterAdapter);

    }

    /**
     * Making recyclerview and texts visible
     */
    private void noVehicleServiceCenterLayoutVisible() {
        mNovehicleNearestServiceCenterText.setVisibility(View.VISIBLE);
        mNovehicleServiceCenterRecyclerView.setVisibility(View.VISIBLE);
        mNovehicleSearchServcieCenterText.setVisibility(View.VISIBLE);
        mNovehicleNearestServiceCenterText.setOnClickListener(this);
        mNoVehicleServiceHistoryLine.setVisibility(View.VISIBLE);
    }

    /**
     * API-call to fetch the Last visited service center for the user
     */
    private void searchServiceCenter() {
        showLoading();
        FirebaseManager.getInstance().fetchDealerResponseFromFirebase("", this);
    }


    /**
     * Populates the searched service center from the list.
     *
     * @param searchedFilteredDealerList : ArrayList<DealersResponse>
     */
    private void showSelectedServiceCenter(ArrayList<DealerMasterResponse> searchedFilteredDealerList) {
        if (searchedFilteredDealerList.size() > 0) {
            int iServiceType;
            iServiceType = ServiceCenterDataModel.NEAR_YOU_NO_VEHICLE_TYPE;
            mServiceCenterListViewItemList.clear();
            for (DealerMasterResponse dealersResponse : searchedFilteredDealerList) {
                mServiceCenterListViewItemList.add(new ServiceCenterDataModel(iServiceType,
                        dealersResponse
                ));
            }
            mServiceCenterAdapter = new ServiceCenterDataViewAdapter(getContext(), mServiceCenterListViewItemList,
                    this, null);

            mNovehicleServiceCenterRecyclerView.setAdapter(mServiceCenterAdapter);

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_label_search_service_center_novehicle) {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra(REConstants.SEARCH_ACTIVITY_INPUT_TYPE, REConstants.SEARCH_ACTIVITY_DEALER_LIST);
            startActivityForResult(intent, REQUEST_CODE_SEARCH_SELECTED_SERVICE_CENTER);
            if (getActivity() != null) {
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SEARCH_SELECTED_SERVICE_CENTER && data != null) {
            if (data.getStringExtra("sc_index") != null) {

                ArrayList searchedFilteredDealerList = data.getParcelableArrayListExtra(REConstants.DEALERS_FILTERED_LIST_EXTRA);
                showSelectedServiceCenter(searchedFilteredDealerList);
            }
        }
    }

    @Override
    public void onItemClick(int value, boolean isSelected) {

    }

    @Override
    public void onTimeClick(int position) {

    }

    @Override
    public void onFirebaseDealersListSuccess(List<DealerMasterResponse> dealersResponseArrayList) {
        mDealersResponseList = dealersResponseArrayList;
        // Initiating asyncTask for calculating distance
        DealersDistanceCalcAsyncTask distanceCalcAsyncTask = new DealersDistanceCalcAsyncTask(mDealersResponseList, this);
        distanceCalcAsyncTask.execute();
    }

    @Override
    public void onFirebaseDealerListFailure(String message) {
        hideLoading();
        REUtils.showErrorDialog(getContext(), message);
    }

    @Override
    public void onFirebaseDealerDetailSuccess(DealerMasterResponse dealersDetailResponse) {

    }

    @Override
    public void onFirebaseDealerDetailFailure(String message) {

    }

    @Override
    public void onDealersDistanceCalcComplete(List<DealerMasterResponse> dealersResponseList) {
        mDealersResponseList = dealersResponseList;
        showNearByServiceCenterView();
        hideLoading();
    }
}
