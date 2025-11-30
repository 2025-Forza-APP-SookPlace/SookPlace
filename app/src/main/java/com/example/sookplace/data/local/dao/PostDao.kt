package com.example.sookplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sookplace.data.local.entity.PostEntity

@Dao
interface  PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC LIMIT :limit")
    fun getLatestPosts(limit: Int): List<PostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("DELETE FROM PostEntity WHERE id NOT IN (:keepIds)")
    suspend fun deleteOldPosts(keepIds: List<Int>)
}