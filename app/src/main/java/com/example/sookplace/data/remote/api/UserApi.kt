package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.response.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface UserApi {

    // 모든 경로 앞에 "api/"를 추가하여 수정합니다.

    // 사용자 정보 조회
    @GET("api/me")
    suspend fun getUserMe(): UserMeResponse

    // 퀘스트 현황 및 목표 조회
    @GET("api/me/quests")
    suspend fun getUserQuests(): UserQuestResponse

    // 현황 리포트 (통계)
    @GET("api/me/stats")
    suspend fun getUserStats(): UserStatsResponse

    // My Places 조회
    @GET("api/me/favorites")
    suspend fun getMyPlaces(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String? = null
    ): MyPlaceResponse

    // My Posts 조회
    @GET("api/me/posts")
    suspend fun getMyPosts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String? = null
    ): MyPostResponse

    // 알림 설정 조회
    @GET("api/me/settings")
    suspend fun getNotificationSettings(): NotificationSettingResponse

    // 알림 설정 변경
    @PATCH("api/me/settings")
    suspend fun updateNotificationSettings(
        @Query("type") type: String,
        @Query("value") value: Boolean
    ): Response<NotificationSettingResponse>
}