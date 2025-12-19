package com.example.sookplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sookplace.data.local.entity.UserPreferenceEntity

@Dao
interface UserPreferenceDao {
    @Query("SELECT * FROM user_preferences WHERE restaurantId = :id")
    suspend fun getPreferenceById(id: Int): UserPreferenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(userPreference: UserPreferenceEntity)

    @Query("DELETE FROM USER_PREFERENCES WHERE restaurantId = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM user_preferences")
    suspend fun deleteAllData()
}