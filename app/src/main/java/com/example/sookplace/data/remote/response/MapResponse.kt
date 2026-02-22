package com.example.sookplace.data.remote.response

data class FavoriteResponse(
    val items: List<FavoriteItem>,
    val totalCount: Int
)

data class FavoriteItem(
    val id: Long,
    val name: String,
    val category: String,
    val thumbnailUrl: String,
    val rating: Double,
    val distanceMinutesFromCampus: Int,
    val hasPartnership: Boolean,
    val partnershipBadge: String?,
    val geo: Geo,
    val detailUrl: String
)

data class Geo(
    val lat: Double,
    val lon: Double
)
