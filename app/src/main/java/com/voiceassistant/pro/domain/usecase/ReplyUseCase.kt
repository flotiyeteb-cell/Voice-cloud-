package com.voiceassistant.pro.domain.usecase

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import androidx.core.app.RemoteInput
import javax.inject.Inject

class ReplyUseCase @Inject constructor() {

    fun execute(action: Notification.Action?, message: String): Boolean {
        return try {
            val remoteInputs = action?.remoteInputs ?: return false

            if (remoteInputs.isEmpty()) return false

            val intent = Intent()
            val bundle = Bundle()

            remoteInputs.forEach { remoteInput ->
                bundle.putCharSequence(remoteInput.resultKey, message)
            }

            RemoteInput.addResultsToIntent(remoteInputs, intent, bundle)
            action.actionIntent.send(null, 0, intent)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
