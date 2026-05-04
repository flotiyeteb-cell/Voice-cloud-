package com.voiceassistant.pro.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
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
                vibrateFeedback(vibrator, 50L)
            }
            "ACTION_PAUSE" -> {
                Timber.d("⏸ Pause clicked")
                audioPlayerManager.pause()
                vibrateFeedback(vibrator, 50L)
            }
            "ACTION_SPEED" -> {
                Timber.d("⚡ Speed up clicked")
                audioPlayerManager.setPlaybackSpeed(2.0f)
                vibratePattern(vibrator, longArrayOf(0, 50, 100, 50))
            }
            "ACTION_DISMISS" -> {
                Timber.d("❌ Dismiss clicked")
                audioPlayerManager.stop()
                context?.stopService(Intent(context, com.voiceassistant.pro.service.ForegroundAudioService::class.java))
                vibrateFeedback(vibrator, 100L)
            }
        }
    }

    private fun vibrateFeedback(vibrator: Vibrator?, duration: Long) {
        if (vibrator?.hasVibrator() != true) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }

    private fun vibratePattern(vibrator: Vibrator?, pattern: LongArray) {
        if (vibrator?.hasVibrator() != true) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, -1)
        }
    }
}
