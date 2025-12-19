package com.example.sookplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferenceEntity(
    @PrimaryKey val restaurantId: Int,
    val name: String,
    val category: String,
    val thumbnailUrl: String?,
    val rating: Double,
    val address: String,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
)