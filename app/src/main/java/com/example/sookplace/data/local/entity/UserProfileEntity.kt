package com.example.sookplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile") //UI용 사용자 기본 정보 저장(+식별자 ID)
data class UserProfileEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val nickname: String,
    val email: String,
    val sookVerified: Boolean,
    val level: Int,
    val levelTitleEn: String,
    val nextLevel: Int?,
    val nextLevelTitleEn: String?,
    val progressToNextPercent: Int,
    val avatarId: String?,
    val avatarUrl: String?,
    val createdAt: String,
    val updatedAt: String
)