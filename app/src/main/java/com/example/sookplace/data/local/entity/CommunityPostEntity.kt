package com.example.sookplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community_post")
data class CommunityPostEntity(
    @PrimaryKey val postId: String,
    val authorId: String,
    val authorName: String,
    val title: String,
    val contentPreview: String, // 리스트용 요약
    val likeCount: Int,
    val commentCount: Int,
    val createdAt: Long,
    val lastUpdated: Long
)