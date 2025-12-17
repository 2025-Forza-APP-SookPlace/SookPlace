package com.example.sookplace.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.RestaurantItem
import com.example.sookplace.data.repository.RestaurantRepository
import com.example.sookplace.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val restaurantRepository: RestaurantRepository
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
}