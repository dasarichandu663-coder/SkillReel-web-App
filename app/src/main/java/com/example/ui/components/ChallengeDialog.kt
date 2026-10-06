package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import com.example.data.Challenge
import com.example.ui.theme.*

@Composable
fun ChallengeDialog(
    challenge: Challenge,
    onDismiss: () -> Unit,
    onCorrectAnswer: (xpEarned: Int, skillName: String) -> Unit
) {
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    val isCorrect = selectedOptionIndex == challenge.correctIndex

    Dialog(onDismissRequest = onDismiss) {
        // Instagram Quiz Sticker Style
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, InstagramBorder, RoundedCornerShape(20.dp))
                .testTag("challenge_dialog"),
            color = InstagramCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top close button & tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, InstagramBorder)
                    ) {
                        Text(
                            text = "⚡ QUIZ STICKER",
                            color = SkillGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Question
                Text(
                    text = challenge.question,
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Options (Instagram Quiz Sticker Options)
                challenge.options.forEachIndexed { index, optionText ->
                    val isSelected = selectedOptionIndex == index
                    val optionBgColor = when {
                        !isAnswered -> InstagramBlack
                        index == challenge.correctIndex -> Color(0xFF00C853).copy(alpha = 0.25f)
                        isSelected && !isCorrect -> InstagramHeartRed.copy(alpha = 0.25f)
                        else -> InstagramBlack
                    }
                    val optionBorderColor = when {
                        !isAnswered -> InstagramBorder
                        index == challenge.correctIndex -> Color(0xFF00C853)
                        isSelected && !isCorrect -> InstagramHeartRed
                        else -> InstagramBorder
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, optionBorderColor, RoundedCornerShape(12.dp))
                            .clickable(enabled = !isAnswered) {
                                selectedOptionIndex = index
                                isAnswered = true
                                if (index == challenge.correctIndex) {
                                    onCorrectAnswer(challenge.xpReward, challenge.skillName)
                                }
                            }
                            .testTag("quiz_option_$index"),
                        color = optionBgColor
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val letter = ('A' + index).toString()
                            Surface(
                                shape = CircleShape,
                                color = if (isAnswered && index == challenge.correctIndex) Color(0xFF00C853) else InstagramCard,
                                modifier = Modifier.size(26.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = letter,
                                        color = TextWhite,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = optionText,
                                color = TextWhite,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Feedback
                AnimatedVisibility(visible = isAnswered) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isCorrect) "Correct! +${challenge.xpReward} XP" else "Nice try! Review and tap again.",
                            color = if (isCorrect) Color(0xFF00C853) else InstagramHeartRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = challenge.explanation,
                            color = TextGray,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(42.dp)
                        ) {
                            Text("Continue Watching", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
