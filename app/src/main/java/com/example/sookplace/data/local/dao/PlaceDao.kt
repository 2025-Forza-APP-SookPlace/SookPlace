package com.example.sookplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sookplace.data.local.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: PlaceEntity)

    @Query("DELETE FROM PlaceEntity WHERE restaurantId = :restaurantId")
    suspend fun deletePlace(restaurantId: Int)

    @Query("SELECT * FROM PlaceEntity ORDER BY addedAt DESC")
    fun getAllSavedPlaces(): Flow<List<PlaceEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM PlaceEntity WHERE restaurantId = :restaurantId)")
    suspend fun isPlaceSaved(restaurantId: Int): Boolean
}