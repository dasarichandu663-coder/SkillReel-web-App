package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.ui.components.UserAvatar
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun CreateScreen(
    repository: SkillReelRepository,
    geminiService: GeminiService,
    onPublished: () -> Unit,
    onCancel: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val user by repository.currentUser.collectAsState()

    var topicTitle by remember { mutableStateOf("") }
    var captionText by remember { mutableStateOf("") }
    var hashtagsText by remember { mutableStateOf("#LearnOnSkillReel #Tech") }
    var selectedSkill by remember { mutableStateOf("Python Programming") }
    var selectedDifficulty by remember { mutableStateOf(DifficultyLevel.Beginner) }

    var includeChallenge by remember { mutableStateOf(true) }
    var challengeQuestion by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var isAiGenerating by remember { mutableStateOf(false) }

    val skills = listOf("Python Programming", "AI & Machine Learning", "SQL", "Web Dev", "Design", "Career")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("create_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar: Cancel, New Reel, Share
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cancel",
                    color = TextWhite,
                    fontSize = 15.sp,
                    modifier = Modifier.clickable { onCancel() }
                )

                Text(
                    text = "New Reel",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Share",
                    color = InstagramBlue,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            val title = topicTitle.ifBlank { "Modern Coding Tip" }
                            val desc = captionText.ifBlank { "Learn something new on SkillReel" }
                            val tags = hashtagsText.split(" ").filter { it.isNotBlank() }
                            val options = if (includeChallenge && optionA.isNotBlank() && optionB.isNotBlank()) {
                                listOf(optionA, optionB, "Neither", "Both")
                            } else null

                            repository.publishReel(
                                title = title,
                                description = desc,
                                skill = selectedSkill,
                                difficulty = selectedDifficulty,
                                hashtags = tags,
                                challengeQuestion = if (includeChallenge) challengeQuestion.ifBlank { "What is the key takeaway?" } else null,
                                options = options,
                                correctOptionIndex = 0
                            )
                            onPublished()
                        }
                        .testTag("instagram_share_btn")
                )
            }
        }

        item {
            HorizontalDivider(color = InstagramBorder, thickness = 0.5.dp)
        }

        // Media Preview Box & Caption Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Video thumbnail preview
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 110.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.verticalGradient(listOf(Color(0xFF2E1065), Color(0xFF3B82F6)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                }

                // Caption TextField
                OutlinedTextField(
                    value = captionText,
                    onValueChange = { captionText = it },
                    placeholder = { Text("Write a caption or educational insight...", color = TextDarkGray, fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .testTag("create_caption_field"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = InstagramCard,
                        unfocusedContainerColor = InstagramCard,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )
            }
        }

        // Reel Title
        item {
            OutlinedTextField(
                value = topicTitle,
                onValueChange = { topicTitle = it },
                label = { Text("Reel Title", color = TextDarkGray) },
                placeholder = { Text("e.g. Python Variables in 30 Seconds", color = TextDarkGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = InstagramBorder,
                    unfocusedBorderColor = InstagramBorder,
                    focusedContainerColor = InstagramCard,
                    unfocusedContainerColor = InstagramCard,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                )
            )
        }

        // Magic Wand / Write with AI tool (Instagram-style subtle tool)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        if (topicTitle.isNotBlank()) {
                            coroutineScope.launch {
                                isAiGenerating = true
                                captionText = geminiService.generateCaption(topicTitle)
                                val tags = geminiService.generateHashtags(topicTitle)
                                hashtagsText = tags.joinToString(" ")
                                val quiz = geminiService.generateQuiz(topicTitle)
                                challengeQuestion = quiz.question
                                if (quiz.options.size >= 2) {
                                    optionA = quiz.options[0]
                                    optionB = quiz.options[1]
                                }
                                isAiGenerating = false
                            }
                        }
                    },
                color = InstagramCard,
                border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = InstagramBlue, modifier = Modifier.size(18.dp))
                    Text(
                        text = if (isAiGenerating) "Auto-generating caption..." else "Auto-suggest caption & quiz (AI Tool)",
                        color = InstagramBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Skill Category Picker
        item {
            Text(text = "Select Skill Topic", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                skills.take(3).forEach { skill ->
                    val isSelected = selectedSkill == skill
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) Color.White else InstagramCard,
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedSkill = skill }
                    ) {
                        Text(
                            text = skill,
                            color = if (isSelected) Color.Black else TextWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        // Attach Interactive Challenge Toggle
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("⚡ Add Quiz Challenge", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Switch(
                    checked = includeChallenge,
                    onCheckedChange = { includeChallenge = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = InstagramBlue)
                )
            }

            if (includeChallenge) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = challengeQuestion,
                        onValueChange = { challengeQuestion = it },
                        placeholder = { Text("Quiz Question: e.g. Which keyword declares a variable?", color = TextDarkGray, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InstagramBorder,
                            unfocusedBorderColor = InstagramBorder,
                            focusedContainerColor = InstagramCard,
                            unfocusedContainerColor = InstagramCard,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                    OutlinedTextField(
                        value = optionA,
                        onValueChange = { optionA = it },
                        placeholder = { Text("Correct Answer", color = TextDarkGray, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InstagramBorder,
                            unfocusedBorderColor = InstagramBorder,
                            focusedContainerColor = InstagramCard,
                            unfocusedContainerColor = InstagramCard,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                    OutlinedTextField(
                        value = optionB,
                        onValueChange = { optionB = it },
                        placeholder = { Text("Incorrect Answer", color = TextDarkGray, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InstagramBorder,
                            unfocusedBorderColor = InstagramBorder,
                            focusedContainerColor = InstagramCard,
                            unfocusedContainerColor = InstagramCard,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
