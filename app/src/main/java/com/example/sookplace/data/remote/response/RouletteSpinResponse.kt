package com.example.sookplace.data.remote.response


data class RouletteSpinResponse(
    val type : String,
    val restaurant : Restaurant?,
    val category: Category?,
    val nextAction: NextAction?
)

data class Restaurant(
    val id: Int,
    val name: String,
    val category: String,
    val thumbnailUrl: String?,
    val rating: Double,
    val distanceMinutesFromCampus: Int?
)

data class Category (
    val key: String,
    val name: String
)

data class NextAction (
    val detailUrl: String,
    val searchUrl: String
)
