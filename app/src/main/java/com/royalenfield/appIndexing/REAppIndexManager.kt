package com.royalenfield.appIndexing

import com.google.gson.JsonObject
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.RELog

/**
 * @author Kiran Dani on 10/09/2020.
 * updated on 04/12/2020.
 * <p>
 * {@link REAppIndexManager} class that acts as deep link indexing class for screens used in application.
 */
class REAppIndexManager : IAppIndexManager {
    var TAG: String = REAppIndexManager::class.java.simpleName
    var deepLinkScreen: REAppIndexScreen? = REAppIndexScreen.NONE
    lateinit var appIndex: IAppIndex

    init {
        println(TAG + "App Indexing Invoked.")
    }

    override fun navigate(payloadJson: JsonObject?) {
        try {
            var id = REAppIndexScreen.NONE.value;
            if (null != payloadJson) {
                if (payloadJson.has(REConstants.NOTIF_DATA_ID)) {
                    id = (payloadJson.get(REConstants.NOTIF_DATA_ID).asString)
                } else {
                    return
                }
            } else {
                return
            }
            //RIDES
            if (id.equals( REAppIndexScreen.RIDES.value)||id.equals(REAppIndexScreen.RIDES_LEGACY.value)) {
                deepLinkScreen = REAppIndexScreen.RIDES
                appIndex.showRides(payloadJson)
            }
            //OUR WORLD
            else if (id.equals( REAppIndexScreen.OUR_WORLD.value)||id.equals(REAppIndexScreen.OUR_WORLD_LEGACY.value)) {
                deepLinkScreen = REAppIndexScreen.OUR_WORLD
                appIndex.showOurWorld(payloadJson)
            }
            // MotorCycle Home Screen
            else if (id.equals( REAppIndexScreen.MOTORCYCLE.value)||id.equals(REAppIndexScreen.MOTORCYCLE_LEGACY.value)) {
                deepLinkScreen = REAppIndexScreen.MOTORCYCLE
                appIndex.showMotorCycle(payloadJson)
            }
            // Make it Yours / MIY
            else if (id.equals( REAppIndexScreen.MIY.value)||id.equals(REAppIndexScreen.MIY_LEGACY.value)) {
                deepLinkScreen = REAppIndexScreen.MIY
                appIndex.showMakeItYours(payloadJson)
            }
            // Specific Motorcycle
            else if (id.equals( REAppIndexScreen.SPECIFIC_MOTORCYCLE.value)||id.equals(REAppIndexScreen.SPECIFIC_MOTORCYCLE_LEGACY.value)) {
                deepLinkScreen = REAppIndexScreen.SPECIFIC_MOTORCYCLE
                appIndex.showSpecificMotorCycle(payloadJson)
            }
            // DRSA
            else if (id.equals( REAppIndexScreen.DRSA.value) ){
                deepLinkScreen = REAppIndexScreen.DRSA
                appIndex.showDRSA(payloadJson)
            }
            // OWNERS MANUAL
            else if (id.equals( REAppIndexScreen.OWNERS_MANUAL.value)) {
                deepLinkScreen = REAppIndexScreen.OWNERS_MANUAL
                appIndex.showOwnerManual(payloadJson)
            }

            // Service Screen
            else if (id.equals( REAppIndexScreen.SERVICE.value)||id.equals(REAppIndexScreen.SERVICE_LEGACY.value)) {
                deepLinkScreen = REAppIndexScreen.SERVICE
                appIndex.showService(payloadJson)
            }
            // Navigate
            else if (id.equals( REAppIndexScreen.NAVIGATION.value)||id.equals(REAppIndexScreen.NAVIGATION_LEGACY.value)) {
                deepLinkScreen = REAppIndexScreen.NAVIGATION
                appIndex.showNavigation(payloadJson)
            }
            else if (id.equals( REAppIndexScreen.NAVIGATION_WEB.value)) {
                deepLinkScreen = REAppIndexScreen.NAVIGATION_WEB
                appIndex.openWebView(payloadJson)
            }
        } catch (e: Exception) {
            RELog.e(e)
        }
    }

    override fun setAppIndexListener(appIndex: IAppIndex) {
        this.appIndex = appIndex
    }
}