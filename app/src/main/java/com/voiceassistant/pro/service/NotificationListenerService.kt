package com.voiceassistant.pro.service

import android.app.Notification
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.voiceassistant.pro.core.Constants
import com.voiceassistant.pro.data.db.VoiceMessageDao
import com.voiceassistant.pro.data.model.VoiceMessageEntity
import com.voiceassistant.pro.data.parser.WhatsAppParser
import com.voiceassistant.pro.data.preferences.PreferencesManager
import com.voiceassistant.pro.domain.usecase.DetectVoiceUseCase
import com.voiceassistant.pro.domain.usecase.ExtractActionsUseCase
import com.voiceassistant.pro.domain.usecase.PlayVoiceUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListenerService : NotificationListenerService() {

    @Inject
    lateinit var detectVoiceUseCase: DetectVoiceUseCase

    @Inject
    lateinit var extractActionsUseCase: ExtractActionsUseCase

    @Inject
    lateinit var playVoiceUseCase: PlayVoiceUseCase

    @Inject
    lateinit var voiceMessageDao: VoiceMessageDao

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn ?: return

        Timber.d("📱 Notification from: ${sbn.packageName}")

        if (sbn.packageName !in listOf(Constants.WHATSAPP_PACKAGE, Constants.WHATSAPP_BUSINESS_PACKAGE)) {
            return
        }

        val notification = sbn.notification ?: return

        val notifData = WhatsAppParser.parse(notification, sbn.packageName)

        val (isVoice, confidence) = detectVoiceUseCase.detectWithConfidence(notifData.text ?: "")
        Timber.d("🎤 Voice confidence: $confidence")

        if (isVoice && confidence > 0.5f) {
            serviceScope.launch {
                val entity = VoiceMessageEntity(
                    id = notifData.messageId ?: "",
                    sender = notifData.senderName ?: "Unknown",
                    groupName = if (notifData.isGroup) notifData.title else null,
                    timestamp = notifData.timestamp
                )
                voiceMessageDao.insert(entity)

                try {
                    val autoPlayEnabled = preferencesManager.autoPlayEnabled.first()
                    if (autoPlayEnabled) {
                        val actions = extractActionsUseCase.execute(notification)
                        val playAction = extractActionsUseCase.findPlayAction(actions)
                        if (playAction != null) {
                            playVoiceUseCase.execute(playAction)
                            Timber.d("▶️ Auto-playing voice")
                        }
                    }
                } catch (e: Exception) {
                    Timber.e("Error checking auto-play: ${e.message}")
                }

                startForegroundService(Intent(this@NotificationListenerService, ForegroundAudioService::class.java).apply {
                    putExtra("sender", notifData.senderName)
                    putExtra("isGroup", notifData.isGroup)
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        Timber.d("🔌 NotificationListenerService destroyed")
    }
}
