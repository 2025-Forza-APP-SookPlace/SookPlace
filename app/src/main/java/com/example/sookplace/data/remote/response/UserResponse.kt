package com.example.sookplace.remote.response

// Firestore 문서와 매핑되는 DTO (Data Transfer Object)
// 기본값을 설정해야 Firestore 역직렬화 시 에러가 나지 않습니다.
data class UserResponse(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String? = null
)