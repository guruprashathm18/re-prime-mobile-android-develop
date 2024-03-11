package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.socket

import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.ConnectedResponse
import com.royalenfield.reprime.ui.triplisting.response.TripListingResponseModel

interface ConnectedServiceInterface {

    fun serviceSuccess(payload: ConnectedResponse)

    fun serviceFails(errorMsg: String)
}