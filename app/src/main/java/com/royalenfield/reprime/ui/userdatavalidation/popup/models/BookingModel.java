package com.royalenfield.reprime.ui.userdatavalidation.popup.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingModel implements Parcelable, Cloneable {


    @SerializedName("transId")
    @Expose
    private String transId;
    @SerializedName("bookingStatus")
    @Expose
    private String bookingStatus;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("showInCountryList")
    @Expose
    private Boolean showInCountryList;
    @SerializedName("status")
    @Expose
    private String status;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public Boolean getShowInCountryList() {
        return showInCountryList;
    }

    public void setShowInCountryList(Boolean showInCountryList) {
        this.showInCountryList = showInCountryList;
    }

    private int mIsSelected = 0;


    protected BookingModel(Parcel in) {
        transId = in.readString();
        description = in.readString();
        bookingStatus = in.readString();
        name=in.readString();
        mIsSelected = in.readInt();
        status = in.readString();
        showInCountryList = in.readInt() == 1;
    }

    public static final Creator<BookingModel> CREATOR = new Creator<BookingModel>() {
        @Override
        public BookingModel createFromParcel(Parcel in) {
            return new BookingModel(in);
        }

        @Override
        public BookingModel[] newArray(int size) {
            return new BookingModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(transId);
        parcel.writeString(description);
        parcel.writeString(name);
        parcel.writeString(bookingStatus);
        parcel.writeString(status);
        parcel.writeInt(mIsSelected);
        parcel.writeInt(showInCountryList ? 1 : 0);
    }

    @Override
    public BookingModel clone(){
        try {

            return(BookingModel) super.clone();
        } catch (CloneNotSupportedException ex) {
            final BookingModel o = null;
            return o;
        }
    }


    public int isSelected() {
        return mIsSelected;
    }

    public void setSelected(int selected) {
        mIsSelected = selected;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
