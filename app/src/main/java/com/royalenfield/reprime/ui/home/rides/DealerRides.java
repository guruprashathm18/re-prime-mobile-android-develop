package com.royalenfield.reprime.ui.home.rides;

/**
 * @author BOP1KOR on 3/26/2019.
 */
public class DealerRides {

    private String dealerName;
    private String dealerPictureUrl;
    private String dealerRideName;
    private String dealerRideLocation;

    public DealerRides(String dealerName, String dealerPictureUrl, String dealerRideName, String dealerRideLocation) {
        this.dealerName = dealerName;
        this.dealerPictureUrl = dealerPictureUrl;
        this.dealerRideName = dealerRideName;
        this.dealerRideLocation = dealerRideLocation;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerPictureUrl() {
        return dealerPictureUrl;
    }

    public void setDealerPictureUrl(String dealerPictureUrl) {
        this.dealerPictureUrl = dealerPictureUrl;
    }

    public String getDealerRidename() {
        return dealerRideName;
    }

    public void setDealerRidename(String dealerRideName) {
        this.dealerRideName = dealerRideName;
    }

    public String getDealerRideLocation() {
        return dealerRideLocation;
    }

    public void setDealerRideLocation(String dealerRideLocation) {
        this.dealerRideLocation = dealerRideLocation;
    }
}
