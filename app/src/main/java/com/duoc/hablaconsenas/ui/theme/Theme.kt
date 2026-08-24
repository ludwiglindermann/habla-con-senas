package com.duoc.hablaconsenas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = NeutralSurface,
    primaryContainer = BlueContainer,
    onPrimaryContainer = BluePrimaryDark,
    secondary = AmberSecondary,
    onSecondary = NeutralSurface,
    secondaryContainer = AmberContainer,
    onSecondaryContainer = AmberSecondary,
    error = ErrorRed,
    background = NeutralBackground,
    onBackground = NeutralOnSurface,
    surface = NeutralSurface,
    onSurface = NeutralOnSurface,
    surfaceVariant = BlueContainer,
    onSurfaceVariant = NeutralOnSurfaceVariant
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueLight,
    onPrimary = BluePrimaryDark,
    primaryContainer = BluePrimaryDark,
    onPrimaryContainer = BlueContainer,
    secondary = AmberContainer,
    onSecondary = AmberSecondary,
    error = ErrorRed
)

@Composable
fun HablaConSenasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
