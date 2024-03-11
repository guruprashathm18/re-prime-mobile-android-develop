package com.royalenfield.reprime.ui.userdatavalidation.popup.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryModel implements Parcelable, Cloneable {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("diallingcode")
    @Expose
    private String diallingcode;
    @SerializedName("showInCountryList")
    @Expose
    private Boolean showInCountryList;
    @SerializedName("validations")
    @Expose
    private Validations validations;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiallingcode() {
        return diallingcode;
    }

    public void setDiallingcode(String diallingcode) {
        this.diallingcode = diallingcode;
    }

    public Boolean getShowInCountryList() {
        return showInCountryList;
    }

    public void setShowInCountryList(Boolean showInCountryList) {
        this.showInCountryList = showInCountryList;
    }

    private int mIsSelected = 0;

    public CountryModel(String description, String dialingCode, String code, boolean toShow) {
        this.diallingcode = dialingCode;
        this.description = description;
        this.code = code;
        this.showInCountryList = toShow;
    }

    protected CountryModel(Parcel in) {
        diallingcode = in.readString();
        description = in.readString();
        code = in.readString();
        mIsSelected = in.readInt();
        showInCountryList = in.readInt() == 1;
    }

    public static final Creator<CountryModel> CREATOR = new Creator<CountryModel>() {
        @Override
        public CountryModel createFromParcel(Parcel in) {
            return new CountryModel(in);
        }

        @Override
        public CountryModel[] newArray(int size) {
            return new CountryModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(diallingcode);
        parcel.writeString(description);
        parcel.writeString(code);
        parcel.writeInt(mIsSelected);
        parcel.writeInt(showInCountryList ? 1 : 0);
    }

    @Override
    public CountryModel clone(){
        try {

            return(CountryModel) super.clone();
        } catch (CloneNotSupportedException ex) {
            final CountryModel o = null;
            return o;
        }
    }


    public int isSelected() {
        return mIsSelected;
    }

    public void setSelected(int selected) {
        mIsSelected = selected;
    }

    public Validations getValidations() {
        return validations;
    }

    public void setValidations(Validations validations) {
        this.validations = validations;
    }
}
