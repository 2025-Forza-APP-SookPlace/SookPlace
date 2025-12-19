package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.response.FeaturedRestaurantsResponse
import com.example.sookplace.data.remote.response.RestaurantDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantApi {
    //홈화면 - 오늘의 숙플레이스
    @GET("/restaurants/featured")
    suspend fun getFeaturedRestaurants(): FeaturedRestaurantsResponse

    //탐색화면 - 식당 상세
    @GET("restaurants/{restaurantId}")
    suspend fun getRestaurantDetail(
        @Path("restaurantId") restaurantId: Int
    ): RestaurantDetailResponse
}