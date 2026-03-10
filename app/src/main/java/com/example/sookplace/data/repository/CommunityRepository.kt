package com.example.sookplace.data.repository

import com.example.sookplace.data.remote.api.CommunityApi
import com.example.sookplace.data.remote.response.Author
import com.example.sookplace.data.remote.response.CommentResponse
import com.example.sookplace.data.remote.response.CommunityResponse
import com.example.sookplace.data.remote.response.LikeResponse
import com.example.sookplace.data.remote.response.PlaceInfo
import com.example.sookplace.data.remote.response.PostContent
import com.example.sookplace.data.remote.response.PostDetailResponse
import com.example.sookplace.data.remote.response.PostWriteResponse
import com.example.sookplace.data.remote.response.WriteCommentRequest
import com.example.sookplace.data.remote.response.WriteCommentResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val communityApi: CommunityApi
) {
    private val USE_DUMMY = false

    private val adminAuthor = Author(
        userId = "admin",
        nickname = "숙플관리자",
        profileImageUrl = "https://picsum.photos/id/1/200/200"
    )
    private val normalAuthor = Author(
        userId = "user_123",
        nickname = "눈송이",
        profileImageUrl = "https://picsum.photos/id/2/200/200"
    )
    private val dummyPlace = PlaceInfo(
        placeId = "place_1",
        name = "숙대 맛집 마라탕"
    )

    /**피드 조회*/
    suspend fun getCommunityFeed(
        category: String?,
        sort: String?,
        page: Int,
        size: Int
    ): CommunityResponse{
        if (USE_DUMMY) {
            delay(500)

            val dummyPosts = listOf(
                PostContent(
                    postId = "post_admin",
                    author = adminAuthor,
                    category = "공지사항",
                    title = "[필독] 숙플 커뮤니티 이용 가이드",
                    excerpt = "커뮤니티 이용 가이드입니다. 클린한 게시판을 만들어주세요.", // content -> excerpt
                    place = dummyPlace, // 추가됨
                    rating = 5.0,       // 추가됨
                    imageUrl = "https://picsum.photos/id/3/300/300", // thumbnailUrl -> imageUrl
                    likeCount = 99,
                    commentCount = 2,
                    createdAt = "2026-02-24T10:00:00Z",
                    displayTime = "1시간 전",
                    likedByMe = false   // 추가됨
                ),
                PostContent(
                    postId = "post_normal",
                    author = normalAuthor,
                    category = "맛집추천",
                    title = "여기 마라탕 진짜 맛있어요 ㅠㅠ",
                    excerpt = "점심에 먹고 왔는데 눈물나는 맛입니다. 꼭 가보세요!",
                    place = dummyPlace,
                    rating = 4.5,
                    imageUrl = null,
                    likeCount = 15,
                    commentCount = 1,
                    createdAt = "2026-02-24T10:30:00Z",
                    displayTime = "30분 전",
                    likedByMe = true
                )
            )

            // 수정됨: totalElements, totalPages 추가
            return CommunityResponse(
                content = dummyPosts,
                page = page,
                size = size,
                totalElements = 2,
                totalPages = 1,
                hasNext = false
            )
        }
        return communityApi.getCommunityFeed(category, sort, page, size)
    }


    /**단건 게시물 조회*/
    suspend fun getPostDetail(postId: String): PostDetailResponse {
        if (USE_DUMMY) {
            delay(500)

            val isAdminPost = postId == "post_admin"
            val currentAuthor = if (isAdminPost) adminAuthor else normalAuthor

            return PostDetailResponse(
                postId = postId,
                author = currentAuthor,
                category = if (isAdminPost) "공지사항" else "맛집추천",
                title = if (isAdminPost) "[필독] 숙플 커뮤니티 이용 가이드" else "여기 마라탕 진짜 맛있어요 ㅠㅠ",
                content = "이것은 ${currentAuthor.nickname}님이 작성한 게시글의 상세 본문입니다.\n\n테스트를 위한 더미 데이터입니다.",
                place = dummyPlace,
                partnership = isAdminPost,
                rating = if (isAdminPost) 5.0 else 4.5,
                images = listOf("https://picsum.photos/id/10/500/500", "https://picsum.photos/id/11/500/500"),
                likeCount = if (isAdminPost) 99 else 15,
                commentCount = 2,
                createdAt = "2026-02-24T10:00:00Z",
                displayTime = if (isAdminPost) "1시간 전" else "30분 전",
                likedByMe = !isAdminPost,
                comments = listOf(
                    CommentResponse(
                        commentId = "comment_1",
                        author = normalAuthor, // 일반 유저의 댓글
                        content = "우와 좋은 정보 감사합니다!",
                        createdAt = "2026-02-24T10:10:00Z",
                        displayTime = "50분 전"
                    ),
                    CommentResponse(
                        commentId = "comment_2",
                        author = adminAuthor, // 관리자의 댓글
                        content = "참고로 주말에는 휴무라고 하네요.",
                        createdAt = "2026-02-24T10:15:00Z",
                        displayTime = "45분 전"
                    )
                )
            )
        }
        return communityApi.getPostDetail(postId)
    }

    /**댓글 작성*/
    suspend fun postComment(postId: String, content: String): WriteCommentResponse {
        if (USE_DUMMY) {
            delay(500)
            return WriteCommentResponse(
                commentId = "c_${System.currentTimeMillis()}",
                postId = postId,
                author = Author("me", "눈송이 1", "https://picsum.photos/id/50/100/100"),
                content = content,
                createdAt = "2025-12-20T22:00:00Z",
                displayTime = "방금 전",
                isOwner = true,
                postCommentCount = 13 // 기존 12개에서 하나 늘어난 상태 가정
            )
        }
        return communityApi.postComment(postId, WriteCommentRequest(content))
    }

    /**좋아요 생성 및 삭제*/
    suspend fun updatePostLike(postId: String, currentStatus: Boolean): LikeResponse? {
        if (USE_DUMMY) {
            delay(300)
            val nextStatus = !currentStatus
            return LikeResponse(
                postId = postId,
                liked = nextStatus,
                likeCount = if (nextStatus) 11 else 10
            )
        }
        val response = communityApi.updatePostLike(postId)
        return if (response.isSuccessful) response.body() else null
    }

    /**댓글 삭제*/
    suspend fun deleteComment(commentId: String): Boolean {
        if (USE_DUMMY) {
            delay(300)
            return true
        }
        val response = communityApi.deleteComment(commentId)
        return response.isSuccessful
    }

    /**게시글 등록*/
    suspend fun createPost(
        title: RequestBody,
        rating: RequestBody,
        content: RequestBody,
        restaurantId: RequestBody,
        images: List<MultipartBody.Part>?
    ): Response<PostWriteResponse> {
        return communityApi.createPost(title, rating, content, restaurantId, images)
    }

    /**게시글 수정*/
    suspend fun updatePost(
        postId: String,
        title: RequestBody,
        rating: RequestBody,
        content: RequestBody,
        placeId: RequestBody,
        removeImageIds: List<String>?,
        images: List<MultipartBody.Part>?
    ): Response<PostWriteResponse> {
        return communityApi.updatePost(postId, title, content, rating, placeId, removeImageIds, images)
    }

    /**게시글 삭제*/
    suspend fun deletePost(postId: String): Response<Unit> {
        return communityApi.deletePost(postId)
    }

}