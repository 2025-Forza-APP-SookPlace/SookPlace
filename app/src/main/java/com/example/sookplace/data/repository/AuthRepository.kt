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
    private val userProfileDao: UserProfileDao
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

    //로그아웃
    suspend fun logout(): Result<Unit> {
        return try {
            val response = api.logout()

            if (!response.isSuccessful) {
                // 서버 응답이 실패하더라도 로컬 로그아웃은 진행해야 하므로 로그만 남김
                android.util.Log.e("AUTH", "Server logout failed with code: ${response.code()}")
            }

            tokenManager.clearTokens()
            userProfileDao.clearUserProfile()
            Result.success(Unit)

//            if (response.isSuccessful) {
//                tokenManager.clearTokens()
//                userProfileDao.clearUserProfile()
//                Result.success(Unit)
//            } else {
//                Result.failure(Exception("로그아웃 실패: ${response.code()}"))
//            }
        }catch (e: Exception){
            android.util.Log.e("AUTH", "Logout exception: ${e.message}")
            tokenManager.clearTokens()
            userProfileDao.clearUserProfile()
            Result.failure(e)
        }
    }
}