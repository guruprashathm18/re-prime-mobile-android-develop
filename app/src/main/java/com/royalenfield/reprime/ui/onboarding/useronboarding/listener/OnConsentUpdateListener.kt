package com.royalenfield.reprime.ui.onboarding.useronboarding.listener

interface OnConsentUpdateListener {
    fun onConsentSuccess(msg:String?)
    fun onConsentFailure(errorMsg: String?)
}