package com.example.sookplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile") //UI용 사용자 기본 정보 저장(+식별자 ID)
data class UserProfileEntity(
    @PrimaryKey
    val id: String,
    val nickname: String,
    val avatarUrl: String,
    val level: Int,
    var levelTitle: String,
    val lastUpdated: Long
)