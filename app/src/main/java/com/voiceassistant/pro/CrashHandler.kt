package com.voiceassistant.pro

import android.content.Context
import android.os.Looper
import android.widget.Toast
import java.io.File

class CrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, throwable: Throwable) {

        try {
            val crashText = throwable.stackTraceToString()

            // 💾 fichier interne
            val file = File(context.filesDir, "crash_log.txt")
            file.appendText(crashText + "\n\n")

            // 📱 afficher à l’écran (important)
            Looper.prepare()
            Toast.makeText(context, "Crash capturé ✔", Toast.LENGTH_LONG).show()
            Looper.loop()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        defaultHandler?.uncaughtException(thread, throwable)
    }
}
