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

package com.bosch.softtec.components.nephele.cloud

import android.util.Log
import com.bosch.softtec.components.core.models.NetworkTimeoutConfiguration
import com.bosch.softtec.components.nephele.*
import com.bosch.softtec.micro.tenzing.client.ApiClient
import com.bosch.softtec.micro.tenzing.client.api.ShareTripApi
import com.bosch.softtec.micro.tenzing.client.api.TripApi
import com.bosch.softtec.micro.tenzing.client.model.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.apache.oltu.oauth2.common.exception.OAuthProblemException
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Provides static access to the cloud (currently Tenzing only) API.
 *
 * @author Dominic Sudy
 * @since  2019-05-16
 */
internal class CloudApi(
    cloudConfiguration: CloudConfiguration
) {
    private var apiProvider = ApiProvider(cloudConfiguration)

    var networkTimeoutConfiguration: NetworkTimeoutConfiguration? // delegates get/set to apiProvider
        get() = apiProvider.networkTimeoutConfiguration
        set(value) {
            apiProvider.networkTimeoutConfiguration = value
        }


    /**
     * Add a new trip
     * Creates a new trip with the given data and sends it to the client.
     *
     * @param authorizationConfiguration the authorizationConfiguration to use
     * @param tripBodyWithImageUrl Represents a request object that adds a new trip. (required)
     *
     * @return the corresponding Trip on success, null otherwise
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun addTrip(
        authorizationConfiguration: AuthorizationConfiguration?,
        tripBodyWithImageUrl: TripBodyWithImageUrl
    ): String {
        val api = apiProvider.provideTripApi(authorizationConfiguration)

        return handleExceptions {
            val request = api.tripPost(tripBodyWithImageUrl)

            val response = request.execute()
            if (!response.isSuccessful) throw CloudException(response)

            val raw = response.raw()
            val location = raw.header("location") ?: throw CloudException(ERROR_HEADER, response)
            extractId(location)
        }
    }

    /**
     * Find trip by ID
     * Returns all data of a specific trip.
     *
     * @param authorizationConfiguration the authorizationConfiguration to use
     * @param tripId The id of the trip.  Size must be between 36 and 36 inclusive. (required)
     *
     * @return the corresponding Trip on success, null otherwise
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun fetchTripById(
        authorizationConfiguration: AuthorizationConfiguration?,
        tripId: String
    ): Trip {
        val api = apiProvider.provideTripApi(authorizationConfiguration)

        return handleExceptions {
            val request = api.tripGet(tripId)

            val response = request.execute()
            if (!response.isSuccessful) throw CloudException(response)

            response.body() ?: throw CloudException(ERROR_RESPONSE_BODY_IS_NULL, response)
        }
    }

    /**
     * Updates an existing trip with the [JSON patch](https://tools.ietf.org/html/rfc6902) methods.
     *
     * @param authorizationConfiguration the authorizationConfiguration to use
     * @param tripId    The id of the trip.  Size must be between 36 and 36 inclusive. (required)
     * @param jsonPatches Represents a request object to update an existing trip. (required)
     *
     * @return true on success
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun updateTrip(
        authorizationConfiguration: AuthorizationConfiguration?,
        tripId: String,
        jsonPatches: List<JsonPatch>
    ): Boolean {
        val api = apiProvider.provideTripApi(authorizationConfiguration)

        return handleExceptions {
            val request = api.tripPatch(tripId, jsonPatches)

            val response = request.execute()
            if (!response.isSuccessful) throw CloudException(response)

            response.isSuccessful
        }
    }

    /**
     * Delete trip by ID
     * Deletes a trip by a given ID and all resources related to that trip (e.g. all images).
     *
     * @param authorizationConfiguration the authorizationConfiguration to use
     * @param tripId The id of the trip.  Size must be between 36 and 36 inclusive. (required)
     *
     * @return the corresponding response on success or null otherwise
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun deleteTrip(
        authorizationConfiguration: AuthorizationConfiguration?,
        tripId: String
    ): Boolean {
        val api = apiProvider.provideTripApi(authorizationConfiguration)

        return handleExceptions {
            val request = api.tripDelete(tripId)

            val response = request.execute()
            if (!response.isSuccessful) throw CloudException(response)

            response.isSuccessful
        }
    }

    /**
     * Gets all &#x60;PRIVATE&#x60; and/or &#x60;SHARED&#x60; trips for the requested user.
     * &#x60;PUBLIC&#x60; trips are not returned, corresponding filters are ignored without errors.
     *
     * @param authorizationConfiguration the authorizationConfiguration to use
     * @param limit     Limits the number of trips in the result. (optional, default to 10d)
     * @param next      (optional)
     * @param visibilityTypes Filters the result by the given trip visibility type. Multiple types can be specified
     * separated by a comma. (optional, default to new ArrayList&lt;VisibilityType&gt;())
     *
     * @return InlineResponse200
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun fetchAllTrips(
        authorizationConfiguration: AuthorizationConfiguration?,
        limit: Int? = null,
        next: String? = null,
        visibilityTypes: List<VisibilityType>? = null
    ): InlineResponse200 {
        val api = apiProvider.provideTripApi(authorizationConfiguration)
        val bigDecimalLimit = limit?.toBigDecimal()

        return handleExceptions {
            val request = api.tripsGet(bigDecimalLimit, next, visibilityTypes)

            val response = request.execute()
            if (!response.isSuccessful) throw CloudException(response)

            response.body() ?: throw CloudException(ERROR_RESPONSE_BODY_IS_NULL, response)
        }
    }

    /**
     * Searches for trips based on the given search context.
     *
     * @param authorizationConfiguration the authorizationConfiguration to use
     * @param searchContext Represents a request object to specify parameters for searching trips. (required)
     * @param limit         Limits the number of trips in the result. (optional, default to 10d)
     * @param next          (optional)
     * @param visibilityTypes     Filters the result by the given trip visibility type. Multiple types can be specified
     * separated by a comma. (optional, default to new ArrayList&lt;VisibilityType&gt;())
     *
     * @return InlineResponse200
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun searchTrips(
        authorizationConfiguration: AuthorizationConfiguration?,
        searchContext: SearchContext,
        limit: Int? = null,
        next: String? = null,
        visibilityTypes: List<VisibilityType>? = null
    ): InlineResponse200 {
        val api = apiProvider.provideTripApi(authorizationConfiguration)
        val bigDecimalLimit = limit?.toBigDecimal()

        return handleExceptions {
            val request = api.tripsSearch(searchContext, bigDecimalLimit, next, visibilityTypes)

            val response = request.execute()
            if (!response.isSuccessful) throw CloudException(response)

            response.body() ?: throw CloudException(ERROR_RESPONSE_BODY_IS_NULL, response)
        }
    }

    /**
     * Share trip
     * Share trip based on the trip ID and defined expiration.
     *
     * @param authorizationConfiguration the authorizationConfiguration to use
     * @param shareTicketBody Represents a request object to specify parameters for sharing trips. (required)
     *
     * @return ShareTicket
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun shareTrip(
        authorizationConfiguration: AuthorizationConfiguration?,
        shareTicketBody: ShareTicketBody
    ): ShareTicket {
        val api = apiProvider.provideShareTripApi(authorizationConfiguration)
        return handleExceptions {
            val request = api.shareTrip(shareTicketBody)

            val response = request.execute()
            if (!response.isSuccessful) throw CloudException(response)

            response.body() ?: throw CloudException(ERROR_RESPONSE_BODY_IS_NULL, response)
        }
    }

    /**
     * Redeem shared trip
     * Redeem shared ticket based on the trip ID and defined expiration.
     *
     * @param authorizationConfiguration the authorizationConfiguration to use
     * @param shareTicketCode Represents a request object to specify parameters to redeem a shared trips. (required)
     *
     * @return the tripId of the successfully added trip
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     * @throws OAuthProblemException when trying to redeem the shareTicketCode from the same account, which shared it
     */
    fun redeemSharedTrip(
        authorizationConfiguration: AuthorizationConfiguration?,
        shareTicketCode: ShareTicketCode
    ): String {
        val api = apiProvider.provideShareTripApi(authorizationConfiguration)
        return handleExceptions {
            val request = api.redeemSharedTrip(shareTicketCode)

            val response = request.execute()
            if (!response.isSuccessful) throw CloudException(response)

            val rawResponse = response.raw()
            val location = rawResponse.header("location") ?: throw CloudException(ERROR_HEADER, response)
            extractId(location)
        }
    }

    fun extractId(location: String): String {
        val splittedLocation = location.split("/")
        return splittedLocation.last()
    }

    private inline fun <R> handleExceptions(block: () -> R): R {
        try {
            return block()
        } catch (e: IOException) {
            throw CloudException(message = e.message, cause = e)
        } catch (e: HttpException) {
            throw CloudException(e.response(), e)
        }
    }

    // encapsulated for testing reasons
    internal class ApiProvider(
        private val cloudConfiguration: CloudConfiguration
    ) {
        fun provideTripApi(
            authorizationConfiguration: AuthorizationConfiguration?,
        ) = provideApi(authorizationConfiguration, TripApi::class.java)

        fun provideShareTripApi(
            authorizationConfiguration: AuthorizationConfiguration?,
        ) = provideApi(authorizationConfiguration, ShareTripApi::class.java)

        var networkTimeoutConfiguration: NetworkTimeoutConfiguration? = null

        /**
         * Returns an API interface we can use to do Retrofit requests on
         * while also adding the necessary access token from the client [authorizationConfiguration] to all requests.
         */
        private fun <T> provideApi(
            authorizationConfiguration: AuthorizationConfiguration?,
            clazz: Class<T>
        ): T {
            val apiClient = ApiClient()

            networkTimeoutConfiguration?.let {
                val okBuilder = apiClient.okBuilder
                configureTimeouts(it, okBuilder)
            }

            val baseUrl = cloudConfiguration.backendConfiguration.backendUrl
            authorizationConfiguration?.let {
                val authInterceptor = AuthInterceptor(it)
                apiClient.addAuthorization(AUTH_NAME, authInterceptor)
            }

            apiClient.adapterBuilder.baseUrl(baseUrl)

            return apiClient.createService(clazz)
        }

        private fun configureTimeouts(
            networkTimeoutConfiguration: NetworkTimeoutConfiguration,
            okBuilder: OkHttpClient.Builder
        ) {
            val disabled: Long = 0

            val connectTimeoutMillis = networkTimeoutConfiguration.connectTimeout?.toMillis() ?: disabled
            okBuilder.connectTimeout(connectTimeoutMillis, TimeUnit.MILLISECONDS)

            val readTimeoutMillis = networkTimeoutConfiguration.readTimeout?.toMillis() ?: disabled
            okBuilder.readTimeout(readTimeoutMillis, TimeUnit.MILLISECONDS)

            val writeTimeoutMillis = networkTimeoutConfiguration.writeTimeout?.toMillis() ?: disabled
            okBuilder.writeTimeout(writeTimeoutMillis, TimeUnit.MILLISECONDS)

            val callTimeoutMillis = networkTimeoutConfiguration.callTimeout?.toMillis() ?: disabled
            okBuilder.callTimeout(callTimeoutMillis, TimeUnit.MILLISECONDS)
        }
    }

    private class AuthInterceptor(private val authorizationConfiguration: AuthorizationConfiguration) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()

            val requestBuilder = request.newBuilder()
            when (authorizationConfiguration) {
                is NoAuthConfiguration -> {
                    val userName = authorizationConfiguration.userName
                    val scope = authorizationConfiguration.scope

                    requestBuilder
                        .header(HEADER_USERNAME, userName)
                        .header(HEADER_SCOPE, scope)
                }

                is OAuthConfiguration -> {
                    val jsonWebToken = authorizationConfiguration.jsonWebToken

                    requestBuilder
                        .header(HEADER_AUTHORIZATION, jsonWebToken)
                }

                else -> {
                    Log.w(TAG, "Unhandled AuthorizationConfiguration: $authorizationConfiguration")
                }
            }

            return chain.proceed(requestBuilder.build())
        }

        private companion object {
            private const val HEADER_AUTHORIZATION = "Authorization"
            private const val HEADER_USERNAME = "username"
            private const val HEADER_SCOPE = "scope"
        }
    }

    private companion object {
        private val TAG = CloudApi::class.java.simpleName.take(23)

        /**
         * Name of the json web token provider in the Swagger spec file.
         */
        private const val AUTH_NAME = "Tenzing"

        private const val ERROR_HEADER = "Header does not contain value for 'location'."
        private const val ERROR_RESPONSE_BODY_IS_NULL = "ResponseBody is null."
    }
}
