package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Instagram-Authentic Dark Theme Palette
val InstagramBlack = Color(0xFF000000)
val InstagramSurface = Color(0xFF121212)
val InstagramCard = Color(0xFF1A1A1A)
val InstagramCardHover = Color(0xFF262626)
val InstagramBorder = Color(0xFF262626)
val InstagramBorderLight = Color(0xFF363636)

// Instagram Brand Colors
val InstagramBlue = Color(0xFF0095F6)
val InstagramBluePressed = Color(0xFF1877F2)
val InstagramHeartRed = Color(0xFFED4956)
val InstagramVerified = Color(0xFF0095F6)

// Instagram Story Gradient Colors
val StoryGradientStart = Color(0xFFFBAA47)
val StoryGradientMiddle = Color(0xFFD91A46)
val StoryGradientEnd = Color(0xFFA60F93)

val InstagramStoryBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFBAA47),
        Color(0xFFD91A46),
        Color(0xFFA60F93)
    )
)

// Learning & Skill Accent Highlights
val SkillGold = Color(0xFFFFD700)
val SkillGreen = Color(0xFF00C853)

// Text Colors
val TextWhite = Color(0xFFF5F5F5)
val TextGray = Color(0xFFA8A8A8)
val TextDarkGray = Color(0xFF737373)

// Semantic token aliases for cross-screen consistency
val ObsidianVoid = InstagramBlack
val SurfaceDark = InstagramSurface
val SurfaceCard = InstagramCard
val SurfaceCardHover = InstagramCardHover
val SurfaceBorder = InstagramBorder

val ElectricViolet = InstagramBlue
val ElectricVioletLight = Color(0xFF38BDF8)
val ElectricVioletDark = InstagramBluePressed

val CyanAccent = InstagramBlue
val CyanAccentLight = Color(0xFF38BDF8)

val XpGold = SkillGold
val XpGoldLight = Color(0xFFFFF59D)

val StreakFlame = Color(0xFFFF6D00)
val SuccessEmerald = SkillGreen
val ErrorRose = InstagramHeartRed

val TextPrimaryDark = TextWhite
val TextSecondaryDark = TextGray
val TextMutedDark = TextDarkGray
