package com.royalenfield.reprime.ui.onboarding.useronboarding.views

import com.royalenfield.reprime.base.REMvpView

interface UserOnboardingView: REMvpView
{
    fun invalidMobileNumber()
    fun onOTPSuccess(reqId: String?)
    fun onOTPFailure(errorMsg:String)
    fun loginSuccess(requestID:String?)
    fun userNotExist(requestID: String?)
    fun otpVerifyFailure(errorMsg:String)
    fun otpVerifyFailureInline(errorMsg:String)
    fun otpInvalidInline(errorMsg:String)
    fun otpLimitExceed(errorMsg:String)
    fun otpExpired(errorMsg:String?)
    fun signupSuccess(reqId:String?)
    fun signupFailed(errorMsg:String?)
    fun emailOrMobileExist(errorMsg:String?)
    fun consentSuccess(message:String?)
    fun consentFailure(error:String?)
}