package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
fun LearnScreen(
    repository: SkillReelRepository,
    onOpenAiCoach: () -> Unit,
    onSelectReel: (Reel) -> Unit
) {
    val user by repository.currentUser.collectAsState()
    val skills by repository.skills.collectAsState()
    val learningPaths by repository.learningPaths.collectAsState()
    var selectedPath by remember { mutableStateOf<LearningPath?>(learningPaths.firstOrNull()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianVoid)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("learn_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Learning",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Track paths, progress & micro-credentials",
                        color = TextSecondaryDark,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = onOpenAiCoach,
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("open_ai_coach_learn_btn")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("AI Coach", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Daily Goal Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp)),
                color = SurfaceDark
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF2A1408),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StreakFlame),
                        modifier = Modifier.size(50.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🔥", fontSize = 24.sp)
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Daily Goal: ${user.dailyGoalMinutes} min",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "12/20 min",
                                color = CyanAccent,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        SkillProgressBar(progressPercent = 60)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${user.streakDays}-day streak active. Watch 1 more Reel to maintain it!",
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Active Skill Mastery Bars
        item {
            Text(
                text = "Skill Mastery",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceDark)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                skills.take(4).forEach { skill ->
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = skill.name,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${skill.progressPercent}%",
                                color = CyanAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        SkillProgressBar(progressPercent = skill.progressPercent)
                    }
                }
            }
        }

        // Learning Paths Section
        item {
            Text(
                text = "Structured Career Paths",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(learningPaths) { path ->
            val isSelected = selectedPath?.id == path.id
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, if (isSelected) ElectricViolet else SurfaceBorder, RoundedCornerShape(18.dp))
                    .clickable { selectedPath = path }
                    .testTag("learn_path_${path.id}"),
                color = SurfaceDark
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ElectricViolet.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = path.category.uppercase(),
                                color = ElectricVioletLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        if (path.isEnrolled) {
                            Text(
                                text = "ENROLLED",
                                color = SuccessEmerald,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = path.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = path.description,
                        color = TextSecondaryDark,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${path.stages.size} Stages • ${path.estimatedHours} Hours total",
                            color = TextMutedDark,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "${path.progressPercent}% Complete",
                            color = CyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    SkillProgressBar(progressPercent = path.progressPercent)

                    // Stages Breakdown
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = SurfaceBorder)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Milestone Stages:",
                            color = TextPrimaryDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            path.stages.forEach { stage ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp)),
                                    color = SurfaceCard
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = when {
                                                stage.isCompleted -> SuccessEmerald.copy(alpha = 0.2f)
                                                stage.isCurrent -> ElectricViolet.copy(alpha = 0.3f)
                                                else -> SurfaceCardHover
                                            },
                                            modifier = Modifier.size(26.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = if (stage.isCompleted) "✓" else "${stage.stageNumber}",
                                                    color = if (stage.isCompleted) SuccessEmerald else Color.White,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = stage.title,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = stage.description,
                                                color = TextSecondaryDark,
                                                fontSize = 10.sp
                                            )
                                        }

                                        if (stage.isCurrent) {
                                            Text(
                                                text = "NEXT",
                                                color = XpGold,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
