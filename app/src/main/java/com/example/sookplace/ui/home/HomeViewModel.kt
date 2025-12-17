package com.example.sookplace.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.Category
import com.example.sookplace.data.remote.response.NextAction
import com.example.sookplace.data.remote.response.Restaurant
import com.example.sookplace.data.remote.response.RestaurantItem
import com.example.sookplace.data.remote.response.RouletteSpinResponse
import com.example.sookplace.data.repository.RestaurantRepository
import com.example.sookplace.data.repository.RouletteRepository
import com.example.sookplace.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val restaurantRepository: RestaurantRepository,
    private val rouletteRepository: RouletteRepository
) : ViewModel() {
    //프로필
    val userProfile = userProfileRepository.userProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            userProfileRepository.refreshIfNeeded()
        }
    }

    //오늘의 숙플레이스
    private val _featuredRestaurants = MutableStateFlow<List<RestaurantItem>>(emptyList())
    val featuredRestaurants: StateFlow<List<RestaurantItem>> get() = _featuredRestaurants

    private val USE_DUMMY = true //디버깅용

    fun loadFeaturedRestaurants() {
        viewModelScope.launch {
            if (USE_DUMMY) { //디버깅용
                _featuredRestaurants.value = listOf(
                    RestaurantItem(
                        name = "캠퍼스 카페",
                        address = "숙명여대 학생회관",
                        thumbnailUrl = "https://via.placeholder.com/300",
                        isLiked = true
                    ),
                    RestaurantItem(
                        name = "청파김밥",
                        address = "서울 용산구 청파로",
                        thumbnailUrl = "https://via.placeholder.com/300",
                        isLiked = false
                    )
                )
                return@launch
            }

            try {
                val list = restaurantRepository.getFeaturedRestaurants()
                _featuredRestaurants.value = list
            } catch (e: Exception) {
                // 오류 처리: Toast, Log, State 업데이트 등
            }
        }
    }

    //룰렛 돌리기
    private val _excludedRestaurantIds = mutableListOf<Int>()

    private val _rouletteState = MutableStateFlow<RouletteUiState>(RouletteUiState.Idle)
    val rouletteState: StateFlow<RouletteUiState> = _rouletteState

    fun spinRoulette(mode: String) {
        viewModelScope.launch {
            _rouletteState.value = RouletteUiState.Loading // 로딩 시작

            delay(2000) //가짜 로딩 시간//TODO: 백엔드와 연결 후 삭제할 것
            val dummyResponse = when (mode) {
                "category" -> {
                    // 카테고리 결과 모드일 때
                    RouletteSpinResponse(
                        type = "CATEGORY",
                        restaurant = null,
                        category = Category(key = "korean", name = "한식"),
                        nextAction = NextAction(detailUrl = "", searchUrl = "")
                    )
                }

                else -> {
                    // 식당 결과 모드일 때 (myplace, top20)
                    RouletteSpinResponse(
                        type = "RESTAURANT",
                        restaurant = Restaurant(
                            id = 1,
                            name = "숙대 앞 맛집 (더미)",
                            category = "일식",
                            thumbnailUrl = "https://picsum.photos/400/300", // 랜덤 이미지
                            rating = 4.8,
                            distanceMinutesFromCampus = 5
                        ),
                        category = null,
                        nextAction = NextAction(detailUrl = "https://example.com", searchUrl = "")
                    )
                }
            }

            _rouletteState.value = RouletteUiState.Success(dummyResponse)
            dummyResponse.restaurant?.let { _excludedRestaurantIds.add(it.id) }

            try {
                val response = rouletteRepository.spin(mode, _excludedRestaurantIds)
                _rouletteState.value = RouletteUiState.Success(response)

                response.restaurant?.let { //중복 제거 식당 리스트
                    _excludedRestaurantIds.add(it.id)
                }
            } catch (e: Exception) {
                _rouletteState.value = RouletteUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    // 상태 초기화 (결과창을 닫거나 다시 시도할 때 사용)
    fun clearExcludedIds() {
        _excludedRestaurantIds.clear()
    }

    fun resetRouletteState() {
        _rouletteState.value = RouletteUiState.Idle
    }

}

sealed class RouletteUiState { //룰렛 상태 정의
    object Idle : RouletteUiState() // 아무것도 안 한 상태
    object Loading : RouletteUiState() // 서버 응답 대기 중
    data class Success(val data: RouletteSpinResponse) : RouletteUiState() // 성공
    data class Error(val message: String) : RouletteUiState() // 실패
}