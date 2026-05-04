package com.voiceassistant.pro.domain.usecase

import com.voiceassistant.pro.core.Constants
import com.voiceassistant.pro.data.model.NotificationData
import timber.log.Timber
import javax.inject.Inject

class DetectVoiceUseCase @Inject constructor() {

    fun execute(data: NotificationData): Boolean {
        val text = data.text?.lowercase() ?: return false

        val isVoice = Constants.VOICE_KEYWORDS.any { keyword ->
            text.contains(keyword.lowercase(), ignoreCase = true)
        }

        if (isVoice) {
            Timber.d("✅ Voice detected from ${data.senderName}")
        } else {
            Timber.d("❌ No voice detected")
        }

        return isVoice
    }

    fun detectWithConfidence(text: String): Pair<Boolean, Float> {
        val lowercase = text.lowercase()
        var score = 0f
        var matches = 0

        Constants.VOICE_KEYWORDS.forEach { keyword ->
            if (lowercase.contains(keyword.lowercase())) {
                matches++
                score += when {
                    text.contains("🎤") -> 1.0f
                    text.contains("🎧") -> 1.0f
                    text.contains("audio", ignoreCase = true) -> 0.9f
                    text.contains("vocal", ignoreCase = true) -> 0.9f
                    text.contains("voice message", ignoreCase = true) -> 0.95f
                    else -> 0.7f
                }
            }
        }

        val confidence = if (matches > 0) score / matches else 0f
        return Pair(confidence > 0.5f, confidence)
    }
}
