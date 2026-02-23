package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.request.SearchRequest
import com.example.sookplace.data.remote.response.LikeToggleResponse
import com.example.sookplace.data.remote.response.SearchResponse
import com.example.sookplace.data.remote.response.SortResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApi {

    //탐색-검색 api
    @GET("restaurants")
    suspend fun searchRestaurants(
        @Query("keyword") keyword: String,
        @Query("category") category: String?,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String?
    ): SearchResponse

    //탐색-정렬 api
    @GET("restaurants")
    suspend fun getSortedRestaurants(
        @Query("category") category: String?,
        @Query("sort") sort: String?,
        @Query("dir") dir: String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): SortResponse

    //식당 좋아요
    @POST("restaurants/{id}/like")
    suspend fun toggleLike(
        @Path("id") restaurantId: Int
    ): LikeToggleResponse

}