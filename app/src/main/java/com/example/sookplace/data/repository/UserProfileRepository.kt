package com.example.sookplace.data.repository

import android.util.Log
import com.example.sookplace.data.local.dao.UserProfileDao
import com.example.sookplace.data.local.entity.UserProfileEntity
import com.example.sookplace.data.remote.api.UserApi
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserProfileRepository @Inject constructor(
    private val userDao: UserProfileDao,
    private val api: UserApi
) {
    val userProfileFlow: Flow<UserProfileEntity?> =
        userDao.getUserProfile()

    //서버에서 최신 프로필 가져와 로컬 업데이트
    suspend fun refreshUserProfile() {
        try {
            // 서버에서 상세 프로필(/me) 정보를 가져옵니다
            val remote = api.getUserMe()
            Log.d("DEBUG_AUTH", "Remote Profile: $remote")
            // /me 응답과 로컬 엔티티의 필드가 동일하므로 별도 mapper 없이 직접 매핑
            val updatedEntity = UserProfileEntity(
                id = remote.id,
                userId = remote.userId,
                nickname = remote.nickname,
                email = remote.email,
                sookVerified = remote.sookVerified,
                level = remote.level,
                levelTitleEn = remote.levelTitleEn,
                nextLevel = remote.nextLevel,
                nextLevelTitleEn = remote.nextLevelTitleEn,
                progressToNextPercent = remote.progressToNextPercent,
                avatarId = remote.avatarId,
                avatarUrl = remote.avatarUrl,
                createdAt = remote.createdAt,
                updatedAt = remote.updatedAt
            )

            //DB에 업데이트
            userDao.upsertUserProfile(updatedEntity)
            Log.d("DEBUG_AUTH", "DB Update Success")
        } catch (e: Exception) {
            Log.e("DEBUG_AUTH", "Refresh Error: ${e.message}")
            e.printStackTrace()
        }
    }

    //유저 프로필 정보 가져오기
    suspend fun getUserProfileOnce(): UserProfileEntity? {
        return userDao.getUserProfileOnce()
    }

    // 필요 시 서버에서 프로필 갱신
    suspend fun refreshIfNeeded() {
        val local = userDao.getUserProfileOnce()
        // 아직 로컬에 프로필이 없으면 한 번만 서버에서 받아옵니다.
        if (local == null) {
            refreshUserProfile()
        }
    }

    suspend fun logout() {
        userDao.clearUserProfile()
    }
}