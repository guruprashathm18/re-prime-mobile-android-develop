package com.royalenfield.reprime.ui.custom.views;


import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;

public class ServiceCenterDataModel {


    public static final int LAST_VISITED_TYPE = 0;
    public static final int NEAR_YOU_TYPE = 1;
    public static final int SEARCH_SC_TYPE = 2;
    public static final int NEAR_YOU_NO_VEHICLE_TYPE = 3;

    private int viewType;
    private boolean isSelected;

    private DealerMasterResponse dealerMasterResponse;

    public ServiceCenterDataModel(int type, DealerMasterResponse dealerMasterResponse) {
        this.viewType = type;
        this.dealerMasterResponse = dealerMasterResponse;

    }

    public int getViewType() {
        return viewType;
    }


    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public DealerMasterResponse getDealerMasterResponse() {
        return dealerMasterResponse;
    }

    public void setDealerMasterResponse(DealerMasterResponse dealerMasterResponse) {
        this.dealerMasterResponse = dealerMasterResponse;
    }
}