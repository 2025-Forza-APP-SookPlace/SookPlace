package com.example.sookplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.sookplace.data.local.entity.FeaturedRestaurantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeaturedRestaurantDao {
    // 전체 목록 가져오기
    @Query("SELECT * FROM featured_restaurants")
    fun getAllFeatured(): Flow<List<FeaturedRestaurantEntity>>

    // 최신 정보로 덮어쓰기
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(restaurants: List<FeaturedRestaurantEntity>)

    // 데이터 전체 삭제
    @Query("DELETE FROM featured_restaurants")
    suspend fun deleteAll()

    // 트랜잭션을 사용해 안전하게 교체
    @Transaction
    suspend fun refreshRestaurants(restaurants: List<FeaturedRestaurantEntity>) {
        deleteAll()
        insertAll(restaurants)
    }
}