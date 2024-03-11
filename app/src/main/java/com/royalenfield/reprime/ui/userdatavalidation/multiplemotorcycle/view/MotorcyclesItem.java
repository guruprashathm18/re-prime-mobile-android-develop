package com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.view;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class MotorcyclesItem implements Parcelable, Cloneable {

    private String mEngineNum;
    private String mRegistrationNumber;
    private boolean mIsNotOwn;
    private boolean mIsError;
    private String mErrorMessage;
    private String mModelCode;
    private String mModelName;
    private String mChassisNumber;
    private String mImageUrl;


    public MotorcyclesItem() {
    }


    protected MotorcyclesItem(Parcel in) {
        mEngineNum = in.readString();
        mRegistrationNumber = in.readString();
        mIsNotOwn = in.readByte() != 0;
        mIsError = in.readByte() != 0;
        mErrorMessage = in.readString();
        mModelCode = in.readString();
        mModelName = in.readString();
        mChassisNumber=in.readString();
        mImageUrl = in.readString();
    }

    public static final Creator<MotorcyclesItem> CREATOR = new Creator<MotorcyclesItem>() {
        @Override
        public MotorcyclesItem createFromParcel(Parcel in) {
            return new MotorcyclesItem(in);
        }

        @Override
        public MotorcyclesItem[] newArray(int size) {
            return new MotorcyclesItem[size];
        }
    };

    public void setModelCode(String modelCode) {

        this.mModelCode = modelCode;
    }

    public void setModelName(String modelName) {
        this.mModelName = modelName;
    }

    public String getModelCode() {
        return mModelCode;
    }

    public String getmEngineNum() {
        return mEngineNum;
    }

    public boolean ismIsNotOwn() {
        return mIsNotOwn;
    }

    public String getmRegistrationNumber() {
        return mRegistrationNumber;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setEngineNum(String mEngineNum) {
        this.mEngineNum = mEngineNum;
    }

    public void setRegistrationNumber(String mRegistrationNumber) {
        this.mRegistrationNumber = mRegistrationNumber;
    }

    public void setIsNotOwn(boolean mIsOwn) {
        this.mIsNotOwn = mIsOwn;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setError(boolean isError) {
        mIsError = isError;
    }

    public void setErrorMessage(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
    }

    public String getModelName() {
        return mModelName;
    }

    public boolean isError() {
        return mIsError;
    }

    public String getmChassisNumber() {
        return mChassisNumber;
    }

    public void setChasisNumber(String chaisisNo) {
        mChassisNumber = chaisisNo;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Override
    public Object clone() {
        try {
            return(MotorcyclesItem) super.clone();
        } catch (CloneNotSupportedException ex) {
            return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mEngineNum);
        parcel.writeString(mRegistrationNumber);
        parcel.writeByte((byte) (mIsNotOwn ? 1 : 0));
        parcel.writeByte((byte) (mIsError ? 1 : 0));
        parcel.writeString(mErrorMessage);
        parcel.writeString(mModelCode);
        parcel.writeString(mModelName);
        parcel.writeString(mChassisNumber);
        parcel.writeString(mImageUrl);
    }


    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        MotorcyclesItem motorCycleObj = (MotorcyclesItem)obj;
        return mModelCode.equals(motorCycleObj.mModelCode)
                && mEngineNum.equals(motorCycleObj.mEngineNum)
                && mRegistrationNumber.equals(motorCycleObj.mRegistrationNumber)&& mChassisNumber.equals(motorCycleObj.mChassisNumber)
                && mIsNotOwn==motorCycleObj.mIsNotOwn && mModelName.equals(motorCycleObj.mModelName);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result ;
        result = prime * result
                + ((mEngineNum == null) ? 0 :   mEngineNum.hashCode());
        result = prime * result
                + ((mRegistrationNumber == null) ? 0 : mRegistrationNumber.hashCode());
        return result;
    }
}
