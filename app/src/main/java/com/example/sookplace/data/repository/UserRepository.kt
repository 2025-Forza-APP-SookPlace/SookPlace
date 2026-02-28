package com.example.sookplace.data.repository

import com.example.sookplace.data.remote.api.UserApi
import com.example.sookplace.data.remote.request.NicknameChangeRequest
import com.example.sookplace.data.remote.request.PasswordChangeRequest
import com.example.sookplace.data.remote.request.UniversityVerifyRequest
import com.example.sookplace.data.remote.response.*
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.Response

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    // 기존 마이페이지 기능들
    suspend fun getUserMe(): UserMeResponse = userApi.getUserMe()

    suspend fun getUserQuests(): UserQuestResponse = userApi.getUserQuests()

    suspend fun getUserStats(): UserStatsResponse = userApi.getUserStats()

    suspend fun getMyPlaces(page: Int, size: Int): MyPlaceResponse {
        return userApi.getMyPlaces(page, size, "createdAt,desc")
    }

    suspend fun getMyPosts(page: Int, size: Int): MyPostResponse {
        return userApi.getMyPosts(page, size, "createdAt,desc")
    }

    // [🔥 핵심! 뷰모델의 빨간 줄 에러를 없애줄 새로운 계정 관리 API들]

    // 1. 닉네임 변경
    suspend fun updateNickname(nickname: String): NicknameChangeResponse {
        return userApi.updateNickname(NicknameChangeRequest(nickname))
    }

    // 2. 비밀번호 변경
    suspend fun updatePassword(current: String, newPass: String): Response<Unit> {
        return userApi.updatePassword(PasswordChangeRequest(current, newPass))
    }

    // 3. 숙명인증 (메일 발송 시에는 code가 null, 인증 시에는 code 포함)
    suspend fun verifyUniversity(email: String, code: String? = null): UniversityVerifyResponse {
        return userApi.verifyUniversity(UniversityVerifyRequest(email, code))
    }
}