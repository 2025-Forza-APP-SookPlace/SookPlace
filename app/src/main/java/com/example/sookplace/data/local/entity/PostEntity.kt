package com.example.sookplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PostEntity")
data class PostEntity (
    @PrimaryKey
    var id: Int = 0,
    var userId: Int = 0,
    var title: String = "",
    val content: String = "",
    val category: String = "",
    val likeCount: Int = 0,
    val saveCount: Int = 0,
    val commentCount: Int = 0
)