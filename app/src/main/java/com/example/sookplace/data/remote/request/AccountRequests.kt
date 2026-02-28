package com.example.sookplace.data.remote.request

import com.google.gson.annotations.SerializedName

// 1. 닉네임 변경 요청
data class NicknameChangeRequest(
    @SerializedName("nickname")
    val nickname: String
)

// 2. 비밀번호 변경 요청
data class PasswordChangeRequest(
    @SerializedName("currentPassword")
    val currentPassword: String,

    @SerializedName("newPassword")
    val newPassword: String
)

// 3. 숙명인증 요청 (메일 발송 시엔 code=null, 인증 시엔 code 포함)
data class UniversityVerifyRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("code")
    val code: String? = null
)