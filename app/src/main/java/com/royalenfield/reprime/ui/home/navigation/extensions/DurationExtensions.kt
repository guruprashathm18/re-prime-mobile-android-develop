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

package com.royalenfield.extensions

import android.content.Context
import com.bosch.softtec.components.core.models.Duration
import com.royalenfield.reprime.R

/**
 * @author Martin Hellwig
 * @since  2019-01-04
 */
/**
 * Generates a UI-friendly representation of the duration.
 *
 * @param context
 *      needed to get some string resources
 *
 * @return the UI-friendly String with either minutes or hours and minutes.
 */
fun Duration.prettify(context: Context): String
{
    var durationInSeconds = toSeconds()

    // Do round to next minute
    durationInSeconds += 59

    // More than 60 min?
    val minutes = durationInSeconds / 60
    val hours = minutes / 60
    if (minutes >= 60)
    {
        val stringId = R.string.duration_x_hour_y_min
        return context.getString(stringId, hours.toInt(), (minutes % 60).toInt())
    }

    val stringId = R.string.duration_x_min
    return context.getString(stringId, minutes.toInt())
}
