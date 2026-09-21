package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
    darkColorScheme(
        primary = DiamondCyan,
        onPrimary = Color(0xFF00363F),
        primaryContainer = Color(0xFF004E5B),
        onPrimaryContainer = DiamondCyanLight,
        secondary = FlameGold,
        onSecondary = Color(0xFF452200),
        secondaryContainer = Color(0xFF633300),
        onSecondaryContainer = FlameYellow,
        tertiary = GemPurpleLight,
        onTertiary = Color(0xFF38006B),
        tertiaryContainer = Color(0xFF55198B),
        onTertiaryContainer = Color(0xFFEADBFF),
        background = DarkNavyBackground,
        onBackground = TextPrimary,
        surface = DarkNavySurface,
        onSurface = TextPrimary,
        surfaceVariant = DarkNavySurfaceVariant,
        onSurfaceVariant = TextSecondary,
        outline = DarkNavyCardBorder,
        outlineVariant = Color(0xFF222C46),
        error = ErrorRed,
        onError = Color.White
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Color(0xFF00758A),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFB2EBFC),
        onPrimaryContainer = Color(0xFF001F26),
        secondary = Color(0xFFB25000),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFFFDCC5),
        onSecondaryContainer = Color(0xFF331400),
        tertiary = Color(0xFF6750A4),
        onTertiary = Color.White,
        background = Color(0xFFF6F8FC),
        onBackground = Color(0xFF191C1E),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF191C1E),
        surfaceVariant = Color(0xFFE1E7F0),
        onSurfaceVariant = Color(0xFF44474E),
        outline = Color(0xFFC6D0DC),
        error = Color(0xFFBA1A1A),
        onError = Color.White
    )

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek gaming dark theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
  )
}

