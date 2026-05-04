package com.voiceassistant.pro.data.db

import androidx.room.*
import com.voiceassistant.pro.data.model.VoiceMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VoiceMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: VoiceMessageEntity)

    @Query("SELECT * FROM voice_messages ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentMessages(limit: Int = 50): Flow<List<VoiceMessageEntity>>

    @Query("SELECT * FROM voice_messages WHERE sender = :sender ORDER BY timestamp DESC")
    fun getMessagesBySender(sender: String): Flow<List<VoiceMessageEntity>>

    @Query("UPDATE voice_messages SET isPlayed = 1 WHERE id = :id")
    suspend fun markAsPlayed(id: String)

    @Query("UPDATE voice_messages SET transcription = :text WHERE id = :id")
    suspend fun updateTranscription(id: String, text: String)

    @Query("DELETE FROM voice_messages WHERE timestamp < :beforeTime")
    suspend fun deleteOlderThan(beforeTime: Long)

    @Query("SELECT COUNT(*) FROM voice_messages WHERE isPlayed = 0")
    fun getUnplayedCount(): Flow<Int>
}
