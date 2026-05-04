package com.voiceassistant.pro.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationData(
    val title: String? = null,
    val text: String? = null,
    val subText: String? = null,
    val senderName: String? = null,
    val isGroup: Boolean = false,
    val packageName: String? = null,
    val key: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val messageId: String? = null,
    val isVoiceMessage: Boolean = false,
    val priority: Int = 3,
    val conversationId: String? = null
) : Parcelable

@Entity(tableName = "voice_messages")
data class VoiceMessageEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val sender: String,
    val groupName: String? = null,
    val timestamp: Long,
    val isPlayed: Boolean = false,
    val duration: Long? = null,
    val transcription: String? = null,
    val audioPath: String? = null
)
