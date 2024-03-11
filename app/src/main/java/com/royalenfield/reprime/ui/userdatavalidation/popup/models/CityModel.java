package com.royalenfield.reprime.ui.userdatavalidation.popup.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CityModel implements Parcelable {

    private String mCityName;
    private int mSelected;
    private String mID;

    public CityModel(String cityName) {
        mCityName = cityName;
        mSelected = 0;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    protected CityModel(Parcel in) {
        mCityName = in.readString();
        mSelected = in.readInt();
        mID = in.readString();
    }

    public static final Creator<CityModel> CREATOR = new Creator<CityModel>() {
        @Override
        public CityModel createFromParcel(Parcel in) {
            return new CityModel(in);
        }

        @Override
        public CityModel[] newArray(int size) {
            return new CityModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mCityName);
        parcel.writeInt(mSelected);
        parcel.writeString(mID);
    }

    public String getCityName() {
        return mCityName;
    }

    public int getSelected() {
        return mSelected;
    }

    public void setSelected(int selected) {
        mSelected = selected;
    }


}
