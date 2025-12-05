package com.example.sookplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sookplace.data.local.entity.RestaurantEntity

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM RestaurantEntity")
    fun getAllData() : List<RestaurantEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurants: List<RestaurantEntity>)

    @Query("DELETE FROM RestaurantEntity")
    fun deleteAllData()
}