package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.ui.theme.*

@Composable
fun VideoCallScreen(
    roomName: String,
    onEndCall: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isVideoOff by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("video_call_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = roomName,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Instagram Video Call • Connected",
                        color = TextGray,
                        fontSize = 12.sp
                    )
                }
            }

            // Remote Participant
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(InstagramCard)
                    .border(1.dp, InstagramBorder, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.5.dp, InstagramStoryBrush, CircleShape)
                            .padding(3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = InstagramBlack,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("SC", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Dr. Sarah Chen", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                // Local PIP Preview
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp)
                        .size(width = 100.dp, height = 140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isVideoOff) Color(0xFF18181B) else InstagramCard)
                        .border(1.dp, InstagramBorder, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isVideoOff) {
                        Text("Camera Off", color = TextGray, fontSize = 10.sp)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("👤", fontSize = 24.sp)
                            Text("You", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Instagram Call Control Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InstagramCallAction(
                    icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    isActive = isMuted,
                    onClick = { isMuted = !isMuted }
                )
                InstagramCallAction(
                    icon = if (isVideoOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                    isActive = isVideoOff,
                    onClick = { isVideoOff = !isVideoOff }
                )
                InstagramCallAction(
                    icon = Icons.Default.Cameraswitch,
                    isActive = false,
                    onClick = { }
                )

                // End call
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(InstagramHeartRed)
                        .clickable { onEndCall() }
                        .testTag("end_call_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InstagramCallAction(
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(InstagramCard)
            .border(0.5.dp, InstagramBorder, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) InstagramHeartRed else Color.White,
            modifier = Modifier.size(22.dp)
        )
    }
}
