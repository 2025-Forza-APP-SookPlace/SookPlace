package com.example.sookplace.data.repository

import com.example.sookplace.data.local.dao.PostDao
import com.example.sookplace.data.local.entity.PostEntity
import com.example.sookplace.data.remote.api.CommunityApi
import com.example.sookplace.data.remote.response.Author
import com.example.sookplace.data.remote.response.CommentResponse
import com.example.sookplace.data.remote.response.CommunityResponse
import com.example.sookplace.data.remote.response.LikeResponse
import com.example.sookplace.data.remote.response.PlaceInfo
import com.example.sookplace.data.remote.response.PostContent
import com.example.sookplace.data.remote.response.PostDetailResponse
import com.example.sookplace.data.remote.response.WriteCommentRequest
import com.example.sookplace.data.remote.response.WriteCommentResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val communityApi: CommunityApi,
    private val postDao: PostDao
) {
    private val USE_DUMMY = true

    /**피드 조회*/
    suspend fun getCommunityFeed(
        category: String?,
        sort: String?,
        page: Int,
        size: Int
    ): CommunityResponse{
        if (USE_DUMMY){
            delay(500)
            return getDummyFeed(page, size)
        }
        return communityApi.getCommunityFeed(category, sort, page, size)
    }

    private fun getDummyFeed(page: Int, size: Int): CommunityResponse {
        val dummyPosts = List(size) { i ->
            val globalIndex = (page * size) + i + 1 // 전체 리스트에서의 인덱스

            PostContent(
                postId = "p_$globalIndex",
                author = Author("u_$globalIndex", "눈송이 $globalIndex", "https://picsum.photos/id/$globalIndex/100/100"),
                category = if (i % 2 == 0) "cafe" else "korean",
                title = "[$page 페이지] 숙대 맛집 탐방 $globalIndex",
                excerpt = "$globalIndex 번째 게시글 내용입니다. 무한 스크롤 테스트 중입니다. 맛집 정보가 가득해요!",
                place = PlaceInfo("pl_$globalIndex", "스머프 식당 $globalIndex"),
                rating = 4.0 + (i % 10) * 0.1,
                imageUrl = if (i % 3 == 0) "https://picsum.photos/id/${globalIndex + 100}/600/400" else null,
                likeCount = 10 + i,
                commentCount = i,
                isBookmarked = false,
                createdAt = "2025-12-20T10:00:00Z",
                displayTime = "${page + 1}일 전",
                likedByMe = i % 2 == 0
            )
        }

        return CommunityResponse(
            content = dummyPosts,
            page = page,
            size = size,
            totalElements = 50,
            totalPages = 5,
            hasNext = page < 4
        )
    }

    //북마크된 게시물 저장(마이 포스트)
    suspend fun saveBookmark(post: PostEntity) = postDao.insertPost(post)
    suspend fun removeBookmark(postId: String) = postDao.deletePost(postId)
    fun getBookmarks(): Flow<List<PostEntity>> = postDao.getAllBookmarkedPosts()


    /**단건 게시물 조회*/
    suspend fun getPostDetail(postId: String): PostDetailResponse {
        if (USE_DUMMY) {
            delay(500)
            val index = postId.replace("p_", "").toIntOrNull() ?: 1
            return getDummyPostDetail(postId, index)
        }
        return communityApi.getPostDetail(postId)
    }

    private fun getDummyPostDetail(postId: String, index: Int): PostDetailResponse {
        // 피드 리스트의 로직과 일치하도록 속성값 설정
        return PostDetailResponse(
            postId = postId,
            author = Author("u_$index", "눈송이 $index", "https://picsum.photos/id/$index/100/100"),
            category = if (index % 2 != 0) "cafe" else "korean",
            title = "숙대 맛집 탐방 $index",
            content = "이 게시글은 $index 번째로 작성된 맛집 탐방기입니다.\n" +
                    "실제로 상세 조회를 하면 본문 전체 내용을 볼 수 있습니다.\n" +
                    "오늘 갔던 곳은 정말 최고였어요! 분위기부터 맛까지 모든 게 완벽했습니다.",
            place = PlaceInfo("pl_$index", "스머프 식당 $index"),
            partnership = index % 5 == 0, // 5의 배수 게시글은 제휴 업체로 설정
            rating = 4.0 + (index % 10) * 0.1,
            // 리스트에서 썼던 이미지와 상세 페이지의 첫 이미지를 일치시킴
            images = listOf(
                "https://picsum.photos/id/${index + 100}/600/400",
                "https://picsum.photos/id/${index + 101}/600/400",
                "https://picsum.photos/id/${index + 102}/600/400"
            ),
            likeCount = 10 + index,
            commentCount = 2,
            isBookmarked = false,
            createdAt = "2025-12-20T10:00:00Z",
            displayTime = "1시간 전",
            comments = listOf(
                CommentResponse("c_01", Author("u_100", "프로맛집러", null), "오 여기 $index 번째 글인데도 퀄리티 좋네요!", "2025-12-20T11:00:00Z", "10분 전"),
                CommentResponse("c_02", Author("u_101", "배고픈송이", null), "사진 보니까 저도 가고 싶어져요ㅠㅠ", "2025-12-20T11:05:00Z", "5분 전")
            )
        )
    }

    /**댓글 작성*/
    suspend fun postComment(postId: String, content: String): WriteCommentResponse {
        if (USE_DUMMY) {
            delay(500)
            return WriteCommentResponse(
                commentId = "c_${System.currentTimeMillis()}",
                postId = postId,
                author = Author("me", "눈송이_나", "https://picsum.photos/id/50/100/100"),
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

}