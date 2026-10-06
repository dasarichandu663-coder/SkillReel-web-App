package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DifficultyLevel
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    onComplete: (interests: List<String>, goal: String, level: DifficultyLevel, minutes: Int) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }

    val allInterests = listOf(
        "Coding", "Artificial Intelligence", "Data Science", "Business",
        "Finance", "Design", "Photography", "Video Editing",
        "Communication", "English", "Science", "Technology",
        "Career", "Entrepreneurship", "Personal Development", "Practical Skills"
    )
    val selectedInterests = remember { mutableStateListOf("Coding", "Artificial Intelligence") }

    val careerRoles = listOf(
        "Software Developer", "AI Engineer", "Data Scientist", "Designer",
        "Entrepreneur", "Content Creator", "Photographer", "Digital Marketer",
        "Researcher", "Student"
    )
    var selectedGoal by remember { mutableStateOf("AI Engineer") }

    val levels = listOf(DifficultyLevel.Beginner, DifficultyLevel.Intermediate, DifficultyLevel.Advanced)
    var selectedLevel by remember { mutableStateOf(DifficultyLevel.Intermediate) }

    val times = listOf(5, 15, 30, 60)
    var selectedMinutes by remember { mutableIntStateOf(15) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("onboarding_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step Indicator Header
            if (step > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Step $step of 6",
                        color = TextGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        (1..6).forEach { i ->
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        if (i <= step) InstagramBlue else InstagramBorder
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            // Step Content
            when (step) {
                1 -> {
                    // SCREEN 1: Welcome (Instagram App Styling)
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(3.dp, InstagramStoryBrush, CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = InstagramCard,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("SR", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "SkillReel",
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-1).sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "“Scroll. Learn. Grow.”",
                        color = TextGray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "The social network for skills, creators, and your future.",
                        color = TextDarkGray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { step = 2 },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("get_started_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Get Started", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            onComplete(selectedInterests.toList(), selectedGoal, selectedLevel, selectedMinutes)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("login_btn"),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, InstagramBorder)
                    ) {
                        Text("Log In", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }

                2 -> {
                    // SCREEN 2: What do you want to learn?
                    Text(
                        text = "What do you want to learn?",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Follow topics to personalize your feed",
                        color = TextGray,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(allInterests) { interest ->
                            val isSelected = selectedInterests.contains(interest)
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        1.dp,
                                        if (isSelected) InstagramBlue else InstagramBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        if (isSelected) selectedInterests.remove(interest)
                                        else selectedInterests.add(interest)
                                    }
                                    .testTag("interest_$interest"),
                                color = if (isSelected) InstagramCard else InstagramCard
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = interest,
                                        color = if (isSelected) Color.White else TextGray,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = InstagramBlue, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { step = 3 },
                        enabled = selectedInterests.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("next_step_2"),
                        colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Next (${selectedInterests.size} selected)", fontWeight = FontWeight.Bold)
                    }
                }

                3 -> {
                    // SCREEN 3: Career goal
                    Text(
                        text = "What do you want to become?",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "We will highlight creator Reels in this domain",
                        color = TextGray,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(careerRoles) { role ->
                            val isSelected = selectedGoal == role
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        1.dp,
                                        if (isSelected) InstagramBlue else InstagramBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedGoal = role }
                                    .testTag("role_$role"),
                                color = InstagramCard
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = role,
                                        color = if (isSelected) Color.White else TextGray,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { step = 4 },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("next_step_3"),
                        colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Next", fontWeight = FontWeight.Bold)
                    }
                }

                4 -> {
                    // SCREEN 4: Level
                    Text(
                        text = "What is your current level?",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Starting difficulty for suggested content",
                        color = TextGray,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        levels.forEach { level ->
                            val isSelected = selectedLevel == level
                            val desc = when (level) {
                                DifficultyLevel.Beginner -> "Starting from fundamentals and intuition"
                                DifficultyLevel.Intermediate -> "Hands-on projects and problem solving"
                                DifficultyLevel.Advanced -> "In-depth architecture and optimization"
                            }
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, if (isSelected) InstagramBlue else InstagramBorder, RoundedCornerShape(10.dp))
                                    .clickable { selectedLevel = level }
                                    .testTag("level_${level.name}"),
                                color = InstagramCard
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = level.name,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = desc,
                                        color = TextGray,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { step = 5 },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("next_step_4"),
                        colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Next", fontWeight = FontWeight.Bold)
                    }
                }

                5 -> {
                    // SCREEN 5: Daily learning pace
                    Text(
                        text = "Daily learning goal",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "How much time do you want to learn each day?",
                        color = TextGray,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        times.forEach { mins ->
                            val isSelected = selectedMinutes == mins
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, if (isSelected) InstagramBlue else InstagramBorder, RoundedCornerShape(10.dp))
                                    .clickable { selectedMinutes = mins }
                                    .testTag("mins_$mins"),
                                color = InstagramCard
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "$mins minutes / day",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = InstagramBlue)
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { step = 6 },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("next_step_5"),
                        colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Next", fontWeight = FontWeight.Bold)
                    }
                }

                6 -> {
                    // SCREEN 6: Ready!
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .border(3.dp, InstagramStoryBrush, CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = InstagramCard,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Welcome to SkillReel",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Your feed is personalized around $selectedGoal and your interests.",
                        color = TextGray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            onComplete(selectedInterests.toList(), selectedGoal, selectedLevel, selectedMinutes)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("start_learning_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Start Exploring", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
