package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.*
import kotlinx.coroutines.launch

@Composable
fun ReelsScreen(
    repository: SkillReelRepository,
    onCreatorClick: (String) -> Unit
) {
    val allReels by repository.reels.collectAsState()
    val commentsMap by repository.commentsMap.collectAsState()

    var activeCommentReel by remember { mutableStateOf<Reel?>(null) }
    var activeShareReel by remember { mutableStateOf<Reel?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Smooth spinning animation for the header refresh icon when refreshing
    val infiniteTransition = rememberInfiniteTransition(label = "reels_header_refresh")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "header_spin"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Standalone Instagram Vertical Reel Feed Component with Pull-To-Refresh
        InstagramVerticalReelFeed(
            reels = allReels,
            isRefreshing = isRefreshing,
            onRefresh = {
                coroutineScope.launch {
                    isRefreshing = true
                    repository.refreshFeed()
                    isRefreshing = false
                }
            },
            onLikeClick = { repository.toggleLike(it.id) },
            onCommentClick = { activeCommentReel = it },
            onSaveClick = { repository.toggleSave(it.id) },
            onShareClick = { activeShareReel = it },
            onFollowClick = { repository.toggleFollowCreator(it.creatorId) },
            onCreatorClick = onCreatorClick,
            onChallengeAnswered = { xp, skill ->
                repository.recordChallengeSolved(xp, skill)
            },
            headerOverlay = {
                // Top Instagram Reels Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reels",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Quick Refresh Button
                        IconButton(
                            onClick = {
                                if (!isRefreshing) {
                                    coroutineScope.launch {
                                        isRefreshing = true
                                        repository.refreshFeed()
                                        isRefreshing = false
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Feed",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(if (isRefreshing) spinAngle else 0f)
                            )
                        }

                        // Camera Button
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Outlined.CameraAlt,
                                contentDescription = "Camera",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        )

        // Comments Bottom Sheet
        if (activeCommentReel != null) {
            val comments = commentsMap[activeCommentReel!!.id] ?: emptyList()
            CommentsBottomSheet(
                reelTitle = activeCommentReel!!.title,
                comments = comments,
                onDismiss = { activeCommentReel = null },
                onAddComment = { text ->
                    repository.addComment(activeCommentReel!!.id, text)
                }
            )
        }

        // Share Bottom Sheet
        if (activeShareReel != null) {
            ShareBottomSheet(
                reelTitle = activeShareReel!!.title,
                onDismiss = { activeShareReel = null },
                onShareToStudyGroup = {
                    repository.sendMessage("chat_1", "Shared Reel: ${activeShareReel!!.title}")
                }
            )
        }
    }
}
