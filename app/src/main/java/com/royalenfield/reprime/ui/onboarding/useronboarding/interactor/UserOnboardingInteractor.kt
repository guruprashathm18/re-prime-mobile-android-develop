package com.royalenfield.reprime.ui.onboarding.useronboarding.interactor

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.models.request.web.otp.OtpRequestModel
import com.royalenfield.reprime.models.request.web.otp.OtpVerifyRequestModel
import com.royalenfield.reprime.models.request.web.signup.SignupRequest
import com.royalenfield.reprime.models.response.web.login.LoginResponse
import com.royalenfield.reprime.models.response.web.otp.LoginOtpResponse
import com.royalenfield.reprime.models.response.web.otp.OtpResponse
import com.royalenfield.reprime.models.response.web.signup.ConsentResponse
import com.royalenfield.reprime.models.response.web.signup.RequestConsent
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnConsentUpdateListener
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnOTPResponseListener
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnOTPVerificationListener
import com.royalenfield.reprime.ui.onboarding.useronboarding.listener.OnRegisterListener
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.RELog
import com.royalenfield.reprime.utils.REUtils
import retrofit2.Call
import retrofit2.Response

class UserOnboardingInteractor:IUserOnboardingInteractor {


    override fun requestForOtp(req: OtpRequestModel, listener: OnOTPResponseListener) {
        REApplication
            .getInstance()
            .reWebsiteApiInstance
            .websiteAPI
            .sendOtpRequest(req)
            .enqueue(object : BaseCallback<LoginOtpResponse>() {
                override fun onResponse(call: Call<LoginOtpResponse>, response: Response<LoginOtpResponse>) {
                    super.onResponse(call, response)
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.code.equals("200")) {
                            listener.onSuccess(response?.body()?.requestId)
                        } else {
                            listener.onFailure(response.body()!!.code.toInt(),response.body()!!.errorMessage)
                        }
                    } else {
                        listener.onFailure(response.code() ,  REApplication.getAppContext().resources.getString(R.string.sorry_please_try_again))
                    }
                }

                override fun onFailure(call: Call<LoginOtpResponse>, t: Throwable) {
                    super.onFailure(call, t)
                    if (t is NetworkErrorException) {
                        listener.onFailure(-1,REApplication.getAppContext().resources.getString(R.string.network_failure))
                    } else {
                        t.message?.let { listener.onFailure(-1, REApplication.getAppContext().resources.getString(R.string.sorry_please_try_again)) }
                    }
                }
            })

    }

    override fun verifyOtp(req: OtpVerifyRequestModel, listener: OnOTPVerificationListener) {
        REApplication
            .getInstance()
            .reWebsiteApiInstance
            .v4API
            .otpLogin(req)
            .enqueue(object : BaseCallback<LoginResponse>() {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    super.onResponse(call, response)
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.success&&response.body()!!.code == REConstants.API_SUCCESS_CODE) {
                            REUtils.setDataToUserInfo(response.body())
                            listener.onOTPVerificationSuccess(response?.body()?.requestId)
                        } else {
                            listener.onOTPVerificationFailure(response.body()!!.code,response.body()!!.message,response?.body()?.requestId)
                        }
                    } else {
                        listener.onOTPVerificationFailure(-1, REApplication.getAppContext().resources.getString(R.string.sorry_please_try_again),null)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    super.onFailure(call, t)
                    if (t is NetworkErrorException) {
                        listener.onOTPVerificationFailure(-1,REApplication.getAppContext().resources.getString(R.string.network_failure),null)
                    } else {
                        t.message?.let { listener.onOTPVerificationFailure(-1,REApplication.getAppContext().resources.getString(R.string.sorry_please_try_again),null) }
                    }
                }
            })

    }

    override fun signup(req: SignupRequest, listener: OnRegisterListener) {
        //Retrofit call
        REApplication
            .getInstance()
            .reWebsiteApiInstance
            .v4API
            .signUp(req)
            .enqueue(object : BaseCallback<LoginResponse?>() {
               @SuppressLint("LogNotTimber")
               override fun onResponse(
                   call: Call<LoginResponse?>,
                   response: Response<LoginResponse?>
               ) {
                    super.onResponse(call, response)
                    Log.e("SIGNUP", "SIGNUP SUCCESS")
                    RELog.e( "SignUp response code : " + response.code() + "  " +
                                "response : " + Gson().toJson(response.body())
                    )
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.code == REConstants.API_SUCCESS_CODE && response.body()!!.success) {
                            Log.d("API", "SignUp  onResponse: " + response.code() + " : " + response.body()!!.message)
                            if (REUtils.validateLoginResponse(response.body())) {
                                REUtils.setDataToUserInfo(response.body())
                                Log.e("SIGNUP", "DATA SAVED LOCALY")
                                listener.onSignupSuccess(response?.body()?.requestId)
                            } else {
                                listener.onSignupFailure(response.body()?.code, response.body()?.message)
                            }
                        } else {
                            Log.d("API", "SignUp  onResponse: " + response.code() + " : " + response.body()!!.message)
                            listener.onSignupFailure(response.body()?.code,response.body()?.message)

                        }
                    } else {
                        // error case
                        Log.d("API", "SignUp  onResponse: " + "Failed")
                        listener.onSignupFailure(-1, REApplication.getAppContext().resources.getString(R.string.sorry_please_try_again))
                    }
                }

               override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    super.onFailure(call, t)
                    RELog.d("API", "SignUp  onFailure : " + t.message)
                   listener.onSignupFailure(-1, REApplication.getAppContext().resources.getString(R.string.sorry_please_try_again))
                }
            })

    }

    override fun updateConsent(reqConsent: RequestConsent, listener: OnConsentUpdateListener) {
        REApplication
            .getInstance()
            .reWebsiteApiInstance
            .websiteAPI
            .takeUserConsentEU(reqConsent)
            .enqueue(object : BaseCallback<ConsentResponse?>() {
               override fun onResponse(
                   call: Call<ConsentResponse?>,
                   response: Response<ConsentResponse?>
               ) {
                    super.onResponse(call, response)
                    Log.e("SIGNUP", "SIGNUP SUCCESS")

                    if (response.code() == 200) {
                        listener.onConsentSuccess(response.body()!!.message)
                    } else {
                        listener.onConsentFailure(response.body()!!.message)
                    }
                }

              override  fun onFailure(call: Call<ConsentResponse?>, t: Throwable) {
                    super.onFailure(call, t)
                    Log.d("API", "SignUp  onFailure : " + t.message)
                    listener.onConsentFailure("")
                }
            })

    }

}