package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.response.FavoriteResponse
import com.example.sookplace.data.remote.response.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapApi {

    //지도에서 MyPlace 리스트 불러오기
    @GET("me/favorites")
    suspend fun getFavorites(
        @Query("limit") limit: Int
    ): FavoriteResponse
}