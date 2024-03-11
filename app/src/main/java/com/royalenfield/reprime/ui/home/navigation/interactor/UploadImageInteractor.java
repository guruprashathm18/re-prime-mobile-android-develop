package com.royalenfield.reprime.ui.home.navigation.interactor;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.web.navigation.ImageUploadResponse;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageInteractor implements IUploadImageInteractor {

    private String TAG = UploadImageInteractor.class.getSimpleName();


    public void uploadImage(List<String> fileList,
                            final RidesListeners.NavigationImageUploadListener finishedListener, String guid, String uuid) {

        String folderName= guid+"/"+uuid;



        MultipartBody.Builder multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("containerName", "re-prime-bct-images")
                .addFormDataPart("dirName", folderName);

        //Add images in createride api
        int imageNumer =0;
        String dateString=getData_yyyy_MM_dd_hh_mm_ss();
        for (String filePath : fileList) {
            String imageName="IMG_"+imageNumer+"_BCT_"+dateString+".jpg";
            imageNumer++;
            if (filePath != null && !filePath.isEmpty()) {
                File file = new File(filePath);
                // Create a request body with file and image media type
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
             //   multipartBody.addFormDataPart("files", filePath, fileReqBody);
                multipartBody.addFormDataPart("files", imageName, fileReqBody);

            }
        }

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
                                finishedListener.onSuccess(response.body().getData().getUriList());
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

    public static String getData_yyyy_MM_dd_hh_mm_ss() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd-HH_mm_ss_SSS");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        RELog.e("date", "getData_yyyy_MM_dd_hh_mm_ss = " + str);
        return str;
    }

}
