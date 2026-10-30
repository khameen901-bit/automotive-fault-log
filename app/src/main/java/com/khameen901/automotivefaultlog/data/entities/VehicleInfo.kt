package com.khameen901.automotivefaultlog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    secondary = AccentTeal,
    background = SurfaceLight,
    surface = CardBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = PrimaryDark,
    onSurface = PrimaryDark,
    error = ErrorRed,
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = PrimaryBlue,
    secondary = AccentTeal,
    background = PrimaryDark,
    surface = PrimaryDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun AutomotiveFaultLogTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
