package com.example.sookplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sookplace.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    // 1) 단일 프로필을 Flow로 가져오기 (UI 자동 업데이트용)
    @Query("SELECT * FROM user_profile LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    // 2) 단일 프로필 가져오기 (비동기, 1회 조회)
    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    // 3) Upsert: userId가 같으면 update, 없으면 insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserProfile(profile: UserProfileEntity)

    // 4) 로그아웃 시 전체 삭제
    @Query("DELETE FROM user_profile")
    suspend fun clearUserProfile()
}