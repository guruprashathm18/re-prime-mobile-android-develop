package com.royalenfield.reprime.ui.home.homescreen.presenter;

import android.net.Uri;

import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.views.UploadDocumentView;

import java.io.File;

public interface IUploadKYCPresenter {

    void validateOwnershipDoc(String fileName, long fileSize, String fileType, Uri filePath);
    void submitDocs(File ownerFile,String chesis);


}
