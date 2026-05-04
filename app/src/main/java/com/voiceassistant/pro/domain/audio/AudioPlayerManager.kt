package com.voiceassistant.pro.domain.audio

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.voiceassistant.pro.data.preferences.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) : Player.Listener {

    private val _player = ExoPlayer.Builder(context).build()
    val player: ExoPlayer = _player

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed

    init {
        _player.addListener(this)
        Timber.d("🎧 AudioPlayerManager initialized")
    }

    fun playAudio(uri: String) {
        try {
            val mediaItem = MediaItem.fromUri(uri)
            _player.setMediaItem(mediaItem)
            _player.prepare()
            _player.play()
            Timber.d("▶️ Playing: $uri")
        } catch (e: Exception) {
            Timber.e("❌ Playback error: ${e.message}")
        }
    }

    fun pause() {
        _player.pause()
        Timber.d("⏸ Paused")
    }

    fun resume() {
        _player.play()
        Timber.d("▶️ Resumed")
    }

    fun stop() {
        _player.stop()
        _player.clearMediaItems()
        Timber.d("⏹ Stopped")
    }

    fun setPlaybackSpeed(speed: Float) {
        _player.setPlaybackSpeed(speed)
        _playbackSpeed.value = speed
        Timber.d("🎚 Speed: ${speed}x")
    }

    fun seekTo(position: Long) {
        _player.seekTo(position)
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        _isPlaying.value = playbackState == Player.STATE_READY && _player.isPlaying
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        _currentPosition.value = _player.currentPosition
    }

    fun release() {
        _player.release()
        Timber.d("🔌 AudioPlayerManager released")
    }
}
