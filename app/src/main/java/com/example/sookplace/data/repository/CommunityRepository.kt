package com.example.sookplace.data.repository

import com.example.sookplace.data.local.dao.PostDao
import com.example.sookplace.data.local.entity.PostEntity
import com.example.sookplace.data.remote.api.CommunityApi
import com.example.sookplace.data.remote.response.Author
import com.example.sookplace.data.remote.response.CommunityResponse
import com.example.sookplace.data.remote.response.PlaceInfo
import com.example.sookplace.data.remote.response.PostContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val communityApi: CommunityApi,
    private val postDao: PostDao
) {
    private val USE_DUMMY = true

    //피드 조회
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
}