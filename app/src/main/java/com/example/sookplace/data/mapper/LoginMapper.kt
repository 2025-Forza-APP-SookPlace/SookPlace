package com.example.sookplace.data.mapper

import com.example.sookplace.data.local.entity.UserProfileEntity
import com.example.sookplace.data.remote.response.LoginResponse

fun LoginResponse.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = this.user.id,
        nickname = "눈송이",
        avatarUrl = "",
        level = 1,
        levelTitle = "새내기 송이",
        lastUpdated = System.currentTimeMillis()
    )
}