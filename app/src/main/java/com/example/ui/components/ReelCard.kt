package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Reel
import com.example.ui.theme.*

@Composable
fun ReelCard(
    reel: Reel,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
    onFollowClick: () -> Unit,
    onTryChallengeClick: () -> Unit,
    onCreatorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPlaying = !isPlaying
            }
            .testTag("reel_card_${reel.id}")
    ) {
        // Video Preview Gradient Surface
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        reel.videoPreviewGradient.map { Color(it) } + listOf(Color.Black)
                    )
                )
        )

        // Center Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.4f),
                modifier = Modifier.size(60.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = reel.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 26.sp
            )
        }

        // Right-Side Instagram Action Column
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onLikeClick() }) {
                Icon(
                    imageVector = if (reel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (reel.isLiked) InstagramHeartRed else Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Text(text = "${reel.likesCount}", color = Color.White, fontSize = 11.sp)
            }

            // Comment
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onCommentClick() }) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
                Text(text = "${reel.commentsCount}", color = Color.White, fontSize = 11.sp)
            }

            // Share
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onShareClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
                Text(text = "${reel.sharesCount}", color = Color.White, fontSize = 11.sp)
            }

            // Save
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onSaveClick() }) {
                Icon(
                    imageVector = if (reel.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Save",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // Bottom-Left Info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 14.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UserAvatar(
                    url = reel.creatorAvatarUrl,
                    name = reel.creatorName,
                    size = 36
                )
                Text(
                    text = reel.creatorUsername,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onCreatorClick() }
                )
                if (!reel.isFollowing) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Transparent,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                        modifier = Modifier.clickable { onFollowClick() }
                    ) {
                        Text(
                            text = "Follow",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Text(
                text = reel.description,
                color = Color.White,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (reel.challenge != null) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SkillGold),
                    modifier = Modifier.clickable { onTryChallengeClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("⚡", fontSize = 12.sp)
                        Text(
                            text = "Solve Quiz (+10 XP)",
                            color = SkillGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
