package com.example.sookplace.ui.search.restaurantDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.local.entity.PlaceEntity
import com.example.sookplace.data.remote.response.RestaurantDetailResponse
import com.example.sookplace.data.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository
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

    //종아요 && 마이플레이스
    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked
    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    //좋아요
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

    //마이 플레이스
    fun toggleSave(restaurantId: Int) {
        val originalIsSaved = _isSaved.value

        viewModelScope.launch {
            _isSaved.value = !originalIsSaved

            try {
                // val response = restaurantRepository.toggleSave(restaurantId)
                // _isSaved.value = response.isSaved
            } catch (e: Exception) {
                _isSaved.value = originalIsSaved
            }
        }
    }

    fun fetchRestaurantDetail(restaurantId: Int) {
        viewModelScope.launch {
            _detailState.value = DetailUiState.Loading
            try {
                val response = restaurantRepository.getRestaurantDetail(restaurantId)

//                _isLiked.value = response.isLiked //TODO: 서버 응답에 좋아요&핀 정보가 없다
//                _isSaved.value = response.isSaved

                _detailState.value = DetailUiState.Success(response)
            } catch (e: Exception) {
                _detailState.value = DetailUiState.Error(e.message ?: "상세 정보를 불러오지 못했습니다.")
            }
        }
    }


}



