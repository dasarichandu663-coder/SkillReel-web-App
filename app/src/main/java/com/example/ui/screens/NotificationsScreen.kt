package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.data.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun NotificationsScreen(
    repository: SkillReelRepository,
    onBack: () -> Unit
) {
    val notifications by repository.notifications.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .statusBarsPadding()
            .testTag("notifications_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
            }
            Text(
                text = "Notifications",
                color = TextWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider(color = InstagramBorder, thickness = 0.5.dp)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Activity",
                    color = TextWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(notifications) { notif ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        UserAvatar(url = "", name = notif.title, size = 44, hasStoryRing = true)
                        Column {
                            Text(
                                text = notif.title,
                                color = TextWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${notif.message} • ${notif.timeAgo}",
                                color = TextDarkGray,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (notif.type == NotificationType.Follow) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = InstagramBlue,
                            modifier = Modifier.clickable { }
                        ) {
                            Text(
                                text = "Follow",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    } else if (notif.xpEarned != null) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = InstagramCard,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder)
                        ) {
                            Text(
                                text = "+${notif.xpEarned} XP",
                                color = SkillGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
