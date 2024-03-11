package com.royalenfield.reprime.ui.barcode.interactor;

import android.accounts.NetworkErrorException;

import com.google.gson.JsonObject;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.otp.OtpResponse;
import com.royalenfield.reprime.models.response.web.qr.QrResponse;
import com.royalenfield.reprime.models.response.web.qr.RequestQR;
import com.royalenfield.reprime.ui.barcode.listeners.OnBarcodeResponseListener;
import com.royalenfield.reprime.ui.forgot.listeners.OnSendOtpResponseListener;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Response;
import com.royalenfield.reprime.utils.RELog;

/**
 * @author BOP1KOR on 12/19/2018.
 * <p>
 * Class handles to do api calls to interact with the RE server to generate the otp.
 */

public class BarcodeReaderInteractor implements IBarcodeReaderInteractor {
    private static final String TAG = BarcodeReaderInteractor.class.getSimpleName();



    @Override
    public void checkBarcode(String barCode, final OnBarcodeResponseListener onBarcodeResponseListener) {
RequestQR request=new RequestQR();
request.setAppId(REConstants.APP_ID+"");
request.setCode(barCode);
request.setGuid(REUserModelStore.getInstance().getUserId());
request.setGpslocation(REUserModelStore.getInstance().getLatitude()+","+REUserModelStore.getInstance().getLongitude());
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .getBarcodeData(request)
                .enqueue(new BaseCallback<QrResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<QrResponse> call, @NotNull Response<QrResponse> response) {
                        super.onResponse(call, response);
                        RELog.d(TAG, "response code : "+ response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            if(response.body().getCode().equalsIgnoreCase("200"))
                            onBarcodeResponseListener.onSuccess(response.body().getData());
                            else
                                onBarcodeResponseListener.onFailure(response.body().getErrorMessage());
                        } else {
                           onBarcodeResponseListener.onFailure("");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<QrResponse> call, @NotNull Throwable t) {
                        super.onFailure(call, t);
                        RELog.d(TAG, "Forgot password otp send failure : "+ t.getMessage());
                        if (t instanceof NetworkErrorException) {
                            onBarcodeResponseListener.onFailure(REApplication.getAppContext().
                                    getResources().getString(R.string.network_failure));
                        } else {
                            onBarcodeResponseListener.onFailure("");
                        }
                    }
                });

    }
}
