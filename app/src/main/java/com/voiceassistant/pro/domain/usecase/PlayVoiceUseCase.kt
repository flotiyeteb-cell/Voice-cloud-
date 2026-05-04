package com.voiceassistant.pro.domain.usecase

import android.app.Notification
import android.content.Intent
import javax.inject.Inject

class PlayVoiceUseCase @Inject constructor() {

    fun execute(action: Notification.Action?) {
        try {
            action?.actionIntent?.send()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun executeWithIntent(action: Notification.Action?, intent: Intent? = null) {
        try {
            action?.actionIntent?.send(null, 0, intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
