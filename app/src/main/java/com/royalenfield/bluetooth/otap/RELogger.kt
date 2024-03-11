package com.royalenfield.bluetooth.otap

import android.content.Context
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


class RELogger(private val context: Context) {

    private val enableLogger:Boolean=false

    fun appendLog(message: String) {
       /* if (enableLogger) {
            val fileName = "OTAP"
            val file = File(context.getExternalFilesDir(null), "$fileName.txt")
            try {
                BufferedWriter(FileWriter(file, true)).use { bufferedWriter ->
                    bufferedWriter.append(message)
                    bufferedWriter.newLine()
                    bufferedWriter.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
    }
}