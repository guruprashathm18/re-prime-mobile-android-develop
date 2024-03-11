package com.royalenfield.reprime.models.request.web.otp



data class  OtpRequestModel(var mobile: String,var callingCode: String, var otpExpirationTime: Int, var otpType: Int, var email: String) {

}
