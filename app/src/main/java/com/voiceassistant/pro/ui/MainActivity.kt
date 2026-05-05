package com.voiceassistant.pro.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("🚀 MainActivity created")

        // Request POST_NOTIFICATIONS for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
                Timber.d("📱 Requesting POST_NOTIFICATIONS permission")
            }
        }

        setContent {
            VoiceAssistantTheme {
                Surface(
                    modifier = androidx.compose.foundation.layout.fillMaxSize(),
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
                Timber.d("✅ POST_NOTIFICATIONS permission granted")
            } else {
                Timber.w("⚠️ POST_NOTIFICATIONS permission denied")
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
