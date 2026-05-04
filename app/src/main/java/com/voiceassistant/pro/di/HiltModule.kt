package com.voiceassistant.pro.di

import android.content.Context
import com.voiceassistant.pro.data.db.VoiceMessageDatabase
import com.voiceassistant.pro.data.db.VoiceMessageDao
import com.voiceassistant.pro.data.preferences.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Singleton
    @Provides
    fun provideVoiceMessageDatabase(
        @ApplicationContext context: Context
    ): VoiceMessageDatabase {
        return VoiceMessageDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideVoiceMessageDao(
        database: VoiceMessageDatabase
    ): VoiceMessageDao {
        return database.voiceMessageDao()
    }

    @Singleton
    @Provides
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager {
        return PreferencesManager(context)
    }
}
