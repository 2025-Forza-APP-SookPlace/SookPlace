package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.request.NicknameChangeRequest
import com.example.sookplace.data.remote.request.PasswordChangeRequest
import com.example.sookplace.data.remote.request.UniversityVerifyRequest
import com.example.sookplace.data.remote.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface UserApi {
    @GET("api/me")
    suspend fun getUserMe(): UserMeResponse

    @GET("api/me/quests")
    suspend fun getUserQuests(): UserQuestResponse

    @GET("api/me/stats")
    suspend fun getUserStats(): UserStatsResponse

    @GET("api/me/places")
    suspend fun getMyPlaces(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): MyPlaceResponse

    @GET("api/me/posts")
    suspend fun getMyPosts(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): MyPostResponse

    // [🔥 뷰모델의 빨간 줄을 없애줄 새로운 계정 관리 API들]

    @PATCH("api/me/profile")
    suspend fun updateNickname(@Body request: NicknameChangeRequest): NicknameChangeResponse

    @PATCH("api/me/password")
    suspend fun updatePassword(@Body request: PasswordChangeRequest): Response<Unit>

    @PATCH("api/me/verify")
    suspend fun verifyUniversity(@Body request: UniversityVerifyRequest): UniversityVerifyResponse
}