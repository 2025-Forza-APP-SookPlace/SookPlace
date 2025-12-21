package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.request.LoginRequest
import com.example.sookplace.data.remote.response.LoginResponse
import com.example.sookplace.data.remote.request.UserSignupRequest
import com.example.sookplace.data.remote.response.UserSignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    //회원가입
    @POST("auth/signup")
    suspend fun signup(
        @Body request: UserSignupRequest
    ): UserSignupResponse

    //로그인
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

}