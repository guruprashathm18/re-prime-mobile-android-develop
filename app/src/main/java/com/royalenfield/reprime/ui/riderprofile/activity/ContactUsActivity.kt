package com.royalenfield.reprime.ui.riderprofile.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.royalenfield.reprime.R
import com.royalenfield.reprime.base.REBaseActivity
import com.royalenfield.reprime.databinding.ActivityContactUsBinding
import com.royalenfield.reprime.ui.custom.views.TitleBarView
import com.royalenfield.reprime.utils.REConstants.*
import com.royalenfield.reprime.utils.REUtils


class ContactUsActivity: REBaseActivity(), TitleBarView.OnNavigationIconClickListener {

    private lateinit var binding : ActivityContactUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=ActivityContactUsBinding.inflate(layoutInflater)

        val paramsScr = Bundle()
        paramsScr.putString("screenname", "Contact Us screen")
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr)
        initView()
    }

    private fun initView() {
       binding.titleBarViewAppSetting.bindData(
            this,
            R.drawable.back_arrow,
            getResources().getString(R.string.text_contact_us).toUpperCase()
        )
        binding.tvEmail.setText(EXTRA_EMAIL)
        binding.tvEmail.setOnClickListener{
            val params = Bundle()
            params.putString("eventCategory", "Settings")
            params.putString("eventLabel", "Email clicked")
            params.putString("eventAction", "Contact Us")
            REUtils.logGTMEvent(KEY_SETTINGS_GTM, params)
          sendEmail()
        }
    }

    private fun sendEmail() {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(EXTRA_EMAIL))
        //        i.putExtra(Intent.EXTRA_SUBJECT, REConstants.EXTRA_SUBJECT);
//        i.putExtra(Intent.EXTRA_TEXT, REConstants.EXTRA_TEXT);
        try {
            startActivity(Intent.createChooser(i, getString(R.string.text_send_mail)))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this,
                R.string.err_not_installed_email_app,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right)
    }

    override fun onNavigationIconClick() {
        val params = Bundle()
        params.putString("eventCategory", "Settings")
        params.putString("eventLabel", "Close Clicked")
        params.putString("eventAction", "Contact Us")
        REUtils.logGTMEvent(KEY_SETTINGS_GTM, params)
        finish()
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right)
    }


}