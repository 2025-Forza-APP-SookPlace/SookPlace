package com.example.sookplace.ui.mypage.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sookplace.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> = _logoutSuccess

    fun logout() {
        viewModelScope.launch {
            // repository에 작성한 logout() 함수 호출
            val result = repository.logout()

            // 성공 여부를 LiveData에 반영
            _logoutSuccess.value = result.isSuccess
        }
    }
}