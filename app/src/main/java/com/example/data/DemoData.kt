package com.example.data

object DemoData {

    val currentUser = User(
        id = "user_me",
        name = "Alex Vance",
        username = "alex_codes",
        bio = "Aspiring AI Engineer | Curious builder. Learning something new every single day on SkillReel 🚀",
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
        isCreator = true,
        creatorTitle = "Learner & Creator",
        followersCount = 428,
        followingCount = 189,
        xp = 480,
        streakDays = 7,
        hoursLearned = 14.2f,
        reelsCompleted = 54,
        challengesCompleted = 32,
        careerGoal = "AI Engineer",
        currentLevel = DifficultyLevel.Intermediate,
        dailyGoalMinutes = 20,
        selectedInterests = listOf("Coding", "Artificial Intelligence", "Data Science", "Business", "Career")
    )

    val creators = listOf(
        User("c1", "Dr. Sarah Chen", "drsarah_ai", "AI Research Scientist & Educator. Demystifying Neural Networks in 45s.", "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150", true, "AI Scientist", 24500, 120),
        User("c2", "Marcus Dev", "marcus_codes", "Senior Staff Engineer @ CloudScale. Master clean code & Python.", "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150", true, "Staff Engineer", 18200, 85),
        User("c3", "Elena Rostova", "elena_ux", "Design Lead @ StudioNorth. Visual hierarchy, Figma tips & UX laws.", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150", true, "Design Lead", 31000, 210),
        User("c4", "Devon Cole", "data_devon", "Data Scientist. SQL, Pandas, & BigQuery made digestible.", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150", true, "Data Scientist", 14300, 94),
        User("c5", "Amara Okafor", "career_amara", "Tech Career Coach & ex-Recruiter. Helping you land $150k+ roles.", "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150", true, "Career Coach", 42100, 160),
        User("c6", "Liam Vance", "cloud_liam", "DevOps & Cloud Architect. Kubernetes, Docker, AWS in bite-sized clips.", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150", true, "Cloud Architect", 12800, 110),
        User("c7", "Maya Lin", "finance_maya", "Chartered Financial Analyst. Wealth building, investing & compound growth.", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150", true, "CFA Analyst", 28900, 140),
        User("c8", "Kenji Sato", "kenji_sec", "Cybersecurity Analyst. Ethical hacking, secure coding & defense tips.", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150", true, "Security Analyst", 19500, 78),
        User("c9", "Chloe Bennett", "chloe_speak", "Public Speaker & Communication strategist. Speak with magnetic confidence.", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150", true, "Speaking Coach", 22400, 95),
        User("c10", "Rafael Gomez", "mobile_rafael", "Android & Kotlin specialist. Modern Compose & Architecture tricks.", "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150", true, "Android Lead", 16200, 130),
        User("c11", "Zoe Alvarez", "zoe_lens", "Commercial Photographer. Lighting setups & cinematic composition rules.", "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=150", true, "Photographer", 15700, 142),
        User("c12", "Tariq Malik", "biz_tariq", "Startup Founder. Unit economics, fundraising & growth marketing.", "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=150", true, "Founder", 35400, 310),
        User("c13", "Nadia Petrova", "deep_nadia", "Deep Learning engineer. Transformers, Attention mechanism, LLMs.", "https://images.unsplash.com/photo-1548142813-c348350df52b?w=150", true, "AI Engineer", 21800, 105),
        User("c14", "Julian Ross", "edit_julian", "Video Editor & Colorist. Premiere, DaVinci and pacing storytelling.", "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150", true, "Editor & VFX", 11900, 64),
        User("c15", "Priya Sharma", "priya_algo", "Competitive Programmer. Dynamic Programming & Graph algorithms simplified.", "https://images.unsplash.com/photo-1567532939604-b6b5b0db2604?w=150", true, "Algo Coach", 38700, 190)
    )

    val skillsList = listOf(
        SkillMastery("s1", "Python Programming", "Programming", 80, "Intermediate", 420, "Generators and Decorators"),
        SkillMastery("s2", "Machine Learning & AI", "AI", 65, "Intermediate", 340, "Gradient Descent Intuition"),
        SkillMastery("s3", "Data Structures & Algos", "Computer Science", 45, "Beginner", 210, "Binary Search Trees"),
        SkillMastery("s4", "SQL & Database Design", "Data Science", 90, "Advanced", 560, "Window Functions & Partitioning"),
        SkillMastery("s5", "System Design", "Engineering", 30, "Beginner", 150, "Load Balancing Strategies"),
        SkillMastery("s6", "UI/UX & Product Design", "Design", 50, "Intermediate", 280, "Fitts's Law in Mobile UX"),
        SkillMastery("s7", "Public Communication", "Soft Skills", 60, "Intermediate", 310, "Eliminating Filler Words"),
        SkillMastery("s8", "Financial Literacy", "Finance", 40, "Beginner", 190, "Index Funds vs ETFs"),
        SkillMastery("s9", "Git & Version Control", "Tools", 95, "Advanced", 620, "Interactive Rebasing"),
        SkillMastery("s10", "Cloud & Docker", "DevOps", 35, "Beginner", 175, "Multi-stage Container Builds")
    )

    val sampleChallenges = listOf(
        Challenge("ch1", "Which Python loop is commonly used when you know how many times you want to repeat something?", listOf("while loop", "for loop", "do-while loop", "repeat loop"), 1, "The 'for' loop iterates over a known sequence or range.", 10, "Python Programming", DifficultyLevel.Beginner),
        Challenge("ch2", "In Neural Networks, what is the primary role of an Activation Function?", listOf("To calculate gradient loss", "To introduce non-linearity into neurons", "To normalize the input dataset", "To increase RAM cache"), 1, "Activation functions allow neural networks to model non-linear boundaries.", 15, "Machine Learning", DifficultyLevel.Intermediate),
        Challenge("ch3", "Which SQL command merges rows from two tables based on a related common column?", listOf("CONCAT", "UNION", "JOIN", "APPEND"), 2, "JOIN combines columns from one or more tables based on relations.", 10, "SQL", DifficultyLevel.Beginner),
        Challenge("ch4", "In Git, which command safely incorporates branch changes by applying commits on top of another branch?", listOf("git merge --force", "git pull --override", "git rebase", "git checkout -b"), 2, "Git rebase reapplies commits on top of another base tip.", 15, "Git", DifficultyLevel.Intermediate),
        Challenge("ch5", "What is the worst-case time complexity of searching an element in a balanced Binary Search Tree?", listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"), 1, "A balanced BST has a height of log n, giving O(log n) lookups.", 15, "Data Structures", DifficultyLevel.Intermediate),
        Challenge("ch6", "What is the golden rule of composition when placing subjects in visual photography?", listOf("Center dead middle always", "Rule of Thirds intersection points", "Extreme bottom left corner", "Avoid symmetry at all costs"), 1, "Rule of thirds guides the viewer naturally along aesthetic intersections.", 10, "Photography", DifficultyLevel.Beginner),
        Challenge("ch7", "In Resume writing, which bullet structure yields highest recruiter conversion?", listOf("Passive responsibilities list", "Action Verb + Task + Quantifiable Impact", "Chronological daily tasks", "Hobbies and generic soft skills"), 1, "Impact-driven metrics (e.g. 'Increased efficiency by 34%') stand out instantly.", 10, "Career", DifficultyLevel.Beginner),
        Challenge("ch8", "What HTTP status code represents a resource that was successfully created?", listOf("200 OK", "201 Created", "204 No Content", "301 Moved"), 1, "201 Created signifies successful server-side generation of a new entity.", 10, "Web APIs", DifficultyLevel.Beginner)
    )

    val sampleReels = listOf(
        Reel(
            id = "reel_1",
            title = "Python Variables in 30 Seconds",
            description = "Understand variable pointers and memory references in Python without confusion. Ready to code?",
            videoPreviewGradient = listOf(0xFF2E1065, 0xFF3B82F6),
            durationSeconds = 30,
            creatorId = "c2",
            creatorName = "Marcus Dev",
            creatorUsername = "marcus_codes",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150",
            likesCount = 8420,
            commentsCount = 234,
            savesCount = 3120,
            sharesCount = 590,
            skillCategory = "Python Programming",
            difficulty = DifficultyLevel.Beginner,
            hashtags = listOf("#Python", "#Coding", "#LearnToCode", "#TechSkills"),
            challenge = sampleChallenges[0],
            viewsCount = 42100
        ),
        Reel(
            id = "reel_2",
            title = "What is Machine Learning really?",
            description = "Instead of writing rules, we feed data and let algorithms detect statistical patterns. Here is the intuition.",
            videoPreviewGradient = listOf(0xFF0F172A, 0xFF8B5CF6),
            durationSeconds = 45,
            creatorId = "c1",
            creatorName = "Dr. Sarah Chen",
            creatorUsername = "drsarah_ai",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150",
            likesCount = 14200,
            commentsCount = 480,
            savesCount = 6840,
            sharesCount = 1430,
            skillCategory = "Machine Learning",
            difficulty = DifficultyLevel.Beginner,
            hashtags = listOf("#ArtificialIntelligence", "#MachineLearning", "#DeepLearning", "#AI"),
            challenge = sampleChallenges[1],
            viewsCount = 98000
        ),
        Reel(
            id = "reel_3",
            title = "SQL JOIN Explained Visually",
            description = "INNER vs LEFT vs RIGHT vs FULL OUTER join. The Venn diagram breakdown that finally makes it click.",
            videoPreviewGradient = listOf(0xFF064E3B, 0xFF059669),
            durationSeconds = 35,
            creatorId = "c4",
            creatorName = "Devon Cole",
            creatorUsername = "data_devon",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            likesCount = 11300,
            commentsCount = 310,
            savesCount = 7400,
            sharesCount = 1100,
            skillCategory = "SQL",
            difficulty = DifficultyLevel.Beginner,
            hashtags = listOf("#SQL", "#DataScience", "#Database", "#Analytics"),
            challenge = sampleChallenges[2],
            viewsCount = 76000
        ),
        Reel(
            id = "reel_4",
            title = "Git in 60 Seconds: Rebasing vs Merging",
            description = "Stop fearing git rebase! Keep a clean linear git history without ugly merge bubbles.",
            videoPreviewGradient = listOf(0xFF831843, 0xFFEC4899),
            durationSeconds = 58,
            creatorId = "c2",
            creatorName = "Marcus Dev",
            creatorUsername = "marcus_codes",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150",
            likesCount = 9800,
            commentsCount = 215,
            savesCount = 5200,
            sharesCount = 890,
            skillCategory = "Git",
            difficulty = DifficultyLevel.Intermediate,
            hashtags = listOf("#Git", "#SoftwareEngineering", "#DevOps", "#Productivity"),
            challenge = sampleChallenges[3],
            viewsCount = 61000
        ),
        Reel(
            id = "reel_5",
            title = "3 Fatal Resume Mistakes Killing Tech Interviews",
            description = "Ex-recruiter secret: Why bullet points starting with 'Responsible for...' get thrown out in 6 seconds.",
            videoPreviewGradient = listOf(0xFF78350F, 0xFFF59E0B),
            durationSeconds = 42,
            creatorId = "c5",
            creatorName = "Amara Okafor",
            creatorUsername = "career_amara",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150",
            likesCount = 22100,
            commentsCount = 890,
            savesCount = 18900,
            sharesCount = 4200,
            skillCategory = "Career",
            difficulty = DifficultyLevel.Beginner,
            hashtags = listOf("#CareerTips", "#Resume", "#TechJobs", "#InterviewPrep"),
            challenge = sampleChallenges[6],
            viewsCount = 145000
        ),
        Reel(
            id = "reel_6",
            title = "How REST APIs Actually Work",
            description = "Client makes a request with HTTP method & headers, server returns status + JSON. Here is the restaurant analogy.",
            videoPreviewGradient = listOf(0xFF1E1B4B, 0xFF6366F1),
            durationSeconds = 40,
            creatorId = "c10",
            creatorName = "Rafael Gomez",
            creatorUsername = "mobile_rafael",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150",
            likesCount = 13400,
            commentsCount = 290,
            savesCount = 8100,
            sharesCount = 1600,
            skillCategory = "Web APIs",
            difficulty = DifficultyLevel.Beginner,
            hashtags = listOf("#APIs", "#Backend", "#AndroidDev", "#WebDev"),
            challenge = sampleChallenges[7],
            viewsCount = 89000
        ),
        Reel(
            id = "reel_7",
            title = "Figma Auto-Layout in 45 Seconds",
            description = "Build responsive cards that stretch and shrink effortlessly using Hug, Fill, and Fixed constraints.",
            videoPreviewGradient = listOf(0xFF312E81, 0xFF4338CA),
            durationSeconds = 45,
            creatorId = "c3",
            creatorName = "Elena Rostova",
            creatorUsername = "elena_ux",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150",
            likesCount = 7600,
            commentsCount = 180,
            savesCount = 4500,
            sharesCount = 630,
            skillCategory = "UI/UX & Product Design",
            difficulty = DifficultyLevel.Intermediate,
            hashtags = listOf("#Figma", "#UXDesign", "#UI", "#ProductDesign"),
            challenge = null,
            viewsCount = 48000
        ),
        Reel(
            id = "reel_8",
            title = "Docker Containers vs Virtual Machines",
            description = "Shared OS kernel vs separate guest OS. Why containers boot up in milliseconds and save massive memory.",
            videoPreviewGradient = listOf(0xFF0369A1, 0xFF0284C7),
            durationSeconds = 50,
            creatorId = "c6",
            creatorName = "Liam Vance",
            creatorUsername = "cloud_liam",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150",
            likesCount = 10500,
            commentsCount = 340,
            savesCount = 6700,
            sharesCount = 1250,
            skillCategory = "Cloud & Docker",
            difficulty = DifficultyLevel.Intermediate,
            hashtags = listOf("#Docker", "#DevOps", "#CloudComputing", "#Kubernetes"),
            challenge = null,
            viewsCount = 72000
        )
    )

    val samplePosts = listOf(
        Post(
            id = "p1",
            creatorId = "c1",
            creatorName = "Dr. Sarah Chen",
            creatorUsername = "drsarah_ai",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150",
            content = "🧠 Quick mental model for Neural Networks: You don't program the solution; you set up the playing field and reward function, and let calculus iteratively optimize weights. What AI concept confused you the most when starting out?",
            postType = PostType.LearningTip,
            category = PostCategory.Educational,
            timeAgo = "2h ago",
            likesCount = 412,
            commentsCount = 89
        ),
        Post(
            id = "p2",
            creatorId = "c2",
            creatorName = "Marcus Dev",
            creatorUsername = "marcus_codes",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150",
            content = "Poll: When learning a new backend framework, what is your primary learning strategy?",
            postType = PostType.Poll,
            category = PostCategory.Discussion,
            timeAgo = "5h ago",
            likesCount = 830,
            commentsCount = 142,
            pollOptions = listOf("Build a real project immediately", "Read official documentation end-to-end", "Watch short video reels & tutorials", "Follow structured curriculum"),
            pollVotes = listOf(48, 12, 28, 12)
        ),
        Post(
            id = "p3",
            creatorId = "c5",
            creatorName = "Amara Okafor",
            creatorUsername = "career_amara",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150",
            content = "🚨 3 golden rules for answering 'Tell me about yourself' in tech interviews:\n1. Present (your current skill focus)\n2. Past (key project highlights + numbers)\n3. Future (why this exact team inspires you).\nKeep it strictly under 90 seconds!",
            postType = PostType.Text,
            category = PostCategory.Career,
            timeAgo = "1d ago",
            likesCount = 1920,
            commentsCount = 205
        )
    )

    val sampleStories = listOf(
        Story("st1", "c1", "Dr. Sarah", "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150", "Daily AI Quiz", "Test your knowledge on Attention Mechanisms! Solve in 30s for +15 XP", listOf(0xFF4C1D95, 0xFF8B5CF6), true, sampleChallenges[1]),
        Story("st2", "c2", "Marcus Dev", "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150", "Python Tip", "Did you know `enumerate()` accepts a start index? `enumerate(list, 1)`", listOf(0xFF1E3A8A, 0xFF3B82F6), false, null),
        Story("st3", "c3", "Elena UX", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150", "Design Challenge", "Which color contrast ratio passes WCAG AAA for normal text?", listOf(0xFF831843, 0xFFF43F5E), true, Challenge("st_ch3", "What contrast ratio is required for WCAG AAA compliance on regular text?", listOf("3:1", "4.5:1", "7:1", "10:1"), 2, "7:1 is the strictest ratio for AAA compliance on body text.", 15, "Design")),
        Story("st4", "c4", "Devon Cole", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150", "SQL Challenge", "GROUP BY vs ORDER BY order of execution", listOf(0xFF065F46, 0xFF10B981), false, null)
    )

    val sampleLearningPaths = listOf(
        LearningPath(
            id = "lp_ai",
            title = "Become an AI Engineer",
            description = "From core Python and mathematics to Deep Learning, LLMs, and Production Generative AI.",
            targetRole = "AI / ML Engineer",
            estimatedHours = 45,
            enrolledCount = 12400,
            progressPercent = 65,
            isEnrolled = true,
            category = "Artificial Intelligence",
            stages = listOf(
                LearningPathStage("s1", 1, "Python for AI Foundations", "Syntax, data structures, list comprehensions, NumPy", 12, 4, true, false),
                LearningPathStage("s2", 2, "Applied Mathematics & Statistics", "Linear algebra, matrix operations, vectors, probability", 10, 3, true, false),
                LearningPathStage("s3", 3, "NumPy & Pandas Wrangling", "Dataset filtering, aggregation, missing values", 14, 5, true, false),
                LearningPathStage("s4", 4, "Machine Learning Algorithms", "Supervised vs Unsupervised, Regression, Random Forests", 16, 6, false, true),
                LearningPathStage("s5", 5, "Deep Learning & Neural Networks", "Backpropagation, CNNs, RNNs, PyTorch setup", 18, 6, false, false),
                LearningPathStage("s6", 6, "Generative AI & LLM Systems", "Transformers, prompt engineering, RAG pipelines", 20, 8, false, false),
                LearningPathStage("s7", 7, "AI Portfolio Projects & Deployment", "FastAPI model serving, HuggingFace, Dockerizing", 8, 2, false, false)
            )
        ),
        LearningPath(
            id = "lp_fullstack",
            title = "Modern Full-Stack Developer",
            description = "Build full-fledged scalable web applications using TypeScript, Next.js, databases, and cloud CI/CD.",
            targetRole = "Full-Stack Engineer",
            estimatedHours = 38,
            enrolledCount = 9800,
            progressPercent = 40,
            isEnrolled = true,
            category = "Programming",
            stages = listOf(
                LearningPathStage("fs1", 1, "Modern JavaScript & TypeScript", "ES6+, async/await, strict typing, interfaces", 10, 4, true, false),
                LearningPathStage("fs2", 2, "React 19 & Component Architecture", "Hooks, state management, memoization, suspense", 14, 5, true, false),
                LearningPathStage("fs3", 3, "Backend APIs with Node & Express", "Routing, middleware, JWT authentication, rate limiting", 12, 4, false, true),
                LearningPathStage("fs4", 4, "Database Architecture (Postgres & Prisma)", "Schemas, relations, migrations, optimization", 10, 3, false, false)
            )
        ),
        LearningPath(
            id = "lp_dsa",
            title = "DSA & Tech Interview Mastery",
            description = "Ace FAANG coding interviews with intuitive patterns: sliding window, two pointers, graphs & dynamic programming.",
            targetRole = "Software Engineer",
            estimatedHours = 30,
            enrolledCount = 15200,
            progressPercent = 25,
            isEnrolled = false,
            category = "Computer Science",
            stages = listOf(
                LearningPathStage("dsa1", 1, "Arrays & Hashing Patterns", "Two sum, frequency counters, prefix sums", 8, 4, true, false),
                LearningPathStage("dsa2", 2, "Two Pointers & Sliding Window", "Subarrays, palindrome checks, water container", 10, 5, false, true),
                LearningPathStage("dsa3", 3, "Trees, BST & Graphs (DFS/BFS)", "Traversals, cycle detection, topological sort", 15, 6, false, false),
                LearningPathStage("dsa4", 4, "Dynamic Programming Intuition", "1D/2D memoization, knapsack, coin change", 12, 4, false, false)
            )
        ),
        LearningPath(
            id = "lp_uiux",
            title = "UI/UX & Product Design Pro",
            description = "Craft intuitive mobile & web interfaces. Master user research, wireframing, design systems & micro-interactions.",
            targetRole = "Product Designer",
            estimatedHours = 24,
            enrolledCount = 6400,
            progressPercent = 10,
            isEnrolled = false,
            category = "Design",
            stages = listOf(
                LearningPathStage("ux1", 1, "UX Psychology & Laws", "Hick's Law, Fitts's Law, Jakob's Law", 6, 2, true, false),
                LearningPathStage("ux2", 2, "Figma Mastery & Design Tokens", "Auto-layout, variants, components, styles", 12, 4, false, true),
                LearningPathStage("ux3", 3, "Design Systems from Scratch", "Typography scales, elevation, accessible color systems", 10, 3, false, false)
            )
        )
    )

    val sampleChats = listOf(
        ChatConversation(
            id = "chat_1",
            name = "Python Study Group 🐍",
            avatarUrl = "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=150",
            isGroup = true,
            lastMessage = "Alex: Has anyone solved today's generator challenge?",
            timeAgo = "12m ago",
            unreadCount = 3,
            memberCount = 14
        ),
        ChatConversation(
            id = "chat_2",
            name = "Dr. Sarah Chen",
            avatarUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150",
            isGroup = false,
            lastMessage = "Great job on the Neural Network challenge! Keep up the momentum.",
            timeAgo = "1h ago",
            unreadCount = 0,
            isOnline = true
        ),
        ChatConversation(
            id = "chat_3",
            name = "AI Explorers Hub 🤖",
            avatarUrl = "https://images.unsplash.com/photo-1531482615713-2afd69097998?w=150",
            isGroup = true,
            lastMessage = "Nadia: Starting a Study Call on LLM Attention at 4 PM!",
            timeAgo = "2h ago",
            unreadCount = 1,
            memberCount = 28
        ),
        ChatConversation(
            id = "chat_4",
            name = "Marcus Dev",
            avatarUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150",
            isGroup = false,
            lastMessage = "Check out the new video on Python decorators.",
            timeAgo = "Yesterday",
            unreadCount = 0,
            isOnline = false
        )
    )

    val sampleMessages = mapOf(
        "chat_1" to listOf(
            ChatMessage("m1", "c2", "Marcus Dev", "Welcome everyone to the Python Study Group! Remember to share daily code nuggets.", "10:00 AM", false),
            ChatMessage("m2", "other1", "David Kim", "Working on recursion today. Anyone have a clean mental model for tail call optimization?", "10:14 AM", false),
            ChatMessage("m3", "user_me", "Alex Vance", "Think of tail recursion as a while loop with variable updates — no stack buildup if optimized!", "10:18 AM", true),
            ChatMessage("m4", "other1", "David Kim", "That actually makes total sense! Thanks Alex.", "10:20 AM", false),
            ChatMessage("m5", "user_me", "Alex Vance", "Has anyone solved today's generator challenge?", "10:25 AM", true, attachedReelTitle = "Python Variables in 30 Seconds")
        ),
        "chat_2" to listOf(
            ChatMessage("m2_1", "c1", "Dr. Sarah Chen", "Hey Alex! I saw your high score on the Activation Function challenge.", "09:00 AM", false),
            ChatMessage("m2_2", "user_me", "Alex Vance", "Thank you Dr. Sarah! Your 45-second explanation of non-linearity was super clear.", "09:12 AM", true),
            ChatMessage("m2_3", "c1", "Dr. Sarah Chen", "Great job on the Neural Network challenge! Keep up the momentum.", "09:15 AM", false)
        )
    )

    val sampleNotifications = listOf(
        NotificationItem("n1", "Challenge Completed! ⚡", "You solved 'Python Variables' correctly on the first attempt.", "10m ago", NotificationType.Challenge, false, xpEarned = 10),
        NotificationItem("n2", "Streak Milestone 🔥", "You hit a 7-day learning streak! Consistency is compounding your skills.", "1h ago", NotificationType.Achievement, false, xpEarned = 25),
        NotificationItem("n3", "Dr. Sarah Chen posted a new Reel", "Check out: 'Understanding Softmax in 40 Seconds'", "3h ago", NotificationType.Reminder, false),
        NotificationItem("n4", "Learning Path Progress", "You are now 65% through 'Become an AI Engineer'!", "Yesterday", NotificationType.PathProgress, true),
        NotificationItem("n5", "Marcus Dev liked your comment", "On 'Git in 60 Seconds: Rebasing vs Merging'", "2d ago", NotificationType.Like, true)
    )

    val sampleAchievements = listOf(
        AchievementItem("ach_1", "First Spark", "Complete your very first Reel challenge", "⚡", 15, true, 1, 1),
        AchievementItem("ach_2", "7-Day Ignition", "Maintain a 7-day learning streak", "🔥", 50, true, 7, 7),
        AchievementItem("ach_3", "AI Explorer", "Complete 10 Artificial Intelligence reels", "🤖", 75, true, 10, 10),
        AchievementItem("ach_4", "Challenge Master", "Solve 25 challenges with 100% accuracy", "🏆", 100, true, 25, 25),
        AchievementItem("ach_5", "Knowledge Architect", "Enroll and finish 3 learning path stages", "📚", 60, true, 3, 3),
        AchievementItem("ach_6", "30-Day Master", "Maintain an uninterrupted 30-day streak", "👑", 250, false, 7, 30),
        AchievementItem("ach_7", "Creator Voice", "Upload your first educational learning Reel", "🎥", 50, false, 0, 1)
    )

    val sampleReports = listOf(
        ModerationReport("rep_1", "Reel", "Spam crypto signals disguised as tech tutorial", "crypto_bot99", "alex_codes", "Misleading content & spam", "4h ago"),
        ModerationReport("rep_2", "Comment", "Inappropriate comment in Python Study Group", "troll_user", "marcus_codes", "Harassment & abusive language", "1d ago")
    )
}
