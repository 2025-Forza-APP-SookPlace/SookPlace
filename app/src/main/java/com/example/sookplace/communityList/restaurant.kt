package com.example.sookplace.communityList

data class restaurant(
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val thumbnailUrl: String = "",
    val rating: Double = 0.0,
    val likeCount: Int = 0,
    val isLiked: Boolean = false,
    val distanceMinutesFromCampus: Int = 0,
    val locationName: String = "",
    val shareUrl: String = ""
)