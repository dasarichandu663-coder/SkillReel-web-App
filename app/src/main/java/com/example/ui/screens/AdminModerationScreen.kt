package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SkillReelRepository
import com.example.ui.theme.*

@Composable
fun AdminModerationScreen(
    repository: SkillReelRepository,
    onBack: () -> Unit
) {
    val reports by repository.moderationReports.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianVoid)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("admin_moderation_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Column {
                    Text(
                        text = "Admin Moderation Center 🛡️",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Platform trust, safety & verified education",
                        color = TextSecondaryDark,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Moderation Queue Overview
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp)),
                    color = SurfaceDark
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Active Reports", color = TextSecondaryDark, fontSize = 11.sp)
                        Text("${reports.size}", color = ErrorRose, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp)),
                    color = SurfaceDark
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Resolved Today", color = TextSecondaryDark, fontSize = 11.sp)
                        Text("18", color = SuccessEmerald, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
            }
        }

        item {
            Text(text = "Pending Moderation Reports", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        if (reports.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("All moderation reports cleared! ✨", color = SuccessEmerald, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            items(reports) { rep ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                    color = SurfaceDark
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ErrorRose.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = rep.contentType.uppercase(),
                                    color = ErrorRose,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(text = rep.timeAgo, color = TextMutedDark, fontSize = 11.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = rep.contentTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Reported by @${rep.reporterUsername} • Author: @${rep.authorUsername}", color = TextSecondaryDark, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Reason: ${rep.reason}", color = XpGold, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action Buttons: Review, Remove, Warn, Dismiss
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { repository.dismissReport(rep.id) },
                                modifier = Modifier.weight(1f).height(36.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ErrorRose),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("Remove", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { repository.dismissReport(rep.id) },
                                modifier = Modifier.weight(1f).height(36.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("Dismiss", color = Color.White, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
