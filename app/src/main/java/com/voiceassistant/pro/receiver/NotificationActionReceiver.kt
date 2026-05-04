package com.voiceassistant.pro.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import com.voiceassistant.pro.data.preferences.PreferencesManager
import com.voiceassistant.pro.domain.audio.AudioPlayerManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var audioPlayerManager: AudioPlayerManager

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

        when (intent.action) {
            "ACTION_PLAY" -> {
                Timber.d("▶️ Play clicked")
                audioPlayerManager.resume()
                vibrator?.vibrate(50)
            }
            "ACTION_PAUSE" -> {
                Timber.d("⏸ Pause clicked")
                audioPlayerManager.pause()
                vibrator?.vibrate(50)
            }
            "ACTION_SPEED" -> {
                Timber.d("⚡ Speed up clicked")
                audioPlayerManager.setPlaybackSpeed(2.0f)
                vibrator?.vibrate(longArrayOf(0, 50, 100, 50))
            }
            "ACTION_DISMISS" -> {
                Timber.d("❌ Dismiss clicked")
                audioPlayerManager.stop()
                context?.stopService(Intent(context, com.voiceassistant.pro.service.ForegroundAudioService::class.java))
                vibrator?.vibrate(100)
            }
        }
    }
}
