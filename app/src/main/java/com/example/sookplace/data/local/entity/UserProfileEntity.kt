package com.example.sookplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile") //UI용 사용자 기본 정보 저장
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 0,
    val nickname: String,
    val avatarUrl: String,
    val level: Int,
    var levelTitle: String,
    val lastUpdated: Long
)