package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    private val apiKey: String
        get() = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

    suspend fun learningCoach(
        userMessage: String,
        userGoal: String,
        currentSkills: String
    ): String = withContext(Dispatchers.IO) {
        val prompt = """
            You are SkillReel Coach, an inspiring, sharp, and practical AI learning advisor for social-first skills growth.
            User's Career Goal: $userGoal
            Current Skills & Level: $currentSkills
            User asks: "$userMessage"
            
            Give a punchy, structured answer (around 3-4 bullet points or step roadmap, clear next steps, max 140 words) that motivates them to learn through short reels and challenges.
        """.trimIndent()

        val response = callGeminiApi(prompt)
        if (response.isNotBlank()) {
            response
        } else {
            getFallbackCoachResponse(userMessage, userGoal)
        }
    }

    suspend fun generateCaption(topic: String): String = withContext(Dispatchers.IO) {
        val prompt = "Create a viral, high-engagement 2-sentence educational reel caption for a video about: $topic. Include a hook."
        val result = callGeminiApi(prompt)
        if (result.isNotBlank()) result else "🚀 Stop making this mistake with $topic! Master the core concept in under 45 seconds with this visual breakdown."
    }

    suspend fun generateHashtags(topic: String): List<String> = withContext(Dispatchers.IO) {
        val prompt = "Return 4 concise hashtags separated by spaces for learning $topic."
        val result = callGeminiApi(prompt)
        if (result.isNotBlank()) {
            result.split(" ").filter { it.startsWith("#") }.take(4)
        } else {
            listOf("#${topic.replace(" ", "")}", "#LearnOnSkillReel", "#TechSkills", "#FastLearning")
        }
    }

    suspend fun detectSkill(topic: String): Pair<String, DifficultyLevel> = withContext(Dispatchers.IO) {
        val lower = topic.lowercase()
        when {
            lower.contains("python") || lower.contains("code") || lower.contains("loop") -> "Python Programming" to DifficultyLevel.Beginner
            lower.contains("ai") || lower.contains("neural") || lower.contains("gpt") || lower.contains("model") -> "Artificial Intelligence" to DifficultyLevel.Intermediate
            lower.contains("sql") || lower.contains("data") || lower.contains("database") -> "SQL & Databases" to DifficultyLevel.Beginner
            lower.contains("figma") || lower.contains("design") || lower.contains("ux") -> "UI/UX & Product Design" to DifficultyLevel.Intermediate
            lower.contains("resume") || lower.contains("interview") || lower.contains("career") -> "Career & Interviews" to DifficultyLevel.Beginner
            else -> "Technology & Development" to DifficultyLevel.Beginner
        }
    }

    suspend fun generateQuiz(topic: String): Challenge = withContext(Dispatchers.IO) {
        val (skill, diff) = detectSkill(topic)
        Challenge(
            id = "ai_ch_${System.currentTimeMillis()}",
            question = "Which fundamental concept is most critical when implementing $topic?",
            options = listOf(
                "Ensuring clear state and bounds checking",
                "Hardcoding all edge cases manually",
                "Skipping input validation for speed",
                "Ignoring memory overhead"
            ),
            correctIndex = 0,
            explanation = "Clear state separation and bounds checking are essential best practices for $topic.",
            xpReward = 15,
            skillName = skill,
            difficulty = diff
        )
    }

    private fun callGeminiApi(prompt: String): String {
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") return ""
        return try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val jsonBody = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return ""
                val resString = response.body?.string() ?: return ""
                val jsonObj = JSONObject(resString)
                val candidates = jsonObj.optJSONArray("candidates") ?: return ""
                val firstCandidate = candidates.optJSONObject(0) ?: return ""
                val content = firstCandidate.optJSONObject("content") ?: return ""
                val parts = content.optJSONArray("parts") ?: return ""
                parts.optJSONObject(0)?.optString("text", "") ?: ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun getFallbackCoachResponse(message: String, goal: String): String {
        val query = message.lowercase()
        return when {
            query.contains("next") || query.contains("learn next") ->
                "🎯 Based on your target goal ($goal) and 80% Python completion:\n\n1. Deepen **NumPy & Pandas matrix operations**\n2. Solve 3 interactive challenges on **Vectorization**\n3. Build your first **Linear Regression model**\n\n⚡ Tap 'Start Learning' on the AI Engineer path to jump right in!"

            query.contains("roadmap") || query.contains("plan") || query.contains("how to become") ->
                "🚀 Here is your customized 4-Phase Roadmap for $goal:\n\n• Phase 1: Python Data Structures & Algorithmic complexity\n• Phase 2: Mathematics (Linear Algebra + Probability)\n• Phase 3: Classical ML & Scikit-learn pipelines\n• Phase 4: Neural Networks, Transformers & LLM fine-tuning\n\nConsistency beats cramming: commit to 2 Reels + 1 Challenge daily!"

            query.contains("explain") || query.contains("simply") ->
                "💡 Simple Mental Model:\nImagine algorithms as cooking recipes. Normal code requires you to list every single step. In Machine Learning, you provide pictures of the finished dish and ingredients, and the oven learns to calibrate its own temperature knobs until it tastes perfect!"

            else ->
                "✨ Great question! To accelerate your progress toward becoming a top-tier $goal:\n\n1. Complete today's daily 30-second challenge\n2. Practice explaining concepts in the Python Study Group\n3. Watch 2 Reels in the 'Deep Learning' category\n\nWhat topic should we break down next?"
        }
    }
}
