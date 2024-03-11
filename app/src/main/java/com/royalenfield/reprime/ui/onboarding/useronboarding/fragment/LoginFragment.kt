package com.royalenfield.reprime.ui.onboarding.useronboarding.fragment

import android.app.PendingIntent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.base.REBaseFragment
import com.royalenfield.reprime.databinding.FragmentLoginOtpBinding
import com.royalenfield.reprime.preference.PreferenceException
import com.royalenfield.reprime.preference.REPreference
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.Pincode
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.RELog
import com.royalenfield.reprime.utils.REUtils


class LoginFragment : REBaseFragment(), OnClickListener {
    private var _binding: FragmentLoginOtpBinding? = null
    private val splashArr: ArrayList<Int> = ArrayList()
    private var isTBTDisabled = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginOtpBinding.inflate(inflater, container, false)

        //This is for alert dialog
        val params = Bundle()
        params.putString("screenname", "App Launch")
        REUtils.logGTMEvent(REConstants.KEY_SCREEN_GTM, params)
        initView()
        return _binding?.root
    }

    private fun initView() {
        setButtonState(false)
        val configFeature = REUtils.getConfigFeatures()
        if (configFeature != null && configFeature.tbt != null) {
            if (configFeature.tbt.tbtReleaseFeatureStatus != null && configFeature.tbt.tbtReleaseFeatureStatus.equals(
                    REConstants.FEATURE_STATUS_DISABLED,
                    ignoreCase = true
                )
            ) {
                isTBTDisabled = true
            }
        }

        if (REApplication.getInstance().featureCountry == null) {
            REApplication.getInstance().featureCountry = REUtils.getFeatures().defaultFeatures
        }
        val feature = REApplication.getInstance().featureCountry
        if (feature != null) {
            if (feature.community.equals(REConstants.FEATURE_ENABLED, ignoreCase = true))
                splashArr.add(R.drawable.splash_rider1)
            if (feature.motorcycleInfo.equals(
                    REConstants.FEATURE_ENABLED,
                    ignoreCase = true
                )
            ) splashArr.add(R.drawable.splash_service)
            if (!isTBTDisabled) splashArr.add(R.drawable.splash_tripper)
        }

        val validMobile = Pincode()
        validMobile.min = REUtils.getGlobalValidations().minPhoneNum
        validMobile.max = REUtils.getGlobalValidations().phoneNumber
        REApplication.getInstance().validMobile = validMobile
        _binding?.continueGuestLayout?.root?.visibility = View.GONE
        _binding?.tvOrLabel?.visibility = View.GONE
        if (REUtils.getFeatures().defaultFeatures != null && REUtils.getFeatures().defaultFeatures.guestLogin.equals(
                REConstants.FEATURE_ENABLED, ignoreCase = true)&&activity?.callingActivity==null) {
            _binding?.tvOrLabel?.visibility = View.VISIBLE
            _binding?.continueGuestLayout?.root?.visibility = View.VISIBLE
        }
        _binding?.btnContinue?.setOnClickListener(this)
        _binding?.continueGuestLayout?.textContinueAsGuest?.setOnClickListener(this)

        if (REUtils.isInternationalRegion()) {
            _binding?.layoutLoginEmail?.visibility=View.VISIBLE
            _binding?.layoutLoginMobileNumber?.visibility=View.GONE
            _binding?.edEmail?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,

                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (REUtils.isValidEmailId(s.toString())) {
                        setButtonState(true)
                    } else {
                        setButtonState(false)
                    }
                    _binding?.tvMobileNuError?.setText(R.string.enter_valid_email_error)
                    _binding?.tvMobileNuError?.visibility = View.GONE
                }

                override fun afterTextChanged(s: Editable) {}
            })

        } else {
            _binding?.layoutLoginEmail?.visibility=View.GONE
            _binding?.layoutLoginMobileNumber?.visibility=View.VISIBLE
            showPhoneNuHint()
            _binding?.edMobileNumber?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.length == 10) {
                        setButtonState(true)
                    } else {
                        setButtonState(false)
                    }
                    _binding?.tvMobileNuError?.setText(R.string.text_valid_phone_error)
                    _binding?.tvMobileNuError?.visibility = View.GONE
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }
    }

    private fun setButtonState(enable:Boolean){
        if(enable){
            _binding?.btnContinue?.background =
                requireContext().getDrawable(R.drawable.shape_global_login_button)
            _binding?.btnContinue?.setTextColor(requireContext().resources.getColor(R.color.white))
            _binding?.btnContinue?.isEnabled = true
        }
        else{
            _binding?.btnContinue?.background =
                requireContext().getDrawable(R.drawable.button_border_disable_text)
            _binding?.btnContinue?.setTextColor(requireContext().resources.getColor(R.color.white_50))
            _binding?.btnContinue?.isEnabled = false
        }
    }
    private fun showPhoneNuHint() {

        val request: GetPhoneNumberHintIntentRequest =
            GetPhoneNumberHintIntentRequest.builder().build()
        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                try {
                    var phoneNumber = Identity.getSignInClient(activity as UserOnboardingActivity).getPhoneNumberFromIntent(result.data)
                    phoneNumber=  phoneNumber.substring(phoneNumber.length -10);
                    setPhoneNumber(phoneNumber)
                } catch (e: Exception) {
                }
            }

        Identity.getSignInClient(activity as UserOnboardingActivity)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener { result: PendingIntent ->
                try {
                    activityResultLauncher.launch(
                        IntentSenderRequest.Builder(result).build()
                    )
                } catch (e: Exception) {
                }
            }
            .addOnFailureListener {
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnContinue -> {
                REApplication.getInstance().arrayListResponse.clear()
               val params:Bundle =  Bundle()
                params.putString("eventCategory", "Sign Up & Login");
                params.putString("eventAction", "Continue click");
                REUtils.logGTMEvent(REConstants.KEY_SIGNUP_LOGIN_GTM,params);
                REApplication.getInstance().clearAllModelStore()
                try {
                    REPreference.getInstance().removeAll(requireContext())
                } catch (e: PreferenceException) {
                    RELog.e(e)
                }
//                if(REUtils.isInternationalRegion())
//                    (activity as UserOnboardingActivity).performEmailLogin(_binding?.edEmail?.text.toString())
//
//                else
                    if (!REUtils.isValidMobileNumber(_binding?.edMobileNumber?.text.toString()))
                    invalidMobileNumber()
                    else
                        (activity as UserOnboardingActivity).gotoOTPScreen(_binding?.tvCountryCode?.text.toString(),_binding?.edMobileNumber?.text.toString())

            }
            R.id.text_continue_as_guest -> {
                (activity as UserOnboardingActivity).performGuestLogin()
            }
        }
    }

    fun invalidMobileNumber() {
        _binding?.tvMobileNuError?.visibility = View.VISIBLE
    }

    fun setPhoneNumber(phone: String) {
        _binding?.edMobileNumber?.setText(phone)
    }
    fun removeMobileNumber(){
        _binding?.edMobileNumber?.setText("")
    }
}