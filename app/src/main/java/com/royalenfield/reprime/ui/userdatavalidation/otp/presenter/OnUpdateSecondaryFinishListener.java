package com.royalenfield.reprime.ui.userdatavalidation.otp.presenter;

import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.UpdateContactRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.UpdateContactResponseModel;

public interface OnUpdateSecondaryFinishListener {

    void onUpdateContactSuccess(UpdateContactResponseModel body, String updateType
            , UpdateContactRequestModel updateContactRequestModel, boolean primaryChecked);

    void onUpdateContactFail(String messageId, String type);
}
