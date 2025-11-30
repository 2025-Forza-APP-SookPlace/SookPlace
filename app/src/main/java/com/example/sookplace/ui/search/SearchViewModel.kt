package com.example.sookplace.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.local.db.AppDatabase
import com.example.sookplace.data.local.entity.RestaurantEntity
import com.example.sookplace.data.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel (application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>().applicationContext
    val db = AppDatabase.getDatabase(context)

    // 테스트/샘플용 데이터 삽입
    fun insertSampleUserData() = viewModelScope.launch(Dispatchers.IO) {
        // User 샘플
        val user = UserEntity(
            id = 1,
            email = "test@example.com",
            nickname = "맛집탐험가 송이",
            level = 1
        )
        db.userDao().insert(user)
    }

    fun insertSampleRestaurants() = viewModelScope.launch(Dispatchers.IO) {
        val restaurants = listOf(
            RestaurantEntity(
                id = 1,
                name = "피자 마루",
                category = "양식",
                address = "숙명여대 후문",
                avgRating = 4.7,
                likeCount = 156,
                distanceMinutesFromCampus = 7
            ),
            RestaurantEntity(
                id = 2,
                name = "달콤 디저트",
                category = "디저트",
                address = "숙명여대 앞 골목",
                avgRating = 4.6,
                likeCount = 156,
                distanceMinutesFromCampus = 6
            ),
            RestaurantEntity(
                id = 3,
                name = "숙명 한식당",
                category = "한식",
                address = "숙명여대 정문 앞",
                avgRating = 4.5,
                likeCount = 128,
                distanceMinutesFromCampus = 3
            ),
            RestaurantEntity(
                id = 4,
                name = "치킨 매니아",
                category = "치킨",
                address = "용산구 청파로",
                avgRating = 4.4,
                likeCount = 92,
                distanceMinutesFromCampus = 4
            ),
            RestaurantEntity(
                id = 5,
                name = "캠퍼스 카페",
                category = "카페",
                address = "숙명여대 학생회관",
                avgRating = 4.2,
                likeCount = 89,
                distanceMinutesFromCampus = 5
            ),
            RestaurantEntity(
                id = 6,
                name = "분식나라",
                category = "분식",
                address = "Seoul, Hongdae",
                avgRating = 4.3,
                likeCount = 67,
                distanceMinutesFromCampus = 2
            )
        )
        db.restaurantDao().insert(restaurants)
    }

    fun getData() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("HomeViewModel", db.userDao().getAllData().toString())
        Log.d("HomeViewModel", db.restaurantDao().getAllData().toString())
    }
}