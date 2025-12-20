package com.example.sookplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "featured_restaurants") //오늘의 숙플레이스 정보 저장
data class FeaturedRestaurantEntity(
    @PrimaryKey val name: String,
    val address: String,
    val thumbnailUrl: String,
    val isLiked: Boolean
)