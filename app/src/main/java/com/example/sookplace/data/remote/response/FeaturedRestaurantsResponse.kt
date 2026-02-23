package com.example.sookplace.data.remote.response

data class FeaturedRestaurantsResponse(
    val featuredRestaurants: List<RestaurantItem>
)

data class RestaurantItem(
    val name: String,
    val address: String,
    val thumbnailUrl: String,
    val isLiked: Boolean
)


