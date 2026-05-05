package com.voiceassistant.pro.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.voiceassistant.pro.ui.screens.HomeScreen
import com.voiceassistant.pro.ui.screens.SettingsScreen
import com.voiceassistant.pro.ui.theme.VoiceAssistantTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var hasNotificationPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("🚀 MainActivity created")

        // SEUL demander POST_NOTIFICATIONS pour Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                hasNotificationPermission = true
                Timber.d("✅ POST_NOTIFICATIONS permission already granted")
            } else {
                // Demander seulement cette permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
                Timber.d("📱 Requesting POST_NOTIFICATIONS permission")
            }
        } else {
            hasNotificationPermission = true
            Timber.d("✅ Android < 13, no permission needed")
        }

        setContent {
            VoiceAssistantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp(
                        onEnableNotificationAccess = { enableNotificationAccess() }
                    )
                }
            }
        }
    }

    private fun enableNotificationAccess() {
        Timber.d("🔔 Opening notification settings")
        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasNotificationPermission = true
                Timber.d("✅ POST_NOTIFICATIONS permission granted")
            } else {
                Timber.w("⚠️ POST_NOTIFICATIONS permission denied - app may not show notifications")
                hasNotificationPermission = false
            }
        }
    }

    companion object {
        const val NOTIFICATION_PERMISSION_CODE = 1001
    }
}

@Composable
fun MainApp(onEnableNotificationAccess: () -> Unit) {
    var currentScreen by remember { mutableStateOf("home") }

    when (currentScreen) {
        "home" -> HomeScreen(
            onNavigateToSettings = { currentScreen = "settings" },
            onEnableNotificationAccess = onEnableNotificationAccess
        )
        "settings" -> SettingsScreen(
            onNavigateBack = { currentScreen = "home" }
        )
    }
}
