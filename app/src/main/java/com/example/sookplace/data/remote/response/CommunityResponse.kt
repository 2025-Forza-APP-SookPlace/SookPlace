package com.example.sookplace.data.remote.response

import com.google.gson.annotations.SerializedName

//피드조회
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
//    val isBookmarked: Boolean?, TODO 게시글 북마크 기능 삭제
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

//게시물 단건 조회
data class PostDetailResponse(
    @SerializedName("postId") val postId: String,
    @SerializedName("author") val author: Author,
    @SerializedName("category") val category: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String, // 본문 전체
    @SerializedName("place") val place: PlaceInfo,
    @SerializedName("partnership") val partnership: Boolean, // 제휴 여부
    @SerializedName("rating") val rating: Double,
    @SerializedName("images") val images: List<String>, // 이미지 여러 장
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("commentCount") val commentCount: Int,
//    @SerializedName("isBookmarked") val isBookmarked: Boolean, TODO 북마크 삭제 + isLiked 추가
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("displayTime") val displayTime: String,
    @SerializedName("comments") val comments: List<CommentResponse>, // 댓글 리스트

    val likedByMe: Boolean = false //UI용으로 추가
)

data class CommentResponse(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("author") val author: Author,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("displayTime") val displayTime: String
)

//좋아요 생성 및 삭제
data class LikeResponse(
    val postId: String,
    val liked: Boolean,    // 현재 내가 좋아요를 눌렀는지 여부
    val likeCount: Int     // 갱신된 총 좋아요 수
)

//댓글 작성 API
data class WriteCommentRequest(
    @SerializedName("content") val content: String
)

// PostResponse.kt (응답)
data class WriteCommentResponse(
    @SerializedName("commentId") val commentId: String,
    @SerializedName("postId") val postId: String,
    @SerializedName("author") val author: Author,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("displayTime") val displayTime: String,
    @SerializedName("isOwner") val isOwner: Boolean,
    @SerializedName("postCommentCount") val postCommentCount: Int // 게시글 전체 댓글 수 갱신용
)

//게시글 작성 api 응답
data class PostWriteResponse(
    @SerializedName("postId") val postId: String,
    @SerializedName("title") val title: String,
    @SerializedName("contentPreview") val contentPreview: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("images") val images: List<PostImageResponse>,
    @SerializedName("restaurant") val restaurant: RestaurantInfo,
    @SerializedName("author") val author: AuthorInfo,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("commentCount") val commentCount: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("isLiked") val isLiked: Boolean,
    @SerializedName("nextAction") val nextAction: PostWriteNextAction
)

data class PostImageResponse(
    @SerializedName("thumbUrl") val thumbUrl: String,
    @SerializedName("url") val url: String
)

data class RestaurantInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String
)

data class AuthorInfo(
    @SerializedName("id") val id: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("avatarUrl") val avatarUrl: String?
)

data class PostWriteNextAction(
    @SerializedName("detailUrl") val detailUrl: String
)