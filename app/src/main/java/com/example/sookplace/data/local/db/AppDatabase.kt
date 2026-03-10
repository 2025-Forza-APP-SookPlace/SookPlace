package com.example.sookplace.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sookplace.data.local.dao.FeaturedRestaurantDao
import com.example.sookplace.data.local.dao.RestaurantDao
import com.example.sookplace.data.local.dao.PlaceDao
import com.example.sookplace.data.local.dao.UserProfileDao
import com.example.sookplace.data.local.entity.FeaturedRestaurantEntity
import com.example.sookplace.data.local.entity.RestaurantEntity
import com.example.sookplace.data.local.entity.PlaceEntity
import com.example.sookplace.data.local.entity.UserProfileEntity

@Database(
    entities = [
        UserProfileEntity::class,
        RestaurantEntity::class,
        FeaturedRestaurantEntity::class,
        PlaceEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserProfileDao
    abstract fun restaurantDao() : RestaurantDao
    abstract fun featuredRestaurantDao(): FeaturedRestaurantDao
    abstract fun userPreferenceDao(): PlaceDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sookplace_db" // [수정] NetworkModule과 이름을 "sookplace_db"로 통일했습니다.
                )
                    .fallbackToDestructiveMigration() // [중요] 버전 변경 시 기존 데이터 초기화 (앱 죽음 방지)
                    .build().also {
                        instance = it
                    }
            }
    }
}