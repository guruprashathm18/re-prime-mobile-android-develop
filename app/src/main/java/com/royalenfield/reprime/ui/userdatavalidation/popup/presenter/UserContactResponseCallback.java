package com.royalenfield.reprime.ui.userdatavalidation.popup.presenter;

import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.ContactData;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.UserContactDetailResponseModel;

public interface UserContactResponseCallback {
    void onUserContactSuccess(ContactData data);

    void onUserContactFail(String message);
}
