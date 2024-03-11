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

/**
 * The BackendConfiguration specifies with which backend to communicate. The backend needs to run the corresponding
 * Tenzing service.
 *
 * @author Andre Weber
 * @since  2019-07-29
 */
data class BackendConfiguration(
    /**
     * The url of the backend without trailing slash. e.g.  https://your.path.to.the.backend.com
     */
    val backendUrl: String
)
