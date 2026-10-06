package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomSheet(
    reelTitle: String,
    onDismiss: () -> Unit,
    onShareToStudyGroup: () -> Unit
) {
    val quickSendFriends = listOf(
        "Marcus" to "marcus_codes",
        "Dr. Sarah" to "drsarah_ai",
        "Python Group" to "group",
        "Elena" to "elena_ux"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = InstagramCard,
        dragHandle = { BottomSheetDefaults.DragHandle(color = InstagramBorderLight) },
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = "Share",
                color = TextWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quick send friends row (Instagram style)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(quickSendFriends) { (name, handle) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            onShareToStudyGroup()
                            onDismiss()
                        }
                    ) {
                        UserAvatar(url = "", name = name, size = 52)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = name, color = TextWhite, fontSize = 11.sp, maxLines = 1)
                        Text(text = "Send", color = InstagramBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = InstagramBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InstagramShareAction(Icons.Default.Link, "Copy link", onClick = onDismiss)
                InstagramShareAction(Icons.Default.Share, "Share to...", onClick = onDismiss)
                InstagramShareAction(Icons.Default.AddCircleOutline, "Add to story", onClick = onDismiss)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun InstagramShareAction(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(InstagramBlack)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = TextWhite, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, color = TextWhite, fontSize = 11.sp)
    }
}
