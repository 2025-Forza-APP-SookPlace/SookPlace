package com.example.sookplace.data.remote.api

import com.example.sookplace.data.remote.response.CommunityResponse
import com.example.sookplace.data.remote.response.LikeResponse
import com.example.sookplace.data.remote.response.PostDetailResponse
import com.example.sookplace.data.remote.response.PostWriteResponse
import com.example.sookplace.data.remote.response.WriteCommentRequest
import com.example.sookplace.data.remote.response.WriteCommentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityApi {

    //피드 조회api
    @GET("posts")
    suspend fun getCommunityFeed(
        @Query("category") category: String?,
        @Query("sort") sort: String?, // 형식: "createdAt,desc"
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): CommunityResponse

    // 게시글 단건 조회
    @GET("posts/{postId}")
    suspend fun getPostDetail(
        @Path("postId") postId: String
    ): PostDetailResponse

    //좋아요 생성 및 삭제
    @POST("posts/{postId}/likes")
    suspend fun updatePostLike(
        @Path("postId") postId: String
    ): retrofit2.Response<LikeResponse>

    //댓글 작성API
    @POST("posts/{postId}/comments")
    suspend fun postComment(
        @Path("postId") postId: String,
        @Body request: WriteCommentRequest
    ): WriteCommentResponse

    //댓글 삭제 API
    @DELETE("comments/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: String
    ): retrofit2.Response<Unit>

    //게시물 작성 API
    @Multipart
    @POST("posts")
    suspend fun createPost(
        @Part("title") title: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("content") content: RequestBody,
        @Part("restaurantId") restaurantId: RequestBody,
        @Part images: List<MultipartBody.Part>?
    ): Response<PostWriteResponse>

    //게시물 수정 API
    @Multipart
    @PATCH("posts/{postId}")
    suspend fun updatePost(
        @Path("postId") postId: String,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("placeId") placeId: RequestBody,
        @Part("removeImageIds") removeImageIds: List<@JvmSuppressWildcards String>?, // 삭제할 URL 리스트
        @Part images: List<MultipartBody.Part>? // 새로 추가할 이미지 파일 리스트
    ): Response<PostWriteResponse>

    //게시물 삭제
    @DELETE("posts/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: String
    ): Response<Unit>
}