package com.royalenfield.reprime.models.request.web.rides;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WayPointsData implements Parcelable {

    public static final Creator<WayPointsData> CREATOR = new Creator<WayPointsData>() {
        @Override
        public WayPointsData createFromParcel(Parcel in) {
            return new WayPointsData(in);
        }

        @Override
        public WayPointsData[] newArray(int size) {
            return new WayPointsData[size];
        }
    };
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("placeName")
    @Expose
    private String placeName;

    public WayPointsData() {
    }

    public WayPointsData(double latitude, double longitude, String placeName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }

    protected WayPointsData(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        placeName = in.readString();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(placeName);
    }
}
