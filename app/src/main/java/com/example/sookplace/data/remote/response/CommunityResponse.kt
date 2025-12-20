package com.example.sookplace.data.remote.response

data class CommunityResponse(
    val content: List<PostContent>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
    val hasNext: Boolean
)

data class PostContent(
    val postId: String,
    val author: Author,
    val category: String,
    val title: String,
    val excerpt: String,
    val place: PlaceInfo,
    val rating: Double,
    val imageUrl: String?,
    val likeCount: Int,
    val commentCount: Int,
    val isBookmarked: Boolean?,
    val createdAt: String,
    val displayTime: String,
    val likedByMe: Boolean
)

data class Author(
    val userId: String,
    val nickname: String,
    val profileImageUrl: String?
)

data class PlaceInfo(
    val placeId: String,
    val name: String
)