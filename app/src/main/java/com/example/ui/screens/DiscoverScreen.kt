package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
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
import com.example.ui.theme.*

@Composable
fun DiscoverScreen(
    repository: SkillReelRepository,
    onSelectReel: (Reel) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val reels by repository.reels.collectAsState()

    val categories = listOf("All", "Coding", "AI", "Data", "Design", "Career", "DevOps", "Soft Skills")

    val filteredReels = remember(reels, searchQuery, selectedCategory) {
        reels.filter { reel ->
            val matchQuery = searchQuery.isBlank() ||
                    reel.title.contains(searchQuery, ignoreCase = true) ||
                    reel.skillCategory.contains(searchQuery, ignoreCase = true) ||
                    reel.creatorUsername.contains(searchQuery, ignoreCase = true)
            val matchCat = selectedCategory == "All" || reel.skillCategory.contains(selectedCategory, ignoreCase = true)
            matchQuery && matchCat
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
            .statusBarsPadding()
            .testTag("explore_screen")
    ) {
        // Search Bar (Instagram Pill)
        Box(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search skills, topics or creators", color = TextDarkGray, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = TextDarkGray, modifier = Modifier.size(18.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextDarkGray)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("explore_search_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = InstagramCard,
                    unfocusedContainerColor = InstagramCard,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                singleLine = true
            )
        }

        // Category Pills
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) Color.White else InstagramCard,
                    modifier = Modifier.clickable { selectedCategory = cat }
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.Black else TextWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Instagram 3-Column Explore Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(1.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(filteredReels) { reel ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(
                            Brush.linearGradient(reel.videoPreviewGradient.map { Color(it) })
                        )
                        .clickable { onSelectReel(reel) }
                        .testTag("explore_tile_${reel.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    // Play icon in top right (Instagram style for video posts)
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Reel",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(16.dp)
                    )

                    // Text snippet
                    Text(
                        text = reel.title,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    // Challenge Gold Indicator
                    if (reel.challenge != null) {
                        Text(
                            text = "⚡",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
