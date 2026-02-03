package com.example.sookplace.data.remote.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int,
    val user: LoginUser
)

data class LoginUser(
    val id: String, //내부 식별자
    val userId: String,
    val email: String,
    val lastLoginAt: String
)
