package com.royalenfield.reprime.ui.home.homescreen.presenter;

import android.net.Uri;

import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;
import com.royalenfield.reprime.ui.home.homescreen.interactor.UploadKYCInteractor;
import com.royalenfield.reprime.ui.home.homescreen.listener.KYCUploadListener;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.interactor.UploadDocInteractor;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.listeners.OnDocsSubmitListener;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.presenter.IUploadDocPresenter;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.views.UploadDocumentView;

import java.io.File;


public class UploadKYCPresenter implements IUploadKYCPresenter, KYCUploadListener {

    private static final String TAG = UploadKYCPresenter.class.getSimpleName();
    private UploadDocumentView uploadDocumentView;
    private UploadKYCInteractor uploadDocInteractor;
    private static final long FILE_SIZE_LIMIT = 5000000;

    public UploadKYCPresenter(UploadDocumentView uploadDocumentView, UploadKYCInteractor uploadDocInteractor) {
        this.uploadDocumentView = uploadDocumentView;
        this.uploadDocInteractor = uploadDocInteractor;
    }


    @Override
    public void validateOwnershipDoc(String fileName, long fileSize, String fileType, Uri filePath) {

        if(fileSize>FILE_SIZE_LIMIT){
            uploadDocumentView.onOwnershipFileSizeError();
        }else {
            if (fileType.equalsIgnoreCase("image/jpeg")||fileType.equalsIgnoreCase("image/png")){
                uploadDocumentView.onSuccessOwnDoc(filePath,fileName,true);
            }

			else if(fileType.equalsIgnoreCase("application/pdf")) {
                uploadDocumentView.onSuccessOwnDoc(filePath,fileName,false);
            }else {
                uploadDocumentView.onOwnershipFileFormatError();
            }
        }
    }



    @Override
    public void submitDocs(File ownerFile, String chessis) {
        uploadDocInteractor.uploadKYC(ownerFile,chessis,this);

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
