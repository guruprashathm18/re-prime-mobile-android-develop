package com.royalenfield.reprime.models.request.web.ourworld;

/**
 * @author BOP1KOR on 4/1/2019.
 * <p>
 * Dealer Ride Request class for the
 */
public class OurWorldNewsRequest {

    private String lastpulldate;

    private String rideCode;

    public OurWorldNewsRequest(String lastpulldate, String branchcode) {
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
