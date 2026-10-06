package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.GeminiService
import com.example.data.SkillReelRepository
import com.example.ui.components.AppDestination
import com.example.ui.components.SkillReelBottomNavBar
import com.example.ui.components.SkillReelSidebar
import com.example.ui.screens.*
import com.example.ui.theme.InstagramBlack
import com.example.ui.theme.SkillReelTheme

class MainActivity : ComponentActivity() {

    private val repository = SkillReelRepository()
    private val geminiService = GeminiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkillReelTheme(darkTheme = true) {
                SkillReelApp(
                    repository = repository,
                    geminiService = geminiService
                )
            }
        }
    }
}

@Composable
fun SkillReelApp(
    repository: SkillReelRepository,
    geminiService: GeminiService
) {
    val isOnboardingDone by repository.isOnboardingCompleted.collectAsState()
    val user by repository.currentUser.collectAsState()

    var currentDestination by remember { mutableStateOf(AppDestination.HOME) }
    var activeCallRoomName by remember { mutableStateOf<String?>(null) }

    // Intercept back button for nested screens
    BackHandler(enabled = activeCallRoomName != null || currentDestination != AppDestination.HOME) {
        when {
            activeCallRoomName != null -> activeCallRoomName = null
            currentDestination != AppDestination.HOME -> currentDestination = AppDestination.HOME
        }
    }

    if (!isOnboardingDone) {
        OnboardingScreen(
            onComplete = { interests, goal, level, minutes ->
                repository.completeOnboarding(interests, goal, level, minutes)
                currentDestination = AppDestination.HOME
            }
        )
        return
    }

    // Call Screen Overlay
    if (activeCallRoomName != null) {
        VideoCallScreen(
            roomName = activeCallRoomName!!,
            onEndCall = { activeCallRoomName = null }
        )
        return
    }

    // Responsive Layout Decision
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
    ) {
        val isWideScreen = maxWidth >= 720.dp

        if (isWideScreen) {
            // Desktop & Tablet Navigation Rail Layout
            Row(modifier = Modifier.fillMaxSize()) {
                SkillReelSidebar(
                    currentDestination = currentDestination,
                    userAvatarUrl = user.avatarUrl,
                    onNavigate = { currentDestination = it }
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    ScreenContent(
                        destination = currentDestination,
                        repository = repository,
                        geminiService = geminiService,
                        onNavigate = { currentDestination = it },
                        onStartCall = { room, _ -> activeCallRoomName = room }
                    )
                }
            }
        } else {
            // Mobile Layout with Instagram-style 5-icon Bottom Bar
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = InstagramBlack,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    // Hide bottom bar inside full-screen reels or messages if desired, or keep sticky like Instagram
                    if (currentDestination != AppDestination.MESSAGES) {
                        SkillReelBottomNavBar(
                            currentDestination = currentDestination,
                            userAvatarUrl = user.avatarUrl,
                            onNavigate = { currentDestination = it }
                        )
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    ScreenContent(
                        destination = currentDestination,
                        repository = repository,
                        geminiService = geminiService,
                        onNavigate = { currentDestination = it },
                        onStartCall = { room, _ -> activeCallRoomName = room }
                    )
                }
            }
        }
    }
}

@Composable
private fun ScreenContent(
    destination: AppDestination,
    repository: SkillReelRepository,
    geminiService: GeminiService,
    onNavigate: (AppDestination) -> Unit,
    onStartCall: (String, String) -> Unit
) {
    when (destination) {
        AppDestination.HOME -> {
            HomeScreen(
                repository = repository,
                onNavigateToMessages = { onNavigate(AppDestination.MESSAGES) },
                onNavigateToNotifications = { onNavigate(AppDestination.NOTIFICATIONS) },
                onNavigateToCreator = { onNavigate(AppDestination.PROFILE) },
                onOpenReelsTab = { onNavigate(AppDestination.REELS) }
            )
        }
        AppDestination.SEARCH -> {
            DiscoverScreen(
                repository = repository,
                onSelectReel = { onNavigate(AppDestination.REELS) }
            )
        }
        AppDestination.CREATE -> {
            CreateScreen(
                repository = repository,
                geminiService = geminiService,
                onPublished = { onNavigate(AppDestination.HOME) },
                onCancel = { onNavigate(AppDestination.HOME) }
            )
        }
        AppDestination.REELS -> {
            ReelsScreen(
                repository = repository,
                onCreatorClick = { onNavigate(AppDestination.PROFILE) }
            )
        }
        AppDestination.PROFILE -> {
            ProfileScreen(
                repository = repository,
                onOpenSettings = { onNavigate(AppDestination.SETTINGS) },
                onSelectReel = { onNavigate(AppDestination.REELS) }
            )
        }
        AppDestination.MESSAGES -> {
            MessagesScreen(
                repository = repository,
                onStartCall = onStartCall,
                onBack = { onNavigate(AppDestination.HOME) }
            )
        }
        AppDestination.NOTIFICATIONS -> {
            NotificationsScreen(
                repository = repository,
                onBack = { onNavigate(AppDestination.HOME) }
            )
        }
        AppDestination.SETTINGS -> {
            SettingsScreen(
                repository = repository,
                onBack = { onNavigate(AppDestination.PROFILE) },
                onRestartOnboarding = { repository.restartOnboarding() }
            )
        }
        AppDestination.STUDIO -> {
            CreatorStudioScreen(
                repository = repository,
                onBack = { onNavigate(AppDestination.PROFILE) }
            )
        }
    }
}
