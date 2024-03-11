package com.royalenfield.bluetooth.client;

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log


class PrefManager {

    var pref: SharedPreferences? = null
    var _context: Context? = null
    var editor: Editor? = null

    var PRIVATE_MODE = 0

    var manager: PrefManager? = null

    private val KEY_TRUSTED = "trusted"

    fun init(context: Context?) {
        _context = context
        pref = _context!!.getSharedPreferences(KEY_TRUSTED, PRIVATE_MODE)
        editor = pref?.edit()

      //  Log.e("test","some value pref = "+pref)
       // Log.e("test","some value editor = "+editor)
       // Log.e("test","some value _context = "+_context)
    }

    fun getInstance(): PrefManager? {
        if (manager == null) manager = PrefManager()
        return manager
    }

    fun setTrusted(isTrusted: Boolean) {
        editor!!.putBoolean(KEY_TRUSTED, isTrusted)
        editor!!.commit()
    }

    fun isTrusted(): Boolean {
        return pref!!.getBoolean(KEY_TRUSTED, false)
    }

    fun isStoredDevice(address: String): Boolean {
       // Log.e("retry", "trusted...." + pref!!.getBoolean("trusted", false))
        val storedAddress = pref!!.getString("address", "")
        return if (storedAddress != null && !storedAddress.isEmpty()) {
            storedAddress == address
        } else {
            false
        }
    }

}
