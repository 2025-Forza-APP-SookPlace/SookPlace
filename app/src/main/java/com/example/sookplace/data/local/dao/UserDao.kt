package com.example.sookplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sookplace.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM UserEntity LIMIT 1")
    fun getAllData() : UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(text : UserEntity)

    @Query("DELETE FROM UserEntity")
    fun deleteAllData()
}