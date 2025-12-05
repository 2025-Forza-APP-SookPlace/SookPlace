package com.example.sookplace.data.remote.response

data class UserProfileResponse(
    val nickname: String,
    val level : Int,
    val levelTitle: String,
    val avatarUrl: String
)