package com.example.sookplace.data.repository

import com.example.sookplace.data.local.TokenManager
import com.example.sookplace.data.local.dao.UserProfileDao
import com.example.sookplace.data.remote.request.LoginRequest
import com.example.sookplace.data.remote.response.LoginResponse
import com.example.sookplace.data.remote.request.UserSignupRequest
import com.example.sookplace.data.remote.response.UserSignupResponse
import com.example.sookplace.data.remote.api.AuthApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager,
    private val userDao: UserProfileDao
) {
    //회원가입
    suspend fun signup(request: UserSignupRequest): UserSignupResponse {
        return api.signup(request)
    }

    //로그인
    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = api.login(request)
            if (response.isSuccessful) {
                val body = response.body()!!
                tokenManager.saveTokens(body.accessToken, body.refreshToken)

                return Result.success(body)
            } else {
                Result.failure(Exception("로그인 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}