package com.example.sookplace.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sookplace.data.local.dao.PostDao
import com.example.sookplace.data.local.dao.RestaurantDao
import com.example.sookplace.data.local.entity.RestaurantEntity

@Database(
    entities = [
        UserEntity::class,
        RestaurantEntity::class,
        PostDao::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun restaurantDao() : RestaurantDao
    abstract fun postDao() : PostDao

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