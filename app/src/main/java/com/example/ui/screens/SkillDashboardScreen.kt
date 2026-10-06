package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun SkillDashboardScreen(
    repository: SkillReelRepository
) {
    val user by repository.currentUser.collectAsState()
    val skills by repository.skills.collectAsState()
    val achievements by repository.achievements.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianVoid)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("skill_dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                Text(
                    text = "Skill Analytics",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Compound growth & gamified achievements",
                    color = TextSecondaryDark,
                    fontSize = 12.sp
                )
            }
        }

        // Top Key Metrics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Total XP",
                    value = "${user.xp}",
                    icon = "⚡",
                    accent = XpGold,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Streak",
                    value = "${user.streakDays} Days",
                    icon = "🔥",
                    accent = StreakFlame,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Hours Learned",
                    value = "${user.hoursLearned}h",
                    icon = "⏱️",
                    accent = CyanAccent,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Challenges Solved",
                    value = "${user.challengesCompleted}",
                    icon = "🏆",
                    accent = SuccessEmerald,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Weekly Activity Visualizer
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp)),
                color = SurfaceDark
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Weekly Activity (Reels & Challenges)",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    val heights = listOf(0.4f, 0.7f, 0.9f, 0.6f, 0.85f, 1.0f, 0.75f)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEachIndexed { idx, day ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .fillMaxHeight(heights[idx])
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (idx == 5) ElectricViolet else CyanAccent.copy(alpha = 0.6f))
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = day, color = TextSecondaryDark, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // Active Skills List
        item {
            Text(
                text = "Detailed Skill Levels",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(skills) { skill ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                color = SurfaceDark
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = skill.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${skill.level} • ${skill.xp} XP earned",
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = "${skill.progressPercent}%",
                            color = CyanAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    SkillProgressBar(progressPercent = skill.progressPercent)
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Next recommendation: ${skill.nextLessonTitle}",
                        color = ElectricVioletLight,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Achievements List
        item {
            Text(
                text = "Achievements & Badges 🏅",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(achievements) { ach ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, if (ach.isUnlocked) XpGold.copy(alpha = 0.4f) else SurfaceBorder, RoundedCornerShape(14.dp)),
                color = SurfaceDark
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (ach.isUnlocked) Color(0xFF261E0A) else SurfaceCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (ach.isUnlocked) XpGold else SurfaceBorder),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(ach.iconEmoji, fontSize = 20.sp)
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ach.title,
                            color = if (ach.isUnlocked) Color.White else TextMutedDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = ach.description,
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (ach.isUnlocked) "UNLOCKED (+${ach.xpBonus} XP)" else "${ach.currentProgress}/${ach.targetProgress}",
                            color = if (ach.isUnlocked) XpGold else TextMutedDark,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
        color = SurfaceDark
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, color = TextSecondaryDark, fontSize = 11.sp)
                Text(text = icon, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                color = accent,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
