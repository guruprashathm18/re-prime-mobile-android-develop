package com.royalenfield.reprime.ui.onboarding.useronboarding.presenter

import com.royalenfield.reprime.models.request.web.otp.OtpVerifyRequestModel
import com.royalenfield.reprime.models.request.web.signup.SignupRequest
import com.royalenfield.reprime.models.response.web.signup.RequestConsent

interface IUserOnboardingPresenter {

    fun sendOTP(callingCode: String,mobileNumber: String)

    fun verifyOTP(credential:OtpVerifyRequestModel)

    fun signup(request:SignupRequest)

    fun consentUpdate(request: RequestConsent)
}