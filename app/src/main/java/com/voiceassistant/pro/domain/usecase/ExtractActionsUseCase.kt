package com.voiceassistant.pro.domain.usecase

import android.app.Notification
import javax.inject.Inject

class ExtractActionsUseCase @Inject constructor() {

    fun execute(notification: Notification): List<Notification.Action> {
        return notification.actions?.toList() ?: emptyList()
    }

    fun findPlayAction(actions: List<Notification.Action>): Notification.Action? {
        return actions.find { action ->
            action.title?.toString()?.contains("play", ignoreCase = true) == true ||
            action.title?.toString()?.contains("listen", ignoreCase = true) == true
        }
    }

    fun findReplyAction(actions: List<Notification.Action>): Notification.Action? {
        return actions.find { action ->
            action.title?.toString()?.contains("reply", ignoreCase = true) == true ||
            action.title?.toString()?.contains("respond", ignoreCase = true) == true
        }
    }
}
