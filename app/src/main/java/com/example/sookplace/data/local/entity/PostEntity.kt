package com.example.sookplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PostEntity")
data class PostEntity ( //마이포스트(북마크된 게시물 저장)
    @PrimaryKey val postId: String,
    val userId: String,
    val nickname: String,
    val profileImageUrl: String?,
    val title: String,
    val excerpt: String,
    val category: String,
    val placeId: String,
    val placeName: String,
    val rating: Double,
    val imageUrl: String?,
    val likeCount: Int,
    val commentCount: Int,
    val displayTime: String
)