package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Challenge
import com.example.data.Reel
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Reusable Instagram-style Vertical Scrolling Video Feed Component
 * Features full social engagement interaction overlay: Like, Comment, Save, Share, and Quiz challenges,
 * alongside an authentic pull-to-refresh mechanism with Instagram gradient spinning indicator.
 */
@Composable
fun InstagramVerticalReelFeed(
    reels: List<Reel>,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    onLikeClick: (Reel) -> Unit,
    onCommentClick: (Reel) -> Unit,
    onSaveClick: (Reel) -> Unit,
    onShareClick: (Reel) -> Unit,
    onFollowClick: (Reel) -> Unit,
    onCreatorClick: (String) -> Unit,
    onChallengeAnswered: (xp: Int, skill: String) -> Unit,
    headerOverlay: (@Composable BoxScope.() -> Unit)? = null
) {
    if (reels.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(InstagramBlack),
            contentAlignment = Alignment.Center
        ) {
            Text("No Reels available", color = TextDarkGray, fontSize = 14.sp)
        }
        return
    }

    val pagerState = rememberPagerState(
        initialPage = initialPage.coerceIn(0, reels.size - 1),
        pageCount = { reels.size }
    )

    var activeChallenge by remember { mutableStateOf<Challenge?>(null) }
    var showRefreshSuccessBadge by remember { mutableStateOf(false) }

    // Pull to refresh physics and dampening
    val density = LocalDensity.current
    val pullThresholdPx = with(density) { 72.dp.toPx() }
    var pullDragOffset by remember { mutableFloatStateOf(0f) }

    val animatedPullOffset by animateFloatAsState(
        targetValue = if (isRefreshing) with(density) { 58.dp.toPx() } else pullDragOffset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pull_offset_anim"
    )

    // Notify user with a sleek Instagram toast badge when fresh reels are loaded
    var previousRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(isRefreshing) {
        if (previousRefreshing && !isRefreshing) {
            showRefreshSuccessBadge = true
            delay(1800)
            showRefreshSuccessBadge = false
        }
        previousRefreshing = isRefreshing
    }

    val nestedScrollConnection = remember(pagerState, isRefreshing, onRefresh) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // When already dragged down and scrolling upward, collapse pull offset first
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
                // When at page 0 (top of vertical reel feed) and dragging downward
                if (pagerState.currentPage == 0 && available.y > 0 && !isRefreshing && onRefresh != null) {
                    val dampedDelta = available.y * 0.45f
                    pullDragOffset = (pullDragOffset + dampedDelta).coerceAtMost(pullThresholdPx * 1.8f)
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (pullDragOffset >= pullThresholdPx && !isRefreshing) {
                    pullDragOffset = 0f
                    onRefresh?.invoke()
                } else {
                    pullDragOffset = 0f
                }
                return Velocity.Zero
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .nestedScroll(nestedScrollConnection)
            .testTag("instagram_vertical_reel_feed")
    ) {
        // Vertical Pager with native snapping & animated pull-down resistance
        VerticalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, animatedPullOffset.roundToInt()) },
            key = { reels[it].id }
        ) { page ->
            val currentReel = reels[page]
            val isCurrentPage = pagerState.currentPage == page

            InstagramReelPageItem(
                reel = currentReel,
                isActivePage = isCurrentPage,
                onLike = { onLikeClick(currentReel) },
                onComment = { onCommentClick(currentReel) },
                onSave = { onSaveClick(currentReel) },
                onShare = { onShareClick(currentReel) },
                onFollow = { onFollowClick(currentReel) },
                onCreatorClick = { onCreatorClick(currentReel.creatorId) },
                onTryChallenge = { activeChallenge = currentReel.challenge }
            )
        }

        // Instagram Pull-To-Refresh Indicator at Top
        if (animatedPullOffset > 4f || isRefreshing) {
            val pullFraction = (animatedPullOffset / pullThresholdPx).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = (8.dp + (animatedPullOffset * 0.25f).dp).coerceAtMost(65.dp)),
                contentAlignment = Alignment.Center
            ) {
                InstagramPullRefreshIndicator(
                    pullFraction = pullFraction,
                    isRefreshing = isRefreshing
                )
            }
        }

        // Success Confirmation Banner ("Latest Educational Reels Loaded")
        AnimatedVisibility(
            visible = showRefreshSuccessBadge,
            enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -40 }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 56.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.88f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.7f)),
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "Fresh Educational Reels Loaded",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Custom Top Overlay (Header Title / Camera / Refresh)
        if (headerOverlay != null) {
            headerOverlay()
        }

        // Active Interactive Challenge Quiz Modal
        if (activeChallenge != null) {
            ChallengeDialog(
                challenge = activeChallenge!!,
                onDismiss = { activeChallenge = null },
                onCorrectAnswer = { xp, skill ->
                    onChallengeAnswered(xp, skill)
                }
            )
        }
    }
}

@Composable
private fun InstagramReelPageItem(
    reel: Reel,
    isActivePage: Boolean,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onSave: () -> Unit,
    onShare: () -> Unit,
    onFollow: () -> Unit,
    onCreatorClick: () -> Unit,
    onTryChallenge: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var showDoubleTapHeart by remember { mutableStateOf(false) }
    var showAudioIndicator by remember { mutableStateOf(false) }
    var showPlayPauseIndicator by remember { mutableStateOf(false) }
    var showSaveToast by remember { mutableStateOf(false) }
    var saveToastMessage by remember { mutableStateOf("Saved to collection") }

    val coroutineScope = rememberCoroutineScope()

    // Video playback progress animation
    val progressAnim = remember { Animatable(0f) }

    LaunchedEffect(isActivePage, isPlaying) {
        if (isActivePage && isPlaying) {
            progressAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = reel.durationSeconds * 1000,
                    easing = LinearEasing
                )
            )
            progressAnim.snapTo(0f)
        }
    }

    // Spinning vinyl record animation
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl_disc")
    val discRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disc_rot"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                    onTap = {
                        isPlaying = !isPlaying
                        showPlayPauseIndicator = true
                        coroutineScope.launch {
                            delay(700)
                            showPlayPauseIndicator = false
                        }
                    }
                )
            }
            .testTag("reel_page_${reel.id}")
    ) {
        // High-fidelity Video Canvas Backdrop
        val gradientColors = reel.videoPreviewGradient.map { Color(it) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            gradientColors.firstOrNull() ?: InstagramBlack,
                            gradientColors.getOrNull(1) ?: Color(0xFF1E1B4B),
                            InstagramBlack
                        )
                    )
                )
        )

        // Center Learning Video Graphic & Playback Status
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.45f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = "Video Status",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.35f),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.25f)),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = reel.title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 28.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }

        // Tap Play/Pause Indicator Overlay
        androidx.compose.animation.AnimatedVisibility(
            visible = showPlayPauseIndicator,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.65f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        // Double-Tap Big Heart Burst Animation
        androidx.compose.animation.AnimatedVisibility(
            visible = showDoubleTapHeart,
            enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Liked",
                tint = Color.White.copy(alpha = 0.95f),
                modifier = Modifier.size(110.dp)
            )
        }

        // Audio Mute/Unmute Indicator Button (Top-Right)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 54.dp, end = 16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        isMuted = !isMuted
                        showAudioIndicator = true
                        coroutineScope.launch {
                            delay(1200)
                            showAudioIndicator = false
                        }
                    }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "Toggle Audio",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Top Gradient Vignette
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                    )
                )
        )

        // Bottom Gradient Vignette for clear readability
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f), Color.Black)
                    )
                )
        )

        // INSTAGRAM ENGAGEMENT INTERACTION OVERLAY (Right Side Column)
        InstagramReelInteractionColumn(
            reel = reel,
            discRotation = discRotation,
            isPlaying = isPlaying,
            onLike = onLike,
            onComment = onComment,
            onSave = {
                onSave()
                saveToastMessage = if (!reel.isSaved) "Saved to collection" else "Removed from saved"
                showSaveToast = true
                coroutineScope.launch {
                    delay(1500)
                    showSaveToast = false
                }
            },
            onShare = onShare,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 22.dp)
        )

        // Floating "Saved" Toast Overlay
        androidx.compose.animation.AnimatedVisibility(
            visible = showSaveToast,
            enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { 40 }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 75.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (reel.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = saveToastMessage,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Bottom-Left Information Overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.82f)
                .padding(start = 14.dp, bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Creator Row + Follow Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UserAvatar(
                    url = reel.creatorAvatarUrl,
                    name = reel.creatorName,
                    size = 38,
                    hasStoryRing = true
                )

                Text(
                    text = reel.creatorUsername,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onCreatorClick() }
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (reel.isFollowing) Color.White.copy(alpha = 0.15f) else Color.Transparent,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (reel.isFollowing) Color.White.copy(alpha = 0.45f) else Color.White),
                    modifier = Modifier
                        .clickable { onFollow() }
                        .testTag("follow_btn_${reel.id}")
                ) {
                    Text(
                        text = if (reel.isFollowing) "Following" else "Follow",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }

            // Caption
            Text(
                text = reel.description,
                color = Color.White,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            // Hashtags
            Text(
                text = reel.hashtags.joinToString(" "),
                color = InstagramBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            // Audio Track Marquee
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Original audio - ${reel.creatorUsername} • Learning Session",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            // Interactive Learning Quiz Sticker Pill (Instagram Style)
            if (reel.challenge != null) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SkillGold),
                    modifier = Modifier
                        .clickable { onTryChallenge() }
                        .testTag("reel_quiz_sticker_${reel.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("⚡", fontSize = 13.sp)
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

        // Bottom Progress Bar (Instagram Video Track Indicator)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.5.dp)
                .align(Alignment.BottomCenter)
                .background(Color.White.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progressAnim.value)
                    .background(Color.White)
            )
        }
    }
}

/**
 * Instagram-Style Vertical Social Interaction Overlay Column:
 * Contains animated Like, Comment, Share, Save, More Options, and Spinning Disc.
 */
@Composable
fun InstagramReelInteractionColumn(
    reel: Reel,
    discRotation: Float,
    isPlaying: Boolean,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onSave: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Like button spring animation
    var likeAnimateState by remember { mutableStateOf(false) }
    val likeScale by animateFloatAsState(
        targetValue = if (likeAnimateState) 1.35f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "like_scale"
    )

    // Save button spring animation
    var saveAnimateState by remember { mutableStateOf(false) }
    val saveScale by animateFloatAsState(
        targetValue = if (saveAnimateState) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "save_scale"
    )

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.testTag("interaction_overlay_${reel.id}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. LIKE BUTTON
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onLike()
                    likeAnimateState = true
                    coroutineScope.launch {
                        delay(250)
                        likeAnimateState = false
                    }
                }
                .testTag("reel_like_btn_${reel.id}")
        ) {
            Icon(
                imageVector = if (reel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (reel.isLiked) InstagramHeartRed else Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .scale(likeScale)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = formatCount(reel.likesCount),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // 2. COMMENT BUTTON
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { onComment() }
                .testTag("reel_comment_btn_${reel.id}")
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Comment",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = formatCount(reel.commentsCount),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // 3. SHARE (PAPER AIRPLANE) BUTTON
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { onShare() }
                .testTag("reel_share_btn_${reel.id}")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = "Share",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = formatCount(reel.sharesCount),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // 4. SAVE (BOOKMARK) BUTTON
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onSave()
                    saveAnimateState = true
                    coroutineScope.launch {
                        delay(250)
                        saveAnimateState = false
                    }
                }
                .testTag("reel_save_btn_${reel.id}")
        ) {
            Icon(
                imageVector = if (reel.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = "Save",
                tint = Color.White,
                modifier = Modifier
                    .size(26.dp)
                    .scale(saveScale)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (reel.isSaved) "Saved" else "Save",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // 5. MORE OPTIONS (THREE DOTS) BUTTON
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More Options",
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .clickable { onShare() }
        )

        // 6. SPINNING VINYL ALBUM ARTWORK
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .border(2.dp, Color.White, CircleShape)
                .rotate(if (isPlaying) discRotation else 0f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(InstagramBlue)
            )
        }
    }
}

private fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 10_000 -> String.format("%.1fK", count / 1_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}

/**
 * Authentic Instagram-Style Circular Pull-To-Refresh Indicator
 * Features the signature Instagram warm orange/magenta/purple gradient ring that rotates
 * proportionally with pull distance and spins smoothly while refreshing.
 */
@Composable
fun InstagramPullRefreshIndicator(
    pullFraction: Float,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "refresh_spinner")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    val currentRotation = if (isRefreshing) spinAngle else (pullFraction * 360f)
    val instagramGradient = Brush.sweepGradient(
        listOf(
            Color(0xFFF58529),
            Color(0xFFDD2A7B),
            Color(0xFF8134AF),
            Color(0xFF515BD4),
            Color(0xFFF58529)
        )
    )

    Surface(
        shape = CircleShape,
        color = Color(0xFF18181B).copy(alpha = 0.95f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
        shadowElevation = 8.dp,
        modifier = modifier.size(38.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(22.dp)
                    .rotate(currentRotation)
            ) {
                val strokeWidth = 2.5.dp.toPx()
                val sweep = if (isRefreshing) 270f else (pullFraction.coerceIn(0.15f, 1f) * 290f)
                drawArc(
                    brush = instagramGradient,
                    startAngle = 0f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = strokeWidth,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                )
            }
        }
    }
}
