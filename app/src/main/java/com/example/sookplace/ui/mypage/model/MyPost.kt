package com.example.sookplace.ui.mypage.model

data class MyPost(
    val id: Int,
    val title: String,
    val imageRes: Int,
    val restaurantName: String,
    val rating: Double,
    val likes: Int,
    val comments: Int,
    val date: String
)