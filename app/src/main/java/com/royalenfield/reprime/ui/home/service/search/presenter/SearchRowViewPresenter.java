package com.royalenfield.reprime.ui.home.service.search.presenter;

import android.graphics.drawable.Drawable;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.ui.home.service.search.interactor.ISearchRowViewpresenter;
import com.royalenfield.reprime.ui.home.service.search.view.DealersListRowView;

import java.util.ArrayList;
import java.util.List;

public class SearchRowViewPresenter implements ISearchRowViewpresenter {


    @Override
    public void onBindDealersListRowViewAtPosition(int position, DealersListRowView rowView, List<DealerMasterResponse> mDealersResponse, boolean filterFlag) {
        if (mDealersResponse != null && mDealersResponse.size() > 0) {
            DealerMasterResponse mDealersResponseList = mDealersResponse.get(position);
            rowView.setDealersName(mDealersResponseList.getDealerName());
            rowView.pickupInfo("No Pickup Info");
            // rowView.pickupInfo(mDealersResponseList.getIsPickandDropAvailbale());
            rowView.setDistance((String.valueOf(mDealersResponseList.getDistance())) + "km");
            if (filterFlag) {
                Drawable drawable = REApplication.getInstance().getDrawable(R.drawable.location);
                rowView.setImage(drawable);
            } else {
                Drawable drawable = REApplication.getInstance().getDrawable(R.drawable.shape);
                rowView.setImage(drawable);
            }
        }
    }

    @Override
    public int getDealersListRowsCount(List<DealerMasterResponse> mDealersResponseList) {
        if (mDealersResponseList != null) {
            return mDealersResponseList.size();
        } else {
            return 0;
        }
    }

    @Override
    public List<DealerMasterResponse> getFilteredResults(String charString, List<DealerMasterResponse> dealersList) {
        List<DealerMasterResponse> filteredResultList = new ArrayList<DealerMasterResponse>();
        for (DealerMasterResponse response : dealersList) {
//            if (response.getDealername() != null && response.getAddressInfo() != null) {
            // TODO Removed address check as there is no field for address in firebase dealer response
            if (response.getDealerName() != null && response.getCity() != null) {
//                if (response.getDealername().toLowerCase().startsWith(charString.toLowerCase()) || response.getAddressInfo().toString().toLowerCase().contains(charString.toLowerCase())) {
                if (response.getDealerName().toLowerCase().contains(charString.toLowerCase()) ||
                        response.getCity().toLowerCase().contains(charString.toLowerCase())) {
                    filteredResultList.add(response);
                }
            } else if (response.getDealerName() != null) {
                if (response.getDealerName().toLowerCase().contains(charString.toLowerCase())) {
                    filteredResultList.add(response);
                }
            } else if (response.getCity() != null) {
                if (response.getCity().toLowerCase().contains(charString.toLowerCase())) {
                    filteredResultList.add(response);
                }
            }
        }
        return filteredResultList;
    }


}
