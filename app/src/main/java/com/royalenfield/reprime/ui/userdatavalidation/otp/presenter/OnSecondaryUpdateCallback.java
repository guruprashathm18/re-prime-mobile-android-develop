package com.royalenfield.reprime.ui.userdatavalidation.otp.presenter;

import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.UpdateContactResponseModel;

public interface OnSecondaryUpdateCallback {
    void onUpdateContactSuccess(String secondaryPhone, String callingCode);

    void onUpdateContactFail(String message);
}
