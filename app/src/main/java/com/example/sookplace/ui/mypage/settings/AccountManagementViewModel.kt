package com.example.sookplace.ui.mypage.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.remote.response.UserMeResponse
import com.example.sookplace.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountManagementViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userMe = MutableStateFlow<UserMeResponse?>(null)
    val userMe: StateFlow<UserMeResponse?> = _userMe.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                // 사용자 정보 로드 (프로필 이미지 등 표시용)
                val response = userRepository.getUserMe()
                _userMe.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}