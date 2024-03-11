package com.royalenfield.reprime.ui.onboarding.useronboarding.fragment

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.royalenfield.firebase.fireStore.FirestoreManager
import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.base.REBaseFragment
import com.royalenfield.reprime.databinding.FragmentConsentBinding
import com.royalenfield.reprime.models.response.web.signup.RequestConsent
import com.royalenfield.reprime.ui.onboarding.signup.listeners.OnConsentDataListener
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity
import com.royalenfield.reprime.ui.riderprofile.activity.ConsentUpdateActivity
import com.royalenfield.reprime.ui.riderprofile.activity.REWebViewActivity
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan
import com.royalenfield.reprime.utils.REUtils
import timber.log.Timber


class ConsentFragment : REBaseFragment(), OnClickListener, OnCheckedChangeListener {
    private var _binding: FragmentConsentBinding? = null
    private var oldConsent = RequestConsent()
    private var newConsent = RequestConsent()
private var  isDataSend=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConsentBinding.inflate(inflater, container, false)
        val params = Bundle()
        params.putString("screenname", "Sign Up Create Account")
        REUtils.logGTMEvent(REConstants.KEY_SCREEN_GTM, params)
        initViews()
        return _binding?.root
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.tvMobileNumber->{
             restartActivity()
            }

            R.id.signup_button -> {
                REApplication.getInstance().clearAllModelStore()
                Log.e("SIGNUP", "BUTTON CLICK")


                if(activity is UserOnboardingActivity){
                    (activity as UserOnboardingActivity).performSignup(getConsentData())
                }
            }
            R.id.submit_button->{
               getConsentData()
                if(activity is ConsentUpdateActivity){
                    (activity as ConsentUpdateActivity).consentUpdate(getConsentData())
                }
                //call submit api
            }
        }
    }

    private fun getConsentData():RequestConsent {
        val consent = RequestConsent()
        consent.allpromotions = _binding?.allCheck?.isChecked
        consent.communityactivities =  _binding?.communityCheck?.isChecked
        consent.promotionsnews =  _binding?.promotionCheck?.isChecked
        consent.surveysandresearch =  _binding?.surveyCheck?.isChecked
        consent.donotemail = false
        consent.donotphone = true
        consent.donotwhatsapp = true
        consent.donotpushnotification = true
        return consent
    }

    private fun initViews() {
        if(activity is UserOnboardingActivity)
        _binding?.topview?.tvMobileNumber?.text = (activity as UserOnboardingActivity).getCallingCode()+" "+(activity as UserOnboardingActivity).getMobileNumber()
      else
            _binding?.topview?.constraint?.visibility=View.GONE
        _binding?.topview?.ivBack?.setOnClickListener(this)
        _binding?.signupButton?.setOnClickListener(this)
        _binding?.topview?.tvMobileNumber?.setOnClickListener(this)
        _binding?.signupButton?.background =
            AppCompatResources.getDrawable(requireContext(), R.drawable.button_border_disable_text)
        _binding?.signupButton?.setTextColor(requireContext().resources.getColor(R.color.white_50))
        _binding?.signupButton?.isEnabled = false
        _binding?.submitButton?.setOnClickListener(this)
        _binding?.submitButton?.isEnabled = false
        _binding?.communityCheck?.setOnCheckedChangeListener(this)
        _binding?.promotionCheck?.setOnCheckedChangeListener(this)
        _binding?.surveyCheck?.setOnCheckedChangeListener(this)
        _binding?.allCheck?.setOnCheckedChangeListener(this)
        _binding?.allCheck?.setOnClickListener(OnClickListener {
            if (_binding?.allCheck?.isChecked == false) {
                _binding?.allCheck?.isChecked = false
                _binding?.promotionCheck?.isChecked = false
                _binding?.surveyCheck?.isChecked = false
                _binding?.communityCheck?.isChecked = false
            } else {
                 _binding?.promotionCheck?.isChecked = true
                _binding?.surveyCheck?.isChecked = true
                _binding?.communityCheck?.isChecked = true

            }
        })
        _binding?.communityCheck?.setOnClickListener(OnClickListener {
            if (_binding?.communityCheck?.isChecked == false) {

            } else {
               logEventCheckedNew(getString(R.string.community_activity))
            }
        })
        _binding?.surveyCheck?.setOnClickListener(OnClickListener {
            if (_binding?.surveyCheck?.isChecked == false) {

            } else {
                logEventCheckedNew(getString(R.string.survey_reserch))
            }
        })
        _binding?.promotionCheck?.setOnClickListener(OnClickListener {
            if (_binding?.promotionCheck?.isChecked == false) {

            } else {
                logEventCheckedNew(getString(R.string.promotions_news))
            }
        })
        val ss = SpannableString(resources.getString(R.string.disclaimer))
        val boldSpan = StyleSpan(Typeface.BOLD)
        ss.setSpan(boldSpan, 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        _binding?.txtDisclaimer?.text = ss
        //This method is for custom text click
        customTextView(_binding?.privacypolicyTv)
        _binding?.privacypolicyCheck?.setOnCheckedChangeListener(OnCheckedChangeListener { _, _ -> checkRequiredFields() })
        if (activity is ConsentUpdateActivity) {
            _binding?.submitButton?.visibility = View.VISIBLE
            _binding?.signupButton?.visibility = View.GONE
            _binding?.privacypolicyCheck?.isChecked = true
            _binding?.privacypolicyCheck?.visibility = View.GONE
            _binding?.privacypolicyTv?.visibility = View.GONE
            showLoading()
            FirestoreManager.getInstance().getUserConsent(object : OnConsentDataListener {
                override fun onSuccess(consentData: RequestConsent) {
                    hideLoading()
                    oldConsent = consentData
                    newConsent = RequestConsent.copy(consentData)
                    setConsentData(consentData)
                }

                override fun onFailure(errorMessage: String) {
                    hideLoading()
                    REUtils.showErrorDialog(
                        activity, resources.getString(R.string.oops_something)
                    ) { activity!!.finish() }
                }
            })
        } else {
            _binding?.submitButton?.visibility = View.GONE
            _binding?.signupButton?.visibility = View.VISIBLE
        }
    }

    private fun setConsentData(consentData: RequestConsent?) {
        if (consentData != null && consentData.allpromotions != null) {
            _binding?.allCheck?.isChecked = consentData.allpromotions
            _binding?.surveyCheck?.isChecked = consentData.surveysandresearch
            _binding?.promotionCheck?.isChecked = consentData.promotionsnews
            _binding?.communityCheck?.isChecked = consentData.communityactivities
        }
    }

    private fun customTextView(view: TextView?) {
        val mTypefaceMontserratRegular = ResourcesCompat.getFont(
            requireContext(),
            R.font.montserrat_regular
        )
        val typeface_montserrat_semibold = ResourcesCompat.getFont(
            requireContext(),
            R.font.montserrat_semibold
        )
        val spanTxt = SpannableStringBuilder(
            requireContext().getString(R.string.text_policies_conditions)
        )
        spanTxt.setSpan(
            RECustomTyperFaceSpan(mTypefaceMontserratRegular), 0,
            requireContext().getString(R.string.text_policies_conditions).length, 0
        )
        spanTxt.append(" ")
        spanTxt.append(requireContext().getString(R.string.text_terms_conditions))
        spanTxt.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val intent = Intent(
                        context,
                        REWebViewActivity::class.java
                    )
                    intent.putExtra(
                        REConstants.SETTING_ACTIVITY_INPUT_TYPE,
                        resources.getString(R.string.term_of_condition)
                    )
                    startActivity(intent)
                    requireActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit)
                }
            }, spanTxt.length - requireContext().getString(R.string.text_terms_conditions)
                .length, spanTxt.length, 0
        )
        spanTxt.setSpan(
            RECustomTyperFaceSpan(typeface_montserrat_semibold), spanTxt.length
                    - requireContext().getString(R.string.text_terms_conditions)
                .length, spanTxt.length, 0
        )

        view?.movementMethod = LinkMovementMethod.getInstance()
        view?.setTextColor(requireContext().resources.getColor(R.color.white))
        view?.setText(spanTxt, TextView.BufferType.SPANNABLE)
    }

    private fun checkRequiredFields() {
        if (_binding?.privacypolicyCheck?.isChecked == true) {
            _binding?.signupButton?.background = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.shape_global_login_button
            )
            _binding?.signupButton?.setTextColor(requireContext().resources.getColor(R.color.white))
            _binding?.signupButton?.isEnabled = true
        } else {
            _binding?.signupButton?.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.button_border_disable_text)
            _binding?.signupButton?.setTextColor(requireContext().resources.getColor(R.color.white_50))
            _binding?.signupButton?.isEnabled = false
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.phone_check -> newConsent.donotphone = !isChecked
            R.id.email_check -> newConsent.donotemail = !isChecked
            R.id.whatsapp_check -> newConsent.donotwhatsapp = !isChecked
            R.id.push_check -> newConsent.donotpushnotification = !isChecked
            R.id.community_check -> {


                newConsent.communityactivities = isChecked

                checkIsEveryChecked()
            }
            R.id.all_check ->{
                newConsent.allpromotions = isChecked
                isDataSend = isChecked

            }
            R.id.promotion_check -> {
                newConsent.promotionsnews = isChecked

                checkIsEveryChecked()
            }
            R.id.survey_check -> {
                newConsent.surveysandresearch = isChecked

                checkIsEveryChecked()
            }
        }
        val objectString1 = Gson().toJson(oldConsent)
        val objectString2 = Gson().toJson(newConsent)
        if (objectString1 == objectString2) {
            _binding?.submitButton?.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.button_border_disable_text)
            _binding?.submitButton?.setTextColor(requireContext().resources.getColor(R.color.white_50))
            _binding?.submitButton?.isEnabled = false
        } else {
            _binding?.submitButton?.background =
                AppCompatResources.getDrawable(requireContext(), R.drawable.button_border_disable_text)
            _binding?.submitButton?.setTextColor(requireContext().resources.getColor(R.color.white))
            _binding?.submitButton?.isEnabled = true
        }
    }

    private fun checkIsEveryChecked() {
        if (_binding?.communityCheck?.isChecked == true && _binding?.surveyCheck?.isChecked == true && _binding?.promotionCheck?.isChecked == true) {
            if(!isDataSend)
                logEventCheckedNew(getString(R.string.community_activity)+"|"+getString(R.string.promotions_news)+"|"+getString(R.string.survey_reserch)+"|"+getString(R.string.all))
            _binding?.allCheck?.isChecked = true


        } else if (_binding?.allCheck?.isChecked == true && (_binding?.communityCheck?.isChecked == false || _binding?.surveyCheck?.isChecked == false || _binding?.promotionCheck?.isChecked == false)) {
            _binding?.allCheck?.isChecked = false
        }
    }

    private fun logEventCheckedNew(action: String) {
        val params = Bundle()
        params.putString("eventCategory", "Sign Up Form")
        params.putString("eventAction", action)
        REUtils.logGTMEvent(REConstants.KEY_SIGNUP_FORM_GTM, params)

    }
    private fun restartActivity() {
        val intent: Intent = Intent(activity, UserOnboardingActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}