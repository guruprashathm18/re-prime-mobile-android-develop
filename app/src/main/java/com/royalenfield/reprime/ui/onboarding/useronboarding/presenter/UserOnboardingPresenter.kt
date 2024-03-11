package com.royalenfield.reprime.ui.onboarding.useronboarding.presenter

import android.os.Bundle
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.models.request.web.otp.OtpRequestModel
import com.royalenfield.reprime.models.request.web.otp.OtpVerifyRequestModel
import com.royalenfield.reprime.models.request.web.signup.SignupRequest
import com.royalenfield.reprime.models.response.web.signup.RequestConsent
import com.royalenfield.reprime.ui.onboarding.LogResponse
import com.royalenfield.reprime.ui.onboarding.useronboarding.interactor.UserOnboardingInteractor
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnConsentUpdateListener
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnOTPResponseListener
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnOTPVerificationListener
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnRegisterListener
import com.royalenfield.reprime.ui.onboarding.useronboarding.views.UserOnboardingView
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.REUtils

private var userOnbordingView:UserOnboardingView?= null
private var userOnboardingInteractor:UserOnboardingInteractor?=null

class UserOnboardingPresenter(mUserOnbordingView: UserOnboardingView, mUserOnboardingInteractor: UserOnboardingInteractor) : IUserOnboardingPresenter,OnOTPResponseListener,OnOTPVerificationListener,OnRegisterListener,OnConsentUpdateListener {
    init {
        userOnbordingView = mUserOnbordingView
        userOnboardingInteractor = mUserOnboardingInteractor
    }


    override fun sendOTP(callingCode: String, mobileNumber: String) {
            userOnbordingView?.showLoading()
            userOnboardingInteractor?.requestForOtp(OtpRequestModel(mobileNumber,callingCode,30,0,""),this)


    }

    override fun verifyOTP(credential: OtpVerifyRequestModel) {
        userOnbordingView?.showLoading()
        userOnboardingInteractor?.verifyOtp(credential,this)

    }

    override fun signup(request: SignupRequest) {
        userOnbordingView?.showLoading()
        userOnboardingInteractor?.signup(request,this)
    }

    override fun consentUpdate(request: RequestConsent) {
        userOnbordingView?.showLoading()
        userOnboardingInteractor?.updateConsent(request,this)
    }

    override fun onSuccess(reqId:String?) {
        if (userOnbordingView != null) {
            userOnbordingView?.onOTPSuccess(reqId)
            userOnbordingView?.hideLoading()
        }
    }

    override fun onOTPVerificationFailure(errorCode: Int, errorMsg: String,reqId:String?) {

        if (userOnbordingView != null) {
            userOnbordingView?.hideLoading()

            when (errorCode) {
                404 ->  userOnbordingView?.userNotExist(reqId)
                419 ->  userOnbordingView?.otpInvalidInline(errorMsg)
                413 ->  userOnbordingView?.otpVerifyFailureInline(errorMsg)
                else ->  userOnbordingView?.otpVerifyFailure(errorMsg)
            }
        }
    }

    override fun onOTPVerificationSuccess(requestId:String?) {
        val params: Bundle =  Bundle()
        params.putString("eventCategory", "OTP");
        params.putString("eventAction", "Continue click");
        params.putString("eventLabel", "success");
        REUtils.logGTMEvent(REConstants.KEY_OTP_GTM,params);
        if (userOnbordingView != null) {
            userOnbordingView?.hideLoading()
            userOnbordingView?.loginSuccess(requestId)

        }
    }

    override fun onFailure(eroCode:Int,errorMsg: String) {
        if (userOnbordingView != null) {
            userOnbordingView?.hideLoading()

            when (eroCode) {
                403 ->  userOnbordingView?.otpLimitExceed(errorMsg)
                else ->  userOnbordingView?.onOTPFailure(errorMsg)
            }
        }

    }

    override fun onSignupSuccess(reqId:String?) {

        if (userOnbordingView != null) {
            userOnbordingView?.hideLoading()
            userOnbordingView?.signupSuccess(reqId)
        }
    }

    override fun onSignupFailure(errorCode: Int?, errorMsg: String?) {
        if (userOnbordingView != null) {
            userOnbordingView?.hideLoading()
            when (errorCode) {
                204-> userOnbordingView?.emailOrMobileExist(errorMsg)
                414 ->  userOnbordingView?.otpExpired(errorMsg)
                else ->  userOnbordingView?.signupFailed(errorMsg)
            }

        }
    }

    override fun onConsentSuccess(msg: String?) {
        if (userOnbordingView != null) {
            userOnbordingView?.hideLoading()
            userOnbordingView?.consentSuccess(msg)
        }
    }

    override fun onConsentFailure(errorMsg: String?) {
        if (userOnbordingView != null) {
            userOnbordingView?.hideLoading()
            userOnbordingView?.consentFailure(errorMsg)
        }
    }
}