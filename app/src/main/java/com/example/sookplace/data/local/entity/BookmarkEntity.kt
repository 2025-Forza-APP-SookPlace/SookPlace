package com.example.sookplace.data.local.entity

import androidx.room.Entity

@Entity(primaryKeys = ["userId","restaurantId"])
data class BookmarkEntity(
    val userId: String,
    val restaurantId: String,
    val createdAt: Long
)