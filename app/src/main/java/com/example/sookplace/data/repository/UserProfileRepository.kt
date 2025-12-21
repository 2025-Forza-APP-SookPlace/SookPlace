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

    private val USE_DUMMY = true //디버깅용

    // 서버에서 최신 프로필 가져와 로컬 업데이트
    suspend fun refreshUserProfile() {
        if (USE_DUMMY) {
            // 더미 데이터
            val dummy = UserProfileEntity(
                nickname = "눈송이_나",
                level = 3,
                levelTitle = "청소년송이",
                avatarUrl = "https://m.blog.naver.com/ambitiones/221438972285",
                lastUpdated = System.currentTimeMillis()
            )
            userDao.upsertUserProfile(dummy)
            return
        }

        val remote = api.getUserProfile()
        userDao.upsertUserProfile(remote.toEntity())
    }

    //유저 프로필 정보 가져오기
    suspend fun getUserProfileOnce(): UserProfileEntity? {
        return userDao.getUserProfileOnce()
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