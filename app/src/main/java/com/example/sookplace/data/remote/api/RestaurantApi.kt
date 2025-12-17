package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.response.FeaturedRestaurantsResponse
import retrofit2.http.GET

interface RestaurantApi {

    @GET("/restaurants/featured")
    suspend fun getFeaturedRestaurants(): FeaturedRestaurantsResponse

}