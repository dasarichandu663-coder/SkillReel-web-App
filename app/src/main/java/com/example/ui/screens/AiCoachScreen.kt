package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

data class CoachMessage(
    val id: String,
    val sender: String,
    val text: String,
    val isUser: Boolean,
    val hasRoadmap: Boolean = false,
    val roadmapSteps: List<String>? = null
)

@Composable
fun AiCoachScreen(
    repository: SkillReelRepository,
    geminiService: GeminiService,
    onStartRoadmap: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val user by repository.currentUser.collectAsState()
    val skills by repository.skills.collectAsState()
    val listState = rememberLazyListState()

    var inputPrompt by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            CoachMessage(
                id = "m1",
                sender = "SkillReel Coach",
                text = "Hey Alex! 👋 I'm your SkillReel AI Coach. I track your goal (${user.careerGoal}) and your recent Python & Machine Learning reels.\n\nWhat are we focusing on today?",
                isUser = false
            ),
            CoachMessage(
                id = "m2",
                sender = "Alex",
                text = "How can I become an AI Engineer step by step?",
                isUser = true
            ),
            CoachMessage(
                id = "m3",
                sender = "SkillReel Coach",
                text = "Here is your customized high-yield progression to land a top AI role:",
                isUser = false,
                hasRoadmap = true,
                roadmapSteps = listOf(
                    "1. Python Essentials & NumPy Vectorization (80% complete)",
                    "2. Linear Algebra & Calculus Intuition (Next up!)",
                    "3. Scikit-Learn Classical ML Algorithms",
                    "4. PyTorch Deep Neural Networks & Backprop",
                    "5. Transformers & HuggingFace Generative AI",
                    "6. Production Deployment with FastAPI & Docker"
                )
            )
        )
    }

    val promptSuggestions = listOf(
        "What should I learn next?",
        "Create a 30-day AI plan",
        "Explain SQL Joins simply",
        "Give me a real-world project",
        "Test my knowledge with a quiz"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("ai_coach_screen")
    ) {
        // Top Header - Instagram DM Style
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF262626),
                    border = androidx.compose.foundation.BorderStroke(1.dp, InstagramBorder),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981))
                        .border(2.dp, InstagramBlack, CircleShape)
                )
            }
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "SkillReel Coach",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = InstagramBlue,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = "Active now • Goal: ${user.careerGoal}",
                    color = TextGray,
                    fontSize = 12.sp
                )
            }
        }

        HorizontalDivider(color = InstagramBorder, thickness = 0.5.dp)

        // Chat Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                if (msg.isUser) {
                    // User message bubble (Instagram Blue)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(
                            shape = RoundedCornerShape(topStart = 18.dp, topEnd = 4.dp, bottomStart = 18.dp, bottomEnd = 18.dp),
                            color = InstagramBlue,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Text(
                                text = msg.text,
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                } else {
                    // AI Coach message bubble (Instagram Dark Gray)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp),
                            color = Color(0xFF262626),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder),
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = msg.text,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )

                                if (msg.hasRoadmap && msg.roadmapSteps != null) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF18181B))
                                            .padding(12.dp)
                                    ) {
                                        msg.roadmapSteps.forEach { stepText ->
                                            Text(
                                                text = stepText,
                                                color = if (stepText.contains("Next")) InstagramBlue else Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = if (stepText.contains("Next")) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Button(
                                            onClick = { onStartRoadmap("Become an AI Engineer") },
                                            modifier = Modifier.fillMaxWidth().height(40.dp).testTag("start_roadmap_btn"),
                                            colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Start Roadmap", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = InstagramBlue, strokeWidth = 2.dp)
                        Text("Coach is thinking...", color = TextGray, fontSize = 12.sp)
                    }
                }
            }
        }

        // Prompt Suggestions Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            items(promptSuggestions) { prompt ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1C1C1E),
                    border = androidx.compose.foundation.BorderStroke(1.dp, InstagramBorder),
                    modifier = Modifier.clickable {
                        inputPrompt = prompt
                    }
                ) {
                    Text(
                        text = prompt,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }

        // Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputPrompt,
                onValueChange = { inputPrompt = it },
                placeholder = { Text("Message SkillReel Coach...", color = TextGray, fontSize = 13.sp) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("coach_input_field"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = InstagramBlue,
                    unfocusedBorderColor = InstagramBorder,
                    focusedContainerColor = Color(0xFF1C1C1E),
                    unfocusedContainerColor = Color(0xFF1C1C1E),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            IconButton(
                onClick = {
                    if (inputPrompt.isNotBlank()) {
                        val userText = inputPrompt
                        inputPrompt = ""
                        messages.add(
                            CoachMessage(
                                id = "msg_${System.currentTimeMillis()}",
                                sender = user.name,
                                text = userText,
                                isUser = true
                            )
                        )
                        coroutineScope.launch {
                            isThinking = true
                            val skillsSummary = skills.joinToString(", ") { "${it.name} (${it.progressPercent}%)" }
                            val aiReply = geminiService.learningCoach(
                                userMessage = userText,
                                userGoal = user.careerGoal,
                                currentSkills = skillsSummary
                            )
                            isThinking = false
                            messages.add(
                                CoachMessage(
                                    id = "msg_${System.currentTimeMillis()}",
                                    sender = "SkillReel Coach",
                                    text = aiReply,
                                    isUser = false
                                )
                            )
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (inputPrompt.isNotBlank()) InstagramBlue else Color(0xFF262626))
                    .testTag("send_coach_msg_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (inputPrompt.isNotBlank()) Color.White else TextGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
