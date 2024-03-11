package com.royalenfield.reprime.ui.userdatavalidation.otp.otpinteractor;

import android.accounts.NetworkErrorException;

import androidx.annotation.NonNull;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.otp.SendOtpRequest;
import com.royalenfield.reprime.models.response.web.login.UpdatePersonalDetailResponse;
import com.royalenfield.reprime.models.response.web.otp.OtpResponse;
import com.royalenfield.reprime.ui.forgot.listeners.OnSendOtpResponseListener;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.userdatavalidation.otp.presenter.OtpPresenter;
import com.royalenfield.reprime.ui.userdatavalidation.otp.views.OnSubmitOtpListener;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Response;

public class OtpInteractor implements IOtpInteractor {

	private static final String TAG = OtpInteractor.class.getSimpleName();
	private OtpPresenter mListener;

	public OtpInteractor() {
	}

	@Override
	public void requestForOtp(SendOtpRequest req, OnSendOtpResponseListener listener) {

		REApplication
				.getInstance()
				.getREWebsiteApiInstance()
				.getWebsiteAPI()
				.requestOtp(req)
				.enqueue(new BaseCallback<OtpResponse>() {
					@Override
					public void onResponse(@NotNull Call<OtpResponse> call, @NotNull Response<OtpResponse> response) {
						super.onResponse(call, response);
						RELog.d(TAG, "OTP response code : " + response.code());

						if (response.isSuccessful() && response.body() != null) {
							if (response.body().getSuccess()) {
								RELog.d(TAG, "OTP response code  : " + response.code() + " : " + response.body().getMessage());

								listener.onSuccess();
							} else {
								listener.onFailure(response.body().getMessage());
							}
						} else {
							RELog.d(TAG, "OTP response code  : " + response.code() + " : " + response.message());

							listener.onFailure(response.code() + " : " + response.message());
						}
					}

					@Override
					public void onFailure(@NotNull Call<OtpResponse> call, @NotNull Throwable t) {
						super.onFailure(call, t);
						RELog.d(TAG, "OTP response code failure : " + t.getMessage());
						if (t instanceof NetworkErrorException) {
							listener.onFailure(REApplication.getAppContext().
									getResources().getString(R.string.network_failure));
						} else {
							listener.onFailure(t.getMessage());
						}
					}
				});
	}

	@Override
	public void submitOtp(String fName, String lName, String secondaryPhone, String otp, String callingCode, String email, OnSubmitOtpListener listener) {

		REApplication
				.getInstance()
				.getREWebsiteApiInstance()
				.getWebsiteAPI()
				.updatePersonalDetail(fName, lName, secondaryPhone, callingCode, email, otp)
				.enqueue(new BaseCallback<UpdatePersonalDetailResponse>() {
					@Override
					public void onResponse(@NonNull Call<UpdatePersonalDetailResponse> call, @NonNull Response<UpdatePersonalDetailResponse> response) {
						super.onResponse(call, response);
						if (response.isSuccessful() && response.body() != null) {
							if (response.body().getSuccess() && response.body().
									getCode() == REConstants.API_SUCCESS_CODE) {

								if (response.body().getSuccess() && response.body().getCode() == REConstants.API_SUCCESS_CODE) {

									listener.onSubmitSuccess();
								} else {
									listener.onSubmitFail(response.body().getMessage());
								}
							} else {
								// error case
								listener.onSubmitFail(response.body().getMessage());
							}
						}
					}

					@Override
					public void onFailure(@NonNull Call<UpdatePersonalDetailResponse> call, @NonNull Throwable t) {
						super.onFailure(call, t);
						listener.onSubmitFail(REUtils.getErrorMessageFromCode(0));
					}
				});
	}

}
