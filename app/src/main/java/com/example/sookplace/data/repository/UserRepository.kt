package com.example.sookplace.data.repository

import com.example.sookplace.data.remote.api.UserApi
import com.example.sookplace.data.remote.response.*
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    // 실제 서버 연동
    suspend fun getUserMe(): UserMeResponse = userApi.getUserMe()

    suspend fun getUserQuests(): UserQuestResponse = userApi.getUserQuests()

    suspend fun getUserStats(): UserStatsResponse = userApi.getUserStats()

    suspend fun getMyPlaces(page: Int, size: Int): MyPlaceResponse {
        return userApi.getMyPlaces(page, size, "createdAt,desc")
    }

    suspend fun getMyPosts(page: Int, size: Int): MyPostResponse {
        return userApi.getMyPosts(page, size, "createdAt,desc")
    }
}