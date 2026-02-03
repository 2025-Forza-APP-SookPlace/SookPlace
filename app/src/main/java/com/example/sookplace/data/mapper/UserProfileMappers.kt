package com.example.sookplace.data.mapper

import com.example.sookplace.data.local.entity.UserProfileEntity
import com.example.sookplace.data.remote.response.UserProfileResponse

fun UserProfileResponse.toEntity(currentId: String): UserProfileEntity {
    return UserProfileEntity(
        id = currentId,
        nickname = this.nickname,
        avatarUrl = this.avatarUrl,
        level = this.level,
        levelTitle = this.levelTitle,
        lastUpdated = System.currentTimeMillis()
    )
}