package com.royalenfield.reprime.ui.onboarding.useronboarding.activity

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.base.REBaseActivity
import com.royalenfield.reprime.databinding.ActivityUserOnboardingBinding
import com.royalenfield.reprime.models.request.web.otp.OtpVerifyRequestModel
import com.royalenfield.reprime.models.request.web.signup.SignupRequest
import com.royalenfield.reprime.models.response.web.signup.RequestConsent
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity
import com.royalenfield.reprime.ui.onboarding.LogResponse
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity
import com.royalenfield.reprime.ui.onboarding.useronboarding.SmsReceiver
import com.royalenfield.reprime.ui.onboarding.useronboarding.fragment.ConsentFragment
import com.royalenfield.reprime.ui.onboarding.useronboarding.fragment.LoginFragment
import com.royalenfield.reprime.ui.onboarding.useronboarding.fragment.OTPFragment
import com.royalenfield.reprime.ui.onboarding.useronboarding.fragment.SignupFragment
import com.royalenfield.reprime.ui.onboarding.useronboarding.interactor.UserOnboardingInteractor
import com.royalenfield.reprime.ui.onboarding.useronboarding.presenter.UserOnboardingPresenter
import com.royalenfield.reprime.ui.onboarding.useronboarding.views.UserOnboardingView
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.REUtils
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class UserOnboardingActivity : REBaseActivity(), UserOnboardingView{
    private lateinit var binding: ActivityUserOnboardingBinding
    private var curFragment: Fragment? = null
    private lateinit var loginPresenter: UserOnboardingPresenter
    private var mobileNumber: String? = null
    private var emailId: String? = null
    private var callingCode: String? = null
    private var otp: String? = null
    private var intentFilter: IntentFilter? = null
    private var smsReceiver: SmsReceiver? = null
    private var signupRequest = SignupRequest()
    private var consentResponse: RequestConsent? = null
    var callingActivity = 0
    private var fromSignup:Boolean=false
    private  var startTime :Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_onboarding)
        binding.lifecycleOwner = this
        isUserActive=true;
        val paramsScr = Bundle()
        paramsScr.putString("screenname", "App Settings screen")
        REUtils.logGTMEvent(REConstants.KEY_SCREEN_GTM, paramsScr)
        initView()
        addFragmentToActivity(LoginFragment(), false)
        checkForUpdates()
        initSmsListener()
        initBroadCast()
    }


    fun initView() {
        callingActivity = intent.getIntExtra(PCUtils.PC_CALLING_ACTIVITY, 0)
        //instantiate presenter.
        loginPresenter = UserOnboardingPresenter(this, UserOnboardingInteractor())
    }

    private fun addFragmentToActivity(fragment: Fragment?, addstack: Boolean) {

        if (fragment == null) return
        val fm = supportFragmentManager
        val tr = fm.beginTransaction()
        tr.add(R.id.frame_main, fragment)
        if (addstack)
            tr.addToBackStack(null)
        tr.commitAllowingStateLoss()
        curFragment = fragment
    }

    private fun addFragmentToActivityandRemovePrevious(fragment: Fragment?) {
try {
    if (fragment == null) return
    val fm = supportFragmentManager
    fm.popBackStack()
    val fragments: List<Fragment> = supportFragmentManager.fragments
    for (i in 0 until fragments.size) {
        try {
            val fragment: Fragment = fragments.get(i)
            if (fragment is LoginFragment)
                fragment.removeMobileNumber()

        } catch (e: Exception) {
            Timber.d("Fragment Back Stack Error: %s", e.localizedMessage)
        }
    }
    val tr = supportFragmentManager.beginTransaction()
    tr.add(R.id.frame_main, fragment)
    tr.addToBackStack(null)
    tr.commitAllowingStateLoss()
    curFragment = fragment
}
catch (e:Exception){

}
    }

    fun performGuestLogin() {
        val guestUserIntent = Intent(this, REHomeActivity::class.java)
        guestUserIntent.putExtra(REUtils.USER_LOGGED_IN, false)
        startActivity(guestUserIntent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    fun performMobileNumberLogin(callingCode: String, mobileNumber: String) {
        initSmsListener()
        initBroadCast()
        startTime=System.currentTimeMillis()
        loginPresenter.sendOTP(callingCode.replace("[-+^:,]".toRegex(), ""), mobileNumber)
    }

    fun performEmailLogin(emaiId: String) {
        this@UserOnboardingActivity.emailId = mobileNumber
       // loginPresenter.sendOTP(emaiId)
    }

    override fun invalidMobileNumber() {
        val f: Fragment? = supportFragmentManager.findFragmentById(R.id.frame_main)
        if (f is LoginFragment)
            f.invalidMobileNumber()
    }

    override fun onOTPSuccess(reqId: String?) {
        REApplication.getInstance().arrayListResponse.add(LogResponse("Send OTP API", System.currentTimeMillis().minus(startTime),reqId))
    }

     fun gotoOTPScreen(callingCode: String,mobileNumber: String) {
         val callingCodeUpdated = callingCode.replace("[-+^]*", "")
         this@UserOnboardingActivity.callingCode = callingCodeUpdated
         this@UserOnboardingActivity.mobileNumber = mobileNumber
        val fragmentOtp = OTPFragment()
        val mBundle = Bundle()
        mBundle.putString("CODE", callingCode)
        mBundle.putString("MOBILE", mobileNumber)
        fragmentOtp.arguments = mBundle
        addFragmentToActivity(fragmentOtp, true)
    }

    override fun onOTPFailure(errorMsg: String) {
        REUtils.showErrorDialog(this, errorMsg) { onBackPressed() }
    }

    override fun loginSuccess(requestID:String?) {
        REApplication.getInstance().flow = "login"
        REApplication.getInstance().arrayListResponse.add(LogResponse("OTP Verification API", System.currentTimeMillis().minus(startTime),requestID))
        REUtils.setFCMTokenSent(false)
       // showLoading()
        gotoSetResult()
        getUserDataInBackground()
    }

    override fun otpLimitExceed(errorMsg: String) {
        val f: Fragment? = supportFragmentManager.findFragmentById(R.id.frame_main)
        if (f is OTPFragment)
            f.hideResendButton(errorMsg)
    }


    private fun initBroadCast() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsReceiver = SmsReceiver()
        smsReceiver?.setOTPListener(object : SmsReceiver.OTPReceiveListener {
            override fun onOTPReceived(otp: String?) {
                val f: Fragment? = supportFragmentManager.findFragmentById(R.id.frame_main)
                if (f is OTPFragment)
                    f.setOTPView(otp)
            }
        })
    }

    private fun initSmsListener() {
        val client = SmsRetriever.getClient(this)
        client.startSmsRetriever()
    }

    override fun onDestroy() {
        super.onDestroy()
        smsReceiver = null
    }

    override fun onResume() {
        super.onResume()
        try {
            registerReceiver(smsReceiver, intentFilter)
        }
        catch (e:Exception){

        }
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(smsReceiver)
        }
        catch (e:Exception){

        }
    }

    fun performOTPBasedLogin(otp: String) {
        startTime=System.currentTimeMillis()
        this@UserOnboardingActivity.otp = otp
        val credentials = "$mobileNumber:$otp"
        val data = credentials.toByteArray(StandardCharsets.UTF_8)
        loginPresenter.verifyOTP(
            OtpVerifyRequestModel(
                android.util.Base64.encodeToString(
                    data,
                    android.util.Base64.NO_WRAP
                )
            )
        )

    }

    fun gotoConsentFragment(firstName: String, lastName: String, dob: String, email: String) {
        signupRequest.lName = lastName
        signupRequest.fName = firstName
        signupRequest.dob = dob
        signupRequest.email = email
        addFragmentToActivity(ConsentFragment(), true)
    }

    fun performSignup(consentData: RequestConsent?) {
        startTime=System.currentTimeMillis()
        fromSignup=true
        signupRequest.otp = otp
        signupRequest.mobile = mobileNumber
        signupRequest.callingCode =callingCode?.replace("[-+^:,]".toRegex(), "")
        consentResponse = consentData
        loginPresenter.signup(signupRequest)
    }

    fun getMobileNumber(): String? {
        return mobileNumber
    }

    fun getCallingCode(): String? {
        return callingCode
    }

    override fun userNotExist(reqId: String?) {
        REApplication.getInstance().flow = "signup"
        REApplication.getInstance().arrayListResponse.add(LogResponse("OTP Verification API", System.currentTimeMillis().minus(startTime),reqId))

        val params: Bundle =  Bundle()
        params.putString("eventCategory", "OTP");
        params.putString("eventAction", "Continue click");
        params.putString("eventLabel", "success");
        REUtils.logGTMEvent(REConstants.KEY_OTP_GTM,params);
        addFragmentToActivityandRemovePrevious(SignupFragment())
    }

    override fun otpVerifyFailure(errorMsg: String) {
        submitInvalidGTM(errorMsg)
        REUtils.showErrorDialog(this, errorMsg)
      // addFragmentToActivityandRemovePrevious(SignupFragment())
    }

    override fun otpVerifyFailureInline(errorMsg: String) {
       submitInvalidGTM(errorMsg)
        val f: Fragment? = supportFragmentManager.findFragmentById(R.id.frame_main)
        if (f is OTPFragment)
            f.showInlineError(errorMsg)
      //  addFragmentToActivityandRemovePrevious(SignupFragment())
    }

    private fun submitInvalidGTM(errorMsg: String) {
        val params: Bundle =  Bundle()
        params.putString("eventCategory", "OTP");
        params.putString("eventAction", "Continue click");
        params.putString("eventLabel", "fail");
        params.putString("errorMessage", errorMsg);
        REUtils.logGTMEvent(REConstants.KEY_OTP_GTM,params);
    }

    override fun otpInvalidInline(errorMsg: String) {
        submitInvalidGTM(errorMsg)
        val f: Fragment? = supportFragmentManager.findFragmentById(R.id.frame_main)
        if (f is OTPFragment)
            f.showInlineError(getString(R.string.otp_invalid))
    }


    override fun signupSuccess(reqId:String?) {
        REApplication.getInstance().arrayListResponse.add(LogResponse("Signup API", System.currentTimeMillis().minus(startTime),reqId))

        var params = Bundle()
        params.putString("eventCategory", "Sign Up Form")
        params.putString("eventAction", "Create Account")
        params.putString("eventLabel", "Success")
        params.putString("communicationInfoTopic",getAllCheckedConsent(consentResponse))
        REUtils.logGTMEvent(REConstants.KEY_SIGNUP_FORM_GTM, params)
            consentResponse?.let { it -> loginPresenter.consentUpdate(it) }
        startTime=System.currentTimeMillis()
        gotoSetResult()
        getUserDataInBackground()

    }

    private fun getAllCheckedConsent(consentResponse: RequestConsent?): String? {
        var checked :java.lang.StringBuilder= StringBuilder()
        if(consentResponse?.communityactivities == true){
            checked.append(getString(R.string.community_activity))
        }

        if(consentResponse?.promotionsnews == true) {
            if(checked.isNotEmpty())
                checked.append("|")
            checked.append(getString(R.string.promotions_news))
        }
        if(consentResponse?.surveysandresearch == true) {
            if (checked.isNotEmpty())
                checked.append("|")
            checked.append(getString(R.string.survey_reserch))
        }
        if(consentResponse?.allpromotions == true) {
            if (checked.isNotEmpty())
                checked.append("|")
            checked.append(getString(R.string.all))
        }
return checked.toString()
    }

    override fun signupFailed(errorMsg: String?) {
        var params = Bundle()
        params.putString("eventCategory", "Sign Up Form")
        params.putString("eventAction", "Create Account")
        params.putString("eventLabel", "fail")
        params.putString("errorMessage", errorMsg)
        params.putString("communicationInfoTopic",getAllCheckedConsent(consentResponse))
        REUtils.logGTMEvent(REConstants.KEY_SIGNUP_FORM_GTM, params)
        REUtils.showErrorDialog(this, errorMsg)
    }

    override fun emailOrMobileExist(errorMsg: String?) {
        var params = Bundle()
        params.putString("eventCategory", "Sign Up Form")
        params.putString("eventAction", "Create Account")
        params.putString("eventLabel", "fail")
        params.putString("errorMessage", errorMsg)
        params.putString("communicationInfoTopic",getAllCheckedConsent(consentResponse))
        REUtils.logGTMEvent(REConstants.KEY_SIGNUP_FORM_GTM, params)
        if(REUtils.isInternationalRegion())
            REUtils.showErrorDialog(this, getString(R.string.mobile_exist))
            else
        REUtils.showErrorDialog(this, getString(R.string.email_exist))
    }

    override fun otpExpired(errorMsg: String?) {
        var params = Bundle()
        params.putString("eventCategory", "Sign Up Form")
        params.putString("eventAction", "Create Account")
        params.putString("eventLabel", "fail")
        params.putString("errorMessage", errorMsg)
        params.putString("communicationInfoTopic",getAllCheckedConsent(consentResponse))
        REUtils.logGTMEvent(REConstants.KEY_SIGNUP_FORM_GTM, params)
        REUtils.showErrorDialog(this, getString(R.string.otp_expired)) {
            val fragmentManager: FragmentManager = supportFragmentManager

            for (i in 0 until fragmentManager.getBackStackEntryCount()) {
                try {
                    val fragmentId: Int = fragmentManager.getBackStackEntryAt(i).getId()
                    fragmentManager.popBackStack(
                        fragmentId,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                } catch (e: Exception) {
                    Timber.d("Fragment Back Stack Error: %s", e.localizedMessage)
                }
            }
        }
    }
    override fun consentSuccess(message: String?) {
    }

    override fun consentFailure(error: String?) {
    }

    fun gotoSetResult() {
        if(getCallingActivity() !=null) {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            if (callingActivity == LoginActivity.CODE_PC_ACTIVITY && !getIntent().hasExtra(PCUtils.PC_BOOK_NOW)) {
                PCTransitionShowDialog()
            } else {
                finish()
            }
        }
        else{
            REApplication.getInstance().setLoggedInUser(true)
            val intent = Intent(this, REHomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(
                REConstants.IS_SERVICE_NOTIFICATION, getIntent().getBooleanExtra(REConstants.IS_SERVICE_NOTIFICATION, false)
            )
            intent.putExtra(
                REConstants.IS_NAVIGATION_NOTIFICATION, getIntent().getBooleanExtra(REConstants.IS_NAVIGATION_NOTIFICATION, false)
            )
            intent.putExtra(
                REConstants.NAVIGATION_NOTIFICATION, getIntent().getStringExtra(REConstants.NAVIGATION_NOTIFICATION)
            )
            intent.putExtra(
                REConstants.IS_NAVIGATION_DEEPLINK, getIntent().getBooleanExtra(REConstants.IS_NAVIGATION_DEEPLINK, false)
            )
            intent.putExtra(
                REConstants.NAVIGATION_DEEPLINK, getIntent().getStringExtra(REConstants.NAVIGATION_DEEPLINK)
            )
            startActivity(intent)
           //
        }
    }

    private fun PCTransitionShowDialog() {
        PCUtils.showDialog(this)
        val handler = Handler()
        handler.postDelayed({
            finish()
        }, 2000)
    }

    fun getDate(): String? {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.ENGLISH)
        val gmtTime = format.format(Date())
        val c = Calendar.getInstance()
        Log.e("date", format.format(c.time))
        return format.format(c.time)
    }

}