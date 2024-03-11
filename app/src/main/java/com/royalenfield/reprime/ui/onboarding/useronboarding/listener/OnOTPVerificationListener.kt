package com.royalenfield.reprime.ui.onboarding.useronboarding.listener

interface OnOTPVerificationListener {
    fun onOTPVerificationSuccess(requestId:String?)
    fun onOTPVerificationFailure(errorCode: Int,errorMsg: String,requestId: String?)
}