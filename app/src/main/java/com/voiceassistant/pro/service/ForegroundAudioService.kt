package com.voiceassistant.pro.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.voiceassistant.pro.core.Constants
import com.voiceassistant.pro.data.preferences.PreferencesManager
import com.voiceassistant.pro.domain.audio.AudioPlayerManager
import com.voiceassistant.pro.receiver.NotificationActionReceiver
import com.voiceassistant.pro.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ForegroundAudioService : Service() {

    @Inject
    lateinit var audioPlayerManager: AudioPlayerManager

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private var vibrator: Vibrator? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("🎧 ForegroundAudioService started")

        val sender = intent?.getStringExtra("sender") ?: "Unknown"
        val isGroup = intent?.getBooleanExtra("isGroup", false) ?: false

        vibrator = getSystemService(VIBRATOR_SERVICE) as? Vibrator

        val notification = buildNotification(sender, isGroup)
        startForeground(Constants.NOTIFICATION_ID_PLAYBACK, notification)

        serviceScope.launch {
            delay(5 * 60 * 1000L)
            stopSelf()
        }

        return START_STICKY
    }

    private fun buildNotification(sender: String, isGroup: Boolean): Notification {
        val title = if (isGroup) "🎤 Voice in $sender" else "🎤 Voice from $sender"

        val openIntent = Intent(this, MainActivity::class.java)
        val openPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playPendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            Intent(this, NotificationActionReceiver::class.java).apply {
                action = "ACTION_PLAY"
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val pausePendingIntent = PendingIntent.getBroadcast(
            this,
            2,
            Intent(this, NotificationActionReceiver::class.java).apply {
                action = "ACTION_PAUSE"
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val speedPendingIntent = PendingIntent.getBroadcast(
            this,
            3,
            Intent(this, NotificationActionReceiver::class.java).apply {
                action = "ACTION_SPEED"
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissPendingIntent = PendingIntent.getBroadcast(
            this,
            4,
            Intent(this, NotificationActionReceiver::class.java).apply {
                action = "ACTION_DISMISS"
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, Constants.CHANNEL_ID_PLAYBACK)
            .setContentTitle(title)
            .setContentText("Tap to control playback")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(openPendingIntent)
            .addAction(android.R.drawable.ic_media_play, "Play", playPendingIntent)
            .addAction(android.R.drawable.ic_media_pause, "Pause", pausePendingIntent)
            .addAction(android.R.drawable.ic_media_next, "Speed 2x", speedPendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Dismiss", dismissPendingIntent)
            .setStyle(MediaStyle())
            .setAutoCancel(false)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        audioPlayerManager.release()
        stopForeground(STOP_FOREGROUND_REMOVE)
        Timber.d("🔌 ForegroundAudioService destroyed")
    }
}
