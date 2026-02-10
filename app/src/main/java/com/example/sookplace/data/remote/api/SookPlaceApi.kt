package com.example.sookplace.data.remote.api

// [중요] 스크린샷에 있는 모델 패키지 경로로 import 합니다.
import com.example.sookplace.ui.mypage.model.MyPlace
import com.example.sookplace.ui.mypage.model.MyPost
import com.example.sookplace.ui.mypage.model.UserProfile
import com.example.sookplace.ui.mypage.model.Quest
import com.example.sookplace.ui.mypage.model.UserStats
import com.example.sookplace.ui.mypage.model.NotificationSettings
import retrofit2.Response
import retrofit2.http.*

interface SookPlaceApi {

    // 1. 사용자 정보 / 퀘스트 / 통계
    @GET("me")
    suspend fun getUserProfile(): Response<UserProfile>

    @GET("me/quests")
    suspend fun getMyQuests(): Response<List<Quest>>

    @GET("me/stats")
    suspend fun getUserStats(): Response<UserStats>

    // 2. 리스트 데이터
    @GET("me/favorites")
    suspend fun getMyPlaces(): Response<List<MyPlace>>

    @GET("me/posts")
    suspend fun getMyPosts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<List<MyPost>>

    // 3. 설정 및 계정 관리
    @PATCH("me/settings")
    suspend fun updateNotificationSettings(@Body settings: NotificationSettings): Response<Void>

    @PATCH("me/profile")
    suspend fun updateNickname(@Body body: Map<String, String>): Response<Void>

    @DELETE("me")
    suspend fun deleteAccount(): Response<Void>

    @POST("auth/logout")
    suspend fun logout(): Response<Void>
}