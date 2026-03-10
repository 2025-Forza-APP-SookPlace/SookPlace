package com.example.sookplace.ui.search.restaurantDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.local.TokenManager
import com.example.sookplace.data.local.entity.PlaceEntity
import com.example.sookplace.data.remote.response.FavoriteToggleResponse
import com.example.sookplace.data.remote.response.RestaurantDetailResponse
import com.example.sookplace.data.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    //상태 변수
    private val _detailState = MutableStateFlow<DetailUiState>(DetailUiState.Idle)
    val detailState: StateFlow<DetailUiState> = _detailState

    sealed class DetailUiState {
        object Idle : DetailUiState()
        object Loading : DetailUiState()
        data class Success(val data: RestaurantDetailResponse) : DetailUiState()
        data class Error(val message: String) : DetailUiState()
    }

    //종아요 && 마이플레이스(핀)
    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked
    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    //좋아요 클릭
    fun toggleLike(restaurantId: Int) {
        val originalIsLiked = _isLiked.value

        viewModelScope.launch {
            _isLiked.value = !originalIsLiked

            try {
                val response = restaurantRepository.toggleLike(restaurantId)
                _isLiked.value = response.liked

            } catch (e: Exception) {
                //에러 발생 시 원래 하트 상태로 복구
                _isLiked.value = originalIsLiked
            }
        }
    }

    //마이 플레이스(핀) 클릭
    fun toggleSave(restaurantId: Int) {
        val originalIsSaved = _isSaved.value

        viewModelScope.launch {
            _isSaved.value = !originalIsSaved

            try {
                val response = restaurantRepository.togglePin(restaurantId)
                Log.d("RestaurantDetail", "Save API 성공! 서버 응답값: ${response.favorited}")
                _isSaved.value = response.favorited
            } catch (e: Exception) {
                Log.e("RestaurantDetail", "Save API 에러 발생", e)
                _isSaved.value = originalIsSaved
            }
        }
    }

    //식당 상세 정보
    fun fetchRestaurantDetail(restaurantId: Int) {
        viewModelScope.launch {
            _detailState.value = DetailUiState.Loading
            try {
                val response = restaurantRepository.getRestaurantDetail(restaurantId)

                _isLiked.value = response.isLiked
                _isSaved.value = response.isFavorited ?: false

                _detailState.value = DetailUiState.Success(response)
            } catch (e: Exception) {
                _detailState.value = DetailUiState.Error(e.message ?: "상세 정보를 불러오지 못했습니다.")
            }
        }
    }

    //로그인 여부 체크
    fun checkUserLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }


}



