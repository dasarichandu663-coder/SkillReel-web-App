package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    repository: SkillReelRepository,
    onNavigateToMessages: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToCreator: (String) -> Unit,
    onOpenReelsTab: () -> Unit
) {
    val user by repository.currentUser.collectAsState()
    val allReels by repository.reels.collectAsState()
    val stories by repository.stories.collectAsState()
    val commentsMap by repository.commentsMap.collectAsState()

    var activeChallenge by remember { mutableStateOf<Challenge?>(null) }
    var activeCommentReel by remember { mutableStateOf<Reel?>(null) }
    var activeShareReel by remember { mutableStateOf<Reel?>(null) }
    var activeStory by remember { mutableStateOf<Story?>(null) }

    // Pull-to-refresh setup
    val listState = rememberLazyListState()
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val pullThresholdPx = with(density) { 72.dp.toPx() }
    var pullDragOffset by remember { mutableFloatStateOf(0f) }

    val animatedPullOffset by animateFloatAsState(
        targetValue = if (isRefreshing) with(density) { 56.dp.toPx() } else pullDragOffset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "home_pull_offset"
    )

    val nestedScrollConnection = remember(listState, isRefreshing) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (pullDragOffset > 0 && available.y < 0) {
                    val newOffset = (pullDragOffset + available.y).coerceAtLeast(0f)
                    val consumedY = pullDragOffset - newOffset
                    pullDragOffset = newOffset
                    return Offset(0f, -consumedY)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0 && available.y > 0 && !isRefreshing) {
                    val dampedDelta = available.y * 0.45f
                    pullDragOffset = (pullDragOffset + dampedDelta).coerceAtMost(pullThresholdPx * 1.8f)
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (pullDragOffset >= pullThresholdPx && !isRefreshing) {
                    pullDragOffset = 0f
                    coroutineScope.launch {
                        isRefreshing = true
                        repository.refreshFeed()
                        isRefreshing = false
                    }
                } else {
                    pullDragOffset = 0f
                }
                return Velocity.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .nestedScroll(nestedScrollConnection)
            .testTag("home_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Instagram-Style Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Brand Wordmark
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "SkillReel",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Default,
                        letterSpacing = (-0.5).sp
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Feed selector",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Top Right Action Icons: Heart (Notifications) & Direct Messages (Paper Plane)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Notifications
                    Box(modifier = Modifier.clickable { onNavigateToNotifications() }) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(InstagramHeartRed)
                        )
                    }

                    // Direct Messages
                    Box(modifier = Modifier.clickable { onNavigateToMessages() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = "Direct Messages",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Surface(
                            shape = CircleShape,
                            color = InstagramHeartRed,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 4.dp, y = (-4).dp)
                        ) {
                            Text(
                                text = "3",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = InstagramBorder, thickness = 0.5.dp)

            // Feed Content with Pull Offset
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, animatedPullOffset.roundToInt()) },
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Stories Tray
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp)
                    ) {
                        // "Your Story"
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.testTag("your_story_btn")
                            ) {
                                Box(contentAlignment = Alignment.BottomEnd) {
                                    UserAvatar(
                                        url = user.avatarUrl,
                                        name = user.name,
                                        size = 64,
                                        hasStoryRing = false
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(InstagramBlue)
                                            .border(2.dp, InstagramBlack, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add story",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Your story",
                                    color = TextGray,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }

                        // Creator Stories with Instagram Gradient Rings
                        items(stories, key = { it.id }) { story ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { activeStory = story }
                                    .testTag("story_${story.id}")
                            ) {
                                UserAvatar(
                                    url = story.creatorAvatarUrl,
                                    name = story.creatorName,
                                    size = 64,
                                    hasStoryRing = true,
                                    isStoryViewed = story.isViewed
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = story.creatorName.split(" ").firstOrNull() ?: "",
                                    color = TextWhite,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = InstagramBorder, thickness = 0.5.dp)
                }

                // Instagram Feed Posts
                items(allReels, key = { it.id }) { reel ->
                    InstagramPostCard(
                        reel = reel,
                        onLike = { repository.toggleLike(reel.id) },
                        onComment = { activeCommentReel = reel },
                        onSave = { repository.toggleSave(reel.id) },
                        onShare = { activeShareReel = reel },
                        onCreatorClick = { onNavigateToCreator(reel.creatorId) },
                        onTryChallenge = { activeChallenge = reel.challenge },
                        onOpenVideo = onOpenReelsTab
                    )
                    HorizontalDivider(color = InstagramBorder, thickness = 0.5.dp)
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }

        // Pull-to-refresh spinner overlay in Home feed
        if (animatedPullOffset > 4f || isRefreshing) {
            val pullFraction = (animatedPullOffset / pullThresholdPx).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = (54.dp + (animatedPullOffset * 0.2f).dp).coerceAtMost(100.dp)),
                contentAlignment = Alignment.Center
            ) {
                InstagramPullRefreshIndicator(
                    pullFraction = pullFraction,
                    isRefreshing = isRefreshing
                )
            }
        }

        // Active Challenge Modal
        if (activeChallenge != null) {
            ChallengeDialog(
                challenge = activeChallenge!!,
                onDismiss = { activeChallenge = null },
                onCorrectAnswer = { xp, skill ->
                    repository.recordChallengeSolved(xp, skill)
                }
            )
        }

        // Active Comments Bottom Sheet
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

        // Active Share Bottom Sheet
        if (activeShareReel != null) {
            ShareBottomSheet(
                reelTitle = activeShareReel!!.title,
                onDismiss = { activeShareReel = null },
                onShareToStudyGroup = {
                    repository.sendMessage("chat_1", "Shared Reel: ${activeShareReel!!.title}")
                }
            )
        }

        // Active Story Viewer Modal
        if (activeStory != null) {
            StoryViewerDialog(
                story = activeStory!!,
                onDismiss = { activeStory = null },
                onSolveChallenge = { xp: Int, skill: String ->
                    repository.recordChallengeSolved(xp, skill)
                }
            )
        }
    }
}

@Composable
fun InstagramPostCard(
    reel: Reel,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onSave: () -> Unit,
    onShare: () -> Unit,
    onCreatorClick: () -> Unit,
    onTryChallenge: () -> Unit,
    onOpenVideo: () -> Unit
) {
    var showDoubleTapHeart by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("post_${reel.id}")
    ) {
        // Post Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.clickable { onCreatorClick() }
            ) {
                UserAvatar(
                    url = reel.creatorAvatarUrl,
                    name = reel.creatorName,
                    size = 34,
                    hasStoryRing = true
                )
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = reel.creatorUsername,
                            color = TextWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = InstagramBlue,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Text(
                        text = reel.skillCategory,
                        color = TextGray,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = TextWhite,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Media Surface (4:5 Aspect Ratio / High-Res Media Backdrop)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .background(
                    Brush.verticalGradient(reel.videoPreviewGradient.map { Color(it) })
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (!reel.isLiked) onLike()
                            showDoubleTapHeart = true
                            coroutineScope.launch {
                                delay(900)
                                showDoubleTapHeart = false
                            }
                        },
                        onTap = { onOpenVideo() }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Post Video Thumbnail Graphics
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = reel.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            // Big Heart Animation on Double Tap
            androidx.compose.animation.AnimatedVisibility(
                visible = showDoubleTapHeart,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(90.dp)
                )
            }

            // Educational Quick Quiz Banner Tag
            if (reel.challenge != null) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .clickable { onTryChallenge() },
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.65f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SkillGold.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("⚡", fontSize = 12.sp)
                        Text(
                            text = "Solve Quiz (+10 XP)",
                            color = SkillGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Action Toolbar (Like, Comment, Share ......... Save)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Heart Like
                Icon(
                    imageVector = if (reel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (reel.isLiked) InstagramHeartRed else TextWhite,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onLike() }
                        .testTag("post_like_${reel.id}")
                )

                // Comment Bubble
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = TextWhite,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onComment() }
                        .testTag("post_comment_${reel.id}")
                )

                // Paper Airplane Share
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = "Share",
                    tint = TextWhite,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onShare() }
                        .testTag("post_share_${reel.id}")
                )
            }

            // Bookmark Save
            Icon(
                imageVector = if (reel.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = "Save",
                tint = TextWhite,
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onSave() }
                    .testTag("post_save_${reel.id}")
            )
        }

        // Likes Count
        Column(modifier = Modifier.padding(horizontal = 14.dp)) {
            Text(
                text = "${reel.likesCount} likes",
                color = TextWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Caption with bold username
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${reel.creatorUsername}  ",
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = reel.description,
                    color = TextWhite,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Hashtags
            Text(
                text = reel.hashtags.joinToString(" "),
                color = InstagramBlue,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // View comments
            if (reel.commentsCount > 0) {
                Text(
                    text = "View all ${reel.commentsCount} comments",
                    color = TextDarkGray,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onComment() }
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Timestamp
            Text(
                text = "2 HOURS AGO",
                color = TextDarkGray,
                fontSize = 10.sp,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
