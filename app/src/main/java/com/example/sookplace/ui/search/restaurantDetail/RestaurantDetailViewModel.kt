package com.example.sookplace.ui.search.restaurantDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.local.dao.UserPreferenceDao
import com.example.sookplace.data.local.entity.UserPreferenceEntity
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
    private val localDao: UserPreferenceDao
) : ViewModel() {
    //상태 변수
    private val _detailState = MutableStateFlow<DetailUiState>(DetailUiState.Idle)
    val detailState: StateFlow<DetailUiState> = _detailState

    //종아요&&마이플레이스
    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked
    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    //좋아요
    fun toggleLike(restaurantId: Int) {
        viewModelScope.launch {
            val nextState = !_isLiked.value
            _isLiked.value = nextState

            val currentState = _detailState.value
            if (currentState is DetailUiState.Success) {
                val data = currentState.data

                val entity = UserPreferenceEntity(
                    restaurantId = restaurantId,
                    name = data.name,
                    category = data.category,
                    thumbnailUrl = data.thumbnailUrl ?: "",
                    rating = data.rating,
                    address = data.address,
                    isLiked = nextState,
                    isSaved = _isSaved.value // 현재 저장 상태 유지
                )
                localDao.insertOrUpdate(entity)
                // TODO: 백엔드 API가 있다면 여기서 호출 (예: restaurantRepository.postLike(restaurantId, nextState))
            }
        }
    }

    //마이 플레이스
    fun toggleSave(restaurantId: Int) {
        viewModelScope.launch {
            val nextState = !_isSaved.value
            _isSaved.value = nextState

            val currentState = _detailState.value
            if (currentState is DetailUiState.Success) {
                val data = currentState.data

                val entity = UserPreferenceEntity(
                    restaurantId = restaurantId,
                    name = data.name,
                    category = data.category,
                    thumbnailUrl = data.thumbnailUrl ?: "",
                    rating = data.rating,
                    address = data.address,
                    isLiked = _isLiked.value, // 현재 좋아요 상태 유지
                    isSaved = nextState
                )
                localDao.insertOrUpdate(entity)
            }
        }
    }

    fun fetchRestaurantDetail(restaurantId: Int) {
        viewModelScope.launch {
            _detailState.value = DetailUiState.Loading
            try {
                val response = restaurantRepository.getRestaurantDetail(restaurantId)
                val localPref = localDao.getPreferenceById(restaurantId)

                _isLiked.value = localPref?.isLiked ?: false
                _isSaved.value = localPref?.isSaved ?: false

                _detailState.value = DetailUiState.Success(response)
            } catch (e: Exception) {
                _detailState.value = DetailUiState.Error(e.message ?: "상세 정보를 불러오지 못했습니다.")
            }
        }
    }

    sealed class DetailUiState {
        object Idle : DetailUiState()
        object Loading : DetailUiState()
        data class Success(val data: RestaurantDetailResponse) : DetailUiState()
        data class Error(val message: String) : DetailUiState()
    }
}



