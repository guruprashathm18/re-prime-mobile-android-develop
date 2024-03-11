package com.bosch.softtec.components.nephele.cloud

import android.graphics.Bitmap
import com.bosch.softtec.components.core.models.NetworkTimeoutConfiguration
import com.bosch.softtec.components.nephele.*
import com.bosch.softtec.micro.tenzing.client.api.ShareTripApi
import com.bosch.softtec.micro.tenzing.client.api.TripApi
import com.bosch.softtec.micro.tenzing.client.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*


/**
 * @author Andre Weber
 * @since 2019-08-27
 */
internal class CloudApiTest : BaseTest<CloudApi>() {
    private val backendConfiguration = BackendConfiguration("https://someBackendUrl.com")

    private val cloudConfiguration = CloudConfiguration(backendConfiguration)

    @Mock
    private lateinit var mockApiProvider: CloudApi.ApiProvider

    @Mock
    private lateinit var mockTripApi: TripApi

    @Mock
    private lateinit var mockShareTripApi: ShareTripApi

    @Mock
    private lateinit var mockAuthConfig: AuthorizationConfiguration

    override fun initClassUnderTest(): CloudApi {
        return CloudApi(cloudConfiguration)
    }

    override fun setUp() {
        super.setUp()

        `when`(mockApiProvider.provideTripApi(mockAuthConfig)).thenReturn(mockTripApi)
        `when`(mockApiProvider.provideShareTripApi(mockAuthConfig)).thenReturn(mockShareTripApi)
    }

    @Test
    fun addTrip() {
        val mockRawResponse = mock<okhttp3.Response>()
        val mockResponse = mock<Response<Void>>()
        val mockCall = mock<Call<Void>>()
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.raw()).thenReturn(mockRawResponse)

        val tripId = "someTripId"
        val location = "/api/v1/trips/$tripId"
        `when`(mockRawResponse.header("location")).thenReturn(location)

        val tracks = listOf<GpsPoint>(mock(), mock(), mock(), mock(), mock())
        val directions = listOf<GpsPoint>(mock(), mock())

        val tripBody = TripBodyWithImageUrl()
            .name("someName")
            .description("someDescription")
            .track(tracks)
            .directions(directions)
            .addImageUrlItem("http://some.example.url")

        `when`(mockTripApi.tripPost(tripBody)).thenReturn(mockCall)

        val response = classUnderTest.addTrip(mockAuthConfig, tripBody)

        assertEquals(tripId, response)
    }

    @Test(expected = CloudException::class)
    fun addTrip_withoutSuccess() {
        val mockResponse = mock<Response<Void>>()
        val mockCall = mock<Call<Void>>()
        `when`(mockTripApi.tripPost(any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(false)

        val tracks = listOf<GpsPoint>(mock(), mock(), mock(), mock(), mock())
        val directions = listOf<GpsPoint>(mock(), mock())

        val tripBody = TripBodyWithImageUrl()
            .name("someName")
            .description("someDescription")
            .track(tracks)
            .directions(directions)
            .addImageUrlItem("http://some.example.url")

        classUnderTest.addTrip(mockAuthConfig, tripBody)
    }

    @Test(expected = CloudException::class)
    fun addTrip_throwsException() {
        val mockCall = mock<Call<Void>>()
        `when`(mockTripApi.tripPost(any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenThrow(IOException::class.java)

        val tracks = listOf<GpsPoint>(mock(), mock(), mock(), mock(), mock())
        val directions = listOf<GpsPoint>(mock(), mock())

        val tripBody = TripBodyWithImageUrl()
            .name("someName")
            .description("someDescription")
            .track(tracks)
            .directions(directions)
            .addImageUrlItem("http://some.example.url")

        classUnderTest.addTrip(mockAuthConfig, tripBody)
    }

    @Test
    fun findTripById() {
        val mockCall = mock<Call<Trip>>()
        val mockResponse = mock<Response<Trip>>()
        val mockTripDto = mock<Trip>()
        `when`(mockTripApi.tripGet(any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.body()).thenReturn(mockTripDto)

        val tripById = classUnderTest.fetchTripById(mockAuthConfig, "someTripId")

        assertEquals(mockTripDto, tripById)
    }

    @Test(expected = CloudException::class)
    fun findTripById_withoutSuccess() {
        val mockCall = mock<Call<Trip>>()
        val mockResponse = mock<Response<Trip>>()
        `when`(mockTripApi.tripGet(any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(false)

        classUnderTest.fetchTripById(mockAuthConfig, "someTripId")
    }

    @Test(expected = CloudException::class)
    fun findTripById_throwsException() {
        val mockCall = mock<Call<Trip>>()
        `when`(mockTripApi.tripGet(any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenThrow(IOException::class.java)

        classUnderTest.fetchTripById(mockAuthConfig, "someTripId")
    }

    @Test
    fun updateTrip() {
        val mockCall = mock<Call<Void>>()
        val mockResponse = mock<Response<Void>>()
        `when`(mockTripApi.tripPatch(any(), any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)

        val isUpdateSuccessful = classUnderTest.updateTrip(mockAuthConfig, "someTripId", listOf(JsonPatch()))

        assertTrue(isUpdateSuccessful)
    }

    @Test(expected = CloudException::class)
    fun updateTrip_withoutSuccess() {
        val mockCall = mock<Call<Void>>()
        val mockResponse = mock<Response<Void>>()
        `when`(mockTripApi.tripPatch(any(), any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(false)

        classUnderTest.updateTrip(mockAuthConfig, "someTripId", listOf(JsonPatch()))
    }

    @Test(expected = CloudException::class)
    fun updateTrip_throwsException() {
        val mockCall = mock<Call<Void>>()
        `when`(mockTripApi.tripPatch(any(), any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenThrow(IOException::class.java)

        classUnderTest.updateTrip(mockAuthConfig, "someTripId", listOf(JsonPatch()))
    }

    @Test
    fun deleteTrip() {
        val mockCall = mock<Call<Void>>()
        val mockResponse = mock<Response<Void>>()
        val mockDeleteResponse = mock<Void>()
        `when`(mockTripApi.tripDelete(any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.body()).thenReturn(mockDeleteResponse)

        classUnderTest.deleteTrip(mockAuthConfig, "someTripId")
    }

    @Test(expected = CloudException::class)
    fun deleteTrip_withoutSuccess() {
        val mockCall = mock<Call<Void>>()
        val mockResponse = mock<Response<Void>>()
        `when`(mockTripApi.tripDelete(any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(false)

        classUnderTest.deleteTrip(mockAuthConfig, "someTripId")
    }

    @Test(expected = CloudException::class)
    fun deleteTrip_throwException() {
        val mockCall = mock<Call<Void>>()
        `when`(mockTripApi.tripDelete(any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenThrow(IOException::class.java)

        classUnderTest.deleteTrip(mockAuthConfig, "someTripId")
    }

    @Test
    fun fetchAllTrips() {
        val mockCall = mock<Call<InlineResponse200>>()
        `when`(mockTripApi.tripsGet(any(), ArgumentMatchers.anyString(), any())).thenReturn(mockCall)

        val mockResponse = mock<Response<InlineResponse200>>()
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)

        val mockInlineResponse = mock<InlineResponse200>()
        `when`(mockResponse.body()).thenReturn(mockInlineResponse)

        val limit = 5
        val next = "someString"
        val visibilityTypes = listOf(VisibilityType.SHARED)

        val response = classUnderTest.fetchAllTrips(mockAuthConfig, limit, next, visibilityTypes)

        assertEquals(mockInlineResponse, response)
    }

    @Test(expected = CloudException::class)
    fun fetchAllTrips_withoutSuccess() {
        val mockCall = mock<Call<InlineResponse200>>()
        `when`(mockTripApi.tripsGet(any(), ArgumentMatchers.anyString(), any())).thenReturn(mockCall)

        val mockResponse = mock<Response<InlineResponse200>>()
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(false)

        val limit = 5
        val next = "someString"
        val visibilityTypes = listOf(VisibilityType.SHARED)

        classUnderTest.fetchAllTrips(mockAuthConfig, limit, next, visibilityTypes)
    }


    @Test(expected = CloudException::class)
    fun fetchAllTrips_throwsException() {
        val mockCall = mock<Call<InlineResponse200>>()
        `when`(mockTripApi.tripsGet(any(), ArgumentMatchers.anyString(), any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenThrow(IOException::class.java)

        val limit = 5
        val next = "someString"
        val visibilityTypes = listOf(VisibilityType.SHARED)

        classUnderTest.fetchAllTrips(mockAuthConfig, limit, next, visibilityTypes)
    }

    @Test
    fun searchTrips() {
        val mockCall = mock<Call<InlineResponse200>>()
        `when`(mockTripApi.tripsSearch(any(), any(), ArgumentMatchers.anyString(), any())).thenReturn(mockCall)

        val mockResponse = mock<Response<InlineResponse200>>()
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)

        val mockInlineResponse = mock<InlineResponse200>()
        `when`(mockResponse.body()).thenReturn(mockInlineResponse)

        val searchContext = mock<SearchContext>()
        val limit = 5
        val next = "someString"
        val visibilityTypes = listOf(VisibilityType.SHARED)

        val response = classUnderTest.searchTrips(mockAuthConfig, searchContext, limit, next, visibilityTypes)

        assertEquals(mockInlineResponse, response)
    }

    @Test(expected = CloudException::class)
    fun searchTrips_withoutSuccess() {
        val mockCall = mock<Call<InlineResponse200>>()
        `when`(mockTripApi.tripsSearch(any(), any(), ArgumentMatchers.anyString(), any())).thenReturn(mockCall)

        val mockResponse = mock<Response<InlineResponse200>>()
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(false)

        val searchContext = mock<SearchContext>()
        val limit = 5
        val next = "someString"
        val visibilityTypes = listOf(VisibilityType.SHARED)

        classUnderTest.searchTrips(mockAuthConfig, searchContext, limit, next, visibilityTypes)
    }

    @Test(expected = CloudException::class)
    fun searchTrips_throwsException() {
        val mockCall = mock<Call<InlineResponse200>>()
        `when`(mockTripApi.tripsSearch(any(), any(), ArgumentMatchers.anyString(), any())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenThrow(IOException::class.java)

        val searchContext = mock<SearchContext>()
        val limit = 5
        val next = "someString"
        val visibilityTypes = listOf(VisibilityType.SHARED)

        classUnderTest.searchTrips(mockAuthConfig, searchContext, limit, next, visibilityTypes)
    }

    @Test
    fun shareTrip() {
        val shareTicketBody = ShareTicketBody()
        val mockRequest = mock<Call<ShareTicket>>()
        val mockResponse = mock<Response<ShareTicket>>()
        val mockShareTicket = mock<ShareTicket>()

        `when`(mockShareTripApi.shareTrip(shareTicketBody)).thenReturn(mockRequest)
        `when`(mockRequest.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.body()).thenReturn(mockShareTicket)

        val shareTicket = classUnderTest.shareTrip(mockAuthConfig, shareTicketBody)
        assertEquals(mockShareTicket, shareTicket)
    }

    @Test(expected = CloudException::class)
    fun shareTrip_withoutBody() {
        val shareTicketBody = ShareTicketBody()
        val mockRequest = mock<Call<ShareTicket>>()
        val mockResponse = mock<Response<ShareTicket>>()

        `when`(mockShareTripApi.shareTrip(shareTicketBody)).thenReturn(mockRequest)
        `when`(mockRequest.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.body()).thenReturn(null)

        classUnderTest.shareTrip(mockAuthConfig, shareTicketBody)
    }

    @Test(expected = CloudException::class)
    fun shareTrip_withoutSuccess() {
        val shareTicketBody = ShareTicketBody()
        val mockRequest = mock<Call<ShareTicket>>()
        val mockResponse = mock<Response<ShareTicket>>()

        `when`(mockShareTripApi.shareTrip(shareTicketBody)).thenReturn(mockRequest)
        `when`(mockRequest.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(false)

        classUnderTest.shareTrip(mockAuthConfig, shareTicketBody)
    }

    @Test
    fun redeemSharedTrip() {
        val shareTicketCode = ShareTicketCode()
        val mockRequest = mock<Call<Void>>()
        val mockResponse = mock<Response<Void>>()
        val mockRawResponse = mock<okhttp3.Response>()

        `when`(mockShareTripApi.redeemSharedTrip(shareTicketCode)).thenReturn(mockRequest)
        `when`(mockRequest.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.raw()).thenReturn(mockRawResponse)

        val tripId = "someTripId"
        val location = "/api/v1/trips/$tripId"
        `when`(mockRawResponse.header("location")).thenReturn(location)

        val sharedTripId = classUnderTest.redeemSharedTrip(mockAuthConfig, shareTicketCode)
        assertEquals(tripId, sharedTripId)
    }

    @Test(expected = CloudException::class)
    fun redeemSharedTrip_withoutSuccess() {
        val shareTicketCode = ShareTicketCode()
        val mockRequest = mock<Call<Void>>()
        val mockResponse = mock<Response<Void>>()

        `when`(mockShareTripApi.redeemSharedTrip(shareTicketCode)).thenReturn(mockRequest)
        `when`(mockRequest.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(false)

        classUnderTest.redeemSharedTrip(mockAuthConfig, shareTicketCode)
    }

    @Test
    fun extractId() {
        val tripId = "5dc40ff2671e0239facb61d9"

        val location = "/api/v1/trips/$tripId"
        val extractedId = classUnderTest.extractId(location)

        assertEquals(tripId, extractedId)
    }

    private fun areBitmapsEqual(bitmap1: Bitmap, bitmap2: Bitmap): Boolean {
        val buffer1 = ByteBuffer.allocate(bitmap1.height * bitmap1.rowBytes)
        bitmap1.copyPixelsToBuffer(buffer1)

        val buffer2 = ByteBuffer.allocate(bitmap2.height * bitmap2.rowBytes)
        bitmap2.copyPixelsToBuffer(buffer2)

        return Arrays.equals(buffer1.array(), buffer2.array())
    }

    @Test
    fun getNetworkTimeoutConfiguration() {
        classUnderTest.networkTimeoutConfiguration

        verify(mockApiProvider).networkTimeoutConfiguration
    }

    @Test
    fun setNetworkTimeoutConfiguration() {
        val mockNetworkTimeoutConfiguration = mock<NetworkTimeoutConfiguration>()
        classUnderTest.networkTimeoutConfiguration = mockNetworkTimeoutConfiguration

        verify(mockApiProvider).networkTimeoutConfiguration = mockNetworkTimeoutConfiguration
    }
}
