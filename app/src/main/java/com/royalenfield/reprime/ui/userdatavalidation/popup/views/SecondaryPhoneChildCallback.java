package com.royalenfield.reprime.ui.userdatavalidation.popup.views;

interface SecondaryPhoneChildCallback {
    void OnSecondaryPhoneCheckChange(int mIndex, boolean isChecked);

    void saveSecondaryAsPrimary(int index, String phoneNumber, boolean checked);
}
