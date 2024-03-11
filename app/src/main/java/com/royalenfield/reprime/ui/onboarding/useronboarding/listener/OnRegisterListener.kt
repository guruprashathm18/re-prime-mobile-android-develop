package com.royalenfield.reprime.ui.onboarding.useronboarding.listener

interface OnRegisterListener {
    fun onSignupSuccess(reqId:String?)
    fun onSignupFailure(errorCode: Int?,errorMsg: String?)
}