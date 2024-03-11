package com.royalenfield.reprime.ui.riderprofile.activity

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.base.REBaseActivity
import com.royalenfield.reprime.databinding.ActivityAppSettingBinding
import com.royalenfield.reprime.databinding.BottomSheetDialogBinding
import com.royalenfield.reprime.databinding.BottomSheetDialogDistanceBinding
import com.royalenfield.reprime.preference.REPreference
import com.royalenfield.reprime.ui.custom.views.TitleBarView
import com.royalenfield.reprime.utils.REConstants.*
import com.royalenfield.reprime.utils.REUtils
import java.util.*

class AppSettingActivity: REBaseActivity(), TitleBarView.OnNavigationIconClickListener {
   lateinit var selectedValue:String
   private lateinit var binding: ActivityAppSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this, R.layout.activity_app_setting)
        binding.lifecycleOwner = this

        val paramsScr = Bundle()
        paramsScr.putString("screenname", "App Settings screen")
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr)
        initView()
    }

    private fun initView() {
        binding.titleBarViewAppSetting.bindData(
            this,
            R.drawable.back_arrow,
            getResources().getString(R.string.app_setting).uppercase(Locale.getDefault())
        )

        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        val selectedValue= REPreference.getInstance().getString(
            REApplication.getAppContext(), KEY_COMMA_OR_POINT,
            KEY_POINT
        )
        if(selectedValue.equals(KEY_POINT))
            binding.tvTitleValue.text = resources.getString(R.string.decimal_point)
        else
            binding.tvTitleValue.text = resources.getString(R.string.decimal_comma)

        binding.tvContact.setOnClickListener{
            intent = Intent(this, ContactUsActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit)
        }
        binding.tvTitleValue.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val bottomSheetBinding = DataBindingUtil.inflate<BottomSheetDialogBinding>(
                layoutInflater, R.layout.bottom_sheet_dialog, null, false
            )
            bottomSheetDialog.setContentView(bottomSheetBinding.root)
            bottomSheetBinding.txtComma.setOnClickListener {
                val params = Bundle()
                params.putString("eventCategory", "App Settings")
                params.putString("eventLabel", resources.getString(R.string.decimal_comma))
                params.putString("eventAction", "Decimal Separator")
                REUtils.logGTMEvent(KEY_SETTINGS_GTM, params)
                bottomSheetDialog.dismiss()
                REPreference.getInstance().putString(
                    REApplication.getAppContext(), KEY_COMMA_OR_POINT,
                    KEY_COMMA
                )
                binding.tvTitleValue.text = resources.getString(R.string.decimal_comma)
                val intent = Intent("notify_setting")
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
            bottomSheetBinding.txtDot.setOnClickListener {
                val params = Bundle()
                params.putString("eventCategory", "App Settings")
                params.putString("eventLabel", resources.getString(R.string.decimal_point))
                params.putString("eventAction", "Decimal Separator")
                REUtils.logGTMEvent(KEY_SETTINGS_GTM, params)
                bottomSheetDialog.dismiss()
                REPreference.getInstance().putString(
                    REApplication.getAppContext(), KEY_COMMA_OR_POINT,
                    KEY_POINT
                )
                binding.tvTitleValue.text = resources.getString(R.string.decimal_point)
                val intent = Intent("notify_setting")
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
            bottomSheetDialog.show()

        }
        var selectedValueDist= REPreference.getInstance().getString(
            REApplication.getAppContext(), KEY_KM_OR_MILES,
            REUtils.getAppSettings().distanceUnit
        )
        if(selectedValueDist.equals(KEY_KMS))
            binding.tvDistValue.text = resources.getString(R.string.unit_kms)
        else
            binding.tvDistValue.text = resources.getString(R.string.unit_miles)

        binding.tvDistValue.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val bottomSheetBinding = DataBindingUtil.inflate<BottomSheetDialogDistanceBinding>(
                layoutInflater, R.layout.bottom_sheet_dialog_distance, null, false
            )
            bottomSheetDialog.setContentView(bottomSheetBinding.root)

            bottomSheetBinding.txtKms.setOnClickListener {
                val params = Bundle()
                params.putString("eventCategory", "App Settings")
                params.putString("eventLabel", "Kilometers")
                params.putString("eventAction", "Distance Unit")
                REUtils.logGTMEvent(KEY_SETTINGS_GTM, params)
                bottomSheetDialog.dismiss()
                REPreference.getInstance().putString(
                    REApplication.getAppContext(), KEY_KM_OR_MILES,
                    KEY_KMS
                )
                binding.tvDistValue.text = resources.getString(R.string.unit_kms)
            }
            bottomSheetBinding.txtMiles.setOnClickListener {
                val params = Bundle()
                params.putString("eventCategory", "App Settings")
                params.putString("eventLabel", "Miles")
                params.putString("eventAction", "Distance Unit")
                REUtils.logGTMEvent(KEY_SETTINGS_GTM, params)
                bottomSheetDialog.dismiss()
                REPreference.getInstance().putString(
                    REApplication.getAppContext(), KEY_KM_OR_MILES,
                    KEY_MILES
                )
                binding.tvDistValue.text = resources.getString(R.string.unit_miles)
            }
            bottomSheetDialog.show()

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right)
    }

    override fun onNavigationIconClick() {
        val params = Bundle()
        params.putString("eventCategory", "App Settings")
        params.putString("eventAction", "Close clicked")
        REUtils.logGTMEvent(KEY_SETTINGS_GTM, params)
        finish()
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right)
    }


}