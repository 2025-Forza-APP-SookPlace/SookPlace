package com.example.sookplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    init {// 앱이 켜지거나 메인으로 돌아왔을 때 최신 정보 갱신
        refreshProfile()
    }
    fun refreshProfile() {
        viewModelScope.launch {
            userProfileRepository.refreshUserProfile()
        }
    }

    val isLoggedIn = userProfileRepository.userProfileFlow
        .map { it != null }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )
}