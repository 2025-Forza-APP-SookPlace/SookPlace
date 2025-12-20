package com.example.sookplace.ui.mypage.model

data class NotificationSettings(
    val enabledPush: Boolean,
    val enabledComment: Boolean,
    val enabledLike: Boolean,
    val enabledRecommendation: Boolean
)