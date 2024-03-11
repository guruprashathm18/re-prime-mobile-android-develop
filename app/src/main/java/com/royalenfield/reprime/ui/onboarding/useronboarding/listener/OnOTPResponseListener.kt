package com.royalenfield.reprime.ui.onboarding.useronboarding.listener

interface OnOTPResponseListener {
    fun onSuccess(reqId:String?)
    fun onFailure(errorCode:Int,errorMsg: String)
}