package com.royalenfield.appIndexing

import com.google.gson.JsonObject

interface IAppIndex {
    fun showRides(payloadJson: JsonObject?)
    fun showOurWorld(payloadJson: JsonObject?)
    fun showMotorCycle(payloadJson: JsonObject?)
    fun showMakeItYours(payloadJson: JsonObject?)
    fun showSpecificMotorCycle(payloadJson: JsonObject?)
    fun showService(payloadJson: JsonObject?)
    fun showNavigation(payloadJson: JsonObject?)
    fun showDRSA(payloadJson: JsonObject?)
    fun showOwnerManual(payloadJson: JsonObject?)
    fun openWebView(payloadJson: JsonObject?)
}