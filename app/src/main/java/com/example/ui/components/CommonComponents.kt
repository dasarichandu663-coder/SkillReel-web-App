package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Challenge
import com.example.data.DifficultyLevel
import com.example.data.Story
import com.example.ui.theme.*

@Composable
fun UserAvatar(
    url: String,
    name: String,
    modifier: Modifier = Modifier,
    size: Int = 40,
    hasStoryRing: Boolean = false,
    isStoryViewed: Boolean = false,
    hasGlowRing: Boolean = false
) {
    val showRing = hasStoryRing || hasGlowRing
    if (showRing) {
        val ringBrush = if (isStoryViewed) androidx.compose.ui.graphics.SolidColor(InstagramBorderLight) else InstagramStoryBrush
        Box(
            modifier = modifier
                .size((size + 8).dp)
                .clip(CircleShape)
                .border(2.dp, ringBrush, CircleShape)
                .padding(2.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .clip(CircleShape)
                    .background(InstagramCard)
                    .border(1.5.dp, InstagramBlack, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AvatarImage(url = url, name = name, size = size)
            }
        }
    } else {
        Box(
            modifier = modifier
                .size(size.dp)
                .clip(CircleShape)
                .background(InstagramCard)
                .border(0.5.dp, InstagramBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AvatarImage(url = url, name = name, size = size)
        }
    }
}

@Composable
private fun AvatarImage(url: String, name: String, size: Int) {
    if (url.isNotBlank()) {
        AsyncImage(
            model = url,
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        val initials = name.split(" ")
            .mapNotNull { it.firstOrNull()?.toString() }
            .take(2)
            .joinToString("")
        Text(
            text = initials.ifEmpty { "SR" },
            color = Color.White,
            fontSize = (size / 2.5).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SkillBadge(
    skill: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(6.dp)),
        color = InstagramCard,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = InstagramBlue,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = skill,
                color = TextWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SkillTagChip(
    skill: String,
    modifier: Modifier = Modifier
) {
    SkillBadge(skill = skill, modifier = modifier)
}

@Composable
fun DifficultyBadge(
    difficulty: DifficultyLevel,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(6.dp)),
        color = InstagramCard,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder)
    ) {
        Text(
            text = difficulty.name.uppercase(),
            color = TextGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun XpBadge(
    xp: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .testTag("xp_badge"),
        color = InstagramCard,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = "XP",
                tint = SkillGold,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = "$xp XP",
                color = SkillGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StreakBadge(
    days: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .testTag("streak_badge"),
        color = InstagramCard,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Streak",
                tint = Color(0xFFFF6D00),
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = "${days}d",
                color = Color(0xFFFF6D00),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SkillProgressBar(
    progressPercent: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(InstagramBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = (progressPercent / 100f).coerceIn(0f, 1f))
                .clip(RoundedCornerShape(2.dp))
                .background(InstagramBlue)
        )
    }
}

@Composable
fun StoryViewerDialog(
    story: Story,
    onDismiss: () -> Unit,
    onSolveChallenge: (Int, String) -> Unit
) {
    var showChallenge by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            if (story.hasChallenge && story.challenge != null) {
                Button(
                    onClick = { showChallenge = true },
                    colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                    modifier = Modifier.testTag("story_challenge_btn")
                ) {
                    Text("⚡ Answer Quiz (+${story.challenge.xpReward} XP)", color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text("Close", color = Color.White)
                }
            }
        },
        containerColor = InstagramCard,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                UserAvatar(url = story.creatorAvatarUrl, name = story.creatorName, size = 36, hasStoryRing = true)
                Column {
                    Text(text = story.creatorName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = story.title, color = TextGray, fontSize = 12.sp)
                }
            }
        },
        text = {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    color = InstagramBlack
                ) {
                    Text(
                        text = story.snippet,
                        color = TextWhite,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }
    )

    if (showChallenge && story.challenge != null) {
        ChallengeDialog(
            challenge = story.challenge,
            onDismiss = {
                showChallenge = false
                onDismiss()
            },
            onCorrectAnswer = onSolveChallenge
        )
    }
}
