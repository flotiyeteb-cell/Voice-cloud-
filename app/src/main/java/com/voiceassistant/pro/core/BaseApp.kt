package com.voiceassistant.pro.core

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

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
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
