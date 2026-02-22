package com.example.sookplace.data.remote.response

//검색
data class SearchResponse(
    val totalElements: Int,
    val totalPages: Int,
    val currentPage: Int,
    val restaurants: List<RestaurantContent>
)

//검색 & 정렬
data class SortResponse(
    val totalElements: Int,
    val totalPages: Int,
    val currentPage: Int,
    val restaurants: List<RestaurantContent>
)

data class RestaurantContent(
    val id: Int,
    val name: String,
    val category: String,
    val thumbnailUrl: String?,
    val rating: Double,
    var likeCount: Int,
    var isLiked: Boolean,
    val distanceMinutesFromCampus: Int?,
    val locationName: String?,
    val shareUrl: String?
)

//식당 좋아요
data class LikeToggleResponse(
    val likeCount: Int,
    val liked: Boolean
)

//식당 핀
