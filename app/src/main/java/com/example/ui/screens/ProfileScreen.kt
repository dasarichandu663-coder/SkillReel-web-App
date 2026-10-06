package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.*
import com.example.ui.components.*
import com.example.ui.theme.*

enum class InstagramGridTab {
    POSTS, REELS, SAVED
}

@Composable
fun ProfileScreen(
    repository: SkillReelRepository,
    onOpenSettings: () -> Unit,
    onSelectReel: (Reel) -> Unit
) {
    val user by repository.currentUser.collectAsState()
    val allReels by repository.reels.collectAsState()
    val skills by repository.skills.collectAsState()

    var activeTab by remember { mutableStateOf(InstagramGridTab.POSTS) }

    val userReels = remember(allReels) {
        allReels.filter { it.creatorId == user.id || it.isSaved }
    }
    val savedReels = remember(allReels) {
        allReels.filter { it.isSaved }
    }

    val highlights = listOf(
        "Python 🐍" to 0xFF2E1065,
        "AI / ML 🤖" to 0xFF0F172A,
        "Roadmap 🗺️" to 0xFF064E3B,
        "Projects 💻" to 0xFF831843
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .statusBarsPadding()
            .testTag("profile_screen")
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = user.username,
                        color = TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Accounts",
                        tint = TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.AddBox,
                        contentDescription = "Create",
                        tint = TextWhite,
                        modifier = Modifier.size(24.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu / Settings",
                        tint = TextWhite,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onOpenSettings() }
                            .testTag("profile_menu_btn")
                    )
                }
            }
        }

        // Profile Avatar & Stats Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Avatar with story ring
                UserAvatar(
                    url = user.avatarUrl,
                    name = user.name,
                    size = 76,
                    hasStoryRing = true
                )

                // Stats: Posts, Followers, Following
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InstagramStatColumn(count = "${user.reelsCompleted}", label = "Posts")
                    InstagramStatColumn(count = "${user.followersCount}", label = "Followers")
                    InstagramStatColumn(count = "${user.followingCount}", label = "Following")
                }
            }
        }

        // Bio & Verified Skill Badges
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = user.name,
                    color = TextWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Education • ${user.careerGoal}",
                    color = TextDarkGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = user.bio,
                    color = TextWhite,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Link
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Link, contentDescription = null, tint = InstagramBlue, modifier = Modifier.size(14.dp))
                    Text(
                        text = "skillreel.com/${user.username}",
                        color = InstagramBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Verified Skill Badges (Instagram Professional Category Chips)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    skills.take(3).forEach { skill ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = InstagramCard,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder)
                        ) {
                            Text(
                                text = "${skill.name} • ${skill.progressPercent}%",
                                color = TextWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Action Buttons: "Edit profile", "Share profile"
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = InstagramCard,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .clickable { onOpenSettings() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Edit profile", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = InstagramCard,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .clickable { }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Share profile", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Story Highlights Carousel
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(highlights) { (title, color) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color(color))
                                .border(1.dp, InstagramBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(title.split(" ").lastOrNull() ?: "", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = title.split(" ").firstOrNull() ?: "", color = TextWhite, fontSize = 11.sp)
                    }
                }

                // Add Highlight
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(InstagramCard)
                                .border(1.dp, InstagramBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "New Highlight", tint = TextWhite)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "New", color = TextWhite, fontSize = 11.sp)
                    }
                }
            }

            HorizontalDivider(color = InstagramBorder, thickness = 0.5.dp)
        }

        // Grid Tabs (Posts, Reels, Saved)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InstagramTabButton(
                    icon = Icons.Default.GridOn,
                    isSelected = activeTab == InstagramGridTab.POSTS,
                    onClick = { activeTab = InstagramGridTab.POSTS }
                )
                InstagramTabButton(
                    icon = Icons.Outlined.VideoLibrary,
                    isSelected = activeTab == InstagramGridTab.REELS,
                    onClick = { activeTab = InstagramGridTab.REELS }
                )
                InstagramTabButton(
                    icon = Icons.Outlined.BookmarkBorder,
                    isSelected = activeTab == InstagramGridTab.SAVED,
                    onClick = { activeTab = InstagramGridTab.SAVED }
                )
            }
        }

        // 3x3 Media Grid
        item {
            val listToDisplay = when (activeTab) {
                InstagramGridTab.POSTS -> userReels
                InstagramGridTab.REELS -> userReels
                InstagramGridTab.SAVED -> savedReels
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                val chunked = listToDisplay.chunked(3)
                chunked.forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        rowItems.forEach { reel ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(
                                        Brush.linearGradient(reel.videoPreviewGradient.map { Color(it) })
                                    )
                                    .clickable { onSelectReel(reel) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(16.dp)
                                )
                                Text(
                                    text = reel.title,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(4.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                        // Fill remainder if row not full
                        repeat(3 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun InstagramStatColumn(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = TextWhite, fontSize = 13.sp)
    }
}

@Composable
private fun InstagramTabButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(44.dp)
            .clickable { onClick() }
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color.White else TextDarkGray,
            modifier = Modifier.size(24.dp)
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(1.5.dp)
                    .background(Color.White)
            )
        }
    }
}
