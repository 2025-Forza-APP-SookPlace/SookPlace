package com.example.sookplace.data.remote.response

import com.google.gson.annotations.SerializedName

// 1. 닉네임 변경 응답
data class NicknameChangeResponse(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("createdAt")
    val createdAt: String
)

// 2. 숙명인증 응답
data class UniversityVerifyResponse(
    @SerializedName("verified")
    val verified: Boolean,
    @SerializedName("verifiedAt")
    val verifiedAt: String,
    @SerializedName("createdAt")
    val createdAt: String
)

// ※ 비밀번호 변경(Password)은 보통 응답 데이터가 없으므로 Response<Unit>으로 처리합니다.