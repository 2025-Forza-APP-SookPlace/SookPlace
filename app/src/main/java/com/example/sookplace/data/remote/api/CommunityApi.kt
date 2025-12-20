package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.response.CommunityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CommunityApi {

    //피드 조회api
    @GET("/posts")
    suspend fun getCommunityFeed(
        @Query("category") category: String?,
        @Query("sort") sort: String?, // 형식: "createdAt,desc"
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): CommunityResponse
}