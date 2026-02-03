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

    private val USE_DUMMY = false //디버깅용

    //서버에서 최신 프로필 가져와 로컬 업데이트
    suspend fun refreshUserProfile() {
        try {
            //현재 로컬에 저장된 정보를 가져옵니다 (로그인 시 저장된 ID를 쓰기 위함)
            val localUser = userDao.getUserProfileOnce()
            if (localUser == null) return

            //서버에서 상세 프로필(/me) 정보를 가져옵니다
            val remote = api.getUserProfile()
            val updatedEntity = remote.toEntity(localUser.id)

            //DB에 업데이트
            userDao.upsertUserProfile(updatedEntity)

        } catch (e: Exception) {
            e.printStackTrace()
        }
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