package com.royalenfield.reprime.ui.riderprofile.interactor;

import com.royalenfield.reprime.ui.riderprofile.listeners.RiderProfilePicUploadListner;

public interface IRiderProfileUploadInteractor {
    void uploadProfileImage(String userId, String imageData, RiderProfilePicUploadListner miderProfilePicUploadListner);
}
