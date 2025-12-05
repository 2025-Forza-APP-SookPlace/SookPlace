package com.example.sookplace.data.mapper

import com.example.sookplace.data.local.entity.UserProfileEntity
import com.example.sookplace.data.remote.response.UserProfileResponse

fun UserProfileResponse.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        nickname = nickname,
        level = level,
        levelTitle = levelTitle,
        avatarUrl = avatarUrl,
        lastUpdated = System.currentTimeMillis()
    )
}