package com.example.sookplace.ui.mypage.model

data class Quest(
    val id: Int,
    val title: String,
    val progress: Int,
    val goal: Int,
    val description: String,
    val rewardExp: Int
)