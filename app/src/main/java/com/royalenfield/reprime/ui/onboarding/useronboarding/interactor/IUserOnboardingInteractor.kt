package com.royalenfield.reprime.ui.onboarding.useronboarding.interactor

import com.royalenfield.reprime.models.request.web.otp.OtpRequestModel
import com.royalenfield.reprime.models.request.web.otp.OtpVerifyRequestModel
import com.royalenfield.reprime.models.request.web.signup.SignupRequest
import com.royalenfield.reprime.models.response.web.signup.RequestConsent
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnConsentUpdateListener
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnOTPResponseListener
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnOTPVerificationListener
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnRegisterListener

interface IUserOnboardingInteractor {
    fun requestForOtp(req:OtpRequestModel,listener:OnOTPResponseListener)

    fun verifyOtp(req:OtpVerifyRequestModel,listener:OnOTPVerificationListener)

    fun signup(req: SignupRequest,listener: OnRegisterListener)

    fun updateConsent(req: RequestConsent,listener: OnConsentUpdateListener)
}