package com.example.sookplace.data.remote.api

import retrofit2.http.GET

class RestaurantApi {

    @GET("/restaurants/featured")
    suspend fun getFeaturedRestaurants(): FeaturedRestaurantsResponse
}