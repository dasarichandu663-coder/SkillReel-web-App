package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val InstagramDarkColorScheme = darkColorScheme(
    primary = InstagramBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = Color.White,
    secondary = InstagramHeartRed,
    onSecondary = Color.White,
    tertiary = SkillGold,
    background = InstagramBlack,
    onBackground = TextWhite,
    surface = InstagramSurface,
    onSurface = TextWhite,
    surfaceVariant = InstagramCard,
    onSurfaceVariant = TextGray,
    outline = InstagramBorder,
    outlineVariant = InstagramBorderLight,
    error = InstagramHeartRed,
    onError = Color.White
)

@Composable
fun SkillReelTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = InstagramDarkColorScheme,
        typography = Typography,
        content = content
    )
}
