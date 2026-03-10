package com.example.sookplace.ui.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.PostContent
import com.example.sookplace.data.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {


    /***피드 조회*/
    //상태 저장
    private val _uiState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
    val uiState: StateFlow<CommunityUiState> = _uiState

    //페이지 정보
    private var currentPage = 0
    private var isLastPage = false
    private var isFetching = false

    //기존 데이터 리스트 보관
    private val allPosts = mutableListOf<PostContent>()

    init {
        fetchPosts()
    }

    fun fetchPosts(isRefresh: Boolean = false) {
        if (isFetching || (isLastPage && !isRefresh)) return

        viewModelScope.launch {
            _uiState.value = CommunityUiState.Loading

            isFetching = true
            if (isRefresh) { //새로고침
                currentPage = 0
                isLastPage = false
            }

            try {
                //최신순(createdAt,desc)으로 10개 가져오기
                val response = communityRepository.getCommunityFeed(
                    category = null,
                    sort = "createdAt,desc",
                    page = currentPage,
                    size = 10
                )

                if (isRefresh) { //새로고침 -> 초기화
                    allPosts.clear()
                }

                allPosts.addAll(response.content) //추가된 리스트를 append

                _uiState.value = CommunityUiState.Success(allPosts.toList())

                currentPage++
                isLastPage = !response.hasNext
            } catch (e: Exception) {
                _uiState.value = CommunityUiState.Error(e.message ?: "게시글을 불러오는 데 실패했습니다.")
            } finally {
                isFetching = false
            }
        }
    }

    // 좋아요 토글 로직
    fun toggleLike(postId: String) {
        val currentState = _uiState.value
        if (currentState !is CommunityUiState.Success) return

        //현재 상태 백업 (롤백용) 및 타겟 포스트 찾기
        val previousList = allPosts.toList()
        val index = allPosts.indexOfFirst { it.postId == postId }
        if (index == -1) return

        val targetPost = allPosts[index]
        val isCurrentlyLiked = targetPost.likedByMe

        //[낙관적 업데이트]
        val updatedPost = targetPost.copy(
            likedByMe = !isCurrentlyLiked,
            likeCount = if (isCurrentlyLiked) targetPost.likeCount - 1 else targetPost.likeCount + 1
        )
        allPosts[index] = updatedPost // 로컬 리스트 업데이트
        _uiState.value = CommunityUiState.Success(allPosts.toList()) // UI에 즉시 통보

        //백엔드 API 호출
        viewModelScope.launch {
            try {
                // 리포지토리에 현재 상태를 넘겨서 더미/실제 API 결과 처리
                val result = communityRepository.updatePostLike(postId, isCurrentlyLiked)

                if (result != null) {
                    // 서버 결과로 최종 확정 (만약 서버에서 준 숫자가 다를 경우를 대비)
                    val finalIndex = allPosts.indexOfFirst { it.postId == postId }
                    if (finalIndex != -1) {
                        allPosts[finalIndex] = allPosts[finalIndex].copy(
                            likedByMe = result.liked,
                            likeCount = result.likeCount
                        )
                        _uiState.value = CommunityUiState.Success(allPosts.toList())
                    }
                } else {
                    rollbackLikes(previousList)
                }
            } catch (e: Exception) {
                rollbackLikes(previousList)
            }
        }
    }

    private fun rollbackLikes(previousList: List<PostContent>) {
        allPosts.clear()
        allPosts.addAll(previousList)
        _uiState.value = CommunityUiState.Success(allPosts.toList())
    }


    //UI 상태 정의 (Success 내부에 담긴 데이터는 PostContent 리스트)
    sealed class CommunityUiState {
        object Loading : CommunityUiState()
        data class Success(val posts: List<PostContent>) : CommunityUiState()
        data class Error(val message: String) : CommunityUiState()
    }

}