package com.royalenfield.reprime.ui.userdatavalidation.popup.views;

public class SecondaryPhoneDataModel {
    private final int mIndex;
    private String mPhoneNumber;
    private boolean mChecked;

    public SecondaryPhoneDataModel(String phoneNumber, boolean checked, int index) {
        mPhoneNumber = phoneNumber;
        mChecked = checked;
        mIndex = index;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public int getIndex() {
        return mIndex;
    }
}
