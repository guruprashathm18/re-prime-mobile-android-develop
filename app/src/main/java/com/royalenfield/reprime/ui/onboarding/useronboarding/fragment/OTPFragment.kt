package com.royalenfield.reprime.ui.onboarding.useronboarding.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.mukesh.OnOtpCompletionListener
import com.royalenfield.reprime.R
import com.royalenfield.reprime.base.REBaseFragment
import com.royalenfield.reprime.databinding.FragmentOtpBinding
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.REUtils

class OTPFragment : REBaseFragment(), OnOtpCompletionListener, OnClickListener {
    private var _binding: FragmentOtpBinding? = null
    /*OTP Message Format for devDebug build -
    <#> Royal Enfield: Your verification code is 143567
    w/IuFiH3eEG*/

    private val totalTimer = 60000L
    private val interval = 1000L
    private var mCountDownTimer: CountDownTimer? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        val params = Bundle()
        params.putString("screenname", "Sign Up & Login OTP")
        REUtils.logGTMEvent(REConstants.KEY_SCREEN_GTM, params)
        initView()
        startCountDownTimer()
        if (REUtils.isInternationalRegion())
//            (activity as UserOnboardingActivity).performEmailLogin(
//                _binding?.edEmail?.text.toString()
//            )

        else
            (activity as UserOnboardingActivity).performMobileNumberLogin(
                getDialingCode()!!,
                getPhone()!!
            )

        return _binding?.root
    }

    private fun startCountDownTimer() {
        mCountDownTimer = object : CountDownTimer(
            totalTimer, interval
        ) {
            override fun onTick(l: Long) {
                _binding?.txvResend?.text = getString(R.string.resend_in_sec, (l / 1000).toInt())
                _binding?.txvResend?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white_50
                    )
                )
            }

            override fun onFinish() {
                _binding?.txvResend?.text = getString(R.string.resend_otp)
                _binding?.txvResend?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
        }
        (mCountDownTimer as CountDownTimer).start()
    }

    private fun initView() {
        setButtonState(false)
        _binding?.otpView?.requestFocus()
        _binding?.otpView?.setOtpCompletionListener(this)
        _binding?.cancel?.setOnClickListener(this)
        _binding?.llResend?.setOnClickListener(this)
        _binding?.btnContinueOtp?.setOnClickListener(this)
        _binding?.txvOtpVerificationMsg?.text = getString(
            R.string.please_enter_verification_code,
            getDialingCode(),
            getPhone()
        )
        _binding?.otpView?.setOnKeyListener { _: View?, keyCode: Int, _: KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                _binding?.txvOtpError?.visibility = View.GONE
            }
            false
        }
        _binding?.otpView?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s?.length!=6){
                    setButtonState(false)
                }
                _binding?.txvOtpError?.visibility = View.GONE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

    }
    private fun getDialingCode(): String? {
        return arguments?.getString("CODE")
    }

    private fun getPhone(): String? {
        return arguments?.getString("MOBILE")
    }

    override fun onOtpCompleted(otp: String?) {
        _binding?.txvOtpError?.visibility = View.GONE
        hideKeyboard(activity)
        setButtonState(true)
    }

    fun setOTPView(otp: String?) {
        _binding?.otpView?.setText(otp)
    }

    fun hideResendButton(errrMsg: String) {
        _binding?.txvOtpError?.visibility = View.VISIBLE
        _binding?.txvOtpError?.text = errrMsg
        _binding?.llResend?.visibility = View.GONE
    }

    fun showInlineError(errrMsg: String) {
        _binding?.otpView?.setText("")
        _binding?.txvOtpError?.visibility = View.VISIBLE
        _binding?.txvOtpError?.text = errrMsg
    }


    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard(activity)
        if (mCountDownTimer != null) {
            mCountDownTimer!!.cancel()
            mCountDownTimer = null
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cancel -> {
                activity?.onBackPressed()
            }
            R.id.ll_resend -> {
                if (_binding?.txvResend?.text == getString(R.string.resend_otp)) {
                    val params:Bundle =  Bundle()
                    params.putString("eventCategory", "OTP_Resend");
                    params.putString("eventAction", "Resend click");
                    REUtils.logGTMEvent(REConstants.KEY_OTP_RESEND_GTM,params);
                    _binding?.txvOtpError?.visibility = View.GONE
                    clearExistingOtpAndError()
                    setButtonState(false)
                    startCountDownTimer()
                    (activity as UserOnboardingActivity).performMobileNumberLogin(
                        getDialingCode()!!,
                        getPhone()!!
                    )
                }
            }
            R.id.btnContinueOtp -> {
                if (  _binding?.otpView?.text != null&&!  _binding?.otpView?.text.isNullOrBlank()) {
                    (activity as UserOnboardingActivity).performOTPBasedLogin(  _binding?.otpView?.text.toString())
                }
                else{
                   // REUtils.showToastMsg(activity,getString(R.string.enter_6_digit_code))
                }
            }

        }
    }


    private fun clearExistingOtpAndError() {
        _binding?.otpView?.setText("")
        _binding?.txvOtpError?.visibility = View.GONE
    }

    private fun setButtonState(enable: Boolean) {
        if (enable) {
            _binding?.btnContinueOtp?.background = requireContext().getDrawable(R.drawable.shape_global_login_button)
            _binding?.btnContinueOtp?.setTextColor(requireContext().resources.getColor(R.color.white))
            _binding?.btnContinueOtp?.isEnabled = true
        } else {
            _binding?.btnContinueOtp?.background =
                requireContext().getDrawable(R.drawable.button_border_disable_text)
            _binding?.btnContinueOtp?.setTextColor(requireContext().resources.getColor(R.color.white_50))
            _binding?.btnContinueOtp?.isEnabled = false
        }
    }

}