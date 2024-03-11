package com.royalenfield.reprime.ui.home.navigation.activity

import android.util.Log
import java.io.IOException
import kotlin.system.exitProcess

class CustomUncaughtExceptionHandler(
    private val crashHandler: CrashHandler,
    private val defaultExceptionHandler: Thread.UncaughtExceptionHandler?
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        try {
            Log.d(TAG, "Exception was caught")
            crashHandler.handleUncaughtException()
            defaultExceptionHandler?.uncaughtException(thread, ex)
            exitProcess(0)
        } catch (e: IOException) {
            // just catch
        }
    }

    private companion object {
        private val TAG = CustomUncaughtExceptionHandler::class.java.simpleName.take(23)
    }
}