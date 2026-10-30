package com.khameen901.automotivefaultlog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    secondary = AccentTeal,
    background = SurfaceLight,
    surface = CardBackground,
    onPrimary = White,
    onSecondary = White,
    onBackground = PrimaryDark,
    onSurface = PrimaryDark,
    error = ErrorRed,
    onError = White
)

private val DarkColors = darkColorScheme(
    primary = PrimaryBlue,
    secondary = AccentTeal,
    background = PrimaryDark,
    surface = PrimaryDark,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    error = ErrorRed,
    onError = White
)

@Composable
fun AutomotiveFaultLogTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
