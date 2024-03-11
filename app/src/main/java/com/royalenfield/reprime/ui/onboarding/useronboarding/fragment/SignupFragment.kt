package com.royalenfield.reprime.ui.onboarding.useronboarding.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.royalenfield.firebase.fireStore.FirestoreManager
import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.base.REBaseFragment
import com.royalenfield.reprime.databinding.FragmentSignupBinding
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity
import com.royalenfield.reprime.ui.riderprofile.activity.REWebViewActivity
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan
import com.royalenfield.reprime.utils.RELog
import com.royalenfield.reprime.utils.REUtils
import java.text.SimpleDateFormat
import java.util.*


class SignupFragment : REBaseFragment(), OnClickListener, TextWatcher,OnFocusChangeListener {
    private var _binding: FragmentSignupBinding? = null
    private var firstName = ""
    private var lastName = ""
    private var eMail = ""
    private var dob = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val params = Bundle()
        params.putString("screenname", "Signup Personal Details")
        REUtils.logGTMEvent(REConstants.KEY_SCREEN_GTM, params)
        initViews()
        return _binding?.root
    }

    private fun initViews() {
        FirestoreManager.getInstance().getHtmlData(activity, REApplication.getInstance().Country)

        _binding?.imageViewCalendar?.setOnClickListener(this)
        _binding?.btnCreateAccount?.setOnClickListener(this)
        _binding?.textNext?.background =
            requireContext().getDrawable(R.drawable.shape_global_login_button)
        _binding?.textNext?.setTextColor(requireContext().resources.getColor(R.color.white))
        _binding?.textNext?.isEnabled = false
        _binding?.textNext?.setOnClickListener(this)
        _binding?.topview?.ivBack?.setOnClickListener(this)
        _binding?.topview?.tvMobileNumber?.setOnClickListener(this)
        _binding?.topview?.tvMobileNumber?.text = (activity as UserOnboardingActivity).getCallingCode()+" "+(activity as UserOnboardingActivity).getMobileNumber()
           if (REUtils.getFeatures().defaultFeatures != null && REUtils.getFeatures().defaultFeatures.showConsentControlsV2.equals(
                FEATURE_ENABLED, ignoreCase = true
            )
        ) {
            _binding?.privacypolicyCheck?.isChecked = true
            _binding?.privacypolicyCheck?.visibility = View.GONE
            _binding?.privacypolicyTv?.visibility = View.GONE
            _binding?.btnCreateAccount?.visibility = View.GONE
            _binding?.textNext?.visibility = View.VISIBLE
            _binding?.btnCreateAccount?.visibility = View.GONE
        } else {
            _binding?.textNext?.visibility = View.GONE
            _binding?.btnCreateAccount?.visibility = View.VISIBLE
        }
        checkRequiredFields()
        _binding?.edFirstName?.addTextChangedListener(this)
        _binding?.edLastName?.addTextChangedListener(this)
        _binding?.edEmail?.addTextChangedListener(this)
        _binding?.edFirstName?.onFocusChangeListener = this
        _binding?.edLastName?.onFocusChangeListener=this
        _binding?.edEmail?.onFocusChangeListener=this
        _binding?.edDOB?.addTextChangedListener(this)
        _binding?.edEmail?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    _binding?.imageViewCalendar?.performClick()
                    return true
                }
                return false
            }
        })

        //This method is for custom text click
        customTextView(_binding?.privacypolicyTv)
        _binding?.privacypolicyCheck?.setOnCheckedChangeListener { buttonView, isChecked ->
            getTexts()
            checkRequiredFields()
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
                    Objects.requireNonNull(activity)
                        ?.overridePendingTransition(R.anim.slide_up, R.anim.anim_exit)
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

        if (firstName.trimEnd().isNotEmpty() && lastName.trimEnd().isNotEmpty() && dob != null && dob.isNotEmpty() && eMail != null && eMail.isNotEmpty() && _binding?.privacypolicyCheck?.isChecked == true
        ) {
            _binding?.btnCreateAccount?.background =
                requireContext().getDrawable(R.drawable.shape_global_login_button)
            _binding?.btnCreateAccount?.setTextColor(requireContext().resources.getColor(R.color.white))
            _binding?.btnCreateAccount?.isEnabled = true
            _binding?.textNext?.background =
                requireContext().getDrawable(R.drawable.shape_global_login_button)
            _binding?.textNext?.setTextColor(requireContext().resources.getColor(R.color.white))
            _binding?.textNext?.isEnabled = true
        } else {
            _binding?.btnCreateAccount?.background =
                requireContext().getDrawable(R.drawable.button_border_disable_text)
            _binding?.btnCreateAccount?.setTextColor(requireContext().resources.getColor(R.color.white_50))
            _binding?.btnCreateAccount?.isEnabled = false
            _binding?.textNext?.background =
                requireContext().getDrawable(R.drawable.button_border_disable_text)
            _binding?.textNext?.setTextColor(requireContext().resources.getColor(R.color.white_50))
            _binding?.textNext?.isEnabled = false
        }

    }

    private fun getTexts() {
        firstName = _binding?.edFirstName?.text.toString()
        lastName = _binding?.edLastName?.text.toString()
        eMail = _binding?.edEmail?.text.toString()
        dob = _binding?.edDOB?.text.toString()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageView_calendar -> {
                try {
                    _binding?.edEmail?.clearFocus()
                    _binding?.edFirstName?.clearFocus()
                    _binding?.edLastName?.clearFocus()
                    showDatePickerDialog()
                } catch (e: java.lang.Exception) {
                    RELog.e(e)
                }
            }
            R.id.text_next->{
                _binding?.edEmail?.clearFocus()
                _binding?.edFirstName?.clearFocus()
                _binding?.edLastName?.clearFocus()
                if(REUtils.isValidEmailId(eMail)) {
                    var params = Bundle()
                    params.putString("eventCategory", "Sign Up Form")
                    params.putString("eventAction", "Next click")
                    REUtils.logGTMEvent(REConstants.KEY_SIGNUP_FORM_GTM, params)
                    _binding?.tvEmailError?.visibility = View.GONE
                    ( activity as UserOnboardingActivity).gotoConsentFragment(firstName,lastName,dob,eMail)
                }
                else
                    _binding?.tvEmailError?.visibility=View.VISIBLE

            }
            R.id.iv_back->{
                restartActivity()
            }
            R.id.tvMobileNumber->{

               // activity?.onBackPressed()
                restartActivity()
            }
            R.id.btnCreateAccount->{

                if(REUtils.isValidEmailId(eMail)) {
                    _binding?.tvEmailError?.visibility = View.GONE
                    //Clear all the model store.
                    REApplication.getInstance().clearAllModelStore()
                    (activity as UserOnboardingActivity).performSignup(null)
                }
                else
                    _binding?.tvEmailError?.visibility=View.VISIBLE

                Log.e("SIGNUP", "BUTTON CLICK")

            }
        }
    }

    private fun restartActivity() {
        val intent: Intent = Intent(activity, UserOnboardingActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }


    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        try {
            getTexts()
            checkRequiredFields()
        } catch (e: Exception) {
            RELog.e(e)
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun showDatePickerDialog() {
        // Get Current Date
        val c = Calendar.getInstance()
        var mYear: Int
        var mMonth: Int
        val mDay: Int
        val date: String = _binding?.edDOB?.text.toString().trim()
        if (date != null && date.length > 0) {

//if you  have previous set date
            val data = date.split("-").toTypedArray()
            mYear = data[0].toInt()
            mMonth = data[1].toInt()
            mMonth -= 1
            mDay = data[2].toInt()
        } else {

//if you dont have previous set date then display current date
            mYear = c[Calendar.YEAR]
            mYear -= 18
            mMonth = c[Calendar.MONTH]
            mDay = c[Calendar.DAY_OF_MONTH]
        }
        if (activity != null) {
            val datePickerDialog = DatePickerDialog(
                requireActivity(), R.style.CustomDatePickerDialogTheme,
                { view, year, monthOfYear, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal[Calendar.YEAR] = year
                    cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                    cal[Calendar.YEAR] = year
                    cal[Calendar.MONTH] = monthOfYear
                    val format1 = SimpleDateFormat("yyyy-MM-dd")
                    System.out.println(cal.time)
                    // Output "Wed Sep 26 14:23:28 EST 2012"
                    val formattedDate: String = format1.format(cal.time)
                    println(formattedDate)
                    // Output "2012-09-26"

                    //    System.out.println(format1.parse(formatted));
                    _binding?.edDOB?.setText(formattedDate)
                    logEventFocus("Date of birth")
                }, mYear, mMonth, mDay
            )
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -18)
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.show()
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
            when (v?.id) {
               R.id.edFirstName -> if (!hasFocus){
                   if(_binding?.edFirstName?.text?.trim()?.isNotEmpty() == true)
                   logEventFocus("First Name")
               }
                R.id.edLastName -> if (!hasFocus) {
                    if(_binding?.edLastName?.text?.trim()?.isNotEmpty() == true)
                    logEventFocus("Last Name")
                }
                R.id.edEmail -> if (!hasFocus){
                    if(_binding?.edEmail?.text?.trim()?.isNotEmpty() == true)
                    logEventFocus("Email")
                }


        }
    }
    private fun logEventFocus(action: String) {
        val params = Bundle()
        params.putString("eventCategory", "Sign Up Form")
        params.putString("eventAction", action)
        REUtils.logGTMEvent(REConstants.KEY_SIGNUP_FORM_GTM, params)
    }
}