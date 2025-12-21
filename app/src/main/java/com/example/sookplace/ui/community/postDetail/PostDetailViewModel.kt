package com.example.sookplace.ui.community.postDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.Author
import com.example.sookplace.data.remote.response.CommentResponse
import com.example.sookplace.data.remote.response.PostDetailResponse
import com.example.sookplace.data.repository.CommunityRepository
import com.example.sookplace.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    //게시물 상세 정보 상태
    private val _postDetail = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val postDetail: StateFlow<DetailUiState> = _postDetail

    //게시물 삭제 상태
    private val _deleteState = MutableStateFlow<DeleteUiState>(DeleteUiState.Idle)
    val deleteState: StateFlow<DeleteUiState> = _deleteState

    private var initialLikedByMe: Boolean = false

    fun setInitialLikeState(isLiked: Boolean) {
        initialLikedByMe = isLiked
    }

    fun getPostDetail(postId: String) { //게시물 상세 젇보 불러오기
        viewModelScope.launch {
            _postDetail.value = DetailUiState.Loading
            try {
                val response = communityRepository.getPostDetail(postId)
                val dataWithLikeStatus = response.copy(likedByMe = initialLikedByMe)
                _postDetail.value = DetailUiState.Success(dataWithLikeStatus)
            } catch (e: Exception) {
                _postDetail.value = DetailUiState.Error(e.message ?: "로딩 실패")
            }
        }
    }

    //사용자 아이디(본인) //TODO: 댓글 닉네임으로 본인 비교
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId

    fun fetchCurrentUserId() {
        viewModelScope.launch {
            val profile = userProfileRepository.getUserProfileOnce()
            _currentUserId.value = profile?.nickname
        }
    }

    //좋아요 생성 및 삭제
    fun toggleLike(postId: String) {
        val currentState = _postDetail.value
        if (currentState !is DetailUiState.Success) return

        // 현재 상태 저장 (통신 실패 시 복구용)
        val previousData = currentState.data
        val isCurrentlyLiked = previousData.likedByMe

        // UI 먼저 업데이트 (낙관적 업데이트)
        val optimisticData = previousData.copy(
            likedByMe = !isCurrentlyLiked,
            likeCount = if (isCurrentlyLiked) previousData.likeCount - 1 else previousData.likeCount + 1
        )
        _postDetail.value = DetailUiState.Success(optimisticData)

        viewModelScope.launch {
            try {
                val result = communityRepository.updatePostLike(postId, isCurrentlyLiked)

                if (result != null) {
                    // 서버 결과가 성공하면 서버에서 준 진짜 데이터로 최종 업데이트
                    val finalData = previousData.copy(
                        likedByMe = result.liked,
                        likeCount = result.likeCount
                    )
                    _postDetail.value = DetailUiState.Success(finalData)
                } else {
                    // 실패 시 롤백
                    _postDetail.value = DetailUiState.Success(previousData)
                }
            } catch (e: Exception) {
                Log.e("PostDetailViewModel", "좋아요 업데이트 실패, 롤백 수행: ${e.message}")
                _postDetail.value = DetailUiState.Success(previousData)
            }
        }
    }

    //댓글 작성
    fun submitComment(postId: String, content: String) {
        val currentState = _postDetail.value
        if (currentState !is DetailUiState.Success) return
        val previousData = currentState.data

        viewModelScope.launch {
            try {
                val response = communityRepository.postComment(postId, content)

                val newComment = CommentResponse(
                    commentId = response.commentId,
                    author = response.author,
                    content = response.content,
                    createdAt = response.createdAt,
                    displayTime = response.displayTime
                )

                // 기존 리스트 + 새 댓글
                val updatedComments =
                    previousData.comments.toMutableList().apply { add(0, newComment) }

                _postDetail.value = DetailUiState.Success(
                    previousData.copy(
                        comments = updatedComments,
                        commentCount = response.postCommentCount
                    )
                )
                Log.d("CommentSubmit", "UI 상태 업데이트 완료")
            } catch (e: Exception) {
                Log.e("CommentSubmit", "댓글 등록 실패 에러: ${e.message}")

                val fakeComment = CommentResponse( //TODO: 서버 연결 실패시에 추가되는 가짜 더미 데이터
                    commentId = "fake_${System.currentTimeMillis()}",
                    author = Author(userId="", nickname = _currentUserId.value ?: "나", profileImageUrl = ""),
                    content = content,
                    createdAt = "방금 전",
                    displayTime = "방금 전"
                )

                val updatedComments = previousData.comments.toMutableList().apply { add(0, fakeComment) }
                _postDetail.value = DetailUiState.Success(
                    previousData.copy(comments = updatedComments, commentCount = previousData.commentCount + 1)
                )
            }
        }
    }

    //댓글 삭제
    fun deleteComment(commentId: String) {
        viewModelScope.launch {
            try {
                // commentId만 전달
                val isSuccess = communityRepository.deleteComment(commentId)

                if (isSuccess) {
                    val currentState = _postDetail.value
                    if (currentState is DetailUiState.Success) {
                        // 서버 응답 성공 후, 로컬 리스트에서 해당 ID만 필터링하여 제거
                        val updatedComments = currentState.data.comments.filter {
                            it.commentId != commentId
                        }

                        // UI 상태 업데이트 및 댓글 카운트 감소
                        val updatedData = currentState.data.copy(
                            comments = updatedComments,
                            commentCount = currentState.data.commentCount - 1
                        )
                        _postDetail.value = DetailUiState.Success(updatedData)
                    }
                }
            } catch (e: Exception) {
                Log.e("PostDetailViewModel", "댓글 삭제 실패: ${e.message}")
            }
        }
    }

    //게시글 삭제
    fun deletePost(postId: String) {
        viewModelScope.launch {
            _deleteState.value = DeleteUiState.Loading
            try {
                val response = communityRepository.deletePost(postId)
                if (response.isSuccessful) {
                    _deleteState.value = DeleteUiState.Success
                } else {
                    _deleteState.value = DeleteUiState.Error("삭제 권한이 없거나 오류가 발생했습니다.")
                }
            } catch (e: Exception) {
                _deleteState.value = DeleteUiState.Error(e.message ?: "네트워크 오류 발생")
            }
        }
    }

    sealed class DetailUiState {
        object Loading : DetailUiState()
        data class Success(val data: PostDetailResponse) : DetailUiState()
        data class Error(val message: String) : DetailUiState()
    }

    sealed class DeleteUiState {
        object Idle : DeleteUiState()
        object Loading : DeleteUiState()
        object Success : DeleteUiState()
        data class Error(val message: String) : DeleteUiState()
    }
}