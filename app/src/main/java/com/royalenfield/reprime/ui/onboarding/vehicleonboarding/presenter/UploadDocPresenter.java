package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.presenter;

import android.net.Uri;

import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.interactor.UploadDocInteractor;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.listeners.OnDocsSubmitListener;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.views.UploadDocumentView;

import java.io.File;


public class UploadDocPresenter implements IUploadDocPresenter, OnDocsSubmitListener {

    private static final String TAG = UploadDocPresenter.class.getSimpleName();
    private UploadDocumentView uploadDocumentView;
    private UploadDocInteractor uploadDocInteractor;
    private static final long FILE_SIZE_LIMIT = 3000000;

    public UploadDocPresenter(UploadDocumentView uploadDocumentView, UploadDocInteractor uploadDocInteractor) {
        this.uploadDocumentView = uploadDocumentView;
        this.uploadDocInteractor = uploadDocInteractor;
    }


    @Override
    public void validateOwnershipDoc(String fileName, long fileSize, String fileType, Uri filePath) {

        if(fileSize>FILE_SIZE_LIMIT){
            uploadDocumentView.onOwnershipFileSizeError();
        }else {
            if (fileType.equalsIgnoreCase("image/jpeg")){
                uploadDocumentView.onSuccessOwnDoc(filePath,fileName,true);
            }else if(fileType.equalsIgnoreCase("application/pdf")) {
                uploadDocumentView.onSuccessOwnDoc(filePath,fileName,false);
            }else {
                uploadDocumentView.onOwnershipFileFormatError();
            }
        }
    }



    @Override
    public void submitDocs(File ownerFile, AddVehicleResponse addVehicleResponse) {
        uploadDocInteractor.uploadDocs(ownerFile,this,addVehicleResponse);

    }

    @Override
    public void onSuccess() {
        uploadDocumentView.onSuccessResponse();
    }

    @Override
    public void onFailure(String errorMessage) {
        uploadDocumentView.onFailureResponse(errorMessage);
    }
}
