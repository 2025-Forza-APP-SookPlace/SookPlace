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
    var latitude: Double = 37.5450,
    var longitude: Double = 126.9647,
    var menu: String = "",
    var avgRating: Double = 0.0,
    var reviewCount: Int = 0,
    var imageUrl: String = "",
    // API에는 있지만 ERD에는 없음
    var likeCount: Int = 0,
    var isLiked: Boolean = false,
    var distanceMinutesFromCampus: Int = 0,
    var shareUrl: String = ""
)