@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.voiceassistant.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voiceassistant.pro.core.Constants
import com.voiceassistant.pro.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val autoPlayEnabled by viewModel.autoPlayEnabled.collectAsState(initial = true)
    val transcriptionEnabled by viewModel.transcriptionEnabled.collectAsState(initial = false)
    val playbackSpeed by viewModel.playbackSpeed.collectAsState(initial = 1.0f)
    val skipSilenceEnabled by viewModel.skipSilenceEnabled.collectAsState(initial = false)
    val vibrationEnabled by viewModel.vibrationEnabled.collectAsState(initial = true)
    val darkModeEnabled by viewModel.darkModeEnabled.collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFECE5DD))
                .verticalScroll(rememberScrollState())
        ) {
            SettingItem(
                title = "Auto-Play Voice Messages",
                description = "Automatically play incoming voice messages",
                icon = "▶️",
                isEnabled = autoPlayEnabled,
                onToggle = { viewModel.setAutoPlayEnabled(it) }
            )

            Divider()

            SettingItem(
                title = "Transcription",
                description = "Convert voice messages to text",
                icon = "📝",
                isEnabled = transcriptionEnabled,
                onToggle = { viewModel.setTranscriptionEnabled(it) }
            )

            Divider()

            SettingItem(
                title = "Skip Silence",
                description = "Automatically skip silent parts",
                icon = "🔇",
                isEnabled = skipSilenceEnabled,
                onToggle = { viewModel.setSkipSilenceEnabled(it) }
            )

            Divider()

            SettingItem(
                title = "Vibration Feedback",
                description = "Haptic feedback on controls",
                icon = "📳",
                isEnabled = vibrationEnabled,
                onToggle = { viewModel.setVibrationEnabled(it) }
            )

            Divider()

            SettingItem(
                title = "Dark Mode",
                description = "Enable dark theme",
                icon = "🌙",
                isEnabled = darkModeEnabled,
                onToggle = { viewModel.setDarkModeEnabled(it) }
            )

            Divider()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🎚 Playback Speed", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Constants.PLAYBACK_SPEEDS.forEach { speed ->
                            Button(
                                onClick = { viewModel.setPlaybackSpeed(speed) },
                                modifier = Modifier.width(60.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (playbackSpeed == speed) Color(0xFF25D366) else Color.LightGray
                                )
                            ) {
                                Text("${speed}x", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    icon: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(description, fontSize = 12.sp, color = Color.Gray)
            }
        }

        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
