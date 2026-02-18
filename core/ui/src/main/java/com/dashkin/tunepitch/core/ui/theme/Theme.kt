package com.dashkin.tunepitch.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NeonColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = NeonBackground,
    primaryContainer = NeonCyanMuted,
    onPrimaryContainer = NeonCyan,
    secondary = NeonMagenta,
    onSecondary = NeonBackground,
    secondaryContainer = NeonMagentaMuted,
    onSecondaryContainer = NeonMagenta,
    tertiary = NeonPurple,
    onTertiary = NeonBackground,
    tertiaryContainer = NeonPurpleDim,
    onTertiaryContainer = NeonPurple,
    background = NeonBackground,
    onBackground = NeonTextPrimary,
    surface = NeonSurface,
    onSurface = NeonTextPrimary,
    surfaceVariant = NeonSurfaceVariant,
    onSurfaceVariant = NeonTextSecondary,
    surfaceContainerLowest = Color(0xFF08081A),
    surfaceContainerLow = NeonSurface,
    surfaceContainer = NeonSurfaceVariant,
    surfaceContainerHigh = NeonSurfaceBright,
    surfaceContainerHighest = Color(0xFF2A2A62),
    outline = NeonCyanMuted,
    outlineVariant = NeonSurfaceBright,
    inverseSurface = NeonTextPrimary,
    inverseOnSurface = NeonBackground,
    inversePrimary = NeonCyanDim,
    error = PitchRed,
    onError = NeonBackground,
    errorContainer = Color(0xFF3D0018),
    onErrorContainer = PitchRed
)

@Composable
fun TunePitchTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NeonColorScheme,
        typography = Typography,
        content = content
    )
}
