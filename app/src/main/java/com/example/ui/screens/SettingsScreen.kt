package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SkillReelRepository
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    repository: SkillReelRepository,
    onBack: () -> Unit,
    onRestartOnboarding: () -> Unit
) {
    var pushNotifsEnabled by remember { mutableStateOf(true) }
    var privateAccount by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                }
                Text(
                    text = "Settings and privacy",
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Accounts Center Card
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = InstagramCard,
                border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Accounts Center", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "Manage your connected experiences and account settings across SkillReel.",
                        color = TextDarkGray,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Section Header
        item {
            Text(
                text = "How you use SkillReel",
                color = TextDarkGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Settings items
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = InstagramCard,
                border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    InstagramSettingsRow(Icons.Default.Notifications, "Notifications")
                    InstagramSettingsRow(Icons.Default.Lock, "Account privacy", if (privateAccount) "Private" else "Public")
                    InstagramSettingsRow(Icons.Default.School, "Learning goals & interests") {
                        onRestartOnboarding()
                    }
                    InstagramSettingsRow(Icons.Default.Shield, "Security and login")
                    InstagramSettingsRow(Icons.Default.Help, "Help")
                    InstagramSettingsRow(Icons.Default.Info, "About SkillReel")
                }
            }
        }

        // Log out button
        item {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = InstagramCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRestartOnboarding() }
                    .padding(vertical = 8.dp)
            ) {
                Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {
                    Text("Log out", color = InstagramHeartRed, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun InstagramSettingsRow(
    icon: ImageVector,
    title: String,
    value: String? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, contentDescription = null, tint = TextWhite, modifier = Modifier.size(22.dp))
            Text(text = title, color = TextWhite, fontSize = 14.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            if (value != null) {
                Text(text = value, color = TextDarkGray, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextDarkGray, modifier = Modifier.size(18.dp))
        }
    }
}
