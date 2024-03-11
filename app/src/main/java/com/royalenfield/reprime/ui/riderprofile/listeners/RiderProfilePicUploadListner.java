package com.royalenfield.reprime.ui.riderprofile.listeners;

import com.royalenfield.reprime.models.response.web.profile.UploadProfilePic;

public interface RiderProfilePicUploadListner {

    void onUploadPicSuccess(UploadProfilePic mUploadProfilePic);

    void onUploadPicFailure(String failureMsg);
}
