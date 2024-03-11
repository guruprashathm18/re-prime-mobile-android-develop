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

sealed class AuthorizationConfiguration

/**
 * NoAuthorizationConfiguration, will send a username and scope in each request.
 *
 * @property userName the username used to sign the request
 * @property scope the scope used to sign the request
 */
data class NoAuthConfiguration(
    val userName: String,
    val scope: String
) : AuthorizationConfiguration()

/**
 * OAuthAuthorizationConfiguration, will send a jsonWebToken in the authorization of each request.
 *
 * @property jsonWebToken the jsonWebToken to use for authorization.
 */
data class OAuthConfiguration(val jsonWebToken: String) : AuthorizationConfiguration()
