package com.royalenfield.reprime.ui.userdatavalidation.popup.views;

import android.os.Parcel;
import android.os.Parcelable;

public class MasterDataModel implements Parcelable {

    private String value;
    private boolean isSelected;
    private String masterDataType;


    private MasterDataModel(Parcel in) {
        value = in.readString();
        isSelected = in.readByte() != 0;
        masterDataType = in.readString();
    }

    public static final Creator<MasterDataModel> CREATOR = new Creator<MasterDataModel>() {
        @Override
        public MasterDataModel createFromParcel(Parcel in) {
            return new MasterDataModel(in);
        }

        @Override
        public MasterDataModel[] newArray(int size) {
            return new MasterDataModel[size];
        }
    };

    public MasterDataModel(String value, boolean isSelected, String masterDataType) {
        this.value = value;
        this.isSelected = isSelected;
        this.masterDataType = masterDataType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(value);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
        parcel.writeString(masterDataType);
    }

    public String getValue() {
        return value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getMasterDataType() {
        return masterDataType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
