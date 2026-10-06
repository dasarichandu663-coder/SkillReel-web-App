package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun CreatorStudioScreen(
    repository: SkillReelRepository,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .testTag("creator_studio_screen"),
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
                        text = "Professional Dashboard",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Insights and creator tools",
                        color = TextGray,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // 30-Day Growth Metrics
        item {
            Text(text = "Insights overview", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StudioStatCard(title = "Accounts reached", value = "128.4K", change = "+28%", modifier = Modifier.weight(1f))
                StudioStatCard(title = "Engagement rate", value = "8.2%", change = "+6.4%", modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StudioStatCard(title = "Reel quizzes solved", value = "1,420", change = "+45%", modifier = Modifier.weight(1f))
                StudioStatCard(title = "Followers gained", value = "+320", change = "+18%", modifier = Modifier.weight(1f))
            }
        }

        // Top Performing Reel
        item {
            Text(text = "Top-performing Reel", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(0.5.dp, InstagramBorder, RoundedCornerShape(12.dp)),
                color = InstagramCard
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(shape = RoundedCornerShape(6.dp), color = InstagramBlack) {
                            Text("MOST VIEWED", color = InstagramBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                        }
                        Text("42.1K Views", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Python Variables in 30 Seconds", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "8,420 likes • 3,120 saves • 94% quiz completion rate", color = TextGray, fontSize = 12.sp)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun StudioStatCard(
    title: String,
    value: String,
    change: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(0.5.dp, InstagramBorder, RoundedCornerShape(12.dp)),
        color = InstagramCard
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, color = TextGray, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = change, color = Color(0xFF00C853), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
