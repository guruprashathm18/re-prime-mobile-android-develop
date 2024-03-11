package com.royalenfield.reprime.ui.userdatavalidation.popup.models;

import android.os.Parcel;
import android.os.Parcelable;

public class StateModel implements Parcelable {

    private String mCode;
    private String mCountryCode;
    private String mDescription;
    private boolean hidden;
    private int mSelected;



    public StateModel(String code, String countryCode, String description) {
        mCode = code;
        mCountryCode = countryCode;
        mDescription = description;
        mSelected = 0;
    }

    protected StateModel(Parcel in) {
        mCode = in.readString();
        mCountryCode = in.readString();
        mDescription = in.readString();
        mSelected = in.readInt();
        hidden=in.readBoolean();
    }

    public static final Creator<StateModel> CREATOR = new Creator<StateModel>() {
        @Override
        public StateModel createFromParcel(Parcel in) {
            return new StateModel(in);
        }

        @Override
        public StateModel[] newArray(int size) {
            return new StateModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mCode);
        parcel.writeString(mCountryCode);
        parcel.writeString(mDescription);
        parcel.writeInt(mSelected);
        parcel.writeBoolean(hidden);
    }

    public String getCode() {
        return mCode;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getSelected() {
        return mSelected;
    }

    public void setSelected(int selected) {
        mSelected  = selected;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
