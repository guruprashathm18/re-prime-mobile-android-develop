package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.presenter;

import android.net.Uri;

import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;

import java.io.File;

public interface IUploadDocPresenter {

    void validateOwnershipDoc(String fileName, long fileSize, String fileType, Uri filePath);
    void submitDocs(File ownerFile, AddVehicleResponse addVehicleResponse);


}
