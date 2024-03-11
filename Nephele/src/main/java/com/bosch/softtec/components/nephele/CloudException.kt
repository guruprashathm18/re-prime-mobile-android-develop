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

package com.bosch.softtec.components.nephele

import com.bosch.softtec.components.nephele.extensions.errorMessage
import retrofit2.Response

/**
 * Exception, which will be thrown whenever an error occurs while interacting with the Cloud Services. It contains
 * information about http status code and http messages.
 *
 * @author Andre Weber
 * @since 2019-11-15
 */
class CloudException : RuntimeException {
    /**
     * The full HTTP response.
     */
    @Transient
    val response: Response<*>?

    /**
     * HTTP status code.
     */
    val httpStatusCode: Int?
        get() = response?.code()

    /**
     * HTTP status message or null if unknown.
     */
    val httpErrorMessage: String?
        get() = message

    /**
     * Constructor for usage with an existing [Response].
     */
    constructor(
        response: Response<*>,
        cause: Throwable? = null
    ) : super(response.errorMessage, cause) {
        this.response = response
    }

    /**
     * Constructor for usage with an error message and throwable.
     */
    constructor(
        message: String?,
        response: Response<*>? = null,
        cause: Throwable? = null
    ) : super(message, cause) {
        this.response = response
    }
}


