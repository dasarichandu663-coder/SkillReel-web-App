package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class AppDestination(val label: String) {
    HOME("Home"),
    SEARCH("Search"),
    CREATE("Create"),
    REELS("Reels"),
    PROFILE("Profile"),
    MESSAGES("Direct"),
    NOTIFICATIONS("Activity"),
    SETTINGS("Settings"),
    STUDIO("Creator Studio")
}

@Composable
fun SkillReelBottomNavBar(
    currentDestination: AppDestination,
    userAvatarUrl: String,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("bottom_nav_bar"),
        color = InstagramBlack,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. HOME
            InstagramNavIcon(
                iconSelected = Icons.Filled.Home,
                iconUnselected = Icons.Outlined.Home,
                isSelected = currentDestination == AppDestination.HOME,
                tag = "nav_home",
                onClick = { onNavigate(AppDestination.HOME) }
            )

            // 2. SEARCH / EXPLORE
            InstagramNavIcon(
                iconSelected = Icons.Filled.Search,
                iconUnselected = Icons.Outlined.Search,
                isSelected = currentDestination == AppDestination.SEARCH,
                tag = "nav_search",
                onClick = { onNavigate(AppDestination.SEARCH) }
            )

            // 3. CREATE (Square Plus)
            InstagramNavIcon(
                iconSelected = Icons.Filled.AddBox,
                iconUnselected = Icons.Outlined.AddBox,
                isSelected = currentDestination == AppDestination.CREATE,
                tag = "nav_create",
                onClick = { onNavigate(AppDestination.CREATE) }
            )

            // 4. REELS (Clapper / Movie / Reels Icon)
            InstagramNavIcon(
                iconSelected = Icons.Filled.VideoLibrary,
                iconUnselected = Icons.Outlined.VideoLibrary,
                isSelected = currentDestination == AppDestination.REELS,
                tag = "nav_reels",
                onClick = { onNavigate(AppDestination.REELS) }
            )

            // 5. PROFILE (User's Circular Avatar)
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .then(
                        if (currentDestination == AppDestination.PROFILE) {
                            Modifier.border(2.dp, Color.White, CircleShape)
                        } else Modifier
                    )
                    .clickable { onNavigate(AppDestination.PROFILE) }
                    .testTag("nav_profile"),
                contentAlignment = Alignment.Center
            ) {
                UserAvatar(
                    url = userAvatarUrl,
                    name = "Me",
                    size = if (currentDestination == AppDestination.PROFILE) 22 else 26
                )
            }
        }
    }
}

@Composable
private fun InstagramNavIcon(
    iconSelected: ImageVector,
    iconUnselected: ImageVector,
    isSelected: Boolean,
    tag: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .testTag(tag)
    ) {
        Icon(
            imageVector = if (isSelected) iconSelected else iconUnselected,
            contentDescription = tag,
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
fun SkillReelSidebar(
    currentDestination: AppDestination,
    userAvatarUrl: String,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(220.dp)
            .fillMaxHeight(),
        color = InstagramBlack,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Instagram-style Script Logo
            Text(
                text = "SkillReel",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
            )

            InstagramSidebarItem(Icons.Default.Home, "Home", currentDestination == AppDestination.HOME) { onNavigate(AppDestination.HOME) }
            InstagramSidebarItem(Icons.Default.Search, "Search", currentDestination == AppDestination.SEARCH) { onNavigate(AppDestination.SEARCH) }
            InstagramSidebarItem(Icons.Default.VideoLibrary, "Reels", currentDestination == AppDestination.REELS) { onNavigate(AppDestination.REELS) }
            InstagramSidebarItem(Icons.Default.Send, "Messages", currentDestination == AppDestination.MESSAGES) { onNavigate(AppDestination.MESSAGES) }
            InstagramSidebarItem(Icons.Default.FavoriteBorder, "Notifications", currentDestination == AppDestination.NOTIFICATIONS) { onNavigate(AppDestination.NOTIFICATIONS) }
            InstagramSidebarItem(Icons.Default.AddBox, "Create", currentDestination == AppDestination.CREATE) { onNavigate(AppDestination.CREATE) }
            InstagramSidebarItem(Icons.Default.Person, "Profile", currentDestination == AppDestination.PROFILE) { onNavigate(AppDestination.PROFILE) }
            InstagramSidebarItem(Icons.Default.Settings, "Settings", currentDestination == AppDestination.SETTINGS) { onNavigate(AppDestination.SETTINGS) }
        }
    }
}

@Composable
private fun InstagramSidebarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
