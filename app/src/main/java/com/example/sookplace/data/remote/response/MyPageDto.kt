package com.example.sookplace.data.remote.response

import com.google.gson.annotations.SerializedName

// 1. 사용자 정보 (/me)
data class UserMeResponse(
    val id: String,
    val userId: String,
    val nickname: String,
    val email: String,
    val sookVerified: Boolean,
    val level: Int,
    val levelTitleEn: String, // 예: "Egg Song"
    val nextLevel: Int?,
    val nextLevelTitleEn: String?,
    val progressToNextPercent: Int,
    val avatarId: String,
    val avatarUrl: String, // 읽기 전용 URL
    val createdAt: String,
    val updatedAt: String
)

// 2. 퀘스트 현황 및 목표 (/me/quests)
data class UserQuestResponse(
    val currentLevel: Int,
    val nextLevel: Int?,
    val quests: List<QuestItem> // 3개 고정
)

data class QuestItem(
    val type: String, // REVIEW, COMMENT, VISIT 등
    val title: String,
    val currentCount: Int,
    val goalCount: Int,
    val progressPercent: Int,
    val description: String
)

// 3. 현황 리포트 (/me/stats)
data class UserStatsResponse(
    val postCount: Int,
    val savedPlaceCount: Int,
    val receivedLikeCount: Int
)

// 4. My Places 전체 보기 (/me/favorites)
data class MyPlaceResponse(
    val items: List<MyPlaceItem>,
    val totalCount: Int
)

data class MyPlaceItem(
    val id: String, // Place ID
    val name: String,
    val category: String, // 예: "KOREAN", "CAFE"
    val imageUrl: String? // 썸네일 이미지가 있다면
)

// 5. My Posts 전체 보기 (/me/posts)
data class MyPostResponse(
    val content: List<MyPostItem>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
    val hasNext: Boolean
)

data class MyPostItem(
    val postId: String,
    val title: String,
    val content: String, // 미리보기 내용
    val likeCount: Int,
    val commentCount: Int,
    val createdAt: String,
    val imageUrl: String? // 대표 이미지
)

// 6. 알림 설정 (/me/settings)
data class NotificationSettingResponse(
    val push: Boolean,
    val comment: Boolean,
    val like: Boolean,
    val recommend: Boolean
)