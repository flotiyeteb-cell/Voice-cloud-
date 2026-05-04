package com.voiceassistant.pro.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.voiceassistant.pro.data.db.VoiceMessageDao
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val voiceMessageDao: VoiceMessageDao
) : ViewModel() {

    val unplayedCount = voiceMessageDao.getUnplayedCount()

    init {
        Timber.d("🏠 HomeViewModel initialized")
    }
}
