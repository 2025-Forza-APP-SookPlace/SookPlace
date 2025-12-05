package com.example.sookplace.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.RestaurantItem
import com.example.sookplace.data.repository.RestaurantRepository
import com.example.sookplace.data.repository.UserProfileRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel @Inject constructor(
    private val userProfilerepository: UserProfileRepository,
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {
    //프로필
    val userProfile = userProfilerepository.userProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            userProfilerepository.refreshIfNeeded()
        }
    }

    //오늘의
    private val _featuredRestaurants = MutableStateFlow<List<RestaurantItem>>(emptyList())
    val featuredRestaurants: StateFlow<List<RestaurantItem>> get() = _featuredRestaurants

    fun loadFeaturedRestaurants() {
        viewModelScope.launch {
            try {
                val list = restaurantRepository.getFeaturedRestaurants()
                _featuredRestaurants.value = list
            } catch (e: Exception) {
                // 오류 처리: Toast, Log, State 업데이트 등
            }
        }
    }
}