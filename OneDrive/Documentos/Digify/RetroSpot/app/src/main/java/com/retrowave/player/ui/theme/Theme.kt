package com.retrowave.player.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val RetroColorScheme = darkColorScheme(
    primary = RetroCyan,
    onPrimary = RetroDeepBlue,
    primaryContainer = RetroNeonBlue,
    onPrimaryContainer = RetroSilver,
    secondary = RetroChrome,
    onSecondary = RetroDeepBlue,
    tertiary = RetroLcdGreen,
    onTertiary = RetroDeepBlue,
    background = RetroDeepBlue,
    onBackground = RetroSilver,
    surface = RetroDarkSurface,
    onSurface = RetroSilver,
    surfaceVariant = RetroDarkCard,
    onSurfaceVariant = RetroDimGray,
    outline = RetroMetallicBlue,
    error = RetroRedAccent
)

@Composable
fun RetroSpotTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RetroColorScheme,
        typography = RetroTypography,
        content = content
    )
}
