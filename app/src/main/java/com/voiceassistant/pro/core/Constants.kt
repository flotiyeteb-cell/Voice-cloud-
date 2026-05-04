package com.voiceassistant.pro.core

object Constants {
    const val APP_VERSION = "2.0.0-PRO"
    const val APP_NAME = "Voice Assistant PRO"

    const val WHATSAPP_PACKAGE = "com.whatsapp"
    const val WHATSAPP_BUSINESS_PACKAGE = "com.whatsapp.w4b"

    const val CHANNEL_ID = "voice_assistant_pro"
    const val CHANNEL_ID_PLAYBACK = "voice_playback"
    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_ID_PLAYBACK = 2

    val VOICE_KEYWORDS = arrayOf(
        "audio", "vocal", "🎤", "🎧", "voice", "message vocal",
        "voice message", "audio message", "recording", "enregistrement"
    )

    const val PREF_AUTO_PLAY_ENABLED = "auto_play_enabled"
    const val PREF_TRANSCRIPTION_ENABLED = "transcription_enabled"
    const val PREF_PLAYBACK_SPEED = "playback_speed"
    const val PREF_SKIP_SILENCE = "skip_silence"
    const val PREF_VIBRATION = "vibration_enabled"
    const val PREF_DARK_MODE = "dark_mode"

    val PLAYBACK_SPEEDS = listOf(0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f)

    const val DATABASE_NAME = "voice_assistant.db"
}
