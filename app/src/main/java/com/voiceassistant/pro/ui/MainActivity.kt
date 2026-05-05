package com.voiceassistant.pro.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.provider.Settings.Secure
import com.voiceassistant.pro.ui.screens.HomeScreen
import com.voiceassistant.pro.ui.screens.PermissionScreen
import com.voiceassistant.pro.ui.screens.SettingsScreen
import com.voiceassistant.pro.ui.theme.VoiceAssistantTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("MainActivity created")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        setContent {
            VoiceAssistantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp(
                        context = this@MainActivity,
                        onEnableNotificationAccess = {
                            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                        }
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d("Permission result: $requestCode")
    }
}

@Composable
fun MainApp(context: android.content.Context, onEnableNotificationAccess: () -> Unit) {
    val currentScreen = remember { mutableStateOf("check_permissions") }
    val isNotificationListenerEnabled = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isNotificationListenerEnabled.value = isNotificationListenerServiceEnabled(context)
    }

    when {
        !isNotificationListenerEnabled.value -> {
            PermissionScreen()
        }
        currentScreen.value == "home" -> HomeScreen(
            onNavigateToSettings = { currentScreen.value = "settings" },
            onEnableNotificationAccess = onEnableNotificationAccess
        )
        currentScreen.value == "settings" -> SettingsScreen(
            onNavigateBack = { currentScreen.value = "home" }
        )
    }
}

fun isNotificationListenerServiceEnabled(context: android.content.Context): Boolean {
    val enabledListeners = android.provider.Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    ) ?: return false
    
    return enabledListeners.contains("com.voiceassistant.pro/.service.NotificationListenerService")
}
