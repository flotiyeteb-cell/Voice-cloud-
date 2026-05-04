package com.voiceassistant.pro.data.parser

import android.app.Notification
import com.voiceassistant.pro.core.Constants
import com.voiceassistant.pro.data.model.NotificationData
import timber.log.Timber

object WhatsAppParser {

    fun parse(notification: Notification, packageName: String): NotificationData {
        val extras = notification.extras

        val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
        val subText = extras.getString(Notification.EXTRA_SUB_TEXT) ?: ""

        val isGroup = text.contains(Regex("\\[.+?\\]")) || text.contains(":")

        val senderName = extractSenderName(title, text, isGroup)
        val messageId = generateMessageId(senderName, text)

        Timber.d("📱 Parsed: $senderName | Group: $isGroup | Text: $text")

        return NotificationData(
            title = title,
            text = text,
            subText = subText,
            senderName = senderName,
            isGroup = isGroup,
            packageName = packageName,
            timestamp = System.currentTimeMillis(),
            messageId = messageId,
            isVoiceMessage = detectVoiceMessage(text),
            conversationId = generateConversationId(senderName)
        )
    }

    private fun extractSenderName(title: String, text: String, isGroup: Boolean): String {
        return when {
            isGroup && text.contains("[") && text.contains("]") -> {
                val start = text.indexOf("[") + 1
                val end = text.indexOf("]")
                if (start < end) text.substring(start, end) else title
            }
            isGroup && text.contains(":") -> {
                val parts = text.split(":")
                if (parts.isNotEmpty()) parts[0] else title
            }
            else -> title
        }
    }

    private fun generateMessageId(sender: String, text: String): String {
        return "${sender}_${text.hashCode()}_${System.currentTimeMillis()}"
    }

    private fun generateConversationId(sender: String): String {
        return sender.replace(Regex("[^a-zA-Z0-9]"), "_").lowercase()
    }

    private fun detectVoiceMessage(text: String): Boolean {
        return Constants.VOICE_KEYWORDS.any { keyword ->
            text.contains(keyword, ignoreCase = true)
        }
    }
}
