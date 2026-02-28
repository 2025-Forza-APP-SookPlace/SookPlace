package com.example.sookplace.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserMeResponse(
    @SerializedName("id") val id: String = "",
    @SerializedName("userId") val userId: String = "",
    @SerializedName("nickname") val nickname: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("sookVerified") val sookVerified: Boolean = false,
    @SerializedName("level") val level: Int = 1,
    @SerializedName("levelTitleEn") val levelTitleEn: String = "",
    @SerializedName("nextLevel") val nextLevel: Int? = null,
    @SerializedName("nextLevelTitleEn") val nextLevelTitleEn: String? = null,
    @SerializedName("progressToNextPercent") val progressToNextPercent: Int = 0,
    @SerializedName("avatarId") val avatarId: String = "",
    @SerializedName("avatarUrl") val avatarUrl: String = "",

    // [중요] 기존 MyPageViewModel에서 사용하던 변수명과의 호환성을 위해 추가
    @SerializedName("profileImageUrl") val profileImageUrl: String = "",

    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String = ""
)