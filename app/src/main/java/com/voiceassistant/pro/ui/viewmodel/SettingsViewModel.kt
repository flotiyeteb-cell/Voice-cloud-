package com.voiceassistant.pro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voiceassistant.pro.data.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val autoPlayEnabled = preferencesManager.autoPlayEnabled
    val transcriptionEnabled = preferencesManager.transcriptionEnabled
    val playbackSpeed = preferencesManager.playbackSpeed
    val skipSilenceEnabled = preferencesManager.skipSilenceEnabled
    val vibrationEnabled = preferencesManager.vibrationEnabled
    val darkModeEnabled = preferencesManager.darkModeEnabled

    fun setAutoPlayEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setAutoPlayEnabled(enabled)
            Timber.d("✅ Auto-play: $enabled")
        }
    }

    fun setTranscriptionEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setTranscriptionEnabled(enabled)
            Timber.d("✅ Transcription: $enabled")
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        viewModelScope.launch {
            preferencesManager.setPlaybackSpeed(speed)
            Timber.d("✅ Playback speed: ${speed}x")
        }
    }

    fun setSkipSilenceEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setSkipSilenceEnabled(enabled)
            Timber.d("✅ Skip silence: $enabled")
        }
    }

    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setVibrationEnabled(enabled)
            Timber.d("✅ Vibration: $enabled")
        }
    }

    fun setDarkModeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setDarkModeEnabled(enabled)
            Timber.d("✅ Dark mode: $enabled")
        }
    }
}
