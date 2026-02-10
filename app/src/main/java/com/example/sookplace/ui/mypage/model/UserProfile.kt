package com.example.sookplace.ui.mypage.model

data class UserProfile(
    val nickname: String,
    val email: String,
    val level: String,
    val progressPercent: Int,
    val levelBadge: Boolean,
    val profileImageUrl: String?
)