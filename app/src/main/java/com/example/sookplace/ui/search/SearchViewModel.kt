package com.example.sookplace.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.local.dao.PlaceDao
import com.example.sookplace.data.remote.response.RestaurantContent
import com.example.sookplace.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val localDao: PlaceDao
) : ViewModel() {
    private val _searchState = MutableStateFlow<SearchUiState> (SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState //SearchUiState를 계속 관찰 확인

    private enum class SearchMode { KEYWORD, CATEGORY }
    private var currentMode: SearchMode = SearchMode.CATEGORY

    // 현재 검색 조건을 저장할 변수
    private var currentKeyword = ""
    private var currentCategory = "all"
    private var currentSort = "popularity"
    private var currentPage = 0

    //중복 로딩 방지 & 마지막 페이지 체크
    private var isLoading = false
    private var isLastPage = false

    /**
     * 검색창(Search)
     * 사용자가 검색창에 글자를 입력한 후 Enter 했을 때 싱행
     */
    fun searchByKeyword(keyword: String) {
        currentKeyword = keyword
        currentMode = SearchMode.KEYWORD // 검색어로 검색
        currentPage = 0 // 검색어가 바뀌면 첫 페이지부터
        isLastPage = false // 마지막 페이지 여부 초기화

        fetchKeywordData(isLoadMore = false)
    }

    private fun fetchKeywordData(isLoadMore: Boolean) {
        viewModelScope.launch {
            if (!isLoadMore) _searchState.value = SearchUiState.Loading
            isLoading = true

            try {
                // API 호출 (현재 페이지 currentPage 사용)
                val response = searchRepository.searchByKeyword(currentKeyword, currentPage)

                handleSuccess(response.restaurants, isLoadMore)

            } catch (e: Exception) {
                handleError(e, isLoadMore)
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * 정렬(Sort)
     * 사용자가 정렬 조건을 변경 했을 때 실행
     */
    fun fetchSortedList(category: String = currentCategory, sort: String = currentSort) {
        currentCategory = category
        currentSort = sort
        currentMode = SearchMode.CATEGORY
        currentPage = 0
        isLastPage = false

        fetchCategoryData(isLoadMore = false)
    }

    private fun fetchCategoryData(isLoadMore: Boolean) {
        viewModelScope.launch {
            if (!isLoadMore) _searchState.value = SearchUiState.Loading
            isLoading = true

            try {
                // API 호출 (현재 페이지 currentPage 사용)
                val response = searchRepository.getFilteredList(currentCategory, currentSort, currentPage)

                Log.d("UiDebug", "1. ViewModel: 서버 응답 성공! 받아온 식당 개수 = ${response.restaurants.size}")

                handleSuccess(response.restaurants, isLoadMore)

            } catch (e: Exception) {
                Log.e("UiDebug", "1-Error. ViewModel: API 호출 실패 - ${e.message}")
                e.printStackTrace()
                handleError(e, isLoadMore)
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * 무한 스크롤이 바닥에 닿았을 때 호출할 함수
     */
    fun loadNextPage() {
        if (isLoading || isLastPage) return // 로딩 중이거나 끝이면 무시

        currentPage++ // 다음 페이지 번호 증가

        // 현재 모드에 따라 알맞은 API 호출
        when (currentMode) {
            SearchMode.KEYWORD -> fetchKeywordData(isLoadMore = true)
            SearchMode.CATEGORY -> fetchCategoryData(isLoadMore = true)
        }
    }

    private fun handleSuccess(newItems: List<RestaurantContent>, isLoadMore: Boolean) {
        if (newItems.isEmpty()) {
            isLastPage = true
        }

        if (isLoadMore) {
            // 더보기: 기존 리스트 + 새 리스트
            val currentList = (_searchState.value as? SearchUiState.Success)?.list ?: emptyList()
            _searchState.value = SearchUiState.Success(currentList + newItems)
        } else {
            // 첫 검색: 새 리스트만
            _searchState.value = SearchUiState.Success(newItems)
        }
    }

    private fun handleError(e: Exception, isLoadMore: Boolean) {
        if (!isLoadMore) {
            _searchState.value = SearchUiState.Error(e.message ?: "에러 발생")
        } else {
            // 더보기 실패시는 페이지를 다시 돌려놓는게 좋음
            currentPage--
        }
    }

    /**
     * 좋아요 로컬에 저장*/
    fun toggleLike(item: RestaurantContent) {
        viewModelScope.launch {
            val optimisticState = !item.isLiked
            val optimisticCount = if (optimisticState) item.likeCount + 1 else item.likeCount - 1
            updateListUI(item.id, optimisticState, optimisticCount)
            //이 뒤는 백엔드
            try {
                val response = searchRepository.toggleLike(item.id)
                updateListUI(item.id, response.liked, response.likeCount)

            } catch (e: Exception) {
                // 서버 통신 실패 시 UI 복구
                updateListUI(item.id, item.isLiked, item.likeCount)
            }
        }
    }

    private fun updateListUI(restaurantId: Int, isLiked: Boolean, likeCount: Int) {
        val currentState = _searchState.value
        if (currentState is SearchUiState.Success) {
            val newList = currentState.list.map { listItem ->
                if (listItem.id == restaurantId) {
                    listItem.copy(
                        isLiked = isLiked,
                        likeCount = likeCount
                    )
                } else {
                    listItem
                }
            }
            //UI 갱신
            _searchState.value = SearchUiState.Success(newList)
        }
    }
}

sealed class SearchUiState {
    object Idle : SearchUiState() //초기 상태
    object Loading : SearchUiState() //서버에서 데이터를 가져오고있는 상태
    data class Success(val list: List<RestaurantContent>) : SearchUiState() //데이터를 성공적으로 가져온 상태
    data class Error(val message: String) : SearchUiState() //실패한 상태
}