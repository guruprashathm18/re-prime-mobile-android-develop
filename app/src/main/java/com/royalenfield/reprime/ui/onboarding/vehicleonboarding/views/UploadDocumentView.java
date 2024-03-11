package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.views;

import android.net.Uri;

import com.royalenfield.reprime.base.REMvpView;


public interface UploadDocumentView  {

    void onOwnershipFileFormatError();

    void onSuccessOwnDoc(Uri path, String fileName, boolean flag);

    void onOwnershipFileSizeError();

    void onFailure(String errorMessage);
    void onSuccessResponse();
    void onFailureResponse(String message);


}
