package com.example.sookplace.data.local.entity

import androidx.room.Entity

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    val nickname: String,
    val avatarUrl: String,
    val level: Int,
    var levelTitle: String,
    val lastUpdated: Long
)