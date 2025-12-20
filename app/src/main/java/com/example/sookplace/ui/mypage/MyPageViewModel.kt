package com.example.sookplace.ui.mypage

import com.example.sookplace.R

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sookplace.ui.mypage.model.*

class MyPageViewModel : ViewModel() {

    private val _myPlaces = MutableLiveData<List<MyPlace>>()
    val myPlaces: LiveData<List<MyPlace>> = _myPlaces

    private val _myPosts = MutableLiveData<List<MyPost>>()
    val myPosts: LiveData<List<MyPost>> = _myPosts

    fun loadMyPage() {

        // 🔹 My Places (2개만)
        _myPlaces.value = listOf(
            MyPlace(
                id = 1,
                name = "속성 한상",
                category = "한식",
                rating = 4.5,
                imageRes = R.drawable.food_img
            ),
            MyPlace(
                id = 2,
                name = "피자마루",
                category = "양식",
                rating = 4.2,
                imageRes = R.drawable.food_img2
            )
        )

        // 🔹 My Posts (2개만)
        _myPosts.value = listOf(
            MyPost(
                id = 1,
                title = "여기 진짜 맛있어요!",
                imageRes = R.drawable.food_img,
                restaurantName = "속성 한상",
                rating = 4.5,
                likes = 12,
                comments = 3,
                date = "2024.12.18"
            ),
            MyPost(
                id = 2,
                title = "재방문 의사 있음",
                imageRes = R.drawable.food_img2,
                restaurantName = "피자마루",
                rating = 4.0,
                likes = 8,
                comments = 1,
                date = "2024.12.15"
            )
        )
    }
}