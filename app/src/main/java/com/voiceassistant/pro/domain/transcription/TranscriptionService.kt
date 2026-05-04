package com.voiceassistant.pro.domain.transcription

import android.content.Context
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.voiceassistant.pro.data.db.VoiceMessageDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranscriptionService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val voiceMessageDao: VoiceMessageDao
) {

    private val _transcriptionText = MutableStateFlow("")
    val transcriptionText: StateFlow<String> = _transcriptionText

    private val _isTranscribing = MutableStateFlow(false)
    val isTranscribing: StateFlow<Boolean> = _isTranscribing

    private var speechRecognizer: SpeechRecognizer? = null

    fun startTranscription() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: android.os.Bundle?) {
                    _isTranscribing.value = true
                    Timber.d("🎤 Ready for speech")
                }

                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {
                    Timber.d("🔇 Speech ended")
                }

                override fun onError(error: Int) {
                    _isTranscribing.value = false
                    Timber.e("❌ Speech error: $error")
                }

                override fun onResults(results: android.os.Bundle?) {
                    _isTranscribing.value = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        _transcriptionText.value = matches[0]
                        Timber.d("✅ Transcribed: ${matches[0]}")
                    }
                }

                override fun onPartialResults(partialResults: android.os.Bundle?) {}
                override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
            })

            Timber.d("🎤 Transcription started")
        } else {
            Timber.w("⚠️ Speech recognition not available")
        }
    }

    fun stopTranscription() {
        speechRecognizer?.stopListening()
        Timber.d("⏹ Transcription stopped")
    }

    fun release() {
        speechRecognizer?.destroy()
        Timber.d("🔌 TranscriptionService released")
    }
}
