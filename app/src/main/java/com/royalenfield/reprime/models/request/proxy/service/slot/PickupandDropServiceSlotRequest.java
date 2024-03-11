package com.royalenfield.reprime.models.request.proxy.service.slot;

public class PickupandDropServiceSlotRequest {

    private String dealerId;

    public PickupandDropServiceSlotRequest(String dealerid){
        this.dealerId = dealerid;
    }
    public String getDealerID() {
        return dealerId;
    }

    public void setDealerID(String dealerID) {
        this.dealerId = dealerID;
    }



}
