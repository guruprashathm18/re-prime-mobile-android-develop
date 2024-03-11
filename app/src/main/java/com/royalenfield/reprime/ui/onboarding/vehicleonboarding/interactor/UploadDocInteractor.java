package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.interactor;

import android.util.Log;
import android.webkit.MimeTypeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;

import com.royalenfield.reprime.models.request.web.vehicleonboarding.UploadDocumentRequest;
import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;
import com.royalenfield.reprime.models.response.web.vehicleonboarding.Data;
import com.royalenfield.reprime.models.response.web.vehicleonboarding.UploadVehicleResponse;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.listeners.OnDocsSubmitListener;
import com.royalenfield.reprime.utils.REConstants;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;

public class UploadDocInteractor implements IUploadDocInteractor {

    @Override
    public void uploadDocs(File ownerFile, OnDocsSubmitListener onDocsSubmitListener, AddVehicleResponse addVehicleResponse) {

        MultipartBody.Part ownerDoc = null;
      //  MultipartBody.Part kycDoc = null;

        String jwtToken = "";
        if (REApplication.getInstance().getUserLoginDetails() != null) {
            jwtToken = REApplication.getInstance().getUserTokenDetails();
        }

        String JSON = getJsonString(addVehicleResponse.getData());

        RequestBody description =
                RequestBody.create(
                        JSON, okhttp3.MultipartBody.FORM);
        if (ownerFile!=null){
            RequestBody ownerDocRequestBody = RequestBody.create(ownerFile, MediaType.parse(getMimeType(ownerFile.getAbsolutePath())));
             ownerDoc = MultipartBody.Part.createFormData("ownerDoc", ownerFile.getName(), ownerDocRequestBody);
        }else {
            RequestBody ownerDocRequestBody = RequestBody.create("",MediaType.parse("text/plain"));
            RequestBody kycDocRequestBody = RequestBody.create("",MediaType.parse("text/plain"));
            ownerDoc = MultipartBody.Part.createFormData("ownerDoc", "", ownerDocRequestBody);
         }

        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .uploadVehicleDetail(jwtToken, description, ownerDoc)
                .enqueue(new BaseCallback<UploadVehicleResponse>() {
                    @Override
                    public void onResponse(Call<UploadVehicleResponse> call, Response<UploadVehicleResponse> response) {
                        super.onResponse(call, response);
                        UploadVehicleResponse uploadVehicleResponse = response.body();
                        if (uploadVehicleResponse != null) {
                            if (uploadVehicleResponse.getCode().equalsIgnoreCase("200") && !uploadVehicleResponse.getError()) {
                                onDocsSubmitListener.onSuccess();
                            } else {
                                onDocsSubmitListener.onFailure(uploadVehicleResponse.getErrorMessage()!=null?uploadVehicleResponse.getErrorMessage().toString():"");
                            }
                        } else {
                            onDocsSubmitListener.onFailure("");
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadVehicleResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        onDocsSubmitListener.onFailure(t.getMessage());

                    }
                });


    }

    private String getJsonString(Data data) {
        UploadDocumentRequest uploadDocumentRequest = new UploadDocumentRequest();
        uploadDocumentRequest.setUserName(data.getUserName());
        uploadDocumentRequest.setChassisNo(data.getChassisNo());
        uploadDocumentRequest.setDateOfMfg(data.getDateOfMfg());
        uploadDocumentRequest.setEngineNo(data.getEngineNo());
        uploadDocumentRequest.setId(data.getId());
        uploadDocumentRequest.setPurchaseDate(data.getPurchaseDate());
        uploadDocumentRequest.setMobileNo(data.getMobileNo());
        uploadDocumentRequest.setVehicleModelCode(data.getModelCode());
        uploadDocumentRequest.setRegistrationNo(data.getRegistrationNo());
        uploadDocumentRequest.setVehicleModelName(data.getModelName());
        uploadDocumentRequest.setInvoiceNo(data.getInvoiceNo());
        uploadDocumentRequest.setAlternateNo(data.getAlternateNo());
        uploadDocumentRequest.setAppId(REConstants.APP_ID);
        uploadDocumentRequest.setGuid(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
        uploadDocumentRequest.setLoggedInUserMobileNo( REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone());
        uploadDocumentRequest.setLoggedInUserEmail( REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail());
        if(REApplication.getInstance().getUserLoginDetails().getData().getUser().getLastName()!=null)
        uploadDocumentRequest.setLoggedInUserName( REApplication.getInstance().getUserLoginDetails().getData().getUser().getFirstName()+" "+REApplication.getInstance().getUserLoginDetails().getData().getUser().getLastName());
        else uploadDocumentRequest.setLoggedInUserName( REApplication.getInstance().getUserLoginDetails().getData().getUser().getFirstName());


        String JSON = "";
        // Creating Object of ObjectMapper define in Jakson Api
        ObjectMapper Obj = new ObjectMapper();
        try {
            JSON = Obj.writerWithDefaultPrettyPrinter().writeValueAsString(uploadDocumentRequest);

        } catch (IOException e) {
            RELog.e(e);
        }
        return JSON;
    }

    // url = file path or whatever suitable URL you want.
    private String getMimeType(String url) {
        String type = "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension.length()>0) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }else {
            if(url.lastIndexOf(".") != -1) {
                String ext = url.substring(url.lastIndexOf(".")+1);
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                type = mime.getMimeTypeFromExtension(ext);
            }
        }
        return type;
    }
}
