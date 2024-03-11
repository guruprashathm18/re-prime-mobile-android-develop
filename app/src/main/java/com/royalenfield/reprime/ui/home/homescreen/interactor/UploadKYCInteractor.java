package com.royalenfield.reprime.ui.home.homescreen.interactor;

import android.webkit.MimeTypeMap;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.web.navigation.ImageUploadResponse;
import com.royalenfield.reprime.ui.home.homescreen.listener.KYCUploadListener;
import com.royalenfield.reprime.ui.home.navigation.interactor.IUploadImageInteractor;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadKYCInteractor implements IUploadKYCInteractor {

    private String TAG = UploadKYCInteractor.class.getSimpleName();

    @Override
    public void uploadKYC(File file,String chessis, final KYCUploadListener finishedListener) {

        MultipartBody.Builder multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("containerName", "techassistkycdocuments")
                .addFormDataPart("dirName", chessis+"/"+REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId())
				.addFormDataPart("resourceName","remechanicdev");

                // Create a request body with file and image media type
		RequestBody fileReqBody = RequestBody.create(file, MediaType.parse(getMimeType(file.getAbsolutePath())));

		         multipartBody.addFormDataPart("files", file.getName(), fileReqBody);


        RequestBody requestBody = multipartBody.build();

        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .uploadImages(REUtils.getJwtToken(), requestBody)
                .enqueue(new Callback<ImageUploadResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<ImageUploadResponse> call, @NotNull Response<ImageUploadResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (!response.body().isError()) {
                                finishedListener.onSuccess();
                            } else if (response.body().getCode() == String.valueOf(REConstants.API_UNAUTHORIZED)) {
                                REUtils.navigateToLogin();
                            } else {
                                finishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            // error case
                            finishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ImageUploadResponse> call, @NotNull Throwable t) {
                        finishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
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
