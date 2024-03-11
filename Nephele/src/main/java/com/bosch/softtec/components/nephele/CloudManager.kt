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

import com.bosch.softtec.components.core.models.NetworkTimeoutConfiguration
import com.bosch.softtec.components.core.models.breadcrumb.BreadcrumbTrail
import com.bosch.softtec.components.nephele.cloud.CloudApi
import com.bosch.softtec.micro.tenzing.client.model.*

/**
 * The CloudManager can be used to log into the Cloud server and use the corresponding api provided by it.
 *
 * @author Andre Weber
 * @since  2019-07-29
 */
class CloudManager private constructor(cloudConfiguration: CloudConfiguration) {
    private var cloudApi = CloudApi(cloudConfiguration)

    /**
     * AuthorizationConfiguration used to authorize against the server.
     *
     * @see NoAuthConfiguration
     * @see OAuthConfiguration
     */
    var authorizationConfiguration: AuthorizationConfiguration? = null

    /**
     * ConnectionTimeoutConfiguration to specify timeouts used while communicating with the Cloud Server.
     */
    var networkTimeoutConfiguration: NetworkTimeoutConfiguration? // delegates get/set to CloudApi
        get() = cloudApi.networkTimeoutConfiguration
        set(value) {
            cloudApi.networkTimeoutConfiguration = value
        }

    // region Trip

    /**
     * Uploads the specified [tripBodyWithImageUrl] to the server and returns the response from the server and return
     * the tripId of the uploaded trip or null if an error occurred while uploading.
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun uploadTrip(tripBodyWithImageUrl: TripBodyWithImageUrl): String {
        return cloudApi.addTrip(authorizationConfiguration, tripBodyWithImageUrl)
    }

    /**
     * Gets all PRIVATE and/or SHARED trips for the requested user. PUBLIC trips are not returned, corresponding
     * filters are ignored without errors.
     * This method delivers only metadata of the trips, meaning that track and directions are not set at all. If track
     * and directions data are needed they can be retrieved by fetching them using [CloudManager.fetchTripById] with
     * the corresponding tripId.
     *
     * @param limit
     *          Limits the number of trips in the result. Default value is 10.
     * @param next
     *          The cursor to get the next result set which the next call of this endpoint.
     * @param visibilityTypes
     *          Filters the result by the given trip visibility type. Multiple types can be specified separated by a
     *          comma. Only the specified types are returned.
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun fetchAllTrips(
        limit: Int? = null, // @IntRange(from = 1L, to = 100L)
        next: String? = null, // @Size(24)
        visibilityTypes: List<VisibilityType>? = null
    ): InlineResponse200 {
        return cloudApi.fetchAllTrips(authorizationConfiguration, limit, next, visibilityTypes)
    }

    /**
     * Fetches the [BreadcrumbTrail], which is associated to the specified [tripId] or returns null if it does
     * not exist.
     *
     * @param tripId
     *          The id of the trip. Size must be between 36 and 36 inclusive. (required)
     * @return the corresponding Trip on success or null otherwise.
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun fetchTripById(tripId: String): Trip {
        return cloudApi.fetchTripById(authorizationConfiguration, tripId)
    }

    /**
     * Searches for trips based on the given search context.
     *
     * @param limit
     *          Limits the number of trips in the result. Default value is 10.
     * @param next
     *          The cursor to get the next result set which the next call of this endpoint.
     * @param visibilityTypes
     *          Filters the result by the given trip visibility type. Multiple types can be specified separated by a
     *          comma. Only the specified types are returned.
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     *
     * @see SearchContextQuery
     * @see SearchContextStartStop
     */
    fun searchTrips(
        searchContext: SearchContext,
        limit: Int? = null, // @IntRange(from = 1L, to = 100L)
        next: String? = null, // @Size(24)
        visibilityTypes: List<VisibilityType>? = null
    ): InlineResponse200 {
        return cloudApi.searchTrips(authorizationConfiguration, searchContext, limit, next, visibilityTypes)
    }

    /**
     * Updates an existing trip with the JSON patch methods.
     *
     * @param tripId The id of the trip the image belongs to.
     * @param jsonPatches list of jsonPatches.
     *
     * @return true if updating the trip was successful, false otherwise
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun updateTrip(
        tripId: String, // @Size(24)
        jsonPatches: List<JsonPatch>
    ): Boolean {
        return cloudApi.updateTrip(authorizationConfiguration, tripId, jsonPatches)
    }

    /**
     * Deletes the [BreadcrumbTrail], which is associated to the specified [tripId].
     *
     * @param tripId
     *          The id of the trip. (required)
     * @return true if deletion was successful, false otherwise
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun deleteTripById(
    tripId: String // @Size(24)
    ): Boolean {
        return cloudApi.deleteTrip(authorizationConfiguration, tripId)
    }

    // endregion

    // region Trip Sharing //

    /**
     * Share trip
     * Share trip based on the trip ID and defined expiration.
     *
     * @param shareTicketBody Represents a request object to specify parameters for sharing trips. (required)
     *
     * @return ShareTicket
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun shareTrip(
        shareTicketBody: ShareTicketBody
    ): ShareTicket {
        return cloudApi.shareTrip(authorizationConfiguration, shareTicketBody)
    }

    /**
     * Redeem shared trip
     * Redeem shared ticket based on the trip ID and defined expiration. It is not possible to redeem a code from the
     * same account, which shared the code.
     *
     * @param shareTicketCode Represents a request object to specify parameters to redeem a shared trips. (required)
     *
     * @return the tripId of the successfully added trip
     *
     * @throws CloudException will be thrown when an error occurs while retrieving the result e.g. when the response
     * from the server is not successful.
     */
    fun redeemSharedTrip(
        shareTicketCode: ShareTicketCode
    ): String {
        return cloudApi.redeemSharedTrip(authorizationConfiguration, shareTicketCode)
    }
    // endregion

    companion object {
        /**
         * Creates a new instance of the CloudManager with the specified CloudConfiguration and the specified
         * jsonWebToken used for authorization.
         */
        fun createInstance(
            cloudConfiguration: CloudConfiguration
        ): CloudManager {
            return CloudManager(cloudConfiguration)
        }
    }
}
