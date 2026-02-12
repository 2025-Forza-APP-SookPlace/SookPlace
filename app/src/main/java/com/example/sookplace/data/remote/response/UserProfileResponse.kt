package com.example.sookplace.data.remote.response

data class UserProfileResponse(
    val id: String,
    val userId: String,
    val nickname: String,
    val email: String,
    val sookVerified: Boolean,
    val level : Int,
    val levelTitleEn: String,
    val nextLevel: Int,
    val nextLevelTitleEn:String,
    val progressToNextPercent:Int,
    val avatarId: String,
    val avatarUrl: String,
    val createdAt: String,
    val updatedAt: String
)