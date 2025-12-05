package com.example.sookplace.data.remote.request

data class UserSignupRequest(
    val userId: String,
    val nickname: String,
    val email: String,
    val password: String
)