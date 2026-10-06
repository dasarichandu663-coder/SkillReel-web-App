package com.example.data

enum class DifficultyLevel {
    Beginner, Intermediate, Advanced
}

enum class PostCategory {
    Educational, Career, Discussion, Project, General
}

enum class PostType {
    Text, Image, LearningTip, Question, Poll
}

data class Challenge(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val xpReward: Int = 10,
    val skillName: String = "General",
    val difficulty: DifficultyLevel = DifficultyLevel.Beginner
)

data class User(
    val id: String,
    val name: String,
    val username: String,
    val bio: String,
    val avatarUrl: String,
    val isCreator: Boolean = false,
    val creatorTitle: String = "Student",
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val xp: Int = 120,
    val streakDays: Int = 5,
    val hoursLearned: Float = 6.5f,
    val reelsCompleted: Int = 42,
    val challengesCompleted: Int = 18,
    val careerGoal: String = "AI Engineer",
    val currentLevel: DifficultyLevel = DifficultyLevel.Intermediate,
    val dailyGoalMinutes: Int = 15,
    val selectedInterests: List<String> = listOf("Coding", "Artificial Intelligence", "Data Science")
)

data class SkillMastery(
    val id: String,
    val name: String,
    val category: String,
    val progressPercent: Int, // 0 - 100
    val level: String,
    val xp: Int,
    val nextLessonTitle: String
)

data class Reel(
    val id: String,
    val title: String,
    val description: String,
    val videoPreviewGradient: List<Long>, // Visual gradient representation for smooth video reel rendering
    val durationSeconds: Int,
    val creatorId: String,
    val creatorName: String,
    val creatorUsername: String,
    val creatorAvatarUrl: String,
    val isFollowing: Boolean = false,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val commentsCount: Int,
    val savesCount: Int,
    val isSaved: Boolean = false,
    val sharesCount: Int,
    val skillCategory: String,
    val difficulty: DifficultyLevel,
    val hashtags: List<String>,
    val challenge: Challenge? = null,
    val viewsCount: Int = 1200,
    val isForYou: Boolean = true,
    val isLearning: Boolean = true
)

data class Comment(
    val id: String,
    val reelId: String,
    val userId: String,
    val userName: String,
    val userAvatarUrl: String,
    val text: String,
    val timeAgo: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class Post(
    val id: String,
    val creatorId: String,
    val creatorName: String,
    val creatorUsername: String,
    val creatorAvatarUrl: String,
    val content: String,
    val postType: PostType,
    val category: PostCategory,
    val timeAgo: String,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val commentsCount: Int,
    val isSaved: Boolean = false,
    val pollOptions: List<String>? = null,
    val pollVotes: List<Int>? = null,
    val userVotedIndex: Int? = null
)

data class Story(
    val id: String,
    val creatorId: String,
    val creatorName: String,
    val creatorAvatarUrl: String,
    val title: String,
    val snippet: String,
    val bgGradient: List<Long>,
    val hasChallenge: Boolean = false,
    val challenge: Challenge? = null,
    val isViewed: Boolean = false
)

data class LearningPathStage(
    val id: String,
    val stageNumber: Int,
    val title: String,
    val description: String,
    val reelsCount: Int,
    val quizzesCount: Int,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

data class LearningPath(
    val id: String,
    val title: String,
    val description: String,
    val targetRole: String,
    val estimatedHours: Int,
    val enrolledCount: Int,
    val progressPercent: Int, // 0 - 100
    val isEnrolled: Boolean,
    val category: String,
    val stages: List<LearningPathStage>
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isMe: Boolean,
    val attachedReelTitle: String? = null,
    val attachedChallengeQuestion: String? = null
)

data class ChatConversation(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val isGroup: Boolean = false,
    val lastMessage: String,
    val timeAgo: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true,
    val memberCount: Int = 2
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val xpEarned: Int? = null
)

enum class NotificationType {
    Achievement, Challenge, Follow, Like, Comment, Reminder, PathProgress
}

data class AchievementItem(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val xpBonus: Int,
    val isUnlocked: Boolean,
    val currentProgress: Int,
    val targetProgress: Int
)

data class ModerationReport(
    val id: String,
    val contentType: String,
    val contentTitle: String,
    val authorUsername: String,
    val reporterUsername: String,
    val reason: String,
    val timeAgo: String,
    val status: String = "Pending"
)
