package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SkillReelRepository {

    private val _currentUser = MutableStateFlow(DemoData.currentUser)
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _reels = MutableStateFlow(DemoData.sampleReels)
    val reels: StateFlow<List<Reel>> = _reels.asStateFlow()

    private val _posts = MutableStateFlow(DemoData.samplePosts)
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _stories = MutableStateFlow(DemoData.sampleStories)
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    private val _skills = MutableStateFlow(DemoData.skillsList)
    val skills: StateFlow<List<SkillMastery>> = _skills.asStateFlow()

    private val _learningPaths = MutableStateFlow(DemoData.sampleLearningPaths)
    val learningPaths: StateFlow<List<LearningPath>> = _learningPaths.asStateFlow()

    private val _conversations = MutableStateFlow(DemoData.sampleChats)
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

    private val _messages = MutableStateFlow<Map<String, List<ChatMessage>>>(DemoData.sampleMessages)
    val messages: StateFlow<Map<String, List<ChatMessage>>> = _messages.asStateFlow()

    private val _notifications = MutableStateFlow(DemoData.sampleNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _achievements = MutableStateFlow(DemoData.sampleAchievements)
    val achievements: StateFlow<List<AchievementItem>> = _achievements.asStateFlow()

    private val _savedCollections = MutableStateFlow(
        listOf("All Saved", "Python", "Artificial Intelligence", "Career Tips", "DSA Patterns")
    )
    val savedCollections: StateFlow<List<String>> = _savedCollections.asStateFlow()

    private val _selectedCollection = MutableStateFlow("All Saved")
    val selectedCollection: StateFlow<String> = _selectedCollection.asStateFlow()

    private val _moderationReports = MutableStateFlow(DemoData.sampleReports)
    val moderationReports: StateFlow<List<ModerationReport>> = _moderationReports.asStateFlow()

    private val _isOnboardingCompleted = MutableStateFlow(true)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

    // Comments map per reel
    private val _commentsMap = MutableStateFlow<Map<String, List<Comment>>>(
        mapOf(
            "reel_1" to listOf(
                Comment("c1", "reel_1", "u2", "David K.", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150", "The pointer reference diagram makes it so clear!", "2h ago", 14),
                Comment("c2", "reel_1", "u3", "Maya S.", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150", "Finally stopped copying arrays by value by mistake haha", "1h ago", 8)
            ),
            "reel_2" to listOf(
                Comment("c3", "reel_2", "u4", "Leo M.", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150", "Statistical pattern recognition > brute force rules. Clean!", "3h ago", 22)
            )
        )
    )
    val commentsMap: StateFlow<Map<String, List<Comment>>> = _commentsMap.asStateFlow()

    fun completeOnboarding(
        interests: List<String>,
        goal: String,
        level: DifficultyLevel,
        minutes: Int
    ) {
        _currentUser.update {
            it.copy(
                selectedInterests = interests,
                careerGoal = goal,
                currentLevel = level,
                dailyGoalMinutes = minutes
            )
        }
        _isOnboardingCompleted.value = true
    }

    fun restartOnboarding() {
        _isOnboardingCompleted.value = false
    }

    suspend fun refreshFeed() {
        kotlinx.coroutines.delay(1000)
        val freshReel = Reel(
            id = "reel_fresh_${System.currentTimeMillis()}",
            title = "Python Generators in 30 Seconds",
            description = "The 'yield' keyword pauses execution and streams values on-demand without memory overhead.",
            videoPreviewGradient = listOf(0xFF064E3B, 0xFF0284C7),
            durationSeconds = 30,
            creatorId = "c2",
            creatorName = "Marcus Dev",
            creatorUsername = "marcus_codes",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150",
            likesCount = 420,
            commentsCount = 36,
            savesCount = 210,
            sharesCount = 52,
            skillCategory = "Python Programming",
            difficulty = DifficultyLevel.Intermediate,
            hashtags = listOf("#Python", "#Generators", "#CodeTips"),
            challenge = Challenge(
                id = "ch_fresh_${System.currentTimeMillis()}",
                question = "What keyword transforms a regular function into a lazy generator in Python?",
                options = listOf("yield", "return", "generate", "pause"),
                correctIndex = 0,
                explanation = "'yield' pauses execution and yields values lazily on-demand.",
                xpReward = 10,
                skillName = "Python Programming",
                difficulty = DifficultyLevel.Intermediate
            ),
            viewsCount = 2400
        )
        _reels.update { list ->
            if (list.none { it.title == freshReel.title }) listOf(freshReel) + list else list.shuffled()
        }
    }

    fun toggleLike(reelId: String) {
        _reels.update { list ->
            list.map { reel ->
                if (reel.id == reelId) {
                    val newLiked = !reel.isLiked
                    val newCount = if (newLiked) reel.likesCount + 1 else (reel.likesCount - 1).coerceAtLeast(0)
                    reel.copy(isLiked = newLiked, likesCount = newCount)
                } else reel
            }
        }
    }

    fun toggleSave(reelId: String) {
        _reels.update { list ->
            list.map { reel ->
                if (reel.id == reelId) {
                    val newSaved = !reel.isSaved
                    val newCount = if (newSaved) reel.savesCount + 1 else (reel.savesCount - 1).coerceAtLeast(0)
                    reel.copy(isSaved = newSaved, savesCount = newCount)
                } else reel
            }
        }
    }

    fun toggleFollowCreator(creatorId: String) {
        _reels.update { list ->
            list.map { reel ->
                if (reel.creatorId == creatorId) {
                    reel.copy(isFollowing = !reel.isFollowing)
                } else reel
            }
        }
    }

    fun recordChallengeSolved(xpEarned: Int, skillName: String) {
        _currentUser.update { user ->
            user.copy(
                xp = user.xp + xpEarned,
                challengesCompleted = user.challengesCompleted + 1
            )
        }
        // Update skill mastery XP
        _skills.update { skills ->
            skills.map { skill ->
                if (skill.name.contains(skillName, ignoreCase = true) || skillName.contains(skill.name, ignoreCase = true)) {
                    val newXp = skill.xp + xpEarned
                    val newProgress = (skill.progressPercent + 5).coerceAtMost(100)
                    skill.copy(xp = newXp, progressPercent = newProgress)
                } else skill
            }
        }
        // Add notification
        val notification = NotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = "Challenge Solved! ⚡",
            message = "You earned +$xpEarned XP in $skillName.",
            timeAgo = "Just now",
            type = NotificationType.Challenge,
            xpEarned = xpEarned
        )
        _notifications.update { listOf(notification) + it }
    }

    fun addComment(reelId: String, text: String) {
        if (text.isBlank()) return
        val newComment = Comment(
            id = "comm_${System.currentTimeMillis()}",
            reelId = reelId,
            userId = _currentUser.value.id,
            userName = _currentUser.value.name,
            userAvatarUrl = _currentUser.value.avatarUrl,
            text = text,
            timeAgo = "Just now",
            likesCount = 0
        )
        _commentsMap.update { map ->
            val existing = map[reelId] ?: emptyList()
            map + (reelId to (existing + newComment))
        }
        _reels.update { list ->
            list.map {
                if (it.id == reelId) it.copy(commentsCount = it.commentsCount + 1) else it
            }
        }
    }

    fun publishReel(
        title: String,
        description: String,
        skill: String,
        difficulty: DifficultyLevel,
        hashtags: List<String>,
        challengeQuestion: String?,
        options: List<String>?,
        correctOptionIndex: Int
    ) {
        val user = _currentUser.value
        val challenge = if (!challengeQuestion.isNullOrBlank() && options != null && options.size >= 2) {
            Challenge(
                id = "ch_${System.currentTimeMillis()}",
                question = challengeQuestion,
                options = options,
                correctIndex = correctOptionIndex,
                explanation = "Key takeaway from $title",
                xpReward = 10,
                skillName = skill,
                difficulty = difficulty
            )
        } else null

        val newReel = Reel(
            id = "reel_custom_${System.currentTimeMillis()}",
            title = title,
            description = description,
            videoPreviewGradient = listOf(0xFF4C1D95, 0xFF06B6D4),
            durationSeconds = 35,
            creatorId = user.id,
            creatorName = user.name,
            creatorUsername = user.username,
            creatorAvatarUrl = user.avatarUrl,
            likesCount = 1,
            isLiked = true,
            commentsCount = 0,
            savesCount = 0,
            sharesCount = 0,
            skillCategory = skill,
            difficulty = difficulty,
            hashtags = hashtags,
            challenge = challenge,
            viewsCount = 1,
            isForYou = true,
            isLearning = true
        )

        _reels.update { listOf(newReel) + it }
        _currentUser.update { it.copy(xp = it.xp + 25) } // +25 XP for educational contribution
    }

    fun sendMessage(chatId: String, text: String) {
        if (text.isBlank()) return
        val user = _currentUser.value
        val msg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderId = user.id,
            senderName = user.name,
            text = text,
            timestamp = "Just now",
            isMe = true
        )
        _messages.update { currentMap ->
            val chatList = currentMap[chatId]?.toMutableList() ?: mutableListOf()
            chatList.add(msg)
            currentMap + (chatId to chatList)
        }
        _conversations.update { list ->
            list.map {
                if (it.id == chatId) it.copy(lastMessage = "You: $text", timeAgo = "Just now") else it
            }
        }
    }

    fun enrollLearningPath(pathId: String) {
        _learningPaths.update { list ->
            list.map {
                if (it.id == pathId) it.copy(isEnrolled = true, enrolledCount = it.enrolledCount + 1) else it
            }
        }
    }

    fun selectCollection(collectionName: String) {
        _selectedCollection.value = collectionName
    }

    fun createCollection(name: String) {
        if (name.isNotBlank() && !_savedCollections.value.contains(name)) {
            _savedCollections.update { it + name }
        }
    }

    fun reportContent(type: String, title: String, author: String, reason: String) {
        val rep = ModerationReport(
            id = "rep_${System.currentTimeMillis()}",
            contentType = type,
            contentTitle = title,
            authorUsername = author,
            reporterUsername = _currentUser.value.username,
            reason = reason,
            timeAgo = "Just now"
        )
        _moderationReports.update { listOf(rep) + it }
    }

    fun dismissReport(reportId: String) {
        _moderationReports.update { list -> list.filter { it.id != reportId } }
    }

    fun votePoll(postId: String, optionIndex: Int) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId && post.userVotedIndex == null && post.pollVotes != null) {
                    val updatedVotes = post.pollVotes.toMutableList()
                    if (optionIndex in updatedVotes.indices) {
                        updatedVotes[optionIndex] = updatedVotes[optionIndex] + 1
                    }
                    post.copy(pollVotes = updatedVotes, userVotedIndex = optionIndex)
                } else post
            }
        }
    }
}
