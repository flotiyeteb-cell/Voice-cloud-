package com.voiceassistant.pro.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.voiceassistant.pro.core.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "voice_assistant_settings")

@Singleton
class PreferencesManager @Inject constructor(private val context: Context) {

    private val dataStore = context.dataStore

    val autoPlayEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[booleanPreferencesKey(Constants.PREF_AUTO_PLAY_ENABLED)] ?: true
    }

    suspend fun setAutoPlayEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(Constants.PREF_AUTO_PLAY_ENABLED)] = enabled
        }
    }

    val transcriptionEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[booleanPreferencesKey(Constants.PREF_TRANSCRIPTION_ENABLED)] ?: false
    }

    suspend fun setTranscriptionEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(Constants.PREF_TRANSCRIPTION_ENABLED)] = enabled
        }
    }

    val playbackSpeed: Flow<Float> = dataStore.data.map { prefs ->
        prefs[floatPreferencesKey(Constants.PREF_PLAYBACK_SPEED)] ?: 1.0f
    }

    suspend fun setPlaybackSpeed(speed: Float) {
        dataStore.edit { prefs ->
            prefs[floatPreferencesKey(Constants.PREF_PLAYBACK_SPEED)] = speed
        }
    }

    val skipSilenceEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[booleanPreferencesKey(Constants.PREF_SKIP_SILENCE)] ?: false
    }

    suspend fun setSkipSilenceEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(Constants.PREF_SKIP_SILENCE)] = enabled
        }
    }

    val vibrationEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[booleanPreferencesKey(Constants.PREF_VIBRATION)] ?: true
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(Constants.PREF_VIBRATION)] = enabled
        }
    }

    val darkModeEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[booleanPreferencesKey(Constants.PREF_DARK_MODE)] ?: false
    }

    suspend fun setDarkModeEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(Constants.PREF_DARK_MODE)] = enabled
        }
    }
}
