/*
 * Copyright (c) 2019 Bosch SoftTec GmbH All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Bosch
 * SoftTec GmbH ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Bosch SoftTec (BSOT).
 *
 * BSOT MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. BSOT SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.royalenfield.reprime.ui.home.homescreen.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener

internal class CurrentLocationProvider(context: Context) {
    private val applicationContext = context.applicationContext

    @SuppressLint("LogNotTimber")
    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION]
    )
    fun requestCurrentLocation(locationCallback: Callback) {
        try {
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(applicationContext)
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnCompleteListener {
                    if (it.result != null) {
                            locationCallback.onLocationReceived(it.result)
                        }else{
                        Log.e( "requestCurrentLocation: ","null value")
                       /* val lastLoc = fusedLocationProviderClient.lastLocation
                        locationCallback.onLocationReceived(lastLoc.result)
                        Log.e( "requestCurrentLocation: ",lastLoc.result.toString())*/
                    }
                }.addOnFailureListener {
                    Log.w(TAG, "Could not get current location: $it")
                }
        }catch (e : Exception){
            e.stackTrace
        }
    }

    interface Callback {
        fun onLocationReceived(location: Location)
    }

    private companion object {
        private const val TAG = "CurrentLocationProvider"
    }
}
