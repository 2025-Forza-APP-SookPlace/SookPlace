package com.example.sookplace.data.remote.response

//검색
data class SearchResponse(
    val content: List<RestaurantContent>,
    val totalElements: Int,
    val totalPages: Int,
    val page: Int,
    val size: Int
)

data class RestaurantContent(
    val id: Int,
    val name: String,
    val address: String,
    val rating: Double,
    val likeCount: Int,
    val thumbnailUrl: String?
)

//정렬
data class SortResponse(
    val totalElements: Int,
    val totalPages: Int,
    val currentPage: Int,
    val restaurants: List<SortRestaurantItem>
)

data class SortRestaurantItem(
    val id: Int,
    val name: String,
    val category: String,
    val thumbnailUrl: String?,
    val rating: Double,
    var likeCount: Int,
    var isLiked: Boolean,
    val distanceMinutesFromCampus: Int?,
    val locationName: String,
    val shareUrl: String
)
