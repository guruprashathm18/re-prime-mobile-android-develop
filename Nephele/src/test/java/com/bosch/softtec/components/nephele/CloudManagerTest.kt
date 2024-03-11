package com.bosch.softtec.components.nephele

import android.app.Activity
import android.content.Intent
import com.bosch.softtec.components.core.models.NetworkTimeoutConfiguration
import com.bosch.softtec.components.nephele.cloud.CloudApi
import com.bosch.softtec.micro.tenzing.client.model.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

/**
 * @author Andre Weber
 * @since 2019-08-27
 */
class CloudManagerTest : BaseTest<CloudManager>() {
    private val mockCloudConfiguration = mock<CloudConfiguration>()

    @Mock
    private lateinit var mockCloudApi: CloudApi

    @Mock
    private lateinit var mockAuthConfig: AuthorizationConfiguration

    private val mockActivity = mock<Activity>()

    override fun initClassUnderTest(): CloudManager {
        val mockIntent = mock<Intent>()
        `when`(mockActivity.applicationContext).thenReturn(applicationContext)
        `when`(mockActivity.intent).thenReturn(mockIntent)

        return CloudManager.createInstance(mockCloudConfiguration)
    }

    @Test
    fun uploadTrip() {
        val mockTripBody = mock<TripBodyWithImageUrl>()
        `when`(mockTripBody.visibility).thenReturn(VisibilityType.PUBLIC)

        val mockTripResponse = "someTripId"
        `when`(mockCloudApi.addTrip(any(), any())).thenReturn(mockTripResponse)

        classUnderTest.uploadTrip(mockTripBody)

        verify(mockCloudApi).addTrip(eq(mockAuthConfig), any())
    }

    @Test
    fun uploadTrip_withoutResponse() {
        val mockTripBody = mock<TripBodyWithImageUrl>()
        `when`(mockTripBody.visibility).thenReturn(VisibilityType.PUBLIC)

        classUnderTest.uploadTrip(mockTripBody)

        verify(mockCloudApi).addTrip(eq(mockAuthConfig), any())
    }

    @Test
    fun findTripById() {
        val tripId = "someTripId"
        classUnderTest.fetchTripById(tripId)

        verify(mockCloudApi).fetchTripById(mockAuthConfig, tripId)
    }

    @Test
    fun deleteTripById() {
        val tripId = "someTripId"
        classUnderTest.deleteTripById(tripId)

        verify(mockCloudApi).deleteTrip(mockAuthConfig, tripId)
    }

    @Test
    fun fetchAllTrips() {
        val limit = 5
        val next = "someString"
        val visibilityTypes = listOf(VisibilityType.SHARED)

        classUnderTest.fetchAllTrips(limit, next, visibilityTypes)

        verify(mockCloudApi).fetchAllTrips(mockAuthConfig, limit, next, visibilityTypes)
    }

    @Test
    fun searchTrips() {
        val searchContext = mock<SearchContext>()
        val limit = 5
        val next = "someString"
        val visibilityTypes = listOf(VisibilityType.SHARED)

        classUnderTest.searchTrips(searchContext, limit, next, visibilityTypes)

        verify(mockCloudApi).searchTrips(mockAuthConfig, searchContext, limit, next, visibilityTypes)
    }

    @Test
    fun updateTrip() {
        val tripId = "someTripId"
        val jsonPatches = mock<List<JsonPatch>>()

        classUnderTest.updateTrip(tripId, jsonPatches)

        verify(mockCloudApi).updateTrip(mockAuthConfig, tripId, jsonPatches)
    }

    @Test
    fun shareTrip() {
        val mockShareTicketBody = mock<ShareTicketBody>()
        classUnderTest.shareTrip(mockShareTicketBody)

        verify(mockCloudApi).shareTrip(mockAuthConfig, mockShareTicketBody)
    }

    @Test
    fun redeemSharedTrip() {
        val mockShareTicketCode = mock<ShareTicketCode>()
        classUnderTest.redeemSharedTrip(mockShareTicketCode)

        verify(mockCloudApi).redeemSharedTrip(mockAuthConfig, mockShareTicketCode)
    }

    @Test
    fun getNetworkTimeoutConfiguration() {
        classUnderTest.networkTimeoutConfiguration

        verify(mockCloudApi).networkTimeoutConfiguration
    }

    @Test
    fun setNetworkTimeoutConfiguration() {
        val mockNetworkTimeoutConfiguration = mock<NetworkTimeoutConfiguration>()
        classUnderTest.networkTimeoutConfiguration = mockNetworkTimeoutConfiguration

        verify(mockCloudApi).networkTimeoutConfiguration = mockNetworkTimeoutConfiguration
    }
}
