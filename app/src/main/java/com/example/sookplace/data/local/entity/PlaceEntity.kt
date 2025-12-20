package com.example.sookplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlaceEntity") //사용자가 핀을 꽂은 식당 저장(마이 플레이스용)
data class PlaceEntity(
    @PrimaryKey val restaurantId: Int,
    val name: String,
    val category: String,
    val thumbnailUrl: String?,
    val rating: Double,
    val address: String,
    val addedAt: Long = System.currentTimeMillis()
)