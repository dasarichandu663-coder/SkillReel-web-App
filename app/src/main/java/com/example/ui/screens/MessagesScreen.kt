package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CameraAlt
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
fun MessagesScreen(
    repository: SkillReelRepository,
    onStartCall: (String, String) -> Unit,
    onBack: () -> Unit
) {
    val user by repository.currentUser.collectAsState()
    val conversations by repository.conversations.collectAsState()
    val allMessages by repository.messages.collectAsState()
    var activeChatId by remember { mutableStateOf<String?>(null) }
    var inputText by remember { mutableStateOf("") }

    val activeConversation = conversations.find { it.id == activeChatId }

    val notes = listOf(
        "Your note" to "Solving Python challenges ⚡",
        "drsarah_ai" to "Reviewing neural net loss",
        "marcus_codes" to "Streaming code live @ 6pm",
        "elena_ux" to "New Figma tips dropping soon"
    )

    if (activeChatId == null) {
        // Instagram Direct Messages Inbox
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(InstagramBlack)
                .statusBarsPadding()
                .testTag("direct_messages_screen")
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                    }
                    Text(
                        text = user.username,
                        color = TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "New Message",
                        tint = TextWhite,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Search Bar
            Box(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = InstagramCard,
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TextDarkGray, modifier = Modifier.size(18.dp))
                        Text("Search", color = TextDarkGray, fontSize = 14.sp)
                    }
                }
            }

            // Instagram Notes Tray
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(notes) { (name, noteSnippet) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Note Bubble above Avatar
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = InstagramCard,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, InstagramBorder),
                            modifier = Modifier.widthIn(max = 84.dp)
                        ) {
                            Text(
                                text = noteSnippet,
                                color = TextWhite,
                                fontSize = 10.sp,
                                maxLines = 2,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        UserAvatar(url = "", name = name, size = 52)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = name, color = TextGray, fontSize = 11.sp, maxLines = 1)
                    }
                }
            }

            // Messages Section Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Messages", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = "Requests (1)", color = InstagramBlue, fontSize = 13.sp)
            }

            // Conversations List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(conversations) { chat ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { activeChatId = chat.id }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .testTag("dm_row_${chat.id}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            UserAvatar(url = chat.avatarUrl, name = chat.name, size = 54)
                            Column {
                                Text(
                                    text = chat.name,
                                    color = TextWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${chat.lastMessage} • ${chat.timeAgo}",
                                    color = if (chat.unreadCount > 0) TextWhite else TextDarkGray,
                                    fontSize = 12.sp,
                                    fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Camera",
                            tint = TextDarkGray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    } else {
        // Individual Conversation Screen
        val messageList = allMessages[activeChatId] ?: emptyList()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(InstagramBlack)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Chat Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(onClick = { activeChatId = null }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                    }
                    UserAvatar(url = activeConversation?.avatarUrl ?: "", name = activeConversation?.name ?: "Chat", size = 36)
                    Column {
                        Text(text = activeConversation?.name ?: "Chat", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Active now", color = TextDarkGray, fontSize = 11.sp)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    IconButton(onClick = { onStartCall(activeConversation?.name ?: "Video Call", activeChatId!!) }) {
                        Icon(Icons.Default.Videocam, contentDescription = "Call", tint = TextWhite)
                    }
                }
            }

            HorizontalDivider(color = InstagramBorder, thickness = 0.5.dp)

            // Message Stream
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messageList) { msg ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (msg.isMe) InstagramBlue else InstagramCard,
                            modifier = Modifier.widthIn(max = 270.dp)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                                Text(
                                    text = msg.text,
                                    color = TextWhite,
                                    fontSize = 14.sp
                                )
                                if (msg.attachedReelTitle != null) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "▶ ${msg.attachedReelTitle}",
                                        color = TextWhite,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Message...", color = TextDarkGray, fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InstagramBorder,
                        unfocusedBorderColor = InstagramBorder,
                        focusedContainerColor = InstagramCard,
                        unfocusedContainerColor = InstagramCard,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    singleLine = true
                )

                if (inputText.isNotBlank()) {
                    Text(
                        text = "Send",
                        color = InstagramBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clickable {
                                repository.sendMessage(activeChatId!!, inputText)
                                inputText = ""
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}
