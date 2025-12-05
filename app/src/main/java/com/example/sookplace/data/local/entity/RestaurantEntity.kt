package com.example.sookplace.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RestaurantEntity")
data class RestaurantEntity (
    @PrimaryKey
    var id: Int = 0,
    var name: String = "",
    var category: String = "",
    var address: String = "",
    var menu: String = "",
    var avgRating: Double = 0.0,
    var likeCount: Int = 0,
    var reviewCount: Int = 0,
    var imageUrl: String = "",
    var isLiked: Boolean = false,
    var distanceMinutesFromCampus: Int = 0,
    val naverMapLink: String?,
    val lastUpdated: Long
)