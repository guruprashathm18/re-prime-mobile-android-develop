/*
 * Copyright (c) 2020 Bosch SoftTec GmbH All Rights Reserved.
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

package com.bosch.softtec.components.nephele.extensions

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

/**
 * @author Andre Weber
 * @since  2019-11-21
 */
internal val Response<*>.errorMessage: String?
    get() {
        val errorJson = errorBody()?.string() ?: return null

        return try {
            val jsonObject = JSONObject(errorJson)
            val errorCode = jsonObject.optInt("status")
            val errorMessage = jsonObject.optString("message")
            "HTTP $errorCode: $errorMessage"
        } catch (e: JSONException) {
            return errorJson
        }
    }
