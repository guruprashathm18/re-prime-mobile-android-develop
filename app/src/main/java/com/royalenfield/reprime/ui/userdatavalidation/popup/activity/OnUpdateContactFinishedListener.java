package com.royalenfield.reprime.ui.userdatavalidation.popup.activity;

import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.UpdateContactRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.AddAddressResponseModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.AddContactResponseModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.UpdateContactResponseModel;

public interface OnUpdateContactFinishedListener {
    void onUpdateContactSuccess(UpdateContactResponseModel body, String updateType, UpdateContactRequestModel updateContactRequestModel);

    void onUpdateContactFail(String messageId, String type);

    void onUpdateAddressSuccess(UpdateAddressResponseModel body, String type, UpdateAddressRequestModel addressRequestModel);

    void onUpdateAddressFail(String message, String type);

    void onDeleteSecondarySuccess();

    void onDeleteSecondaryFail();

    void onSwapApprovalSuccess();

    void onSwapApprovalFail(String message);
}
