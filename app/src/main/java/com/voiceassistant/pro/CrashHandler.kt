package com.voiceassistant.pro

import android.content.Context
import java.io.File

class CrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            val crash = throwable.stackTraceToString()

            val file = File(context.filesDir, "crash_log.txt")
            file.writeText(crash)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        defaultHandler?.uncaughtException(thread, throwable)
    }
}
