package com.example.sookplace.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RestaurantEntity")
data class RestaurantEntity (
    @PrimaryKey
    var id: Int = 0, // 주의: 서버에서 받은 유효한 ID를 사용해야 함. 기본값 0은 실제 식당 ID와 충돌 가능
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