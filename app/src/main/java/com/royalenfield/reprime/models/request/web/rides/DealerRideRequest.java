package com.royalenfield.reprime.models.request.web.rides;

/**
 * @author BOP1KOR on 4/1/2019.
 * <p>
 * Dealer Ride Request class for the
 */
public class DealerRideRequest {

    private String category;

    private int page;

    private String latitude;

    private String longitude;

    public DealerRideRequest(String category, int page, String latitude, String longitude) {
        this.category = category;
        this.page = page;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "DealerRideRequest{" +
                "category='" + category + '\'' +
                ", page=" + page +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
