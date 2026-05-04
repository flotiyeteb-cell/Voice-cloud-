package com.voiceassistant.pro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WhatsAppGreen = Color(0xFF25D366)
private val WhatsAppDarkGreen = Color(0xFF075E54)
private val WhatsAppGray = Color(0xFFECE5DD)

private val LightColors = lightColorScheme(
    primary = WhatsAppGreen,
    secondary = WhatsAppDarkGreen,
    tertiary = WhatsAppGray
)

private val DarkColors = darkColorScheme(
    primary = WhatsAppGreen,
    secondary = WhatsAppDarkGreen,
    tertiary = Color(0xFF1F1B20)
)

@Composable
fun VoiceAssistantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
