package com.example.sookplace.data.remote.auth

import com.example.sookplace.data.local.TokenManager
import com.example.sookplace.data.local.dao.UserProfileDao
import com.example.sookplace.data.remote.api.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

//토큰을 헤더에 자동 추가(로그인 여부를 토큰으로 확인!!)
@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val userDao: UserProfileDao,
    private val authApi: javax.inject.Provider<AuthApi>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        //로그인,회원가입 api는 토큰없이 통과
        if (request.url.encodedPath.contains("auth/login") ||
            request.url.encodedPath.contains("auth/signup")) {
            return chain.proceed(request)
        }

        val accessToken = tokenManager.getAccessToken() ?: return chain.proceed(request)
        val newRequest = if (accessToken != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else request

        // 서버 요청
        val response = chain.proceed(newRequest)

        // 401 Unauthorized → 토큰 없거나 만료됨
        if (response.code == 401) {
            response.close()
            // 토큰 삭제 및 로컬 프로필 초기화
            tokenManager.clearTokens()
            runBlocking {
                userDao.clearUserProfile()
            }
        }

        // 정상 토큰 → 헤더 추가
        request = request.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return response
    }
}