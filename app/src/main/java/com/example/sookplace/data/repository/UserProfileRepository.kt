package com.example.sookplace.data.repository

import com.example.sookplace.data.local.dao.UserProfileDao
import com.example.sookplace.data.local.entity.UserProfileEntity
import com.example.sookplace.data.mapper.toEntity
import com.example.sookplace.data.remote.api.UserProfileApi
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserProfileRepository @Inject constructor(
    private val userDao: UserProfileDao,
    private val api: UserProfileApi
) {
    val userProfileFlow: Flow<UserProfileEntity?> =
        userDao.getUserProfile()

    // 서버에서 최신 프로필 가져와 로컬 업데이트
    suspend fun refreshUserProfile() {
        val remote = api.getUserProfile()
        userDao.upsertUserProfile(remote.toEntity())
    }

    // lastUpdated 비교해서 서버 요청할지 결정
    suspend fun refreshIfNeeded() {
        val local = userDao.getUserProfileOnce()
        val now = System.currentTimeMillis()
        val oneHour = 60 * 60 * 1000L

        if (local == null || (now - local.lastUpdated) > oneHour) {
            refreshUserProfile()
        }
    }

    suspend fun logout() {
        userDao.clearUserProfile()
    }
}