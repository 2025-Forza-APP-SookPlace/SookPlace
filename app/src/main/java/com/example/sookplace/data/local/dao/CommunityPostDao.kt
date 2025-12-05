package com.example.sookplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sookplace.data.local.entity.CommunityPostEntity

@Dao
interface CommunityPostDao {
    @Query("SELECT * FROM community_post ORDER BY createdAt DESC")
    fun pagingSource(): PagingSource<Int, CommunityPostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<CommunityPostEntity>)
}