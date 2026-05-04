package com.voiceassistant.pro.core

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.voiceassistant.pro.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Setup Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        createNotificationChannels()
        Timber.d("${Constants.APP_NAME} v${Constants.APP_VERSION} initialized")
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)

            val mainChannel = NotificationChannel(
                Constants.CHANNEL_ID,
                "Voice Assistant",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Voice message notifications"
            }

            val playbackChannel = NotificationChannel(
                Constants.CHANNEL_ID_PLAYBACK,
                "Playback Controls",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Audio playback controls"
            }

            manager?.apply {
                createNotificationChannel(mainChannel)
                createNotificationChannel(playbackChannel)
            }

            Timber.d("✅ Notification channels created")
        }
    }
}
