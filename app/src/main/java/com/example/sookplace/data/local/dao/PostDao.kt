package com.example.sookplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sookplace.data.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface  PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Query("DELETE FROM PostEntity WHERE postId = :postId")
    suspend fun deletePost(postId: String)

    @Query("SELECT * FROM PostEntity")
    fun getAllBookmarkedPosts(): Flow<List<PostEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM PostEntity WHERE postId = :postId)")
    suspend fun isBookmarked(postId: String): Boolean
}