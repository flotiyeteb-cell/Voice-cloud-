package com.voiceassistant.pro.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.voiceassistant.pro.core.Constants
import com.voiceassistant.pro.data.model.VoiceMessageEntity

@Database(
    entities = [VoiceMessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VoiceMessageDatabase : RoomDatabase() {

    abstract fun voiceMessageDao(): VoiceMessageDao

    companion object {
        @Volatile
        private var INSTANCE: VoiceMessageDatabase? = null

        fun getInstance(context: Context): VoiceMessageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VoiceMessageDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
