package com.example.sookplace.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sookplace.data.local.dao.FeaturedRestaurantDao
import com.example.sookplace.data.local.dao.PostDao
import com.example.sookplace.data.local.dao.RestaurantDao
import com.example.sookplace.data.local.dao.UserProfileDao
import com.example.sookplace.data.local.entity.FeaturedRestaurantEntity
import com.example.sookplace.data.local.entity.PostEntity
import com.example.sookplace.data.local.entity.RestaurantEntity
import com.example.sookplace.data.local.entity.UserProfileEntity

@Database(
    entities = [
        UserProfileEntity::class,
        RestaurantEntity::class,
        PostEntity::class,
        FeaturedRestaurantEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserProfileDao
    abstract fun restaurantDao() : RestaurantDao
    abstract fun postDao() : PostDao
    abstract fun featuredRestaurantDao(): FeaturedRestaurantDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database.db"
                ).build().also {
                    instance = it
                }
            }
    }
}