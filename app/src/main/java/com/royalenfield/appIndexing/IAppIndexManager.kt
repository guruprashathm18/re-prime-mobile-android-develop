package com.royalenfield.appIndexing

import com.google.gson.JsonObject

interface IAppIndexManager {
    fun navigate(payloadJson: JsonObject?)
    fun setAppIndexListener(appIndex: IAppIndex)
}