package com.voiceassistant.pro

import android.content.Context
import android.os.Process
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess

class CrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, throwable: Throwable) {

        try {
            // 📅 date du crash
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(Date())

            // 📄 contenu du crash
            val crashReport = buildString {
                append("===== CRASH REPORT =====\n")
                append("Date: $date\n")
                append("Thread: ${thread.name}\n\n")
                append("Error:\n")
                append(throwable.stackTraceToString())
                append("\n========================\n")
            }

            // 💾 fichier crash
            val file = File(context.filesDir, "crash_log.txt")
            file.appendText(crashReport)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        // ⚠️ laisser Android terminer proprement
        defaultHandler?.uncaughtException(thread, throwable)

        // 🔴 optionnel : tuer le process proprement
        Process.killProcess(Process.myPid())
        exitProcess(10)
    }
}
