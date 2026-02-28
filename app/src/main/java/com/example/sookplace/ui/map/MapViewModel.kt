package com.example.sookplace.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.FavoriteResponse
import com.example.sookplace.data.repository.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: MapRepository
) : ViewModel() {

    private val _favorites = MutableLiveData<FavoriteResponse>()
    val favorites: LiveData<FavoriteResponse> get() = _favorites

    fun fetchFavorites(limit: Int) {
        viewModelScope.launch {
            repository.getFavorites(limit).onSuccess { response ->
                _favorites.value = response
            }.onFailure { e ->
                e.printStackTrace()
            }
        }
    }

    // 나중에 서버에서 받아올 지도 중심 좌표
    val defaultLatitude = 37.5450
    val defaultLongitude = 126.9647

    // 줌 레벨
    val defaultZoom = 15.0
}