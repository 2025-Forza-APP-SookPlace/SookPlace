package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.response.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface UserApi {

    // 사용자 정보 조회
    @GET("me")
    suspend fun getUserMe(): UserMeResponse

    // 퀘스트 현황 및 목표 조회
    @GET("me/quests")
    suspend fun getUserQuests(): UserQuestResponse

    // 현황 리포트 (통계)
    @GET("me/stats")
    suspend fun getUserStats(): UserStatsResponse

    // My Places 조회
    @GET("me/favorites")
    suspend fun getMyPlaces(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String? = null
    ): MyPlaceResponse

    // My Posts 조회
    @GET("me/posts")
    suspend fun getMyPosts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String? = null
    ): MyPostResponse

    // 알림 설정 조회 (GET이 없다면 생략 가능하지만 보통 초기값을 위해 존재)
    @GET("me/settings")
    suspend fun getNotificationSettings(): NotificationSettingResponse

    // 알림 설정 변경 (PATCH)
    @PATCH("me/settings")
    suspend fun updateNotificationSettings(
        @Query("type") type: String, // 예: "push", "like" 등
        @Query("value") value: Boolean
    ): Response<NotificationSettingResponse>
}