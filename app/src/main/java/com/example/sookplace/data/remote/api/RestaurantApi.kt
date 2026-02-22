package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.response.FeaturedRestaurantsResponse
import com.example.sookplace.data.remote.response.LikeToggleResponse
import com.example.sookplace.data.remote.response.RestaurantDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RestaurantApi {
    //홈화면 - 오늘의 숙플레이스
    @GET("restaurants/featured")
    suspend fun getFeaturedRestaurants(): FeaturedRestaurantsResponse

    //탐색화면 - 식당 상세
    @GET("restaurants/{restaurantId}")
    suspend fun getRestaurantDetail(
        @Path("restaurantId") restaurantId: Int
    ): RestaurantDetailResponse

    //식당 좋아요 //TODO: 식당 좋아요, 핀 기능 구현 후 주석 해제
    @POST("/restaurants/{id}/like")
    suspend fun toggleLike(
        @Path("id") restaurantId: Int
    ): LikeToggleResponse

    //식당 핀
//    @POST("restaurants/{restaurantId}/pin")
//    suspend fun postPin(
//        @Path("restaurantId") restaurantId: Int
//    ) :Response<Unit>
}
