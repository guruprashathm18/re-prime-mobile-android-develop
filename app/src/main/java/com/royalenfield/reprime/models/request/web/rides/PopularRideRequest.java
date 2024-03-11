package com.royalenfield.reprime.models.request.web.rides;

public class PopularRideRequest {

    private String lastpulldate;

    private String rideCode;

    public PopularRideRequest(String lastpulldate, String branchcode) {
        this.lastpulldate = lastpulldate;
        this.rideCode = branchcode;
    }


    public String getLastpulldate() {
        return lastpulldate;
    }

    public void setLastpulldate(String lastpulldate) {
        this.lastpulldate = lastpulldate;
    }

    public String getBranchcode() {
        return rideCode;
    }

    public void setBranchcode(String branchcode) {
        this.rideCode = branchcode;
    }

}
