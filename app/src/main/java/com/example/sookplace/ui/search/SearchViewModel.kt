package com.example.sookplace.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.RestaurantContent
import com.example.sookplace.data.remote.response.SortRestaurantItem
import com.example.sookplace.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchState = MutableStateFlow<SearchUiState> (SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState //SearchUiState를 계속 관찰 확인

    // 현재 검색 조건을 저장할 변수
    private var currentKeyword = ""
    private var currentCategory = "all"
    private var currentSort = "popularity"
    private var currentPage = 0

    /**
     * 검색창(Search)
     * 사용자가 검색창에 글자를 입력한 후 Enter 했을 때 싱행
     */
    fun searchByKeyword(keyword: String) {
        currentKeyword = keyword
        currentPage = 0 // 검색어가 바뀌면 첫 페이지부터

        viewModelScope.launch {
            _searchState.value = SearchUiState.Loading
            try {
                val response = searchRepository.searchByKeyword(
                    query = currentKeyword,
                    page = currentPage
                )
                val mappedList = response.content.map { content ->
                    SortRestaurantItem(
                        id = content.id,
                        name = content.name,
                        category = "검색", // 검색 결과에는 카테고리가 없으므로 임시값
                        thumbnailUrl = content.thumbnailUrl,
                        rating = content.rating,
                        likeCount = content.likeCount,
                        isLiked = false, // 기본값
                        distanceMinutesFromCampus = null, // 기본값
                        locationName = content.address,
                        shareUrl = "" // 기본값
                    )
                }
                _searchState.value = SearchUiState.Success(mappedList)
            } catch (e: Exception) {
                _searchState.value = SearchUiState.Error(e.message ?: "검색 결과가 없습니다.")
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
        currentPage = 0

        viewModelScope.launch {
            _searchState.value = SearchUiState.Loading
            try {
                val response = searchRepository.getFilteredList(
                    category = currentCategory,
                    sort = currentSort,
                    page = currentPage
                )

                _searchState.value = SearchUiState.Success(response.restaurants)
            } catch (e: Exception) {
                _searchState.value = SearchUiState.Error(e.message ?: "리스트를 불러오지 못했습니다.")
            }
        }
    }
}

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val list: List<SortRestaurantItem>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}