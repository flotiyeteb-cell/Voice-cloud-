package com.voiceassistant.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import com.voiceassistant.pro.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onEnableNotificationAccess: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val unplayedCount by viewModel.unplayedCount.collectAsState(initial = 0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Voice Assistant PRO",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, "Settings")
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF25D366))
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "🎤",
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Auto Voice Playback",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Unplayed: $unplayedCount messages",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }

            Button(
                onClick = onEnableNotificationAccess,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF25D366)
                )
            ) {
                Text("Enable Notification Access", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            FeatureCard(
                icon = "⚡",
                title = "Auto-Play",
                description = "Automatically plays voice messages"
            )

            FeatureCard(
                icon = "🎚",
                title = "Speed Control",
                description = "Adjust playback speed (0.75x - 2x)"
            )

            FeatureCard(
                icon = "🎤",
                title = "Transcription",
                description = "Convert voice to text (Coming soon)"
            )

            FeatureCard(
                icon = "🔇",
                title = "Skip Silence",
                description = "Faster playback without silence"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FeatureCard(icon: String, title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(description, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
